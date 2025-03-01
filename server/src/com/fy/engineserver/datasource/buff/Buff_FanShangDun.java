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
public class Buff_FanShangDun extends Buff{
	int ironMaidenPercent;
	byte shield;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			byte shield = -1;
			BuffTemplate_FanShangDun bt = (BuffTemplate_FanShangDun)this.getTemplate();
			if(bt.ironMaidenPercent != null && bt.ironMaidenPercent.length > getLevel()){
				ironMaidenPercent = bt.ironMaidenPercent[getLevel()];
				shield = bt.getShield();
			}
			p.setIronMaidenPercent((p.getIronMaidenPercent() + ironMaidenPercent));
			p.setShield(shield);
			
		}else if(owner instanceof Sprite){
//			Sprite p = (Sprite)owner;
//			byte shield = -1;
//			
//			
//			BuffTemplate_FanShangDun bt = (BuffTemplate_FanShangDun)this.getTemplate();
//			if(bt.dmamges != null && bt.dmamges.length > getLevel()){
//				damage = bt.dmamges[getLevel()];
//				shield = bt.getShield();
//			}
//			p.setShield(shield);
//			p.setHuDunDamage(damage);
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			p.setShield(Player.SHIELD_NONE);

			p.setIronMaidenPercent((p.getIronMaidenPercent() - ironMaidenPercent));

		} else if (owner instanceof Sprite) {
//			Sprite p = (Sprite) owner;
//			p.setShield(Player.SHIELD_NONE);
//
//			p.setHuDunDamage(0);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.getShield() < 0){
				p.setShield(shield);//立即失效
			}
		}else if(owner instanceof Sprite){
//			Sprite s = (Sprite)owner;
//			if(s.getHuDunDamage() <= 0){
//				this.setInvalidTime(0);//立即失效
//			}
		}
	}

}
