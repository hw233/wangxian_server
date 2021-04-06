package com.fy.engineserver.sprite;

public class TeamChangeNotice {

	
	
	private long playerId;
	//类型  hp  mp
	private int changeType;
	
	// 现在的值
	private long nowValue;
	
	public TeamChangeNotice(){
		super();
	}
	
	public TeamChangeNotice(long playerId,int type,long value){
		super();
		this.playerId = playerId;
		this.changeType = type;
		this.nowValue = value;
	}
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}


	public int getChangeType() {
		return changeType;
	}

	public void setChangeType(int changeType) {
		this.changeType = changeType;
	}

	public long getNowValue() {
		return nowValue;
	}

	public void setNowValue(long nowValue) {
		this.nowValue = nowValue;
	}
	
}
