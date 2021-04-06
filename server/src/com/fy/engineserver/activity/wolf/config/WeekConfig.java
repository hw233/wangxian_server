package com.fy.engineserver.activity.wolf.config;

import java.util.Calendar;


/**
 * 每周周二2点10分到3点2分
 * 
 * @create 2016-3-28 下午03:58:10
 */
public class WeekConfig implements ActivityConfig{

	private int dayOfWeek;
	private int startHourOfDay;
	private int startMinute;
	private int endHourOfDay;
	private int endMinute;
	
	public WeekConfig(int dayOfWeek,int startHourOfDay,int startMinute,int endHourOfDay,int endMinute){
		this.dayOfWeek = dayOfWeek;
		this.startHourOfDay = startHourOfDay;
		this.startMinute = startMinute;
		this.endHourOfDay = endHourOfDay;
		this.endMinute = endMinute;
	}
	
	@Override
	public boolean isStart() {
		Calendar cl = Calendar.getInstance();
		int day = cl.get(Calendar.DAY_OF_WEEK);
		if(day == dayOfWeek){
			cl.set(Calendar.HOUR_OF_DAY, startHourOfDay);
			cl.set(Calendar.MINUTE, startMinute);
			long startTime = cl.getTimeInMillis();
			cl.set(Calendar.HOUR_OF_DAY, endHourOfDay);
			cl.set(Calendar.MINUTE, endMinute);
			long endTime = cl.getTimeInMillis();
			if(System.currentTimeMillis() >= startTime && System.currentTimeMillis() <= endTime){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean notice10Minute() {
		Calendar cl = Calendar.getInstance();
		int day = cl.get(Calendar.DAY_OF_WEEK);
		if(day == dayOfWeek){
			cl.set(Calendar.HOUR_OF_DAY, startHourOfDay);
			cl.set(Calendar.MINUTE, startMinute);
			if(((System.currentTimeMillis() + 10*60*1000)/1000) == (cl.getTimeInMillis()/1000)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean notice1Minute() {
		Calendar cl = Calendar.getInstance();
		int day = cl.get(Calendar.DAY_OF_WEEK);
		if(day == dayOfWeek){
			cl.set(Calendar.HOUR_OF_DAY, startHourOfDay);
			cl.set(Calendar.MINUTE, startMinute);
			if(((System.currentTimeMillis() + 1*60*1000)/1000) == (cl.getTimeInMillis()/1000)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean notice5Minute() {
		Calendar cl = Calendar.getInstance();
		int day = cl.get(Calendar.DAY_OF_WEEK);
		if(day == dayOfWeek){
			cl.set(Calendar.HOUR_OF_DAY, startHourOfDay);
			cl.set(Calendar.MINUTE, startMinute);
			if(((System.currentTimeMillis() + 5*60*1000)/1000) == (cl.getTimeInMillis()/1000)){
				return true;
			}
		}
		return false;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public int getStartHourOfDay() {
		return startHourOfDay;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public int getEndHourOfDay() {
		return endHourOfDay;
	}

	public int getEndMinute() {
		return endMinute;
	}

	@Override
	public String toString() {
		return "WeekConfig [dayOfWeek=" + dayOfWeek + ", endHourOfDay="
				+ endHourOfDay + ", endMinute=" + endMinute
				+ ", startHourOfDay=" + startHourOfDay + ", startMinute="
				+ startMinute + "]";
	}

	@Override
	public String getOpenTimeMess() {
		return "";
	}

	@Override
	public String getCurrOpenFlag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean platFit() {
		// TODO Auto-generated method stub
		return false;
	}

}
