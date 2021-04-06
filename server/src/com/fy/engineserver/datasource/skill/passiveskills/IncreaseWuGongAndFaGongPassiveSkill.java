package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseWuGongAndFaGongPassiveSkill extends PassiveSkill {
	/** 物理攻击上限百分比 **/
	int[] physicalDamageUpperLimitC;

	/** 物理攻击下限百分比 **/
	int[] physicalDamagerLowerLimitC;

	/** 道术攻击伤害力的上限百分比 **/
	int[] spellDamageUpperLimitC;

	/** 道术攻击伤害力的下限百分比 **/
	int[] spellDamageLowerLimitC;

	public int[] getPhysicalDamageUpperLimitC() {
		return physicalDamageUpperLimitC;
	}

	public void setPhysicalDamageUpperLimitC(int[] physicalDamageUpperLimitC) {
		this.physicalDamageUpperLimitC = physicalDamageUpperLimitC;
	}

	public int[] getPhysicalDamagerLowerLimitC() {
		return physicalDamagerLowerLimitC;
	}

	public void setPhysicalDamagerLowerLimitC(int[] physicalDamagerLowerLimitC) {
		this.physicalDamagerLowerLimitC = physicalDamagerLowerLimitC;
	}

	public int[] getSpellDamageUpperLimitC() {
		return spellDamageUpperLimitC;
	}

	public void setSpellDamageUpperLimitC(int[] spellDamageUpperLimitC) {
		this.spellDamageUpperLimitC = spellDamageUpperLimitC;
	}

	public int[] getSpellDamageLowerLimitC() {
		return spellDamageLowerLimitC;
	}

	public void setSpellDamageLowerLimitC(int[] spellDamageLowerLimitC) {
		this.spellDamageLowerLimitC = spellDamageLowerLimitC;
	}

	@Override
	public void levelUp(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	@Override
	public void run(Fighter fighter, int skillLevel) {}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {}

	public String getDescription(int level){
		if(level < 1 || level > physicalDamageUpperLimitC.length){
			return Translate.text_3916;
		}else{
			return Translate.text_3917 + physicalDamageUpperLimitC[level-1] + Translate.text_3918+physicalDamagerLowerLimitC[level-1]+Translate.text_3919+spellDamageUpperLimitC[level-1]+Translate.text_3920 + spellDamageLowerLimitC + " %";
		}
	}
}
