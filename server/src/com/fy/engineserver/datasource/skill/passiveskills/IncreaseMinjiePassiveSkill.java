package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

/**
 * 增加敏捷被动技能
 * 
 * @author Administrator
 *
 */
public class IncreaseMinjiePassiveSkill extends PassiveSkill {
	int[] dexterity;

	public void levelUp(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	public int[] getDexterity() {
		return dexterity;
	}

	public void setDexterity(int[] dexterity) {
		this.dexterity = dexterity;
	}

	public void run(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel;
		
		int dexterityChange = this.dexterity[currentLevel - 1];
		
		player.setDexterityC((short)(player.getDexterityC() + dexterityChange));
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		super.close(player, skillLevel);
		int currentLevel = skillLevel;
		
		int dexterityChange = this.dexterity[currentLevel - 1];
		
		player.setDexterityC((short)(player.getDexterityC() - dexterityChange));
	}

	public String getDescription(int level){
		if(level < 1 || level > dexterity.length){
			return Translate.text_3899;
		}else{
			return Translate.text_3900+dexterity[level-1]+"%";
		}
	}
}
