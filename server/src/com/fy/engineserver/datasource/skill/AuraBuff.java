package com.fy.engineserver.datasource.skill;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.sprite.Fighter;

/**
 * 光环BUFF
 * 
 * 光环Buff主要处理脱离规则：
 * 脱离规则包括：
 *      对应的光环技能关闭
 *      对应的光环技能升级
 *      对应的光环技能的玩家下线，离开地图
 *      离开范围
 *      敌我双方发生改变
 * 
 * 
 * 
 *
 */
public class AuraBuff extends Buff{

	transient Buff buff;
	transient AuraSkillAgent agent;
	transient AuraSkill skill;
	int level;
	int skillLevel;
	
	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public AuraBuff(AuraSkillAgent agent,Buff buff){
		
		if(buff instanceof AuraBuff){
			throw new java.lang.IllegalArgumentException("光环Buff内部不能嵌套光环Buff");
		}
		this.agent = agent;
		this.skill = agent.getAuraSkill();
		this.level = buff.getLevel();
		this.buff = buff;
		this.setInvalidTime(buff.getInvalidTime());
		this.setAdvantageous(buff.isAdvantageous());
		this.setSyncWithClient(true);
		this.setForover(true);
	}
	
	public String getTemplateName() {
		return buff.getTemplateName();
	}
	
	public byte getBufferType() {
		return buff.getBufferType();
	}

	public String getDescription() {
		return buff.getDescription();
	}

	/**
	 * 级别，同一类型的Buff，高级别的Buff将替代低级别的Buff
     * 替代的时候，低级别的buff的end方法将会被调用
	 * @return
	 */
	public int getLevel(){
		return level;
	}
	
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		buff.start(owner);
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		buff.end(owner);
	}
	
	/**
	 * 心跳函数，此心跳函数每1秒钟跳一次
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		
		buff.heartbeat(owner, heartBeatStartTime, interval, game);
		
		if(agent.getOwner() == null){
			this.setInvalidTime(heartBeatStartTime);
		}else if(!game.contains((LivingObject)agent.getOwner())){
			this.setInvalidTime(heartBeatStartTime);
		}else if(agent.getOwner().isDeath()){
			this.setInvalidTime(heartBeatStartTime);
		}else if(agent.getAuraSkill() == null){
			this.setInvalidTime(heartBeatStartTime);
		}else if(agent.getAuraSkill() != this.skill){
			this.setInvalidTime(heartBeatStartTime);
		}else if(agent.getSkillLevel() != this.getSkillLevel()){
			this.setInvalidTime(heartBeatStartTime);
		}else{
			/**
			 * 范围的类型：<br>
			 * 0 范围内的所有的队友，包括自己<br>
			 * 1 范围内的所有的队友，中立方，和自己
			 * 2 范围内的所有的中立方
			 * 3 范围内的所有的敌方
			 * 4 范围内的所有的中立方，敌方
			 */
			int ft = agent.getOwner().getFightingType(owner);
			if(skill.getRangeType() == 0){
				if(ft != Fighter.FIGHTING_TYPE_FRIEND){
					this.setInvalidTime(heartBeatStartTime);
				}
			}else if(skill.getRangeType() == 1){
				if(ft != Fighter.FIGHTING_TYPE_FRIEND && ft != Fighter.FIGHTING_TYPE_NEUTRAL){
					this.setInvalidTime(heartBeatStartTime);
				}
			}else if(skill.getRangeType() == 2){
				if(ft != Fighter.FIGHTING_TYPE_NEUTRAL){
					this.setInvalidTime(heartBeatStartTime);
				}
			}else if(skill.getRangeType() == 3){
				if(ft != Fighter.FIGHTING_TYPE_ENEMY){
					this.setInvalidTime(heartBeatStartTime);
				}
			}else if(skill.getRangeType() == 4){
				if(ft != Fighter.FIGHTING_TYPE_ENEMY && ft != Fighter.FIGHTING_TYPE_NEUTRAL){
					this.setInvalidTime(heartBeatStartTime);
				}
			}
			
			if(this.getInvalidTime() > heartBeatStartTime){//
				//检查范围
				float dx = owner.getX() - agent.getOwner().getX();
				float dy = owner.getY() - agent.getOwner().getY();
				if(dx < - skill.rangeWidth/2 || dx > skill.rangeWidth/2 || dy < -skill.rangeHeight/2 || dy > skill.rangeHeight/2){
					this.setInvalidTime(heartBeatStartTime);
				}
			}
		}
	}
	
}
