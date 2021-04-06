package com.fy.engineserver.datasource.article.data.magicweapon.huntLife;

public class TempHuntArticle2{
	
	public long articleId;
	
	public int level;
	
	public long exp;
	
	public int addType;
	
	public TempHuntArticle2(long articleId, int level, long exp, int addType) {
		super();
		this.articleId = articleId;
		this.level = level;
		this.exp = exp;
		this.addType = addType;
	}
	
	@Override
	public String toString() {
		return "TempHuntArticle2 [articleId=" + articleId + ", level=" + level + ", exp=" + exp + ", addType=" + addType + "]";
	}


}
