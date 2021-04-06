package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseLiMinZhiNaiPassiveSkill extends PassiveSkill {
	int[] param;

	public int[] getParam() {
		return param;
	}

	public void setParam(int[] param) {
		this.param = param;
	}

	public void levelUp(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		int currentLevel = skillLevel - 1;
		this.close(player, currentLevel);
		this.run(player, skillLevel);
	}

	public void run(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		int paramChange = param[skillLevel - 1];
		
		player.setStrengthC((short)(player.getStrengthC() + paramChange));
		player.setDexterityC((short)(player.getDexterityC() + paramChange));
		player.setSpellpowerC((short)(player.getSpellpowerC() + paramChange));
		player.setConstitutionC((short)(player.getConstitutionC() + paramChange));
	}

	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		super.close(player, skillLevel);
		int paramChange = param[skillLevel - 1];
		
		player.setStrengthC((short)(player.getStrengthC() - paramChange));
		player.setDexterityC((short)(player.getDexterityC() - paramChange));
		player.setSpellpowerC((short)(player.getSpellpowerC() - paramChange));
		player.setConstitutionC((short)(player.getConstitutionC() - paramChange));
	}

	public String getDescription(int level) {
		if(level < 1 || level > param.length){
			return Translate.text_3893;
		}else{
			return Translate.text_3894 + param[level - 1] + " %";			
		}
	}
	
	
}
