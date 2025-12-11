output "vpc_id" {
  description = "VPC created id"
  value       = module.vpc.vpc_id
}

output "ecr-arn" {
  description = "arn of created ecr repo"
  value = module.ecr.repository_arn
}

output "ecr-repository_url" {
  description = "arn of created ecr repo"
  value = module.ecr.repository_url
}