package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseWuFangPassiveSkill extends PassiveSkill {
	/** 物理防御 **/
	int[] physicalDefenceB;

	public int[] getPhysicalDefenceB() {
		return physicalDefenceB;
	}

	public void setPhysicalDefenceB(int[] physicalDefenceB) {
		this.physicalDefenceB = physicalDefenceB;
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
		
		int physicalDefenceB = this.physicalDefenceB[currentLevel - 1];
		
		player.setPhyDefenceB(player.getPhyDefenceB() + physicalDefenceB);
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		
		super.close(player, skillLevel);

		int currentLevel = skillLevel;
		
		int physicalDefenceB = this.physicalDefenceB[currentLevel - 1];
		
		player.setPhyDefenceB(player.getPhyDefenceB() - physicalDefenceB);
	}

	public String getDescription(int level, Player player){
//		if(level < 1 || level > physicalDefenceB.length){
//			return "物理防御增加";
//		}else{
			return Translate.translateString(Translate.累计物理防御增加, new String[][]{{Translate.COUNT_1,getNewMagicDefB(level, player)+""}});
//		}
	}

	private int getNewMagicDefB(int level, Player player){
		int temp = 0;
		if (level > 0) {
			temp = physicalDefenceB[level-1];
		}
		int temp0 = 0;
		int temp1 = 0;
		try{
			if(this.getId() == 5100 || this.getId() == 5200){
				CareerManager cm = CareerManager.getInstance();
				Career career = cm.getCareer(player.getCareer());
				IncreaseWuFangPassiveSkill tempSkill = (IncreaseWuFangPassiveSkill) career.getSkillById(5000);
				temp0 = tempSkill.getPhysicalDefenceB()[tempSkill.getMaxLevel()-1];
				if(this.getId() == 5200) {
					IncreaseWuFangPassiveSkill tempSkill1 = (IncreaseWuFangPassiveSkill) career.getSkillById(5100);
					temp1 = tempSkill1.getPhysicalDefenceB()[tempSkill1.getMaxLevel()-1];
				}
			}
		}catch(Exception e){
			TransitRobberyManager.logger.error("物理防御：" + e);
		}
		temp = temp + temp0 + temp1;
		return temp;
	}
	public String getDescription(int level, boolean nextLevel, Player player){
		if(level < 1 || level > physicalDefenceB.length){
			return "物理防御增加";
		}else{
//			if(level == 1){
//				return Translate.translateString(Translate.物理防御增加, new String[][]{{Translate.COUNT_1,getNewMagicDefB(level, player)+""}});
//			}else{
				return Translate.translateString(Translate.物理防御增加, new String[][]{{Translate.COUNT_1,(getNewMagicDefB(level, player)-getNewMagicDefB(level-1, player))+""}});
//			}
		}
	}
}
