package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseMpRecoveryPassiveSkill extends PassiveSkill {
	/** 魔法恢复速度，每五秒恢复一次 **/
	int[] mpRecovery;

	public int[] getMpRecovery() {
		return mpRecovery;
	}

	public void setMpRecovery(int[] mpRecovery) {
		this.mpRecovery = mpRecovery;
	}

	@Override
	public void levelUp(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	@Override
	public void run(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel;
		
		int mp = mpRecovery[currentLevel - 1];
		
		player.setMpRecoverBaseB((short)(player.getMpRecoverBaseB() + mp));
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		super.close(player, skillLevel);
		int currentLevel = skillLevel;
		
		int mp = mpRecovery[currentLevel - 1];
		
		player.setMpRecoverBaseB((short)(player.getMpRecoverBaseB() - mp));
	}

	public String getDescription(int level){
		if(level < 1 || level > mpRecovery.length){
			return Translate.text_3904;
		}else{
			return Translate.text_3905+mpRecovery[level-1]+Translate.text_1469;
		}
	}
}
