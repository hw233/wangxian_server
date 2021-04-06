package com.fy.engineserver.constants;

/**
 * 事件的动画Mapping编号
 *  
 */
public interface EventMapping {
	/** 升级 **/
	public final static int EVENT_UPGRADE = 0;
	
	/** 治疗 **/
	public final static int EVENT_HEAL = 1;
	
	/** 加血 **/
	public final static int EVENT_HP_INCREASE = 2;
	
	/** 减血 **/
	public final static int EVENT_HP_DECREASE = 3;
	
	/** 加蓝 **/
	public final static int EVENT_MP_INCREASE = 4;
	
	/** 减蓝 **/
	public final static int EVENT_MP_DECREASE = 5;
	
	/** 获得经验值 **/
	public final static int EVENT_GAIN_EXPERIENCE = 6;
}
