package com.ai.aws.vpc.directconnect.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VPCRepository {

	private Connection connect() throws SQLException {

		return DriverManager.getConnection("jdbc:mysql://localhost:3306/ai_aws_vpc?useSSL=false", "root", "admin");
	}

	public List<Map<String, String>> getAllVPCs() {
		List<Map<String, String>> vpcs = new ArrayList<>();
		String query = "SELECT * FROM vpcs";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				Map<String, String> row = new HashMap<>();
				row.put("vpc_id", rs.getString("vpc_id"));
				row.put("cidr_block", rs.getString("cidr_block"));
				row.put("state", rs.getString("state"));
				vpcs.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vpcs;
	}

	public List<Map<String, String>> getAllDirectConnections() {
		List<Map<String, String>> list = new ArrayList<>();
		String query = "SELECT * FROM direct_connect";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				Map<String, String> row = new HashMap<>();
				row.put("connection_id", rs.getString("connection_id"));
				row.put("location", rs.getString("location"));
				row.put("bandwidth", rs.getString("bandwidth"));
				list.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
}