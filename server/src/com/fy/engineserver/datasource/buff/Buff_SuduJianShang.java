package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 速度和减少伤害百分比
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_SuduJianShang extends Buff{

	int speed = 0;
	int skillDamageDecreaseRate = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_SuduJianShang bt = (BuffTemplate_SuduJianShang)this.getTemplate();
				if(bt.speed != null && bt.speed.length > getLevel()){
					speed = bt.speed[getLevel()];
				}
				p.setSpeedC((p.getSpeedC() + speed));
				
				if(bt.skillDamageDecreaseRate != null && bt.skillDamageDecreaseRate.length > getLevel()){
					skillDamageDecreaseRate = bt.skillDamageDecreaseRate[getLevel()];
				}
//				p.setSkillDamageDecreaseRate((p.getSkillDamageDecreaseRate() + skillDamageDecreaseRate));
			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_SuduJianShang bt = (BuffTemplate_SuduJianShang)this.getTemplate();
				if(bt.speed != null && bt.speed.length > getLevel()){
					speed = bt.speed[getLevel()];
				}
				s.setSpeedC((s.getSpeedC() + speed));
				
				if(bt.skillDamageDecreaseRate != null && bt.skillDamageDecreaseRate.length > getLevel()){
					skillDamageDecreaseRate = bt.skillDamageDecreaseRate[getLevel()];
				}
				s.setSkillDamageDecreaseRate((s.getSkillDamageDecreaseRate() + skillDamageDecreaseRate));
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setSpeedC((p.getSpeedC()- speed));
//			p.setSkillDamageDecreaseRate((p.getSkillDamageDecreaseRate() - skillDamageDecreaseRate));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setSpeedC((s.getSpeedC() - speed));
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
