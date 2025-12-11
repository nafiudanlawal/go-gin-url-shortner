module "lambda_function_container_image" {
	depends_on = [ module.ecr, null_resource.build_and_push ]
  source = "terraform-aws-modules/lambda/aws"
	version = "~> 8.1.2"

  function_name = "${var.project_name}-backend"
  description   = "ecr lambda handler"

  create_package = false

  image_uri    = "${module.ecr.repository_url}:latest"
  package_type = "Image"

  memory_size = 128

	architectures = ["x86_64"]
  environment_variables = {
    PORT        = 8080
    DB_HOST     = "localhost"
    DB_NAME     = "url_shortner"
    DB_USERNAME = "postgres"
    DB_PORT     = 5432
  }

  vpc_subnet_ids         = module.vpc.private_subnets
  vpc_security_group_ids = [module.vpc.default_security_group_id]

  attach_network_policy = true

	cloudwatch_logs_retention_in_days = 7

  allowed_triggers = {
    AllowExecutionFromAPIGateway = {
      service    = "${var.project_name}-apigateway"
      source_arn = "${module.api_gateway.api_execution_arn}/*/*"
    }
  }
}


resource "null_resource" "build_and_push" {
	depends_on = [ module.ecr ]
	triggers = {
    # only rerun if the ecr repo changes.
    ecr_repo = module.ecr.repository_arn
  }
  provisioner "local-exec" {
    command = <<EOF
      aws ecr get-login-password --region ${var.default_region} | docker login --username AWS --password-stdin ${module.ecr.repository_url}
      docker build -t blank.Dockerfile .
      docker tag blank:latest ${module.ecr.repository_url}:latest
      docker push ${module.ecr.repository_url}:latest
    EOF
  }
}

module "ecr" {
  source = "terraform-aws-modules/ecr/aws"

  repository_name = "${var.project_name}-repo"

  repository_lifecycle_policy = jsonencode({
    rules = [
      {
        rulePriority = 1,
        description  = "Keep last 20 images",
        selection = {
          tagStatus     = "tagged",
          tagPrefixList = ["v"],
          countType     = "imageCountMoreThan",
          countNumber   = 20
        },
        action = {
          type = "expire"
        }
      }
    ]
  })

  registry_scan_rules = [{
    scan_frequency = "SCAN_ON_PUSH",
		filter: []
  }]


  tags = local.tags
}
