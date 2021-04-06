package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 增加荣誉
 *
 */
@SimpleEmbeddable
public class Buff_HonorPercent extends Buff{

	int honorPercent = 0;
	
	public int getHonorPercent() {
		return honorPercent;
	}

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_HonorPercent bt = (BuffTemplate_HonorPercent)this.getTemplate();
			if(bt.honorPercent != null && bt.honorPercent.length > getLevel()){
				honorPercent = bt.honorPercent[getLevel()];
			}
//			p.setHonorPercent(p.getHonorPercent() + honorPercent);
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
//			p.setHonorPercent(p.getHonorPercent() - honorPercent);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
