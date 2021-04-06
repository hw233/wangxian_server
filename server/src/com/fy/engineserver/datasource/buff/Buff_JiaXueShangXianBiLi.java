package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_JiaXueShangXianBiLi extends Buff {
	public int totalHPC = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			calc();
			p.setMaxHPC((p.getMaxHPC() + totalHPC));
			
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			
			calc();
			p.setMaxHPC((p.getMaxHPC() + totalHPC));
		}
	}


	public void calc() {
		if(totalHPC != 0){
			return;
		}
		BuffTemplate_JiaXueShangXianBiLi bt = (BuffTemplate_JiaXueShangXianBiLi)this.getTemplate();
		if(bt.totalHPC != null && bt.totalHPC.length > getLevel()){
			totalHPC = bt.totalHPC[getLevel()];
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setMaxHPC((p.getMaxHPC() - totalHPC));
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setMaxHPC((p.getMaxHPC() - totalHPC));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}
}
