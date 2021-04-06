package com.xuanzhi.tools.cache.lateral.concrete.protocol;

import java.io.IOException;
import java.util.Hashtable;

import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionCreatedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.ConnectionSelector;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public abstract class TFWClientAdapter implements ConnectionCreatedHandler,MessageHandler{

	protected ConnectionSelector selector;
	protected boolean privateModel = false;
	
	protected Hashtable<RequestMessage,ResponseMessage> messageMap = new Hashtable<RequestMessage,ResponseMessage>();
	
	protected TFWClientAdapter(){}
	
	protected TFWClientAdapter(String host,int port,int minConnectionNum,int maxConnectionNum) throws Exception{
		privateModel = true;
		DefaultConnectionSelector ds = new DefaultConnectionSelector(host,port,minConnectionNum,maxConnectionNum);
		ds.setConnectionCreatedHandler(this);
		ds.init();
		selector = ds;
	}
	
	public void close(){
		if(privateModel){
			try {
				((DefaultConnectionSelector)selector).destory();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public ConnectionSelector getConnectionSelector(){
		return selector;
	}
	
	public void setConnectionSelector(ConnectionSelector selector){
		this.selector = selector;
		((DefaultConnectionSelector)selector).setConnectionCreatedHandler(this);
	}
	
	public void created(Connection conn, String attachment) throws IOException {
		conn.setMessageFactory(TFWMessageFactory.getInstance());
		conn.setMessageHandler(this);
	}

	public void discardRequestMessage(Connection conn, RequestMessage req) throws ConnectionException {
		synchronized(req){
			req.notify();
		}
	}

	public void receiveResponseMessage(Connection conn, RequestMessage req, ResponseMessage res) throws ConnectionException {
		if(res instanceof ACTIVE_TEST_RES){
			//noop
		}else if(req != null){
			messageMap.put(req,res);
			synchronized(req){
				req.notify();
			}
		}
	}

	public RequestMessage waitingTimeout(Connection conn, long timeout) throws ConnectionException {
		return new ACTIVE_TEST_REQ(TFWMessageFactory.nextSequnceNum());
	}

	public ResponseMessage receiveRequestMessage(Connection conn, RequestMessage message) throws ConnectionException {
		if(message instanceof ACTIVE_TEST_REQ){
			ACTIVE_TEST_REQ req = (ACTIVE_TEST_REQ)message;
			ACTIVE_TEST_RES res = new ACTIVE_TEST_RES(req.getSequnceNum());
			return res;
		}
		return null;
	}


}
