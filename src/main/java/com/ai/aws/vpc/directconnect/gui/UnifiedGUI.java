package com.ai.aws.vpc.directconnect.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.ai.aws.vpc.directconnect.model.DirectConnect;
import com.ai.aws.vpc.directconnect.model.VPC;
import com.ai.aws.vpc.directconnect.repository.VPCRepository;

public class UnifiedGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTable vpcTable;
	private JTable dcTable;
	private JTextArea aiTextArea;
	private VPCRepository repository;

	public UnifiedGUI() {
		repository = new VPCRepository();

		setTitle("AWS VPC + Direct Connect Viewer with AI Insights");
		setSize(1000, 700);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Tables
		JPanel tablePanel = new JPanel(new GridLayout(2, 1));

		vpcTable = new JTable();
		dcTable = new JTable();

		JScrollPane vpcScroll = new JScrollPane(vpcTable);
		JScrollPane dcScroll = new JScrollPane(dcTable);

		vpcScroll.setBorder(BorderFactory.createTitledBorder("VPCs"));
		dcScroll.setBorder(BorderFactory.createTitledBorder("Direct Connect Connections"));

		tablePanel.add(vpcScroll);
		tablePanel.add(dcScroll);

		// AI Output Area
		aiTextArea = new JTextArea(8, 50);
		aiTextArea.setEditable(false);
		aiTextArea.setLineWrap(true);
		aiTextArea.setWrapStyleWord(true);
		JScrollPane aiScroll = new JScrollPane(aiTextArea);
		aiScroll.setBorder(BorderFactory.createTitledBorder("AI Analysis"));

		// Refresh Button
		JButton refreshBtn = new JButton("Refresh Data & AI Insights");
		refreshBtn.addActionListener(this::refreshData);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(refreshBtn, BorderLayout.NORTH);
		bottomPanel.add(aiScroll, BorderLayout.CENTER);

		add(tablePanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		// Initial data load
		refreshData(null);

		setVisible(true);
	}

	private void refreshData(ActionEvent e) {
		List<VPC> vpcs = repository.getAllVPCs();
		List<DirectConnect> dcs = repository.getAllDirectConnections();

		// Update VPC table
		DefaultTableModel vpcModel = new DefaultTableModel();
		vpcModel.setColumnIdentifiers(new String[] { "VPC ID", "CIDR Block", "State" });
		for (VPC vpc : vpcs) {
			vpcModel.addRow(new String[] { vpc.getVpcId(), vpc.getCidrBlock(), vpc.getState() });
		}
		vpcTable.setModel(vpcModel);

		// Update Direct Connect table
		DefaultTableModel dcModel = new DefaultTableModel();
		dcModel.setColumnIdentifiers(new String[] { "Connection ID", "Location", "Bandwidth" });
		for (DirectConnect dc : dcs) {
			dcModel.addRow(new String[] { dc.getConnectionId(), dc.getLocation(), dc.getBandwidth() });
		}
		dcTable.setModel(dcModel);

		// Run AI analysis
		StringBuilder aiReport = new StringBuilder("🤖 AI Analysis:\n\n");

		// VPC AI logic
		for (VPC vpc : vpcs) {
			if (!vpc.getCidrBlock().startsWith("10.") && !vpc.getCidrBlock().startsWith("192.168.")
					&& !vpc.getCidrBlock().startsWith("172.16.") && !vpc.getCidrBlock().startsWith("172.31.")) {
				aiReport.append("🔍 ").append(vpc.getVpcId()).append(" has uncommon CIDR block: ")
						.append(vpc.getCidrBlock()).append("\n");
			}
			if (!vpc.getState().equalsIgnoreCase("available")) {
				aiReport.append("⚠️ ").append(vpc.getVpcId()).append(" is not in 'available' state (current: ")
						.append(vpc.getState()).append(")\n");
			}
		}

		// Direct Connect AI logic
		for (DirectConnect dc : dcs) {
			if (dc.getLocation() == null || dc.getLocation().isBlank()) {
				aiReport.append("📍 ").append(dc.getConnectionId()).append(" has no assigned location.\n");
			}
			if (!dc.getBandwidth().matches("1Gbps|10Gbps|100Gbps")) {
				aiReport.append("📉 ").append(dc.getConnectionId()).append(" has unsupported bandwidth: ")
						.append(dc.getBandwidth()).append("\n");
			}
		}

		if (vpcs.isEmpty() && dcs.isEmpty()) {
			aiReport.append("⚠️ No data found. Please fetch data first.");
		} else if (aiReport.toString().equals("🤖 AI Analysis:\n\n")) {
			aiReport.append("✅ All entries appear healthy.");
		}

		aiTextArea.setText(aiReport.toString());
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(UnifiedGUI::new);
	}
}
