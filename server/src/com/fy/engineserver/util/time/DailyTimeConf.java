package com.fy.engineserver.util.time;

import java.util.Calendar;

import com.fy.engineserver.util.CompoundReturn;

/**
 * 每日
 * 
 * 
 */
public class DailyTimeConf extends TimeConf {

	/** 开始时间(小时) */
	private int[] startHours;
	/** 开始时间(分钟) */
	private int[] startMintues;

	public DailyTimeConf(long timeDelay, int[] startHours, int[] startMintues) {
		this.timeDelay = timeDelay;
		this.startHours = startHours;
		this.startMintues = startMintues;
	}

	@Override
	public TimeConfigType getConfigType() {
		return TimeConfigType.day;
	}

	// true在活动配置的时间内
	// 0:活动进行中
	// 1:需要刷新
	@Override
	public CompoundReturn inConfTimedistance(Calendar calendar, long lastTime) {
		if (lastTime != 0 && calendar.getTimeInMillis() - lastTime < getTimeDelay()) {// 当前时间与最后一次开启时间小于时间间隔,表示活动运行中
			return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(0).setStringValue("current");
		}
		for (int i = 0; i < startHours.length; i++) {
			int hour = startHours[i];
			int mintue = startMintues[i];
			Calendar tempTime = Calendar.getInstance();
			tempTime.set(Calendar.SECOND, 0);
			tempTime.set(Calendar.HOUR_OF_DAY, hour);
			tempTime.set(Calendar.MINUTE, mintue);
			long start = tempTime.getTimeInMillis();
			if (calendar.after(tempTime)) {// 当前在开启时间之后
				tempTime.add(Calendar.MILLISECOND, (int) getTimeDelay());// 当前在结束时间之前
				long end = tempTime.getTimeInMillis();
				if (calendar.before(tempTime)) {
					if (start <= lastTime && lastTime <= end) {// 最后一次刷新时间在本次的时间范围之内,不刷新
						return CompoundReturn.createCompoundReturn().setBooleanValue(true).setStringValue("current");
					}
					return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(1).setStringValue("refresh");
				}
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue("remove");
	}

	@Override
	public long getTimeDelay() {
		return timeDelay;
	}
}