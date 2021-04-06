package com.fy.gamegateway.stat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.GatewayClient;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.queue.DefaultQueue;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * 统计管理者
 *
 */
public class StatManager implements Runnable{
	static Logger logger = Logger.getLogger(StatManager.class);
	
	static StatManager self;
	
	public static StatManager getInstance(){
		return self;
	}
	
	public SimpleEntityManager<Client2> em4Client = null;
	public SimpleEntityManager<Client> em4Client1 = null;
	public SimpleEntityManager<ClientAccount> em4Account = null;
	
	Thread thread;
	boolean running = true;
	DefaultQueue queue = new DefaultQueue(1024*1024);
	
	LruMapCache clientCache = new LruMapCache(10240,1800000L,"Client-Cache");
	
	public Client2 getClient(String clientId){
		Client2 cl = (Client2)clientCache.get(clientId);
		if(cl != null) return cl;
		try {
			//List<Client> list = em4Client.query(Client.class, "clientId=?",new Object[]{clientId}, "", 1, 2);
			long ids[] = em4Client.queryIds(Client2.class, "clientId=?",new Object[]{clientId}, "", 1, 2);
			if(ids != null && ids.length > 0 ){
				cl = em4Client.find(ids[0]);
			}
			
			if(cl != null){
				clientCache.put(clientId, cl);
			}
			
			return cl;
			
		} catch (Exception e) {
			logger.error("[加载Client] [出现异常] [clientId:"+clientId+"]",e);
			return null;
		}
		
	}
	
	
	
	
	public void init() throws Exception{
		em4Client = SimpleEntityManagerFactory.getSimpleEntityManager(Client2.class);
		em4Client1 = SimpleEntityManagerFactory.getSimpleEntityManager(Client.class);
		em4Account = SimpleEntityManagerFactory.getSimpleEntityManager(ClientAccount.class);
		thread = new Thread(this,"StatManager-Thread");
		thread.start();
		running = true;
		self = this;
	}
	
	public void destroy(){
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		em4Client.destroy();
	}
	/**
	 * 通知统计
	 * 有一个客户端连接上来进行版本检查
	 * 
	 */
	public void notifyVersionCheck(
			String clientId,String channel,
			String platform,String resource,
			String cpv,String crv,String spv,String srv,
			String phoneType,String gpuType,String packageType,String networkType
			){
		Client2 client2 = getClient(clientId);
		
		if(client2 == null)
		{
			notifyFirstRun(clientId, channel, platform, resource, cpv, crv, spv, srv, phoneType, gpuType, packageType, networkType);
			return;
		}
		
		
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("doVersionCheck");
		al.add(clientId);
		al.add(channel);
		al.add(platform);
		al.add(resource);
		al.add(cpv);
		al.add(crv);
		al.add(spv);
		al.add(srv);
		al.add(phoneType);
		al.add(gpuType);
		al.add(packageType);
		al.add(networkType);
		
		queue.push(al);
	}
	
	/**
	 * 通知统计
	 * 有一个客户端连接上来进行版本检查
	 * 
	 */
	public void notifyRegister(String clientId,String username,boolean success,String userSource){
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("doRegister");
		al.add(clientId);
		al.add(username);
		al.add(success);
		al.add(userSource);
	
		
		queue.push(al);
	}
	
	public void notifyResourceProcess(GatewayClient client,String process){
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("doResourceProcess");
		al.add(client);
		al.add(process);
		
		queue.push(al);
	}
	
	/**
	 * 更新UUID，只有原来的UUID没有设置过，才能更新。
	 * 已经设置过UUID，是不更新的。为了解决篡改UUID用黑卡帮助别人充值的问题
	 * @param client
	 * @param uuid
	 * @param deviceId
	 * @param macAddress
	 * @param imsi
	 */
	public void notifyPhoneUUIDUpdate(GatewayClient client,String uuid,String deviceId,String macAddress,String imsi){
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("doPhoneUUIDUpdate");
		al.add(client);
		al.add(uuid);
		al.add(deviceId);
		al.add(macAddress);
		al.add(imsi);
		
		queue.push(al);
	}
	
