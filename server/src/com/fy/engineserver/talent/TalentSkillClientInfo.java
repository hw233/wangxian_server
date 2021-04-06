package com.fy.engineserver.talent;

public class TalentSkillClientInfo {

	private int id;
	private String name;
	private int currLevel;
	private int maxLevel;
	private String icon;
	private String mess;
	private String mess1;
	private String mess2;
	private String mess3;
	private int relyId;			//前置id
	private int talentType;		//0:A,1:B
	private boolean isOpen;		//技能是否开启
	private int layer;
	private int propType;
	private double levelValues[];	//每级增加的值
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
	public int getPropType() {
		return propType;
	}
	public void setPropType(int propType) {
		this.propType = propType;
	}
	public String getMess3() {
		return mess3;
	}
	public void setMess3(String mess3) {
		this.mess3 = mess3;
	}
	public String getMess2() {
		return mess2;
	}
	public void setMess2(String mess2) {
		this.mess2 = mess2;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
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
	public int getCurrLevel() {
		return currLevel;
	}
	public void setCurrLevel(int currLevel) {
		this.currLevel = currLevel;
	}
	public int getTalentType() {
		return talentType;
	}
	public void setTalentType(int talentType) {
		this.talentType = talentType;
	}
	
	public boolean isIsOpen() {
		return isOpen;
	}
	public void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	public double[] getLevelValues() {
		return levelValues;
	}
	public void setLevelValues(double[] levelValues) {
		this.levelValues = levelValues;
	}
	@Override
	public String toString() {
		return "TalentSkillClientInfo [currLevel=" + currLevel + ", icon="
				+ icon + ", id=" + id + ", maxLevel=" + maxLevel + ", mess="
				+ mess + ", name=" + name + ", relyId=" + relyId
				+ ", talentType=" + talentType + "]";
	}
	
}
