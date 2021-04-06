package com.fy.engineserver.downcity.downcity2;

import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.PLAY_DOWNCITY_TUN_REQ;
import com.fy.engineserver.message.PLAY_DOWNCITY_TUN_RES;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class DownCitySystem2 extends SubSystemAdapter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "DownCitySystem2";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[]{"PLAY_DOWNCITY_TUN_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, PlayerAimManager.logger);
		if(PlayerAimManager.logger.isDebugEnabled()) {
			DownCityManager2.logger.debug("[收到玩家请求] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]");
		}
		try {
			if (message instanceof PLAY_DOWNCITY_TUN_REQ) {
				return handle_PLAY_DOWNCITY_TUN_REQ(conn, message, player);
			}
		} catch(Exception e) {
			DownCityManager2.logger.error("[新副本] [处理目标协议出错] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]", e);
		}
		return null;
	}
	
	public ResponseMessage handle_PLAY_DOWNCITY_TUN_REQ(Connection conn, RequestMessage message, Player player) {
		long[] result = DownCityManager2.instance.startTun(player, false);
		if (result[0] < 0) {
			return null;
		}
		long resultId = result[0];
		int num = (int) result[1];
		int left = (int) result[2];
		PLAY_DOWNCITY_TUN_RES resp = new PLAY_DOWNCITY_TUN_RES(message.getSequnceNum(), resultId, num, left);
		return resp;
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
