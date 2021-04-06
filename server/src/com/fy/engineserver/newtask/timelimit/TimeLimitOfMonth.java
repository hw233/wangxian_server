package com.fy.engineserver.newtask.timelimit;

import java.util.Calendar;

import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.util.StringTool;

/**
 * 每月中某些天的时间段
 * 1，2,3|12:40,20：00
 * |前是日期 |后是每天开启时间段
 * 
 * 
 */
public class TimeLimitOfMonth extends TimeLimit implements TaskConfig {

	int[] days = new int[0];

	TimeLimitOfDay limitOfDay;

	public TimeLimitOfMonth(String value) {
		String[] split = value.split("\\|");
		if (split == null || split.length != 2) {
			throw new IllegalStateException("每月中某些天的时间段 配置错误:[" + value + "]");
		}
		days = StringTool.string2IntArr(split[0], ",");
		limitOfDay = new TimeLimitOfDay(split[1]);
		setLimltType(TASK_TIME_LIMIT_MONTH);
	}

	@Override
	public boolean timeAllow(long time) {
		Calendar calendar = Calendar.getInstance();
		if (time != -1) {
			calendar.setTimeInMillis(time);
		}
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		boolean dayFit = false;
		for (int i = 0, j = days.length; i < j; i++) {
			if (days[i] == day) {
				dayFit = true;
				break;
			}
		}
		if (!dayFit) {
//			System.out.println("月配置,时间不符");
			return false;
		}
		return limitOfDay.timeAllow(time);
	}

	public static void main(String[] args) {
		TimeLimitOfMonth limitOfMonth = new TimeLimitOfMonth("1,2,3,14|10:40,10:41");
//		System.out.println(limitOfMonth.timeAllow());
	}
}
