package com.xuanzhi.tools.cache.lateral.concrete;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.lateral.LateralCacheManager;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.ACTIVE_TEST_REQ;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.ACTIVE_TEST_RES;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.GET_REQ;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.GET_RES;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.MODIFY_REQ;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.MODIFY_RES;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.PUT_REQ;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.PUT_RES;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.REMOVE_REQ;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.REMOVE_RES;
import com.xuanzhi.tools.cache.lateral.concrete.protocol.TFWServerAdapter;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class ServerAdapter extends TFWServerAdapter {

	protected static Logger logger = Logger.getLogger(ServerAdapter.class);
	
	LateralCacheManager cacheManager;
	
	protected int maxWindowSize = 16;
	protected long waitingResponseMessageTimeout = 30 * 1000L;
	protected long waitingRequestMessageTimeout = 180*1000L;
	protected int maxReSendTimes = 3;
	protected long sendBufferSize = 64 * 1024L;
	protected long receiveBufferSize = 64 * 1024L;
	
	protected int putNum = 0;
	protected int modifyNum = 0;
	protected int removeNum = 0;
	protected int getNum = 0;
	protected int getHitNum = 0;
	
	public int getGetHitNum() {
		return getHitNum;
	}

	public int getGetNum() {
		return getNum;
	}

	public int getModifyNum() {
		return modifyNum;
	}

	public int getPutNum() {
		return putNum;
	}

	public int getRemoveNum() {
		return removeNum;
	}

	public void setCacheManager(LateralCacheManager cm){
		cacheManager = cm;
	}
	
	public void init() throws Exception{
		
	}
	
	public void connected(Connection conn) throws IOException {
		super.connected(conn);
		conn.setMaxWindowSize(maxWindowSize);
		conn.setMaxReSendTimes(maxReSendTimes);
		conn.setSendBufferSize((int)sendBufferSize);
		conn.setReceiveBufferSize((int)receiveBufferSize);
		conn.setWaitingRequestMessageTimeout(waitingRequestMessageTimeout);
		conn.setWaitingResponseMessageTimeout(waitingResponseMessageTimeout);
	}
	
	public ResponseMessage receiveRequestMessage(Connection conn,
			RequestMessage message) throws ConnectionException {
		long startTime = System.currentTimeMillis();
		String remoteHost = ((InetSocketAddress)((SocketChannel)conn.getChannel()).socket().getRemoteSocketAddress()).getAddress().getHostAddress();
		
		if(message instanceof ACTIVE_TEST_REQ){
			ACTIVE_TEST_REQ req = (ACTIVE_TEST_REQ)message;
			ACTIVE_TEST_RES res = new ACTIVE_TEST_RES(req.getSequnceNum());
			if(logger.isDebugEnabled())
				logger.debug("[receive] [succ] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+(System.currentTimeMillis()-startTime)+"ms]");
			return res;
		}else if(message instanceof PUT_REQ){
			PUT_REQ req = (PUT_REQ)message;
			String cacheName = req.getCacheName();
			int model = req.getHandleModel();
			Object key = req.getKey();
			Object value = req.getValue();
			this.putNum ++;
			try{
				DefaultLateralCache cache = (DefaultLateralCache)cacheManager.getCache(cacheName);
				if(cache != null){
					if(model == DefaultLateralCache.STRONG_OVERWRITE_MODEL){
						if(value != null){
							cache.m_cache.put(key,(Cacheable)value);
							cache.m_refcache.remove(key);
						}
					}else if(model == DefaultLateralCache.STRONG_REFERENCE_MODEL){
						cache.m_cache.remove(key);
						cache.m_refcache.put(key,new DefaultLateralCache.ObjectReference(req.getIdentity()));
					}else if(model == DefaultLateralCache.WEAK_REFERENCE_MODEL){
						if(cache.m_cache.get(key) == null){
							cache.m_refcache.put(key,new DefaultLateralCache.ObjectReference(req.getIdentity()));
						}
					}
				}
				PUT_RES res = new PUT_RES(req.getSequnceNum(),"OK");
				if(logger.isDebugEnabled())
					logger.debug("[receive] [succ] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] ["+value+"]");
				return res;
			}catch(Throwable e){
				try {
					PUT_RES res = new PUT_RES(req.getSequnceNum(),e);
					logger.warn("[receive] [failed] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] ["+value+"]",e);
					return res;
				} catch (Exception e1) {
					logger.error("[receive] [error] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] [-] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] ["+value+"]",e1);
					throw new ConnectionException("create PUT_RES error: " + e1);
				}
			}
		}else if(message instanceof MODIFY_REQ){
			MODIFY_REQ req = (MODIFY_REQ)message;
			String cacheName = req.getCacheName();
			int model = req.getHandleModel();
			Object key = req.getKey();
			Object value = req.getValue();
			this.modifyNum ++;
			try{
				DefaultLateralCache cache = (DefaultLateralCache)cacheManager.getCache(cacheName);
				if(cache != null){
					if(model == DefaultLateralCache.STRONG_OVERWRITE_MODEL
							|| model == DefaultLateralCache.WEAK_OVERWRITE_MODEL){
						if(value != null){
							cache.m_cache.put(key,(Cacheable)value);
							cache.m_refcache.remove(key);
						}
					}else if(model == DefaultLateralCache.STRONG_REFERENCE_MODEL
							|| model == DefaultLateralCache.WEAK_REFERENCE_MODEL){
						cache.m_cache.remove(key);
						cache.m_refcache.put(key,new DefaultLateralCache.ObjectReference(req.getIdentity()));
					}else if(model == DefaultLateralCache.RELOAD_MODEL){
						cache.m_cache.remove(key);
						cache.m_refcache.remove(key);
					}
				}
				MODIFY_RES res = new MODIFY_RES(req.getSequnceNum(),"OK");
				if(logger.isDebugEnabled())
					logger.debug("[receive] [succ] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] ["+value+"]");
				return res;
			}catch(Throwable e){
				try {
					MODIFY_RES res = new MODIFY_RES(req.getSequnceNum(),e);
					logger.warn("[receive] [failed] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] ["+value+"]",e);
					return res;
				} catch (Exception e1) {
					logger.error("[receive] [error] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] [-] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] ["+value+"]",e1);
					throw new ConnectionException("create PUT_RES error: " + e1);
				}
			}
		}else if(message instanceof REMOVE_REQ){
			REMOVE_REQ req = (REMOVE_REQ)message;
			String cacheName = req.getCacheName();
			int model = req.getHandleModel();
			Object key = req.getKey();
			this.removeNum++;
			try{
				DefaultLateralCache cache = (DefaultLateralCache)cacheManager.getCache(cacheName);
				if(cache != null){
					cache.m_cache.remove(key);
					cache.m_refcache.remove(key);
				}
				REMOVE_RES res = new REMOVE_RES(req.getSequnceNum(),"OK");
				if(logger.isDebugEnabled())
					logger.debug("[receive] [succ] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] [-]");
				return res;
			}catch(Throwable e){
				try {
					REMOVE_RES res = new REMOVE_RES(req.getSequnceNum(),e);
					logger.warn("[receive] [failed] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] [-]",e);
					return res;
				} catch (Exception e1) {
					logger.error("[receive] [error] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] [-] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] [-]",e1);
					throw new ConnectionException("create PUT_RES error: " + e1);
				}
			}
		}else if(message instanceof GET_REQ){
			GET_REQ req = (GET_REQ)message;
			String cacheName = req.getCacheName();
			int model = req.getHandleModel();
			Object key = req.getKey();
			Object value = null;
			this.getNum++;
			try{
				DefaultLateralCache cache = (DefaultLateralCache)cacheManager.getCache(cacheName);
				if(cache != null){
					value = cache.m_cache.get(key);
					if(value != null){
						this.getHitNum++;
					}
				}
				GET_RES res = new GET_RES(req.getSequnceNum(),cacheName,model,key,value);
				if(logger.isDebugEnabled())
					logger.debug("[receive] [succ] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] ["+value+"]");
				return res;
			}catch(Throwable e){
				try {
					GET_RES res = new GET_RES(req.getSequnceNum(),cacheName,model,null,null);
					logger.warn("[receive] [failed] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] ["+res.getTypeDescription()+"] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] [-]",e);
					return res;
				} catch (Exception e1) {
					logger.error("[receive] [error] ["+remoteHost+"] ["+conn.getIdentity()+"] ["+req.getTypeDescription()+"] [-] ["+(System.currentTimeMillis()-startTime)+"ms] -- ["+cacheName+"] ["+DefaultLateralCache.getStringOfModel(model)+"] ["+key+"] [-]",e1);
					throw new ConnectionException("create PUT_RES error: " + e1);
				}
			}
		}
		
		return null;
	}

}
