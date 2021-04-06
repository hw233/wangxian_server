package com.fy.engineserver.datasource.article.data.soulpith;

import com.fy.engineserver.datasource.article.data.articles.Article;
/**
 * 仙气葫芦道具
 */
public class GourdArticle  extends Article{
	/** 增加的经验值 */
	private long addExp;

	public long getAddExp() {
		return addExp;
	}

	public void setAddExp(long addExp) {
		this.addExp = addExp;
	}
}
