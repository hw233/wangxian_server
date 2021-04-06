package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseNeiGongFangYuZhiPercentagePassiveSkill extends
		PassiveSkill {
	int[] resistance;

	public int[] getResistance() {
		return resistance;
	}

	public void setResistance(int[] resistance) {
		this.resistance = resistance;
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
		if(level < 1 || level > resistance.length){
			return Translate.text_3906;
		}else{
			return Translate.text_3907+resistance[level-1]+"%";
		}
	}
}
