package com.fy.engineserver.sprite.monster.bossactions;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;

/**
 * 如果目标跳跑，对其释放技能
 * 
 * 
 *
 */
public class TargetRunAwayAndAttack implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id
	int skillId;
	//对应主动技能的等级
	int skillLevel;
	//此动作的描述
	String description;
	
	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
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
		}else if((skill instanceof ActiveSkill) == false){
			return false;
		}else{
			ActiveSkill as = (ActiveSkill)skill;
			if(skillLevel <= 0 || skillLevel > as.getMaxLevel()){
				return false;
			}else{
				if(as.check(boss, target, skillLevel) == Skill.OK){
					if(boss.getSkillAgent().isDuringCoolDown(skill.getId())){
						return false;
					}else{
						return true;
					}
				}else{
					return false;
				}
			}
		}
	}
	
	public void doAction(Game game, BossMonster boss,Fighter target) {
		boolean runaway = false;
		if(target instanceof Player){
			Player p = (Player)target;
			MoveTrace path = p.getMoveTrace();
			if(path != null){
				float d1 = (boss.getX() - p.getX())*(boss.getX() - p.getX()) + (boss.getY() - p.getY())*(boss.getY() - p.getY());
				float d2 = (boss.getX() - path.getStartPointX())*(boss.getX() - path.getStartPointX()) + (boss.getY() - path.getStartPointY())*(boss.getY() - path.getStartPointY());
				d1 = (int)Math.sqrt(d1);
				d2 = (int)Math.sqrt(d2);
				if(d1 > d2 + 30){
					runaway = true;
				}
			}
		}
		if(runaway){
		
			CareerManager cm = CareerManager.getInstance();
			Skill skill = cm.getSkillById(skillId);
			if(skill == null){
//				logger.warn("[执行BOSS动作] [对目标跳跑攻击] [错误] [GAME:"+game.gi.getName()+"] [BOSS:"+boss.getName()+"] [TARGET:"+(target==null?"无目标":target.getName())+"] [技能不存在(id="+skillId+")]");
				if(logger.isWarnEnabled())
					logger.warn("[执行BOSS动作] [对目标跳跑攻击] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能不存在(id={})]", new Object[]{game.gi.getName(),boss.getName(),(target==null?"无目标":target.getName()),skillId});
			}else if((skill instanceof ActiveSkill) == false){
//				logger.warn("[执行BOSS动作] [对目标跳跑攻击] [错误] [GAME:"+game.gi.getName()+"] [BOSS:"+boss.getName()+"] [TARGET:"+(target==null?"无目标":target.getName())+"] [技能不是主动技能(id="+skillId+")]");
				if(logger.isWarnEnabled())
					logger.warn("[执行BOSS动作] [对目标跳跑攻击] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能不是主动技能(id={})]", new Object[]{game.gi.getName(),boss.getName(),(target==null?"无目标":target.getName()),skillId});
			}else{
				ActiveSkill as = (ActiveSkill)skill;
				if(skillLevel <= 0 || skillLevel > as.getMaxLevel()){
//					logger.warn("[执行BOSS动作] [对目标跳跑攻击] [错误] [GAME:"+game.gi.getName()+"] [BOSS:"+boss.getName()+"] [TARGET:"+(target==null?"无目标":target.getName())+"] [技能"+as.getName()+"等级配置错误(lv="+skillLevel+")]");
					if(logger.isWarnEnabled())
						logger.warn("[执行BOSS动作] [对目标跳跑攻击] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能{}等级配置错误(lv={})]", new Object[]{game.gi.getName(),boss.getName(),(target==null?"无目标":target.getName()),as.getName(),skillLevel});
				}else{
					boss.getBossFightingAgent().start(as, skillLevel, target);
				}
			}
		}
		
		
	}

	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

}
