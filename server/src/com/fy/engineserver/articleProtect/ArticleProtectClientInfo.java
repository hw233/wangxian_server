package com.fy.engineserver.articleProtect;

public class ArticleProtectClientInfo {
	public static final int ARTICLETYPE_KNAP = 10;		//背包物品
	public static final int ARTICLETYPE_PET = 11;		//宠物栏

	private int articleType;
	
	private long articleID;
	
	private int state;
	
	private long removeTime;

	public void setArticleType(int articleType) {
		this.articleType = articleType;
	}

	public int getArticleType() {
		return articleType;
	}

	public void setArticleID(long articleID) {
		this.articleID = articleID;
	}

	public long getArticleID() {
		return articleID;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setRemoveTime(long removeTime) {
		this.removeTime = removeTime;
	}

	public long getRemoveTime() {
		return removeTime;
	}
	
}
