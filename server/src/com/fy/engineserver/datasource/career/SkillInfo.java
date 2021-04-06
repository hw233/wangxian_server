package com.fy.engineserver.datasource.career;

import java.util.Arrays;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.SkillInfoHelper;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;

/**
 * 技能信息类，此类用于将技能的信息传递给客户端
 * 
 * 
 * 
 */
public class SkillInfo {
	protected int id;
	protected int maxLevel;
	protected int indexOfCareerThread;
	protected int needCareerThreadPoints;
	protected byte skillType;
	protected byte columnIndex;
	protected String name="";
	protected String iconId="";
	protected String description="";
	protected int[] needMoney;
	protected int[] needYuanQi;
	protected int[] needExp;
	protected long[] needLongExp;			//这个是因为不够了加长  判断以这个为准

	protected int[] needPoint;
	protected int[] needPlayerLevel;

	public void setInfo(Player player, Skill skill) {
		id = skill.getId();
		maxLevel = skill.getMaxLevel();
		needCareerThreadPoints = skill.getNeedCareerThreadPoints();
		skillType = skill.getSkillType();
		columnIndex = (byte) skill.getColIndex();
		name = skill.getName();
		if(name == null) {
			Game.logger.warn("[玩家技能数据有误name为NULL] [playerName:"+player.getName()+"] [career:"+player.getCareer()+"] [skillId:"+skill.getId()+"]");
		}
		iconId = skill.getIconId();
		description = SkillInfoHelper.generate(player, skill);
		needMoney = skill.needMoney;
		needYuanQi = skill.needYuanQi;
		needExp = new int[skill.getNeedExp().length];
		for (int i = 0; i < needExp.length; i++) {
			needExp[i] = (int)skill.getNeedExp()[i];
		}
		needLongExp = skill.getNeedExp();
		needPoint = skill.needPoint;
		needPlayerLevel = skill.needPlayerLevel;
	}

	/**
	 * 专门为宠物设置的技能info
	 * @param mw
	 * @param skill
	 */
	public void setInfo(Pet pet, Skill skill) {
		id = skill.getId();
		maxLevel = skill.getMaxLevel();
		needCareerThreadPoints = skill.getNeedCareerThreadPoints();
		skillType = skill.getSkillType();
		columnIndex = (byte) skill.getColIndex();
		name = skill.getName();
		iconId = skill.getIconId();
		Career career = CareerManager.getInstance().getCareer(pet.getCareer());
		CareerThread ct = career.getCareerThreadBySkillId(skill.getId());
		indexOfCareerThread = ct.getIndexBySkillId(skill.getId());
		description = "";// SkillInfoHelper.generate(player, skill);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getIndexOfCareerThread() {
		return indexOfCareerThread;
	}

	public void setIndexOfCareerThread(int indexOfCareerThread) {
		this.indexOfCareerThread = indexOfCareerThread;
	}

	public int getNeedCareerThreadPoints() {
		return needCareerThreadPoints;
	}

	public void setNeedCareerThreadPoints(int needCareerThreadPoints) {
		this.needCareerThreadPoints = needCareerThreadPoints;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconId() {
		return iconId;
	}

	public void setIconId(String icon) {
		this.iconId = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getSkillType() {
		return skillType;
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

	public int[] getNeedExp() {
		return needExp;
	}

	public void setNeedExp(int[] needExp) {
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

	public void setSkillType(byte skillType) {
		this.skillType = skillType;
	}

	public byte getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(byte columnIndex) {
		this.columnIndex = columnIndex;
	}

	public long[] getNeedLongExp() {
		return needLongExp;
	}

	public void setNeedLongExp(long[] needLongExp) {
		this.needLongExp = needLongExp;
	}

	
}
