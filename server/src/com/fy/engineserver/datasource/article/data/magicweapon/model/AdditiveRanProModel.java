package com.fy.engineserver.datasource.article.data.magicweapon.model;
/**
 * 附加技能随机概率
 * @author Administrator
 *
 */
public class AdditiveRanProModel {
	/** 技能id */
	private int skillId;
	/** 概率 */
	private int probbly;
	
	
	
	public AdditiveRanProModel(int skillId, int probbly) {
		super();
		this.skillId = skillId;
		this.probbly = probbly;
	}
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	public int getProbbly() {
		return probbly;
	}
	public void setProbbly(int probbly) {
		this.probbly = probbly;
	}
}
