package com.fy.engineserver.util;

import java.util.Calendar;
import java.util.Date;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 每日时间段。只对小时有限制，对年份、月份、日期、星期都没有限制，即每天的规定时间内都有效<br>
 * 如果起始时间为0，终止时间为24，代表全天24小时有效
 * 
 * @author 
 * 
 */
public class DailyTimeSlice extends TimeSlice {
	/**
	 * 起始时间，单位小时，取值从0-24；
	 */
	final byte startHour;
	/**
	 * 终止时间，单位小时，取值从0-24；
	 */
	final byte endHour;

	public DailyTimeSlice(byte endHour, byte startHour) {
		this.endHour = endHour;
		this.startHour = startHour;
	}

	public boolean isValid(Date time) {
		boolean ok = false;
		if (startHour == 0 && endHour == 24) {
			ok = true;
		} else if (startHour == endHour) {
			ok = false;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			if (startHour < endHour) {
				// 如7点到9点的情况
				ok = startHour <= hour && hour < endHour;
			} else {
				// 如22点到凌晨2点的情况
				ok = hour >= startHour || hour < endHour;
			}
		}
		if (Game.logger.isInfoEnabled()) {
//			Game.logger.info("[DailyTimeSlice] [时间点是否允许] [" + (DateUtil.formatDate(time, "yyMMdd HH:mm")) + "] [" + ok + "] [条件:" + this.toString() + "]");
			if(Game.logger.isInfoEnabled())
				Game.logger.info("[DailyTimeSlice] [时间点是否允许] [{}] [{}] [条件:{}]", new Object[]{(DateUtil.formatDate(time, "yyMMdd HH:mm")),ok,this.toString()});
		}
		return ok;
	}

	public byte getStartHour() {
		return startHour;
	}

	public byte getEndHour() {
		return endHour;
	}

	public String toString() {
		return Translate.text_5977 + startHour + Translate.text_5462 + endHour + Translate.text_1469;
	}
}
