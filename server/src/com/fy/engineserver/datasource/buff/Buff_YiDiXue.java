package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 一滴血
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_YiDiXue extends Buff{

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(!p.isImmunity()){
				int hp = 0;
				if(p.getHp() > 1){
					hp = p.getHp() - 1;
				}
				
				if(this.getCauser() != null){
					owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_PHYSICAL);
					getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_PHYSICAL);
					Skill.logger.debug("Buff_YiDiXue.start: owner {}, getCauser {}",owner.getName(), getCauser().getName());
				}else{Skill.logger.debug("Buff_YiDiXue.start: getCauser is null");};
			}
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			if(!p.isImmunity()){
				int hp = 0;
				if(p.getHp() > 1){
					hp = p.getHp() - 1;
				}
				
				if(this.getCauser() != null){
					owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_PHYSICAL);
					getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_PHYSICAL);
					Skill.logger.debug("Buff_YiDiXue.start: owner {}, getCauser {} BB",owner.getName(), getCauser().getName());
				}else{Skill.logger.debug("Buff_YiDiXue.start: getCauser is null BB");};
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){

		}else if(owner instanceof Sprite){
		
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
