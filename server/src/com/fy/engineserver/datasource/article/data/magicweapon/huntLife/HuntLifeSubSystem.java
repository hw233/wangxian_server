package com.fy.engineserver.datasource.article.data.magicweapon.huntLife;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.client.Shouhun4Client;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model.ShouHunModel;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.GET_SHOUHUN_ADDEXPS_REQ;
import com.fy.engineserver.message.GET_SHOUHUN_ADDEXPS_RES;
import com.fy.engineserver.message.GET_SHOUHUN_KNAP_REQ;
import com.fy.engineserver.message.GET_SHOUHUN_KNAP_RES;
import com.fy.engineserver.message.OPEN_SHOUHUN_LUCK_WINDOW_REQ;
import com.fy.engineserver.message.OPEN_SHOUHUN_LUCK_WINDOW_RES;
import com.fy.engineserver.message.OPEN_SHOUHUN_WINDOW_REQ;
import com.fy.engineserver.message.OPEN_SHOUHUN_WINDOW_RES;
import com.fy.engineserver.message.REPLACE_ALL_SHOUHUN_REQ;
import com.fy.engineserver.message.REPLACE_ALL_SHOUHUN_RES;
import com.fy.engineserver.message.REPLACE_ONE_SHOUHUN_REQ;
import com.fy.engineserver.message.REPLACE_ONE_SHOUHUN_RES;
import com.fy.engineserver.message.SHOUHUN_TUNSHI_REQ;
import com.fy.engineserver.message.SHOUHUN_TUNSHI_RES;
import com.fy.engineserver.message.TAKEOUT_SHOUHUN_REQ;
import com.fy.engineserver.message.TAKEOUT_SHOUHUN_RES;
import com.fy.engineserver.message.TAKE_SHOUHUN_LUCK_REQ;
import com.fy.engineserver.message.ZHENLI_SHOUHUN_KNAP_REQ;
import com.fy.engineserver.message.ZHENLI_SHOUHUN_KNAP_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class HuntLifeSubSystem extends SubSystemAdapter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "HuntLifeSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[]{"OPEN_SHOUHUN_WINDOW_REQ", "REPLACE_ALL_SHOUHUN_REQ", "REPLACE_ONE_SHOUHUN_REQ", "SHOUHUN_TUNSHI_REQ"
				,"OPEN_SHOUHUN_LUCK_WINDOW_REQ", "TAKE_SHOUHUN_LUCK_REQ", "TAKEOUT_SHOUHUN_REQ", "GET_SHOUHUN_ADDEXPS_REQ"
				,"GET_SHOUHUN_KNAP_REQ", "ZHENLI_SHOUHUN_KNAP_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, EnchantEntityManager.logger);
		if (HuntLifeManager.logger.isDebugEnabled()) {
			HuntLifeManager.logger.debug("[收到玩家消息] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]");
		}
		try {
			if (message instanceof OPEN_SHOUHUN_WINDOW_REQ) {
				return handle_OPEN_SHOUHUN_WINDOW_REQ(conn, message, player);
			} else if (message instanceof REPLACE_ALL_SHOUHUN_REQ) {
				return handle_REPLACE_ALL_SHOUHUN_REQ(conn, message, player);
			} else if (message instanceof REPLACE_ONE_SHOUHUN_REQ) {
				return handle_REPLACE_ONE_SHOUHUN_REQ(conn, message, player);
			} else if (message instanceof SHOUHUN_TUNSHI_REQ) {
				return handle_SHOUHUN_TUNSHI_REQ(conn, message, player);
			} else if (message instanceof OPEN_SHOUHUN_LUCK_WINDOW_REQ) {
				return handle_OPEN_SHOUHUN_LUCK_WINDOW_REQ(conn, message, player);
			} else if (message instanceof TAKE_SHOUHUN_LUCK_REQ) {
				return handle_TAKE_SHOUHUN_LUCK_REQ(conn, message, player);
			} else if (message instanceof TAKEOUT_SHOUHUN_REQ) {
				return handle_TAKEOUT_SHOUHUN_REQ(conn, message, player);
			} else if (message instanceof GET_SHOUHUN_ADDEXPS_REQ) {
				return handle_GET_SHOUHUN_ADDEXPS_REQ(conn, message, player);
			} else if (message instanceof GET_SHOUHUN_KNAP_REQ) {
				return handle_GET_SHOUHUN_KNAP_REQ(conn, message, player);
			} else if (message instanceof ZHENLI_SHOUHUN_KNAP_REQ) {
				return handle_ZHENLI_SHOUHUN_KNAP_REQ(conn, message, player);
			} 
		} catch (Exception e) {
			HuntLifeManager.logger.warn("[处理协议异常] [" + message.getTypeDescription() + "] [" + player.getLogString() + "]", e);
		}
		
		return null;
	}
	/**
	 * 获取兽魂仓库
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_SHOUHUN_KNAP_REQ(Connection conn, RequestMessage message, Player player) {
		GET_SHOUHUN_KNAP_RES resp = new GET_SHOUHUN_KNAP_RES(message.getSequnceNum(), player.getShouhunKnap());
		return resp;
	}
	/**
	 * 整理兽魂仓库
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ZHENLI_SHOUHUN_KNAP_REQ(Connection conn, RequestMessage message, Player player) {
		player.sortShouhunKnap();
		ZHENLI_SHOUHUN_KNAP_RES rsep = new ZHENLI_SHOUHUN_KNAP_RES(message.getSequnceNum(), player.getShouhunKnap());
		return rsep;
	}
	
	public ResponseMessage handle_GET_SHOUHUN_ADDEXPS_REQ(Connection conn, RequestMessage message, Player player) {
		GET_SHOUHUN_ADDEXPS_REQ req = (GET_SHOUHUN_ADDEXPS_REQ) message;
		long addExps = HuntLifeEntityManager.instance.getMateriaAddExps(req.getShouhunIds());
		GET_SHOUHUN_ADDEXPS_RES resp = new GET_SHOUHUN_ADDEXPS_RES(message.getSequnceNum(), addExps);
		return resp;
	}
	/**
	 * 抽取兽魂
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TAKEOUT_SHOUHUN_REQ(Connection conn, RequestMessage message, Player player) {
		TAKEOUT_SHOUHUN_REQ req = (TAKEOUT_SHOUHUN_REQ) message;
		if (req.getShouhunId() <= 0) {
			return null;
		}
		boolean b = HuntLifeEntityManager.instance.takeShouhun(player, req.getShouhunId());
		byte result = (byte) (b ? 1 : 0);
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(req.getShouhunId());
		HuntLifeArticle a = (HuntLifeArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
		Shouhun4Client shouhunModel = HuntLifeManager.instance.createModel4Client(player, a.getAddAttrType());
		TAKEOUT_SHOUHUN_RES resp = new TAKEOUT_SHOUHUN_RES(message.getSequnceNum(), result, shouhunModel);
		return resp;
	}
	/**
	 * 打开兽魂抽奖界面
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_OPEN_SHOUHUN_LUCK_WINDOW_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.getLevel() < 110) {
			player.sendError(Translate.等级不足);
			return null;
		}
		HuntLifeEntity he = player.getHuntLifr();
		long now = System.currentTimeMillis();
		long result = HuntLifeManager.instance.baseModel.getInterverFreeTime() - (now - he.getLastTaskFreeTime());
		if (result < 0) {
			result = 0L;
		}
		OPEN_SHOUHUN_LUCK_WINDOW_RES resp = new OPEN_SHOUHUN_LUCK_WINDOW_RES(message.getSequnceNum(), result, Translate.单抽描述, Translate.十连抽描述);
		return resp;
	}
	/**
	 * 兽魂抽奖
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TAKE_SHOUHUN_LUCK_REQ(Connection conn, RequestMessage message, Player player) {
		TAKE_SHOUHUN_LUCK_REQ req = (TAKE_SHOUHUN_LUCK_REQ) message;
		boolean free = false;
		long now = System.currentTimeMillis();
		HuntLifeEntity he = player.getHuntLifr();
		free = (now - he.getLastTaskFreeTime()) >= HuntLifeManager.instance.baseModel.getInterverFreeTime();
		if (req.getTakeType() == 0 && !free && !HuntLifeManager.tempIdList.contains(player.getId())) {
			HuntLifeManager.popConfirmWindow2(player, req.getTakeType());
			HuntLifeManager.tempIdList.add(player.getId());
		} else if (req.getTakeType() == 1 && !HuntLifeManager.tempIdList2.contains(player.getId())) {
			HuntLifeManager.popConfirmWindow2(player, req.getTakeType());
			HuntLifeManager.tempIdList2.add(player.getId());
		} else {
			if (req.getTakeType() == 0) {		//单抽
				return HuntLifeManager.instance.onceHunt(player, req);
			} else if (req.getTakeType() == 1) {	//十连抽
				return HuntLifeManager.instance.tenHunt(player, req);
			}
		}
		return null;
	}
	
	/**
	 * 打开裂魂主界面
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_OPEN_SHOUHUN_WINDOW_REQ(Connection conn, RequestMessage message, Player player) {
		if (player.getLevel() < 110) {
			player.sendError(Translate.等级不足);
			return null;
		}
		int len = HuntLifeManager.命格属性类型.length;
		Shouhun4Client[] shouhunModels = new Shouhun4Client[len];
		for (int i=0; i<len; i++) {
			shouhunModels[i] = HuntLifeManager.instance.createModel4Client(player, HuntLifeManager.命格属性类型[i]);
		}
		OPEN_SHOUHUN_WINDOW_RES res = new OPEN_SHOUHUN_WINDOW_RES(message.getSequnceNum(), shouhunModels, HuntLifeManager.对应兽魂道具名);
		return res;
	}
	/**
	 * 一键装备最好兽魂
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_REPLACE_ALL_SHOUHUN_REQ(Connection conn, RequestMessage message, Player player) {
		long[] shouhunIds = player.getShouhunKnap();
		Map<Integer, TempHuntArticle[]> maps = new HashMap<Integer, TempHuntArticle[]>();
		for (int i=0; i<shouhunIds.length; i++) {
			if (shouhunIds[i] > 0) {
				ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(shouhunIds[i]);
				if (ae instanceof HuntLifeArticleEntity) {
					HuntLifeArticleEntity tt = (HuntLifeArticleEntity) ae;
					HuntLifeArticle a = (HuntLifeArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
					TempHuntArticle[] list = maps.get(a.getAddAttrType());
					if (list == null) {
						list = new TempHuntArticle[0];
					}
					list = Arrays.copyOf(list, list.length + 1);
					list[list.length - 1] = new TempHuntArticle(ae.getId(), tt.getExtraData().getLevel(), tt.getExtraData().getExps());
					maps.put(a.getAddAttrType(), list);
				}
			}
		}
		Shouhun4Client[] shouhunModels = new Shouhun4Client[0];
		if (maps.size() > 0) {
			HuntLifeEntity entity = player.getHuntLifr();
			for (int i=0; i<HuntLifeManager.命格属性类型.length; i++) {
				TempHuntArticle[] list = maps.get(HuntLifeManager.命格属性类型[i]);
				if (list != null && list.length > 0) {
					HuntLifeManager.sort(list);
					long oldId = entity.getHuntDatas()[HuntLifeManager.命格属性类型[i]];
					boolean replace = false;
					if (oldId <= 0) {
						replace = true;
					} else {
						HuntLifeArticleEntity oldAe = (HuntLifeArticleEntity) DefaultArticleEntityManager.getInstance().getEntity(oldId);
						if (oldAe.getExtraData().getLevel() < list[0].level) {
							replace = true;
						}
					}
					if (replace) {
						HuntLifeEntityManager.instance.inlay(player, list[0].articleId, false);
						shouhunModels = Arrays.copyOf(shouhunModels, shouhunModels.length + 1);
						shouhunModels[shouhunModels.length - 1] = HuntLifeManager.instance.createModel4Client(player, HuntLifeManager.命格属性类型[i]);
					}
				}
			}
		}
		if (shouhunModels.length <= 0) {
			player.sendError(Translate.没有可以替换的兽魂);
			return null;
		}
		player.sendError(Translate.text_替换成功);
		REPLACE_ALL_SHOUHUN_RES resp = new REPLACE_ALL_SHOUHUN_RES(message.getSequnceNum(), shouhunModels);
		return resp;
	}
	/**
	 * 装备单个兽魂
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_REPLACE_ONE_SHOUHUN_REQ(Connection conn, RequestMessage message, Player player) {
		REPLACE_ONE_SHOUHUN_REQ req = (REPLACE_ONE_SHOUHUN_REQ) message;
		boolean result = HuntLifeEntityManager.instance.inlay(player, req.getArticleId(), true);
		if (result) {
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(req.getArticleId());
			HuntLifeArticle a = (HuntLifeArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
			Shouhun4Client shouhunModel = HuntLifeManager.instance.createModel4Client(player, a.getAddAttrType());
			REPLACE_ONE_SHOUHUN_RES resp = new REPLACE_ONE_SHOUHUN_RES(message.getSequnceNum(), shouhunModel);
			return resp;
		}
		return null;
	}
	/**
	 * 吞噬
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SHOUHUN_TUNSHI_REQ(Connection conn, RequestMessage message, Player player) {
		SHOUHUN_TUNSHI_REQ req = (SHOUHUN_TUNSHI_REQ) message;
		boolean result = HuntLifeEntityManager.instance.tunshi(player, req.getMainArticleId(), req.getMaterialIds());
		if (result) {
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(req.getMainArticleId());
			HuntLifeArticle a = (HuntLifeArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
			Shouhun4Client shouhunModel = HuntLifeManager.instance.createModel4Client(player, a.getAddAttrType());
			ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(((HuntLifeArticleEntity)ae).getExtraData().getLevel());
			String ic = model.getIcon1() + "," + a.getIconId() + "," + model.getIcon2();
			shouhunModel.setIcons(ic);
			SHOUHUN_TUNSHI_RES resp = new SHOUHUN_TUNSHI_RES(message.getSequnceNum(), shouhunModel);
			return resp;
		}
		return null;
	}
	

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
