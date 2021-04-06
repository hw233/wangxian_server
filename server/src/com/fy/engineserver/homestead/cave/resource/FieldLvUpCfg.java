package com.fy.engineserver.homestead.cave.resource;

import com.fy.engineserver.homestead.cave.ResourceCollection;

/**
 * 田地等级配置
 * 
 * 
 */
public class FieldLvUpCfg implements Comparable<FieldLvUpCfg> {

	private int grade;
	private ResourceCollection lvUpCost;
	private int lvUpTime;
	/** 升级描述信息 */
	private String levelUpDes;
	private String currentLevelDes;
	private ResourceCollection maintenanceCost;
	private ResourceCollection maintenanceReback;

	public FieldLvUpCfg(int grade, ResourceCollection lvUpCost, int lvUpTime, String levelUpDes, String currentLevelDes, ResourceCollection maintenanceCost, ResourceCollection maintenanceReback) {
		this.grade = grade;
		this.lvUpCost = lvUpCost;
		this.lvUpTime = lvUpTime;
		this.maintenanceReback = maintenanceReback;
		this.levelUpDes = levelUpDes;
		this.currentLevelDes = currentLevelDes;
		this.maintenanceCost = maintenanceCost;
	}

	public final int getGrade() {
		return grade;
	}

	public final void setGrade(int grade) {
		this.grade = grade;
	}

	public final ResourceCollection getLvUpCost() {
		return lvUpCost;
	}

	public final void setLvUpCost(ResourceCollection lvUpCost) {
		this.lvUpCost = lvUpCost;
	}

	public final int getLvUpTime() {
		return lvUpTime;
	}

	public final void setLvUpTime(int lvUpTime) {
		this.lvUpTime = lvUpTime;
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
	public int compareTo(FieldLvUpCfg o) {
		return getGrade() - o.getGrade();
	}

	@Override
	public String toString() {
		return "FieldLvUpCfg [grade=" + grade + ", lvUpCost=" + lvUpCost + ", lvUpTime=" + lvUpTime + ", levelUpDes=" + levelUpDes + ", currentLevelDes=" + currentLevelDes + "]";
	}

}
