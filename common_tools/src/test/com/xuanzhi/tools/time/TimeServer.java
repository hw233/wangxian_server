package com.xuanzhi.tools.time;

import java.io.IOException;

import com.xuanzhi.tools.transport.*;

public class TimeServer 
	implements ConnectionConnectedHandler,MessageHandler{

	/**
	 * 当客户端连接上服务器的时候，Lamp会回调这个方法
	 * 此方法最重要的功能是设置链接的消息工厂和消息处理器
	 */
	public void connected(Connection conn) throws IOException {
		conn.setMessageFactory(TimeMessageFactory.getInstance());
		conn.setMessageHandler(this);
	}

	public void discardRequestMessage(Connection conn, RequestMessage req)
			throws ConnectionException {
	}

	/**
	 * 当服务器收到客户端的请求时，会调用此方法，此方法返回的响应消息
	 * Lamp会传输给客户端，如果返回null，lamp不传递任何消息给客户端
	 */
	public ResponseMessage receiveRequestMessage(Connection conn,
			RequestMessage message) throws ConnectionException {
		if(message instanceof TIME_REQ){
			TIME_REQ req = (TIME_REQ)message;
			return new TIME_RES(req.getSequnceNum(),System.currentTimeMillis());
		}
		return null;
	}

	public void receiveResponseMessage(Connection conn, RequestMessage req,
			ResponseMessage res) throws ConnectionException {
	}

	public RequestMessage waitingTimeout(Connection conn, long timeout)
			throws ConnectionException {
		return null;
	}

	public static void main(String args[]) throws Exception{
		int port = 7777;
		TimeServer server = new TimeServer();
		DefaultConnectionSelector selector = new DefaultConnectionSelector();
		selector.setClientModel(false);
		selector.setPort(port);
		selector.setConnectionConnectedHandler(server);
		selector.init();
	}
}
