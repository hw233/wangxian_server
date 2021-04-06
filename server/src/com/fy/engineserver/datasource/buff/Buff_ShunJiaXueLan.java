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
public class Buff_ShunJiaXueLan extends Buff{

	int hp = 0;
	int mp = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_ShunJiaXueLan bt = (BuffTemplate_ShunJiaXueLan)this.getTemplate();
			if(bt.hp != null && bt.hp.length > getLevel()){
				hp = bt.hp[getLevel()];
				hp = p.getMaxHP()*hp/100;
			}
			if(!p.isCanNotIncHp()){
				if((p.getHp()+hp)>p.getMaxHP()){
					p.setHp(p.getMaxHP());
				}else if((p.getHp()+hp) <= 0){
					p.setHp(1);
				}else{
					p.setHp(p.getHp()+hp);
				}
			}else{
				if(Skill.logger.isDebugEnabled())Skill.logger.debug("[无法回血状态] [屏蔽Buff_ShunJiaXueLan] [" + p.getLogString() + "] [血]");
			}
			
			if(bt.mp != null && bt.mp.length > getLevel()){
				mp = bt.mp[getLevel()];
				mp = p.getMaxMP()*mp/100;
			}
			if((p.getMp()+mp)>p.getMaxMP()){
				p.setMp(p.getMaxMP());
			}else if(p.getMp()+mp < 0){
				p.setMp(0);
			}else{
				p.setMp(p.getMp()+mp);
			}
			NOTIFY_EVENT_REQ req = null;
			if(!p.isCanNotIncHp()){
				// 加血，通知客户端
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
						.getId(), (byte) Event.HP_INCREASED, hp);
				p.addMessageToRightBag(req);
			}
			// 加MP，通知客户端
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
					.getId(), (byte) Event.MP_INCREASED, mp);
			p.addMessageToRightBag(req);
			
			//通知施放这个buff的人
			if(p != this.getCauser()){
				if(getCauser() instanceof Player){
					Player p2 = (Player)getCauser();
					NOTIFY_EVENT_REQ req2 = null;
					if(p.isCanNotIncHp()){
						req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
								.getId(), (byte) Event.HP_INCREASED, hp);
						p2.addMessageToRightBag(req2);
					}

					req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
							.getId(), (byte) Event.MP_INCREASED, mp);
					p2.addMessageToRightBag(req2);
				}
			}
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			if(p.isCanNotIncHp()){
				Skill.logger.debug("[无法回血状态] [屏蔽Buff_ShunJiaXueLan] [Sprite] [血]");
				return;
			}
			BuffTemplate_ShunJiaXueLan bt = (BuffTemplate_ShunJiaXueLan)this.getTemplate();
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
