package com.ai.aws.vpc.directconnect.controller;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.ai.aws.vpc.directconnect.repository.VPCRepository;

public class VPCController {
	
	private VPCRepository repo = new VPCRepository();

	public void showGUI() {
		
		JFrame frame = new JFrame("AWS VPC and Direct Connect Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLayout(new BorderLayout());

		JTabbedPane tabs = new JTabbedPane();

		// VPC Tab
		JTable vpcTable = new JTable();
		JScrollPane vpcScroll = new JScrollPane(vpcTable);
		tabs.add("VPCs", vpcScroll);

		// Direct Connect Tab
		JTable dcTable = new JTable();
		JScrollPane dcScroll = new JScrollPane(dcTable);
		tabs.add("Direct Connect", dcScroll);

		frame.add(tabs, BorderLayout.CENTER);

		// Load data
		List<Map<String, String>> vpcs = repo.getAllVPCs();
		DefaultTableModel vpcModel = new DefaultTableModel(new String[] { "VPC ID", "CIDR Block", "State" }, 0);
		for (Map<String, String> vpc : vpcs) {
			vpcModel.addRow(new String[] { vpc.get("vpc_id"), vpc.get("cidr_block"), vpc.get("state") });
		}
		vpcTable.setModel(vpcModel);

		List<Map<String, String>> dcs = repo.getAllDirectConnections();
		DefaultTableModel dcModel = new DefaultTableModel(new String[] { "Connection ID", "Location", "Bandwidth" }, 0);
		for (Map<String, String> dc : dcs) {
			dcModel.addRow(new String[] { dc.get("connection_id"), dc.get("location"), dc.get("bandwidth") });
		}
		dcTable.setModel(dcModel);

		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new VPCController().showGUI());
	}
}
