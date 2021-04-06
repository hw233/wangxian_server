package com.xuanzhi.tools.cache.lateral.concrete;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.lateral.LateralCache;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.ACTIVE_TEST_RES;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.GET_REQ;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.GET_RES;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.MODIFY_REQ;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.MODIFY_RES;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.PUT_REQ;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.PUT_RES;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.REMOVE_REQ;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.REMOVE_RES;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.TFWClientAdapter;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.TFWMessageFactory;
import com.xuanzhi.tools.queue.DefaultQueue;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;
import com.xuanzhi.tools.transport.SelectorPolicy;

public class ClientAdapter extends TFWClientAdapter {

	protected static Logger logger = Logger.getLogger(ClientAdapter.class);
	
	protected HashMap<String,InetSocketAddress> addressMap;
	protected String remotes[] = null;
	protected long timeout = 1000L;
	
	protected DefaultQueue putQueue;
	protected DefaultQueue modifyQueue;
	protected DefaultQueue removeQueue;
	RemoveSendThread rt;
	PutSendThread pt;
	ModifySendThread mt;
	protected int queueSize = 4096;
	
	boolean running = true;
	String identity = null;
	
	protected int maxWindowSize = 16;
	protected long waitingResponseMessageTimeout = 30 * 1000L;
	protected long waitingRequestMessageTimeout = 180*1000L;
	protected int maxReSendTimes = 3;
	protected long sendBufferSize = 64 * 1024L;
	protected long receiveBufferSize = 64 * 1024L;
	
	protected int getNum = 0;
	protected int getHitNum = 0;
	
	public void created(Connection conn, String attachment) throws IOException {
		super.created(conn,attachment);
		conn.setMaxWindowSize(maxWindowSize);
		conn.setMaxReSendTimes(maxReSendTimes);
		conn.setSendBufferSize((int)sendBufferSize);
		conn.setReceiveBufferSize((int)receiveBufferSize);
		conn.setWaitingRequestMessageTimeout(waitingRequestMessageTimeout);
		conn.setWaitingResponseMessageTimeout(waitingResponseMessageTimeout);
	}
	
