package com.fy.engineserver.homestead.cave.resource;

import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;

/**
 * 仓库配置
 * 
 * 
 */
public class StorehouseCfg implements Comparable<StorehouseCfg>, FaeryConfig {

	private int grade;
	private ResourceCollection lvUpCost;
	private ResourceCollection maintenanceReback;
	private int lvUpTime;

	private int foodLv;
	private int woodLv;
	private int stoneLv;
	/** 升级描述信息 */
	private String levelUpDes;

	private String currentLevelDes;
	/** 维护费用 */
	private ResourceCollection maintenanceCost;

	public StorehouseCfg(int grade, ResourceCollection lvUpCost, int lvUpTime, int foodLv, int woodLv, int stoneLv, String levelUpDes, String currentLevelDes, ResourceCollection maintenanceCost, ResourceCollection maintenanceReback) {
		this.grade = grade;
		this.lvUpCost = lvUpCost;
		this.lvUpTime = lvUpTime;
		this.foodLv = foodLv;
		this.maintenanceReback = maintenanceReback;
		this.woodLv = woodLv;
		this.stoneLv = stoneLv;
		this.levelUpDes = levelUpDes;
		this.currentLevelDes = currentLevelDes;
		this.maintenanceCost = maintenanceCost;
	}

	/**
	 * 得到当前开放的某种资源的等级
	 * @param resourceType
	 * @return
	 */
	public int getCurrResourceLevel(int resourceType) {
		switch (resourceType) {
		case FRUIT_RES_FOOD:
			return getFoodLv();
		case FRUIT_RES_STONE:
			return getStoneLv();
		case FRUIT_RES_WOOD:
			return getWoodLv();
		}
		throw new IllegalStateException("resourceType = [" + resourceType + "]");
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public ResourceCollection getLvUpCost() {
		return lvUpCost;
	}

	public void setLvUpCost(ResourceCollection lvUpCost) {
		this.lvUpCost = lvUpCost;
	}

	public int getLvUpTime() {
		return lvUpTime;
	}

	public void setLvUpTime(int lvUpTime) {
		this.lvUpTime = lvUpTime;
	}

	public int getFoodLv() {
		return foodLv;
	}

	public void setFoodLv(int foodLv) {
		this.foodLv = foodLv;
	}

	public int getWoodLv() {
		return woodLv;
	}

	public void setWoodLv(int woodLv) {
		this.woodLv = woodLv;
	}

	public int getStoneLv() {
		return stoneLv;
	}

	public void setStoneLv(int stoneLv) {
		this.stoneLv = stoneLv;
	}

	public String getLevelUpDes() {
		return levelUpDes;
	}

	public void setLevelUpDes(String levelUpDes) {
		this.levelUpDes = levelUpDes;
	}

	@Override
	public int compareTo(StorehouseCfg o) {
		return getGrade() - o.getGrade();
	}

	public String getCurrentLevelDes() {
		return currentLevelDes;
	}

	public void setCurrentLevelDes(String currentLevelDes) {
		this.currentLevelDes = currentLevelDes;
	}

	public ResourceCollection getMaintenanceCost() {
		return maintenanceCost;
	}

	public void setMaintenanceCost(ResourceCollection maintenanceCost) {
		this.maintenanceCost = maintenanceCost;
	}

	public ResourceCollection getMaintenanceReback() {
		return maintenanceReback;
	}

	public void setMaintenanceReback(ResourceCollection maintenanceReback) {
		this.maintenanceReback = maintenanceReback;
	}

	@Override
	public String toString() {
		return "StorehouseCfg [grade=" + grade + ", lvUpCost=" + lvUpCost + ", lvUpTime=" + lvUpTime + ", foodLv=" + foodLv + ", woodLv=" + woodLv + ", stoneLv=" + stoneLv + ", levelUpDes=" + levelUpDes + ", currentLevelDes=" + currentLevelDes + ", maintenanceCost=" + maintenanceCost + "]";
	}
}
