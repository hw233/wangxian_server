package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 减速
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_ZhengShu extends Buff{

	int speed = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_ZengShu bt = (BuffTemplate_ZengShu)this.getTemplate();
				if(bt.speed != null && bt.speed.length > getLevel()){
					speed = bt.speed[getLevel()];
				}
				p.setSpeedC((p.getSpeedC() + speed));
			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_ZengShu bt = (BuffTemplate_ZengShu)this.getTemplate();
				if(bt.speed != null && bt.speed.length > getLevel()){
					speed = bt.speed[getLevel()];
				}
				s.setSpeedC((s.getSpeedC() + speed));
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setSpeedC((p.getSpeedC()- speed));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setSpeedC((s.getSpeedC() - speed));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