	/**
	 * 收到消息后进行存储
	 */
	public void notifySaveClientExtendInfo(String clientid,String mobilenum,String username,String os,String channel){
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("doSaveExtendInfo");
		al.add(clientid);
		al.add(mobilenum);
		al.add(username);
		al.add(os);
		al.add(channel);
		
		queue.push(al);
	}
	
	public void doPhoneUUIDUpdate(GatewayClient gclient,String uuid,String deviceId,String macAddress,String imsi){
//		
//		try {
//			Client client = getClient(gclient.getClientId());
//			if(client != null){
//				if(uuid.length() > 0 && (client.uuid == null || client.uuid.length() == 0)){
//					client.uuid = uuid;
//					em4Client.notifyFieldChange(client, "uuid");
//				}
//				if(deviceId.length() > 0 && (client.deviceId == null || client.deviceId.length() == 0)){
//					client.deviceId = deviceId;
//					em4Client.notifyFieldChange(client, "deviceId");
//				}
//				if(macAddress.length() > 0 && (client.macAddress == null || client.macAddress.length() == 0)){
//					client.macAddress = macAddress;
//					em4Client.notifyFieldChange(client, "macAddress");
//				}
//				if(imsi.length() > 0)
//					client.imsi = imsi;
//	
//				em4Client.notifyFieldChange(client, "imsi");
//				
//				//em4Client.save(client);
//			}
//			
//		} catch (Exception e) {
//			logger.error("[处理UUID更新] [出现异常] "+gclient.getLogStr()+"",e);
//			
//		}
	}
	
	
	public void doSaveExtendInfo(String clientid,String mobilenum,String username,String os,String channel){
//		try {
//				//先查对象是否存在
//			List<ClientExtendInfo> lst = getClientExtendInfos(clientid, mobilenum, username, os, channel);
//			
//			if(lst != null)
//			{
//				if(lst.size() > 0)
//				{
//					if(logger.isInfoEnabled())
//					{
//						logger.info("[存储扩展信息] [数据库中已有记录] [clientid:"+clientid+"] [mobilenum:"+mobilenum+"] [username:"+username+"] [os:"+os+"] [channel:"+channel+"]");
//					}
//				}
//				else
//				{
//					ClientExtendInfo c = new ClientExtendInfo();
//					c.setId(em4ExtendInfo.nextId());
//					c.setClientId(clientid);
//					c.setMobilenum(mobilenum);
//					c.setUsername(username);
//					c.setOs(os);
//					c.setChannel(channel);
//					
//					em4ExtendInfo.save(c);
//				}
//			}
//			else
//			{
//				logger.warn("[存储扩展信息] [出现异常:从数据库中获取list为null] [clientid:"+clientid+"] [mobilenum:"+mobilenum+"] [username:"+username+"] [os:"+os+"] [channel:"+channel+"]");
//			}
//			
//			
//		} catch (Exception e) {
//			logger.error("[存储扩展信息] [出现异常] [clientid:"+clientid+"] [mobilenum:"+mobilenum+"] [username:"+username+"] [os:"+os+"] [channel:"+channel+"]",e);
//			
//		}
	}
	
	public List<ClientExtendInfo> getClientExtendInfos(String clientid,String mobilenum,String username,String os,String channel)
	{
		List<ClientExtendInfo> existList = new ArrayList<ClientExtendInfo>();
//		try
//		{
//			existList = em4ExtendInfo.query(ClientExtendInfo.class, "clientId=? and mobilenum=? and username=? and os=? and channel=?",new Object[]{clientid,mobilenum,username,os,channel}, "", 1, 2);
//		}
//		catch(Exception e)
//		{
//			logger.error("[获取扩展信息] [出现异常] [clientid:"+clientid+"] [mobilenum:"+mobilenum+"] [username:"+username+"] [os:"+os+"] [channel:"+channel+"]",e);
//		}
//		
		return existList;
		
		
	}
	
	
	
