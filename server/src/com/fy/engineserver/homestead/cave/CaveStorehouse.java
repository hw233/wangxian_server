package com.fy.engineserver.homestead.cave;

import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 仓库
 * 
 * 
 */
@SimpleEmbeddable
public class CaveStorehouse extends CaveBuilding implements FaeryConfig {

	/** 食物存放等级 */
	private int foodLevel;
	/** 木材存放等级 */
	private int woodLevel;
	/** 石料存放等级 */
	private int stoneLevel;

	public CaveStorehouse() {
		setType(CAVE_BUILDING_TYPE_STORE);
	}

	/**
	 * 得到当前某种资源的等级
	 * @param resourceType
	 * @return
	 */
	public int getCurrResourceLevel(int resourceType) {
		switch (resourceType) {
		case FRUIT_RES_FOOD:
			return getFoodLevel();
		case FRUIT_RES_STONE:
			return getStoneLevel();
		case FRUIT_RES_WOOD:
			return getWoodLevel();
		}
		throw new IllegalStateException("resourceType = [" + resourceType + "]");
	}

	public void checkSchould() {

	}

	public int getFoodLevel() {
		return foodLevel;
	}

	public void setFoodLevel(int foodLevel) {
		this.foodLevel = foodLevel;
	}

	public int getWoodLevel() {
		return woodLevel;
	}

	public void setWoodLevel(int woodLevel) {
		this.woodLevel = woodLevel;
	}

	public int getStoneLevel() {
		return stoneLevel;
	}

	public void setStoneLevel(int stoneLevel) {
		this.stoneLevel = stoneLevel;
	}

	@Override
	public void modifyName() {
		super.modifyName();
	}

	@Override
	public String toString() {
		return "CaveStorehouse [foodLevel=" + foodLevel + ", woodLevel=" + woodLevel + ", stoneLevel=" + stoneLevel + "]";
	}
}
