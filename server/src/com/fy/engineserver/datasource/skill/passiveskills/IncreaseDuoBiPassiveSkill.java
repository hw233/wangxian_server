package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseDuoBiPassiveSkill extends PassiveSkill {
	/** 闪避率 **/
	int[] dodge;

	public int[] getDodge() {
		return dodge;
	}

	public void setDodge(int[] dodge) {
		this.dodge = dodge;
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
		
		int dodge = this.dodge[currentLevel - 1];
		
		player.setDodgeRateOther(player.getDodgeRateOther() + dodge);
	}

	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		super.close(player, skillLevel);
		
		int currentLevel = skillLevel;
		
		int dodge = this.dodge[currentLevel - 1];
		
		player.setDodgeRateOther(player.getDodgeRateOther() - dodge);
	}

	public String getDescription(int level){
		if(level < 1 || level > dodge.length){
			return Translate.text_3870;
		}else{
			return Translate.text_3871 + dodge[level-1] + "%";
		}
	}
}
