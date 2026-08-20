terraform {
  required_version = ">= 1.6.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project     = "RenewalGuard"
      Environment = "dev"
      ManagedBy   = "Terraform"
    }
  }
}

locals {
  name = "renewalguard-dev"
}

module "vpc" {
  source = "../../modules/vpc"

  name                 = local.name
  cluster_name         = local.name
  vpc_cidr             = var.vpc_cidr
  availability_zones   = var.availability_zones
  public_subnet_cidrs  = var.public_subnet_cidrs
  private_subnet_cidrs = var.private_subnet_cidrs

  tags = {
    Environment = "dev"
  }
}

module "eks" {
  source = "../../modules/eks"

  cluster_name         = local.name
  kubernetes_version   = var.kubernetes_version
  public_subnet_ids    = module.vpc.public_subnet_ids
  private_subnet_ids   = module.vpc.private_subnet_ids
  desired_node_count   = 2
  min_node_count       = 1
  max_node_count       = 4
  node_instance_types  = ["t3.medium"]

  tags = {
    Environment = "dev"
  }
}

module "rds" {
  source = "../../modules/rds"

  name                  = local.name
  db_identifier         = "${local.name}-postgres"
  postgres_version      = var.postgres_version
  instance_class        = "db.t3.micro"
  allocated_storage     = 20
  max_allocated_storage = 50
  database_name         = "renewalguard"
  database_username     = var.database_username
  database_password     = var.database_password

  private_subnet_ids = module.vpc.private_subnet_ids

  multi_az                = false
  backup_retention_period  = 7
  skip_final_snapshot     = true
  deletion_protection     = false

  tags = {
    Environment = "dev"
  }
}

module "s3" {
  source = "../../modules/s3"

  bucket_name = var.contract_bucket_name

  tags = {
    Environment = "dev"
  }
}
