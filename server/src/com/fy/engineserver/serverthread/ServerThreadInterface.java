package com.fy.engineserver.serverthread;
/**
 * 服务器线程管理
 * 
 * @date on create 2016年8月24日 下午1:20:43
 */
public interface ServerThreadInterface {
	/** 是否需要改变线程数量 */
	public boolean needChangeThreadNums();
	/** 调整线程数量 */
	public void changeThreadNums();
}
