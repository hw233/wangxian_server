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
 * 
 * 回复血量百分比降低速度百分比
 * 
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_XueHuiFuAndJiangDiSuDu extends Buff{

	/**
	 * 降低速度百分比
	 */
	protected int speedC;

	/**
	 * 时间间隔
	 */
	protected int lastingTime = 1000;
	
	private long lastExeTime;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setCure(true);
			BuffTemplate_XueHuiFuAndJiangDiSuDu bt = (BuffTemplate_XueHuiFuAndJiangDiSuDu)this.getTemplate();
			if(bt.speedC != null && bt.speedC.length > getLevel()){
				speedC = bt.speedC[getLevel()];
			}
			p.setSpeedC(p.getSpeedC() - speedC);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			BuffTemplate_XueHuiFuAndJiangDiSuDu bt = (BuffTemplate_XueHuiFuAndJiangDiSuDu)this.getTemplate();
			if(bt.speedC != null && bt.speedC.length > getLevel()){
				speedC = bt.speedC[getLevel()];
			}
			p.setSpeedC(p.getSpeedC() - speedC);
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setCure(false);
			p.setSpeedC(p.getSpeedC() + speedC);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setSpeedC(p.getSpeedC() + speedC);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		
		if(heartBeatStartTime - lastExeTime >= lastingTime){
			lastExeTime = heartBeatStartTime;
			if(owner instanceof Player){
				Player p = (Player)owner;
				if(p.isCanNotIncHp()){
					if(Skill.logger.isDebugEnabled())Skill.logger.debug("[无法回血状态] [屏蔽Buff_XueHuiFuAndJiangDiSuDu] [" + p.getLogString() + "] [血]");
					return;
				}
				if(!p.isCure()){
					p.setCure(true);
				}
				int hp = 0;
				BuffTemplate_XueHuiFuAndJiangDiSuDu bt = (BuffTemplate_XueHuiFuAndJiangDiSuDu)this.getTemplate();
				if(bt.hpC != null && bt.hpC.length > getLevel()){
					hp = bt.hpC[getLevel()];
					hp = p.getMaxHP()*hp/100;
				}
				if(bt.lastingTime != null && bt.lastingTime.length > getLevel()){
					lastingTime = bt.lastingTime[getLevel()];
				}
				if((p.getHp()+hp) > p.getMaxHP()){
					p.setHp(p.getMaxHP());
				}else{
					p.setHp(p.getHp()+hp);
				}
				//副本统计
//				if(game != null && game.getDownCity() != null){
//					DownCity dc = game.getDownCity();
//					dc.statPlayerHuanliaoHp(p, hp);
//				}
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
					if(Skill.logger.isDebugEnabled())Skill.logger.debug("[无法回血状态] [屏蔽Buff_XueHuiFuAndJiangDiSuDu] [Sprite] [血]");
					return;
				}
				int hp = 0;
				BuffTemplate_XueHuiFuAndJiangDiSuDu bt = (BuffTemplate_XueHuiFuAndJiangDiSuDu)this.getTemplate();
				if(bt.hpC != null && bt.hpC.length > getLevel()){
					hp = bt.hpC[getLevel()];
					hp = s.getMaxHP()*hp/100;
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
