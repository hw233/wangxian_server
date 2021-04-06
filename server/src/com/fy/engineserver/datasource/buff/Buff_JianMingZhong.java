package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 降低命中
 * 
 */
@SimpleEmbeddable
public class Buff_JianMingZhong extends Buff{

	public int attackRating = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_JianMingZhong bt = (BuffTemplate_JianMingZhong)this.getTemplate();
				calc(bt);
				p.setHitRateOther((p.getHitRateOther() - attackRating));
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_JianMingZhong bt = (BuffTemplate_JianMingZhong)this.getTemplate();
				calc(bt);
				s.setHitRateOther((s.getHitRateOther() - attackRating));
			}
		}
	}


	public void calc(BuffTemplate_JianMingZhong bt) {
		if(attackRating != 0){
			return;
		}
		if(bt.attackRating != null && bt.attackRating.length > this.getLevel()){
			attackRating = bt.attackRating[getLevel()]*10;
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setHitRateOther((p.getHitRateOther() + attackRating));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setHitRateOther((s.getHitRateOther() + attackRating));
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
