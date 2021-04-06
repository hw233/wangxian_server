package com.fy.engineserver.soulpith;

import java.util.Arrays;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.SoulPithArticleEntity;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticle;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticleLevelData;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.ARTIFICE_SOULPITH_REQ;
import com.fy.engineserver.message.ARTIFICE_SOULPITH_RES;
import com.fy.engineserver.message.DEVOUR_SOULPITH_REQ;
import com.fy.engineserver.message.DEVOUR_SOULPITH_RES;
import com.fy.engineserver.message.GOURD_INFO_REQ;
import com.fy.engineserver.message.GOURD_INFO_RES;
import com.fy.engineserver.message.INLAY_SOULPITH_REQ;
import com.fy.engineserver.message.INLAY_SOULPITH_RES;
import com.fy.engineserver.message.POURIN_SOULPITH_REQ;
import com.fy.engineserver.message.POURIN_SOULPITH_RES;
import com.fy.engineserver.message.SOULPITH_EXTRA_INFO_REQ;
import com.fy.engineserver.message.SOULPITH_EXTRA_INFO_RES;
import com.fy.engineserver.message.SOULPITH_INFO_REQ;
import com.fy.engineserver.message.SOULPITH_INFO_RES;
import com.fy.engineserver.message.SOULPITH_LEVEL_INFO_REQ;
import com.fy.engineserver.message.SOULPITH_LEVEL_INFO_RES;
import com.fy.engineserver.message.SOUL_ARTICLE_INFO_REQ;
import com.fy.engineserver.message.SOUL_ARTICLE_INFO_RES;
import com.fy.engineserver.message.SOUL_LEVELUP_INFO_REQ;
import com.fy.engineserver.message.SOUL_LEVELUP_INFO_RES;
import com.fy.engineserver.message.TAKEOUT_SOULPITH_REQ;
import com.fy.engineserver.message.TAKEOUT_SOULPITH_RES;
import com.fy.engineserver.soulpith.module.SoulInfo4Client;
import com.fy.engineserver.soulpith.module.SoulLevelupExpModule;
import com.fy.engineserver.soulpith.module.SoulPithAritlcePropertyModule;
import com.fy.engineserver.soulpith.module.SoulPithExtraAttrModule;
import com.fy.engineserver.soulpith.module.SoulpithInfo4Client;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class SoulPithSubSystem extends SubSystemAdapter{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SoulPithSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[]{"SOULPITH_INFO_REQ", "INLAY_SOULPITH_REQ", "TAKEOUT_SOULPITH_REQ", "POURIN_SOULPITH_REQ", "DEVOUR_SOULPITH_REQ"
				, "ARTIFICE_SOULPITH_REQ", "SOULPITH_EXTRA_INFO_REQ", "GOURD_INFO_REQ", "SOUL_ARTICLE_INFO_REQ", "SOULPITH_LEVEL_INFO_REQ"
				, "SOUL_LEVELUP_INFO_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		Player player = check(conn, message, Horse2Manager.logger);
//		if(SoulPithManager.logger.isDebugEnabled()) {
//			SoulPithManager.logger.debug("[SoulPithSubSystem] [收到玩家请求] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]");
//		}
		try {
			if (player.getLevel() < SoulPithConstant.openLevel) {
				if (SoulPithManager.logger.isDebugEnabled()) {
					SoulPithManager.logger.debug("[等级不足] [" + player.getLogString() + "]");
				}
				return null;
			}
			if (message instanceof SOULPITH_INFO_REQ) {
				return handle_SOULPITH_INFO_REQ(conn, message, player);
			} else if (message instanceof INLAY_SOULPITH_REQ) {
				return handle_INLAY_SOULPITH_REQ(conn, message, player);
			} else if (message instanceof TAKEOUT_SOULPITH_REQ) {
				return handle_TAKEOUT_SOULPITH_REQ(conn, message, player);
			} else if (message instanceof POURIN_SOULPITH_REQ) {
				return handle_POURIN_SOULPITH_REQ(conn, message, player);
			} else if (message instanceof DEVOUR_SOULPITH_REQ) {
				return handle_DEVOUR_SOULPITH_REQ(conn, message, player);
			} else if (message instanceof ARTIFICE_SOULPITH_REQ) {
				return handle_ARTIFICE_SOULPITH_REQ(conn, message, player);
			} else if (message instanceof SOULPITH_EXTRA_INFO_REQ) {
				return handle_SOULPITH_EXTRA_INFO_REQ(conn, message, player);
			} else if (message instanceof GOURD_INFO_REQ) {
				return handle_GOURD_INFO_REQ(conn, message, player);
			} else if (message instanceof SOUL_ARTICLE_INFO_REQ) {
				return handle_SOUL_ARTICLE_INFO_REQ(conn, message, player);
			} else if (message instanceof SOULPITH_LEVEL_INFO_REQ) {
				return handle_SOULPITH_LEVEL_INFO_REQ(conn, message, player);
			} else if (message instanceof SOUL_LEVELUP_INFO_REQ) {
				return handle_SOUL_LEVELUP_INFO_REQ(conn, message, player);
			}
		} catch (Exception e) {
			SoulPithManager.logger.error("[灵根] [处理协议出现异常] [" + player.getLogString() + "] [" + message.getTypeDescription() + "]", e);
		}
		return null;
	}
	/**
	 * 灵髓宝石升级经验
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SOUL_LEVELUP_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		SOUL_LEVELUP_INFO_REQ req = (SOUL_LEVELUP_INFO_REQ) message;
		long[] needExp = new long[SoulPithManager.getInst().soulLevelModules.size()];
		for (int i=0; i<needExp.length; i++) {
			int lv = i + 1;
			needExp[i] = SoulPithManager.getInst().soulLevelModules.get(lv).getNeedExp();
		}
		int[] aa = new int[SoulPithConstant.COLOR_RATE.length];
		for (int i=0; i<aa.length; i++) {
			aa[i] = (int) (SoulPithConstant.COLOR_RATE[i] * 100);
		}
		SOUL_LEVELUP_INFO_RES resp = new SOUL_LEVELUP_INFO_RES(req.getSequnceNum(), SoulPithConstant.DEVOUR_COST, SoulPithConstant.ARTIFICE_COST, needExp, aa);
		return resp;
	}
	/**
	 * 灵髓宝石等级属性
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SOULPITH_LEVEL_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		SOULPITH_LEVEL_INFO_REQ req = (SOULPITH_LEVEL_INFO_REQ) message;
		SoulInfo4Client[] datas = null;
		SoulPithAritlcePropertyModule module = SoulPithManager.getInst().soulArticleDatas.get(req.getSoulpithType());
		datas = new SoulInfo4Client[module.getLevelData().size()];
		for (int i=0; i<datas.length; i++) {
			int lv = i+1;
			SoulPithArticleLevelData data = module.getLevelData().get(lv);
			datas[i] = new SoulInfo4Client();
			datas[i].setSoulLevel(lv);
			datas[i].setPropTypes(data.getProperTypes());
			datas[i].setPropNums(data.getProperNums());
			datas[i].setSoulNums(data.getSoulNums());
		}
		SOULPITH_LEVEL_INFO_RES resp = new SOULPITH_LEVEL_INFO_RES(req.getSequnceNum(), req.getSoulpithType(), datas);
		return resp;
	}
	/**
	 * 灵髓宝石物品信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SOUL_ARTICLE_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		SOUL_ARTICLE_INFO_REQ req = (SOUL_ARTICLE_INFO_REQ) message;
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(req.getArticleId());
		if (ae instanceof SoulPithArticleEntity) {
			SoulPithArticleEntity s = (SoulPithArticleEntity) ae;
			SoulPithArticle a = (SoulPithArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
			SoulPithArticleLevelData data = a.getLevelDatas().get(s.getLevel());
			int[] propTypes = Arrays.copyOf(data.getProperTypes(), data.getProperTypes().length);
//			int[] propNums = Arrays.copyOf(data.getProperNums(), data.getProperNums().length);
			int[] propNums = new int[data.getProperNums().length];
			for (int i=0; i<propNums.length; i++) {
				propNums[i] = (int) (data.getProperNums()[i] * SoulPithConstant.COLOR_RATE[ae.getColorType()]);	
			}
			if (s.getLevel() <SoulPithConstant.SOUL_MAX_LEVEL && s.getExp() > 0) {
				SoulLevelupExpModule sem = SoulPithManager.getInst().soulLevelModules.get(s.getLevel());
				SoulPithArticleLevelData data2 = a.getLevelDatas().get(s.getLevel()+1);
				float rate = (float)s.getExp() / (float)sem.getNeedExp();
				for (int j=0; j<data2.getProperTypes().length; j++) {
					int num = (int) (data.getProperNums()[j] * SoulPithConstant.COLOR_RATE[ae.getColorType()]);			
					int num2 = (int) (data2.getProperNums()[j] * SoulPithConstant.COLOR_RATE[ae.getColorType()]);
					int addNum = (int)((num2 - num) * rate);
					propNums[j] += addNum;
				}
			}
			
			SOUL_ARTICLE_INFO_RES resp = new SOUL_ARTICLE_INFO_RES(req.getSequnceNum(), req.getArticleId(), s.getLevel(), s.getExp(), propTypes, propNums);
			return resp;
		} 
		if (SoulPithManager.logger.isDebugEnabled()) {
			SoulPithManager.logger.debug("[handle_SOUL_ARTICLE_INFO_REQ] [客户端发过来的id错误] [" + player.getLogString() + "] [aeid:" + req.getArticleId() + "]");
		}
		return null;
	}
	public ResponseMessage handle_GOURD_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		int[] tempLvs = new int[SoulPithConstant.MAX_FILL_NUM];
		for (int i=0; i<tempLvs.length; i++) {
			for (int j=0; j<SoulPithConstant.openSoulNums.length; j++) {
				if (i < SoulPithConstant.openSoulNums[j]) {
					tempLvs[i] = SoulPithConstant.playerLevels[j];
					break;
				}
			}
		}
		GOURD_INFO_RES resp = new GOURD_INFO_RES(message.getSequnceNum(), SoulPithManager.gourdIds, SoulPithManager.addExps, tempLvs);
		return resp;
	}
	/**
	 * 获取灵根信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SOULPITH_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		SOULPITH_INFO_REQ req = (SOULPITH_INFO_REQ) message;
		int soulType = req.getSoultype();
		SoulpithInfo4Client datas = new SoulpithInfo4Client(player, soulType);
		SOULPITH_INFO_RES resp = new SOULPITH_INFO_RES(req.getSequnceNum(), soulType, datas);
		return resp;
	}
	/**
	 * 镶嵌灵髓宝石
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_INLAY_SOULPITH_REQ(Connection conn, RequestMessage message, Player player) {
		INLAY_SOULPITH_REQ req = (INLAY_SOULPITH_REQ) message;
		SoulPithEntityManager.getInst().inlay(player, req.getSoultype(), req.getArticleId(), req.getInlayIndex());
		SoulpithInfo4Client datas = new SoulpithInfo4Client(player, req.getSoultype());
		INLAY_SOULPITH_RES resp = new INLAY_SOULPITH_RES(req.getSequnceNum(), req.getSoultype(), datas);
		return resp;
	}
	/**
	 * 取出灵髓宝石
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TAKEOUT_SOULPITH_REQ(Connection conn, RequestMessage message, Player player) {
		TAKEOUT_SOULPITH_REQ req = (TAKEOUT_SOULPITH_REQ) message;
		SoulPithEntityManager.getInst().takeOut(player, req.getSoultype(), req.getInlayIndex());
		SoulpithInfo4Client datas = new SoulpithInfo4Client(player, req.getSoultype());
		TAKEOUT_SOULPITH_RES resp = new TAKEOUT_SOULPITH_RES(req.getSequnceNum(), req.getSoultype(), datas);
		return resp;
	}
	/**
	 * 注灵
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_POURIN_SOULPITH_REQ(Connection conn, RequestMessage message, Player player) {
		POURIN_SOULPITH_REQ req = (POURIN_SOULPITH_REQ) message;
		boolean b = SoulPithEntityManager.getInst().pourIn(player, req.getArticleId(), req.getMeterialIds(), req.getMeterialNums());
		int result = -1;
		if (b) {
			result = 1;
		}
		POURIN_SOULPITH_RES resp = new POURIN_SOULPITH_RES(req.getSequnceNum(), result);
		return resp;
	}
	/**
	 * 吞噬
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_DEVOUR_SOULPITH_REQ(Connection conn, RequestMessage message, Player player) {
		DEVOUR_SOULPITH_REQ req = (DEVOUR_SOULPITH_REQ) message;
		boolean b = SoulPithEntityManager.getInst().devour(player, req.getMainId(), req.getMeterials(), false);
		if (b) {
			DEVOUR_SOULPITH_RES resp = new DEVOUR_SOULPITH_RES(req.getSequnceNum(), 1);
			return resp;
		}
		return null;
	}
	/**
	 * 炼化
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_ARTIFICE_SOULPITH_REQ(Connection conn, RequestMessage message, Player player) {
		ARTIFICE_SOULPITH_REQ req = (ARTIFICE_SOULPITH_REQ) message;
		long id = SoulPithEntityManager.getInst().artifice(player, req.getMeterialIds(), false);
		if (id > 0) {
			ARTIFICE_SOULPITH_RES resp = new ARTIFICE_SOULPITH_RES(req.getSequnceNum(), id);
			return resp;
		}
		return null;
	}
	/**
	 * 灵髓点数激活属性
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SOULPITH_EXTRA_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		SoulPithExtraAttrModule[] datas = SoulPithManager.getInst().extraAttrs.toArray(new SoulPithExtraAttrModule[0]);
		SOULPITH_EXTRA_INFO_RES resp = new SOULPITH_EXTRA_INFO_RES(message.getSequnceNum(),datas);
		return resp;
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
