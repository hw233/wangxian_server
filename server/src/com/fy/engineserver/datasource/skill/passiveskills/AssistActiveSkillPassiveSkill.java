package com.fy.engineserver.datasource.skill.passiveskills;

import java.util.HashMap;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillParam;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

/**
 * 
 * 辅助主动技能的被动技能
 * 
 * 
 *
 */
public class AssistActiveSkillPassiveSkill extends PassiveSkill{

	/**
	 * 技能各个等级的描述
	 */
	protected String desps[] = new String[0];
	
	/**
	 * 数组的下标为此被动技能的等级
	 * 
	 * HashMap结构为：
	 *         主动技能的名字 -->  ActiveSkillParam
	 *         
	 * 标识此技能要修改的主动技能的属性，可以修改多个主动技能        
	 */
	protected HashMap<Integer,ActiveSkillParam[]> maps = new HashMap<Integer,ActiveSkillParam[]>();
	
	/**
	 * 调用此方法可使被动技能生效
	 * 
	 * @param player
	 * @param skillLevel
	 *            技能等级，最小是1级
	 */
	@Override
	public void run(Fighter fighter, int skillLevel) {
		Player player = (Player)fighter;
		int l = skillLevel -1;
		Integer ints[] = maps.keySet().toArray(new Integer[0]);
		for(int i = 0 ; i < ints.length ; i++){
			int skillId = ints[i].intValue();
			CareerManager cm = CareerManager.getInstance(); 
			Skill skill = cm.getSkillById(skillId);
			if(skill == null){
//				Game.logger.warn("[辅助主动技能的被动技能] ["+player.getName()+"] ["+this.getName()+"] [设置的主动技能不存在] [id:"+skillId+"]");
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[辅助主动技能的被动技能] [{}] [{}] [设置的主动技能不存在] [id:{}]", new Object[]{player.getName(),this.getName(),skillId});
			}else{
				if(skill instanceof ActiveSkill){
					ActiveSkillParam[] params = maps.get(ints[i]);
					if(params != null && params.length > l){
						ActiveSkillParam q = params[l];
						ActiveSkillParam p = player.getActiveSkillParam((ActiveSkill)skill);
						if(p != null){
							p.setAttackPercent(p.getAttackPercent() + q.getAttackPercent());
							p.setBaojiPercent(p.getBaojiPercent() + q.getBaojiPercent());
							p.setBuffLastingTime(p.getBuffLastingTime() + q.getBuffLastingTime());
							p.setBuffProbability(p.getBuffProbability() + q.getBuffProbability());
							p.setMp(p.getMp() + q.getMp());
						}else{
							player.addActiveSkillParam((ActiveSkill)skill, q.newOne());
						}
						
					}
				}else{
//					Game.logger.warn("[辅助主动技能的被动技能] ["+player.getName()+"] ["+this.getName()+"] [设置的技能不是主动技能] [id:"+skillId+"]");
					if(Game.logger.isWarnEnabled())
						Game.logger.warn("[辅助主动技能的被动技能] [{}] [{}] [设置的技能不是主动技能] [id:{}]", new Object[]{player.getName(),this.getName(),skillId});
				}
			}
		}
	}

	@Override
	public void close(Fighter fighter, int skillLevel) {
		// TODO Auto-generated method stub
		super.close(fighter, skillLevel);
		Player player = (Player)fighter;
//		Game.logger.warn("[严重错误:辅助主动技能的被动技能不能被关闭，请检查是否配给了法宝！] [skill:"+this.id+"] [playerid:"+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"]");
		if(Game.logger.isWarnEnabled())
			Game.logger.warn("[严重错误:辅助主动技能的被动技能不能被关闭，请检查是否配给了法宝！] [skill:{}] [playerid:{}] [{}] [{}]", new Object[]{this.id,player.getId(),player.getName(),player.getUsername()});
	}

	/**
	 * 技能升级时调用此方法
	 * 
	 * @param player
	 * @param skillLevel
	 *            升级后的新等级，最小是2级
	 */
	@Override
	public void levelUp(Fighter fighter, int skillLevel) {
		if(skillLevel < 2) return;
		
		Player player = (Player)fighter;
		
		int l = skillLevel -1;
		Integer ints[] = maps.keySet().toArray(new Integer[0]);
		for(int i = 0 ; i < ints.length ; i++){
			int skillId = ints[i].intValue();
			CareerManager cm = CareerManager.getInstance(); 
			Skill skill = cm.getSkillById(skillId);
			if(skill == null){
//				Game.logger.warn("[辅助主动技能的被动技能] ["+player.getName()+"] ["+this.getName()+"] [设置的主动技能不存在] [id:"+skillId+"]");
				if(Game.logger.isWarnEnabled())
					Game.logger.warn("[辅助主动技能的被动技能] [{}] [{}] [设置的主动技能不存在] [id:{}]", new Object[]{player.getName(),this.getName(),skillId});
			}else{
				if(skill instanceof ActiveSkill){
					ActiveSkillParam[] params = maps.get(ints[i]);
					if(params != null && params.length > l){
						ActiveSkillParam q = params[l-1];
						ActiveSkillParam p = player.getActiveSkillParam((ActiveSkill)skill);
						if(p != null){
							p.setAttackPercent(p.getAttackPercent() - q.getAttackPercent());
							p.setBaojiPercent(p.getBaojiPercent() - q.getBaojiPercent());
							p.setBuffLastingTime(p.getBuffLastingTime() - q.getBuffLastingTime());
							p.setBuffProbability(p.getBuffProbability() - q.getBuffProbability());
							p.setMp(p.getMp() - q.getMp());
						}
						q = params[l];
						if(p != null){
							p.setAttackPercent(p.getAttackPercent() + q.getAttackPercent());
							p.setBaojiPercent(p.getBaojiPercent() + q.getBaojiPercent());
							p.setBuffLastingTime(p.getBuffLastingTime() + q.getBuffLastingTime());
							p.setBuffProbability(p.getBuffProbability() + q.getBuffProbability());
							p.setMp(p.getMp() + q.getMp());
						}else{
							player.addActiveSkillParam((ActiveSkill)skill, q.newOne());
						}
						 
					}
				}else{
//					Game.logger.warn("[辅助主动技能的被动技能] ["+player.getName()+"] ["+this.getName()+"] [设置的技能不是主动技能] [id:"+skillId+"]");
					if(Game.logger.isWarnEnabled())
						Game.logger.warn("[辅助主动技能的被动技能] [{}] [{}] [设置的技能不是主动技能] [id:{}]", new Object[]{player.getName(),this.getName(),skillId});
				}
			}
		}
		
	}
	
	
	public String getDescription(int level){
		if(level < 1 || level > desps.length){
			return "";
		}else{
			return desps[level-1];
		}
	}

	public String[] getDesps() {
		return desps;
	}

	public void setDesps(String[] desps) {
		this.desps = desps;
	}

	public HashMap<Integer, ActiveSkillParam[]> getMaps() {
		return maps;
	}


	
}
