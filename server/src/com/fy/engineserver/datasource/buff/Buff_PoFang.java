package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 破防
 * 
 */
@SimpleEmbeddable
public class Buff_PoFang extends Buff{

	int breakDefence = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_PoFang bt = (BuffTemplate_PoFang)this.getTemplate();
			if(bt.breakDefence != null && bt.breakDefence.length > this.getLevel()){
				breakDefence = bt.breakDefence[getLevel()];
			}

			p.setBreakDefenceB((p.getBreakDefenceB() + breakDefence));

			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_PoFang bt = (BuffTemplate_PoFang)this.getTemplate();
			if(bt.breakDefence != null && bt.breakDefence.length > this.getLevel()){
				breakDefence = bt.breakDefence[getLevel()];
			}
			s.setBreakDefenceB((s.getBreakDefenceB() + breakDefence));
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setBreakDefenceB((p.getBreakDefenceB() - breakDefence));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setBreakDefenceB((s.getBreakDefenceB() - breakDefence));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
