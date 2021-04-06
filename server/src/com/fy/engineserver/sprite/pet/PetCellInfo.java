package com.fy.engineserver.sprite.pet;

public class PetCellInfo {

	private int id;
	private String name;
	private int level;
	private int addProp;
	private int materialNum;
	private long exp;
	private String showInfo;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getAddProp() {
		return addProp;
	}
	public void setAddProp(int addProp) {
		this.addProp = addProp;
	}
	public int getMaterialNum() {
		return materialNum;
	}
	public void setMaterialNum(int materialNum) {
		this.materialNum = materialNum;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public String getShowInfo() {
		return showInfo;
	}
	public void setShowInfo(String showInfo) {
		this.showInfo = showInfo;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "[addProp=" + addProp + ", exp=" + exp + ", id="
				+ id + ", level=" + level + ", materialNum=" + materialNum
				+ ", name=" + name + ", showInfo=" + showInfo + "]";
	}
	
	
}
