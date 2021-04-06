package com.xuanzhi.tools.transport;

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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.ds.AvlTree;
import com.xuanzhi.tools.ds.AvlTree.TreeNode;
import com.xuanzhi.tools.queue.DefaultQueue;

/**
 * 链接管理器
 * 此链接管理器，有两种模式：
 * 客户端模式：
 * 		所有的链接都是由本地发起，链接远程服务器，远程服务器可以是多个地址。
 * 
 * 服务器模式：
 * 		所有的链接都是有远端发起，链接本地地址，本地有一个SocketServer在监听。
 *      远端客户端可以是多个地址。
 *      
 * 多服务器模式：（2008-09-20）
 * 		多服务器模式，是服务器模式的一种，必须是服务器模式，多服务器模式才能生效。
 * 		多服务器模式，是指采用一个主服务器，用于负责处理客户端的连接请求。
 * 		然后用多个副服务器来处理各个链接上的数据包。
 * 		多服务器模式，是用于大量并发连接的情况。
 * 		实践测试：
 * 			1台Dell的2850，服务器模式的上限是5000个链接，每秒钟处理25000个128字节左右大小的包。
 * 			如果您需要处理更多的链接或者更大的流量，需要采用多服务器模式。
 * 
 * 		在一个客户端连接多个服务器的模式下，当其中某些服务器出现网络问题是，尤其是创建链接时，
 * 		不能及时成功或者失败时，这样会导致takeoutConnection方法占用DefaultConnectionSelector对象的monitor lock
 * 		，最终导致主线程阻塞在synchronized(this)上，所有的消息都得不到处理。
 * 
 *  	改进的办法，取消使用synchronized，采用ReentrantLock
 *  	
 * 		
 */
public class DefaultConnectionSelector implements ConnectionSelector ,Runnable{

	protected static Logger logger = LoggerFactory.getLogger(DefaultConnectionSelector.class);
	
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition notEmpty = lock.newCondition();
	
	private final Condition threadPoolFull = lock.newCondition();
	
	/**
	 * 无上限的线程池
	 */
	
	protected ThreadPoolExecutor threadPool = null;//new ThreadPoolExecutor(1,Integer.MAX_VALUE,30,TimeUnit.SECONDS,new SynchronousQueue());

	protected int threadPoolCorePoolSize = 1;
	
	protected int threadPoolMaxPoolSize = 256;
	//秒
	protected int threadPoolKeepAlive = 30;
	

	
	protected Selector selector;
	protected Thread thread = null;
	
	protected String name = "DefaultConnectionSelector";
	protected String attachment = null;
	
	protected boolean enableHeapForTimeout = false;
	protected AvlTree connTree;
	
	
	protected boolean enableTimepiece = false;
	protected long stepOfTimePiece = 10L;
	protected long timeOfTimePiece = System.currentTimeMillis();
	protected Thread timepieceThread = null;
	
	//单线程的模式
	protected boolean singleThreadMode = false;
	
	//用于标记，没有实际的意义，
	//此变量是为了防止出现死循环
	protected boolean wakeupSelectorFlag = false;
	
	public static class TimepieceThread extends Thread{
		DefaultConnectionSelector dcs;
		public TimepieceThread(DefaultConnectionSelector dcs){
			this.dcs = dcs;
		}
		
