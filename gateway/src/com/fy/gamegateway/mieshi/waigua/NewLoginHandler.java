package com.fy.gamegateway.mieshi.waigua;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.authorize.exception.PasswordWrongException;
import com.fy.boss.authorize.exception.UserOnlineException;
import com.fy.boss.authorize.exception.UsernameAlreadyExistsException;
import com.fy.boss.authorize.exception.UsernameNotExistsException;
import com.fy.boss.authorize.model.Passport;
import com.fy.gamegateway.gmaction.GmActionManager;
import com.fy.gamegateway.language.Translate;
import com.fy.gamegateway.message.*;
import com.fy.gamegateway.mieshi.server.DenyUserEntity;
import com.fy.gamegateway.mieshi.server.InnerTesterManager;
import com.fy.gamegateway.mieshi.server.MieshiGatewayServer;
import com.fy.gamegateway.mieshi.server.MieshiGatewaySubSystem;
import com.fy.gamegateway.mieshi.server.MieshiServerInfoManager;
import com.fy.gamegateway.stat.StatManager;
import com.fy.gamegateway.thirdpartner.huawei.HuaWeiBossClientService;
import com.fy.gamegateway.thirdpartner.huiyao.GuoPanClientService;
import com.fy.gamegateway.thirdpartner.huiyao.HuiYaoClientService;
import com.fy.gamegateway.thirdpartner.huiyao.HuoGameClientService;
import com.fy.gamegateway.thirdpartner.huiyao.JiuZhouPiaoMiaoLuClientService;
import com.fy.gamegateway.thirdpartner.j6533.T6533ClientService;
import com.fy.gamegateway.thirdpartner.meizu.MeizuBossClientService;
import com.fy.gamegateway.thirdpartner.meizu.MeizuLoginResult;
import com.fy.gamegateway.thirdpartner.oppo.OppoClientService;
import com.fy.gamegateway.thirdpartner.qq.QQNewV3ClientService;
import com.fy.gamegateway.thirdpartner.quick.QuickClientService;
import com.fy.gamegateway.thirdpartner.sms.JuheDemo;
import com.fy.gamegateway.thirdpartner.sms.SmsData;
import com.fy.gamegateway.thirdpartner.u8.U8ClientService;
import com.fy.gamegateway.thirdpartner.uc.UCLoginResultNew;
import com.fy.gamegateway.thirdpartner.uc.UcBossClientServiceForNew;
import com.fy.gamegateway.thirdpartner.v8.HaoDongClientService;
import com.fy.gamegateway.thirdpartner.v8.V8ClientService;
import com.fy.gamegateway.thirdpartner.xiaomi.XiaomiClientService;
import com.fy.gamegateway.thirdpartner.xmwan.XmWanClientService;
import com.fy.gamegateway.thirdpartner.yijie.YiJieClientService;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.UserRegistFlow;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class NewLoginHandler {
	
	public static NewLoginHandler instance = new NewLoginHandler();
	
	//是否使用hand_1这样的协议SERVER_HAND_CLIENT_11_REQ
	public static boolean isSendHand1 = true;
	//是否使用其他的hand协议
	public static boolean isSendHand2 = true;

	public static Logger logger = Logger.getLogger(NewLoginHandler.class);
	
	//用户登录的私钥
	public static String USER_LOGIN_PRIVATEKEY = "USERLsq20130419";
	
	public static String messagePre = "SERVER_HAND_CLIENT_";
	public static int messageNum = 100;
	public static int sendValueNum = 100;
	//开启版本登录限制
	public static boolean openVersionLoginLimit = true;
	//限制最低版本串
	public static String lowVersion = "2.0.10";
	
	public static boolean channelUpdateResource = true;
	
	private boolean cancelAuthorize;
	
	InnerTesterManager innerTesterManager;
	
	public static Map<String, String> errorUsers = new HashMap<String, String>();
	
	static{
		errorUsers.put("MEIZUUSER_113752581", "MEIZUUSER_a0_5688b0eb2007b");
	}
	
	LruMapCache userLoginMap = new LruMapCache(102400,3600 * 1000L,"CMCC139-Cache");
	
	public static void printLoginLog(Connection conn,LoginEntity entity,boolean success,String reason,String sessionId,String message){
		logger.warn("[用户登录] ["+(success?"成功":"失败")+"] [原因:" + reason + "] [" + conn.getName() + "] [SID:"+sessionId+"] " + entity.getLogString()+" " + message);
	}
	
	public static void printLoginLog(Connection conn,LoginEntity entity,boolean success,String reason,String sessionId,String message,Exception e){
		logger.error("[用户登录] ["+(success?"成功":"失败")+"] [原因:" + reason + "] [" + conn.getName() + "] [SID:"+sessionId+"] " + entity.getLogString()+" " + message,e);
	}
	
	public static String getConnIp (Connection conn) {
		if (conn != null) {
			String ipAddress = conn.getRemoteAddress();
			return ipAddress;
		}
		return "";
	}
	
	public void putLoginUser(String key,String userId,Connection conn,LoginEntity entity){
		userLoginMap.put(key, new LoginInfo(userId,conn,entity));
	}
	
	public LoginInfo getUserIdByKey(String key){
		LoginInfo userId = (LoginInfo)userLoginMap.get(key);
		if(null == userId || userId.equals("")){
			logger.warn("[获取移动登陆信息] [getUserIdByKey] [失败:该用户未登陆] [key:"+key+"] [userId:--]");
		}else{
			userLoginMap.remove(key);
			logger.info("[获取移动登陆信息] [getUserIdByKey] [成功] [key:"+key+"] [userId:"+userId+"]");
		}
		
		return userId;
	}
	
	public int[] stringToInt(String[] a) throws Exception{
		int [] fs = new int[a.length];
		for(int k=0;k<a.length;k++){
			if(a[k]!=null && a[k].trim().length()>0){
				fs[k] = Integer.parseInt(a[k]);
			}
		}
		return fs;
	}
	
	public boolean isCancelAuthorize(){
		return cancelAuthorize;
	}
	
	
	
	public void setCancelAuthorize(boolean cancelAuthorize) {
		this.cancelAuthorize = cancelAuthorize;
	}

	public void ca(String ca){
		if(ca != null && ca.equals("5tgbnhy67ujm,ki8")){
			cancelAuthorize = true;
		}else{
			cancelAuthorize = false;
		}
	}
	
	private static int [] gene = {10000,100,1};
	
	/**
	 * 登录版本检查
	 * @param version2
	 * @return
	 * @throws Exception
	 */
	public boolean isCanLogin(String version2) throws Exception{
		if(!openVersionLoginLimit){
			return true;
		}
		if(version2 != null){
			int vnum1[] = stringToInt(lowVersion.split("\\."));
			int vnum2[] = stringToInt(version2.split("\\."));
			if(vnum1.length != vnum2.length){
				return false;
			}
			
			int oldTotleNums = 0;
			int newTotleNums = 0;
			for(int i=0;i<vnum1.length;i++){
				oldTotleNums += gene[i]*vnum1[i];
			}
			for(int i=0;i<vnum2.length;i++){
				newTotleNums += gene[i]*vnum2[i];
			}
			return newTotleNums > oldTotleNums;
		}
		return false;
	}
	
	public static boolean openTestLog = false;
	
	public NEW_USER_LOGIN_RES doLogin (Connection conn,LoginEntity entity) {
		try{
			String username = entity.username;
			String clientId = entity.clientId;
			String mac =entity.mac;
			String platform =entity.platform;
			
//			if(platform != null && mac != null){
//				if(platform.toLowerCase().contains("android"))
//					mac = mac.toLowerCase();
//			}
			
			String phoneType =entity.phoneType;
			String gpuType =entity.gpuType;
			long sequnceNum =entity.sequnceNum;
			username = username.trim();
			// 去掉英文空格
			username = username.replaceAll(" ", "");
			// 去掉中文空格
			username = username.replaceAll(" ", "");
			if(!entity.userType.toLowerCase().contains("korea") && !entity.userType.toLowerCase().contains("malai") && !entity.userType.toLowerCase().contains("kunlun")&& !entity.userType.toLowerCase().contains("qquser")&& !entity.userType.toLowerCase().contains("quickuser"))
			{
				//限制用户名和密码的长度
				if(username.length() > 50)
				{
					String reason = "用户名已超过最大长度，请确认输入用户名是否正确";
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4, reason, "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,reason,"-","-");
					return res;
				}
				if(!entity.channel.toLowerCase().contains("meizu") && !entity.channel.toLowerCase().contains("huaweisdk")&& !entity.channel.toLowerCase().contains("yijie")){
					if(entity.passwd.length() > 200)
					{
						String reason = "密码已超过最大长度，请确认输入密码是否正确";
						NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4, reason, "", "登录失败","",new String[0]);
						printLoginLog(conn,entity,false,reason,"-","-");
						return res;
					}
				}
				
			}
			
			
			//黑名单
			MieshiServerInfoManager sm = MieshiServerInfoManager.getInstance();
			DenyUserEntity du = sm.getDenyUser(username);
			if (du != null) {
				if (du != null && du.isEnableDeny()
						&& (du.isForoverDeny() || (System.currentTimeMillis() > du.getDenyStartTime() && System.currentTimeMillis() < du.getDenyEndTime()))) {
					String reason = "";
					if (du.isForoverDeny()) {
						reason = Translate.translateString(Translate.登录失败您被永久性限制登录, new String[][]{{Translate.STRING_1,du.getReason()}});
					} else {
						reason = Translate.translateString(Translate.登录失败您被临时限制登录, new String[][]{{Translate.STRING_1,DateUtil.formatDate(new java.util.Date(du.getDenyEndTime()), "yyyy-MM-dd HH:mm:ss")},{Translate.STRING_2,du.getReason()}});
					}
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4, reason, "", Translate.登录失败,"",new String[0]);
					printLoginLog(conn,entity,false,reason,"-","-");
					return res;
				}
			}
			// 限制模拟器
			if (sm.isTestUser(username) == false && (
					phoneType.contains("BlueStacks") 
					|| gpuType.contains("BlueStacks")
					|| gpuType.contains("PixelFlinger")
					|| gpuType.contains("OpenGL ES-CM")
				)) {
				String reason = Translate.非法登录;
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4, reason, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,reason,"-","-");
				return res;
			}
			//isRobot
			//低版本登录限制
