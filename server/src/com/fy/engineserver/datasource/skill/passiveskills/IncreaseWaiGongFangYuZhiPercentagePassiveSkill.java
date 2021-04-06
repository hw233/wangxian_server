package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseWaiGongFangYuZhiPercentagePassiveSkill extends
		PassiveSkill {
	int[] physicalDefenceC;

	public int[] getPhysicalDefenceC() {
		return physicalDefenceC;
	}

	public void setPhysicalDefenceC(int[] physicalDefenceC) {
		this.physicalDefenceC = physicalDefenceC;
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
	public void run(Fighter fighter, int skillLevel) {}
	
	
	@Override
	public void close(Fighter fighter, int skillLevel) {}

	public String getDescription(int level){
		if(level < 1 || level > physicalDefenceC.length){
			return Translate.text_3913;
		}else{
			return Translate.text_3914+physicalDefenceC[level-1]+"%";
		}
	}
}
