package com.xuanzhi.tools.cache.lateral.concrete;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.cache.lateral.LateralCache;
import com.xuanzhi.tools.cache.lateral.LateralCacheManager;
import com.xuanzhi.tools.text.XmlUtil;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;
import com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter;
import com.xuanzhi.tools.watchdog.ConfigFileChangedListener;

public class DefaultLateralCacheManager implements LateralCacheManager,ConfigFileChangedListener{

	protected ClientAdapter ca;
	protected ServerAdapter sa;
	
	protected File cacheConfigFile;
	
	protected HashMap<String,LateralCache> cacheMap = new HashMap<String,LateralCache>();
	
	public HashMap<String,LateralCache> getCacheMap(){
		return cacheMap;
	}
	/**
	 * 设置配置文件
	 * @param f
	 */
	public void setCacheConfigFile(File f){
		cacheConfigFile = f;
	}
	
	public void setClientAdapter(ClientAdapter ca){
		this.ca = ca;
	}
	
	public void setServerAdapter(ServerAdapter sa){
		this.sa = sa;
	}
	
	public ClientAdapter getClientAdapter(){
		return ca;
	}
	
	public ServerAdapter getServerAdapter(){
		return sa;
	}
	
	public void init() throws Exception{
		Document doc = XmlUtil.load(cacheConfigFile.getAbsolutePath());
		configure(doc.getDocumentElement());
		
		ConfigFileChangedAdapter adapter = ConfigFileChangedAdapter.getInstance();
		adapter.addListener(cacheConfigFile,this);
	}
	
	public void destory(){
		ca.close();
		
		DefaultConnectionSelector selector = (DefaultConnectionSelector)ca.getConnectionSelector();
		try {
			selector.destory();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		ca = null;
		
		selector = (DefaultConnectionSelector)sa.getConnectionSelector();
		try {
			selector.destory();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		sa = null;
		
		cacheMap.clear();
	}
	/**
	 * <pre> 此配置文件一旦发生改变，系统会自动重新加载，
	 *       重新加载就相当于重新启动，所有的状态都恢复原始状态。
	 * <?xml version="1.0"?>
	 * <lateral-cache-manager>
	 * 		<!--  本CacheManager标识，此标识必须和其他机器上配置的标识这个CacheManager的标识一致  -->
	 * 		<identity>xxxxx1</identity>
	 * 		<network-props>
	 * 			<bindHost>211.99.200.91</bindHost>
	 * 			<bindPort>3334</bindPort>
	 * 
	 * 			<!-- 以下这些配置都是有关网络连接参数，都有默认值，实际当中可以不配置 -->
	 * 			<sendQueueSize>4096</sendQueueSize>
	 * 			<minConnectionNum>1</minConnectionNum>
	 * 			<maxConnectionNum>5</maxConnectionNum>
	 * 			<connectTimeout>3000</connectTimeout>
	 * 			<maxIdelTime>600000</maxIdelTime>
	 * 			<maxCheckoutTime>30000</maxCheckoutTime>
	 * 			<maxWindowSize>16</maxWindowSize>
	 * 			<waitingResponseMessageTimeout>30000</waitingResponseMessageTimeout>
	 * 			<waitingRequestMessageTimeout>180000</waitingRequestMessageTimeout>
	 * 			<maxReSendTimes>3</maxReSendTimes>
	 * 			<sendBufferSize>64000</sendBufferSize>
	 * 			<receiveBufferSize>64000</receiveBufferSize>
	 * 		</network-props>		
	 *
	 * 		<peers>
	 * 				<peer identity="xxxxx2" host="211.99.200.90" port="3334"/>
	 * 				<peer identity="xxxxx3" host="211.99.200.92" port="3334"/>
	 * 				<peer identity="xxxxx4" host="211.99.200.77" port="3334"/>
	 * 				<peer identity="xxxxx5" host="211.99.200.70" port="3334"/>
	 * 		</peers>
	 * 		
	 *  	<caches>
	 * 			<cache name="xxxx" model="STRONG_OVERWRITE" max-size="1024" max-life-time="100" max-reference-num="4096" max-reference-life-time="1000"/>
	 *  		<cache name="xxxx2" model="WEAK_OVERWRITE" max-size="1024" max-life-time="1800" max-reference-num="4096" max-reference-life-time="1000"/>
	 *	  	</caches>
	 * </lateral-cache-manager>
	 * 
	 * 
	 * 其中，时间单位都是秒
	 * </pre>
	 * @author myzdf
	 *
	 */
	public void configure(Element root) throws Exception{
		
		Element network = XmlUtil.getChildByName(root,"network-props");
		
		int sendQueueSize = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(network,"sendQueueSize"),4096);
		int minConnectionNum = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(network,"minConnectionNum"),0);
		int maxConnectionNum = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(network,"maxConnectionNum"),5);
		long connectTimeout = XmlUtil.getValueAsLong(XmlUtil.getChildByName(network,"connectTimeout"),3000L);
		long maxIdelTime = XmlUtil.getValueAsLong(XmlUtil.getChildByName(network,"maxIdelTime"),600000L);
		long maxCheckoutTime = XmlUtil.getValueAsLong(XmlUtil.getChildByName(network,"maxCheckoutTime"),30000L);
		
