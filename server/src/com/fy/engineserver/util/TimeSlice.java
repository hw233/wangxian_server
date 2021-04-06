package com.fy.engineserver.util;

import java.util.Date;

/**
 * 抽象表达任何逻辑的有效时间片段
 * 
 * @author 
 * 
 */
public abstract class TimeSlice {
	/**
	 * 判断当前时间是否有效，即是否在有效时间片段之内
	 * 
	 * @return true-时间有效
	 */
	public abstract boolean isValid(Date time);
}
