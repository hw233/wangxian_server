package com.xuanzhi.tools.transport2;

import java.io.IOException;
import java.util.Hashtable;

import org.slf4j.Logger;

import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.MessageFactory;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;


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
public abstract class AbstractClientService2 implements ConnectionCreatedHandler2,MessageHandler2{
	
	
	protected Hashtable<Long, ResponseMessage> responseMessageMap = new Hashtable<Long,ResponseMessage>();
	protected Hashtable<Long, RequestMessage> requestMessageMap = new Hashtable<Long,RequestMessage>();
	
	protected DefaultConnectionSelector2 selector;
	
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
	public ResponseMessage handleReqeustMessage(Connection2 conn,
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
		Connection2 conn = null;
		try {
			long takeoutTimeout = timeout;
			if(takeoutTimeout <= 0) takeoutTimeout = 6000;
			conn = selector.takeoutConnection(null,takeoutTimeout);
		} catch (Exception e) {
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWaittingResponse] [FAIL] [Exception] ["+req.getTypeDescription()+"] [sid:"+req.getSequenceNumAsString()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("takeoutConnection error cause " + e.getMessage());
		}
		
		if(conn == null){
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWaittingResponse] [FAIL] [can't_takeout_conn] ["+req.getTypeDescription()+"] [sid:"+req.getSequenceNumAsString()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return null;
		}
		try {
			requestMessageMap.put(req.getSequnceNum(), req);
			conn.sendMessage(req);
		}catch(Exception e){
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWaittingResponse] [FAIL] [Exception] ["+req.getTypeDescription()+"] [sid:"+req.getSequenceNumAsString()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]",e);
			throw e;
		}
		synchronized(req){
			ResponseMessage resp = (ResponseMessage)responseMessageMap.get(req.getSequnceNum());
			if(resp == null) {
				try{
					req.wait(timeout);
				}catch(Exception e){
					//do something
				}
			}
		}
		
		ResponseMessage resp = (ResponseMessage)responseMessageMap.remove(req.getSequnceNum());
		requestMessageMap.remove(req.getSequnceNum());
		
		if(resp != null) {
			if(getLogger() != null)
				getLogger().debug("["+this+"] [sendMessageAndWaittingResponse] [SUCC] [OK] ["+req.getTypeDescription()+"] [sid:"+req.getSequenceNumAsString()+"] ["+resp.getTypeDescription()+"] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return resp;
		} else {
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWaittingResponse] [FAIL] [TIMEOUT] ["+req.getTypeDescription()+"] [sid:"+req.getSequenceNumAsString()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
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
		Connection2 conn = null;
		try {
			long takeoutTimeout = 6000;
			conn = selector.takeoutConnection(null, takeoutTimeout);
		} catch (Exception e) {
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWithoutResponse] [FAIL] [Exception] ["+req.getTypeDescription()+"] [sid:"+req.getSequenceNumAsString()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("takeoutConnection error cause " + e.getMessage(), e);
		}
		
		if(conn == null){
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWithoutResponse] [FAIL] [can't_takeout_conn] ["+req.getTypeDescription()+"] [sid:"+req.getSequenceNumAsString()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return ;
		}
		try {
			conn.sendMessage(req);
		}catch(Exception e){
			if(getLogger() != null)
				getLogger().warn("["+this+"] [sendMessageAndWithoutResponse] [FAIL] [Exception] ["+req.getTypeDescription()+"] [sid:"+req.getSequenceNumAsString()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]",e);
			throw e;
		}
		if(getLogger() != null)
			getLogger().debug("["+this+"] [sendMessageAndWithoutResponse] [SUCC] [OK] ["+req.getTypeDescription()+"] [sid:"+req.getSequenceNumAsString()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////

	public void setConnectionSelector(DefaultConnectionSelector2 selector) {
		this.selector = (DefaultConnectionSelector2) selector;
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
	public void created(Connection2 conn,String attachment) throws IOException{
		conn.setMessageFactory(getMessageFactory());
		conn.setMessageHandler(this);
	}
	
	public void discardMessage(Connection2 conn,Message req)
			throws ConnectionException {
		synchronized(req){
			req.notify();
		}
	}



	
	public void receiveMessage(Connection2 conn,Message message) throws ConnectionException{
		if(message instanceof ResponseMessage){
			ResponseMessage res = (ResponseMessage)message;
			RequestMessage req = requestMessageMap.get(res.getSequnceNum());
			if(req != null){
				if(getLogger() != null)
					getLogger().debug("[receiveResponseMessage] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] [sid:"+res.getSequenceNumAsString()+"] [len:"+res.getLength()+"]");
				responseMessageMap.put(res.getSequnceNum(),res);
				
				synchronized(req){
					req.notify();
				}
			}else{
				if(getLogger() != null)
					getLogger().debug("[receiveResponseMessage] [req_is_null] ["+res.getTypeDescription()+"] [sid:"+res.getSequenceNumAsString()+"] [len:"+res.getLength()+"]");
			}
		}else if(message instanceof RequestMessage){
			RequestMessage req = (RequestMessage)message;
			
			long startTime = System.currentTimeMillis();
			ResponseMessage res;
			try {
					res = handleReqeustMessage(conn, req);
					if (res != null) {
						try{
							conn.sendMessage(res);
							if (getLogger()!= null && getLogger().isDebugEnabled()) {
								getLogger().debug("["+this+"] [receive-request] [success] [return-" + res.getTypeDescription()+"] [" + req.getTypeDescription() + "] [sid:"+req.getSequenceNumAsString()+"] [len:"+req.getLength()+"] [len:"+res.getLength()+"] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]");
							}
						}catch(Exception e){
							
							getLogger().warn("["+this+"] [receive-request] [fail] [return-fail" + res.getTypeDescription()+"] [" + req.getTypeDescription() + "] [sid:"+req.getSequenceNumAsString()+"] [len:"+req.getLength()+"] [len:"+res.getLength()+"] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]",e);
							
						}
					}
			}catch (ConnectionException e) {
					if(getLogger()!= null)
						getLogger().error("["+this+"] [receive-request] [error] [catch-ConnectionException] [" + req.getTypeDescription() + "] [sid:"+req.getSequenceNumAsString()+"] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]",	e);
					throw e;
			} catch (Throwable e) {
				if(getLogger()!= null)	
					getLogger().warn("["+this+"] [receive-request] [fail] [catch-exception] [" + req.getTypeDescription() + "] [sid:"+req.getSequenceNumAsString()+"] [" + conn.getName() + "] [" + conn.getIdentity() + "] [cost:" + (System.currentTimeMillis() - startTime) + "]",	e);
			}
		
	
		}
	}
	


	public void waitingTimeout(Connection2 conn, long timeout)
			throws ConnectionException {
	}

}
