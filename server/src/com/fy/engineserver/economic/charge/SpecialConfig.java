package com.fy.engineserver.economic.charge;

import com.fy.engineserver.util.CompoundReturn;

/**
 * 充值列表针对服务器的特殊配置
 * 
 * 
 */
public abstract class SpecialConfig {
	/** 布尔值表示是否要特殊处理.int值表示要修改的位置索引,String是要增加的配置 */
	
	public long startTime;
	
	public long endTime;
	
	public SpecialConfig(){};
	
	public SpecialConfig(long startTime,long endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	};
	
	public abstract CompoundReturn getSpacialConfig();
}
