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
public class Buff_JingZhun extends Buff{

	int accurate = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_JingZhun bt = (BuffTemplate_JingZhun)this.getTemplate();
			if(bt.accurate != null && bt.accurate.length > this.getLevel()){
				accurate = bt.accurate[getLevel()];
			}

			p.setAccurateB((p.getAccurateB() + accurate));

			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_JingZhun bt = (BuffTemplate_JingZhun)this.getTemplate();
			if(bt.accurate != null && bt.accurate.length > this.getLevel()){
				accurate = bt.accurate[getLevel()];
			}
			s.setAccurateB((s.getAccurateB() + accurate));
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setAccurateB((p.getAccurateB() - accurate));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setAccurateB((s.getAccurateB() - accurate));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
