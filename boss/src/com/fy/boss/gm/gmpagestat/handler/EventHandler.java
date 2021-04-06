package com.fy.boss.gm.gmpagestat.handler;


import java.util.List;

import com.fy.boss.gm.gmpagestat.RecordEvent;
import com.fy.boss.gm.gmpagestat.EventForJson.EventType;


public abstract class EventHandler {

	public EventType eventtype;
	
	/**
	 * 初始化所有符合规则的事件
	 * @param e
	 * @return
	 */
	public abstract void initFitRule(List<RecordEvent> list);
	
	/**
	 * 符合规则的事件处理
	 */
	public abstract void handle();
	
}
