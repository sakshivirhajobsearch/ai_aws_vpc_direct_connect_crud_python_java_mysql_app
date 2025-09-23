package com.ai.aws.vpc.directconnect.model;

public class VPC {
	
	private String vpcId;
	private String cidrBlock;
	private String state;

	public VPC(String vpcId, String cidrBlock, String state) {
		this.vpcId = vpcId;
		this.cidrBlock = cidrBlock;
		this.state = state;
	}

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public String getCidrBlock() {
		return cidrBlock;
	}

	public void setCidrBlock(String cidrBlock) {
		this.cidrBlock = cidrBlock;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}