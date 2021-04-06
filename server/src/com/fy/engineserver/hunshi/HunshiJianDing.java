package com.fy.engineserver.hunshi;

import com.fy.engineserver.util.SimpleKey;

public class HunshiJianDing {
	@SimpleKey
	private int hunshiColor;
	private String[] materialNames;
	private String[] materialCNNames;
	private int[] materialNums;
	private long jianDingCost;// 鉴定花费
	private long mergeCost;// 合成花费
	private String[] ronghezhi;// 数值
	private int[] ronghezhiRate;// 数值是融合值乘以100000后的数

	public HunshiJianDing() {
	}

	public HunshiJianDing(int hunshiColor, String[] materialNames, String[] materialCNNames, int[] materialNums, long jianDingCost, long mergeCost, String[] ronghezhi, int[] ronghezhiRate) {
		this.hunshiColor = hunshiColor;
		this.materialNames = materialNames;
		this.materialCNNames = materialCNNames;
		this.materialNums = materialNums;
		this.jianDingCost = jianDingCost;
		this.mergeCost = mergeCost;
		this.ronghezhi = ronghezhi;
		this.ronghezhiRate = ronghezhiRate;
	}

	public int getHunshiColor() {
		return hunshiColor;
	}

	public void setHunshiColor(int hunshiColor) {
		this.hunshiColor = hunshiColor;
	}

	public String[] getMaterialNames() {
		return materialNames;
	}

	public void setMaterialNames(String[] materialNames) {
		this.materialNames = materialNames;
	}

	public String[] getMaterialCNNames() {
		return materialCNNames;
	}

	public void setMaterialCNNames(String[] materialCNNames) {
		this.materialCNNames = materialCNNames;
	}

	public int[] getMaterialNums() {
		return materialNums;
	}

	public void setMaterialNums(int[] materialNums) {
		this.materialNums = materialNums;
	}

	public long getJianDingCost() {
		return jianDingCost;
	}

	public void setJianDingCost(long jianDingCost) {
		this.jianDingCost = jianDingCost;
	}

	public String[] getRonghezhi() {
		return ronghezhi;
	}

	public void setRonghezhi(String[] ronghezhi) {
		this.ronghezhi = ronghezhi;
	}

	public long getMergeCost() {
		return mergeCost;
	}

	public void setMergeCost(long mergeCost) {
		this.mergeCost = mergeCost;
	}

	public int[] getRonghezhiRate() {
		return ronghezhiRate;
	}

	public void setRonghezhiRate(int[] ronghezhiRate) {
		this.ronghezhiRate = ronghezhiRate;
	}

}
