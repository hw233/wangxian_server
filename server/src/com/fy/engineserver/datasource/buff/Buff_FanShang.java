package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 增加仇恨比例
 *
 */
@SimpleEmbeddable
public class Buff_FanShang extends Buff{

	int ironMaidenPercent = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_FanShang bt = (BuffTemplate_FanShang)this.getTemplate();
			if(bt.ironMaidenPercent != null && bt.ironMaidenPercent.length > getLevel()){
				ironMaidenPercent = bt.ironMaidenPercent[getLevel()];
			}
			p.setIronMaidenPercent((p.getIronMaidenPercent() + ironMaidenPercent));
			
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			
			BuffTemplate_FanShang bt = (BuffTemplate_FanShang)this.getTemplate();
			if(bt.ironMaidenPercent != null && bt.ironMaidenPercent.length > getLevel()){
				ironMaidenPercent = bt.ironMaidenPercent[getLevel()];
			}
			p.setIronMaidenPercent((short)(p.getIronMaidenPercent() + ironMaidenPercent));
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setIronMaidenPercent((p.getIronMaidenPercent() - ironMaidenPercent));
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setIronMaidenPercent((short)(p.getIronMaidenPercent() - ironMaidenPercent));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
