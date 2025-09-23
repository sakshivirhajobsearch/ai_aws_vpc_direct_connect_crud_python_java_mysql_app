package com.ai.aws.vpc.directconnect.model;

public class DirectConnect {
	
	private String connectionId;
	private String location;
	private String bandwidth;

	public DirectConnect(String connectionId, String location, String bandwidth) {
		this.connectionId = connectionId;
		this.location = location;
		this.bandwidth = bandwidth;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
}
