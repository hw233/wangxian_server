package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_LanShangXian extends Buff {
	int totalMP = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_LanShangXian bt = (BuffTemplate_LanShangXian)this.getTemplate();
			if(bt.totalMP != null && bt.totalMP.length > getLevel()){
				totalMP = bt.totalMP[getLevel()];
			}
			p.setMaxMPB((p.getMaxMPB() + totalMP));
			
		}else if(owner instanceof Sprite){
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setMaxMPB((p.getMaxMPB() - totalMP));
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
