package com.fy.engineserver.activity.fairyRobbery.instance;

import java.util.Arrays;

public class TempBoss {
	private long refreshTime;
	
	private int bossId;
	
	private int[] points;
	
	private long lastTime;
	
	private int type;

	public TempBoss(long refreshTime, int bossId, int[] points, long lastTime, int type) {
		super();
		this.refreshTime = refreshTime;
		this.bossId = bossId;
		this.points = points;
		this.lastTime = lastTime;
		this.type = type;
	}

	@Override
	public String toString() {
		return "TempBoss [refreshTime=" + refreshTime + ", bossId=" + bossId + ", points=" + Arrays.toString(points) + ", lastTime=" + lastTime + ", type=" + type + "]";
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

	public int getBossId() {
		return bossId;
	}

	public void setBossId(int bossId) {
		this.bossId = bossId;
	}

	public int[] getPoints() {
		return points;
	}

	public void setPoints(int[] points) {
		this.points = points;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
