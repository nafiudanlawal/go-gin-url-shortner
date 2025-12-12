module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "6.5.1"

  name = "${var.project_name}-vpc"
  cidr = "10.0.0.0/16"

  azs              = ["us-east-1a", "us-east-1b", "us-east-1c"]
  private_subnets  = ["10.0.1.0/24", "10.0.3.0/24", "10.0.5.0/24"]
  public_subnets   = ["10.0.2.0/24", "10.0.4.0/24", "10.0.6.0/24"]
  database_subnets = ["10.0.250.0/24", "10.0.251.0/24", "10.0.252.0/24"]

  enable_dns_hostnames = true
  create_igw           = true # enable internet access for public subnets


  create_multiple_public_route_tables = false
  create_multiple_intra_route_tables  = false
  igw_tags = {
    Name = "${var.project_name}-igw"
  }

  default_route_table_tags = {
    Name = "${var.project_name}-default-subnet"
  }
  tags = local.tags
}


module "api_gateway" {
  source  = "terraform-aws-modules/apigateway-v2/aws"
  version = "~> 6.0.0"

  # API
  body = templatefile("./api.yaml", {
    lambda_function_arn = module.lambda_function_container_image.lambda_function_arn
  })

  cors_configuration = {
    allow_headers = ["content-type", "x-amz-date", "authorization", "x-api-key", "x-amz-security-token", "x-amz-user-agent"]
    allow_methods = ["*"]
    allow_origins = ["*"]
  }

  description      = "${var.project_name} HTTP API Gateway"
  fail_on_warnings = false
  name             = "${var.project_name}-api-gw"

  # Domain Name
  domain_name           = var.api_domain_name
  create_domain_records = true
  create_certificate    = true

  # Routes & Integration(s)
  /*routes = {
    "POST /shorten" = {
      integration = {
        uri                    = module.lambda_function_container_image.lambda_function_arn
        timeout_milliseconds   = var.api_gateway_route_timeout
      }
    }

	"GET /shorten" = {
      integration = {
        uri                    = module.lambda_function_container_image.lambda_function_arn
        timeout_milliseconds   = var.api_gateway_route_timeout
      }
    }
	"GET /shorten/{code}" = {
      integration = {
        uri                    = module.lambda_function_container_image.lambda_function_arn
        timeout_milliseconds   = var.api_gateway_route_timeout
      }
    }

	"GET /shorten/{code}/stats" = {
      integration = {
        uri                    = module.lambda_function_container_image.lambda_function_arn
        timeout_milliseconds   = var.api_gateway_route_timeout
      }
    }

	"PUT /shorten/{code}" = {
      integration = {
        uri                    = module.lambda_function_container_image.lambda_function_arn
        timeout_milliseconds   = var.api_gateway_route_timeout
      }
    }

	"DELETE /shorten/{code}" = {
      integration = {
        uri                    = module.lambda_function_container_image.lambda_function_arn
        timeout_milliseconds   = var.api_gateway_route_timeout
      }
    }

    "$default" = {
      integration = {
        uri = module.lambda_function_container_image.lambda_function_arn
        tls_config = {
          server_name_to_verify = "short-url.${var.api_domain_name}"
        }

        response_parameters = [
          {
            status_code = 500
            mappings = {
              "append:header.header1" = "$context.requestId"
              "overwrite:statuscode"  = "403"
            }
          },
          {
            status_code = 404
            mappings = {
              "append:header.error" = "$stageVariables.environmentId"
            }
          }
        ]
      }
    }
  }*/

  # Stage
  stage_access_log_settings = {
    create_log_group            = true
    log_group_retention_in_days = 7
    format = jsonencode({
      context = {
        domainName              = "$context.domainName"
        integrationErrorMessage = "$context.integrationErrorMessage"
        protocol                = "$context.protocol"
        requestId               = "$context.requestId"
        requestTime             = "$context.requestTime"
        responseLength          = "$context.responseLength"
        routeKey                = "$context.routeKey"
        stage                   = "$context.stage"
        status                  = "$context.status"
        error = {
          message      = "$context.error.message"
          responseType = "$context.error.responseType"
        }
        identity = {
          sourceIP = "$context.identity.sourceIp"
        }
        integration = {
          error             = "$context.integration.error"
          integrationStatus = "$context.integration.integrationStatus"
        }
      }
    })
  }

  stage_default_route_settings = {
    detailed_metrics_enabled = true
    throttling_burst_limit   = 100
    throttling_rate_limit    = 100
  }

  tags = local.tags
}
