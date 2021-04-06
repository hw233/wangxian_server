package com.fy.engineserver.homestead.cave.resource;

import com.fy.engineserver.homestead.cave.ResourceCollection;

/**
 * 资源等级配置
 * 
 * 
 */
public class ResourceLevelCfg {
	private int grade;
	private ResourceCollection lvUpCost;
	private int lvUpTime;
	private int lvUpResourceNum;

	public ResourceLevelCfg(int grade, ResourceCollection lvUpCost, int lvUpTime, int lvUpResourceNum) {
		this.grade = grade;
		this.lvUpCost = lvUpCost;
		this.lvUpTime = lvUpTime;
		this.lvUpResourceNum = lvUpResourceNum;
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

	public int getLvUpResourceNum() {
		return lvUpResourceNum;
	}

	public void setLvUpResourceNum(int lvUpResourceNum) {
		this.lvUpResourceNum = lvUpResourceNum;
	}

	@Override
	public String toString() {
		return "[仓库资源等级配置]等级[" + grade + "]升级消耗[" + lvUpCost.toString() + "]升级时间[" + lvUpTime + "]升级后资源上限[" + lvUpResourceNum + "]";
	}
}
