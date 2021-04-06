package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 闪避
 * 
 *
 */
@SimpleEmbeddable
public class Buff_ShanBi extends Buff{

	int dodge = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_ShanBi bt = (BuffTemplate_ShanBi)this.getTemplate();
			if(bt.dodge != null && bt.dodge.length > this.getLevel()){
				dodge = bt.dodge[getLevel()];
			}
			p.setDodgeRateOther((p.getDodgeRateOther() + dodge));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			BuffTemplate_ShanBi bt = (BuffTemplate_ShanBi)this.getTemplate();
			if(bt.dodge != null && bt.dodge.length > this.getLevel()){
				dodge = bt.dodge[getLevel()];
			}
			s.setDodgeC((s.getDodgeC() + dodge));
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setDodgeRateOther((p.getDodgeRateOther() - dodge));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setDodgeC((s.getDodgeC() - dodge));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

}
