package com.fy.engineserver.activity.luckfruit;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.ForLuckFruitNPC;
import com.fy.engineserver.stat.ArticleStatManager;

public class ForLuckFruitTimeBar implements Callbackable {

	private Player player;
	private ForLuckFruitNPC npc;

	public ForLuckFruitTimeBar(Player player, ForLuckFruitNPC npc) {
		this.player = player;
		this.npc = npc;
	}

	@Override
	public void callback() {
		try {
			if (ForLuckFruitManager.logger.isInfoEnabled()) ForLuckFruitManager.logger.info("[摘取祝福果读条结束][{}][NPC:{}][剩余个数:{}]", new Object[] { player.getLogString(), npc.getName(), npc.getLeftNum() });
			if (npc != null && player.getCurrentGame().contains(npc) && npc.isInService() && npc.canCollection(player) && npc.getLeftNum() > 0) {
				synchronized (npc) {
					if (npc != null && player.getCurrentGame().contains(npc) && npc.isInService() && npc.canCollection(player) && npc.getLeftNum() > 0) {
						int num = npc.getOncePickupNum();
						int color = npc.getOncePickupColor();
						if (ForLuckFruitManager.logger.isInfoEnabled()) ForLuckFruitManager.logger.info(player.getLogString() + "[一次摘取{}个][颜色{}]", new Object[] { num, color });
						npc.getReapers().put(player.getId(), SystemTime.currentTimeMillis());

						// 给人加上物品
						Article article = ArticleManager.getInstance().getArticle(npc.getArticleName());
						if (article != null) {
							for (int i = 0; i < num; i++) {
								try {
									ArticleEntity articleEntity = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_COLLECTION, player, color, 1, true);
									if (!player.canAddArticle(articleEntity)) {
										player.sendError(Translate.text_forluck_018);
										return;
									}
									if (articleEntity != null) {
										player.putToKnapsacks(articleEntity, "摘取祝福果");
										player.noticeGetArticle(articleEntity);
										ArticleStatManager.addToArticleStat(player, null, articleEntity, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "家族摘取祝福果", "");
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						} else {
							ForLuckFruitManager.logger.error("[物品不存在:{}]", new Object[] { npc.getArticleName() });
						}
						npc.collection(num);
						if (ForLuckFruitManager.logger.isInfoEnabled()) ForLuckFruitManager.logger.info(player.getLogString() + "[摘取了祝福果][数量:{}][颜色:{}][剩余:{}]", new Object[] { num, color, npc.getLeftNum() });
					}
				}
			} else {
				if (ForLuckFruitManager.logger.isInfoEnabled()) ForLuckFruitManager.logger.info("[摘取祝福果读条结束][不符合摘取条件了][player.getCurrentGame().contains(npc){}][npc.isInService(){}][npc.canCollection(player){}][npc.getLeftNum() > 0{}]", new Object[] { player.getCurrentGame().contains(npc), npc.isInService(), npc.canCollection(player), npc.getLeftNum() > 0 });
			}
		} catch (Exception e) {
			ForLuckFruitManager.logger.error("[摘取祝福果读条结束异常]", e);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ForLuckFruitNPC getNpc() {
		return npc;
	}

	public void setNpc(ForLuckFruitNPC npc) {
		this.npc = npc;
	}
}
