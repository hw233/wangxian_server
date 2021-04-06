package com.xuanzhi.tools.transport;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.log4j.Logger;


/**
 *  客户端抽象类，方便基于LAMP实现的客户端使用
 *  
 *  需要继承此类。
 *  
 *  此类提供了两个方便的方法从外部使用：
 *  	sendMessageAndWaittingResponse 发送消息并等待响应
 *  	sendMessageAndWithoutResponse  发送消息不等待响应
 *  
 *  此类需要注意的问题：
 *  	当网络不可用或者网络拥堵且发送缓冲区满的情况下，消息将无法发送，直接抛出异常给上层。	
 *  
 * 
 *
 */
public abstract class AbstractClientService implements ConnectionCreatedHandler,MessageHandler{
	
	
	protected Hashtable<RequestMessage, ResponseMessage> responseMessageMap = new Hashtable<RequestMessage,ResponseMessage>();

	protected DefaultConnectionSelector selector;
	
	/**
	 * 子类可以重载此方法，以打印日志
	 * @return
	 */
	public abstract Logger getLogger();
	
	public abstract MessageFactory getMessageFactory();
	
	
	/**
	 * 子类可以重载此方法，处理消息
	 * 此方法可以直接抛出异常，日志都已经打印。
	 * 
	 * @return
	 */
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage request) throws Exception{
		return null;
	}
			
	
	/**
	 * 发送消息给服务器端，并且等待服务器端的返回。
	 * 可以设置等待的时间timeout，0为永久等待。
	 * 
	 * 如果服务器在规定的时间内没有返回，此方法将返回null
	 * 如果底层出现异常，那么此异常将被抛出来。
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public ResponseMessage sendMessageAndWaittingResponse(RequestMessage req,long timeout) throws Exception {
		long now = System.currentTimeMillis();
		Connection conn = null;
		try {
			long takeoutTimeout = timeout;
			if(takeoutTimeout <= 0) takeoutTimeout = 6000;
			conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy,takeoutTimeout);
		} catch (Exception e) {
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWaittingResponse] [FAIL] [Exception] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("takeoutConnection error cause " + e.getMessage());
		}
		
		if(conn == null){
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWaittingResponse] [FAIL] [can't_takeout_conn] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return null;
		}
		try {
			conn.writeMessage(req, true);
		}catch(Exception e){
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWaittingResponse] [FAIL] [Exception] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]",e);
			throw e;
		}
		synchronized(req){
			ResponseMessage resp = (ResponseMessage)responseMessageMap.get(req);
			if(resp == null) {
				try{
					req.wait(timeout);
				}catch(Exception e){
					//do something
				}
			}
		}
		ResponseMessage resp = (ResponseMessage)responseMessageMap.remove(req);
		if(resp != null) {
			if(getLogger() != null)
				getLogger().debug("["+this+"] [sendMessageAndWaittingResponse] [SUCC] [OK] ["+req.getTypeDescription()+"] ["+resp.getTypeDescription()+"] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return resp;
		} else {
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWaittingResponse] [FAIL] [TIMEOUT] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return null;
		}
	}
	
	/**
	 * 发送消息给服务器端，并且等待服务器端的返回。
	 * 可以设置等待的时间timeout，0为永久等待。
	 * 
	 * 如果服务器在规定的时间内没有返回，此方法将返回null
	 * 如果底层出现异常，那么此异常将被抛出来。
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public void sendMessageAndWithoutResponse(Message req) throws Exception {
		long now = System.currentTimeMillis();
		Connection conn = null;
		try {
			long takeoutTimeout = 6000;
			conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy,takeoutTimeout);
		} catch (Exception e) {
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWithoutResponse] [FAIL] [Exception] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("takeoutConnection error cause " + e.getMessage(), e);
		}
		
		if(conn == null){
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWithoutResponse] [FAIL] [can't_takeout_conn] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return ;
		}
		try {
			if(req instanceof RequestMessage)
				conn.writeMessage((RequestMessage)req, false);
			else
				conn.writeResponse((ResponseMessage)req);
		}catch(Exception e){
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWithoutResponse] [FAIL] [Exception] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]",e);
			throw e;
		}
		if(getLogger() != null)
			getLogger().debug("["+this+"] [sendMessageAndWithoutResponse] [SUCC] [OK] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////

	public void setConnectionSelector(ConnectionSelector selector) {
		this.selector = (DefaultConnectionSelector) selector;
		this.selector.setConnectionCreatedHandler(this);
	}
	
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
	public void created(Connection conn,String attachment) throws IOException{
		conn.setMessageFactory(getMessageFactory());
		conn.setMessageHandler(this);
	}
	
	public void discardRequestMessage(Connection conn, RequestMessage req)
			throws ConnectionException {
		synchronized(req){
			req.notify();
		}
	}

	public ResponseMessage receiveRequestMessage(Connection conn,
			RequestMessage request) throws ConnectionException {
		
		long startTime = System.currentTimeMillis();
		ResponseMessage res;
		try {
				res = handleReqeustMessage(conn, request);
				if (res != null) {
					if (getLogger()!= null && getLogger().isDebugEnabled()) {
						getLogger().debug("["+this+"] [receive-request] [success] [return-" + res.getTypeDescription()+"] [" + request.getTypeDescription() + "] [len:"+request.getLength()+"] [len:"+res.getLength()+"] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]");
					}
					return res;
				}
		}catch (ConnectionException e) {
				if(getLogger()!= null)
					getLogger().error("["+this+"] [receive-request] [error] [catch-ConnectionException] [" + request.getTypeDescription() + "] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]",	e);
				throw e;
		} catch (Throwable e) {
			if(getLogger()!= null)	
				getLogger().warn("["+this+"] [receive-request] [fail] [catch-exception] [" + request.getTypeDescription() + "] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]",	e);
				return null;
		}
	
		if(getLogger()!= null)
			getLogger().info("["+this+"] [receive-request] [success] [return-null] [" + request.getTypeDescription() + "] [len:"+request.getLength()+"] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]");

		return null;
	}

	public void receiveResponseMessage(Connection conn, RequestMessage req,
			ResponseMessage res) throws ConnectionException {
		if(req != null){
			if(getLogger() != null)
				getLogger().debug("[receiveResponseMessage] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+res.getLength()+"]");
			responseMessageMap.put(req,res);
			synchronized(req){
				req.notify();
			}
		}	
	}

	public RequestMessage waitingTimeout(Connection conn, long timeout)
			throws ConnectionException {
		return null;
	}

}
