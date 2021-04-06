package com.xuanzhi.tools.cache.distribute.concrete;

import com.xuanzhi.tools.cache.distribute.concrete.protocol.*;

/**
 * 消息发送和接受器，响应的有两个队列，分别是发送消息队列和接受消息队列。
 * 这两个队列都是优先级消息队列，优先处理优先级高的队列。
 *
 */
public interface MessageTransport {

	/**
	 * 获得一个消息，如果没有消息，一直等待直到超时
	 * @param timeout 等待的时间，如果小于等于0，就一直等待
	 * @return
	 */
	public Message receive(long timeout);
	
	/**
	 * 发送消息，此方法只是将消息存放到发送队列中
	 * @param message
	 */
	public void send(Message message);
}
