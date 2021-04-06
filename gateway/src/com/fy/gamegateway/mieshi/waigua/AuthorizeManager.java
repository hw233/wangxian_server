package com.fy.gamegateway.mieshi.waigua;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fy.gamegateway.language.Translate;
import com.fy.gamegateway.menu.MenuWindow;
import com.fy.gamegateway.menu.MenuWindowManager;
import com.fy.gamegateway.menu.Option;
import com.fy.gamegateway.menu.Option_Authorize;
import com.fy.gamegateway.menu.Option_AuthorizeRe;
import com.fy.gamegateway.menu.Option_CloseWin;
import com.fy.gamegateway.message.GameMessageFactory;
import com.fy.gamegateway.message.NEW_QUERY_WINDOW_REQ;
import com.fy.gamegateway.mieshi.server.MieshiGatewayServer;
import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.mieshi.server.MieshiServerInfoManager;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.transport.Connection;

public class AuthorizeManager {
	public static boolean isTestNeedAuthorize = true;
	
	public static long waitTime = 10 * 60 * 1000L;
	
	public static long THIRTY_DAY = 30 * 24 * 60 * 60 * 1000L;
	
	public boolean testLog = false;
	
	public boolean closeAuth = true;
	private boolean openning = true;
	
	private static AuthorizeManager instance;
	public static AuthorizeManager getInstance(){
		return instance;
	}
	
	public static Logger logger = NewLoginHandler.logger;

	public SimpleEntityManager<ClientAuthorization> em_ca = null;
	public SimpleEntityManager<ClientEntity> em_ce = null;
	
	public LruMapCache authorizationCache = new LruMapCache(100000, 10 * 60 * 1000L, false, "授权");
	
	public Map<String, ClientAuthorization> waitAuthorizeTishi = new ConcurrentHashMap<String, ClientAuthorization>();
	
	static class AuthorizeList implements Cacheable {
		public List<ClientAuthorization> list = new ArrayList<ClientAuthorization>();

		@Override
		public int getSize() {
			return 1;
		}
	}
	
	public LruMapCache entityCache = new LruMapCache(100000, 10 * 60 * 1000L, false, "ClientEntity");
	
	public void init () {
		instance = this;
		em_ca = SimpleEntityManagerFactory.getSimpleEntityManager(ClientAuthorization.class);
		em_ce = SimpleEntityManagerFactory.getSimpleEntityManager(ClientEntity.class);
		System.out.println("AuthorizeManager  启动成功");
	}
	
	public void destroy() {
		em_ca.destroy();
		em_ce.destroy();
	}
	
	
	
	public boolean isOpenning() {
		return openning;
	}

	public void setOpenning(boolean openning) {
		this.openning = openning;
	}

