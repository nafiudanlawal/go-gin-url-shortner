variable "vpc_name" {
  type        = string
  description = "name of the insfrastructure vpc"
  default     = "main_vpc"
}

variable "ssh_keypair_name" {
  type        = string
  description = "default ssh keypair"
  default     = "dev_server"
}


variable "instance_type" {
  type        = string
  description = "instance type"
  default     = "t3.micro"
}

variable "default_region" {
  type        = string
  description = "default aws region"
  default     = "us-east-1"
}

variable "project_name" {
  default = "url-shortner"
}

variable "db_name" {
  default = "mainDB"
}

variable "db_engine_name" {
  default = "aurora-postgresql"
}

variable "db_engine_version" {
  default = "17.4"
}

variable "environment" {
  default = "dev"
}

variable "cluster_instance_class" {
  default = "db.serverless"
}

variable "api_domain_name" {
	default = "aiklearning.click"
  
}

variable "db_master_password" {
	sensitive = true
	default = "postgres_password152"
  
}

variable "db_master_username" {
  sensitive = true
  default = "postgres"
}

variable "api_gateway_route_timeout" {
  type = number
  default = 1000 # 1 second
}


locals {
  tags = {
    Terraform   = "true"
    Environment = var.environment
    Project     = var.project_name
  }
}
