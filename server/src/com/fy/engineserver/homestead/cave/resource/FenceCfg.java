package com.fy.engineserver.homestead.cave.resource;

import com.fy.engineserver.homestead.cave.ResourceCollection;

/**
 * 栅栏配置
 * 
 * 
 */
public class FenceCfg implements Comparable<FenceCfg> {

	private int grade;
	private ResourceCollection lvUpCost;
	private int lvUpTime;
	private int enterCost;
	private String levelUpDes;
	private String currentLevelDes;
	private ResourceCollection maintenanceCost;
	private ResourceCollection maintenanceReback;

	public FenceCfg(int grade, ResourceCollection lvUpCost, int lvUpTime, int enterCost, String levelUpDes, String currentLevelDes, ResourceCollection maintenanceCost, ResourceCollection maintenanceReback) {
		this.grade = grade;
		this.lvUpCost = lvUpCost;
		this.lvUpTime = lvUpTime;
		this.maintenanceReback = maintenanceReback;
		this.enterCost = enterCost;
		this.levelUpDes = levelUpDes;
		this.currentLevelDes = currentLevelDes;
		this.maintenanceCost = maintenanceCost;
	}

	public int getGrade() {
		return grade;
	}

	public ResourceCollection getLvUpCost() {
		return lvUpCost;
	}

	public int getLvUpTime() {
		return lvUpTime;
	}

	public int getEnterCost() {
		return enterCost;
	}

	public final void setGrade(int grade) {
		this.grade = grade;
	}

	public final void setLvUpCost(ResourceCollection lvUpCost) {
		this.lvUpCost = lvUpCost;
	}

	public final void setLvUpTime(int lvUpTime) {
		this.lvUpTime = lvUpTime;
	}

	public final void setEnterCost(int enterCost) {
		this.enterCost = enterCost;
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
	public int compareTo(FenceCfg o) {
		return getGrade() - o.getGrade();
	}

	@Override
	public String toString() {
		return "FenceCfg [grade=" + grade + ", lvUpCost=" + lvUpCost + ", lvUpTime=" + lvUpTime + ", enterCost=" + enterCost + ", levelUpDes=" + levelUpDes + ", currentLevelDes=" + currentLevelDes + "]";
	}
}
