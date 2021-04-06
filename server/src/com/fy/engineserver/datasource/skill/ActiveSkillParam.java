package com.fy.engineserver.datasource.skill;

/**
 * 主动技能参数
 * 
 *
 */
public class ActiveSkillParam {

	/**
	 * 暴击率
	 */
	protected int baojiPercent;
	
	/**
	 * 技能伤害提升百分比
	 */
	protected int attackPercent;
	
	/**
	 * 减少耗蓝，数值的含义是，减少基础内力值多少百分比
	 */
	protected int mp;
	
	/**
	 * 产生buff的几率
	 */
	protected int buffProbability;
	
	/**
	 * 产生的buff持续的时间
	 */
	protected int buffLastingTime;

	public int getBaojiPercent() {
		return baojiPercent;
	}

	public void setBaojiPercent(int baojiPercent) {
		this.baojiPercent = baojiPercent;
	}

	public int getAttackPercent() {
		return attackPercent;
	}

	public void setAttackPercent(int attackPercent) {
		this.attackPercent = attackPercent;
	}

	public int getMp() {
		return mp;
	}

	public void setMp(int mpPercent) {
		this.mp= mpPercent;
	}

	public int getBuffProbability() {
		return buffProbability;
	}

	public void setBuffProbability(int buffProbability) {
		this.buffProbability = buffProbability;
	}

	public int getBuffLastingTime() {
		return buffLastingTime;
	}

	public void setBuffLastingTime(int buffLastingTime) {
		this.buffLastingTime = buffLastingTime;
	}
	
	public ActiveSkillParam newOne(){
		ActiveSkillParam p =  new ActiveSkillParam();
		p.attackPercent = attackPercent;
		p.baojiPercent = baojiPercent;
		p.buffLastingTime = buffLastingTime;
		p.buffProbability = buffProbability;
		p.mp = mp;
		return p;
	}
}
