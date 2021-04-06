package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 瞬间加血
 *
 */
@SimpleEmbeddable
public class Buff_ShunJiaXue extends Buff{

	int hp = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner.getHp() <= 0){
			this.setInvalidTime(0);
		}else{
			if(owner instanceof Player){
				Player p = (Player)owner;
				if(p.isCanNotIncHp()){
					Skill.logger.debug("[无法回血状态] [屏蔽Buff_ShunJiaXue] [" + p.getLogString() + "] [血]");
					return;
				}
				BuffTemplate_ShunJiaXue bt = (BuffTemplate_ShunJiaXue)this.getTemplate();
				if(bt.hp != null && bt.hp.length > getLevel()){
					hp = bt.hp[getLevel()];
					float temp = hp / 100f;
					hp = (int) (p.getMaxHP()*temp);
				}
				if((p.getHp()+hp)>p.getMaxHP()){
					p.setHp(p.getMaxHP());
				}else{
					p.setHp(p.getHp()+hp);
				}
				// 加血，通知客户端
				NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
						.getId(), (byte) Event.HP_INCREASED, hp);
				p.addMessageToRightBag(req);
				
				//通知施放这个buff的人
				if(p != this.getCauser()){
					if(getCauser() instanceof Player){
						Player p2 = (Player)getCauser();
						NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
								.getId(), (byte) Event.HP_INCREASED, hp);
						p2.addMessageToRightBag(req2);
					}
				}
			}else if(owner instanceof Sprite){
				Sprite p = (Sprite)owner;
				if(p.isCanNotIncHp()){
					Skill.logger.debug("[无法回血状态] [屏蔽Buff_ShunJiaXue] [Sprite] [血]");
					return;
				}
				BuffTemplate_ShunJiaXue bt = (BuffTemplate_ShunJiaXue)this.getTemplate();
				if(bt.hp != null && bt.hp.length > getLevel()){
					hp = bt.hp[getLevel()];
					hp = p.getMaxHP()*hp/100;
				}
				if((p.getHp()+hp)>p.getMaxHP()){
					p.setHp(p.getMaxHP());
				}else{
					p.setHp(p.getHp()+hp);
				}
				if(getCauser() instanceof Player){
					Player p2 = (Player)getCauser();
					NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
							.getId(), (byte) Event.HP_INCREASED, hp);
					p2.addMessageToRightBag(req2);
				}
			}
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