	/**
	 * 通知统计
	 * 有一个客户端连接上来进行版本检查
	 * 
	 */
	public void notifyLogin(String clientId,String username,boolean success){
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("doLogin");
		al.add(clientId);
		al.add(username);
		al.add(success);
	
		
		queue.push(al);
	}
	
	
	
	public void notifyFirstRun(
			String clientId,String channel,
			String platform,String resource,
			String cpv,String crv,String spv,String srv,
			String phoneType,String gpuType,String packageType,String networkType
			){
		
//		if(AuthorizeManager.getInstance().getClientEntity(clientId) != null) return;
		
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("doFirstRun");
		al.add(clientId);
		al.add(channel);
		al.add(platform);
		al.add(resource);
		al.add(cpv);
		al.add(crv);
		al.add(spv);
		al.add(srv);
		al.add(phoneType);
		al.add(gpuType);
		al.add(packageType);
		al.add(networkType);
		
		queue.push(al);
	}
	
	/**
	 * 通知客户端第一次运行
	 * 有一个客户端第一次运行，则开始进行记录
	 */
	void doFirstRun(
			String clientId,String channel,
			String platform,String resource,
			String cpv,String crv,String spv,String srv,
			String phoneType,String gpuType,String packageType,String networkType
			){
		Client2 client = null;
		try {
			client = getClient(clientId);
			if(client != null){
				
			}else{
				
				
				client = new Client2();
				client.setId(em4Client.nextId());
				
				client.clientId = clientId;
				client.channel = channel;
				client.platform = platform;
				client.resourceName = resource;
				client.phoneType = phoneType;
				client.gpuType = gpuType;
				client.packageType = packageType;

				
				client.timeOfFirstConnected = new Date();
				client.networkTypeOfFirstConnected = networkType;
				client.clientProgramVersionOfFirstConnected = cpv;
				client.clientResourceVersionOfFirstConnected = crv;
				client.serverProgramVersionOfFirstConnected = spv;
				client.serverResourceVersionOfFirstConnected = srv;
				
				int index = 0;
				client.setClientState(index);
				client.set客户端状态(Client2.客户端状态列表[index]);
				
				//em4Client.save(client);
				em4Client.notifyNewObject(client);
				clientCache.put(clientId, client);
			}
			
		} catch (Exception e) {
			logger.error("[记录客户端第一次运行] [出现异常] [clientId:"+clientId+"]",e);
			
		}
	}
	
	
	/**
	 * 通知统计
	 * 有一个客户端连接上来进行版本检查
	 * 
	 */
	void doVersionCheck(
			String clientId,String channel,
			String platform,String resource,
			String cpv,String crv,String spv,String srv,
			String phoneType,String gpuType,String packageType,String networkType
			){
		Client2 client = null;
		try {
			client = getClient(clientId);
			if(client != null){
				
				if(client.getClientState() == 0){
					int index = 1;
					client.setClientState(index);
					client.set客户端状态(Client2.客户端状态列表[index]);
					
					em4Client.notifyFieldChange(client, "clientState");
					em4Client.notifyFieldChange(client, "客户端状态");
				}
			}
			else
			{
//				client = new Client2();
//				client.setId(em4Client.nextId());
//				
//				client.clientId = clientId;
//				client.channel = channel;
//				client.platform = platform;
//				client.resourceName = resource;
//				client.phoneType = phoneType;
//				client.gpuType = gpuType;
//				client.packageType = packageType;
//
//				
//				client.timeOfFirstConnected = new Date();
//				client.networkTypeOfFirstConnected = networkType;
//				client.clientProgramVersionOfFirstConnected = cpv;
//				client.clientResourceVersionOfFirstConnected = crv;
//				client.serverProgramVersionOfFirstConnected = spv;
//				client.serverResourceVersionOfFirstConnected = srv;
//				
//				int index = 0;
//				client.setClientState(index);
//				client.set客户端状态(Client2.客户端状态列表[index]);
//				
//				//em4Client.save(client);
//				em4Client.notifyNewObject(client);
//				clientCache.put(clientId, client);
			}
		} catch (Exception e) {
			logger.error("[处理版本检查] [出现异常] [clientId:"+clientId+"]",e);
			
		}
	}
	
	
	public void notifyStartDownloadResource(String clientId){
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("doStartDownloadResource");
		al.add(clientId);
		queue.push(al);
	}
	
	
	/**
	 * 客户端通知第一次开始下载资源
	 * GET_RESOURCE_REQ
	 * 判断字段是否为空，为空就为第一次下载资源
	 * @param gclient
	 * @param process
	 */
	void doStartDownloadResource(String clientId)
	{
		Client2 client = null;
		try {
			client = getClient(clientId);
			if(client != null){
				
				if(client.getClientState() < 2){
					int index = 2;
					client.setClientState(index);
					client.set客户端状态(Client2.客户端状态列表[index]);
					
					em4Client.notifyFieldChange(client, "clientState");
					em4Client.notifyFieldChange(client, "客户端状态");
				}
			}
		} catch (Exception e) {
			logger.error("[处理版本检查] [出现异常] [clientId:"+clientId+"]",e);
			
		}
	}
	
