package com.fy.gamegateway.mieshi.server;

import java.io.Serializable;

public class InnerTesterInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 156970675563746009L;

	private String clientId;
	
	/**
	 * 状态0内测1正式
	 */
	private byte state;
	
	private String testerName;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getTesterName() {
		return testerName;
	}

	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}
	
}
