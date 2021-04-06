package com.fy.engineserver.activity.oldPlayerComeBack;

import java.io.Serializable;

public class ActivityStat implements Serializable{

	private long value;
	
	private long lasttime;
	
	/**
	 * 活动状态
	 * 0:不符合
	 * 1:符合
	 */
	private int activitystat;

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getLasttime() {
		return lasttime;
	}

	public void setLasttime(long lasttime) {
		this.lasttime = lasttime;
	}

	public int getActivitystat() {
		return activitystat;
	}

	public void setActivitystat(int activitystat) {
		this.activitystat = activitystat;
	}
	
	
}
