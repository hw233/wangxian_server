package com.fy.gamegateway.mieshi.waigua;

import com.xuanzhi.tools.cache.Cacheable;

public class Session implements Cacheable {

	private String sessionID;
	
	private String username;
	
	private String clientID;
	
	private long createTime;
	
	@Override
	public int getSize() {
		return 1;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getCreateTime() {
		return createTime;
	}
}
