package com.fy.engineserver.activity.disaster.module;

import java.util.Calendar;

/**
 * 开启时间规则
 */
public class DisasterOpenModule {
	/** 周几开启  1周天。。7周六 */
	private int dayOfWeek;
	/** 开启小时 */
	private int startHourOfDay;
	/** 开启分 */
	private int startMinOfHour;
	/** 持续时间(ms) */
	private long durationTime;
	
	public DisasterOpenModule(String str) {
		String[] s = str.split(",");
		dayOfWeek = Integer.parseInt(s[0]);
		startHourOfDay = Integer.parseInt(s[1]);
		startMinOfHour = Integer.parseInt(s[2]);
		durationTime = Integer.parseInt(s[3]);
	}
	/**
	 * 
	 * @return [0]活动开启毫秒  [1]活动结束毫秒
	 */
	public long[] getOpenTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		c.set(Calendar.HOUR_OF_DAY, startHourOfDay);
		c.set(Calendar.MINUTE, startMinOfHour);
		long[] result = new long[2];
		result[0] = c.getTimeInMillis();
		result[1] = result[0] + durationTime;
		return result;
	}
	
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getStartHourOfDay() {
		return startHourOfDay;
	}
	public void setStartHourOfDay(int startHourOfDay) {
		this.startHourOfDay = startHourOfDay;
	}
	public int getStartMinOfHour() {
		return startMinOfHour;
	}
	public void setStartMinOfHour(int startMinOfHour) {
		this.startMinOfHour = startMinOfHour;
	}
	public long getDurationTime() {
		return durationTime;
	}
	public void setDurationTime(long durationTime) {
		this.durationTime = durationTime;
	}
}
