package com.xuanzhi.tools.transport2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.queue.DefaultQueue2;

/**
 * <pre>
 * 链接管理器
 * 此链接管理器，有两种模式：
 * 客户端模式：
 * 		所有的链接都是由本地发起，链接远程服务器，远程服务器只能是一个服务器。
 * 
 * 服务器模式：
 * 		所有的链接都是有远端发起，链接本地地址，本地有一个SocketServer在监听。
 *      远端客户端可以是多个地址。
 *      
 * 2014-01-28:
 * 		DefaultConnectionSelector第一版从2007年开始构建，资金已有7年，7年的今天，我对DefaultConnectionSelector进行了改进。
 * 
 * 		新版的DefaultConnectionSelector2与旧版的DefaultConnectionSelector相比，做了如下的改进：
 * 
 * 		1. 取消了Connection上链接大多数状态，只保留了链接[CONNECT]和关闭[CLOSE]两个状态
 * 		2. 将链接的读取数据和发送数据，完全独立开。即在读取数据时，可以随时发送数据。
 * 		3. 读取数据仍然有系统线程池中的线程读取，会保证同一时间只有一个线程读取。
 * 		4. 发送数据时，可以有多个线程同时发送。也无需获取链接后在发送，只有有链接的对象，即可调用发送发放。
 * 
 * 		这些改进，理论上使得LAMP更加的高效，更加的易用。
 * 		但实际效果如何，还需要进一步验证。
 * 
 *  	我会先用先用足球的项目进行验证。
 *  
 *  	
 * </pre>		
 */
public class DefaultConnectionSelector2 implements Runnable{

	protected static Logger logger = LoggerFactory.getLogger(DefaultConnectionSelector2.class);
	
	private final ReentrantLock threadPoolLock = new ReentrantLock();
	
	private final Condition threadPoolFull = threadPoolLock.newCondition();
	private int threadPoolFullWaitingCount = 0;
	
	private final ReentrantLock takeConnectionLock = new ReentrantLock();
	
	private final Condition takeConnectionCondition = takeConnectionLock.newCondition();
	private int takeConnectionConditionCount = 0;
	
	/**
	 * 无上限的线程池
	 */
	
	protected ThreadPoolExecutor threadPool = null;//new ThreadPoolExecutor(1,Integer.MAX_VALUE,30,TimeUnit.SECONDS,new SynchronousQueue());

	protected int threadPoolCorePoolSize = 1;
	
	protected int threadPoolMaxPoolSize = 512;
	//秒
	protected int threadPoolKeepAlive = 30;
	

	protected int sendDataThreadNum = 4;
	
	/**
	 * 设置将各个链接中的发送缓冲区的数据，发送到网络层的线程数目
	 * 默认为4
	 * @param n
	 */
	public void setSendDataThreadNum(int n){
		this.sendDataThreadNum = n;
	}
	
	protected Selector selector;
	protected Thread thread = null;
	
	protected Thread sendDatathreads[] = null;
	protected NotifyQueueCheckThreadForWaitting notifyQueueCheckThreadForWaitting = null;
	
	protected String name = "DefaultConnectionSelector2";
	protected String attachment = null;

	//单线程的模式
	protected boolean singleThreadMode = false;
	

