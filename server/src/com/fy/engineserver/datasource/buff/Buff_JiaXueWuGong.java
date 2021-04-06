package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.NPC;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 加血，每设定秒钟加血若干HP
 * 
 *
 */
@SimpleEmbeddable
public class Buff_JiaXueWuGong extends Buff{
	
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
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		
		if(heartBeatStartTime - lastExeTime >= LastingTime){
			lastExeTime = heartBeatStartTime;
			if(owner instanceof Player){
				Player p = (Player)owner;
				if(p.isCanNotIncHp()){
					Skill.logger.debug("[无法回血状态] [屏蔽Buff_JiaXueWuGong] [" + p.getLogString() + "] [血]");
					return;
				}
				p.setCure(true);
				int hp = 0;
				BuffTemplate_JiaXueWuGong bt = (BuffTemplate_JiaXueWuGong)this.getTemplate();
				if(bt.modulus != null && bt.modulus.length > getLevel()){
					if(getCauser() instanceof Player){
						//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
						hp = (int)(((Player)getCauser()).getPhyAttack()*bt.modulus[getLevel()]/100);	
					}else if(getCauser() instanceof Monster){
						Monster m = (Monster)getCauser();
						hp = (int)(m.getPhyAttack()*bt.modulus[getLevel()]/100);
					}else if(getCauser() instanceof NPC){
						NPC n = (NPC)getCauser();
						hp = (int)(n.getPhyAttack()*bt.modulus[getLevel()]/100);
					}
				}
				if(p.getHp() + hp > p.getMaxHP()){
					p.setHp(p.getMaxHP());
				}else{
					p.setHp(p.getHp() + hp);
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
					Skill.logger.debug("[无法回血状态] [屏蔽Buff_JiaXueWuGong] [Sprite] [血]");
					return;
				}
				int hp = 0;
				BuffTemplate_JiaXueWuGong bt = (BuffTemplate_JiaXueWuGong)this.getTemplate();
				if(bt.modulus != null && bt.modulus.length > getLevel()){
					if(getCauser() instanceof Player){
						//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
						hp = (int)(((Player)getCauser()).getPhyAttack()*bt.modulus[getLevel()]/100);	
					}else if(getCauser() instanceof Monster){
						Monster m = (Monster)getCauser();
						hp = (int)(m.getPhyAttack()*bt.modulus[getLevel()]/100);
					}else if(getCauser() instanceof NPC){
						NPC n = (NPC)getCauser();
						hp = (int)(n.getPhyAttack()*bt.modulus[getLevel()]/100);
					}
				}
				if(s.getHp() + hp > s.getMaxHP()){
					s.setHp(s.getMaxHP());
				}else{
					s.setHp(s.getHp() + hp);
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
	
	public String getDescription(){
		int hp = 0;
		BuffTemplate_JiaXueWuGong bt = (BuffTemplate_JiaXueWuGong)this.getTemplate();
		if(bt != null && bt.modulus != null && bt.modulus.length > getLevel()){
			if(getCauser() instanceof Player){
				//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
				hp = (int)(((Player)getCauser()).getPhyAttack()*bt.modulus[getLevel()]/100);	
			}else if(getCauser() instanceof Monster){
				Monster m = (Monster)getCauser();
				hp = (int)(m.getPhyAttack()*bt.modulus[getLevel()]/100);
			}else if(getCauser() instanceof NPC){
				NPC n = (NPC)getCauser();
				hp = (int)(n.getPhyAttack()*bt.modulus[getLevel()]/100);
			}
			return Translate.text_3231+((double)LastingTime/1000)+Translate.text_3232+hp+Translate.text_3275;
		}
		return super.getDescription();
	}

}
