package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 虚弱，降低物理防御和法术防御力
 * 
 *
 */
@SimpleEmbeddable
public class Buff_MingJie extends Buff{

	int dexterity = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			
				BuffTemplate_MinJie bt = (BuffTemplate_MinJie)this.getTemplate();
				if(bt.dexterity != null && bt.dexterity.length > this.getLevel()){
					dexterity = bt.dexterity[getLevel()];
				}
				p.setDexterityC((p.getDexterityC() + dexterity));
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setDexterityC((p.getDexterityC() - dexterity));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
