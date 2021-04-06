package com.fy.engineserver.datasource.article.entity.client;

public class PropsEntity extends ArticleEntity {
	/**
	 * 可使用道具的类型定义如下：<br>
	 * 0 未定义<br>
	 * 1 食品<br>
	 * 2 加血药品<br>
	 * 3 加蓝药品<br>
	 * 4 加血和蓝药品<br>
	 * 5 传送符<br>
	 * 6 坐骑<br>
	 * 7 复活道具<br>
	 * 8 洗点<br>
	 * 9 接任务道具
	 */
	protected byte propsType;

	/**
	 * 战斗状态限制，true战斗状态下不可用，false战斗状态下可用
	 */
	protected boolean fightStateLimit;

	/**
	 * 玩家等级限制
	 */
	protected short levelLimit;

	/**
	 * 道具的分类，同一分类下的道具共享CD时间
	 */
	protected String categoryName;

	public byte getPropsType() {
		return propsType;
	}

	public void setPropsType(byte propsType) {
		this.propsType = propsType;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public boolean isFightStateLimit() {
		return fightStateLimit;
	}

	public void setFightStateLimit(boolean fightStateLimit) {
		this.fightStateLimit = fightStateLimit;
	}

	public boolean getFightStateLimit() {
		return fightStateLimit;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(short levelLimit) {
		this.levelLimit = levelLimit;
	}
}
