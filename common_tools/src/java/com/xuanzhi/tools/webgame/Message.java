package com.xuanzhi.tools.webgame;

/**
 * 消息接口
 * 
 */
public interface Message {
	
	/**
	 * 消息的ID
	 * @return
	 */
	public int getMessageID();
	
	/**
	 * 消息的描述信息
	 * @return
	 */
	public String getTypeDescription();
	
	/**
	 * 得到消息的序列
	 * @return
	 */
	public long getSequeceNum();
}
