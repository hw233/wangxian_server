package com.fy.engineserver.newtask.timelimit;

public class TimeOfDayMinu {
	/** 开始时间-小时 */
	private int startHour;
	/** 结束时间-小时 */
	private int endHour;
	/** 开始时间-分钟 */
	private int startMinu;
	/** 结束时间-分钟 */
	private int endMinu;
	
	public int getStartHour() {
		return startHour;
	}
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}
	public int getEndHour() {
		return endHour;
	}
	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}
	public int getStartMinu() {
		return startMinu;
	}
	public void setStartMinu(int startMinu) {
		this.startMinu = startMinu;
	}
	public int getEndMinu() {
		return endMinu;
	}
	public void setEndMinu(int endMinu) {
		this.endMinu = endMinu;
	}
	
}
