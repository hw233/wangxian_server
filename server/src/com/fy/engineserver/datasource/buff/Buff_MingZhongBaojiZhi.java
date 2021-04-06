package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 命中和暴击值
 * 
 *
 */
@SimpleEmbeddable
public class Buff_MingZhongBaojiZhi extends Buff{

	int attackRating = 0;
	int criticalHit = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_MingZhongBaojiZhi bt = (BuffTemplate_MingZhongBaojiZhi)this.getTemplate();
			if(bt.attackRatingB != null && bt.attackRatingB.length > this.getLevel()){
				attackRating = bt.attackRatingB[getLevel()];
			}
			p.setHitB((p.getHitB() + attackRating));
			
			if(bt.criticalHitB != null && bt.criticalHitB.length > getLevel()){
				criticalHit = bt.criticalHitB[getLevel()];
			}
			p.setCriticalHitB((p.getCriticalHitB() + criticalHit));

		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			BuffTemplate_MingZhongBaojiZhi bt = (BuffTemplate_MingZhongBaojiZhi)this.getTemplate();
			if(bt.attackRatingB != null && bt.attackRatingB.length > this.getLevel()){
				attackRating = bt.attackRatingB[getLevel()];
			}
			s.setHitB((s.getHitB() + attackRating));

			if(bt.criticalHitB != null && bt.criticalHitB.length > getLevel()){
				criticalHit = bt.criticalHitB[getLevel()];
			}
			s.setCriticalHitB((s.getCriticalHitB() + criticalHit));
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setHitB((p.getHitB() - attackRating));
			p.setCriticalHitB((p.getCriticalHitB() - criticalHit));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setHitB((s.getHitB() - attackRating));
			s.setCriticalHitB((s.getCriticalHitB() - criticalHit));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

}
