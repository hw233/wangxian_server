package com.fy.engineserver.activity.dice.config;

public interface DiceActivityConfig {
	
	/**
	 * 开启骰子活动
	 * @return
	 */
	boolean isStart();
	
	long getEndTime();

}
