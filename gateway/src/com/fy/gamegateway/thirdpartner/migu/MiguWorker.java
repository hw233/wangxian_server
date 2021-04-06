/**
 * 
 */
package com.fy.gamegateway.thirdpartner.migu;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.mieshi.server.MieshiPlayerInfo;
import com.fy.gamegateway.mieshi.server.MieshiPlayerInfoManager;
import com.fy.gamegateway.mieshi.waigua.AuthorizeManager;
import com.fy.gamegateway.mieshi.waigua.ClientAuthorization;
import com.fy.gamegateway.mieshi.waigua.ClientEntity;


/**
 * 
 *
 */
public class MiguWorker {
	
	public static MieshiPlayerInfo[] queryAllPlayer(String username,String platform,String channel)
	{
		return MieshiPlayerInfoManager.getInstance().getMieshiPlayerInfoByUsername(username);
	}
	
	/**
	 * 去clientauth表中查找对应此clientid的记录，找到最近30天的登录记录
	 * 所谓的最近30天实现如下
	 * 首先根据用户名得到所有的授权记录 
	 * 然后再找到授权对应的cliententity
	 * 迭代cliententity
	 * 对于android 对比其mac地址，发现一样则认为是同一个设备
	 * 然后查看授权时间是否是最近登陆的时间 并且大于等于30天
	 * 若确认则视为合法设备，否则为非法设备
	 * 
	 */
//	public static long LAST_LOGINTIME_GAP = 30 * 3600 * 24 *1000l;
	public static long LAST_LOGINTIME_GAP = 30 * 3600 * 24 *1000l;
	public static Logger logger = MieshiGatewaySubSystem.logger;
	public static Map<String,DeviceInfo> deviceInfoCache = new ConcurrentHashMap<String,DeviceInfo>();
	
	public static long 缓存条目过期时间 = 60 * 60 * 1000l;
	public static volatile boolean isCleanUp = true;
	public static volatile boolean isRunning = false;
	public static Thread cleanThread  = new Thread(new CleanupTask());
	
	//注册72小时的用户都视为非法设备
	public static long REG_NEW_USER_GAP_TIME = 72 * 3600 * 1000l;
	public static long REG_NEW_USER_GAP_TIME_SALE_ROLE = 30*24*60*60*1000L;		//出售角色授权时间验证
	
	static
	{
		cleanThread.setName("MIGU_GATEWAY_DEVICE_CLEAN");
	}
	
	public static class DeviceInfo
	{
		public boolean isValid;
		public long createTime;
	}
	
	public static class CleanupTask implements Runnable
	{
		@Override
		public void run() {
			while(isCleanUp)
			{
				try {
					Thread.sleep(5000l);
					cleanupCacheEntry();
				} catch (InterruptedException e) {
					logger.warn("[米谷通讯] [调用设备缓存清理] [失败] [出现未知异常]",e);
				}
			}
			
		}
	}
	
	public static  void cleanupCacheEntry()
	{
		try
		{
			Iterator<String> iterator = deviceInfoCache.keySet().iterator();
			
			while(iterator.hasNext())
			{
				String key = iterator.next();
				
				DeviceInfo deviceInfo = deviceInfoCache.get(key);
				
				if(deviceInfo != null)
				{
					if((System.currentTimeMillis()-deviceInfo.createTime) >= 缓存条目过期时间)
					{
						iterator.remove();
						if(logger.isInfoEnabled())
							logger.info("[米谷通讯] [设备缓存清理] [成功] ["+key+"] ["+deviceInfo.createTime+"] ["+deviceInfo.isValid+"]");
					}
				}
			}
		}
		catch(Exception e)
		{
			logger.warn("[米谷通讯] [设备缓存清理] [失败] [出现未知异常]",e);
		}
	}
	
