package com.fy.engineserver.activity.disaster;

import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.DISASTER_OPT_REQ;
import com.fy.engineserver.message.DISASTER_OPT_RES;
import com.fy.engineserver.message.DISASTER_RANK_INFO_REQ;
import com.fy.engineserver.message.DISASTER_SKILL_INFO_REQ;
import com.fy.engineserver.message.DISASTER_SKILL_INFO_RES;
import com.fy.engineserver.message.PLAYER_IN_SPESCENE_REQ;
import com.fy.engineserver.message.PLAYER_IN_SPESCENE_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class DisasterSubSystem extends SubSystemAdapter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "DisasterSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[]{"DISASTER_OPT_REQ", "PLAYER_IN_SPESCENE_REQ", "DISASTER_RANK_INFO_REQ", "DISASTER_SKILL_INFO_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, Horse2Manager.logger);
		if(DisasterManager.logger.isDebugEnabled()) {
			DisasterManager.logger.debug("[DisasterSubSystem] [收到玩家请求] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]");
		}
		try {
			if (message instanceof DISASTER_OPT_REQ) {
				return handle_DISASTER_OPT_REQ(conn, message, player);
			} else if (message instanceof PLAYER_IN_SPESCENE_REQ) {
				return handle_PLAYER_IN_SPESCENE_REQ(conn, message, player);
			} else if (message instanceof DISASTER_RANK_INFO_REQ) {
				return handle_DISASTER_RANK_INFO_REQ(conn, message, player);
			} else if (message instanceof DISASTER_SKILL_INFO_REQ) {
				return handle_DISASTER_SKILL_INFO_REQ(conn, message, player);
			}
		} catch (Exception e) {
			DisasterManager.logger.error("[金猴天灾] [处理协议出现异常] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]", e);
		}
		return null;
	}
	/**
	 * 取消、进入、放弃、离开金猴天灾活动操作
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_DISASTER_OPT_REQ(Connection conn, RequestMessage message, Player player) {
		DISASTER_OPT_REQ req = (DISASTER_OPT_REQ) message;
		int optType = req.getOptType();
		if (optType == 0) {			//进入
			DisasterManager.getInst().optionEnterDisaster(player);
		} else if (optType == 1) {		//放弃
			DisasterManager.getInst().optionCancel(player);
		} else if (optType == 3) {		//取消
			DisasterManager.getInst().cancelSign(player);
		} else if (optType == 4) {		//离开
			DisasterManager.getInst().leaveDisaster(player);
		} 
		DISASTER_OPT_RES resp = new DISASTER_OPT_RES(req.getSequnceNum(), optType, 1);
		return resp;
	}
	/**
	 * 客户端请求玩家是否在特定场景内
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PLAYER_IN_SPESCENE_REQ(Connection conn, RequestMessage message, Player player) {
		PLAYER_IN_SPESCENE_REQ req = (PLAYER_IN_SPESCENE_REQ) message;
		int sceneType = req.getSceneType();
		int result = -1;
		if (sceneType == 1) {
			if (DisasterManager.getInst().isPlayerInGame(player)) {
				result = 1;
			}
		}else if(sceneType == 2){
//			if(AcrossServerManager.isAcrossServer() && player.crossBattleType == 0){
//				result = 1;
//			}
		}else if(sceneType == 3){
//			if(AcrossServerManager.isAcrossServer() && player.crossBattleType > 0){
//				result = 1;
//			}
		}else if(sceneType == 4){
			if(player.getCurrentGame() != null){
				if(player.getCurrentGame().gi != null){
					if(player.getCurrentGame().gi.name != null && player.getCurrentGame().gi.name.equals(BossCityManager.mapname)){
						result = 1;
					}
				}
			}
		}else if(sceneType == 5){//1:空岛,2:全民boss,3:冰封幻境（70）,4:虚空幻境（81）,5:家族远征
			if(player.getCurrentGame() != null){
				if(player.getCurrentGame().gi != null){
					if(player.getCurrentGame().gi.name != null && player.getCurrentGame().gi.name.equals("bingfenghuanjing")){
						result = 1;
					}
				}
			}
		}else if(sceneType == 6){//虚空幻境（81）
			if(player.getCurrentGame() != null){
				if(player.getCurrentGame().gi != null){
					if(player.getCurrentGame().gi.name != null && player.getCurrentGame().gi.name.equals("shengshougeliuceng")){
						result = 1;
					}
				}
			}
		}else if(sceneType == 7){//家族远征,按钮，家族鼓舞，死亡
			if(player.getCurrentGame() != null){
				if(player.getCurrentGame().gi != null){
					if(player.getCurrentGame().gi.name != null && player.getCurrentGame().gi.name.equals("zhanmotianyu")){
						result = 1;
					}
				}
			}
		}
		PLAYER_IN_SPESCENE_RES resp = new PLAYER_IN_SPESCENE_RES(message.getSequnceNum(), sceneType, result);
		return resp;
	}
	public ResponseMessage handle_DISASTER_RANK_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		DisasterManager.getInst().notifyAroundChange(player);
		return null;
	}
	public ResponseMessage handle_DISASTER_SKILL_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		DISASTER_SKILL_INFO_RES resp = new DISASTER_SKILL_INFO_RES(message.getSequnceNum(), CareerManager.getInstance().disasterSkills);
		return resp;
	}


	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
