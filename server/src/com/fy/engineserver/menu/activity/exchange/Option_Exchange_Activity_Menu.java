package com.fy.engineserver.menu.activity.exchange;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.CompoundReturn;

public class Option_Exchange_Activity_Menu extends Option {
	private String optionName = "";

	private ExchangeActivity ea;

	public Option_Exchange_Activity_Menu(String optionName, ExchangeActivity ea) {
		this.optionName = optionName;
		this.ea = ea;
	}

	@Override
	public void doSelect(Game game, Player player) {
		ActivitySubSystem.logger.warn("optionName:" + optionName + "--" + player.getLogString());
		CompoundReturn cr = hasAllcostArticle(player);
		if (!cr.getBooleanValue()) {
			player.sendError(Translate.translateString(Translate.你没有所需道具, new String[][] { { Translate.STRING_1, cr.getStringValue() } }));
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [兑换失败] [缺少物品:" + cr.toString() + "]");
			return;
		}
		if (prizeExist()) {
			if (ea.getTipYN()) {
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				mw.setDescriptionInUUB(ea.getTip());
				Option_Exchange_Activity_MenuWithTip option_sure = new Option_Exchange_Activity_MenuWithTip(ea);
				option_sure.setText(Translate.确定);
				Option_Cancel option_cancle = new Option_Cancel();
				option_cancle.setText(Translate.取消);
				mw.setOptions(new Option[]{option_sure,option_cancle});
				QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(req);
			} else {
				boolean b = removeAllcost(player);
			//	doPrize(player);
				if (b) {
					doPrize(player);
				} else {
					ActivitySubSystem.logger.warn("[Option_Exchange_Activity_MenuWithTip] [删除物品不成功] [无法获得奖励] [" + player.getLogString() + "]");
				}
			}
		} else {
			StringBuffer sbf = new StringBuffer();
			for (String name : ea.getGainArticleNameArr()) {
				sbf.append(name + " ");
			}
			player.sendError(Translate.translateString(Translate.物品不存在提示, new String[][] { { Translate.STRING_1, sbf.toString() } }));
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [兑换失败] [奖励不存在] [" + sbf + "]");
		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public boolean prizeExist() {
		Article[] prize = new Article[ea.getCostArticleCNNameArr().length];
		for (int i = 0; i < ea.getCostArticleCNNameArr().length; i++) {
			prize[i] = ArticleManager.getInstance().getArticleByCNname(ea.getCostArticleCNNameArr()[i]);
			if (prize[i] == null) return false;
		}
		return true;
	}

	public CompoundReturn hasAllcostArticle(Player player) {
		StringBuffer notice = new StringBuffer();
		boolean enough = true;
		for (int i = 0; i < ea.getCostArticleCNNameArr().length; i++) {
			String articleName = ea.getCostArticleCNNameArr()[i];
			String showArticleName = ea.getCostArticleNameArr()[i];
			int articleColor = ea.getCostArticleColorArr()[i];
			int articleNum = ea.getCostArticleNumArr()[i];
			Article article = ArticleManager.getInstance().getArticleByCNname(articleName);
			if (article == null) {
				enough = false;
				notice.append("[" + ea.getCostArticleNameArr()[i] + " not exist!]");
				continue;
			}
			if(article instanceof InlayArticle){
				showArticleName = ((InlayArticle)article).getShowName();
			}
			int hasNum = player.getArticleNum(showArticleName, articleColor, BindType.BOTH);
			if (hasNum < articleNum) {
				enough = false;
				int colorValue = ArticleManager.getColorValue(article, articleColor);
				notice.append("<f color='" + colorValue + "'>" + showArticleName + "</f>*" + (articleNum - hasNum) + ".");
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(enough).setStringValue(notice.toString());
	}

	public boolean removeAllcost(Player player) {
		ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
		for (int i = 0; i < ea.getCostArticleCNNameArr().length; i++) {
			String articleName = ea.getCostArticleNameArr()[i];
			int articleColor = ea.getCostArticleColorArr()[i];
			int articleNum = ea.getCostArticleNumArr()[i];
			for (int n = 0; n < articleNum; n++) {
				ArticleEntity aee = player.removeArticleByNameColorAndBind(articleName, articleColor, BindType.BOTH, "活动", true);
				if(aee==null){
					String description = Translate.删除物品不成功;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (ActivitySubSystem.logger.isWarnEnabled()) ActivitySubSystem.logger.warn("[兑换物品活动] ["+description+"] ["+articleName+"]");
					return false;
				}
				// 统计
				ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "活动", null);
				ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [扣除物品] [articleName:" + articleName + "] [articleColor:" + articleColor + "]");
				try {
					if (aee != null) {
						strongMaterialEntitys.add(aee);
					}
				} catch (Exception e) {
					ActivitySubSystem.logger.error("[使用赠送活动] [兑换物品活动1] [" + player.getLogString() + "]", e);
				}
			}
		}
		ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
		return true;
	}

	// public void doPrize(Player player) {
	// Article[] prize = new Article[ea.getGainArticleCNNameArr().length];
	// List<ArticleEntity> list = new ArrayList<ArticleEntity>();
	// String str1 = "";// 用于提示玩家的
	// String str2 = "";// 用于邮件里的
	// try {
	// for (int i = 0; i < ea.getGainArticleCNNameArr().length; i++) {
	// prize[i] = ArticleManager.getInstance().getArticleByCNname(ea.getGainArticleCNNameArr()[i]);
	// // ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() +
	// //
	// "] ["+(prize[i]!=null?prize[i]:"prizeno")+"] ["+(ea.getGainArticleBindArr()[i]!=null?ea.getGainArticleBindArr()[i]:"ea.getGainArticleBindArr()[i]no")+"] ["+(ea.getCostArticleColorArr()[i]!=null?ea.getCostArticleColorArr()[i]:"ea.getCostArticleColorArr()[i]no")+"] ["+(ea.getCostArticleNumArr()[i]!=null?ea.getCostArticleNumArr()[i]:"ea.getCostArticleNumArr()[i]no")+"]");
	// if (prize[i] != null && ea.getGainArticleBindArr()[i] != null && ea.getCostArticleColorArr()[i] != null && ea.getCostArticleNumArr()[i] != null) {
	//
	// ArticleEntity aee = ArticleEntityManager.getInstance().createEntity(prize[i], ea.getGainArticleBindArr()[i], ArticleEntityManager.活动, player, ea.getCostArticleColorArr()[i],
	// ea.getCostArticleNumArr()[i], true);
	// if (aee != null) {
	// list.add(aee);
	// }
	// int colorValue = ArticleManager.getColorValue(prize[i], ea.getCostArticleColorArr()[i]);
	// if (i < ea.getGainArticleCNNameArr().length - 1) {
	// str1 += "<f color='" + colorValue + "'>" + ea.getGainArticleCNNameArr()[i] + "*" + ea.getCostArticleNumArr()[i] + " </f>";
	// str2 += ea.getGainArticleCNNameArr()[i] + "*" + ea.getCostArticleNumArr()[i] + ",";
	// } else {
	// str1 += "<f color='" + colorValue + "'>" + ea.getGainArticleCNNameArr()[i] + "*" + ea.getCostArticleNumArr()[i] + "</f>";
	// str2 += ea.getGainArticleCNNameArr()[i] + "*" + ea.getCostArticleNumArr()[i] + ".";
	// }
	// }
	// }
	//
	// if (list != null && list.size() > 0) {
	// MailManager.getInstance().sendMail(player.getId(), list.toArray(new ArticleEntity[] {}), ArrayUtils.toPrimitive(ea.getCostArticleNumArr()), ea.getMailTitle(),
	// ea.getMailContent(), 0L, 0L, 0L, "兑换物品活动-" + getText());
	// player.sendError(Translate.translateString(Translate.获得兑换奖励, new String[][] { { Translate.STRING_1, str1 } }));
	// ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [奖励OK]");
	// }
	// // MailManager.getInstance().sendMail(player.getId(), ae, ArrayUtils.toPrimitive(exchangeArticleNumArr), Translate.恭喜您获得了奖励, Translate.恭喜您获得了奖励 + str2, 0L, 0L, 0L,
	// // "兑换物品活动-" + getText());
	//
	// } catch (Exception e) {
	// ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [奖励异常]", e);
	// }
	//
	// }

	public void doPrize(Player player) {
		Article[] prize = new Article[ea.getGainArticleCNNameArr().length];
		ActivityProp[] activityProps = new ActivityProp[ea.getGainArticleCNNameArr().length];
		List<ArticleEntity> list = new ArrayList<ArticleEntity>();
		String str1 = "";// 用于提示玩家的
		String str2 = "";// 用于邮件里的
		try {
			for (int i = 0; i < ea.getGainArticleCNNameArr().length; i++) {
				prize[i] = ArticleManager.getInstance().getArticleByCNname(ea.getGainArticleCNNameArr()[i]);
				if (prize[i] != null && ea.getGainArticleBindArr()[i] != null && ea.getCostArticleColorArr()[i] != null && ea.getCostArticleNumArr()[i] != null) {

					ArticleEntity aee = ArticleEntityManager.getInstance().createEntity(prize[i], ea.getGainArticleBindArr()[i], ArticleEntityManager.活动, player, ea.getCostArticleColorArr()[i], ea.getCostArticleNumArr()[i], true);
					if (aee != null) {
						list.add(aee);
					}else{
						ActivitySubSystem.logger.warn("[兑换物品活动] [奖励物品不存在] [" + player.getLogString() + "] ["+ea.getGainArticleCNNameArr()[i]+"]");
					}
					// 统计
					ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, ea.getCostArticleNumArr()[i], "活动", null);
					int colorValue = ArticleManager.getColorValue(prize[i], ea.getCostArticleColorArr()[i]);
					if (i < ea.getGainArticleCNNameArr().length - 1) {
						str1 += "<f color='" + colorValue + "'>" + ea.getGainArticleNameArr()[i] + "*" + ea.getGainArticleNumArr()[i] + " </f>";
						str2 += ea.getGainArticleNameArr()[i] + "*" + ea.getGainArticleNumArr()[i] + ",";
					} else {
						str1 += "<f color='" + colorValue + "'>" + ea.getGainArticleNameArr()[i] + "*" + ea.getGainArticleNumArr()[i] + "</f>";
						str2 += ea.getGainArticleNameArr()[i] + "*" + ea.getGainArticleNumArr()[i] + ".";
					}
				}
				activityProps[i] = new ActivityProp(ea.getGainArticleCNNameArr()[i], ea.getGainArticleColorArr()[i], ea.getGainArticleNumArr()[i], ea.getGainArticleBindArr()[i]);

			}

			if (list != null && list.size() > 0) {
				// MailManager.getInstance().sendMail(player.getId(), list.toArray(new ArticleEntity[] {}), ArrayUtils.toPrimitive(ea.getCostArticleNumArr()), ea.getMailTitle(),
				// ea.getMailContent(), 0L, 0L, 0L, "兑换物品活动-" + getText());
				List<Player> players = new ArrayList<Player>();
				players.add(player);
				ActivityManager.sendMailForActivity(players, activityProps, ea.getMailTitle(), ea.getMailContent(), "兑换物品活动");
				player.sendError(Translate.translateString(Translate.获得兑换奖励, new String[][] { { Translate.STRING_1, str1 } }));
				ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [奖励OK]");
			}
			// MailManager.getInstance().sendMail(player.getId(), ae, ArrayUtils.toPrimitive(exchangeArticleNumArr), Translate.恭喜您获得了奖励, Translate.恭喜您获得了奖励 + str2, 0L, 0L, 0L,
			// "兑换物品活动-" + getText());

		} catch (Exception e) {
			ActivitySubSystem.logger.warn("[兑换物品活动] [" + getText() + "] [" + player.getLogString() + "] [奖励异常]", e);
		}

	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public ExchangeActivity getEa() {
		return ea;
	}

	public void setEa(ExchangeActivity ea) {
		this.ea = ea;
	}

	@Override
	public String toString() {
		return "Option_Exchange_Activity_Menu [ea=" + ea + ", optionName=" + optionName + "]";
	}

}
