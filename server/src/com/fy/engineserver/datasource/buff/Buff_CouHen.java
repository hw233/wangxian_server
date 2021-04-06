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
public class Buff_CouHen extends Buff{

	int hatridRate = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_CouHen bt = (BuffTemplate_CouHen)this.getTemplate();
				if(bt.hatridRate != null && bt.hatridRate.length > getLevel()){
					hatridRate = bt.hatridRate[getLevel()];
				}
				p.setHatridRate((p.getHatridRate() + hatridRate));
			
		}else if(owner instanceof Sprite){
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setHatridRate((p.getHatridRate() - hatridRate));
		}else if(owner instanceof Sprite){
		
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
