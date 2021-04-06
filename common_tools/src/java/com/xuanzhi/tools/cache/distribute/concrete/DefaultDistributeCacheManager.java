package com.xuanzhi.tools.cache.distribute.concrete;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Iterator;

import org.w3c.dom.Element;

import com.xuanzhi.tools.cache.Cache;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.cache.distribute.*;

import com.xuanzhi.tools.cache.distribute.concrete.protocol.*;

import com.xuanzhi.tools.text.XmlUtil;
import org.apache.log4j.Logger;


/**
 * <pre>
 * 分布式Cache管理类的实现
 * 
 * 具体的使用方法：
 * 		DefaultDistributeCacheManager ddcm = DefaultDistributeCacheManager();
 * 		try{
 *			Element configuration = XmlUtil.load("/usr/local/AProject/conf/ddc.xml").getDocumentElement();
 * 			ddcm.configure(configuration);
 * 		}catch(Exception e){
 * 			System.out.println("分布式Cache配置有问题："+e);
 * 			System.exit(1);
 * 		}
 * 		...
 * 
 * 		DistributeCache dc = ddcm.getDistributeCache("some-name");
 * 		dc.get(...);
 * 		dc.put(...,...);
 * </pre>
 *
 */
public class DefaultDistributeCacheManager implements DistributeCacheManager{

	protected MessageTransport transport = null;
	
	protected Hashtable<String,Message> tables = new Hashtable<String,Message>();
	
	protected Hashtable<String,DistributeCacheGroup> groups = new Hashtable<String,DistributeCacheGroup>();
	
	protected Hashtable<String,DistributeCacheManagerStub> stubManagers = new Hashtable<String,DistributeCacheManagerStub>();
	
	protected LruMapCache getObjectResCache = new LruMapCache(4096,60000L,false,"GetObjectResponseMessageCache");
	
	protected int STUB_MAX_KEY_NUM = 4096;
	protected long STUB_MAX_LIFE_TIME = 1800000L;
	protected int MAX_HEARTBEET_NUM = 16;
	
	protected long intervalOfDiscover = 10 * 60 * 1000L;
	protected long intervalOfHeartBit = 30 * 1000L;
	protected long invokeTimeout = 1000L;
	protected Thread handleThreads[] = null;
	protected Thread heartbitThread = null;
	protected InetAddress broadcastAddress = null;
	protected int port = 0;
	protected int threadNum = 4;
	
	boolean initialized  = false;
	
	protected static Logger logger = Logger.getLogger(DefaultDistributeCacheManager.class);
	
	public DefaultDistributeCacheManager(){
	}
	
