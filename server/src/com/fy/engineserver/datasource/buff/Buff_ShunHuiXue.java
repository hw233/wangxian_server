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
 * 瞬间回满血
 *
 */
@SimpleEmbeddable
public class Buff_ShunHuiXue extends Buff{

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isCanNotIncHp()){
				Skill.logger.debug("[无法回血状态] [屏蔽Buff_ShunHuiXue] [" + p.getLogString() + "] [血]");
				return;
			}
			p.setHp(p.getMaxHP());
			// 加血，通知客户端
			NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
					.getId(), (byte) Event.HP_INCREASED, p.getMaxHP());
			p.addMessageToRightBag(req);
			
			//通知施放这个buff的人
			if(p != this.getCauser()){
				if(getCauser() instanceof Player){
					Player p2 = (Player)getCauser();
					NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
							.getId(), (byte) Event.HP_INCREASED, p.getMaxHP());
					p2.addMessageToRightBag(req2);
				}
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isCanNotIncHp()){
				Skill.logger.debug("[无法回血状态] [屏蔽Buff_ShunHuiXue] [Sprite] [血]");
				return;
			}
			s.setHp(s.getMaxHP());
			if(getCauser() instanceof Player){
				Player p2 = (Player)getCauser();
				NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, s
						.getId(), (byte) Event.HP_INCREASED, s.getMaxHP());
				p2.addMessageToRightBag(req2);
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
	
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
