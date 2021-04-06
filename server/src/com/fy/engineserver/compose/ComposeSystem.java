package com.fy.engineserver.compose;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.ENTER_COMPOSE_REQ;
import com.fy.engineserver.message.ENTER_COMPOSE_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class ComposeSystem extends SubSystemAdapter{
	
	public static Logger logger = ComposeManager.logger;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ComposeSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[] {"ENTER_COMPOSE_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, logger);
		if (logger.isInfoEnabled()) logger.info("[收到玩家的活动请求]{}:{}", new Object[] { player.getName(), message.getTypeDescription() });
		
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}
		Class<?> clazz = this.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		m1.setAccessible(true);
		Object obj = m1.invoke(this, conn, message, player);
		return obj == null ? null : (ResponseMessage) obj;
	}
	/**
	 * 物品合成
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ENTER_COMPOSE_REQ(Connection conn, RequestMessage message, Player player) {
		ENTER_COMPOSE_REQ req = (ENTER_COMPOSE_REQ) message;
		if(req.getPropIds().length <= 0 || req.getPropNums().length <= 0) {
			logger.warn("[合成] [发过来的物品为空][" + player.getLogString() + "][" + req.getPropIds().length + "][" + req.getPropNums().length + "]");
			return null;
		}
		byte result = 0;
		int resultNum = 0;
		long atId = 0;
		long[] meteriaIds = req.getPropIds();
		if(req.getComposetype() == ComposeConstant.compost4Color) {
			int num = req.getPropNums()[0];
			long[] temp = new long[num];
			for(int i=0; i<temp.length; i++) {
				temp[i] = meteriaIds[0];
			}
			meteriaIds = temp;
			logger.info("[新合成] [提升颜色] [" + meteriaIds.length + "]");
		}
		try {
			List<ArticleEntity> rList = ComposeManager.instance.composeArticle(player, req.getComposetype(), req.getCostType(), meteriaIds, false);
			if(rList != null && rList.size() > 0) {
				result = 1;
				resultNum = rList.size();
				atId = rList.get(0).getId();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[合成系统] [合成物品出错][" + player.getLogString() + "]", e);
		}
		ENTER_COMPOSE_RES res = new ENTER_COMPOSE_RES(message.getSequnceNum(), req.getComposetype(), result, atId, resultNum);
		return res;
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
