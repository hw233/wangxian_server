package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 加蓝
 *
 */
@SimpleEmbeddable
public class Buff_JiaLanBase extends Buff{

	int mpRecoverBase = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_JiaLanBase bt = (BuffTemplate_JiaLanBase)this.getTemplate();
				if(bt.mpRecoverBase != null && bt.mpRecoverBase.length > getLevel()){
					mpRecoverBase = bt.mpRecoverBase[getLevel()];
				}
				p.setMpRecoverBaseB((p.getMpRecoverBaseB() + mpRecoverBase));
			
//				p.setCure(true);
		}else if(owner instanceof Sprite){
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setMpRecoverBaseB((p.getMpRecoverBaseB() - mpRecoverBase));
			
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
