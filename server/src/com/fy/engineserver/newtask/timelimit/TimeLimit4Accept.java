package com.fy.engineserver.newtask.timelimit;

import java.util.Calendar;
import java.util.List;

/**
 * 接取任务限制
 * @author Administrator
 *
 */
public abstract class TimeLimit4Accept {
	/** 限制类型:1 每周特定天不可接取且不可交付  2每天固定时间不可接取交付 (其他类型之后再加) */
	private byte limitType;
	/** 不可接取时间表   type为1时代表周几 */
	private List<Integer> timeList;
	/** 对于每日限时不可接取的任务  type为2时使用   [0]hour */
	private List<TimeOfDayMinu> timeList4Minu;
	
	@Override
	public String toString() {
		return "TimeLimit4Accept [limitType=" + limitType + ", timeList=" + timeList + ", timeList4Minu=" + timeList4Minu + "]";
	}

	/**
	 * 固定时间通知-做相应操作（比如跨天删除有时间限制的任务）
	 */
	public abstract void doAct(List<Long> playerIds);
	
	/**
	 * 检测是否可以接取或交付任务
	 * @return	true为可接取交付
	 */
	public boolean check4date() {
		boolean  result = false;
		Calendar calendar = Calendar.getInstance();
		if(limitType == TimeLimitManager.type_of_weed) {			// 固定周几不开放
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);		
			if(timeList != null && timeList.size() > 0) {
				for(int i=0; i<timeList.size(); i++) {
					if(timeList.get(i) == dayOfWeek) {
						result = true;
						break;
					}
				}
			}
		} else if (limitType == TimeLimitManager.type_of_day) {		//固定每天时间段不开放
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minu = calendar.get(Calendar.MINUTE);
			result = checkHourAndMinu(hour, minu);
		}
		return result;
	}
	/**
	 * 检查时间分钟是否开放
	 * @param hour
	 * @param minu
	 * @return
	 */
	private boolean checkHourAndMinu(int hour, int minu) {
		boolean result = true;
		for(TimeOfDayMinu tm : timeList4Minu) {
			if(hour >= tm.getStartHour() && hour <= tm.getEndHour() && minu >= tm.getStartMinu() && minu <= tm.getEndMinu()) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 获取操作类型
	 * @return 0无操作    1清除原有玩家身上任务    2刷新所有玩家functionNpc
	 */
	public byte getActType() {
		byte result = 0;
		Calendar calendar = Calendar.getInstance();
		if(limitType == TimeLimitManager.type_of_weed) {
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			int yestorday = (dayOfWeek - 1) == 0 ? 7 : (dayOfWeek - 1);
			if(timeList != null && timeList.size() > 0) {
				if(timeList.contains(dayOfWeek)) {			//当天不开启
					result = 1;
				} else if(timeList.contains(yestorday)) {		//当天开启前一天不开启
					result = 2;
				}
			}
		} else if(limitType == TimeLimitManager.type_of_day) {
			if(timeList4Minu != null && timeList4Minu.size() > 0) {
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minu = calendar.get(Calendar.MINUTE);
				int preminu = (minu - 1) < 0 ? 59 : (minu - 1);
				int prehour = (minu - 1) < 0 ? (hour - 1) : hour;
				if(!checkHourAndMinu(hour, minu)) {				//当前不开放
					result = 1;
				} else if (!checkHourAndMinu(preminu, prehour)) {		//当前开放 前一分钟不开放
					result = 2;
				}
			}
		}
		return result;
	}
	
	public byte getLimitType() {
		return limitType;
	}
	public void setLimitType(byte limitType) {
		this.limitType = limitType;
	}
	public List<Integer> getTimeList() {
		return timeList;
	}
	public void setTimeList(List<Integer> timeList) {
		if(timeList != null && timeList.size() > 0) {
			for(int i=0; i<timeList.size(); i++) {
				int tempDay = timeList.get(i) % 7 + 1;
				timeList.set(i, tempDay);
			}
		}
		this.timeList = timeList;
	}
	public List<TimeOfDayMinu> getTimeList4Minu() {
		return timeList4Minu;
	}
	public void setTimeList4Minu(List<TimeOfDayMinu> timeList4Minu) {
		this.timeList4Minu = timeList4Minu;
	}
	
	
}
