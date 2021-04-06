package com.fy.engineserver.activity.dice.config;

import java.util.Calendar;

import com.fy.engineserver.activity.dice.DiceManager;

/**
 * 每周周几几点几分
 * 
 * @create 2016-3-9 下午04:52:33
 */
public class DiceActivityWeekConfig implements DiceActivityConfig{
	
	private int dayOfWeek;
	
	private int hourOfDay;
	
	private int minute;
	
	private long eTime;
	
	public DiceActivityWeekConfig(){}
	
	public DiceActivityWeekConfig(int dayOfWeek,int hourOfDay,int minute){
		this.dayOfWeek = dayOfWeek;
		this.hourOfDay = hourOfDay;
		this.minute = minute;
	}
	
	@Override
	public boolean isStart() {
		Calendar cl = Calendar.getInstance();
		int day = cl.get(Calendar.DAY_OF_WEEK);
		if(day == dayOfWeek){
			cl.set(Calendar.HOUR_OF_DAY, hourOfDay);
			cl.set(Calendar.MINUTE, minute);
			cl.set(Calendar.SECOND, 0);
			long startTime = cl.getTimeInMillis();
			cl.set(Calendar.MINUTE, (minute + DiceManager.SIGN_LAST_TIME));
			cl.set(Calendar.SECOND, 0);
			long endTime = cl.getTimeInMillis();
			eTime = endTime;
			if(System.currentTimeMillis() >= startTime && System.currentTimeMillis() < endTime){
				return true;
			}
		}
		return false;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getHourOfDay() {
		return hourOfDay;
	}

	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	@Override
	public long getEndTime() {
		return eTime;
	}
	
	

}
