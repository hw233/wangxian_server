package com.fy.engineserver.sprite;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.event.LeaveGameEvent;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.lineup.EnterServerAgent;
import com.fy.engineserver.message.*;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.security.SecuritySubSystem;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.tengxun.TengXunDataManager;
import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class NewUserEnterServerService {
	
	//是否使用hand_1这样的协议  SERVER_HAND_CLIENT_11_REQ
	public static boolean isSendHand1 = true;
	//是否使用其他的hand协议   乱七八糟的随机的
	public static boolean isSendHand2 = true;
	
	public static Logger logger = LoggerFactory.getLogger(NewUserEnterServerService.class);

	//新登录协议的usersion
	public static String UserLoginPrivateKey = "sq22wx11ageZWLL";
	//用户登录连接，因为登录后需要去网关校验sessionID，校验后要再给用户返会，所以暂时把连接保存在这 用userName为key
	//这个时间是
	public static long userLoginConnTime = 5 * 60 * 1000L;
	public static Map<String, Connection> userLoginConnections = new ConcurrentHashMap<String, Connection>();
	
	//检查协议info GET_PHONE_INFO_1_REQ  GET_CLIENT_INFO_REQ  这类型的协议
	public static String[] checkStrings = new String[]{"wangx","shenqishidai","shenqiage"};
	public static HashMap<Long, ClientInfo> checkInfo = new HashMap<Long, NewUserEnterServerService.ClientInfo>();
	
	/**
	 * 收到新的用户登录server协议，
	 * 先验证客户端发过来的md5码是不是与服务器算出来的一致
	 * 然后去网关校验sessionID
	 * @param conn
	 * @param message
	 */
	public static void handle_NEW_USER_ENTER_SERVER_REQ (Connection conn, RequestMessage message) {
		NEW_USER_ENTER_SERVER_REQ req = (NEW_USER_ENTER_SERVER_REQ)message;
		
		// 创建新加的代码.原则:1在预创建的状态,2在预创建时间内,3创建的国家未满
		if(PlatformManager.getInstance().isPlatformOf(Platform.官方))
		{
			if (!CoreSubSystem.getInstance().judgeCanEnterServer()) {
				long startTime = System.currentTimeMillis();
				// 是预创建的服务器
				if (!CoreSubSystem.getInstance().timeCanBeforehandCreate(startTime)) {

					CoreSubSystem.logger.warn("是预创建的服务器,但是时间已经过了,不能创建角色");
					
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 31, "亲爱的仙友，预创建时间已结束，无法进行预创建操作，服务器开启时间请留意官网与论坛公告!");
					GameNetworkFramework.getInstance().sendMessage(conn, hreq, "");
					return;
				}

			}
		}
		
		
		String userName = req.getUsername();
		String password = req.getPassword();
		String sessionId = req.getSessionId();
		
		String clientId = req.getClientId();
		String channel = req.getChannel();
		String clientPlatform = req.getClientPlatform();
		String clientFull = req.getClientFull();
		String clientProgramVersion = req.getClientProgramVersion();
		String clientResourceVersion = req.getClientResourceVersion();
		
		String phoneType = req.getPhoneType();
		String network = req.getNetwork();
		String gpu = req.getGpu();
		String gpuOtherName = req.getGpuOtherName();
		String MACADDRESS = req.getMACADDRESS();
		String md5 = req.getMd5();
		
		Object o = conn.getAttachmentData("USER_CLIENT_INFO_REQ");
		if (o instanceof USER_CLIENT_INFO_REQ) {
			USER_CLIENT_INFO_REQ req1 = (USER_CLIENT_INFO_REQ) o;
			req1.setClientId(clientId);
			req1.setChannel(channel);
			req1.setClientPlatform(clientPlatform);
			req1.setClientProgramVersion(clientProgramVersion);
			req1.setPhoneType(phoneType);
			req1.setGpu(gpu);
			req1.setGpuOtherName(gpuOtherName);
			req1.setMACADDRESS(MACADDRESS);
		}
		
		StringBuffer sb = new StringBuffer("");
		sb.append(userName).append(password).append(sessionId).append(clientId).append(channel).append(clientPlatform).append(clientFull)
		.append(clientProgramVersion).append(clientResourceVersion).append(phoneType).append(network).append(gpu)
		.append(gpuOtherName).append(MACADDRESS).append(UserLoginPrivateKey);
		try {
			String server_hash = StringUtil.hash(sb.toString());
			if (md5.equals(server_hash)) {
				//匹配  去网关校验
				conn.setAttachmentData("NEW_USER_ENTER_SERVER_REQ", req);
				conn.setAttachmentData("NEW_TIME_AA", System.currentTimeMillis());
				
				try {
					if(channel != null && channel.contains("KUAIYONGSDK")){
						Passport passport = SpriteSubSystem.getInstance().bossService.getPassportByUserName(userName);
						if(passport != null){
							//快用sdk，客户端传递username作为nickname，通过nickname获取的passport，做username的修正
							userName = passport.getUserName();
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				userLoginConnections.put(userName, conn);
				Message m = new SESSION_VALIDATE_REQ(GameMessageFactory.nextSequnceNum(), GameConstants.getInstance().getServerName(), userName, sessionId, clientId, channel, clientPlatform, phoneType, network, gpuOtherName, MACADDRESS);
				MieshiGatewayClientService.getInstance().sendMessageToGateway(m);
				logger.warn("[去网关校验session] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", 
						new Object[]{conn.getName(), "--" , userName, sessionId, clientId, channel, clientPlatform, clientFull, clientProgramVersion, 
						clientResourceVersion, phoneType, network, gpu, gpuOtherName, MACADDRESS});
			}else {
				conn.close();
				logger.warn("[NEW_USER_ENTER_SERVER_REQ的MD5不匹配] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", 
						new Object[]{conn.getName(), userName, sessionId, clientId, channel, clientPlatform, clientFull, clientProgramVersion, 
						clientResourceVersion, phoneType, network, gpu, gpuOtherName, MACADDRESS, md5, server_hash});
			}
		} catch (Exception e) {
			conn.close();
			logger.warn("[NEW_USER_ENTER_SERVER_REQ的出现错误] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", 
					new Object[]{conn.getName(), userName, sessionId, clientId, channel, clientPlatform, clientFull, clientProgramVersion, 
					clientResourceVersion, phoneType, network, gpu, gpuOtherName, MACADDRESS, md5});
			GamePlayerManager.logger.error("[NEW_USER_ENTER_SERVER_REQ的出现错误] ["+conn.getName()+"] ["+userName+"]", e);
		}
	}
	
	public static void handle_NOTIFY_SERVER_TIREN_REQ (Connection conn, RequestMessage message) {
		NOTIFY_SERVER_TIREN_REQ req = (NOTIFY_SERVER_TIREN_REQ)message;
		Connection userConn = userLoginConnections.get(req.getUsername());
		if (userConn != null) {
			userConn.close();
			userLoginConnections.remove(req.getUsername());
		}else {
			Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
			for (Player p : ps) {
				if (p.getUsername().equals(req.getUsername())) {
					if (p.getConn() != null ) {
						p.getConn().close();
					}
				}
			}
		}
		NewUserEnterServerService.logger.warn("[收到网关T人] ["+req.getUsername()+"] ["+req.getReason()+"]");
	}
	
	public static void handle_SESSION_VALIDATE_RES (Connection conn, ResponseMessage message) {
		//session验证
		try{
			SESSION_VALIDATE_RES res = (SESSION_VALIDATE_RES)message;
			String username = res.getUsername();
			String sessionId = res.getSessionId();
			String clientId = res.getClientId();
			int result = res.getResult();
			String desc = res.getDesc();
			Connection userConn = userLoginConnections.get(username);
			if (userConn == null || userConn.getState() == Connection.CONN_STATE_CLOSE) {
				logger.warn("[连接不存在或已经关闭] [{}] [{}] [{}] [{}] [{}]", new Object[]{username, sessionId, clientId, result, desc});
				return;
			}
			NEW_USER_ENTER_SERVER_REQ req = (NEW_USER_ENTER_SERVER_REQ)userConn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
			if (req == null) {
				userConn.close();
				logger.warn("[连接中登陆协议不见] [{}] [{}] [{}] [{}] [{}]", new Object[]{username, sessionId, clientId, result, desc});
				return ;
			}
			String enterUserName = req.getUsername();
			if(req.getChannel() != null && req.getChannel().contains("KUAIYONGSDK")){
				Passport passport = SpriteSubSystem.getInstance().bossService.getPassportByUserName(enterUserName);
				if(passport != null){
					enterUserName = passport.getUserName();
				}
			}
			if ((!enterUserName.equals(username)) || (!req.getSessionId().equals(sessionId)) || (!req.getClientId().equals(clientId))) {
				userConn.close();
				logger.warn("[协议与请求网关不一致] [enterUserName:{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{enterUserName,username, req.getUsername(), sessionId, req.getSessionId(), clientId, req.getClientId(), result, desc});
				return;
			}
			if (result == 0) {
				if (EnterLimitManager.isCheckIOS_Server) {
					Object reqO = userConn.getAttachmentData("IOS_CLIENT_MSG_REQ");
					if (reqO == null) {
						//没有这个协议
						if (desc.indexOf("IIOS") >= 0) {
//							DENY_USER_REQ deny = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", username, "使用android客户端非法访问APPSTORE服务器", GameConstants.getInstance().getServerName() + "-外挂检测模块", false, true, true, 0, -1);
//							MieshiGatewayClientService.getInstance().sendMessageToGateway(deny);
							desc.substring(desc.indexOf("IIOS")+5);
							userConn.close();
							EnterLimitManager.logger.warn("[未有IOS协议的客户端] ["+username+"] ["+clientId+"]");
							return;
						}
					}
				}
				if (desc.indexOf("IIOS") >= 0) {
					desc.substring(desc.indexOf("IIOS")+5);
				}
				//验证成功  发送握手协议
				if(userConn.getAttachmentData("SEND_HAND_MESSAGE") != null || userConn.getAttachmentData("SEND_HAND_MESSAGE_NEW") != null) {
					return ;
				}
				userConn.setAttachmentData("user_shenfen", desc);
				if (isSendHand2) {
					Message m_new = createSendMessage_new();
					userConn.setAttachmentData("SEND_HAND_MESSAGE_NEW", m_new);
					GameNetworkFramework.getInstance().sendMessage(userConn, m_new, "");
					logger.warn("[发送握手协议2] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] {m_new.getTypeDescription(), username, req.getUsername(), sessionId, req.getSessionId(), clientId, req.getClientId(), result, desc});
				}
				if (isSendHand1) {
					Message m = createSendMessage();
					userConn.setAttachmentData("SEND_HAND_MESSAGE", m);
					GameNetworkFramework.getInstance().sendMessage(userConn, m, "");
					logger.warn("[发送握手协议1] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] {m.getTypeDescription(), username, req.getUsername(), sessionId, req.getSessionId(), clientId, req.getClientId(), result, desc});
				}
				return;
			}else {
				//验证失败
				userConn.close();
				logger.warn("[网关验证失败] [{}] [{}] [{}] [{}] [{}]", new Object[]{username, sessionId, clientId, result, desc});
				return;
			}
		}catch(Exception e) {
			logger.error("handle_SESSION_VALIDATE_RES", e);
		}
	}
	
	/**
	 * 握手协议没有在连接中找到对应的REQ协议
	 * @param conn
	 */
	public static void noFindReq (Connection conn, ResponseMessage message, int messageIndex) {
		conn.close();
		NEW_USER_ENTER_SERVER_REQ req = (NEW_USER_ENTER_SERVER_REQ)conn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
		String u = "";
		if (req != null) {
			u += " [" + req.getUsername() + "]";
			u += " [" + req.getClientId() + "]";
			u += " [" + req.getChannel() + "]";
			u += " [" + req.getClientPlatform() + "]";
			u += " [" + req.getClientProgramVersion() + "]";
			u += " [" + req.getNetwork() + "]";
			u += " [" + req.getGpuOtherName() + "]";
			u += " [" + req.getMACADDRESS() + "]";
		}
		Message m = (Message)conn.getAttachmentData("SEND_HAND_MESSAGE");
		logger.info("[握手协议连接中未找到] ["+conn.getName()+"] ["+u+"] ["+(m != null ? m.getTypeDescription() : null)+"] ["+message.getTypeDescription()+"]");
	}
	
	/**
	 * 握手协议返回的值不正确
	 * @param conn
	 */
	public static void backValueNE (Connection conn, long create, long re, long[] sendValues, int messageIndex) {
		conn.close();
		NEW_USER_ENTER_SERVER_REQ req = (NEW_USER_ENTER_SERVER_REQ)conn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
		String u = "";
		if (req != null) {
			u += " [" + req.getUsername() + "]";
			u += " [" + req.getClientId() + "]";
			u += " [" + req.getChannel() + "]";
			u += " [" + req.getClientPlatform() + "]";
			u += " [" + req.getClientProgramVersion() + "]";
			u += " [" + req.getNetwork() + "]";
			u += " [" + req.getGpuOtherName() + "]";
			u += " [" + req.getMACADDRESS() + "]";
		}
		Message m = (Message)conn.getAttachmentData("SEND_HAND_MESSAGE");
		logger.info("[握手协议值不对] ["+conn.getName()+"] ["+u+"] ["+(m != null ? m.getTypeDescription() : null)+"] ["+create+"~"+re+"]");
	}
	
	/**
	 * 握手成功，发送用户登录正确给客户端
	 * @param conn
	 */
	public static void sendEnterServer (Connection conn, long create, long[] sendValues, int messageIndex) {
		NEW_USER_ENTER_SERVER_REQ req = (NEW_USER_ENTER_SERVER_REQ)conn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
		if (req == null) {
			conn.close();
			logger.info("[登录协议连接中没有] ["+conn.getName()+"]");
		}else {
			try{
				
				conn.setAttachmentData("SEND_HAND_MESSAGE", null);
				conn.setAttachmentData("SEND_HAND_MESSAGE_NEW", null);
				
				String u = "";
				if (req != null) {
					u += " [" + req.getUsername() + "]";
					u += " [" + req.getClientId() + "]";
					u += " [" + req.getChannel() + "]";
					u += " [" + req.getClientPlatform() + "]";
					u += " [" + req.getClientProgramVersion() + "]";
					u += " [" + req.getNetwork() + "]";
					u += " [" + req.getGpuOtherName() + "]";
					u += " [" + req.getMACADDRESS() + "]";
				}
				
				conn.setAttachmentData("NEW_TIME_AA", null);
				EnterServerAgent.getInstance().cancelLineup(req.getUsername());
				Passport passport = SpriteSubSystem.getInstance().bossService.getPassportByUserName(req.getUsername());
				// 登陆限制
				{
					if (EnterLimitManager.LIMIT && EnterLimitManager.getInstance() != null && EnterLimitManager.getInstance().inEnterLimit(passport.getUserName(), conn)) {
						conn.close();
						logger.warn("[疑似外挂] [阻止登陆] [" + passport.getUserName() + "] ["+u+"]");
						return;
					}
				}
				// 登录成功，检查是否有这个用户的玩家在线，如果有，全部踢掉
				PlayerManager playerManager = PlayerManager.getInstance();
				Player ps[] = PlayerManager.getInstance().getOnlinePlayerByUser(req.getUsername());
				for (Player p : ps) {
					if (p.isOnline()) {
						if (p.getCurrentGame() != null) {
							p.getCurrentGame().getQueue().push(new LeaveGameEvent(p));
						}
						p.leaveServer();
						p.getConn().close();
						// logger.warn("[用户进入服务器] [已有此用户的玩家在线] [踢掉此用户] [用户:" + userName + "] [玩家:" + p.getName() + "]");
						if (logger.isWarnEnabled()) logger.warn("[用户进入服务器] [已有此用户的玩家在线] [踢掉此用户] [用户:{}] [玩家:{}]", new Object[] { req.getUsername(), p.getName() });
					}
				}
				conn.setAttachment(passport);
				conn.setAttachmentData(SecuritySubSystem.USERNAME, req.getUsername());
				conn.setAttachmentData(SecuritySubSystem.MACADDRESS, req.getMACADDRESS());
				// 检查排队
				EnterServerAgent agent = EnterServerAgent.getInstance();
				boolean canEnter = agent.canEnterDirectly(req.getUsername(), conn);
				boolean vipEnter = false;
				if(!canEnter) {
					//检查这个账号的玩家是否有角色是vip2以上，如果有则不需要排队
					Player pps[] = playerManager.getPlayerByUser(req.getUsername());
					for(Player p : pps) {
						if(p.getVipLevel() >= SpriteSubSystem.NOLINEUP_VIPLEVEL) {
							vipEnter = true;
							break;
						}
					}
				}
				
				{
					Object o = conn.getAttachmentData("USER_CLIENT_INFO_REQ");
					if (o instanceof USER_CLIENT_INFO_REQ) {
						USER_CLIENT_INFO_REQ req1 = (USER_CLIENT_INFO_REQ) o;
						String clientId = req1.getClientId();
						String channel = req1.getChannel();
						String clientPlatform = req1.getClientPlatform();
						String clientFull = req1.getClientFull();
						String clientProgramVersion = req1.getClientProgramVersion();
						String clientResourceVersion = req1.getClientResourceVersion();
						String phoneType = req1.getPhoneType();
						String network = req1.getNetwork();
						String gpu = req1.getGpu();
						String gpuOtherName = req1.getGpuOtherName();
						String UUID = req1.getUUID();
						String DEVICEID = req1.getDEVICEID();
						String IMSI = req1.getIMSI();
						String MACADDRESS = req1.getMACADDRESS();
						boolean isExistOtherServer = req1.getIsExistOtherServer();
						boolean isStartServerSucess = req1.getIsStartServerSucess();
						boolean isStartServerBindFail = req1.getIsStartServerBindFail();
						logger.warn("[USER_CLIENT_INFO_REQ] [userName:" + req.getUsername() + "] [isExistOtherServer:" + isExistOtherServer + "] [isStartServerSucess:" + isStartServerSucess + "] [isStartServerBindFail:" + isStartServerBindFail + "] [clientId:" + clientId + "] [channel:" + channel + "] [clientPlatform:" + clientPlatform + "] [clientFull:" + clientFull + "] [clientProgramVersion:" + clientProgramVersion + "] [clientResourceVersion:" + clientResourceVersion + "] [phoneType:" + phoneType + "] [network:" + network + "] [gpu:" + gpu + "] [gpuOtherName:" + gpuOtherName + "] [UUID:" + UUID + "] [DEVICEID:" + DEVICEID + "] [IMSI:" + IMSI + "] [MACADDRESS:" + MACADDRESS + "]");
					}
				}
				TengXunDataManager.instance.putTXLoginInfo(req.getUsername(), req.getPassword());
				if (canEnter || vipEnter) {
					if(vipEnter) {
						conn.setAttachmentData("vipEnter", Boolean.TRUE);
					}
					logger.info("[用户进入服务器握手] [成功:直接进入] [vipEnter:"+vipEnter+"] [用户:{}] [channel:{}] [authCode:{}] [{}]", new Object[] { req.getUsername(), passport != null ? passport.getRegisterChannel() : "", "", u});
					USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 0, 0L, 0L, 0L);
					GameNetworkFramework.getInstance().sendMessage(conn, res, "");
				} else {
					agent.doLineup(req.getUsername(), conn);
					int stat[] = agent.getLineupStatus(req.getUsername());
					int pos = agent.getNowPosition(req.getUsername());
					long etime = agent.getEstimateTime(pos);
					logger.info("[用户进入服务器握手] [成功：需要排队] [用户:{}] [{}] [位置:" + pos + "] [前面在线/离线:" + stat[0] + "/" + stat[1] + "] [预计进入时长:" + etime + "ms]", new Object[] { req.getUsername() , u});
					USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(req.getSequnceNum(), (byte) 1, stat[0], stat[1], etime);
					GameNetworkFramework.getInstance().sendMessage(conn, res, "");
				}
			}catch(Exception e) {
				logger.error("sendEnterServer" + messageIndex, e);
			}
		}
	}
	

	public static void handle_GET_PHONE_INFO_1_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_1_RES res = (GET_PHONE_INFO_1_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_1_REQ")) {
			info.isMessageRight = true;
		}
		try{
			if (info.backInfo[0].equals(checkStrings[0])){
				if (info.backInfo[2].equals(checkStrings[1])) {
					if (info.backInfo[4].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_PHONE_INFO_2_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_2_RES res = (GET_PHONE_INFO_2_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_2_REQ")) {
			info.isMessageRight = true;
		}
		try{
			if (info.backInfo[0].equals(checkStrings[0])){
				if (info.backInfo[2].equals(checkStrings[1])) {
					if (info.backInfo[5].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_PHONE_INFO_3_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_3_RES res = (GET_PHONE_INFO_3_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_3_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[0])){
				if (info.backInfo[2].equals(checkStrings[1])) {
					if (info.backInfo[6].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_PHONE_INFO_4_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_4_RES res = (GET_PHONE_INFO_4_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_4_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[0])){
				if (info.backInfo[2].equals(checkStrings[1])) {
					int pNum = Integer.parseInt(info.backInfo[5]);
					if (info.backInfo[6+pNum].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_PHONE_INFO_5_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_5_RES res = (GET_PHONE_INFO_5_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_5_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[0])){
				if (info.backInfo[2].equals(checkStrings[1])) {
					int pNum = Integer.parseInt(info.backInfo[5]);
					if (info.backInfo[7+pNum].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_PHONE_INFO_6_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_6_RES res = (GET_PHONE_INFO_6_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_6_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[0])){
				if (info.backInfo[2].equals(checkStrings[1])) {
					if (info.backInfo[info.backInfo.length-1].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_PHONE_INFO_7_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_7_RES res = (GET_PHONE_INFO_7_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_7_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[0])){
				if (info.backInfo[3].equals(checkStrings[1])) {
					if (info.backInfo[info.backInfo.length-1].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_PHONE_INFO_8_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_8_RES res = (GET_PHONE_INFO_8_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_8_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[0])){
				if (info.backInfo[4].equals(checkStrings[1])) {
					if (info.backInfo[info.backInfo.length-1].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_PHONE_INFO_9_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_9_RES res = (GET_PHONE_INFO_9_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_9_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[0])){
				if (info.backInfo[5].equals(checkStrings[1])) {
					if (info.backInfo[info.backInfo.length-1].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_PHONE_INFO_10_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_10_RES res = (GET_PHONE_INFO_10_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_10_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[0])){
				int pNum = Integer.parseInt(info.backInfo[5]);
				if (info.backInfo[6+pNum].equals(checkStrings[1])) {
					if (info.backInfo[info.backInfo.length-1].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_PHONE_INFO_11_RES(Connection conn,ResponseMessage message, Player player) {
		GET_PHONE_INFO_11_RES res = (GET_PHONE_INFO_11_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_PHONE_INFO_11_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[0])){
				int pNum = Integer.parseInt(info.backInfo[5]);
				if (info.backInfo[7+pNum].equals(checkStrings[1])) {
					if (info.backInfo[info.backInfo.length-1].equals(checkStrings[2])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}

	public static void handle_GET_CLIENT_INFO_RES(Connection conn, ResponseMessage message, Player player) {
		GET_CLIENT_INFO_RES res = (GET_CLIENT_INFO_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_CLIENT_INFO_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[2])){
				if (info.backInfo[1].equals(checkStrings[0])) {
					if (info.backInfo[2].equals(checkStrings[1])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}
	
	public static void handle_GET_CLIENT_INFO_1_RES(Connection conn, ResponseMessage message, Player player) {
		GET_CLIENT_INFO_1_RES res = (GET_CLIENT_INFO_1_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_CLIENT_INFO_1_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[2])){
				if (info.backInfo[1].equals(checkStrings[0])) {
					if (info.backInfo[3].equals(checkStrings[1])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}
	
	public static void handle_GET_CLIENT_INFO_2_RES(Connection conn, ResponseMessage message, Player player) {
		GET_CLIENT_INFO_2_RES res = (GET_CLIENT_INFO_2_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_CLIENT_INFO_2_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[2])){
				if (info.backInfo[1].equals(checkStrings[0])) {
					if (info.backInfo[4].equals(checkStrings[1])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}
	
	public static void handle_GET_CLIENT_INFO_3_RES(Connection conn, ResponseMessage message, Player player) {
		GET_CLIENT_INFO_3_RES res = (GET_CLIENT_INFO_3_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_CLIENT_INFO_3_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[2])){
				if (info.backInfo[1].equals(checkStrings[0])) {
					if (info.backInfo[5].equals(checkStrings[1])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}
	
	public static void handle_GET_CLIENT_INFO_4_RES(Connection conn, ResponseMessage message, Player player) {
		GET_CLIENT_INFO_4_RES res = (GET_CLIENT_INFO_4_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_CLIENT_INFO_4_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[2])){
				if (info.backInfo[1].equals(checkStrings[0])) {
					if (info.backInfo[6].equals(checkStrings[1])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}
	
	public static void handle_GET_CLIENT_INFO_5_RES(Connection conn, ResponseMessage message, Player player) {
		GET_CLIENT_INFO_5_RES res = (GET_CLIENT_INFO_5_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_CLIENT_INFO_5_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[2])){
				if (info.backInfo[1].equals(checkStrings[0])) {
					int pNum = Integer.parseInt(info.backInfo[5]);
					if (info.backInfo[pNum+6].equals(checkStrings[1])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}
	
	public static void handle_GET_CLIENT_INFO_6_RES(Connection conn, ResponseMessage message, Player player) {
		GET_CLIENT_INFO_6_RES res = (GET_CLIENT_INFO_6_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_CLIENT_INFO_6_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[2])){
				if (info.backInfo[1].equals(checkStrings[0])) {
					int pNum = Integer.parseInt(info.backInfo[5]);
					if (info.backInfo[pNum+7].equals(checkStrings[1])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}
	
	public static void handle_GET_CLIENT_INFO_7_RES(Connection conn, ResponseMessage message, Player player) {
		GET_CLIENT_INFO_7_RES res = (GET_CLIENT_INFO_7_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_CLIENT_INFO_7_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[0].equals(checkStrings[2])){
				if (info.backInfo[1].equals(checkStrings[0])) {
					if (info.backInfo[info.backInfo.length-1].equals(checkStrings[1])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}
	
	public static void handle_GET_CLIENT_INFO_8_RES(Connection conn, ResponseMessage message, Player player) {
		GET_CLIENT_INFO_8_RES res = (GET_CLIENT_INFO_8_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_CLIENT_INFO_8_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[1].equals(checkStrings[2])){
				if (info.backInfo[2].equals(checkStrings[0])) {
					if (info.backInfo[info.backInfo.length-1].equals(checkStrings[1])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}
	
	public static void handle_GET_CLIENT_INFO_9_RES(Connection conn, ResponseMessage message, Player player) {
		GET_CLIENT_INFO_9_RES res = (GET_CLIENT_INFO_9_RES)message;
		if (player == null) {
			return;
		}
		String[] cValues = res.getClientValue();
		ClientInfo info = checkInfo.get(player.getId());
		if (info == null) {
			return;
		}
		info.backInfo = cValues;
		info.isBack = true;
		if (info.messageName.equals("GET_CLIENT_INFO_9_REQ")) {
			info.isMessageRight = true;
		}
		try {
			if (info.backInfo[2].equals(checkStrings[2])){
				if (info.backInfo[3].equals(checkStrings[0])) {
					if (info.backInfo[info.backInfo.length-1].equals(checkStrings[1])) {
						info.isBackRight = true;
					}
				}
			}
		}catch (Exception e) {
			logger.warn("分析backInfo出错 [" + message.getClass().getName() + "] [" +player.getLogString()+ "]", e);
		}
		logger.warn("[收到玩家PhoneInfo协议] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), res.getClass().getName(), "--" , conn.getName(), Arrays.toString(info.backInfo), info.isMessageRight, info.messageName});
	}
	
	
	/**
	 * 返回协议，根据每年的天数 对 协议数做求余来决定取哪个协议
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int messageNum = 100;
	public static int sendValueNum = 100;
	public static Message createSendMessage() {
		Calendar ca = Calendar.getInstance();
		int day_y = ca.get(Calendar.DAY_OF_YEAR);
		day_y = day_y % messageNum;
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
	
	public static Message createSendMessage_new() {
		Calendar ca = Calendar.getInstance();
		int day_y = ca.get(Calendar.DAY_OF_YEAR);
		day_y = day_y % messageNum;
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
	
	//------------------------------------------------
	//------------------下面的handle 都是通过自动生成---请勿轻易生成，下面的计算方法必须保证与客户端一致-------------------
	//------------------------------------------------
	//------------------------------------------------
	//------------------下面的handle 都是通过自动生成---请勿轻易生成，下面的计算方法必须保证与客户端一致-------------------
	//------------------------------------------------
	//------------------------------------------------
	//------------------下面的handle 都是通过自动生成---请勿轻易生成，下面的计算方法必须保证与客户端一致-------------------
	//------------------------------------------------
	//TODO: hand_client_N;
	public static void handle_SERVER_HAND_CLIENT_1_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_1_REQ))) {
			noFindReq(conn, message, 1);
			return;
		}
		SERVER_HAND_CLIENT_1_REQ req = (SERVER_HAND_CLIENT_1_REQ)message_req;
		SERVER_HAND_CLIENT_1_RES res = (SERVER_HAND_CLIENT_1_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[30]-sendValues[94]*sendValues[2]-sendValues[58]-sendValues[33]*sendValues[38]-sendValues[70]+sendValues[26]-sendValues[23]+sendValues[93];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 1);
			return;
		}
		sendEnterServer(conn, create, sendValues, 1);
	}

	public static void handle_SERVER_HAND_CLIENT_2_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_2_REQ))) {
			noFindReq(conn, message, 2);
			return;
		}
		SERVER_HAND_CLIENT_2_REQ req = (SERVER_HAND_CLIENT_2_REQ)message_req;
		SERVER_HAND_CLIENT_2_RES res = (SERVER_HAND_CLIENT_2_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[95]/sendValues[87]-sendValues[19]/sendValues[3]-sendValues[70]*sendValues[33]*sendValues[51]*sendValues[45]-sendValues[65]+sendValues[71];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 2);
			return;
		}
		sendEnterServer(conn, create, sendValues, 2);
	}

	public static void handle_SERVER_HAND_CLIENT_3_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_3_REQ))) {
			noFindReq(conn, message, 3);
			return;
		}
		SERVER_HAND_CLIENT_3_REQ req = (SERVER_HAND_CLIENT_3_REQ)message_req;
		SERVER_HAND_CLIENT_3_RES res = (SERVER_HAND_CLIENT_3_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[69]-sendValues[25]/sendValues[42]/sendValues[51]-sendValues[52]-sendValues[10]*sendValues[3]/sendValues[1]-sendValues[99]*sendValues[96];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 3);
			return;
		}
		sendEnterServer(conn, create, sendValues, 3);
	}

	public static void handle_SERVER_HAND_CLIENT_4_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_4_REQ))) {
			noFindReq(conn, message, 4);
			return;
		}
		SERVER_HAND_CLIENT_4_REQ req = (SERVER_HAND_CLIENT_4_REQ)message_req;
		SERVER_HAND_CLIENT_4_RES res = (SERVER_HAND_CLIENT_4_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[76]/sendValues[8]-sendValues[60]/sendValues[42]+sendValues[29]/sendValues[62]-sendValues[68]/sendValues[94]/sendValues[53]+sendValues[13];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 4);
			return;
		}
		sendEnterServer(conn, create, sendValues, 4);
	}

	public static void handle_SERVER_HAND_CLIENT_5_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_5_REQ))) {
			noFindReq(conn, message, 5);
			return;
		}
		SERVER_HAND_CLIENT_5_REQ req = (SERVER_HAND_CLIENT_5_REQ)message_req;
		SERVER_HAND_CLIENT_5_RES res = (SERVER_HAND_CLIENT_5_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[6]/sendValues[12]-sendValues[78]/sendValues[99]-sendValues[39]-sendValues[81]-sendValues[26]*sendValues[34]/sendValues[80]+sendValues[10];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 5);
			return;
		}
		sendEnterServer(conn, create, sendValues, 5);
	}

	public static void handle_SERVER_HAND_CLIENT_6_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_6_REQ))) {
			noFindReq(conn, message, 6);
			return;
		}
		SERVER_HAND_CLIENT_6_REQ req = (SERVER_HAND_CLIENT_6_REQ)message_req;
		SERVER_HAND_CLIENT_6_RES res = (SERVER_HAND_CLIENT_6_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[24]/sendValues[13]+sendValues[26]*sendValues[93]/sendValues[45]/sendValues[88]-sendValues[51]*sendValues[47]*sendValues[53]/sendValues[0];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 6);
			return;
		}
		sendEnterServer(conn, create, sendValues, 6);
	}

	public static void handle_SERVER_HAND_CLIENT_7_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_7_REQ))) {
			noFindReq(conn, message, 7);
			return;
		}
		SERVER_HAND_CLIENT_7_REQ req = (SERVER_HAND_CLIENT_7_REQ)message_req;
		SERVER_HAND_CLIENT_7_RES res = (SERVER_HAND_CLIENT_7_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[40]+sendValues[62]+sendValues[82]-sendValues[33]+sendValues[57]*sendValues[32]+sendValues[67]/sendValues[79]*sendValues[77]-sendValues[59];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 7);
			return;
		}
		sendEnterServer(conn, create, sendValues, 7);
	}

	public static void handle_SERVER_HAND_CLIENT_8_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_8_REQ))) {
			noFindReq(conn, message, 8);
			return;
		}
		SERVER_HAND_CLIENT_8_REQ req = (SERVER_HAND_CLIENT_8_REQ)message_req;
		SERVER_HAND_CLIENT_8_RES res = (SERVER_HAND_CLIENT_8_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[0]*sendValues[98]+sendValues[53]/sendValues[13]-sendValues[35]/sendValues[97]-sendValues[93]*sendValues[56]*sendValues[19]-sendValues[78];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 8);
			return;
		}
		sendEnterServer(conn, create, sendValues, 8);
	}

	public static void handle_SERVER_HAND_CLIENT_9_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_9_REQ))) {
			noFindReq(conn, message, 9);
			return;
		}
		SERVER_HAND_CLIENT_9_REQ req = (SERVER_HAND_CLIENT_9_REQ)message_req;
		SERVER_HAND_CLIENT_9_RES res = (SERVER_HAND_CLIENT_9_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[60]*sendValues[7]-sendValues[33]+sendValues[78]*sendValues[83]/sendValues[39]-sendValues[37]/sendValues[50]-sendValues[24]-sendValues[9];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 9);
			return;
		}
		sendEnterServer(conn, create, sendValues, 9);
	}

	public static void handle_SERVER_HAND_CLIENT_10_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_10_REQ))) {
			noFindReq(conn, message, 10);
			return;
		}
		SERVER_HAND_CLIENT_10_REQ req = (SERVER_HAND_CLIENT_10_REQ)message_req;
		SERVER_HAND_CLIENT_10_RES res = (SERVER_HAND_CLIENT_10_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[35]-sendValues[79]/sendValues[24]/sendValues[30]/sendValues[66]-sendValues[4]+sendValues[18]*sendValues[69]*sendValues[16]-sendValues[32];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 10);
			return;
		}
		sendEnterServer(conn, create, sendValues, 10);
	}

	public static void handle_SERVER_HAND_CLIENT_11_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_11_REQ))) {
			noFindReq(conn, message, 11);
			return;
		}
		SERVER_HAND_CLIENT_11_REQ req = (SERVER_HAND_CLIENT_11_REQ)message_req;
		SERVER_HAND_CLIENT_11_RES res = (SERVER_HAND_CLIENT_11_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[1]+sendValues[52]-sendValues[98]*sendValues[3]*sendValues[54]-sendValues[88]/sendValues[91]/sendValues[8]-sendValues[44]*sendValues[61];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 11);
			return;
		}
		sendEnterServer(conn, create, sendValues, 11);
	}

	public static void handle_SERVER_HAND_CLIENT_12_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_12_REQ))) {
			noFindReq(conn, message, 12);
			return;
		}
		SERVER_HAND_CLIENT_12_REQ req = (SERVER_HAND_CLIENT_12_REQ)message_req;
		SERVER_HAND_CLIENT_12_RES res = (SERVER_HAND_CLIENT_12_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[98]+sendValues[74]-sendValues[94]+sendValues[87]/sendValues[20]-sendValues[78]/sendValues[90]*sendValues[29]/sendValues[56]*sendValues[95];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 12);
			return;
		}
		sendEnterServer(conn, create, sendValues, 12);
	}

	public static void handle_SERVER_HAND_CLIENT_13_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_13_REQ))) {
			noFindReq(conn, message, 13);
			return;
		}
		SERVER_HAND_CLIENT_13_REQ req = (SERVER_HAND_CLIENT_13_REQ)message_req;
		SERVER_HAND_CLIENT_13_RES res = (SERVER_HAND_CLIENT_13_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[37]-sendValues[50]/sendValues[54]*sendValues[7]/sendValues[17]*sendValues[96]/sendValues[86]*sendValues[94]-sendValues[38]/sendValues[6];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 13);
			return;
		}
		sendEnterServer(conn, create, sendValues, 13);
	}

	public static void handle_SERVER_HAND_CLIENT_14_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_14_REQ))) {
			noFindReq(conn, message, 14);
			return;
		}
		SERVER_HAND_CLIENT_14_REQ req = (SERVER_HAND_CLIENT_14_REQ)message_req;
		SERVER_HAND_CLIENT_14_RES res = (SERVER_HAND_CLIENT_14_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]*sendValues[39]/sendValues[21]*sendValues[68]+sendValues[32]+sendValues[8]/sendValues[91]/sendValues[23]+sendValues[51]*sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 14);
			return;
		}
		sendEnterServer(conn, create, sendValues, 14);
	}

	public static void handle_SERVER_HAND_CLIENT_15_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_15_REQ))) {
			noFindReq(conn, message, 15);
			return;
		}
		SERVER_HAND_CLIENT_15_REQ req = (SERVER_HAND_CLIENT_15_REQ)message_req;
		SERVER_HAND_CLIENT_15_RES res = (SERVER_HAND_CLIENT_15_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[53]*sendValues[74]/sendValues[70]-sendValues[12]/sendValues[93]*sendValues[27]/sendValues[57]-sendValues[96]+sendValues[9]+sendValues[71];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 15);
			return;
		}
		sendEnterServer(conn, create, sendValues, 15);
	}

	public static void handle_SERVER_HAND_CLIENT_16_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_16_REQ))) {
			noFindReq(conn, message, 16);
			return;
		}
		SERVER_HAND_CLIENT_16_REQ req = (SERVER_HAND_CLIENT_16_REQ)message_req;
		SERVER_HAND_CLIENT_16_RES res = (SERVER_HAND_CLIENT_16_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[94]*sendValues[35]*sendValues[23]-sendValues[67]+sendValues[37]*sendValues[58]*sendValues[7]+sendValues[15]+sendValues[97]+sendValues[77];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 16);
			return;
		}
		sendEnterServer(conn, create, sendValues, 16);
	}

	public static void handle_SERVER_HAND_CLIENT_17_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_17_REQ))) {
			noFindReq(conn, message, 17);
			return;
		}
		SERVER_HAND_CLIENT_17_REQ req = (SERVER_HAND_CLIENT_17_REQ)message_req;
		SERVER_HAND_CLIENT_17_RES res = (SERVER_HAND_CLIENT_17_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[80]/sendValues[66]-sendValues[20]-sendValues[55]/sendValues[41]+sendValues[79]*sendValues[18]/sendValues[25]/sendValues[43]+sendValues[59];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 17);
			return;
		}
		sendEnterServer(conn, create, sendValues, 17);
	}

	public static void handle_SERVER_HAND_CLIENT_18_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_18_REQ))) {
			noFindReq(conn, message, 18);
			return;
		}
		SERVER_HAND_CLIENT_18_REQ req = (SERVER_HAND_CLIENT_18_REQ)message_req;
		SERVER_HAND_CLIENT_18_RES res = (SERVER_HAND_CLIENT_18_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[61]-sendValues[20]*sendValues[34]/sendValues[76]-sendValues[68]-sendValues[73]*sendValues[58]/sendValues[65]*sendValues[16]+sendValues[93];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 18);
			return;
		}
		sendEnterServer(conn, create, sendValues, 18);
	}

	public static void handle_SERVER_HAND_CLIENT_19_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_19_REQ))) {
			noFindReq(conn, message, 19);
			return;
		}
		SERVER_HAND_CLIENT_19_REQ req = (SERVER_HAND_CLIENT_19_REQ)message_req;
		SERVER_HAND_CLIENT_19_RES res = (SERVER_HAND_CLIENT_19_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[71]-sendValues[99]+sendValues[95]+sendValues[33]+sendValues[16]-sendValues[6]+sendValues[93]-sendValues[35]-sendValues[31]+sendValues[86];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 19);
			return;
		}
		sendEnterServer(conn, create, sendValues, 19);
	}

	public static void handle_SERVER_HAND_CLIENT_20_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_20_REQ))) {
			noFindReq(conn, message, 20);
			return;
		}
		SERVER_HAND_CLIENT_20_REQ req = (SERVER_HAND_CLIENT_20_REQ)message_req;
		SERVER_HAND_CLIENT_20_RES res = (SERVER_HAND_CLIENT_20_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[73]*sendValues[10]*sendValues[22]*sendValues[12]*sendValues[40]-sendValues[33]-sendValues[83]+sendValues[5]+sendValues[41]-sendValues[38];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 20);
			return;
		}
		sendEnterServer(conn, create, sendValues, 20);
	}

	public static void handle_SERVER_HAND_CLIENT_21_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_21_REQ))) {
			noFindReq(conn, message, 21);
			return;
		}
		SERVER_HAND_CLIENT_21_REQ req = (SERVER_HAND_CLIENT_21_REQ)message_req;
		SERVER_HAND_CLIENT_21_RES res = (SERVER_HAND_CLIENT_21_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[28]-sendValues[44]+sendValues[81]+sendValues[97]*sendValues[51]*sendValues[95]*sendValues[52]+sendValues[89]-sendValues[57]+sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 21);
			return;
		}
		sendEnterServer(conn, create, sendValues, 21);
	}

	public static void handle_SERVER_HAND_CLIENT_22_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_22_REQ))) {
			noFindReq(conn, message, 22);
			return;
		}
		SERVER_HAND_CLIENT_22_REQ req = (SERVER_HAND_CLIENT_22_REQ)message_req;
		SERVER_HAND_CLIENT_22_RES res = (SERVER_HAND_CLIENT_22_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[65]*sendValues[70]/sendValues[98]/sendValues[36]*sendValues[90]*sendValues[66]*sendValues[77]-sendValues[50]*sendValues[68]+sendValues[45];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 22);
			return;
		}
		sendEnterServer(conn, create, sendValues, 22);
	}

	public static void handle_SERVER_HAND_CLIENT_23_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_23_REQ))) {
			noFindReq(conn, message, 23);
			return;
		}
		SERVER_HAND_CLIENT_23_REQ req = (SERVER_HAND_CLIENT_23_REQ)message_req;
		SERVER_HAND_CLIENT_23_RES res = (SERVER_HAND_CLIENT_23_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[88]/sendValues[28]-sendValues[41]+sendValues[51]-sendValues[31]+sendValues[42]*sendValues[24]+sendValues[82]/sendValues[78]*sendValues[77];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 23);
			return;
		}
		sendEnterServer(conn, create, sendValues, 23);
	}

	public static void handle_SERVER_HAND_CLIENT_24_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_24_REQ))) {
			noFindReq(conn, message, 24);
			return;
		}
		SERVER_HAND_CLIENT_24_REQ req = (SERVER_HAND_CLIENT_24_REQ)message_req;
		SERVER_HAND_CLIENT_24_RES res = (SERVER_HAND_CLIENT_24_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[45]/sendValues[52]+sendValues[94]-sendValues[81]*sendValues[10]/sendValues[47]-sendValues[76]*sendValues[22]-sendValues[90]*sendValues[16];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 24);
			return;
		}
		sendEnterServer(conn, create, sendValues, 24);
	}

	public static void handle_SERVER_HAND_CLIENT_25_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_25_REQ))) {
			noFindReq(conn, message, 25);
			return;
		}
		SERVER_HAND_CLIENT_25_REQ req = (SERVER_HAND_CLIENT_25_REQ)message_req;
		SERVER_HAND_CLIENT_25_RES res = (SERVER_HAND_CLIENT_25_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[85]*sendValues[59]-sendValues[95]+sendValues[86]-sendValues[50]-sendValues[99]-sendValues[3]/sendValues[28]*sendValues[63]/sendValues[43];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 25);
			return;
		}
		sendEnterServer(conn, create, sendValues, 25);
	}

	public static void handle_SERVER_HAND_CLIENT_26_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_26_REQ))) {
			noFindReq(conn, message, 26);
			return;
		}
		SERVER_HAND_CLIENT_26_REQ req = (SERVER_HAND_CLIENT_26_REQ)message_req;
		SERVER_HAND_CLIENT_26_RES res = (SERVER_HAND_CLIENT_26_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[76]-sendValues[83]*sendValues[65]*sendValues[91]*sendValues[51]-sendValues[29]-sendValues[71]/sendValues[57]+sendValues[38]-sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 26);
			return;
		}
		sendEnterServer(conn, create, sendValues, 26);
	}

	public static void handle_SERVER_HAND_CLIENT_27_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_27_REQ))) {
			noFindReq(conn, message, 27);
			return;
		}
		SERVER_HAND_CLIENT_27_REQ req = (SERVER_HAND_CLIENT_27_REQ)message_req;
		SERVER_HAND_CLIENT_27_RES res = (SERVER_HAND_CLIENT_27_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[36]-sendValues[62]-sendValues[80]/sendValues[94]-sendValues[53]-sendValues[29]-sendValues[30]/sendValues[4]*sendValues[9]*sendValues[13];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 27);
			return;
		}
		sendEnterServer(conn, create, sendValues, 27);
	}

	public static void handle_SERVER_HAND_CLIENT_28_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_28_REQ))) {
			noFindReq(conn, message, 28);
			return;
		}
		SERVER_HAND_CLIENT_28_REQ req = (SERVER_HAND_CLIENT_28_REQ)message_req;
		SERVER_HAND_CLIENT_28_RES res = (SERVER_HAND_CLIENT_28_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[43]-sendValues[74]*sendValues[66]-sendValues[12]-sendValues[59]+sendValues[41]-sendValues[70]-sendValues[35]*sendValues[92]+sendValues[25];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 28);
			return;
		}
		sendEnterServer(conn, create, sendValues, 28);
	}

	public static void handle_SERVER_HAND_CLIENT_29_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_29_REQ))) {
			noFindReq(conn, message, 29);
			return;
		}
		SERVER_HAND_CLIENT_29_REQ req = (SERVER_HAND_CLIENT_29_REQ)message_req;
		SERVER_HAND_CLIENT_29_RES res = (SERVER_HAND_CLIENT_29_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[84]*sendValues[1]+sendValues[13]-sendValues[49]-sendValues[32]+sendValues[55]+sendValues[0]/sendValues[99]*sendValues[82]+sendValues[8];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 29);
			return;
		}
		sendEnterServer(conn, create, sendValues, 29);
	}

	public static void handle_SERVER_HAND_CLIENT_30_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_30_REQ))) {
			noFindReq(conn, message, 30);
			return;
		}
		SERVER_HAND_CLIENT_30_REQ req = (SERVER_HAND_CLIENT_30_REQ)message_req;
		SERVER_HAND_CLIENT_30_RES res = (SERVER_HAND_CLIENT_30_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[9]*sendValues[7]*sendValues[45]*sendValues[56]/sendValues[41]-sendValues[26]/sendValues[16]/sendValues[68]+sendValues[23]*sendValues[73];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 30);
			return;
		}
		sendEnterServer(conn, create, sendValues, 30);
	}

	public static void handle_SERVER_HAND_CLIENT_31_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_31_REQ))) {
			noFindReq(conn, message, 31);
			return;
		}
		SERVER_HAND_CLIENT_31_REQ req = (SERVER_HAND_CLIENT_31_REQ)message_req;
		SERVER_HAND_CLIENT_31_RES res = (SERVER_HAND_CLIENT_31_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[52]*sendValues[95]+sendValues[35]+sendValues[47]+sendValues[14]-sendValues[81]+sendValues[80]*sendValues[41]/sendValues[20]/sendValues[29];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 31);
			return;
		}
		sendEnterServer(conn, create, sendValues, 31);
	}

	public static void handle_SERVER_HAND_CLIENT_32_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_32_REQ))) {
			noFindReq(conn, message, 32);
			return;
		}
		SERVER_HAND_CLIENT_32_REQ req = (SERVER_HAND_CLIENT_32_REQ)message_req;
		SERVER_HAND_CLIENT_32_RES res = (SERVER_HAND_CLIENT_32_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[13]*sendValues[30]*sendValues[25]*sendValues[92]/sendValues[56]+sendValues[22]*sendValues[80]*sendValues[35]*sendValues[12]-sendValues[61];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 32);
			return;
		}
		sendEnterServer(conn, create, sendValues, 32);
	}

	public static void handle_SERVER_HAND_CLIENT_33_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_33_REQ))) {
			noFindReq(conn, message, 33);
			return;
		}
		SERVER_HAND_CLIENT_33_REQ req = (SERVER_HAND_CLIENT_33_REQ)message_req;
		SERVER_HAND_CLIENT_33_RES res = (SERVER_HAND_CLIENT_33_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[38]-sendValues[51]*sendValues[64]/sendValues[8]+sendValues[90]+sendValues[89]+sendValues[85]*sendValues[78]*sendValues[74]-sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 33);
			return;
		}
		sendEnterServer(conn, create, sendValues, 33);
	}

	public static void handle_SERVER_HAND_CLIENT_34_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_34_REQ))) {
			noFindReq(conn, message, 34);
			return;
		}
		SERVER_HAND_CLIENT_34_REQ req = (SERVER_HAND_CLIENT_34_REQ)message_req;
		SERVER_HAND_CLIENT_34_RES res = (SERVER_HAND_CLIENT_34_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[24]/sendValues[86]/sendValues[48]-sendValues[19]/sendValues[54]+sendValues[69]*sendValues[35]*sendValues[95]+sendValues[51]+sendValues[29];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 34);
			return;
		}
		sendEnterServer(conn, create, sendValues, 34);
	}

	public static void handle_SERVER_HAND_CLIENT_35_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_35_REQ))) {
			noFindReq(conn, message, 35);
			return;
		}
		SERVER_HAND_CLIENT_35_REQ req = (SERVER_HAND_CLIENT_35_REQ)message_req;
		SERVER_HAND_CLIENT_35_RES res = (SERVER_HAND_CLIENT_35_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[70]+sendValues[81]+sendValues[42]+sendValues[29]*sendValues[79]-sendValues[63]/sendValues[76]*sendValues[55]+sendValues[87]+sendValues[46];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 35);
			return;
		}
		sendEnterServer(conn, create, sendValues, 35);
	}

	public static void handle_SERVER_HAND_CLIENT_36_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_36_REQ))) {
			noFindReq(conn, message, 36);
			return;
		}
		SERVER_HAND_CLIENT_36_REQ req = (SERVER_HAND_CLIENT_36_REQ)message_req;
		SERVER_HAND_CLIENT_36_RES res = (SERVER_HAND_CLIENT_36_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[97]-sendValues[19]+sendValues[24]/sendValues[13]-sendValues[69]-sendValues[2]-sendValues[30]+sendValues[37]-sendValues[83]-sendValues[20];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 36);
			return;
		}
		sendEnterServer(conn, create, sendValues, 36);
	}

	public static void handle_SERVER_HAND_CLIENT_37_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_37_REQ))) {
			noFindReq(conn, message, 37);
			return;
		}
		SERVER_HAND_CLIENT_37_REQ req = (SERVER_HAND_CLIENT_37_REQ)message_req;
		SERVER_HAND_CLIENT_37_RES res = (SERVER_HAND_CLIENT_37_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[61]+sendValues[34]*sendValues[82]*sendValues[83]/sendValues[99]+sendValues[63]*sendValues[70]/sendValues[11]+sendValues[73]-sendValues[47];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 37);
			return;
		}
		sendEnterServer(conn, create, sendValues, 37);
	}

	public static void handle_SERVER_HAND_CLIENT_38_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_38_REQ))) {
			noFindReq(conn, message, 38);
			return;
		}
		SERVER_HAND_CLIENT_38_REQ req = (SERVER_HAND_CLIENT_38_REQ)message_req;
		SERVER_HAND_CLIENT_38_RES res = (SERVER_HAND_CLIENT_38_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[22]/sendValues[99]*sendValues[90]*sendValues[58]+sendValues[89]*sendValues[34]-sendValues[44]+sendValues[14]/sendValues[71]+sendValues[85];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 38);
			return;
		}
		sendEnterServer(conn, create, sendValues, 38);
	}

	public static void handle_SERVER_HAND_CLIENT_39_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_39_REQ))) {
			noFindReq(conn, message, 39);
			return;
		}
		SERVER_HAND_CLIENT_39_REQ req = (SERVER_HAND_CLIENT_39_REQ)message_req;
		SERVER_HAND_CLIENT_39_RES res = (SERVER_HAND_CLIENT_39_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[12]+sendValues[46]*sendValues[99]*sendValues[42]+sendValues[76]/sendValues[50]-sendValues[58]+sendValues[41]+sendValues[8]/sendValues[69];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 39);
			return;
		}
		sendEnterServer(conn, create, sendValues, 39);
	}

	public static void handle_SERVER_HAND_CLIENT_40_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_40_REQ))) {
			noFindReq(conn, message, 40);
			return;
		}
		SERVER_HAND_CLIENT_40_REQ req = (SERVER_HAND_CLIENT_40_REQ)message_req;
		SERVER_HAND_CLIENT_40_RES res = (SERVER_HAND_CLIENT_40_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[26]/sendValues[28]+sendValues[90]+sendValues[88]+sendValues[10]-sendValues[94]*sendValues[97]*sendValues[98]/sendValues[55]*sendValues[23];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 40);
			return;
		}
		sendEnterServer(conn, create, sendValues, 40);
	}

	public static void handle_SERVER_HAND_CLIENT_41_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_41_REQ))) {
			noFindReq(conn, message, 41);
			return;
		}
		SERVER_HAND_CLIENT_41_REQ req = (SERVER_HAND_CLIENT_41_REQ)message_req;
		SERVER_HAND_CLIENT_41_RES res = (SERVER_HAND_CLIENT_41_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[14]*sendValues[7]*sendValues[12]*sendValues[25]/sendValues[74]+sendValues[96]/sendValues[38]-sendValues[2]-sendValues[9]+sendValues[18];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 41);
			return;
		}
		sendEnterServer(conn, create, sendValues, 41);
	}

	public static void handle_SERVER_HAND_CLIENT_42_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_42_REQ))) {
			noFindReq(conn, message, 42);
			return;
		}
		SERVER_HAND_CLIENT_42_REQ req = (SERVER_HAND_CLIENT_42_REQ)message_req;
		SERVER_HAND_CLIENT_42_RES res = (SERVER_HAND_CLIENT_42_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[79]+sendValues[86]-sendValues[45]-sendValues[97]+sendValues[57]/sendValues[34]/sendValues[48]+sendValues[74]/sendValues[85]/sendValues[21];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 42);
			return;
		}
		sendEnterServer(conn, create, sendValues, 42);
	}

	public static void handle_SERVER_HAND_CLIENT_43_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_43_REQ))) {
			noFindReq(conn, message, 43);
			return;
		}
		SERVER_HAND_CLIENT_43_REQ req = (SERVER_HAND_CLIENT_43_REQ)message_req;
		SERVER_HAND_CLIENT_43_RES res = (SERVER_HAND_CLIENT_43_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[86]*sendValues[88]-sendValues[24]/sendValues[94]+sendValues[66]*sendValues[32]-sendValues[80]+sendValues[3]*sendValues[65]/sendValues[22];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 43);
			return;
		}
		sendEnterServer(conn, create, sendValues, 43);
	}

	public static void handle_SERVER_HAND_CLIENT_44_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_44_REQ))) {
			noFindReq(conn, message, 44);
			return;
		}
		SERVER_HAND_CLIENT_44_REQ req = (SERVER_HAND_CLIENT_44_REQ)message_req;
		SERVER_HAND_CLIENT_44_RES res = (SERVER_HAND_CLIENT_44_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[62]-sendValues[18]+sendValues[68]/sendValues[50]-sendValues[34]+sendValues[85]-sendValues[56]+sendValues[39]/sendValues[94]/sendValues[58];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 44);
			return;
		}
		sendEnterServer(conn, create, sendValues, 44);
	}

	public static void handle_SERVER_HAND_CLIENT_45_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_45_REQ))) {
			noFindReq(conn, message, 45);
			return;
		}
		SERVER_HAND_CLIENT_45_REQ req = (SERVER_HAND_CLIENT_45_REQ)message_req;
		SERVER_HAND_CLIENT_45_RES res = (SERVER_HAND_CLIENT_45_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[31]/sendValues[93]/sendValues[17]*sendValues[7]-sendValues[91]*sendValues[15]+sendValues[41]-sendValues[9]/sendValues[36]*sendValues[76];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 45);
			return;
		}
		sendEnterServer(conn, create, sendValues, 45);
	}

	public static void handle_SERVER_HAND_CLIENT_46_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_46_REQ))) {
			noFindReq(conn, message, 46);
			return;
		}
		SERVER_HAND_CLIENT_46_REQ req = (SERVER_HAND_CLIENT_46_REQ)message_req;
		SERVER_HAND_CLIENT_46_RES res = (SERVER_HAND_CLIENT_46_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[83]*sendValues[72]+sendValues[23]*sendValues[13]+sendValues[21]*sendValues[42]*sendValues[81]-sendValues[14]/sendValues[32]/sendValues[91];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 46);
			return;
		}
		sendEnterServer(conn, create, sendValues, 46);
	}

	public static void handle_SERVER_HAND_CLIENT_47_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_47_REQ))) {
			noFindReq(conn, message, 47);
			return;
		}
		SERVER_HAND_CLIENT_47_REQ req = (SERVER_HAND_CLIENT_47_REQ)message_req;
		SERVER_HAND_CLIENT_47_RES res = (SERVER_HAND_CLIENT_47_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[82]+sendValues[17]/sendValues[96]*sendValues[80]-sendValues[35]*sendValues[38]/sendValues[30]+sendValues[44]+sendValues[11]*sendValues[49];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 47);
			return;
		}
		sendEnterServer(conn, create, sendValues, 47);
	}

	public static void handle_SERVER_HAND_CLIENT_48_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_48_REQ))) {
			noFindReq(conn, message, 48);
			return;
		}
		SERVER_HAND_CLIENT_48_REQ req = (SERVER_HAND_CLIENT_48_REQ)message_req;
		SERVER_HAND_CLIENT_48_RES res = (SERVER_HAND_CLIENT_48_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[56]/sendValues[78]/sendValues[17]*sendValues[18]-sendValues[9]-sendValues[25]-sendValues[65]+sendValues[26]/sendValues[40]*sendValues[95];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 48);
			return;
		}
		sendEnterServer(conn, create, sendValues, 48);
	}

	public static void handle_SERVER_HAND_CLIENT_49_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_49_REQ))) {
			noFindReq(conn, message, 49);
			return;
		}
		SERVER_HAND_CLIENT_49_REQ req = (SERVER_HAND_CLIENT_49_REQ)message_req;
		SERVER_HAND_CLIENT_49_RES res = (SERVER_HAND_CLIENT_49_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]*sendValues[3]-sendValues[29]/sendValues[9]/sendValues[34]*sendValues[46]/sendValues[42]+sendValues[95]+sendValues[35]/sendValues[96];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 49);
			return;
		}
		sendEnterServer(conn, create, sendValues, 49);
	}

	public static void handle_SERVER_HAND_CLIENT_50_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_50_REQ))) {
			noFindReq(conn, message, 50);
			return;
		}
		SERVER_HAND_CLIENT_50_REQ req = (SERVER_HAND_CLIENT_50_REQ)message_req;
		SERVER_HAND_CLIENT_50_RES res = (SERVER_HAND_CLIENT_50_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[10]*sendValues[84]*sendValues[74]*sendValues[89]+sendValues[19]-sendValues[85]-sendValues[1]+sendValues[62]-sendValues[58]*sendValues[57];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 50);
			return;
		}
		sendEnterServer(conn, create, sendValues, 50);
	}

	public static void handle_SERVER_HAND_CLIENT_51_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_51_REQ))) {
			noFindReq(conn, message, 51);
			return;
		}
		SERVER_HAND_CLIENT_51_REQ req = (SERVER_HAND_CLIENT_51_REQ)message_req;
		SERVER_HAND_CLIENT_51_RES res = (SERVER_HAND_CLIENT_51_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[33]*sendValues[35]-sendValues[8]*sendValues[74]-sendValues[73]+sendValues[61]/sendValues[37]/sendValues[68]-sendValues[69]/sendValues[16];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 51);
			return;
		}
		sendEnterServer(conn, create, sendValues, 51);
	}

	public static void handle_SERVER_HAND_CLIENT_52_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_52_REQ))) {
			noFindReq(conn, message, 52);
			return;
		}
		SERVER_HAND_CLIENT_52_REQ req = (SERVER_HAND_CLIENT_52_REQ)message_req;
		SERVER_HAND_CLIENT_52_RES res = (SERVER_HAND_CLIENT_52_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[48]-sendValues[70]/sendValues[53]*sendValues[57]-sendValues[96]-sendValues[44]+sendValues[10]*sendValues[47]*sendValues[60]-sendValues[40];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 52);
			return;
		}
		sendEnterServer(conn, create, sendValues, 52);
	}

	public static void handle_SERVER_HAND_CLIENT_53_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_53_REQ))) {
			noFindReq(conn, message, 53);
			return;
		}
		SERVER_HAND_CLIENT_53_REQ req = (SERVER_HAND_CLIENT_53_REQ)message_req;
		SERVER_HAND_CLIENT_53_RES res = (SERVER_HAND_CLIENT_53_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[77]-sendValues[34]+sendValues[84]*sendValues[21]-sendValues[4]-sendValues[19]/sendValues[30]*sendValues[65]*sendValues[62]+sendValues[27];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 53);
			return;
		}
		sendEnterServer(conn, create, sendValues, 53);
	}

	public static void handle_SERVER_HAND_CLIENT_54_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_54_REQ))) {
			noFindReq(conn, message, 54);
			return;
		}
		SERVER_HAND_CLIENT_54_REQ req = (SERVER_HAND_CLIENT_54_REQ)message_req;
		SERVER_HAND_CLIENT_54_RES res = (SERVER_HAND_CLIENT_54_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[76]/sendValues[33]*sendValues[22]+sendValues[93]+sendValues[11]/sendValues[36]*sendValues[3]*sendValues[58]-sendValues[96]-sendValues[40];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 54);
			return;
		}
		sendEnterServer(conn, create, sendValues, 54);
	}

	public static void handle_SERVER_HAND_CLIENT_55_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_55_REQ))) {
			noFindReq(conn, message, 55);
			return;
		}
		SERVER_HAND_CLIENT_55_REQ req = (SERVER_HAND_CLIENT_55_REQ)message_req;
		SERVER_HAND_CLIENT_55_RES res = (SERVER_HAND_CLIENT_55_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[20]*sendValues[53]-sendValues[47]*sendValues[68]+sendValues[83]-sendValues[80]-sendValues[30]*sendValues[85]-sendValues[86]/sendValues[21];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 55);
			return;
		}
		sendEnterServer(conn, create, sendValues, 55);
	}

	public static void handle_SERVER_HAND_CLIENT_56_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_56_REQ))) {
			noFindReq(conn, message, 56);
			return;
		}
		SERVER_HAND_CLIENT_56_REQ req = (SERVER_HAND_CLIENT_56_REQ)message_req;
		SERVER_HAND_CLIENT_56_RES res = (SERVER_HAND_CLIENT_56_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[83]*sendValues[73]*sendValues[45]-sendValues[55]-sendValues[14]+sendValues[61]+sendValues[96]+sendValues[24]/sendValues[34]*sendValues[79];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 56);
			return;
		}
		sendEnterServer(conn, create, sendValues, 56);
	}

	public static void handle_SERVER_HAND_CLIENT_57_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_57_REQ))) {
			noFindReq(conn, message, 57);
			return;
		}
		SERVER_HAND_CLIENT_57_REQ req = (SERVER_HAND_CLIENT_57_REQ)message_req;
		SERVER_HAND_CLIENT_57_RES res = (SERVER_HAND_CLIENT_57_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[44]+sendValues[93]/sendValues[2]*sendValues[76]+sendValues[74]/sendValues[4]-sendValues[38]+sendValues[65]/sendValues[85]/sendValues[18];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 57);
			return;
		}
		sendEnterServer(conn, create, sendValues, 57);
	}

	public static void handle_SERVER_HAND_CLIENT_58_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_58_REQ))) {
			noFindReq(conn, message, 58);
			return;
		}
		SERVER_HAND_CLIENT_58_REQ req = (SERVER_HAND_CLIENT_58_REQ)message_req;
		SERVER_HAND_CLIENT_58_RES res = (SERVER_HAND_CLIENT_58_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[38]-sendValues[89]*sendValues[92]-sendValues[48]+sendValues[29]+sendValues[51]+sendValues[90]*sendValues[30]/sendValues[3]-sendValues[11];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 58);
			return;
		}
		sendEnterServer(conn, create, sendValues, 58);
	}

	public static void handle_SERVER_HAND_CLIENT_59_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_59_REQ))) {
			noFindReq(conn, message, 59);
			return;
		}
		SERVER_HAND_CLIENT_59_REQ req = (SERVER_HAND_CLIENT_59_REQ)message_req;
		SERVER_HAND_CLIENT_59_RES res = (SERVER_HAND_CLIENT_59_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[17]-sendValues[61]/sendValues[50]*sendValues[85]-sendValues[9]+sendValues[49]*sendValues[88]*sendValues[39]*sendValues[51]*sendValues[8];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 59);
			return;
		}
		sendEnterServer(conn, create, sendValues, 59);
	}

	public static void handle_SERVER_HAND_CLIENT_60_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_60_REQ))) {
			noFindReq(conn, message, 60);
			return;
		}
		SERVER_HAND_CLIENT_60_REQ req = (SERVER_HAND_CLIENT_60_REQ)message_req;
		SERVER_HAND_CLIENT_60_RES res = (SERVER_HAND_CLIENT_60_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[12]-sendValues[17]/sendValues[76]+sendValues[16]-sendValues[22]-sendValues[97]+sendValues[36]+sendValues[26]*sendValues[56]-sendValues[42];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 60);
			return;
		}
		sendEnterServer(conn, create, sendValues, 60);
	}

	public static void handle_SERVER_HAND_CLIENT_61_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_61_REQ))) {
			noFindReq(conn, message, 61);
			return;
		}
		SERVER_HAND_CLIENT_61_REQ req = (SERVER_HAND_CLIENT_61_REQ)message_req;
		SERVER_HAND_CLIENT_61_RES res = (SERVER_HAND_CLIENT_61_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[96]-sendValues[34]+sendValues[9]*sendValues[11]*sendValues[76]/sendValues[56]-sendValues[19]-sendValues[79]-sendValues[94]-sendValues[97];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 61);
			return;
		}
		sendEnterServer(conn, create, sendValues, 61);
	}

	public static void handle_SERVER_HAND_CLIENT_62_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_62_REQ))) {
			noFindReq(conn, message, 62);
			return;
		}
		SERVER_HAND_CLIENT_62_REQ req = (SERVER_HAND_CLIENT_62_REQ)message_req;
		SERVER_HAND_CLIENT_62_RES res = (SERVER_HAND_CLIENT_62_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[44]*sendValues[18]*sendValues[76]*sendValues[53]-sendValues[65]/sendValues[93]+sendValues[34]+sendValues[50]+sendValues[48]-sendValues[7];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 62);
			return;
		}
		sendEnterServer(conn, create, sendValues, 62);
	}

	public static void handle_SERVER_HAND_CLIENT_63_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_63_REQ))) {
			noFindReq(conn, message, 63);
			return;
		}
		SERVER_HAND_CLIENT_63_REQ req = (SERVER_HAND_CLIENT_63_REQ)message_req;
		SERVER_HAND_CLIENT_63_RES res = (SERVER_HAND_CLIENT_63_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[71]*sendValues[92]/sendValues[46]*sendValues[97]-sendValues[30]+sendValues[1]*sendValues[64]+sendValues[4]/sendValues[38]*sendValues[73];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 63);
			return;
		}
		sendEnterServer(conn, create, sendValues, 63);
	}

	public static void handle_SERVER_HAND_CLIENT_64_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_64_REQ))) {
			noFindReq(conn, message, 64);
			return;
		}
		SERVER_HAND_CLIENT_64_REQ req = (SERVER_HAND_CLIENT_64_REQ)message_req;
		SERVER_HAND_CLIENT_64_RES res = (SERVER_HAND_CLIENT_64_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[78]+sendValues[11]*sendValues[61]*sendValues[56]/sendValues[23]+sendValues[29]-sendValues[7]*sendValues[2]-sendValues[49]+sendValues[88];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 64);
			return;
		}
		sendEnterServer(conn, create, sendValues, 64);
	}

	public static void handle_SERVER_HAND_CLIENT_65_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_65_REQ))) {
			noFindReq(conn, message, 65);
			return;
		}
		SERVER_HAND_CLIENT_65_REQ req = (SERVER_HAND_CLIENT_65_REQ)message_req;
		SERVER_HAND_CLIENT_65_RES res = (SERVER_HAND_CLIENT_65_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[43]/sendValues[31]-sendValues[44]*sendValues[94]+sendValues[49]+sendValues[42]-sendValues[85]/sendValues[27]/sendValues[61]*sendValues[26];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 65);
			return;
		}
		sendEnterServer(conn, create, sendValues, 65);
	}

	public static void handle_SERVER_HAND_CLIENT_66_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_66_REQ))) {
			noFindReq(conn, message, 66);
			return;
		}
		SERVER_HAND_CLIENT_66_REQ req = (SERVER_HAND_CLIENT_66_REQ)message_req;
		SERVER_HAND_CLIENT_66_RES res = (SERVER_HAND_CLIENT_66_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[13]/sendValues[26]*sendValues[69]-sendValues[90]+sendValues[27]*sendValues[28]-sendValues[15]-sendValues[10]*sendValues[62]-sendValues[4];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 66);
			return;
		}
		sendEnterServer(conn, create, sendValues, 66);
	}

	public static void handle_SERVER_HAND_CLIENT_67_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_67_REQ))) {
			noFindReq(conn, message, 67);
			return;
		}
		SERVER_HAND_CLIENT_67_REQ req = (SERVER_HAND_CLIENT_67_REQ)message_req;
		SERVER_HAND_CLIENT_67_RES res = (SERVER_HAND_CLIENT_67_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[7]-sendValues[93]-sendValues[70]+sendValues[32]/sendValues[75]-sendValues[76]+sendValues[74]*sendValues[4]/sendValues[82]+sendValues[41];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 67);
			return;
		}
		sendEnterServer(conn, create, sendValues, 67);
	}

	public static void handle_SERVER_HAND_CLIENT_68_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_68_REQ))) {
			noFindReq(conn, message, 68);
			return;
		}
		SERVER_HAND_CLIENT_68_REQ req = (SERVER_HAND_CLIENT_68_REQ)message_req;
		SERVER_HAND_CLIENT_68_RES res = (SERVER_HAND_CLIENT_68_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[94]-sendValues[30]-sendValues[38]*sendValues[59]-sendValues[70]+sendValues[74]-sendValues[28]*sendValues[86]*sendValues[4]+sendValues[56];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 68);
			return;
		}
		sendEnterServer(conn, create, sendValues, 68);
	}

	public static void handle_SERVER_HAND_CLIENT_69_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_69_REQ))) {
			noFindReq(conn, message, 69);
			return;
		}
		SERVER_HAND_CLIENT_69_REQ req = (SERVER_HAND_CLIENT_69_REQ)message_req;
		SERVER_HAND_CLIENT_69_RES res = (SERVER_HAND_CLIENT_69_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[12]*sendValues[23]-sendValues[86]/sendValues[79]/sendValues[72]*sendValues[57]+sendValues[13]*sendValues[37]+sendValues[26]+sendValues[1];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 69);
			return;
		}
		sendEnterServer(conn, create, sendValues, 69);
	}

	public static void handle_SERVER_HAND_CLIENT_70_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_70_REQ))) {
			noFindReq(conn, message, 70);
			return;
		}
		SERVER_HAND_CLIENT_70_REQ req = (SERVER_HAND_CLIENT_70_REQ)message_req;
		SERVER_HAND_CLIENT_70_RES res = (SERVER_HAND_CLIENT_70_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[90]/sendValues[36]*sendValues[87]+sendValues[97]-sendValues[93]*sendValues[70]/sendValues[31]/sendValues[22]*sendValues[69]-sendValues[88];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 70);
			return;
		}
		sendEnterServer(conn, create, sendValues, 70);
	}

	public static void handle_SERVER_HAND_CLIENT_71_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_71_REQ))) {
			noFindReq(conn, message, 71);
			return;
		}
		SERVER_HAND_CLIENT_71_REQ req = (SERVER_HAND_CLIENT_71_REQ)message_req;
		SERVER_HAND_CLIENT_71_RES res = (SERVER_HAND_CLIENT_71_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[11]*sendValues[47]+sendValues[91]-sendValues[55]/sendValues[62]+sendValues[77]/sendValues[93]-sendValues[46]/sendValues[40]*sendValues[7];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 71);
			return;
		}
		sendEnterServer(conn, create, sendValues, 71);
	}

	public static void handle_SERVER_HAND_CLIENT_72_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_72_REQ))) {
			noFindReq(conn, message, 72);
			return;
		}
		SERVER_HAND_CLIENT_72_REQ req = (SERVER_HAND_CLIENT_72_REQ)message_req;
		SERVER_HAND_CLIENT_72_RES res = (SERVER_HAND_CLIENT_72_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[25]-sendValues[68]*sendValues[9]*sendValues[27]*sendValues[81]-sendValues[98]-sendValues[7]+sendValues[75]-sendValues[85]-sendValues[78];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 72);
			return;
		}
		sendEnterServer(conn, create, sendValues, 72);
	}

	public static void handle_SERVER_HAND_CLIENT_73_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_73_REQ))) {
			noFindReq(conn, message, 73);
			return;
		}
		SERVER_HAND_CLIENT_73_REQ req = (SERVER_HAND_CLIENT_73_REQ)message_req;
		SERVER_HAND_CLIENT_73_RES res = (SERVER_HAND_CLIENT_73_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[50]+sendValues[1]-sendValues[33]*sendValues[69]-sendValues[71]-sendValues[0]/sendValues[12]-sendValues[65]-sendValues[67]+sendValues[5];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 73);
			return;
		}
		sendEnterServer(conn, create, sendValues, 73);
	}

	public static void handle_SERVER_HAND_CLIENT_74_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_74_REQ))) {
			noFindReq(conn, message, 74);
			return;
		}
		SERVER_HAND_CLIENT_74_REQ req = (SERVER_HAND_CLIENT_74_REQ)message_req;
		SERVER_HAND_CLIENT_74_RES res = (SERVER_HAND_CLIENT_74_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[93]/sendValues[22]+sendValues[86]/sendValues[6]/sendValues[33]-sendValues[85]/sendValues[95]+sendValues[39]+sendValues[16]/sendValues[87];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 74);
			return;
		}
		sendEnterServer(conn, create, sendValues, 74);
	}

	public static void handle_SERVER_HAND_CLIENT_75_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_75_REQ))) {
			noFindReq(conn, message, 75);
			return;
		}
		SERVER_HAND_CLIENT_75_REQ req = (SERVER_HAND_CLIENT_75_REQ)message_req;
		SERVER_HAND_CLIENT_75_RES res = (SERVER_HAND_CLIENT_75_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[63]/sendValues[28]*sendValues[12]*sendValues[52]/sendValues[47]+sendValues[38]-sendValues[59]+sendValues[33]/sendValues[35]-sendValues[92];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 75);
			return;
		}
		sendEnterServer(conn, create, sendValues, 75);
	}

	public static void handle_SERVER_HAND_CLIENT_76_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_76_REQ))) {
			noFindReq(conn, message, 76);
			return;
		}
		SERVER_HAND_CLIENT_76_REQ req = (SERVER_HAND_CLIENT_76_REQ)message_req;
		SERVER_HAND_CLIENT_76_RES res = (SERVER_HAND_CLIENT_76_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[84]-sendValues[47]/sendValues[36]+sendValues[24]-sendValues[20]*sendValues[26]-sendValues[45]*sendValues[42]-sendValues[48]*sendValues[78];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 76);
			return;
		}
		sendEnterServer(conn, create, sendValues, 76);
	}

	public static void handle_SERVER_HAND_CLIENT_77_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_77_REQ))) {
			noFindReq(conn, message, 77);
			return;
		}
		SERVER_HAND_CLIENT_77_REQ req = (SERVER_HAND_CLIENT_77_REQ)message_req;
		SERVER_HAND_CLIENT_77_RES res = (SERVER_HAND_CLIENT_77_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[63]*sendValues[11]+sendValues[65]*sendValues[47]-sendValues[77]+sendValues[0]+sendValues[99]*sendValues[82]/sendValues[14]*sendValues[16];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 77);
			return;
		}
		sendEnterServer(conn, create, sendValues, 77);
	}

	public static void handle_SERVER_HAND_CLIENT_78_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_78_REQ))) {
			noFindReq(conn, message, 78);
			return;
		}
		SERVER_HAND_CLIENT_78_REQ req = (SERVER_HAND_CLIENT_78_REQ)message_req;
		SERVER_HAND_CLIENT_78_RES res = (SERVER_HAND_CLIENT_78_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[43]*sendValues[2]*sendValues[58]-sendValues[22]-sendValues[54]*sendValues[70]-sendValues[50]+sendValues[41]-sendValues[55]/sendValues[7];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 78);
			return;
		}
		sendEnterServer(conn, create, sendValues, 78);
	}

	public static void handle_SERVER_HAND_CLIENT_79_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_79_REQ))) {
			noFindReq(conn, message, 79);
			return;
		}
		SERVER_HAND_CLIENT_79_REQ req = (SERVER_HAND_CLIENT_79_REQ)message_req;
		SERVER_HAND_CLIENT_79_RES res = (SERVER_HAND_CLIENT_79_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[0]+sendValues[11]+sendValues[25]+sendValues[10]+sendValues[96]+sendValues[38]+sendValues[85]-sendValues[54]+sendValues[33]-sendValues[56];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 79);
			return;
		}
		sendEnterServer(conn, create, sendValues, 79);
	}

	public static void handle_SERVER_HAND_CLIENT_80_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_80_REQ))) {
			noFindReq(conn, message, 80);
			return;
		}
		SERVER_HAND_CLIENT_80_REQ req = (SERVER_HAND_CLIENT_80_REQ)message_req;
		SERVER_HAND_CLIENT_80_RES res = (SERVER_HAND_CLIENT_80_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[59]+sendValues[16]-sendValues[92]-sendValues[82]*sendValues[66]+sendValues[11]/sendValues[97]/sendValues[42]/sendValues[58]*sendValues[28];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 80);
			return;
		}
		sendEnterServer(conn, create, sendValues, 80);
	}

	public static void handle_SERVER_HAND_CLIENT_81_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_81_REQ))) {
			noFindReq(conn, message, 81);
			return;
		}
		SERVER_HAND_CLIENT_81_REQ req = (SERVER_HAND_CLIENT_81_REQ)message_req;
		SERVER_HAND_CLIENT_81_RES res = (SERVER_HAND_CLIENT_81_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[99]+sendValues[80]-sendValues[16]-sendValues[75]-sendValues[60]/sendValues[97]+sendValues[66]/sendValues[23]+sendValues[20]+sendValues[24];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 81);
			return;
		}
		sendEnterServer(conn, create, sendValues, 81);
	}

	public static void handle_SERVER_HAND_CLIENT_82_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_82_REQ))) {
			noFindReq(conn, message, 82);
			return;
		}
		SERVER_HAND_CLIENT_82_REQ req = (SERVER_HAND_CLIENT_82_REQ)message_req;
		SERVER_HAND_CLIENT_82_RES res = (SERVER_HAND_CLIENT_82_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[21]*sendValues[39]/sendValues[64]+sendValues[67]/sendValues[98]*sendValues[80]-sendValues[29]-sendValues[44]*sendValues[18]+sendValues[61];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 82);
			return;
		}
		sendEnterServer(conn, create, sendValues, 82);
	}

	public static void handle_SERVER_HAND_CLIENT_83_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_83_REQ))) {
			noFindReq(conn, message, 83);
			return;
		}
		SERVER_HAND_CLIENT_83_REQ req = (SERVER_HAND_CLIENT_83_REQ)message_req;
		SERVER_HAND_CLIENT_83_RES res = (SERVER_HAND_CLIENT_83_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[26]+sendValues[42]*sendValues[58]/sendValues[55]+sendValues[43]/sendValues[17]-sendValues[9]-sendValues[68]/sendValues[16]-sendValues[25];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 83);
			return;
		}
		sendEnterServer(conn, create, sendValues, 83);
	}

	public static void handle_SERVER_HAND_CLIENT_84_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_84_REQ))) {
			noFindReq(conn, message, 84);
			return;
		}
		SERVER_HAND_CLIENT_84_REQ req = (SERVER_HAND_CLIENT_84_REQ)message_req;
		SERVER_HAND_CLIENT_84_RES res = (SERVER_HAND_CLIENT_84_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[66]*sendValues[20]*sendValues[55]-sendValues[3]+sendValues[43]/sendValues[18]*sendValues[10]+sendValues[89]/sendValues[34]-sendValues[49];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 84);
			return;
		}
		sendEnterServer(conn, create, sendValues, 84);
	}

	public static void handle_SERVER_HAND_CLIENT_85_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_85_REQ))) {
			noFindReq(conn, message, 85);
			return;
		}
		SERVER_HAND_CLIENT_85_REQ req = (SERVER_HAND_CLIENT_85_REQ)message_req;
		SERVER_HAND_CLIENT_85_RES res = (SERVER_HAND_CLIENT_85_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]+sendValues[22]*sendValues[78]+sendValues[20]*sendValues[28]+sendValues[7]*sendValues[0]*sendValues[30]+sendValues[35]*sendValues[40];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 85);
			return;
		}
		sendEnterServer(conn, create, sendValues, 85);
	}

	public static void handle_SERVER_HAND_CLIENT_86_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_86_REQ))) {
			noFindReq(conn, message, 86);
			return;
		}
		SERVER_HAND_CLIENT_86_REQ req = (SERVER_HAND_CLIENT_86_REQ)message_req;
		SERVER_HAND_CLIENT_86_RES res = (SERVER_HAND_CLIENT_86_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]-sendValues[13]*sendValues[8]/sendValues[90]+sendValues[5]-sendValues[64]*sendValues[96]+sendValues[83]/sendValues[95]+sendValues[46];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 86);
			return;
		}
		sendEnterServer(conn, create, sendValues, 86);
	}

	public static void handle_SERVER_HAND_CLIENT_87_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_87_REQ))) {
			noFindReq(conn, message, 87);
			return;
		}
		SERVER_HAND_CLIENT_87_REQ req = (SERVER_HAND_CLIENT_87_REQ)message_req;
		SERVER_HAND_CLIENT_87_RES res = (SERVER_HAND_CLIENT_87_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[70]/sendValues[12]-sendValues[44]*sendValues[75]+sendValues[40]+sendValues[5]/sendValues[59]*sendValues[36]*sendValues[32]/sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 87);
			return;
		}
		sendEnterServer(conn, create, sendValues, 87);
	}

	public static void handle_SERVER_HAND_CLIENT_88_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_88_REQ))) {
			noFindReq(conn, message, 88);
			return;
		}
		SERVER_HAND_CLIENT_88_REQ req = (SERVER_HAND_CLIENT_88_REQ)message_req;
		SERVER_HAND_CLIENT_88_RES res = (SERVER_HAND_CLIENT_88_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[41]-sendValues[97]/sendValues[63]-sendValues[90]-sendValues[68]/sendValues[69]*sendValues[43]+sendValues[33]+sendValues[31]-sendValues[64];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 88);
			return;
		}
		sendEnterServer(conn, create, sendValues, 88);
	}

	public static void handle_SERVER_HAND_CLIENT_89_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_89_REQ))) {
			noFindReq(conn, message, 89);
			return;
		}
		SERVER_HAND_CLIENT_89_REQ req = (SERVER_HAND_CLIENT_89_REQ)message_req;
		SERVER_HAND_CLIENT_89_RES res = (SERVER_HAND_CLIENT_89_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[69]*sendValues[73]-sendValues[63]*sendValues[36]*sendValues[98]*sendValues[2]/sendValues[31]+sendValues[10]-sendValues[53]+sendValues[6];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 89);
			return;
		}
		sendEnterServer(conn, create, sendValues, 89);
	}

	public static void handle_SERVER_HAND_CLIENT_90_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_90_REQ))) {
			noFindReq(conn, message, 90);
			return;
		}
		SERVER_HAND_CLIENT_90_REQ req = (SERVER_HAND_CLIENT_90_REQ)message_req;
		SERVER_HAND_CLIENT_90_RES res = (SERVER_HAND_CLIENT_90_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[67]/sendValues[99]+sendValues[51]*sendValues[35]-sendValues[14]+sendValues[80]/sendValues[63]/sendValues[34]*sendValues[33]*sendValues[57];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 90);
			return;
		}
		sendEnterServer(conn, create, sendValues, 90);
	}

	public static void handle_SERVER_HAND_CLIENT_91_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_91_REQ))) {
			noFindReq(conn, message, 91);
			return;
		}
		SERVER_HAND_CLIENT_91_REQ req = (SERVER_HAND_CLIENT_91_REQ)message_req;
		SERVER_HAND_CLIENT_91_RES res = (SERVER_HAND_CLIENT_91_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[14]+sendValues[39]/sendValues[80]+sendValues[56]*sendValues[52]/sendValues[99]*sendValues[11]+sendValues[90]*sendValues[37]/sendValues[50];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 91);
			return;
		}
		sendEnterServer(conn, create, sendValues, 91);
	}

	public static void handle_SERVER_HAND_CLIENT_92_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_92_REQ))) {
			noFindReq(conn, message, 92);
			return;
		}
		SERVER_HAND_CLIENT_92_REQ req = (SERVER_HAND_CLIENT_92_REQ)message_req;
		SERVER_HAND_CLIENT_92_RES res = (SERVER_HAND_CLIENT_92_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[57]-sendValues[4]-sendValues[54]-sendValues[17]/sendValues[12]/sendValues[35]-sendValues[29]-sendValues[79]+sendValues[65]+sendValues[91];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 92);
			return;
		}
		sendEnterServer(conn, create, sendValues, 92);
	}

	public static void handle_SERVER_HAND_CLIENT_93_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_93_REQ))) {
			noFindReq(conn, message, 93);
			return;
		}
		SERVER_HAND_CLIENT_93_REQ req = (SERVER_HAND_CLIENT_93_REQ)message_req;
		SERVER_HAND_CLIENT_93_RES res = (SERVER_HAND_CLIENT_93_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[17]-sendValues[10]+sendValues[66]/sendValues[67]+sendValues[57]/sendValues[43]+sendValues[95]/sendValues[82]*sendValues[7]-sendValues[42];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 93);
			return;
		}
		sendEnterServer(conn, create, sendValues, 93);
	}

	public static void handle_SERVER_HAND_CLIENT_94_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_94_REQ))) {
			noFindReq(conn, message, 94);
			return;
		}
		SERVER_HAND_CLIENT_94_REQ req = (SERVER_HAND_CLIENT_94_REQ)message_req;
		SERVER_HAND_CLIENT_94_RES res = (SERVER_HAND_CLIENT_94_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[15]*sendValues[86]-sendValues[71]-sendValues[54]-sendValues[4]/sendValues[95]-sendValues[28]+sendValues[80]*sendValues[32]+sendValues[34];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 94);
			return;
		}
		sendEnterServer(conn, create, sendValues, 94);
	}

	public static void handle_SERVER_HAND_CLIENT_95_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_95_REQ))) {
			noFindReq(conn, message, 95);
			return;
		}
		SERVER_HAND_CLIENT_95_REQ req = (SERVER_HAND_CLIENT_95_REQ)message_req;
		SERVER_HAND_CLIENT_95_RES res = (SERVER_HAND_CLIENT_95_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[43]*sendValues[76]+sendValues[71]/sendValues[15]-sendValues[33]-sendValues[12]+sendValues[77]-sendValues[96]-sendValues[86]-sendValues[21];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 95);
			return;
		}
		sendEnterServer(conn, create, sendValues, 95);
	}

	public static void handle_SERVER_HAND_CLIENT_96_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_96_REQ))) {
			noFindReq(conn, message, 96);
			return;
		}
		SERVER_HAND_CLIENT_96_REQ req = (SERVER_HAND_CLIENT_96_REQ)message_req;
		SERVER_HAND_CLIENT_96_RES res = (SERVER_HAND_CLIENT_96_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[77]/sendValues[86]/sendValues[66]*sendValues[32]*sendValues[26]*sendValues[39]-sendValues[65]/sendValues[2]*sendValues[97]*sendValues[7];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 96);
			return;
		}
		sendEnterServer(conn, create, sendValues, 96);
	}

	public static void handle_SERVER_HAND_CLIENT_97_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_97_REQ))) {
			noFindReq(conn, message, 97);
			return;
		}
		SERVER_HAND_CLIENT_97_REQ req = (SERVER_HAND_CLIENT_97_REQ)message_req;
		SERVER_HAND_CLIENT_97_RES res = (SERVER_HAND_CLIENT_97_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[54]*sendValues[45]/sendValues[63]*sendValues[78]*sendValues[21]*sendValues[12]-sendValues[23]*sendValues[32]*sendValues[70]-sendValues[41];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 97);
			return;
		}
		sendEnterServer(conn, create, sendValues, 97);
	}

	public static void handle_SERVER_HAND_CLIENT_98_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_98_REQ))) {
			noFindReq(conn, message, 98);
			return;
		}
		SERVER_HAND_CLIENT_98_REQ req = (SERVER_HAND_CLIENT_98_REQ)message_req;
		SERVER_HAND_CLIENT_98_RES res = (SERVER_HAND_CLIENT_98_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[74]-sendValues[56]*sendValues[94]-sendValues[79]/sendValues[39]/sendValues[14]-sendValues[85]/sendValues[35]/sendValues[30]/sendValues[71];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 98);
			return;
		}
		sendEnterServer(conn, create, sendValues, 98);
	}

	public static void handle_SERVER_HAND_CLIENT_99_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_99_REQ))) {
			noFindReq(conn, message, 99);
			return;
		}
		SERVER_HAND_CLIENT_99_REQ req = (SERVER_HAND_CLIENT_99_REQ)message_req;
		SERVER_HAND_CLIENT_99_RES res = (SERVER_HAND_CLIENT_99_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[99]/sendValues[61]*sendValues[97]-sendValues[24]/sendValues[48]*sendValues[88]/sendValues[37]-sendValues[44]+sendValues[21]+sendValues[67];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 99);
			return;
		}
		sendEnterServer(conn, create, sendValues, 99);
	}

	public static void handle_SERVER_HAND_CLIENT_100_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE");
		if (message_req == null || (!(message_req instanceof SERVER_HAND_CLIENT_100_REQ))) {
			noFindReq(conn, message, 100);
			return;
		}
		SERVER_HAND_CLIENT_100_REQ req = (SERVER_HAND_CLIENT_100_REQ)message_req;
		SERVER_HAND_CLIENT_100_RES res = (SERVER_HAND_CLIENT_100_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[57]-sendValues[28]*sendValues[1]-sendValues[12]/sendValues[91]*sendValues[97]/sendValues[29]+sendValues[3]*sendValues[73]-sendValues[51];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 100);
			return;
		}
		sendEnterServer(conn, create, sendValues, 100);
	}

	//TODO: 模拟正常的  hand 协议
	
	public static void handle_TRY_GETPLAYERINFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_GETPLAYERINFO_REQ))) {
			noFindReq(conn, message, 1);
			return;
		}
		TRY_GETPLAYERINFO_REQ req = (TRY_GETPLAYERINFO_REQ)message_req;
		TRY_GETPLAYERINFO_RES res = (TRY_GETPLAYERINFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[79]*sendValues[38]*sendValues[61]*sendValues[36]/sendValues[87]-sendValues[10]/sendValues[86]/sendValues[93]-sendValues[78]+sendValues[85];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 1);
			return;
		}
		sendEnterServer(conn, create, sendValues, 1);
	}

	public static void handle_WAIT_FOR_OTHER_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof WAIT_FOR_OTHER_REQ))) {
			noFindReq(conn, message, 2);
			return;
		}
		WAIT_FOR_OTHER_REQ req = (WAIT_FOR_OTHER_REQ)message_req;
		WAIT_FOR_OTHER_RES res = (WAIT_FOR_OTHER_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[55]-sendValues[57]*sendValues[63]*sendValues[83]*sendValues[77]/sendValues[66]/sendValues[11]*sendValues[31]+sendValues[67]/sendValues[18];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 2);
			return;
		}
		sendEnterServer(conn, create, sendValues, 2);
	}

	public static void handle_GET_REWARD_2_PLAYER_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_REWARD_2_PLAYER_REQ))) {
			noFindReq(conn, message, 3);
			return;
		}
		GET_REWARD_2_PLAYER_REQ req = (GET_REWARD_2_PLAYER_REQ)message_req;
		GET_REWARD_2_PLAYER_RES res = (GET_REWARD_2_PLAYER_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[70]+sendValues[52]-sendValues[91]+sendValues[22]*sendValues[93]-sendValues[28]*sendValues[18]*sendValues[74]*sendValues[76]+sendValues[99];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 3);
			return;
		}
		sendEnterServer(conn, create, sendValues, 3);
	}

	public static void handle_REQUESTBUY_GET_ENTITY_INFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REQUESTBUY_GET_ENTITY_INFO_REQ))) {
			noFindReq(conn, message, 4);
			return;
		}
		REQUESTBUY_GET_ENTITY_INFO_REQ req = (REQUESTBUY_GET_ENTITY_INFO_REQ)message_req;
		REQUESTBUY_GET_ENTITY_INFO_RES res = (REQUESTBUY_GET_ENTITY_INFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[15]*sendValues[59]+sendValues[99]+sendValues[0]+sendValues[20]-sendValues[87]*sendValues[75]/sendValues[36]+sendValues[42]/sendValues[44];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 4);
			return;
		}
		sendEnterServer(conn, create, sendValues, 4);
	}

	public static void handle_PLAYER_SOUL_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_SOUL_REQ))) {
			noFindReq(conn, message, 5);
			return;
		}
		PLAYER_SOUL_REQ req = (PLAYER_SOUL_REQ)message_req;
		PLAYER_SOUL_RES res = (PLAYER_SOUL_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[34]-sendValues[16]*sendValues[27]*sendValues[71]-sendValues[49]/sendValues[87]-sendValues[90]/sendValues[6]/sendValues[93]/sendValues[95];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 5);
			return;
		}
		sendEnterServer(conn, create, sendValues, 5);
	}

	public static void handle_CARD_TRYSAVING_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CARD_TRYSAVING_REQ))) {
			noFindReq(conn, message, 6);
			return;
		}
		CARD_TRYSAVING_REQ req = (CARD_TRYSAVING_REQ)message_req;
		CARD_TRYSAVING_RES res = (CARD_TRYSAVING_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[45]/sendValues[91]*sendValues[34]*sendValues[35]*sendValues[81]-sendValues[21]/sendValues[56]/sendValues[89]/sendValues[10]/sendValues[41];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 6);
			return;
		}
		sendEnterServer(conn, create, sendValues, 6);
	}

	public static void handle_GANG_WAREHOUSE_JOURNAL_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GANG_WAREHOUSE_JOURNAL_REQ))) {
			noFindReq(conn, message, 7);
			return;
		}
		GANG_WAREHOUSE_JOURNAL_REQ req = (GANG_WAREHOUSE_JOURNAL_REQ)message_req;
		GANG_WAREHOUSE_JOURNAL_RES res = (GANG_WAREHOUSE_JOURNAL_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[38]/sendValues[50]*sendValues[58]+sendValues[24]*sendValues[42]-sendValues[20]*sendValues[89]/sendValues[18]+sendValues[36]/sendValues[99];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 7);
			return;
		}
		sendEnterServer(conn, create, sendValues, 7);
	}

	public static void handle_GET_WAREHOUSE_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_WAREHOUSE_REQ))) {
			noFindReq(conn, message, 8);
			return;
		}
		GET_WAREHOUSE_REQ req = (GET_WAREHOUSE_REQ)message_req;
		GET_WAREHOUSE_RES res = (GET_WAREHOUSE_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[32]*sendValues[18]/sendValues[88]/sendValues[19]-sendValues[80]+sendValues[7]-sendValues[90]+sendValues[42]/sendValues[31]+sendValues[49];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 8);
			return;
		}
		sendEnterServer(conn, create, sendValues, 8);
	}

	public static void handle_QUERY__GETAUTOBACK_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QUERY__GETAUTOBACK_REQ))) {
			noFindReq(conn, message, 9);
			return;
		}
		QUERY__GETAUTOBACK_REQ req = (QUERY__GETAUTOBACK_REQ)message_req;
		QUERY__GETAUTOBACK_RES res = (QUERY__GETAUTOBACK_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[17]-sendValues[63]/sendValues[8]*sendValues[75]*sendValues[61]-sendValues[74]*sendValues[48]*sendValues[56]-sendValues[5]-sendValues[23];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 9);
			return;
		}
		sendEnterServer(conn, create, sendValues, 9);
	}

	public static void handle_GET_ZONGPAI_NAME_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_ZONGPAI_NAME_REQ))) {
			noFindReq(conn, message, 10);
			return;
		}
		GET_ZONGPAI_NAME_REQ req = (GET_ZONGPAI_NAME_REQ)message_req;
		GET_ZONGPAI_NAME_RES res = (GET_ZONGPAI_NAME_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[14]+sendValues[12]*sendValues[99]/sendValues[25]/sendValues[76]-sendValues[20]*sendValues[42]/sendValues[24]-sendValues[27]+sendValues[49];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 10);
			return;
		}
		sendEnterServer(conn, create, sendValues, 10);
	}

	public static void handle_TRY_LEAVE_ZONGPAI_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_LEAVE_ZONGPAI_REQ))) {
			noFindReq(conn, message, 11);
			return;
		}
		TRY_LEAVE_ZONGPAI_REQ req = (TRY_LEAVE_ZONGPAI_REQ)message_req;
		TRY_LEAVE_ZONGPAI_RES res = (TRY_LEAVE_ZONGPAI_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[7]/sendValues[42]+sendValues[41]+sendValues[55]-sendValues[43]/sendValues[0]+sendValues[64]*sendValues[56]-sendValues[54]+sendValues[52];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 11);
			return;
		}
		sendEnterServer(conn, create, sendValues, 11);
	}

	public static void handle_REBEL_EVICT_NEW_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REBEL_EVICT_NEW_REQ))) {
			noFindReq(conn, message, 12);
			return;
		}
		REBEL_EVICT_NEW_REQ req = (REBEL_EVICT_NEW_REQ)message_req;
		REBEL_EVICT_NEW_RES res = (REBEL_EVICT_NEW_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[27]-sendValues[42]*sendValues[7]-sendValues[49]/sendValues[96]*sendValues[84]-sendValues[1]*sendValues[5]+sendValues[34]/sendValues[67];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 12);
			return;
		}
		sendEnterServer(conn, create, sendValues, 12);
	}

	public static void handle_GET_PLAYERTITLE_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_PLAYERTITLE_REQ))) {
			noFindReq(conn, message, 13);
			return;
		}
		GET_PLAYERTITLE_REQ req = (GET_PLAYERTITLE_REQ)message_req;
		GET_PLAYERTITLE_RES res = (GET_PLAYERTITLE_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[33]/sendValues[45]+sendValues[54]/sendValues[40]-sendValues[97]+sendValues[68]+sendValues[36]-sendValues[51]+sendValues[62]*sendValues[84];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 13);
			return;
		}
		sendEnterServer(conn, create, sendValues, 13);
	}

	public static void handle_MARRIAGE_TRY_BEQSTART_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MARRIAGE_TRY_BEQSTART_REQ))) {
			noFindReq(conn, message, 14);
			return;
		}
		MARRIAGE_TRY_BEQSTART_REQ req = (MARRIAGE_TRY_BEQSTART_REQ)message_req;
		MARRIAGE_TRY_BEQSTART_RES res = (MARRIAGE_TRY_BEQSTART_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[53]/sendValues[80]+sendValues[26]*sendValues[94]*sendValues[82]/sendValues[92]/sendValues[40]/sendValues[30]-sendValues[37]/sendValues[83];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 14);
			return;
		}
		sendEnterServer(conn, create, sendValues, 14);
	}

	public static void handle_MARRIAGE_GUESTNEW_OVER_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MARRIAGE_GUESTNEW_OVER_REQ))) {
			noFindReq(conn, message, 15);
			return;
		}
		MARRIAGE_GUESTNEW_OVER_REQ req = (MARRIAGE_GUESTNEW_OVER_REQ)message_req;
		MARRIAGE_GUESTNEW_OVER_RES res = (MARRIAGE_GUESTNEW_OVER_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[36]-sendValues[87]/sendValues[24]+sendValues[46]/sendValues[1]/sendValues[15]*sendValues[50]/sendValues[63]*sendValues[82]*sendValues[85];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 15);
			return;
		}
		sendEnterServer(conn, create, sendValues, 15);
	}

	public static void handle_MARRIAGE_HUNLI_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MARRIAGE_HUNLI_REQ))) {
			noFindReq(conn, message, 16);
			return;
		}
		MARRIAGE_HUNLI_REQ req = (MARRIAGE_HUNLI_REQ)message_req;
		MARRIAGE_HUNLI_RES res = (MARRIAGE_HUNLI_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[64]-sendValues[11]*sendValues[0]/sendValues[59]*sendValues[22]*sendValues[4]-sendValues[56]+sendValues[40]-sendValues[79]-sendValues[10];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 16);
			return;
		}
		sendEnterServer(conn, create, sendValues, 16);
	}

	public static void handle_COUNTRY_COMMENDCANCEL_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof COUNTRY_COMMENDCANCEL_REQ))) {
			noFindReq(conn, message, 17);
			return;
		}
		COUNTRY_COMMENDCANCEL_REQ req = (COUNTRY_COMMENDCANCEL_REQ)message_req;
		COUNTRY_COMMENDCANCEL_RES res = (COUNTRY_COMMENDCANCEL_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[13]-sendValues[46]*sendValues[91]+sendValues[26]*sendValues[4]*sendValues[69]/sendValues[29]/sendValues[30]-sendValues[81]-sendValues[5];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 17);
			return;
		}
		sendEnterServer(conn, create, sendValues, 17);
	}

	public static void handle_COUNTRY_NEWQIUJIN_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof COUNTRY_NEWQIUJIN_REQ))) {
			noFindReq(conn, message, 18);
			return;
		}
		COUNTRY_NEWQIUJIN_REQ req = (COUNTRY_NEWQIUJIN_REQ)message_req;
		COUNTRY_NEWQIUJIN_RES res = (COUNTRY_NEWQIUJIN_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[12]-sendValues[60]-sendValues[93]+sendValues[74]-sendValues[84]/sendValues[96]+sendValues[77]-sendValues[1]/sendValues[20]-sendValues[39];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 18);
			return;
		}
		sendEnterServer(conn, create, sendValues, 18);
	}

	public static void handle_GET_COUNTRYJINKU_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_COUNTRYJINKU_REQ))) {
			noFindReq(conn, message, 19);
			return;
		}
		GET_COUNTRYJINKU_REQ req = (GET_COUNTRYJINKU_REQ)message_req;
		GET_COUNTRYJINKU_RES res = (GET_COUNTRYJINKU_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[52]-sendValues[18]+sendValues[56]+sendValues[5]/sendValues[25]*sendValues[85]/sendValues[27]-sendValues[70]/sendValues[54]-sendValues[39];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 19);
			return;
		}
		sendEnterServer(conn, create, sendValues, 19);
	}

	public static void handle_CAVE_NEWBUILDING_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CAVE_NEWBUILDING_REQ))) {
			noFindReq(conn, message, 20);
			return;
		}
		CAVE_NEWBUILDING_REQ req = (CAVE_NEWBUILDING_REQ)message_req;
		CAVE_NEWBUILDING_RES res = (CAVE_NEWBUILDING_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[75]/sendValues[16]+sendValues[19]/sendValues[12]+sendValues[93]/sendValues[32]/sendValues[87]/sendValues[39]+sendValues[22]*sendValues[89];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 20);
			return;
		}
		sendEnterServer(conn, create, sendValues, 20);
	}

	public static void handle_CAVE_FIELD_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CAVE_FIELD_REQ))) {
			noFindReq(conn, message, 21);
			return;
		}
		CAVE_FIELD_REQ req = (CAVE_FIELD_REQ)message_req;
		CAVE_FIELD_RES res = (CAVE_FIELD_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[34]+sendValues[30]-sendValues[44]-sendValues[84]/sendValues[46]*sendValues[96]/sendValues[8]*sendValues[36]/sendValues[1]/sendValues[16];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 21);
			return;
		}
		sendEnterServer(conn, create, sendValues, 21);
	}

	public static void handle_CAVE_NEW_PET_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CAVE_NEW_PET_REQ))) {
			noFindReq(conn, message, 22);
			return;
		}
		CAVE_NEW_PET_REQ req = (CAVE_NEW_PET_REQ)message_req;
		CAVE_NEW_PET_RES res = (CAVE_NEW_PET_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[44]/sendValues[74]-sendValues[56]+sendValues[73]/sendValues[64]/sendValues[86]/sendValues[81]*sendValues[76]/sendValues[13]+sendValues[20];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 22);
			return;
		}
		sendEnterServer(conn, create, sendValues, 22);
	}

	public static void handle_CAVE_TRY_SCHEDULE_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CAVE_TRY_SCHEDULE_REQ))) {
			noFindReq(conn, message, 23);
			return;
		}
		CAVE_TRY_SCHEDULE_REQ req = (CAVE_TRY_SCHEDULE_REQ)message_req;
		CAVE_TRY_SCHEDULE_RES res = (CAVE_TRY_SCHEDULE_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[60]/sendValues[27]/sendValues[84]+sendValues[88]*sendValues[46]-sendValues[25]+sendValues[71]+sendValues[30]-sendValues[51]-sendValues[1];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 23);
			return;
		}
		sendEnterServer(conn, create, sendValues, 23);
	}

	public static void handle_CAVE_SEND_COUNTYLIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CAVE_SEND_COUNTYLIST_REQ))) {
			noFindReq(conn, message, 24);
			return;
		}
		CAVE_SEND_COUNTYLIST_REQ req = (CAVE_SEND_COUNTYLIST_REQ)message_req;
		CAVE_SEND_COUNTYLIST_RES res = (CAVE_SEND_COUNTYLIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[68]*sendValues[4]/sendValues[92]-sendValues[86]*sendValues[47]/sendValues[48]-sendValues[76]+sendValues[23]*sendValues[22]*sendValues[30];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 24);
			return;
		}
		sendEnterServer(conn, create, sendValues, 24);
	}

	public static void handle_PLAYER_NEW_LEVELUP_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_NEW_LEVELUP_REQ))) {
			noFindReq(conn, message, 25);
			return;
		}
		PLAYER_NEW_LEVELUP_REQ req = (PLAYER_NEW_LEVELUP_REQ)message_req;
		PLAYER_NEW_LEVELUP_RES res = (PLAYER_NEW_LEVELUP_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[0]/sendValues[16]+sendValues[90]*sendValues[87]+sendValues[92]+sendValues[17]-sendValues[28]*sendValues[93]+sendValues[50]-sendValues[56];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 25);
			return;
		}
		sendEnterServer(conn, create, sendValues, 25);
	}

	public static void handle_CLEAN_FRIEND_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CLEAN_FRIEND_LIST_REQ))) {
			noFindReq(conn, message, 26);
			return;
		}
		CLEAN_FRIEND_LIST_REQ req = (CLEAN_FRIEND_LIST_REQ)message_req;
		CLEAN_FRIEND_LIST_RES res = (CLEAN_FRIEND_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[31]/sendValues[95]*sendValues[93]*sendValues[91]-sendValues[64]-sendValues[44]-sendValues[40]-sendValues[90]-sendValues[89]*sendValues[94];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 26);
			return;
		}
		sendEnterServer(conn, create, sendValues, 26);
	}

	public static void handle_DO_ACTIVITY_NEW_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DO_ACTIVITY_NEW_REQ))) {
			noFindReq(conn, message, 27);
			return;
		}
		DO_ACTIVITY_NEW_REQ req = (DO_ACTIVITY_NEW_REQ)message_req;
		DO_ACTIVITY_NEW_RES res = (DO_ACTIVITY_NEW_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[20]-sendValues[40]/sendValues[79]+sendValues[23]+sendValues[65]*sendValues[81]+sendValues[52]/sendValues[30]-sendValues[37]*sendValues[9];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 27);
			return;
		}
		sendEnterServer(conn, create, sendValues, 27);
	}

	public static void handle_REF_TESK_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REF_TESK_LIST_REQ))) {
			noFindReq(conn, message, 28);
			return;
		}
		REF_TESK_LIST_REQ req = (REF_TESK_LIST_REQ)message_req;
		REF_TESK_LIST_RES res = (REF_TESK_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[32]+sendValues[48]+sendValues[23]*sendValues[9]/sendValues[58]/sendValues[36]*sendValues[73]+sendValues[29]+sendValues[88]+sendValues[42];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 28);
			return;
		}
		sendEnterServer(conn, create, sendValues, 28);
	}

	public static void handle_DELTE_PET_INFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DELTE_PET_INFO_REQ))) {
			noFindReq(conn, message, 29);
			return;
		}
		DELTE_PET_INFO_REQ req = (DELTE_PET_INFO_REQ)message_req;
		DELTE_PET_INFO_RES res = (DELTE_PET_INFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[54]*sendValues[91]/sendValues[64]+sendValues[44]*sendValues[93]/sendValues[2]*sendValues[40]*sendValues[89]-sendValues[88]+sendValues[8];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 29);
			return;
		}
		sendEnterServer(conn, create, sendValues, 29);
	}

	public static void handle_MARRIAGE_DOACTIVITY_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MARRIAGE_DOACTIVITY_REQ))) {
			noFindReq(conn, message, 30);
			return;
		}
		MARRIAGE_DOACTIVITY_REQ req = (MARRIAGE_DOACTIVITY_REQ)message_req;
		MARRIAGE_DOACTIVITY_RES res = (MARRIAGE_DOACTIVITY_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[35]*sendValues[1]-sendValues[92]*sendValues[73]/sendValues[11]/sendValues[15]*sendValues[72]+sendValues[99]+sendValues[53]/sendValues[69];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 30);
			return;
		}
		sendEnterServer(conn, create, sendValues, 30);
	}

	public static void handle_LA_FRIEND_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof LA_FRIEND_REQ))) {
			noFindReq(conn, message, 31);
			return;
		}
		LA_FRIEND_REQ req = (LA_FRIEND_REQ)message_req;
		LA_FRIEND_RES res = (LA_FRIEND_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[49]*sendValues[85]+sendValues[90]/sendValues[68]-sendValues[31]-sendValues[62]/sendValues[64]-sendValues[46]/sendValues[13]+sendValues[34];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 31);
			return;
		}
		sendEnterServer(conn, create, sendValues, 31);
	}

	public static void handle_TRY_NEWFRIEND_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_NEWFRIEND_LIST_REQ))) {
			noFindReq(conn, message, 32);
			return;
		}
		TRY_NEWFRIEND_LIST_REQ req = (TRY_NEWFRIEND_LIST_REQ)message_req;
		TRY_NEWFRIEND_LIST_RES res = (TRY_NEWFRIEND_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[59]/sendValues[99]*sendValues[34]-sendValues[60]-sendValues[28]*sendValues[36]-sendValues[53]+sendValues[42]*sendValues[69]*sendValues[4];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 32);
			return;
		}
		sendEnterServer(conn, create, sendValues, 32);
	}

	public static void handle_QINGQIU_PET_INFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QINGQIU_PET_INFO_REQ))) {
			noFindReq(conn, message, 33);
			return;
		}
		QINGQIU_PET_INFO_REQ req = (QINGQIU_PET_INFO_REQ)message_req;
		QINGQIU_PET_INFO_RES res = (QINGQIU_PET_INFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[8]*sendValues[40]+sendValues[99]+sendValues[60]/sendValues[41]+sendValues[81]/sendValues[15]+sendValues[98]*sendValues[77]/sendValues[69];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 33);
			return;
		}
		sendEnterServer(conn, create, sendValues, 33);
	}

	public static void handle_REMOVE_ACTIVITY_NEW_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REMOVE_ACTIVITY_NEW_REQ))) {
			noFindReq(conn, message, 34);
			return;
		}
		REMOVE_ACTIVITY_NEW_REQ req = (REMOVE_ACTIVITY_NEW_REQ)message_req;
		REMOVE_ACTIVITY_NEW_RES res = (REMOVE_ACTIVITY_NEW_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[69]/sendValues[11]/sendValues[66]*sendValues[61]-sendValues[17]/sendValues[27]/sendValues[24]/sendValues[92]*sendValues[22]+sendValues[76];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 34);
			return;
		}
		sendEnterServer(conn, create, sendValues, 34);
	}

	public static void handle_TRY_LEAVE_GAME_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_LEAVE_GAME_REQ))) {
			noFindReq(conn, message, 35);
			return;
		}
		TRY_LEAVE_GAME_REQ req = (TRY_LEAVE_GAME_REQ)message_req;
		TRY_LEAVE_GAME_RES res = (TRY_LEAVE_GAME_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[53]*sendValues[17]-sendValues[66]/sendValues[30]*sendValues[0]*sendValues[45]-sendValues[39]-sendValues[26]*sendValues[96]-sendValues[92];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 35);
			return;
		}
		sendEnterServer(conn, create, sendValues, 35);
	}

	public static void handle_GET_TESK_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_TESK_LIST_REQ))) {
			noFindReq(conn, message, 36);
			return;
		}
		GET_TESK_LIST_REQ req = (GET_TESK_LIST_REQ)message_req;
		GET_TESK_LIST_RES res = (GET_TESK_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[47]*sendValues[32]+sendValues[74]-sendValues[60]+sendValues[17]/sendValues[59]+sendValues[55]*sendValues[46]*sendValues[77]-sendValues[45];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 36);
			return;
		}
		sendEnterServer(conn, create, sendValues, 36);
	}

	public static void handle_GET_GAME_PALAYERNAME_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_GAME_PALAYERNAME_REQ))) {
			noFindReq(conn, message, 37);
			return;
		}
		GET_GAME_PALAYERNAME_REQ req = (GET_GAME_PALAYERNAME_REQ)message_req;
		GET_GAME_PALAYERNAME_RES res = (GET_GAME_PALAYERNAME_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[90]-sendValues[96]*sendValues[21]-sendValues[59]*sendValues[25]-sendValues[95]*sendValues[26]-sendValues[2]+sendValues[87]/sendValues[13];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 37);
			return;
		}
		sendEnterServer(conn, create, sendValues, 37);
	}

	public static void handle_GET_ACTIVITY_JOINIDS_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_ACTIVITY_JOINIDS_REQ))) {
			noFindReq(conn, message, 38);
			return;
		}
		GET_ACTIVITY_JOINIDS_REQ req = (GET_ACTIVITY_JOINIDS_REQ)message_req;
		GET_ACTIVITY_JOINIDS_RES res = (GET_ACTIVITY_JOINIDS_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[50]-sendValues[59]-sendValues[72]/sendValues[13]/sendValues[26]+sendValues[43]+sendValues[85]+sendValues[80]/sendValues[64]-sendValues[29];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 38);
			return;
		}
		sendEnterServer(conn, create, sendValues, 38);
	}

	public static void handle_QUERY_GAMENAMES_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QUERY_GAMENAMES_REQ))) {
			noFindReq(conn, message, 39);
			return;
		}
		QUERY_GAMENAMES_REQ req = (QUERY_GAMENAMES_REQ)message_req;
		QUERY_GAMENAMES_RES res = (QUERY_GAMENAMES_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[46]+sendValues[44]+sendValues[79]+sendValues[94]+sendValues[9]-sendValues[76]*sendValues[35]+sendValues[60]+sendValues[29]*sendValues[8];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 39);
			return;
		}
		sendEnterServer(conn, create, sendValues, 39);
	}

	public static void handle_GET_PET_NBWINFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_PET_NBWINFO_REQ))) {
			noFindReq(conn, message, 40);
			return;
		}
		GET_PET_NBWINFO_REQ req = (GET_PET_NBWINFO_REQ)message_req;
		GET_PET_NBWINFO_RES res = (GET_PET_NBWINFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[11]+sendValues[30]/sendValues[85]/sendValues[54]-sendValues[52]*sendValues[31]+sendValues[34]/sendValues[70]-sendValues[29]/sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 40);
			return;
		}
		sendEnterServer(conn, create, sendValues, 40);
	}

	public static void handle_CLONE_FRIEND_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CLONE_FRIEND_LIST_REQ))) {
			noFindReq(conn, message, 41);
			return;
		}
		CLONE_FRIEND_LIST_REQ req = (CLONE_FRIEND_LIST_REQ)message_req;
		CLONE_FRIEND_LIST_RES res = (CLONE_FRIEND_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[28]*sendValues[2]/sendValues[96]/sendValues[30]+sendValues[31]*sendValues[73]/sendValues[17]/sendValues[20]/sendValues[98]/sendValues[40];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 41);
			return;
		}
		sendEnterServer(conn, create, sendValues, 41);
	}

	public static void handle_QUERY_OTHERPLAYER_PET_MSG_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QUERY_OTHERPLAYER_PET_MSG_REQ))) {
			noFindReq(conn, message, 42);
			return;
		}
		QUERY_OTHERPLAYER_PET_MSG_REQ req = (QUERY_OTHERPLAYER_PET_MSG_REQ)message_req;
		QUERY_OTHERPLAYER_PET_MSG_RES res = (QUERY_OTHERPLAYER_PET_MSG_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[93]*sendValues[95]/sendValues[19]-sendValues[41]*sendValues[60]/sendValues[68]*sendValues[56]/sendValues[33]*sendValues[16]-sendValues[13];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 42);
			return;
		}
		sendEnterServer(conn, create, sendValues, 42);
	}

	public static void handle_CSR_GET_PLAYER_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CSR_GET_PLAYER_REQ))) {
			noFindReq(conn, message, 43);
			return;
		}
		CSR_GET_PLAYER_REQ req = (CSR_GET_PLAYER_REQ)message_req;
		CSR_GET_PLAYER_RES res = (CSR_GET_PLAYER_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[57]-sendValues[36]-sendValues[99]+sendValues[55]/sendValues[4]-sendValues[81]*sendValues[53]-sendValues[83]/sendValues[19]+sendValues[38];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 43);
			return;
		}
		sendEnterServer(conn, create, sendValues, 43);
	}

	public static void handle_HAVE_OTHERNEW_INFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof HAVE_OTHERNEW_INFO_REQ))) {
			noFindReq(conn, message, 44);
			return;
		}
		HAVE_OTHERNEW_INFO_REQ req = (HAVE_OTHERNEW_INFO_REQ)message_req;
		HAVE_OTHERNEW_INFO_RES res = (HAVE_OTHERNEW_INFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[79]+sendValues[80]/sendValues[59]+sendValues[28]+sendValues[29]*sendValues[13]-sendValues[73]-sendValues[87]/sendValues[76]+sendValues[22];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 44);
			return;
		}
		sendEnterServer(conn, create, sendValues, 44);
	}

	public static void handle_SHANCHU_FRIENDLIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof SHANCHU_FRIENDLIST_REQ))) {
			noFindReq(conn, message, 45);
			return;
		}
		SHANCHU_FRIENDLIST_REQ req = (SHANCHU_FRIENDLIST_REQ)message_req;
		SHANCHU_FRIENDLIST_RES res = (SHANCHU_FRIENDLIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[73]/sendValues[95]+sendValues[10]+sendValues[47]-sendValues[79]+sendValues[45]/sendValues[69]-sendValues[16]*sendValues[97]-sendValues[24];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 45);
			return;
		}
		sendEnterServer(conn, create, sendValues, 45);
	}

	public static void handle_QUERY_TESK_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QUERY_TESK_LIST_REQ))) {
			noFindReq(conn, message, 46);
			return;
		}
		QUERY_TESK_LIST_REQ req = (QUERY_TESK_LIST_REQ)message_req;
		QUERY_TESK_LIST_RES res = (QUERY_TESK_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]+sendValues[47]*sendValues[85]+sendValues[64]*sendValues[62]-sendValues[6]-sendValues[99]-sendValues[45]+sendValues[54]-sendValues[2];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 46);
			return;
		}
		sendEnterServer(conn, create, sendValues, 46);
	}

	public static void handle_CL_HORSE_INFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CL_HORSE_INFO_REQ))) {
			noFindReq(conn, message, 47);
			return;
		}
		CL_HORSE_INFO_REQ req = (CL_HORSE_INFO_REQ)message_req;
		CL_HORSE_INFO_RES res = (CL_HORSE_INFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[0]-sendValues[16]*sendValues[63]+sendValues[47]+sendValues[36]/sendValues[96]*sendValues[48]*sendValues[91]*sendValues[57]*sendValues[75];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 47);
			return;
		}
		sendEnterServer(conn, create, sendValues, 47);
	}

	public static void handle_CL_NEWPET_MSG_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof CL_NEWPET_MSG_REQ))) {
			noFindReq(conn, message, 48);
			return;
		}
		CL_NEWPET_MSG_REQ req = (CL_NEWPET_MSG_REQ)message_req;
		CL_NEWPET_MSG_RES res = (CL_NEWPET_MSG_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[16]-sendValues[86]+sendValues[50]*sendValues[93]+sendValues[79]/sendValues[95]/sendValues[19]-sendValues[36]+sendValues[85]+sendValues[4];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 48);
			return;
		}
		sendEnterServer(conn, create, sendValues, 48);
	}

	public static void handle_GET_ACTIVITY_NEW_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_ACTIVITY_NEW_REQ))) {
			noFindReq(conn, message, 49);
			return;
		}
		GET_ACTIVITY_NEW_REQ req = (GET_ACTIVITY_NEW_REQ)message_req;
		GET_ACTIVITY_NEW_RES res = (GET_ACTIVITY_NEW_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[67]+sendValues[84]/sendValues[45]*sendValues[10]*sendValues[55]+sendValues[92]-sendValues[66]*sendValues[40]*sendValues[47]*sendValues[29];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 49);
			return;
		}
		sendEnterServer(conn, create, sendValues, 49);
	}

	public static void handle_DO_SOME_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DO_SOME_REQ))) {
			noFindReq(conn, message, 50);
			return;
		}
		DO_SOME_REQ req = (DO_SOME_REQ)message_req;
		DO_SOME_RES res = (DO_SOME_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[62]-sendValues[28]*sendValues[85]*sendValues[23]*sendValues[21]*sendValues[86]*sendValues[32]/sendValues[92]-sendValues[14]*sendValues[4];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 50);
			return;
		}
		sendEnterServer(conn, create, sendValues, 50);
	}

	public static void handle_TY_PET_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TY_PET_REQ))) {
			noFindReq(conn, message, 51);
			return;
		}
		TY_PET_REQ req = (TY_PET_REQ)message_req;
		TY_PET_RES res = (TY_PET_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[10]+sendValues[66]-sendValues[25]-sendValues[29]+sendValues[50]+sendValues[35]-sendValues[33]-sendValues[0]-sendValues[54]*sendValues[27];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 51);
			return;
		}
		sendEnterServer(conn, create, sendValues, 51);
	}

	public static void handle_EQUIPMENT_GET_MSG_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof EQUIPMENT_GET_MSG_REQ))) {
			noFindReq(conn, message, 52);
			return;
		}
		EQUIPMENT_GET_MSG_REQ req = (EQUIPMENT_GET_MSG_REQ)message_req;
		EQUIPMENT_GET_MSG_RES res = (EQUIPMENT_GET_MSG_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[71]/sendValues[27]*sendValues[14]-sendValues[23]*sendValues[39]+sendValues[42]*sendValues[49]*sendValues[8]*sendValues[45]/sendValues[69];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 52);
			return;
		}
		sendEnterServer(conn, create, sendValues, 52);
	}

	public static void handle_EQU_NEW_EQUIPMENT_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof EQU_NEW_EQUIPMENT_REQ))) {
			noFindReq(conn, message, 53);
			return;
		}
		EQU_NEW_EQUIPMENT_REQ req = (EQU_NEW_EQUIPMENT_REQ)message_req;
		EQU_NEW_EQUIPMENT_RES res = (EQU_NEW_EQUIPMENT_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[3]+sendValues[81]-sendValues[90]+sendValues[18]-sendValues[48]-sendValues[8]+sendValues[26]*sendValues[57]/sendValues[42]-sendValues[33];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 53);
			return;
		}
		sendEnterServer(conn, create, sendValues, 53);
	}

	public static void handle_DELETE_FRIEND_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DELETE_FRIEND_LIST_REQ))) {
			noFindReq(conn, message, 54);
			return;
		}
		DELETE_FRIEND_LIST_REQ req = (DELETE_FRIEND_LIST_REQ)message_req;
		DELETE_FRIEND_LIST_RES res = (DELETE_FRIEND_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[14]*sendValues[52]-sendValues[13]*sendValues[37]/sendValues[81]+sendValues[95]/sendValues[58]-sendValues[46]*sendValues[74]-sendValues[39];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 54);
			return;
		}
		sendEnterServer(conn, create, sendValues, 54);
	}

	public static void handle_DO_PET_EQUIPMENT_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DO_PET_EQUIPMENT_REQ))) {
			noFindReq(conn, message, 55);
			return;
		}
		DO_PET_EQUIPMENT_REQ req = (DO_PET_EQUIPMENT_REQ)message_req;
		DO_PET_EQUIPMENT_RES res = (DO_PET_EQUIPMENT_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[73]/sendValues[59]*sendValues[8]-sendValues[99]*sendValues[92]+sendValues[72]/sendValues[25]/sendValues[27]/sendValues[71]/sendValues[15];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 55);
			return;
		}
		sendEnterServer(conn, create, sendValues, 55);
	}

	public static void handle_QILING_NEW_INFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QILING_NEW_INFO_REQ))) {
			noFindReq(conn, message, 56);
			return;
		}
		QILING_NEW_INFO_REQ req = (QILING_NEW_INFO_REQ)message_req;
		QILING_NEW_INFO_RES res = (QILING_NEW_INFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[55]-sendValues[2]-sendValues[34]+sendValues[46]/sendValues[69]/sendValues[57]*sendValues[98]*sendValues[83]*sendValues[89]+sendValues[26];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 56);
			return;
		}
		sendEnterServer(conn, create, sendValues, 56);
	}

	public static void handle_HORSE_QILING_INFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof HORSE_QILING_INFO_REQ))) {
			noFindReq(conn, message, 57);
			return;
		}
		HORSE_QILING_INFO_REQ req = (HORSE_QILING_INFO_REQ)message_req;
		HORSE_QILING_INFO_RES res = (HORSE_QILING_INFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[60]-sendValues[40]*sendValues[8]-sendValues[48]/sendValues[35]+sendValues[12]*sendValues[15]*sendValues[44]/sendValues[77]-sendValues[80];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 57);
			return;
		}
		sendEnterServer(conn, create, sendValues, 57);
	}

	public static void handle_HORSE_EQUIPMENT_QILING_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof HORSE_EQUIPMENT_QILING_REQ))) {
			noFindReq(conn, message, 58);
			return;
		}
		HORSE_EQUIPMENT_QILING_REQ req = (HORSE_EQUIPMENT_QILING_REQ)message_req;
		HORSE_EQUIPMENT_QILING_RES res = (HORSE_EQUIPMENT_QILING_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[72]+sendValues[16]*sendValues[62]-sendValues[25]-sendValues[40]*sendValues[78]-sendValues[55]-sendValues[15]/sendValues[8]+sendValues[2];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 58);
			return;
		}
		sendEnterServer(conn, create, sendValues, 58);
	}

	public static void handle_PET_EQU_QILING_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_EQU_QILING_REQ))) {
			noFindReq(conn, message, 59);
			return;
		}
		PET_EQU_QILING_REQ req = (PET_EQU_QILING_REQ)message_req;
		PET_EQU_QILING_RES res = (PET_EQU_QILING_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[31]/sendValues[32]+sendValues[40]/sendValues[2]-sendValues[12]*sendValues[93]*sendValues[0]/sendValues[42]-sendValues[11]-sendValues[80];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 59);
			return;
		}
		sendEnterServer(conn, create, sendValues, 59);
	}

	public static void handle_MARRIAGE_TRYACTIVITY_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MARRIAGE_TRYACTIVITY_REQ))) {
			noFindReq(conn, message, 60);
			return;
		}
		MARRIAGE_TRYACTIVITY_REQ req = (MARRIAGE_TRYACTIVITY_REQ)message_req;
		MARRIAGE_TRYACTIVITY_RES res = (MARRIAGE_TRYACTIVITY_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[29]-sendValues[80]*sendValues[96]-sendValues[31]/sendValues[41]+sendValues[27]+sendValues[52]*sendValues[43]+sendValues[23]*sendValues[30];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 60);
			return;
		}
		sendEnterServer(conn, create, sendValues, 60);
	}

	public static void handle_PET_TRY_QILING_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_TRY_QILING_REQ))) {
			noFindReq(conn, message, 61);
			return;
		}
		PET_TRY_QILING_REQ req = (PET_TRY_QILING_REQ)message_req;
		PET_TRY_QILING_RES res = (PET_TRY_QILING_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[73]/sendValues[82]-sendValues[79]/sendValues[57]-sendValues[38]/sendValues[29]/sendValues[81]/sendValues[80]*sendValues[8]-sendValues[62];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 61);
			return;
		}
		sendEnterServer(conn, create, sendValues, 61);
	}

	public static void handle_PLAYER_CLEAN_QILINGLIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_CLEAN_QILINGLIST_REQ))) {
			noFindReq(conn, message, 62);
			return;
		}
		PLAYER_CLEAN_QILINGLIST_REQ req = (PLAYER_CLEAN_QILINGLIST_REQ)message_req;
		PLAYER_CLEAN_QILINGLIST_RES res = (PLAYER_CLEAN_QILINGLIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[10]-sendValues[34]+sendValues[86]*sendValues[93]+sendValues[52]*sendValues[45]*sendValues[67]*sendValues[70]/sendValues[0]-sendValues[36];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 62);
			return;
		}
		sendEnterServer(conn, create, sendValues, 62);
	}

	public static void handle_DELETE_TESK_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DELETE_TESK_LIST_REQ))) {
			noFindReq(conn, message, 63);
			return;
		}
		DELETE_TESK_LIST_REQ req = (DELETE_TESK_LIST_REQ)message_req;
		DELETE_TESK_LIST_RES res = (DELETE_TESK_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[90]+sendValues[40]-sendValues[70]+sendValues[74]-sendValues[73]*sendValues[99]+sendValues[75]/sendValues[1]-sendValues[66]/sendValues[79];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 63);
			return;
		}
		sendEnterServer(conn, create, sendValues, 63);
	}

	public static void handle_GET_HEIMINGDAI_NEWLIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_HEIMINGDAI_NEWLIST_REQ))) {
			noFindReq(conn, message, 64);
			return;
		}
		GET_HEIMINGDAI_NEWLIST_REQ req = (GET_HEIMINGDAI_NEWLIST_REQ)message_req;
		GET_HEIMINGDAI_NEWLIST_RES res = (GET_HEIMINGDAI_NEWLIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[11]-sendValues[67]-sendValues[27]/sendValues[8]/sendValues[69]+sendValues[74]*sendValues[95]/sendValues[28]/sendValues[82]+sendValues[22];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 64);
			return;
		}
		sendEnterServer(conn, create, sendValues, 64);
	}

	public static void handle_QUERY_CHOURENLIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QUERY_CHOURENLIST_REQ))) {
			noFindReq(conn, message, 65);
			return;
		}
		QUERY_CHOURENLIST_REQ req = (QUERY_CHOURENLIST_REQ)message_req;
		QUERY_CHOURENLIST_RES res = (QUERY_CHOURENLIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[35]/sendValues[53]*sendValues[88]+sendValues[24]-sendValues[65]-sendValues[75]*sendValues[86]-sendValues[34]*sendValues[59]-sendValues[19];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 65);
			return;
		}
		sendEnterServer(conn, create, sendValues, 65);
	}

	public static void handle_QINCHU_PLAYER_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof QINCHU_PLAYER_REQ))) {
			noFindReq(conn, message, 66);
			return;
		}
		QINCHU_PLAYER_REQ req = (QINCHU_PLAYER_REQ)message_req;
		QINCHU_PLAYER_RES res = (QINCHU_PLAYER_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[52]+sendValues[14]-sendValues[95]/sendValues[19]+sendValues[0]*sendValues[6]/sendValues[12]/sendValues[31]-sendValues[37]+sendValues[34];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 66);
			return;
		}
		sendEnterServer(conn, create, sendValues, 66);
	}

	public static void handle_REMOVE_FRIEND_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REMOVE_FRIEND_LIST_REQ))) {
			noFindReq(conn, message, 67);
			return;
		}
		REMOVE_FRIEND_LIST_REQ req = (REMOVE_FRIEND_LIST_REQ)message_req;
		REMOVE_FRIEND_LIST_RES res = (REMOVE_FRIEND_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[34]*sendValues[61]/sendValues[8]/sendValues[15]/sendValues[26]/sendValues[63]+sendValues[68]-sendValues[99]-sendValues[91]+sendValues[11];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 67);
			return;
		}
		sendEnterServer(conn, create, sendValues, 67);
	}

	public static void handle_TRY_REMOVE_CHOUREN_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_REMOVE_CHOUREN_REQ))) {
			noFindReq(conn, message, 68);
			return;
		}
		TRY_REMOVE_CHOUREN_REQ req = (TRY_REMOVE_CHOUREN_REQ)message_req;
		TRY_REMOVE_CHOUREN_RES res = (TRY_REMOVE_CHOUREN_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[42]+sendValues[41]*sendValues[57]*sendValues[2]+sendValues[38]-sendValues[94]*sendValues[66]+sendValues[13]/sendValues[0]*sendValues[18];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 68);
			return;
		}
		sendEnterServer(conn, create, sendValues, 68);
	}

	public static void handle_REMOVE_CHOUREN_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REMOVE_CHOUREN_REQ))) {
			noFindReq(conn, message, 69);
			return;
		}
		REMOVE_CHOUREN_REQ req = (REMOVE_CHOUREN_REQ)message_req;
		REMOVE_CHOUREN_RES res = (REMOVE_CHOUREN_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[13]+sendValues[87]+sendValues[94]*sendValues[23]+sendValues[42]-sendValues[71]/sendValues[49]-sendValues[0]*sendValues[91]+sendValues[54];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 69);
			return;
		}
		sendEnterServer(conn, create, sendValues, 69);
	}

	public static void handle_DELETE_TASK_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DELETE_TASK_LIST_REQ))) {
			noFindReq(conn, message, 70);
			return;
		}
		DELETE_TASK_LIST_REQ req = (DELETE_TASK_LIST_REQ)message_req;
		DELETE_TASK_LIST_RES res = (DELETE_TASK_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[93]/sendValues[82]*sendValues[75]*sendValues[8]-sendValues[9]+sendValues[78]+sendValues[27]+sendValues[2]-sendValues[44]/sendValues[17];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 70);
			return;
		}
		sendEnterServer(conn, create, sendValues, 70);
	}

	public static void handle_PLAYER_TO_PLAYER_DEAL_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_TO_PLAYER_DEAL_REQ))) {
			noFindReq(conn, message, 71);
			return;
		}
		PLAYER_TO_PLAYER_DEAL_REQ req = (PLAYER_TO_PLAYER_DEAL_REQ)message_req;
		PLAYER_TO_PLAYER_DEAL_RES res = (PLAYER_TO_PLAYER_DEAL_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[85]-sendValues[6]-sendValues[16]+sendValues[86]-sendValues[1]+sendValues[37]/sendValues[51]/sendValues[4]-sendValues[26]-sendValues[15];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 71);
			return;
		}
		sendEnterServer(conn, create, sendValues, 71);
	}

	public static void handle_AUCTION_NEW_LIST_MSG_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof AUCTION_NEW_LIST_MSG_REQ))) {
			noFindReq(conn, message, 72);
			return;
		}
		AUCTION_NEW_LIST_MSG_REQ req = (AUCTION_NEW_LIST_MSG_REQ)message_req;
		AUCTION_NEW_LIST_MSG_RES res = (AUCTION_NEW_LIST_MSG_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[19]+sendValues[48]+sendValues[26]/sendValues[58]+sendValues[90]/sendValues[9]*sendValues[10]/sendValues[63]+sendValues[54]/sendValues[7];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 72);
			return;
		}
		sendEnterServer(conn, create, sendValues, 72);
	}

	public static void handle_REQUEST_BUY_PLAYER_INFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof REQUEST_BUY_PLAYER_INFO_REQ))) {
			noFindReq(conn, message, 73);
			return;
		}
		REQUEST_BUY_PLAYER_INFO_REQ req = (REQUEST_BUY_PLAYER_INFO_REQ)message_req;
		REQUEST_BUY_PLAYER_INFO_RES res = (REQUEST_BUY_PLAYER_INFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[38]+sendValues[33]-sendValues[24]+sendValues[64]+sendValues[29]-sendValues[98]+sendValues[41]/sendValues[83]*sendValues[69]+sendValues[26];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 73);
			return;
		}
		sendEnterServer(conn, create, sendValues, 73);
	}

	public static void handle_BOOTHER_PLAYER_MSGNAME_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof BOOTHER_PLAYER_MSGNAME_REQ))) {
			noFindReq(conn, message, 74);
			return;
		}
		BOOTHER_PLAYER_MSGNAME_REQ req = (BOOTHER_PLAYER_MSGNAME_REQ)message_req;
		BOOTHER_PLAYER_MSGNAME_RES res = (BOOTHER_PLAYER_MSGNAME_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[1]/sendValues[70]+sendValues[92]+sendValues[7]+sendValues[71]-sendValues[67]+sendValues[80]/sendValues[27]*sendValues[36]+sendValues[75];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 74);
			return;
		}
		sendEnterServer(conn, create, sendValues, 74);
	}

	public static void handle_BOOTHER_MSG_CLEAN_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof BOOTHER_MSG_CLEAN_REQ))) {
			noFindReq(conn, message, 75);
			return;
		}
		BOOTHER_MSG_CLEAN_REQ req = (BOOTHER_MSG_CLEAN_REQ)message_req;
		BOOTHER_MSG_CLEAN_RES res = (BOOTHER_MSG_CLEAN_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[20]+sendValues[54]*sendValues[48]/sendValues[35]-sendValues[32]+sendValues[46]-sendValues[62]+sendValues[86]+sendValues[77]/sendValues[29];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 75);
			return;
		}
		sendEnterServer(conn, create, sendValues, 75);
	}

	public static void handle_TRY_REQUESTBUY_CLEAN_ALL_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRY_REQUESTBUY_CLEAN_ALL_REQ))) {
			noFindReq(conn, message, 76);
			return;
		}
		TRY_REQUESTBUY_CLEAN_ALL_REQ req = (TRY_REQUESTBUY_CLEAN_ALL_REQ)message_req;
		TRY_REQUESTBUY_CLEAN_ALL_RES res = (TRY_REQUESTBUY_CLEAN_ALL_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[32]/sendValues[61]-sendValues[98]*sendValues[77]+sendValues[66]/sendValues[48]-sendValues[65]*sendValues[72]-sendValues[30]/sendValues[31];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 76);
			return;
		}
		sendEnterServer(conn, create, sendValues, 76);
	}

	public static void handle_SCHOOL_INFONAMES_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof SCHOOL_INFONAMES_REQ))) {
			noFindReq(conn, message, 77);
			return;
		}
		SCHOOL_INFONAMES_REQ req = (SCHOOL_INFONAMES_REQ)message_req;
		SCHOOL_INFONAMES_RES res = (SCHOOL_INFONAMES_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[61]+sendValues[75]/sendValues[67]+sendValues[48]+sendValues[91]+sendValues[22]*sendValues[60]+sendValues[88]+sendValues[82]/sendValues[89];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 77);
			return;
		}
		sendEnterServer(conn, create, sendValues, 77);
	}

	public static void handle_PET_NEW_LEVELUP_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_NEW_LEVELUP_REQ))) {
			noFindReq(conn, message, 78);
			return;
		}
		PET_NEW_LEVELUP_REQ req = (PET_NEW_LEVELUP_REQ)message_req;
		PET_NEW_LEVELUP_RES res = (PET_NEW_LEVELUP_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[10]*sendValues[97]*sendValues[38]*sendValues[8]+sendValues[22]*sendValues[82]*sendValues[13]+sendValues[40]+sendValues[61]+sendValues[69];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 78);
			return;
		}
		sendEnterServer(conn, create, sendValues, 78);
	}

	public static void handle_VALIDATE_ASK_NEW_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof VALIDATE_ASK_NEW_REQ))) {
			noFindReq(conn, message, 79);
			return;
		}
		VALIDATE_ASK_NEW_REQ req = (VALIDATE_ASK_NEW_REQ)message_req;
		VALIDATE_ASK_NEW_RES res = (VALIDATE_ASK_NEW_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[1]*sendValues[72]+sendValues[52]+sendValues[56]*sendValues[14]*sendValues[48]/sendValues[82]+sendValues[0]+sendValues[23]/sendValues[25];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 79);
			return;
		}
		sendEnterServer(conn, create, sendValues, 79);
	}

	public static void handle_JINGLIAN_NEW_TRY_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof JINGLIAN_NEW_TRY_REQ))) {
			noFindReq(conn, message, 80);
			return;
		}
		JINGLIAN_NEW_TRY_REQ req = (JINGLIAN_NEW_TRY_REQ)message_req;
		JINGLIAN_NEW_TRY_RES res = (JINGLIAN_NEW_TRY_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[63]+sendValues[20]*sendValues[81]*sendValues[58]+sendValues[35]*sendValues[93]+sendValues[88]+sendValues[78]+sendValues[60]*sendValues[38];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 80);
			return;
		}
		sendEnterServer(conn, create, sendValues, 80);
	}

	public static void handle_JINGLIAN_NEW_DO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof JINGLIAN_NEW_DO_REQ))) {
			noFindReq(conn, message, 81);
			return;
		}
		JINGLIAN_NEW_DO_REQ req = (JINGLIAN_NEW_DO_REQ)message_req;
		JINGLIAN_NEW_DO_RES res = (JINGLIAN_NEW_DO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[67]+sendValues[94]-sendValues[63]+sendValues[59]+sendValues[29]+sendValues[8]-sendValues[41]+sendValues[40]-sendValues[42]+sendValues[26];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 81);
			return;
		}
		sendEnterServer(conn, create, sendValues, 81);
	}

	public static void handle_JINGLIAN_PET_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof JINGLIAN_PET_REQ))) {
			noFindReq(conn, message, 82);
			return;
		}
		JINGLIAN_PET_REQ req = (JINGLIAN_PET_REQ)message_req;
		JINGLIAN_PET_RES res = (JINGLIAN_PET_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[33]-sendValues[27]+sendValues[77]+sendValues[29]-sendValues[55]/sendValues[23]-sendValues[39]*sendValues[14]/sendValues[13]-sendValues[66];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 82);
			return;
		}
		sendEnterServer(conn, create, sendValues, 82);
	}

	public static void handle_SEE_NEWFRIEND_LIST_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof SEE_NEWFRIEND_LIST_REQ))) {
			noFindReq(conn, message, 83);
			return;
		}
		SEE_NEWFRIEND_LIST_REQ req = (SEE_NEWFRIEND_LIST_REQ)message_req;
		SEE_NEWFRIEND_LIST_RES res = (SEE_NEWFRIEND_LIST_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[5]-sendValues[40]/sendValues[98]-sendValues[73]*sendValues[10]-sendValues[4]/sendValues[21]-sendValues[7]/sendValues[15]/sendValues[58];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 83);
			return;
		}
		sendEnterServer(conn, create, sendValues, 83);
	}

	public static void handle_EQU_PET_HUN_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof EQU_PET_HUN_REQ))) {
			noFindReq(conn, message, 84);
			return;
		}
		EQU_PET_HUN_REQ req = (EQU_PET_HUN_REQ)message_req;
		EQU_PET_HUN_RES res = (EQU_PET_HUN_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[3]-sendValues[16]-sendValues[97]/sendValues[15]+sendValues[66]*sendValues[41]/sendValues[5]/sendValues[32]-sendValues[35]/sendValues[55];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 84);
			return;
		}
		sendEnterServer(conn, create, sendValues, 84);
	}

	public static void handle_PET_ADD_HUNPO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_ADD_HUNPO_REQ))) {
			noFindReq(conn, message, 85);
			return;
		}
		PET_ADD_HUNPO_REQ req = (PET_ADD_HUNPO_REQ)message_req;
		PET_ADD_HUNPO_RES res = (PET_ADD_HUNPO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[7]/sendValues[76]+sendValues[93]*sendValues[46]/sendValues[69]/sendValues[75]*sendValues[59]*sendValues[47]*sendValues[52]+sendValues[72];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 85);
			return;
		}
		sendEnterServer(conn, create, sendValues, 85);
	}

	public static void handle_PET_ADD_SHENGMINGVALUE_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_ADD_SHENGMINGVALUE_REQ))) {
			noFindReq(conn, message, 86);
			return;
		}
		PET_ADD_SHENGMINGVALUE_REQ req = (PET_ADD_SHENGMINGVALUE_REQ)message_req;
		PET_ADD_SHENGMINGVALUE_RES res = (PET_ADD_SHENGMINGVALUE_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[81]+sendValues[56]+sendValues[1]-sendValues[39]+sendValues[44]+sendValues[88]+sendValues[57]+sendValues[92]-sendValues[82]-sendValues[43];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 86);
			return;
		}
		sendEnterServer(conn, create, sendValues, 86);
	}

	public static void handle_HORSE_REMOVE_HUNPO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof HORSE_REMOVE_HUNPO_REQ))) {
			noFindReq(conn, message, 87);
			return;
		}
		HORSE_REMOVE_HUNPO_REQ req = (HORSE_REMOVE_HUNPO_REQ)message_req;
		HORSE_REMOVE_HUNPO_RES res = (HORSE_REMOVE_HUNPO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[0]-sendValues[3]*sendValues[22]/sendValues[51]+sendValues[56]+sendValues[43]/sendValues[90]*sendValues[67]/sendValues[36]-sendValues[48];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 87);
			return;
		}
		sendEnterServer(conn, create, sendValues, 87);
	}

	public static void handle_PET_REMOVE_HUNPO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PET_REMOVE_HUNPO_REQ))) {
			noFindReq(conn, message, 88);
			return;
		}
		PET_REMOVE_HUNPO_REQ req = (PET_REMOVE_HUNPO_REQ)message_req;
		PET_REMOVE_HUNPO_RES res = (PET_REMOVE_HUNPO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[42]*sendValues[34]-sendValues[68]+sendValues[93]*sendValues[63]+sendValues[7]/sendValues[44]+sendValues[67]+sendValues[64]+sendValues[54];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 88);
			return;
		}
		sendEnterServer(conn, create, sendValues, 88);
	}

	public static void handle_PLAYER_CLEAN_HORSEHUNLIANG_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_CLEAN_HORSEHUNLIANG_REQ))) {
			noFindReq(conn, message, 89);
			return;
		}
		PLAYER_CLEAN_HORSEHUNLIANG_REQ req = (PLAYER_CLEAN_HORSEHUNLIANG_REQ)message_req;
		PLAYER_CLEAN_HORSEHUNLIANG_RES res = (PLAYER_CLEAN_HORSEHUNLIANG_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[88]*sendValues[6]*sendValues[95]+sendValues[39]+sendValues[8]+sendValues[27]/sendValues[85]-sendValues[45]*sendValues[94]*sendValues[5];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 89);
			return;
		}
		sendEnterServer(conn, create, sendValues, 89);
	}

	public static void handle_GET_NEW_LEVELUP_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GET_NEW_LEVELUP_REQ))) {
			noFindReq(conn, message, 90);
			return;
		}
		GET_NEW_LEVELUP_REQ req = (GET_NEW_LEVELUP_REQ)message_req;
		GET_NEW_LEVELUP_RES res = (GET_NEW_LEVELUP_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[78]*sendValues[10]/sendValues[92]-sendValues[45]/sendValues[93]*sendValues[70]*sendValues[48]-sendValues[8]/sendValues[75]-sendValues[94];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 90);
			return;
		}
		sendEnterServer(conn, create, sendValues, 90);
	}

	public static void handle_DO_HOSEE2OTHER_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DO_HOSEE2OTHER_REQ))) {
			noFindReq(conn, message, 91);
			return;
		}
		DO_HOSEE2OTHER_REQ req = (DO_HOSEE2OTHER_REQ)message_req;
		DO_HOSEE2OTHER_RES res = (DO_HOSEE2OTHER_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[69]/sendValues[40]+sendValues[76]*sendValues[22]-sendValues[30]+sendValues[43]/sendValues[36]+sendValues[62]*sendValues[47]-sendValues[1];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 91);
			return;
		}
		sendEnterServer(conn, create, sendValues, 91);
	}

	public static void handle_TRYDELETE_PET_INFO_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof TRYDELETE_PET_INFO_REQ))) {
			noFindReq(conn, message, 92);
			return;
		}
		TRYDELETE_PET_INFO_REQ req = (TRYDELETE_PET_INFO_REQ)message_req;
		TRYDELETE_PET_INFO_RES res = (TRYDELETE_PET_INFO_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[3]-sendValues[72]*sendValues[62]+sendValues[98]/sendValues[36]/sendValues[69]/sendValues[86]-sendValues[1]*sendValues[75]-sendValues[44];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 92);
			return;
		}
		sendEnterServer(conn, create, sendValues, 92);
	}

	public static void handle_HAHA_ACTIVITY_MSG_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof HAHA_ACTIVITY_MSG_REQ))) {
			noFindReq(conn, message, 93);
			return;
		}
		HAHA_ACTIVITY_MSG_REQ req = (HAHA_ACTIVITY_MSG_REQ)message_req;
		HAHA_ACTIVITY_MSG_RES res = (HAHA_ACTIVITY_MSG_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[93]*sendValues[15]/sendValues[33]+sendValues[79]-sendValues[53]*sendValues[74]*sendValues[7]/sendValues[12]/sendValues[21]*sendValues[59];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 93);
			return;
		}
		sendEnterServer(conn, create, sendValues, 93);
	}

	public static void handle_VALIDATE_NEW_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof VALIDATE_NEW_REQ))) {
			noFindReq(conn, message, 94);
			return;
		}
		VALIDATE_NEW_REQ req = (VALIDATE_NEW_REQ)message_req;
		VALIDATE_NEW_RES res = (VALIDATE_NEW_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[60]*sendValues[81]/sendValues[29]-sendValues[93]/sendValues[34]*sendValues[79]/sendValues[59]/sendValues[41]/sendValues[38]+sendValues[70];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 94);
			return;
		}
		sendEnterServer(conn, create, sendValues, 94);
	}

	public static void handle_VALIDATE_ANDSWER_NEW_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof VALIDATE_ANDSWER_NEW_REQ))) {
			noFindReq(conn, message, 95);
			return;
		}
		VALIDATE_ANDSWER_NEW_REQ req = (VALIDATE_ANDSWER_NEW_REQ)message_req;
		VALIDATE_ANDSWER_NEW_RES res = (VALIDATE_ANDSWER_NEW_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[17]-sendValues[49]+sendValues[12]/sendValues[99]-sendValues[22]*sendValues[74]*sendValues[10]+sendValues[53]+sendValues[95]-sendValues[75];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 95);
			return;
		}
		sendEnterServer(conn, create, sendValues, 95);
	}

	public static void handle_PLAYER_ASK_TO_OTHWE_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof PLAYER_ASK_TO_OTHWE_REQ))) {
			noFindReq(conn, message, 96);
			return;
		}
		PLAYER_ASK_TO_OTHWE_REQ req = (PLAYER_ASK_TO_OTHWE_REQ)message_req;
		PLAYER_ASK_TO_OTHWE_RES res = (PLAYER_ASK_TO_OTHWE_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[74]+sendValues[27]*sendValues[42]-sendValues[41]/sendValues[58]*sendValues[56]/sendValues[96]*sendValues[39]*sendValues[44]/sendValues[50];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 96);
			return;
		}
		sendEnterServer(conn, create, sendValues, 96);
	}

	public static void handle_GA_GET_SOME_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof GA_GET_SOME_REQ))) {
			noFindReq(conn, message, 97);
			return;
		}
		GA_GET_SOME_REQ req = (GA_GET_SOME_REQ)message_req;
		GA_GET_SOME_RES res = (GA_GET_SOME_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[85]-sendValues[77]-sendValues[76]-sendValues[13]-sendValues[36]+sendValues[72]*sendValues[54]+sendValues[90]/sendValues[28]-sendValues[38];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 97);
			return;
		}
		sendEnterServer(conn, create, sendValues, 97);
	}

	public static void handle_OTHER_PET_LEVELUP_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof OTHER_PET_LEVELUP_REQ))) {
			noFindReq(conn, message, 98);
			return;
		}
		OTHER_PET_LEVELUP_REQ req = (OTHER_PET_LEVELUP_REQ)message_req;
		OTHER_PET_LEVELUP_RES res = (OTHER_PET_LEVELUP_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[70]+sendValues[33]/sendValues[14]/sendValues[18]*sendValues[21]-sendValues[23]-sendValues[55]+sendValues[22]-sendValues[57]/sendValues[50];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 98);
			return;
		}
		sendEnterServer(conn, create, sendValues, 98);
	}

	public static void handle_MY_OTHER_FRIEDN_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof MY_OTHER_FRIEDN_REQ))) {
			noFindReq(conn, message, 99);
			return;
		}
		MY_OTHER_FRIEDN_REQ req = (MY_OTHER_FRIEDN_REQ)message_req;
		MY_OTHER_FRIEDN_RES res = (MY_OTHER_FRIEDN_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[72]*sendValues[38]+sendValues[94]+sendValues[35]*sendValues[5]-sendValues[54]+sendValues[53]/sendValues[46]+sendValues[52]+sendValues[15];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 99);
			return;
		}
		sendEnterServer(conn, create, sendValues, 99);
	}

	public static void handle_DOSOME_SB_MSG_RES(Connection conn, ResponseMessage message){
		Object message_req = conn.getAttachmentData("SEND_HAND_MESSAGE_NEW");
		if (message_req == null || (!(message_req instanceof DOSOME_SB_MSG_REQ))) {
			noFindReq(conn, message, 100);
			return;
		}
		DOSOME_SB_MSG_REQ req = (DOSOME_SB_MSG_REQ)message_req;
		DOSOME_SB_MSG_RES res = (DOSOME_SB_MSG_RES)message;
		long re = res.getCreate();
		long[] sendValues = req.getSendValues();
		long create = 0;
		create = sendValues[26]/sendValues[80]*sendValues[76]+sendValues[57]+sendValues[91]+sendValues[55]/sendValues[42]+sendValues[40]*sendValues[82]+sendValues[43];
		if (create != re) {
			backValueNE(conn, create, re, sendValues, 100);
			return;
		}
		sendEnterServer(conn, create, sendValues, 100);
	}
	
	public static class ClientInfo {
		public long pID;
		public String userName;
		public String pName;
		public long creatTime;
		public String messageName;
		public boolean isMessageRight;
		public boolean isBack;
		public boolean isBackRight;
		public String[] backInfo = null;
		public ClientInfo (long pID, String userName, String pName, long creatTime, String messageName) {
			this.pID = pID;
			this.userName = userName;
			this.pName = pName;
			this.creatTime = creatTime;
			this.messageName = messageName;
			isBack = false;
			isMessageRight = false;
			isBackRight = false;
		}
	}
}
