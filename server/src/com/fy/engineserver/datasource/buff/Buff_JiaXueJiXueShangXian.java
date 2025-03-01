package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_JiaXueJiXueShangXian extends Buff {
	int totalHP = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_JiaXueJiXueShangXian bt = (BuffTemplate_JiaXueJiXueShangXian)this.getTemplate();
			if(bt.totalHP != null && bt.totalHP.length > getLevel()){
				totalHP = bt.totalHP[getLevel()];
			}
			p.setMaxHPB((p.getMaxHPB() + totalHP));
			p.setHp(p.getHp() + totalHP);
			// 加血，通知客户端
			NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
					.getId(), (byte) Event.HP_INCREASED, totalHP);
			p.addMessageToRightBag(req);
			
			//通知施放这个buff的人
			if(p != this.getCauser()){
				if(getCauser() instanceof Player){
					Player p2 = (Player)getCauser();
					NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
							.getId(), (byte) Event.HP_INCREASED, totalHP);
					p2.addMessageToRightBag(req2);
				}
			}
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			
			BuffTemplate_JiaXueJiXueShangXian bt = (BuffTemplate_JiaXueJiXueShangXian)this.getTemplate();
			if(bt.totalHP != null && bt.totalHP.length > getLevel()){
				totalHP = bt.totalHP[getLevel()];
			}
			p.setMaxHPB((p.getMaxHPB() + totalHP));
			p.setHp(p.getHp() + totalHP);
			if(getCauser() instanceof Player){
				Player p2 = (Player)getCauser();
				NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
						.getId(), (byte) Event.HP_INCREASED, totalHP);
				p2.addMessageToRightBag(req2);
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setMaxHPB((p.getMaxHPB() - totalHP));
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setMaxHPB((p.getMaxHPB() - totalHP));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}
}
