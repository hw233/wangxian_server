package com.fy.gamegateway.mieshi.waigua;

public class UserGameServerEntity {

	public UserGameServerEntity (String servername,String username,String sessionId,String clientId,String channel,String MACADDRESS,String ipAddress,String platform,String phoneType,String network,String gpuOtherName) {
		this.servername = servername;
		this.username = username;
		this.sessionId = sessionId;
		this.clientId = clientId;
		this.channel = channel;
		this.MACADDRESS = MACADDRESS;
		this.ipAddress = ipAddress;
		this.platform = platform;
		this.phoneType = phoneType;
		this.network = network;
		this.gpuOtherName = gpuOtherName;
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(servername).append("]");
		sb.append(" [").append(username).append("]");
		sb.append(" [sid:").append(sessionId).append("]");
		sb.append(" [cid:").append(clientId).append("]");
		sb.append(" [").append(channel).append("]");
		sb.append(" [mac:").append(MACADDRESS).append("]");
		//sb.append(" [ip:").append(ipAddress).append("]");
		sb.append(" [").append(platform).append("]");
		sb.append(" [").append(phoneType).append("]");
		sb.append(" [").append(network).append("]");
		sb.append(" [gpu:").append(gpuOtherName).append("]");
		logString = sb.toString();
	}
	
	public String servername;					//服务器名字
	public String username;
	public String sessionId;
	public String clientId;
	public String channel;
	public String MACADDRESS;
	public String ipAddress;
	public String platform;
	public String phoneType;
	public String network;
	public String gpuOtherName;
	public String logString;					//日志
	
}
