package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加暴击
 * 
 * 
 *
 */
public class BuffTemplate_ActiveSkillParam extends BuffTemplate{

	/**
	 * 影响具体的某个技能
	 */
	protected int skillId;
	
	/**
	 * 暴击率
	 */
	protected int baojiPercent[];
	
	/**
	 * 技能伤害提升百分比
	 */
	protected int attackPercent[];
	
	/**
	 * 减少耗蓝，数值的含义是，减少基础内力值多少百分比
	 */
	protected int mpPercent[];
	
	/**
	 * 产生buff的几率
	 */
	protected int buffProbability[];
	
	/**
	 * 产生的buff持续的时间
	 */
	protected int buffLastingTime[];
	
	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int[] getBaojiPercent() {
		return baojiPercent;
	}

	public void setBaojiPercent(int[] baojiPercent) {
		this.baojiPercent = baojiPercent;
	}

	public int[] getAttackPercent() {
		return attackPercent;
	}

	public void setAttackPercent(int[] attackPercent) {
		this.attackPercent = attackPercent;
	}

	public int[] getMpPercent() {
		return mpPercent;
	}

	public void setMpPercent(int[] mpPercent) {
		this.mpPercent = mpPercent;
	}

	public int[] getBuffProbability() {
		return buffProbability;
	}

	public void setBuffProbability(int[] buffProbability) {
		this.buffProbability = buffProbability;
	}

	public int[] getBuffLastingTime() {
		return buffLastingTime;
	}

	public void setBuffLastingTime(int[] buffLastingTime) {
		this.buffLastingTime = buffLastingTime;
	}

	public BuffTemplate_ActiveSkillParam(){
		setName(Translate.text_3138);
		setDescription(Translate.text_3138);
	}
	
	public Buff createBuff(int level) {
		Buff_ActiveSkillParam buff = new Buff_ActiveSkillParam();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		
		
		buff.setDescription(this.getDescription());
		
		
		
		return buff;
	}

}
