package com.fy.boss.platform.qq.message;

import com.fy.boss.platform.qq.message.QQMessage;

public class LoginNotifyREQ extends QQMessage {
	
	private String uid;
	
	private String sessionId;
	
	private int loginTime;
	
	private int reserve;
	
	public LoginNotifyREQ(int ver, int seqNum, int cmd, String uid, String sessionId, int loginTime, int reserve) {
		super(ver, seqNum, cmd);
		this.uid = uid;
		this.sessionId = sessionId;
		this.loginTime = loginTime;
		this.reserve = reserve;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(int loginTime) {
		this.loginTime = loginTime;
	}

	public int getReserve() {
		return reserve;
	}

	public void setReserve(int reserve) {
		this.reserve = reserve;
	}
	
	
}
