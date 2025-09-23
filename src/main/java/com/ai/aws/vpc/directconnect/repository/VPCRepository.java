package com.ai.aws.vpc.directconnect.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ai.aws.vpc.directconnect.model.DirectConnect;
import com.ai.aws.vpc.directconnect.model.VPC;

public class VPCRepository {

	private static final String URL = "jdbc:mysql://localhost:3306/ai_aws_vpc?useSSL=false";
	private static final String USER = "root";
	private static final String PASSWORD = "admin";

	public List<VPC> getAllVPCs() {
		List<VPC> vpcs = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM vpcs")) {

			while (rs.next()) {
				String vpcId = rs.getString("vpc_id");
				String cidrBlock = rs.getString("cidr_block");
				String state = rs.getString("state");
				vpcs.add(new VPC(vpcId, cidrBlock, state));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vpcs;
	}

	public List<DirectConnect> getAllDirectConnections() {
		List<DirectConnect> connections = new ArrayList<>();
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM direct_connect")) {

			while (rs.next()) {
				String connectionId = rs.getString("connection_id");
				String location = rs.getString("location");
				String bandwidth = rs.getString("bandwidth");
				connections.add(new DirectConnect(connectionId, location, bandwidth));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connections;
	}
}
