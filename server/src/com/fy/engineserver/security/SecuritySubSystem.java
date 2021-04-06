package com.fy.engineserver.security;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.GameSubSystem;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 安全检查子系统，所有的请求包都都经过此系统
 * 
 * 
 * 
 *
 */
public class SecuritySubSystem implements GameSubSystem {

//	protected static Logger logger = Logger.getLogger(SecuritySubSystem.class);
public	static Logger logger = LoggerFactory.getLogger(SecuritySubSystem.class);

	public final static String HASLOGIN = "hasLogin";
	public final static String USERNAME = "username";
	public final static String MACADDRESS = "macAddress";
	
	protected PlayerManager playerManager;
	
	public String[] getInterestedMessageTypes() {
		return new String[]{
		};
	}

	GameNetworkFramework framework;
	
	public void setGameNetworkFramework(GameNetworkFramework framework){
		this.framework = framework;
	}
	
	public String getName() {
		return "SecuritySubSystem";
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		//Boolean b = (Boolean)conn.getAttachmentData(HASLOGIN);
		Boolean b = true;
		if(b == null || b.booleanValue() == false){
			if(logger.isWarnEnabled())
				logger.warn("[{}] [illegal_attack] [not_login] [{}] [packet:{}]", new Object[]{getName(),message.getTypeDescription(),message.getLength()});
			throw new ConnectionException("connection[--] not login when receive message ["+message.getTypeDescription()+"]!");
		}
		return null;
	}

	public boolean handleResponseMessage(Connection conn,RequestMessage req,
			ResponseMessage message) throws ConnectionException, Exception {
	
		return false;
	}

}
