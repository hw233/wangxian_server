package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 降低暴击
 *
 */
@SimpleEmbeddable
public class Buff_JianBaoJi extends Buff{

	public int criticalHit = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_JianBaoJi bt = (BuffTemplate_JianBaoJi)this.getTemplate();
				calc(bt);
				p.setCriticalHitRateOther((p.getCriticalHitRateOther() - criticalHit));
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_JianBaoJi bt = (BuffTemplate_JianBaoJi)this.getTemplate();
				calc(bt);
				s.setCriticalHitC((s.getCriticalHitC() - criticalHit));
			}
		}
	}


	public void calc(BuffTemplate_JianBaoJi bt) {
		if(criticalHit != 0){
			return;
		}
		if(bt.criticalHit != null && bt.criticalHit.length > getLevel()){
			criticalHit = bt.criticalHit[getLevel()];
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setCriticalHitRateOther((p.getCriticalHitRateOther() + criticalHit));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setCriticalHitC((s.getCriticalHitC() + criticalHit));
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
