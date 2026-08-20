output "bucket_id" {
  value = aws_s3_bucket.contracts.id
}

output "bucket_arn" {
  value = aws_s3_bucket.contracts.arn
}

output "bucket_name" {
  value = aws_s3_bucket.contracts.bucket
}
