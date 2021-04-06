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
 */
@SimpleEmbeddable
public class Buff_ChiDun extends Buff{

	int attackRating = 0;
	int dodge = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_ChiDun bt = (BuffTemplate_ChiDun)this.getTemplate();
				if(bt.attackRating != null && bt.attackRating.length > this.getLevel()){
					attackRating = bt.attackRating[getLevel()];
				}
				if(bt.dodge != null && bt.dodge.length > this.getLevel()){
					dodge = bt.dodge[getLevel()];
				}
//				p.setAttackRatingC((p.getAttackRatingC() - attackRating));
				p.setDodgeC((p.getDodgeC() - dodge));
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_ChiDun bt = (BuffTemplate_ChiDun)this.getTemplate();
				if(bt.attackRating != null && bt.attackRating.length > this.getLevel()){
					attackRating = bt.attackRating[getLevel()];
				}
				if(bt.dodge != null && bt.dodge.length > this.getLevel()){
					dodge = bt.dodge[getLevel()];
				}
//				s.setAttackRatingC((s.getAttackRatingC() - attackRating));
				s.setDodgeC((s.getDodgeC() - dodge));
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
//			p.setAttackRatingC((p.getAttackRatingC() + attackRating));
			p.setDodgeC((p.getDodgeC() + dodge));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
//			s.setAttackRatingC((s.getAttackRatingC() + attackRating));
			s.setDodgeC((s.getDodgeC() + dodge));
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
