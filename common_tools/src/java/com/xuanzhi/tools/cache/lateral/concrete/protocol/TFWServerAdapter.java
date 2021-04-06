package com.xuanzhi.tools.cache.lateral.concrete.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;

import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionConnectedHandler;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.ConnectionSelector;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;

import com.xuanzhi.tools.transport.MessageHandler;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public abstract class TFWServerAdapter implements ConnectionConnectedHandler,MessageHandler{


	protected ConnectionSelector selector;
	
	protected Hashtable<RequestMessage,ResponseMessage> messageMap = new Hashtable<RequestMessage,ResponseMessage>();
	
	protected TFWServerAdapter(){}
	
	protected String [] m_ipAllows = null;
    
    public void setIpAllows(String allows) {
        m_ipAllows = allows.split("[ ,;]+");
    }
    
    /**
     * 判断来取消息的机器，是否在允许的范围内
     * @param remoteHost
     * @return
     */
    public boolean isIpAllows(String remoteHost) {

        if (m_ipAllows == null)
            return true;

        for (int i = 0; i < m_ipAllows.length; i++) {
                if (remoteHost.matches(m_ipAllows[i]))
                    return true;
        }
        return false;
    }
    
	public ConnectionSelector getConnectionSelector(){
		return selector;
	}
	
	public void setConnectionSelector(ConnectionSelector selector){
		this.selector = selector;
		((DefaultConnectionSelector)selector).setConnectionConnectedHandler(this);
	}
	
	public void connected(Connection conn) throws IOException {
		if(m_ipAllows != null){
			ByteChannel channel = conn.getChannel();
			if(channel instanceof SocketChannel){
				InetSocketAddress isa = (InetSocketAddress)((SocketChannel)channel).socket().getRemoteSocketAddress();
				String remoteHost = isa.getAddress().getHostAddress();
				if(isIpAllows(remoteHost) == false){
					throw new IOException("invalid client ip ["+remoteHost+"]");
				}
			}else{
				throw new IOException("invalid channel type");
			}
		}
		
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

}
