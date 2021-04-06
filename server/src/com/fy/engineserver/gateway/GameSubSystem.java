package com.fy.engineserver.gateway;

import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 游戏服务器的子系统接口
 * 
 * 
 * 
 * 
 */
public interface GameSubSystem {

	/**
	 * 系统的名词，必须唯一
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 获得感兴趣的消息类型，返回的消息类型的描述数组，不要重复添加
	 * 
	 * @see {@link com.xuanzhi.tools.transport.RequestMessage#getTypeDescription()}
	 * @return
	 */
	public String[] getInterestedMessageTypes();

	/**
	 * 处理客户端主动发给服务器的请求消息
	 * 
	 * @param conn
	 *            客户端到服务器的链接，此链接不能直接用。
	 * @param message
	 *            客户端向服务器发起的请求消息
	 * @return 返回给客户端的响应消息，如果返回null，表明需要下一个子系统处理，如果下一个子系统存在的话
	 * @throws ConnectionException
	 *             抛出这个异常表明链接有问题，要求服务器强制与客户端断开链接
	 * @throws Exception
	 *             其他异常，不会断开连接
	 */
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception;

	/**
	 * 处理客户端返回给服务器的响应消息
	 * 
	 * @param conn
	 *            客户端到服务器的链接，此链接不能直接用。
	 * @param request
	 *            服务器发给客户端的请求
	 * @param response
	 *            客户端回复服务器的响应
	 * @return true标识需要下一个子系统处理，false标识已经处理完毕，不需要再处理。
	 * @throws ConnectionException
	 *             抛出这个异常表明链接有问题，要求服务器强制与客户端断开链接
	 * @throws Exception
	 *             其他异常，不会断开连接
	 */
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception;
}
