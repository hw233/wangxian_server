package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 减攻，降低物理攻击和法术攻击
 * 
 *
 */
@SimpleEmbeddable
public class Buff_JianGong extends Buff{

	int physicalDamage = 0;
	int spellDamage = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity() && !getTemplateName().trim().equals(RobberyConstant.FAILBUFF)){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_JianGong bt = (BuffTemplate_JianGong)this.getTemplate();
				if(bt.physicalDamage != null && bt.physicalDamage.length > this.getLevel()){
					physicalDamage = bt.physicalDamage[getLevel()];
				}
				if(bt.spellDamage != null && bt.spellDamage.length > this.getLevel()){
					spellDamage = bt.spellDamage[getLevel()];
				}
				p.setPhyAttackC((p.getPhyAttackC() - physicalDamage));
				
				p.setMagicAttackC((p.getMagicAttackC() - spellDamage));
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_JianGong bt = (BuffTemplate_JianGong)this.getTemplate();
				if(bt.physicalDamage != null && bt.physicalDamage.length > this.getLevel()){
					physicalDamage = bt.physicalDamage[getLevel()];
				}
				if(bt.spellDamage != null && bt.spellDamage.length > this.getLevel()){
					spellDamage = bt.spellDamage[getLevel()];
				}
				s.setPhyAttackC((s.getPhyAttackC() - physicalDamage));
				
				s.setMagicAttackC((s.getMagicAttackC() - spellDamage));

			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setPhyAttackC((p.getPhyAttackC() + physicalDamage));
			p.setMagicAttackC((p.getMagicAttackC() + spellDamage));
			if(getTemplateName().trim().equals(RobberyConstant.FAILBUFF)) {
				TransitRobberyManager.logger.error("[渡劫虚弱buff消失][" + p.getLogString() + "]");
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setPhyAttackC((s.getPhyAttackC() + physicalDamage));
			s.setMagicAttackC((s.getMagicAttackC() + spellDamage));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity() && !getTemplateName().trim().equals(RobberyConstant.FAILBUFF)){		//无敌不可以去掉虚弱buff
				this.setInvalidTime(0);//立即失效
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);//立即失效
			}
		}
	}

}