		int maxWindowSize = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(network,"maxWindowSize"),16);
		long waitingResponseMessageTimeout = XmlUtil.getValueAsLong(XmlUtil.getChildByName(network,"waitingResponseMessageTimeout"),30000L);
		long waitingRequestMessageTimeout = XmlUtil.getValueAsLong(XmlUtil.getChildByName(network,"waitingRequestMessageTimeout"),180000L);
		int maxReSendTimes = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(network,"maxReSendTimes"),3);
		long sendBufferSize = XmlUtil.getValueAsLong(XmlUtil.getChildByName(network,"sendBufferSize"),30000L);
		long receiveBufferSize = XmlUtil.getValueAsLong(XmlUtil.getChildByName(network,"receiveBufferSize"),30000L);
		
		if(sa == null){
			String bindHost = XmlUtil.getChildText(network,"bindHost",null);
			if(bindHost != null && bindHost.trim().length() == 0) bindHost = null;
			
			int port = XmlUtil.getValueAsInteger(XmlUtil.getChildByName(network,"bindPort"),3334);
			
			DefaultConnectionSelector cs = new DefaultConnectionSelector(bindHost,port);

			sa = new ServerAdapter();
			sa.setCacheManager(this);
			sa.setConnectionSelector(cs);
			String ipAllows = XmlUtil.getChildText(network,"ipAllows",null);
			if(ipAllows != null && ipAllows.trim().length() > 0)
				sa.setIpAllows(ipAllows);
			
			cs.setConnectionConnectedHandler(sa);
			cs.setName("Server");
			cs.setConnectTimeout(connectTimeout);
			cs.setMaxIdelTime(maxIdelTime);
			cs.setMaxCheckoutTime(maxCheckoutTime);
			
			sa.maxWindowSize = maxWindowSize;
			sa.waitingResponseMessageTimeout = waitingResponseMessageTimeout;
			sa.waitingRequestMessageTimeout = waitingRequestMessageTimeout;
			sa.maxReSendTimes = maxReSendTimes;
			sa.sendBufferSize = sendBufferSize;
			sa.receiveBufferSize = receiveBufferSize;
			
			cs.init();
			sa.init();
			
		}
		
		if(ca == null){
			String identity = XmlUtil.getChildText(root,"identity",null);
			if(identity == null || identity.trim().equals("")) throw new Exception("identity is empty.");
			
			DefaultConnectionSelector cs = new DefaultConnectionSelector(null,0,minConnectionNum,maxConnectionNum);
			ca = new ClientAdapter();
			ca.setIdentity(identity);
			ca.setConnectionSelector(cs);
			ca.setQueueSize(sendQueueSize);
			
			cs.setConnectionCreatedHandler(ca);
			cs.setName("Client");
			cs.setConnectTimeout(connectTimeout);
			cs.setMaxIdelTime(maxIdelTime);
			cs.setMaxCheckoutTime(maxCheckoutTime);
			
			HashMap<String,String> map = new HashMap<String,String>();
			Element peers[] = XmlUtil.getChildrenByName(XmlUtil.getChildByName(root,"peers"),"peer");
			for(int i = 0 ; i < peers.length ; i++){
				String id = XmlUtil.getAttributeAsString(peers[i],"identity",null);
				String host = XmlUtil.getAttributeAsString(peers[i],"host",null);
				int port =  XmlUtil.getAttributeAsInteger(peers[i],"port",3334);
				map.put(id,host+":"+port);
			}
			ca.setAddressMap(map);
			
			ca.maxWindowSize = maxWindowSize;
			ca.waitingResponseMessageTimeout = waitingResponseMessageTimeout;
			ca.waitingRequestMessageTimeout = waitingRequestMessageTimeout;
			ca.maxReSendTimes = maxReSendTimes;
			ca.sendBufferSize = sendBufferSize;
			ca.receiveBufferSize = receiveBufferSize;
			
			cs.init();
			ca.init();
		}
		
		
		Element ces[] = XmlUtil.getChildrenByName(XmlUtil.getChildByName(root,"caches"),"cache");
		for(int i = 0 ; i < ces.length ; i++){
			String name = XmlUtil.getAttributeAsString(ces[i],"name",null);
			int model = XmlUtil.getAttributeAsInteger(ces[i],"model",0);
			if(model == 0){
				model = DefaultLateralCache.getTypeOfModel(XmlUtil.getAttributeAsString(ces[i],"model",null));
			}
			int maxSize = XmlUtil.getAttributeAsInteger(ces[i],"max-size",1024 * 32);
			long maxTime = XmlUtil.getAttributeAsLong(ces[i],"max-life-time",1800L) * 1000;
			int maxRefSize = XmlUtil.getAttributeAsInteger(ces[i],"max-reference-num",maxSize);
			long maxRefTime = XmlUtil.getAttributeAsLong(ces[i],"max-reference-life-time",1800L) * 1000;
			
			DefaultLateralCache cache = new DefaultLateralCache(ca,name,model,maxSize,maxTime,maxRefSize,maxRefTime);
			cacheMap.put(name,cache);
		}
		
	}

	public LateralCache getCache(String name){
		return cacheMap.get(name);
	}

	public void fileChanged(File file) {
		try {
			destory();
			
			Thread.sleep(5000L);
			Document doc = XmlUtil.load(file.getAbsolutePath());
			try{
				configure(doc.getDocumentElement());
			}catch(java.net.BindException e){
				sa = null;
				Thread.sleep(15000L);
				try{
					configure(doc.getDocumentElement());
				}catch(java.net.BindException ex){
					sa = null;
					Thread.sleep(15000L);
					configure(doc.getDocumentElement());
				}
			}
			System.out.println("Lateral Cache Manager ReInitialized Succ with configFile ["+file.getAbsolutePath()+"].");
		} catch (Exception e) {
			System.out.println("Lateral Cache Manager ReInitialized Failed with configFile ["+file.getAbsolutePath()+"].");
			e.printStackTrace(System.out);
		}
		
	}
}
