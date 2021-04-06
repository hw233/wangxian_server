package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

/**
 * 增加耐力被动技能
 * 
 * @author Administrator
 *
 */
public class IncreaseTizhiPassiveSkill extends PassiveSkill{
	short[] constitution;
	
	public void levelUp(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	public short[] getConstitution() {
		return constitution;
	}

	public void setConstitution(short[] constitution) {
		this.constitution = constitution;
	}

	public void run(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel;
		
		int constitutionChange = this.constitution[currentLevel - 1];
		
		player.setConstitutionC((short)(player.getConstitutionC() + constitutionChange));
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		super.close(player, skillLevel);
		int currentLevel = skillLevel;
		
		int constitutionChange = this.constitution[currentLevel - 1];
		
		player.setConstitutionC((short)(player.getConstitutionC() - constitutionChange));
	}

	public String getDescription(int level){
		if(level < 1 || level > constitution.length){
			return Translate.text_3908;
		}else{
			return Translate.text_3909+constitution[level-1]+"%";
		}
	}
}
