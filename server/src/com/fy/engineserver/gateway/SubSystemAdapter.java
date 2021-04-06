package com.fy.engineserver.gateway;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 便于使用缓存的Method反射。
 * 
 *         create on 2013年7月25日
 */
public abstract class SubSystemAdapter implements GameSubSystem {

	public Map<Integer, Method> methodCache = new HashMap<Integer, Method>();
	public boolean useMethodCache = false;

	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message, Player player) throws ConnectionException, Exception {
		int id = message.getType();
		Method m1 = methodCache.get(id);
		if (m1 == null) {
			Class<?> clazz = this.getClass();
			m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
			m1.setAccessible(true);
			methodCache.put(id, m1);
		}
		ResponseMessage res = null;
		try {
			res = (ResponseMessage) m1.invoke(this, conn, message, player);
		} catch (Exception e) {
			GameNetworkFramework.logger.error("[协议处理异常] [method:" + m1 + "/" + message.getTypeDescription() + "] [conn:" + conn + "] [message:" + message + "] [player:" + player + "]", e);
			return null;
		}
		return res;
	}

	public Player check(Connection conn, RequestMessage message, Logger logger) throws ConnectionException {
		Player player = (Player) conn.getAttachmentData("Player");
		if (player != null) {
			player.setLastRequestTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		} else {
			// logger.warn("[警告:连接上没有玩家信息] [断开连接] [" + message.getTypeDescription() + "]");
			if (logger.isWarnEnabled()) logger.warn("[警告:连接上没有玩家信息] [断开连接] [{}]", new Object[] { message.getTypeDescription() });
			throw new ConnectionException("警告:连接上没有玩家信息，断开连接");
		}
		Passport passport = (Passport) conn.getAttachment();
		if (passport == null) {
			// logger.warn("[警告:连接上没有账号信息] [断开连接] [" + message.getTypeDescription() + "]");
			if (logger.isWarnEnabled()) logger.warn("[警告:连接上没有账号信息] [断开连接] [{}]", new Object[] { message.getTypeDescription() });
			throw new ConnectionException("警告:连接上没有账号信息，断开连接");
		}
		return player;
	}
}
