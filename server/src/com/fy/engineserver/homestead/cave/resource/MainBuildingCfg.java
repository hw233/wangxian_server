package com.fy.engineserver.homestead.cave.resource;

import com.fy.engineserver.homestead.cave.ResourceCollection;

/**
 * 主建筑配置信息
 * 
 * 
 */
public class MainBuildingCfg implements Comparable<MainBuildingCfg> {

	private int grade;
	private ResourceCollection lvUpCost;
	private ResourceCollection maintenanceCost;
	private ResourceCollection maintenanceReback;
	private int lvUpTime;
	private int storehouseLvLimit;
	private int pethouseLvLimit;
	private int fenceLvLimit;
	private int fieldLvLimit;
	private int fieldNumLimit;
	/** 封印（天） */
	private int khatamDay;
	/** 删除（天） */
	private int deleteDay;
	/** 升级描述信息 */
	private String levelUpDes;
	/** 当前等级描述:在NPC窗口显示 */
	private String currentLevelDes;

	public MainBuildingCfg(int grade, ResourceCollection lvUpCost, ResourceCollection maintenanceCost, ResourceCollection maintenanceReback, int lvUpTime, int storehouseLvLimit, int pethouseLvLimit, int fenceLvLimit, int fieldLvLimit, int fieldNumLimit, int khatamDay, int deleteDay, String levelUpDes, String currentLevelDes) {
		this.grade = grade;
		this.lvUpCost = lvUpCost;
		this.maintenanceCost = maintenanceCost;
		this.maintenanceReback = maintenanceReback;
		this.lvUpTime = lvUpTime;
		this.storehouseLvLimit = storehouseLvLimit;
		this.pethouseLvLimit = pethouseLvLimit;
		this.fenceLvLimit = fenceLvLimit;
		this.fieldLvLimit = fieldLvLimit;
		this.fieldNumLimit = fieldNumLimit;
		this.khatamDay = khatamDay;
		this.deleteDay = deleteDay;
		this.levelUpDes = levelUpDes;
		this.currentLevelDes = currentLevelDes;
	}

	@Override
	public int compareTo(MainBuildingCfg o) {
		return getGrade() - o.getGrade();
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

	public ResourceCollection getMaintenanceCost() {
		return maintenanceCost;
	}

	public void setMaintenanceCost(ResourceCollection maintenanceCost) {
		this.maintenanceCost = maintenanceCost;
	}

	public int getLvUpTime() {
		return lvUpTime;
	}

	public void setLvUpTime(int lvUpTime) {
		this.lvUpTime = lvUpTime;
	}

	public int getStorehouseLvLimit() {
		return storehouseLvLimit;
	}

	public void setStorehouseLvLimit(int storehouseLvLimit) {
		this.storehouseLvLimit = storehouseLvLimit;
	}

	public int getPethouseLvLimit() {
		return pethouseLvLimit;
	}

	public void setPethouseLvLimit(int pethouseLvLimit) {
		this.pethouseLvLimit = pethouseLvLimit;
	}

	public int getFenceLvLimit() {
		return fenceLvLimit;
	}

	public void setFenceLvLimit(int fenceLvLimit) {
		this.fenceLvLimit = fenceLvLimit;
	}

	public int getFieldLvLimit() {
		return fieldLvLimit;
	}

	public void setFieldLvLimit(int fieldLvLimit) {
		this.fieldLvLimit = fieldLvLimit;
	}

	public int getFieldNumLimit() {
		return fieldNumLimit;
	}

	public void setFieldNumLimit(int fieldNumLimit) {
		this.fieldNumLimit = fieldNumLimit;
	}

	public int getKhatamDay() {
		return khatamDay;
	}

	public void setKhatamDay(int khatamDay) {
		this.khatamDay = khatamDay;
	}

	public int getDeleteDay() {
		return deleteDay;
	}

	public void setDeleteDay(int deleteDay) {
		this.deleteDay = deleteDay;
	}

	public String getLevelUpDes() {
		return levelUpDes;
	}

	public void setLevelUpDes(String levelUpDes) {
		this.levelUpDes = levelUpDes;
	}

	public String getCurrentLevelDes() {
		return currentLevelDes;
	}

	public void setCurrentLevelDes(String currentLevelDes) {
		this.currentLevelDes = currentLevelDes;
	}

	public ResourceCollection getMaintenanceReback() {
		return maintenanceReback;
	}

	public void setMaintenanceReback(ResourceCollection maintenanceReback) {
		this.maintenanceReback = maintenanceReback;
	}

	@Override
	public String toString() {
		return "MainBuildingCfg [grade=" + grade + ", lvUpCost=" + lvUpCost + ", maintenanceCost=" + maintenanceCost + ", lvUpTime=" + lvUpTime + ", storehouseLvLimit=" + storehouseLvLimit + ", pethouseLvLimit=" + pethouseLvLimit + ", fenceLvLimit=" + fenceLvLimit + ", fieldLvLimit=" + fieldLvLimit + ", fieldNumLimit=" + fieldNumLimit + ", khatamDay=" + khatamDay + ", deleteDay=" + deleteDay + ", levelUpDes=" + levelUpDes + ", currentLevelDes=" + currentLevelDes + "]";
	}
}
