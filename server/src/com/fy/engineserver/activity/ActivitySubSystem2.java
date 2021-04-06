package com.fy.engineserver.activity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.activity.chestFight.model.TempReturnModel;
import com.fy.engineserver.activity.dailyTurnActivity.DailyTurnManager;
import com.fy.engineserver.activity.dailyTurnActivity.model.DiskFileModel;
import com.fy.engineserver.activity.dailyTurnActivity.model.RewardArticleModel;
import com.fy.engineserver.activity.dailyTurnActivity.model.TurnModel;
import com.fy.engineserver.activity.dailyTurnActivity.model.TurnModel4Client;
import com.fy.engineserver.activity.datamanager.ActivityDataManager;
import com.fy.engineserver.activity.datamanager.module.ActivityModule;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyEntityManager;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyManager;
import com.fy.engineserver.activity.fairyRobbery.instance.FairyRobberyEntity;
import com.fy.engineserver.activity.fairyRobbery.model.FairyRobberyModel;
import com.fy.engineserver.activity.furnace.FurnaceManager;
import com.fy.engineserver.activity.levelUpReward.LevelUpRewardManager;
import com.fy.engineserver.activity.levelUpReward.model.LevelUpRewardModel;
import com.fy.engineserver.activity.treasure.TreasureActivityManager;
import com.fy.engineserver.activity.treasure.model.TreasureArticleModel;
import com.fy.engineserver.activity.treasure.model.TreasureModel;
import com.fy.engineserver.bourn.BournCfg;
import com.fy.engineserver.bourn.BournManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.message.ALCHEMY_END_REQ;
import com.fy.engineserver.message.ALCHEMY_END_RES;
import com.fy.engineserver.message.BUY_EXTRA_TIMES4TURN_REQ;
import com.fy.engineserver.message.BUY_LEVELUPREWARD_GOOD_REQ;
import com.fy.engineserver.message.CHARGE_AGRS_RES;
import com.fy.engineserver.message.GET_FAIRY_ROBBERY_STATE_REQ;
import com.fy.engineserver.message.GET_FAIRY_ROBBERY_STATE_RES;
import com.fy.engineserver.message.GET_ONE_DAILY_TURN_REQ;
import com.fy.engineserver.message.GET_ONE_DAILY_TURN_RES;
import com.fy.engineserver.message.GET_ONE_TREASUREACTIVITY_INFO_REQ;
import com.fy.engineserver.message.GET_ONE_TREASUREACTIVITY_INFO_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_ACVITITY_SHOW_INFO_REQ;
import com.fy.engineserver.message.NEW_ACVITITY_SHOW_INFO_RES;
import com.fy.engineserver.message.NEW_GET_ONE_TREASUREACTIVITY_INFO_REQ;
import com.fy.engineserver.message.NEW_GET_ONE_TREASUREACTIVITY_INFO_RES;
import com.fy.engineserver.message.NOTICE_PLAYER_OUT_OF_MAP_REQ;
import com.fy.engineserver.message.OPEN_DAILY_TURN_WINDOW_REQ;
import com.fy.engineserver.message.OPEN_DAILY_TURN_WINDOW_RES;
import com.fy.engineserver.message.OPEN_LEVELUPREWARD_WINDOW_REQ;
import com.fy.engineserver.message.OPEN_LEVELUPREWARD_WINDOW_RES;
import com.fy.engineserver.message.OPEN_TREASUREACTIVITY_WINDOW_REQ;
import com.fy.engineserver.message.OPEN_TREASUREACTIVITY_WINDOW_RES;
import com.fy.engineserver.message.PLAY_DAILY_TURN_REQ;
import com.fy.engineserver.message.PLAY_DAILY_TURN_RES;
import com.fy.engineserver.message.PLAY_TREASUREACTIVITY_REQ;
import com.fy.engineserver.message.PLAY_TREASUREACTIVITY_RES;
import com.fy.engineserver.message.QUERY_CHEST_TYPE_TIME_REQ;
import com.fy.engineserver.message.QUERY_CHEST_TYPE_TIME_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class ActivitySubSystem2 extends SubSystemAdapter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ActivitySubSystem2";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[]{"GET_ONE_DAILY_TURN_REQ", "PLAY_DAILY_TURN_REQ", "OPEN_DAILY_TURN_WINDOW_REQ", "BUY_EXTRA_TIMES4TURN_REQ",
				"GET_ONE_TREASUREACTIVITY_INFO_REQ", "PLAY_TREASUREACTIVITY_REQ", "OPEN_TREASUREACTIVITY_WINDOW_REQ",
				"OPEN_LEVELUPREWARD_WINDOW_REQ","BUY_LEVELUPREWARD_GOOD_REQ","ALCHEMY_END_REQ","NOTICE_PLAYER_OUT_OF_MAP_REQ"
				,"GET_FAIRY_ROBBERY_STATE_REQ","QUERY_CHEST_TYPE_TIME_REQ", "NEW_ACVITITY_SHOW_INFO_REQ", "NEW_GET_ONE_TREASUREACTIVITY_INFO_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, JiazuManager2.logger);
		if(ActivitySubSystem.logger.isDebugEnabled()) {
			ActivitySubSystem.logger.debug("[收到玩家请求] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]");
		}
		try {
			if(message instanceof GET_ONE_DAILY_TURN_REQ) {
				return handle_GET_ONE_DAILY_TURN_REQ(conn, message, player);
			} else if (message instanceof PLAY_DAILY_TURN_REQ) {
				return handle_PLAY_DAILY_TURN_REQ(conn, message, player);
			} else if (message instanceof OPEN_DAILY_TURN_WINDOW_REQ) {
				return handle_OPEN_DAILY_TURN_WINDOW_REQ(conn, message, player);
			} else if (message instanceof BUY_EXTRA_TIMES4TURN_REQ) {
				return handle_BUY_EXTRA_TIMES4TURN_REQ(conn, message, player);
			} else if (message instanceof GET_ONE_TREASUREACTIVITY_INFO_REQ) {
				return handle_GET_ONE_TREASUREACTIVITY_INFO_REQ(conn, message, player);
			} else if (message instanceof PLAY_TREASUREACTIVITY_REQ) {
				return handle_PLAY_TREASUREACTIVITY_REQ(conn, message, player);
			} else if (message instanceof OPEN_TREASUREACTIVITY_WINDOW_REQ) {
				return handle_OPEN_TREASUREACTIVITY_WINDOW_REQ(conn, message, player);
			} else if (message instanceof OPEN_LEVELUPREWARD_WINDOW_REQ) {
				return handle_OPEN_LEVELUPREWARD_WINDOW_REQ(conn, message, player);
			} else if (message instanceof BUY_LEVELUPREWARD_GOOD_REQ) {
				return handle_BUY_LEVELUPREWARD_GOOD_REQ(conn, message, player);
			} else if (message instanceof ALCHEMY_END_REQ) {
				return handle_ALCHEMY_END_REQ(conn, message, player);
			} else if (message instanceof NOTICE_PLAYER_OUT_OF_MAP_REQ) {
				return handle_NOTICE_PLAYER_OUT_OF_MAP_REQ(conn, message, player);
			} else if (message instanceof GET_FAIRY_ROBBERY_STATE_REQ) {
				return handle_GET_FAIRY_ROBBERY_STATE_REQ(conn, message, player);
			} else if (message instanceof QUERY_CHEST_TYPE_TIME_REQ) {
				return handle_QUERY_CHEST_TYPE_TIME_REQ(conn, message, player);
			} else if (message instanceof NEW_ACVITITY_SHOW_INFO_REQ) {
				return handle_NEW_ACVITITY_SHOW_INFO_REQ(conn, message, player);
			} else if (message instanceof NEW_GET_ONE_TREASUREACTIVITY_INFO_REQ) {
				return handle_NEW_GET_ONE_TREASUREACTIVITY_INFO_REQ(conn, message, player);
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.warn("[处理协议] [异常] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]", e);
		}
		return null;
	}
	public ResponseMessage handle_QUERY_CHEST_TYPE_TIME_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_CHEST_TYPE_TIME_REQ req = (QUERY_CHEST_TYPE_TIME_REQ) message;
		int[] chestType = new int[0];
		long[] chestTimes = new long[0];
		TempReturnModel m = ChestFightManager.inst.checkPlayerChestTypeAndTime(req.getPlayerId());
		QUERY_CHEST_TYPE_TIME_RES resp = null;
		if (m != null ) {
			resp = new QUERY_CHEST_TYPE_TIME_RES(req.getSequnceNum(), req.getPlayerId(), m.chestType, m.chestTimes);
		} else {
			resp = new QUERY_CHEST_TYPE_TIME_RES(req.getSequnceNum(), req.getPlayerId(), chestType, chestTimes);
		}
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn("[handle_QUERY_CHEST_TYPE_TIME_REQ] [" + player.getLogString() + "] [pId:" + req.getPlayerId() + "] [chestType:" + Arrays.toString(resp.getChestTypes()) + "] [chestTimes:" + Arrays.toString(resp.getChestTimes()) + "]");
		}
		return resp;
	}
	/**
	 *  玩家进入碰撞区
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_NOTICE_PLAYER_OUT_OF_MAP_REQ(Connection conn, RequestMessage message, Player player) {
		NOTICE_PLAYER_OUT_OF_MAP_REQ req = (NOTICE_PLAYER_OUT_OF_MAP_REQ) message;
		ChestFightManager.inst.notifyPlayerOutOfMap(req.getPlayerId());
		return null;
	}
	public ResponseMessage handle_NEW_ACVITITY_SHOW_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		ActivityModule[] datas = ActivityDataManager.getInst().getAllOpenActivitys(player).toArray(new ActivityModule[0]);
		NEW_ACVITITY_SHOW_INFO_RES resp = new NEW_ACVITITY_SHOW_INFO_RES(message.getSequnceNum(), datas);
		return resp;
	}
	public ResponseMessage handle_GET_FAIRY_ROBBERY_STATE_REQ(Connection conn, RequestMessage message, Player player) {
		int needRobberyLv = 0;
		int clLv = player.getClassLevel();
		for (FairyRobberyModel m : FairyRobberyManager.inst.robberys.values()) {
			if (m.getClassLvLimit() == clLv) {
				needRobberyLv = m.getId();
				break;
			}
		}
		BournCfg bournCfg = BournManager.getInstance().getBournCfg(clLv);
		BournCfg nextBournCfg = BournManager.getInstance().getBournCfg(clLv + 1);
		GET_FAIRY_ROBBERY_STATE_RES res = null;
		if (clLv > BournManager.maxBournLevel || nextBournCfg == null || bournCfg == null || player.getBournExp() < bournCfg.getExp()) {
			res = new GET_FAIRY_ROBBERY_STATE_RES(message.getSequnceNum(), -1, FairyRobberyManager.NPCNAME, 
					FairyRobberyManager.传送出地图, FairyRobberyManager.坐标);
			return res;
		}
		Task task = TaskManager.getInstance().getTask(bournCfg.getEndTask());
		if (task == null || player.getTaskStatus(task) != BournManager.TASK_STATUS_DEILVER) {
			res = new GET_FAIRY_ROBBERY_STATE_RES(message.getSequnceNum(), -1, FairyRobberyManager.NPCNAME, 
					FairyRobberyManager.传送出地图, FairyRobberyManager.坐标);
			return res;
		}
//		if (clLv >= 11) {
//			FairyRobberyEntity entity = FairyRobberyEntityManager.inst.getEntity(player);
//			if (entity == null || entity.getPassLevel() <= (clLv - 11)) {
//				res = new GET_FAIRY_ROBBERY_STATE_RES(message.getSequnceNum(), -1, FairyRobberyManager.NPCNAME, 
//						FairyRobberyManager.传送出地图, FairyRobberyManager.坐标);
//				return res;
//			}
//		}
		FairyRobberyEntity entity = FairyRobberyEntityManager.inst.getEntity(player);
		if (needRobberyLv== 0 || entity == null || entity.getPassLevel() < needRobberyLv) {
			res = new GET_FAIRY_ROBBERY_STATE_RES(message.getSequnceNum(), 1, FairyRobberyManager.NPCNAME, 
					FairyRobberyManager.传送出地图, FairyRobberyManager.坐标);
		} else {
			res = new GET_FAIRY_ROBBERY_STATE_RES(message.getSequnceNum(), 0,  FairyRobberyManager.NPCNAME, 
					FairyRobberyManager.传送出地图, FairyRobberyManager.坐标);
		}
		return res;
	}
	/**
	 * 炼丹
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ALCHEMY_END_REQ(Connection conn, RequestMessage message, Player player) {
		ALCHEMY_END_REQ req = (ALCHEMY_END_REQ) message;
		FurnaceManager.inst.pickup(player, req.getPositionIndex(),req.getNpcId());
		ALCHEMY_END_RES resp = new ALCHEMY_END_RES(message.getSequnceNum(), req.getPositionIndex());
		return resp;
	}
	/**
	 * 购买冲级返利
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BUY_LEVELUPREWARD_GOOD_REQ(Connection conn, RequestMessage message, Player player) {
		if (!LevelUpRewardManager.instance.isActivityAct()) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		BUY_LEVELUPREWARD_GOOD_REQ req = (BUY_LEVELUPREWARD_GOOD_REQ) message;
		int rewardId = req.getRewardId();
		String rewardName = req.getRewardName();
		String channleName = req.getChannelName();
		if (LevelUpRewardManager.logger.isDebugEnabled()) {
			LevelUpRewardManager.logger.debug("[handle_BUY_LEVELUPREWARD_GOOD_REQ] [" + player.getLogString() + "] [rewardName:" + rewardName + "] [rewardId:" + rewardId + "]");
		}
		if(channleName == null || channleName.isEmpty()){
			player.sendError(Translate.请选择正确的冲级红利);
			return null;
		}
		LevelUpRewardModel model = LevelUpRewardManager.instance.getModelByPlayer(player);
		if (model == null  || model.getType() != rewardId) {
			player.sendError(Translate.您无法购买红利);
			return null;
		}
		String result = LevelUpRewardManager.instance.verlificata4Buy(player);
		if (result != null) {
			player.sendError(result);
			return null;
		}
		List<ChargeMode> list = ChargeManager.getInstance().getChannelChargeModes(channleName);
		if (list == null || list.size() == 0) {
			player.sendError(Translate.无匹的充值信息请联系GM);
			ChargeManager.logger.error(player.getLogString() + "[查询充值列表] [异常] [无匹配的充值列表] [rewardId:{}] [rewardId:{}] [渠道:{}] [玩家:{}]",new Object[]{rewardId,rewardName, channleName,player.getLogString()});
			return null;
		}
		
		ChargeMode chargemode = null;
		long money = model.getNeedRmb() * 100;
		String cardId = rewardId + "";
		String chargeId = "";
		String specConfig = "";
		end:for(ChargeMode cm : list){
			if(cm != null && cm.getModeName().equals("支付宝")){
				if(cm.getMoneyConfigures() != null){
					for(ChargeMoneyConfigure config : cm.getMoneyConfigures()){
						if(config.getDenomination() == money){
							chargemode = cm;
							chargeId = config.getId();
							specConfig = config.getSpecialConf();
							break end;
						}
					}
				}
			}
		}
		
		if(chargemode == null){
			end:for(ChargeMode cm : list){
				if(cm != null && cm.getMoneyConfigures() != null){
					for(ChargeMoneyConfigure config : cm.getMoneyConfigures()){
						if(config.getDenomination() == money){
							chargemode = cm;
							chargeId = config.getId();
							specConfig = config.getSpecialConf();
							break end;
						}
					}
				}
			}
		}
		
		if(chargemode == null){
			player.sendError(Translate.暂时不能充值联系GM);
			return null;
		}
		CHARGE_AGRS_RES res = new CHARGE_AGRS_RES(GameMessageFactory.nextSequnceNum(), chargemode.getModeName(), chargeId, specConfig, cardId, SavingReasonType.冲级返利活动, money, new String[0]);
		return res;
	}
	/**
	 * 打开冲级返利界面
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_OPEN_LEVELUPREWARD_WINDOW_REQ(Connection conn, RequestMessage message, Player player) {
		if (!LevelUpRewardManager.instance.isActivityAct()) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		int len = LevelUpRewardManager.instance.rewardMaps.size();
		int[] rewardIds = new int[len];
		String[] rewardNames = new String[len];
		String[] descriptions = new String[len];
		int[] lowLevels = new int[len];
		int[] highLevels = new int[len];
		int index = 0;
		Iterator<Integer> ito = LevelUpRewardManager.instance.rewardMaps.keySet().iterator();
		while (ito.hasNext()) {
			int key = ito.next();
			LevelUpRewardModel value = LevelUpRewardManager.instance.rewardMaps.get(key);
			rewardIds[index] = value.getType();
			rewardNames[index] = value.getName();
			descriptions[index] = value.getDescription();
			lowLevels[index] = value.getLowLevelLimit();
			highLevels[index] = value.getHighLevelLimit();
			index++;
		}
		OPEN_LEVELUPREWARD_WINDOW_RES resp = new OPEN_LEVELUPREWARD_WINDOW_RES(message.getSequnceNum(), rewardIds, rewardNames, 
				descriptions, lowLevels, highLevels);
		return resp;
	}
	/**
	 * 请求极地寻宝单个页签
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_ONE_TREASUREACTIVITY_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		if (!TreasureActivityManager.instance.isActivityOpen()) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		GET_ONE_TREASUREACTIVITY_INFO_REQ req = (GET_ONE_TREASUREACTIVITY_INFO_REQ) message;
		TreasureModel tm = TreasureActivityManager.instance.treasureModels.get(req.getTreasureId());
		if (!tm.isIsopen()) {
			player.sendError(Translate.本章活动未开启);
			return null;
		}
		long[] defaultArticleIds = new long[tm.getNeedShowGoods().size()];
		for (int i=0; i<defaultArticleIds.length; i++) {
			TreasureArticleModel tam = TreasureActivityManager.instance.articleModels.get(tm.getNeedShowGoods().get(i));
			defaultArticleIds[i] = tam.getTempEntityId();
		}
		String des = tm.getDescription();
		GET_ONE_TREASUREACTIVITY_INFO_RES resp = new GET_ONE_TREASUREACTIVITY_INFO_RES(message.getSequnceNum(), req.getTreasureId(), 
				defaultArticleIds, des);
		return resp;
	}
	public ResponseMessage handle_NEW_GET_ONE_TREASUREACTIVITY_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		if (!TreasureActivityManager.instance.isActivityOpen()) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		NEW_GET_ONE_TREASUREACTIVITY_INFO_REQ req = (NEW_GET_ONE_TREASUREACTIVITY_INFO_REQ) message;
		TreasureModel tm = TreasureActivityManager.instance.treasureModels.get(req.getTreasureId());
		if (!tm.isIsopen()) {
			player.sendError(Translate.本章活动未开启);
			return null;
		}
		long[] defaultArticleIds = new long[tm.getNeedShowGoods().size()];
		long[] costSilvers = new long[tm.getCostS().size()];
		for (int i=0; i<tm.getCostS().size(); i++) {
			costSilvers[i] = tm.getCostS().get(i);
		}
		for (int i=0; i<defaultArticleIds.length; i++) {
			TreasureArticleModel tam = TreasureActivityManager.instance.articleModels.get(tm.getNeedShowGoods().get(i));
			defaultArticleIds[i] = tam.getTempEntityId();
		}
		String des = tm.getDescription();
		NEW_GET_ONE_TREASUREACTIVITY_INFO_RES resp = new NEW_GET_ONE_TREASUREACTIVITY_INFO_RES(message.getSequnceNum(), req.getTreasureId(), 
				defaultArticleIds, des, costSilvers);
		return resp;
	}
	/***
	 * 寻宝 
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PLAY_TREASUREACTIVITY_REQ(Connection conn, RequestMessage message, Player player) {
		if (!TreasureActivityManager.instance.isActivityOpen()) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		PLAY_TREASUREACTIVITY_REQ req = (PLAY_TREASUREACTIVITY_REQ) message;
		long[] takeArticleIds = TreasureActivityManager.instance.digTreasure(player, req.getTreasureId(), req.getDigTimes(), false);
		if (takeArticleIds == null) {
			return null;
		}
		PLAY_TREASUREACTIVITY_RES resp = new PLAY_TREASUREACTIVITY_RES(message.getSequnceNum(), req.getTreasureId(), takeArticleIds);
		return resp;
	}
	
	public static int a = 1;
	/**
	 * 打开极地寻宝界面
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_OPEN_TREASUREACTIVITY_WINDOW_REQ(Connection conn, RequestMessage message, Player player) {
		if (!TreasureActivityManager.instance.isActivityOpen()) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		long leftTime = TreasureActivityManager.instance.getActivityLeftTime() / 1000;		//返回给客户端秒数
		int len = TreasureActivityManager.instance.treasureModels.size();
		TreasureModel tm = null;			//默认打开第一个
		String description = "";			//玩法说明
		String[] treasureNames = new String[len];		//寻宝页签名
		int[] treasureIds = new int[len];			//寻宝id
		
		int tempIndex = 0;
		
		Iterator<Integer> ite = TreasureActivityManager.instance.treasureModels.keySet().iterator();
		while (ite.hasNext()) {
			int key = ite.next();
			TreasureModel value = TreasureActivityManager.instance.treasureModels.get(key);
			treasureNames[tempIndex] = value.getName();
			treasureIds[tempIndex] = value.getType();
			if (value.isIsopen() && tm == null) {
				tm = value;
			}
			tempIndex ++;
		}
		int[] digTimes = new int[tm.getDigTimes().size()];				//可挖取次数
		long[] costSilvers = new long[tm.getDigTimes().size()];			//对应消耗金钱
		for (int i=0; i<tm.getDigTimes().size(); i++) {
			digTimes[i] = tm.getDigTimes().get(i);
			costSilvers[i] = tm.getCostS().get(i);
		}
		long[] defaultArticleIds = new long[tm.getNeedShowGoods().size()];	//默认打开的寻宝界展示的物品临时id
		for (int i=0; i<defaultArticleIds.length; i++) {
			TreasureArticleModel tam = TreasureActivityManager.instance.articleModels.get(tm.getNeedShowGoods().get(i));
			defaultArticleIds[i] = tam.getTempEntityId();
		}
		description = tm.getDescription();
		
		OPEN_TREASUREACTIVITY_WINDOW_RES resp = new OPEN_TREASUREACTIVITY_WINDOW_RES(message.getSequnceNum(), leftTime, description, 
				treasureNames, treasureIds, digTimes, costSilvers, defaultArticleIds);
		if (ActivitySubSystem.logger.isDebugEnabled()) {
			ActivitySubSystem.logger.debug("[OPEN_TREASUREACTIVITY_WINDOW_RES] [" + player.getLogString() + "] [" + Arrays.toString(costSilvers) + "] [" + treasureIds + "]");;
		}
		return resp;
	}
	
	/**
	 * 购买额外次数
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_BUY_EXTRA_TIMES4TURN_REQ(Connection conn, RequestMessage message, Player player) {
		if (!DailyTurnManager.instance.isDailyTurnOpen()) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		BUY_EXTRA_TIMES4TURN_REQ req = (BUY_EXTRA_TIMES4TURN_REQ) message;
		DailyTurnManager.instance.buyExtraTimes(player, req.getTurnId(), false);
		return null;
//		TurnModel4Client info = DailyTurnManager.instance.getTurnModel4Client(player, req.getTurnId());
//		BUY_EXTRA_TIMES4TURN_RES resp = new BUY_EXTRA_TIMES4TURN_RES(message.getSequnceNum(), info);
//		return resp;
	}
	/**
	 * 获取单个转盘信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_ONE_DAILY_TURN_REQ(Connection conn, RequestMessage message, Player player) {
		if (!DailyTurnManager.instance.isDailyTurnOpen()) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		GET_ONE_DAILY_TURN_REQ req = (GET_ONE_DAILY_TURN_REQ) message;
		TurnModel4Client info = DailyTurnManager.instance.getTurnModel4Client(player, req.getTurnId());
		GET_ONE_DAILY_TURN_RES resp = new GET_ONE_DAILY_TURN_RES(message.getSequnceNum(), info);
		return resp;
	}
	/***
	 * 抽奖
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_PLAY_DAILY_TURN_REQ(Connection conn, RequestMessage message, Player player) {
		if (!DailyTurnManager.instance.isDailyTurnOpen()) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		PLAY_DAILY_TURN_REQ req = (PLAY_DAILY_TURN_REQ) message;
		int resultId = DailyTurnManager.instance.startExtract(player, req.getTurnId());
		TurnModel4Client info = DailyTurnManager.instance.getTurnModel4Client(player, req.getTurnId());
		TurnModel tm = DailyTurnManager.instance.turnMaps.get(req.getTurnId());
		int index = -1;
		for (int i=0; i<tm.getGoodIds().size(); i++) {
			RewardArticleModel rm = DailyTurnManager.instance.articleMaps.get(tm.getGoodIds().get(i));
			if (rm.getId() == resultId) {
				index = i;
				break;
			}
		}
		if (index < 0) {
			return null;
		}
		PLAY_DAILY_TURN_RES resp = new PLAY_DAILY_TURN_RES(message.getSequnceNum(), index, info);
		return resp;
	}
	/**
	 * 打开每日转盘界面
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_OPEN_DAILY_TURN_WINDOW_REQ(Connection conn, RequestMessage message, Player player) {
		if (!DailyTurnManager.instance.isDailyTurnOpen()) {
			player.sendError(Translate.活动未开启);
			return null;
		}
		long now = System.currentTimeMillis();
		DiskFileModel model = DailyTurnManager.instance.getDiskFileModel(player, now);
		model.reset(now, player);
		Iterator<Integer> ite = DailyTurnManager.instance.turnMaps.keySet().iterator();
		String[] turnNames = new String[DailyTurnManager.instance.turnMaps.size()];
		int[] turnIds = new int[DailyTurnManager.instance.turnMaps.size()];
		int index = 0;
		int tempKey = -1;
		while (ite.hasNext()) {
			int key = ite.next();
			if (index == 0) {
				tempKey = key;
			}
			TurnModel tm = DailyTurnManager.instance.turnMaps.get(key);
			turnNames[index] = tm.getTurnName();
			turnIds[index] = tm.getTurnId();
			index++;
		}
		TurnModel4Client info = DailyTurnManager.instance.getTurnModel4Client(player, tempKey);
		OPEN_DAILY_TURN_WINDOW_RES resp = new OPEN_DAILY_TURN_WINDOW_RES(message.getSequnceNum(), turnNames, turnIds, info);
		return resp;
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