	/**
	 * map的格式是：
	 * 		identity  -->  host:port
	 * @param map
	 */
	public void setAddressMap(HashMap<String,String> map){
		this.addressMap = new HashMap<String,InetSocketAddress>();
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String id = it.next();
			String s = map.get(id);
			String ss[] = s.split(":");
			this.addressMap.put(id,new InetSocketAddress(ss[0],Integer.parseInt(ss[1])));
		}
		remotes = map.keySet().toArray(new String[0]);
	}
	
	public void setIdentity(String identity){
		this.identity = identity;
	}
	
	public void init(){
		putQueue = new DefaultQueue(queueSize);
		modifyQueue = new DefaultQueue(queueSize);
		removeQueue = new DefaultQueue(queueSize);
		
		running =  true;
		
		pt = new PutSendThread("Put-Send-Thread");
		pt.start();
		
		rt = new RemoveSendThread("Remove-Send-Thread");
		rt.start();
		
		mt = new ModifySendThread("Modify-Send-Thread");
		mt.start();
		
	}
	
	public void close(){
		super.close();
		running = false;
		if(pt != null) pt.interrupt();
		if(rt != null) rt.interrupt();
		if(mt != null) mt.interrupt();
		
	}
	
	public void sendPut(String remote,LateralCache cache,Object key,Object value) throws Exception{
		InetSocketAddress sa = addressMap.get(remote);
		if(sa == null) {
			throw new Exception("remote ["+remote+"] is invalid");
		}
		Connection conn = null;
		conn = selector.takeoutConnection(new SelectorPolicy.IdentitySelectorAndCreatePolicy(remote,sa.getAddress().getHostAddress(),sa.getPort()),timeout);
		if(conn.getIdentity() == null) conn.setIdentity(remote);
		if(conn == null){
			throw new Exception("no avaiable connection");
		}else{
			PUT_REQ req = null;
			if(cache.getHandleModel() == LateralCache.STRONG_OVERWRITE_MODEL){
				req = new PUT_REQ(TFWMessageFactory.nextSequnceNum(),identity,cache.getName(),cache.getHandleModel(),key,value);
			}else{
				req = new PUT_REQ(TFWMessageFactory.nextSequnceNum(),identity,cache.getName(),cache.getHandleModel(),key,null);
			}
			try{
				conn.writeMessage(req);
			}catch(IOException e){
				conn.close();
				throw e;
			}
		}
	}
	
	public void sendPut(LateralCache cache,Object key,Object value){
		if(cache.getHandleModel() == LateralCache.NOTHING_MODEL
				|| cache.getHandleModel() == LateralCache.RELOAD_MODEL
				|| cache.getHandleModel() == LateralCache.WEAK_OVERWRITE_MODEL
				){
			return;
		}
		putQueue.push(new Object[]{cache,key,value});
		
	
	}
	
	public void sendModify(String remote,LateralCache cache,Object key,Object value) throws Exception{
		InetSocketAddress sa = addressMap.get(remote);
		if(sa == null) throw new Exception("remote ["+remote+"] is invalid");
		Connection conn = null;
		conn = selector.takeoutConnection(new SelectorPolicy.IdentitySelectorAndCreatePolicy(remote,sa.getAddress().getHostAddress(),sa.getPort()),timeout);
		if(conn.getIdentity() == null) conn.setIdentity(remote);
		if(conn == null){
			throw new Exception("no avaiable connection");
		}else{
			MODIFY_REQ req = null;
			if(cache.getHandleModel() == LateralCache.STRONG_OVERWRITE_MODEL
					|| cache.getHandleModel() == LateralCache.WEAK_OVERWRITE_MODEL){
				req = new MODIFY_REQ(TFWMessageFactory.nextSequnceNum(),identity,cache.getName(),cache.getHandleModel(),key,value);
			}else{
				req = new MODIFY_REQ(TFWMessageFactory.nextSequnceNum(),identity,cache.getName(),cache.getHandleModel(),key,null);
			}
			try{
				conn.writeMessage(req);
			}catch(IOException e){
				conn.close();
				throw e;
			}
		}
	}
	
	public void sendModify(LateralCache cache,Object key,Object value){
		if(cache.getHandleModel() == LateralCache.NOTHING_MODEL){
			return;
		}
		modifyQueue.push(new Object[]{cache,key,value});
	}
	
	public void sendRemove(String remote,LateralCache cache,Object key) throws Exception{
		InetSocketAddress sa = addressMap.get(remote);
		if(sa == null) throw new Exception("remote ["+remote+"] is invalid");
		Connection conn = null;
		conn = selector.takeoutConnection(new SelectorPolicy.IdentitySelectorAndCreatePolicy(remote,sa.getAddress().getHostAddress(),sa.getPort()),timeout);
		if(conn.getIdentity() == null) conn.setIdentity(remote);
		if(conn == null){
			throw new Exception("no avaiable connection");
		}else{
			REMOVE_REQ req = new REMOVE_REQ(TFWMessageFactory.nextSequnceNum(),cache.getName(),cache.getHandleModel(),key);
			try{
				conn.writeMessage(req);
			}catch(IOException e){
				conn.close();
				throw e;
			}
		}
	}
	
	public void sendRemove(LateralCache cache,Object key){
		if(cache.getHandleModel() == LateralCache.NOTHING_MODEL){
			return;
		}
		removeQueue.push(new Object[]{cache,key});

	}
	
	public Object getObjectFromRemoteCache(String remote,LateralCache cache,Object key) {
		long startTime = System.currentTimeMillis();
		InetSocketAddress sa = addressMap.get(remote);
		try{
			if(sa == null) throw new Exception("remote ["+remote+"] is invalid");
			Connection conn = null;
			conn = selector.takeoutConnection(new SelectorPolicy.IdentitySelectorAndCreatePolicy(remote,sa.getAddress().getHostAddress(),sa.getPort()),timeout);
			if(conn.getIdentity() == null) conn.setIdentity(remote);
			if(conn == null){
				throw new Exception("no avaiable connection");
			}else{
				GET_REQ req = new GET_REQ(TFWMessageFactory.nextSequnceNum(),cache.getName(),cache.getHandleModel(),key);
				try{
					conn.writeMessage(req);
				}catch(IOException e){
					conn.close();
					throw e;
				}
				this.getNum++;
				long endTime = System.currentTimeMillis() + timeout;
				try{
					synchronized(req){
						req.wait(timeout);
					}
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				
				GET_RES res = (GET_RES)messageMap.remove(req);
				if(res != null){
					Object ret = res.getValue();
					if(logger.isDebugEnabled()){
						logger.debug("[send] [getobject] [success] [-] ["+remote+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cache.getName()+"] ["+key+"] ["+ret+"]");
					}
					if(ret != null)
						this.getHitNum++;
					
					return ret;
				}else{
					if(System.currentTimeMillis() > endTime){
						throw new Exception("timeout ["+timeout+"ms] for waiting reply of sending message");
					}else{
						throw new Exception("no reply of sending message");
					}
				}
			}
		}catch(Throwable e){
			logger.warn("[send] [getobject] [failed] [-] ["+remote+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cache.getName()+"] ["+key+"] [-]",e);
			return null;
		}
	}
	
	public void receiveResponseMessage(Connection conn, RequestMessage req, ResponseMessage res) throws ConnectionException {
		long startTime = System.currentTimeMillis();
		String cacheName = null;
		Object key = null;
		Object value = null;
		if(res instanceof ACTIVE_TEST_RES){
			//noop
		}else if(res instanceof PUT_RES){
			PUT_REQ pq = (PUT_REQ)req;
			PUT_RES pr = (PUT_RES)res;
			if(pq != null){
				key = pq.getKey();
				value = pq.getValue();
				cacheName = pq.getCacheName();
			}
			if(pr.isSuccess()){
				if(logger.isDebugEnabled()){
					logger.debug("[send] [put] [success] ["+putQueue.size()+"] ["+conn.getIdentity()+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cacheName+"] ["+key+"] ["+value+"]");
				}
			}else{
				if(logger.isDebugEnabled()){
					logger.debug("[send] [put] [failed] ["+putQueue.size()+"] ["+conn.getIdentity()+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cacheName+"] ["+key+"] ["+value+"]",pr.getException());
				}
			}
		}else if(res instanceof MODIFY_RES){
			MODIFY_REQ pq = (MODIFY_REQ)req;
			MODIFY_RES pr = (MODIFY_RES)res;
			
			if(pq != null){
				key = pq.getKey();
				value = pq.getValue();
				cacheName = pq.getCacheName();
			}
			if(pr.isSuccess()){
				if(logger.isDebugEnabled()){
					logger.debug("[send] [modify] [success] ["+modifyQueue.size()+"] ["+conn.getIdentity()+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cacheName+"] ["+key+"] ["+value+"]");
				}
			}else{
				if(logger.isDebugEnabled()){
					logger.debug("[send] [modify] [failed] ["+modifyQueue.size()+"] ["+conn.getIdentity()+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cacheName+"] ["+key+"] ["+value+"]",pr.getException());
				}
			}
		}else if(res instanceof REMOVE_RES){
			REMOVE_REQ pq = (REMOVE_REQ)req;
			REMOVE_RES pr = (REMOVE_RES)res;
			
			if(pq != null){
				key = pq.getKey();
				cacheName = pq.getCacheName();
			}
			if(pr.isSuccess()){
				if(logger.isDebugEnabled()){
					logger.debug("[send] [remove] [success] ["+removeQueue.size()+"] ["+conn.getIdentity()+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cacheName+"] ["+key+"] ["+value+"]");
				}
			}else{
				if(logger.isDebugEnabled()){
					logger.debug("[send] [remove] [failed] ["+removeQueue.size()+"] ["+conn.getIdentity()+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cacheName+"] ["+key+"] ["+value+"]",pr.getException());
				}
			}
		}else if(res instanceof GET_RES){
			messageMap.put(req,res);
			synchronized(req){
				req.notify();
			}
		}else{
			
		}
	}
	
	class PutSendThread extends Thread{
		PutSendThread(String name){
			super(name);
		}
		public void run(){
			while(running){
				Object o[] = (Object[])putQueue.pop(1000L);
				if(o == null) continue;
				for(int i = 0 ; i < remotes.length ; i++){
					long startTime = System.currentTimeMillis();
					LateralCache cache = (LateralCache)o[0];
					try {
						sendPut(remotes[i],cache,o[1],o[2]);
					} catch (Exception e) {
						logger.warn("[send] [put] [failed] ["+putQueue.size()+"] ["+remotes[i]+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cache.getName()+"] ["+o[1]+"] ["+o[2]+"]",e);
					}
				}
			}
		}
	}
	
	class ModifySendThread extends Thread{
		ModifySendThread(String name){
			super(name);
		}
		public void run(){
			while(running){
				Object o[] = (Object[])modifyQueue.pop(1000L);
				if(o == null) continue;
				for(int i = 0 ; i < remotes.length ; i++){
					long startTime = System.currentTimeMillis();
					LateralCache cache = (LateralCache)o[0];
					try {
						sendModify(remotes[i],cache,o[1],o[2]);
					} catch (Exception e) {
						logger.warn("[send] [modify] [failed] ["+modifyQueue.size()+"] ["+remotes[i]+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cache.getName()+"] ["+o[1]+"] ["+o[2]+"]",e);
					}
				}
			}
		}
	}
	
	class RemoveSendThread extends Thread{
		RemoveSendThread(String name){
			super(name);
		}
		public void run(){
			while(running){
				Object o[] = (Object[])removeQueue.pop(1000L);
				if(o == null) continue;
				for(int i = 0 ; i < remotes.length ; i++){
					long startTime = System.currentTimeMillis();
					LateralCache cache = (LateralCache)o[0];
					try {
						sendRemove(remotes[i],cache,o[1]);
					} catch (Exception e) {
						logger.warn("[send] [remove] [failed] ["+removeQueue.size()+"] ["+remotes[i]+"] ["+(System.currentTimeMillis()- startTime)+"ms] -- ["+cache.getName()+"] ["+o[1]+"] [-]",e);
					}
				}
			}
		}
	}

	public int getGetNum(){
		return this.getNum;
	}
	
	public int getGetHitNum(){
		return this.getHitNum;
	}
	
	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public HashMap<String, InetSocketAddress> getAddressMap() {
		return addressMap;
	}

	public DefaultQueue getModifyQueue() {
		return modifyQueue;
	}

	public DefaultQueue getPutQueue() {
		return putQueue;
	}

	public DefaultQueue getRemoveQueue() {
		return removeQueue;
	}
}
