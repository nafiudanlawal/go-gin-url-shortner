

provider "aws" {
  region = var.default-region
}

data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd-gp3/ubuntu-noble-24.04-amd64-server-*"]
  }

  owners = ["099720109477"] # Canonical
}

resource "aws_instance" "app_server" {
  depends_on    = [module.vpc]
  ami           = data.aws_ami.ubuntu.id
  instance_type = var.instance-type

  # Networking
  vpc_security_group_ids = [module.vpc.default_security_group_id]
  subnet_id              = module.vpc.private_subnets[0]

  # Security
  key_name = var.ssh-keypair-name

  # Metadata
  tags = {
    Name    = "learn-terraform"
    Project = var.project-name
  }
}
