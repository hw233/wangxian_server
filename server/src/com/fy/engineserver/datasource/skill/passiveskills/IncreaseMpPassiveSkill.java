package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseMpPassiveSkill extends PassiveSkill {
	/** 角色的最大魔法值百分比 **/
	int[] totalMpC;

	public int[] getTotalMpC() {
		return totalMpC;
	}

	public void setTotalMpC(int[] totalMpC) {
		this.totalMpC = totalMpC;
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
		if(level < 1 || level > totalMpC.length){
			return Translate.text_3884;
		}else{
			return Translate.text_3903+totalMpC[level-1]+"%";
		}
	}
	
}
