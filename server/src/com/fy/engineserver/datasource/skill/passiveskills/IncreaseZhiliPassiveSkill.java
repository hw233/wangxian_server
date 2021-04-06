package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

/**
 * 增加智力被动技能
 * 
 * @author Administrator
 *
 */
public class IncreaseZhiliPassiveSkill extends PassiveSkill {
	short[] spellpower;
	
	public void levelUp(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}
	
	public short[] getSpellpower() {
		return spellpower;
	}

	public void setSpellpower(short[] spellpower) {
		this.spellpower = spellpower;
	}

	public void run(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel;
		
		int spellpowerChange = this.spellpower[currentLevel - 1];
		
		player.setSpellpowerC((short)(player.getSpellpowerC() + spellpowerChange));
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		super.close(player, skillLevel);
		int currentLevel = skillLevel;
		
		int spellpowerChange = this.spellpower[currentLevel - 1];
		
		player.setSpellpowerC((short)(player.getSpellpowerC() - spellpowerChange));
	}

	public String getDescription(int level){
		if(level < 1 || level > spellpower.length){
			return Translate.text_3923;
		}else{
			return Translate.text_3924+spellpower[level-1]+"%";
		}
	}
}
