package com.fy.engineserver.sprite.npc;

import java.util.Hashtable;
import java.util.Map;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 采集NPC-养蚕活动NPC-爆在地上的虫子
 * 
 * 
 */
@SuppressWarnings("serial")
public class WormCollectionNPC extends NPC implements Collectionable {

	private long jiazuId;
	private int totalNum;
	private int leftNum;
	private String articleNum;
	private int articleColor;
	private Game game;
	private String articleName;
	private boolean canPickup = true;

	private boolean inService;

	private long collectionBarTime;

	private Hashtable<Long, Long> reapers = new Hashtable<Long, Long>();

	public WormCollectionNPC() {

	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getLeftNum() {
		return leftNum;
	}

	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}

	public String getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(String articleNum) {
		this.articleNum = articleNum;
	}

	public int getArticleColor() {
		return articleColor;
	}

	public void setArticleColor(int articleColor) {
		this.articleColor = articleColor;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public boolean isCanPickup() {
		return canPickup;
	}

	public void setCanPickup(boolean canPickup) {
		this.canPickup = canPickup;
	}

	public long getJiazuId() {
		return jiazuId;
	}

	public void setJiazuId(long jiazuId) {
		this.jiazuId = jiazuId;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	@Override
	public boolean canCollection(Player player) {
		return jiazuId == player.getJiazuId();
	}

	@Override
	public int getOncePickupNum() {
		return 1;
	}

	@Override
	public int getOncePickupColor() {
		return articleColor;
	}

	@Override
	public Map<Long, Long> getReapers() {
		return reapers;
	}

	@Override
	public void collection(int num) {
		leftNum -= num;
		leftNum = leftNum < 0 ? 0 : leftNum;
	}

	@Override
	public String getArticleName() {
		return articleName;
	}

	@Override
	public Game getBelongs() {
		return game;
	}

	@Override
	public synchronized void onBeCollection(Player player) {
		if (!isInService()) {
			player.sendError(Translate.text_jiazu_100);
			return;
		}
		if (this.canCollection(player)) {
			int num = this.getOncePickupNum();
			int color = this.getOncePickupColor();
			String articleName = this.getArticleName();
			this.collection(num);
			Article article = ArticleManager.getInstance().getArticle(articleName);
			if (article != null) {
				for (int i = 0; i < num; i++) {
					try {
						ArticleEntity articleEntity = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_COLLECTION, player, color, 1, true);
						if (!player.canAddArticle(articleEntity)) {
							player.sendError(Translate.text_forluck_018);
							return;
						}
						player.putToKnapsacks(articleEntity, "养蚕");
						player.noticeGetArticle(articleEntity);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				// 此处应该不触发任务
			}
		}
		// 采集没了 从地图上移除
		if (this.getLeftNum() == 0) {
			this.getBelongs().removeSprite(this);
		}
		setInService(false);
	}

	@Override
	public long getReadTimebarTime() {
		return 5 * 1000;
	}

	@Override
	public boolean isInService() {
		return inService;
	}

	public void setInService(boolean inService) {
		this.inService = inService;
	}

	@Override
	public long getCollectionBarTime() {
		return collectionBarTime;
	}

	@Override
	public void setCollectionBarTime(long collectionBarTime) {
		this.collectionBarTime = collectionBarTime;
	}

	@Override
	public byte getNpcType() {
		return NPC_TYPE_COLLECTION;
	}

	@Override
	public Object clone() {
		WormCollectionNPC p = new WormCollectionNPC();
		p.cloneAllInitNumericalProperty(this);
//		p.collectionBarTime = FeedSilkwormManager.getInstance().getCollectionBarTime();
		p.country = this.country;
		p.setnPCCategoryId(this.getnPCCategoryId());
		return p;
	}
}
