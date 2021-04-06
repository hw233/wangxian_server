package com.fy.engineserver.sprite.npc;

import com.xuanzhi.tools.cache.Cacheable;

/**
 * 以地物实现的NPC
 * 
 * 
 */
public class GroundNPC extends NPC implements Cacheable {
	/** 等级 */
	private int grade;
	/** 形象设置 */
	private String[] avatas;
	/** 建设中的avata */
	private String[] buildingAvatas;

	private boolean inBuilding = false;

	public GroundNPC() {

	}

	@Override
	public int getSize() {
		return 0;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.setLevel(grade);
		this.grade = grade;
	}

	public String[] getAvatas() {
		return avatas;
	}

	public void setAvatas(String[] avatas) {
		this.avatas = avatas;
	}

	public String[] getBuildingAvatas() {
		return buildingAvatas;
	}

	public void setBuildingAvatas(String[] buildingAvatas) {
		this.buildingAvatas = buildingAvatas;
	}

	public boolean isInBuilding() {
		return inBuilding;
	}

	public void setInBuilding(boolean inBuilding) {
		this.inBuilding = inBuilding;
	}

}
