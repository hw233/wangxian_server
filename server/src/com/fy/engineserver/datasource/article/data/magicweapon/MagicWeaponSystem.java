package com.fy.engineserver.datasource.article.data.magicweapon;

import java.lang.reflect.Method;

import org.slf4j.Logger;

import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponColorUpModule;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.COMFIRM_MAGICWEAPON_NAIJIU_REQ;
import com.fy.engineserver.message.COMFIRM_MAGICWEAPON_NAIJIU_RES;
import com.fy.engineserver.message.MAGICWEAPON_BREAK_REQ;
import com.fy.engineserver.message.MAGICWEAPON_BREAK_RES;
import com.fy.engineserver.message.MAGICWEAPON_EXTRAATTR_INFO_REQ;
import com.fy.engineserver.message.MAGICWEAPON_EXTRAATTR_INFO_RES;
import com.fy.engineserver.message.OPEN_MAGICWEAPON_BREAK_WINDOW_REQ;
import com.fy.engineserver.message.OPEN_MAGICWEAPON_BREAK_WINDOW_RES;
import com.fy.engineserver.message.QUERY_MAGICWEAPON_BREAK_NEEDPROP_REQ;
import com.fy.engineserver.message.QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES;
import com.fy.engineserver.message.QUERY_MAGICWEAPON_NAIJIU_REQ;
import com.fy.engineserver.message.QUERY_MAGICWEAPON_NAIJIU_RES;
import com.fy.engineserver.message.RESET_MAGICWEAPON_EXTRAATTR_REQ;
import com.fy.engineserver.message.RESET_MAGICWEAPON_EXTRAATTR_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class MagicWeaponSystem extends SubSystemAdapter{
	public static Logger logger = MagicWeaponManager.logger;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "MagicWeaponSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		// TODO Auto-generated method stub
		return new String[] {"QUERY_MAGICWEAPON_NAIJIU_REQ", "COMFIRM_MAGICWEAPON_NAIJIU_REQ","OPEN_MAGICWEAPON_BREAK_WINDOW_REQ"
				,"QUERY_MAGICWEAPON_BREAK_NEEDPROP_REQ", "MAGICWEAPON_BREAK_REQ", "MAGICWEAPON_EXTRAATTR_INFO_REQ", "RESET_MAGICWEAPON_EXTRAATTR_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
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
	
	public ResponseMessage handle_OPEN_MAGICWEAPON_BREAK_WINDOW_REQ(Connection conn, RequestMessage message, Player player) {
		OPEN_MAGICWEAPON_BREAK_WINDOW_REQ req = (OPEN_MAGICWEAPON_BREAK_WINDOW_REQ) message;
		OPEN_MAGICWEAPON_BREAK_WINDOW_RES res = new OPEN_MAGICWEAPON_BREAK_WINDOW_RES(req.getSequnceNum(), Translate.法宝突破界面描述);
		return res;
	}
	public ResponseMessage handle_QUERY_MAGICWEAPON_BREAK_NEEDPROP_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_MAGICWEAPON_BREAK_NEEDPROP_REQ req = (QUERY_MAGICWEAPON_BREAK_NEEDPROP_REQ) message;
		QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES res = null;
		try{
			NewMagicWeaponEntity mw = (NewMagicWeaponEntity) ArticleEntityManager.getInstance().getEntity(req.getMagicweaponId());
			if (mw.getColorType() < 3) {
				player.sendError(Translate.紫色以上法宝才能操作);
				return null;
			}
			MagicWeaponColorUpModule module = MagicWeaponManager.instance.colorUpMaps.get(mw.getColorType());
			String aeName = null;
			int prob = 0;
			String jie = MagicWeaponManager.getJieJiMess(mw.getMcolorlevel());
			int maxlv = MagicWeaponManager.instance.getMaxP(mw.getColorType());
			if (mw.getMcolorlevel() >= maxlv) {
				player.sendError(Translate.已经吞噬最大);
				return new QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES(req.getSequnceNum(), req.getMagicweaponId(), jie, "", prob);
			}
			int needLv = 80;
			int maxBreakLv = module.getMagicWeaponLvs()[module.getMagicWeaponLvs().length-1];
			if (mw.getMcolorlevel() > maxBreakLv) {
				player.sendError(Translate.法宝已突破至最高);
				return new QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES(req.getSequnceNum(), req.getMagicweaponId(), jie, "", prob);
			}
			for (int i=0; i<module.getMagicWeaponLvs().length; i++) {
				if (mw.getMcolorlevel() == module.getMagicWeaponLvs()[i]) {
					aeName = module.getArticleCNNNames()[i];
					prob = module.getProbabblys()[i];
					break;
				} else if (mw.getMcolorlevel() < module.getMagicWeaponLvs()[i]) {
					needLv = module.getMagicWeaponLvs()[i];
					break;
				}
			}
			if (aeName == null) {
				res = new QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES(req.getSequnceNum(), req.getMagicweaponId(), jie, "", prob);
				player.sendError(String.format(Translate.满阶的法宝才可以突破, MagicWeaponManager.getJieJiMess(needLv)));
			} else {
				res = new QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES(req.getSequnceNum(), req.getMagicweaponId(), jie, aeName, prob);
			}
		} catch(Exception e) {
			logger.error("[请求法宝阶突破信息] [" + player.getLogString() + "] ", e);
		}
		return res;
	}
	public ResponseMessage handle_MAGICWEAPON_BREAK_REQ(Connection conn, RequestMessage message, Player player) {
		MAGICWEAPON_BREAK_REQ req = (MAGICWEAPON_BREAK_REQ) message;
		MAGICWEAPON_BREAK_RES res = null;
		try{
			NewMagicWeaponEntity mw = (NewMagicWeaponEntity) ArticleEntityManager.getInstance().getEntity(req.getMagicweaponId());
			boolean b = MagicWeaponManager.instance.magicWeaponEatBreak(player, req.getMagicweaponId());
			int result = b ? 1:0;
			String jie = MagicWeaponManager.getJieJiMess(mw.getMcolorlevel());
			res = new MAGICWEAPON_BREAK_RES(req.getSequnceNum(), req.getMagicweaponId(), jie, result);
		} catch(Exception e) {
			logger.error("[法宝阶突破] [" + player.getLogString() + "] ", e);
		}
		return res;
	}
	
	/**
	 * 请求法宝耐久度
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_MAGICWEAPON_NAIJIU_REQ(Connection conn, RequestMessage message, Player player) {
		QUERY_MAGICWEAPON_NAIJIU_REQ req = (QUERY_MAGICWEAPON_NAIJIU_REQ) message;
		QUERY_MAGICWEAPON_NAIJIU_RES res = null;
		try{
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(req.getMagicweaponId());
			if(ae != null && ae instanceof NewMagicWeaponEntity) {
				MagicWeapon ac = (MagicWeapon) ArticleManager.getInstance().getArticle(ae.getArticleName());
				NewMagicWeaponEntity ne = (NewMagicWeaponEntity) ae;
				res = new QUERY_MAGICWEAPON_NAIJIU_RES(message.getSequnceNum(), ne.getMdurability(), ac.getNaijiudu());
			} else {
				player.sendError(Translate.请放入法宝);
			}
		} catch(Exception e) {
			logger.error("[请求补充灵气错误] [" + player.getLogString() + "] ", e);
		}
		return res;
	}
	/**
	 * 确认恢复法宝耐久
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_COMFIRM_MAGICWEAPON_NAIJIU_REQ(Connection conn, RequestMessage message, Player player) {
		COMFIRM_MAGICWEAPON_NAIJIU_REQ req = (COMFIRM_MAGICWEAPON_NAIJIU_REQ) message;
		boolean result = MagicWeaponManager.instance.supplementLingqi(player, req.getMagicweaponId(), req.getMateriaId(), req.getMateriaNum());
		byte rr = 0;
		if(result) {
			rr = 1;
		}
		COMFIRM_MAGICWEAPON_NAIJIU_RES res = new COMFIRM_MAGICWEAPON_NAIJIU_RES(message.getSequnceNum(), rr);
		player.addMessageToRightBag(res);
		return null;
	}
	/**
	 * 获取法宝附加属性描述
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_MAGICWEAPON_EXTRAATTR_INFO_REQ(Connection conn, RequestMessage message, Player player) {
		MAGICWEAPON_EXTRAATTR_INFO_REQ req = (MAGICWEAPON_EXTRAATTR_INFO_REQ) message;
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(req.getArticleId());
			if (ae instanceof NewMagicWeaponEntity) {
				String str = ((NewMagicWeaponEntity)ae).getExtraAttrDes(player);
				MAGICWEAPON_EXTRAATTR_INFO_RES resp = new MAGICWEAPON_EXTRAATTR_INFO_RES(req.getSequnceNum(), MagicWeaponManager.getRefreshCost(((NewMagicWeaponEntity)ae).getIllusionLevel()),str);
				return resp;
			}
		} catch (Exception e) {
			MagicWeaponManager.logger.warn("[获取法宝附加属性描述] [异常] [" + player.getLogString() + "] [aeId:" + req.getArticleId() + "]", e);
		}
		return null;
	}
	
	/**
	 * 刷新法宝附加属性
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_RESET_MAGICWEAPON_EXTRAATTR_REQ(Connection conn, RequestMessage message, Player player) {
		RESET_MAGICWEAPON_EXTRAATTR_REQ req = (RESET_MAGICWEAPON_EXTRAATTR_REQ) message;
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(req.getArticleId());
			if (ae instanceof NewMagicWeaponEntity) {
				String str = MagicWeaponManager.instance.refreshExtraAttr(player, req.getArticleId(), false);
				String str2 = ((NewMagicWeaponEntity)ae).getExtraAttrDes(player);
				if (str == null) {
					RESET_MAGICWEAPON_EXTRAATTR_RES resp = new RESET_MAGICWEAPON_EXTRAATTR_RES(req.getSequnceNum(), MagicWeaponManager.getRefreshCost(((NewMagicWeaponEntity)ae).getIllusionLevel()), str2);
					return resp;
				}
				if (!str.isEmpty()) {
					player.sendError(str);
				}
			}
		} catch (Exception e) {
			MagicWeaponManager.logger.warn("[刷新法宝附加属性] [异常] [" + player.getLogString() + "] [aeId:" + req.getArticleId() + "]", e);
		}
		return null;
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
