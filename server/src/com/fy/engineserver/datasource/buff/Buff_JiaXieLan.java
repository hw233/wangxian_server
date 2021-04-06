package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 减速
 *
 */
@SimpleEmbeddable
public class Buff_JiaXieLan extends Buff{

	/**
	 * 每半秒钟恢复血多少点
	 */
	protected int hpRecoverExtend;
	
	/**
	 * 每半秒钟恢复蓝多少点
	 */
	protected int mpRecoverExtend;
	
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_JiaXieLan bt = (BuffTemplate_JiaXieLan)this.getTemplate();
			if(bt.hpRecoverExtend != null && bt.hpRecoverExtend.length > getLevel()){
				hpRecoverExtend = bt.hpRecoverExtend[getLevel()];
			}
			if(bt.mpRecoverExtend != null && bt.mpRecoverExtend.length > getLevel()){
				mpRecoverExtend = bt.mpRecoverExtend[getLevel()];
			}
			p.setHpRecoverExtend((p.getHpRecoverExtend() + hpRecoverExtend));
			p.setMpRecoverExtend((p.getMpRecoverExtend() + mpRecoverExtend));
			
//			p.setCure(true);
		}else if(owner instanceof Sprite){
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setHpRecoverExtend((p.getHpRecoverExtend() - hpRecoverExtend));
			p.setMpRecoverExtend((p.getMpRecoverExtend() - mpRecoverExtend));
			
//			p.setCure(false);
		}else if(owner instanceof Sprite){
		
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		if(owner instanceof Player){
			Player p = (Player)owner;
//			p.setCure(true);
		}
	}

}
