package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 命中
 * 
 *
 */
@SimpleEmbeddable
public class Buff_MingZhongZhi extends Buff{

	int attackRatingB = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_MingZhongZhi bt = (BuffTemplate_MingZhongZhi)this.getTemplate();
			if(bt.attackRatingB != null && bt.attackRatingB.length > this.getLevel()){
				attackRatingB = bt.attackRatingB[getLevel()];
			}
			p.setHitB((p.getHitB() + attackRatingB));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			BuffTemplate_MingZhongZhi bt = (BuffTemplate_MingZhongZhi)this.getTemplate();
			if(bt.attackRatingB != null && bt.attackRatingB.length > this.getLevel()){
				attackRatingB = bt.attackRatingB[getLevel()];
			}
			s.setHitB((s.getHitB() + attackRatingB));
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setHitB((p.getHitB() - attackRatingB));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setHitB((s.getHitB() - attackRatingB));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

}