	/**
	 * <pre>
	 * &lt;cache-manager class="">
	 *  &lt;!-- 必须项。广播地址，此地址必须正确，否则此方法会抛出SocketException -->
	 *  &lt;broadcast-address>192.168.1.255&lt;/broadcast-address>
	 *  &lt;!-- 必须项。UDP接受消息的端口 -->
	 *  &lt;port>8192&lt;/port>
	 *  &lt;!-- 可选项。处理消息的线程数目，当消息数量庞大的时候，可以适当增加此数目，默认为1 -->
	 *  &lt;handle-thread-num>1&lt;/handle-thread-num>
	 *  &lt;!-- 可选项。从网络上获取其他服务器Cache中的对象的超时时间，默认为1秒，单位是秒-->
	 *  &lt;invoke-timeout>1&lt;/invoke-timeout>
	 *  &lt;!-- 可选项。从网络上发现其他伙伴的时间间隔，每个时间间隔，就将广播发现协议，以发现伙伴，一开始会立即发送一个，默认为600秒 -->	
	 *  &lt;interval-discover>600&lt;/interval-discover>
	 *  &lt;!-- 可选项。心跳协议发送的时间间隔，如果从时间没有收到一个伙伴发送的心跳协议，就认为此伙伴已经死掉了-->
	 *  &lt;interval-heartbit>30&lt;/interval-heartbit>
	 *  &lt;!-- 可选项。一个伙伴最多经历多少个心跳没有回应就认为死了，
	 *  	默认为16，也就是在 16 * 30 = 480秒内如果没有收到一个伙伴的任何消息，就认为此伙伴已经死掉了。-->
	 *  &lt;heart-beet-for-die>16&lt;/heart-beet-for-die>
	 *  &lt;!-- 本地保存远程对应的Cache的Key的最大数目，超过这个数会按照LRU的原则丢弃。
	 *  	每一个Cache都回对应一组远程的Cache，本地部分的保存这些远程Cache中存在的Key，
	 *  	以方便定位某个Key对应的Object在哪个远程Cache中 -->
	 *  &lt;stub-max-life-time>1800&lt;/stub-max-life-time>
	 *  &lt;!-- 本地保存远程对应的Cache的Key的最大生命时间，超过这个时间会按照LRU的原则丢弃 -->
	 *  &lt;stub-max-item-num>4096&lt;/stub-max-item-num>
	 *  &lt;!-- 配置本地的Cache，本地Cache的实现类是com.airinbox.cache.LruMapCache，
	 *  	其中max-size和max-life-time参考com.airinbox.cache.LruMapCache的文档，
	 *  	注意，这里的本地Cache都没有启动校验线程。
	 *  	load-factor是指转载因子,0~1之间一个浮点数，就是当本地Cache容量占总容量的比例达到转载因子时，
	 *  	从远程Cache中获取的(key,object)对就不保持到本地Cache中。这么做是为了防止
	 *  	所以的Cache都缓存同样的内容，导致分布式Cache失去增加Cache容量的功能。默认为0.5 -->
	 * 	&lt;caches>
	 * 		&lt;cache name="aaa" max-size="1024000" max-life-time="1800" load-factor="0.5"/>
	 * 		&lt;cache name="bbb" max-size="1024" max-life-time="60" load-factor="0.0"/>
	 * 		&lt;cache name="bbb" max-size="10240000" max-life-time="60" load-factor="1.0"/>
	 * 	&lt;/caches>
	 * &lt;/cache-manager&gt;
	 * 
	 * 其中，时间单位都是秒
	 * </pre>
	 * @author myzdf
	 *
	 */
	public void configure(Element conf) throws Exception{
		
		if(initialized){
			throw new Exception("alreay configured.You only configure one time.");
		}
		
		String s = XmlUtil.getValueAsString(XmlUtil.getChildByName(conf,"broadcast-address"),null);
		try{
			broadcastAddress = InetAddress.getByName(s);
		}catch(UnknownHostException e){
			throw new Exception("<broadcast-address>["+s+"] is an unknown host.");
		}
		
		this.port = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(conf,"port"));
		this.threadNum = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(conf,"handle-thread-num"),1);
		this.intervalOfDiscover = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(conf,"interval-discover"),600) * 1000L;
		this.intervalOfHeartBit = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(conf,"interval-heartbit"),30) * 1000L;
		this.STUB_MAX_KEY_NUM =  XmlUtil.getValueAsInteger(XmlUtil.getChildByName(conf,"stub-max-item-num"),4096);
		this.STUB_MAX_LIFE_TIME = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(conf,"stub-max-life-time"),1800) * 1000;
		this.MAX_HEARTBEET_NUM = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(conf,"heart-beet-for-die"),16);
		this.invokeTimeout = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(conf,"invoke-timeout"),1) * 1000L;
		try{
			if(transport == null)
				transport = new DefaultMessageTransport(broadcastAddress,port);
		}catch(Exception e){
			throw e;
		}
		
		Element ces[] = XmlUtil.getChildrenByName(XmlUtil.getChildByName(conf,"caches"),"cache");
		
		groups.clear();
		
		for(int i = 0 ; i < ces.length ; i++){
			String name = XmlUtil.getAttributeAsString(ces[i],"name",null);
			int maxSize = XmlUtil.getAttributeAsInteger(ces[i],"max-size",1024 * 1024);
			long maxTime = XmlUtil.getAttributeAsLong(ces[i],"max-life-time",1800L) * 1000;
			float loadFactor = Float.parseFloat(XmlUtil.getAttributeAsString(ces[i],"load-factor","0.5",null));
			
			DefaultDistributeCache cache = new DefaultDistributeCache(name,maxSize,maxTime,loadFactor,invokeTimeout);
			DistributeCacheGroup group = new DistributeCacheGroup(this,cache);
			cache.setDistributeCacheGroup(group);
			
			groups.put(name,group);
		}
		
		handleThreads = new Thread[threadNum];
		for(int i = 0 ; i < threadNum; i ++){
			handleThreads[i] = new HandleThread();
			handleThreads[i].setName("DCM-HandleThread-"+(i+1));
			handleThreads[i].start();
		}
		
		heartbitThread = new HeartBitThread();
		heartbitThread.setName("DCM-HeartBit-Thread");
		heartbitThread.start();
		
		initialized = true;
	}
	
	public Cache getCache(String name) {
		return getDistributeCache(name);
	}
	
	public DistributeCache getDistributeCache(String name){
		DistributeCacheGroup group = getGroup(name);
		if(group != null)
			return group.getCache();
		return null;
	}

	protected DistributeCacheGroup getGroup(String name){
		return groups.get(name);
	}
	
	protected java.util.Set getGetObjectResponseMessageEntrySet(){
		return getObjectResCache.entrySet();
	}
	
	/**
	 * 通过地址获取Manager的存根，如果不存在，就创建一个新的Manager存根返回
	 * @param address
	 * @return
	 */
	protected DistributeCacheManagerStub getManagerStub(InetAddress address){
		DistributeCacheManagerStub ms = stubManagers.get(address.toString());
		if(ms == null){
			ms = new DistributeCacheManagerStub();
			ms.setInetAddress(address);
			stubManagers.put(address.toString(),ms);
		}
		return ms;
	}
	
	private String getStatusAsString(long startTime){
		return "["+(System.currentTimeMillis() - startTime) + "ms]";
	}
	
	/**
	 * <pre>
	 * 从指定的远程Cache获取对象。
	 * 本实现采取了一个也是唯一一个临时的Cache来缓存从远程Cache获取的对象。
	 * 这个临时的Cache最多缓存4096个来自所有远程的对象，缓存的最大生命期为1分钟。
	 * 采用临时Cache的目地是为了解决在很短的一个时间内，有多个线程同时试图从网络中
	 * 获取同样的对象。当其中任意一个线程获得了这个对象，其他线程立即被通知到，并且
	 * 可以共享此对象。
	 * 所以，此方法不一定都是访问网络以获得对象，也可能是通过临时Cache获得对象的。
	 * 
	 * 此方法可能返回null，返回null时，将认为这个远程Cache中不包含指定key，所以
	 * 将会把key从这个远程Cache的Stub中清除掉。
	 * </pre>
	 * @param stub 远程Cache，本地Cache认为这个远程Cache可能包含需要的对象，但不能保证一定包含。
	 * @param key 必须实现java.io.Serializable接口，否则直接返回null
	 * @param timeout 最多等待的时间，这个时间有开始配置的时候指定，就是invoke-timeout参数
	 * @return 远程Cache，以及临时Cache没有需要的对象时，返回null
	 */
	protected Cacheable getObjectFromStub(DistributeCacheStub stub,Object key,long timeout){
		if(!(key instanceof Serializable)){
			return null;
		}
		long startTime = System.currentTimeMillis();
		
		GetObjectReq fo = new GetObjectReq();
		fo.key = (Serializable)key;
		fo.cacheName = stub.getGroup().getName();
		fo.setToAddress(stub.getInetAddress());
		
		String hashKey = fo.getName()+fo.key+fo.cacheName;
		GetObjectRes res = (GetObjectRes)getObjectResCache.get(hashKey);
		if(res != null){
			logger.debug("[get-object-from-stub] [succ] [from-cache] [key:"+key+"] [stub:"+stub+"] [to:"+timeout+"] ["+res.value+"] " + getStatusAsString(startTime));
			return (Cacheable)res.value;
		}
		GetObjectReq lastWaitReq = null;
		synchronized(tables){
			lastWaitReq = (GetObjectReq)tables.get(hashKey);
			if(lastWaitReq == null ){
				lastWaitReq = fo;
				tables.put(hashKey,lastWaitReq);
			}
		}
		
		synchronized(lastWaitReq){
			res = (GetObjectRes)getObjectResCache.get(hashKey);
			if(res != null){
				logger.debug("[get-object-from-stub] [succ] [from-cache] [key:"+key+"] [stub:"+stub+"] [to:"+timeout+"] ["+res.value+"] " + getStatusAsString(startTime));
				return (Cacheable)res.value;
			}
			
			
			transport.send(fo);
			
			try{
				lastWaitReq.wait(timeout);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
			res = (GetObjectRes)getObjectResCache.get(hashKey);
			if(res != null){
				logger.debug("[get-object-from-stub] [succ] [from-stub] [key:"+key+"] [stub:"+stub+"] [to:"+timeout+"] ["+res.value+"] " + getStatusAsString(startTime));
				return (Cacheable)res.value;
			}
		}
		stub.removeKey(key);
		logger.debug("[get-object-from-stub] [fail] [from-stub] [key:"+key+"] [stub:"+stub+"] [to:"+timeout+"] [-] " + getStatusAsString(startTime));
		return null;
	}
	
	/**
	 * <pre>
	 * 通过局域网内的广播，寻找某个包含指定Key的远程Cache，
	 * 然后调用方法<code>getObjectFromStub</code>获取此对象。
	 * 
	 * 类似于getObjectFromStub方法，此方法也采用了一个Cache来缓存从网络上返回的信息。
	 * 不过这个Cache不是临时的，而是每个存根（Stub）中保存的远程Cache中部分的Key。
	 * 这个Cache的参数也是通过configure方法配置的，stub-max-life-time设置这个cache
	 * 的生命期，stub-max-item-num设置最多能保存多少个Key。
	 * 
	 * 另外，此方法一旦阻塞，除了被相应的响应消息唤醒外，也可以被其他远程Cache唤醒。
	 * 比如，我们要获得a对应的对象，但是此时网络中没有，那么当前线程阻塞，直到超时。
	 * 但是，在超时前，某个远程Cache被加入了a对应的对象，这时，这个远程cache会发送
	 * 一个AddObjectReq的广播，本地一旦接收到此消息（包含a），会唤醒等待a的线程。
	 * </pre>
	 * @param group 广播的范围，将在这个组内进行广播，寻找是否有远程Cache包含指定的Key
	 * @param key 必须实现java.io.Serializable接口，否则直接返回null
	 * @param timeout 最多等待的时间，这个时间有开始配置的时候指定，就是invoke-timeout参数
	 * @return 
	 */
	 protected Cacheable findObjectByBroadcast(DistributeCacheGroup group,Object key,long timeout){
		if(!(key instanceof Serializable)){
			return null;
		}
		
		long startTime = System.currentTimeMillis();
		
		Cacheable value = null;
		
		FindObjectReq fo = new FindObjectReq();
		fo.key = (Serializable)key;
		fo.cacheName = group.getName();
		
		String hashKey = fo.getName()+fo.key+fo.cacheName;
		FindObjectReq lastWaitReq = null;
		synchronized(tables){
			lastWaitReq = (FindObjectReq)tables.get(hashKey);
			if(lastWaitReq == null ){
				lastWaitReq = fo;
				tables.put(hashKey,lastWaitReq);
			}
		}
		DistributeCacheStub stub = null;
		
		synchronized(lastWaitReq){
			stub = group.getStubContainsKey(key);
			
			if(stub == null){
				transport.send(fo);
				try{
					//System.out.println("["+Thread.currentThread().getName()+"] will wait on object FindObjectReq["+lastWaitReq+"]."+lastWaitReq.id);
					lastWaitReq.wait(timeout);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				stub = group.getStubContainsKey(key);
				logger.debug("[find-stub-contains-key] [succ] [from-broadcast] [key:"+key+"] [group:"+group+"] [to:"+timeout+"] [stub:"+stub+"] " + getStatusAsString(startTime));
			}else{
				logger.debug("[find-stub-contains-key] [succ] [from-cache] [key:"+key+"] [group:"+group+"] [to:"+timeout+"] [stub:"+stub+"] " + getStatusAsString(startTime));
			}
		}
		
		if(stub != null){
			return getObjectFromStub(stub,key,timeout);
		}

		return null;
	}
	
	/**
     * 广播通知各个机器上的Cache，某个对象值发生了变化
     * 要求其他虚拟机上对应的cache重新装载这个对象
     * @param key
     */
	protected void updateObjectByBroadcast(DistributeCacheGroup group,Object key){
		if(!(key instanceof Serializable)) return;
		
		UpdateObjectReq req = new UpdateObjectReq();
		req.cacheName = group.getName();
		req.key = (Serializable)key;
		
		transport.send(req);
	}

	/**
	 * 通知本地cache在其他机器上的存根，remove了一个对象，
	 * 这个通知信息首先是进入队列，然后有专门发送通知的线程向外广播。
	 * 不保证此信息能及时到达其他机器上的存根
	 * @param key
	 */
	protected void notifyRemoveObject(DistributeCacheGroup group,Object key){
		if(!(key instanceof Serializable)) return;
		
		RemoveObjectReq req = new RemoveObjectReq();
		req.cacheName = group.getName();
		req.key = (Serializable)key;
		
		transport.send(req);
	}
	
	/**
	 * 通知本地cache在其他机器上的存根，put了一个新的对象到本地cache中，
	 * 这个通知信息首先是进入队列，然后有专门发送通知的线程向外广播。
	 * 不保证此信息能及时到达其他机器上的存根
	 * @param key
	 */
	protected void notifyPutObject(DistributeCacheGroup group,Object key){
		if(!(key instanceof Serializable)) return;
		
		AddObjectReq req = new AddObjectReq();
		req.cacheName = group.getName();
		req.key = (Serializable)key;
		
		transport.send(req);
	}
	/**
	 * 通知本地cache在其他机器上的存根，cache被清空了，
	 * 这个通知信息首先是进入队列，然后有专门发送通知的线程向外广播。
	 * 不保证此信息能及时到达其他机器上的存根
	 * @param key
	 */
	protected void notifyClearCache(DistributeCacheGroup group){
		ClearCacheReq req = new ClearCacheReq();
		req.cacheName = group.getName();
		transport.send(req);
	}
	
	protected void sendDiscoverReq(){
		DiscoverReq ds = new DiscoverReq();
		Iterator it = groups.keySet().iterator();
		while(it.hasNext()){
			ds.cacheNameList.add((String)it.next());
		}
		transport.send(ds);
	}
	
	protected void sendHeartBitReq(){
		HeartBitReq req = new HeartBitReq();
		transport.send(req);
	}
	
	/**
	 * <pre>
	 * 处理接受到的消息，此方法包含了所有的消息处理。
	 * 具体的处理过程如下：
	 * 		DISCOVER_REQ：创建相应的存根，并且用DISCOVER_RES消息响应
	 * 
	 * 		DISCOVER_RES：创建相应的存根
	 * 
	 * 	 FIND_OBJECT_REQ：检查本地是否包含感兴趣的Key，如果包含，用FIND_OBJECT_RES响应，否则什么都不做
	 * 
	 *   FIND_OBJECT_RES：在相应的存根中加入key，同时唤醒正在等待这个消息说包含的key的所有的线程
	 *   
	 *    GET_OBJECT_REQ：检查本地是否包含感兴趣的Key，无论有没有，都要用GET_OBJECT_RES来响应
	 *    
	 *    GET_OBJECT_RES：将此消息存入到临时的Cache中，同时唤醒正在等待这个消息包含的对象的所有线程	
	 *    
	 * UPDATE_OBJECT_REQ：将更新的对象从本地Cache中清除，同时特别注意将更新的对象从临时Cache中清除
	 * 
	 *    ADD_OBJECT_REQ：在相应的存根中加入key，同时唤醒正在等待这个消息说包含的key的所有的线程
	 *  
	 * REMOVE_OBJECT_REQ：在相应的存根中清除key
	 * 
	 *   CLEAR_CACHE_REQ：在相应的存根中清除所有的key
	 *   
	 *   	HEARTBIT_REQ：更新对应的Manager存根的状态
	 * </pre>  
	 * @param message
	 */
	protected void handleReceiveMessage(Message message){
		long startTime = System.currentTimeMillis();
		try{
			DistributeCacheManagerStub ms = getManagerStub(message.getFromAddress());
			ms.setMaxHeartBeatNum(MAX_HEARTBEET_NUM);
			
			int type = message.getType();
			DefaultDistributeCache cache = null;
			switch(type){
			case MessageFactory.DISCOVER_REQ:
				DiscoverReq dq = (DiscoverReq)message;
				ms = getManagerStub(dq.getFromAddress());
				
				for(int i = 0 ; i < dq.cacheNameList.size() ; i++){
					String cacheName = (String)dq.cacheNameList.get(i);
					DistributeCacheGroup group = getGroup(cacheName);
					if(group != null && group.getStub(dq.getFromAddress()) == null){
						DistributeCacheStub stub = new DistributeCacheStub(ms,STUB_MAX_KEY_NUM,STUB_MAX_LIFE_TIME);
						stub.setDistributeCacheGroup(group);
						group.addStub(stub);
					}
				}
				
				DiscoverRes ds = new DiscoverRes();
				ds.setToAddress(dq.getFromAddress());
				Iterator it = groups.keySet().iterator();
				while(it.hasNext()){
					ds.cacheNameList.add((String)it.next());
				}
				transport.send(ds);
				break;
			case MessageFactory.DISCOVER_RES:
				ds = (DiscoverRes)message;
				ms = getManagerStub(ds.getFromAddress());
				
				for(int i = 0 ; i < ds.cacheNameList.size() ; i++){
					String cacheName = (String)ds.cacheNameList.get(i);
					DistributeCacheGroup group = getGroup(cacheName);
					if(group != null && group.getStub(ds.getFromAddress()) == null){
						DistributeCacheStub stub = new DistributeCacheStub(ms,STUB_MAX_KEY_NUM,STUB_MAX_LIFE_TIME);
						stub.setDistributeCacheGroup(group);
						group.addStub(stub);
					}
				}
				break;
			case MessageFactory.FIND_OBJECT_REQ:
				FindObjectReq fo = (FindObjectReq)message;
				cache = (DefaultDistributeCache)getCache(fo.cacheName);
				if(cache != null ){
					Object value = cache.get(fo.key,false);
					if(value != null && value instanceof Serializable){
						FindObjectRes fos = new FindObjectRes();
						fos.setToAddress(fo.getFromAddress());
						fos.key = fo.key;
						fos.cacheName = fo.cacheName;
						transport.send(fos);
					}
				}
				break;
			case MessageFactory.FIND_OBJECT_RES:
				FindObjectRes fos = (FindObjectRes)message;
				DistributeCacheGroup group = getGroup(fos.cacheName);
				if(group != null){
					DistributeCacheStub stub = group.getStub(fos.getFromAddress());
					if(stub != null){
						stub.addKey(fos.key);
					}else{
						ms = getManagerStub(fos.getFromAddress());
						stub = new DistributeCacheStub(ms,STUB_MAX_KEY_NUM,STUB_MAX_LIFE_TIME);
						stub.setDistributeCacheGroup(group);
						group.addStub(stub);
						
						stub.addKey(fos.key);
					}
				}
				
				fo = new FindObjectReq();
				fo.key = fos.key;
				fo.cacheName = fos.cacheName;
				String hashKey = fo.getName()+fo.key+fo.cacheName;
				
				FindObjectReq m = (FindObjectReq)tables.get(hashKey);
				if(m != null){
					tables.remove(hashKey);
					synchronized(m){
						m.notifyAll();
					}
				}
				
				break;
			case MessageFactory.GET_OBJECT_REQ:
				GetObjectReq go = (GetObjectReq)message;
				cache = (DefaultDistributeCache)getCache(go.cacheName);
				Object value = null;
				if(cache != null ){
					value = cache.get(go.key,false);
				}
				GetObjectRes gos = new GetObjectRes();
				gos.setToAddress(go.getFromAddress());
				gos.key = go.key;
				gos.cacheName = go.cacheName;
				
				if(value != null && value instanceof Serializable){
					gos.found = true;
					gos.value = (Serializable)value;
				}else{
					gos.found = false;
					gos.value = null;
				}
				transport.send(gos);

				break;
			case MessageFactory.GET_OBJECT_RES:
				gos = (GetObjectRes)message;
				
				go = new GetObjectReq();
				go.key = gos.key;
				go.cacheName = gos.cacheName;
				hashKey = go.getName() + go.key + go.cacheName;
				
				if(gos.found)
					getObjectResCache.put(hashKey,gos);
				
				GetObjectReq mo = (GetObjectReq)tables.get(hashKey);
				if(mo != null){
					tables.remove(hashKey);
					synchronized(mo){
						mo.notifyAll();
					}
				}
				break;
			case MessageFactory.UPDATE_OBJECT_REQ:
				UpdateObjectReq uo = (UpdateObjectReq)message;
				cache = (DefaultDistributeCache)getCache(uo.cacheName);
				if(cache != null && cache.get(uo.key,false) != null){
					cache.remove(uo.key,false);
				}
				go = new GetObjectReq();
				go.key = uo.key;
				go.cacheName = uo.cacheName;
				hashKey = go.getName() + go.key + go.cacheName;
				getObjectResCache.remove(hashKey);
				
				break;
			case MessageFactory.ADD_OBJECT_REQ:
				AddObjectReq ao = (AddObjectReq)message;
				DistributeCacheGroup g = getGroup(ao.cacheName);
				if(g != null){
					DistributeCacheStub stub = g.getStub(ao.getFromAddress());
					if(stub != null){
						stub.addKey(ao.key);
					}else{
						ms = getManagerStub(ao.getFromAddress());
						stub = new DistributeCacheStub(ms,STUB_MAX_KEY_NUM,STUB_MAX_LIFE_TIME);
						stub.setDistributeCacheGroup(g);
						g.addStub(stub);
						
						stub.addKey(ao.key);
					}
					
					fo = new FindObjectReq();
					fo.key = ao.key;
					fo.cacheName = ao.cacheName;
					hashKey = fo.getName()+fo.key+fo.cacheName;
					m = (FindObjectReq)tables.get(hashKey);
					if(m != null){
						tables.remove(hashKey);
						synchronized(m){
							m.notifyAll();
						}
					}
				}
				break;
			case MessageFactory.REMOVE_OBJECT_REQ:
				RemoveObjectReq ro = (RemoveObjectReq)message;
				g = getGroup(ro.cacheName);
				if(g != null){
					DistributeCacheStub stub = g.getStub(ro.getFromAddress());
					if(stub != null){
						stub.removeKey(ro.key);
					}
				}
				break;
			case MessageFactory.CLEAR_CACHE_REQ:
				ClearCacheReq cc = (ClearCacheReq)message;
				g = getGroup(cc.cacheName);
				if(g != null){
					DistributeCacheStub stub = g.getStub(cc.getFromAddress());
					if(stub != null){
						stub.clear();
					}
				}
				break;
			case MessageFactory.HEARTBIT_REQ:
				HeartBitReq hb = (HeartBitReq)message;
				ms = this.getManagerStub(hb.getFromAddress());
				ms.setMaxHeartBeatNum(MAX_HEARTBEET_NUM);
				break;	
			}
			
			
			logger.debug("[handle-message] [succ] ["+Thread.currentThread().getName()+"] "+ message +" "+ getStatusAsString(startTime));
			
		}catch(Throwable e){
			logger.debug("[handle-message] [fail] ["+Thread.currentThread().getName()+"] "+ message +" "+ getStatusAsString(startTime),e);
		}
	}
	
	/**
	 * 清空本地所有关于这个ManagerStub的记录，因为系统已经认为这个ManagerStub对应的服务器已经死掉
	 * @param ms
	 */
	protected void removeDeadStub(DistributeCacheManagerStub ms){
		Iterator it = this.groups.values().iterator();
		while(it.hasNext()){
			DistributeCacheGroup g = (DistributeCacheGroup)it.next();
			DistributeCacheStub stub = g.getStub(ms.getInetAddress());
			if(stub != null){
				stub.clear();
			}
			g.removeStub(ms.getInetAddress());
		}
	}
	
	/**
	 * <pre>
	 * 心跳线程，定期发送HEARTBIT_REQ包和DISCOVER_REQ包，
	 * 同时，每次心跳后，都给所有的ManagerStub的生命值减一。
	 * 检查所有的ManagerStub，一旦发现某个ManagerStub的生命值等于或者小于0，
	 * 就认为这个ManagerStub对应的服务器已经死掉，清空本地所有关于这个ManagerStub的记录。
	 * </pre>
	 * @author myzdf
	 *
	 */
	protected class HeartBitThread extends Thread{
		public void run(){
			long lastDiscoverReqSendTime = 0;
			long lastHeartBitReqSendTime = 0;
			Object lock = new Object(){};
			while(Thread.currentThread().isInterrupted() == false){
				
				try{
					if(System.currentTimeMillis() - lastDiscoverReqSendTime > intervalOfDiscover){
						sendDiscoverReq();
						lastDiscoverReqSendTime = System.currentTimeMillis() ;
					}
					
					if(System.currentTimeMillis() - lastHeartBitReqSendTime > intervalOfHeartBit){
						sendHeartBitReq();
						lastHeartBitReqSendTime = System.currentTimeMillis() ;
						
						Iterator it = stubManagers.values().iterator();
						while(it.hasNext()){
							DistributeCacheManagerStub ms = (DistributeCacheManagerStub)it.next();
							ms.heartBeat();
							
							if(!ms.isAlive()){
								removeDeadStub(ms);
							}
						}
						
					}
					
					synchronized(lock){
						try{
							lock.wait(1000L);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}catch(Throwable e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 消息处理线程，收到一个消息后，立即调用handleReceiveMessage方法。
	 * @author myzdf
	 *
	 */
	protected class HandleThread extends Thread{
		public void run(){
			Object lock = new Object(){};
			while(Thread.currentThread().isInterrupted() == false){
				try{
					Message m = transport.receive(intervalOfHeartBit);
					if(m != null)
						handleReceiveMessage(m);
				}catch(Throwable e){
					e.printStackTrace();
				}
			}
		}
	}
}
