package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 命中和暴击百分比
 * 
 *
 */
@SimpleEmbeddable
public class Buff_MingZhongBaoji extends Buff{

	int attackRating = 0;
	int criticalHit = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_MingZhongBaoji bt = (BuffTemplate_MingZhongBaoji)this.getTemplate();
			if(bt.attackRating != null && bt.attackRating.length > this.getLevel()){
				attackRating = bt.attackRating[getLevel()]*10;
			}
			p.setHitRateOther((p.getHitRateOther() + attackRating));
			
			if(bt.criticalHit != null && bt.criticalHit.length > getLevel()){
				criticalHit = bt.criticalHit[getLevel()];
			}
			p.setCriticalHitC((p.getCriticalHitC() + criticalHit));

		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			BuffTemplate_MingZhongBaoji bt = (BuffTemplate_MingZhongBaoji)this.getTemplate();
			if(bt.attackRating != null && bt.attackRating.length > this.getLevel()){
				attackRating = bt.attackRating[getLevel()]*10;
			}
			s.setHitRateOther((s.getHitRateOther() + attackRating));

			if(bt.criticalHit != null && bt.criticalHit.length > getLevel()){
				criticalHit = bt.criticalHit[getLevel()];
			}
			s.setCriticalHitC((s.getCriticalHitC() + criticalHit));
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setHitRateOther((p.getHitRateOther() - attackRating));
			p.setCriticalHitC((p.getCriticalHitC() - criticalHit));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setHitRateOther((s.getHitRateOther() - attackRating));
			s.setCriticalHitC((s.getCriticalHitC() - criticalHit));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

}
