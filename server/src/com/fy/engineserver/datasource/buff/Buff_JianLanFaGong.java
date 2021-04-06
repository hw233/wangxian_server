package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
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
 * 中毒，每设定秒钟减少若干MP
 * 
 *
 */
@SimpleEmbeddable
public class Buff_JianLanFaGong extends Buff{
	
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
		
		if(heartBeatStartTime - lastExeTime >= LastingTime){
			lastExeTime = heartBeatStartTime;
			if(owner instanceof Player){
				Player p = (Player)owner;
				if(p.isImmunity()){
					this.setInvalidTime(0);//立即失效
				}else{
					int mp = 0;
					BuffTemplate_JianLanFaGong bt = (BuffTemplate_JianLanFaGong)this.getTemplate();
					if(bt.modulus != null && bt.modulus.length > getLevel()){
						if(getCauser() instanceof Player){
							//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
							mp = (int)(((Player)getCauser()).getMagicAttack()*bt.modulus[getLevel()]/100);	
						}else if(getCauser() instanceof Monster){
							Monster m = (Monster)getCauser();
							mp = (int)(m.getMagicAttack()*bt.modulus[getLevel()]/100);
						}else if(getCauser() instanceof NPC){
							NPC n = (NPC)getCauser();
							mp = (int)(n.getMagicAttack()*bt.modulus[getLevel()]/100);
						}
					}
					if(p.getMp() > mp){
						p.setMp(p.getMp() - mp);
					}else{
						p.setMp(0);
					}
					// 减蓝，通知客户端
					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
							.getId(), (byte) Event.MP_DECREASED, mp);
					p.addMessageToRightBag(req);
					//通知施放这个buff的人
					if(p != this.getCauser()){
						if(getCauser() instanceof Player){
							Player p2 = (Player)getCauser();
							NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
									.getId(), (byte) Event.MP_DECREASED, mp);
							p2.addMessageToRightBag(req2);
						}
					}
				}
			}
		}
	}
	
	public String getDescription(){
		int mp = 0;
		BuffTemplate_JianLanFaGong bt = (BuffTemplate_JianLanFaGong)this.getTemplate();
		if(bt != null && bt.modulus != null && bt.modulus.length > getLevel()){
			if(getCauser() instanceof Player){
				//除以100是因为在编辑器里编辑的系数为整数，约定除以100为真正的系数
				mp = (int)(((Player)getCauser()).getMagicAttack()*bt.modulus[getLevel()]/100);	
			}else if(getCauser() instanceof Monster){
				Monster m = (Monster)getCauser();
				mp = (int)(m.getMagicAttack()*bt.modulus[getLevel()]/100);
			}else if(getCauser() instanceof NPC){
				NPC n = (NPC)getCauser();
				mp = (int)(n.getMagicAttack()*bt.modulus[getLevel()]/100);
			}
			return Translate.text_3231+((double)LastingTime/1000)+Translate.text_3234+mp+Translate.text_3301;
		}
		return super.getDescription();
	}

}
