package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 无敌
 * 
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_WuDi extends Buff{
	int decreaseDamagePercent;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setInvulnerable(true);
			p.setImmunity(true);
			decreaseDamagePercent = 50;
//			p.setSkillDamageIncreaseRate(p.getSkillDamageIncreaseRate() - decreaseDamagePercent);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setInvulnerable(true);
			p.setImmunity(true);
			decreaseDamagePercent = 50;
//			p.setSkillDamageIncreaseRate(p.getSkillDamageIncreaseRate() - decreaseDamagePercent);
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setInvulnerable(false);
			p.setImmunity(false);
//			p.setSkillDamageIncreaseRate(p.getSkillDamageIncreaseRate() + decreaseDamagePercent);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setInvulnerable(false);
			p.setImmunity(false);
//			p.setSkillDamageIncreaseRate(p.getSkillDamageIncreaseRate() + decreaseDamagePercent);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
