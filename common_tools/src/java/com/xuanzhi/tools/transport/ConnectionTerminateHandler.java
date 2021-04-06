package com.xuanzhi.tools.transport;

import java.nio.ByteBuffer;
import java.util.List;

public interface ConnectionTerminateHandler{
	
	/**
	 * 当某个链接被关闭时，会启动一个新的线程，调用此方法。
	 * 这样做是为了让那些没有回应的消息，有机会重发。
	 * 同时，receiveBuffer中可能包含一些回应消息，或者RequestMessage，
	 * 此方法应该对此也进行处理。
	 * 
	 * @param conn
	 * @param noResponseMessages
	 * @param receiveBuffer
	 */
	public void ternimate(Connection conn,List<RequestMessage> noResponseMessages,ByteBuffer receiveBuffer);
}
