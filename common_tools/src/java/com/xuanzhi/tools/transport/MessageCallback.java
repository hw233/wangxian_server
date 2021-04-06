package com.xuanzhi.tools.transport;

/**
 * 响应消息回调接口
 * 
 */
public interface MessageCallback {

	/**
	 * 某个RequestMessage，在重发了n次以后，然后没有收到对应的ResponseMessage，
	 * 当n达到最大重发次数后，将不在重发，并且从滑动窗口中清除。
	 * 
	 * 如果Handler要关闭此链接，请抛出ConnectionException
	 * 
	 * @param req 被丢弃的消息
	 * @param conn 对应的链接
	 * @throws ConnectionException 预示着出现链接异常，应该关闭此链接
	 */
	public void discardRequestMessage(Connection conn,RequestMessage req) throws ConnectionException;
	
	/**
	 * 收到ResponseMessage，捎带的参数还有先前对应的RequestMessage。
	 * 此方法返回有，才会去处理下一个消息，所以此方法应该尽快返回。
	 * 并且此方法不要抛出异常。
	 * 
	 * @param req 先前发送的RequestMessage，如果找不到，则可能为null
	 * @param res 返回的ResponseMessage
	 * @param conn 对应的链接
	 * @throws ConnectionException 预示着出现链接异常，应该关闭此链接
	 */
	public void receiveResponseMessage(Connection conn,RequestMessage req,ResponseMessage res) throws ConnectionException;
	
}