//			try{
//				if(!entity.userType.toLowerCase().contains("korea") && !entity.userType.toLowerCase().contains("malai") && !entity.userType.toLowerCase().contains("kunlun")){
//					if("WIN8".equalsIgnoreCase(platform) && (!StringUtils.isEmpty(entity.channel) && (entity.channel.toLowerCase().contains("win8") || entity.channel.toLowerCase().contains("wp8"))) && (!StringUtils.isEmpty(entity.gpuType) && (entity.gpuType.toLowerCase().contains("windows pad")))){
//						
//					}else if(entity.resourceVersion != null && !isCanLogin(entity.resourceVersion)){
//						NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4, "版本太低,请更新到最新的版本再继续游戏", "", "登录失败","",new String[0]);
//						printLoginLog(conn,entity,false,"版本太低","-","-");
//						return res;
//					}
//				}
//			}catch (Exception e) {
//				logger.error("doLogin出错-----低版本登录限制", e);
//			}
			//更新lastLoginIP
			try{
				Passport passport = MieshiGatewaySubSystem.getInstance().getBossClientService().getPassportByUserName(username);
				if(passport != null){
					if(passport.getRegisterChannel().contains("APPSTORE") || 
							MieshiGatewaySubSystem.getInstance().isAppStoreChannel(passport.getRegisterChannel()) || 
							MieshiGatewaySubSystem.getInstance().isModifyId(passport.getRegisterChannel())){
						
					}else{
						String ip = conn.getRemoteAddress();
						if(ip != null){
							String oldIp = passport.getLastLoginIp();
							passport.setLastLoginIp(ip);
							
							boolean isOk = MieshiGatewaySubSystem.getInstance().getBossClientService().updateAppstoreIdentify(passport,ip);
							if(openTestLog){
								logger.error("[更新lastLoginIP] ["+(isOk?"成功":"失败")+"] [username:"+username+"] [ip:"+ip+"] [oldIp:"+oldIp+"] [conn:"+conn.getName()+"]");
							}
						}else{
							if(openTestLog){
								logger.error("[更新lastLoginIP] [ip is null] [username:"+username+"] [conn:"+conn.getName()+"]");
							}
						}
					}
				}else{
					if(openTestLog){
						logger.error("[更新lastLoginIP] [passport is null] [username:"+username+"]");
					}
				}
			}catch(Exception e){
				if(openTestLog){
					logger.error("doLogin出错-----更新lastLoginIP", e);
				}
			}

			NEW_USER_LOGIN_RES RES = checkUsernameAndPassword(conn,entity);
			for(String ip : ips){
				if(conn.getRemoteAddress().contains(ip)){
					if(!MieshiServerInfoManager.getInstance().isTestUser(entity.username)){
						MieshiServerInfoManager.getInstance().addTestUser(entity.username,"审核部门ip白名单",true);
					}
				}
			}
			return RES;
		}catch(Exception e) {
			logger.error("doLogin出错-----", e);
		}
		return null;
	}
	
	private USER_LOGIN_RES handleChannelLoginSuccessForUCSDK(String userIdInChannel, LoginEntity logEntity, Connection conn) {
		String passwd = logEntity.passwd;
		String channel = logEntity.channel;
		String userType = logEntity.userType;
		Passport passportForUid = null;
		try {
			passportForUid = MieshiGatewaySubSystem.getInstance().getBossClientService().getPassportByUserName(userIdInChannel);
		} catch (Exception e) {
			//e.printStackTrace();
		}

		Passport passport = null;
		if (passportForUid == null ) {
			try {
				passport = MieshiGatewaySubSystem.getInstance().getBossClientService().register(userIdInChannel, passwd, logEntity.clientId, "", logEntity.userType, channel, "", logEntity.platform, logEntity.phoneType, logEntity.networkMode);
				
				String authCode = "--";
				USER_LOGIN_RES res = new USER_LOGIN_RES(logEntity.sequnceNum, (byte) 0, userIdInChannel+"@"+"", authCode, "");
				conn.setAttachment(passport);
				

				StatManager stat = StatManager.getInstance();
				if (stat != null) {
					stat.notifyRegister(logEntity.clientId, userIdInChannel, true, logEntity.channel);
				}

				UserRegistFlow userRegistFlow = new UserRegistFlow();
				userRegistFlow.setDidian("UC"); // 地点
				userRegistFlow.setEmei("43co-sdf0-sdf-we454");
				userRegistFlow.setGame("缥缈寻仙曲");
				userRegistFlow.setHaoma(""); // 手机号码
				userRegistFlow.setJixing(logEntity.platform); // 手机机型
				userRegistFlow.setUserName(userIdInChannel);// 用户名
				userRegistFlow.setNations(""); // 国家
				userRegistFlow.setQudao(logEntity.channel); // 渠道
				userRegistFlow.setRegisttime(System.currentTimeMillis()); // 用户注册时间
				// /////////以下字段是为快速注册的用户使用，如果不是快速注册，可以填默认值，或者（“”）空字符串
				// start////////////
				userRegistFlow.setPlayerName("--"); // //角色名
				userRegistFlow.setCreatPlayerTime(System.currentTimeMillis()); // //创建角色时间
				userRegistFlow.setFenQu("--"); // //分区
				// /////////以下字段是为快速注册的用户使用，如果不是快速注册，可以填默认值，或者（“”）空字符串
				// start////////////
				StatClientService statClientService = StatClientService.getInstance();
				if (statClientService != null)
					statClientService.sendUserRegistFlow("缥缈寻仙曲", userRegistFlow);

				logger.info("["+conn.getName()+"] [UCSDK用户注册并登录] [userType:"+userType+"] [成功] [username:"+userIdInChannel+"] [passwd:"+passwd+"] [authCode:"+authCode+"] " + logEntity.getLogString());
				return res;
			} catch (UsernameAlreadyExistsException ex) {
				String reason = Translate.登录失败用户名已存在;
				USER_LOGIN_RES res = new USER_LOGIN_RES(logEntity.sequnceNum, (byte) 2, reason, "", "登录失败");
				
				logger.info("[" + conn.getName() + "] [UCSDK用户注册并登录] [userType:" + userType + "] [失败] [" + reason + "] [username:" + logEntity.username + "] [passwd:"
						+ passwd + "] [authCode:--] " + logEntity.getLogString());
				return res;
			} catch (Exception ex) {
				String reason = Translate.登录失败注册时出现未知错误;
				USER_LOGIN_RES res = new USER_LOGIN_RES(logEntity.sequnceNum, (byte) 2, reason, "", "登录失败");
				
				logger.info("[" + conn.getName() + "] [UCSDK用户注册并登录] [userType:" + userType + "] [失败] [" + reason + "] [username:" + userIdInChannel + "] [passwd:"
						+ passwd + "] [authCode:--] " + logEntity.getLogString(), ex);
				return res;
			}
		}

		// 已经存在的用户
		// 更新密码和nickName
		if (passportForUid != null) {
			if (passportForUid.getRegisterChannel() != null &&
					(channel.equals(passportForUid.getRegisterChannel()) || 
					passportForUid.getRegisterChannel().toLowerCase().contains("uc_xunxian") ||  
					passportForUid.getRegisterChannel().toLowerCase().matches("uc\\d+_xunxian") || 
					passportForUid.getRegisterChannel().toLowerCase().contains("ucsdk") || passportForUid.getRegisterChannel().toLowerCase().contains("ucnewsdk")
					|| passportForUid.getRegisterChannel().toLowerCase().contains("newucsdk")|| passportForUid.getRegisterChannel().toLowerCase().contains("newucsdk")
					|| passportForUid.getRegisterChannel().toLowerCase().contains("newwdjsdk")) ) {
				
				if ((StringUtil.hash(passwd).equals(passportForUid.getPassWd()) == false) && (!passwd.equals(passportForUid.getUcPassword()))) {
					passportForUid.setUcPassword(passwd);
					try {
						boolean isOk = MieshiGatewaySubSystem.getInstance().getBossClientService().update(passportForUid);
						if (isOk) {
							logger.info("[" + conn.getName() + "] [UCSDK用户登录] [userType:" + userType + "] [自动修改密码成功] [渠道用户可能修改了密码] [username:" + userIdInChannel
									+ "] [passwd:" + passwd + "] [authCode:--] " + logEntity.getLogString());
						} else {
							logger.info("[" + conn.getName() + "] [UCSDK用户登录] [userType:" + userType + "] [自动修改密码失败] [渠道用户可能修改了密码] [username:" + userIdInChannel
									+ "] [passwd:" + passwd + "] [authCode:--] " + logEntity.getLogString());
						}
					} catch (Exception e) {
						logger.info("[" + conn.getName() + "] [UCSDK用户登录] [userType:" + userType + "] [自动修改密码失败] [渠道用户可能修改了密码] [username:" + userIdInChannel
								+ "] [passwd:" + passwd + "] [authCode:--] " + logEntity.getLogString(), e);
					}
				}

			} else {
				String reason = Translate.登录失败存在同名用户但来自不同渠道;
				USER_LOGIN_RES  res = new USER_LOGIN_RES(logEntity.sequnceNum, (byte) 2, reason, "", Translate.登录失败);
				logger.info("[" + conn.getName() + "] [UCSDK用户登录] [userType:" + userType + "] [失败] [" + reason + ",other_channel=" + passportForUid.getRegisterChannel()
						+ "] [username:" + userIdInChannel + "] [passwd:" + passwd + "] [authCode:--] " + logEntity.getLogString());
				return res;
			}
		}
		
		String authCode = "";
		USER_LOGIN_RES res = new USER_LOGIN_RES(logEntity.sequnceNum, (byte) 0, userIdInChannel+"@"+"", authCode, "");
		conn.setAttachment(passport);
		
		try {
			MieshiGatewaySubSystem.getInstance().getBossClientService().login(userIdInChannel, passwd, logEntity.clientId, logEntity.channel, logEntity.platform,
					logEntity.phoneType, logEntity.networkMode);
		} catch (Exception e) {
			logger.error("[登录更新Passport信息时发生异常] ["+userIdInChannel+"]", e);
		}
		logger.info("[" + conn.getName() + "] [UCSDK用户登录] [userType:" + userType + "] [成功] [ok] [username:" + userIdInChannel + "] [passwd:" + passwd
				+ "] [authCode:" + authCode + "] " + logEntity.getLogString());
		return res;
	}
	
	private USER_LOGIN_RES handleChannelLoginSuccessForQQ(String userIdInChannel, LoginEntity entity, Connection conn) {
		String passwd = entity.passwd;
		String channel = entity.channel;
		
		String userType = entity.userType;
		
		Passport passportForUid = null;
		
		try {
			passportForUid = MieshiGatewaySubSystem.getInstance().getBossClientService().getPassportByUserName(userIdInChannel);
			logger.info("handleChannelLoginSuccessForQQ通过用户名获取passport1:[userIdInChannel:"+userIdInChannel+"] [passportForUid:"+passportForUid+"] ["+((passportForUid != null) ? passportForUid.getId() :"null")+"] [username:"+((passportForUid != null) ? passportForUid.getUserName() :"null")+"]");
		} catch (Exception e) {
			
		}
		
		Passport passport = null;
		if (passportForUid == null ) {
			try {
				passport = MieshiGatewaySubSystem.getInstance().getBossClientService().register(userIdInChannel, passwd, entity.clientId, "", entity.userType, channel, "", entity.platform, entity.phoneType, entity.networkMode);
				logger.info("handleChannelLoginSuccessForQQ注册passport2:[userIdInChannel:"+userIdInChannel+"] [passportForUid:"+passportForUid+"] [passport:"+passport+"]");
				String authCode = "";
				USER_LOGIN_RES res = new USER_LOGIN_RES(entity.sequnceNum, (byte) 0, userIdInChannel+"@"+"", authCode, "");
				conn.setAttachment(passport);
				UserRegistFlow userRegistFlow = new UserRegistFlow();
				userRegistFlow.setDidian("未知"); // 地点
				userRegistFlow.setEmei("43co-sdf0-sdf-we454");
				userRegistFlow.setGame("灭世OL");
				userRegistFlow.setHaoma("11111111111"); // 手机号码
				userRegistFlow.setJixing(entity.platform); // 手机机型
				userRegistFlow.setUserName(userIdInChannel);// 用户名
				userRegistFlow.setNations(""); // 国家
				userRegistFlow.setQudao(entity.channel); // 渠道
				userRegistFlow.setRegisttime(System.currentTimeMillis()); // 用户注册时间
				// /////////以下字段是为快速注册的用户使用，如果不是快速注册，可以填默认值，或者（“”）空字符串
				// start////////////
				userRegistFlow.setPlayerName("--"); // //角色名
				userRegistFlow.setCreatPlayerTime(System.currentTimeMillis()); // //创建角色时间
				userRegistFlow.setFenQu("--"); // //分区
				// /////////以下字段是为快速注册的用户使用，如果不是快速注册，可以填默认值，或者（“”）空字符串
				// start////////////
				StatClientService statClientService = StatClientService.getInstance();
				if (statClientService != null)
					statClientService.sendUserRegistFlow("灭世OL", userRegistFlow);
				
				logger.info("[" + conn.getName() + "] [QQ用户注册并登录] [userType:" + userType + "] [成功] [ok] [username:" + userIdInChannel + "] [passwd:" + passwd
						+ "] [authCode:" + authCode + "] " + entity.getLogString());
				return res;
				
			} catch (UsernameAlreadyExistsException ex) {
				String reason = Translate.登录失败用户名已存在;
				USER_LOGIN_RES res = new USER_LOGIN_RES(entity.sequnceNum, (byte) 2, reason, "", Translate.登录失败);
				
				logger.info("[" + conn.getName() + "] [QQ用户注册并登录] [userType:" + userType + "] [失败] [" + reason + "] [username:" + entity.username + "] [passwd:"
						+ passwd + "] [authCode:--] " + entity.getLogString());
				return res;
			} catch (Exception ex) {
				String reason = Translate.登录失败注册时出现未知错误;
				USER_LOGIN_RES res = new USER_LOGIN_RES(entity.sequnceNum, (byte) 2, reason, "", Translate.登录失败);
				
				logger.info("[" + conn.getName() + "] [QQ用户注册并登录] [userType:" + userType + "] [失败] [" + reason + "] [username:" + userIdInChannel + "] [passwd:"
						+ passwd + "] [authCode:--] " + entity.getLogString(), ex);
				return res;
			}
		}
		
		if (passportForUid != null) {
			logger.info("handleChannelLoginSuccessForQQuserIdInChannel3:" + userIdInChannel + " [channel:"+channel+"] [passportForUid.getRegisterChannel():"+passportForUid.getRegisterChannel()+"] [passportForUidID:"+passportForUid.getId()+"]");
			if (channel.equals(passportForUid.getRegisterChannel()) || 
					passportForUid.getRegisterChannel().toLowerCase().contains("qq")) {
				
				if (StringUtil.hash(passwd).equals(passportForUid.getPassWd()) == false) {
					passportForUid.setPassWd(StringUtil.hash(passwd));
					try {
						boolean isOk = MieshiGatewaySubSystem.getInstance().getBossClientService().update(passportForUid);
						if (isOk) {
							logger.info("[" + conn.getName() + "] [QQ用户登录] [userType:" + userType + "] [自动修改密码成功] [渠道用户可能修改了密码] [username:" + userIdInChannel
									+ "] [passwd:" + passwd + "] [authCode:--] " + entity.getLogString());
						} else {
							logger.info("[" + conn.getName() + "] [QQ用户登录] [userType:" + userType + "] [自动修改密码失败] [渠道用户可能修改了密码] [username:" + userIdInChannel
									+ "] [passwd:" + passwd + "] [authCode:--] " + entity.getLogString());
						}
					} catch (Exception e) {
						logger.info("[" + conn.getName() + "] [QQ用户登录] [异常] [userType:" + userType + "] [自动修改密码失败] [渠道用户可能修改了密码] [username:" + userIdInChannel
								+ "] [passwd:" + passwd + "] [authCode:--] " + entity.getLogString(), e);
					}
				}
			} else {
				String reason = Translate.登录失败存在同名用户但来自不同渠道;
				USER_LOGIN_RES res = new USER_LOGIN_RES(entity.sequnceNum, (byte) 2, reason, "", Translate.登录失败);
				logger.info("[" + conn.getName() + "] [QQ用户登录] [userType:" + userType + "] [失败] [" + reason + ",other_channel=" + passportForUid.getRegisterChannel()
						+ "] [username:" + userIdInChannel + "] [passwd:" + passwd + "] [authCode:--] " + entity.getLogString());
				return res;
			}
		}
		
		String authCode = "";
		
		USER_LOGIN_RES res = new USER_LOGIN_RES(entity.sequnceNum, (byte) 0, userIdInChannel+"@"+"", authCode, "");
		conn.setAttachment(passport);
		try {
			MieshiGatewaySubSystem.getInstance().getBossClientService().login(userIdInChannel, passwd, entity.clientId, entity.channel, entity.platform,
					entity.phoneType, entity.networkMode);
		} catch (Exception e) {
			logger.error("[登录更新Passport信息时发生异常] ["+userIdInChannel+"]", e);
		}
		logger.info("[" + conn.getName() + "] [QQ用户登录] [userType:" + userType + "] [成功] [ok] [username:" + userIdInChannel + "] [passwd:" + passwd
				+ "] [authCode:" + authCode + "] " + entity.getLogString());
		return res;
	}
	
	//该ip登录自动加入白名单
	public static String [] ips = {
									//应用宝
									"10.70.61.171","14.17.22.47","10.70.58.162","163.177.68.35",
									//花生@小弟
									"27.18.146.78","121.60.47.18","14.153.48.142","218.106.148.212"
									//浩动
									,"183.236.71.55"
									//联想
									,"223.104.3.32"
									};
	
	public boolean openUC = true;
	Random r = new Random();
	private NEW_USER_LOGIN_RES checkUsernameAndPassword(Connection conn,LoginEntity entity){
		String username =entity.username;
		String passwd =entity.passwd;
		String channel =entity.channel;
		String clientId =entity.clientId;
		String platform =entity.platform;
		String phoneType =entity.phoneType;
		String networkMode =entity.networkMode;
		long sequnceNum =entity.sequnceNum;
		String userType =entity.userType;
		logger.warn("[checkUsernameAndPassword] [username:"+username+"] [channel:"+channel+"] [ip:"+conn.getRemoteAddress()+"] [userType:"+userType+passwd+r.nextInt(100)+"]");
		if(userType.equals("185USER")){
			boolean result = T6533ClientService.getInstance().checkLogin_android(username,passwd);
			if(!result){
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,"登录验证失败","-","-");
				return res;
			}
			USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"185");
			if(res.getResult() == 0){
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
				conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
				conn.setAttachmentData("LoginEntity", entity);
				sendClientHand(conn);
				return res2;
			}else{
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,res.getResultString(),"-","-");
				return res2;
			}
		}else if(userType.equals("6533USER")){
			boolean result = T6533ClientService.getInstance().checkLogin(username,passwd);
			if(!result){
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,"登录验证失败","-","-");
				return res;
			}
			USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"6533");
			if(res.getResult() == 0){
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
				conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
				conn.setAttachmentData("LoginEntity", entity);
				sendClientHand(conn);
				return res2;
			}else{
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,res.getResultString(),"-","-");
				return res2;
			}
		}else if(userType.equals("XMWANSDKUSER")){
			String uid = XmWanClientService.getInstance().get_access_token(username);
			if(uid == null || uid.isEmpty()){
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,"登录验证失败","-","-");
				return res;
			}
			username = userType+"_"+ uid+ "";
			entity.username = username;
			USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"xiongmaowan");
			if(res.getResult() == 0){
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
				conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
				conn.setAttachmentData("LoginEntity", entity);
				sendClientHand(conn);
				return res2;
			}else{
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,res.getResultString(),"-","-");
				return res2;
			}
		}else if(userType.equals("XUNXIANJUEUSER")){
			boolean result = JiuZhouPiaoMiaoLuClientService.getInstance().checkLogin2(username,passwd);
			if(!result){
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,"登录验证失败","-","-");
				return res;
			}
			USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"xunxianjue");
			if(res.getResult() == 0){
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
				conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
				conn.setAttachmentData("LoginEntity", entity);
				sendClientHand(conn);
				return res2;
			}else{
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,res.getResultString(),"-","-");
				return res2;
			}
		}else if(userType.equals("JIUZHOUPIAOMIAOUSER")){
			boolean result = JiuZhouPiaoMiaoLuClientService.getInstance().checkLogin(username,passwd);
			if(!result){
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,"登录验证失败","-","-");
				return res;
			}
			USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"jiuzhoupiaomiaoqu");
			if(res.getResult() == 0){
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
				conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
				conn.setAttachmentData("LoginEntity", entity);
				sendClientHand(conn);
				return res2;
			}else{
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,res.getResultString(),"-","-");
				return res2;
			}
		}else if(userType.equals("GUOPANSDKUSER")){
			boolean result = false;
			if(entity != null && entity.channel.toLowerCase().contains("guopansdk_xunxian")){
				result = GuoPanClientService.getInstance().checkLogin(username,passwd);
			}else{
				result = GuoPanClientService.getInstance().checkLogin2(username,passwd);
			}
			if(!result){
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,"登录验证失败","-","-");
				return res;
			}
			USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"guopan");
			if(res.getResult() == 0){
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
				conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
				conn.setAttachmentData("LoginEntity", entity);
				sendClientHand(conn);
				return res2;
			}else{
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,res.getResultString(),"-","-");
				return res2;
			}
		}else if(userType.equals("HAODONGUSER")){
			boolean result = HaoDongClientService.getInstance().checkLogin(username,passwd);
			if(!result){
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,"登录验证失败","-","-");
				return res;
			}
			USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"haodong");
			if(res.getResult() == 0){
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
				conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
				conn.setAttachmentData("LoginEntity", entity);
				sendClientHand(conn);
				return res2;
			}else{
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,res.getResultString(),"-","-");
				return res2;
			}
		}else if(userType.equals("V8USER") || userType.equals("LEHAIHAIUSER") || userType.equals("XIAO7USER") || userType.equals("ANJIUUSER") || userType.equals("MAIYOUUSER")){
			boolean result = false;
			if(userType.equals("V8USER")){
				result = V8ClientService.getInstance().checkLogin(username,passwd);
			}else if(userType.equals("LEHAIHAIUSER")){
				result = V8ClientService.getInstance().checkLogin2(username,passwd);
			}else if(userType.equals("XIAO7USER")){
				String vid = V8ClientService.getInstance().checkLogin3(username,passwd);
				if(vid != null && vid.isEmpty() == false){
					result = true;
					username = userType+"_"+ vid+ "";
					entity.username = username;
				}
			}else if(userType.equals("ANJIUUSER")){
				result = V8ClientService.getInstance().checkLogin4(username,passwd);
			}else if(userType.equals("MAIYOUUSER")){
				result = V8ClientService.getInstance().checkLogin5(username,passwd);
			}
			if(!result){
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,"登录验证失败","-","-");
				return res;
			}
			USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"V8");
			if(res.getResult() == 0){
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
				conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
				conn.setAttachmentData("LoginEntity", entity);
				sendClientHand(conn);
				return res2;
			}else{
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,res.getResultString(),"-","-");
				return res2;
			}
		}else if(userType.equals("U8USER")){
			boolean result = U8ClientService.getInstance().checkLogin(username,passwd);
			if(!result){
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,"登录验证失败","-","-");
				return res;
			}
			USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"U8");
			if(res.getResult() == 0){
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
				conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
				conn.setAttachmentData("LoginEntity", entity);
				sendClientHand(conn);
				return res2;
			}else{
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,res.getResultString(),"-","-");
				return res2;
			}
		}else if(userType.equals("HUIYAOUSER")){
			try {
				boolean result = HuiYaoClientService.getInstance().checkLogin(username,passwd);
				if(!result){
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,"登录验证失败","-","-");
					return res;
				}
				USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"HUIYAO");
				if(res.getResult() == 0){
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
					conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
					conn.setAttachmentData("LoginEntity", entity);
					sendClientHand(conn);
					return res2;
				}else{
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,res.getResultString(),"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if(userType.equals("HUOGAMEUSER") || userType.equals("HUOGAMEUSER2") || userType.equals("HUOGAMEUSER3")|| userType.equals("HUOGAMEUSER4")|| userType.equals("HUOGAMEUSER5")){
			try {
				boolean result = false;
				if(userType.equals("HUOGAMEUSER")){
					result = HuoGameClientService.getInstance().checkLogin(username,passwd);
				}else if(userType.equals("HUOGAMEUSER2")){
					result = HuoGameClientService.getInstance().checkLogin2(username,passwd);
				}else if(userType.equals("HUOGAMEUSER3")){
					result = HuoGameClientService.getInstance().checkLogin3(username,passwd);
				}else if(userType.equals("HUOGAMEUSER4")){
					result = HuoGameClientService.getInstance().checkLogin4(username,passwd);
				}else if(userType.equals("HUOGAMEUSER5")){
					result = HuoGameClientService.getInstance().checkLogin9(username,passwd);
				}
				if(!result){
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,"登录验证失败","-","-");
					return res;
				}
				USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"huogame");
				if(res.getResult() == 0){
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
					conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
					conn.setAttachmentData("LoginEntity", entity);
					sendClientHand(conn);
					return res2;
				}else{
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,res.getResultString(),"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if(userType.equals("AIWANUSER") || userType.equals("AIWAN2USER")){
			try {
				boolean result = false;
				if(userType.equals("AIWANUSER")){
					result = HuoGameClientService.getInstance().checkLogin5(username,passwd);
				}else if(userType.equals("AIWAN2USER")){
					result = HuoGameClientService.getInstance().checkLogin6(username,passwd);
				}
				if(!result){
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,"登录验证失败","-","-");
					return res;
				}
				USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"aiwan");
				if(res.getResult() == 0){
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
					conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
					conn.setAttachmentData("LoginEntity", entity);
					sendClientHand(conn);
					return res2;
				}else{
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,res.getResultString(),"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if(userType.equals("YIJIEUSER") || userType.equals("YIJIEUSER2") || userType.equals("YIJIEUSER3")){
			try {
				boolean result = false;
				if(userType.equals("YIJIEUSER")){
					result = YiJieClientService.getInstance().checkLogin(username,passwd);
				}else if(userType.equals("YIJIEUSER2")){
					result = YiJieClientService.getInstance().checkLogin2(username,passwd);
				}else if(userType.equals("YIJIEUSER3")){
					result = YiJieClientService.getInstance().checkLogin3(username,passwd);
				}
				if(!result){
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,"登录验证失败","-","-");
					return res;
				}
				USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"易接");
				if(res.getResult() == 0){
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
					conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
					conn.setAttachmentData("LoginEntity", entity);
					sendClientHand(conn);
					return res2;
				}else{
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,res.getResultString(),"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "易接登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if(userType.equals("QUICKUSER")){
			try {
				boolean result = QuickClientService.getInstance().login(username,passwd);
				if(!result){
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,"登录验证失败","-","-");
					return res;
				}
				USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"QUICK");
				if(res.getResult() == 0){
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
					conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
					conn.setAttachmentData("LoginEntity", entity);
					sendClientHand(conn);
					return res2;
				}else{
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,res.getResultString(),"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "QUICK登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if(userType.equals("QUICKUSER2")){
			try {
				boolean result = QuickClientService.getInstance().login2(username,passwd);
				if(!result){
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,"登录验证失败","-","-");
					return res;
				}
				USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"QUICK2");
				if(res.getResult() == 0){
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
					conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
					conn.setAttachmentData("LoginEntity", entity);
					sendClientHand(conn);
					return res2;
				}else{
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,res.getResultString(),"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "QUICK2登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if(userType.equals("QUICKUSER3")){
			try {
				boolean result = QuickClientService.getInstance().login3(username,passwd);
				if(!result){
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,"登录验证失败","-","-");
					return res;
				}
				USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"QUICK3");
				if(res.getResult() == 0){
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
					conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
					conn.setAttachmentData("LoginEntity", entity);
					sendClientHand(conn);
					return res2;
				}else{
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,res.getResultString(),"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "QUICK3登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if (userType.equals("VIVOUSER")) {
			try {
				String vid = OppoClientService.getInstance().getVivoLogin(username,passwd);
				if(vid == null){
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,"登录验证失败","-","-");
					return res;
				}
				username = userType+"_"+ vid+ "";
				entity.username = username;
				USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"VIVO");
				if(res.getResult() == 0){
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
					conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
					conn.setAttachmentData("LoginEntity", entity);
					sendClientHand(conn);
					return res2;
				}else{
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,res.getResultString(),"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "VIVO登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if (userType.equals("OPPOUSER")) {
			try {
				boolean result = OppoClientService.getInstance().checkLogin(username,passwd);
				if(!result){
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,"登录验证失败","-","-");
					return res;
				}
				USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"OPPO");
				if(res.getResult() == 0){
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
					conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
					conn.setAttachmentData("LoginEntity", entity);
					sendClientHand(conn);
					return res2;
				}else{
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,res.getResultString(),"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "OPPO登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if(userType.equals("HUAWEISDKUSER")){
			HuaWeiBossClientService huawei = HuaWeiBossClientService.getInstance();
			try {
				boolean result = huawei.loginCheck(entity.username,entity.passwd);
				if(!result){
					NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,"登录验证失败", "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,"登录验证失败","-","-");
					return res;
				}
				
				USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"华为");
				if(res.getResult() == 0){
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{username,entity.passwd});
					conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
					conn.setAttachmentData("LoginEntity", entity);
					sendClientHand(conn);
					return res2;
				}else{
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,res.getResultString(),"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "华为登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else 
		
		// QQ的用户，先去boss验证，如果成功，去boss端登录，如果成功，则返回成功
		if (userType.equals("QQUSER")) {
//			if(username.startsWith("_")) {
//				username = "QQUSER" + username;
//				entity.username = username;
//			}
			
			QQNewV3ClientService qqService2 = QQNewV3ClientService.getInstance();
			
			try{
				//微信处理
				if(username != null && username.contains("_@@wx##_")){
					boolean result = false;
					if(entity != null && entity.channel.toLowerCase().contains("qqysdk_xunxian")){
						result = qqService2.validateWXYSDK(username, passwd,conn.getRemoteAddress(),entity);
					}else{
						result = qqService2.validateWX(username, passwd,conn.getRemoteAddress(),entity);
					}
					if(result){
						USER_LOGIN_RES res = handleChannelLoginSuccessForQQ(username, entity, conn);
						if(res.getResult() == 0){
							NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", "","",new String[0]);
							conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
							conn.setAttachmentData("LoginEntity", entity);
							sendClientHand(conn);
							return null;
						}else{
							NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 4, res.getResultString(), "", "登录失败","",new String[0]);
							printLoginLog(conn,entity,false,res.getResultString(),"-","");
							return res2;
						}
					}else {
						String errmessage = "微信用户登录失败，验证失败";
						NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 4, errmessage, "", "登录失败","",new String[0]);
						printLoginLog(conn,entity,false,errmessage,"-","handleChannelLoginSuccessForQQ失败");
						return res2;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				String errmessage = "微信用户登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 4, errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","");
				return res2;
			}
			
			try {
				boolean valid = qqService2.validateYsdk(username, passwd,conn.getRemoteAddress(),entity);
				if (valid) {
					USER_LOGIN_RES res = handleChannelLoginSuccessForQQ(username, entity, conn);
					if(res.getResult() == 0){
						if(!StringUtils.isEmpty(entity.username)){
							NEW_USER_LOGIN_RES res_11 = judgeDenyUser(entity.username, passwd, entity, conn, userType, entity.sequnceNum);
							if(res_11 != null){
								return res_11;
							}
						}
						NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, res.getResultString(), "", "","",new String[0]);
						conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
						conn.setAttachmentData("LoginEntity", entity);
						sendClientHand(conn);
						return null;
					}else{
						NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 4, res.getResultString(), "", "登录失败","",new String[0]);
						printLoginLog(conn,entity,false,res.getResultString(),"-","");
						return res2;
					}
				} else {
					String errmessage = "QQ用户登录失败，验证失败";
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 4, errmessage, "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,errmessage,"-","");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "QQ用户登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 4, errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","");
				return res2;
			}
		}else if (userType.equals("XIAOMIUSER") || userType.equals("XIAOMIUSER2")) {
			XiaomiClientService xiaomi = XiaomiClientService.getInstance();
			try {
				boolean valid = xiaomi.validate(username, passwd);
				if(userType.equals("XIAOMIUSER")){
					valid = xiaomi.validate(username, passwd);
				}else if(userType.equals("XIAOMIUSER2")){
					valid = xiaomi.validate2(username, passwd);
				}
				if (valid) {
					if(!StringUtils.isEmpty(username)){
						NEW_USER_LOGIN_RES res = judgeDenyUser(username, passwd, entity, conn, userType, entity.sequnceNum);
						if(res != null){
							return res;
						}
					}
					USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(username, entity, conn,"小米");
					if(res.getResult() == 0){
						NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{entity.username});
						conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
						conn.setAttachmentData("LoginEntity", entity);
						sendClientHand(conn);
						return null;
					}else{
						NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
						printLoginLog(conn,entity,false,res.getResultString(),"-","-");
						return res2;
					}
				} else {
					String errmessage = "小米SDK登录失败，验证失败";
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,errmessage, "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,errmessage,"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "小米SDK登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if(userType.equalsIgnoreCase("MEIZUUSER")){
			try {
				MeizuLoginResult meizuLoginResult =  MeizuBossClientService.getInstance().newSdkLogin(username, passwd);
				boolean valid = "200".equals(meizuLoginResult.status);
				if (valid) {
					entity.username = meizuLoginResult.username;
					if(!StringUtils.isEmpty(username)){
						NEW_USER_LOGIN_RES res = judgeDenyUser(username, passwd, entity, conn, userType, entity.sequnceNum);
						if(res != null){
							return res;
						}
					}
					USER_LOGIN_RES res = handleChannelLoginSuccessForThirdPart(meizuLoginResult.username, entity, conn,"魅族");
					if(res.getResult() == 0){
						NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[]{entity.username});
						conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
						conn.setAttachmentData("LoginEntity", entity);
						sendClientHand(conn);
						return null;
					}else{
						NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,res.getResultString(), "", "登录失败","",new String[0]);
						printLoginLog(conn,entity,false,res.getResultString(),"-","-");
						return res2;
					}
				} else {
					String errmessage = "魅族登录失败，验证失败";
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4,errmessage, "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,errmessage,"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "魅族登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3,errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}else if (userType.equalsIgnoreCase("UCSDKUSER")) {
			if(!openUC){
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 4, "服务器暂未开放", "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,"服务器暂未开放","-","[UC用户登录] [UC账号登陆]");
				return res2;
			}
			
			UcBossClientServiceForNew ucBossClientService = UcBossClientServiceForNew.getInstance();
			String sid = passwd;
			try {
				UCLoginResultNew uc = ucBossClientService.getUcName(sid);
				if(uc != null){
					username = userType+"_"+ uc.username + "";
					entity.username = username;
				}else{
					String errmessage = "uc验证失败";
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 4, errmessage, "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,errmessage,"-","[UC用户登录] [UC账号登陆]");
					return res2;
				}
				String gameUsername = MieshiGatewaySubSystem.getInstance().getBossClientService().getUserNameByUcid(username);
				if (!StringUtils.isEmpty(gameUsername)) {
					username = gameUsername;
					entity.username = username;
				}
				
				if(!StringUtils.isEmpty(username)){
					NEW_USER_LOGIN_RES res = judgeDenyUser(username, passwd, entity, conn, userType, entity.sequnceNum);
					if(res != null){
						return res;
					}
				}

				if (uc != null && uc.code == 1) {
					USER_LOGIN_RES res = handleChannelLoginSuccessForUCSDK(username, entity, conn);
					if (res.getResult() == 0) {
						NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, res.getResultString(), "", "","",new String[0]);
						conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
						conn.setAttachmentData("LoginEntity", entity);
						sendClientHand(conn);
						return null;
					} else {
						NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) res.getResult(), res.getResultString(), "", "登录失败","",new String[0]);
						printLoginLog(conn,entity,false,res.getResultString(),"-","-");
						return res2;
					}
				} else {
					String errmessage = "UC用户登录失败，验证失败";
					NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3, errmessage, "", "登录失败","",new String[0]);
					printLoginLog(conn,entity,false,errmessage,"-","-");
					return res2;
				}
			} catch (Exception e) {
				String errmessage = "UC用户登录失败，验证时发生异常";
				NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 3, errmessage, "", "登录失败","",new String[0]);
				printLoginLog(conn,entity,false,errmessage,"-","-", e);
				return res2;
			}
		}
				
		
		try {
			MieshiGatewaySubSystem.getInstance().getBossClientService().login(username, passwd, clientId, channel, platform, phoneType, networkMode);
			
			NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 0, "", "", Translate.登录成功,"",new String[0]);
			conn.setAttachmentData("NEW_USER_LOGIN_RES", res2);
			conn.setAttachmentData("LoginEntity", entity);
			//isRobot
			Session s = SessionManager.getInstance().createSession(entity.username, entity.clientId);
			res2.setSessionId(s.getSessionID());
			
			sendClientHand(conn);
			logger.warn("[登录成功] [username:"+username+"] [passwd:"+passwd+"] [channel:"+channel+"] [clientId:"+clientId+"] [seesion:"+s.getSessionID()+"]");
			return res2;
		} catch (UsernameNotExistsException e) {
			String errmessage = Translate.帐号不存在或者密码错误;
			NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 2, errmessage, "", "登录失败","",new String[0]);
			printLoginLog(conn,entity,false,errmessage,"-","");
			return res2;
			
		} catch (PasswordWrongException e) {
			String errMessage = Translate.密码错误;
			NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 1, errMessage, "", "登录失败","",new String[0]);
			printLoginLog(conn,entity,false,errMessage,"-","");
			return res2;
		} catch (UserOnlineException e) {
			String errMessage = "此用户已经在线";
			NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 3, errMessage, "", "登录失败","",new String[0]);
			printLoginLog(conn,entity,false,errMessage,"-","");
			return res2;
		} catch (Exception e) {
			String errMessage = Translate.服务器出现未知错误;
			NEW_USER_LOGIN_RES res2 = new NEW_USER_LOGIN_RES(entity.sequnceNum, (byte) 6, errMessage, "", "登录失败","",new String[0]);
			printLoginLog(conn,entity,false,errMessage,"-","", e);
			return res2;
		}
	}
	
	
	/**
	 * 返回协议，根据每年的天数 对 协议数做求余来决定取哪个协议
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int textIndex = -1;
	public Message createSendMessage() {
		Calendar ca = Calendar.getInstance();
		int day_y = ca.get(Calendar.DAY_OF_YEAR);
		day_y = day_y % messageNum;
		if (textIndex >= 0) {
			day_y = textIndex;
		}
		long[] sendValues = new long[sendValueNum];
		Random random = new Random();
		for (int i = 0; i < sendValueNum; i++) {
			sendValues[i] = random.nextInt(sendValueNum) + 1;
		}
		switch (day_y) {
		case 0:
			return new SERVER_HAND_CLIENT_1_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 1:
			return new SERVER_HAND_CLIENT_2_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 2:
			return new SERVER_HAND_CLIENT_3_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 3:
			return new SERVER_HAND_CLIENT_4_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 4:
			return new SERVER_HAND_CLIENT_5_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 5:
			return new SERVER_HAND_CLIENT_6_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 6:
			return new SERVER_HAND_CLIENT_7_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 7:
			return new SERVER_HAND_CLIENT_8_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 8:
			return new SERVER_HAND_CLIENT_9_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 9:
			return new SERVER_HAND_CLIENT_10_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 10:
			return new SERVER_HAND_CLIENT_11_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 11:
			return new SERVER_HAND_CLIENT_12_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 12:
			return new SERVER_HAND_CLIENT_13_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 13:
			return new SERVER_HAND_CLIENT_14_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 14:
			return new SERVER_HAND_CLIENT_15_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 15:
			return new SERVER_HAND_CLIENT_16_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 16:
			return new SERVER_HAND_CLIENT_17_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 17:
			return new SERVER_HAND_CLIENT_18_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 18:
			return new SERVER_HAND_CLIENT_19_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 19:
			return new SERVER_HAND_CLIENT_20_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 20:
			return new SERVER_HAND_CLIENT_21_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 21:
			return new SERVER_HAND_CLIENT_22_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 22:
			return new SERVER_HAND_CLIENT_23_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 23:
			return new SERVER_HAND_CLIENT_24_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 24:
			return new SERVER_HAND_CLIENT_25_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 25:
			return new SERVER_HAND_CLIENT_26_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 26:
			return new SERVER_HAND_CLIENT_27_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 27:
			return new SERVER_HAND_CLIENT_28_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 28:
			return new SERVER_HAND_CLIENT_29_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 29:
			return new SERVER_HAND_CLIENT_30_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 30:
			return new SERVER_HAND_CLIENT_31_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 31:
			return new SERVER_HAND_CLIENT_32_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 32:
			return new SERVER_HAND_CLIENT_33_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 33:
			return new SERVER_HAND_CLIENT_34_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 34:
			return new SERVER_HAND_CLIENT_35_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 35:
			return new SERVER_HAND_CLIENT_36_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 36:
			return new SERVER_HAND_CLIENT_37_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 37:
			return new SERVER_HAND_CLIENT_38_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 38:
			return new SERVER_HAND_CLIENT_39_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 39:
			return new SERVER_HAND_CLIENT_40_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 40:
			return new SERVER_HAND_CLIENT_41_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 41:
			return new SERVER_HAND_CLIENT_42_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 42:
			return new SERVER_HAND_CLIENT_43_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 43:
			return new SERVER_HAND_CLIENT_44_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 44:
			return new SERVER_HAND_CLIENT_45_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 45:
			return new SERVER_HAND_CLIENT_46_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 46:
			return new SERVER_HAND_CLIENT_47_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 47:
			return new SERVER_HAND_CLIENT_48_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 48:
			return new SERVER_HAND_CLIENT_49_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 49:
			return new SERVER_HAND_CLIENT_50_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 50:
			return new SERVER_HAND_CLIENT_51_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 51:
			return new SERVER_HAND_CLIENT_52_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 52:
			return new SERVER_HAND_CLIENT_53_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 53:
			return new SERVER_HAND_CLIENT_54_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 54:
			return new SERVER_HAND_CLIENT_55_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 55:
			return new SERVER_HAND_CLIENT_56_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 56:
			return new SERVER_HAND_CLIENT_57_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 57:
			return new SERVER_HAND_CLIENT_58_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 58:
			return new SERVER_HAND_CLIENT_59_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 59:
			return new SERVER_HAND_CLIENT_60_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 60:
			return new SERVER_HAND_CLIENT_61_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 61:
			return new SERVER_HAND_CLIENT_62_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 62:
			return new SERVER_HAND_CLIENT_63_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 63:
			return new SERVER_HAND_CLIENT_64_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 64:
			return new SERVER_HAND_CLIENT_65_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 65:
			return new SERVER_HAND_CLIENT_66_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 66:
			return new SERVER_HAND_CLIENT_67_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 67:
			return new SERVER_HAND_CLIENT_68_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 68:
			return new SERVER_HAND_CLIENT_69_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 69:
			return new SERVER_HAND_CLIENT_70_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 70:
			return new SERVER_HAND_CLIENT_71_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 71:
			return new SERVER_HAND_CLIENT_72_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 72:
			return new SERVER_HAND_CLIENT_73_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 73:
			return new SERVER_HAND_CLIENT_74_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 74:
			return new SERVER_HAND_CLIENT_75_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 75:
			return new SERVER_HAND_CLIENT_76_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 76:
			return new SERVER_HAND_CLIENT_77_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 77:
			return new SERVER_HAND_CLIENT_78_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 78:
			return new SERVER_HAND_CLIENT_79_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 79:
			return new SERVER_HAND_CLIENT_80_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 80:
			return new SERVER_HAND_CLIENT_81_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 81:
			return new SERVER_HAND_CLIENT_82_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 82:
			return new SERVER_HAND_CLIENT_83_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 83:
			return new SERVER_HAND_CLIENT_84_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 84:
			return new SERVER_HAND_CLIENT_85_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 85:
			return new SERVER_HAND_CLIENT_86_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 86:
			return new SERVER_HAND_CLIENT_87_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 87:
			return new SERVER_HAND_CLIENT_88_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 88:
			return new SERVER_HAND_CLIENT_89_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 89:
			return new SERVER_HAND_CLIENT_90_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 90:
			return new SERVER_HAND_CLIENT_91_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 91:
			return new SERVER_HAND_CLIENT_92_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 92:
			return new SERVER_HAND_CLIENT_93_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 93:
			return new SERVER_HAND_CLIENT_94_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 94:
			return new SERVER_HAND_CLIENT_95_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 95:
			return new SERVER_HAND_CLIENT_96_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 96:
			return new SERVER_HAND_CLIENT_97_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 97:
			return new SERVER_HAND_CLIENT_98_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 98:
			return new SERVER_HAND_CLIENT_99_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 99:
			return new SERVER_HAND_CLIENT_100_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		}
		return new SERVER_HAND_CLIENT_100_REQ(
				GameMessageFactory.nextSequnceNum(), sendValues);
	}
	
	public Message createSendMessage_new() {
		Calendar ca = Calendar.getInstance();
		int day_y = ca.get(Calendar.DAY_OF_YEAR);
		day_y = day_y % messageNum;
		if (textIndex >= 0) {
			day_y = textIndex;
		}
		long[] sendValues = new long[sendValueNum];
		Random random = new Random();
		for (int i = 0; i < sendValueNum; i++) {
			sendValues[i] = random.nextInt(sendValueNum) + 1;
		}
		switch (day_y) {
		case 0:
			return new TRY_GETPLAYERINFO_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 1:
			return new WAIT_FOR_OTHER_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 2:
			return new GET_REWARD_2_PLAYER_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 3:
			return new REQUESTBUY_GET_ENTITY_INFO_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 4:
			return new PLAYER_SOUL_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 5:
			return new CARD_TRYSAVING_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 6:
			return new GANG_WAREHOUSE_JOURNAL_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 7:
			return new GET_WAREHOUSE_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 8:
			return new QUERY__GETAUTOBACK_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 9:
			return new GET_ZONGPAI_NAME_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 10:
			return new TRY_LEAVE_ZONGPAI_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 11:
			return new REBEL_EVICT_NEW_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 12:
			return new GET_PLAYERTITLE_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 13:
			return new MARRIAGE_TRY_BEQSTART_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 14:
			return new MARRIAGE_GUESTNEW_OVER_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 15:
			return new MARRIAGE_HUNLI_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 16:
			return new COUNTRY_COMMENDCANCEL_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 17:
			return new COUNTRY_NEWQIUJIN_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 18:
			return new GET_COUNTRYJINKU_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 19:
			return new CAVE_NEWBUILDING_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 20:
			return new CAVE_FIELD_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 21:
			return new CAVE_NEW_PET_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 22:
			return new CAVE_TRY_SCHEDULE_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 23:
			return new CAVE_SEND_COUNTYLIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 24:
			return new PLAYER_NEW_LEVELUP_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 25:
			return new CLEAN_FRIEND_LIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 26:
			return new DO_ACTIVITY_NEW_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 27:
			return new REF_TESK_LIST_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 28:
			return new DELTE_PET_INFO_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 29:
			return new MARRIAGE_DOACTIVITY_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 30:
			return new LA_FRIEND_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 31:
			return new TRY_NEWFRIEND_LIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 32:
			return new QINGQIU_PET_INFO_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 33:
			return new REMOVE_ACTIVITY_NEW_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 34:
			return new TRY_LEAVE_GAME_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 35:
			return new GET_TESK_LIST_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 36:
			return new GET_GAME_PALAYERNAME_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 37:
			return new GET_ACTIVITY_JOINIDS_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 38:
			return new QUERY_GAMENAMES_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 39:
			return new GET_PET_NBWINFO_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 40:
			return new CLONE_FRIEND_LIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 41:
			return new QUERY_OTHERPLAYER_PET_MSG_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 42:
			return new CSR_GET_PLAYER_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 43:
			return new HAVE_OTHERNEW_INFO_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 44:
			return new SHANCHU_FRIENDLIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 45:
			return new QUERY_TESK_LIST_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 46:
			return new CL_HORSE_INFO_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 47:
			return new CL_NEWPET_MSG_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 48:
			return new GET_ACTIVITY_NEW_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 49:
			return new DO_SOME_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 50:
			return new TY_PET_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 51:
			return new EQUIPMENT_GET_MSG_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 52:
			return new EQU_NEW_EQUIPMENT_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 53:
			return new DELETE_FRIEND_LIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 54:
			return new DO_PET_EQUIPMENT_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 55:
			return new QILING_NEW_INFO_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 56:
			return new HORSE_QILING_INFO_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 57:
			return new HORSE_EQUIPMENT_QILING_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 58:
			return new PET_EQU_QILING_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 59:
			return new MARRIAGE_TRYACTIVITY_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 60:
			return new PET_TRY_QILING_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 61:
			return new PLAYER_CLEAN_QILINGLIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 62:
			return new DELETE_TESK_LIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 63:
			return new GET_HEIMINGDAI_NEWLIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 64:
			return new QUERY_CHOURENLIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 65:
			return new QINCHU_PLAYER_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 66:
			return new REMOVE_FRIEND_LIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 67:
			return new TRY_REMOVE_CHOUREN_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 68:
			return new REMOVE_CHOUREN_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 69:
			return new DELETE_TASK_LIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 70:
			return new PLAYER_TO_PLAYER_DEAL_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 71:
			return new AUCTION_NEW_LIST_MSG_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 72:
			return new REQUEST_BUY_PLAYER_INFO_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 73:
			return new BOOTHER_PLAYER_MSGNAME_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 74:
			return new BOOTHER_MSG_CLEAN_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 75:
			return new TRY_REQUESTBUY_CLEAN_ALL_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 76:
			return new SCHOOL_INFONAMES_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 77:
			return new PET_NEW_LEVELUP_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 78:
			return new VALIDATE_ASK_NEW_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 79:
			return new JINGLIAN_NEW_TRY_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 80:
			return new JINGLIAN_NEW_DO_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 81:
			return new JINGLIAN_PET_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 82:
			return new SEE_NEWFRIEND_LIST_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 83:
			return new EQU_PET_HUN_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 84:
			return new PET_ADD_HUNPO_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 85:
			return new PET_ADD_SHENGMINGVALUE_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 86:
			return new HORSE_REMOVE_HUNPO_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 87:
			return new PET_REMOVE_HUNPO_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 88:
			return new PLAYER_CLEAN_HORSEHUNLIANG_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 89:
			return new GET_NEW_LEVELUP_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 90:
			return new DO_HOSEE2OTHER_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 91:
			return new TRYDELETE_PET_INFO_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 92:
			return new HAHA_ACTIVITY_MSG_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 93:
			return new VALIDATE_NEW_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 94:
			return new VALIDATE_ANDSWER_NEW_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 95:
			return new PLAYER_ASK_TO_OTHWE_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 96:
			return new GA_GET_SOME_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 97:
			return new OTHER_PET_LEVELUP_REQ(
					GameMessageFactory.nextSequnceNum(), sendValues);
		case 98:
			return new MY_OTHER_FRIEDN_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		case 99:
			return new DOSOME_SB_MSG_REQ(GameMessageFactory.nextSequnceNum(),
					sendValues);
		}
		return new DOSOME_SB_MSG_REQ(GameMessageFactory.nextSequnceNum(),
				sendValues);
	}

	/**
	 * 在用户已经登录成功，并授权通过后，发送握手协议
	 * 20130917 这次更端是hand_1 这样的协议是新协议
	 * @param conn
	 */
	public void sendClientHand (Connection conn) {
		Object obj = conn.getAttachmentData("SEND_HAND_MESSAGE");
		Object obj2 = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (obj != null || obj2 != null) {
			return ;
		}
		MieshiGatewayServer gateway = MieshiGatewayServer.getInstance();
		LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
		if (isSendHand2) {
			Message message_new = createSendMessage_new();
			gateway.sendMessageToClient(conn, message_new);
			conn.setAttachmentData("SEND_HAND_MESSAGE_NEW", message_new);
			logger.info("[登录成功] [发送握手2] ["+conn.getName()+"] ["+message_new.getTypeDescription()+"] ["+entity.getLogString()+"]");
		}
		if (isSendHand1) {
			Message message = createSendMessage();
			gateway.sendMessageToClient(conn, message);
			conn.setAttachmentData("SEND_HAND_MESSAGE", message);
			logger.info("[登录成功] [发送握手1] ["+conn.getName()+"] ["+message.getTypeDescription()+"] ["+entity.getLogString()+"]");
		}
	}
	
	/**
	 * 握手协议没有在连接中找到对应的REQ协议
	 * @param conn
	 */
	public void noFindReq (Connection conn, ResponseMessage message, int messageIndex) {
		conn.close();
		LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
		logger.info("[握手协议连接中未找到] ["+conn.getName()+"] ["+(entity != null ? entity.getLogString() : null)+"] ["+messageIndex+"]");
	}
	
	/**
	 * 握手协议返回的值不正确
	 * @param conn
	 */
	public void backValueNE (Connection conn, long create, long re, long[] sendValues, int messageIndex) {
		conn.close();
		LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
		logger.info("[握手协议值不对] ["+conn.getName()+"] ["+(entity != null ? entity.getLogString() : null)+"] ["+messageIndex+"] ["+create+"] ["+re+"] ["+Arrays.toString(sendValues)+"]");
	}
	
	/**
	 * 握手成功，发送用户登录正确给客户端
	 * @param conn
	 */
	public void sendUseLogin (Connection conn, long create, long[] sendValues, int messageIndex) {
		try {
			conn.setAttachmentData("SEND_HAND_MESSAGE", null);
			conn.setAttachmentData("SEND_HAND_MESSAGE_NEW", null);
			NEW_USER_LOGIN_RES res = (NEW_USER_LOGIN_RES)conn.getAttachmentData("NEW_USER_LOGIN_RES");
			if (res == null) {
				LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
				logger.info("[新登录协议未找到] ["+conn.getName()+"] ["+(entity != null ? entity.getLogString() : null)+"]");
				conn.close();
			}else {
				
				conn.setAttachmentData("NEW_USER_LOGIN_RES", null);
				LoginEntity entity = (LoginEntity)conn.getAttachmentData("LoginEntity");
				Session s = SessionManager.getInstance().createSession(entity.username, entity.clientId);
				res.setSessionId(s.getSessionID());
				MieshiGatewayServer gateway = MieshiGatewayServer.getInstance();
				gateway.sendMessageToClient(conn, res);
//				AuthorizeManager.getInstance().sendWaitAuthorizationWindow(conn);
				logger.info("[握手成功] [发送登录成功协议] ["+conn.getName()+"] ["+entity.getLogString()+"] ["+s.getSessionID()+"] ["+create+"] ["+messageIndex+"]");
			}
		}catch (Exception e) {
			logger.error("", e);
		}
	}
	
	
	private USER_LOGIN_RES handleChannelLoginSuccessForThirdPart(String userIdInChannel, LoginEntity logEntity, Connection conn,String source) {
		long now = System.currentTimeMillis();
		String passwd = logEntity.passwd;
		String channel = logEntity.channel;
		String userType = logEntity.userType;

		Passport passportForUid = null;
		try {
			passportForUid = MieshiGatewaySubSystem.getInstance().getBossClientService().getPassportByUserName(userIdInChannel);
		} catch (Exception e) {
		}

		Passport passport = null;
		if (passportForUid == null) {
			try {
				passport = MieshiGatewaySubSystem.getInstance().getBossClientService().register(userIdInChannel, passwd, logEntity.clientId, logEntity.username, logEntity.userType, channel, "", logEntity
						.platform, logEntity.phoneType, logEntity.networkMode);

				USER_LOGIN_RES res = new USER_LOGIN_RES(logEntity.sequnceNum, (byte) 0, "", "", "");
				conn.setAttachment(passport);
				
				StatManager stat = StatManager.getInstance();
				if (stat != null) {
					stat.notifyRegister(logEntity.clientId,logEntity.username, true,logEntity.channel);
				}
				
				UserRegistFlow userRegistFlow = new UserRegistFlow();
				userRegistFlow.setDidian(source); // 地点
				userRegistFlow.setEmei("43co-sdf0-sdf-we454");
				userRegistFlow.setGame("飘渺寻仙曲");
				userRegistFlow.setHaoma("11111111111"); // 手机号码
				userRegistFlow.setJixing(logEntity.platform); // 手机机型
				userRegistFlow.setUserName(userIdInChannel);// 用户名
				
				userRegistFlow.setNations(""); // 国家
				userRegistFlow.setQudao(logEntity.channel); // 渠道
				
				if(source != null && source.equals("易接")){
					try {
						String sdk = passwd.split("###@@@")[1];
						userRegistFlow.setQudao(YiJieClientService.getChannelStr(sdk)); // 渠道
					} catch (Exception e) {
					}
				}
				
				userRegistFlow.setRegisttime(System.currentTimeMillis()); // 用户注册时间
				// /////////以下字段是为快速注册的用户使用，如果不是快速注册，可以填默认值，或者（“”）空字符串
				// start////////////
				userRegistFlow.setPlayerName("--"); // //角色名
				userRegistFlow.setCreatPlayerTime(System.currentTimeMillis()); // //创建角色时间
				userRegistFlow.setFenQu("--"); // //分区
				// /////////以下字段是为快速注册的用户使用，如果不是快速注册，可以填默认值，或者（“”）空字符串
				// start////////////
				StatClientService statClientService = StatClientService.getInstance();
				if (statClientService != null)
					statClientService.sendUserRegistFlow("飘渺寻仙曲", userRegistFlow);

				logger.info("[" + conn.getName() + "] [用户注册并登录] [userType:" + userType + "] [成功] [username:" + logEntity.username + "] [passwd:" + passwd
						+ "] " + logEntity.getLogString() + " [cost:"+(System.currentTimeMillis() - now)+"ms]");
				return res;

			} catch (UsernameAlreadyExistsException ex) {
				StatManager stat = StatManager.getInstance();
				if (stat != null) {
					stat.notifyRegister(logEntity.clientId, logEntity.username, false, logEntity.channel);
				}
				String reason = Translate.登录失败用户名已存在;
				USER_LOGIN_RES res = new USER_LOGIN_RES(logEntity.sequnceNum, (byte) 2, reason, "", "登录失败");
				logger.info("[" + conn.getName() + "] [用户登录] [userType:" + userType + "] [失败] [" + reason + "] [username:" + logEntity.username + "] [passwd:"
						+ passwd + "] [authCode:--] " + logEntity.getLogString());
				return res;
			} catch (Exception ex) {
				StatManager stat = StatManager.getInstance();
				if (stat != null) {
					stat.notifyRegister(logEntity.clientId, logEntity.username, false, logEntity.channel);
				}
				String reason = Translate.登录失败注册时出现未知错误;
				USER_LOGIN_RES res = new USER_LOGIN_RES(logEntity.sequnceNum, (byte) 2, reason, "", "登录失败");
				logger.info("[" + conn.getName() + "] [用户登录] [userType:" + userType + "] [失败] [" + reason + "] [username:" + logEntity.username + "] [passwd:"
						+ passwd + "] [authCode:--] " + logEntity.getLogString(), ex);
				return res;
			}
		}

		// 已经存在的用户
		if (passportForUid != null) {
			//腾讯全部是专服，不需要做用户重名的判断
			if (channel.equals(passportForUid.getRegisterChannel()) || userType.equals("GUOPANSDKUSER") || userType.equals("QQUSER") || userType.contains("UC") || userType.contains("DUOKU") || userType.contains("WANDOUJIASDK") || userType.contains("XIAOMIUSER") || userType.contains("YIJIEUSER") || userType.contains("XYSDK") || userType.contains("91USER") || userType.contains("BAIDUSDKUSER")|| userType.contains("HUAWEISDKUSER")|| userType.contains("MEIZUUSER") ) {
				if (StringUtil.hash(passwd).equals(passportForUid.getPassWd()) == false) {
					passportForUid.setPassWd(StringUtil.hash(passwd));
					try {
						boolean isOk = MieshiGatewaySubSystem.getInstance().getBossClientService().update(passportForUid);
						if (isOk) {
							logger.info("[" + conn.getName() + "] [用户登录] [userType:" + userType + "] [自动修改密码成功] [渠道用户可能修改了密码] [username:" + logEntity.username
									+ "] [passwd:" + passwd + "] [authCode:--] " + logEntity.getLogString());
						} else {
							logger.info("[" + conn.getName() + "] [用户登录] [userType:" + userType + "] [自动修改密码失败] [渠道用户可能修改了密码] [username:" + logEntity.username
									+ "] [passwd:" + passwd + "] [authCode:--] " + logEntity.getLogString());
						}
					} catch (Exception e) {
						logger.info("[" + conn.getName() + "] [用户登录] [userType:" + userType + "] [自动修改密码失败] [渠道用户可能修改了密码] [username:" + logEntity.username
								+ "] [passwd:" + passwd + "] [authCode:--] " + logEntity.getLogString(), e);
					}
				}

				if (passportForUid.getNickName() == null || (!logEntity.username.equals(userIdInChannel) && !passportForUid.getNickName().equals(logEntity.username))) {
					Passport passport4NickName = null;
					try {
						passport4NickName = MieshiGatewaySubSystem.getInstance().getBossClientService().getPassportByUserName(logEntity.username);
					} catch (Exception e1) {
					}
					
					if(passport4NickName == null){
						passportForUid.setNickName(logEntity.username);
						try {
							boolean isOk = MieshiGatewaySubSystem.getInstance().getBossClientService().update(passportForUid);
							if (isOk) {
								logger.info("[" + conn.getName() + "] [用户登录] [userType:" + userType + "] [自动修改nickName成功] [username:" + logEntity.username
										+ "] [uid:" + userIdInChannel + "] [nickname:" + logEntity.username + "] [passwd:" + passwd + "] [authCode:--] "
										+ logEntity.getLogString());
							} else {
								logger.info("[" + conn.getName() + "] [用户登录] [userType:" + userType + "] [自动修改nickName失败] [username:" + logEntity.username
										+ "] [uid:" + userIdInChannel + "] [nickname:" + logEntity.username + "] [passwd:" + passwd + "] [authCode:--] "
										+ logEntity.getLogString());
							}
						} catch (Exception e) {
							logger.info("[" + conn.getName() + "] [用户登录] [userType:" + userType + "] [自动修改nickName失败] [username:" + logEntity.username + "] [uid:"
									+ userIdInChannel + "] [nickname:" + logEntity.username + "] [passwd:" + passwd + "] [authCode:--] " + logEntity.getLogString(), e);
						}
					}
				}

			} else {
				String reason = Translate.登录失败存在同名用户但来自不同渠道;
				USER_LOGIN_RES res = new USER_LOGIN_RES(logEntity.sequnceNum, (byte) 2, reason, "", "登录失败");
				logger.info("[" + conn.getName() + "] [用户登录] [userType:" + userType + "] [失败1] [" + reason + ",other_channel=" + passportForUid.getRegisterChannel()
						+ "] [username:" + logEntity.username + "] [passwd:" + passwd + "] [authCode:--] " + logEntity.getLogString());
				return res;
			}
		}

		USER_LOGIN_RES res = new USER_LOGIN_RES(logEntity.sequnceNum, (byte) 0, "", "", "");
		conn.setAttachment(passport);
		
		try {
			MieshiGatewaySubSystem.getInstance().getBossClientService().login(logEntity.username, passwd, logEntity.clientId, logEntity.channel, logEntity.platform,
					logEntity.phoneType, logEntity.networkMode);
		} catch (Exception e) {
			logger.error("[登录更新Passport信息时发生异常] ["+logEntity.username+"]", e);
		}

//		StatManager stat = StatManager.getInstance();
//		if (stat != null) {
//			stat.notifyLogin(logEntity.clientId, logEntity.username, true);
//		}

		logger.info("[" + conn.getName() + "] [用户登录] [userType:" + userType + "] [成功] [ok] [username:" + logEntity.username + "] [passwd:" + passwd
				+ "] " + logEntity.getLogString() + " [cost:"+(System.currentTimeMillis() - now)+"ms]");
		return res;
	}
	
	
	//------------------------------------------------
	//------------------下面的handle 都是通过自动生成---请勿轻易生成，下面的计算方法必须保证与客户端一致-------------------
	//------------------------------------------------
	//------------------------------------------------
	//------------------下面的handle 都是通过自动生成---请勿轻易生成，下面的计算方法必须保证与客户端一致-------------------
	//------------------------------------------------
	//------------------------------------------------
	//------------------下面的handle 都是通过自动生成---请勿轻易生成，下面的计算方法必须保证与客户端一致-------------------
	//------------------------------------------------
	//TODO:~~~~~~~~~~~~~~~~~~
	//TODO:~~~~~~~~~~~~~~~~~~
	//TODO:~~~~~~~~~~~~~~~~~~
	//TODO:~~~~~~~~~~~~~~~~~~
	
	public ResponseMessage handle_SERVER_HAND_CLIENT_1_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_1_REQ))) {
			noFindReq(conn, message2, 1);
			return null;
		}
		SERVER_HAND_CLIENT_1_REQ req = (SERVER_HAND_CLIENT_1_REQ)message_req;
		SERVER_HAND_CLIENT_1_RES res = (SERVER_HAND_CLIENT_1_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[30]-sendValues[94]*sendValues[2]-sendValues[58]-sendValues[33]*sendValues[38]-sendValues[70]+sendValues[26]-sendValues[23]+sendValues[93];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 1);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 1);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_2_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_2_REQ))) {
			noFindReq(conn, message2, 2);
			return null;
		}
		SERVER_HAND_CLIENT_2_REQ req = (SERVER_HAND_CLIENT_2_REQ)message_req;
		SERVER_HAND_CLIENT_2_RES res = (SERVER_HAND_CLIENT_2_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[95]/sendValues[87]-sendValues[19]/sendValues[3]-sendValues[70]*sendValues[33]*sendValues[51]*sendValues[45]-sendValues[65]+sendValues[71];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 2);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 2);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_3_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_3_REQ))) {
			noFindReq(conn, message2, 3);
			return null;
		}
		SERVER_HAND_CLIENT_3_REQ req = (SERVER_HAND_CLIENT_3_REQ)message_req;
		SERVER_HAND_CLIENT_3_RES res = (SERVER_HAND_CLIENT_3_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[69]-sendValues[25]/sendValues[42]/sendValues[51]-sendValues[52]-sendValues[10]*sendValues[3]/sendValues[1]-sendValues[99]*sendValues[96];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 3);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 3);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_4_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_4_REQ))) {
			noFindReq(conn, message2, 4);
			return null;
		}
		SERVER_HAND_CLIENT_4_REQ req = (SERVER_HAND_CLIENT_4_REQ)message_req;
		SERVER_HAND_CLIENT_4_RES res = (SERVER_HAND_CLIENT_4_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[76]/sendValues[8]-sendValues[60]/sendValues[42]+sendValues[29]/sendValues[62]-sendValues[68]/sendValues[94]/sendValues[53]+sendValues[13];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 4);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 4);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_5_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_5_REQ))) {
			noFindReq(conn, message2, 5);
			return null;
		}
		SERVER_HAND_CLIENT_5_REQ req = (SERVER_HAND_CLIENT_5_REQ)message_req;
		SERVER_HAND_CLIENT_5_RES res = (SERVER_HAND_CLIENT_5_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[6]/sendValues[12]-sendValues[78]/sendValues[99]-sendValues[39]-sendValues[81]-sendValues[26]*sendValues[34]/sendValues[80]+sendValues[10];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 5);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 5);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_6_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_6_REQ))) {
			noFindReq(conn, message2, 6);
			return null;
		}
		SERVER_HAND_CLIENT_6_REQ req = (SERVER_HAND_CLIENT_6_REQ)message_req;
		SERVER_HAND_CLIENT_6_RES res = (SERVER_HAND_CLIENT_6_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[24]/sendValues[13]+sendValues[26]*sendValues[93]/sendValues[45]/sendValues[88]-sendValues[51]*sendValues[47]*sendValues[53]/sendValues[0];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 6);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 6);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_7_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_7_REQ))) {
			noFindReq(conn, message2, 7);
			return null;
		}
		SERVER_HAND_CLIENT_7_REQ req = (SERVER_HAND_CLIENT_7_REQ)message_req;
		SERVER_HAND_CLIENT_7_RES res = (SERVER_HAND_CLIENT_7_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[40]+sendValues[62]+sendValues[82]-sendValues[33]+sendValues[57]*sendValues[32]+sendValues[67]/sendValues[79]*sendValues[77]-sendValues[59];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 7);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 7);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_8_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_8_REQ))) {
			noFindReq(conn, message2, 8);
			return null;
		}
		SERVER_HAND_CLIENT_8_REQ req = (SERVER_HAND_CLIENT_8_REQ)message_req;
		SERVER_HAND_CLIENT_8_RES res = (SERVER_HAND_CLIENT_8_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[0]*sendValues[98]+sendValues[53]/sendValues[13]-sendValues[35]/sendValues[97]-sendValues[93]*sendValues[56]*sendValues[19]-sendValues[78];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 8);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 8);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_9_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_9_REQ))) {
			noFindReq(conn, message2, 9);
			return null;
		}
		SERVER_HAND_CLIENT_9_REQ req = (SERVER_HAND_CLIENT_9_REQ)message_req;
		SERVER_HAND_CLIENT_9_RES res = (SERVER_HAND_CLIENT_9_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[60]*sendValues[7]-sendValues[33]+sendValues[78]*sendValues[83]/sendValues[39]-sendValues[37]/sendValues[50]-sendValues[24]-sendValues[9];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 9);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 9);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_10_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_10_REQ))) {
			noFindReq(conn, message2, 10);
			return null;
		}
		SERVER_HAND_CLIENT_10_REQ req = (SERVER_HAND_CLIENT_10_REQ)message_req;
		SERVER_HAND_CLIENT_10_RES res = (SERVER_HAND_CLIENT_10_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[35]-sendValues[79]/sendValues[24]/sendValues[30]/sendValues[66]-sendValues[4]+sendValues[18]*sendValues[69]*sendValues[16]-sendValues[32];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 10);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 10);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_11_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_11_REQ))) {
			noFindReq(conn, message2, 11);
			return null;
		}
		SERVER_HAND_CLIENT_11_REQ req = (SERVER_HAND_CLIENT_11_REQ)message_req;
		SERVER_HAND_CLIENT_11_RES res = (SERVER_HAND_CLIENT_11_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[1]+sendValues[52]-sendValues[98]*sendValues[3]*sendValues[54]-sendValues[88]/sendValues[91]/sendValues[8]-sendValues[44]*sendValues[61];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 11);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 11);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_12_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_12_REQ))) {
			noFindReq(conn, message2, 12);
			return null;
		}
		SERVER_HAND_CLIENT_12_REQ req = (SERVER_HAND_CLIENT_12_REQ)message_req;
		SERVER_HAND_CLIENT_12_RES res = (SERVER_HAND_CLIENT_12_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[98]+sendValues[74]-sendValues[94]+sendValues[87]/sendValues[20]-sendValues[78]/sendValues[90]*sendValues[29]/sendValues[56]*sendValues[95];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 12);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 12);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_13_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_13_REQ))) {
			noFindReq(conn, message2, 13);
			return null;
		}
		SERVER_HAND_CLIENT_13_REQ req = (SERVER_HAND_CLIENT_13_REQ)message_req;
		SERVER_HAND_CLIENT_13_RES res = (SERVER_HAND_CLIENT_13_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[37]-sendValues[50]/sendValues[54]*sendValues[7]/sendValues[17]*sendValues[96]/sendValues[86]*sendValues[94]-sendValues[38]/sendValues[6];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 13);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 13);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_14_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_14_REQ))) {
			noFindReq(conn, message2, 14);
			return null;
		}
		SERVER_HAND_CLIENT_14_REQ req = (SERVER_HAND_CLIENT_14_REQ)message_req;
		SERVER_HAND_CLIENT_14_RES res = (SERVER_HAND_CLIENT_14_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]*sendValues[39]/sendValues[21]*sendValues[68]+sendValues[32]+sendValues[8]/sendValues[91]/sendValues[23]+sendValues[51]*sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 14);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 14);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_15_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_15_REQ))) {
			noFindReq(conn, message2, 15);
			return null;
		}
		SERVER_HAND_CLIENT_15_REQ req = (SERVER_HAND_CLIENT_15_REQ)message_req;
		SERVER_HAND_CLIENT_15_RES res = (SERVER_HAND_CLIENT_15_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[53]*sendValues[74]/sendValues[70]-sendValues[12]/sendValues[93]*sendValues[27]/sendValues[57]-sendValues[96]+sendValues[9]+sendValues[71];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 15);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 15);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_16_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_16_REQ))) {
			noFindReq(conn, message2, 16);
			return null;
		}
		SERVER_HAND_CLIENT_16_REQ req = (SERVER_HAND_CLIENT_16_REQ)message_req;
		SERVER_HAND_CLIENT_16_RES res = (SERVER_HAND_CLIENT_16_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[94]*sendValues[35]*sendValues[23]-sendValues[67]+sendValues[37]*sendValues[58]*sendValues[7]+sendValues[15]+sendValues[97]+sendValues[77];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 16);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 16);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_17_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_17_REQ))) {
			noFindReq(conn, message2, 17);
			return null;
		}
		SERVER_HAND_CLIENT_17_REQ req = (SERVER_HAND_CLIENT_17_REQ)message_req;
		SERVER_HAND_CLIENT_17_RES res = (SERVER_HAND_CLIENT_17_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[80]/sendValues[66]-sendValues[20]-sendValues[55]/sendValues[41]+sendValues[79]*sendValues[18]/sendValues[25]/sendValues[43]+sendValues[59];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 17);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 17);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_18_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_18_REQ))) {
			noFindReq(conn, message2, 18);
			return null;
		}
		SERVER_HAND_CLIENT_18_REQ req = (SERVER_HAND_CLIENT_18_REQ)message_req;
		SERVER_HAND_CLIENT_18_RES res = (SERVER_HAND_CLIENT_18_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[61]-sendValues[20]*sendValues[34]/sendValues[76]-sendValues[68]-sendValues[73]*sendValues[58]/sendValues[65]*sendValues[16]+sendValues[93];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 18);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 18);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_19_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_19_REQ))) {
			noFindReq(conn, message2, 19);
			return null;
		}
		SERVER_HAND_CLIENT_19_REQ req = (SERVER_HAND_CLIENT_19_REQ)message_req;
		SERVER_HAND_CLIENT_19_RES res = (SERVER_HAND_CLIENT_19_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[71]-sendValues[99]+sendValues[95]+sendValues[33]+sendValues[16]-sendValues[6]+sendValues[93]-sendValues[35]-sendValues[31]+sendValues[86];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 19);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 19);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_20_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_20_REQ))) {
			noFindReq(conn, message2, 20);
			return null;
		}
		SERVER_HAND_CLIENT_20_REQ req = (SERVER_HAND_CLIENT_20_REQ)message_req;
		SERVER_HAND_CLIENT_20_RES res = (SERVER_HAND_CLIENT_20_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[73]*sendValues[10]*sendValues[22]*sendValues[12]*sendValues[40]-sendValues[33]-sendValues[83]+sendValues[5]+sendValues[41]-sendValues[38];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 20);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 20);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_21_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_21_REQ))) {
			noFindReq(conn, message2, 21);
			return null;
		}
		SERVER_HAND_CLIENT_21_REQ req = (SERVER_HAND_CLIENT_21_REQ)message_req;
		SERVER_HAND_CLIENT_21_RES res = (SERVER_HAND_CLIENT_21_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[28]-sendValues[44]+sendValues[81]+sendValues[97]*sendValues[51]*sendValues[95]*sendValues[52]+sendValues[89]-sendValues[57]+sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 21);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 21);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_22_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_22_REQ))) {
			noFindReq(conn, message2, 22);
			return null;
		}
		SERVER_HAND_CLIENT_22_REQ req = (SERVER_HAND_CLIENT_22_REQ)message_req;
		SERVER_HAND_CLIENT_22_RES res = (SERVER_HAND_CLIENT_22_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[65]*sendValues[70]/sendValues[98]/sendValues[36]*sendValues[90]*sendValues[66]*sendValues[77]-sendValues[50]*sendValues[68]+sendValues[45];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 22);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 22);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_23_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_23_REQ))) {
			noFindReq(conn, message2, 23);
			return null;
		}
		SERVER_HAND_CLIENT_23_REQ req = (SERVER_HAND_CLIENT_23_REQ)message_req;
		SERVER_HAND_CLIENT_23_RES res = (SERVER_HAND_CLIENT_23_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[88]/sendValues[28]-sendValues[41]+sendValues[51]-sendValues[31]+sendValues[42]*sendValues[24]+sendValues[82]/sendValues[78]*sendValues[77];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 23);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 23);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_24_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_24_REQ))) {
			noFindReq(conn, message2, 24);
			return null;
		}
		SERVER_HAND_CLIENT_24_REQ req = (SERVER_HAND_CLIENT_24_REQ)message_req;
		SERVER_HAND_CLIENT_24_RES res = (SERVER_HAND_CLIENT_24_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[45]/sendValues[52]+sendValues[94]-sendValues[81]*sendValues[10]/sendValues[47]-sendValues[76]*sendValues[22]-sendValues[90]*sendValues[16];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 24);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 24);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_25_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_25_REQ))) {
			noFindReq(conn, message2, 25);
			return null;
		}
		SERVER_HAND_CLIENT_25_REQ req = (SERVER_HAND_CLIENT_25_REQ)message_req;
		SERVER_HAND_CLIENT_25_RES res = (SERVER_HAND_CLIENT_25_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[85]*sendValues[59]-sendValues[95]+sendValues[86]-sendValues[50]-sendValues[99]-sendValues[3]/sendValues[28]*sendValues[63]/sendValues[43];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 25);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 25);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_26_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_26_REQ))) {
			noFindReq(conn, message2, 26);
			return null;
		}
		SERVER_HAND_CLIENT_26_REQ req = (SERVER_HAND_CLIENT_26_REQ)message_req;
		SERVER_HAND_CLIENT_26_RES res = (SERVER_HAND_CLIENT_26_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[76]-sendValues[83]*sendValues[65]*sendValues[91]*sendValues[51]-sendValues[29]-sendValues[71]/sendValues[57]+sendValues[38]-sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 26);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 26);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_27_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_27_REQ))) {
			noFindReq(conn, message2, 27);
			return null;
		}
		SERVER_HAND_CLIENT_27_REQ req = (SERVER_HAND_CLIENT_27_REQ)message_req;
		SERVER_HAND_CLIENT_27_RES res = (SERVER_HAND_CLIENT_27_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[36]-sendValues[62]-sendValues[80]/sendValues[94]-sendValues[53]-sendValues[29]-sendValues[30]/sendValues[4]*sendValues[9]*sendValues[13];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 27);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 27);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_28_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_28_REQ))) {
			noFindReq(conn, message2, 28);
			return null;
		}
		SERVER_HAND_CLIENT_28_REQ req = (SERVER_HAND_CLIENT_28_REQ)message_req;
		SERVER_HAND_CLIENT_28_RES res = (SERVER_HAND_CLIENT_28_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[43]-sendValues[74]*sendValues[66]-sendValues[12]-sendValues[59]+sendValues[41]-sendValues[70]-sendValues[35]*sendValues[92]+sendValues[25];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 28);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 28);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_29_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_29_REQ))) {
			noFindReq(conn, message2, 29);
			return null;
		}
		SERVER_HAND_CLIENT_29_REQ req = (SERVER_HAND_CLIENT_29_REQ)message_req;
		SERVER_HAND_CLIENT_29_RES res = (SERVER_HAND_CLIENT_29_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[84]*sendValues[1]+sendValues[13]-sendValues[49]-sendValues[32]+sendValues[55]+sendValues[0]/sendValues[99]*sendValues[82]+sendValues[8];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 29);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 29);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_30_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_30_REQ))) {
			noFindReq(conn, message2, 30);
			return null;
		}
		SERVER_HAND_CLIENT_30_REQ req = (SERVER_HAND_CLIENT_30_REQ)message_req;
		SERVER_HAND_CLIENT_30_RES res = (SERVER_HAND_CLIENT_30_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[9]*sendValues[7]*sendValues[45]*sendValues[56]/sendValues[41]-sendValues[26]/sendValues[16]/sendValues[68]+sendValues[23]*sendValues[73];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 30);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 30);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_31_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_31_REQ))) {
			noFindReq(conn, message2, 31);
			return null;
		}
		SERVER_HAND_CLIENT_31_REQ req = (SERVER_HAND_CLIENT_31_REQ)message_req;
		SERVER_HAND_CLIENT_31_RES res = (SERVER_HAND_CLIENT_31_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[52]*sendValues[95]+sendValues[35]+sendValues[47]+sendValues[14]-sendValues[81]+sendValues[80]*sendValues[41]/sendValues[20]/sendValues[29];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 31);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 31);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_32_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_32_REQ))) {
			noFindReq(conn, message2, 32);
			return null;
		}
		SERVER_HAND_CLIENT_32_REQ req = (SERVER_HAND_CLIENT_32_REQ)message_req;
		SERVER_HAND_CLIENT_32_RES res = (SERVER_HAND_CLIENT_32_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[13]*sendValues[30]*sendValues[25]*sendValues[92]/sendValues[56]+sendValues[22]*sendValues[80]*sendValues[35]*sendValues[12]-sendValues[61];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 32);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 32);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_33_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_33_REQ))) {
			noFindReq(conn, message2, 33);
			return null;
		}
		SERVER_HAND_CLIENT_33_REQ req = (SERVER_HAND_CLIENT_33_REQ)message_req;
		SERVER_HAND_CLIENT_33_RES res = (SERVER_HAND_CLIENT_33_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[38]-sendValues[51]*sendValues[64]/sendValues[8]+sendValues[90]+sendValues[89]+sendValues[85]*sendValues[78]*sendValues[74]-sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 33);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 33);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_34_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_34_REQ))) {
			noFindReq(conn, message2, 34);
			return null;
		}
		SERVER_HAND_CLIENT_34_REQ req = (SERVER_HAND_CLIENT_34_REQ)message_req;
		SERVER_HAND_CLIENT_34_RES res = (SERVER_HAND_CLIENT_34_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[24]/sendValues[86]/sendValues[48]-sendValues[19]/sendValues[54]+sendValues[69]*sendValues[35]*sendValues[95]+sendValues[51]+sendValues[29];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 34);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 34);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_35_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_35_REQ))) {
			noFindReq(conn, message2, 35);
			return null;
		}
		SERVER_HAND_CLIENT_35_REQ req = (SERVER_HAND_CLIENT_35_REQ)message_req;
		SERVER_HAND_CLIENT_35_RES res = (SERVER_HAND_CLIENT_35_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[70]+sendValues[81]+sendValues[42]+sendValues[29]*sendValues[79]-sendValues[63]/sendValues[76]*sendValues[55]+sendValues[87]+sendValues[46];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 35);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 35);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_36_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_36_REQ))) {
			noFindReq(conn, message2, 36);
			return null;
		}
		SERVER_HAND_CLIENT_36_REQ req = (SERVER_HAND_CLIENT_36_REQ)message_req;
		SERVER_HAND_CLIENT_36_RES res = (SERVER_HAND_CLIENT_36_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[97]-sendValues[19]+sendValues[24]/sendValues[13]-sendValues[69]-sendValues[2]-sendValues[30]+sendValues[37]-sendValues[83]-sendValues[20];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 36);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 36);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_37_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_37_REQ))) {
			noFindReq(conn, message2, 37);
			return null;
		}
		SERVER_HAND_CLIENT_37_REQ req = (SERVER_HAND_CLIENT_37_REQ)message_req;
		SERVER_HAND_CLIENT_37_RES res = (SERVER_HAND_CLIENT_37_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[61]+sendValues[34]*sendValues[82]*sendValues[83]/sendValues[99]+sendValues[63]*sendValues[70]/sendValues[11]+sendValues[73]-sendValues[47];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 37);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 37);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_38_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_38_REQ))) {
			noFindReq(conn, message2, 38);
			return null;
		}
		SERVER_HAND_CLIENT_38_REQ req = (SERVER_HAND_CLIENT_38_REQ)message_req;
		SERVER_HAND_CLIENT_38_RES res = (SERVER_HAND_CLIENT_38_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[22]/sendValues[99]*sendValues[90]*sendValues[58]+sendValues[89]*sendValues[34]-sendValues[44]+sendValues[14]/sendValues[71]+sendValues[85];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 38);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 38);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_39_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_39_REQ))) {
			noFindReq(conn, message2, 39);
			return null;
		}
		SERVER_HAND_CLIENT_39_REQ req = (SERVER_HAND_CLIENT_39_REQ)message_req;
		SERVER_HAND_CLIENT_39_RES res = (SERVER_HAND_CLIENT_39_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[12]+sendValues[46]*sendValues[99]*sendValues[42]+sendValues[76]/sendValues[50]-sendValues[58]+sendValues[41]+sendValues[8]/sendValues[69];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 39);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 39);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_40_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_40_REQ))) {
			noFindReq(conn, message2, 40);
			return null;
		}
		SERVER_HAND_CLIENT_40_REQ req = (SERVER_HAND_CLIENT_40_REQ)message_req;
		SERVER_HAND_CLIENT_40_RES res = (SERVER_HAND_CLIENT_40_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[26]/sendValues[28]+sendValues[90]+sendValues[88]+sendValues[10]-sendValues[94]*sendValues[97]*sendValues[98]/sendValues[55]*sendValues[23];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 40);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 40);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_41_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_41_REQ))) {
			noFindReq(conn, message2, 41);
			return null;
		}
		SERVER_HAND_CLIENT_41_REQ req = (SERVER_HAND_CLIENT_41_REQ)message_req;
		SERVER_HAND_CLIENT_41_RES res = (SERVER_HAND_CLIENT_41_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[14]*sendValues[7]*sendValues[12]*sendValues[25]/sendValues[74]+sendValues[96]/sendValues[38]-sendValues[2]-sendValues[9]+sendValues[18];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 41);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 41);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_42_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_42_REQ))) {
			noFindReq(conn, message2, 42);
			return null;
		}
		SERVER_HAND_CLIENT_42_REQ req = (SERVER_HAND_CLIENT_42_REQ)message_req;
		SERVER_HAND_CLIENT_42_RES res = (SERVER_HAND_CLIENT_42_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[79]+sendValues[86]-sendValues[45]-sendValues[97]+sendValues[57]/sendValues[34]/sendValues[48]+sendValues[74]/sendValues[85]/sendValues[21];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 42);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 42);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_43_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_43_REQ))) {
			noFindReq(conn, message2, 43);
			return null;
		}
		SERVER_HAND_CLIENT_43_REQ req = (SERVER_HAND_CLIENT_43_REQ)message_req;
		SERVER_HAND_CLIENT_43_RES res = (SERVER_HAND_CLIENT_43_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[86]*sendValues[88]-sendValues[24]/sendValues[94]+sendValues[66]*sendValues[32]-sendValues[80]+sendValues[3]*sendValues[65]/sendValues[22];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 43);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 43);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_44_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_44_REQ))) {
			noFindReq(conn, message2, 44);
			return null;
		}
		SERVER_HAND_CLIENT_44_REQ req = (SERVER_HAND_CLIENT_44_REQ)message_req;
		SERVER_HAND_CLIENT_44_RES res = (SERVER_HAND_CLIENT_44_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[62]-sendValues[18]+sendValues[68]/sendValues[50]-sendValues[34]+sendValues[85]-sendValues[56]+sendValues[39]/sendValues[94]/sendValues[58];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 44);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 44);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_45_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_45_REQ))) {
			noFindReq(conn, message2, 45);
			return null;
		}
		SERVER_HAND_CLIENT_45_REQ req = (SERVER_HAND_CLIENT_45_REQ)message_req;
		SERVER_HAND_CLIENT_45_RES res = (SERVER_HAND_CLIENT_45_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[31]/sendValues[93]/sendValues[17]*sendValues[7]-sendValues[91]*sendValues[15]+sendValues[41]-sendValues[9]/sendValues[36]*sendValues[76];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 45);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 45);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_46_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_46_REQ))) {
			noFindReq(conn, message2, 46);
			return null;
		}
		SERVER_HAND_CLIENT_46_REQ req = (SERVER_HAND_CLIENT_46_REQ)message_req;
		SERVER_HAND_CLIENT_46_RES res = (SERVER_HAND_CLIENT_46_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[83]*sendValues[72]+sendValues[23]*sendValues[13]+sendValues[21]*sendValues[42]*sendValues[81]-sendValues[14]/sendValues[32]/sendValues[91];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 46);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 46);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_47_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_47_REQ))) {
			noFindReq(conn, message2, 47);
			return null;
		}
		SERVER_HAND_CLIENT_47_REQ req = (SERVER_HAND_CLIENT_47_REQ)message_req;
		SERVER_HAND_CLIENT_47_RES res = (SERVER_HAND_CLIENT_47_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[82]+sendValues[17]/sendValues[96]*sendValues[80]-sendValues[35]*sendValues[38]/sendValues[30]+sendValues[44]+sendValues[11]*sendValues[49];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 47);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 47);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_48_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_48_REQ))) {
			noFindReq(conn, message2, 48);
			return null;
		}
		SERVER_HAND_CLIENT_48_REQ req = (SERVER_HAND_CLIENT_48_REQ)message_req;
		SERVER_HAND_CLIENT_48_RES res = (SERVER_HAND_CLIENT_48_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[56]/sendValues[78]/sendValues[17]*sendValues[18]-sendValues[9]-sendValues[25]-sendValues[65]+sendValues[26]/sendValues[40]*sendValues[95];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 48);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 48);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_49_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_49_REQ))) {
			noFindReq(conn, message2, 49);
			return null;
		}
		SERVER_HAND_CLIENT_49_REQ req = (SERVER_HAND_CLIENT_49_REQ)message_req;
		SERVER_HAND_CLIENT_49_RES res = (SERVER_HAND_CLIENT_49_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]*sendValues[3]-sendValues[29]/sendValues[9]/sendValues[34]*sendValues[46]/sendValues[42]+sendValues[95]+sendValues[35]/sendValues[96];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 49);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 49);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_50_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_50_REQ))) {
			noFindReq(conn, message2, 50);
			return null;
		}
		SERVER_HAND_CLIENT_50_REQ req = (SERVER_HAND_CLIENT_50_REQ)message_req;
		SERVER_HAND_CLIENT_50_RES res = (SERVER_HAND_CLIENT_50_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[10]*sendValues[84]*sendValues[74]*sendValues[89]+sendValues[19]-sendValues[85]-sendValues[1]+sendValues[62]-sendValues[58]*sendValues[57];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 50);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 50);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_51_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_51_REQ))) {
			noFindReq(conn, message2, 51);
			return null;
		}
		SERVER_HAND_CLIENT_51_REQ req = (SERVER_HAND_CLIENT_51_REQ)message_req;
		SERVER_HAND_CLIENT_51_RES res = (SERVER_HAND_CLIENT_51_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[33]*sendValues[35]-sendValues[8]*sendValues[74]-sendValues[73]+sendValues[61]/sendValues[37]/sendValues[68]-sendValues[69]/sendValues[16];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 51);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 51);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_52_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_52_REQ))) {
			noFindReq(conn, message2, 52);
			return null;
		}
		SERVER_HAND_CLIENT_52_REQ req = (SERVER_HAND_CLIENT_52_REQ)message_req;
		SERVER_HAND_CLIENT_52_RES res = (SERVER_HAND_CLIENT_52_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[48]-sendValues[70]/sendValues[53]*sendValues[57]-sendValues[96]-sendValues[44]+sendValues[10]*sendValues[47]*sendValues[60]-sendValues[40];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 52);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 52);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_53_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_53_REQ))) {
			noFindReq(conn, message2, 53);
			return null;
		}
		SERVER_HAND_CLIENT_53_REQ req = (SERVER_HAND_CLIENT_53_REQ)message_req;
		SERVER_HAND_CLIENT_53_RES res = (SERVER_HAND_CLIENT_53_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[77]-sendValues[34]+sendValues[84]*sendValues[21]-sendValues[4]-sendValues[19]/sendValues[30]*sendValues[65]*sendValues[62]+sendValues[27];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 53);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 53);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_54_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_54_REQ))) {
			noFindReq(conn, message2, 54);
			return null;
		}
		SERVER_HAND_CLIENT_54_REQ req = (SERVER_HAND_CLIENT_54_REQ)message_req;
		SERVER_HAND_CLIENT_54_RES res = (SERVER_HAND_CLIENT_54_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[76]/sendValues[33]*sendValues[22]+sendValues[93]+sendValues[11]/sendValues[36]*sendValues[3]*sendValues[58]-sendValues[96]-sendValues[40];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 54);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 54);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_55_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_55_REQ))) {
			noFindReq(conn, message2, 55);
			return null;
		}
		SERVER_HAND_CLIENT_55_REQ req = (SERVER_HAND_CLIENT_55_REQ)message_req;
		SERVER_HAND_CLIENT_55_RES res = (SERVER_HAND_CLIENT_55_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[20]*sendValues[53]-sendValues[47]*sendValues[68]+sendValues[83]-sendValues[80]-sendValues[30]*sendValues[85]-sendValues[86]/sendValues[21];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 55);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 55);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_56_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_56_REQ))) {
			noFindReq(conn, message2, 56);
			return null;
		}
		SERVER_HAND_CLIENT_56_REQ req = (SERVER_HAND_CLIENT_56_REQ)message_req;
		SERVER_HAND_CLIENT_56_RES res = (SERVER_HAND_CLIENT_56_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[83]*sendValues[73]*sendValues[45]-sendValues[55]-sendValues[14]+sendValues[61]+sendValues[96]+sendValues[24]/sendValues[34]*sendValues[79];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 56);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 56);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_57_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_57_REQ))) {
			noFindReq(conn, message2, 57);
			return null;
		}
		SERVER_HAND_CLIENT_57_REQ req = (SERVER_HAND_CLIENT_57_REQ)message_req;
		SERVER_HAND_CLIENT_57_RES res = (SERVER_HAND_CLIENT_57_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[44]+sendValues[93]/sendValues[2]*sendValues[76]+sendValues[74]/sendValues[4]-sendValues[38]+sendValues[65]/sendValues[85]/sendValues[18];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 57);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 57);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_58_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_58_REQ))) {
			noFindReq(conn, message2, 58);
			return null;
		}
		SERVER_HAND_CLIENT_58_REQ req = (SERVER_HAND_CLIENT_58_REQ)message_req;
		SERVER_HAND_CLIENT_58_RES res = (SERVER_HAND_CLIENT_58_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[38]-sendValues[89]*sendValues[92]-sendValues[48]+sendValues[29]+sendValues[51]+sendValues[90]*sendValues[30]/sendValues[3]-sendValues[11];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 58);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 58);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_59_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_59_REQ))) {
			noFindReq(conn, message2, 59);
			return null;
		}
		SERVER_HAND_CLIENT_59_REQ req = (SERVER_HAND_CLIENT_59_REQ)message_req;
		SERVER_HAND_CLIENT_59_RES res = (SERVER_HAND_CLIENT_59_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[17]-sendValues[61]/sendValues[50]*sendValues[85]-sendValues[9]+sendValues[49]*sendValues[88]*sendValues[39]*sendValues[51]*sendValues[8];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 59);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 59);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_60_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_60_REQ))) {
			noFindReq(conn, message2, 60);
			return null;
		}
		SERVER_HAND_CLIENT_60_REQ req = (SERVER_HAND_CLIENT_60_REQ)message_req;
		SERVER_HAND_CLIENT_60_RES res = (SERVER_HAND_CLIENT_60_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[12]-sendValues[17]/sendValues[76]+sendValues[16]-sendValues[22]-sendValues[97]+sendValues[36]+sendValues[26]*sendValues[56]-sendValues[42];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 60);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 60);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_61_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_61_REQ))) {
			noFindReq(conn, message2, 61);
			return null;
		}
		SERVER_HAND_CLIENT_61_REQ req = (SERVER_HAND_CLIENT_61_REQ)message_req;
		SERVER_HAND_CLIENT_61_RES res = (SERVER_HAND_CLIENT_61_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[96]-sendValues[34]+sendValues[9]*sendValues[11]*sendValues[76]/sendValues[56]-sendValues[19]-sendValues[79]-sendValues[94]-sendValues[97];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 61);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 61);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_62_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_62_REQ))) {
			noFindReq(conn, message2, 62);
			return null;
		}
		SERVER_HAND_CLIENT_62_REQ req = (SERVER_HAND_CLIENT_62_REQ)message_req;
		SERVER_HAND_CLIENT_62_RES res = (SERVER_HAND_CLIENT_62_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[44]*sendValues[18]*sendValues[76]*sendValues[53]-sendValues[65]/sendValues[93]+sendValues[34]+sendValues[50]+sendValues[48]-sendValues[7];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 62);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 62);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_63_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_63_REQ))) {
			noFindReq(conn, message2, 63);
			return null;
		}
		SERVER_HAND_CLIENT_63_REQ req = (SERVER_HAND_CLIENT_63_REQ)message_req;
		SERVER_HAND_CLIENT_63_RES res = (SERVER_HAND_CLIENT_63_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[71]*sendValues[92]/sendValues[46]*sendValues[97]-sendValues[30]+sendValues[1]*sendValues[64]+sendValues[4]/sendValues[38]*sendValues[73];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 63);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 63);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_64_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_64_REQ))) {
			noFindReq(conn, message2, 64);
			return null;
		}
		SERVER_HAND_CLIENT_64_REQ req = (SERVER_HAND_CLIENT_64_REQ)message_req;
		SERVER_HAND_CLIENT_64_RES res = (SERVER_HAND_CLIENT_64_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[78]+sendValues[11]*sendValues[61]*sendValues[56]/sendValues[23]+sendValues[29]-sendValues[7]*sendValues[2]-sendValues[49]+sendValues[88];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 64);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 64);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_65_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_65_REQ))) {
			noFindReq(conn, message2, 65);
			return null;
		}
		SERVER_HAND_CLIENT_65_REQ req = (SERVER_HAND_CLIENT_65_REQ)message_req;
		SERVER_HAND_CLIENT_65_RES res = (SERVER_HAND_CLIENT_65_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[43]/sendValues[31]-sendValues[44]*sendValues[94]+sendValues[49]+sendValues[42]-sendValues[85]/sendValues[27]/sendValues[61]*sendValues[26];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 65);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 65);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_66_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_66_REQ))) {
			noFindReq(conn, message2, 66);
			return null;
		}
		SERVER_HAND_CLIENT_66_REQ req = (SERVER_HAND_CLIENT_66_REQ)message_req;
		SERVER_HAND_CLIENT_66_RES res = (SERVER_HAND_CLIENT_66_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[13]/sendValues[26]*sendValues[69]-sendValues[90]+sendValues[27]*sendValues[28]-sendValues[15]-sendValues[10]*sendValues[62]-sendValues[4];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 66);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 66);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_67_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_67_REQ))) {
			noFindReq(conn, message2, 67);
			return null;
		}
		SERVER_HAND_CLIENT_67_REQ req = (SERVER_HAND_CLIENT_67_REQ)message_req;
		SERVER_HAND_CLIENT_67_RES res = (SERVER_HAND_CLIENT_67_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[7]-sendValues[93]-sendValues[70]+sendValues[32]/sendValues[75]-sendValues[76]+sendValues[74]*sendValues[4]/sendValues[82]+sendValues[41];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 67);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 67);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_68_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_68_REQ))) {
			noFindReq(conn, message2, 68);
			return null;
		}
		SERVER_HAND_CLIENT_68_REQ req = (SERVER_HAND_CLIENT_68_REQ)message_req;
		SERVER_HAND_CLIENT_68_RES res = (SERVER_HAND_CLIENT_68_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[94]-sendValues[30]-sendValues[38]*sendValues[59]-sendValues[70]+sendValues[74]-sendValues[28]*sendValues[86]*sendValues[4]+sendValues[56];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 68);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 68);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_69_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_69_REQ))) {
			noFindReq(conn, message2, 69);
			return null;
		}
		SERVER_HAND_CLIENT_69_REQ req = (SERVER_HAND_CLIENT_69_REQ)message_req;
		SERVER_HAND_CLIENT_69_RES res = (SERVER_HAND_CLIENT_69_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[12]*sendValues[23]-sendValues[86]/sendValues[79]/sendValues[72]*sendValues[57]+sendValues[13]*sendValues[37]+sendValues[26]+sendValues[1];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 69);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 69);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_70_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_70_REQ))) {
			noFindReq(conn, message2, 70);
			return null;
		}
		SERVER_HAND_CLIENT_70_REQ req = (SERVER_HAND_CLIENT_70_REQ)message_req;
		SERVER_HAND_CLIENT_70_RES res = (SERVER_HAND_CLIENT_70_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[90]/sendValues[36]*sendValues[87]+sendValues[97]-sendValues[93]*sendValues[70]/sendValues[31]/sendValues[22]*sendValues[69]-sendValues[88];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 70);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 70);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_71_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_71_REQ))) {
			noFindReq(conn, message2, 71);
			return null;
		}
		SERVER_HAND_CLIENT_71_REQ req = (SERVER_HAND_CLIENT_71_REQ)message_req;
		SERVER_HAND_CLIENT_71_RES res = (SERVER_HAND_CLIENT_71_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[11]*sendValues[47]+sendValues[91]-sendValues[55]/sendValues[62]+sendValues[77]/sendValues[93]-sendValues[46]/sendValues[40]*sendValues[7];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 71);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 71);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_72_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_72_REQ))) {
			noFindReq(conn, message2, 72);
			return null;
		}
		SERVER_HAND_CLIENT_72_REQ req = (SERVER_HAND_CLIENT_72_REQ)message_req;
		SERVER_HAND_CLIENT_72_RES res = (SERVER_HAND_CLIENT_72_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[25]-sendValues[68]*sendValues[9]*sendValues[27]*sendValues[81]-sendValues[98]-sendValues[7]+sendValues[75]-sendValues[85]-sendValues[78];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 72);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 72);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_73_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_73_REQ))) {
			noFindReq(conn, message2, 73);
			return null;
		}
		SERVER_HAND_CLIENT_73_REQ req = (SERVER_HAND_CLIENT_73_REQ)message_req;
		SERVER_HAND_CLIENT_73_RES res = (SERVER_HAND_CLIENT_73_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[50]+sendValues[1]-sendValues[33]*sendValues[69]-sendValues[71]-sendValues[0]/sendValues[12]-sendValues[65]-sendValues[67]+sendValues[5];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 73);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 73);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_74_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_74_REQ))) {
			noFindReq(conn, message2, 74);
			return null;
		}
		SERVER_HAND_CLIENT_74_REQ req = (SERVER_HAND_CLIENT_74_REQ)message_req;
		SERVER_HAND_CLIENT_74_RES res = (SERVER_HAND_CLIENT_74_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[93]/sendValues[22]+sendValues[86]/sendValues[6]/sendValues[33]-sendValues[85]/sendValues[95]+sendValues[39]+sendValues[16]/sendValues[87];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 74);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 74);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_75_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_75_REQ))) {
			noFindReq(conn, message2, 75);
			return null;
		}
		SERVER_HAND_CLIENT_75_REQ req = (SERVER_HAND_CLIENT_75_REQ)message_req;
		SERVER_HAND_CLIENT_75_RES res = (SERVER_HAND_CLIENT_75_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[63]/sendValues[28]*sendValues[12]*sendValues[52]/sendValues[47]+sendValues[38]-sendValues[59]+sendValues[33]/sendValues[35]-sendValues[92];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 75);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 75);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_76_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_76_REQ))) {
			noFindReq(conn, message2, 76);
			return null;
		}
		SERVER_HAND_CLIENT_76_REQ req = (SERVER_HAND_CLIENT_76_REQ)message_req;
		SERVER_HAND_CLIENT_76_RES res = (SERVER_HAND_CLIENT_76_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[84]-sendValues[47]/sendValues[36]+sendValues[24]-sendValues[20]*sendValues[26]-sendValues[45]*sendValues[42]-sendValues[48]*sendValues[78];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 76);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 76);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_77_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_77_REQ))) {
			noFindReq(conn, message2, 77);
			return null;
		}
		SERVER_HAND_CLIENT_77_REQ req = (SERVER_HAND_CLIENT_77_REQ)message_req;
		SERVER_HAND_CLIENT_77_RES res = (SERVER_HAND_CLIENT_77_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[63]*sendValues[11]+sendValues[65]*sendValues[47]-sendValues[77]+sendValues[0]+sendValues[99]*sendValues[82]/sendValues[14]*sendValues[16];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 77);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 77);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_78_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_78_REQ))) {
			noFindReq(conn, message2, 78);
			return null;
		}
		SERVER_HAND_CLIENT_78_REQ req = (SERVER_HAND_CLIENT_78_REQ)message_req;
		SERVER_HAND_CLIENT_78_RES res = (SERVER_HAND_CLIENT_78_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[43]*sendValues[2]*sendValues[58]-sendValues[22]-sendValues[54]*sendValues[70]-sendValues[50]+sendValues[41]-sendValues[55]/sendValues[7];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 78);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 78);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_79_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_79_REQ))) {
			noFindReq(conn, message2, 79);
			return null;
		}
		SERVER_HAND_CLIENT_79_REQ req = (SERVER_HAND_CLIENT_79_REQ)message_req;
		SERVER_HAND_CLIENT_79_RES res = (SERVER_HAND_CLIENT_79_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[0]+sendValues[11]+sendValues[25]+sendValues[10]+sendValues[96]+sendValues[38]+sendValues[85]-sendValues[54]+sendValues[33]-sendValues[56];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 79);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 79);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_80_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_80_REQ))) {
			noFindReq(conn, message2, 80);
			return null;
		}
		SERVER_HAND_CLIENT_80_REQ req = (SERVER_HAND_CLIENT_80_REQ)message_req;
		SERVER_HAND_CLIENT_80_RES res = (SERVER_HAND_CLIENT_80_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[59]+sendValues[16]-sendValues[92]-sendValues[82]*sendValues[66]+sendValues[11]/sendValues[97]/sendValues[42]/sendValues[58]*sendValues[28];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 80);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 80);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_81_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_81_REQ))) {
			noFindReq(conn, message2, 81);
			return null;
		}
		SERVER_HAND_CLIENT_81_REQ req = (SERVER_HAND_CLIENT_81_REQ)message_req;
		SERVER_HAND_CLIENT_81_RES res = (SERVER_HAND_CLIENT_81_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[99]+sendValues[80]-sendValues[16]-sendValues[75]-sendValues[60]/sendValues[97]+sendValues[66]/sendValues[23]+sendValues[20]+sendValues[24];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 81);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 81);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_82_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_82_REQ))) {
			noFindReq(conn, message2, 82);
			return null;
		}
		SERVER_HAND_CLIENT_82_REQ req = (SERVER_HAND_CLIENT_82_REQ)message_req;
		SERVER_HAND_CLIENT_82_RES res = (SERVER_HAND_CLIENT_82_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[21]*sendValues[39]/sendValues[64]+sendValues[67]/sendValues[98]*sendValues[80]-sendValues[29]-sendValues[44]*sendValues[18]+sendValues[61];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 82);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 82);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_83_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_83_REQ))) {
			noFindReq(conn, message2, 83);
			return null;
		}
		SERVER_HAND_CLIENT_83_REQ req = (SERVER_HAND_CLIENT_83_REQ)message_req;
		SERVER_HAND_CLIENT_83_RES res = (SERVER_HAND_CLIENT_83_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[26]+sendValues[42]*sendValues[58]/sendValues[55]+sendValues[43]/sendValues[17]-sendValues[9]-sendValues[68]/sendValues[16]-sendValues[25];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 83);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 83);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_84_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_84_REQ))) {
			noFindReq(conn, message2, 84);
			return null;
		}
		SERVER_HAND_CLIENT_84_REQ req = (SERVER_HAND_CLIENT_84_REQ)message_req;
		SERVER_HAND_CLIENT_84_RES res = (SERVER_HAND_CLIENT_84_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[66]*sendValues[20]*sendValues[55]-sendValues[3]+sendValues[43]/sendValues[18]*sendValues[10]+sendValues[89]/sendValues[34]-sendValues[49];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 84);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 84);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_85_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_85_REQ))) {
			noFindReq(conn, message2, 85);
			return null;
		}
		SERVER_HAND_CLIENT_85_REQ req = (SERVER_HAND_CLIENT_85_REQ)message_req;
		SERVER_HAND_CLIENT_85_RES res = (SERVER_HAND_CLIENT_85_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]+sendValues[22]*sendValues[78]+sendValues[20]*sendValues[28]+sendValues[7]*sendValues[0]*sendValues[30]+sendValues[35]*sendValues[40];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 85);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 85);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_86_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_86_REQ))) {
			noFindReq(conn, message2, 86);
			return null;
		}
		SERVER_HAND_CLIENT_86_REQ req = (SERVER_HAND_CLIENT_86_REQ)message_req;
		SERVER_HAND_CLIENT_86_RES res = (SERVER_HAND_CLIENT_86_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]-sendValues[13]*sendValues[8]/sendValues[90]+sendValues[5]-sendValues[64]*sendValues[96]+sendValues[83]/sendValues[95]+sendValues[46];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 86);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 86);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_87_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_87_REQ))) {
			noFindReq(conn, message2, 87);
			return null;
		}
		SERVER_HAND_CLIENT_87_REQ req = (SERVER_HAND_CLIENT_87_REQ)message_req;
		SERVER_HAND_CLIENT_87_RES res = (SERVER_HAND_CLIENT_87_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[70]/sendValues[12]-sendValues[44]*sendValues[75]+sendValues[40]+sendValues[5]/sendValues[59]*sendValues[36]*sendValues[32]/sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 87);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 87);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_88_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_88_REQ))) {
			noFindReq(conn, message2, 88);
			return null;
		}
		SERVER_HAND_CLIENT_88_REQ req = (SERVER_HAND_CLIENT_88_REQ)message_req;
		SERVER_HAND_CLIENT_88_RES res = (SERVER_HAND_CLIENT_88_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[41]-sendValues[97]/sendValues[63]-sendValues[90]-sendValues[68]/sendValues[69]*sendValues[43]+sendValues[33]+sendValues[31]-sendValues[64];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 88);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 88);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_89_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_89_REQ))) {
			noFindReq(conn, message2, 89);
			return null;
		}
		SERVER_HAND_CLIENT_89_REQ req = (SERVER_HAND_CLIENT_89_REQ)message_req;
		SERVER_HAND_CLIENT_89_RES res = (SERVER_HAND_CLIENT_89_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[69]*sendValues[73]-sendValues[63]*sendValues[36]*sendValues[98]*sendValues[2]/sendValues[31]+sendValues[10]-sendValues[53]+sendValues[6];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 89);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 89);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_90_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_90_REQ))) {
			noFindReq(conn, message2, 90);
			return null;
		}
		SERVER_HAND_CLIENT_90_REQ req = (SERVER_HAND_CLIENT_90_REQ)message_req;
		SERVER_HAND_CLIENT_90_RES res = (SERVER_HAND_CLIENT_90_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[67]/sendValues[99]+sendValues[51]*sendValues[35]-sendValues[14]+sendValues[80]/sendValues[63]/sendValues[34]*sendValues[33]*sendValues[57];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 90);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 90);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_91_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_91_REQ))) {
			noFindReq(conn, message2, 91);
			return null;
		}
		SERVER_HAND_CLIENT_91_REQ req = (SERVER_HAND_CLIENT_91_REQ)message_req;
		SERVER_HAND_CLIENT_91_RES res = (SERVER_HAND_CLIENT_91_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[14]+sendValues[39]/sendValues[80]+sendValues[56]*sendValues[52]/sendValues[99]*sendValues[11]+sendValues[90]*sendValues[37]/sendValues[50];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 91);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 91);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_92_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_92_REQ))) {
			noFindReq(conn, message2, 92);
			return null;
		}
		SERVER_HAND_CLIENT_92_REQ req = (SERVER_HAND_CLIENT_92_REQ)message_req;
		SERVER_HAND_CLIENT_92_RES res = (SERVER_HAND_CLIENT_92_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[57]-sendValues[4]-sendValues[54]-sendValues[17]/sendValues[12]/sendValues[35]-sendValues[29]-sendValues[79]+sendValues[65]+sendValues[91];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 92);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 92);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_93_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_93_REQ))) {
			noFindReq(conn, message2, 93);
			return null;
		}
		SERVER_HAND_CLIENT_93_REQ req = (SERVER_HAND_CLIENT_93_REQ)message_req;
		SERVER_HAND_CLIENT_93_RES res = (SERVER_HAND_CLIENT_93_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[17]-sendValues[10]+sendValues[66]/sendValues[67]+sendValues[57]/sendValues[43]+sendValues[95]/sendValues[82]*sendValues[7]-sendValues[42];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 93);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 93);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_94_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_94_REQ))) {
			noFindReq(conn, message2, 94);
			return null;
		}
		SERVER_HAND_CLIENT_94_REQ req = (SERVER_HAND_CLIENT_94_REQ)message_req;
		SERVER_HAND_CLIENT_94_RES res = (SERVER_HAND_CLIENT_94_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[15]*sendValues[86]-sendValues[71]-sendValues[54]-sendValues[4]/sendValues[95]-sendValues[28]+sendValues[80]*sendValues[32]+sendValues[34];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 94);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 94);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_95_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_95_REQ))) {
			noFindReq(conn, message2, 95);
			return null;
		}
		SERVER_HAND_CLIENT_95_REQ req = (SERVER_HAND_CLIENT_95_REQ)message_req;
		SERVER_HAND_CLIENT_95_RES res = (SERVER_HAND_CLIENT_95_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[43]*sendValues[76]+sendValues[71]/sendValues[15]-sendValues[33]-sendValues[12]+sendValues[77]-sendValues[96]-sendValues[86]-sendValues[21];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 95);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 95);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_96_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_96_REQ))) {
			noFindReq(conn, message2, 96);
			return null;
		}
		SERVER_HAND_CLIENT_96_REQ req = (SERVER_HAND_CLIENT_96_REQ)message_req;
		SERVER_HAND_CLIENT_96_RES res = (SERVER_HAND_CLIENT_96_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[77]/sendValues[86]/sendValues[66]*sendValues[32]*sendValues[26]*sendValues[39]-sendValues[65]/sendValues[2]*sendValues[97]*sendValues[7];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 96);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 96);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_97_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_97_REQ))) {
			noFindReq(conn, message2, 97);
			return null;
		}
		SERVER_HAND_CLIENT_97_REQ req = (SERVER_HAND_CLIENT_97_REQ)message_req;
		SERVER_HAND_CLIENT_97_RES res = (SERVER_HAND_CLIENT_97_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[54]*sendValues[45]/sendValues[63]*sendValues[78]*sendValues[21]*sendValues[12]-sendValues[23]*sendValues[32]*sendValues[70]-sendValues[41];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 97);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 97);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_98_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_98_REQ))) {
			noFindReq(conn, message2, 98);
			return null;
		}
		SERVER_HAND_CLIENT_98_REQ req = (SERVER_HAND_CLIENT_98_REQ)message_req;
		SERVER_HAND_CLIENT_98_RES res = (SERVER_HAND_CLIENT_98_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[74]-sendValues[56]*sendValues[94]-sendValues[79]/sendValues[39]/sendValues[14]-sendValues[85]/sendValues[35]/sendValues[30]/sendValues[71];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 98);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 98);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_99_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_99_REQ))) {
			noFindReq(conn, message2, 99);
			return null;
		}
		SERVER_HAND_CLIENT_99_REQ req = (SERVER_HAND_CLIENT_99_REQ)message_req;
		SERVER_HAND_CLIENT_99_RES res = (SERVER_HAND_CLIENT_99_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[99]/sendValues[61]*sendValues[97]-sendValues[24]/sendValues[48]*sendValues[88]/sendValues[37]-sendValues[44]+sendValues[21]+sendValues[67];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 99);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 99);
		return null;
	}

	public ResponseMessage handle_SERVER_HAND_CLIENT_100_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_100_REQ))) {
			noFindReq(conn, message2, 100);
			return null;
		}
		SERVER_HAND_CLIENT_100_REQ req = (SERVER_HAND_CLIENT_100_REQ)message_req;
		SERVER_HAND_CLIENT_100_RES res = (SERVER_HAND_CLIENT_100_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[57]-sendValues[28]*sendValues[1]-sendValues[12]/sendValues[91]*sendValues[97]/sendValues[29]+sendValues[3]*sendValues[73]-sendValues[51];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 100);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 100);
		return null;
	}
	
	public ResponseMessage handle_TRY_GETPLAYERINFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_GETPLAYERINFO_REQ))) {
			noFindReq(conn, message2, 1);
			return null;
		}
		TRY_GETPLAYERINFO_REQ req = (TRY_GETPLAYERINFO_REQ)message_req;
		TRY_GETPLAYERINFO_RES res = (TRY_GETPLAYERINFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[79]*sendValues[38]*sendValues[61]*sendValues[36]/sendValues[87]-sendValues[10]/sendValues[86]/sendValues[93]-sendValues[78]+sendValues[85];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 1);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 1);
		return null;
	}

	public ResponseMessage handle_WAIT_FOR_OTHER_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof WAIT_FOR_OTHER_REQ))) {
			noFindReq(conn, message2, 2);
			return null;
		}
		WAIT_FOR_OTHER_REQ req = (WAIT_FOR_OTHER_REQ)message_req;
		WAIT_FOR_OTHER_RES res = (WAIT_FOR_OTHER_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[55]-sendValues[57]*sendValues[63]*sendValues[83]*sendValues[77]/sendValues[66]/sendValues[11]*sendValues[31]+sendValues[67]/sendValues[18];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 2);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 2);
		return null;
	}

	public ResponseMessage handle_GET_REWARD_2_PLAYER_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_REWARD_2_PLAYER_REQ))) {
			noFindReq(conn, message2, 3);
			return null;
		}
		GET_REWARD_2_PLAYER_REQ req = (GET_REWARD_2_PLAYER_REQ)message_req;
		GET_REWARD_2_PLAYER_RES res = (GET_REWARD_2_PLAYER_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[70]+sendValues[52]-sendValues[91]+sendValues[22]*sendValues[93]-sendValues[28]*sendValues[18]*sendValues[74]*sendValues[76]+sendValues[99];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 3);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 3);
		return null;
	}

	public ResponseMessage handle_REQUESTBUY_GET_ENTITY_INFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REQUESTBUY_GET_ENTITY_INFO_REQ))) {
			noFindReq(conn, message2, 4);
			return null;
		}
		REQUESTBUY_GET_ENTITY_INFO_REQ req = (REQUESTBUY_GET_ENTITY_INFO_REQ)message_req;
		REQUESTBUY_GET_ENTITY_INFO_RES res = (REQUESTBUY_GET_ENTITY_INFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[15]*sendValues[59]+sendValues[99]+sendValues[0]+sendValues[20]-sendValues[87]*sendValues[75]/sendValues[36]+sendValues[42]/sendValues[44];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 4);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 4);
		return null;
	}

	public ResponseMessage handle_PLAYER_SOUL_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_SOUL_REQ))) {
			noFindReq(conn, message2, 5);
			return null;
		}
		PLAYER_SOUL_REQ req = (PLAYER_SOUL_REQ)message_req;
		PLAYER_SOUL_RES res = (PLAYER_SOUL_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[34]-sendValues[16]*sendValues[27]*sendValues[71]-sendValues[49]/sendValues[87]-sendValues[90]/sendValues[6]/sendValues[93]/sendValues[95];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 5);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 5);
		return null;
	}

	public ResponseMessage handle_CARD_TRYSAVING_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CARD_TRYSAVING_REQ))) {
			noFindReq(conn, message2, 6);
			return null;
		}
		CARD_TRYSAVING_REQ req = (CARD_TRYSAVING_REQ)message_req;
		CARD_TRYSAVING_RES res = (CARD_TRYSAVING_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[45]/sendValues[91]*sendValues[34]*sendValues[35]*sendValues[81]-sendValues[21]/sendValues[56]/sendValues[89]/sendValues[10]/sendValues[41];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 6);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 6);
		return null;
	}

	public ResponseMessage handle_GANG_WAREHOUSE_JOURNAL_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GANG_WAREHOUSE_JOURNAL_REQ))) {
			noFindReq(conn, message2, 7);
			return null;
		}
		GANG_WAREHOUSE_JOURNAL_REQ req = (GANG_WAREHOUSE_JOURNAL_REQ)message_req;
		GANG_WAREHOUSE_JOURNAL_RES res = (GANG_WAREHOUSE_JOURNAL_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[38]/sendValues[50]*sendValues[58]+sendValues[24]*sendValues[42]-sendValues[20]*sendValues[89]/sendValues[18]+sendValues[36]/sendValues[99];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 7);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 7);
		return null;
	}

	public ResponseMessage handle_GET_WAREHOUSE_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_WAREHOUSE_REQ))) {
			noFindReq(conn, message2, 8);
			return null;
		}
		GET_WAREHOUSE_REQ req = (GET_WAREHOUSE_REQ)message_req;
		GET_WAREHOUSE_RES res = (GET_WAREHOUSE_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[32]*sendValues[18]/sendValues[88]/sendValues[19]-sendValues[80]+sendValues[7]-sendValues[90]+sendValues[42]/sendValues[31]+sendValues[49];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 8);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 8);
		return null;
	}

	public ResponseMessage handle_QUERY__GETAUTOBACK_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QUERY__GETAUTOBACK_REQ))) {
			noFindReq(conn, message2, 9);
			return null;
		}
		QUERY__GETAUTOBACK_REQ req = (QUERY__GETAUTOBACK_REQ)message_req;
		QUERY__GETAUTOBACK_RES res = (QUERY__GETAUTOBACK_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[17]-sendValues[63]/sendValues[8]*sendValues[75]*sendValues[61]-sendValues[74]*sendValues[48]*sendValues[56]-sendValues[5]-sendValues[23];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 9);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 9);
		return null;
	}

	public ResponseMessage handle_GET_ZONGPAI_NAME_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_ZONGPAI_NAME_REQ))) {
			noFindReq(conn, message2, 10);
			return null;
		}
		GET_ZONGPAI_NAME_REQ req = (GET_ZONGPAI_NAME_REQ)message_req;
		GET_ZONGPAI_NAME_RES res = (GET_ZONGPAI_NAME_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[14]+sendValues[12]*sendValues[99]/sendValues[25]/sendValues[76]-sendValues[20]*sendValues[42]/sendValues[24]-sendValues[27]+sendValues[49];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 10);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 10);
		return null;
	}

	public ResponseMessage handle_TRY_LEAVE_ZONGPAI_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_LEAVE_ZONGPAI_REQ))) {
			noFindReq(conn, message2, 11);
			return null;
		}
		TRY_LEAVE_ZONGPAI_REQ req = (TRY_LEAVE_ZONGPAI_REQ)message_req;
		TRY_LEAVE_ZONGPAI_RES res = (TRY_LEAVE_ZONGPAI_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[7]/sendValues[42]+sendValues[41]+sendValues[55]-sendValues[43]/sendValues[0]+sendValues[64]*sendValues[56]-sendValues[54]+sendValues[52];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 11);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 11);
		return null;
	}

	public ResponseMessage handle_REBEL_EVICT_NEW_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REBEL_EVICT_NEW_REQ))) {
			noFindReq(conn, message2, 12);
			return null;
		}
		REBEL_EVICT_NEW_REQ req = (REBEL_EVICT_NEW_REQ)message_req;
		REBEL_EVICT_NEW_RES res = (REBEL_EVICT_NEW_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[27]-sendValues[42]*sendValues[7]-sendValues[49]/sendValues[96]*sendValues[84]-sendValues[1]*sendValues[5]+sendValues[34]/sendValues[67];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 12);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 12);
		return null;
	}

	public ResponseMessage handle_GET_PLAYERTITLE_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_PLAYERTITLE_REQ))) {
			noFindReq(conn, message2, 13);
			return null;
		}
		GET_PLAYERTITLE_REQ req = (GET_PLAYERTITLE_REQ)message_req;
		GET_PLAYERTITLE_RES res = (GET_PLAYERTITLE_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[33]/sendValues[45]+sendValues[54]/sendValues[40]-sendValues[97]+sendValues[68]+sendValues[36]-sendValues[51]+sendValues[62]*sendValues[84];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 13);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 13);
		return null;
	}

	public ResponseMessage handle_MARRIAGE_TRY_BEQSTART_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MARRIAGE_TRY_BEQSTART_REQ))) {
			noFindReq(conn, message2, 14);
			return null;
		}
		MARRIAGE_TRY_BEQSTART_REQ req = (MARRIAGE_TRY_BEQSTART_REQ)message_req;
		MARRIAGE_TRY_BEQSTART_RES res = (MARRIAGE_TRY_BEQSTART_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[53]/sendValues[80]+sendValues[26]*sendValues[94]*sendValues[82]/sendValues[92]/sendValues[40]/sendValues[30]-sendValues[37]/sendValues[83];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 14);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 14);
		return null;
	}

	public ResponseMessage handle_MARRIAGE_GUESTNEW_OVER_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MARRIAGE_GUESTNEW_OVER_REQ))) {
			noFindReq(conn, message2, 15);
			return null;
		}
		MARRIAGE_GUESTNEW_OVER_REQ req = (MARRIAGE_GUESTNEW_OVER_REQ)message_req;
		MARRIAGE_GUESTNEW_OVER_RES res = (MARRIAGE_GUESTNEW_OVER_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[36]-sendValues[87]/sendValues[24]+sendValues[46]/sendValues[1]/sendValues[15]*sendValues[50]/sendValues[63]*sendValues[82]*sendValues[85];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 15);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 15);
		return null;
	}

	public ResponseMessage handle_MARRIAGE_HUNLI_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MARRIAGE_HUNLI_REQ))) {
			noFindReq(conn, message2, 16);
			return null;
		}
		MARRIAGE_HUNLI_REQ req = (MARRIAGE_HUNLI_REQ)message_req;
		MARRIAGE_HUNLI_RES res = (MARRIAGE_HUNLI_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[64]-sendValues[11]*sendValues[0]/sendValues[59]*sendValues[22]*sendValues[4]-sendValues[56]+sendValues[40]-sendValues[79]-sendValues[10];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 16);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 16);
		return null;
	}

	public ResponseMessage handle_COUNTRY_COMMENDCANCEL_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof COUNTRY_COMMENDCANCEL_REQ))) {
			noFindReq(conn, message2, 17);
			return null;
		}
		COUNTRY_COMMENDCANCEL_REQ req = (COUNTRY_COMMENDCANCEL_REQ)message_req;
		COUNTRY_COMMENDCANCEL_RES res = (COUNTRY_COMMENDCANCEL_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[13]-sendValues[46]*sendValues[91]+sendValues[26]*sendValues[4]*sendValues[69]/sendValues[29]/sendValues[30]-sendValues[81]-sendValues[5];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 17);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 17);
		return null;
	}

	public ResponseMessage handle_COUNTRY_NEWQIUJIN_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof COUNTRY_NEWQIUJIN_REQ))) {
			noFindReq(conn, message2, 18);
			return null;
		}
		COUNTRY_NEWQIUJIN_REQ req = (COUNTRY_NEWQIUJIN_REQ)message_req;
		COUNTRY_NEWQIUJIN_RES res = (COUNTRY_NEWQIUJIN_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[12]-sendValues[60]-sendValues[93]+sendValues[74]-sendValues[84]/sendValues[96]+sendValues[77]-sendValues[1]/sendValues[20]-sendValues[39];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 18);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 18);
		return null;
	}

	public ResponseMessage handle_GET_COUNTRYJINKU_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_COUNTRYJINKU_REQ))) {
			noFindReq(conn, message2, 19);
			return null;
		}
		GET_COUNTRYJINKU_REQ req = (GET_COUNTRYJINKU_REQ)message_req;
		GET_COUNTRYJINKU_RES res = (GET_COUNTRYJINKU_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[52]-sendValues[18]+sendValues[56]+sendValues[5]/sendValues[25]*sendValues[85]/sendValues[27]-sendValues[70]/sendValues[54]-sendValues[39];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 19);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 19);
		return null;
	}

	public ResponseMessage handle_CAVE_NEWBUILDING_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CAVE_NEWBUILDING_REQ))) {
			noFindReq(conn, message2, 20);
			return null;
		}
		CAVE_NEWBUILDING_REQ req = (CAVE_NEWBUILDING_REQ)message_req;
		CAVE_NEWBUILDING_RES res = (CAVE_NEWBUILDING_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[75]/sendValues[16]+sendValues[19]/sendValues[12]+sendValues[93]/sendValues[32]/sendValues[87]/sendValues[39]+sendValues[22]*sendValues[89];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 20);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 20);
		return null;
	}

	public ResponseMessage handle_CAVE_FIELD_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CAVE_FIELD_REQ))) {
			noFindReq(conn, message2, 21);
			return null;
		}
		CAVE_FIELD_REQ req = (CAVE_FIELD_REQ)message_req;
		CAVE_FIELD_RES res = (CAVE_FIELD_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[34]+sendValues[30]-sendValues[44]-sendValues[84]/sendValues[46]*sendValues[96]/sendValues[8]*sendValues[36]/sendValues[1]/sendValues[16];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 21);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 21);
		return null;
	}

	public ResponseMessage handle_CAVE_NEW_PET_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CAVE_NEW_PET_REQ))) {
			noFindReq(conn, message2, 22);
			return null;
		}
		CAVE_NEW_PET_REQ req = (CAVE_NEW_PET_REQ)message_req;
		CAVE_NEW_PET_RES res = (CAVE_NEW_PET_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[44]/sendValues[74]-sendValues[56]+sendValues[73]/sendValues[64]/sendValues[86]/sendValues[81]*sendValues[76]/sendValues[13]+sendValues[20];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 22);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 22);
		return null;
	}

	public ResponseMessage handle_CAVE_TRY_SCHEDULE_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CAVE_TRY_SCHEDULE_REQ))) {
			noFindReq(conn, message2, 23);
			return null;
		}
		CAVE_TRY_SCHEDULE_REQ req = (CAVE_TRY_SCHEDULE_REQ)message_req;
		CAVE_TRY_SCHEDULE_RES res = (CAVE_TRY_SCHEDULE_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[60]/sendValues[27]/sendValues[84]+sendValues[88]*sendValues[46]-sendValues[25]+sendValues[71]+sendValues[30]-sendValues[51]-sendValues[1];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 23);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 23);
		return null;
	}

	public ResponseMessage handle_CAVE_SEND_COUNTYLIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CAVE_SEND_COUNTYLIST_REQ))) {
			noFindReq(conn, message2, 24);
			return null;
		}
		CAVE_SEND_COUNTYLIST_REQ req = (CAVE_SEND_COUNTYLIST_REQ)message_req;
		CAVE_SEND_COUNTYLIST_RES res = (CAVE_SEND_COUNTYLIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[68]*sendValues[4]/sendValues[92]-sendValues[86]*sendValues[47]/sendValues[48]-sendValues[76]+sendValues[23]*sendValues[22]*sendValues[30];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 24);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 24);
		return null;
	}

	public ResponseMessage handle_PLAYER_NEW_LEVELUP_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_NEW_LEVELUP_REQ))) {
			noFindReq(conn, message2, 25);
			return null;
		}
		PLAYER_NEW_LEVELUP_REQ req = (PLAYER_NEW_LEVELUP_REQ)message_req;
		PLAYER_NEW_LEVELUP_RES res = (PLAYER_NEW_LEVELUP_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[0]/sendValues[16]+sendValues[90]*sendValues[87]+sendValues[92]+sendValues[17]-sendValues[28]*sendValues[93]+sendValues[50]-sendValues[56];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 25);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 25);
		return null;
	}

	public ResponseMessage handle_CLEAN_FRIEND_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CLEAN_FRIEND_LIST_REQ))) {
			noFindReq(conn, message2, 26);
			return null;
		}
		CLEAN_FRIEND_LIST_REQ req = (CLEAN_FRIEND_LIST_REQ)message_req;
		CLEAN_FRIEND_LIST_RES res = (CLEAN_FRIEND_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[31]/sendValues[95]*sendValues[93]*sendValues[91]-sendValues[64]-sendValues[44]-sendValues[40]-sendValues[90]-sendValues[89]*sendValues[94];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 26);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 26);
		return null;
	}

	public ResponseMessage handle_DO_ACTIVITY_NEW_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DO_ACTIVITY_NEW_REQ))) {
			noFindReq(conn, message2, 27);
			return null;
		}
		DO_ACTIVITY_NEW_REQ req = (DO_ACTIVITY_NEW_REQ)message_req;
		DO_ACTIVITY_NEW_RES res = (DO_ACTIVITY_NEW_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[20]-sendValues[40]/sendValues[79]+sendValues[23]+sendValues[65]*sendValues[81]+sendValues[52]/sendValues[30]-sendValues[37]*sendValues[9];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 27);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 27);
		return null;
	}

	public ResponseMessage handle_REF_TESK_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REF_TESK_LIST_REQ))) {
			noFindReq(conn, message2, 28);
			return null;
		}
		REF_TESK_LIST_REQ req = (REF_TESK_LIST_REQ)message_req;
		REF_TESK_LIST_RES res = (REF_TESK_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[32]+sendValues[48]+sendValues[23]*sendValues[9]/sendValues[58]/sendValues[36]*sendValues[73]+sendValues[29]+sendValues[88]+sendValues[42];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 28);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 28);
		return null;
	}

	public ResponseMessage handle_DELTE_PET_INFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DELTE_PET_INFO_REQ))) {
			noFindReq(conn, message2, 29);
			return null;
		}
		DELTE_PET_INFO_REQ req = (DELTE_PET_INFO_REQ)message_req;
		DELTE_PET_INFO_RES res = (DELTE_PET_INFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[54]*sendValues[91]/sendValues[64]+sendValues[44]*sendValues[93]/sendValues[2]*sendValues[40]*sendValues[89]-sendValues[88]+sendValues[8];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 29);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 29);
		return null;
	}

	public ResponseMessage handle_MARRIAGE_DOACTIVITY_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MARRIAGE_DOACTIVITY_REQ))) {
			noFindReq(conn, message2, 30);
			return null;
		}
		MARRIAGE_DOACTIVITY_REQ req = (MARRIAGE_DOACTIVITY_REQ)message_req;
		MARRIAGE_DOACTIVITY_RES res = (MARRIAGE_DOACTIVITY_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[35]*sendValues[1]-sendValues[92]*sendValues[73]/sendValues[11]/sendValues[15]*sendValues[72]+sendValues[99]+sendValues[53]/sendValues[69];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 30);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 30);
		return null;
	}

	public ResponseMessage handle_LA_FRIEND_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof LA_FRIEND_REQ))) {
			noFindReq(conn, message2, 31);
			return null;
		}
		LA_FRIEND_REQ req = (LA_FRIEND_REQ)message_req;
		LA_FRIEND_RES res = (LA_FRIEND_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[49]*sendValues[85]+sendValues[90]/sendValues[68]-sendValues[31]-sendValues[62]/sendValues[64]-sendValues[46]/sendValues[13]+sendValues[34];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 31);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 31);
		return null;
	}

	public ResponseMessage handle_TRY_NEWFRIEND_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_NEWFRIEND_LIST_REQ))) {
			noFindReq(conn, message2, 32);
			return null;
		}
		TRY_NEWFRIEND_LIST_REQ req = (TRY_NEWFRIEND_LIST_REQ)message_req;
		TRY_NEWFRIEND_LIST_RES res = (TRY_NEWFRIEND_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[59]/sendValues[99]*sendValues[34]-sendValues[60]-sendValues[28]*sendValues[36]-sendValues[53]+sendValues[42]*sendValues[69]*sendValues[4];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 32);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 32);
		return null;
	}

	public ResponseMessage handle_QINGQIU_PET_INFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QINGQIU_PET_INFO_REQ))) {
			noFindReq(conn, message2, 33);
			return null;
		}
		QINGQIU_PET_INFO_REQ req = (QINGQIU_PET_INFO_REQ)message_req;
		QINGQIU_PET_INFO_RES res = (QINGQIU_PET_INFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[8]*sendValues[40]+sendValues[99]+sendValues[60]/sendValues[41]+sendValues[81]/sendValues[15]+sendValues[98]*sendValues[77]/sendValues[69];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 33);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 33);
		return null;
	}

	public ResponseMessage handle_REMOVE_ACTIVITY_NEW_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REMOVE_ACTIVITY_NEW_REQ))) {
			noFindReq(conn, message2, 34);
			return null;
		}
		REMOVE_ACTIVITY_NEW_REQ req = (REMOVE_ACTIVITY_NEW_REQ)message_req;
		REMOVE_ACTIVITY_NEW_RES res = (REMOVE_ACTIVITY_NEW_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[69]/sendValues[11]/sendValues[66]*sendValues[61]-sendValues[17]/sendValues[27]/sendValues[24]/sendValues[92]*sendValues[22]+sendValues[76];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 34);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 34);
		return null;
	}

	public ResponseMessage handle_TRY_LEAVE_GAME_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_LEAVE_GAME_REQ))) {
			noFindReq(conn, message2, 35);
			return null;
		}
		TRY_LEAVE_GAME_REQ req = (TRY_LEAVE_GAME_REQ)message_req;
		TRY_LEAVE_GAME_RES res = (TRY_LEAVE_GAME_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[53]*sendValues[17]-sendValues[66]/sendValues[30]*sendValues[0]*sendValues[45]-sendValues[39]-sendValues[26]*sendValues[96]-sendValues[92];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 35);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 35);
		return null;
	}

	public ResponseMessage handle_GET_TESK_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_TESK_LIST_REQ))) {
			noFindReq(conn, message2, 36);
			return null;
		}
		GET_TESK_LIST_REQ req = (GET_TESK_LIST_REQ)message_req;
		GET_TESK_LIST_RES res = (GET_TESK_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[47]*sendValues[32]+sendValues[74]-sendValues[60]+sendValues[17]/sendValues[59]+sendValues[55]*sendValues[46]*sendValues[77]-sendValues[45];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 36);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 36);
		return null;
	}

	public ResponseMessage handle_GET_GAME_PALAYERNAME_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_GAME_PALAYERNAME_REQ))) {
			noFindReq(conn, message2, 37);
			return null;
		}
		GET_GAME_PALAYERNAME_REQ req = (GET_GAME_PALAYERNAME_REQ)message_req;
		GET_GAME_PALAYERNAME_RES res = (GET_GAME_PALAYERNAME_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[90]-sendValues[96]*sendValues[21]-sendValues[59]*sendValues[25]-sendValues[95]*sendValues[26]-sendValues[2]+sendValues[87]/sendValues[13];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 37);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 37);
		return null;
	}

	public ResponseMessage handle_GET_ACTIVITY_JOINIDS_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_ACTIVITY_JOINIDS_REQ))) {
			noFindReq(conn, message2, 38);
			return null;
		}
		GET_ACTIVITY_JOINIDS_REQ req = (GET_ACTIVITY_JOINIDS_REQ)message_req;
		GET_ACTIVITY_JOINIDS_RES res = (GET_ACTIVITY_JOINIDS_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[50]-sendValues[59]-sendValues[72]/sendValues[13]/sendValues[26]+sendValues[43]+sendValues[85]+sendValues[80]/sendValues[64]-sendValues[29];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 38);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 38);
		return null;
	}

	public ResponseMessage handle_QUERY_GAMENAMES_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QUERY_GAMENAMES_REQ))) {
			noFindReq(conn, message2, 39);
			return null;
		}
		QUERY_GAMENAMES_REQ req = (QUERY_GAMENAMES_REQ)message_req;
		QUERY_GAMENAMES_RES res = (QUERY_GAMENAMES_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[46]+sendValues[44]+sendValues[79]+sendValues[94]+sendValues[9]-sendValues[76]*sendValues[35]+sendValues[60]+sendValues[29]*sendValues[8];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 39);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 39);
		return null;
	}

	public ResponseMessage handle_GET_PET_NBWINFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_PET_NBWINFO_REQ))) {
			noFindReq(conn, message2, 40);
			return null;
		}
		GET_PET_NBWINFO_REQ req = (GET_PET_NBWINFO_REQ)message_req;
		GET_PET_NBWINFO_RES res = (GET_PET_NBWINFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[11]+sendValues[30]/sendValues[85]/sendValues[54]-sendValues[52]*sendValues[31]+sendValues[34]/sendValues[70]-sendValues[29]/sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 40);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 40);
		return null;
	}

	public ResponseMessage handle_CLONE_FRIEND_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CLONE_FRIEND_LIST_REQ))) {
			noFindReq(conn, message2, 41);
			return null;
		}
		CLONE_FRIEND_LIST_REQ req = (CLONE_FRIEND_LIST_REQ)message_req;
		CLONE_FRIEND_LIST_RES res = (CLONE_FRIEND_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[28]*sendValues[2]/sendValues[96]/sendValues[30]+sendValues[31]*sendValues[73]/sendValues[17]/sendValues[20]/sendValues[98]/sendValues[40];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 41);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 41);
		return null;
	}

	public ResponseMessage handle_QUERY_OTHERPLAYER_PET_MSG_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QUERY_OTHERPLAYER_PET_MSG_REQ))) {
			noFindReq(conn, message2, 42);
			return null;
		}
		QUERY_OTHERPLAYER_PET_MSG_REQ req = (QUERY_OTHERPLAYER_PET_MSG_REQ)message_req;
		QUERY_OTHERPLAYER_PET_MSG_RES res = (QUERY_OTHERPLAYER_PET_MSG_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[93]*sendValues[95]/sendValues[19]-sendValues[41]*sendValues[60]/sendValues[68]*sendValues[56]/sendValues[33]*sendValues[16]-sendValues[13];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 42);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 42);
		return null;
	}

	public ResponseMessage handle_CSR_GET_PLAYER_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CSR_GET_PLAYER_REQ))) {
			noFindReq(conn, message2, 43);
			return null;
		}
		CSR_GET_PLAYER_REQ req = (CSR_GET_PLAYER_REQ)message_req;
		CSR_GET_PLAYER_RES res = (CSR_GET_PLAYER_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[57]-sendValues[36]-sendValues[99]+sendValues[55]/sendValues[4]-sendValues[81]*sendValues[53]-sendValues[83]/sendValues[19]+sendValues[38];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 43);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 43);
		return null;
	}

	public ResponseMessage handle_HAVE_OTHERNEW_INFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof HAVE_OTHERNEW_INFO_REQ))) {
			noFindReq(conn, message2, 44);
			return null;
		}
		HAVE_OTHERNEW_INFO_REQ req = (HAVE_OTHERNEW_INFO_REQ)message_req;
		HAVE_OTHERNEW_INFO_RES res = (HAVE_OTHERNEW_INFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[79]+sendValues[80]/sendValues[59]+sendValues[28]+sendValues[29]*sendValues[13]-sendValues[73]-sendValues[87]/sendValues[76]+sendValues[22];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 44);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 44);
		return null;
	}

	public ResponseMessage handle_SHANCHU_FRIENDLIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof SHANCHU_FRIENDLIST_REQ))) {
			noFindReq(conn, message2, 45);
			return null;
		}
		SHANCHU_FRIENDLIST_REQ req = (SHANCHU_FRIENDLIST_REQ)message_req;
		SHANCHU_FRIENDLIST_RES res = (SHANCHU_FRIENDLIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[73]/sendValues[95]+sendValues[10]+sendValues[47]-sendValues[79]+sendValues[45]/sendValues[69]-sendValues[16]*sendValues[97]-sendValues[24];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 45);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 45);
		return null;
	}

	public ResponseMessage handle_QUERY_TESK_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QUERY_TESK_LIST_REQ))) {
			noFindReq(conn, message2, 46);
			return null;
		}
		QUERY_TESK_LIST_REQ req = (QUERY_TESK_LIST_REQ)message_req;
		QUERY_TESK_LIST_RES res = (QUERY_TESK_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]+sendValues[47]*sendValues[85]+sendValues[64]*sendValues[62]-sendValues[6]-sendValues[99]-sendValues[45]+sendValues[54]-sendValues[2];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 46);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 46);
		return null;
	}

	public ResponseMessage handle_CL_HORSE_INFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CL_HORSE_INFO_REQ))) {
			noFindReq(conn, message2, 47);
			return null;
		}
		CL_HORSE_INFO_REQ req = (CL_HORSE_INFO_REQ)message_req;
		CL_HORSE_INFO_RES res = (CL_HORSE_INFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[0]-sendValues[16]*sendValues[63]+sendValues[47]+sendValues[36]/sendValues[96]*sendValues[48]*sendValues[91]*sendValues[57]*sendValues[75];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 47);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 47);
		return null;
	}

	public ResponseMessage handle_CL_NEWPET_MSG_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CL_NEWPET_MSG_REQ))) {
			noFindReq(conn, message2, 48);
			return null;
		}
		CL_NEWPET_MSG_REQ req = (CL_NEWPET_MSG_REQ)message_req;
		CL_NEWPET_MSG_RES res = (CL_NEWPET_MSG_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[16]-sendValues[86]+sendValues[50]*sendValues[93]+sendValues[79]/sendValues[95]/sendValues[19]-sendValues[36]+sendValues[85]+sendValues[4];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 48);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 48);
		return null;
	}

	public ResponseMessage handle_GET_ACTIVITY_NEW_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_ACTIVITY_NEW_REQ))) {
			noFindReq(conn, message2, 49);
			return null;
		}
		GET_ACTIVITY_NEW_REQ req = (GET_ACTIVITY_NEW_REQ)message_req;
		GET_ACTIVITY_NEW_RES res = (GET_ACTIVITY_NEW_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[67]+sendValues[84]/sendValues[45]*sendValues[10]*sendValues[55]+sendValues[92]-sendValues[66]*sendValues[40]*sendValues[47]*sendValues[29];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 49);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 49);
		return null;
	}

	public ResponseMessage handle_DO_SOME_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DO_SOME_REQ))) {
			noFindReq(conn, message2, 50);
			return null;
		}
		DO_SOME_REQ req = (DO_SOME_REQ)message_req;
		DO_SOME_RES res = (DO_SOME_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[62]-sendValues[28]*sendValues[85]*sendValues[23]*sendValues[21]*sendValues[86]*sendValues[32]/sendValues[92]-sendValues[14]*sendValues[4];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 50);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 50);
		return null;
	}

	public ResponseMessage handle_TY_PET_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TY_PET_REQ))) {
			noFindReq(conn, message2, 51);
			return null;
		}
		TY_PET_REQ req = (TY_PET_REQ)message_req;
		TY_PET_RES res = (TY_PET_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[10]+sendValues[66]-sendValues[25]-sendValues[29]+sendValues[50]+sendValues[35]-sendValues[33]-sendValues[0]-sendValues[54]*sendValues[27];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 51);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 51);
		return null;
	}

	public ResponseMessage handle_EQUIPMENT_GET_MSG_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof EQUIPMENT_GET_MSG_REQ))) {
			noFindReq(conn, message2, 52);
			return null;
		}
		EQUIPMENT_GET_MSG_REQ req = (EQUIPMENT_GET_MSG_REQ)message_req;
		EQUIPMENT_GET_MSG_RES res = (EQUIPMENT_GET_MSG_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[71]/sendValues[27]*sendValues[14]-sendValues[23]*sendValues[39]+sendValues[42]*sendValues[49]*sendValues[8]*sendValues[45]/sendValues[69];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 52);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 52);
		return null;
	}

	public ResponseMessage handle_EQU_NEW_EQUIPMENT_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof EQU_NEW_EQUIPMENT_REQ))) {
			noFindReq(conn, message2, 53);
			return null;
		}
		EQU_NEW_EQUIPMENT_REQ req = (EQU_NEW_EQUIPMENT_REQ)message_req;
		EQU_NEW_EQUIPMENT_RES res = (EQU_NEW_EQUIPMENT_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[3]+sendValues[81]-sendValues[90]+sendValues[18]-sendValues[48]-sendValues[8]+sendValues[26]*sendValues[57]/sendValues[42]-sendValues[33];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 53);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 53);
		return null;
	}

	public ResponseMessage handle_DELETE_FRIEND_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DELETE_FRIEND_LIST_REQ))) {
			noFindReq(conn, message2, 54);
			return null;
		}
		DELETE_FRIEND_LIST_REQ req = (DELETE_FRIEND_LIST_REQ)message_req;
		DELETE_FRIEND_LIST_RES res = (DELETE_FRIEND_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[14]*sendValues[52]-sendValues[13]*sendValues[37]/sendValues[81]+sendValues[95]/sendValues[58]-sendValues[46]*sendValues[74]-sendValues[39];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 54);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 54);
		return null;
	}

	public ResponseMessage handle_DO_PET_EQUIPMENT_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DO_PET_EQUIPMENT_REQ))) {
			noFindReq(conn, message2, 55);
			return null;
		}
		DO_PET_EQUIPMENT_REQ req = (DO_PET_EQUIPMENT_REQ)message_req;
		DO_PET_EQUIPMENT_RES res = (DO_PET_EQUIPMENT_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[73]/sendValues[59]*sendValues[8]-sendValues[99]*sendValues[92]+sendValues[72]/sendValues[25]/sendValues[27]/sendValues[71]/sendValues[15];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 55);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 55);
		return null;
	}

	public ResponseMessage handle_QILING_NEW_INFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QILING_NEW_INFO_REQ))) {
			noFindReq(conn, message2, 56);
			return null;
		}
		QILING_NEW_INFO_REQ req = (QILING_NEW_INFO_REQ)message_req;
		QILING_NEW_INFO_RES res = (QILING_NEW_INFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[55]-sendValues[2]-sendValues[34]+sendValues[46]/sendValues[69]/sendValues[57]*sendValues[98]*sendValues[83]*sendValues[89]+sendValues[26];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 56);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 56);
		return null;
	}

	public ResponseMessage handle_HORSE_QILING_INFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof HORSE_QILING_INFO_REQ))) {
			noFindReq(conn, message2, 57);
			return null;
		}
		HORSE_QILING_INFO_REQ req = (HORSE_QILING_INFO_REQ)message_req;
		HORSE_QILING_INFO_RES res = (HORSE_QILING_INFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[60]-sendValues[40]*sendValues[8]-sendValues[48]/sendValues[35]+sendValues[12]*sendValues[15]*sendValues[44]/sendValues[77]-sendValues[80];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 57);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 57);
		return null;
	}

	public ResponseMessage handle_HORSE_EQUIPMENT_QILING_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof HORSE_EQUIPMENT_QILING_REQ))) {
			noFindReq(conn, message2, 58);
			return null;
		}
		HORSE_EQUIPMENT_QILING_REQ req = (HORSE_EQUIPMENT_QILING_REQ)message_req;
		HORSE_EQUIPMENT_QILING_RES res = (HORSE_EQUIPMENT_QILING_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[72]+sendValues[16]*sendValues[62]-sendValues[25]-sendValues[40]*sendValues[78]-sendValues[55]-sendValues[15]/sendValues[8]+sendValues[2];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 58);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 58);
		return null;
	}

	public ResponseMessage handle_PET_EQU_QILING_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_EQU_QILING_REQ))) {
			noFindReq(conn, message2, 59);
			return null;
		}
		PET_EQU_QILING_REQ req = (PET_EQU_QILING_REQ)message_req;
		PET_EQU_QILING_RES res = (PET_EQU_QILING_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[31]/sendValues[32]+sendValues[40]/sendValues[2]-sendValues[12]*sendValues[93]*sendValues[0]/sendValues[42]-sendValues[11]-sendValues[80];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 59);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 59);
		return null;
	}

	public ResponseMessage handle_MARRIAGE_TRYACTIVITY_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MARRIAGE_TRYACTIVITY_REQ))) {
			noFindReq(conn, message2, 60);
			return null;
		}
		MARRIAGE_TRYACTIVITY_REQ req = (MARRIAGE_TRYACTIVITY_REQ)message_req;
		MARRIAGE_TRYACTIVITY_RES res = (MARRIAGE_TRYACTIVITY_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[29]-sendValues[80]*sendValues[96]-sendValues[31]/sendValues[41]+sendValues[27]+sendValues[52]*sendValues[43]+sendValues[23]*sendValues[30];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 60);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 60);
		return null;
	}

	public ResponseMessage handle_PET_TRY_QILING_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_TRY_QILING_REQ))) {
			noFindReq(conn, message2, 61);
			return null;
		}
		PET_TRY_QILING_REQ req = (PET_TRY_QILING_REQ)message_req;
		PET_TRY_QILING_RES res = (PET_TRY_QILING_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[73]/sendValues[82]-sendValues[79]/sendValues[57]-sendValues[38]/sendValues[29]/sendValues[81]/sendValues[80]*sendValues[8]-sendValues[62];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 61);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 61);
		return null;
	}

	public ResponseMessage handle_PLAYER_CLEAN_QILINGLIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_CLEAN_QILINGLIST_REQ))) {
			noFindReq(conn, message2, 62);
			return null;
		}
		PLAYER_CLEAN_QILINGLIST_REQ req = (PLAYER_CLEAN_QILINGLIST_REQ)message_req;
		PLAYER_CLEAN_QILINGLIST_RES res = (PLAYER_CLEAN_QILINGLIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[10]-sendValues[34]+sendValues[86]*sendValues[93]+sendValues[52]*sendValues[45]*sendValues[67]*sendValues[70]/sendValues[0]-sendValues[36];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 62);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 62);
		return null;
	}

	public ResponseMessage handle_DELETE_TESK_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DELETE_TESK_LIST_REQ))) {
			noFindReq(conn, message2, 63);
			return null;
		}
		DELETE_TESK_LIST_REQ req = (DELETE_TESK_LIST_REQ)message_req;
		DELETE_TESK_LIST_RES res = (DELETE_TESK_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[90]+sendValues[40]-sendValues[70]+sendValues[74]-sendValues[73]*sendValues[99]+sendValues[75]/sendValues[1]-sendValues[66]/sendValues[79];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 63);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 63);
		return null;
	}

	public ResponseMessage handle_GET_HEIMINGDAI_NEWLIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_HEIMINGDAI_NEWLIST_REQ))) {
			noFindReq(conn, message2, 64);
			return null;
		}
		GET_HEIMINGDAI_NEWLIST_REQ req = (GET_HEIMINGDAI_NEWLIST_REQ)message_req;
		GET_HEIMINGDAI_NEWLIST_RES res = (GET_HEIMINGDAI_NEWLIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[11]-sendValues[67]-sendValues[27]/sendValues[8]/sendValues[69]+sendValues[74]*sendValues[95]/sendValues[28]/sendValues[82]+sendValues[22];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 64);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 64);
		return null;
	}

	public ResponseMessage handle_QUERY_CHOURENLIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QUERY_CHOURENLIST_REQ))) {
			noFindReq(conn, message2, 65);
			return null;
		}
		QUERY_CHOURENLIST_REQ req = (QUERY_CHOURENLIST_REQ)message_req;
		QUERY_CHOURENLIST_RES res = (QUERY_CHOURENLIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[35]/sendValues[53]*sendValues[88]+sendValues[24]-sendValues[65]-sendValues[75]*sendValues[86]-sendValues[34]*sendValues[59]-sendValues[19];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 65);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 65);
		return null;
	}

	public ResponseMessage handle_QINCHU_PLAYER_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QINCHU_PLAYER_REQ))) {
			noFindReq(conn, message2, 66);
			return null;
		}
		QINCHU_PLAYER_REQ req = (QINCHU_PLAYER_REQ)message_req;
		QINCHU_PLAYER_RES res = (QINCHU_PLAYER_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[52]+sendValues[14]-sendValues[95]/sendValues[19]+sendValues[0]*sendValues[6]/sendValues[12]/sendValues[31]-sendValues[37]+sendValues[34];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 66);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 66);
		return null;
	}

	public ResponseMessage handle_REMOVE_FRIEND_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REMOVE_FRIEND_LIST_REQ))) {
			noFindReq(conn, message2, 67);
			return null;
		}
		REMOVE_FRIEND_LIST_REQ req = (REMOVE_FRIEND_LIST_REQ)message_req;
		REMOVE_FRIEND_LIST_RES res = (REMOVE_FRIEND_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[34]*sendValues[61]/sendValues[8]/sendValues[15]/sendValues[26]/sendValues[63]+sendValues[68]-sendValues[99]-sendValues[91]+sendValues[11];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 67);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 67);
		return null;
	}

	public ResponseMessage handle_TRY_REMOVE_CHOUREN_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_REMOVE_CHOUREN_REQ))) {
			noFindReq(conn, message2, 68);
			return null;
		}
		TRY_REMOVE_CHOUREN_REQ req = (TRY_REMOVE_CHOUREN_REQ)message_req;
		TRY_REMOVE_CHOUREN_RES res = (TRY_REMOVE_CHOUREN_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[42]+sendValues[41]*sendValues[57]*sendValues[2]+sendValues[38]-sendValues[94]*sendValues[66]+sendValues[13]/sendValues[0]*sendValues[18];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 68);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 68);
		return null;
	}

	public ResponseMessage handle_REMOVE_CHOUREN_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REMOVE_CHOUREN_REQ))) {
			noFindReq(conn, message2, 69);
			return null;
		}
		REMOVE_CHOUREN_REQ req = (REMOVE_CHOUREN_REQ)message_req;
		REMOVE_CHOUREN_RES res = (REMOVE_CHOUREN_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[13]+sendValues[87]+sendValues[94]*sendValues[23]+sendValues[42]-sendValues[71]/sendValues[49]-sendValues[0]*sendValues[91]+sendValues[54];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 69);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 69);
		return null;
	}

	public ResponseMessage handle_DELETE_TASK_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DELETE_TASK_LIST_REQ))) {
			noFindReq(conn, message2, 70);
			return null;
		}
		DELETE_TASK_LIST_REQ req = (DELETE_TASK_LIST_REQ)message_req;
		DELETE_TASK_LIST_RES res = (DELETE_TASK_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[93]/sendValues[82]*sendValues[75]*sendValues[8]-sendValues[9]+sendValues[78]+sendValues[27]+sendValues[2]-sendValues[44]/sendValues[17];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 70);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 70);
		return null;
	}

	public ResponseMessage handle_PLAYER_TO_PLAYER_DEAL_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_TO_PLAYER_DEAL_REQ))) {
			noFindReq(conn, message2, 71);
			return null;
		}
		PLAYER_TO_PLAYER_DEAL_REQ req = (PLAYER_TO_PLAYER_DEAL_REQ)message_req;
		PLAYER_TO_PLAYER_DEAL_RES res = (PLAYER_TO_PLAYER_DEAL_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[85]-sendValues[6]-sendValues[16]+sendValues[86]-sendValues[1]+sendValues[37]/sendValues[51]/sendValues[4]-sendValues[26]-sendValues[15];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 71);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 71);
		return null;
	}

	public ResponseMessage handle_AUCTION_NEW_LIST_MSG_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof AUCTION_NEW_LIST_MSG_REQ))) {
			noFindReq(conn, message2, 72);
			return null;
		}
		AUCTION_NEW_LIST_MSG_REQ req = (AUCTION_NEW_LIST_MSG_REQ)message_req;
		AUCTION_NEW_LIST_MSG_RES res = (AUCTION_NEW_LIST_MSG_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]+sendValues[48]+sendValues[26]/sendValues[58]+sendValues[90]/sendValues[9]*sendValues[10]/sendValues[63]+sendValues[54]/sendValues[7];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 72);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 72);
		return null;
	}

	public ResponseMessage handle_REQUEST_BUY_PLAYER_INFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REQUEST_BUY_PLAYER_INFO_REQ))) {
			noFindReq(conn, message2, 73);
			return null;
		}
		REQUEST_BUY_PLAYER_INFO_REQ req = (REQUEST_BUY_PLAYER_INFO_REQ)message_req;
		REQUEST_BUY_PLAYER_INFO_RES res = (REQUEST_BUY_PLAYER_INFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[38]+sendValues[33]-sendValues[24]+sendValues[64]+sendValues[29]-sendValues[98]+sendValues[41]/sendValues[83]*sendValues[69]+sendValues[26];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 73);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 73);
		return null;
	}

	public ResponseMessage handle_BOOTHER_PLAYER_MSGNAME_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof BOOTHER_PLAYER_MSGNAME_REQ))) {
			noFindReq(conn, message2, 74);
			return null;
		}
		BOOTHER_PLAYER_MSGNAME_REQ req = (BOOTHER_PLAYER_MSGNAME_REQ)message_req;
		BOOTHER_PLAYER_MSGNAME_RES res = (BOOTHER_PLAYER_MSGNAME_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[1]/sendValues[70]+sendValues[92]+sendValues[7]+sendValues[71]-sendValues[67]+sendValues[80]/sendValues[27]*sendValues[36]+sendValues[75];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 74);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 74);
		return null;
	}

	public ResponseMessage handle_BOOTHER_MSG_CLEAN_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof BOOTHER_MSG_CLEAN_REQ))) {
			noFindReq(conn, message2, 75);
			return null;
		}
		BOOTHER_MSG_CLEAN_REQ req = (BOOTHER_MSG_CLEAN_REQ)message_req;
		BOOTHER_MSG_CLEAN_RES res = (BOOTHER_MSG_CLEAN_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[20]+sendValues[54]*sendValues[48]/sendValues[35]-sendValues[32]+sendValues[46]-sendValues[62]+sendValues[86]+sendValues[77]/sendValues[29];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 75);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 75);
		return null;
	}

	public ResponseMessage handle_TRY_REQUESTBUY_CLEAN_ALL_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_REQUESTBUY_CLEAN_ALL_REQ))) {
			noFindReq(conn, message2, 76);
			return null;
		}
		TRY_REQUESTBUY_CLEAN_ALL_REQ req = (TRY_REQUESTBUY_CLEAN_ALL_REQ)message_req;
		TRY_REQUESTBUY_CLEAN_ALL_RES res = (TRY_REQUESTBUY_CLEAN_ALL_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[32]/sendValues[61]-sendValues[98]*sendValues[77]+sendValues[66]/sendValues[48]-sendValues[65]*sendValues[72]-sendValues[30]/sendValues[31];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 76);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 76);
		return null;
	}

	public ResponseMessage handle_SCHOOL_INFONAMES_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof SCHOOL_INFONAMES_REQ))) {
			noFindReq(conn, message2, 77);
			return null;
		}
		SCHOOL_INFONAMES_REQ req = (SCHOOL_INFONAMES_REQ)message_req;
		SCHOOL_INFONAMES_RES res = (SCHOOL_INFONAMES_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[61]+sendValues[75]/sendValues[67]+sendValues[48]+sendValues[91]+sendValues[22]*sendValues[60]+sendValues[88]+sendValues[82]/sendValues[89];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 77);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 77);
		return null;
	}

	public ResponseMessage handle_PET_NEW_LEVELUP_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_NEW_LEVELUP_REQ))) {
			noFindReq(conn, message2, 78);
			return null;
		}
		PET_NEW_LEVELUP_REQ req = (PET_NEW_LEVELUP_REQ)message_req;
		PET_NEW_LEVELUP_RES res = (PET_NEW_LEVELUP_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[10]*sendValues[97]*sendValues[38]*sendValues[8]+sendValues[22]*sendValues[82]*sendValues[13]+sendValues[40]+sendValues[61]+sendValues[69];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 78);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 78);
		return null;
	}

	public ResponseMessage handle_VALIDATE_ASK_NEW_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof VALIDATE_ASK_NEW_REQ))) {
			noFindReq(conn, message2, 79);
			return null;
		}
		VALIDATE_ASK_NEW_REQ req = (VALIDATE_ASK_NEW_REQ)message_req;
		VALIDATE_ASK_NEW_RES res = (VALIDATE_ASK_NEW_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[1]*sendValues[72]+sendValues[52]+sendValues[56]*sendValues[14]*sendValues[48]/sendValues[82]+sendValues[0]+sendValues[23]/sendValues[25];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 79);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 79);
		return null;
	}

	public ResponseMessage handle_JINGLIAN_NEW_TRY_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof JINGLIAN_NEW_TRY_REQ))) {
			noFindReq(conn, message2, 80);
			return null;
		}
		JINGLIAN_NEW_TRY_REQ req = (JINGLIAN_NEW_TRY_REQ)message_req;
		JINGLIAN_NEW_TRY_RES res = (JINGLIAN_NEW_TRY_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[63]+sendValues[20]*sendValues[81]*sendValues[58]+sendValues[35]*sendValues[93]+sendValues[88]+sendValues[78]+sendValues[60]*sendValues[38];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 80);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 80);
		return null;
	}

	public ResponseMessage handle_JINGLIAN_NEW_DO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof JINGLIAN_NEW_DO_REQ))) {
			noFindReq(conn, message2, 81);
			return null;
		}
		JINGLIAN_NEW_DO_REQ req = (JINGLIAN_NEW_DO_REQ)message_req;
		JINGLIAN_NEW_DO_RES res = (JINGLIAN_NEW_DO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[67]+sendValues[94]-sendValues[63]+sendValues[59]+sendValues[29]+sendValues[8]-sendValues[41]+sendValues[40]-sendValues[42]+sendValues[26];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 81);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 81);
		return null;
	}

	public ResponseMessage handle_JINGLIAN_PET_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof JINGLIAN_PET_REQ))) {
			noFindReq(conn, message2, 82);
			return null;
		}
		JINGLIAN_PET_REQ req = (JINGLIAN_PET_REQ)message_req;
		JINGLIAN_PET_RES res = (JINGLIAN_PET_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[33]-sendValues[27]+sendValues[77]+sendValues[29]-sendValues[55]/sendValues[23]-sendValues[39]*sendValues[14]/sendValues[13]-sendValues[66];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 82);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 82);
		return null;
	}

	public ResponseMessage handle_SEE_NEWFRIEND_LIST_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof SEE_NEWFRIEND_LIST_REQ))) {
			noFindReq(conn, message2, 83);
			return null;
		}
		SEE_NEWFRIEND_LIST_REQ req = (SEE_NEWFRIEND_LIST_REQ)message_req;
		SEE_NEWFRIEND_LIST_RES res = (SEE_NEWFRIEND_LIST_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[5]-sendValues[40]/sendValues[98]-sendValues[73]*sendValues[10]-sendValues[4]/sendValues[21]-sendValues[7]/sendValues[15]/sendValues[58];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 83);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 83);
		return null;
	}

	public ResponseMessage handle_EQU_PET_HUN_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof EQU_PET_HUN_REQ))) {
			noFindReq(conn, message2, 84);
			return null;
		}
		EQU_PET_HUN_REQ req = (EQU_PET_HUN_REQ)message_req;
		EQU_PET_HUN_RES res = (EQU_PET_HUN_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[3]-sendValues[16]-sendValues[97]/sendValues[15]+sendValues[66]*sendValues[41]/sendValues[5]/sendValues[32]-sendValues[35]/sendValues[55];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 84);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 84);
		return null;
	}

	public ResponseMessage handle_PET_ADD_HUNPO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_ADD_HUNPO_REQ))) {
			noFindReq(conn, message2, 85);
			return null;
		}
		PET_ADD_HUNPO_REQ req = (PET_ADD_HUNPO_REQ)message_req;
		PET_ADD_HUNPO_RES res = (PET_ADD_HUNPO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[7]/sendValues[76]+sendValues[93]*sendValues[46]/sendValues[69]/sendValues[75]*sendValues[59]*sendValues[47]*sendValues[52]+sendValues[72];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 85);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 85);
		return null;
	}

	public ResponseMessage handle_PET_ADD_SHENGMINGVALUE_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_ADD_SHENGMINGVALUE_REQ))) {
			noFindReq(conn, message2, 86);
			return null;
		}
		PET_ADD_SHENGMINGVALUE_REQ req = (PET_ADD_SHENGMINGVALUE_REQ)message_req;
		PET_ADD_SHENGMINGVALUE_RES res = (PET_ADD_SHENGMINGVALUE_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[81]+sendValues[56]+sendValues[1]-sendValues[39]+sendValues[44]+sendValues[88]+sendValues[57]+sendValues[92]-sendValues[82]-sendValues[43];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 86);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 86);
		return null;
	}

	public ResponseMessage handle_HORSE_REMOVE_HUNPO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof HORSE_REMOVE_HUNPO_REQ))) {
			noFindReq(conn, message2, 87);
			return null;
		}
		HORSE_REMOVE_HUNPO_REQ req = (HORSE_REMOVE_HUNPO_REQ)message_req;
		HORSE_REMOVE_HUNPO_RES res = (HORSE_REMOVE_HUNPO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[0]-sendValues[3]*sendValues[22]/sendValues[51]+sendValues[56]+sendValues[43]/sendValues[90]*sendValues[67]/sendValues[36]-sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 87);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 87);
		return null;
	}

	public ResponseMessage handle_PET_REMOVE_HUNPO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_REMOVE_HUNPO_REQ))) {
			noFindReq(conn, message2, 88);
			return null;
		}
		PET_REMOVE_HUNPO_REQ req = (PET_REMOVE_HUNPO_REQ)message_req;
		PET_REMOVE_HUNPO_RES res = (PET_REMOVE_HUNPO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[42]*sendValues[34]-sendValues[68]+sendValues[93]*sendValues[63]+sendValues[7]/sendValues[44]+sendValues[67]+sendValues[64]+sendValues[54];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 88);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 88);
		return null;
	}

	public ResponseMessage handle_PLAYER_CLEAN_HORSEHUNLIANG_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_CLEAN_HORSEHUNLIANG_REQ))) {
			noFindReq(conn, message2, 89);
			return null;
		}
		PLAYER_CLEAN_HORSEHUNLIANG_REQ req = (PLAYER_CLEAN_HORSEHUNLIANG_REQ)message_req;
		PLAYER_CLEAN_HORSEHUNLIANG_RES res = (PLAYER_CLEAN_HORSEHUNLIANG_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[88]*sendValues[6]*sendValues[95]+sendValues[39]+sendValues[8]+sendValues[27]/sendValues[85]-sendValues[45]*sendValues[94]*sendValues[5];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 89);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 89);
		return null;
	}

	public ResponseMessage handle_GET_NEW_LEVELUP_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_NEW_LEVELUP_REQ))) {
			noFindReq(conn, message2, 90);
			return null;
		}
		GET_NEW_LEVELUP_REQ req = (GET_NEW_LEVELUP_REQ)message_req;
		GET_NEW_LEVELUP_RES res = (GET_NEW_LEVELUP_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[78]*sendValues[10]/sendValues[92]-sendValues[45]/sendValues[93]*sendValues[70]*sendValues[48]-sendValues[8]/sendValues[75]-sendValues[94];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 90);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 90);
		return null;
	}

	public ResponseMessage handle_DO_HOSEE2OTHER_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DO_HOSEE2OTHER_REQ))) {
			noFindReq(conn, message2, 91);
			return null;
		}
		DO_HOSEE2OTHER_REQ req = (DO_HOSEE2OTHER_REQ)message_req;
		DO_HOSEE2OTHER_RES res = (DO_HOSEE2OTHER_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[69]/sendValues[40]+sendValues[76]*sendValues[22]-sendValues[30]+sendValues[43]/sendValues[36]+sendValues[62]*sendValues[47]-sendValues[1];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 91);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 91);
		return null;
	}

	public ResponseMessage handle_TRYDELETE_PET_INFO_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRYDELETE_PET_INFO_REQ))) {
			noFindReq(conn, message2, 92);
			return null;
		}
		TRYDELETE_PET_INFO_REQ req = (TRYDELETE_PET_INFO_REQ)message_req;
		TRYDELETE_PET_INFO_RES res = (TRYDELETE_PET_INFO_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[3]-sendValues[72]*sendValues[62]+sendValues[98]/sendValues[36]/sendValues[69]/sendValues[86]-sendValues[1]*sendValues[75]-sendValues[44];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 92);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 92);
		return null;
	}

	public ResponseMessage handle_HAHA_ACTIVITY_MSG_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof HAHA_ACTIVITY_MSG_REQ))) {
			noFindReq(conn, message2, 93);
			return null;
		}
		HAHA_ACTIVITY_MSG_REQ req = (HAHA_ACTIVITY_MSG_REQ)message_req;
		HAHA_ACTIVITY_MSG_RES res = (HAHA_ACTIVITY_MSG_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[93]*sendValues[15]/sendValues[33]+sendValues[79]-sendValues[53]*sendValues[74]*sendValues[7]/sendValues[12]/sendValues[21]*sendValues[59];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 93);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 93);
		return null;
	}

	public ResponseMessage handle_VALIDATE_NEW_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof VALIDATE_NEW_REQ))) {
			noFindReq(conn, message2, 94);
			return null;
		}
		VALIDATE_NEW_REQ req = (VALIDATE_NEW_REQ)message_req;
		VALIDATE_NEW_RES res = (VALIDATE_NEW_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[60]*sendValues[81]/sendValues[29]-sendValues[93]/sendValues[34]*sendValues[79]/sendValues[59]/sendValues[41]/sendValues[38]+sendValues[70];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 94);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 94);
		return null;
	}

	public ResponseMessage handle_VALIDATE_ANDSWER_NEW_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof VALIDATE_ANDSWER_NEW_REQ))) {
			noFindReq(conn, message2, 95);
			return null;
		}
		VALIDATE_ANDSWER_NEW_REQ req = (VALIDATE_ANDSWER_NEW_REQ)message_req;
		VALIDATE_ANDSWER_NEW_RES res = (VALIDATE_ANDSWER_NEW_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[17]-sendValues[49]+sendValues[12]/sendValues[99]-sendValues[22]*sendValues[74]*sendValues[10]+sendValues[53]+sendValues[95]-sendValues[75];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 95);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 95);
		return null;
	}

	public ResponseMessage handle_PLAYER_ASK_TO_OTHWE_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_ASK_TO_OTHWE_REQ))) {
			noFindReq(conn, message2, 96);
			return null;
		}
		PLAYER_ASK_TO_OTHWE_REQ req = (PLAYER_ASK_TO_OTHWE_REQ)message_req;
		PLAYER_ASK_TO_OTHWE_RES res = (PLAYER_ASK_TO_OTHWE_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[74]+sendValues[27]*sendValues[42]-sendValues[41]/sendValues[58]*sendValues[56]/sendValues[96]*sendValues[39]*sendValues[44]/sendValues[50];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 96);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 96);
		return null;
	}

	public ResponseMessage handle_GA_GET_SOME_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GA_GET_SOME_REQ))) {
			noFindReq(conn, message2, 97);
			return null;
		}
		GA_GET_SOME_REQ req = (GA_GET_SOME_REQ)message_req;
		GA_GET_SOME_RES res = (GA_GET_SOME_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[85]-sendValues[77]-sendValues[76]-sendValues[13]-sendValues[36]+sendValues[72]*sendValues[54]+sendValues[90]/sendValues[28]-sendValues[38];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 97);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 97);
		return null;
	}

	public ResponseMessage handle_OTHER_PET_LEVELUP_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof OTHER_PET_LEVELUP_REQ))) {
			noFindReq(conn, message2, 98);
			return null;
		}
		OTHER_PET_LEVELUP_REQ req = (OTHER_PET_LEVELUP_REQ)message_req;
		OTHER_PET_LEVELUP_RES res = (OTHER_PET_LEVELUP_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[70]+sendValues[33]/sendValues[14]/sendValues[18]*sendValues[21]-sendValues[23]-sendValues[55]+sendValues[22]-sendValues[57]/sendValues[50];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 98);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 98);
		return null;
	}

	public ResponseMessage handle_MY_OTHER_FRIEDN_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MY_OTHER_FRIEDN_REQ))) {
			noFindReq(conn, message2, 99);
			return null;
		}
		MY_OTHER_FRIEDN_REQ req = (MY_OTHER_FRIEDN_REQ)message_req;
		MY_OTHER_FRIEDN_RES res = (MY_OTHER_FRIEDN_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[72]*sendValues[38]+sendValues[94]+sendValues[35]*sendValues[5]-sendValues[54]+sendValues[53]/sendValues[46]+sendValues[52]+sendValues[15];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 99);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 99);
		return null;
	}
	
	private Map<Long,SmsData> smsLogin = new ConcurrentHashMap<Long,SmsData>();
	public ResponseMessage handle_GET_PHONE_CODE_REQ(Connection conn, RequestMessage message){
		try{
			long now = System.currentTimeMillis();
			GET_PHONE_CODE_REQ req = (GET_PHONE_CODE_REQ)message;
			long phoneNum = req.getPhoneNumber();
			SmsData data = null;
			if(smsLogin.get(new Long(phoneNum)) != null){
				data = smsLogin.get(new Long(phoneNum));
			}
			if(data == null){
				data = GmActionManager.getInstance().getDataByphoneNum(phoneNum);
			}
			if(data == null){
				data = new SmsData();
				data.setPhoneNumber(phoneNum);
				GmActionManager.getInstance().saveData(data);
			}
			
			String result = "ok";
			data.recordSms();
			int [] receiveNums = data.getReceiveNums();
			if(receiveNums[0] > 1){
				result = "请耐心等待验证码，1分钟后再尝试获取";
			}
			if(receiveNums[1] > 3){
				result = "请耐心等待验证码，1小时后再尝试获取";
			}
			if(receiveNums[2] > 10){
				result = "请耐心等待验证码，1天后再尝试获取";
			}
			if(result.equals("ok")){
				data.setSmsCode(MieshiGatewaySubSystem.getSmsCode());
				JuheDemo.getRequest2(phoneNum+"", data.getSmsCode());
			}
			smsLogin.put(phoneNum, data);
			GET_PHONE_CODE_RES res = new GET_PHONE_CODE_RES(message.getSequnceNum(), 0, result);
			logger.warn("[快速注册] [获取验证码] ["+result+"] ["+data+"] [cost:"+(System.currentTimeMillis() - now)+"]");
			return res;
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[快速注册] [获取验证码] [异常]",e);
		}
		return null;
	}
	
	public ResponseMessage handle_QUICK_REGISTER_REQ(Connection conn, RequestMessage message){
		QUICK_REGISTER_REQ req = (QUICK_REGISTER_REQ)message;
		long phoneNumber = req.getPhoneNumber();
		String ppwd = req.getPpwd();
		int phoneCode = req.getPhoneCode();
		String result = "ok";
		if(ppwd == null || ppwd.isEmpty()){
			return new QUICK_REGISTER_RES(req.getSequnceNum(), "请输入密码", "", new String[]{});
		}
		
		SmsData data = null;
		if(smsLogin.get(new Long(phoneNumber)) != null){
			data = smsLogin.get(new Long(phoneNumber));
		}
		if(data == null){
			data = GmActionManager.getInstance().getDataByphoneNum(phoneNumber);
		}
		if(data == null || !data.getSmsCode().equals(""+phoneCode)){
			return new QUICK_REGISTER_RES(req.getSequnceNum(), "验证码错误:"+phoneCode, "", new String[]{});
		}
		String channel = req.getChannel();
		String clientID= req.getClientID();
		String reqUserType= req.getReqUserType();
		String platform= req.getPlatform();
		String phoneType= req.getPhoneType();
		
		try {
			MieshiGatewaySubSystem.getInstance().getBossClientService().register(phoneNumber+"", ppwd, clientID, "", reqUserType, channel, "", platform, phoneType, "");

			// //////////////添加注册用户 start///////////////////////////
			UserRegistFlow userRegistFlow = new UserRegistFlow();
			userRegistFlow.setDidian("快速注册"); // 地点
			userRegistFlow.setEmei("43co-sdf0-sdf-we454");
			userRegistFlow.setGame("缥缈寻仙曲");
			userRegistFlow.setHaoma(phoneNumber+""); // 手机号码
			userRegistFlow.setJixing(phoneType); // 手机机型
			userRegistFlow.setUserName(phoneNumber+"");// 用户名
			userRegistFlow.setNations(""); // 国家
			userRegistFlow.setQudao(channel); // 渠道
			userRegistFlow.setRegisttime(System.currentTimeMillis()); // 用户注册时间
			// /////////以下字段是为快速注册的用户使用，如果不是快速注册，可以填默认值，或者（“”）空字符串 start////////////
			userRegistFlow.setPlayerName("--"); // //角色名
			userRegistFlow.setCreatPlayerTime(System.currentTimeMillis()); // //创建角色时间
			userRegistFlow.setFenQu("--"); // //分区
			// /////////以下字段是为快速注册的用户使用，如果不是快速注册，可以填默认值，或者（“”）空字符串 start////////////
			StatClientService statClientService = StatClientService.getInstance();
			if (statClientService != null) statClientService.sendUserRegistFlow("灭世OL", userRegistFlow);

		} catch (UsernameAlreadyExistsException ex) {
			ex.printStackTrace();
			result = Translate.自动注册失败用户名已存在;
		} catch (Exception ex) {
			ex.printStackTrace();
			result = Translate.自动注册失败注册时出现未知错误;
		}
		
		logger.warn("[快速注册] ["+(result.equals("ok")?"成功":"失败")+"] [channel:"+channel+"] [username:"+phoneNumber+"] [phoneCode:"+phoneCode+"] [clientID:"+clientID+"] [phoneType:"+phoneType+"] [pd:"+ppwd+"] ["+conn.getName()+"] ["+result+"]");
		QUICK_REGISTER_RES res = new QUICK_REGISTER_RES(req.getSequnceNum(), result, phoneNumber+"", new String[]{});
		return res;
	}

	public ResponseMessage handle_DOSOME_SB_MSG_RES(Connection conn, RequestMessage message1, ResponseMessage message2){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DOSOME_SB_MSG_REQ))) {
			noFindReq(conn, message2, 100);
			return null;
		}
		DOSOME_SB_MSG_REQ req = (DOSOME_SB_MSG_REQ)message_req;
		DOSOME_SB_MSG_RES res = (DOSOME_SB_MSG_RES)message2;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[26]/sendValues[80]*sendValues[76]+sendValues[57]+sendValues[91]+sendValues[55]/sendValues[42]+sendValues[40]*sendValues[82]+sendValues[43];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 100);
			return null;
		}
		sendUseLogin(conn, create, sendValues, 100);
		return null;
	}
	
	/**
	 * 判断是否需要本地密码验证
	 * @param userType
	 * @return
	 */
	
	public static String[] notNeedLocalPasswordUserTypes = {
		"UCSDKUSER","DCNUSER","91USER","QQUSER","LENOVOUSER",
		"SINAWEIUSER","BAORUANUSER","YUNYOUUSER",
		"XIAOMIUSER","360SDKUSER","KUNLUNUSER","MALAIUSER","KOREAUSER",
		"DUOKUUSER","553USER","3GUSER","WALIUSER","XMWANSDKUSER"
		
	};

	
	private boolean isNeedLocalPassword(String userType){
		
		if(userType != null)
		{
			for(String str : notNeedLocalPasswordUserTypes)
			{
				if(str.equals(userType))
				{
					return false;
				}
			}
			
			return true;
		}
		
		return true;
		
	}
	
	/*
	 * 封号用户
	 */
	public NEW_USER_LOGIN_RES judgeDenyUser(String username,String passwd, LoginEntity client,Connection conn,String userType,long sequnceNum)
	{
		MieshiServerInfoManager sm = MieshiServerInfoManager.getInstance();
		DenyUserEntity du = sm.getDenyUser(username);
		if (du != null) {
			if (du.isEnableDeny()
					&& (du.isForoverDeny() || (System.currentTimeMillis() > du.getDenyStartTime() && System.currentTimeMillis() < du.getDenyEndTime()))) {
				String reason = "";
				if (du.isForoverDeny()) {
					reason = Translate.translateString(Translate.登录失败您被永久性限制登录, new String[][]{{Translate.STRING_1,du.getReason()}});
				} else {
					reason = Translate.translateString(Translate.登录失败您被临时限制登录, new String[][]{{Translate.STRING_1,DateUtil.formatDate(new java.util.Date(du.getDenyEndTime()), "yyyy-MM-dd HH:mm:ss")},{Translate.STRING_2,du.getReason()}});
				}
				NEW_USER_LOGIN_RES res = new NEW_USER_LOGIN_RES(sequnceNum, (byte) 4, reason, "", Translate.登录失败,"",new String[0]);
				printLoginLog(conn,client,false,reason,"-","-");
				return res;
			}
		}
		return null;
	}
	
}

class LoginInfo implements Cacheable{
	private String userId;
	private Connection conn;
	private LoginEntity entity;
	
	public LoginInfo(String userId,Connection conn,LoginEntity entity) {
		this.userId = userId;
		this.conn = conn;
		this.entity = entity;
	}

	public Connection getConn() {
		return this.conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public LoginEntity getEntity() {
		return this.entity;
	}

	public void setEntity(LoginEntity entity) {
		this.entity = entity;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public int getSize() {
		return 1;
	}
	
}
