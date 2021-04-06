package com.fy.engineserver.newtask.timelimit;

import java.text.ParseException;
import java.util.Calendar;

import com.fy.engineserver.newtask.service.TaskConfig;

/**
 * 时间区间
 * 2010-12-23 14:44|2010-12-24 15:22
 * 
 * 
 */
public class TimeLimitOfInterzone extends TimeLimit implements TaskConfig {

	Calendar from = Calendar.getInstance();
	Calendar to = Calendar.getInstance();

	public TimeLimitOfInterzone(String value) {
		String[] split = value.split("\\|");
		if (split == null || split.length != 2) {
			throw new IllegalStateException("时间区间 限制异常[" + value + "]");
		}
		try {
			from.setTime(sdf16.parse(split[0]));
			to.setTime(sdf16.parse(split[1]));
			setLimltType(TASK_TIME_LIMIT_TIME_INTERZONE);
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

		if (now.before(from) || now.after(to)) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		TimeLimitOfInterzone interzone = new TimeLimitOfInterzone("2010-12-23 14:44|2010-12-24 15:22");
//		System.out.println(interzone.timeAllow());
	}
}
