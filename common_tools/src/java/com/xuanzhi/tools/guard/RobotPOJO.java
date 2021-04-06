package com.xuanzhi.tools.guard;

/**
 *
 * 
 */
public class RobotPOJO implements Robot {
	
	private String name;
	
	private int state;
	
	private String action;
	
	private long sequence;
	
	private long lastActionTime;
	
	private long createTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	public long getLastActionTime() {
		return lastActionTime;
	}

	public void setLastActionTime(long lastActionTime) {
		this.lastActionTime = lastActionTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
}
