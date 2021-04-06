package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 无敌且降低命中
 * 
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_WuDiAndJiangDiMingZhong extends Buff{
	int decreaseAttackRatingPercent;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setInvulnerable(true);
			p.setImmunity(true);
			if(getLevel() >= 0 && getLevel() < ((BuffTemplate_WuDiAndJiangDiMingZhong)this.getTemplate()).attackRating.length){
				decreaseAttackRatingPercent = ((BuffTemplate_WuDiAndJiangDiMingZhong)this.getTemplate()).attackRating[getLevel()];
			}
			p.setHitRateOther(p.getHitRateOther() - decreaseAttackRatingPercent);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setInvulnerable(true);
			p.setImmunity(true);
			if(getLevel() >= 0 && getLevel() < ((BuffTemplate_WuDiAndJiangDiMingZhong)this.getTemplate()).attackRating.length){
				decreaseAttackRatingPercent = ((BuffTemplate_WuDiAndJiangDiMingZhong)this.getTemplate()).attackRating[getLevel()];
			}
			p.setHitRateOther(p.getHitRateOther() - decreaseAttackRatingPercent);
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setInvulnerable(false);
			p.setImmunity(false);
			p.setHitRateOther(p.getHitRateOther() + decreaseAttackRatingPercent);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setInvulnerable(false);
			p.setImmunity(false);
			p.setHitRateOther(p.getHitRateOther() + decreaseAttackRatingPercent);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
