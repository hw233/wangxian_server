package com.fy.engineserver.hunshi;

import com.fy.engineserver.util.SimpleKey;

public class SuitSkill {
	@SimpleKey
	private int id;
	private String name;
	private int type;
	private SkillType skillType;
	private int lifeRate;
	private int activeRate;
	private int affectValue;
	private int lastingTime;
	private String buffName;
	private int buffLevel;
	private String des;
	private int skillGroup;


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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public SkillType getSkillType() {
		return skillType;
	}

	public void setSkillType(SkillType skillType) {
		this.skillType = skillType;
	}

	public int getLifeRate() {
		return lifeRate;
	}

	public void setLifeRate(int lifeRate) {
		this.lifeRate = lifeRate;
	}

	public int getActiveRate() {
		return activeRate;
	}

	public void setActiveRate(int activeRate) {
		this.activeRate = activeRate;
	}

	public int getAffectValue() {
		return affectValue;
	}

	public void setAffectValue(int affectValue) {
		this.affectValue = affectValue;
	}

	public int getLastingTime() {
		return lastingTime;
	}

	public void setLastingTime(int lastingTime) {
		this.lastingTime = lastingTime;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getSkillGroup() {
		return skillGroup;
	}

	public void setSkillGroup(int skillGroup) {
		this.skillGroup = skillGroup;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public int getBuffLevel() {
		return buffLevel;
	}

	public void setBuffLevel(int buffLevel) {
		this.buffLevel = buffLevel;
	}

}
