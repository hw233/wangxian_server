package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseHpRecoveryAndMpRecoveryPassiveSkill extends PassiveSkill {
	/** 体力恢复速度，每五秒恢复一次 **/
	int[] hpRecovery;
	
	/** 魔法恢复速度，每五秒恢复一次 **/
	int[] mpRecovery;

	public int[] getHpRecovery() {
		return hpRecovery;
	}

	public void setHpRecovery(int[] hpRecovery) {
		this.hpRecovery = hpRecovery;
	}

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
		
		int hp = hpRecovery[currentLevel - 1];
		int mp = mpRecovery[currentLevel - 1];
		
		player.setHpRecoverBaseB((short)(player.getMpRecoverBaseB() + mp));
		player.setHpRecoverBaseB((short)(player.getHpRecoverBaseB() + hp));
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		super.close(player, skillLevel);
		int currentLevel = skillLevel;
		
		int hp = hpRecovery[currentLevel - 1];
		int mp = mpRecovery[currentLevel - 1];
		
		player.setHpRecoverBaseB((short)(player.getMpRecoverBaseB() - mp));
		player.setHpRecoverBaseB((short)(player.getHpRecoverBaseB() - hp));
	}

	public String getDescription(int level){
		if(level < 1 || level > hpRecovery.length){
			return Translate.text_3887;
		}else{
			return Translate.text_3888+hpRecovery[level-1]+Translate.text_3889+mpRecovery[level-1]+Translate.text_1469;
		}
	}
	
}
