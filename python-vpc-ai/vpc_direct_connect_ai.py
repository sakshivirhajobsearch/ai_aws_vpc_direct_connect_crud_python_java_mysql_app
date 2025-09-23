# vpc_direct_connect_ai.py

import boto3
import pymysql
from botocore.exceptions import NoCredentialsError, PartialCredentialsError, ClientError
from ai_analyzer import analyze_vpcs, analyze_direct_connections

# === Configuration ===
AWS_REGION = 'ap-south-1'

MYSQL_HOST = 'localhost'
MYSQL_USER = 'root'
MYSQL_PASSWORD = 'admin'
MYSQL_DB = 'ai_aws_vpc'

# === Fetch VPC Data ===
def fetch_vpcs():
    try:
        ec2 = boto3.client('ec2', region_name=AWS_REGION)
        response = ec2.describe_vpcs()
        vpcs = response.get('Vpcs', [])
        print(f"✅ Fetched {len(vpcs)} VPC(s).")
        for vpc in vpcs:
            print(f"  VPC ID: {vpc.get('VpcId')}, CIDR: {vpc.get('CidrBlock')}, State: {vpc.get('State')}")
        return vpcs
    except (NoCredentialsError, PartialCredentialsError, ClientError) as e:
        print(f"❌ AWS VPC fetch error: {e}")
        return []

# === Fetch Direct Connect Data ===
def fetch_direct_connections():
    try:
        dc = boto3.client('directconnect')
        response = dc.describe_connections()
        connections = response.get('connections', [])
        print(f"✅ Fetched {len(connections)} Direct Connect connection(s).")
        for conn in connections:
            print(f"  ID: {conn.get('connectionId')}, Location: {conn.get('location')}, Bandwidth: {conn.get('bandwidth')}")
        return connections
    except (NoCredentialsError, PartialCredentialsError, ClientError) as e:
        print(f"❌ AWS Direct Connect fetch error: {e}")
        return []

# === Insert Data into MySQL ===
def insert_into_mysql(vpcs, connections):
    try:
        conn = pymysql.connect(
            host=MYSQL_HOST,
            user=MYSQL_USER,
            password=MYSQL_PASSWORD,
            database=MYSQL_DB
        )
        cursor = conn.cursor()

        # Insert VPCs
        if vpcs:
            for vpc in vpcs:
                vpc_id = vpc.get('VpcId', '')
                cidr_block = vpc.get('CidrBlock', '')
                state = vpc.get('State', '')
                cursor.execute("""
                    REPLACE INTO vpcs (vpc_id, cidr_block, state)
                    VALUES (%s, %s, %s)
                """, (vpc_id, cidr_block, state))
            print(f"✅ Inserted {len(vpcs)} VPC(s) into MySQL.")

        # Insert Direct Connect connections
        if connections:
            for dc in connections:
                connection_id = dc.get('connectionId', '')
                location = dc.get('location', '')
                bandwidth = dc.get('bandwidth', '')
                cursor.execute("""
                    REPLACE INTO direct_connect (connection_id, location, bandwidth)
                    VALUES (%s, %s, %s)
                """, (connection_id, location, bandwidth))
            print(f"✅ Inserted {len(connections)} Direct Connect connection(s) into MySQL.")

        conn.commit()
    except pymysql.MySQLError as e:
        print(f"❌ MySQL error: {e}")
    finally:
        if conn:
            conn.close()

# === Main Entry ===
if __name__ == '__main__':
    print("🚀 Starting AWS VPC & Direct Connect data fetch...")

    vpc_data = fetch_vpcs()
    direct_connect_data = fetch_direct_connections()

    if not vpc_data and not direct_connect_data:
        print("⚠️ No data fetched from AWS. Nothing to insert.")
    else:
        insert_into_mysql(vpc_data, direct_connect_data)
        analyze_vpcs(vpc_data)
        analyze_direct_connections(direct_connect_data)
        print("\n✅ All available data processed and inserted into MySQL.")
