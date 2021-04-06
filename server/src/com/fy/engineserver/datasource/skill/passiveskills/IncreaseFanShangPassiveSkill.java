package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseFanShangPassiveSkill extends PassiveSkill {
	int[] fanShang;

	public int[] getFanShang() {
		return fanShang;
	}

	public void setFanShang(int[] fanShang) {
		this.fanShang = fanShang;
	}

	public void levelUp(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	public void run(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		
		player.setIronMaidenPercent((short)(player.getIronMaidenPercent() + fanShang[currentLevel]));
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		super.close(player, skillLevel);
		int currentLevel = skillLevel - 1;
		
		player.setIronMaidenPercent((short)(player.getIronMaidenPercent() - fanShang[currentLevel]));
	}

	public String getDescription(int level){
		if(level < 1 || level > fanShang.length){
			return Translate.text_3882;
		}else{
			return Translate.text_3883+fanShang[level-1]+"%";
		}
	}
}
