package com.fy.engineserver.util;

import java.util.Calendar;
import java.util.Date;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 每周时间段，即每周日、周一、周二、周三、周四、周五、周六的某个时间段。周几可以多选，具体时间精确到小时
 * 
 * @author 
 * 
 */
public class WeeklyTimeSlice extends TimeSlice {
	/**
	 * 长度为7，第一个（index=0）表示周日，第二个表示周一，以此类推，最后一个表示周六
	 */
	final boolean[] weeklyValid;
	final DailyTimeSlice dailyTimeSlice;
	public final static String[] NAMES_OF_WEEK_DAYS = { Translate.text_5669, Translate.text_5670, Translate.text_5671, Translate.text_5672, Translate.text_5673, Translate.text_5674, Translate.text_5675 };

	public WeeklyTimeSlice(boolean[] weeklyValid, byte startHour, byte endHour) {
		this.weeklyValid = weeklyValid;
		dailyTimeSlice = new DailyTimeSlice(endHour, startHour);
	}

	public boolean isValid(Date time) {
		boolean ok = false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		if (dayOfWeek < 0 || dayOfWeek >= weeklyValid.length) {
			ok = false;
		} else {
			if (weeklyValid[dayOfWeek]) {
				ok = dailyTimeSlice.isValid(time);
			} else {
				ok = false;
			}
		}
		if (Game.logger.isInfoEnabled()) {
//			Game.logger.info("[WeeklyTimeSlice] [时间点是否允许] [" + (DateUtil.formatDate(time, "yyMMdd HH:mm")) + "] [" + ok + "] [条件:" + this.toString() + "]");
			if(Game.logger.isInfoEnabled())
				Game.logger.info("[WeeklyTimeSlice] [时间点是否允许] [{}] [{}] [条件:{}]", new Object[]{(DateUtil.formatDate(time, "yyMMdd HH:mm")),ok,this.toString()});
		}
		return ok;
	}

	public boolean[] getWeeklyValid() {
		return weeklyValid;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Translate.text_6066 + dailyTimeSlice.startHour + Translate.text_5462 + dailyTimeSlice.endHour + Translate.text_1469);
		sb.append(" ");
		for (int i = 0; i < NAMES_OF_WEEK_DAYS.length; i++) {
			if (weeklyValid[i]) {
				sb.append(NAMES_OF_WEEK_DAYS[i] + ",");
			}
		}

		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}

		sb.append(" ");

		return sb.toString();
	}
}
