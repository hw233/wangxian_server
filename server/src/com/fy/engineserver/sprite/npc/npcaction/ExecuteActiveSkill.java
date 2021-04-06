package com.fy.engineserver.sprite.npc.npcaction;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.npc.FightableNPC;
import com.fy.engineserver.sprite.npc.NPC;

/**
 * 释放一个主动技能
 * 
 * 
 *
 */
public class ExecuteActiveSkill implements NpcAction, Cloneable{

//	static Logger logger = Logger.getLogger(NpcAction.class);
public	static Logger logger = LoggerFactory.getLogger(NpcAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id
	public int skillId;
	//对应主动技能的等级
	public int skillLevel;
	
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

	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

	@Override
	public void doAction(Game game, NPC npc, NpcExecuteItem item,
			Fighter target, long heartBeatStartTime) {
		// TODO Auto-generated method stub
		if(!isExeAvailable(npc, item, target, heartBeatStartTime)){
			return;
		}
		CareerManager cm = CareerManager.getInstance();
		Skill skill = cm.getSkillById(skillId);
		if(skill == null){
			if(logger.isWarnEnabled())
				logger.warn("[执行NPC动作] [执行技能] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能不存在(id={})]", new Object[]{game.gi.getName(),npc.getName(),(target==null?"无目标":target.getName()),skillId});
		}else if((skill instanceof ActiveSkill) == false){
			if(logger.isWarnEnabled())
				logger.warn("[执行NPC动作] [执行技能] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能不是主动技能(id={})]", new Object[]{game.gi.getName(),npc.getName(),(target==null?"无目标":target.getName()),skillId});
		}else{
			ActiveSkill as = (ActiveSkill)skill;
			if(skillLevel <= 0 || skillLevel > as.getMaxLevel()){
				if(logger.isWarnEnabled())
					logger.warn("[执行NPC动作] [执行技能] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能{}等级配置错误(lv={})]", new Object[]{game.gi.getName(),npc.getName(),(target==null?"无目标":target.getName()),as.getName(),skillLevel});
			}else{
				if(npc instanceof FightableNPC){
					if(target != null && !((FightableNPC)npc).getActiveSkillAgent().isDuringCoolDown(skillId)){
						((FightableNPC)npc).getNPCFightingAgent().start(as, skillLevel, target);
						item.exeTimes++;
						item.lastExeTime = heartBeatStartTime;
						if(logger.isInfoEnabled()){
							logger.info("[执行NPC动作] [执行技能] [成功] [GAME:{}] [BOSS:{} {}] [TARGET:{} {} {} {}] [技能{}，等级(lv={})]", new Object[]{game.gi.getName(),npc.getName(),npc.getId(), (target==null?"无目标":target.getName()),(target==null?"无目标":target.getId()),(target==null?"无目标":target.getX()),(target==null?"无目标":target.getY()),as.getName(),skillLevel});	
						}
					}else{
						if(logger.isInfoEnabled()){
							logger.info("[执行NPC动作] [执行技能] [失败] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能{}冷却，等级(lv={})]", new Object[]{game.gi.getName(),npc.getName(),(target==null?"无目标":target.getName()),as.getName(),skillLevel});	
						}
					}
				}
			}
		}

	}

	@Override
	public boolean isExeAvailable(NPC npc, NpcExecuteItem item, Fighter target,
			long heartBeatStartTime) {
		// TODO Auto-generated method stub
		if(!item.turnOnFlag){
			item.turnOnFlag = true;
			item.turnOnTime = heartBeatStartTime;
		}
		if(item.exeTimeLimit > heartBeatStartTime - item.turnOnTime){
			if (logger.isDebugEnabled()) {
				logger.debug("[执行NPC动作] [执行技能] [失败] [刚打开没到时间] [BOSS:{} {}] [TARGET:{}] )]", new Object[]{npc.getName(),npc.getId(), (target==null?"无目标":target.getName())});
			}
			return false;
//			int d = (int)Math.sqrt( (x - target.getX())*(x - target.getX()) + (y - target.getY())*(y - target.getY()));
//			if(d >= item.minDistance && d <= item.maxDistance){
//				if( (item.hpPercent > 0 && 100 * getHp()/this.getTotalHP() <= item.hpPercent)
//						|| (item.hpPercent == 0 && getHp() == 0)){
//					
//				}
//			}
		}
		if(item.exeTimes >= item.maxExeTimes){
			if (logger.isDebugEnabled()) {
				logger.debug("[执行NPC动作] [执行技能] [失败] [超过上限] [BOSS:{} {}] [TARGET:{}] )]", new Object[]{npc.getName(),npc.getId(), (target==null?"无目标":target.getName())});
			}
			return false;
		}
		if(item.lastExeTime + item.exeTimeGap > heartBeatStartTime){
			if (logger.isDebugEnabled()) {
				logger.debug("[执行NPC动作] [执行技能] [失败] [间隔没到] [BOSS:{} {}] [TARGET:{}] item.lastExeTime:{} item.exeTimeGap:{} heartBeatStartTime:{})]", new Object[]{npc.getName(),npc.getId(), (target==null?"无目标":target.getName()), item.lastExeTime, item.exeTimeGap, heartBeatStartTime});
			}
			return false;
		}

		CareerManager cm = CareerManager.getInstance();
		Skill skill = cm.getSkillById(skillId);
		if(skill == null){
			if (logger.isDebugEnabled()) {
				logger.debug("[执行NPC动作] [执行技能] [失败] [技能不存在] [BOSS:{} {}] [TARGET:{}] )]", new Object[]{npc.getName(),npc.getId(), (target==null?"无目标":target.getName())});
			}
			return false;
		}else if((skill instanceof ActiveSkill) == false){
			if (logger.isDebugEnabled()) {
				logger.debug("[执行NPC动作] [执行技能] [失败] [不是主动技能] [BOSS:{} {}] [TARGET:{}] )]", new Object[]{npc.getName(),npc.getId(), (target==null?"无目标":target.getName())});
			}
			return false;
		}else{
			ActiveSkill as = (ActiveSkill)skill;
			if(skillLevel <= 0 || skillLevel > as.getMaxLevel()){
				if (logger.isDebugEnabled()) {
					logger.debug("[执行NPC动作] [执行技能] [失败] [技能等级错误] [BOSS:{} {}] [TARGET:{}] )]", new Object[]{npc.getName(),npc.getId(), (target==null?"无目标":target.getName())});
				}
				return false;
			}else{
				if(npc instanceof FightableNPC){
					if(((FightableNPC)npc).getActiveSkillAgent().isDuringCoolDown(skill.getId())){
						if (logger.isDebugEnabled()) {
							logger.debug("[执行NPC动作] [执行技能] [失败] [cd中] [BOSS:{} {}] [TARGET:{}] )]", new Object[]{npc.getName(),npc.getId(), (target==null?"无目标":target.getName())});
						}
						return false;
					}else{
						int r = as.check(npc, target, skillLevel);
						if( r== Skill.OK || r == Skill.TARGET_TOO_FAR){
							return true;
						}else{
							if (logger.isDebugEnabled()) {
								logger.debug("[执行NPC动作] [执行技能] [失败] ["+r+"] [BOSS:{} {}] [TARGET:{}] )]", new Object[]{npc.getName(),npc.getId(), (target==null?"无目标":target.getName())});
							}
							return false;
						}
					}
				}
			}
		}
		return false;
	}

	public Object clone(){
		// TODO Auto-generated method stub
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
