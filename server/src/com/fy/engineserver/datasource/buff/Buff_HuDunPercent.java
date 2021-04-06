package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 护盾
 * 
 *
 */
@SimpleEmbeddable
public class Buff_HuDunPercent extends Buff{
	int damage_percent;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			byte shield = -1;
			BuffTemplate_HuDunPercent bt = (BuffTemplate_HuDunPercent)this.getTemplate();
			if(bt.dmamges_percent != null && bt.dmamges_percent.length > getLevel()){
				damage_percent = bt.dmamges_percent[getLevel()];
				shield = bt.getShield();
			}
			p.setShield(shield);
//			p.setHuDundamage_percent(damage_percent);
			
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			byte shield = -1;
			
			
			BuffTemplate_HuDunPercent bt = (BuffTemplate_HuDunPercent)this.getTemplate();
			if(bt.dmamges_percent != null && bt.dmamges_percent.length > getLevel()){
				damage_percent = bt.dmamges_percent[getLevel()];
				shield = bt.getShield();
			}
			p.setShield(shield);
//			p.setHuDundamage_percent(damage_percent);
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			p.setShield(Player.SHIELD_NONE);

//			p.setHuDundamage_percent(0);

		} else if (owner instanceof Sprite) {
			Sprite p = (Sprite) owner;
			p.setShield(Player.SHIELD_NONE);

//			p.setHuDundamage_percent(0);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		
		if(owner instanceof Player){
			Player p = (Player)owner;
//			if(p.getHuDundamage_percent() <= 0){
//				this.setInvalidTime(0);//立即失效
//			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
//			if(s.getHuDundamage_percent() <= 0){
//				this.setInvalidTime(0);//立即失效
//			}
		}
	}

}
