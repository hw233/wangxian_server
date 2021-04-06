package com.fy.engineserver.datasource.skill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 技能的基础抽象类，一个对象代表一种技能
 * 
 * @author Administrator
 * 
 */
public class Skill {
	public static Logger logger = LoggerFactory.getLogger(Skill.class);
	/**
	 * 伤害类型，物理伤害
	 */
	public static final int PHYSICAL = 0;

	/**
	 * 法术伤害
	 */
	public static final int SPELL = 1;

	/**
	 * 主动技能
	 */
	public static final int SKILL_ACTIVE = 0;
	/**
	 * 被动技能
	 */
	public static final int SKILL_PASSIVE = 1;
	/**
	 * 辅助技能
	 */
	public static final int SKILL_AURA = 2;

	public static String[] skillTypeDescription = { Translate.text_45, Translate.text_46, Translate.text_47 };

	public static String getSkillFailReason(int result) {
		StringBuffer sb = new StringBuffer();
		if ((result & MP_NOT_ENOUGH) != 0)
			sb.append(Translate.text_3925);
		if ((result & TARGET_TOO_FAR) != 0)
			sb.append(Translate.text_33);
		if ((result & TARGET_NOT_EXIST) != 0)
			sb.append(Translate.text_3926);
		if ((result & WEAPON_NOT_MATCH) != 0)
			sb.append(Translate.text_35);
		if ((result & TARGET_NOT_MATCH) != 0)
			sb.append(Translate.text_36);
		if ((result & FIGHTING_NOT_MATCH) != 0)
			sb.append(Translate.text_3927);
		if ((result & NUQI_NOT_ENOUGH) != 0)
			sb.append(Translate.怒气值不够);
		if ((result & DOU_NOT_ENOUGH) != 0 && sb.length() <= 0)
			sb.append(Translate.兽魁豆不够);
		if ((result & STATUS_NOT_ENOUGH) != 0 && sb.length() <= 0)
			sb.append(Translate.XX状态不能使用技能);
		if ((result & CD_NOW_ENOUGH) != 0 && sb.length() <= 0)
			sb.append(Translate.技能cd中);
		if(result == 0){
			sb.append(Translate.text_3928);
		}
		if (sb.length() > 0)
			return sb.toString();
		return Translate.text_38;
	}
	
	/**
	 * 技能执行方法的返回值
	 */
	public static final int OK = 0;
	public static final int MP_NOT_ENOUGH = 1 << 0;
	public static final int TARGET_TOO_FAR = 1 << 1;
	public static final int TARGET_NOT_EXIST = 1 << 2;
	public static final int WEAPON_NOT_MATCH = 1 << 3;
	public static final int TARGET_NOT_MATCH = 1 << 4;
	public static final int FIGHTING_NOT_MATCH = 1 << 5;
	public static final int NUQI_NOT_ENOUGH = 1 << 6;
	public static final int DOU_NOT_ENOUGH = 1 << 7;
	public static final int STATUS_NOT_ENOUGH = 1 << 8;
	public static final int CD_NOW_ENOUGH = 1 << 9;

	protected String iconId="";
	protected String name;
	protected String description = "";
	protected String shortDescription = "";
	
	//技能能达到的最大等级
	protected byte maxLevel = 1;
	
	//需要的职业发展线路点数，这是学习此技能的条件
	protected byte needCareerThreadPoints = 0;
	//这个只是系数，带入公式计算得到最终需要的钱，后面以此类推
	public int[] needMoney = new int[0];
	public int[] needYuanQi = new int[0];
	public long[] needExp = new long[0];
	
	public int[] needPoint = new int[0];
	public int[] needPlayerLevel = new int[0];
	
	protected int id;
	protected int colIndex;
	
	/**
	 * 进阶技能用，表示此技能在本职业进阶技能中的第几个。
	 */
	public int pageIndex = -1;
	
	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public byte getNeedCareerThreadPoints() {
		return needCareerThreadPoints;
	}

	public void setNeedCareerThreadPoints(byte needCareerThreadPoints) {
		this.needCareerThreadPoints = needCareerThreadPoints;
	}

	public int[] getNeedMoney() {
		return needMoney;
	}

	public void setNeedMoney(int[] needMoney) {
		this.needMoney = needMoney;
	}

	public int[] getNeedYuanQi() {
		return needYuanQi;
	}

	public void setNeedYuanQi(int[] needYuanQi) {
		this.needYuanQi = needYuanQi;
	}

	public long[] getNeedExp() {
		return needExp;
	}

	public void setNeedExp(long[] needExp) {
		this.needExp = needExp;
	}

	public int[] getNeedPoint() {
		return needPoint;
	}

	public void setNeedPoint(int[] needPoint) {
		this.needPoint = needPoint;
	}

	public int[] getNeedPlayerLevel() {
		return needPlayerLevel;
	}

	public void setNeedPlayerLevel(int[] needPlayerLevel) {
		this.needPlayerLevel = needPlayerLevel;
	}

	public Skill() {
	}

	public Skill(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getIconId() {
		return iconId;
	}

	public void setIconId(String icon) {
		this.iconId = icon;
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

	public String getDescription() {
		return description;
	}

	/**
	 * 技能的简单描述
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 技能的某个特定的等级的描述，
	 * 描述包括对人物的属性修改，各个技能需要重载此方法，
	 * 以满足客户端查看的需求。
	 * 
	 * @param level
	 * @return
	 */
	public String getDescription(int level) {
		return description;
	}
	public String getDescription(int level, Player player) {
		return description;
	}

	/**
	 * 技能的某个特定的等级的描述，
	 * 描述包括对人物的属性修改，各个技能需要重载此方法，
	 * 以满足客户端查看的需求。
	 * 
	 * @param level
	 * @return
	 */
	public String getDescription(int level, boolean nextLevel) {
		return description;
	}
	public String getDescription(int level, boolean nextLevel, Player player) {
		return description;
	}

	public byte getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(byte maxLevel) {
		this.maxLevel = maxLevel;
	}

	/**
	 * 返回技能的类型
	 * 
	 * @return
	 */
	public byte getSkillType() {
		return SKILL_ACTIVE;
	}

	public void setSkillType(byte t) {

	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	
}
