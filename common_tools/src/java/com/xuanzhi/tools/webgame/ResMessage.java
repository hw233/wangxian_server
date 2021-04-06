package com.xuanzhi.tools.webgame;

/**
 * 一个回应消息
 * 
 */
public interface ResMessage extends Message {
	
	/**
	 * 消息转化为json字符串
	 * @return
	 */
	public String toJson();
}
