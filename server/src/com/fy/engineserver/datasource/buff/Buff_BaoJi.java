package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 增加暴击
 *
 */
@SimpleEmbeddable
public class Buff_BaoJi extends Buff{

	public int criticalHit = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			calcValue();
				p.setCriticalHitRateOther((p.getCriticalHitRateOther() + criticalHit));
			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			calcValue();
				s.setCriticalHitRateOther((s.getCriticalHitRateOther() + criticalHit));
			
		}
	}


	public void calcValue() {
		if(criticalHit>0){//阻止二次计算
			return;
		}
		BuffTemplate_BaoJi bt = (BuffTemplate_BaoJi)this.getTemplate();
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
			p.setCriticalHitRateOther((p.getCriticalHitRateOther() - criticalHit));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
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
