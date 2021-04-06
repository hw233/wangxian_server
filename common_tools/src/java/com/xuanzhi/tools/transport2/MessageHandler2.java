package com.xuanzhi.tools.transport2;

import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.Message;
/**
 * 消息处理类，消息处理类中所有的方法都是处理Connection的Thread直接调用的，
 * 所以，这些方法如果不返回，就一直占用Connection资源，此Connection就不能
 * 用于发送或者接收其他消息。
 * 
 *
 */
public interface MessageHandler2 {

	/**
	 * <pre>
	 * 某个请求消息（RequestMessage），在重发了n次以后，还没有收到对应的响应消息（ResponseMessage），
	 * 当n达到最大重发次数后，将不再重发，并且从滑动窗口中清除。
	 * 清除后，将通知上层应用程序，吃请求消息发送失败。
	 * 
	 * 如果上层应用程序要关闭此链接，请抛出ConnectionException
	 * 
	 * 注意：此方法调用是在获取此链接的情况下，也就是说此方法如果不立即返回，
	 *      那么此链接将不能用作其他用途，包括接收数据，发送数据。
	 *      如果调用此方法的时间超过链接最大Checkout的时间，链接将被强行
	 *      关闭。此强行关闭，将不通知上层应用程序定义的ConnectionTerminateHandler接口。
	 * </pre>
	 * @param req 被丢弃的消息
	 * @param conn 对应的链接
	 * @throws ConnectionException 预示着出现链接异常，应该关闭此链接
	 */
	public void discardMessage(Connection2 conn,Message req) throws ConnectionException;
	
	/**
	 * <pre>
	 * 等待对方发送消息过来，超时。
	 * 
	 * 此方法返回后，对应的链接才会继续处理下一条消息，所以此方法应该尽快返回。
	 * 
	 * 如果上层应用程序要关闭此链接，请抛出ConnectionException
	 * 
	 * 注意：此方法调用是在获取此链接的情况下，也就是说此方法如果不立即返回，
	 *      那么此链接将不能用作其他用途，包括接收数据，发送数据。
	 *      如果调用此方法的时间超过链接最大Checkout的时间，链接将被强行
	 *      关闭。此强行关闭，将不通知上层应用程序定义的ConnectionTerminateHandler接口。
	 * </pre>     
	 * @param conn 对应的链接
	 * @return 可以为null，也可以为任何其他消息
	 * @throws ConnectionException 预示着出现链接异常，应该关闭此链接
	 */
	public void waitingTimeout(Connection2 conn,long timeout) throws ConnectionException;
	
	
	/**
	 * <pre>
	 * 收到一条RequestMessage，进行相应的处理，处理结束后，返回给对方一条对应的ResponseMessage
	 * 此方法不应返回null，或者抛出异常。
	 * 
	 * 此方法返回后，对应的链接才会继续处理下一条消息，所以此方法应该尽快返回。
	 * 
	 *  * 如果上层应用程序要关闭此链接，请抛出ConnectionException
	 * 
	 * 注意：此方法调用是在获取此链接的情况下，也就是说此方法如果不立即返回，
	 *      那么此链接将不能用作其他用途，包括接收数据，发送数据。
	 *      如果调用此方法的时间超过链接最大Checkout的时间，链接将被强行
	 *      关闭。此强行关闭，将不通知上层应用程序定义的ConnectionTerminateHandler接口。
	 * </pre>     
	 * @param message
	 * @param conn 对应的链接
	 * @return 返回对应的响应消息
	 * @throws ConnectionException 预示着出现链接异常，应该关闭此链接
	 */
	public void receiveMessage(Connection2 conn,Message message) throws ConnectionException;
}
