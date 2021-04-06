package com.fy.engineserver.datasource.skill.passiveskills;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseWuFangAndFaFangPassiveSkill extends PassiveSkill {
	protected static Logger logger = Game.logger;
	
	/** 外功伤害减伤百分比 **/
	int[] physicalDefenceC;
	
	/** 内法伤害减伤百分比 **/
	int[] resistance;
	
	public int[] getPhysicalDefenceC() {
		return physicalDefenceC;
	}

	public void setPhysicalDefenceC(int[] physicalDefenceC) {
		this.physicalDefenceC = physicalDefenceC;
	}

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
		if(level < 1 || level > physicalDefenceC.length){
			return Translate.text_3910;
		}else{
			return Translate.text_3911+physicalDefenceC[level-1]+Translate.text_3912+resistance[level-1]+"%";
		}
	}
}
