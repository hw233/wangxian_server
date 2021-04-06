package com.fy.engineserver.chuangong;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.gateway.GameSubSystem;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.APPLY_CHUANGONG_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class ChuanGongSubSystem extends SubSystemAdapter {
	
	
//	public static Logger logger = Logger.getLogger(ChuanGongSubSystem.class);
public	static Logger logger = LoggerFactory.getLogger(ChuanGongManager.class);
	
	public String[] getInterestedMessageTypes() {
		return new String[] {"ACCEPT_CHUANGONG_ARTICLE_REQ","APPLY_CHUANGONG_REQ","FINISH_CHUANGONG_REQ"};
	}
	
	private ChuanGongManager chuanGongManager;
	@Override
	public String getName() {
		
		return "ChuanGongSubSystem";
	}

	public ChuanGongManager getChuanGongManager() {
		return chuanGongManager;
	}

	public void setChuanGongManager(ChuanGongManager chuanGongManager) {
		this.chuanGongManager = chuanGongManager;
	}

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * TODO 如果此方法已经处理了请求消息，但是不需要返回响应，那么最后也会return null<br>
	 * 这样一来，调用者就无法区分出消息是否已经被处理了
	 */
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}
		try {
			Class clazz = this.getClass();
			Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
			ResponseMessage res = null;
			m1.setAccessible(true);
			res = (ResponseMessage) m1.invoke(this, conn, message, player);
			return res;
		} catch (InvocationTargetException e) {
			// TODO: handle exception
			e.getTargetException().printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 得到传功石
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ACCEPT_CHUANGONG_ARTICLE_REQ(Connection conn,RequestMessage message, Player player) {
		
		try {
			String result = this.chuanGongManager.acceptChuangongArticle(player);
			if(!result.equals("")){
				player.send_HINT_REQ(result);
			}
		} catch (Exception e) {
			logger.error("[领取传功石错误] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
		return null;
	}
	
	/**
	 * 申请传功
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_APPLY_CHUANGONG_REQ(Connection conn,RequestMessage message, Player player) {
		APPLY_CHUANGONG_REQ req = (APPLY_CHUANGONG_REQ)message;
		Player passive;
		try {
			passive = PlayerManager.getInstance().getPlayer(req.getPlayerId());
			String result = this.chuanGongManager.applyChuangong(player, passive);
			if(!result.equals("")){
				player.send_HINT_REQ(result);
			}
		} catch (Exception e) {
			logger.error("[申请传功错误] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}

		return null;
	}
	
	
//	/**
//	 * 完成传功
//	 * @param conn
//	 * @param message
//	 * @param player
//	 * @return
//	 */
//	public ResponseMessage handle_FINISH_CHUANGONG_REQ(Connection conn,RequestMessage message, Player player) {
//		FINISH_CHUANGONG_REQ req = (FINISH_CHUANGONG_REQ)message;
//		long cgId = req.getCgId();
//		this.chuanGongManager.finishChuangong(cgId);
//		return null;
//	}
}
