variable "vpc-name" {
  type        = string
  description = "name of the insfrastructure vpc"
  default     = "main-vpc"
}

variable "ssh-keypair-name" {
  type        = string
  description = "default ssh keypair"
  default     = "dev-server"
}


variable "instance-type" {
  type        = string
  description = "instance type"
  default     = "t3.micro"
}

variable "default-region" {
  type        = string
  description = "default aws region"
  default     = "us-east-1"
}

variable "project-name" {
  default = "url-shortner"
}

variable "db-name" {
  default = "main-db"
}

variable "db-engine-name" {
  default = "aurora-postgresql"
}

variable "db-engine-version" {
  default = "17.4"
}

variable "environment" {
  default = "dev"
}

variable "cluster_instance_class" {
  default = "db.serverless"
}

locals {
  tags = {
    Terraform   = "true"
    Environment = var.environment
    Project     = var.project-name
  }
}
