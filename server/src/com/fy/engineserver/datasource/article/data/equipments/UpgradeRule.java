package com.fy.engineserver.datasource.article.data.equipments;

import java.util.Arrays;

public class UpgradeRule {

	private String ruleName;
	private int typename;
	private String oldEquipmentName;
	private String oldEquipmentName_stat;
	private String newEquipmentName_stat;
	private String newEquipmentName;
	private int newEquipmentColor;
	private String materialNames[] = new String[0];
	private String materialNames_stat[] = new String[0];
	private int materialNum[] = new int[0]; 
	private long costSilver;
	private String showMess;
	
	public int getTypename() {
		return typename;
	}
	public void setTypename(int typename) {
		this.typename = typename;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getOldEquipmentName() {
		return oldEquipmentName;
	}
	public void setOldEquipmentName(String oldEquipmentName) {
		this.oldEquipmentName = oldEquipmentName;
	}
	public String getOldEquipmentName_stat() {
		return oldEquipmentName_stat;
	}
	public void setOldEquipmentName_stat(String oldEquipmentName_stat) {
		this.oldEquipmentName_stat = oldEquipmentName_stat;
	}
	public String getNewEquipmentName_stat() {
		return newEquipmentName_stat;
	}
	public void setNewEquipmentName_stat(String newEquipmentName_stat) {
		this.newEquipmentName_stat = newEquipmentName_stat;
	}
	public String getNewEquipmentName() {
		return newEquipmentName;
	}
	public void setNewEquipmentName(String newEquipmentName) {
		this.newEquipmentName = newEquipmentName;
	}
	public int getNewEquipmentColor() {
		return newEquipmentColor;
	}
	public void setNewEquipmentColor(int newEquipmentColor) {
		this.newEquipmentColor = newEquipmentColor;
	}
	public String[] getMaterialNames() {
		return materialNames;
	}
	public void setMaterialNames(String[] materialNames) {
		this.materialNames = materialNames;
	}
	public String[] getMaterialNames_stat() {
		return materialNames_stat;
	}
	public void setMaterialNames_stat(String[] materialNames_stat) {
		this.materialNames_stat = materialNames_stat;
	}
	public int[] getMaterialNum() {
		return materialNum;
	}
	public void setMaterialNum(int[] materialNum) {
		this.materialNum = materialNum;
	}
	public long getCostSilver() {
		return costSilver;
	}
	public void setCostSilver(long costSilver) {
		this.costSilver = costSilver;
	}
	public String getShowMess() {
		return showMess;
	}
	public void setShowMess(String showMess) {
		this.showMess = showMess;
	}
	@Override
	public String toString() {
		return "UpgradeRule [ruleName=" + this.ruleName + ", typename=" + this.typename + ", oldEquipmentName=" + this.oldEquipmentName + ", oldEquipmentName_stat=" + this.oldEquipmentName_stat + ", newEquipmentName_stat=" + this.newEquipmentName_stat + ", newEquipmentName=" + this.newEquipmentName + ", newEquipmentColor=" + this.newEquipmentColor + ", materialNames=" + Arrays.toString(this.materialNames) + ", materialNames_stat=" + Arrays.toString(this.materialNames_stat) + ", materialNum=" + Arrays.toString(this.materialNum) + ", costSilver=" + this.costSilver + ", showMess=" + this.showMess + "]";
	}
	
}
