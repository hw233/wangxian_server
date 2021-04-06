package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseFaGongPassiveSkill extends PassiveSkill {
	/** 法术强度 **/
	int[] magicAttackB;

	public int[] getMagicAttackB() {
		return magicAttackB;
	}

	public void setMagicAttackB(int[] magicAttackB) {
		this.magicAttackB = magicAttackB;
	}

	@Override
	public void levelUp(Fighter fighter, final int skillLevel) {
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
		
		int magicAttackB = this.magicAttackB[currentLevel - 1];
		
		player.setMagicAttackB(player.getMagicAttackB() + magicAttackB);
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		
		super.close(player, skillLevel);

		int currentLevel = skillLevel;
		
		int magicAttackB = this.magicAttackB[currentLevel - 1];
		
		player.setMagicAttackB(player.getMagicAttackB() - magicAttackB);
	}

	public String getDescription(int level, Player player){
//		if(level < 1 || level > magicAttackB.length){
//			return "法术攻击增加";
//		}else{
			return Translate.translateString(Translate.累计法术攻击增加, new String[][]{{Translate.COUNT_1,getNewMagicDefB(level, player)+""}});
//		}
	}
	private int getNewMagicDefB(int level, Player player){
		int temp = 0;
		if (level > 0) {
			temp = magicAttackB[level-1];
		}
		int temp0 = 0;
		int temp1 = 0;
		try{
			if(this.getId() == 6100 || this.getId() == 6200){
				CareerManager cm = CareerManager.getInstance();
				Career career = cm.getCareer(player.getCareer());
				IncreaseFaGongPassiveSkill tempSkill = (IncreaseFaGongPassiveSkill) career.getSkillById(6000);
				temp0 = tempSkill.getMagicAttackB()[tempSkill.getMaxLevel()-1];
				if(this.getId() == 6200) {
					IncreaseFaGongPassiveSkill tempSkill1 = (IncreaseFaGongPassiveSkill) career.getSkillById(6100);
					temp1 = tempSkill1.getMagicAttackB()[tempSkill1.getMaxLevel()-1];
				}
			}
		}catch(Exception e){
			TransitRobberyManager.logger.info("法术攻击：" + e);
		}
		temp = temp + temp0 + temp1;
		return temp;
	}
	

	public String getDescription(int level, boolean nextLevel, Player player){
		if(level < 1 || level > magicAttackB.length){
			return "法术攻击增加";
		}else{
//			if(level == 1){
//				return Translate.translateString(Translate.法术攻击增加, new String[][]{{Translate.COUNT_1,getNewMagicDefB(level, player)+""}});
//			}else{
				return Translate.translateString(Translate.法术攻击增加, new String[][]{{Translate.COUNT_1,(getNewMagicDefB(level, player)-getNewMagicDefB(level-1, player))+""}});
//			}
		}
	}
}
