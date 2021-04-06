package com.fy.engineserver.sprite.horse2.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayEntityManager;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayManager;
import com.fy.engineserver.datasource.article.data.horseInlay.instance.HorseEquInlay;
import com.fy.engineserver.datasource.article.data.horseInlay.module.InlayModule;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.ACTIVITY_HORSEEQU_INLAY_REQ;
import com.fy.engineserver.message.ACTIVITY_HORSEEQU_INLAY_RES;
import com.fy.engineserver.message.GET_HORSE_BLOOD_WINDOW_INFO_REQ;
import com.fy.engineserver.message.GET_HORSE_BLOOD_WINDOW_INFO_RES;
import com.fy.engineserver.message.GET_HORSE_COLOR_STRONG_WIND_REQ;
import com.fy.engineserver.message.GET_HORSE_COLOR_STRONG_WIND_RES;
import com.fy.engineserver.message.GET_HORSE_HELP_INFO_REQ;
import com.fy.engineserver.message.GET_HORSE_HELP_INFO_RES;
import com.fy.engineserver.message.GET_HORSE_NEED_ARTICLE_WIND_REQ;
import com.fy.engineserver.message.GET_HORSE_NEED_ARTICLE_WIND_RES;
import com.fy.engineserver.message.GET_HORSE_RANK_WINDOW_INFO_REQ;
import com.fy.engineserver.message.GET_HORSE_RANK_WINDOW_INFO_RES;
import com.fy.engineserver.message.GET_HORSE_SKILL_INFO_SHOW_REQ;
import com.fy.engineserver.message.GET_HORSE_SKILL_INFO_SHOW_RES;
import com.fy.engineserver.message.GET_HORSE_SKILL_OPEN_INFO_REQ;
import com.fy.engineserver.message.GET_HORSE_SKILL_OPEN_INFO_RES;
import com.fy.engineserver.message.GET_HORSE_SKILL_WINDOW_INFO_REQ;
import com.fy.engineserver.message.GET_HORSE_SKILL_WINDOW_INFO_RES;
import com.fy.engineserver.message.HIDDEN_POPWINDOW_REQ;
import com.fy.engineserver.message.HORSE_BLOODSTAR_STRONG_REQ;
import com.fy.engineserver.message.HORSE_BLOODSTAR_STRONG_RES;
import com.fy.engineserver.message.HORSE_COLOR_STRONG_REQ;
import com.fy.engineserver.message.HORSE_COLOR_STRONG_RES;
import com.fy.engineserver.message.HORSE_RANKSTAR_STRONG_REQ;
import com.fy.engineserver.message.HORSE_RANKSTAR_STRONG_RES;
import com.fy.engineserver.message.INLAYTAKE_HORSEBAOSHI_REQ;
import com.fy.engineserver.message.INLAYTAKE_HORSEBAOSHI_RES;
import com.fy.engineserver.message.LEARN_HORSE_SKILL_REQ;
import com.fy.engineserver.message.LEARN_HORSE_SKILL_RES;
import com.fy.engineserver.message.NOTIFY_AUTOFEED_HORSE_REQ;
import com.fy.engineserver.message.QUERY_HORSE_DATA_REQ;
import com.fy.engineserver.message.QUERY_HORSE_DATA_RES;
import com.fy.engineserver.message.QUERY_HORSE_EQUIPMENT_INFO_REQ;
import com.fy.engineserver.message.QUERY_HORSE_EQUIPMENT_INFO__RES;
import com.fy.engineserver.message.QUERY_HORSE_LIST2_REQ;
import com.fy.engineserver.message.QUERY_HORSE_LIST2_RES;
import com.fy.engineserver.message.QUERY_INLAY_WINDOW_INFO_REQ;
import com.fy.engineserver.message.QUERY_INLAY_WINDOW_INFO_RES;
import com.fy.engineserver.message.QUERY_SKILLS_MAP_REQ;
import com.fy.engineserver.message.QUERY_SKILLS_MAP_RES;
import com.fy.engineserver.message.REFRESH_HORSE_SKILL_REQ;
import com.fy.engineserver.message.REFRESH_HORSE_SKILL_RES;
import com.fy.engineserver.message.UPGRADE_HORSE_SKILL_REQ;
import com.fy.engineserver.message.UPGRADE_HORSE_SKILL_RES;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity;
import com.fy.engineserver.sprite.horse2.model.BloodStarModel;
import com.fy.engineserver.sprite.horse2.model.HorseAttrModel;
import com.fy.engineserver.sprite.horse2.model.HorseColorModel;
import com.fy.engineserver.sprite.horse2.model.HorseRankModel;
import com.fy.engineserver.sprite.horse2.model.HorseSkillModel;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.fy.engineserver.sprite.pet2.PetsOfPlayer;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class Horse2System extends SubSystemAdapter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Horse2System";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[]{"QUERY_HORSE_LIST2_REQ","QUERY_HORSE_DATA_REQ","HORSE_BLOODSTAR_STRONG_REQ","QUERY_SKILLS_MAP_REQ"
				,"HORSE_RANKSTAR_STRONG_REQ", "REFRESH_HORSE_SKILL_REQ", "LEARN_HORSE_SKILL_REQ", "HORSE_COLOR_STRONG_REQ",
				"UPGRADE_HORSE_SKILL_REQ", "GET_HORSE_SKILL_INFO_SHOW_REQ","GET_HORSE_SKILL_WINDOW_INFO_REQ","GET_HORSE_RANK_WINDOW_INFO_REQ"
				,"GET_HORSE_BLOOD_WINDOW_INFO_REQ","GET_HORSE_NEED_ARTICLE_WIND_REQ", "GET_HORSE_SKILL_OPEN_INFO_REQ"
				,"GET_HORSE_HELP_INFO_REQ", "GET_HORSE_COLOR_STRONG_WIND_REQ", "HIDDEN_POPWINDOW_REQ", "NOTIFY_AUTOFEED_HORSE_REQ"
				,"QUERY_HORSE_EQUIPMENT_INFO_REQ", "ACTIVITY_HORSEEQU_INLAY_REQ", "QUERY_INLAY_WINDOW_INFO_REQ", "INLAYTAKE_HORSEBAOSHI_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, Horse2Manager.logger);
		if(Horse2Manager.logger.isDebugEnabled()) {
			Horse2Manager.logger.debug("[Horse2System] [收到玩家请求] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]");
		}
		try {
			if(message instanceof QUERY_HORSE_LIST2_REQ) {
				return handle_QUERY_HORSE_LIST2_REQ(conn, message, player);
			} else if (message instanceof HIDDEN_POPWINDOW_REQ) {
				return handle_HIDDEN_POPWINDOW_REQ(conn, message, player);
			} else if (message instanceof NOTIFY_AUTOFEED_HORSE_REQ) {
				return handle_NOTIFY_AUTOFEED_HORSE_REQ(conn, message, player);
			} else if (message instanceof QUERY_HORSE_DATA_REQ) {
				return handle_QUERY_HORSE_DATA_REQ(conn, message, player);
			} else if (message instanceof HORSE_BLOODSTAR_STRONG_REQ) {
				return handle_HORSE_BLOODSTAR_STRONG_REQ(conn, message, player);
			} else if (message instanceof QUERY_SKILLS_MAP_REQ) {
				return handle_QUERY_SKILLS_MAP_REQ(conn, message, player);
			} else if (message instanceof HORSE_RANKSTAR_STRONG_REQ) {
				return handle_HORSE_RANKSTAR_STRONG_REQ(conn, message, player);
			} else if (message instanceof REFRESH_HORSE_SKILL_REQ) {
				return handle_REFRESH_HORSE_SKILL_REQ(conn, message, player);
			} else if (message instanceof LEARN_HORSE_SKILL_REQ) {
				return handle_LEARN_HORSE_SKILL_REQ(conn, message, player);
			} else if (message instanceof HORSE_COLOR_STRONG_REQ) {
				return handle_HORSE_COLOR_STRONG_REQ(conn, message, player);
			} else if (message instanceof UPGRADE_HORSE_SKILL_REQ) {
				return handle_UPGRADE_HORSE_SKILL_REQ(conn, message, player);
			} else if (message instanceof GET_HORSE_SKILL_INFO_SHOW_REQ) {
				return handle_GET_HORSE_SKILL_INFO_SHOW_REQ(conn, message, player);
			} else if (message instanceof GET_HORSE_SKILL_WINDOW_INFO_REQ) {
				return handle_GET_HORSE_SKILL_WINDOW_INFO_REQ(conn, message, player);
			} else if (message instanceof GET_HORSE_RANK_WINDOW_INFO_REQ) {
				return handle_GET_HORSE_RANK_WINDOW_INFO_REQ(conn, message, player);
			} else if (message instanceof GET_HORSE_BLOOD_WINDOW_INFO_REQ) {
				return handle_GET_HORSE_BLOOD_WINDOW_INFO_REQ(conn, message, player);
			}  else if (message instanceof GET_HORSE_NEED_ARTICLE_WIND_REQ) {
				return handle_GET_HORSE_NEED_ARTICLE_WIND_REQ(conn, message, player);
			} else if (message instanceof GET_HORSE_SKILL_OPEN_INFO_REQ) {
				return handle_GET_HORSE_SKILL_OPEN_INFO_REQ(conn, message, player);
			} else if (message instanceof GET_HORSE_HELP_INFO_REQ) {
				return handle_GET_HORSE_HELP_INFO_REQ(conn, message, player);
			} else if (message instanceof GET_HORSE_COLOR_STRONG_WIND_REQ) {
				return handle_GET_HORSE_COLOR_STRONG_WIND_REQ(conn, message, player);
			} else if (message instanceof QUERY_HORSE_EQUIPMENT_INFO_REQ) {
				return handle_QUERY_HORSE_EQUIPMENT_INFO_REQ(conn, message, player);
			} else if (message instanceof ACTIVITY_HORSEEQU_INLAY_REQ) {
				return handle_ACTIVITY_HORSEEQU_INLAY_REQ(conn, message, player);
			} else if (message instanceof QUERY_INLAY_WINDOW_INFO_REQ) {
				return handle_QUERY_INLAY_WINDOW_INFO_REQ(conn, message, player);
			} else if (message instanceof INLAYTAKE_HORSEBAOSHI_REQ) {
				return handle_INLAYTAKE_HORSEBAOSHI_REQ(conn, message, player);
			}

		} catch (Exception e) {
			Horse2Manager.logger.error("[新坐骑系统] [处理协议出现异常] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]", e);
		}
		return null;
	}
	
	public ResponseMessage handle_QUERY_HORSE_EQUIPMENT_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_HORSE_EQUIPMENT_INFO_REQ req = (QUERY_HORSE_EQUIPMENT_INFO_REQ) message;
		long horseEquId = req.getHorseEquId();
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(horseEquId);
		int[] inlayColors = new int[HorseEquInlayManager.MAX_INLAY_NUM];
		Arrays.fill(inlayColors, -1);
		long[] inlayIds = new long[HorseEquInlayManager.MAX_INLAY_NUM];
		String[] needArticles = new String[HorseEquInlayManager.MAX_INLAY_NUM];
		int[] needNums = new int[HorseEquInlayManager.MAX_INLAY_NUM];
		int[] haveNums = new int[HorseEquInlayManager.MAX_INLAY_NUM];
		long[] tempAeId = new long[HorseEquInlayManager.MAX_INLAY_NUM];
		boolean needNotice = false;
		if (ae instanceof EquipmentEntity) {
			HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity((EquipmentEntity) ae);
			if (entity != null) {
				inlayColors = entity.getInlayColorType();
				inlayIds = entity.getInlayArticleIds();
			}
			EquipmentEntity ee = (EquipmentEntity) ae;
			if (ee.getStar() < HorseEquInlayManager.getInst().baseModule.getStarLimit()) {
				needNotice = true;
			}
			if (!needNotice) {
				Equipment e = (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());
				if (e.getEquipmentType() < 10 || e.getEquipmentType() > 14) {			//只针对坐骑装备
					needNotice = true;
				}
				if (e.getPlayerLevelLimit() < HorseEquInlayManager.getInst().baseModule.getEquLvLimit()) {
					needNotice = true;
				}
			}
		} else {
			needNotice = true;
		}
		Knapsack bag = player.getKnapsack_common();
		for (int i=0; i<needArticles.length; i++) {
			InlayModule module = HorseEquInlayManager.getInst().inlayMap.get(i);
			String[] tempStr = module.getCostByType(HorseEquInlayEntityManager.开孔);
			needArticles[i] = tempStr[0];
			needNums[i] = Integer.parseInt(tempStr[1]);
			haveNums[i] = bag.countArticle(needArticles[i]);
			tempAeId[i] = module.getTempAeId();
		}
		if (needNotice) {
			player.send_HINT_REQ(HorseEquInlayManager.getInst().getTranslate("invalid_star"));
		}
		QUERY_HORSE_EQUIPMENT_INFO__RES resp = new QUERY_HORSE_EQUIPMENT_INFO__RES(req.getSequnceNum(), horseEquId, inlayColors, 
				inlayIds, needArticles, needNums, haveNums, tempAeId);
		return resp;
	}
	public ResponseMessage handle_ACTIVITY_HORSEEQU_INLAY_REQ(Connection conn, RequestMessage message, Player player) {
		ACTIVITY_HORSEEQU_INLAY_REQ req = (ACTIVITY_HORSEEQU_INLAY_REQ) message;
		long horseEquId = req.getHorseEquId();
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(horseEquId);
		int inlayIndex = req.getInlayIndex();
		int inlayColor = -1;
		if (ae instanceof EquipmentEntity) {
			String result = HorseEquInlayEntityManager.getInst().punch(player, horseEquId, inlayIndex, req.getOpt(), req.getCostIds(), req.getCostNums(), false);
			if ("".equals(result)) {
				return null;
			}
			if (result == null) {
				HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity((EquipmentEntity) ae);
				if (entity != null) {
					inlayColor = entity.getInlayColorType()[inlayIndex];
				}
			} else {
				player.sendError(result);
			}
		}
		String[] needArticles = new String[HorseEquInlayManager.MAX_INLAY_NUM];
		int[] haveNums = new int[HorseEquInlayManager.MAX_INLAY_NUM];
		Knapsack bag = player.getKnapsack_common();
		for (int i=0; i<needArticles.length; i++) {
			InlayModule module = HorseEquInlayManager.getInst().inlayMap.get(i);
			String[] tempStr = module.getCostByType(HorseEquInlayEntityManager.开孔);
			needArticles[i] = tempStr[0];
			haveNums[i] = bag.countArticle(needArticles[i]);
		}
		ACTIVITY_HORSEEQU_INLAY_RES resp = new ACTIVITY_HORSEEQU_INLAY_RES(req.getSequnceNum(), horseEquId, inlayIndex, inlayColor, needArticles, haveNums);
		return resp;
	}
	public ResponseMessage handle_QUERY_INLAY_WINDOW_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_INLAY_WINDOW_INFO_REQ req = (QUERY_INLAY_WINDOW_INFO_REQ) message;
		long horseEquId = req.getHorseEquId();
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(horseEquId);
		int[] inlayIndexs = new int[HorseEquInlayManager.MAX_INLAY_NUM];
		int[] inlayColors = new int[HorseEquInlayManager.MAX_INLAY_NUM];
		long[] inlayIds = new long[HorseEquInlayManager.MAX_INLAY_NUM];
		if (ae instanceof EquipmentEntity) {
			HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity((EquipmentEntity) ae);
			if (entity != null) {
				for (int i=0; i<entity.getInlayColorType().length; i++) {
					inlayIndexs[i] = i;
					inlayColors[i] = entity.getInlayColorType()[i];
					inlayIds[i] = entity.getInlayArticleIds()[i];
				}
			} else {
				for (int i=0; i<inlayIndexs.length; i++) {
					inlayIndexs[i] = i;
					inlayColors[i] = -1;
					inlayIds[i] = -1;
				}
			}
		}
		QUERY_INLAY_WINDOW_INFO_RES resp = new QUERY_INLAY_WINDOW_INFO_RES(req.getSequnceNum(), horseEquId, inlayIndexs, inlayColors, inlayIds);
		return resp;
	}
	public ResponseMessage handle_INLAYTAKE_HORSEBAOSHI_REQ(Connection conn, RequestMessage message, Player player) {
		INLAYTAKE_HORSEBAOSHI_REQ req = (INLAYTAKE_HORSEBAOSHI_REQ) message;
		long horseEquId = req.getHorseEquId();
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(horseEquId);
		int inlayIndex = req.getInlayIndex();
		long shenxiaId = req.getShenxiaId();
		long inlayId = -1;
		if (ae instanceof EquipmentEntity) {
			String result = Translate.服务器出现错误;
			if (shenxiaId > 0) {
				result = HorseEquInlayEntityManager.getInst().inlay(player, horseEquId, inlayIndex, shenxiaId, false);
			} else {
				result = HorseEquInlayEntityManager.getInst().takeOff(player, horseEquId, inlayIndex);
			}
			if (Translate.高级锁魂物品不能做此操作.equals(result)) {
				return null;
			}
			if (result == null) {
				HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity((EquipmentEntity) ae);
				if (entity != null) {
					inlayId = entity.getInlayArticleIds()[inlayIndex];
				}
			}
		}
		INLAYTAKE_HORSEBAOSHI_RES resp = new INLAYTAKE_HORSEBAOSHI_RES(req.getSequnceNum(), horseEquId, inlayIndex, inlayId);
		return resp;
	}
	
	
	public ResponseMessage handle_HIDDEN_POPWINDOW_REQ(Connection conn, RequestMessage message, Player player) {
		HIDDEN_POPWINDOW_REQ req = (HIDDEN_POPWINDOW_REQ) message;
		if (req.getHiddenExchange().length < 6) {
			Horse2Manager.logger.warn("[屏蔽功能] [客户端传过来的数组不对] [" + Arrays.toString(req.getHiddenExchange()) + "]");
			return null;
		}
		player.hiddenTransformPop = req.getHiddenExchange()[0];
		player.hiddenChangePop = req.getHiddenExchange()[1];
		player.hiddenFanzhiPop = req.getHiddenExchange()[2];
		player.hiddenTeamPop = req.getHiddenExchange()[3];
		player.isUseSiliver = req.getHiddenExchange()[5];
		if (HorseManager.logger.isDebugEnabled()) {
			HorseManager.logger.debug("[handle_HIDDEN_POPWINDOW_REQ] [" + player.getLogString() + "] [" + Arrays.toString(req.getHiddenExchange()) + "]");
		}
		return null;
	}
	public ResponseMessage handle_NOTIFY_AUTOFEED_HORSE_REQ(Connection conn, RequestMessage message, Player player) {
		NOTIFY_AUTOFEED_HORSE_REQ req = (NOTIFY_AUTOFEED_HORSE_REQ) message;
		player.autoFeedHorse = req.getAutoFeedSwitch();
		player.autoBuyArticle = req.getAutoBuyArticle();
		player.feedArticleName = req.getAutoFeedArticleName();
		int feedLine = req.getAutoFeedLine() * 600 / 100 ;
		player.autoFeedLine = feedLine;
		if (player.autoBuyArticle) {			//自动购买物品，检测玩家背包是否有物品，是否购买
			int count = player.countArticleInKnapsacksByName(player.feedArticleName);
			if (count <= 0) {
				String shopName = ShopManager.得到随身商店名字(player.getLevel());
				Shop shop = ShopManager.getInstance().getShop(shopName, player);
				int goodsId = shop.getGoodsIdByGoodName(player.feedArticleName);
				if (goodsId >= 0) {
					shop.buyGoods(player, goodsId, 5, 0);
				}
			}
		}
		if (player.autoFeedHorse && player.getRideHorseId() > 0) {		//勾选时检测坐骑体力，考虑是否喂养坐骑
			Horse horse = HorseManager.getInstance().getHorseById(player.getRideHorseId(), player);
			if (horse != null) {
				if (horse.getEnergy() < player.autoFeedLine) {
					Knapsack bag = player.getKnapsack_common();
					int idx = bag.indexOf(player.feedArticleName);
					if (idx > 0) {
						ArticleEntity ae = player.getArticleEntity(player.feedArticleName);
						if (ae != null) {
							player.feedHorse(ae.getId(), horse);
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 查询玩家所有坐骑(包含飞行坐骑)
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_HORSE_LIST2_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_HORSE_LIST2_REQ req = (QUERY_HORSE_LIST2_REQ) message;
		Player tempP = null;
		if (req.getPlayerId() < 0 || req.getPlayerId() == player.getId()) {
			tempP = player;
		} else {
			try {
				tempP = GamePlayerManager.getInstance().getPlayer(req.getPlayerId());
			} catch (Exception e) {
				HorseManager.logger.error("[新坐骑系统] [查看其他玩家坐骑] [异常] [没有取得玩家信息] [" + player.getLogString() + "] [playerId:" + req.getPlayerId() + "]", e);
				return null;
			}
		}
		List<Horse> list = new ArrayList<Horse>();
		QUERY_HORSE_LIST2_RES res = null;
		try {
			List<Long> ids = tempP.getHorseIdList();
		
			for(long id : ids){
				Horse horse = HorseManager.getInstance().getHorseById(id,tempP);
				if(horse != null){
					horse.setHorseLevel(tempP.getSoulLevel());
					list.add(horse);
				}else{
					Horse2Manager.logger.error("[新坐骑系统] [查询坐骑失败] [坐骑为null] [坐骑id:"+id+"] ["+tempP.getLogString()+"]");
				}
			}
			res = new QUERY_HORSE_LIST2_RES(message.getSequnceNum(),req.getPlayerId(), list.toArray(new Horse[0]));
			if(Horse2Manager.logger.isDebugEnabled()){
				Horse2Manager.logger.debug("[新坐骑系统] [查询坐骑成功] ["+tempP.getLogString()+"]");
				for (int i=0; i<res.getHorseList().length; i++) {
					Horse2Manager.logger.debug("[新坐骑系统] [查询坐骑成功] ["+tempP.getLogString()+"] [返回坐骑名字:" + res.getHorseList()[i].getHorseShowName() + "] [成长:" + res.getHorseList()[i].getGrowRate() + "]");
				}
			}
		} catch (Exception e) {
			Horse2Manager.logger.error("[新坐骑系统] [查询坐骑2] ["+tempP.getLogString()+"]",e);
		}
		return res;
	}
	
	
	/**
	 * 请求单个坐骑数据
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_HORSE_DATA_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_HORSE_DATA_REQ req = (QUERY_HORSE_DATA_REQ) message;
		Player tempP = null;
		if (req.getPlayerId() <0 || req.getPlayerId() == player.getId()) {
			tempP = player;
		} else {
			try {
				tempP = GamePlayerManager.getInstance().getPlayer(req.getPlayerId());
			} catch (Exception e) {
				HorseManager.logger.error("[新坐骑系统] [查看其他玩家坐骑] [异常] [没有取得玩家信息] [" + player.getLogString() + "] [playerId:" + req.getPlayerId() + "]", e);
				return null;
			}
		}
		Horse horse = HorseManager.getInstance().getHorseById(req.getHorseId(),tempP);
		if(horse == null) {
			Horse2Manager.logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + tempP.getLogString() + "] [" + req.getHorseId() + "]");
			return null;
		}
		Horse2RelevantEntity entity = Horse2EntityManager.instance.getEntity(tempP.getId());
		int[] skillIds = null;
		int[] skillLevels = null;
		String[] skillIcons = null;
		if(horse.getOtherData().getSkillList() != null && horse.getOtherData().getSkillList().length > 0) {
			skillIds = horse.getOtherData().getSkillList();
			skillLevels = horse.getOtherData().getSkillLevel();
			skillIcons = new String[skillIds.length];
			for(int i=0; i<skillIds.length; i++) {
				skillIcons[i] = Horse2Manager.instance.getIconBySkillId(skillIds[i], skillLevels[i]);
			}
		} else {
			skillIds = new int[0];
			skillIcons = new String[0];
			skillLevels = new int[0];
		}
		
		int[] tempSkillIds = null;
		String[] tempSkillIcons = null;
		String[] tempskillNames = null;
		String[] tempskillDes = null;
		if(entity.getTempSkillList() != null && entity.getTempSkillList().size() > 0 && entity.getTempHorseId().contains(req.getHorseId())) {
			for (int ii=0; ii<entity.getTempHorseId().size(); ii++) {
				if (entity.getTempHorseId().get(ii) == req.getHorseId()) {
					tempSkillIds = new int[1];
					tempSkillIcons = new String[1];
					tempskillNames = new String[1];
					tempskillDes = new String[1];
					for(int i=0; i<tempSkillIds.length; i++) {
						tempSkillIds[i] = entity.getTempSkillList().get(ii);
						tempSkillIcons[i] = Horse2Manager.instance.getIconBySkillId(tempSkillIds[i], 0);
						String srt = "<f color='0x5aff00'>";
						if (Horse2Manager.instance.horseSkillMap.get(tempSkillIds[i]).getSkillType() != 0) {
							srt = "<f color='0xff8400'>";
						}
						tempskillNames[i] = srt + Horse2Manager.instance.horseSkillMap.get(tempSkillIds[i]).getName() + "</f>";
						HorseSkillModel hsm = Horse2Manager.instance.horseSkillMap.get(tempSkillIds[i]);
						String bt = MagicWeaponConstant.mappingKeyValue2.get(hsm.getAddAttrType());
						if (bt != null) {
							tempskillDes[i] = "<f size='28'>" + bt.replace("(", "").replace(")", "") + ":" + "</f>";
//							tempskillDes[i].replace("，", "");
							String addedNum = "";
							if (hsm.getAddType() == 0) {
								addedNum = (int)hsm.getAddNum()[0] + "";
							} else {
								int tt = (int) (hsm.getAddNum()[0] / 10L);
								addedNum =  tt + "%";
							}
							tempskillDes[i] += addedNum;
						} else {
							bt = "";
						}
					}
				}
			}
		} else {
			tempSkillIds = new int[0];
			tempSkillIcons = new String[0];
			tempskillNames = new String[0];
			tempskillDes = new String[0];
		}
		
		int free4Rank = Horse2Manager.instance.baseModel.getFreeTimes4Rank() - entity.getCultureTime4Free();
		int free4Skill = Horse2Manager.instance.baseModel.getFreeTimes4Skill() - entity.getRefreshSkillTime();
		free4Rank = free4Rank < 0 ? 0 : free4Rank;
		free4Skill = free4Skill < 0 ? 0 : free4Skill;
		HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(horse.getColorType());
		int maxBloodStar = hcm.getMaxStar();
		QUERY_HORSE_DATA_RES resp = new QUERY_HORSE_DATA_RES(message.getSequnceNum(), req.getPlayerId(), req.getHorseId(), skillIds, skillLevels, skillIcons, 
				tempSkillIds, tempSkillIcons, tempskillNames, tempskillDes, free4Rank, free4Skill, maxBloodStar);
		if (Horse2Manager.logger.isDebugEnabled()) {
			Horse2Manager.logger.debug("[新坐骑系统] [handle_QUERY_HORSE_DATA_REQ] [剩余免费刷新技能次数:" + resp.getFree4Skill() + "] [剩余 免费阶培养次数:" + resp.getFree4Rank() + "]");
		}
		return resp;
	}
	/**
	 * 获取技能池
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_SKILLS_MAP_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_SKILLS_MAP_REQ req = (QUERY_SKILLS_MAP_REQ) message;
		int[] skillIds = null;
		if (req.getSkillType() > 0) {
			skillIds = Horse2Manager.instance.getSkillsByLevel(req.getSkillType());
		} else {
			skillIds = Horse2Manager.instance.getAllSkill();
		}
		if(skillIds.length <= 0) {
			Horse2Manager.logger.warn("[新坐骑系统] [查询技能池失败] [客户端发过来类型为：" + req.getSkillType() + "] [" + player.getLogString() + "]");
		}
		String[] skillIcons = new String[skillIds.length];
		int[] skillTypes = new int[skillIds.length];
		for(int i=0; i<skillIds.length; i++) {
			skillIcons[i] = Horse2Manager.instance.getIconBySkillId(skillIds[i], 0);
			skillTypes[i] = Horse2Manager.instance.horseSkillMap.get(skillIds[i]).getSkillType();
		}
		QUERY_SKILLS_MAP_RES resp = new QUERY_SKILLS_MAP_RES(message.getSequnceNum(), skillIds, skillIcons, skillTypes);
		return resp;
	}
	/**
	 * 请求升级血脉星级
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_HORSE_BLOODSTAR_STRONG_REQ(Connection conn, RequestMessage message, Player player) {
		HORSE_BLOODSTAR_STRONG_REQ req = (HORSE_BLOODSTAR_STRONG_REQ) message;
		boolean result = Horse2EntityManager.instance.bloodStarStrong(player, req.getHorseId());
		HORSE_BLOODSTAR_STRONG_RES resp = new HORSE_BLOODSTAR_STRONG_RES(message.getSequnceNum(), (byte) (result ? 1 : 0));
		return resp;
	}
	/**
	 * 进行阶升星
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_HORSE_RANKSTAR_STRONG_REQ(Connection conn, RequestMessage message, Player player) {
		HORSE_RANKSTAR_STRONG_REQ req = (HORSE_RANKSTAR_STRONG_REQ) message;
		byte result = Horse2EntityManager.instance.rankStarStrong(player, req.getHorseId(), req.getStrongType(), false);
		if (result <= -2) {
			return null;
		}
		Horse2RelevantEntity entity = Horse2EntityManager.instance.getEntity(player.getId());
		
		int leftTime = 0;
		if (entity != null) {
			leftTime = Horse2Manager.instance.baseModel.getFreeTimes4Rank() - entity.getCultureTime4Free();
			if (leftTime < 0) {
				leftTime = 0;
			}
		}
		HORSE_RANKSTAR_STRONG_RES resp = new HORSE_RANKSTAR_STRONG_RES(message.getSequnceNum(), leftTime, result);
		return resp;
	}
	/**
	 * 刷新技能
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_REFRESH_HORSE_SKILL_REQ(Connection conn, RequestMessage message, Player player) {
		REFRESH_HORSE_SKILL_REQ req = (REFRESH_HORSE_SKILL_REQ) message;
		int[] resultId = Horse2EntityManager.instance.refreshSkill(player, req.getRefreshType(), req.getHorseId());
		String skillIcon = "";
		String skillName = "";
		if (resultId[0] > 0) {
			skillIcon = Horse2Manager.instance.getIconBySkillId(resultId[0], 0);
			HorseSkillModel hsm = Horse2Manager.instance.horseSkillMap.get(resultId[0]);
			if (hsm != null) {
				skillName = hsm.getName();
			}
		}
		Article a = ArticleManager.getInstance().getArticleByCNname(Horse2Manager.instance.baseModel.getNeedArticle4NomalSkill());
		Article a2 = ArticleManager.getInstance().getArticleByCNname(Horse2Manager.instance.baseModel.getNeedArticle4SpecSkill());
		Knapsack bag = player.getKnapsack_common();
		int count1 = 0;
		int count2 = 0;
		if (bag != null) {
			count1 = bag.countArticle(a.getName());
			count2 = bag.countArticle(a2.getName());
		}
//		int count1 = player.countArticleInKnapsacksByName(a.getName());
//		int count2 = player.countArticleInKnapsacksByName(a2.getName());
		REFRESH_HORSE_SKILL_RES resp = new REFRESH_HORSE_SKILL_RES(message.getSequnceNum(), req.getHorseId(), resultId[0], skillName, skillIcon, resultId[1], resultId[2], count1, count2);
		return resp;
	}
	/**
	 * 替换技能
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_LEARN_HORSE_SKILL_REQ(Connection conn, RequestMessage message, Player player) {
		LEARN_HORSE_SKILL_REQ req = (LEARN_HORSE_SKILL_REQ) message;
		boolean result = Horse2EntityManager.instance.learnSkill(player, req.getHorseId(), req.getSkillIndex(), req.getSkillId(), false);
		LEARN_HORSE_SKILL_RES resp = new LEARN_HORSE_SKILL_RES(message.getSequnceNum(), req.getHorseId(), (byte) (result?1:0));
		return resp;
	}
	/**
	 * 技能格开启描述
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_HORSE_SKILL_OPEN_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		GET_HORSE_SKILL_OPEN_INFO_REQ req = (GET_HORSE_SKILL_OPEN_INFO_REQ) message;
		int index = req.getSkillIndex();
		String jie = "八";
		if (index < Horse2Manager.needRankLevel.length) {
			jie = Horse2Manager.needRankLevel[index];
		}
		String description = String.format(Horse2Manager.instance.translate.get(7),jie);
		GET_HORSE_SKILL_OPEN_INFO_RES resp = new GET_HORSE_SKILL_OPEN_INFO_RES(message.getSequnceNum(), index, description);
		return resp;
	}
	/**
	 * 请求坐骑界面描述信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_HORSE_HELP_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		GET_HORSE_HELP_INFO_REQ req = (GET_HORSE_HELP_INFO_REQ) message;
		String des = "";
		if (req.getTypeIndex() == 0) {
			des = Horse2Manager.instance.translate.get(10);
		} else if (req.getTypeIndex() == 1) {
			des = HorseEquInlayManager.getInst().getTranslate("query_help_des");
		} else if (req.getTypeIndex() == 2) {
			des = HorseEquInlayManager.getInst().getTranslate("query_help_des_2");
		}
		GET_HORSE_HELP_INFO_RES res = new GET_HORSE_HELP_INFO_RES(message.getSequnceNum(), req.getTypeIndex(), des);
		return res;
	}
	/**
	 * 请求升级坐骑颜色窗口信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_HORSE_COLOR_STRONG_WIND_REQ(Connection conn, RequestMessage message, Player player) {
		GET_HORSE_COLOR_STRONG_WIND_REQ req = (GET_HORSE_COLOR_STRONG_WIND_REQ) message;
		Horse horse = HorseManager.getInstance().getHorseById(req.getHorseId(), player);
		if (horse == null) {
			Horse2Manager.logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + req.getHorseId() + "]");
			return null;
		}
		HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(horse.getColorType());
		int newcolorType = horse.getColorType()+1;
		String desCription = "";
		if (horse.getColorType() >= Horse2EntityManager.MAX_COLOR_TYPE) {
			newcolorType = Horse2EntityManager.MAX_COLOR_TYPE;
		}
		HorseColorModel hcm2 = Horse2Manager.instance.naturalRateMap.get(newcolorType);
		long articleId = hcm.getTempArticleId();
		int articleNum = hcm.getNeedNum();
		int growRate3 = 0;
		if (hcm2 != null) {
			growRate3 = (int) (hcm2.getGrowUpRate() * 100);
		}
		String[] icons = new String[2];
		Article a = ArticleManager.getInstance().getArticle(horse.getHorseName());
		icons[0] = icons[1] = a.getIconId();
		if (horse.getColorType() < Horse2EntityManager.MAX_COLOR_TYPE) {
			desCription = String.format(Horse2Manager.instance.translate.get(4), articleNum);
		} else {
			desCription = Horse2Manager.instance.translate.get(19);
		}
		HorseAttrModel changeHorseAttr = new HorseAttrModel();
		HorseAttrModel temp1 = Horse2Manager.instance.getNewHorseAttrModel(req.getHorseId(), horse.getCareer(), horse.getColorType(), horse.getOtherData().getRankStar(), 
				horse.getOtherData().getBloodStar(), horse.getOtherData().getSkillList(), horse.getOtherData().getSkillLevel());
		HorseAttrModel temp2 = Horse2Manager.instance.getNewHorseAttrModel(req.getHorseId(), horse.getCareer(), newcolorType, horse.getOtherData().getRankStar(), 
				horse.getOtherData().getBloodStar(), horse.getOtherData().getSkillList(), horse.getOtherData().getSkillLevel());
		for (Integer[] ii : temp2.getAttrNumAndType()) {
			changeHorseAttr.setValue(ii[0], ii[1] - temp1.getValue(ii[0]));
		}
		GET_HORSE_COLOR_STRONG_WIND_RES res = new GET_HORSE_COLOR_STRONG_WIND_RES(message.getSequnceNum(), req.getHorseId(), articleId, articleNum,
				desCription, icons, newcolorType, growRate3, changeHorseAttr);
		return res;
	}
	/**
	 * 升级坐骑颜色
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_HORSE_COLOR_STRONG_REQ(Connection conn, RequestMessage message, Player player) {
		HORSE_COLOR_STRONG_REQ req = (HORSE_COLOR_STRONG_REQ) message;
		boolean result = Horse2EntityManager.instance.colorStrong(player, req.getHorseId());
		HorseAttrModel[] changeHorseAttr = new HorseAttrModel[2];
		if (result) {
			Horse horse = HorseManager.getInstance().getHorseById(req.getHorseId(), player);
			changeHorseAttr[0] = Horse2Manager.instance.getNewHorseAttrModel(req.getHorseId(), horse.getCareer(), (horse.getColorType()-1), horse.getRank(), 
					horse.getOtherData().getBloodStar(), horse.getOtherData().getSkillList(), horse.getOtherData().getSkillLevel());
			changeHorseAttr[1] = Horse2Manager.instance.getNewHorseAttrModel(req.getHorseId(), horse.getCareer(), horse.getColorType(), horse.getRank(), 
					horse.getOtherData().getBloodStar(), horse.getOtherData().getSkillList(), horse.getOtherData().getSkillLevel());
		} else {
			changeHorseAttr[0] = new HorseAttrModel();
			changeHorseAttr[1] = new HorseAttrModel();
		}
		HORSE_COLOR_STRONG_RES resp = new HORSE_COLOR_STRONG_RES(message.getSequnceNum(), (byte) (result?1:0), changeHorseAttr);
		if (Horse2Manager.logger.isDebugEnabled()) {
			Horse2Manager.logger.debug("[新坐骑系统] [handle_HORSE_COLOR_STRONG_REQ返回] [之前属性:" + changeHorseAttr[0] + "] [升级后属性:" + changeHorseAttr[1] + "]");
		}
		return resp;
	}
	/**
	 * 升级坐骑技能
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_UPGRADE_HORSE_SKILL_REQ(Connection conn, RequestMessage message, Player player) {
		UPGRADE_HORSE_SKILL_REQ req = (UPGRADE_HORSE_SKILL_REQ) message;
		boolean result = Horse2EntityManager.instance.upGradeSkill(player, req.getHorseId(), req.getSkillIndex(), req.getSkillId());
		UPGRADE_HORSE_SKILL_RES resp = new UPGRADE_HORSE_SKILL_RES(message.getSequnceNum(), (byte) (result?1:0));
		return resp;
	}
	/**
	 * 请求升级颜色或者技能需要的物品信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_HORSE_NEED_ARTICLE_WIND_REQ(Connection conn, RequestMessage message, Player player) {
		GET_HORSE_NEED_ARTICLE_WIND_REQ req = (GET_HORSE_NEED_ARTICLE_WIND_REQ) message;
		Horse horse = HorseManager.getInstance().getHorseById(req.getHorseId(), player);
		if(horse == null) {
			Horse2Manager.logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + req.getHorseId() + "]");
			return null;
		}
		byte windType = 0;
		long articleId = 0;
		int articleNum = 0;
		String desCription = "";
		if (req.getSkillId() > 0) {
			boolean exist = false;
			int skLevel = 0;
			for (int i=0; i<horse.getOtherData().getSkillList().length; i++) {
				if (horse.getOtherData().getSkillList()[i] == req.getSkillId()) {
					skLevel = horse.getOtherData().getSkillLevel()[i];
					exist = true;
				}
			}
			if (!exist) {
				player.sendError(Translate.当前坐骑没有此技能);
				return null;
			}
			HorseSkillModel hsm = Horse2Manager.instance.horseSkillMap.get(req.getSkillId());
			if (skLevel >= hsm.getMaxLevel() - 1) {
				player.sendError(Translate.此技能已经达到最高级);
				return null;
			}
			windType = 2;
			articleId = hsm.getTempArticleIds()[skLevel];
			articleNum = hsm.getNeedArticleNum()[skLevel];
			desCription = Horse2Manager.instance.translate.get(5);
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(articleId);
			desCription = String.format(desCription, ae.getArticleName());
		} else {
			if (horse.getColorType() >= Horse2EntityManager.MAX_COLOR_TYPE) {
				player.sendError(Horse2Manager.instance.translate.get(19));
				return null;
			}
			windType = 1;
			HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(horse.getColorType());
			HorseColorModel hcm2 = Horse2Manager.instance.naturalRateMap.get(horse.getColorType()+1);
			articleId = hcm.getTempArticleId();
			articleNum = hcm.getNeedNum();
			String currColor = "白色";
			String nextColor = "绿色";
			String growRate1 = hcm.getGrowUpRate() + "";
			String growRate2 = "1.11";
			if (hcm2 != null) {
				growRate2 = hcm2.getGrowUpRate() + "";
			}
			if (horse.getColorType() < ArticleManager.color_article_Strings.length) {
				currColor = ArticleManager.color_article_Strings[horse.getColorType()];
				if ((horse.getColorType()+1) < ArticleManager.color_article_Strings.length) {
					nextColor = ArticleManager.color_article_Strings[horse.getColorType()+1];
				}
			}
			desCription = String.format(Horse2Manager.instance.translate.get(4), currColor, growRate1, nextColor, growRate2, articleNum);
		}
		GET_HORSE_NEED_ARTICLE_WIND_RES resp = new GET_HORSE_NEED_ARTICLE_WIND_RES(message.getSequnceNum(), req.getHorseId(), articleId, 
				articleNum, windType, desCription);
		return resp;
	}
	/**
	 * 请求坐骑技能描述
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_HORSE_SKILL_INFO_SHOW_REQ(Connection conn, RequestMessage message, Player player) {
		GET_HORSE_SKILL_INFO_SHOW_REQ req = (GET_HORSE_SKILL_INFO_SHOW_REQ) message;
		Player tempP = null;
		int skLevel = 0;
		if (req.getPlayerId() < 0) {
			tempP = player;
		} else {
			try {
				tempP = GamePlayerManager.getInstance().getPlayer(req.getPlayerId());
			} catch (Exception e) {
				Horse2Manager.logger.warn("[新坐骑系统] [获取player出错] [" + player.getLogString() + "] [目标playerId :" + req.getPlayerId() + "]", e);
			}
		}
		if (req.getHorseId() > 0 && tempP != null) {
			Horse horse = HorseManager.getInstance().getHorseById(req.getHorseId(), tempP);
			if(horse == null) {
				Horse2Manager.logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + tempP.getLogString() + "] [" + req.getHorseId() + "]");
			} else {
				skLevel = horse.getOtherData().getSkillLevelById(req.getSkillId());
			}
		}
		HorseSkillModel hsm = Horse2Manager.instance.horseSkillMap.get(req.getSkillId());
		if (Horse2Manager.logger.isDebugEnabled()) {
			Horse2Manager.logger.debug("[新坐骑系统] [请求技能泡泡] [" + player.getLogString() + "] [skLevel:" + skLevel  + "] [skillId :" + req.getSkillId() + "] [tempP:" + tempP + "] [req.getPlayerId():" + req.getPlayerId() + "][req.getHorseId():" + req.getHorseId() +"]");
		}
		if (hsm != null) {
			GET_HORSE_SKILL_INFO_SHOW_RES resp = new GET_HORSE_SKILL_INFO_SHOW_RES(message.getSequnceNum(), req.getSkillId(), hsm.getInfoShow(skLevel));
			return resp;
		}
		return null;
	}
	/**
	 * 打开坐骑花道具刷技能界面
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_HORSE_SKILL_WINDOW_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		GET_HORSE_SKILL_WINDOW_INFO_REQ req = (GET_HORSE_SKILL_WINDOW_INFO_REQ) message;
		Horse horse = HorseManager.getInstance().getHorseById(req.getHorseId(), player);
		if (horse != null) {
			int tempSkillId = Horse2EntityManager.instance.getTempSkillIdById(player, req.getHorseId());
			long[] articleIds = Horse2Manager.instance.articleId4Skill;
			String tempSkillIcon = Horse2Manager.instance.getIconBySkillId(tempSkillId, 0);
			String normalIcon = Horse2Manager.instance.translate.get(2) == null ? "" : Horse2Manager.instance.translate.get(2);
			String specilIcon = Horse2Manager.instance.translate.get(3) == null ? "" : Horse2Manager.instance.translate.get(3);
			String[] icons = new String[]{normalIcon, specilIcon};
			String desCription = Horse2Manager.instance.translate.get(1) == null ? "" : Horse2Manager.instance.translate.get(1);
			Article a = ArticleManager.getInstance().getArticleByCNname(Horse2Manager.instance.baseModel.getNeedArticle4NomalSkill());
			Article a2 = ArticleManager.getInstance().getArticleByCNname(Horse2Manager.instance.baseModel.getNeedArticle4SpecSkill());
			Knapsack bag = player.getKnapsack_common();
			int count1 = 0;
			int count2 = 0;
			if (bag != null) {
				count1 = bag.countArticle(a.getName());
				count2 = bag.countArticle(a2.getName());
			}
//			int count1 = player.countArticleInKnapsacksByName(a.getName());
//			int count2 = player.countArticleInKnapsacksByName(a2.getName());
			
			GET_HORSE_SKILL_WINDOW_INFO_RES resp = new GET_HORSE_SKILL_WINDOW_INFO_RES(message.getSequnceNum(), req.getHorseId(), 
					tempSkillId, tempSkillIcon, icons, articleIds, desCription, count1, count2);
			return resp;
		}
		Horse2Manager.logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + req.getHorseId() + "]");
		return null;
	}
	/**
	 * 打开阶培养界面
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_HORSE_RANK_WINDOW_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		GET_HORSE_RANK_WINDOW_INFO_REQ req = (GET_HORSE_RANK_WINDOW_INFO_REQ) message;
		Horse horse = HorseManager.getInstance().getHorseById(req.getHorseId(), player);
		if (horse != null) {
			HorseAttrModel changeHorseAttr = new HorseAttrModel();
			int rankStar = horse.getOtherData().getRankStar();
			int newStar = rankStar + 1;
			if (newStar <= (Horse2EntityManager.MAX_RANK_STAR + horse.getOtherData().getUpLevel())) {
				HorseAttrModel temp1 = Horse2Manager.instance.getNewHorseAttrModel(req.getHorseId(), horse.getCareer(), horse.getColorType(), rankStar, 
						horse.getOtherData().getBloodStar(), horse.getOtherData().getSkillList(), horse.getOtherData().getSkillLevel());
				HorseAttrModel temp2 = Horse2Manager.instance.getNewHorseAttrModel(req.getHorseId(), horse.getCareer(), horse.getColorType(), newStar, 
						horse.getOtherData().getBloodStar(), horse.getOtherData().getSkillList(), horse.getOtherData().getSkillLevel());
				for (Integer[] ii : temp2.getAttrNumAndType()) {
					changeHorseAttr.setValue(ii[0], ii[1] - temp1.getValue(ii[0]));
				}
			}
			HorseRankModel hrm = Horse2Manager.instance.rankModelMap.get(rankStar);
			long currentExp = horse.getOtherData().getTrainExp();
			long nextStarNeedExp = hrm.getNeedExp();
			if (rankStar >= (Horse2EntityManager.MAX_RANK_STAR + horse.getOtherData().getUpLevel())) {
				currentExp = nextStarNeedExp;
			}
			long needArticleId = Horse2Manager.instance.rankPeiyangArticleId;
			String helpInfo = Horse2Manager.instance.translate.get(6);
			
			String articleName = Horse2Manager.instance.baseModel.getNeedArticle4Rank();
			Article a = ArticleManager.getInstance().getArticleByCNname(articleName);
			Knapsack bag = player.getKnapsack_common();
			int count = 0;
			if (bag != null) {
				count = bag.countArticle(a.getName());
			}
//			int count = player.countArticleInKnapsacksByName(a.getName());
			
			GET_HORSE_RANK_WINDOW_INFO_RES resp = new GET_HORSE_RANK_WINDOW_INFO_RES(message.getSequnceNum(), req.getHorseId(), currentExp, 
					nextStarNeedExp, needArticleId, helpInfo, count, changeHorseAttr);
			if (Horse2Manager.logger.isDebugEnabled()) {
				Horse2Manager.logger.debug("[handle_GET_HORSE_RANK_WINDOW_INFO_REQ] [" + player.getLogString() + "] [" + changeHorseAttr + "]");
			}
			return resp;
		}
		Horse2Manager.logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + req.getHorseId() + "]");
		return null;
	}
	/**
	 * 打开血脉升星界面
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_HORSE_BLOOD_WINDOW_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		GET_HORSE_BLOOD_WINDOW_INFO_REQ req = (GET_HORSE_BLOOD_WINDOW_INFO_REQ) message;
		Horse horse = HorseManager.getInstance().getHorseById(req.getHorseId(), player);
		if (horse != null) {
			HorseAttrModel changeHorseAttr = new HorseAttrModel();
			int bloodStar = horse.getOtherData().getBloodStar();
			int newStar = bloodStar + 1;
			if (newStar <= Horse2EntityManager.MAX_BLOOD_STAR) {
				HorseAttrModel temp1 = Horse2Manager.instance.getNewHorseAttrModel(req.getHorseId(), horse.getCareer(), horse.getColorType(), horse.getOtherData().getRankStar(), 
						bloodStar, horse.getOtherData().getSkillList(), horse.getOtherData().getSkillLevel());
				HorseAttrModel temp2 = Horse2Manager.instance.getNewHorseAttrModel(req.getHorseId(), horse.getCareer(), horse.getColorType(), horse.getOtherData().getRankStar(), 
						newStar, horse.getOtherData().getSkillList(), horse.getOtherData().getSkillLevel());
				for (Integer[] ii : temp2.getAttrNumAndType()) {
					changeHorseAttr.setValue(ii[0], ii[1] - temp1.getValue(ii[0]));
				}
			}
			HorseColorModel hcm = Horse2Manager.instance.naturalRateMap.get(horse.getColorType());
			BloodStarModel bsm = Horse2Manager.instance.bloodStarMap.get(bloodStar);
			PetsOfPlayer bean0 = Pet2Manager.getInst().findPetsOfPlayer(player);
			long currentXuemai = bean0.getXueMai();
			long needXuemai = bsm.getNeedBloodNum();
			int maxBloodStar = hcm.getMaxStar();
			GET_HORSE_BLOOD_WINDOW_INFO_RES resp = new GET_HORSE_BLOOD_WINDOW_INFO_RES(message.getSequnceNum(), req.getHorseId(), currentXuemai, 
					needXuemai, maxBloodStar, changeHorseAttr);
			if (Horse2Manager.logger.isDebugEnabled()) {
				Horse2Manager.logger.debug("[新坐骑系统] [handle_GET_HORSE_BLOOD_WINDOW_INFO_REQ] [玩家剩余血脉值:" + resp.getCurrentXuemai() + "] [升级需要血脉值:" + resp.getNeedXuemai() + "] [" + player.getLogString() + "]");
			}
			return resp;
		}
		Horse2Manager.logger.warn("[新坐骑系统] [获取玩家当前元神坐骑异常] [" + player.getLogString() + "] [" + req.getHorseId() + "]");
		return null;
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
