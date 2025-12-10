# Url Shortner Service

## Requirements
- Terraform
- AWS Cli

## Deployment

```bash
# prepares terraform environment and  downloads dependencies
terraform init 

# created plan for deployment without saving use -out plan to guarantee plan deployment
terraform plan

# executes deployment plan to cloud provider(AWS)
terraform apply

```

## Infractructure Architecture
![Architecture Diagram](architecture.png)