	/**
	 * 客户端进入注册页面
	 * GET_REGISTER_IMAGE_NEW_R
	 * 判断字段是否为空 为空就为第一次进入注册页面（下载完成）
	 * 
	 * @param gclient
	 * @param process
	 */
	
	
	void doResourceProcess(GatewayClient gclient,String process){
		Client2 client = null;
		try {
			client = getClient(gclient.getClientId());
			if(client != null){
				 if(process.contains("1005 enterLoginModule begin") || process.contains("资源包下载完成") ){
						if(client.getClientState() < 3){
							int index = 3;
							client.setClientState(index);
							client.set客户端状态(Client2.客户端状态列表[index]);
							
							em4Client.notifyFieldChange(client, "clientState");
							em4Client.notifyFieldChange(client, "客户端状态");
						}
					
				}
			
				//em4Client.save(client);
			}
			
		} catch (Exception e) {
			logger.error("[处理客户端进度] [出现异常] "+gclient.getLogStr()+" [process:"+process+"]",e);
			
		}
	}
	
	
	
	
	
	/**
	 * 通知统计
	 * 有一个客户端连接上来进行注册
	 * 
	 */
	void doRegister(String clientId,String username,boolean success,String userSource){
		Client2 client = null;
		try {
			client = getClient(clientId);
			if(client != null){
				
				if(client.hasRegister == false){
					client.hasRegister = true;
					client.firstRegisterTime = new Date();
					em4Client.notifyFieldChange(client, "firstRegisterTime");
					em4Client.notifyFieldChange(client, "hasRegister");
					
				}
				if(success){
					if(client.successRegisterUsernames.contains(username) == false){
						client.successRegisterUsernames.add(username);
						client.successRegisterUserAmount++;
						em4Client.notifyFieldChange(client, "successRegisterUsernames");
						em4Client.notifyFieldChange(client, "successRegisterUserAmount");
					}
				}
				if(client.getClientState() < 4){
					int index = 4;
					client.setClientState(index);
					client.set客户端状态(Client2.客户端状态列表[index]);
					
					em4Client.notifyFieldChange(client, "clientState");
					em4Client.notifyFieldChange(client, "客户端状态");
				}
				
				
			}
		} catch (Exception e) {
			logger.error("[处理注册] [出现异常] [clientId:"+clientId+"] [username:"+username+"]",e);
			
		}
	}
	

