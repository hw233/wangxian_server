package com.fy.engineserver.sprite.monster.bossactions;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.Monster;

/**
 * 检测多少范围内是否有伙伴血量小，有就进行技能释放(加血)
 *  
 * 
 *
 */
public class SearchPartnerAndHelpHim implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	//前方的范围
	int range;
	
	String partnerNames[];
	
	int hpPercent;
	
	//主动技能的Id
	int skillId;
	
	//主动技能的等级
	int skillLevel;
	
	
	

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

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

	public boolean isPartner(Monster m){
		if(partnerNames != null){
			for(int i = 0 ; i < partnerNames.length ; i++){
				if(partnerNames[i].equals(m.getName())){
					return true;
				}
			}
		}
		return false;
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
		
		int w = boss.getViewWidth();
		int h = boss.getViewHeight();
		boss.setViewWidth(range * 2);
		boss.setViewHeight(range * 2);
		ArrayList<Monster> al = new ArrayList<Monster>();
		Fighter fs[] = game.getVisbleFighter(boss, true);
		for(int i = 0 ; i < fs.length ; i++){
			if(fs[i] instanceof Monster){
				Monster m = (Monster)fs[i];
				if(isPartner(m) || m == boss){
					if( 100* m.getHp()/m.getMaxHP() <= hpPercent){
						al.add(m);
					}
				}
			}
		}
		boss.setViewWidth(w);
		boss.setViewHeight(h);
	
		Monster targetPlayer = null;
		if(al.size() > 0){
			int k = (int)(Math.random() * al.size());
			targetPlayer = al.get(k);
		}
		
		if(targetPlayer != null){
			CareerManager cm = CareerManager.getInstance();
			Skill skill = cm.getSkillById(skillId);
			if(skill == null){
//				logger.warn("[执行BOSS动作] [给同伴加血] [错误] [GAME:"+game.gi.getName()+"] [BOSS:"+boss.getName()+"] [TARGET:"+(target==null?"无目标":target.getName())+"] [技能不存在(id="+skillId+")]");
				if(logger.isWarnEnabled())
					logger.warn("[执行BOSS动作] [给同伴加血] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能不存在(id={})]", new Object[]{game.gi.getName(),boss.getName(),(target==null?"无目标":target.getName()),skillId});
			}else if((skill instanceof ActiveSkill) == false){
//				logger.warn("[执行BOSS动作] [给同伴加血] [错误] [GAME:"+game.gi.getName()+"] [BOSS:"+boss.getName()+"] [TARGET:"+(target==null?"无目标":target.getName())+"] [技能不是主动技能(id="+skillId+")]");
				if(logger.isWarnEnabled())
					logger.warn("[执行BOSS动作] [给同伴加血] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能不是主动技能(id={})]", new Object[]{game.gi.getName(),boss.getName(),(target==null?"无目标":target.getName()),skillId});
			}else{
				ActiveSkill as = (ActiveSkill)skill;
				if(skillLevel <= 0 || skillLevel > as.getMaxLevel()){
//					logger.warn("[执行BOSS动作] [给同伴加血] [错误] [GAME:"+game.gi.getName()+"] [BOSS:"+boss.getName()+"] [TARGET:"+(target==null?"无目标":target.getName())+"] [技能"+as.getName()+"等级配置错误(lv="+skillLevel+")]");
					if(logger.isWarnEnabled())
						logger.warn("[执行BOSS动作] [给同伴加血] [错误] [GAME:{}] [BOSS:{}] [TARGET:{}] [技能{}等级配置错误(lv={})]", new Object[]{game.gi.getName(),boss.getName(),(target==null?"无目标":target.getName()),as.getName(),skillLevel});
				}else{
					boss.getBossFightingAgent().start(as, skillLevel, targetPlayer);
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

	public String[] getPartnerNames() {
		return partnerNames;
	}

	public void setPartnerNames(String[] partnerNames) {
		this.partnerNames = partnerNames;
	}

	public int getHpPercent() {
		return hpPercent;
	}

	public void setHpPercent(int hpPercent) {
		this.hpPercent = hpPercent;
	}

}
