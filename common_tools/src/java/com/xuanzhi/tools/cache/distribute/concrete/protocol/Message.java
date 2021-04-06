package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.net.InetAddress;

public interface Message {

	/**
	 * 消息类型
	 * @return
	 */
	public int getType();
	
	/**
	 * 消息发送类型，分广播和点播两种
	 * @return
	 */
	public int getSendType();
	
	/**
	 * 消息的名称
	 * @return
	 */
	public String getName();
	
	/**
	 * 消息内容
	 * @return
	 */
	public void writeTo(java.io.OutputStream out) throws Exception;
	
	public void readFrom(java.io.ObjectInputStream input) throws Exception;
	
	public InetAddress getToAddress();
	
	public InetAddress getFromAddress();
	
	public void setFromAddress(InetAddress address);
	
	public void setToAddress(InetAddress address);
	
	public int getPriority();
}
