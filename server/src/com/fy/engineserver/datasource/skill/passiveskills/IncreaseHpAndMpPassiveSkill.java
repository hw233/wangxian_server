package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseHpAndMpPassiveSkill extends PassiveSkill {
	/** 角色的最大生命值百分比 **/
	int[] totalHpC;
	
	/** 角色的最大魔法值百分比 **/
	int[] totalMpC;

	public int[] getTotalHpC() {
		return totalHpC;
	}

	public void setTotalHpC(int[] totalHpC) {
		this.totalHpC = totalHpC;
	}

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
		if(level < 1 || level > totalHpC.length){
			return Translate.text_3884;
		}else{
			return Translate.text_3885+totalHpC[level-1]+Translate.text_3886+totalMpC[level-1]+"%";
		}
	}
}