	void doLogin(String clientId,String username,boolean success){
		Client2 client = null;
		try {
			client = getClient(clientId);
			if(client != null){
				
				if(client.hasLogin == false){
					client.hasLogin = true;
					client.firstLoginTime = new Date();
					em4Client.notifyFieldChange(client, "hasLogin");
					em4Client.notifyFieldChange(client, "firstLoginTime");
					
					if(client.getClientState() < 5){
						int index = 5;
						client.setClientState(index);
						client.set客户端状态(Client2.客户端状态列表[index]);
						
						em4Client.notifyFieldChange(client, "clientState");
						em4Client.notifyFieldChange(client, "客户端状态");
					}
				}
				client.lastLoginTime = new Date();
				client.lastLoginUsername = username;
				client.loginTimes++;

				em4Client.notifyFieldChange(client, "lastLoginTime");
				em4Client.notifyFieldChange(client, "lastLoginUsername");
				em4Client.notifyFieldChange(client, "loginTimes");
				
				if(success){
					if(client.successLoginUsernames.contains(username) == false){
						client.successLoginUsernames.add(username);
						client.successLoginUserAmount++;
						em4Client.notifyFieldChange(client, "successLoginUsernames");
						em4Client.notifyFieldChange(client, "successLoginUserAmount");
					}
				}
			}
				
//				if(client.逐个下载资源状态 == null || client.逐个下载资源状态.equals(Client.逐个下载资源状态列表[0])){
//					client.逐个下载资源状态 = Client.逐个下载资源状态列表[1];
//					em4Client.notifyFieldChange(client, "逐个下载资源状态");
//				}
//				
//				if(client.逐个下载资源状态.equals(Client.逐个下载资源状态列表[2])){
//					client.逐个下载资源状态 = Client.逐个下载资源状态列表[3];
//					client.逐个下载资源已用时长 = System.currentTimeMillis() - client.逐个下载资源已用时长;
//					
//					em4Client.notifyFieldChange(client, "逐个下载资源状态");
//					em4Client.notifyFieldChange(client, "逐个下载资源已用时长");
//				}
				
				//em4Client.save(client);
//			}
//			
//			ClientAccount ca = getClientAccount(clientId,username);
//			if(ca != null){
//				if(ca.hasSuccessLogin == false){
//					if(success){
//						ca.hasSuccessLogin = true;
//						em4Account.notifyFieldChange(ca, "hasSuccessLogin");
//						ca.loginTimeForFirst = new Date();
//						em4Account.notifyFieldChange(ca, "loginTimeForFirst");
//					}
//				}
//
//				ca.loginTimeForLast = new Date();
//				em4Account.notifyFieldChange(ca, "loginTimeForLast");
//				//em4Account.flush(ca);
//			}else{
//				ca = new ClientAccount();
//				ca.setId(em4Account.nextId());
//				
//				ca.setClientId(clientId);
//				ca.setUsername(username);
//			
//				if(success){
//					ca.hasSuccessLogin = true;
//				}
//				ca.loginTimeForFirst = new Date();
//				ca.loginTimeForLast = new Date();
//				ca.userSource = "未知";
//				ca.loginTimes++;
//				
//				//em4Account.save(ca);
//				em4Account.notifyNewObject(ca);
//				this.clientAcountCache.put(clientId+username, ca);
//			}
		} catch (Exception e) {
			logger.error("[处理登录] [出现异常] [clientId:"+clientId+"] [username:"+username+"]",e);
			
		}
	}
	
	
	public void notifyEnterServer(String clientId){
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("doEnterServer");
		al.add(clientId);
		queue.push(al);
	}
	
	
	/**
	 */
	void doEnterServer(String clientId)
	{
		Client2 client = null;
		try {
			client = getClient(clientId);
			if(client != null){
				
				if(client.getClientState() < 6){
					int index = 6;
					client.setClientState(index);
					client.set客户端状态(Client2.客户端状态列表[index]);
					
					em4Client.notifyFieldChange(client, "clientState");
					em4Client.notifyFieldChange(client, "客户端状态");
				}
			}
		} catch (Exception e) {
			logger.error("[处理版本检查] [出现异常] [clientId:"+clientId+"]",e);
			
		}
	}
	