	public static boolean checkClient4Changename(String[] deviceInfo, String username) {
		AuthorizeManager authorizeManager = AuthorizeManager.getInstance();
		boolean isValid = false;
		String clientId = deviceInfo[0];
		try {
			List<ClientAuthorization> authList  = authorizeManager.em_ca.query(ClientAuthorization.class, "username=? and clientID=?", new Object[]{username,clientId}, "authorizeTime", 1, 2);
			if (authList == null || authList.size() <= 0) {
				logger.warn("[角色改名] [验证合法设备] [失败] [" + username + "] [clientId:" + clientId + "]");
				return false;
			}
			for (ClientAuthorization au : authList) {
				if (au.getAuthorizeTime() >= (System.currentTimeMillis()-REG_NEW_USER_GAP_TIME)) {
					logger.warn("[角色改名] [验证合法设备] [失败] [" + username + "] [clientId:" + clientId + "] [授权时间:" + au.getAuthorizeTime() + "]");
				} else {
					isValid = true;
					logger.warn("[角色改名] [验证合法设备] [成功] [" + username + "] [clientId:" + clientId + "] [授权时间:" + au.getAuthorizeTime() + "]");
					break;
				}
			}
		} catch (Exception e) {
			logger.warn("[角色改名] [验证合法设备] [异常] [" + username + "]", e);
		}
		logger.warn("[角色改名] [验证设备合法性] [完成] [结果:" + isValid + "] [username:" + username + "] [clientid:" + clientId + "]");
		return isValid; 
	}
	
	/**
	 * 验证合法设备
	 * @param deviceInfo
	 * @param username
	 * @return
	 */
	public static String checkClient4SaleRole(String[] deviceInfo, String username) {
		AuthorizeManager authorizeManager = AuthorizeManager.getInstance();
		boolean isValid = false;
		String result = "没有授权信息，无法出售角色";
		String myMac = deviceInfo[0];
		try {
			ClientAuthorization cz = null;
			List<ClientAuthorization> authList = authorizeManager.getUsernameClientAuthorization(username);
//			List<ClientAuthorization> authList  = authorizeManager.em_ca.query(ClientAuthorization.class, "username=? ", new Object[]{username}, "authorizeTime desc", 1, 2);
			if (logger.isInfoEnabled()) {
				logger.info("[米谷通讯] [上架角色验证] [" + myMac + "] [" + username + "]");
			}
			Set<String> tempClientIds = new HashSet<String>();
 			if(authList != null && authList.size() > 0)
			{
				for (int i=0; i<authList.size(); i++) {
					cz = authList.get(i);
					if (cz != null && cz.getLastLoginTime() >= (System.currentTimeMillis()-REG_NEW_USER_GAP_TIME_SALE_ROLE)) {
						if (!tempClientIds.contains(cz.getClientID())) {
							tempClientIds.add(cz.getClientID());
						}
					}
					if (cz != null && cz.getClientID().equalsIgnoreCase(myMac)) {
						if (cz.getAuthorizeType() == 0) {				//自动授权。判定为主设备
							if (cz.getAuthorizeTime() >= (System.currentTimeMillis()-REG_NEW_USER_GAP_TIME)) {
								isValid = false;
								result = "账号注册＞3天才能出售角色";
								if(logger.isInfoEnabled())
								{
									logger.info("[验证合法设备] [成功] [主设备验证] [授权时间在"+REG_NEW_USER_GAP_TIME/1000/3600+"小时之内] ["+username+"] ["+myMac+"] ["+cz.getAuthorizeTime()+"] ["+cz.getClientID()+"] ");
								}
							} else {
								isValid = true;
								if(logger.isInfoEnabled())
								{
									logger.info("[验证合法设备] [成功] [主设备验证] [授权时间已超过3天] [" + username + "] [" + myMac + "] ["+cz.getAuthorizeTime()+"] ["+cz.getClientID()+"] ");
								}
							}
						} else {					//手动授权。默认为非主设备
							if (cz.getAuthorizeTime() >= (System.currentTimeMillis()-REG_NEW_USER_GAP_TIME_SALE_ROLE)) {
								isValid = false;
								result = "您使用的不是主设备，且被授权＜30天,无法出售角色";
								if(logger.isInfoEnabled())
								{
									logger.info("[验证合法设备] [成功] [非主设备验证] [授权时间在"+REG_NEW_USER_GAP_TIME_SALE_ROLE/1000/3600+"小时之内] ["+username+"] ["+myMac+"] ["+cz.getAuthorizeTime()+"] ["+cz.getClientID()+"] ");
								}
							} else {
								isValid = true;
								if(logger.isInfoEnabled())
								{
									logger.info("[验证合法设备] [成功] [非主设备验证] [授权时间已超过30天] [" + username + "] [" + myMac + "] ["+cz.getAuthorizeTime()+"] ["+cz.getClientID()+"] ");
								}
							}
						}
					}
				}
			}
 			if (tempClientIds.size() > 一月内登陆账号clientId限制个数) {
 				isValid = false;
 				result = "30天内登陆此账号的clientId数超过"+一月内登陆账号clientId限制个数+"个,无法出售角色";
 				if(logger.isInfoEnabled())
				{
					logger.info("[验证合法设备] [成功] [30天内登陆此账号的clientId数超过"+一月内登陆账号clientId限制个数+"个] [" + username + "] [" + myMac + "] ["+tempClientIds.size()+"]");
				}
 			}
		} catch(Exception e) {
			logger.warn("[米谷通讯] [上架角色验证] [异常] [" + Arrays.toString(deviceInfo) + "] [" + username + "]", e);
		}
		if (测试) {
			return null;
		}
		if (isValid) {
			result = null;
		}
		return result;
	}
	
