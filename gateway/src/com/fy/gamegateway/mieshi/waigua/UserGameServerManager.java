package com.fy.gamegateway.mieshi.waigua;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fy.gamegateway.message.GameMessageFactory;
import com.fy.gamegateway.message.NOTIFY_SERVER_TIREN_REQ;
import com.fy.gamegateway.message.NOTIFY_USER_ENTERSERVER_REQ;
import com.fy.gamegateway.message.NOTIFY_USER_LEAVESERVER_REQ;
import com.fy.gamegateway.mieshi.server.MieshiGatewayUpdateResourceServer;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.SelectorPolicy;

public class UserGameServerManager {

	public static Logger logger = Logger.getLogger(NewLoginHandler.class);
	
	public static UserGameServerManager instance = new UserGameServerManager();
	
	//用户名做key的用户登录信息
	public Map<String, UserGameServerEntity> usernameMap = new ConcurrentHashMap<String, UserGameServerEntity>();
	//ip和mac地址做key的用户登录信息
	public Map<String, UserGameServerEntity> ipMacMap = new ConcurrentHashMap<String, UserGameServerEntity>();
	
	public void handle_NOTIFY_USER_LEAVESERVER_REQ (Connection conn, NOTIFY_USER_LEAVESERVER_REQ req) {
		try {
			String username = req.getUsername();
			String macAddress = req.getMACADDRESS();
			String ipAddress = req.getIpAddress();
			String serverName = req.getServername();
			UserGameServerEntity old = usernameMap.get(username);
			if (old != null) {
				if (old.servername.equals(serverName)) {
					usernameMap.remove(username);
				}
			}
			old = null;
			old = ipMacMap.get(ipAddress + macAddress);
			if (old != null) {
				if (old.servername.equals(serverName) && old.username.equals(username)) {
					ipMacMap.remove(ipAddress + macAddress);
				}
			}
			logger.warn("[用户离开游戏服] ["+username+"] ["+macAddress+"] ["+serverName+"]");
		}catch(Exception e) {
			logger.error("handle_NOTIFY_USER_LEAVESERVER_REQ出错  ["+conn.getName()+"] ["+req.getUsername()+"] [us="+usernameMap.size()+"] [is="+ipMacMap.size()+"]", e);
		}
	}
	
	/**
	 * 处理角色进入游戏服，检查用户是不是在其他游戏服登录过，如果有就给那个服务器发送T人协议
	 * 检查相同IP和mac地址是不是有人在其他服务器登录过，如果有也发送T人协议
	 * @param conn
	 * @param req
	 */
	public void handle_NOTIFY_USER_ENTERSERVER_REQ (Connection conn, NOTIFY_USER_ENTERSERVER_REQ req) {
		try{
			UserGameServerEntity entity = new UserGameServerEntity(req.getServername(), req.getUsername(), req.getSessionId(), req.getClientId(), req.getChannel(), req.getMACADDRESS(), req.getIpAddress(), req.getPlatform(), req.getPhoneType(), req.getNetwork(), req.getGpuOtherName());
			UserGameServerEntity old = usernameMap.get(entity.username);
			if (old != null) {
				if (!old.servername.equals(entity.servername)) {
					Connection otherConn = null;
					try {
						otherConn = MieshiGatewayUpdateResourceServer.getInstance().getConnectionSelector().takeoutConnection(new SelectorPolicy.IdentitySelectorPolicy(old.servername), 100L);
					} catch (IOException e) {
						logger.error("[取其他服务器连接出错] ["+old.servername+"] ["+entity.logString+"]", e);
					}
					if (otherConn == null) {
						logger.warn("[连接未取到] ["+old.servername+"] ["+entity.logString+"]");
					}else {
						try{
							otherConn.writeMessage(new NOTIFY_SERVER_TIREN_REQ(GameMessageFactory.nextSequnceNum(), old.username, old.servername + "有角色登录。"), false);
							logger.warn("[踢出其他服务器玩家] ["+otherConn.getName()+"] ["+old.servername+"] ["+entity.logString+"]");
						}catch (Exception e) {
							logger.error("[发送T人出错:] ["+entity.logString+"]", e);
						}
					}
				}
			}
			old = null;
			old = ipMacMap.get(entity.ipAddress + entity.MACADDRESS);
			if (old != null && !(entity.MACADDRESS.equals("02:00:00:00:00:00") && entity.platform.equalsIgnoreCase("IOS"))) {
				if (!old.servername.equals(entity.servername) || !(old.username.equals(entity.username))) {
					Connection otherConn = null;
					try {
						otherConn = MieshiGatewayUpdateResourceServer.getInstance().getConnectionSelector().takeoutConnection(new SelectorPolicy.IdentitySelectorPolicy(old.servername), 100L);
					} catch (IOException e) {
						logger.error("[取其他服务器连接出错] ["+old.servername+"] ["+entity.logString+"]", e);
					}
					if (otherConn == null) {
						logger.warn("[连接未取到] ["+old.servername+"] ["+entity.logString+"]");
					}else {
						try{
							otherConn.writeMessage(new NOTIFY_SERVER_TIREN_REQ(GameMessageFactory.nextSequnceNum(), old.username, "ipMac有角色登录。"), false);
							logger.warn("[ipmac相同踢出玩家] ["+otherConn.getName()+"] ["+old.servername+"] ["+old.logString+"] ["+entity.logString+"]");
						}catch (Exception e) {
							logger.error("[发送T人出错:] ["+entity.logString+"]", e);
						}
					}
				}
			}
			
			usernameMap.put(entity.username, entity);
			if (entity.MACADDRESS.length() > 0) {
				ipMacMap.put(entity.ipAddress + entity.MACADDRESS, entity);
			}
			logger.warn("[用户进入游戏服] ["+entity.logString+"] [us="+usernameMap.size()+"] [is="+ipMacMap.size()+"]");
//			Connection otherConn = null;
//			try {
//				otherConn = MieshiGatewayUpdateResourceServer.getInstance().getConnectionSelector().takeoutConnection(new SelectorPolicy.IdentitySelectorPolicy(entity.servername), 1000L);
//			} catch (IOException e) {
//				logger.error("取连接出错",e);
//			}
//			if (otherConn == null) {
//				logger.warn("连接找不到？");
//			}else {
//				try{
//					otherConn.writeMessage(new NOTIFY_SERVER_TIREN_REQ(GameMessageFactory.nextSequnceNum(), entity.username, entity.servername + "有角色登录。"), false);
//					logger.warn("[测试T人] ["+otherConn.getName()+"] ["+entity.logString+"]");
//				}catch (Exception e) {
//					logger.error("[发送T人出错:] ["+entity.logString+"]", e);
//				}
//			}
		}catch(Exception e) {
			logger.error("handle_NOTIFY_USER_ENTERSERVER_REQ出错:", e);
		}
	}
	
}
