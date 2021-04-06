package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseFaFangPassiveSkill extends PassiveSkill {
	/** 法防 **/
	int[] magicDefB;

	public int[] getMagicDefB() {
		return magicDefB;
	}

	public void setMagicDefB(int[] magicDefB) {
		this.magicDefB = magicDefB;
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
		
		int magicDefB = this.magicDefB[currentLevel - 1];
		
		player.setMagicDefenceB(player.getMagicDefenceB() + magicDefB);
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		
		super.close(player, skillLevel);

		int currentLevel = skillLevel;
		
		int magicDefB = this.magicDefB[currentLevel - 1];
		
		player.setMagicDefenceB(player.getMagicDefenceB() - magicDefB);
	}

	public String getDescription(int level, Player player){
//		if(level < 1 || level > magicDefB.length){
//			return Translate.text_3872;
//		}else{
			int temp = getNewMagicDefB(level, player);
			return Translate.translateString(Translate.累计法术防御增加, new String[][]{{Translate.COUNT_1,temp+""}});
//		}
	}
	private int getNewMagicDefB(int level, Player player){
		int temp = 0;
		if (level > 0) {
			temp = magicDefB[level-1];
		}
		int temp0 = 0;
		int temp1 = 0;
		try{
			if(this.getId() == 7100 || this.getId() == 7200){
				CareerManager cm = CareerManager.getInstance();
				Career career = cm.getCareer(player.getCareer());
				IncreaseFaFangPassiveSkill tempSkill = (IncreaseFaFangPassiveSkill) career.getSkillById(7000);
				temp0 = tempSkill.getMagicDefB()[tempSkill.getMaxLevel()-1];
				if(this.getId() == 7200) {
					IncreaseFaFangPassiveSkill tempSkill1 = (IncreaseFaFangPassiveSkill) career.getSkillById(7100);
					temp1 = tempSkill1.getMagicDefB()[tempSkill1.getMaxLevel()-1];
				}
			}
		}catch(Exception e){
			TransitRobberyManager.logger.error("法术防御：" + e);
		}
		temp = temp + temp0 + temp1;
		return temp;
	}
	

	public String getDescription(int level, boolean nextLevel, Player player){
		if(level < 1 || level > magicDefB.length){
			return Translate.text_3872;
		}else{
//			if(level == 1){
//				return Translate.translateString(Translate.法术防御增加, new String[][]{{Translate.COUNT_1,getNewMagicDefB(level, player)+""}});
//			}else{
				return Translate.translateString(Translate.法术防御增加, new String[][]{{Translate.COUNT_1,(getNewMagicDefB(level, player)-getNewMagicDefB(level-1, player))+""}});
//			}
		}
	}
}
