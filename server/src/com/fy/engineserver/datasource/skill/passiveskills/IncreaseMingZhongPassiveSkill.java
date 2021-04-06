package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseMingZhongPassiveSkill extends PassiveSkill {
	/** 命中率 **/
	int[] attackRatingC;

	public int[] getAttackRatingC() {
		return attackRatingC;
	}

	public void setAttackRatingC(int[] attackRatingC) {
		this.attackRatingC = attackRatingC;
	}

	@Override
	public void levelUp(Fighter fighter,final int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	@Override
	public void run(Fighter fighter,final int skillLevel) {}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {}

	public String getDescription(int level){
		if(level < 1 || level > attackRatingC.length){
			return Translate.text_3898;
		}else{
			return Translate.text_3896+attackRatingC[level-1]+"%";
		}
	}
}
