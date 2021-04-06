package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 攻击强度和减少伤害百分比
 *
 */
@SimpleEmbeddable
public class Buff_GongQiangJianShang extends Buff{

	int gongQiang = 0;

	int skillDamageDecreaseRate = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_GongQiangJianShang bt = (BuffTemplate_GongQiangJianShang)this.getTemplate();
				if(bt.gongQiang != null && bt.gongQiang.length > getLevel()){
					gongQiang = bt.gongQiang[getLevel()];
				}
				
				if(bt.skillDamageDecreaseRate != null && bt.skillDamageDecreaseRate.length > getLevel()){
					skillDamageDecreaseRate = bt.skillDamageDecreaseRate[getLevel()];
				}
//				p.setSkillDamageDecreaseRate((p.getSkillDamageDecreaseRate() + skillDamageDecreaseRate));
//
//				p.setMeleeAttackIntensityC((p.getMeleeAttackIntensityC() + gongQiang));
//				p.setSpellAttackIntensityC((p.getSpellAttackIntensityC() + gongQiang));
			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_GongQiangJianShang bt = (BuffTemplate_GongQiangJianShang)this.getTemplate();
			if(bt.gongQiang != null && bt.gongQiang.length > getLevel()){
				gongQiang = bt.gongQiang[getLevel()];
			}
			
			if(bt.skillDamageDecreaseRate != null && bt.skillDamageDecreaseRate.length > getLevel()){
				skillDamageDecreaseRate = bt.skillDamageDecreaseRate[getLevel()];
			}
			s.setSkillDamageDecreaseRate((s.getSkillDamageDecreaseRate() + skillDamageDecreaseRate));

//			s.setMeleeAttackIntensityC((s.getMeleeAttackIntensityC() + gongQiang));
//			s.setSpellAttackIntensityC((s.getSpellAttackIntensityC() + gongQiang));
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
//			p.setMeleeAttackIntensityC((p.getMeleeAttackIntensityC() - gongQiang));
//			p.setSpellAttackIntensityC((p.getSpellAttackIntensityC() - gongQiang));
//			p.setSkillDamageDecreaseRate((p.getSkillDamageDecreaseRate() - skillDamageDecreaseRate));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
//			s.setMeleeAttackIntensityC((s.getMeleeAttackIntensityC() - gongQiang));
//			s.setSpellAttackIntensityC((s.getSpellAttackIntensityC() - gongQiang));
			s.setSkillDamageDecreaseRate((s.getSkillDamageDecreaseRate() - skillDamageDecreaseRate));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
