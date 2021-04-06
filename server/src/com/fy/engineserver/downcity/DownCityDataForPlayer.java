package com.fy.engineserver.downcity;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class DownCityDataForPlayer {

	private String downCityName = "";
	
	/**
	 * 今天进入这个副本次数
	 */
	private int todayComeInCount;
	
	/**
	 * 最后一次进入副本时间(用于刷新进入副本次数)
	 */
	private long lastComeInTime;

	public String getDownCityName() {
		return downCityName;
	}

	public void setDownCityName(String downCityName) {
		this.downCityName = downCityName;
	}

	public int getTodayComeInCount() {
		return todayComeInCount;
	}

	public void setTodayComeInCount(int todayComeInCount) {
		this.todayComeInCount = todayComeInCount;
	}

	public long getLastComeInTime() {
		return lastComeInTime;
	}

	public void setLastComeInTime(long lastComeInTime) {
		this.lastComeInTime = lastComeInTime;
	}
	
	
}
