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
 * 
 * 减血，每设定秒钟增加若干HP
 * 
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_SiLie extends Buff{
	
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
			if(p.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}else{
				int hp = 0;
				BuffTemplate_SiLie bt = (BuffTemplate_SiLie)this.getTemplate();
				if(bt.hp != null && bt.hp.length > getLevel()){
					hp = bt.hp[getLevel()];
				}
				if(p.getHp() >= hp){
					p.setHp(p.getHp() - hp);
				}else{
					p.setHp(0);
				}
				lastExeTime = System.currentTimeMillis();
			}
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}else{
				int hp = 0;
				BuffTemplate_SiLie bt = (BuffTemplate_SiLie)this.getTemplate();
				if(bt.hp != null && bt.hp.length > getLevel()){
					hp = bt.hp[getLevel()];
				}
				if(p.getHp() >= hp){
					p.setHp(p.getHp() - hp);
				}else{
					p.setHp(0);
				}
				lastExeTime = System.currentTimeMillis();
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

		if(owner.getHp() >= owner.getMaxHP()){
			this.setInvalidTime(0);
		}else{
			if(heartBeatStartTime - lastExeTime >= LastingTime){
				lastExeTime = heartBeatStartTime;
				if(owner instanceof Player){
					Player p = (Player)owner;
					if(p.isImmunity()){
						this.setInvalidTime(0);//立即失效
					}else{
						int hp = 0;
						BuffTemplate_SiLie bt = (BuffTemplate_SiLie)this.getTemplate();
						if(bt.hp != null && bt.hp.length > getLevel()){
							hp = bt.hp[getLevel()];
						}
						if(p.getHp() >= hp){
							p.setHp(p.getHp() - hp);
						}else{
							p.setHp(0);
						}
						// 减血，通知客户端
						NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
								.getId(), (byte) Event.HP_DECREASED_SPELL, hp);
						p.addMessageToRightBag(req);
						
						//通知施放这个buff的人
						if(p != this.getCauser()){
							if(getCauser() instanceof Player){
								Player p2 = (Player)getCauser();
								NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
										.getId(), (byte) Event.HP_DECREASED_SPELL, hp);
								p2.addMessageToRightBag(req2);
							}
						}
						//护盾吸收伤害
						int hudun = owner.getHuDunDamage();
						if(hudun > 0){
							int damage = hp;
							if (hudun >= damage) {
								owner.setHuDunDamage(hudun - damage);

								owner.causeDamage(getCauser(), damage, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, damage, 10,Fighter.DAMAGETYPE_XISHOU);
							} else {
							
								owner.setHuDunDamage(0);

								owner.causeDamage(getCauser(), hudun, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, hudun, 10,Fighter.DAMAGETYPE_XISHOU);

								owner.causeDamage(getCauser(), damage - hudun, 10,Fighter.DAMAGETYPE_SPELL);
								getCauser().damageFeedback(owner, damage - hudun, 10,Fighter.DAMAGETYPE_SPELL);
							}
						}else{
							owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_SPELL);
							getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_SPELL);
						}
						
					}
				}else if(owner instanceof Sprite){
					Sprite s = (Sprite)owner;
					if(s.isImmunity()){
						this.setInvalidTime(0);//立即失效
					}else{
						int hp = 0;
						BuffTemplate_SiLie bt = (BuffTemplate_SiLie)this.getTemplate();
						if(bt.hp != null && bt.hp.length > getLevel()){
							hp = bt.hp[getLevel()];
						}
						if(s.getHp() >= hp){
							s.setHp(s.getHp() - hp);
						}else{
							s.setHp(0);
						}
						if(getCauser() instanceof Player){
							Player p2 = (Player)getCauser();
							NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, s
									.getId(), (byte) Event.HP_DECREASED_SPELL, hp);
							p2.addMessageToRightBag(req2);
						}
						//护盾吸收伤害
						int hudun = owner.getHuDunDamage();
						if(hudun > 0){
							int damage = hp;
							if (hudun >= damage) {
								owner.setHuDunDamage(hudun - damage);

								owner.causeDamage(getCauser(), damage, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, damage, 10,Fighter.DAMAGETYPE_XISHOU);
							} else {
							
								owner.setHuDunDamage(0);

								owner.causeDamage(getCauser(), hudun, 10,Fighter.DAMAGETYPE_XISHOU);
								getCauser().damageFeedback(owner, hudun, 10,Fighter.DAMAGETYPE_XISHOU);

								owner.causeDamage(getCauser(), damage - hudun, 10,Fighter.DAMAGETYPE_SPELL);
								getCauser().damageFeedback(owner, damage - hudun, 10,Fighter.DAMAGETYPE_SPELL);
							}
						}else{
							owner.causeDamage(getCauser(), hp, 10,Fighter.DAMAGETYPE_SPELL);
							getCauser().damageFeedback(owner, hp, 10,Fighter.DAMAGETYPE_SPELL);
						}
					}
				}
			}
		}
	}

}
