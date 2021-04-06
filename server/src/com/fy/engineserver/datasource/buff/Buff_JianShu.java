package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.articleEnchant.ControlBuff;
import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.datasource.skill.passivetrigger.PassiveTriggerImmune;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 减速
 *
 */
@SimpleEmbeddable
public class Buff_JianShu extends Buff implements ControlBuff{

	int speed = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);
			} else if (p.getImmuType() == PassiveTriggerImmune.免疫减速 || p.getImmuType() == PassiveTriggerImmune.免疫控制) {
				this.setInvalidTime(0);
			}else{
//				if(p.getHorse() > 0){
//					p.getDownFromHorse();
//				}
					long decreaseTime = (this.getInvalidTime() - this.getStartTime()) * p.decreaseConTimeRate / 1000;
					this.setInvalidTime(this.getInvalidTime() - decreaseTime);
					EnchantEntityManager.instance.notifyCheckEnchant(p);
				/*if (p.checkPassiveEnchant(EnchantEntityManager.受到控制类技能) == 100) {
					this.setInvalidTime(0);
					if (EnchantManager.logger.isDebugEnabled()) {
						EnchantManager.logger.debug("["+this.getClass().getSimpleName()+"] [检测被动控制类技能] [成功] [" + p.getLogString() + "]");
//						p.sendError("触发成功！");
					}
				} else {*/
					BuffTemplate_JianShu bt = (BuffTemplate_JianShu)this.getTemplate();
					if(bt.speed != null && bt.speed.length > getLevel()){
						speed = bt.speed[getLevel()];
					}
					p.setJiansuState(true);
					p.setSpeedC((p.getSpeedC() - speed));
			//	}
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_JianShu bt = (BuffTemplate_JianShu)this.getTemplate();
				if(bt.speed != null && bt.speed.length > getLevel()){
					speed = bt.speed[getLevel()];
				}
				s.setSpeedC((s.getSpeedC() - speed));
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setSpeedC((p.getSpeedC()+ speed));
			p.setJiansuState(false);
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setSpeedC((s.getSpeedC() + speed));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);//立即失效
			} else if (p.getImmuType() == PassiveTriggerImmune.免疫减速 || p.getImmuType() == PassiveTriggerImmune.免疫控制) {
				this.setInvalidTime(0);
			}else{
//				if(p.getHorse() > 0){
//					p.getDownFromHorse();
//				}
			}
			if(SkEnhanceManager.getInst().checkIsMinusSpeed(p)){
				this.setInvalidTime(0);
//				Skill.logger.debug("影魅 《狼印速度速度不受控制》 [继续] ["+(p!=null?p.getName():"null")+"]");
				return;
			}
//			Skill.logger.debug("影魅 《狼印速度速度不受控制》 [结束] ["+(p!=null?p.getName():"null")+"]");
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}
		}
	}

}
