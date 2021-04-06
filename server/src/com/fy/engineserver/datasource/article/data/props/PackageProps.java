package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;

/**
 * 特殊道具：包裹
 * 包裹中有一些物品，打开包裹可以得到物品
 * 
 */
public class PackageProps extends Props implements Gem {

	/**
	 * 包裹中的物品名字数组
	 */
	private ArticleProperty[] articleNames;
	
	private ArticleProperty[] articleNames_stat;

	/**
	 * 绑定标记，打开宝箱后宝箱中的物品是否绑定 0为绑定，1为不管
	 */
	private byte openBindType;

	private boolean needSendNotice = false;

	public boolean isNeedSendNotice() {
		return needSendNotice;
	}

	public void setNeedSendNotice(boolean needSendNotice) {
		this.needSendNotice = needSendNotice;
	}

	public ArticleProperty[] getArticleNames() {
		return articleNames;
	}

	public void setArticleNames(ArticleProperty[] articleNames) {
		this.articleNames = articleNames;
	}

	public ArticleProperty[] getArticleNames_stat() {
		return articleNames_stat;
	}

	public void setArticleNames_stat(ArticleProperty[] articleNames_stat) {
		this.articleNames_stat = articleNames_stat;
	}

	public byte getOpenBindType() {
		return openBindType;
	}

	public void setOpenBindType(byte openBindType) {
		this.openBindType = openBindType;
	}

	/**
	 * 打开包裹方法(重写父类方法)
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity aee) {
		if (!super.use(game, player, aee)) {
			return false;
		}
		// 往玩家背包中放物品
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		boolean bind = true;
		if (this.openBindType == 1) {
			bind = false;
		}
		int reason = ArticleEntityManager.CREATE_REASON_USE_PACKAGEPROPS;
		StringBuffer notice = new StringBuffer();

		if (am != null && aem != null && articleNames != null) {
			for (int i = 0; i < articleNames.length; i++) {
				ArticleProperty s = articleNames[i];
				if (s != null) {
					Article a = am.getArticle(s.articleName);
					if (a != null) {
						ArticleEntity ae = null;
						int color = s.color;
						if (a.isOverlap()) {
							try {
								ae = aem.createEntity(a, bind, reason, player, color, s.count, true);
								for (int j = 0; j < s.count; j++) {
									player.putToKnapsacks(ae, "包裹");
									if (ArticleManager.logger.isInfoEnabled()) ArticleManager.logger.info("[{}] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName(), ae.getId() });
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else {
							for (int j = 0; j < s.count; j++) {
								try {
									ae = aem.createEntity(a, bind, reason, player, color, 1, true);
									player.putToKnapsacks(ae, "包裹");
									if (ArticleManager.logger.isInfoEnabled()) ArticleManager.logger.info("[{}] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), ae.getArticleName(), ae.getId() });
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}

						if (ae != null) {
							int colorValue = ArticleManager.getColorValue(a, Integer.valueOf(ae.getColorType()));
							notice.append("<f color='" + colorValue + "'>");
							if(a instanceof InlayArticle){
								notice.append(((InlayArticle)a).getShowName()).append("*").append(s.count);
							}else{
								notice.append(ae.getArticleName()).append("*").append(s.count);
							}
							notice.append("</f>,");
						}

						// 统计
						ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, s.count, "礼包获得", null);
						if(a instanceof InlayArticle){
							if (ae != null) {
								player.send_HINT_REQ(Translate.translateString(Translate.恭喜您获得了, new String[][] { { Translate.COUNT_1, s.count + "" }, { Translate.STRING_1, ((InlayArticle)a).getShowName() } }));
							}
						}else{
							if (ae != null) {
								player.send_HINT_REQ(Translate.translateString(Translate.恭喜您获得了, new String[][] { { Translate.COUNT_1, s.count + "" }, { Translate.STRING_1, ae.getArticleName() } }));
							}
						}
						
						
					}
				}
			}
			if (needSendNotice) {
				// System.out.println("需要广播:" + aee.getArticleName() + ">>>" + notice.toString());
				try {
					ChatMessageService cm = ChatMessageService.getInstance();
					ChatMessage msg = new ChatMessage();
					msg.setSort(ChatChannelType.SYSTEM);
					String des = Translate.translateString(Translate.恭喜玩家幸运开出物品, new String[][] { { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_1, this.name }, { Translate.STRING_2, notice.toString() } });
					msg.setMessageText(des);
					cm.sendMessageToWorld(msg);
					// System.out.println("发送广播:" + aee.getArticleName() + ">>>" + des);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 判断某个玩家是否可以使用此物品 子类可以重载此方法
	 * 返回null表示可以使用
	 * 返回字符串表示不能使用
	 * 字符串为不能使用的详细信息
	 * @param p
	 * @return
	 */
	public String canUse(Player p) {

		String resultStr = super.canUse(p);
		if (resultStr == null) {
			// TODO 判断背包剩余格子数是否满足要求
			if (articleNames != null) {
				List<ArticleEntity> aeList = new ArrayList<ArticleEntity>();
				ArticleManager am = ArticleManager.getInstance();
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				int reason = ArticleEntityManager.CREATE_REASON_TEMP_ARTICLE;
				boolean bind = true;
				if (this.openBindType == 1) {
					bind = false;
				}
				for (int i = 0; i < articleNames.length; i++) {
					ArticleProperty s = articleNames[i];
					if (s != null) {
						Article a = am.getArticle(s.articleName);
						if (a != null) {
							ArticleEntity ae = null;
							int color = s.color;
							for (int j = 0; j < s.count; j++) {
								try {
									ae = aem.createTempEntity(a, bind, reason, p, color);
									aeList.add(ae);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
					}
				}
				if (!p.putAllOK(aeList.toArray(new ArticleEntity[0]))) {
					resultStr = Translate.背包空间不足;
				}
			}
		}
		return resultStr;
	}

	public String getComment() {
		StringBuffer sb = new StringBuffer();
		// sb.append(name+"中包括:\n");
		// if(money != 0){
		// sb.append("游戏币,\n");
		// }
		// if(bindyuanbao != 0){
		// sb.append("绑定元宝,\n");
		// }
		// if(rmbyuanbao != 0){
		// sb.append("人民币元宝,\n");
		// }
		// if(articleNames != null){
		// for(ArticleProperty str : articleNames){
		// sb.append(str.value+"个"+str.articleName+",\n");
		// }
		// }
		// if(sb.indexOf(",") > 0){
		// return sb.substring(0, sb.lastIndexOf(","));
		// }
		return sb.toString();
	}
}