		public void run(){
			while(dcs.enableTimepiece){
				dcs.timeOfTimePiece = System.currentTimeMillis();
				try{
					Thread.sleep(dcs.stepOfTimePiece);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setEnableHeapForTimeout(boolean b){
		enableHeapForTimeout = b;
	}
	
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
	protected ConnectionConnectedHandler newClientConnectHandler;
	
	public void setConnectionConnectedHandler(ConnectionConnectedHandler handler){
		newClientConnectHandler = handler;
	}
	
	/**
	 * 当创建一个新的链接连接到服务器上后，调用此方法
	 */
	protected ConnectionCreatedHandler connectionCreatedHandler;
	
	public void setConnectionCreatedHandler(ConnectionCreatedHandler handler){
		connectionCreatedHandler = handler;
	}
	/**
	 * 当一个链接关闭的时候，调用此接口
	 */
	protected ConnectionTerminateHandler terminateHangler;
	
	public void setConnectionTerminateHandler(ConnectionTerminateHandler handler){
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
	 * 接收数据超时，长时间没有读到任何数据，将关闭连接
	 * 0标识没有限制。默认没有限制。
	 */
	protected long maxReceiveDataTimeout = 0;
	
	public void setMaxReceiveDataTimeout(long maxReceiveDataTimeout){
		this.maxReceiveDataTimeout = maxReceiveDataTimeout;
	}
	
	public long getMaxReceiveDataTimeout(){
		return maxReceiveDataTimeout;
	}
	/**
	 * 链接最大的使用时间，如果checkout出去的时间，超过这个时间，链接将被强制关闭
	 */
	protected long maxCheckoutTime = 30 * 1000L;
	
	protected int connectionSendBufferSize = 64 * 1024;
	protected int connectionReceiveBufferSize = 64 * 1024;
	
	protected DefaultQueue newConnQueue = null;
	
	protected DefaultQueue closingConnQueue = null;
	
	protected List<Connection> newConnectionList = Collections.synchronizedList(new LinkedList<Connection>());

	protected boolean multiServerModel = false;
	
	protected int secondaryServerNum = 5;
	
	private boolean isPrimaryServer = true;
	
	private DefaultConnectionSelector secondaryServers[] = null;
	
	public DefaultConnectionSelector(){
	}
	
	/**
	 * * 多服务器模式：（2008-09-20）
	 * 		多服务器模式，是服务器模式的一种，必须是服务器模式，多服务器模式才能生效。
	 * 		多服务器模式，是指采用一个主服务器，用于负责处理客户端的连接请求。
	 * 		然后用多个副服务器来处理各个链接上的数据包。
	 * 		多服务器模式，是用于大量并发连接的情况。
	 * 		实践测试：
	 * 			1台Dell的2850，服务器模式的上限是5000个链接，每秒钟处理25000个128字节左右大小的包。
	 * 			如果您需要处理更多的链接或者更大的流量，需要采用多服务器模式。
	 * @param b
	 */
	public void setMultiServerModel(boolean b){
		multiServerModel = b;
	}
	
	public boolean isMultiServerModel(){
		return multiServerModel;
	}
	
	public boolean isPrimaryServer(){
		return isPrimaryServer;
	}
	
	public DefaultConnectionSelector[] getSecondaryServers(){
		return secondaryServers;
	}
	/**
	 * 设置副服务器的数量，建议2000个链接设置一个副服务器，
	 * 默认为5个副服务器
	 * @param num
	 */
	public void setSecondaryServerNum(int num){
		secondaryServerNum = num;
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
	
	public long getMaxCheckoutTime() {
		return maxCheckoutTime;
	}
	
	public long getConnectTimeout(){
		return connectTimeout;
	}

	public void setConnectTimeout(long timeout){
		connectTimeout = timeout;
	}
	public void setMaxCheckoutTime(long maxCheckoutTime) {
		this.maxCheckoutTime = maxCheckoutTime;
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
	public DefaultConnectionSelector(String host,int port){
		clientModel = false;
		this.host = host;
		this.port = port;
		this.minConnectionNum = 0;
		this.maxConnectionNum = Integer.MAX_VALUE;
	}
	
	/**
	 * 客户端的模式，Socket链接到指定的IP，端口上
	 * @param host 远端SocketServer的ip地址默认值，此地址在创建链接时，如果指定新的host，就用新的host，如果新的host不指定，就用默认值。
	 * @param port 远端SocketServer的port默认值，此地址在创建链接时，如果指定新的port，就用新的port，如果新的port不指定，就用默认值。
	 * @param minConnectionNum 对某一个远端SocketServer，最小链接数
	 * @param maxConnectionNum 对某一个远端SocketServer，最大链接数
	 */
	public DefaultConnectionSelector(String host,int port,int minConnectionNum,int maxConnectionNum){
		clientModel = true;
		this.host = host;
		this.port = port;
		this.minConnectionNum = minConnectionNum;
		this.maxConnectionNum = maxConnectionNum;
		
	}
	
	private ServerSocketChannel ssc;
	
	public void init() throws Exception{
		
		threadPool = new ThreadPoolExecutor(this.threadPoolCorePoolSize,
				this.threadPoolMaxPoolSize, this.threadPoolKeepAlive, TimeUnit.SECONDS, new SynchronousQueue(),
				new ThreadPoolExecutor.AbortPolicy());
		
		
		if(this.enableTimepiece){
			timepieceThread = new TimepieceThread(this);
			timepieceThread.setName(getName()+"-TimePiece-Thread");
			timepieceThread.setDaemon(true);
			timepieceThread.start();
		}
		
		newConnQueue = new DefaultQueue(1024);
		closingConnQueue = new DefaultQueue(1024);
		
		selector = Selector.open();
		if(clientModel == false){
			if(this.newClientConnectHandler == null)
				throw new Exception("ConnectHandler is null");
			
			if(!this.multiServerModel || (this.multiServerModel && this.isPrimaryServer)){
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
			
			if(enableHeapForTimeout){
				this.connTree = new AvlTree(new Comparator(){
					public int compare(Object o1, Object o2) {
						if(o1 == o2) return 0;
						Connection c1 = (Connection)o1;
						Connection c2 = (Connection)o2;
						if(c1.getTimeForNextTimeout() < c2.getTimeForNextTimeout()) return -1;
						if(c1.getTimeForNextTimeout() > c2.getTimeForNextTimeout()) return 1;
						int r = c1.getName().compareTo(c2.getName());
						if(r < 0) return -1;
						if(r > 0) return 1;
						return 0;
					}
					
				});
			}
			
			if(this.multiServerModel && this.isPrimaryServer){
				secondaryServers = new DefaultConnectionSelector[this.secondaryServerNum];
				for(int i = 0 ; i < secondaryServers.length ; i++){
					secondaryServers[i] = new DefaultConnectionSelector();
					secondaryServers[i].setClientModel(false);
					secondaryServers[i].setMultiServerModel(true);
					secondaryServers[i].isPrimaryServer = false;
					secondaryServers[i].setConnectionConnectedHandler(this.newClientConnectHandler);
					secondaryServers[i].setConnectionTerminateHandler(this.terminateHangler);
					secondaryServers[i].setConnectTimeout(this.connectTimeout);
					secondaryServers[i].setEnableHeapForTimeout(this.enableHeapForTimeout);
					secondaryServers[i].setMaxCheckoutTime(maxCheckoutTime);
					secondaryServers[i].setMaxIdelTime(maxIdelTime);
					secondaryServers[i].setName(this.name+":secondary-"+(i+1));
					secondaryServers[i].setSingleThreadMode(singleThreadMode);
					secondaryServers[i].init();
				}
			}
		}
		
		thread = new Thread(this,name + "_Thread");
		thread.start();
	}
	
	
	public void destory() throws Exception{
		
		enableTimepiece = false;
		
		if(this.multiServerModel && this.isPrimaryServer && this.secondaryServers != null){
			for(int i = 0 ;i < secondaryServers.length ; i++){
				secondaryServers[i].destory();
			}
		}
		
		thread.interrupt();
		
		SelectionKey keys[] = selector.keys().toArray(new SelectionKey[0]);
		
		for(int i = 0 ; i < keys.length ; i++){
			SelectionKey sk = keys[i];
			if(sk != null && sk.attachment() != null && sk.attachment() instanceof Connection){
				Connection conn = (Connection)sk.attachment();
				conn.forceTerminate("销毁Selector，关闭连接");
			}
		}
		if(ssc != null){
			ssc.socket().close();
		}
		selector.close();
	}
	
	protected int selectedNum = 0;
	public String getStatusString(){
		int poolThread = threadPool.getPoolSize();
		int size = selector.keys().size() -1;
		
		return "{poolThread:"+poolThread+",poolConn:"+selectedNum+",totalConn:"+size+"}";
	}
	
	/**
	 * 临时存放新的Connection的队列，selector会来检查这个队列，并将这些Connection注册到自己当中。
	 * 
	 * 当Selector在执行select操作的时候，占用了selector，key set, selected key set，这三个对象的锁，
	 * 
	 * 如果此时调用register方法，此方法需要获得selector对象的锁，这样就产生问题。
	 */
	
	protected int getNumOfConnectionNotInSelector(String identity){
		int count = 0;
		try{
			Iterator<Connection> it = newConnectionList.iterator();
			while(it.hasNext()){
				Connection c = it.next();
				if(c.getState() == Connection.CONN_STATE_CLOSE){
					it.remove();
				}else if(identity != null){
					if(identity.equals(c.getIdentity())){
						count ++;
					}
				}else{
					count++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			Iterator it = newConnQueue.iterator();
			while(it.hasNext()){
				Object objs[] = (Object[])it.next();
				Connection c = (Connection)objs[0];
				if(identity != null){
					if(identity.equals(c.getIdentity())){
						count ++;
					}
				}else{
					count++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return count;
	}
	/**
	 * 一个新的链接加入到Selector中，此链接可能已经在Selector中，也可能是新的还没有加入Selector中
	 */
	public void returnConnectoin(Connection conn, int operation) {
		
		if(this.multiServerModel && this.isPrimaryServer){
			logger.error("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] return to PrimaryServer selector"+getStatusString()+"");
			throw new  IllegalArgumentException("connection can't return to PrimaryServer in MultiServerModel");
		}
		
		if(conn.selector != this){
			logger.error("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] return to invalid selector.");
			throw new  IllegalArgumentException("connection can't return to another selector");
		}
		
		if(conn.getState() == Connection.CONN_STATE_CLOSE){
			logger.error("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] can't return to selector cause its state is CLOSE.");
			throw new  IllegalArgumentException("connection can't return to selector cause its state is CLOSE");
		}
		
		
		SelectableChannel channel = (SelectableChannel)conn.channel;
		if(channel.isBlocking()){
			throw new IllegalArgumentException("the connection must be non-blocking model");
		}
		
		lock.lock();
		try{
			SelectionKey sk = channel.keyFor(selector);
			if(sk == null){
				newConnQueue.push(new Object[]{conn,operation});
				newConnectionList.remove(conn);
			}else{
				sk.interestOps(operation);
				sk.attach(conn);
			}
			conn.hasCheckout = false;
			
			if(enableTimepiece){
				conn.lastReturnTime = this.timeOfTimePiece;
			}else{
				conn.lastReturnTime = System.currentTimeMillis();
			}
			
			if(this.enableHeapForTimeout && this.connTree != null){
				conn.getTimeForNextTimeout();
				connTree.insert(conn);
			}

			notEmpty.signalAll();
			
			wakeupSelectorFlag = true;
			selector.wakeup();
			
		}finally{
			
			lock.unlock();
		}
		if(logger.isDebugEnabled()){
			long now = 0;
			if(this.enableTimepiece){
				now = this.timeOfTimePiece;
			}else{
				now = System.currentTimeMillis();
			}
			logger.debug("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] return to selector"+getStatusString()+",using time ["+(now - conn.lastCheckoutTime)+"],and next timeout ["+(conn.getTimeForNextTimeout() - now)+"]ms");
		}
		
	}

	protected void notifyThreadReturnToThreadPool(){
		lock.lock();
		try{
			threadPoolFull.signal();
		}finally{
			lock.unlock();
		}
	}

	/**
	 * 从Selector中取出一个Connection，这个Connection对应的operation将被清空。
	 * 即时这个Connection上有数据到达，或者有空间可以写数据，Selector都不会关心。
	 * 这样做，是为了保证同一个Connetion只有一个线程操作。
	 * 
	 * 此方法返回null表示，没有更多的链接可以用.
	 * 如果在创建新的链接出现错误，则抛出IOException,
	 * 如果所有的链接都在别使用，而且也不能再创建新的链接，则等待一段时间
	 * 
	 * 注意：如果你需要用一个链接，那么必须调用此方法来获得此链接，否则将产生不可预知的结果。
	 *      比如，你保留一个链接的引用，然后不调用此方法直接使用此链接，将产生不可预知的结果。
	 *      一种方法是选择ConnectionSelectPolicy来重新选择一下此链接。
	 *      
	 * @param timeout 等待的时间，如果小于等于0，表示永远等待
	 */
	public Connection takeoutConnection(SelectorPolicy policy,long timeout) throws java.io.IOException{
		if(policy == null) throw new NullPointerException("selector policy is null");
		if(this.multiServerModel && this.isPrimaryServer){
			throw new IllegalArgumentException("connection can't takeout from PrimaryServer in MultiServerModel");
		}
		
		long startTime = 0;
		if(enableTimepiece){
			startTime = this.timeOfTimePiece;
		}else{
			startTime = System.currentTimeMillis();
		}
		if(timeout < 0) timeout = 0;
		long endTime = startTime + timeout;
		
		lock.lock();
		try{
			
			Connection conn = policy.select(this);
			if(conn != null){
				if(logger.isDebugEnabled()){
					long now = 0;
					if(this.enableTimepiece){
						now = this.timeOfTimePiece;
					}else{
						now = System.currentTimeMillis();
					}
					logger.debug("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] takeout from selector "+getStatusString()+" by policy ["+policy+"] ,cost time ["+(now - startTime)+"]");
				}
				if(this.enableHeapForTimeout && this.connTree != null){
					connTree.remove(conn);
				}
				if(this.enableTimepiece){
					conn.lastTakeoutTime = this.timeOfTimePiece;
				}else{
					conn.lastTakeoutTime = System.currentTimeMillis();
				}
				return conn;
			}
			
			while(true){
				long now = 0;
				if(this.enableTimepiece){
					now = this.timeOfTimePiece;
				}else{
					now = System.currentTimeMillis();
				}
				if(timeout > 0 && now >= endTime){
					if(logger.isInfoEnabled()){
						logger.info("["+name+"] no connection takeout from selector "+getStatusString()+" cause timeout for waitting,cost time ["+(now - startTime)+"]");
					}
					return null;
				}
				
				try{
					if(timeout == 0)
						notEmpty.await();
					else
						notEmpty.await(endTime - now,TimeUnit.MILLISECONDS);
				}catch(Exception e){
					logger.error("["+name+"] catch exception while wait.",e);
				}
				
				conn = policy.select(this);
				if(conn != null){
					if(logger.isDebugEnabled()){
						if(this.enableTimepiece){
							now = this.timeOfTimePiece;
						}else{
							now = System.currentTimeMillis();
						}
						logger.debug("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] takeout from selector "+getStatusString()+" by policy ["+policy+"] ,cost time ["+(now - startTime)+"]");
					}
					if(this.enableHeapForTimeout && this.connTree != null){
						connTree.remove(conn);
					}
					if(this.enableTimepiece){
						conn.lastTakeoutTime = this.timeOfTimePiece;
					}else{
						conn.lastTakeoutTime = System.currentTimeMillis();
					}
					return conn;
				}
			}
		}catch(IOException e){
			
			logger.info("["+name+"] no connection takeout from selector "+getStatusString()+" cause exception,cost time ["+(System.currentTimeMillis() - startTime)+"]",e);
			
			throw e;
		}finally{
			lock.unlock();
		}
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
	protected void closeConnection(Connection conn){
		
		if(conn.selector != this){
			logger.error("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] closing to invalid selector.");
			throw new  IllegalArgumentException("connection can't return to another selector");
		}
		
		if(conn.getState() != Connection.CONN_STATE_CLOSE){
			logger.error("["+name+"] connection["+conn.name+","+conn.remoteAddress+"] can't closing connection its state is NOT CLOSE.");
			throw new  IllegalArgumentException("connection can't closing cause its state is NOT CLOSE");
		}
		
		lock.lock();
		try{
			if(this.enableHeapForTimeout && this.connTree != null){
				connTree.remove(conn);
			}
		}finally{
			lock.unlock();
		}
		
		
		closingConnQueue.push(conn);
		
		this.wakeupSelectorFlag = true;
		selector.wakeup();
		
		
	}
	
	/**
	 * 创建一个新的连接，如果当前的线程已经获得lock，那么就释放lock，等链接创建完毕后，在获得lock
	 * @param host
	 * @param port
	 * @return
	 * @throws java.io.IOException
	 */
	protected Connection createConnection(String host,int port) throws java.io.IOException{
		if(isClientModel() == false){
			throw new IOException("server model can't create connection");
		}
		boolean heldByCurrentThread = lock.isHeldByCurrentThread();
		if(heldByCurrentThread){
			lock.unlock();
		}
		try{
			SocketChannel channel = SocketChannel.open();
			InetSocketAddress sa = new InetSocketAddress(host,port);
			if(bindLocalHost != null){
				InetSocketAddress saLocal = new InetSocketAddress(this.bindLocalHost,this.bindLocalPort);
				channel.socket().bind(saLocal);
			}

			channel.socket().connect(sa,(int)connectTimeout);
			
			Connection conn = new Connection(this,channel,null,null,connectionSendBufferSize,connectionReceiveBufferSize,16,3,30 * 1000L,180 * 1000L,30 * 1000L,6 * 60 * 1000L);
			conn.name = genId();
			conn.remoteAddress = "" + sa;
			conn.changeState(Connection.CONN_STATE_OPEN);
			
			try{
				synchronized(newConnectionList){
					if(newConnectionList.size() > 0){
						Iterator<Connection> it = newConnectionList.iterator();
						while(it.hasNext()){
							Connection c = it.next();
							if(c.getState() == Connection.CONN_STATE_CLOSE){
								it.remove();
							}
						}
					}
					newConnectionList.add(conn);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(connectionCreatedHandler != null){
				try{
					connectionCreatedHandler.created(conn,attachment);
				}catch(IOException e){
					conn.forceTerminate("回调创建连接接口，出现异常，关闭连接");
					throw e;
				}
			}
			
			conn.changeState(Connection.CONN_STATE_WAITING_MESSAGE);
			
			channel.configureBlocking(false);
			conn.hasCheckout = true;
			if(terminateHangler != null)
				conn.setConnectionTerminateHandler(terminateHangler);
			
			if(logger.isDebugEnabled()){
				logger.debug("["+name+"] create connection["+conn.name+","+conn.remoteAddress+"] and connect to ["+host+","+port+"] success");
			}
			
			return conn;
		}catch(IOException e){
			logger.warn("["+name+"] create connection to ["+host+","+port+"] failed:",e);
			throw e;
		}finally{
			if(heldByCurrentThread){
				lock.lock();
			}

		}
	}
	
	public void run() {
		long lastAutoCreateTime = 0L;
		int secondaryIndex = 0;
		while(Thread.currentThread().isInterrupted() == false){
			try{
				
				while(!newConnQueue.isEmpty()){
					Object[] objs = (Object[])newConnQueue.pop();
					Connection conn = (Connection)objs[0];
					int operation = (Integer)objs[1];
					SocketChannel channel = (SocketChannel)conn.channel;
					channel.register(selector,operation,conn);
				}
				
				while(!closingConnQueue.isEmpty()){
					Connection conn = (Connection)closingConnQueue.pop();
					conn.forceTerminate("主动调用Connection.close()关闭连接");
				}
				
			
				
				long now = 0;
				if(this.enableTimepiece){
					now = this.timeOfTimePiece;
				}else{
					now = System.currentTimeMillis();
				}
				
				if(now - lastAutoCreateTime > 30000L){
	
					try{
						synchronized(newConnectionList){
							if(newConnectionList.size() > 0){
								Iterator<Connection> it = newConnectionList.iterator();
								while(it.hasNext()){
									Connection c = it.next();
									if(c.getState() == Connection.CONN_STATE_CLOSE){
										it.remove();
									}else if(c.hasCheckout && now - c.lastCheckoutTime > this.maxCheckoutTime && now - c.lastReceiveDataTime > this.maxCheckoutTime){
										c.forceTerminate("新链接超过最大CheckOutTime，强制关闭");
										it.remove();
									}
								}
							}
						}
					}catch(Exception e){
						logger.warn("["+name+"] selector "+getStatusString()+" check new connection list error, list size is "+newConnectionList.size()+"",e);
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
								Connection conn = (Connection)sk.attachment();
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
										Connection conn = createConnection(((InetSocketAddress)sas[i]).getAddress().getHostAddress(),((InetSocketAddress)sas[i]).getPort());
										returnConnectoin(conn,SelectionKey.OP_READ);
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
										Connection conn = (Connection)sk.attachment();
										SocketAddress sa = ((SocketChannel)conn.getChannel()).socket().getRemoteSocketAddress();
										if(sa.equals(sas[i])){
											t++;
											if(t > this.maxConnectionNum){
												if(conn.hasCheckout ==false && conn.getState() == Connection.CONN_STATE_WAITING_MESSAGE){
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
							Connection conn = (Connection)sk.attachment();
							if(conn.hasCheckout){
								long checkoutTime = now - conn.lastCheckoutTime;
								if(checkoutTime > this.maxCheckoutTime 
										&& (now - conn.lastReceiveDataTime > this.maxCheckoutTime)){
									conn.forceTerminate("Checkout超时，关闭连接");
									if(conn.lastHandledMessage != null){
										logger.warn("["+name+"] selector "+getStatusString()+" close connection["+
											conn.name+"] cause reach max checkout time ["+checkoutTime+","+this.maxCheckoutTime+"],last handle message ["+conn.lastHandledMessage.getTypeDescription()+"]");
									}else{
										logger.warn("["+name+"] selector "+getStatusString()+" close connection["+
												conn.name+"] cause reach max checkout time ["+checkoutTime+","+this.maxCheckoutTime+"],last handle message [--]");
									}
								}
							}else{
								long idleTime = now - conn.lastReturnTime;
								long receiveDataTimeout = now - conn.lastReceiveDataTime;
								
								if(maxIdelTime > 0 && idleTime > this.maxIdelTime){
									conn.forceTerminate("Idle超时，关闭连接");
									logger.warn("["+name+"] selector "+getStatusString()+" close connection["+
											conn.name+"] cause reach max idle time ["+idleTime+","+this.maxIdelTime+"]");
									
								}else if(maxReceiveDataTimeout > 0 && receiveDataTimeout > this.maxReceiveDataTimeout){
									conn.forceTerminate("接受数据超时，关闭连接");
									logger.warn("["+name+"] selector "+getStatusString()+" close connection["+
											conn.name+"] cause reach receivedata timeout ["+receiveDataTimeout+","+this.maxReceiveDataTimeout+"]");
								}
								else if(clientModel){
									idleTime = now - conn.lastTakeoutTime;
									if(maxIdelTime > 0 && idleTime > this.maxIdelTime){
										conn.forceTerminate("Idle超时，关闭连接");
										logger.warn("["+name+"] selector "+getStatusString()+" close connection["+
												conn.name+"] cause reach max idle time ["+idleTime+","+this.maxIdelTime+"]");
										
									}
								}
								
							}
						}
					}
					lastAutoCreateTime = now;
				}
				
				long nextTimeout = now + 30000L;
				selectedNum = 0;
				//此方法纯粹是为了在大量链接情况下，提高效率的办法，比如网络游戏服务器，有上万个链接时
				if(this.enableHeapForTimeout && this.connTree != null){
					selectedNum = this.connTree.size();
					lock.lock();
					try{
						TreeNode tn = connTree.minimum();
						if(tn != null){
							Connection conn = (Connection)tn.getObject();
							long tt = conn.getTimeForNextTimeout();
							if(tt < nextTimeout){
								nextTimeout = tt;
							}
						}
					}finally{
						lock.unlock();
					}
				}else{
					Set<SelectionKey> keys = selector.keys();
					Iterator<SelectionKey> it = keys.iterator();
					while(it.hasNext()){
						SelectionKey sk = it.next();
						if(sk.isValid() == false) continue;
						if(sk.interestOps() != 0)
							selectedNum ++;
						if(sk.interestOps() == 0 || sk.attachment() == null) continue;
						Connection conn = (Connection)sk.attachment();
						long tt = conn.getTimeForNextTimeout();
						if(tt < nextTimeout){
							nextTimeout = tt;
						}
					}
				}

				long timeout = nextTimeout - now;
				if(logger.isDebugEnabled()){
					logger.debug("["+name+"] selector "+getStatusString()+" prepared to select for timeout["+timeout+"] ...");
				}
				long startTime = now;
				int k = 0;
				boolean beWakenUp = false;
				
				if(timeout <= 0){
					k = selector.selectNow();
				}else{
					k = selector.select(timeout);
				}
				
				if(wakeupSelectorFlag){
					wakeupSelectorFlag = false;
					beWakenUp = true;
				}
				
				if(this.enableTimepiece){
					now = this.timeOfTimePiece;
				}else{
					now = System.currentTimeMillis();
				}
				
				if(logger.isDebugEnabled()){
					logger.debug("["+name+"] selector "+getStatusString()+" wakeup with return="+k+" for select time["+(now - startTime)+"]");
				}
				
				if(Thread.currentThread().isInterrupted()) break;
				
				//不是被唤醒的情况下，select()方法在设置了timeout的情况下，立即返回，并且返回值为0
				//这种情况，在日志中出现了，出现的时间为2010-4-30日，一直持续到5月2日，
				//期间用户的连接都可以正常连接.
				//并且出现的时候，没有任何用户连接。
				//if(timeout > 10 &&  k == 0 && now == startTime && beWakenUp == false){
					
					//logger.warn("["+name+"] ["+getStatusString()+"] [调试日志，目前不太确定，只是可能出现死循环，"+k+"=select("+timeout+")方法没有阻塞，也没有返回任何事件，也没有任何唤醒标记] [为防止死循环，系统强制主线程sleep(0)]");

//					try{
//						Thread.sleep(10);
//					}catch(InterruptedException e){
//						logger.error("强制主线程Sleep(10)出现异常",e);
//					}
					
				//}
				
				 
				try{
					lock.lockInterruptibly();
				}catch(InterruptedException e){
					e.printStackTrace();
					break;
				}
				try{
					Iterator<SelectionKey> it = selector.selectedKeys().iterator();
					while(it.hasNext()){
						SelectionKey sk = it.next();
						it.remove();
						if(sk.isValid() == false){
							Connection c = (Connection)sk.attachment();
							if(c != null){
								logger.warn("["+name+"] selector checking: ["+c.getName()+"] ["+sk.interestOps()+"] ["+sk.isReadable()+"] ["+sk.isWritable()+"] [SelectionKey非法，直接跳过] ["+Connection.getStateString(c.getState())+"]");
							}else{
								logger.warn("["+name+"] selector checking: [对应连接不存在] ["+sk.interestOps()+"] ["+sk.isReadable()+"] ["+sk.isWritable()+"] [SelectionKey非法，直接跳过] [--]");
							}
							continue;
						}
						try{	
							Connection c = (Connection)sk.attachment();
							if(sk.interestOps() == 0 ){
								logger.warn("["+name+"] selector checking: [对应连接不存在] ["+sk.interestOps()+"] ["+sk.isReadable()+"] ["+sk.isWritable()+"] [没有任何关注的标记,直接跳过] [--]");
								continue;
							}
							if(c != null && logger.isDebugEnabled()){
								logger.debug("["+name+"] selector checking: ["+c.name+"] ["+sk.interestOps()+"] ["+sk.isReadable()+"] ["+sk.isWritable()+"] ["+( c.getTimeForNextTimeout() - now)+"]");
							}
							
							if(sk.isAcceptable()){
								ServerSocketChannel ssc = (ServerSocketChannel)sk.channel();
								SocketChannel sc = ssc.accept();
								if(sc != null && newClientConnectHandler != null){
									DefaultConnectionSelector cs = null;
									if(multiServerModel){
										cs = this.secondaryServers[secondaryIndex++];
										if(secondaryIndex == secondaryServers.length)
											secondaryIndex = 0;
									}else{
										cs = this;
									}
									Connection conn = new Connection(cs,sc,null,null,connectionSendBufferSize,connectionReceiveBufferSize,16,3,30 * 1000L,180 * 1000L,30 * 1000L,6 * 60 * 1000L);
									conn.name = genId();
									conn.remoteAddress = "" + sc.socket().getRemoteSocketAddress();
									conn.changeState(Connection.CONN_STATE_OPEN);
									conn.setConnectionTerminateHandler(terminateHangler);
									
									synchronized(newConnectionList){
										newConnectionList.add(conn);
									}
									
									if(singleThreadMode){
										ExecNewClientConnected executor = new ExecNewClientConnected(cs,newClientConnectHandler,conn);
										executor.run();
									}else{
										try{
											threadPool.execute(new ExecNewClientConnected(cs,newClientConnectHandler,conn));
										}catch(RejectedExecutionException e){
											
											long lll = System.currentTimeMillis() ;
											try{
												threadPoolFull.await(30, TimeUnit.SECONDS);
											}catch(InterruptedException ex){
												ex.printStackTrace();
											}
											
											logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [新的连接接连上来] [ThreadPool_Full] [waiting("+(System.currentTimeMillis() - lll)+")ms]",e);
											
											
											try{
												threadPool.execute(new ExecNewClientConnected(cs,newClientConnectHandler,conn));
											}catch(RejectedExecutionException ex){
												logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [新的连接接连上来] [ThreadPool_Full] [无可用的线程，等待强制关闭新的连接] [cost:"+(System.currentTimeMillis() - lll)+"ms]",ex);
												conn.close();
											}
										}
									}
								}
							}else if(sk.isReadable()){
								Connection conn = (Connection)sk.attachment();
								int interestOps = sk.interestOps();
								sk.interestOps(0);
								conn.hasCheckout = true;
								conn.lastCheckoutTime = now;
								
								
								if(this.enableHeapForTimeout && this.connTree != null){
									connTree.remove(conn);
								}
								if(singleThreadMode){
									try{
										conn.channelReadyToReadData();
									}catch(Exception e){
										logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelReadyToReadData() error and will be stopped:",e);
										conn.forceTerminate("处理channelReadyToReadData出现异常");
									}
								}else{
									try{
										threadPool.execute(new ExecConnectionReadReady(this,conn));
									}catch(RejectedExecutionException e){
										long lll = System.currentTimeMillis();
										try{
											threadPoolFull.await(1, TimeUnit.SECONDS);
										}catch(InterruptedException ex){
											ex.printStackTrace();
										}
										
										logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [有数据可读] [ThreadPool_Full] [waiting("+(System.currentTimeMillis() - lll)+")ms]",e);
										
										
										try{
											threadPool.execute(new ExecConnectionReadReady(this,conn));
										}catch(RejectedExecutionException ex){
											
											sk.interestOps(interestOps);
											conn.hasCheckout = false;
											if(enableTimepiece){
												conn.lastReturnTime = this.timeOfTimePiece;
											}else{
												conn.lastReturnTime = System.currentTimeMillis();
											}
											if(this.enableHeapForTimeout && this.connTree != null){
												conn.getTimeForNextTimeout();
												connTree.insert(conn);
											}
											notEmpty.signalAll();
											
											logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [有数据可读] [ThreadPool_Full] [无可用的线程，不作任何处理，将连接返回给Selector] [cost:"+(System.currentTimeMillis() - lll)+"ms]",ex);
										}
										
										
									}
								}
							}else if(sk.isWritable()){
								Connection conn = (Connection)sk.attachment();
								int interestOps = sk.interestOps();
								sk.interestOps(0);
								conn.hasCheckout = true;
								conn.lastCheckoutTime = now;
								if(this.enableHeapForTimeout && this.connTree != null){
									connTree.remove(conn);
								}
								if(singleThreadMode){
									try{
										conn.channelReadyToWriteData();
									}catch(Exception e){
										logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelReadyToWriteData() error and will be stopped:",e);
										conn.forceTerminate("处理channelReadyToWriteData出现异常");
									}
								}else{
									try{
										threadPool.execute(new ExecConnectionWriteReady(this,conn));
									}catch(RejectedExecutionException e){
										long lll = System.currentTimeMillis();

										try{
											threadPoolFull.await(1, TimeUnit.SECONDS);
										}catch(InterruptedException ex){
											ex.printStackTrace();
										}
										
										logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [有网络缓冲区可写] [ThreadPool_Full] [waiting("+(System.currentTimeMillis() - lll)+")ms]",e);
										
										
										try{
											threadPool.execute(new ExecConnectionWriteReady(this,conn));
										}catch(RejectedExecutionException ex){
											
											sk.interestOps(interestOps);
											conn.hasCheckout = false;
											if(enableTimepiece){
												conn.lastReturnTime = this.timeOfTimePiece;
											}else{
												conn.lastReturnTime = System.currentTimeMillis();
											}
											if(this.enableHeapForTimeout && this.connTree != null){
												conn.getTimeForNextTimeout();
												connTree.insert(conn);
											}
											notEmpty.signalAll();
											
											logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] [有网络缓冲区可写] [ThreadPool_Full] [无可用的线程，不作任何处理，将连接返回给Selector] [cost:"+(System.currentTimeMillis() - lll)+"ms]",ex);
										}
									}
								}
							}else{
								logger.warn("["+name+"] ["+c.getName()+"] [unknow selectionkey interestOps="+sk.interestOps()+"]");
							}
						}catch(Throwable e){
							logger.warn("["+name+"] unkown error while handle selected key in Thread["+Thread.currentThread().getName()+"]",e);
						}
					}
					//此方法纯粹是为了在大量链接情况下，提高效率的办法，比如网络游戏服务器，有上万个链接时
					if(this.enableHeapForTimeout && this.connTree != null){
						ArrayList<Connection> al = new ArrayList<Connection>();
						TreeNode tn = connTree.minimum();
						while(tn != null){
							Connection conn = (Connection)tn.getObject();
							if(conn.getTimeForNextTimeout() < now + 5){
								al.add(conn);
							}else{
								break;
							}
							tn = connTree.next(tn);
						}
						for(int i = 0 ; i < al.size() ; i++){
							Connection conn = al.get(i);
							connTree.remove(conn);
							SelectableChannel channel = (SelectableChannel)conn.channel;
							SelectionKey sk = channel.keyFor(selector);
							if(sk == null)continue;
							if(sk.isValid() == false) continue;
							if(sk.interestOps() == 0 ) continue;
							sk.interestOps(0);
							conn.hasCheckout = true;
							conn.lastCheckoutTime = now;
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
									
									try{
										threadPoolFull.await(60, TimeUnit.SECONDS);
									}catch(InterruptedException ex){
										ex.printStackTrace();
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
					}else if(now - startTime > timeout){//timeout
						it = selector.keys().iterator();
						while(it.hasNext()){
							SelectionKey sk = it.next();
							if(sk.isValid() == false) continue;
							if(sk.interestOps() == 0 ) continue;
							Connection conn = (Connection)sk.attachment();
							if(conn == null) continue;
							
							long tt = conn.getTimeForNextTimeout();
							if(tt <= now + 5){
								sk.interestOps(0);
								conn.hasCheckout = true;
								conn.lastCheckoutTime = now;
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
										try{
											threadPoolFull.await(60, TimeUnit.SECONDS);
										}catch(InterruptedException ex){
											ex.printStackTrace();
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
				}finally{
					lock.unlock();
				}
				
			}catch(Throwable e){
				
				logger.warn("unkown error in Thread["+Thread.currentThread().getName()+"]",e);
				e.printStackTrace();
			}
		}
		
		logger.info("Thread["+Thread.currentThread().getName()+"] will be stopped");
		
	}

	static class ExecNewClientConnected implements Runnable{
		Connection conn = null;
		ConnectionConnectedHandler handler = null;
		DefaultConnectionSelector cs;
		ExecNewClientConnected(DefaultConnectionSelector cs,ConnectionConnectedHandler handler,Connection conn){
			this.conn = conn;
			this.cs = cs;
			this.handler = handler;
		}
		
		public void run() {
			try{
				handler.connected(conn);
				conn.changeState(Connection.CONN_STATE_CONNECT);
				SelectableChannel sc = (SelectableChannel)conn.channel;
				sc.configureBlocking(false);
				conn.changeState(Connection.CONN_STATE_WAITING_MESSAGE);
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
		DefaultConnectionSelector cs;
		InetSocketAddress address;
		ExecCreatingConnection(DefaultConnectionSelector cs,InetSocketAddress address){
			this.cs = cs;
			this.address = address;
		}
		
		public void run() {
			try{
				Connection conn = cs.createConnection(address.getAddress().getHostAddress(),address.getPort());
				cs.returnConnectoin(conn,SelectionKey.OP_READ);
			}catch(Exception e){
				logger.warn("["+Thread.currentThread().getName()+"], create connection failed:",e);
			}finally{
				cs.notifyThreadReturnToThreadPool();
			}
		}
	}
	
	static class ExecConnectionTimeout implements Runnable{
		Connection conn = null;
		DefaultConnectionSelector cs;
		ExecConnectionTimeout(DefaultConnectionSelector cs,Connection conn){
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
		Connection conn = null;
		DefaultConnectionSelector cs;
		ExecConnectionReadReady(DefaultConnectionSelector cs,Connection conn){
			this.conn = conn;
			this.cs = cs;
		}
		
		public void run() {
			try{
				conn.channelReadyToReadData();
			}catch(Exception e){
				logger.warn("["+Thread.currentThread().getName()+"],connection["+conn.name+","+conn.remoteAddress+"] call channelReadyToReadData() error and will be stopped:",e);
				conn.forceTerminate("读取并处理数据时出错："+e.getMessage());
			}finally{
				cs.notifyThreadReturnToThreadPool();
			}
		}
	}
	
	static class ExecConnectionWriteReady implements Runnable{
		Connection conn = null;
		DefaultConnectionSelector cs;
		ExecConnectionWriteReady(DefaultConnectionSelector cs,Connection conn){
			this.conn = conn;
			this.cs = cs;
		}
		
		public void run() {
			try{
				conn.channelReadyToWriteData();
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

	public boolean isEnableTimepiece() {
		return enableTimepiece;
	}

	public void setEnableTimepiece(boolean enableTimepiece) {
		this.enableTimepiece = enableTimepiece;
	}

	public long getStepOfTimePiece() {
		return stepOfTimePiece;
	}

	public void setStepOfTimePiece(long stepOfTimePiece) {
		this.stepOfTimePiece = stepOfTimePiece;
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
