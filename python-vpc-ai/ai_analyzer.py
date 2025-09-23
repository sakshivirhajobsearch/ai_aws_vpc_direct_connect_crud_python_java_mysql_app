# ai_analyzer.py

def analyze_vpcs(vpcs):
    print("\n🤖 AI ANALYSIS: VPC Health Report")
    if not vpcs:
        print("⚠️ No VPCs found to analyze.")
        return

    issues = []
    for vpc in vpcs:
        vpc_id = vpc.get('VpcId')
        cidr_block = vpc.get('CidrBlock', '')
        state = vpc.get('State', '')

        # Rule 1: Common CIDR blocks allowed
        if cidr_block.startswith(("10.", "192.168.", "172.16.", "172.31.")):
            pass
        else:
            issues.append(f"🔍 {vpc_id} has uncommon CIDR block: {cidr_block}")

        # Rule 2: VPC state should be 'available'
        if state != "available":
            issues.append(f"⚠️ {vpc_id} is not in 'available' state (current: {state})")

    if not issues:
        print("✅ All VPCs appear healthy and configured correctly.")
    else:
        for issue in issues:
            print(issue)


def analyze_direct_connections(connections):
    print("\n🤖 AI ANALYSIS: Direct Connect Health Report")
    if not connections:
        print("⚠️ No Direct Connect connections found.")
        return

    issues = []
    supported_bandwidths = ['1Gbps', '10Gbps', '100Gbps']

    for conn in connections:
        cid = conn.get('connectionId', 'Unknown')
        location = conn.get('location', '')
        bandwidth = conn.get('bandwidth', '')

        # Rule 1: Missing location
        if not location:
            issues.append(f"📍 {cid} has no assigned location.")

        # Rule 2: Unsupported bandwidth
        if bandwidth not in supported_bandwidths:
            issues.append(f"📉 {cid} has unsupported bandwidth: {bandwidth}")

        # Rule 3: Specifically flag low bandwidth
        elif bandwidth == '50Mbps':
            issues.append(f"⚠️ {cid} has low bandwidth: {bandwidth}")

    if not issues:
        print("✅ All Direct Connect connections appear healthy.")
    else:
        for issue in issues:
            print(issue)


# === Standalone Test Mode ===
if __name__ == '__main__':
    print("🧪 Running AI Analyzer in standalone test mode...")

    sample_vpcs = [
        {'VpcId': 'vpc-1234abcd', 'CidrBlock': '10.0.0.0/16', 'State': 'available'},
        {'VpcId': 'vpc-5678efgh', 'CidrBlock': '100.64.0.0/16', 'State': 'pending'},
    ]

    sample_direct_connect = [
        {'connectionId': 'dxcon-1', 'location': 'eq-ny2', 'bandwidth': '1Gbps'},
        {'connectionId': 'dxcon-2', 'location': '', 'bandwidth': '50Mbps'},
        {'connectionId': 'dxcon-3', 'location': 'eq-abc', 'bandwidth': '2Gbps'},
    ]

    analyze_vpcs(sample_vpcs)
    analyze_direct_connections(sample_direct_connect)
