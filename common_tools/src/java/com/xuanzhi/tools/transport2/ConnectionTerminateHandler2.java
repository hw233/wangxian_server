package com.xuanzhi.tools.transport2;


public interface ConnectionTerminateHandler2{
	
	/**
	 * 当某个链接被关闭时，会启动一个新的线程，调用此方法。
	 * 
	 * @param conn
	 */
	public void ternimate(Connection2 conn);
}
