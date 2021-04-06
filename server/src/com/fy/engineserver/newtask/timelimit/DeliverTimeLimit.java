package com.fy.engineserver.newtask.timelimit;

public class DeliverTimeLimit {

	private int type;
	private long time;

	public DeliverTimeLimit(int type, long time) {
		this.type = type;
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
