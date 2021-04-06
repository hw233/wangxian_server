package com.fy.engineserver.activity.wolf.config;

public interface ActivityConfig {
	
	/**
	 * 活动是否开启
	 * @return
	 */
	boolean isStart();
	
	boolean platFit();
	
	/**
	 * 1分钟即将开启通知
	 * @return
	 */
	boolean notice1Minute();
	
	boolean notice5Minute();
	
	boolean notice10Minute();
	
	String getOpenTimeMess();
	
	String getCurrOpenFlag();
}
