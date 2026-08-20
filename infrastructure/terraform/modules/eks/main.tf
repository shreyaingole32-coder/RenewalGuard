
resource "aws_db_subnet_group" "this" {
  name       = "${var.name}-db-subnet-group"
  subnet_ids = var.private_subnet_ids

  tags = merge(var.tags, {
    Name = "${var.name}-db-subnet-group"
  })
}

resource "aws_db_instance" "this" {
  identifier = var.db_identifier

  engine         = "postgres"
  engine_version = var.postgres_version

  instance_class        = var.instance_class
  allocated_storage     = var.allocated_storage
  max_allocated_storage = var.max_allocated_storage
  storage_type          = "gp3"
  storage_encrypted     = true

  db_name  = var.database_name
  username = var.database_username
  password = var.database_password
  port     = 5432

  db_subnet_group_name = aws_db_subnet_group.this.name

  publicly_accessible    = false
  multi_az               = var.multi_az
  skip_final_snapshot    = var.skip_final_snapshot
  deletion_protection    = var.deletion_protection
  backup_retention_period = var.backup_retention_period

  backup_window           = "03:00-04:00"
  maintenance_window      = "sun:04:00-sun:05:00"
  auto_minor_version_upgrade = true

  tags = var.tags
}
