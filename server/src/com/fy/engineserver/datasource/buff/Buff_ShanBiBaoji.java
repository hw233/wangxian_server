package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 闪避和暴击百分比
 * 
 *
 */
@SimpleEmbeddable
public class Buff_ShanBiBaoji extends Buff{

	int dodge = 0;
	int criticalHit = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_ShanBiBaoji bt = (BuffTemplate_ShanBiBaoji)this.getTemplate();
			if(bt.dodge != null && bt.dodge.length > this.getLevel()){
				dodge = bt.dodge[getLevel()];
			}
			p.setDodgeRateOther((p.getDodgeRateOther() + dodge));
			
			if(bt.criticalHit != null && bt.criticalHit.length > getLevel()){
				criticalHit = bt.criticalHit[getLevel()];
			}
			p.setCriticalHitRateOther((p.getCriticalHitRateOther() + criticalHit));

		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			BuffTemplate_ShanBiBaoji bt = (BuffTemplate_ShanBiBaoji)this.getTemplate();
			if(bt.dodge != null && bt.dodge.length > this.getLevel()){
				dodge = bt.dodge[getLevel()];
			}
			s.setDodgeRateOther((s.getDodgeRateOther() + dodge));
			
			if(bt.criticalHit != null && bt.criticalHit.length > getLevel()){
				criticalHit = bt.criticalHit[getLevel()];
			}
			s.setCriticalHitRateOther((s.getCriticalHitRateOther() + criticalHit));
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setDodgeRateOther((p.getDodgeRateOther() - dodge));
			p.setCriticalHitRateOther((p.getCriticalHitRateOther() - criticalHit));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setDodgeRateOther((s.getDodgeRateOther() - dodge));
			s.setCriticalHitRateOther((s.getCriticalHitRateOther() - criticalHit));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

}
