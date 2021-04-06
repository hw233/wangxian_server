package com.fy.engineserver.datasource.article.data.props;

import java.util.List;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 时间包
 * 有时间限制的包裹，当时间到了以后只能从包裹中取出物品，不能够放入物品
 * 
 */
@SimpleEmbeddable
public class Knapsack_TimeLimit extends Knapsack {

	/**
	 * 此包裹对应的物品id
	 */
	private long articleId;

	public Knapsack_TimeLimit() {

	}

	public Knapsack_TimeLimit(Player owner) {
		this.owner = owner;
	}

	public Knapsack_TimeLimit(Player owner, int numCell, long articleId) {
		this.owner = owner;
		this.articleId = articleId;
		this.cells = new Cell[numCell];
		this.cellChangeFlags = new byte[numCell];
		for (int i = 0; i < numCell; i++) {
			this.cells[i] = new Cell();
		}
	}

	/**
	 * 找一个空格子放置物品
	 * @param index
	 * @param ae
	 */
	@Override
	public boolean put(ArticleEntity ae, String reason) {
		if (!isValid()) {
			if (logger.isInfoEnabled()) logger.info("[put] [{}] [fail] [背包到期] [owner:{}] [{}] [{}] [单元格：--] [id:{}] [{}] [{}] [-] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), ae.getId(), ae.getArticleName(), ae.getColorType(), reason});
			return false;
		}
		return super.put(ae, reason);
	}

	/**
	 * 在某个单元格上放置某个物品，如果原单元格上有物品，
	 * 分几种情况：
	 * 可叠加的情况，返回null
	 * 空格子，返回null
	 * 原格子中有其他物品，且只有1件，返回原来的物品
	 * 原格子中有其他物品，多余1件，返回要放置的物品
	 * 
	 * 返回的物品标识没有放入到背包中
	 * 
	 * @param index
	 * @param ae
	 */
	@Override
	public ArticleEntity put(int index, ArticleEntity ae, String reason) {
		if (!isValid()) {
			if (logger.isInfoEnabled()) logger.info("[put] [{}] [fail] [背包到期] [owner:{}] [{}] [{}] [单元格：--] [id:{}] [{}] [{}] [-] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), ae.getId(), ae.getArticleName(), ae.getColorType(), reason});
			return ae;
		}

		return super.put(index, ae, reason);
	}

	/**
	 * 测试找一个空格子放置物品
	 * @param index
	 * @param ae
	 */
	private boolean putTest(ArticleEntity ae) {
		if (!isValid()) {
			if (logger.isInfoEnabled()) logger.info("[putTest] [{}] [fail] [背包到期] [owner:{}] [{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName() });
			return false;
		}
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(ae.getArticleName());
		if (a.isOverlap()) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null && cells[i].entityId == ae.getId() && cells[i].count < a.getOverLapNum()) {
					if (cells[i].count >= 0) cells[i].count++;
					else cells[i].count = 1;
					return true;
				}
			}
		}
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null) {
				Cell c = new Cell();
				c.entityId = ae.getId();
				c.count = 1;
				cells[i] = c;
				return true;
			} else if (cells[i].entityId == -1 || cells[i].count <= 0) {
				cells[i].entityId = ae.getId();
				cells[i].count = 1;
				return true;
			}
		}
		return false;
	}

	/**
	 * 测试是否可以全部放下列表中的物品
	 * @param elist
	 * @return
	 */
	public boolean putAllOK(List<ArticleEntity> elist) {
		if (!isValid()) {
			if (logger.isInfoEnabled()) logger.info("[putAllOK] [{}] [fail] [背包到期] [owner:{}] [{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName() });
			return false;
		}
		Knapsack_TimeLimit testknap = new Knapsack_TimeLimit(null);
		Cell testcells[] = new Cell[cells.length];
		for (int i = 0; i < testcells.length; i++) {
			testcells[i] = new Cell();
			if (cells[i] != null) {
				testcells[i].setEntityId(cells[i].getEntityId());
				testcells[i].setCount(cells[i].getCount());
			}
		}
		testknap.setCells(testcells);
		testknap.setCellChangeFlags(new byte[this.cellChangeFlags.length]);
		for (ArticleEntity entity : elist) {
			if (!testknap.putTest(entity)) {
				return false;
			}
		}
		return true;
	}

	public void putAll(List<ArticleEntity> elist, String reason) {
		if (!isValid()) {
			if (logger.isInfoEnabled()) logger.info("[putAll] [{}] [fail] [背包到期] [owner:{}] [{}] [{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), reason});
			return;
		}
		for (ArticleEntity entity : elist) {
			put(entity,reason);
		}
	}

	/**
	 * 加入一个空格
	 * @param entityId
	 * @param count
	 * @return
	 */
	public boolean putToEmptyCell(long entityId, int count, String reason) {
		if (!isValid()) {
			if (logger.isInfoEnabled()) logger.info("[put] [{}] [fail] [背包到期] [owner:{}] [{}] [{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), reason});
			return false;
		}
		return super.putToEmptyCell(entityId, count, reason);
	}

	/**
	 * 获取空格子的数量
	 * 在使用的时候一定要判断有效期
	 * @return
	 */
	public int getEmptyNum() {
		if (!isValid()) {
			if (logger.isInfoEnabled()) logger.info("[EmptyNum] [fail] [背包到期] [owner:" + owner.getId() + "] [" + owner.getUsername() + "] [" + owner.getName() + "]");
			return 0;
		}
		int c = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null || cells[i].entityId == -1 || cells[i].count <= 0) c++;
		}
		return c;
	}

	/**
	 * 获取空格子的数量
	 * 在使用的时候一定要判断有效期
	 * @return
	 */
	public int 得到剩余格子数不论是否有效() {
		int c = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null || cells[i].entityId == -1 || cells[i].count <= 0) c++;
		}
		return c;
	}

	/**
	 * 判断背包是否已满
	 * 在使用的时候一定要判断有效期
	 * @return
	 */
	public boolean isFull() {
		if (!isValid()) {
			if (logger.isInfoEnabled()) logger.info("[isFull] [fail] [背包到期] [owner:" + owner.getId() + "] [" + owner.getUsername() + "] [" + owner.getName() + "]");
			return true;
		}
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null || cells[i].entityId == -1 || cells[i].count <= 0) return false;
		}
		return true;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
		this.setModified(true);
	}

	public boolean isValid() {
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleEntity ae = aem.getEntity(articleId);
		if (ae != null && ae.getTimer() != null && !ae.getTimer().isClosed()) {
			return true;
		}
		return false;
	}
}
