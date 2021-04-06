package com.fy.gamegateway.mieshi.waigua;

public class LoginEntity {
	public LoginEntity(	String username,
		String passwd,
		String channel,
		String clientId,
		String mac,
		String platform,
		String phoneType,
		String gpuType,
		String clientVersion,
		String networkMode,
		String channelUserFlag,
		String resourceVersion,
		long sequnceNum,
		String userType){
		this.clientId = clientId;
		this.username = username;
		this.passwd = passwd;
		this.userType = userType;
		this.platform = platform;
		this.channel = channel;
		this.channelUserFlag = channelUserFlag;
		this.phoneType = phoneType;
		this.gpuType = gpuType;
		this.mac = mac;
		this.resourceVersion = resourceVersion;
		this.clientVersion = clientVersion;
		this.networkMode = networkMode;
		this.sequnceNum = sequnceNum;
		
		
	}

	public String clientId;
	public String username;
	public String passwd;
	public String userType;
	public String platform;
	public String channel;
	public String channelUserFlag;
	public String phoneType;
	public String gpuType;
	public String mac;
	public String resourceVersion;
	public String clientVersion;
	public String networkMode;
	public long sequnceNum;
	
	public String getLogString () {
		String log = "[uN="+username+"] [ps="+passwd+"] [ch="+channel+"] ["+clientId+"] ["+mac+"] ["
		+platform+"] [rV="+resourceVersion+"] [cV="+clientVersion+"] ["+networkMode+"] [sqNum="
		+sequnceNum+"] [uT="+userType+"] [channelUserFlag="+channelUserFlag+"] ["+phoneType+"] ["+gpuType+"]";
		return log;
	}
}
