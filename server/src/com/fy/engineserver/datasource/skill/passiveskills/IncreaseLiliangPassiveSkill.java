package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

/**
 * 增加力量被动技能
 * 
 * @author Administrator
 *
 */
public class IncreaseLiliangPassiveSkill extends PassiveSkill {
	int[] strength;

	public void levelUp(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	public int[] getStrength() {
		return strength;
	}

	public void setStrength(int[] strength) {
		this.strength = strength;
	}

	public void run(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel;
		
		int strengthChange = this.strength[currentLevel - 1];
		
		player.setStrengthC((short)(player.getStrengthC() + strengthChange));
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		super.close(player, skillLevel);
		int currentLevel = skillLevel;
		
		int strengthChange = this.strength[currentLevel - 1];
		
		player.setStrengthC((short)(player.getStrengthC() - strengthChange));
	}

	public String getDescription(int level){
		if(level < 1 || level > strength.length){
			return Translate.text_3891;
		}else{
			return Translate.text_3892+strength[level-1]+"%";
		}
	}
}
