CREATE DATABASE IF NOT EXISTS ai_aws_vpc;
USE ai_aws_vpc;

CREATE TABLE IF NOT EXISTS vpcs (
    vpc_id VARCHAR(50) PRIMARY KEY,
    cidr_block VARCHAR(50),
    state VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS direct_connect (
    connection_id VARCHAR(100) PRIMARY KEY,
    location VARCHAR(100),
    bandwidth VARCHAR(50)
);
