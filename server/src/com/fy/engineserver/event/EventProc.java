package com.fy.engineserver.event;

/**
 * 事件处理器。注册关心的事件，然后处理。
 * 
 * create on 2013年8月27日
 */
public interface EventProc {
	public void proc(Event evt);
	/**
	 * 需要使用者自行实现并调用《一次》此方法，进行事件的注册。
	 */
	public void doReg();
}
