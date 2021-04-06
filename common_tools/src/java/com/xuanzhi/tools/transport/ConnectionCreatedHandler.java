package com.xuanzhi.tools.transport;

import java.io.IOException;

/**
 * 一个新的链接创建处理接口
 *
 */
public interface ConnectionCreatedHandler {

	/**
	 * 当一个新的链接与服务器连接成功后，在一切其他操作前调用此方法。
	 * 
	 * 此方法至少应该设置Connection的MessageFactory和MessageHangler，
	 * 如果不设置，返回的链接将被关闭。
	 * 
	 * 调用此方法时，Connection是blocking模式的，并处于CONN_STATE_OPEN状态。
	 * 此方法可以用来做一开始的协议检查，版本检查，或者认证等。
	 * 
	 * 如果正常返回，Connection将被设置为non-blocking，并加入到
	 * selector中，供以后发送消息使用。
	 * 
	 * 如果返回异常，链接将被关闭。
	 * @param conn
	 * @throws Exception
	 */
	public void created(Connection conn,String attachment) throws IOException;
}
