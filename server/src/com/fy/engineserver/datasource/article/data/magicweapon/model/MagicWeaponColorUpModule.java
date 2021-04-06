package com.fy.engineserver.datasource.article.data.magicweapon.model;

import java.util.Arrays;

/**
 * 法宝突破阶等级限制module
 */
public class MagicWeaponColorUpModule {
	/** 法宝颜色 */
	private int colorType;
	/** 法宝阶级 */
	private int[] magicWeaponLvs;
	/** 突破消耗物品统计名 */
	private String[] articleCNNNames;
	/** 消耗道具数量 */
	private int[] costNums;
	/** 成功概率 */
	private int[] probabblys;
	
	@Override
	public String toString() {
		return "MagicWeaponColorUpModule [colorType=" + colorType + ", magicWeaponLvs=" + Arrays.toString(magicWeaponLvs) + ", articleCNNNames=" + Arrays.toString(articleCNNNames) + ", costNums=" + Arrays.toString(costNums) + ", probabblys=" + Arrays.toString(probabblys) + "]";
	}

	public boolean isBreakLv(int lv) {
		for (int i=0; i<magicWeaponLvs.length; i++) {
			if (magicWeaponLvs[i] == lv) {
				return true;
			}
		}
		return false;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public String[] getArticleCNNNames() {
		return articleCNNNames;
	}

	public void setArticleCNNNames(String[] articleCNNNames) {
		this.articleCNNNames = articleCNNNames;
	}

	public int[] getCostNums() {
		return costNums;
	}

	public void setCostNums(int[] costNums) {
		this.costNums = costNums;
	}

	public int[] getProbabblys() {
		return probabblys;
	}

	public void setProbabblys(int[] probabblys) {
		this.probabblys = probabblys;
	}

	public int[] getMagicWeaponLvs() {
		return magicWeaponLvs;
	}

	public void setMagicWeaponLvs(int[] magicWeaponLvs) {
		this.magicWeaponLvs = magicWeaponLvs;
	}
	
}
