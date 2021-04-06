package com.fy.engineserver.activity.shop;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;

public class ActivityPropHasRate extends ActivityProp {

	public ActivityPropHasRate() {
		// TODO Auto-generated constructor stub
	}

	public ActivityPropHasRate(String articleCNName, int articleColor, int articleNum, boolean bind) {
		super(articleCNName, articleColor, articleNum, bind);
	}

	public ActivityPropHasRate(String articleCNName, int articleColor, int articleNum, boolean bind, int wight) {
		super(articleCNName, articleColor, articleNum, bind);
		this.wight = wight;
	}

	private int wight;

	public int getWight() {
		return wight;
	}

	public void setWight(int wight) {
		this.wight = wight;
	}

	public ArticleEntity getTempArticleEntity() {
		Article article = ArticleManager.getInstance().getArticleByCNname(this.getArticleCNName());
		if (article == null) {
			throw new IllegalStateException("物品不存在:[" + this.getArticleCNName() + "]");
		}
		try {
			return ArticleEntityManager.getInstance().createTempEntity(article, isBind(), ArticleEntityManager.VIP转盘, null, this.getArticleColor());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new IllegalStateException("出现异常" + this.getArticleCNName());
	}
}
