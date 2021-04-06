package com.fy.engineserver.talent;

import java.util.Arrays;

public class TalentSkillInfo {

	private int id;
	private String name;
	private int maxLevel;
	private String icon;
	private String mess;
	private String mess1;
	private String mess2;
	private String mess3;
	private int relyId;			//前置id
	private double levelValues[];	//每级增加的值
	private int propType;		//增加的属性类型,0:具体数，1:百分比
	private int talentType;		//0:A,1:B
	private int layer;
	private String skillLimitMess;
	
	public String getSkillLimitMess() {
		return skillLimitMess;
	}
	public void setSkillLimitMess(String skillLimitMess) {
		this.skillLimitMess = skillLimitMess;
	}
	public String getMess1() {
		return mess1;
	}
	public void setMess1(String mess1) {
		this.mess1 = mess1;
	}
	public String getMess3() {
		return mess3;
	}
	public void setMess3(String mess3) {
		this.mess3 = mess3;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getMess() {
		return mess;
	}
	public void setMess(String mess) {
		this.mess = mess;
	}
	public int getRelyId() {
		return relyId;
	}
	public void setRelyId(int relyId) {
		this.relyId = relyId;
	}
	public int getPropType() {
		return propType;
	}
	public void setPropType(int propType) {
		this.propType = propType;
	}
	
	public int getTalentType() {
		return talentType;
	}
	public void setTalentType(int talentType) {
		this.talentType = talentType;
	}
	
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public String getMess2() {
		return mess2;
	}
	public void setMess2(String mess2) {
		this.mess2 = mess2;
	}
	
	public double[] getLevelValues() {
		return levelValues;
	}
	public void setLevelValues(double[] levelValues) {
		this.levelValues = levelValues;
	}
	@Override
	public String toString() {
		return "[icon=" + icon + ", id=" + id
				+ ", levelValues=" + Arrays.toString(levelValues)
				+ ", maxLevel=" + maxLevel + ", mess=" + mess + ", name="
				+ name + ", propType=" + propType + ", relyId=" + relyId + "]";
	}
	
}
