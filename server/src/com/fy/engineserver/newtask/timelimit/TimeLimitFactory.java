package com.fy.engineserver.newtask.timelimit;

import com.fy.engineserver.newtask.service.TaskConfig;

public class TimeLimitFactory implements TaskConfig {

	public static TimeLimit createTimeLimit(String type, String value) {
		if (type == null || type.isEmpty()) {
			return null;
		}
		return createTimeLimit(Integer.valueOf(type), value);

	}

	public static TimeLimit createTimeLimit(int type, String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		switch (type) {
		case TASK_TIME_LIMIT_DAY:
			return new TimeLimitOfDay(value);
		case TASK_TIME_LIMIT_MONTH:
			return new TimeLimitOfMonth(value);
		case TASK_TIME_LIMIT_WEEK:
			return new TimeLimitOfWeek(value);
		case TASK_TIME_LIMIT_INTERZONE_DAILY:
			return new TimeLimitOfInterzoneDaily(value);
		case TASK_TIME_LIMIT_TIME_INTERZONE:
			return new TimeLimitOfInterzone(value);
		}
		return null;
	}
}
