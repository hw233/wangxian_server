package com.fy.engineserver.jiazu2.model;

public class JiazuSkillModel {
	private int skillId;
	
	private String skillName;
	
	private String icon;
	
	private int maxLevel;
	
	private int currentLevel;
	
	private int needExp;
	
	private int currentExp;
	
	private int costType;
	
	private long costNum;
	
	private String currentLevelDes;
	
	@Override
	public String toString() {
		return "JiazuSkillModel [skillId=" + skillId + ", skillName=" + skillName + ", icon=" + icon + ", maxLevel=" + maxLevel + ", currentLevel=" + currentLevel + ", needExp=" + needExp + ", currentExp=" + currentExp + ", costType=" + costType + ", costNum=" + costNum + ", currentLevelDes=" + currentLevelDes + "]";
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public int getNeedExp() {
		return needExp;
	}

	public void setNeedExp(int needExp) {
		this.needExp = needExp;
	}

	public int getCurrentExp() {
		return currentExp;
	}

	public void setCurrentExp(int currentExp) {
		this.currentExp = currentExp;
	}

	public int getCostType() {
		return costType;
	}

	public void setCostType(int costType) {
		this.costType = costType;
	}

	public long getCostNum() {
		return costNum;
	}

	public void setCostNum(long costNum) {
		this.costNum = costNum;
	}

	public String getCurrentLevelDes() {
		return currentLevelDes;
	}

	public void setCurrentLevelDes(String currentLevelDes) {
		this.currentLevelDes = currentLevelDes;
	}
	
	
}
