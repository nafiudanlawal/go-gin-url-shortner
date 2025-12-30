module "lambda_function_container_image" {
  depends_on = [module.ecr, null_resource.build_and_push]
  source     = "terraform-aws-modules/lambda/aws"
  version    = "~> 8.1.2"

  function_name = "${var.project_name}-backend"
  description   = "ecr lambda handler"

  create_package = false
  source_path = "../spring/target/spring-0.0.1-SNAPSHOT.jar"
  package_type = "Zip"

  memory_size = 128

  architectures = ["x86_64"]
  runtime = "java21"
  environment_variables = {
    PORT                 = 8080
    DB_HOST              = "database-dev-test.cluster-ckbauu0sa7cs.us-east-1.rds.amazonaws.com"
    DB_NAME              = "url_shortner"
    DB_USERNAME          = "postgres"
    DB_CREDENTIAL_SECRET = "rds!cluster-2fabacd5-267b-4089-8d07-d1575f659783"
    DB_PORT              = 5432

  }

  vpc_subnet_ids         = module.vpc.private_subnets
  vpc_security_group_ids = [module.vpc.default_security_group_id]

  attach_network_policy = true

  cloudwatch_logs_retention_in_days = 7

  publish = true
  allowed_triggers = {
    AllowExecutionFromAPIGateway = {
      service    = "apigateway"
      source_arn = "${module.api_gateway.api_execution_arn}/*/*"
    }
  }
}


module "s3" {
  
}