	public static int 一月内登陆账号clientId限制个数 = 8;
	
	public static boolean 测试 = false;
	
	public static boolean checkClient(String[]deviceInfo ,String username)
	{
		if(!isRunning)
		{
			cleanThread.start();
			isRunning = true;
			if(logger.isInfoEnabled())
				logger.info("[米谷通讯] [启动设备缓存清理线程] [成功]");
		}
		
		boolean isValid = false;
		String myMac = deviceInfo[0];
		String cacheKey = buildKey(myMac, username);
		
		DeviceInfo deviceInfo2 = deviceInfoCache.get(cacheKey);
		if(deviceInfo2 != null)
		{
			isValid = deviceInfo2.isValid;
			
			if(logger.isInfoEnabled())
			{
				logger.info("[验证合法设备] [成功] [命中缓存] ["+username+"] ["+myMac+"] ["+isValid+"] ["+cacheKey+"]");
			}
			return isValid;
		}
		
		
		
		AuthorizeManager authorizeManager = AuthorizeManager.getInstance();
		try {
			ClientAuthorization clientAuthorization = null;
			List<ClientAuthorization> authList  = authorizeManager.em_ca.query(ClientAuthorization.class, "username=? ", new Object[]{username}, "authorizeTime", 1, 2);
			if(authList != null && authList.size() > 0)
			{
				clientAuthorization = authList.get(0);
				if(clientAuthorization != null)
				{
					ClientEntity clientEntity = authorizeManager.getClientEntity(clientAuthorization.getClientID());
					
					if(clientEntity != null)
					{
						if(clientAuthorization.getAuthorizeTime() >= (System.currentTimeMillis()-REG_NEW_USER_GAP_TIME))
						{
							isValid = false;
							if(logger.isInfoEnabled())
							{
								logger.info("[验证合法设备] [成功] [授权时间在"+REG_NEW_USER_GAP_TIME/1000/3600+"小时之内] ["+username+"] ["+myMac+"] ["+clientEntity.getMac()+"] ["+clientEntity.getClientID()+"] ["+clientEntity.getCreateTime()+"] ["+clientAuthorization.getAuthorizeTime()+"]");
							}
						}
						
						else if(myMac.equalsIgnoreCase(clientEntity.getMac()))
						{
								isValid = true;
						}
						else
						{
							if(logger.isInfoEnabled())
							{
								logger.info("[验证合法设备] [成功] [mac地址和注册地址不匹配] ["+username+"] ["+myMac+"] ["+clientEntity.getMac()+"] ["+clientEntity.getClientID()+"]");
							}
						}
					}
				}
			}
			else
			{
				logger.warn("[验证合法设备] [失败] [注册设备没有查到授权列表] ["+username+"] ["+myMac+"]");
			}
			
			if(!isValid)
			{
				authList  = authorizeManager.em_ca.query(ClientAuthorization.class, "username=? ", new Object[]{username}, "authorizeTime desc", 1, 2);
				
				
				if(authList != null && authList.size() > 0)
				{
					clientAuthorization = authList.get(0);
					
					//如果mac地址不相等，则说明授权设备发生过变化，根据实际业务来定
					if(clientAuthorization != null)
					{
						long authTime = clientAuthorization.getAuthorizeTime();
						ClientEntity clientEntity = authorizeManager.getClientEntity(clientAuthorization.getClientID());
						if(authTime <=  (System.currentTimeMillis() - LAST_LOGINTIME_GAP))
						{
							//获取cliententity
							
							if(clientEntity != null)
							{
								if(myMac.equalsIgnoreCase(clientEntity.getMac()))
								{
									isValid = true;
								}
								else
								{
									if(logger.isInfoEnabled())
									{
										logger.info("[验证合法设备] [成功] [mac地址不匹配] ["+authTime+"] ["+username+"] ["+myMac+"] ["+clientEntity.getMac()+"] ["+clientEntity.getClientID()+"]");
									}
								}
							}
							else
							{
								logger.warn("[验证合法设备] [失败] [无法获得对应的cliententity] ["+authTime+"] ["+username+"] ["+myMac+"] ["+clientAuthorization.getClientID()+"]");
							}
							
						}
						else
						{
							logger.warn("[验证合法设备] [失败] [授权时间在"+LAST_LOGINTIME_GAP+"内] ["+authTime+"] ["+username+"] ["+myMac+"] ["+clientAuthorization.getClientID()+"]");
						}
					}
					else
					{
						logger.warn("[验证合法设备] [失败] [没有找到授权] ["+username+"] ["+myMac+"]");
					}
					
				}
				else
				{
					logger.warn("[验证合法设备] [失败] [没有查到授权列表] ["+username+"] ["+myMac+"]");
				}
			}
			if (!isValid) {
				List<ClientEntity> list = authorizeManager.em_ce.query(ClientEntity.class, "mac=?", new Object[]{myMac}, "", 1, 10);
				if (list != null && list.size() > 0) {
					for (ClientEntity c : list) {
						List<ClientAuthorization> authList2  = authorizeManager.em_ca.query(ClientAuthorization.class, "username=? and clientID=?", new Object[]{username,c.getClientID()}, "authorizeTime", 1, 10);
						if (authList2 != null && authList2.size() > 0) {
							ClientAuthorization au = authList2.get(0);
							if (au.getAuthorizeTime() < (System.currentTimeMillis()-MiguWorker.REG_NEW_USER_GAP_TIME)) {		//需要授权时间大于3天
								isValid = true;
								logger.info("[验证合法设备] [成功] [查询到设备授权时间在"+(MiguWorker.REG_NEW_USER_GAP_TIME)+"内] [授权时间:"+ au.getAuthorizeTime()+"] ["+username+"] ["+myMac+"] [" + authList2.get(0).getUsername() + "]<br>");
							} else {
								logger.info("[验证合法设备] [失败] [授权时间不在"+(MiguWorker.REG_NEW_USER_GAP_TIME)+"内] [授权时间:"+ au.getAuthorizeTime()+"] ["+username+"] ["+myMac+"] [" + authList2.get(0).getUsername() + "]<br>");
							}
						}
					}
				} else {
					logger.info("[验证合法设备] [失败] [没有获取到ClientEntity] ["+username+"] ["+myMac+"] <br>");
				}
			}
			
		} catch (Exception e) {
			logger.error("[验证合法设备] [失败] [出现未知异常] ["+username+"] ["+deviceInfo[0]+"]",e);
		}
		
		DeviceInfo info = new DeviceInfo();
		info.isValid = isValid;
		info.createTime = System.currentTimeMillis();
		
		deviceInfoCache.put(cacheKey, info);
		if(logger.isInfoEnabled())
			logger.info("[米谷通讯] [设备缓存] [成功] ["+cacheKey+"] ["+info.createTime+"] ["+info.isValid+"]");
		return isValid;
	}
	
	public static String buildKey(String mac,String username)
	{
		return mac+"_"+username;
	}
	
}
