package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 
 * @date on create 2015年8月26日 下午4:47:52
 */
@SimpleEmbeddable
public class Buff_XuLuo2 extends Buff{

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
				p.setPhyDefenceRateOther((p.getPhyDefenceRateOther() - defence));
				p.setMagicDefenceRateOther((p.getMagicDefenceRateOther() - resistance));
				p.setSpellDecrease(p.getSpellDecrease());
				p.setPhysicalDecrease(p.getPhysicalDecrease());
				p.setPojiaState(true);
//				p.setWeak(true);
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);
			}else{
				calc();
				s.setPhyDefenceRateOther((s.getPhyDefenceRateOther() - defence));
				s.setMagicDefenceRateOther((s.getMagicDefenceRateOther() - resistance));
				
//				s.setWeak(true);
			}
		}
	}


	public void calc() {
		if(defence != 0 || resistance != 0){
			return;
		}
		BuffTemplate_XuRuo2 bt = (BuffTemplate_XuRuo2)this.getTemplate();
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
			p.setPhyDefenceRateOther((p.getPhyDefenceRateOther() + defence));
			p.setMagicDefenceRateOther((p.getMagicDefenceRateOther() + resistance));
			p.setSpellDecrease(p.getSpellDecrease());
			p.setPhysicalDecrease(p.getPhysicalDecrease());
//			p.setPhyDefenceC((p.getPhyDefenceC() + defence));
//			p.setMagicDefenceC((p.getMagicDefenceC() + resistance));
			
			p.setPojiaState(false);
//			p.setWeak(false);
			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setPhyDefenceRateOther((s.getPhyDefenceRateOther() - defence));
			s.setMagicDefenceRateOther((s.getMagicDefenceRateOther() - resistance));
//			s.setPhyDefenceC((s.getPhyDefenceC() + defence));
//			s.setMagicDefenceC((s.getMagicDefenceC() + resistance));
			
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
