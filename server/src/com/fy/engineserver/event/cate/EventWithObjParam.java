package com.fy.engineserver.event.cate;

import com.fy.engineserver.event.Event;

/**
 * 带有一个Object参数的事件。用于通用的只有一个对象参数的事件。
 * 
 * create on 2013年8月16日
 */
public class EventWithObjParam extends Event{
	
	public Object param;
	
	public EventWithObjParam(int id, Object param){
		this.id = id;
		this.param = param;
	}

	@Override
	public void initId() {
		// TODO Auto-generated method stub
		
	}

}
