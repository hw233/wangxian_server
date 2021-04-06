package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 加血
 *
 */
@SimpleEmbeddable
public class Buff_JiaXieBase extends Buff{

	int hpRecoverBase = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_JiaXieBase bt = (BuffTemplate_JiaXieBase)this.getTemplate();
				if(bt.hpRecoverBase != null && bt.hpRecoverBase.length > getLevel()){
					hpRecoverBase = bt.hpRecoverBase[getLevel()];
				}
				p.setHpRecoverBaseB((p.getHpRecoverBaseB() + hpRecoverBase));
//				p.setCure(true);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			
			BuffTemplate_JiaXieBase bt = (BuffTemplate_JiaXieBase)this.getTemplate();
				if(bt.hpRecoverBase != null && bt.hpRecoverBase.length > getLevel()){
					hpRecoverBase = bt.hpRecoverBase[getLevel()];
				}
				p.setHpRecoverBaseB((p.getHpRecoverBaseB() + hpRecoverBase));
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setHpRecoverBaseB((p.getHpRecoverBaseB() - hpRecoverBase));
//			p.setCure(false);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setHpRecoverBaseB((p.getHpRecoverBaseB() - hpRecoverBase));
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
