package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 加血，每设定秒钟增加若干HP(千分比)
 */
@SimpleEmbeddable
public class Buff_JiaXue extends Buff{
	
	private long lastExeTime = 0;
	
	private long lastingTime = 1000;
	
	public int hpFix;

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setCure(true);
		}else if(owner instanceof Sprite){
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setCure(false);
		}else if(owner instanceof Sprite){
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		
		if(heartBeatStartTime - lastExeTime >= lastingTime){
			lastExeTime = heartBeatStartTime;
			if(owner.getHp() > 0){
				if(owner instanceof Player){
					Player p = (Player)owner;
					if(p.isCanNotIncHp()){
						Skill.logger.debug("[无法回血状态] [屏蔽Buff_JiaXue] [" + p.getLogString() + "] [血]");
						return;
					}
					p.setCure(true);
					int hp = 0;
					BuffTemplate_JiaXue bt = (BuffTemplate_JiaXue)this.getTemplate();
					if(bt.hp != null && bt.hp.length > getLevel()){
						hp = bt.hp[getLevel()];
						hp += hpFix;
						long temp = ((long)p.getMaxHP()*(long)hp);
						hp = (int) (temp/1000);
//						hp = p.getMaxHP()*hp/1000;
					}
					if(bt.lastingTime != null && bt.lastingTime.length > getLevel()){
						lastingTime = bt.lastingTime[getLevel()];
					}
					if ((p.getHp() + hp) > p.getMaxHP()) {
						p.setHp(p.getMaxHP());
					} else {
						p.setHp(p.getHp() + hp);
					}
					//副本统计
					if(game != null && game.getDownCity() != null){
						DownCity dc = game.getDownCity();
						dc.statPlayerHuanliaoHp(p, hp);
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
					Sprite s = (Sprite)owner;
					if(s.isCanNotIncHp()){
						Skill.logger.debug("[无法回血状态] [屏蔽Buff_JiaXue] [Sprite] [血]");
						return;
					}
					int hp = 0;
					BuffTemplate_JiaXue bt = (BuffTemplate_JiaXue)this.getTemplate();
					if(bt.hp != null && bt.hp.length > getLevel()){
						hp = bt.hp[getLevel()];
						hp += hpFix; //(270 + 19) *7800000
						long temp = ((long)s.getMaxHP()*(long)hp);
						hp = (int) (temp/1000);
					}
					if(bt.lastingTime != null && bt.lastingTime.length > getLevel()){
						lastingTime = bt.lastingTime[getLevel()];
					}
					if((s.getHp()+hp) > s.getMaxHP()){
						s.setHp(s.getMaxHP());
					}else{
						s.setHp(s.getHp()+hp);
					}
					//通知施放这个buff的人
					if(getCauser() instanceof Player){
						Player p2 = (Player)getCauser();
						NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, s
								.getId(), (byte) Event.HP_INCREASED, hp);
						p2.addMessageToRightBag(req2);
					}
				}
			}

		}
	}
	
}
