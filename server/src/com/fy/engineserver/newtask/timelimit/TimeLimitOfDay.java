package com.fy.engineserver.newtask.timelimit;

import java.util.Calendar;

import com.fy.engineserver.newtask.service.TaskConfig;

/**
 * 时间限制 每天的aa:bb,cc:dd精确到分
 * 
 * 
 */
public class TimeLimitOfDay extends TimeLimit implements TaskConfig {

	Calendar start;
	Calendar end;

	public TimeLimitOfDay(String value) {
		String[] split = value.split(",");
		if (split == null || split.length != 2) {
			throw new IllegalStateException("每天时间限制异常[" + value + "]");
		}
		String[] startTime = split[0].split(":");
		String[] endTime = split[1].split(":");
		setLimltType(TASK_TIME_LIMIT_DAY);
		start = Calendar.getInstance();
		end = Calendar.getInstance();

		start.set(Calendar.HOUR_OF_DAY, Integer.valueOf(startTime[0]));
		start.set(Calendar.MINUTE, Integer.valueOf(startTime[1]));
		end.set(Calendar.HOUR_OF_DAY, Integer.valueOf(endTime[0]));
		end.set(Calendar.MINUTE, Integer.valueOf(endTime[1]));
	}

	@Override
	public boolean timeAllow(long time) {
		Calendar now = Calendar.getInstance();

		if (time != -1) {
			now.setTimeInMillis(time);
		}

		Calendar todayStart = start;
		todayStart.set(Calendar.YEAR, now.get(Calendar.YEAR));
		todayStart.set(Calendar.MONTH, now.get(Calendar.MONTH));
		todayStart.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR));

		Calendar todayEnd = end;
		todayEnd.set(Calendar.YEAR, now.get(Calendar.YEAR));
		todayEnd.set(Calendar.MONTH, now.get(Calendar.MONTH));
		todayEnd.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR));

		if (now.before(todayStart) || now.after(todayEnd)) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		TimeLimitOfDay day = new TimeLimitOfDay("9:33,12:00");
//		System.out.println(day.timeAllow());
	}
}
