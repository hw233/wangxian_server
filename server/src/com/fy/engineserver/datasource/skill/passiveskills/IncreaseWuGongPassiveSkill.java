package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseWuGongPassiveSkill extends PassiveSkill {
	/** 攻击强度 **/
	int[] phyAttackB;

	public int[] getPhyAttackB() {
		return phyAttackB;
	}

	public void setPhyAttackB(int[] phyAttackB) {
		this.phyAttackB = phyAttackB;
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
	public void run(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		int currentLevel = skillLevel;
		
		int phyAttackB = this.phyAttackB[currentLevel - 1];
		
		player.setPhyAttackB(player.getPhyAttackB() + phyAttackB);
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		
		super.close(player, skillLevel);

		int currentLevel = skillLevel;
		
		int phyAttackB = this.phyAttackB[currentLevel - 1];
		
		player.setPhyAttackB(player.getPhyAttackB() - phyAttackB);
	}

	public String getDescription(int level, Player player){
//		if(level < 1 || level > phyAttackB.length){
//			return "物理攻击增加";
//		}else{
			return Translate.translateString(Translate.累计外功攻击增加, new String[][]{{Translate.COUNT_1,getNewMagicDefB(level, player)+""}});
//		}
	}

	private int getNewMagicDefB(int level, Player player){
		int temp = 0;
		if (level > 0) {
			temp = phyAttackB[level-1];
		}
		int temp0 = 0;
		int temp1 = 0;
		try{
			if(this.getId() == 4100 || this.getId() == 4200){
				CareerManager cm = CareerManager.getInstance();
				Career career = cm.getCareer(player.getCareer());
				IncreaseWuGongPassiveSkill tempSkill = (IncreaseWuGongPassiveSkill) career.getSkillById(4000);
				temp0 = tempSkill.getPhyAttackB()[tempSkill.getMaxLevel()-1];
				if(this.getId() == 4200) {
					IncreaseWuGongPassiveSkill tempSkill1 = (IncreaseWuGongPassiveSkill) career.getSkillById(4100);
					temp1 = tempSkill1.getPhyAttackB()[tempSkill1.getMaxLevel()-1];
				}
			}
		}catch(Exception e){
			TransitRobberyManager.logger.error("物理攻击：" + e);
		}
		temp = temp + temp0 + temp1;
		return temp;
	}
	public String getDescription(int level, boolean nextLevel, Player player){
		if(level < 1 || level > phyAttackB.length){
			return "物理攻击增加";
		}else{
//			if(level == 1){
//				return Translate.translateString(Translate.外功攻击增加, new String[][]{{Translate.COUNT_1,getNewMagicDefB(level, player)+""}});
//			}else{
				return Translate.translateString(Translate.外功攻击增加, new String[][]{{Translate.COUNT_1,(getNewMagicDefB(level, player)-getNewMagicDefB(level-1, player))+""}});
//			}
		}
	}
}
