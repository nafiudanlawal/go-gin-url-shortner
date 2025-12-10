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