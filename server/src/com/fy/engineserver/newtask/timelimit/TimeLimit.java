package com.fy.engineserver.newtask.timelimit;

import java.text.SimpleDateFormat;

/**
 * 时间限制
 * 
 * 
 */
public abstract class TimeLimit {
	/** 时间限制类型 :天，周，月，年 */
	private int limltType;

	protected SimpleDateFormat sdf16 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	protected SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");

	public boolean timeAllow() {
		return timeAllow(-1);
	}

	public abstract boolean timeAllow(long time);

	public int getLimltType() {
		return limltType;
	}

	public void setLimltType(int limltType) {
		this.limltType = limltType;
	}

}
