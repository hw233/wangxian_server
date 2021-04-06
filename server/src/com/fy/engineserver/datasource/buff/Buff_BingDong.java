package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 冰冻
 * 
 *
 */
@SimpleEmbeddable
public class Buff_BingDong extends Buff{

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setIceState(true);
			p.setInvulnerable(true);
			p.setImmunity(true);
		}else if(owner instanceof Sprite){

		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setIceState(false);
			p.setInvulnerable(false);
			p.setImmunity(false);
			SkEnhanceManager.getInst().checkskill8212(p, null);
		}else if(owner instanceof Sprite){

		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(!p.isIceState()){
				p.setIceState(true);
			}
		}
	}

}