	public void notifyCreatePlayer(String clientId){
		ArrayList<Object> al = new ArrayList<Object>();
		al.add("doCreatePlayer");
		al.add(clientId);
		queue.push(al);
	}
	
	
	/**
	 */
	void doCreatePlayer(String clientId)
	{
		Client2 client = null;
		try {
			client = getClient(clientId);
			if(client != null){
				
				if(client.getClientState() < 7){
					int index = 7;
					client.setClientState(index);
					client.set客户端状态(Client2.客户端状态列表[index]);
					
					em4Client.notifyFieldChange(client, "clientState");
					em4Client.notifyFieldChange(client, "客户端状态");
				}
			}
		} catch (Exception e) {
			logger.error("[处理版本检查] [出现异常] [clientId:"+clientId+"]",e);
			
		}
	}
	
	
	public void run(){
		//em4Client.setAutoSave(false, 30);
		//em4Account.setAutoSave(false, 30);
		
		while(running){
			try{
				ArrayList<Object> m = (ArrayList<Object>)queue.pop(500);
//				if (running) {
//					continue;
//				}
				if(m != null){
					String s = (String)m.get(0);
					if(s.equals("doFirstRun")){
						doFirstRun(
								(String)m.get(1),
								(String)m.get(2),
								(String)m.get(3),
								(String)m.get(4),
								(String)m.get(5),
								(String)m.get(6),
								(String)m.get(7),
								(String)m.get(8),
								(String)m.get(9),
								(String)m.get(10),
								(String)m.get(11),
								(String)m.get(12)
							);
					}
					else if(s.equals("doStartDownloadResource")){
						doStartDownloadResource(
								(String)m.get(1)
							);
					}
					else if(s.equals("doVersionCheck")){
						doVersionCheck(
								(String)m.get(1),
								(String)m.get(2),
								(String)m.get(3),
								(String)m.get(4),
								(String)m.get(5),
								(String)m.get(6),
								(String)m.get(7),
								(String)m.get(8),
								(String)m.get(9),
								(String)m.get(10),
								(String)m.get(11),
								(String)m.get(12)
							);
					}else if(s.equals("doRegister")){
						doRegister(
								(String)m.get(1),
								(String)m.get(2),
								((Boolean)m.get(3)).booleanValue(),
								(String)m.get(4)
							);
					}else if(s.equals("doLogin")){
						doLogin(
								(String)m.get(1),
								(String)m.get(2),
								((Boolean)m.get(3)).booleanValue()
							);
					}
					else if(s.equals("doResourceProcess")){
						doResourceProcess((GatewayClient)m.get(1),
								(String)m.get(2));
					}
					else if(s.equals("doEnterServer")){
						doEnterServer(
								(String)m.get(1)
							);
					}
					else if(s.equals("doCreatePlayer")){
						doCreatePlayer(
								(String)m.get(1)
							);
					}
//					else if(s.equals("doPhoneUUIDUpdate")){
//						doPhoneUUIDUpdate((GatewayClient)m.get(1),(String)m.get(2),
//								(String)m.get(3),(String)m.get(4),(String)m.get(5));
//					}
//					else if(s.equals("doSaveExtendInfo")){
//						doSaveExtendInfo(
//								(String)m.get(1),
//								(String)m.get(2),
//								(String)m.get(3),
//								(String)m.get(4),
//								(String)m.get(5)
//							);
//					}
				}
			}catch(Throwable e){
				logger.error("[统计线程处理出现异常]",e);
			}
		}
	}
}