	public void setName(String s){
		name = s;
		if( attachment == null)
				attachment= name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setAttachment(String s){
		attachment = s;
	}
	
	public String getAttachment(){
		return attachment;
	}
	/**
	 * 新的客户端连接后，调用此接口处理新来的链接
	 */
	protected ConnectionConnectedHandler2 newClientConnectHandler;
	
	public void setConnectionConnectedHandler(ConnectionConnectedHandler2 handler){
		newClientConnectHandler = handler;
	}
	
	/**
	 * 当创建一个新的链接连接到服务器上后，调用此方法
	 */
	protected ConnectionCreatedHandler2 connectionCreatedHandler;
	
	public void setConnectionCreatedHandler(ConnectionCreatedHandler2 handler){
		connectionCreatedHandler = handler;
	}
	/**
	 * 当一个链接关闭的时候，调用此接口
	 */
	protected ConnectionTerminateHandler2 terminateHangler;
	
	public void setConnectionTerminateHandler(ConnectionTerminateHandler2 handler){
		terminateHangler = handler;
	}
	
	/**
	 * 当为Client模式时，对某一个远端SocketServer，最小链接数，当链接小于这个数时，系统自动创建新的链接。
	 * 当为Server模式时，此参数无意义。
	 */
	protected int minConnectionNum = 0;
	
	/**
	 * 对Client模式，对某一个远端SocketServer，最大链接数，系统最多不能超过这么多的链接。
	 * 当为Server模式时，此参数无意义。
	 */
	protected int maxConnectionNum = 16;
	
	/**
	 * 当为client模式，host为远程服务器的IP地址
	 * 当为Server模式，host为bind本地的地址，可以为null，标识bind在任意地址上。
	 */
	protected String host = null;
	
	/**
	 * 当为client模式，host为远程服务器的IP地址
	 * 当为Server模式，host为bind本地的地址，可以为null，标识bind在任意地址上。
	 */
	protected int port = 0;
	
	/**
	 * 服务器模式，还是客户端模式
	 */
	protected boolean clientModel = true;
	
	/**
	 * 作为client模式运行时，socket绑定的本地ip，null标识任意ip 
	 * 一台机器上有多个ip时，可能会用到这参数
	 */
	protected String bindLocalHost = null;
	
	/**
	 * 作为client模式运行时，socket绑定的本地port，0标识任意端口。
	 */
	protected int bindLocalPort = 0;
	
	/**
	 * 连接超时，当需要创建新的连接时，调用socket的connect方法中的超时参数。
	 */
	protected long connectTimeout = 3000L;
	
	/**
	 * 链接最大的空闲时间，超过这个时间，链接将关闭
	 */
	protected long maxIdelTime = 10 * 60 * 1000L;
	
	/**
	 * 接收数据超时，长时间没有读到任何数据，将调用超时接口
	 */
	protected long maxReceiveDataTimeout = 3 * 60 * 1000L;
	
	public void setMaxReceiveDataTimeout(long maxReceiveDataTimeout){
		this.maxReceiveDataTimeout = maxReceiveDataTimeout;
	}
	
	public long getMaxReceiveDataTimeout(){
		return maxReceiveDataTimeout;
	}

	
	protected int connectionSendBufferSize = 64 * 1024;
	protected int connectionReceiveBufferSize = 64 * 1024;
	
	
	//统计信息
	protected java.util.Date createTime = new java.util.Date();
	protected long lastStatNumAndPacketTime = 0;
	protected long closedConnectionSendMessageNum = 0;
	protected long closedConnectionReceiveMessageNum = 0;
	protected long closedConnectionSendMessagePacketSize = 0;
	protected long closedConnectionReceiveMessagePacketSize = 0;
	
	protected long totalSendMessageNum = 0;
	protected long totalReceiveMessageNum = 0;
	protected long totalSendMessagePacketSize = 0;
	protected long totalReceiveMessagePacketSize = 0;
	
	
	
	//新创建的链接，准备加入到selector中
	protected DefaultQueue2 newConnQueue = null;
	
	//准备关闭的链接
	protected DefaultQueue2 closingConnQueue = null;
	
	//记录正在创建的链接，即某个链接被new出来，调用connected方法或者created方法过程中的链接，
	//此链接当放入到selector中，将从此列表中移除
	protected List<Connection2> newingConnectionList = Collections.synchronizedList(new LinkedList<Connection2>());


	public DefaultConnectionSelector2(){
	}
	

	public void setClientModel(boolean b){
		clientModel = b;
	}
	
	public boolean isClientModel(){
		return clientModel;
	}
	
	public void setHost(String s){
		if(s != null && s.trim().length() > 0)
			host = s;
	}
	
	public String getHost(){
		return host;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public int getPort(){
		return port;
	}
	
	public void setMinConnectionNum(int n){
		minConnectionNum = n;
	}
	
	public int getMinConnectionNum(){
		return this.minConnectionNum;
	}
	
	public int getMaxConnectionNum(){
		return this.maxConnectionNum;
	}
	
	public void setMaxConnectionNum(int n){
		maxConnectionNum = n;
	}
	
	public long getConnectTimeout(){
		return connectTimeout;
	}

	public void setConnectTimeout(long timeout){
		connectTimeout = timeout;
	}

	public long getMaxIdelTime() {
		return maxIdelTime;
	}

	public void setMaxIdelTime(long maxIdelTime) {
		this.maxIdelTime = maxIdelTime;
	}
	
	/**
	 * 服务器模式，相当于监听指定地址，端口上的Socket连接。
	 * @param host
	 * @param port
	 */
	public DefaultConnectionSelector2(String host,int port,boolean clientModel){
		this.clientModel = clientModel;
		this.host = host;
		this.port = port;
		this.minConnectionNum = 0;
		this.maxConnectionNum = Integer.MAX_VALUE;
	}

	
	private ServerSocketChannel ssc;
	
	public void init() throws Exception{
		
		threadPool = new ThreadPoolExecutor(this.threadPoolCorePoolSize,
				this.threadPoolMaxPoolSize, this.threadPoolKeepAlive, TimeUnit.SECONDS, new SynchronousQueue(),
				new ThreadPoolExecutor.AbortPolicy());
		
		
		newConnQueue = new DefaultQueue2();
		closingConnQueue = new DefaultQueue2();
		
		selector = Selector.open();
		if(clientModel == false){
			if(this.newClientConnectHandler == null)
				throw new Exception("ConnectHandler is null");
			
			
			InetSocketAddress address = null;
			if(host != null)
				address = new InetSocketAddress(host,port);
			else
				address = new InetSocketAddress(port);
				
			try {
				ssc = ServerSocketChannel.open();
				ssc.socket().bind(address,5);
				ssc.configureBlocking(false);
					
				ssc.register(selector,SelectionKey.OP_ACCEPT);
			} catch(Exception e) {
				System.out.println("DefaultConnectionSelector["+host+":"+port+"] init failed for bind error");
				throw e;
			}
				
			logger.info("["+name+"] [bind_success] ["+host==null?"*":host+"] ["+port+"]");
		}
		
		thread = new Thread(this,name + "_Thread");
		thread.start();
		
		this.notifyQueueCheckThreadForWaitting = new NotifyQueueCheckThreadForWaitting(this);
		notifyQueueCheckThreadForWaitting.start();
		
		NotifyQueueCheckThreadForBuffer notifyQueueCheckThreadForBuffer = new NotifyQueueCheckThreadForBuffer(this);
		sendDatathreads = new Thread[sendDataThreadNum];
		for(int i = 0 ; i < this.sendDataThreadNum ; i++){
			this.sendDatathreads[i] = new Thread(notifyQueueCheckThreadForBuffer,"NotifyQueueCheckThreadForBuffer-Thread-"+(i+1));
			this.sendDatathreads[i].start();
		}
		
		ConnectionSelectorHelper2.selectorMap.put(this.getName(), this);
	}
	
	
	public void destory() throws Exception{
		
		thread.interrupt();
		notifyQueueCheckThreadForWaitting.interrupt();
		for(int i = 0 ; i < this.sendDataThreadNum ; i++){
			this.sendDatathreads[i].interrupt();
		}
		SelectionKey keys[] = selector.keys().toArray(new SelectionKey[0]);
		
		for(int i = 0 ; i < keys.length ; i++){
			SelectionKey sk = keys[i];
			if(sk != null && sk.attachment() != null && sk.attachment() instanceof Connection2){
				Connection2 conn = (Connection2)sk.attachment();
				conn.forceTerminate("销毁Selector，关闭连接");
			}
		}
		if(ssc != null){
			ssc.socket().close();
		}
		selector.close();
	}
	
	public String getStatusString(){
		int poolThread = threadPool.getPoolSize();
		int size = selector.keys().size() -1;
		return "{poolThread:"+poolThread+",totalConn:"+size+"}";
	}
	
	
	/**
	 * 一个链接要返回到selector中，新的operation要与原有的合并
	 */
	private void returnConnectoin(Connection2 conn, int operation) {
	
		if(conn.getState() == Connection2.CONN_STATE_CLOSE){
			logger.error("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] can't return to selector cause its state is CLOSE.");
			throw new  IllegalArgumentException("connection can't return to selector cause its state is CLOSE");
		}
		
		SelectableChannel channel = (SelectableChannel)conn.channel;
		if(channel.isBlocking()){
			throw new IllegalArgumentException("the connection must be non-blocking model");
		}

		
		SelectionKey sk = channel.keyFor(selector);
		if(sk == null){
			newConnQueue.push(new Object[]{conn,operation});
			newingConnectionList.remove(conn);
			selector.wakeup();
			
			if(logger.isDebugEnabled()){
				String operationStr = "";
				if((operation & SelectionKey.OP_READ) != 0){
					operationStr = "OP_READ";
				}
				if((operation & SelectionKey.OP_WRITE) != 0){
					if(operationStr.length() == 0){
						operationStr = "OP_WRITE";
					}else{
						operationStr = operationStr + "|OP_WRITE";
					}
				}
				logger.debug("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] return to selector"+getStatusString()+" and push to newConnQueue with operation="+operationStr+"");
			}
			
		}else{
			int interestOps = sk.interestOps();
			int newInterestOps = interestOps | operation;
			if(newInterestOps != interestOps){
				sk.interestOps(newInterestOps);
				sk.attach(conn);
				conn.selector_operation = newInterestOps;
				selector.wakeup();
			}
			conn.selector_operation = newInterestOps;
			
			
			if(logger.isDebugEnabled()){
				
				String operationStr = "";
				if((newInterestOps & SelectionKey.OP_READ) != 0){
					operationStr = "OP_READ";
				}
				if((newInterestOps & SelectionKey.OP_WRITE) != 0){
					if(operationStr.length() == 0){
						operationStr = "OP_WRITE";
					}else{
						operationStr = operationStr + "|OP_WRITE";
					}
				}
				
				logger.debug("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] return to selector"+getStatusString()+" with newInterestOps="+operationStr+"");
			}
		}
		
			
		
		
	}

	protected void notifyThreadReturnToThreadPool(){
		if(threadPoolFullWaitingCount > 0){
			threadPoolLock.lock();
			try{
				threadPoolFull.signal();
			}finally{
				threadPoolLock.unlock();
			}
		}
	}

	protected void notifyTakeConnectionCondition(){
		if(takeConnectionConditionCount > 0){
			this.takeConnectionLock.lock();
			try{
				takeConnectionCondition.signal();
			}finally{
				takeConnectionLock.unlock();
			}
		}
	}
	
	protected static final int NOTIFY_TYPE_WAITTINGQUEUE = 1;
	protected static final int NOTIFY_TYPE_SENDBUFFER = 2;
	

	DefaultQueue2 notifyQueueForWaitting = new DefaultQueue2();
	DefaultQueue2 notifyQueueForBuffer = new DefaultQueue2();
	
	
	protected void notify(Connection2 conn,int type){
		if(type == NOTIFY_TYPE_SENDBUFFER){
			notifyQueueForBuffer.push(conn);
		}else if(type == NOTIFY_TYPE_WAITTINGQUEUE){
			notifyQueueForWaitting.push(conn);
		}
	}
	
	//一个线程即可
	static class NotifyQueueCheckThreadForWaitting extends Thread{
		
		static class HoldItem{
			Connection2 conn;
			long holdBeginTime;
			long holdTime = 100L;
			HoldItem(Connection2 conn){
				this.conn = conn;
				holdBeginTime = System.currentTimeMillis();
			}
		}
		
		LinkedList<HoldItem> holdList = new LinkedList<HoldItem>();
		DefaultConnectionSelector2 cs;
		
		NotifyQueueCheckThreadForWaitting(DefaultConnectionSelector2 cs){
			this.cs = cs;
			this.setName("NotifyQueueCheckThreadForWaitting-Thread");
		}
		
		private void addConnectionToHoldList(Connection2 conn){
			HoldItem item = null;
			for(int i = 0 ; i < holdList.size() ; i++){
				HoldItem hi = holdList.get(i);
				if(hi.conn == conn){
					item = hi;
					break;
				}
			}
			if(item == null){
				item = new HoldItem(conn);
				item.holdTime = 100L;
				holdList.add(item);
			}
		}
		
		public void run(){
			long lastCheckHoldList = 0;
			while(Thread.currentThread().isInterrupted() == false){
				long now = System.currentTimeMillis();
				
				Connection2 conn = (Connection2)cs.notifyQueueForWaitting.pop(100L);
				if(conn != null && conn.state == Connection2.CONN_STATE_CONNECT){
					try{
						int ret = conn.writeWaittingMessageToBuffer();
						if(ret > 0 && conn.sendDataBuffer.position() > 0){
							cs.notify(conn, NOTIFY_TYPE_SENDBUFFER);
						}
						if(ret > 0 && conn.waitingQueue.size() > 0){
							cs.notify(conn, NOTIFY_TYPE_WAITTINGQUEUE);
						}
						if(ret == 0 && conn.waitingQueue.size() > 0){
							addConnectionToHoldList(conn);
						}
					}catch(Exception e){
						logger.warn("["+Thread.currentThread().getName()+","+cs.getName()+","+conn.getName()+"] [writeWaittingMessageToBuffer] [failed] [catch_exception_and_discard_notify_event] ["+Connection2.getStateString(conn.state)+"] ["+conn.remoteAddress+"]",e);
					}
				}
				
				//
				if(now - lastCheckHoldList > 100L){
					lastCheckHoldList = now;
					
					Iterator<HoldItem> it = holdList.iterator();
					while(it.hasNext()){
						HoldItem hi = it.next();
						if(hi.conn.waitingQueue.size() == 0){
							it.remove();
						}else if(now - hi.holdBeginTime > hi.holdTime){
							cs.notify(hi.conn, NOTIFY_TYPE_WAITTINGQUEUE);
							it.remove();
						}
					}
				}
				
			}
			DefaultConnectionSelector2.logger.warn("["+getName()+"] has stopped!");
		}
	}
	
	static class NotifyQueueCheckThreadForBuffer implements Runnable{
		DefaultConnectionSelector2 cs;
		
		NotifyQueueCheckThreadForBuffer(DefaultConnectionSelector2 cs){
			this.cs = cs;
		}
	
		public void run(){

			while(Thread.currentThread().isInterrupted() == false){
				
				Connection2 conn = (Connection2)cs.notifyQueueForBuffer.pop(100L);
				if(conn != null && conn.state == Connection2.CONN_STATE_CONNECT){
					try{	
						int n = conn.writeSendBufferToNetwork();

						if(conn.state == Connection2.CONN_STATE_CONNECT && conn.sendDataBuffer.position() > 0){
							SelectableChannel channel = (SelectableChannel)conn.channel;
							SelectionKey key = channel.keyFor(cs.selector);
							if(key != null && conn.sendDataBuffer.position() > 0){
								int interestOps = key.interestOps();
								if((interestOps & SelectionKey.OP_WRITE) == 0){
									interestOps = interestOps|SelectionKey.OP_WRITE;
									key.interestOps(interestOps);
									conn.selector_operation = interestOps;
									cs.selector.wakeup();
								}
							}
						}
					}catch(Exception e){
						logger.warn("["+Thread.currentThread().getName()+","+cs.getName()+","+conn.getName()+"] [writeSendBufferToNetwork] [failed] [catch_exception_and_discard_notify_event] ["+Connection2.getStateString(conn.state)+"] ["+conn.remoteAddress+"]",e);
					}
				}
				
			}
			DefaultConnectionSelector2.logger.warn("["+Thread.currentThread().getName()+"] has stopped!");
		}
	}
	/**
	 * <pre>
	 * 从selector中取出一个链接，用于发送数据给链接的另一端。
	 * 一般情况下为，客户端去一个链接发送数据给服务器。
	 * 也可以是，服务器取出一个链接发送给客户端。
	 * 
	 * connIdentity为要取的链接的标识，可以通过链接的setIdentity()设置。
	 * 当connIdentity为null时，标识取任意的一个链接，用于发送数据。
	 *      
	 * </pre>     
	 * @param timeout 等待的时间，如果小于0，表示永远等待，等于0表示立即返回。大于0表示要等待
	 */
	public Connection2 takeoutConnection(String connIdentity,long timeout) throws Exception{
		
		long startTime = System.currentTimeMillis();
		long endTime = startTime + timeout;
		takeConnectionLock.lock();
		try{
			Connection2 conn = this.findConnection(connIdentity);
			if(conn != null){
				if(logger.isDebugEnabled()){
					logger.debug("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] takeout from selector "+getStatusString()+" by connIdentity ["+connIdentity+"] ,cost time ["+(System.currentTimeMillis() - startTime)+"]");
				}
				return conn;
			}
			
			if(this.clientModel && selector.keys().size() -1 < this.maxConnectionNum && connIdentity == null && host != null && port > 0){
				//
				conn = this.createConnection(host, port);
				this.returnConnectoin(conn, SelectionKey.OP_READ);
				
				if(logger.isDebugEnabled()){
					logger.debug("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] takeout from selector "+getStatusString()+" by create new connection ,cost time ["+(System.currentTimeMillis() - startTime)+"]");
				}
				
				return conn;
			}
			
			while(true){
				long now = System.currentTimeMillis();
				if(timeout >= 0 && now >= endTime){
					if(logger.isInfoEnabled()){
						logger.info("["+name+"] no connection takeout from selector "+getStatusString()+"  by connIdentity ["+connIdentity+"] cause timeout for waitting,cost time ["+(now - startTime)+"]");
					}
					return null;
				}
				
				try{
					this.takeConnectionConditionCount++;
					if(timeout < 0)
						takeConnectionCondition.await();
					else
						takeConnectionCondition.await(endTime - now,TimeUnit.MILLISECONDS);
				}catch(Exception e){
					logger.error("["+name+"] catch exception while wait.",e);
				}finally{
					this.takeConnectionConditionCount--;
				}
				
				conn = this.findConnection(connIdentity);
				if(conn != null){

					if(logger.isDebugEnabled()){
						logger.debug("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] takeout from selector "+getStatusString()+" by connIdentity ["+connIdentity+"] ,cost time ["+(System.currentTimeMillis() - startTime)+"]");
					}
					return conn;
				}
			}
		}catch(Exception e){
			
			logger.info("["+name+"] no connection takeout from selector "+getStatusString()+" cause exception,cost time ["+(System.currentTimeMillis() - startTime)+"]",e);
			
			throw e;
		}finally{
			takeConnectionLock.unlock();
		}
	}

	
	/**
	 * 创建一个新的连接
	 * 典型的用法：
	 * 
	 * <pre>
	 * 		String serverId = "....";
	 * 		Connection2 conn = selector.takeoutConnection(serverId,0L);
	 * 		if(conn == null){
	 * 			conn = selector.takeoutNewConnection(serverHost,serverPort);
	 * 			if(conn != null){
	 * 				conn.setIdentity(serverId);
	 * 			}
	 * 		}
	 * 		if(conn != null){
	 * 			//TODO ....
	 * 		}
	 * </pre>
	 * @param serverHost
	 * @param serverPort
	 * @return
	 * @throws Exception
	 */
	public Connection2 takeoutNewConnection(String serverHost,int serverPort) throws Exception{
		long startTime = System.currentTimeMillis();
		takeConnectionLock.lock();
		try{
			if(this.clientModel && selector.keys().size() -1 < this.maxConnectionNum){
				//
				Connection2 conn = this.createConnection(serverHost, serverPort);
				this.returnConnectoin(conn, SelectionKey.OP_READ);
				
				if(logger.isDebugEnabled()){
					logger.debug("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] takeout from selector "+getStatusString()+" by create new connection ,cost time ["+(System.currentTimeMillis() - startTime)+"]");
				}
				
				return conn;
			}else{
				logger.warn("["+name+"] takeout new connection failed from selector "+getStatusString()+" by create new connection ,cost time ["+(System.currentTimeMillis() - startTime)+"]");
				return null;
			}
		}catch(Exception e){
			
			logger.info("["+name+"] no connection takeout from selector "+getStatusString()+" cause exception,cost time ["+(System.currentTimeMillis() - startTime)+"]",e);
			
			throw e;
		}finally{
			takeConnectionLock.unlock();
		}
	}
	
	private Connection2 findConnection(String connIdentity){
		try{
			Connection2 retConn = null;
			long lastReceiveDataTime = 0;
			Iterator<SelectionKey> it = selector.keys().iterator();
			while(it.hasNext()){
				SelectionKey key = it.next();
				if(key.isValid() == false) continue;
				if(key.attachment() == null) continue;
				Connection2 conn = (Connection2)key.attachment();
				if(conn.state != Connection2.CONN_STATE_CONNECT) continue;
				if(conn.waitingQueue.size() == 0 && conn.sendDataBuffer.position() == 0){
					if(connIdentity == null || connIdentity.equals(conn.getIdentity())){
						if(retConn == null || conn.lastReceiveDataTime < lastReceiveDataTime){
							retConn = conn;
							lastReceiveDataTime = conn.lastReceiveDataTime;
						}
					}
				}
			}
			if(retConn != null) return retConn;
			
			it = selector.keys().iterator();
			while(it.hasNext()){
				SelectionKey key = it.next();
				if(key.isValid() == false) continue;
				if(key.attachment() == null) continue;
				Connection2 conn = (Connection2)key.attachment();
				if(conn.state != Connection2.CONN_STATE_CONNECT) continue;
				if(conn.waitingQueue.size() == 0){
					if(connIdentity == null || connIdentity.equals(conn.getIdentity())){
						if(retConn == null || conn.lastReceiveDataTime < lastReceiveDataTime){
							retConn = conn;
							lastReceiveDataTime = conn.lastReceiveDataTime;
						}
					}
				}
			}
			if(retConn != null) return retConn;
			
			it = selector.keys().iterator();
			while(it.hasNext()){
				SelectionKey key = it.next();
				if(key.isValid() == false) continue;
				if(key.attachment() == null) continue;
				Connection2 conn = (Connection2)key.attachment();
				if(conn.state != Connection2.CONN_STATE_CONNECT) continue;
				
				if(connIdentity == null || connIdentity.equals(conn.getIdentity())){
					if(retConn == null || conn.lastReceiveDataTime < lastReceiveDataTime){
						retConn = conn;
						lastReceiveDataTime = conn.lastReceiveDataTime;
					}
				}
			}
			if(retConn != null) return retConn;
		}catch(Exception e){
			logger.warn("["+Thread.currentThread().getName()+","+name+"] find connection by connIdentity ["+connIdentity+"] error cause:",e);
		}
		return null;
	}
	
	private long seq = 0;
	private synchronized String genId(){
		seq ++;
		return "connection-"+seq;
	}
	
	/**
	 * 关闭一个连接
	 * @param conn
	 */
	protected void closeConnection(Connection2 conn){
		
		if(conn.selector != this){
			logger.error("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] closing to invalid selector.");
			throw new  IllegalArgumentException("connection can't return to another selector");
		}
		
		if(conn.getState() != Connection2.CONN_STATE_CLOSE){
			logger.error("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] can't closing connection its state is NOT CLOSE.");
			throw new  IllegalArgumentException("connection can't closing cause its state is NOT CLOSE");
		}

		closingConnQueue.push(conn);

		selector.wakeup();
	}
	
	/**
	 * 创建一个新的连接，如果当前的线程已经获得lock，那么就释放lock，等链接创建完毕后，在获得lock
	 * @param host
	 * @param port
	 * @return
	 * @throws java.io.IOException
	 */
	protected Connection2 createConnection(String host,int port) throws java.io.IOException{
		if(isClientModel() == false){
			throw new IOException("server model can't create connection");
		}

		try{
			SocketChannel channel = SocketChannel.open();
			InetSocketAddress sa = new InetSocketAddress(host,port);
			if(bindLocalHost != null){
				InetSocketAddress saLocal = new InetSocketAddress(this.bindLocalHost,this.bindLocalPort);
				channel.socket().bind(saLocal);
			}

			channel.socket().connect(sa,(int)connectTimeout);
			
			Connection2 conn = new Connection2(this,channel,null,null,connectionSendBufferSize,connectionReceiveBufferSize,this.maxReceiveDataTimeout,this.maxIdelTime);
			conn.name = genId();
			conn.remoteAddress = "" + sa;
			conn.state = Connection2.CONN_STATE_OPEN;
			
			newingConnectionList.add(conn);
			
			conn.state = Connection2.CONN_STATE_CONNECT;
			
			if(connectionCreatedHandler != null){
				try{
					connectionCreatedHandler.created(conn,attachment);
				}catch(IOException e){
					conn.forceTerminate("回调创建连接接口，出现异常，关闭连接");
					throw e;
				}
			}
			
			channel.configureBlocking(false);
			
			if(terminateHangler != null)
				conn.setConnectionTerminateHandler(terminateHangler);

			
			if(logger.isDebugEnabled()){
				logger.debug("["+name+"] create connection["+conn.name+","+conn.remoteAddress+"] and connect to ["+host+","+port+"] success");
			}
			
			return conn;
		}catch(IOException e){
			logger.warn("["+name+"] create connection to ["+host+","+port+"] failed:",e);
			throw e;
		}
	}
	
	protected void stat(long now){
		long tmpSendMessageNum = 0;
		long tmpSendMessagePacketSize = 0;
		long tmpReceiveMessageNum = 0;
		long tmpReceiveMessagePacketSize = 0;
		
		if(selector.keys().size() > 0){
			Iterator<SelectionKey> it = selector.keys().iterator();
			while(it.hasNext()){
				SelectionKey sk = it.next();
				if(sk.attachment() == null) continue;
				Connection2 conn = (Connection2)sk.attachment();
				if(conn.getState() == Connection2.CONN_STATE_CONNECT){
					tmpSendMessageNum += conn.sendMessageNum;
					tmpSendMessagePacketSize += conn.sendMessagePacketSize;
					tmpReceiveMessageNum += conn.receiveMessageNum;
					tmpReceiveMessagePacketSize += conn.receiveMessagePacketSize;
				}
			}
		}
		
		this.totalSendMessageNum = tmpSendMessageNum + this.closedConnectionSendMessageNum;
		this.totalSendMessagePacketSize = tmpSendMessagePacketSize + this.closedConnectionSendMessagePacketSize;
		this.totalReceiveMessageNum = tmpReceiveMessageNum + this.closedConnectionReceiveMessageNum;
		this.totalReceiveMessagePacketSize = tmpReceiveMessagePacketSize + this.closedConnectionReceiveMessagePacketSize;
		this.lastStatNumAndPacketTime = now;
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	Object statLock = new Object();
	//统计协议的数量和大小
	protected boolean statProtocolFlag = false;
	//type --> [数量，大小]
	protected HashMap<String,long[]> sendMessageStatMap = null;
	//type --> [数量，大小]
	protected HashMap<String,long[]> receiveMessageStatMap = null;
	
	protected void statClosedConnection(Connection2 conn){
		synchronized(statLock){
			this.closedConnectionSendMessageNum += conn.sendMessageNum;
			this.closedConnectionSendMessagePacketSize += conn.sendMessagePacketSize;
			this.closedConnectionReceiveMessageNum += conn.receiveMessageNum;
			this.closedConnectionReceiveMessagePacketSize += conn.receiveMessagePacketSize;
			
			if(statProtocolFlag == false) return;
			
			if(sendMessageStatMap == null){
				sendMessageStatMap = new HashMap<String,long[]>();
			}
			if(receiveMessageStatMap == null){
				receiveMessageStatMap = new HashMap<String,long[]>();
			}
			if(conn.sendMessageStatMap != null){
				Iterator<java.util.Map.Entry<String,long[]>> it = conn.sendMessageStatMap.entrySet().iterator();
				while(it.hasNext()){
					java.util.Map.Entry<String,long[]> entry = it.next();
					long data[] = sendMessageStatMap.get(entry.getKey());
					if(data == null){
						data = new long[2];
						sendMessageStatMap.put(entry.getKey(), data);
					}
					data[0] += entry.getValue()[0];
					data[1] += entry.getValue()[1];
				}
			}
			if(conn.receiveMessageStatMap != null){
				Iterator<java.util.Map.Entry<String,long[]>> it = conn.receiveMessageStatMap.entrySet().iterator();
				while(it.hasNext()){
					java.util.Map.Entry<String,long[]> entry = it.next();
					long data[] = receiveMessageStatMap.get(entry.getKey());
					if(data == null){
						data = new long[2];
						receiveMessageStatMap.put(entry.getKey(), data);
					}
					data[0] += entry.getValue()[0];
					data[1] += entry.getValue()[1];
				}
			}
		}
	}
	
	
	
	private void autoCheck(long now){
		
		try{
			synchronized(newingConnectionList){
				if(newingConnectionList.size() > 0){
					Iterator<Connection2> it = newingConnectionList.iterator();
					while(it.hasNext()){
						Connection2 c = it.next();
						if(c.getState() == Connection2.CONN_STATE_CLOSE){
							it.remove();
						}else if(c.connectionMaxIdleTimeout > 0 && Math.max(c.lastSendDataTime,c.lastReceiveDataTime) + c.connectionMaxIdleTimeout < now){
							c.forceTerminate("新链接超过最大空闲时间，强制关闭");
							it.remove();
						}
					}
				}
			}
		}catch(Exception e){
			logger.warn("["+name+"] selector "+getStatusString()+" check new connection list error, list size is "+newingConnectionList.size()+"",e);
		}
		
		
		if(clientModel){
			//generate every host:port connection number
			HashMap<SocketAddress,Integer> connCountMap = new HashMap<SocketAddress,Integer>();
			if(selector.keys().size() > 0){
				Iterator<SelectionKey> it = selector.keys().iterator();
				while(it.hasNext()){
					SelectionKey sk = it.next();
					if(sk.isValid() == false) continue;
					if(sk.attachment() == null) continue;
					Connection2 conn = (Connection2)sk.attachment();
					SocketAddress sa = ((SocketChannel)conn.getChannel()).socket().getRemoteSocketAddress();
					if(connCountMap.containsKey(sa)){
						int c = connCountMap.get(sa);
						connCountMap.put(sa,c+1);
					}else{
						connCountMap.put(sa,1);
					}
				}							
			}
			SocketAddress sas[] = connCountMap.keySet().toArray(new SocketAddress[0]);
			for(int i = 0 ; i < sas.length ; i++){
				int s = connCountMap.get(sas[i]);
				for(int j = s ; j < this.minConnectionNum ; j++){
					if(singleThreadMode){
						try{
							Connection2 conn = createConnection(((InetSocketAddress)sas[i]).getAddress().getHostAddress(),((InetSocketAddress)sas[i]).getPort());
							newConnQueue.push(new Object[]{conn,SelectionKey.OP_READ});
							newingConnectionList.remove(conn);
						}catch(Exception e){
							logger.warn("["+Thread.currentThread().getName()+"], create connection failed:",e);
						}
					}else{
						threadPool.execute(new ExecCreatingConnection(this,(InetSocketAddress)sas[i]));
					}
				}
				
				if(s > this.maxConnectionNum){
					int t = 0;
					SelectionKey sks[] = selector.keys().toArray(new SelectionKey[0]);
					for(int j = 0 ; j < sks.length ; j++){
						SelectionKey sk = sks[i];
						if(sk != null && sk.isValid() && sk.attachment() != null){
							Connection2 conn = (Connection2)sk.attachment();
							SocketAddress sa = ((SocketChannel)conn.getChannel()).socket().getRemoteSocketAddress();
							if(sa.equals(sas[i])){
								t++;
								if(t > this.maxConnectionNum){
									if(Math.max(conn.lastSendDataTime,conn.lastReceiveDataTime) + conn.waitingRequestMessageTimeout < now && conn.waitingQueue.size() == 0 && conn.sendDataBuffer.position() == 0){
										conn.forceTerminate("超过最大连接数，关闭连接");
										logger.warn("["+name+"] selector "+getStatusString()
												+" close connection["+conn.name+"] from ["+
												sa+"] cause open too many connections, max num is ["+maxConnectionNum+"]");
									}
								}
							}
							
						}
					}
				}
			}//客户端模式，创建最小连接数，关闭超过最大连接数的
		}
		
		if(selector.keys().size() > 0){
			Iterator<SelectionKey> it = selector.keys().iterator();
			while(it.hasNext()){
				SelectionKey sk = it.next();
				if(sk.isValid() == false) continue;
				if(sk.attachment() == null) continue;
				Connection2 conn = (Connection2)sk.attachment();
				long idleTime = now - Math.max(conn.lastSendDataTime,conn.lastReceiveDataTime);
					
				if(maxIdelTime > 0 && idleTime > this.maxIdelTime){
					conn.forceTerminate("Idle超时，关闭连接");
					logger.warn("["+name+"] selector "+getStatusString()+" close connection["+
							conn.name+"] cause reach max idle time ["+idleTime+","+this.maxIdelTime+"]");
					
				}
			}
		}
		
	}
	
	protected void checkTimeout(long now){
		//是否有超时的
		//timeout
		Iterator<SelectionKey> it = selector.keys().iterator();
		while(it.hasNext()){
			SelectionKey sk = it.next();
			if(sk.isValid() == false) continue;
			Connection2 conn = (Connection2)sk.attachment();
			if(conn == null) continue;
			
			if(conn.lastReceiveDataTime + conn.waitingRequestMessageTimeout < now
					&& conn.lastCheckTimeout + conn.waitingRequestMessageTimeout < now){
				conn.lastCheckTimeout = now;
				if(singleThreadMode){
					try{
						conn.channelSelectorTimeout();
					}catch(Exception e){
						logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelSelectorTimeout() error and will be stopped:",e);
						conn.forceTerminate("处理channelSelectorTimeout出现异常");
					}
				}else{
					try{
						threadPool.execute(new ExecConnectionTimeout(this,conn));
					}catch(RejectedExecutionException e){
						
						long lll = System.currentTimeMillis();
						threadPoolLock.lock();
						try{
							threadPoolFullWaitingCount++;
							threadPoolFull.await(60, TimeUnit.SECONDS);
						}catch(InterruptedException ex){
							ex.printStackTrace();
						}finally{
							threadPoolFullWaitingCount--;
							threadPoolLock.unlock();
						}
						
						logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelSelectorTimeout() error cause ThreadPool is Full and waiting "+(System.currentTimeMillis() - lll)+"ms",e);
						
						
						try{
							threadPool.execute(new ExecConnectionTimeout(this,conn));
						}catch(RejectedExecutionException ex){
							logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelSelectorTimeout() error cause ThreadPool is Full and do anything",ex);
						}
						
					}
				}
			}
		}
	
	}
	
	public void run() {
		long lastAutoCreateTime = 0L;
		long lastStatTime = 0L;
		long lastTimeoutCheckTime = 0L;
		while(Thread.currentThread().isInterrupted() == false){
			try{
				long now = System.currentTimeMillis();
				if(now - lastAutoCreateTime > 30000L){
					lastAutoCreateTime = now;
					autoCheck(now);
				}
				
				if(now - lastStatTime > 1000L){
					lastStatTime = now;
					stat(now);
				}

				
				while(!newConnQueue.isEmpty()){
					Object[] objs = (Object[])newConnQueue.pop();
					if(objs == null) continue;
					Connection2 conn = (Connection2)objs[0];
					int operation = (Integer)objs[1];
					SocketChannel channel = (SocketChannel)conn.channel;
					this.takeConnectionLock.lock();
					try{
						channel.register(selector,operation,conn);
						
						conn.selector_operation = operation;
						
						if(takeConnectionConditionCount > 0){
							takeConnectionCondition.signal();
						}
					}finally{
						takeConnectionLock.unlock();
					}
				}
				
				while(!closingConnQueue.isEmpty()){
					Connection2 conn = (Connection2)closingConnQueue.pop();
					if(conn != null)
						conn.forceTerminate("主动调用Connection.close()关闭连接");
				}
				
				
				long timeout = 1000L;
				if(logger.isDebugEnabled()){
					logger.debug("["+name+"] selector "+getStatusString()+" prepared to select for timeout["+timeout+"] ...");
				}
				long startTime = now;

				
				int k = selector.select(timeout);
				
				
				now = System.currentTimeMillis();

				if(logger.isDebugEnabled()){
					logger.debug("["+name+"] selector "+getStatusString()+" wakeup with return="+k+" for select time["+(now - startTime)+"]");
				}
				
				if(Thread.currentThread().isInterrupted()) break;
				
				try{
					Iterator<SelectionKey> it = selector.selectedKeys().iterator();
					while(it.hasNext()){
						SelectionKey sk = it.next();
						it.remove();
						if(sk.isValid() == false){
							Connection2 c = (Connection2)sk.attachment();
							if(c != null){
								logger.warn("["+name+"] selector checking: ["+c.getName()+"] ["+sk.interestOps()+"] ["+sk.isReadable()+"] ["+sk.isWritable()+"] [SelectionKey非法，直接跳过] ["+Connection2.getStateString(c.getState())+"]");
							}else{
								logger.warn("["+name+"] selector checking: [对应连接不存在] ["+sk.interestOps()+"] ["+sk.isReadable()+"] ["+sk.isWritable()+"] [SelectionKey非法，直接跳过] [--]");
							}
							continue;
						}
						try{	
							
							if(sk.isAcceptable()){
								ServerSocketChannel ssc = (ServerSocketChannel)sk.channel();
								SocketChannel sc = ssc.accept();
								if(sc != null && newClientConnectHandler != null){
									
									Connection2 conn = new Connection2(this,sc,null,null,connectionSendBufferSize,connectionReceiveBufferSize,this.maxReceiveDataTimeout,this.maxIdelTime);
									conn.name = genId();
									conn.remoteAddress = "" + sc.socket().getRemoteSocketAddress();
									conn.state = Connection2.CONN_STATE_OPEN;
									conn.setConnectionTerminateHandler(terminateHangler);
									
									newingConnectionList.add(conn);
									
									if(singleThreadMode){
										try{
											conn.state = Connection2.CONN_STATE_CONNECT;
											newClientConnectHandler.connected(conn);
											sc.configureBlocking(false);
											newingConnectionList.remove(conn);
											newConnQueue.push(new Object[]{conn,SelectionKey.OP_READ});
										}catch(Exception e){
											logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] connect error and will be stopped:",e);
											conn.forceTerminate("创建新的连接出错："+e.getMessage());
										}
									}else{
										try{
											threadPool.execute(new ExecNewClientConnected(this,newClientConnectHandler,conn));
										}catch(RejectedExecutionException e){
											
											long lll = System.currentTimeMillis() ;
											threadPoolLock.lock();
											try{
												threadPoolFullWaitingCount++;
												threadPoolFull.await(30, TimeUnit.SECONDS);
											}catch(InterruptedException ex){
												ex.printStackTrace();
											}finally{
												threadPoolFullWaitingCount--;
												threadPoolLock.unlock();
											}
											
											logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [新的连接接连上来] [ThreadPool_Full] [waiting("+(System.currentTimeMillis() - lll)+")ms]",e);
											
											
											try{
												threadPool.execute(new ExecNewClientConnected(this,newClientConnectHandler,conn));
											}catch(RejectedExecutionException ex){
												logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [新的连接接连上来] [ThreadPool_Full] [无可用的线程，等待强制关闭新的连接] [cost:"+(System.currentTimeMillis() - lll)+"ms]",ex);
												conn.close("无可用的线程，等待强制关闭新的连接");
											}
										}
									}
								}
							}else if(sk.isReadable()){
								Connection2 conn = (Connection2)sk.attachment();
								int interestOps = sk.interestOps();
								interestOps = interestOps&(~SelectionKey.OP_READ);
								sk.interestOps(interestOps);
								conn.selector_operation = interestOps;
								
								if(singleThreadMode){
									try{
										conn.channelReadyToReadData();
										interestOps = interestOps|SelectionKey.OP_READ;
										sk.interestOps(interestOps);
										conn.selector_operation = interestOps;
									}catch(Exception e){
										logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelReadyToReadData() error and will be stopped:",e);
										conn.forceTerminate("处理channelReadyToReadData出现异常");
									}
								}else{
									try{
										threadPool.execute(new ExecConnectionReadReady(this,conn));
									}catch(RejectedExecutionException e){
										long lll = System.currentTimeMillis();
										
										threadPoolLock.lock();
										try{
											threadPoolFullWaitingCount++;
											threadPoolFull.await(1, TimeUnit.SECONDS);
										}catch(InterruptedException ex){
											ex.printStackTrace();
										}finally{
											threadPoolFullWaitingCount--;
											threadPoolLock.unlock();
										}
										
										logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [有数据可读] [ThreadPool_Full] [waiting("+(System.currentTimeMillis() - lll)+")ms]",e);
										
										
										try{
											threadPool.execute(new ExecConnectionReadReady(this,conn));
										}catch(RejectedExecutionException ex){
											interestOps = interestOps|SelectionKey.OP_READ;
											sk.interestOps(interestOps);
											conn.selector_operation = interestOps;
											
											logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [有数据可读] [ThreadPool_Full] [无可用的线程，不作任何处理，将连接返回给Selector] [cost:"+(System.currentTimeMillis() - lll)+"ms]",ex);
										}
									}
								}
							}else if(sk.isWritable()){
								Connection2 conn = (Connection2)sk.attachment();
								int interestOps = sk.interestOps();
								interestOps = interestOps&(~SelectionKey.OP_WRITE);
								sk.interestOps(interestOps);
								conn.selector_operation = interestOps;
								
								if(singleThreadMode){
									try{
										conn.channelReadyToWriteData();
										if(conn.sendDataBuffer.position() > 0){
											interestOps = interestOps|SelectionKey.OP_WRITE;
											sk.interestOps(interestOps);
											conn.selector_operation = interestOps;
											
										}else{
											notifyTakeConnectionCondition();
										}
									}catch(Exception e){
										logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelReadyToWriteData() error and will be stopped:",e);
										conn.forceTerminate("处理channelReadyToWriteData出现异常");
									}
								}else{
									try{
										threadPool.execute(new ExecConnectionWriteReady(this,conn));
									}catch(RejectedExecutionException e){
										long lll = System.currentTimeMillis();
										threadPoolLock.lock();
										try{
											threadPoolFullWaitingCount++;
											threadPoolFull.await(1, TimeUnit.SECONDS);
										}catch(InterruptedException ex){
											ex.printStackTrace();
										}finally{
											threadPoolFullWaitingCount--;
											threadPoolLock.unlock();
										}
										
										logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [有网络缓冲区可写] [ThreadPool_Full] [waiting("+(System.currentTimeMillis() - lll)+")ms]",e);
										
										
										try{
											threadPool.execute(new ExecConnectionWriteReady(this,conn));
										}catch(RejectedExecutionException ex){
											
											if(conn.sendDataBuffer.position() > 0){
												interestOps = interestOps|SelectionKey.OP_WRITE;
												sk.interestOps(interestOps);
												conn.selector_operation = interestOps;
												
											}

											logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [有网络缓冲区可写] [ThreadPool_Full] [无可用的线程，不作任何处理，将连接返回给Selector] [cost:"+(System.currentTimeMillis() - lll)+"ms]",ex);
										}
									}
								}
							}else{
								logger.warn("["+name+"] [unknow selectionkey interestOps="+sk.interestOps()+"]");
							}
						}catch(Throwable e){
							logger.warn("["+name+"] unkown error while handle selected key in Thread["+Thread.currentThread().getName()+"]",e);
						}
					}
					
					
					if(now - lastTimeoutCheckTime > 1000L){
						lastTimeoutCheckTime = now;
						checkTimeout(now);
					}
					
					
				}catch(Exception e){
					logger.warn("["+Thread.currentThread().getName()+"],selctor warkup with k="+k+" and handle error cause exception",e);
				}
				
			}catch(Throwable e){
				
				logger.warn("unkown error in Thread["+Thread.currentThread().getName()+"]",e);
				e.printStackTrace();
			}
		}
		
		logger.info("Thread["+Thread.currentThread().getName()+"] will be stopped");
		
	}

	static class ExecNewClientConnected implements Runnable{
		Connection2 conn = null;
		ConnectionConnectedHandler2 handler = null;
		DefaultConnectionSelector2 cs;
		ExecNewClientConnected(DefaultConnectionSelector2 cs,ConnectionConnectedHandler2 handler,Connection2 conn){
			this.conn = conn;
			this.cs = cs;
			this.handler = handler;
		}
		
		public void run() {
			try{
				handler.connected(conn);
				conn.state = Connection2.CONN_STATE_CONNECT;
				SelectableChannel sc = (SelectableChannel)conn.channel;
				sc.configureBlocking(false);
				cs.returnConnectoin(conn,SelectionKey.OP_READ);
				
			}catch(Exception e){
				logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] connect error and will be stopped:",e);
				conn.forceTerminate("创建新的连接出错："+e.getMessage());
			}finally{
				cs.notifyThreadReturnToThreadPool();
			}
		}
	}
	
	static class ExecCreatingConnection implements Runnable{
		DefaultConnectionSelector2 cs;
		InetSocketAddress address;
		ExecCreatingConnection(DefaultConnectionSelector2 cs,InetSocketAddress address){
			this.cs = cs;
			this.address = address;
		}
		
		public void run() {
			try{
				Connection2 conn = cs.createConnection(address.getAddress().getHostAddress(),address.getPort());
				cs.returnConnectoin(conn,SelectionKey.OP_READ);
			}catch(Exception e){
				logger.warn("["+Thread.currentThread().getName()+"], create connection failed:",e);
			}finally{
				cs.notifyThreadReturnToThreadPool();
			}
		}
	}
	
	static class ExecConnectionTimeout implements Runnable{
		Connection2 conn = null;
		DefaultConnectionSelector2 cs;
		ExecConnectionTimeout(DefaultConnectionSelector2 cs,Connection2 conn){
			this.conn = conn;
			this.cs = cs;
		}
		
		public void run() {
			try{
				conn.channelSelectorTimeout();
			}catch(Exception e){
				logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelSelectorTimeout() error and will be stopped:",e);
				conn.forceTerminate("处理连接超时出错："+e.getMessage());
			}finally{
				cs.notifyThreadReturnToThreadPool();
			}
		}
	}
	
	static class ExecConnectionReadReady implements Runnable{
		Connection2 conn = null;
		DefaultConnectionSelector2 cs;
		ExecConnectionReadReady(DefaultConnectionSelector2 cs,Connection2 conn){
			this.conn = conn;
			this.cs = cs;
			
		}
		
		public void run() {
			try{
				conn.channelReadyToReadData();
				if(conn.state == Connection2.CONN_STATE_CONNECT){
					cs.returnConnectoin(conn, SelectionKey.OP_READ);
				}
			}catch(Exception e){
				logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelReadyToReadData() error and will be stopped:",e);
				conn.forceTerminate("读取并处理数据时出错："+e.getMessage());
			}finally{
				cs.notifyThreadReturnToThreadPool();
			}
		}
	}
	
	static class ExecConnectionWriteReady implements Runnable{
		Connection2 conn = null;
		DefaultConnectionSelector2 cs;
		ExecConnectionWriteReady(DefaultConnectionSelector2 cs,Connection2 conn){
			this.conn = conn;
			this.cs = cs;
		}
		
		public void run() {
			try{
				conn.channelReadyToWriteData();
				if(conn.state == Connection2.CONN_STATE_CONNECT && conn.sendDataBuffer.position() > 0){
					cs.returnConnectoin(conn, SelectionKey.OP_WRITE);
				}else if(conn.state == Connection2.CONN_STATE_CONNECT){
					cs.notifyTakeConnectionCondition();
				}
			}catch(Exception e){
				logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelReadyToWriteData() error and will be stopped:",e);
				conn.forceTerminate("写出数据时出错："+e.getMessage());
			}finally{
				cs.notifyThreadReturnToThreadPool();
			}
		}
	}

	public int getConnectionSendBufferSize() {
		return connectionSendBufferSize;
	}

	/**
	 * 预先设置新的连接的发送缓冲区
	 */
	public void setConnectionSendBufferSize(int connectionSendBufferSize) {
		this.connectionSendBufferSize = connectionSendBufferSize;
	}

	public int getConnectionReceiveBufferSize() {
		return connectionReceiveBufferSize;
	}

	/**
	 * 预先设置新的连接的接收缓冲区
	 */
	public void setConnectionReceiveBufferSize(int connectionReceiveBufferSize) {
		this.connectionReceiveBufferSize = connectionReceiveBufferSize;
	}



	public String getBindLocalHost() {
		return bindLocalHost;
	}

	public void setBindLocalHost(String bindLocalHost) {
		this.bindLocalHost = bindLocalHost;
	}

	public int getBindLocalPort() {
		return bindLocalPort;
	}

	public void setBindLocalPort(int bindLocalPort) {
		this.bindLocalPort = bindLocalPort;
	}

	public boolean isSingleThreadMode() {
		return singleThreadMode;
	}

	public void setSingleThreadMode(boolean singleThreadMode) {
		this.singleThreadMode = singleThreadMode;
	}

	public int getThreadPoolCorePoolSize() {
		return threadPoolCorePoolSize;
	}

	public void setThreadPoolCorePoolSize(int threadPoolCorePoolSize) {
		this.threadPoolCorePoolSize = threadPoolCorePoolSize;
	}

	public int getThreadPoolMaxPoolSize() {
		return threadPoolMaxPoolSize;
	}

	public void setThreadPoolMaxPoolSize(int threadPoolMaxPoolSize) {
		this.threadPoolMaxPoolSize = threadPoolMaxPoolSize;
	}

	public int getThreadPoolKeepAlive() {
		return threadPoolKeepAlive;
	}

	public void setThreadPoolKeepAlive(int threadPoolKeepAlive) {
		this.threadPoolKeepAlive = threadPoolKeepAlive;
	}

	


}
