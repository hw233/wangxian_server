package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.articleEnchant.ControlBuff;
import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.datasource.skill.passivetrigger.PassiveTriggerImmune;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.pet.Pet;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 定身流血(百分比)
 *
 */
@SimpleEmbeddable
public class Buff_DingShengLiuXue extends Buff implements ControlBuff{
	
	private long lastExeTime = 0;
	
	private long lastingTime = 1000;

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);
			}else if (p.getImmuType() != PassiveTriggerImmune.免疫定身 && p.getImmuType() != PassiveTriggerImmune.免疫控制){
//				if(p.getHorse() > 0){
//					p.getDownFromHorse();
//				}
					long decreaseTime = (this.getInvalidTime() - this.getStartTime()) * p.decreaseConTimeRate / 1000;
					this.setInvalidTime(this.getInvalidTime() - decreaseTime);
					EnchantEntityManager.instance.notifyCheckEnchant(p);
				/*if (p.checkPassiveEnchant(EnchantEntityManager.受到控制类技能) == 100) {
					this.setInvalidTime(0);
				} else {*/
					p.setHold(true);
					SkEnhanceManager.getInst().addSkillBuff(p);
				//}
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);
			}else{
				s.setHold(true);
				if (owner instanceof Pet) {
					if (((Pet)owner).getImmuType() == PassiveTriggerImmune.免疫定身) {
						s.setHold(false);
					}
				}
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setHold(false);
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setHold(false);
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
				if(p.isImmunity()){
					this.setInvalidTime(0);//立即失效
				}else{
					int hp = 0;
					BuffTemplate_DingShengLiuXue bt = (BuffTemplate_DingShengLiuXue)this.getTemplate();
					if(bt.hpD != null && bt.hpD.length > getLevel()){
						hp = bt.hpD[getLevel()];
						hp = p.getMaxHP()*hp/1000;
					}
					if(bt.lastingTime != null && bt.lastingTime.length > getLevel()){
						lastingTime = bt.lastingTime[getLevel()];
					}
					if((p.getHp()-hp) > 0){
						p.setHp(p.getHp()-hp);
					}else{
						p.setHp(0);
					}

					// 加血，通知客户端
					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
							.getId(), (byte) Event.HP_DECREASED_ZHONGDU, hp);
					p.addMessageToRightBag(req);
					
					//通知施放这个buff的人
					if(p != this.getCauser()){
						if(getCauser() instanceof Player){
							Player p2 = (Player)getCauser();
							NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
									.getId(), (byte) Event.HP_DECREASED_ZHONGDU, hp);
							p2.addMessageToRightBag(req2);
						}
					}
				}
			}else if(owner instanceof Sprite){
				Sprite s = (Sprite)owner;
				if(s.isImmunity()){
					this.setInvalidTime(0);//立即失效
				}else{
					int hp = 0;
					BuffTemplate_DingShengLiuXue bt = (BuffTemplate_DingShengLiuXue)this.getTemplate();
					if(bt.hpD != null && bt.hpD.length > getLevel()){
						hp = bt.hpD[getLevel()];
						hp = s.getMaxHP()*hp/1000;
					}
					if(bt.lastingTime != null && bt.lastingTime.length > getLevel()){
						lastingTime = bt.lastingTime[getLevel()];
					}
					if((s.getHp()-hp) > 0){
						s.setHp(s.getHp()-hp);
					}else{
						s.setHp(0);
					}
					//通知施放这个buff的人
					if(getCauser() instanceof Player){
						Player p2 = (Player)getCauser();
						NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, s
								.getId(), (byte) Event.HP_DECREASED_ZHONGDU, hp);
						p2.addMessageToRightBag(req2);
					}
				}
			}
		}
	}

}
