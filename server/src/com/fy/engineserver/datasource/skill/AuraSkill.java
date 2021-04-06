package com.fy.engineserver.datasource.skill;

public class AuraSkill  extends Skill{

	/**
	 * 作用范围，以自身为中心的一个矩形
	 * 
	 * 矩形的宽度
	 */
	protected int rangeWidth;
	
	/**
	 * 作用范围，以自身为中心的一个矩形
	 * 
	 * 矩形的高度
	 */
	protected int rangeHeight;
	
	/**
	 * 范围的类型：<br>
	 * 0 范围内的所有的队友，包括自己<br>
	 * 1 范围内的所有的队友，中立方，和自己
	 * 2 范围内的所有的中立方
	 * 3 范围内的所有的敌方
	 * 4 范围内的所有的中立方，敌方
	 */
	protected byte rangeType;
	
	/**
	 * 光环类型，此数值表示玩家脚下的光环
	 * 此值与Mapping编辑器中的下标对应，从0开始。
	 */
	protected byte auraType;
	
	
	/**
	 * 每一个级别的技能，使用的Buff的名称，
	 * 此名称必须索引到一个存在的Buff
	 */
	protected String buffName;
	
	/**
	 * 每一个级别的技能，通过buffName确定要使用的Buff后，
	 * 指定这个buff的级别，用于表示buff的威力
	 */
	protected int buffLevel[];

	
	/**
	 * 返回技能的类型
	 * @return
	 */
	public byte getSkillType(){
		return Skill.SKILL_AURA;
	}
	
	public int getRangeWidth() {
		return rangeWidth;
	}

	public void setRangeWidth(int rangeWidth) {
		this.rangeWidth = rangeWidth;
	}

	public int getRangeHeight() {
		return rangeHeight;
	}

	public void setRangeHeight(int rangeHeight) {
		this.rangeHeight = rangeHeight;
	}

	public byte getRangeType() {
		return rangeType;
	}

	public void setRangeType(byte rangeType) {
		this.rangeType = rangeType;
	}

	public byte getAuraType() {
		return auraType;
	}

	public void setAuraType(byte auraType) {
		this.auraType = auraType;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public int[] getBuffLevel() {
		return buffLevel;
	}

	public void setBuffLevel(int[] buffLevel) {
		this.buffLevel = buffLevel;
	}
	
	
}
