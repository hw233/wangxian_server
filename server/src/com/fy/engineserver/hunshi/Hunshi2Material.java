package com.fy.engineserver.hunshi;

/**
 * 套装魂石合成需要的材料
 * 
 * 
 */
public class Hunshi2Material {
	// private String targetName;
	private String targetCNName;
	private int color;
	private long targetTempId;
	// private String[] materialNames;
	private int[] materialCNIds;
	private int[] materialColors;
	private long[] materialTempIds;
	private long mergeCost;

	public Hunshi2Material() {
	}

	public Hunshi2Material(String targetCNName, int color, long targetTempId, int[] materialCNIds, int[] materialColors, long[] materialTempIds, long mergeCost) {
		this.targetCNName = targetCNName;
		this.color = color;
		this.targetTempId = targetTempId;
		this.materialCNIds = materialCNIds;
		this.materialColors = materialColors;
		this.materialTempIds = materialTempIds;
		this.mergeCost = mergeCost;
	}

	public String getTargetCNName() {
		return targetCNName;
	}

	public void setTargetCNName(String targetCNName) {
		this.targetCNName = targetCNName;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public long getTargetTempId() {
		return targetTempId;
	}

	public void setTargetTempId(long targetTempId) {
		this.targetTempId = targetTempId;
	}

	public int[] getMaterialCNIds() {
		return materialCNIds;
	}

	public void setMaterialCNIds(int[] materialCNIds) {
		this.materialCNIds = materialCNIds;
	}

	public int[] getMaterialColors() {
		return materialColors;
	}

	public void setMaterialColors(int[] materialColors) {
		this.materialColors = materialColors;
	}

	public long[] getMaterialTempIds() {
		return materialTempIds;
	}

	public void setMaterialTempIds(long[] materialTempIds) {
		this.materialTempIds = materialTempIds;
	}

	public long getMergeCost() {
		return mergeCost;
	}

	public void setMergeCost(long mergeCost) {
		this.mergeCost = mergeCost;
	}

}
