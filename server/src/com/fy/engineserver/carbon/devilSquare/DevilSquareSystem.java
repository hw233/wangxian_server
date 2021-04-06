package com.fy.engineserver.carbon.devilSquare;

import java.lang.reflect.Method;

import org.slf4j.Logger;

import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.ENTER_DEVILSQUARE_REQ;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class DevilSquareSystem extends SubSystemAdapter{
	
	public static Logger logger = DevilSquareManager.logger;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "DevilSquareSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[] {"ENTER_DEVILSQUARE_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, logger);
		if (logger.isInfoEnabled()) logger.info("[收到玩家的活动请求]{}:{}", new Object[] { player.getName(), message.getTypeDescription() });
		if (message instanceof ENTER_DEVILSQUARE_REQ) {
			return handle_ENTER_DEVILSQUARE_REQ(conn, message, player);
		}
		return null;
	}
	/**
	 * 进入副本
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ENTER_DEVILSQUARE_REQ(Connection conn, RequestMessage message, Player player) {
		ENTER_DEVILSQUARE_REQ req = (ENTER_DEVILSQUARE_REQ) message;
		DevilSquareManager.instance.notifyEnterCarbon(player, req.getCarbonLevel(), false);
		return null;
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
