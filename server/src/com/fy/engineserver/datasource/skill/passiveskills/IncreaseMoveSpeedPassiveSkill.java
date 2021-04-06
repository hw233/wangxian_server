package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseMoveSpeedPassiveSkill extends PassiveSkill {
	int[] speed;

	public int[] getSpeed() {
		return speed;
	}

	public void setSpeed(int[] speed) {
		this.speed = speed;
	}

	public void levelUp(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	public void run(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		int speedC = speed[skillLevel - 1];
		player.setSpeedC((short)(player.getSpeedC() + speedC));
	}

	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		super.close(player, skillLevel);
		int speedC = speed[skillLevel - 1];
		player.setSpeedC((short)(player.getSpeedC() - speedC));
	}

	public String getDescription(int level) {
		if(level < 1 || level > speed.length){
			return Translate.text_3901;
		}else{
			return Translate.text_3902 + speed[level - 1] + " %";			
		}
	}
	
	
}
