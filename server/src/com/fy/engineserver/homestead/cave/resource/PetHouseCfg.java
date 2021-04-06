package com.fy.engineserver.homestead.cave.resource;

import com.fy.engineserver.homestead.cave.ResourceCollection;

/**
 * 宠物房配置
 * 
 * 
 */
public class PetHouseCfg implements Comparable<PetHouseCfg> {

	private int grade;
	private ResourceCollection lvUpCost;
	private int lvUpTime;
	private int hookNum;
	private double expParm;
	private int storeNum;
	private String levelUpDes;
	private String currentLevelDes;
	private ResourceCollection maintenanceCost;
	private ResourceCollection maintenanceReback;

	public PetHouseCfg(int grade, ResourceCollection lvUpCost, int lvUpTime, int positionNum, double expParm, int storeNum, String levelUpDes, String currentLevelDes, ResourceCollection maintenanceCost, ResourceCollection maintenanceReback) {
		this.grade = grade;
		this.lvUpCost = lvUpCost;
		this.maintenanceReback = maintenanceReback;
		this.lvUpTime = lvUpTime;
		this.hookNum = positionNum;
		this.expParm = expParm;
		this.storeNum = storeNum;
		this.levelUpDes = levelUpDes;
		this.currentLevelDes = currentLevelDes;
		this.maintenanceCost = maintenanceCost;
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

	public int getHookNum() {
		return hookNum;
	}

	public void setHookNum(int hookNum) {
		this.hookNum = hookNum;
	}

	public double getExpParm() {
		return expParm;
	}

	public void setExpParm(double expParm) {
		this.expParm = expParm;
	}

	public int getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(int storeNum) {
		this.storeNum = storeNum;
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
	public int compareTo(PetHouseCfg o) {
		return getGrade() - o.getGrade();
	}

	public ResourceCollection getMaintenanceCost() {
		return maintenanceCost;
	}

	public void setMaintenanceCost(ResourceCollection maintenanceCost) {
		this.maintenanceCost = maintenanceCost;
	}

	@Override
	public String toString() {
		return "PetHouseCfg [grade=" + grade + ", lvUpCost=" + lvUpCost + ", lvUpTime=" + lvUpTime + ", hookNum=" + hookNum + ", expParm=" + expParm + ", storeNum=" + storeNum + ", levelUpDes=" + levelUpDes + ", currentLevelDes=" + currentLevelDes + ", maintenanceCost=" + maintenanceCost + "]";
	}

}
