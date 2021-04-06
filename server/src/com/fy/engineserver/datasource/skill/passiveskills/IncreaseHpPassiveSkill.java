package com.fy.engineserver.datasource.skill.passiveskills;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.datasource.career.Career;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class IncreaseHpPassiveSkill extends PassiveSkill {
	/** 角色的最大生命值百分比 **/
	int[] totalHpB;
	
	public int[] getTotalHpB() {
		return totalHpB;
	}

	public void setTotalHpB(int[] totalHpB) {
		this.totalHpB = totalHpB;
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
		
		int totalHpB = this.totalHpB[currentLevel - 1];
		
		player.setMaxHPB(player.getMaxHPB() + totalHpB);
	}
	
	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		Player player = (Player)fighter;
		
		super.close(player, skillLevel);

		int currentLevel = skillLevel;
		
		int totalHpB = this.totalHpB[currentLevel - 1];
		
		player.setMaxHPB(player.getMaxHPB() - totalHpB);
	}

	public String getDescription(int level, Player player){
//		if(level < 1 || level > totalHpB.length){
//			return null;
//		}else{
			return Translate.translateString(Translate.累计增加血量上限描述, new String[][]{{Translate.COUNT_1,getNewMagicDefB(level, player)+""}});
//		}
	}
	
	private int getNewMagicDefB(int level, Player player){
		int temp = 0;
		if (level > 0) {
			temp = totalHpB[level-1];
		}
		int temp0 = 0;
		int temp1 = 0;
		try{
			if(this.getId() == 3100 || this.getId() == 3200){
				CareerManager cm = CareerManager.getInstance();
				Career career = cm.getCareer(player.getCareer());
				IncreaseHpPassiveSkill tempSkill = (IncreaseHpPassiveSkill)career.getSkillById(3000);
				temp0 = tempSkill.getTotalHpB()[tempSkill.getMaxLevel()-1];
				if(this.getId() == 3200) {
					IncreaseHpPassiveSkill tempSkill1 = (IncreaseHpPassiveSkill)career.getSkillById(3100);
					temp1 = tempSkill1.getTotalHpB()[tempSkill1.getMaxLevel()-1];
				}
			}
		}catch(Exception e){
			TransitRobberyManager.logger.error("气血上限：" + e);
		}
		temp = temp + temp0 + temp1;
		return temp;
	}

	public String getDescription(int level, boolean nextLevel, Player player){
		if(level < 1 || level > totalHpB.length){
			return null;
		}else{
//			if(level  == 1){
//				return Translate.translateString(Translate.增加血量上限描述, new String[][]{{Translate.COUNT_1,getNewMagicDefB(level, player)+""}});
//			}else{
				return Translate.translateString(Translate.增加血量上限描述, new String[][]{{Translate.COUNT_1,(getNewMagicDefB(level, player)-getNewMagicDefB(level-1, player))+""}});
//			}
			
		}
	}
}
