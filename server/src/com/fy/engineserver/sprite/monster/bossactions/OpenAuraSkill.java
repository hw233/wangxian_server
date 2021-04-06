package com.fy.engineserver.sprite.monster.bossactions;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.skill.AuraSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;

/**
 * 开启一个光环
 * 
 *
 */
public class OpenAuraSkill implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id
	int skillId;
	//此动作的描述
	String description;
	
	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isExeAvailable(BossMonster boss,Fighter target){
		CareerManager cm = CareerManager.getInstance();
		Skill skill = cm.getSkillById(skillId);
		if(skill == null){
			return false;
		}else if((skill instanceof AuraSkill) == false){
			return false;
		}else{
			return true;
		}
	}
	
	public void doAction(Game game, BossMonster boss,Fighter target) {
		CareerManager cm = CareerManager.getInstance();
		Skill skill = cm.getSkillById(skillId);
		if(skill == null){
//			logger.warn("[执行BOSS动作] [开启光环] [错误] [GAME:"+game.gi.getName()+"] [BOSS:"+boss.getName()+"] [TARGET:"+(target==null?"无目标":target.getName())+"] [技能不存在(id="+skillId+")]");
			if(logger.isWarnEnabled())
				logger.warn("[执行BOSS动作] [开启光环] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能不存在(id={})]", new Object[]{game.gi.getName(),boss.getName(),(target==null?"无目标":target.getName()),skillId});
		}else if((skill instanceof AuraSkill) == false){
//			logger.warn("[执行BOSS动作] [开启光环] [错误] [GAME:"+game.gi.getName()+"] [BOSS:"+boss.getName()+"] [TARGET:"+(target==null?"无目标":target.getName())+"] [技能不是光环技能(id="+skillId+")]");
			if(logger.isWarnEnabled())
				logger.warn("[执行BOSS动作] [开启光环] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能不是光环技能(id={})]", new Object[]{game.gi.getName(),boss.getName(),(target==null?"无目标":target.getName()),skillId});
		}else{
			AuraSkill as = (AuraSkill)skill;
			boss.getAuraSkillAgent().openAuraSkill(as);
			
		}
		
	}

	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

}
