data "aws_rds_engine_version" "postgresql" {
  engine  = var.db-engine-name
  version = var.db-engine-version
}

module "cluster" {
  source = "terraform-aws-modules/rds-aurora/aws"
  # cluster config
  name                   = var.db-name
  database_name          = var.db-name
  engine                 = data.aws_rds_engine_version.postgresql.engine
  engine_version         = data.aws_rds_engine_version.postgresql.version
  cluster_instance_class = var.cluster_instance_class
  instances = {
    one = {}
  }

  vpc_id               = module.vpc.default_vpc_id
  db_subnet_group_name = module.vpc.database_subnet_group_name
  availability_zones   = module.vpc.azs

  # serverless config
  serverlessv2_scaling_configuration = {
    min_capacity             = 0
    max_capacity             = 10
    seconds_until_auto_pause = 300 # 5 minutes
  }
  storage_encrypted = true
  apply_immediately = true

  # security
  manage_master_user_password = true
  create_security_group       = true
  security_group_ingress_rules = {
    db-az1 = {
      cidr_ipv4 = element(module.vpc.database_subnets_cidr_blocks, 0)
    }
    db-az2 = {
      cidr_ipv4 = element(module.vpc.database_subnets_cidr_blocks, 1)
    }
  }

  # monitoring
  enabled_cloudwatch_logs_exports = ["postgresql"]
  cluster_monitoring_interval     = 0 # Turn off advanced monitoring

  tags = local.tags
}