	/**
	 * 默认主设备或授权超过30天设备可更改VIP资料
	 */
	public boolean modifyVipInfo(String userName,String clientID){
		if(closeAuth){
			return true;
		}
		long startTime = System.currentTimeMillis();
		List<ClientAuthorization> caList = null;
		if(testLog){
			logger.warn("[修改vip授权检查] [userName:"+userName+"] [clientID:"+clientID+"]");
		}
		try {
			caList = getUsernameClientAuthorization(userName);
		} catch (Exception e) {
			logger.error("[授权检查是否可以修改vip信息] [异常] ["+userName+"]", e);
			return false;
		}
		//上次登录时间最久的授权信息
		ClientAuthorization oldLoginAuthor = null;
		//当前clientID登录的授权信息
		ClientAuthorization currAuthor = null;
		for (ClientAuthorization ca : caList) {
			if(ca == null) continue;
			if (ca.getAuthorizeType() == 0) {
				if(oldLoginAuthor == null){
					oldLoginAuthor = ca;
				}else if(oldLoginAuthor.getLastLoginTime() > ca.getLastLoginTime()){
					oldLoginAuthor = ca;
				}
			}
			if(ca.getClientID().equals(clientID)){
				currAuthor = ca;
			}
		}
		if(testLog){
			logger.warn("[修改vip授权检查] [userName:"+userName+"] [clientID:"+clientID+"] [oldClientId:"+(oldLoginAuthor!=null?oldLoginAuthor.getClientID():"0")+"] [授权时间:"+currAuthor!=null?currAuthor.getAuthorizeTime():"nul"+"] [当前时间:"+System.currentTimeMillis()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
		}
		if(oldLoginAuthor != null && oldLoginAuthor.getClientID().equals(clientID)){
			return true;
		}
		if(currAuthor != null && ((currAuthor.getAuthorizeTime()+ THIRTY_DAY )<= System.currentTimeMillis())){
			return true;
		}
		return false;
	}
	
	/**
	 * 此方法表示Server端已经收到协议，并确认登陆协议没问题的情况。
	 * 并且用户名密码未匹配，
	 * 此方法用于检查授权情况，如满足自动授权情况就自动授权，如需要手动授权，生成等待授权信息（只有一个）。
	 * 无授权的情况下返回无授权情况。
	 * 0代表有授权
	 * 1代表自动授权
	 * 2代表需要手动授权
	 * 3代表无授权			如果已经有等待授权
	 * 4出现未知错误
	 * 5拒绝授权
	 */
	public int tryAuthorize (boolean doAuthorize, String ipAddress,String username, String channel, String clientID, String mac, String platform, String phoneType, String gpuType) {
		//if(NewLoginHandler.instance.isCancelAuthorize() && channel != null && channel.equals("139NEWSDK_MIESHI")){
		if(!openning) {
			logger.warn("[有授权] [关闭授权状态] [doAuthorize:"+doAuthorize+"] [username:"+username+"] [channel:"+channel+"] [clientID:"+clientID+"] [mac:"+mac+"] [platform:"+platform+"] [phoneType;"+phoneType+"] [gpuType:"+gpuType+"]");
			return 0;
		}
		List<ClientAuthorization> caList = null;
		try {
			caList = getUsernameClientAuthorization(username);
		} catch (Exception e) {
			logger.error("[取用户授权出错] ["+username+"] ["+clientID+"] ["+mac+"]", e);
			return 4;
		}
		if (isTestNeedAuthorize) {
			if (MieshiGatewaySubSystem.getInstance().getInnerTesterManager().isInnerTester(clientID)) {
				return 0;
			}else if (MieshiServerInfoManager.getInstance().isTestUser(username)) {
				return 0;
			}
		}
		//再检查是否有等待
		for (ClientAuthorization ca : caList) {
			if (ca.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_DENGDAI) {
				//如果是等待
				if (ca.getWaitAuthorizeTime() < System.currentTimeMillis()) {
					ca.setAuthorizeState(ClientAuthorization.AUTHORIZE_STATE_DENGDAI_GUOQI);
					em_ca.notifyFieldChange(ca, "authorizeState");
					waitAuthorizeTishi.put(ca.getUsername(), ca);
				}
			}
		}
		//
		if(waitAuthorizeTishi.containsKey(username)){
			ClientAuthorization ca = waitAuthorizeTishi.get(username);
			if(ca != null && ca.getAuthorizeState() != ClientAuthorization.AUTHORIZE_STATE_DENGDAI_GUOQI){
				waitAuthorizeTishi.remove(username);
			}
		}
		
		//先检查是否有授权
		for (ClientAuthorization ca : caList) {
			if (ca.getClientID().equals(clientID)) {
				if (ca.isAuthorized()) {
					//有授权
					if (doAuthorize) {
						if (ca.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_BEITIDAI) {
							//被替代了 但又登陆了
							ca.setAuthorizeState(ClientAuthorization.AUTHORIZE_STATE_YICHANG);
							em_ca.notifyFieldChange(ca, "authorizeState");
						}
						ca.setLastLoginTime(System.currentTimeMillis());
						em_ca.notifyFieldChange(ca, "lastLoginTime");
						ca.setIp(ipAddress);
						em_ca.notifyFieldChange(ca, "ip");
						ca.setLoginNum(ca.getLoginNum()+1);
						em_ca.notifyFieldChange(ca, "loginNum");
						
						ClientEntity ce = getClientEntity(ca.getClientID());
						if (ce != null) {
							//TODO:关于平台变化可以再加入其它判断，例如又android变成ios应该是肯定不可以的
							ce.setLastActiveTime(System.currentTimeMillis());
							em_ce.notifyFieldChange(ce, "lastActiveTime");
							
							if (channel != null && !channel.equals(ce.getChannel())) {
								ce.setChannel(channel);
								em_ce.notifyFieldChange(ce, "channel");
							}
							
							if ((ce.getMac() == null && mac != null && mac.length() > 0) || (mac != null && mac.length() >0 && !ce.getMac().equalsIgnoreCase(mac))) {
								ce.setMac(mac);
								em_ce.notifyFieldChange(ce, "mac");
							}
							if (platform != null && platform.length() >0 && !ce.getPlatform().equals(platform)) {
								ce.setPlatform(platform);
								em_ce.notifyFieldChange(ce, "platform");
							}
							if (phoneType != null && phoneType.length() >0 && !ce.getPhoneType().equals(phoneType)) {
								ce.setPhoneType(phoneType);
								em_ce.notifyFieldChange(ce, "phoneType");
							}
							if (gpuType != null && gpuType.length() >0 && !ce.getGpuType().equals(gpuType)) {
								ce.setGpuType(gpuType);
								em_ce.notifyFieldChange(ce, "gpuType");
							}
						}
					}
					logger.warn("[有授权] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
					return 0;
				}
			}
		}
		//再检查是否拒绝
		for (ClientAuthorization ca : caList) {
			if (ca.getClientID().equals(clientID)) {
				if (ca.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_JUJUE) {
					logger.warn("[授权已拒绝] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
					return 5;
				}
			}
		}
		
		//再检查是否有等待
		for (ClientAuthorization ca : caList) {
			if (ca.getClientID().equals(clientID)) {
				if (ca.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_DENGDAI) {
					//如果是等待
					logger.warn("[以等待授权] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
					return 2;
					
				}
			}
		}
		//没有匹配到授权
		

		//是否被拒绝
		for (ClientAuthorization ca : caList) {
			if(ca.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_JUJUE){
				ClientEntity ce = this.getClientEntity(ca.getClientID());
				if(ce != null && ce.getMac() != null && ce.getMac().length() > 0 && ce.getMac().equalsIgnoreCase(mac)){
					logger.warn("[授权mac以被拒绝] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
					return 5;
				}
			}
		}
		
		//自动授权
		boolean autoAuthorize = false;
		ClientAuthorization macCA = null;
		ClientAuthorization ipCA = null;
		if (caList.size() == 0) {
			autoAuthorize = true;
		} else {
			//查询是否存在一个已经授权的CA与此MAC一致
			for (ClientAuthorization ca : caList) {
				if(ca.isAuthorized()){
					ClientEntity ce = this.getClientEntity(ca.getClientID());
					if(ce != null && ce.getMac() != null && ce.getMac().length() > 0 && ce.getMac().equalsIgnoreCase(mac)){
						autoAuthorize = true;
						macCA = ca;
					}
				}
			}
			
			//根据ip自动授权,设备相同，且授权的ca和此次登录的mac地址至少有一个为null，ip相同
			if(macCA == null){
				for (ClientAuthorization ca : caList) {
					if(ca.isAuthorized()){
						if(ca.getIp() != null && ca.getIp().length() > 0 && ipAddress != null){
							String ip1 = ca.getIp().split(":")[0];
							String ip2 = ipAddress.split(":")[0];
							if(ip1.equals(ip2)){//ip相同
								ClientEntity ce = this.getClientEntity(ca.getClientID());
								//ip相同，机型相同，mac至少有一个为null
								if(ce != null 
								&& (ce.getMac() == null || mac == null || ce.getMac().length() == 0 || mac.length() == 0)
								&& ce.getPhoneType().equals(phoneType) && ce.getGpuType().equals(gpuType)){
									autoAuthorize = true;
									ipCA = ca;
								}
							}
						}
					}
				}
			}
			//通过MAC和IP都没有匹配上
			//根据ip自动授权,设备相同，此帐号一直都是一个设备，且授权的ca和此次登录的mac地址至少有一个为null，ip前3位相同
			if(macCA == null && ipCA == null){
				int countA = 0;
				ClientAuthorization ca_au = null;
				for (ClientAuthorization ca : caList) {
					if(ca.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_ZHENGCHANG){
						countA++;
						ca_au = ca;
					}
				}
				
				if(countA == 1 && ca_au != null){
					boolean sameIp = false;
					if(ca_au.getIp() != null && ca_au.getIp().length() > 0 && ipAddress != null){
						String ip1 = ca_au.getIp().split(":")[0];
						String ip2 = ipAddress.split(":")[0];
						int ip1_index = ip1.lastIndexOf(".");
						int ip2_index = ip2.lastIndexOf(".");
						if(ip1_index>0 && ip2_index > 0 && ip1.substring(0,ip1_index).equals(ip2.substring(0,ip2_index))){
							//ip相同前3位相同，机型
							sameIp = true;
						}
					}
					
					if(sameIp == true){
						boolean samePhoneType = true;
						ClientEntity ce_au = this.getClientEntity(ca_au.getClientID());
						
						for (ClientAuthorization ca : caList) {
							ClientEntity ce = this.getClientEntity(ca.getClientID());
							if(ca.isAuthorized() == false && ce != null && ce_au != null){
								if((ce_au.getPhoneType() != null && ce_au.getPhoneType().equals(ce.getPhoneType()) == false)
										|| (ce_au.getGpuType() != null && ce_au.getGpuType().equals(ce.getGpuType()) == false)){
									samePhoneType = false;
								}
								if(ce_au.getMac() != null && ce_au.getMac().length() > 0
										&& ce.getMac() != null && ce.getMac().length() > 0
										&& ce_au.getMac().equals(ce.getMac()) == false){
									samePhoneType = false;
								}
							}
						}
						//一种机型，一个授权
						if(samePhoneType){
							if(ce_au != null 
									&& (ce_au.getMac() == null || mac == null || ce_au.getMac().length() == 0 || mac.length() == 0)
									&& ce_au.getPhoneType().equals(phoneType) && ce_au.getGpuType().equals(gpuType)){
								autoAuthorize = true;
								ipCA = ca_au;
							}
						}
					}
				} //end if(countA == 1 && ca_au != null){
			}//end if(macCA == null && ipCA == null){
		}
		
		if(autoAuthorize){
			//有个已授权的
			if (doAuthorize) {
				ClientEntity entity = getClientEntity(clientID);
				if (entity == null) {
					entity = createClientEntity(clientID, channel, mac, platform, phoneType, gpuType);
					if (entity == null) {
						logger.warn("[自动授权] [创建clientEntity出错] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
						return 4;
					}
				}else {
					if (channel != null && !channel.equals(entity.getChannel())) {
						entity.setChannel(channel);
						em_ce.notifyFieldChange(entity, "channel");
					}
				}
				if (macCA != null) {
					macCA.setAuthorizeState(ClientAuthorization.AUTHORIZE_STATE_BEITIDAI);
					em_ca.notifyFieldChange(macCA, "authorizeState");
					ClientAuthorization ca = createClientAuthorization(username, ipAddress, ClientAuthorization.AUTHORIZE_STATE_ZHENGCHANG, entity, 0);
					if (ca == null) {
						logger.warn("[mac相同自动] [创建ClientAuthorization出错] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
						return 4;
					}
					logger.warn("[mac相同自动] [成功] [原mac相同ca信息="+macCA.getLogString()+"] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"] ["+ca.getId()+"] ["+entity.getId()+"]");
				}else if (ipCA != null) {
					ipCA.setAuthorizeState(ClientAuthorization.AUTHORIZE_STATE_BEITIDAI);
					em_ca.notifyFieldChange(ipCA, "authorizeState");
					ClientAuthorization ca = createClientAuthorization(username, ipAddress, ClientAuthorization.AUTHORIZE_STATE_ZHENGCHANG, entity, 0);
					if (ca == null) {
						logger.warn("[ip和机型相同自动] [创建ClientAuthorization出错] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
						return 4;
					}
					logger.warn("[ip和机型相同自动] [成功] [原ip相同ca信息="+ipCA.getLogString()+"] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"] ["+ca.getId()+"] ["+entity.getId()+"]");
				}
				else {
					ClientAuthorization ca = createClientAuthorization(username, ipAddress, ClientAuthorization.AUTHORIZE_STATE_ZHENGCHANG, entity, 0);
					if (ca == null) {
						logger.warn("[自动授权] [创建ClientAuthorization出错] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
						return 4;
					}
					logger.warn("[自动授权] [成功] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"] ["+ca.getId()+"] ["+entity.getId()+"]");
				}
			}
			return 1;
		}
		
		
		//没有自动授权
		boolean waittingAuthorize = false;
		int todayAuthorizeCount = 0;
		
		
		for (ClientAuthorization ca : caList) {
			if(ca.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_DENGDAI){
				if(!ca.getClientID().equals(clientID)){	
					waittingAuthorize = true;
				}
			}
		}
		
		if(waittingAuthorize == false){
			for (ClientAuthorization ca : caList) {
				if(ca.isAuthorized() && System.currentTimeMillis() - ca.getAuthorizeTime() < 24*3600*1000L){
					todayAuthorizeCount++;
				}
			}
			if(todayAuthorizeCount>=5){
				waittingAuthorize = true;
			}
		}
		
		if(waittingAuthorize){
			//代表其他Client在等待授权
			logger.warn("[等待授权或授权过N个或已有其他等待] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"] [今天授权:"+todayAuthorizeCount+"]");
			return 3;
		}
		
		//需要授权
		if (doAuthorize) {
			ClientEntity entity = getClientEntity(clientID);
			if (entity == null) {
				entity = createClientEntity(clientID, channel, mac, platform, phoneType, gpuType);
				if (entity == null) {
					logger.warn("[等待授权] [创建ClientEntity出错] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
					return 4;
				}
			}else {
				if (channel != null && !channel.equals(entity.getChannel())) {
					entity.setChannel(channel);
					em_ce.notifyFieldChange(entity, "channel");
				}
			}
			
			ClientAuthorization waitCA = null;
			for (ClientAuthorization ca : caList) {
				if(ca.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_DENGDAI 
						|| ca.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_DENGDAI_GUOQI){
					if(ca.getClientID().equals(clientID)){	
						waitCA = ca;
					}
				}
			}
			if (waitCA == null) {
				ClientAuthorization ca = createClientAuthorization(username, ipAddress, ClientAuthorization.AUTHORIZE_STATE_DENGDAI, entity, 1);
				if (ca == null) {
					logger.warn("[等待授权] [创建ClientAuthorization出错] ["+doAuthorize+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
					return 4;
				}
				logger.warn("[生成等待授权成功] ["+ca.getLogString()+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
			}else if(waitCA.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_DENGDAI ){
				
				waitCA.setLastLoginTime(System.currentTimeMillis());
				em_ca.notifyFieldChange(waitCA, "lastLoginTime");
				
				logger.warn("[已存在的等待授权] ["+waitCA.getLogString()+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
				
			}else{
				if (waitCA.getQueryNum() > 10) {
					logger.warn("[请求等待授权] [等待过期次数太多] ["+waitCA.getLogString()+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
					return 5;
				}
				waitCA.setAuthorizeState(ClientAuthorization.AUTHORIZE_STATE_DENGDAI);
				em_ca.notifyFieldChange(waitCA, "authorizeState");
				
				waitCA.setWaitAuthorizeTime(System.currentTimeMillis() + waitTime);
				em_ca.notifyFieldChange(waitCA, "waitAuthorizeTime");
				
				waitCA.setLastLoginTime(System.currentTimeMillis());
				em_ca.notifyFieldChange(waitCA, "lastLoginTime");
				
				
				waitCA.setQueryNum(waitCA.getQueryNum() + 1);
				em_ca.notifyFieldChange(waitCA, "queryNum");
				
				logger.warn("[等待授权过期变等待] ["+waitCA.getLogString()+"] ["+username+"] ["+clientID+"] ["+mac+"] ["+platform+"] ["+phoneType+"] ["+gpuType+"]");
			}
		}
		return 2;
	}
	
	/**
	 * A授权B,或者拒绝B
	 * @param clientID_A
	 * @param clientID_B
	 */
	public boolean doManualAuthorize(String username,String clientID_A,String clientID_B,boolean passAuthorize){
		List<ClientAuthorization> caList = null;
		try {
			caList = getUsernameClientAuthorization(username);
		} catch (Exception e) {
			logger.error("doManualAuthorize", e);
			return false;
		}

		boolean authorizedA = false;
		ClientAuthorization from = null;
		for (ClientAuthorization ca : caList) {
			if (ca.getClientID().equals(clientID_A)) {
				if (ca.isAuthorized()) {
					authorizedA = true;
					from = ca;
					break;
				}
			}
		}
		if(authorizedA == false) return false;
		ClientAuthorization ca = getWaittingClientAuthorization(username);
		if(ca == null) return false;
		if(ca.getClientID().equals(clientID_B)){
			ca.setAuthorizeClientId(clientID_A);
			em_ca.notifyFieldChange(ca, "authorizeClientId");
			if(passAuthorize){
				ca.setAuthorizeState(ClientAuthorization.AUTHORIZE_STATE_ZHENGCHANG);
			}else{
				ca.setAuthorizeState(ClientAuthorization.AUTHORIZE_STATE_JUJUE);
			}
			em_ca.notifyFieldChange(ca, "authorizeState");
			
			ca.setAuthorizeTime(System.currentTimeMillis());
			em_ca.notifyFieldChange(ca, "authorizeTime");
			logger.warn("["+(passAuthorize ? "授权" : "拒绝")+"] [成功] ["+username+"] ["+from.getLogString()+"] ["+ca.getLogString()+"]");
			return true;
		}
		return false;
	}
	/**
	 * 返回等待的授权
	 * @param username
	 * @return
	 */
	public ClientAuthorization getWaittingClientAuthorization(String username){
		List<ClientAuthorization> caList = null;
		try {
			caList = getUsernameClientAuthorization(username);
		} catch (Exception e) {
			logger.error("getWaittingClientAuthorization ["+username+"]", e);
			return null;
		}
		for (ClientAuthorization ca : caList) {
			if(ca.getAuthorizeState() == ClientAuthorization.AUTHORIZE_STATE_DENGDAI){
				if (ca.getWaitAuthorizeTime() > System.currentTimeMillis()) {
					return ca;
				}else {
					ca.setAuthorizeState(ClientAuthorization.AUTHORIZE_STATE_DENGDAI_GUOQI);
					em_ca.notifyFieldChange(ca, "authorizeState");
					waitAuthorizeTishi.put(ca.getUsername(), ca);
				}
			}
		}
		return null;
	}
	
	/**
	 * 通过用户名取他的有效授权
	 * 如果出现数据库查询错误，会直接抛出异常，需要调用去处理，不能当成没有授权处理
	 * @param username
	 * @return
	 */
	public List<ClientAuthorization> getUsernameClientAuthorization (String username) throws Exception {
		List<ClientAuthorization> re = null;
		AuthorizeList aL = (AuthorizeList)authorizationCache.get(username);
		if (aL != null) {
			re = aL.list;
			return re;
		}
		long lastTime = System.currentTimeMillis() - 300 * 24 * 60 * 60 * 1000L;
		try{
			re = em_ca.query(ClientAuthorization.class, "username=? and lastLoginTime>?", new Object[]{username, lastTime}, "", 1, 5000);
		}catch(Exception e) {
			logger.error("从数据中取ClientAuthorization出错:", e);
			throw e;
		}
		aL = new AuthorizeList();
		aL.list = re;
		authorizationCache.put(username, aL);
		return re;
	}
	
	/**
	 * 查不到或出错都返回null
	 * @param id
	 * @return
	 */
	public ClientEntity getClientEntity (String clientId) {
		ClientEntity entity = (ClientEntity)entityCache.get(clientId);
		if (entity == null) {
			try {
				List<ClientEntity> list = em_ce.query(ClientEntity.class, "clientID=?", new Object[]{clientId}, "", 1, 2);
				if (list.size() > 0) {
					entity = list.get(0);
				}
			} catch (Exception e) {
				logger.error("从数据中取ClientEntity出错:", e);
			}
			if (entity != null) {
				entityCache.put(clientId, entity);
			}
		}
		return entity;
	}
	
	public ClientEntity createClientEntity (String clientID, String channel, String mac, String platform, String phoneType, String gpuType) {
		try {
			long enID = em_ce.nextId();
			ClientEntity entity = new ClientEntity();
			entity.setId(enID);
			entity.setChannel(channel);
			entity.setClientID(clientID);
			entity.setMac(mac);
			entity.setPhoneType(phoneType);
			entity.setPlatform(platform);
			entity.setGpuType(gpuType);
			em_ce.notifyNewObject(entity);
			entityCache.put(entity.getClientID(), entity);
			return entity;
		} catch (Exception e) {
			logger.error("createClientEntity出错", e);
		}
		return null;
	}
	
	public ClientAuthorization createClientAuthorization (String username, String ip, int state, ClientEntity entity, int type) {
		try{
			long caID = em_ca.nextId();
			ClientAuthorization ca = new ClientAuthorization();
			ca.setId(caID);
			ca.setAuthorizeState(state);
			ca.setClientID(entity.getClientID());
			ca.setAuthorizeTime(System.currentTimeMillis());
			ca.setLastLoginTime(System.currentTimeMillis());
			ca.setIp(ip);
			em_ca.notifyFieldChange(ca, "ip");
			ca.setWaitAuthorizeTime(System.currentTimeMillis() + waitTime);
			ca.setQueryNum(1);
			ca.setAuthorizeType(type);
			ca.setUsername(username);
			em_ca.notifyNewObject(ca);
			AuthorizeList aL = (AuthorizeList)authorizationCache.get(username);
			if (aL == null) {
				aL = new AuthorizeList();
				authorizationCache.put(username, aL);
			}
			aL.list.add(ca);
			return ca;
		}catch(Exception e) {
			logger.error("createClientAuthorization出错", e);
		}
		return null;
	}
	
	public void sendWaitAuthorizationWindow (Connection conn) {
		LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
		if (entity != null) {
			ClientAuthorization ca = getWaittingClientAuthorization(entity.username);
			if (ca != null) {
				ClientEntity ce = getClientEntity(ca.getClientID());
				MenuWindow mw = MenuWindowManager.createMenuWindow(600000);
				Option_Authorize opt1 = new Option_Authorize();
				opt1.setText(Translate.同意授权);
				Option_AuthorizeRe opt2 = new Option_AuthorizeRe();
				opt2.setText(Translate.拒绝授权);
				mw.setOptions(new Option[]{opt2, opt1});
				String title = Translate.申请授权;
				String feng = "";
				long t = System.currentTimeMillis() + 600 * 1000L - ca.getWaitAuthorizeTime();
				t = t /1000/60;
				if (t < 0) {
					t = 1;
				}
				feng = t + "";
				String des = Translate.translateString(Translate.授权说明, new String[][]{{Translate.COUNT_1, feng},{Translate.STRING_1, ce.getPhoneType()}});
				MieshiGatewayServer.getInstance().sendMessageToClient(conn, new NEW_QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getWinId(), title, des, new String[0], new byte[0], new byte[0], new String[0], new byte[0], mw.getOptions()));
				ClientAuthorization waitGuoQi = waitAuthorizeTishi.get(entity.username);
				if (waitGuoQi != null) {
					waitAuthorizeTishi.remove(entity.username);
				}
				logger.warn("[提示用户进行授权操作] ["+conn.getName()+"] ["+mw.getWinId()+"] ["+entity.getLogString()+"] ["+ca.getClientID()+"] ["+ca.getUsername()+"] ["+ce.getMac()+"]");
			}else {
				ClientAuthorization wait = waitAuthorizeTishi.get(entity.username);
				if (wait != null) {
					ClientEntity ce = getClientEntity(wait.getClientID());
					waitAuthorizeTishi.remove(entity.username);
					MenuWindow mw = MenuWindowManager.createMenuWindow(600000);
					Option_CloseWin opt1 = new Option_CloseWin();
					opt1.setText(Translate.确定);
					mw.setOptions(new Option[] {opt1});
					String title = Translate.警告;
					SimpleDateFormat format = new SimpleDateFormat(Translate.yyyy年MM月dd日HH点mm分ss秒);
					String des = Translate.translateString(Translate.等待授权过期说明, new String[][]{{Translate.STRING_1,format.format(new Date(wait.getWaitAuthorizeTime()))},{Translate.STRING_2, ce.getPhoneType()}});
					MieshiGatewayServer.getInstance().sendMessageToClient(conn, new NEW_QUERY_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getWinId(), title, des, new String[0], new byte[0], new byte[0], new String[0], new byte[0], mw.getOptions()));
					logger.warn("[等待授权过期] [提示用户有人登录过] ["+entity.getLogString()+"] ["+waitTime+"]");
				}
			}
		}
	}
}
