package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseBaoJiPassiveSkill extends PassiveSkill{
	/** 暴击率 **/
	int[] baoJi;

	@Override
	public void levelUp(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	public int[] getBaoJi() {
		return baoJi;
	}

	public void setBaoJi(int[] baoJi) {
		this.baoJi = baoJi;
	}

	@Override
	public void run(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel;
		
		int baoJi = this.baoJi[currentLevel - 1];
		
		player.setCriticalHitRateOther(player.getCriticalHitRateOther() + baoJi);
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		
		super.close(player, skillLevel);

		int currentLevel = skillLevel;
		
		int baoJi = this.baoJi[currentLevel - 1];
		
		player.setCriticalHitRateOther(player.getCriticalHitRateOther() - baoJi);
	}

	public String getDescription(int level){
		if(level < 1 || level > baoJi.length){
			return Translate.text_3868;
		}else{
			return Translate.text_3869 +baoJi[level-1]+"%";
		}
	}
}
