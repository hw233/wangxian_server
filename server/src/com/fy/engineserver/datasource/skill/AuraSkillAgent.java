package com.fy.engineserver.datasource.skill;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.Monster;


/**
 * 光环技能代理，用于执行光环技能
 * 
 * 
 * 
 *
 */
public class AuraSkillAgent {

	//玩家
	private Fighter owner;
	
	//当前开启的光环技能，如果没有开启，那么此属性为null
	private AuraSkill skill;
	
	public AuraSkillAgent() {
		
	}
	@Override
	protected void finalize() throws Throwable {
		
	}
	
	public AuraSkillAgent(Fighter owner){
		this();
		this.owner = owner;
	}
	
	/**
	 * 当前开启的光环技能，如果没有开启，那么此属性为null
	 * @return
	 */
	public AuraSkill getAuraSkill(){
		return skill;
	}
	
	public Fighter getOwner() {
		return owner;
	}

	protected int getSkillLevel() {
		if(skill == null) return 0;
		if(owner instanceof Player){
			Player p = (Player)owner;
			return p.getSkillLevel(skill.getId());
		}else if(owner instanceof Monster){
			Monster s = (Monster)owner;
			return s.getSkillLevelById(skill.getId());
		}
		return 1;
	}
	
	/**
	 * 开启光环技能
	 * @param skill
	 */
	public void openAuraSkill(AuraSkill skill){
		this.skill = skill;
		
		if(skill == null){
			if(ActiveSkill.logger.isDebugEnabled()){
				ActiveSkill.logger.debug("[使用光环技能] [关闭] [{}] [--]", new Object[]{owner.getName()});
			}
		}else{
			if(ActiveSkill.logger.isDebugEnabled()){
				ActiveSkill.logger.debug("[使用光环技能] [开启] [{}] [{}] [buff:{}] [level:{}]", new Object[]{owner.getName(),skill.getName(),skill.getBuffName(),getSkillLevel()});
			}
		}
		
	}

	/**
	 * 心跳函数，此心跳函数每1秒钟跳一次
	 */
	public void heartbeat(long heartBeatStartTime, long interval, Game game){
		
		if(skill == null) return;
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		int level = getSkillLevel();
		
		if(btm == null || skill.buffName == null || skill.buffLevel == null ){
			if(ActiveSkill.logger.isWarnEnabled())
				ActiveSkill.logger.warn("执行光环技能] [失败] [{}] [{}] [buff:{}] [level:{}] [配置错误，buff或者buff级别不存在]", new Object[]{owner.getName(),skill.getName(),skill.getBuffName(),getSkillLevel()});
			openAuraSkill(null);
			return;
		}
		if(level == 0 || level > skill.buffLevel.length){
			if(ActiveSkill.logger.isWarnEnabled())
				ActiveSkill.logger.warn("执行光环技能] [失败] [{}] [{}] [buff:{}] [level:{}] [配置错误，技能级别和buff级别配置不匹配]", new Object[]{owner.getName(),skill.getName(),skill.getBuffName(),getSkillLevel()});
			openAuraSkill(null);
			return;
		}
		
		String templateName = skill.buffName;
		BuffTemplate bt = btm.getBuffTemplateByName(templateName);
		if(bt == null){
			if(ActiveSkill.logger.isWarnEnabled())
				ActiveSkill.logger.warn("执行光环技能] [失败] [{}] [{}] [buff:{}] [level:{}] [配置错误，buff不存在]", new Object[]{owner.getName(),skill.getName(),skill.getBuffName(),getSkillLevel()});
			openAuraSkill(null);
			return;
		}
		
		int templateId = bt.getId();
		
		Fighter fs[] = game.getVisbleFighter((LivingObject)owner, true);
		for(int i = 0 ; i < fs.length ; i++){
			int ft = owner.getFightingType(fs[i]);
			boolean b = false;
			/**
			 * 范围的类型：<br>
			 * 0 范围内的所有的队友，包括自己<br>
			 * 1 范围内的所有的队友，中立方，和自己
			 * 2 范围内的所有的中立方
			 * 3 范围内的所有的敌方
			 * 4 范围内的所有的中立方，敌方
			 */
			
			if(skill.getRangeType() == 0){
				if(ft == Fighter.FIGHTING_TYPE_FRIEND && owner.isSameTeam(fs[i])){
					b = true;
				}
			}else if(skill.getRangeType() == 1){
				if(ft == Fighter.FIGHTING_TYPE_FRIEND || ft == Fighter.FIGHTING_TYPE_NEUTRAL){
					b = true;
				}
			}else if(skill.getRangeType() == 2){
				if(ft == Fighter.FIGHTING_TYPE_NEUTRAL){
					b = true;
				}
			}else if(skill.getRangeType() == 3){
				if(ft == Fighter.FIGHTING_TYPE_ENEMY){
					b = true;
				}
			}else if(skill.getRangeType() == 4){
				if(ft == Fighter.FIGHTING_TYPE_ENEMY || ft == Fighter.FIGHTING_TYPE_NEUTRAL){
					b = true;
				}
			}
			
			if(b){ //满足敌我双方
				b = false;
				float dx = owner.getX() - fs[i].getX();
				float dy = owner.getY() - fs[i].getY();
				if(dx >= - skill.rangeWidth/2 && dx <= skill.rangeWidth/2 && dy >= -skill.rangeHeight/2 && dy <= skill.rangeHeight/2){
					b = true;
				}
			}
			
			if(b){ //满足范围
				Buff buff = fs[i].getBuff(templateId);
				int buffLevel = skill.buffLevel[level-1]-1;
				if(buff == null || buff.getLevel() < buffLevel){
					buff = bt.createBuff(buffLevel);
					buff.setTemplate(bt);
					buff.setGroupId(bt.getGroupId());
					buff.setLevel(buffLevel);
					buff.setInvalidTime(heartBeatStartTime + 600 * 1000L);
					buff.setStartTime(heartBeatStartTime);
					buff.setCauser(owner);
					
					AuraBuff ab = new AuraBuff(this,buff);
					ab.setCauser(owner);
					ab.setInvalidTime(heartBeatStartTime + 600 * 1000L);
					ab.setStartTime(heartBeatStartTime);
					ab.setIconId(buff.getIconId());
					ab.setLevel(buffLevel);
					ab.setSkillLevel(level);
					ab.setTemplateId(templateId);
					ab.setGroupId(bt.getGroupId());
					fs[i].placeBuff(ab);
//					HunshiManager.getInstance().dealWithInfectSkill(owner, fs[i], buff);
					
					if(ActiveSkill.logger.isWarnEnabled())
						ActiveSkill.logger.warn("执行光环技能] [成功] [{}-->{}] [{}] [buff:{}] [level:{}] [{}]", new Object[]{owner.getName(),fs[i].getName(),skill.getName(),skill.getBuffName(),getSkillLevel(),buff.getDescription()});
				}
			}
		}
	}
}
