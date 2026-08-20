aws_region = "ap-south-1"

availability_zones = [
  "ap-south-1a",
  "ap-south-1b"
]

public_subnet_cidrs = [
  "10.20.0.0/20",
  "10.20.16.0/20"
]

private_subnet_cidrs = [
  "10.20.128.0/20",
  "10.20.144.0/20"
]

# Set this to an EKS version currently supported in your AWS region.
kubernetes_version = "1.32"

postgres_version = "16"

database_username = "renewalguard"

database_password = "CHANGE_THIS_BEFORE_APPLY"

contract_bucket_name = "renewalguard-dev-contracts-YOUR-UNIQUE-ID"
