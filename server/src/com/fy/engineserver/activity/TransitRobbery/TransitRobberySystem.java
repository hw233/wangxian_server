package com.fy.engineserver.activity.TransitRobbery;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Random;

import org.slf4j.Logger;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyDataModel;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyManager;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.PlayerMessagePair;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.ENTER_ROBBERT_RES;
import com.fy.engineserver.message.QUERY_DUJIE_SKILL_INFO_RES;
import com.fy.engineserver.message.ROBBERY_TIPS_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tool.GlobalTool;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class TransitRobberySystem extends SubSystemAdapter{
	public static Logger logger = TransitRobberyManager.logger;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "TransitRobberySystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[] {"ENTER_ROBBERT_REQ","ROBBERY_TIPS_REQ","START_ROBBERT_REQ", "QUERY_DUJIE_SKILL_INFO_REQ", "CHANAGE_PLAYER_AVATA_REQ","BACE_TOWN_IN_ROBBERT_REQ","FEISHENG_END_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
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
	 * 进入天劫
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ENTER_ROBBERT_REQ(Connection conn, RequestMessage message, Player player){
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		if(entity == null || entity.getCurrentLevel() >= 9){
			return null;
		}
		String result = GlobalTool.verifyMapTrans(player.getId());
		if(result != null) {
			player.sendError(result);
			if(logger.isInfoEnabled()) {
				logger.info("[渡劫] [不允许渡劫] [原因:" + result + "][" + player.getLogString() + "]");
			}
			return null;
		}
		if(WolfManager.signList.contains(player.getId())){
			if(logger.isInfoEnabled()) {
				logger.info("[渡劫] [不允许渡劫] [原因:报名了小羊活动] [" + player.getLogString() + "]");
			}
			player.sendError(Translate.副本不能渡劫提示);
			return null;
		}
		int currentLevel = entity.getCurrentLevel() + 1;
		RobberyDataModel rmd = TransitRobberyManager.getInstance().getRobberyDataModel(currentLevel);
		ENTER_ROBBERT_RES req = new ENTER_ROBBERT_RES(message.getSequnceNum(), currentLevel, (byte) 0, rmd.getTips2());
		if(!TransitRobberyManager.getInstance().ready2EnterRobberyScene(player, false)){
			return null;
		}
		return req;
	}
	/**
	 * 渡劫中回城
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BACE_TOWN_IN_ROBBERT_REQ(Connection conn, RequestMessage message, Player player){
		TransitRobberyManager.logger.info("收到开始渡劫的协议！");
		TransitRobberyManager.getInstance().回城(player, false);
		return null;
	}
	/**
	 * 飞升完成过图
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_FEISHENG_END_REQ(Connection conn, RequestMessage message, Player player){
		//过图===========
		GameInfo gi = GameManager.getInstance().getGameInfo("wanhuaxiangu");
		Game currentGame = player.getCurrentGame();
		if(gi == null){
			logger.error("[飞升][取得飞升gi出错==========" + player.getLogString() + "]");
		}
		if(currentGame == null){
			logger.error("[飞升][玩家当前地图未空==========" + player.getLogString() + "]");
		}
		MapArea area = gi.getMapAreaByName(Translate.出生点);
		int bornX = 300;
		int bornY = 300;
		if (area != null) {
			Random random = new Random();
			bornX = area.getX() + random.nextInt(area.getWidth());
			bornY = area.getY() + random.nextInt(area.getHeight());
		}
		currentGame.transferGame(player, new TransportData(0, 0, 0, 0, "wanhuaxiangu", bornX, bornY));
		return null;
	}
	/**
	 * 天劫心法
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_DUJIE_SKILL_INFO_REQ(Connection conn, RequestMessage message, Player player){
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		int robberyLevel = 0;
		if(entity != null){
			robberyLevel = entity.getCurrentLevel();
		}
		int[] skillOneLevels = RobberyConstant.diyitaoxinfa;
		int[] needOneDuJies = new int[]{0,1,2,3,4,5,6,7,8};
		int[] skillTwoLevels = RobberyConstant.diertaoxinfa;
		int[] needTwoDuJies = new int[]{8,9};
		QUERY_DUJIE_SKILL_INFO_RES res = new QUERY_DUJIE_SKILL_INFO_RES(message.getSequnceNum(),robberyLevel,skillOneLevels,needOneDuJies,skillTwoLevels,needTwoDuJies);
		return res;
	}
	/**
	 * 请求天劫提示信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ROBBERY_TIPS_REQ(Connection conn, RequestMessage message, Player player){
//		TransitRobberyManager.getInstance().ready2EnterRobberyScene(player, false);
		Timestamp cutTime = new Timestamp(System.currentTimeMillis());
		if(cutTime.compareTo(RobberyConstant.OPENTIME) < 0){
			ROBBERY_TIPS_RES req = new ROBBERY_TIPS_RES(message.getSequnceNum(), 0, Translate.渡劫功能开启前提示);
			return req;
		}
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		if(entity == null){
			ROBBERY_TIPS_RES req = new ROBBERY_TIPS_RES(message.getSequnceNum(), 0, Translate.渡劫开启);
			return req;
		}
		int currentLevel = entity.getCurrentLevel() + 1;
		String tips = Translate.渡过九重天劫;
		if(currentLevel <= 9){
			RobberyDataModel rmd = TransitRobberyManager.getInstance().getRobberyDataModel(currentLevel);
			tips = rmd.getTips();
		}
		ROBBERY_TIPS_RES req = new ROBBERY_TIPS_RES(message.getSequnceNum(), currentLevel, tips);
		return req;
	}
	
	public static boolean 开启仙界渡劫 = true;
	/**
	 * 正式开始渡劫
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_START_ROBBERT_REQ(Connection conn, RequestMessage message, Player player){
		TransitRobberyManager.logger.info("收到开始渡劫的协议！=====");
		if (开启仙界渡劫 && FairyRobberyManager.inst.isPlayerInRobbery(player)) {
			FairyRobberyManager.inst.notifyPlayerStart(player);
		} else {
			TransitRobberyManager.getInstance().startRobbery(player);
		}
		return null;
	}
	
	public ResponseMessage handle_CHANAGE_PLAYER_AVATA_REQ(Connection conn, RequestMessage message, Player player) {
		if(player.getCurrentGame() != null) {
			player.getCurrentGame().messageQueue.push(new PlayerMessagePair(player, message, null));
		}
		return null;
	}
	

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
