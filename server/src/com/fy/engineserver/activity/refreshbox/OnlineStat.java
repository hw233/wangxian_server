package com.fy.engineserver.activity.refreshbox;

import java.io.Serializable;

public class OnlineStat implements Serializable{
	
	private long lasttime;
	
	private long lasttime2;
	
	private int value;
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public long getLasttime() {
		return lasttime;
	}

	public void setLasttime(long lasttime) {
		this.lasttime = lasttime;
	}

	public long getLasttime2() {
		return lasttime2;
	}

	public void setLasttime2(long lasttime2) {
		this.lasttime2 = lasttime2;
	}

}
