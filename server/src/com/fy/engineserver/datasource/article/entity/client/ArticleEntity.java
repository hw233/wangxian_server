package com.fy.engineserver.datasource.article.entity.client;

/**
 * 物品实体类，表明某个物体
 * 
 */
public class ArticleEntity {
	protected long id;

	protected String name;

	/**
	 * 物品实体对应的显示的名字
	 */
	protected String showName;

	protected String iconId="";

	/**
	 * 绑定方式0不绑定，1装备绑定，2拾取绑定
	 */
	protected byte bindStyle;

	protected boolean binded;

	protected byte assignFlag = 0;

	/**
	 * 物品质地（颜色）
	 */
	protected byte colorType;

	protected int sellPrice;
	/**
	 * 道具失效后的处理方式
	 * 0:不处理 1：可以续费 2：消失
	 * 
	 */
	protected byte sequelType;

	/**
	 * 道具失效后的续费
	 */
	protected long sequelPrice;

	/**
	 * 物品的类型
	 */
	protected byte articleType = 0;
	
	
	/**
	 * 同类是否可以重叠防止（所谓同类是指同一个物种）<br>
	 * 如果为true，那么对应的此物种的物体上，有一个总体的数量<br>
	 * 多个此物体公用一个物体
	 */
	protected boolean overlap;
	
	/**
	 * 堆叠数量
	 */
	protected int overLapNum;
	
	/**
	 * 客户端用的，用于可合成物品，0不可合成物品 1可合成的物品
	 */
	protected byte composeArticleType;

	/**
	 * 背包类型，用于区分放入哪种背包
	 * KNAP_装备 = 0;
	 * KNAP_奇珍 = 1;
	 * KNAP_异宝 = 2;
	 * KNAP_任务 = 3;
	 * KNAP_宠物 = 4;
	 */
	protected int knapsackType = 2;
	
	/**
	 * 物品一级分类
	 */
	protected String oneClass;
	
	/**
	 * 物品二级分类
	 */
	protected String twoClass;

	public String getOneClass() {
		return oneClass;
	}

	public void setOneClass(String oneClass) {
		this.oneClass = oneClass;
	}

	public String getTwoClass() {
		return twoClass;
	}

	public void setTwoClass(String twoClass) {
		this.twoClass = twoClass;
	}

	public int getKnapsackType() {
		return knapsackType;
	}

	public void setKnapsackType(int knapsackType) {
		this.knapsackType = knapsackType;
	}

	public byte getComposeArticleType() {
		return composeArticleType;
	}

	public void setComposeArticleType(byte composeArticleType) {
		this.composeArticleType = composeArticleType;
	}

	public boolean isOverlap() {
		return overlap;
	}

	public void setOverlap(boolean overlap) {
		this.overlap = overlap;
	}

	public int getOverLapNum() {
		return overLapNum;
	}

	public void setOverLapNum(int overLapNum) {
		this.overLapNum = overLapNum;
	}

	public byte getArticleType() {
		return articleType;
	}

	public void setArticleType(byte articleType) {
		this.articleType = articleType;
	}
	
	public boolean isBinded() {
		return binded;
	}

	public void setBinded(boolean binded) {
		this.binded = binded;
	}

	public byte getBindStyle() {
		return bindStyle;
	}

	public void setBindStyle(byte bindStyle) {
		this.bindStyle = bindStyle;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShowName() {
		if (showName == null) {
			return name;
		}
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getIconId() {
		return iconId;
	}

	public void setIconId(String iconName) {
		this.iconId = iconName;
	}

	public byte getAssignFlag() {
		return assignFlag;
	}

	public void setAssignFlag(byte assignFlag) {
		this.assignFlag = assignFlag;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public byte getSequelType() {
		return sequelType;
	}

	public void setSequelType(byte sequelType) {
		this.sequelType = sequelType;
	}

	public long getSequelPrice() {
		return sequelPrice;
	}

	public void setSequelPrice(long sequelPrice) {
		this.sequelPrice = sequelPrice;
	}

	public byte getColorType() {
		return colorType;
	}

	public void setColorType(byte colorType) {
		this.colorType = colorType;
	}
}
