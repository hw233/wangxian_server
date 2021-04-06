package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 虚弱，降低物理防御和法术防御力
 * 
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_XuLuo extends Buff{

	public int defence = 0;
	public int resistance = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);
			}else{
				calc();
				p.setPhyDefenceC((p.getPhyDefenceC() - defence));
				p.setMagicDefenceC((p.getMagicDefenceC() - resistance));
				
				p.setPojiaState(true);
//				p.setWeak(true);
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);
			}else{
				calc();
				s.setPhyDefenceC((s.getPhyDefenceC() - defence));
				s.setMagicDefenceC((s.getMagicDefenceC() - resistance));
				
//				s.setWeak(true);
			}
		}
	}


	public void calc() {
		if(defence != 0 || resistance != 0){
			return;
		}
		BuffTemplate_XuRuo bt = (BuffTemplate_XuRuo)this.getTemplate();
		if(defence ==0 && bt.defence != null && bt.defence.length > this.getLevel()){
			defence = bt.defence[getLevel()];
		}
		if(resistance == 0 && bt.resistance != null && bt.resistance.length > this.getLevel()){
			resistance = bt.resistance[getLevel()];
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setPhyDefenceC((p.getPhyDefenceC() + defence));
			p.setMagicDefenceC((p.getMagicDefenceC() + resistance));
			
			p.setPojiaState(false);
//			p.setWeak(false);
			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setPhyDefenceC((s.getPhyDefenceC() + defence));
			s.setMagicDefenceC((s.getMagicDefenceC() + resistance));
			
//			s.setWeak(false);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}
		}
	}

}
