package com.fy.engineserver.util.time;

import java.util.Calendar;

import com.fy.engineserver.util.CompoundReturn;

/**
 * 所有时间配置类型的超类
 * 
 * 
 */
public abstract class TimeConf {
	/** 时间类型 */
	protected TimeConfigType timeConfigType;
	protected long timeDelay;

	public abstract TimeConfigType getConfigType();

	/** 是否在活动时间范围内 */
	public abstract CompoundReturn inConfTimedistance(Calendar calendar,long lastTime);

	public abstract long getTimeDelay();
}