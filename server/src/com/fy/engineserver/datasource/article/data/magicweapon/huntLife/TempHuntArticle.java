package com.fy.engineserver.datasource.article.data.magicweapon.huntLife;

public class TempHuntArticle implements Comparable<TempHuntArticle>{
	
	public long articleId;
	
	public int level;
	
	public long exp;
	
	public TempHuntArticle(long articleId, int level, long exp) {
		super();
		this.articleId = articleId;
		this.level = level;
		this.exp = exp;
	}
	

	@Override
	public String toString() {
		return "TempHuntArticle [articleId=" + articleId + ", level=" + level + ", exp=" + exp + "]";
	}





	@Override
	public int compareTo(TempHuntArticle o) {
		// TODO Auto-generated method stub
		if (this.level < o.level) {
			return -1;
		} 
		if (this.level == o.level) {
			if (this.exp < o.exp) {
				return -1;
			}
		}
		return 0;
	}

}
