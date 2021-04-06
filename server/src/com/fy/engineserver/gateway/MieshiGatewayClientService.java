package com.fy.engineserver.gateway;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.enterlimit.Player_Process;
import com.fy.engineserver.message.GET_WAIGUA_PROCESS_NAMES_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_SERVER_TIREN_REQ;
import com.fy.engineserver.message.QUERY_CLIENT_INFO_RES;
import com.fy.engineserver.message.SESSION_VALIDATE_RES;
import com.fy.engineserver.sprite.NewUserEnterServerService;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.queue.DefaultQueue;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionCreatedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.ConnectionSelector;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;
import com.xuanzhi.tools.transport.SelectorPolicy;

/**
 * 用于游戏服务器连接网关用，发送一些消息给网关
 * 
 *
 */
public class MieshiGatewayClientService implements ConnectionCreatedHandler,
	MessageHandler,Runnable {

	static Logger logger = LoggerFactory.getLogger(MieshiGatewayClientService.class);
	
	protected Hashtable<RequestMessage, ResponseMessage> responseMessageMap = new Hashtable<RequestMessage,ResponseMessage>();
	
	private static MieshiGatewayClientService mself;

	public static MieshiGatewayClientService getInstance() {
		return mself;
	}

	protected DefaultConnectionSelector selector;
	Thread thread = null;
	DefaultQueue dq = new DefaultQueue(409600);
	public void init() throws Exception {
		
		thread = new Thread(this,"服务器发送消息给网关线程");
		thread.start();
		mself = this;
		ServiceStartRecord.startLog(this);
	}

	public void setConnectionSelector(ConnectionSelector selector) {
		this.selector = (DefaultConnectionSelector) selector;
		this.selector.setConnectionCreatedHandler(this);
		this.selector.setConnectionSendBufferSize(512*1024);
	}

	public void discardRequestMessage(Connection conn, RequestMessage req)
			throws ConnectionException {
		synchronized(req){
			req.notify();
		}
	}

	public ResponseMessage receiveRequestMessage(Connection conn,
			RequestMessage request) throws ConnectionException {
		if(request instanceof NOTIFY_SERVER_TIREN_REQ){
			//踢人
			NewUserEnterServerService.handle_NOTIFY_SERVER_TIREN_REQ(conn, request);
		}
		return null;
		
	}

	public void receiveResponseMessage(Connection conn, RequestMessage req,
			ResponseMessage res) throws ConnectionException {
		if(res instanceof QUERY_CLIENT_INFO_RES){
			QUERY_CLIENT_INFO_RES rr = (QUERY_CLIENT_INFO_RES)res;
 			PlayerManager pm = PlayerManager.getInstance();
 			if(rr.getClientId().length() > 0){
 				Player p[] = null;
				try {
					p = pm.getPlayerByUser(rr.getUsername());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 				if(p != null && p[0].getConn() != null){
 					p[0].getConn().setAttachmentData("QUERY_CLIENT_INFO_RES", res);
 				}
 			}
		}else if(res instanceof SESSION_VALIDATE_RES){
			NewUserEnterServerService.handle_SESSION_VALIDATE_RES(conn, res);
		}else if (res instanceof GET_WAIGUA_PROCESS_NAMES_RES) {
			Player_Process.moniqiArr = ((GET_WAIGUA_PROCESS_NAMES_RES)res).getProNames();
		}
		/**
		 * 此处这样写是为了不影响以前的逻辑
		 */
		else
		{
			if(req != null){
					logger.debug("[receiveResponseMessage] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+res.getLength()+"]");
				responseMessageMap.put(req,res);
				synchronized(req){
					req.notify();
				}
			}
		}
	}
	
	public RequestMessage waitingTimeout(Connection arg0, long arg1)
			throws ConnectionException {
		//return new ACTIVE_TEST_REQ(GameMessageFactory.nextSequnceNum());
		return null;
	}

	public void ternimate(Connection conn,
			List<RequestMessage> noResponseMessages, ByteBuffer receiveBuffer) {
	}
	//这个队列只发送RequestMessage,不发送Response
	
	@Override
	public void created(Connection conn, String attachment) throws IOException {
		// TODO Auto-generated method stub
		GameMessageFactory mf = GameMessageFactory.getInstance();
		conn.setMessageFactory(mf);
		conn.setMessageHandler(this);
	}

	
	public void sendMessageToGateway(Message m){
		dq.push(m);
	}
	
	
	public void run(){

		while(Thread.currentThread().isInterrupted() == false){
			try {
				Message message = (Message)dq.peek();
				if(message != null){
					long now = System.currentTimeMillis();
					Connection conn = null;
					try {
						conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy, 6000L);
					} catch (Exception e) {
						logger.error("[takeoutConnection] [异常]",e);
					}
					if(conn == null){
						logger.error("[takeoutConnection] [conn==null]");
						continue;
					}
					dq.pop();
					try {
						if(message instanceof RequestMessage){
							conn.writeMessage((RequestMessage)message, false);
						}else{
							conn.writeResponse((ResponseMessage)message);
						}
						
						logger.info("[writemessage] ["+message.getTypeDescription()+"] ["+(System.currentTimeMillis() - now)+"ms]");
					}catch (Exception e) {
						logger.error("[writemessage] [异常]",e);
						dq.push(message);
					}
				}else{
					try{
						Thread.sleep(1000L);
					}catch(Exception e){
						logger.error("[run] [异常]",e);
					}
				}
			}catch (Throwable e) {
				logger.error("心跳出错了:", e);
			}
		}
		

	}
	
	
	public ResponseMessage sendMessageAndWaittingResponse(RequestMessage req,long timeout) throws Exception {
		long now = System.currentTimeMillis();
		Connection conn = null;
		try {
			long takeoutTimeout = timeout;
			if(takeoutTimeout <= 0) takeoutTimeout = 6000;
			conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy,takeoutTimeout);
		} catch (Exception e) {
			if(logger != null)
				logger.warn("["+this+"] [sendMessageAndWaittingResponse] ["+selector.getHost()+"] ["+selector.getPort()+"] [FAIL] [Exception] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]",e);
			throw new Exception("takeoutConnection error cause " + e.getMessage());
		}
		
		if(conn == null){
			if(logger != null)
				logger.warn("["+this+"] [sendMessageAndWaittingResponse] ["+selector.getHost()+"] ["+selector.getPort()+"] [FAIL] [can't_takeout_conn] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return null;
		}
		try {
			conn.writeMessage(req, true);
		}catch(Exception e){
			if(logger != null)
				logger.warn("["+this+"] [sendMessageAndWaittingResponse] ["+selector.getHost()+"] ["+selector.getPort()+"] [FAIL] [Exception] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]",e);
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
			if(logger != null)
				logger.debug("["+this+"] [sendMessageAndWaittingResponse] ["+selector.getHost()+"] ["+selector.getPort()+"] [SUCC] [OK] ["+req.getTypeDescription()+"] ["+resp.getTypeDescription()+"] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return resp;
		} else {
			if(logger != null)
				logger.warn("["+this+"] [sendMessageAndWaittingResponse] ["+selector.getHost()+"] ["+selector.getPort()+"] [FAIL] [TIMEOUT] ["+req.getTypeDescription()+"] [--] [mapsize:"+responseMessageMap.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
			return null;
		}
	}

}
