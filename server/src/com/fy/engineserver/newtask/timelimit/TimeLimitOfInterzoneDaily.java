package com.fy.engineserver.newtask.timelimit;

import java.text.ParseException;
import java.util.Calendar;

import com.fy.engineserver.newtask.service.TaskConfig;

/**
 * 任务接取时间限制-限制时间段内每天的时间段
 * 2010-12-23,2010-12-25|12：21,14:44
 * |前是日期段 |后是每天的时间段
 * 
 * 
 */
public class TimeLimitOfInterzoneDaily extends TimeLimit implements TaskConfig {

	Calendar start = Calendar.getInstance();
	Calendar end = Calendar.getInstance();
	TimeLimitOfDay limitOfDay;

	public TimeLimitOfInterzoneDaily(String value) {
		String[] split = value.split("\\|");
		if (split == null || split.length != 2) {
			throw new IllegalStateException("月配置异常[" + value + "]");
		}
		try {
			String[] startTime = split[0].split(",");
			start.setTime(sdf10.parse(startTime[0]));
			end.setTime(sdf10.parse(startTime[1]));
			limitOfDay = new TimeLimitOfDay(split[1]);
			setLimltType(TASK_TIME_LIMIT_INTERZONE_DAILY);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean timeAllow(long time) {
		Calendar now = Calendar.getInstance();
		if (time != -1) {
			now.setTimeInMillis(time);
		}
		start.set(Calendar.HOUR, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);

		end.set(Calendar.HOUR, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);

		if (now.before(start) || now.after(end)) {
//			System.out.println("限制时间段内每天的时间段 时间不符");
			return false;
		}
		return limitOfDay.timeAllow(time);
	}
	
	public static void main(String[] args) {
		TimeLimitOfInterzoneDaily interzoneDaily = new TimeLimitOfInterzoneDaily("2010-12-23,2011-12-25|10:21,10:44");
//		System.out.println(interzoneDaily.timeAllow());
	}
}
