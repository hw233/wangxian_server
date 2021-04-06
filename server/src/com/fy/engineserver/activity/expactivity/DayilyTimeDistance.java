package com.fy.engineserver.activity.expactivity;

import java.util.Calendar;

/**
 * 每天的时间段,精确到分钟,不能跨天.小时:分钟
 * 
 */
public class DayilyTimeDistance {
	private int startHour;
	private int endHour;

	private int startMinute;
	private int endMinute;

	private int startDistance;// 换算成距每天0点0分的分钟数,这样效率高一点点
	private int endDistance;// 换算成距每天0点0分的分钟数,这样效率高一点点
	
	public String getInfoString() {
		String str = startHour + "时" + startMinute + "分--" + endHour + "时" + endMinute + "分";
		return str;
	}

	public DayilyTimeDistance(int startHour, int startMinute, int endHour, int endMinute) {
		super();
		this.startHour = startHour;
		this.endHour = endHour;
		this.startMinute = startMinute;
		this.endMinute = endMinute;
		init();
	}

	private void init() {
		startDistance = startHour * 60 + startMinute;
		endDistance = endHour * 60 + endMinute;
	}

	/**
	 * 所选时间是否在配置时间内
	 * @param calendar
	 * @return
	 */
	public boolean inthisTimeDistance(Calendar calendar) {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int thisTimeDistance = hour * 60 + minute;
		return startDistance <= thisTimeDistance && thisTimeDistance <= endDistance;
	}
	
	/**
	 * 通知活动即将开启 
	 * @param minute 提前几分钟
	 */
	public boolean noticeActivityStart(int bMinute){
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int thisTimeDistance = hour * 60 + minute;
		if(thisTimeDistance + bMinute == startDistance){
			return true;
		}
		return false;
	}

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

	public int getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

	public int getEndMinute() {
		return endMinute;
	}

	public void setEndMinute(int endMinute) {
		this.endMinute = endMinute;
	}

	public int getStartDistance() {
		return startDistance;
	}

	public void setStartDistance(int startDistance) {
		this.startDistance = startDistance;
	}

	public int getEndDistance() {
		return endDistance;
	}

	public void setEndDistance(int endDistance) {
		this.endDistance = endDistance;
	}
	
	

}
