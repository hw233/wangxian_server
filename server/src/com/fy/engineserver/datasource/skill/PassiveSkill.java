package com.fy.engineserver.datasource.skill;

import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

/**
 * 无条件永久生效的被动技能<br>
 * 效果限于以增量方式提升Player中多个属性的数值
 * 
 * @author 
 */
public class PassiveSkill extends Skill {

	/**
	 * 返回技能的类型
	 * @return
	 */
	public byte getSkillType(){
		return Skill.SKILL_PASSIVE;
	}
	/**
	 * 调用此方法可使被动技能生效
	 * 
	 * @param player
	 * @param skillLevel
	 *            技能等级，最小是1级
	 */
	public void run(Fighter fighter, int skillLevel) {
		
	}
	
	/**
	 * 调用此方法使被动技能失效
	 * @param Player
	 * @param skillLevel
	 */
	public void close(Fighter fighter, int skillLevel) {
		
	}

	/**
	 * 技能升级时调用此方法
	 * 
	 * @param player
	 * @param skillLevel
	 *            升级后的新等级，最小是2级
	 */
	public void levelUp(Fighter fighter, int skillLevel) {
		
	}
	
	public String getDescription(int level, Player player) {
		int lev = level < 1 ? 1 : level;
		return this.getDescription(lev);
	}
	
	public String getDescription(int level, boolean nextLevel, Player player) {
		return this.getDescription(level, nextLevel);
	}

	
}
