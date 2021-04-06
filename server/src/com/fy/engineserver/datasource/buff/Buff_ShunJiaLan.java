package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 瞬间加蓝
 *
 */
@SimpleEmbeddable
public class Buff_ShunJiaLan extends Buff{

	int mp = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_ShunJiaLan bt = (BuffTemplate_ShunJiaLan)this.getTemplate();
			if(bt.mp != null && bt.mp.length > getLevel()){
				mp = bt.mp[getLevel()];
				mp = p.getMaxMP()*mp/100;
			}
			if((p.getMp()+mp)>p.getMaxMP()){
				p.setMp(p.getMaxMP());
			}else{
				p.setMp(p.getMp()+mp);
			}
			// 加蓝，通知客户端
			NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
					.getId(), (byte) Event.MP_INCREASED, mp);
			p.addMessageToRightBag(req);
			
			//通知施放这个buff的人
			if(p != this.getCauser()){
				if(getCauser() instanceof Player){
					Player p2 = (Player)getCauser();
					NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
							.getId(), (byte) Event.MP_INCREASED, mp);
					p2.addMessageToRightBag(req2);
				}
			}
		}else if(owner instanceof Sprite){
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){

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
