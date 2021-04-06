package com.fy.engineserver.activity.loginActivity;

import java.io.Serializable;

public class TaskRewardStat implements Serializable{

	private long pid;
	private int value;
	private long lastupdatetime;
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public long getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(long lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
	
}
