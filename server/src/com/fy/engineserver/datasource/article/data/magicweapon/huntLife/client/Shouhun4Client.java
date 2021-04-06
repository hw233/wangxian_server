package com.fy.engineserver.datasource.article.data.magicweapon.huntLife.client;

public class Shouhun4Client {
	/** 物品id */
	private long articleId;
	/** 兽魂等级 */
	private int level;
	/** 当前经验  */
	private long currentExp;
	/** 升级所需经验 */
	private long needExp;
	/** 增加属性类型  0气血  1攻击  2外防  3内防  4暴击   5命中   6闪避   7破甲   8精准   9免暴*/
	private int attrType;
	/** 增加属性数值 */
	private int attrNum;
	/** 颜色 */
	private int colorType;
	/** icon */
	private String icons;
	
	public Shouhun4Client(){}
	
	public Shouhun4Client(long articleId, int level, long currentExp, long needExp, int attrType, int attrNum, int colorType) {
		super();
		this.articleId = articleId;
		this.level = level;
		this.currentExp = currentExp;
		this.needExp = needExp;
		this.attrType = attrType;
		this.attrNum = attrNum;
		this.colorType = colorType;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getCurrentExp() {
		return currentExp;
	}

	public void setCurrentExp(long currentExp) {
		this.currentExp = currentExp;
	}

	public long getNeedExp() {
		return needExp;
	}

	public void setNeedExp(long needExp) {
		this.needExp = needExp;
	}

	public int getAttrType() {
		return attrType;
	}

	public void setAttrType(int attrType) {
		this.attrType = attrType;
	}

	public int getAttrNum() {
		return attrNum;
	}

	public void setAttrNum(int attrNum) {
		this.attrNum = attrNum;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public String getIcons() {
		return icons;
	}

	public void setIcons(String icons) {
		this.icons = icons;
	}
	
	
}
