package com.fy.engineserver.activity.fairyRobbery.instance;

public class TempBoss2 {
	private long bossId;
	
	private long endTime;
	
	private int type;
	

	public TempBoss2(long bossId, long endTime, int type) {
		super();
		this.bossId = bossId;
		this.endTime = endTime;
		this.type = type;
	}

	@Override
	public String toString() {
		return "TempBoss2 [bossId=" + bossId + ", endTime=" + endTime + ", type=" + type + "]";
	}

	public long getBossId() {
		return bossId;
	}

	public void setBossId(long bossId) {
		this.bossId = bossId;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
