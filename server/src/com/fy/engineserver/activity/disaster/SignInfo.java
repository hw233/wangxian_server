package com.fy.engineserver.activity.disaster;

public class SignInfo {
	private long playerId;
	/** 报名时间 */
	private long signTime;
	
	public SignInfo(long playerId, long signTime) {
		super();
		this.playerId = playerId;
		this.signTime = signTime;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getSignTime() {
		return signTime;
	}

	public void setSignTime(long signTime) {
		this.signTime = signTime;
	}
}
