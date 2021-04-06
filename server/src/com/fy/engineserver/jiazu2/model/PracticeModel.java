package com.fy.engineserver.jiazu2.model;

import java.util.Arrays;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.SkillInfoHelper;
import com.fy.engineserver.jiazu2.manager.JiazuEntityManager2;

public class PracticeModel {
	/** 修炼技能id */
	private int skillId;
	/** 修炼技能名 */
	private String skillName;
	/** 可升最大等级 */
	private int maxLevel;
	/** 消耗修炼值数量 */
//	private int[] costPracticeNum;
	/** 升级需要经验 */
	private long[] exp4next;
	/** 消耗银子类型(0优先消耗工资   1只消耗银子) */
	private int[] costSiliverType;
	/** 消耗银子数量 */
//	private long[] costSiliverNum;
	/** 技能增加属性类型(血、攻击等) */
	private int addAttrType;
	/** 技能增加类型(0为增加具体数值   1为增加千分比) */
	private int addType;
	/** 每个等级对应增加数值 */
	private int[] addNum;
	/** 描述 */
	private String description;
	/** icon */
	private String icon;
	/** 升级需要武器坊等级 */
	private int[] weaponShopLevel;
	/** 升级需要防具坊等级 */
	private int[] armorShopLevel;
	
	public String getNextLevelInfoShow(int level) {
		int ll = level;
		StringBuffer sb = new StringBuffer();
		String addNum1 = "";
		if (ll < addNum.length) {
			sb.append("<f color='0xff0000'>" + Translate.下一级 + "</f>");
			if (addType == JiazuEntityManager2.增加数值) {
				addNum1 = addNum[ll] + "";
			} else {
				addNum1 = addNum[ll]/10d + "%";
			}
			sb.append(String.format(this.getDescription(), addNum1));
		} else {
			sb.append("<f color='0xff0000'>" + Translate.当前加成 + "</f>");
			if (addType == JiazuEntityManager2.增加数值) {
				addNum1 = addNum[addNum.length - 1] + "";
			} else {
				addNum1 = addNum[addNum.length - 1]/10d + "%";
			}
			sb.append(String.format(this.getDescription(), addNum1));
		}
		sb.append("\n<f color='" + SkillInfoHelper.color_des_yellow +"'>" + Translate.本尊与元神都可获得属性加成 + "</f>");
		return sb.toString();
	}
	/**
	 * 技能泡泡
	 * @return
	 */
	public String getInfoShow(int level) {
		StringBuffer sb = new StringBuffer();
		String addNum1 = "0";
		int ll = level - 1;
		if (ll < 0) {
			if (addType == JiazuEntityManager2.增加数值) {
				addNum1 = "0";
			} else {
				addNum1 = "0%";
			}
		} else if (ll < addNum.length) {
			if (addType == JiazuEntityManager2.增加数值) {
				addNum1 = addNum[ll] + "";
			} else {
				addNum1 = addNum[ll]/10d + "%";
			}
		} else {
			if (addType == JiazuEntityManager2.增加数值) {
				addNum1 = addNum[addNum.length - 1] + "";
			} else {
				addNum1 = addNum[addNum.length - 1]/10d + "%";
			}
		}
		sb.append(String.format(this.getDescription(), addNum1));
		return sb.toString();
	}
	/**
	 * 根据技能等级获取加的属性类型、数值等
	 * @param skillLevel
	 * @return [1]增加数值or千分比 [2]具体数值
	 */
	public int[] getAddNumAndTypeByLevel(int skillLevel) {
		if (skillLevel > maxLevel || skillLevel <= 0) {
			return new int[]{addType,0};
		}
		int[] result = new int[2];
		result[0] = addType;
		result[1] = addNum[skillLevel-1];
		return result;
	}
	
	public int getNoticeType() {
		if (weaponShopLevel[1] > 0) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 根据武器坊和防具坊的等级获取技能可升的最大等级
	 * @param weaponLevel
	 * @param armorLevel
	 * @return
	 */
	public int getCanLearnLevel(int weaponLevel, int armorLevel) {
		int level4weapon = 0;
		int level4armor = 0;
		for (int i=0; i<weaponShopLevel.length; i++) {
			if (weaponShopLevel[i] <= 0 || weaponLevel <= 0) {
				continue;
			}
			if (weaponLevel <= weaponShopLevel[i]) {
				level4weapon = i+1;
				break;
			}
		}
		if (weaponShopLevel[weaponShopLevel.length-1] > 0 && weaponLevel >= weaponShopLevel.length) {
			return weaponShopLevel[weaponShopLevel.length-1];
		}
		if (armorShopLevel[armorShopLevel.length-1] > 0 && armorLevel >= armorShopLevel.length) {
			return armorShopLevel[armorShopLevel.length-1];
		}
		for (int i=0; i<armorShopLevel.length; i++) {
			if (armorShopLevel[i] <= 0 || armorLevel <= 0) {
				continue;
			}
			if (armorLevel <= armorShopLevel[i]) {
				level4armor = i+1;
				break;
			}
		}
		if (weaponShopLevel[weaponShopLevel.length-1] > 0) {
			return level4weapon;
		}
		return level4armor;
	}
	
	@Override
	public String toString() {
		return "PracticeModel [skillId=" + skillId + ", skillName=" + skillName + ", maxLevel=" + maxLevel + ", costSiliverType=" + Arrays.toString(costSiliverType) + ", addAttrType=" + addAttrType + ", addType=" + addType + ", addNum=" + Arrays.toString(addNum) + ", description=" + description + ", icon=" + icon + ", weaponShopLevel=" + Arrays.toString(weaponShopLevel) + ", armorShopLevel=" + Arrays.toString(armorShopLevel) + "]";
	}
	public int[] getWeaponShopLevel() {
		return weaponShopLevel;
	}
	public void setWeaponShopLevel(int[] weaponShopLevel) {
		this.weaponShopLevel = weaponShopLevel;
	}
	public int[] getArmorShopLevel() {
		return armorShopLevel;
	}
	public void setArmorShopLevel(int[] armorShopLevel) {
		this.armorShopLevel = armorShopLevel;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	public int[] getCostSiliverType() {
		return costSiliverType;
	}
	public void setCostSiliverType(int[] costSiliverType) {
		this.costSiliverType = costSiliverType;
	}
	public int getAddAttrType() {
		return addAttrType;
	}
	public void setAddAttrType(int addAttrType) {
		this.addAttrType = addAttrType;
	}
	public int getAddType() {
		return addType;
	}
	public void setAddType(int addType) {
		this.addType = addType;
	}
	public int[] getAddNum() {
		return addNum;
	}
	public void setAddNum(int[] addNum) {
		this.addNum = addNum;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public long[] getExp4next() {
		return exp4next;
	}

	public void setExp4next(long[] exp4next) {
		this.exp4next = exp4next;
	}
	
	
}
