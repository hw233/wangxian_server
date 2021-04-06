package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 精准
 * 
 *
 */
@SimpleEmbeddable
public class Buff_JingZhunPercent extends Buff{

	int accurateC = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_JingZhunPercent bt = (BuffTemplate_JingZhunPercent)this.getTemplate();
			if(bt.accurateC != null && bt.accurateC.length > this.getLevel()){
				accurateC = bt.accurateC[getLevel()];
			}

			p.setAccurateC((p.getAccurateC() + accurateC));

			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_JingZhunPercent bt = (BuffTemplate_JingZhunPercent)this.getTemplate();
			if(bt.accurateC != null && bt.accurateC.length > this.getLevel()){
				accurateC = bt.accurateC[getLevel()];
			}
			s.setAccurateC((s.getAccurateC() + accurateC));
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setAccurateC((p.getAccurateC() - accurateC));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setAccurateC((s.getAccurateC() - accurateC));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
