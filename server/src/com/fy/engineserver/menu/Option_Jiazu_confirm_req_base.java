package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.JIAZU_BASE_REQ;
import com.fy.engineserver.message.JIAZU_BASE_RES;
import com.fy.engineserver.sept.exception.NameExistException;
import com.fy.engineserver.sept.exception.SeptNotExistException;
import com.fy.engineserver.sept.exception.StationExistException;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.xuanzhi.tools.transport.Message;

public class Option_Jiazu_confirm_req_base extends Option {
	JIAZU_BASE_REQ req;

	public Option_Jiazu_confirm_req_base(JIAZU_BASE_REQ req) {
		this.req = req;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		// oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_CONFIRM_APPLY_JIAZU_BASE;
	}

	public void setOId(int oid) {
	}

	@Override
	public void doSelect(Game game, Player player) {

		try {
			JiazuManager jiazuManager = JiazuManager.getInstance();
			Jiazu jiazu = jiazuManager.getJiazu(player.getJiazuName());
			boolean baseNameExist = false;
			if (jiazu == null) {
				HINT_REQ nreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_6199);
				player.addMessageToRightBag(nreq);
				player.setJiazuName(null);
				return;
			}
			Message errEes = null;
			String pwd = req.getJiazuPassword();
			String pwdNotice = req.getJiazuPasswordNotice();
			if (req.getBaseName() == null || req.getBaseName().length() == 0) {
				errEes = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6141);
			} else if (baseNameExist) {
				errEes = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_6145);
			} else {
				try {
					ArticleEntity ae = player.getArticleEntity(Jiazu.REQUEST_BASE_ARTICLE);
					boolean hasArticle = ae != null;
					long needSilver = Jiazu.REQUEST_BASE_MONEY;
					if (player.bindSilverEnough(needSilver) && hasArticle) {
						BillingCenter.getInstance().playerExpense(player, needSilver, CurrencyType.BIND_YINZI, ExpenseReasonType.JIAZU_BASE_CREATE, "");
						player.removeArticleEntityFromKnapsackByArticleId(ae.getId(), "家族删除", true);
						ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "申请家族驻地", "");
					} else {
						if (!hasArticle) {
							player.sendError(Translate.translateString(Translate.text_jiazu_011, new String[][] { { Translate.STRING_1, Jiazu.REQUEST_BASE_ARTICLE } }));
						} else {
							player.sendError(Translate.translateString(Translate.text_jiazu_010, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(Jiazu.REQUEST_BASE_MONEY) } }));
						}
						return;
					}
					// TODO 测试数据
					long NPCId = 240001L;
					SeptStation station = SeptStationManager.getInstance().createDefaultSeptStation(jiazu.getJiazuID(), "", req.getBaseName(), player.getCountry(), NPCId);
					station.setCountry(player.getCountry());
					station.setMapName(SeptStationManager.jiazuMapName + "_jz_" + jiazu.getJiazuID());
					station.setJiazu(jiazu);
					station.initInfo();
					jiazu.setJiazuPasswordHint(pwdNotice);
					jiazu.setJiazuPassword(pwd);
					jiazu.setLevel(1);
					JIAZU_BASE_RES res = new JIAZU_BASE_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_jiazu_020, station.getId());
					player.addMessageToRightBag(res);
					if (JiazuSubSystem.logger.isWarnEnabled()) {
						JiazuSubSystem.logger.warn(player.getLogString() + "[申请家族驻地] [成功] [家族:{}] [map:{}] [密码:{}] [密码提示:{}] [{}]", new Object[] { jiazu.getJiazuID(),station.getMapName(), pwd, req.getJiazuPasswordNotice() ,player.getLogString()});
					}
					return;
				} catch (NameExistException e) {
					errEes = new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.text_6313);
				} catch (StationExistException e) {
					errEes = new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.text_6315);
				} catch (SeptNotExistException e) {
					errEes = new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.text_6199);
				}
			}
			if (JiazuSubSystem.logger.isWarnEnabled()) {
				JiazuSubSystem.logger.warn(player.getLogString() + "[申请家族驻地] [异常] [家族:{}] [错误] [{}]", new Object[] { jiazu.getJiazuID(), errEes });
			}
			if (errEes != null) {
				player.addMessageToRightBag(errEes);
			}
		} catch (Exception e) {
			SeptStationManager.logger.error(player.getLogString() + "error ： （提示框出错了", e);
		}
	}
}
