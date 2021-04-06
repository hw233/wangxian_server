package com.fy.engineserver.newtask.timelimit;

import java.util.Calendar;

import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.util.StringTool;

/**
 * 每周中某些天的时间段
 * 1,2,3|12:30,12:50
 * |前是星期几 |后是每天开启时间
 * 
 * 
 */
public class TimeLimitOfWeek extends TimeLimit implements TaskConfig {

	// 策划真实录入的
	int[] days = new int[0];
	// 和java匹配的
	int[] modifyDays = new int[0];
	TimeLimitOfDay limitOfDay;

// Calendar start;
// Calendar end;

	public TimeLimitOfWeek(String value) {
		String[] split = value.split("\\|");

		if (split == null || split.length != 2) {
			throw new IllegalStateException("每周 日期限制配置错误:[" + value + "]");
		}
		days = StringTool.string2IntArr(split[0], ",");
		modifyDays();
		limitOfDay = new TimeLimitOfDay(split[1]);
		setLimltType(TASK_TIME_LIMIT_WEEK);
	}

	/**
	 * 修正日期
	 */
	private void modifyDays() {
		modifyDays = new int[days.length];
		for (int i = 0, j = days.length; i < j; i++) {
			modifyDays[i] = (days[i] + 8) % 7;
		}
	}

	@Override
	public boolean timeAllow(long time) {
		Calendar calendar = Calendar.getInstance();
		if (time != -1) {
			calendar.setTimeInMillis(time);
		}
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		boolean dayFit = false;
		for (int i = 0, j = modifyDays.length; i < j; i++) {
			if (modifyDays[i] == week) {
				dayFit = true;
				break;
			}
		}
		if (!dayFit) {
//			System.out.println("日期不符");
			return false;
		}
		return limitOfDay.timeAllow(time);
	}

	public static void main(String[] args) {
		TimeLimitOfWeek limitOfWeek = new TimeLimitOfWeek("1,2,3|10:20,12:50");
//		System.out.println(limitOfWeek.timeAllow());
	}
}
