package com.fy.engineserver.datasource.article.data.magicweapon.model;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;

public class MagicWeaponBaseModel {
	/** 法宝颜色 */
	private int colorType;
	/** 法宝等级上限 */
	private int maxLevel;
	/** 名称 */
	private String name;
	/** 附加属性个数上限 */
	private int maxAdditiveAttr;
	/** 附加属性个数下限 */
	private int minAdditiveAttr;
	/** 附加属性---可以获得的技能id以及随机概率） */
	private List<AdditiveRanProModel> additiveSkillList = new ArrayList<AdditiveRanProModel>();
	/** 隐藏属性个数 */
	private int hiddenAttrNum;
	/** 神识后最多开启的隐藏属性个数 */
	private int appraisalNum;
	/** 鉴定消耗道具名 */
	private String propForAppraisal;
	/** 道具颜色 */
	private int propColorType;
	/** 消耗道具数量 */
	private int usePropNum;
	/** 消耗银子数量(单位：文) */
	private int costSiliverNum;
	
	@Override
	public String toString() {
		return "MagicWeaponBaseModel [colorType=" + colorType + ", maxLevel=" + maxLevel + ", name=" + name + ", maxAdditiveAttr=" + maxAdditiveAttr + ", minAdditiveAttr=" + minAdditiveAttr + ", additiveSkillList=" + additiveSkillList + ", hiddenAttrNum=" + hiddenAttrNum + ", appraisalNum=" + appraisalNum + ", propForAppraisal=" + propForAppraisal + ", propColorType=" + propColorType + ", usePropNum=" + usePropNum + ", costSiliverNum=" + costSiliverNum + "]";
	}

	/**
	 * 
	 * @return 1：代表具体数值 2：代表百分比
	 */
	public byte getAddType(int index) {
		byte result = -1;
		switch (index) {
		case MagicWeaponConstant.hpNum:
		case MagicWeaponConstant.magicAttNum:
		case MagicWeaponConstant.magicDefanceNum:
		case MagicWeaponConstant.physicAttNum:
		case MagicWeaponConstant.physicDefanceNum:
			result = MagicWeaponConstant.addNum;
			break;
		case MagicWeaponConstant.cirtNum:
		case MagicWeaponConstant.dodgeNum:
		case MagicWeaponConstant.hitNum: result = MagicWeaponConstant.addPercent;
			break;
		default:
			MagicWeaponManager.logger.error("[获取法宝基础属性出错，未知的属性类型] [" + index + "]");
		}
		return result;
	}

	/**
	 * 重写
	 * @param str
	 */
	public void add2AdditiveSkillList(int id, int percent) {
		AdditiveRanProModel arm = new AdditiveRanProModel(id, percent);
		additiveSkillList.add(arm);
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxAdditiveAttr() {
		return maxAdditiveAttr;
	}

	public void setMaxAdditiveAttr(int maxAdditiveAttr) {
		this.maxAdditiveAttr = maxAdditiveAttr;
	}

	public int getMinAdditiveAttr() {
		return minAdditiveAttr;
	}

	public void setMinAdditiveAttr(int minAdditiveAttr) {
		this.minAdditiveAttr = minAdditiveAttr;
	}

	public int getHiddenAttrNum() {
		return hiddenAttrNum;
	}

	public void setHiddenAttrNum(int hiddenAttrNum) {
		this.hiddenAttrNum = hiddenAttrNum;
	}

	public int getAppraisalNum() {
		return appraisalNum;
	}

	public void setAppraisalNum(int appraisalNum) {
		this.appraisalNum = appraisalNum;
	}

	public String getPropForAppraisal() {
		return propForAppraisal;
	}

	public void setPropForAppraisal(String propForAppraisal) {
		this.propForAppraisal = propForAppraisal;
	}

	public int getPropColorType() {
		return propColorType;
	}

	public void setPropColorType(int propColorType) {
		this.propColorType = propColorType;
	}

	public int getUsePropNum() {
		return usePropNum;
	}

	public void setUsePropNum(int usePropNum) {
		this.usePropNum = usePropNum;
	}

	public int getCostSiliverNum() {
		return costSiliverNum;
	}

	public void setCostSiliverNum(int costSiliverNum) {
		this.costSiliverNum = costSiliverNum;
	}

	public List<AdditiveRanProModel> getAdditiveSkillList() {
		return additiveSkillList;
	}

	public void setAdditiveSkillList(List<AdditiveRanProModel> additiveSkillList) {
		this.additiveSkillList = additiveSkillList;
	}

}
