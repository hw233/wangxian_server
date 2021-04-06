package com.xuanzhi.tools.webgame;

/**
 * 客户端发给服务器的请求消息
 * 
 */
public interface ReqMessage extends Message {
	
	/**
	 * 消息转化为字符串，paramName1=value1&paramName2=value2&......
	 * @return
	 */
	public String getString();
}
