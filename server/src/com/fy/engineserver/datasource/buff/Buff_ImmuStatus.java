package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 免疫某些异常状态 
 *
 */
@SimpleEmbeddable
public class Buff_ImmuStatus extends Buff{

	public void start(Fighter owner){
		if(owner instanceof Player){
			byte status = ((BuffTemplate_ImmuStatus)this.getTemplate()).getImmuType()[this.getLevel()];
			Player p = (Player)owner;
			p.setImmuType((byte) status);
		}else if(owner instanceof Sprite){
		}
	}


	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setImmuType((byte) 0);
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
