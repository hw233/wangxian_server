package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillParam;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;


/**
 * 增加暴击
 *
 */
@SimpleEmbeddable
public class Buff_ActiveSkillParam extends Buff{

	/**
	 * 影响具体的某个技能
	 */
	protected int skillId;

	ActiveSkillParam param;
	
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player player = (Player)owner;
			
			BuffTemplate_ActiveSkillParam bt = (BuffTemplate_ActiveSkillParam)this.getTemplate();
			skillId = bt.skillId;
			param = new ActiveSkillParam();
			if(bt.attackPercent != null && bt.attackPercent.length > this.getLevel()){
				param.setAttackPercent(bt.attackPercent[this.getLevel()]);
			}
			if(bt.baojiPercent != null && bt.baojiPercent.length > this.getLevel()){
				param.setBaojiPercent(bt.baojiPercent[this.getLevel()]);
			}
			if(bt.buffLastingTime != null && bt.buffLastingTime.length > this.getLevel()){
				param.setBuffLastingTime(bt.buffLastingTime[this.getLevel()]);
			}
			if(bt.buffProbability != null && bt.buffProbability.length > this.getLevel()){
				param.setBuffProbability(bt.buffProbability[this.getLevel()]);
			}
			if(bt.mpPercent != null && bt.mpPercent.length > this.getLevel()){
				param.setMp(bt.mpPercent[this.getLevel()]);
			}
			
			Skill skill = player.getSkillById(skillId);
			if(skill != null && skill instanceof ActiveSkill){
				ActiveSkillParam p = player.getActiveSkillParam((ActiveSkill)skill);
				if(p != null){
					p.setAttackPercent(p.getAttackPercent() + param.getAttackPercent());
					p.setBaojiPercent(p.getBaojiPercent() + param.getBaojiPercent());
					p.setBuffLastingTime(p.getBuffLastingTime() + param.getBuffLastingTime());
					p.setBuffProbability(p.getBuffProbability() + param.getBuffProbability());
					p.setMp(p.getMp() + param.getMp());
				}else{
					player.addActiveSkillParam((ActiveSkill)skill, param.newOne());
				}
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player player = (Player)owner;
			Skill skill = player.getSkillById(skillId);
			if(param != null && skill != null && skill instanceof ActiveSkill){
				ActiveSkillParam p = player.getActiveSkillParam((ActiveSkill)skill);
				if(p != null){
					p.setAttackPercent(p.getAttackPercent() - param.getAttackPercent());
					p.setBaojiPercent(p.getBaojiPercent() - param.getBaojiPercent());
					p.setBuffLastingTime(p.getBuffLastingTime() - param.getBuffLastingTime());
					p.setBuffProbability(p.getBuffProbability() - param.getBuffProbability());
					p.setMp(p.getMp() - param.getMp());
				}
			}
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
