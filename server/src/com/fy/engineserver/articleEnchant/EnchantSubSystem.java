package com.fy.engineserver.articleEnchant;

import java.util.Arrays;

import com.fy.engineserver.articleEnchant.client.CompaModel;
import com.fy.engineserver.articleEnchant.model.EnchantArticle;
import com.fy.engineserver.articleEnchant.model.EnchantModel;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.ENCHANT_ALL_EQUIPT_REQ;
import com.fy.engineserver.message.ENCHANT_ALL_EQUIPT_RES;
import com.fy.engineserver.message.ENCHANT_EQUIPMENT_REQ;
import com.fy.engineserver.message.ENCHANT_EQUIPMENT_RES;
import com.fy.engineserver.message.ENCHANT_LOCK_REQ;
import com.fy.engineserver.message.ENCHANT_UNLOCK_REQ;
import com.fy.engineserver.message.ENCHANT_UNLOCK_RES;
import com.fy.engineserver.message.GET_ARTICLE_DES_REQ;
import com.fy.engineserver.message.GET_ARTICLE_DES_RES;
import com.fy.engineserver.message.GET_ENCHANT_DESCRIPTION_REQ;
import com.fy.engineserver.message.OPEN_ENCHANT_LOCK_WINDOW_REQ;
import com.fy.engineserver.message.OPEN_ENCHANT_LOCK_WINDOW_RES;
import com.fy.engineserver.message.SGET_ENCHANT_DESCRIPTION_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class EnchantSubSystem extends SubSystemAdapter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "EnchantSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[]{"GET_ENCHANT_DESCRIPTION_REQ", "GET_ARTICLE_DES_REQ", "ENCHANT_EQUIPMENT_REQ", "OPEN_ENCHANT_LOCK_WINDOW_REQ"
				,"ENCHANT_LOCK_REQ", "ENCHANT_UNLOCK_REQ", "ENCHANT_ALL_EQUIPT_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, EnchantEntityManager.logger);
		if (EnchantEntityManager.logger.isDebugEnabled()) {
			EnchantEntityManager.logger.debug("[收到玩家消息] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]");
		}
		try {
			if (message instanceof GET_ENCHANT_DESCRIPTION_REQ) {
				return handle_GET_ENCHANT_DESCRIPTION_REQ(conn, message, player);
			} else if (message instanceof GET_ARTICLE_DES_REQ) {
				return handle_GET_ARTICLE_DES_REQ(conn, message, player);
			} else if (message instanceof ENCHANT_EQUIPMENT_REQ) {
				return handle_ENCHANT_EQUIPMENT_REQ(conn, message, player);
			} else if (message instanceof OPEN_ENCHANT_LOCK_WINDOW_REQ) {
				return handle_OPEN_ENCHANT_LOCK_WINDOW_REQ(conn, message, player);
			} else if (message instanceof ENCHANT_LOCK_REQ) {
				return handle_ENCHANT_LOCK_REQ(conn, message, player);
			} else if (message instanceof ENCHANT_UNLOCK_REQ) {
				return handle_ENCHANT_UNLOCK_REQ(conn, message, player);
			} else if (message instanceof ENCHANT_ALL_EQUIPT_REQ) {
				return handle_ENCHANT_ALL_EQUIPT_REQ(conn, message, player);
			}
		} catch (Exception e) {
			EnchantEntityManager.logger.warn("[处理附魔相关协议] [异常] [" + player.getLogString() + "]", e);
		}
		return null;
	}
	/**
	 * 一键锁定（解锁）
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ENCHANT_ALL_EQUIPT_REQ(Connection conn, RequestMessage message, Player player) {
		ENCHANT_ALL_EQUIPT_REQ req = (ENCHANT_ALL_EQUIPT_REQ) message;
		
		if (req.getEnchantType() == 0) {
			boolean rr = EnchantEntityManager.instance.lockAllEnchant(player, req.getEnchantType());
			int result = rr ? 1 : -1;
			ENCHANT_ALL_EQUIPT_RES resp = new ENCHANT_ALL_EQUIPT_RES(message.getSequnceNum(), result, req.getEnchantType());
			return resp;
		} 
		EnchantEntityManager.instance.openLockTimerTask(player, -1, -1, 2, false);
		return null;
	}
	/**
	 * 请求附魔相关规则结束
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_ENCHANT_DESCRIPTION_REQ(Connection conn, RequestMessage message, Player player) {
		GET_ENCHANT_DESCRIPTION_REQ req = (GET_ENCHANT_DESCRIPTION_REQ) message;
		String result = EnchantManager.instance.translate.get(req.getRuleType());
		SGET_ENCHANT_DESCRIPTION_RES resp = new SGET_ENCHANT_DESCRIPTION_RES(message.getSequnceNum(), result);
		return resp;
	}
	/**
	 * 请求装备附魔描述
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_ARTICLE_DES_REQ(Connection conn, RequestMessage message, Player player) {
		GET_ARTICLE_DES_REQ req = (GET_ARTICLE_DES_REQ) message;
		String articleDes = "";
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(req.getArticleId());
		if (ae != null && ae instanceof EquipmentEntity) {
			articleDes = ((EquipmentEntity)ae).getEnchantInfoShow(player, false);
		} else if (ae != null) {
			Article ac = ArticleManager.getInstance().getArticle(ae.getArticleName());
			if (ac instanceof EnchantArticle) {
				EnchantModel model = EnchantManager.instance.modelMap.get(((EnchantArticle)ac).getEnchantId());
				StringBuffer sb = new StringBuffer();
				sb.append("\n\n<f color='0xFFFF00' size='28'>").append(ac.getDescription()).append("</f>");
				sb.append("\n\n<f size='28'>").append(Translate.灵性).append(":").append(model.getDurable()).append("/").append(model.getDurable()).append("</f>");
				articleDes = sb.toString();
			}
		}
		GET_ARTICLE_DES_RES resp = new GET_ARTICLE_DES_RES(message.getSequnceNum(), articleDes, req.getArticleId());
		return resp;
	}
	/**
	 * 附魔
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ENCHANT_EQUIPMENT_REQ(Connection conn, RequestMessage message, Player player) {
		ENCHANT_EQUIPMENT_REQ req = (ENCHANT_EQUIPMENT_REQ) message;
		boolean result = EnchantEntityManager.instance.enchant(player, req.getArticleId(), req.getMetriIndex(), false);
		if (result) {
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(req.getArticleId());
			String articleDes = "";
			if (ae != null && ae instanceof EquipmentEntity) {
				articleDes = ((EquipmentEntity)ae).getEnchantInfoShow(player, false);
			}
			ENCHANT_EQUIPMENT_RES resp = new ENCHANT_EQUIPMENT_RES(message.getSequnceNum(), articleDes);
			return resp;
		}
		return null;
	}
	/**
	 * 打开锁定附魔界面
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_OPEN_ENCHANT_LOCK_WINDOW_REQ(Connection conn, RequestMessage message, Player player) {
		int len = EnchantManager.开放附魔的位置.length;
		long[] articleIds = new long[len];
		int[] articleTypes = new int[len];
		int[] enchantStatus = new int[len];
		String[] description1 = new String[len];
		String[] description2 = new String[len];
		
		int[] temp = Arrays.copyOf(EnchantManager.开放附魔的位置, EnchantManager.开放附魔的位置.length);
		
		CompaModel[] list = new CompaModel[len];
		
		for (int i=0; i<len; i++) {
			ArticleEntity ae = player.getEquipmentColumns().get(temp[i]);
			CompaModel cm = null;
			if (ae == null) {
				cm = new CompaModel(-1, temp[i], 0, "", "");
			} else {
				EquipmentEntity ee = (EquipmentEntity) ae;
				String[] ss = ee.getSimpleEnchantInfo();
				int status = 0;
				if (ee.getEnchantData() == null || ee.getEnchantData().getEnchants().size() <= 0) {
//					enchantStatus[i] = 0;
				} else if (ee.getEnchantData().isLock()) {
					status = 2;
				} else {
					status = 1;
				}
				cm = new CompaModel(ee.getId(), temp[i], status, ss[0], ss[1]);
			}
			list[i] = cm;
		}
		Arrays.sort(list);
		for (int i=0;i<list.length; i++) {
			articleIds[i] = list[i].articleIds;
			articleTypes[i] = list[i].articleTypes;
			enchantStatus[i] = list[i].enchantStatus;
			description1[i] = list[i].description1;
			description2[i] = list[i].description2;
		}
		
		OPEN_ENCHANT_LOCK_WINDOW_RES resp = new OPEN_ENCHANT_LOCK_WINDOW_RES(message.getSequnceNum(), articleIds, articleTypes, 
				enchantStatus, description1, description2);
		return resp;
	}
	/**
	 * 锁定附魔
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ENCHANT_LOCK_REQ(Connection conn, RequestMessage message, Player player) {
		ENCHANT_LOCK_REQ req = (ENCHANT_LOCK_REQ) message;
		
		int index = EnchantManager.getEquiptIndex(player, req.getArticleId());
		if (index < 0) {
			player.sendError("装备id错误:" + req.getArticleId());
			return null;
		}
		if (player.getBindSilver() + player.getShopSilver() + player.getSilver() < EnchantManager.锁定附魔消耗) {
			player.sendError(Translate.金币不足);
			return null;
		}
		EnchantEntityManager.instance.openLockTimerTask(player, index, req.getArticleId(), 1,false);
//		player.getTimerTaskAgent().createTimerTask(new EnchantTimerTask(player, index, req.getArticleId()), EnchantManager.锁定读条时间,TimerTask.type_附魔锁定);
//		player.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), EnchantManager.锁定读条时间, Translate.锁定附魔));
		
		return null;
		/*boolean rr = EnchantEntityManager.instance.lockEnchant(player, index);
		int result = rr ? 1 : 0;
		ENCHANT_LOCK_RES resp = new ENCHANT_LOCK_RES(message.getSequnceNum(), result, req.getArticleId());
		return resp;*/
	}
	/**
	 * 解锁附魔
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ENCHANT_UNLOCK_REQ(Connection conn, RequestMessage message, Player player) {
		ENCHANT_UNLOCK_REQ req = (ENCHANT_UNLOCK_REQ) message;
		
		int index = EnchantManager.getEquiptIndex(player, req.getArticleId());
		if (index < 0) {
			player.sendError("装备id错误:" + req.getArticleId());
			return null;
		}
		boolean rr = EnchantEntityManager.instance.unlockEnchant(player, index);
		int result = rr ? 1 : 0;
		ENCHANT_UNLOCK_RES resp = new ENCHANT_UNLOCK_RES(message.getSequnceNum(), result, req.getArticleId());
		return resp;
	}
	

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
