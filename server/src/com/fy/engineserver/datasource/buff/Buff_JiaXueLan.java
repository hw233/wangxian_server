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
import com.fy.engineserver.talent.FlyTalentManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 加血，每设定秒钟增加若干HP
 * 
 *
 */
@SimpleEmbeddable
public class Buff_JiaXueLan extends Buff{
	
	private long lastExeTime = 0;
	
	private long LastingTime;

	public void setLastingTime(long lastingTime) {
		LastingTime = lastingTime;
	}


	
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
	int count = 0;
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		if(heartBeatStartTime - lastExeTime >= LastingTime){
			lastExeTime = heartBeatStartTime;
			if(owner.getHp() > 0){
				if(owner instanceof Player){
					Player p = (Player)owner;
					p.setCure(true);
					int hp = 0;
					int mp = 0;
					BuffTemplate_JiaXueLan bt = (BuffTemplate_JiaXueLan)this.getTemplate();
					if(bt.hp != null && bt.hp.length > getLevel()){
						hp = bt.hp[getLevel()];
					}
					//
					int pvalue = p.minusHp();
					if(Skill.logger.isDebugEnabled()){
						Skill.logger.debug("[技能触发buff] [Buff_JiaXueLan] [降低治疗的buff] [hp:"+hp+"] [减量："+pvalue+"] [血]");
					}
					hp -= hp*pvalue/100;
					
					hp+=FlyTalentManager.getInstance().handle_仙疗(p, hp);
					
					if(!p.isCanNotIncHp()){ //玩家处于不能回血状态
						if((p.getHp()+hp) > p.getMaxHP()){
							p.setHp(p.getMaxHP());
						}else{
							p.setHp(p.getHp()+hp);
						}
					}else{
						if(Skill.logger.isDebugEnabled())Skill.logger.debug("[无法回血状态] [屏蔽Buff_JiaXueLan] [" + p.getLogString() + "] [血]");
					}
					
					if(bt.mp != null && bt.mp.length > getLevel()){
						mp = bt.mp[getLevel()];
					}
					//
					if(Skill.logger.isDebugEnabled()){
						Skill.logger.debug("[技能触发buff] [Buff_JiaXueLan] [降低治疗的buff] [mp:"+mp+"] [减量："+pvalue+"] [蓝]");
					}
					mp -= mp*pvalue/100;
					
					if((p.getMp()+mp) > p.getMaxMP()){
						p.setMp(p.getMaxMP());
					}else{
						p.setMp(p.getMp()+mp);
					}
					//副本统计
					if(game != null && game.getDownCity() != null){
						DownCity dc = game.getDownCity();
						dc.statPlayerHuanliaoMp(p, mp);
						dc.statPlayerHuanliaoHp(p, hp);
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
							if(!p.isCanNotIncHp()){
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
					Sprite s = (Sprite)owner;
					if(s.isCanNotIncHp()){
						Skill.logger.debug("[无法回血状态] [屏蔽Buff_JiaXueLan] [Sprite] [血]");
						return;
					}
					int hp = 0;
					BuffTemplate_JiaXueLan bt = (BuffTemplate_JiaXueLan)this.getTemplate();
					if(bt.hp != null && bt.hp.length > getLevel()){
						hp = bt.hp[getLevel()];
					}
					if((s.getHp()+hp) > s.getMaxHP()){
						s.setHp(s.getMaxHP());
					}else{
						s.setHp(s.getHp()+hp);
					}
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
