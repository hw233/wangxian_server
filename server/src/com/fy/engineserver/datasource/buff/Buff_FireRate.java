package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 增加篝火经验
 *
 */
@SimpleEmbeddable
public class Buff_FireRate extends Buff{

	int rate = 0;
	
	public int getRate() {
		return rate;
	}

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			((Player) owner).setSitdownState(true);
			int state = (this.getLevel()+1)%5;
			if(state == 0){
				state = 5;
			}
			((Player) owner).setBeerState((byte)state);
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		((Player) owner).setSitdownState(false);
		((Player) owner).setBeerState((byte)0);
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		try{
			super.heartbeat(owner, heartBeatStartTime, interval, game);
			if(owner instanceof Player){
				int state = (this.getLevel()+1)%5;
				if(state == 0){
					state = 5;
				}
				((Player) owner).setBeerState((byte)state);
			}
		}catch(Exception e){
			e.printStackTrace();
			Game.logger.warn("喝酒buff,异常:"+owner.getName()+"----"+e);
		}
	}

}
