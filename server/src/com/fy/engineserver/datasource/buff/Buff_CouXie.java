package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.pet.Pet;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 减速
 *
 */
@SimpleEmbeddable
public class Buff_CouXie extends Buff{

	public int hpStealPercent = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			calc();
			p.setHpStealPercent((p.getHpStealPercent() + hpStealPercent));
			
		}else if(owner instanceof Pet){
			calc();
			Pet pet = (Pet)owner;
			pet.hpStealPercent += hpStealPercent;
			Skill.logger.debug("Buff_CouXie.end: 开始 {} {}",pet.getName(),hpStealPercent);
		}else if(owner instanceof Sprite){
			
		}
	}


	public void calc() {
		if(hpStealPercent>0){
			return;
		}
		BuffTemplate_ChouXie bt = (BuffTemplate_ChouXie)this.getTemplate();
			if(bt.hpStealPercent != null && bt.hpStealPercent.length > getLevel()){
				hpStealPercent = bt.hpStealPercent[getLevel()];
			}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setHpStealPercent((p.getHpStealPercent() - hpStealPercent));
		}else if(owner instanceof Pet){
			Pet pet = (Pet)owner;
			pet.hpStealPercent -= hpStealPercent;
			Skill.logger.debug("Buff_CouXie.end: 结束 {} {}",pet.getName(),hpStealPercent);
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
