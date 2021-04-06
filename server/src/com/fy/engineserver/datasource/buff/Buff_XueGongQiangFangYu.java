package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 增加血物理攻击法术攻击物理防御法术防御
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_XueGongQiangFangYu extends Buff{

	int percent = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){

		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_XueGongQiangFangYu bt = (BuffTemplate_XueGongQiangFangYu)this.getTemplate();
			int pp[] = bt.getPercent();
			if(pp != null && pp.length > this.getLevel()){
				percent = pp[getLevel()];
			}
//			p.setTotalHpC(p.getTotalHpC() + percent);
//			p.setSkillDamageIncreaseRate(p.getSkillDamageIncreaseRate() + percent);
//			p.setDefenceC((p.getDefenceC() + percent));
//			p.setResistanceC((p.getResistanceC()+ percent));
//			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_XueGongQiangFangYu bt = (BuffTemplate_XueGongQiangFangYu)this.getTemplate();
			int pp[] = bt.getPercent();
			if(pp != null && pp.length > this.getLevel()){
				percent = pp[getLevel()];
			}
			
//			s.setTotalHpC(s.getTotalHpC() + percent);
//			s.setSkillDamageIncreaseRate(s.getSkillDamageIncreaseRate() + percent);
//			s.setDefenceC((s.getDefenceC() + percent));
//			s.setResistanceC((s.getResistanceC()+ percent));
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
//			p.setTotalHpC(p.getTotalHpC() - percent);
//			p.setSkillDamageIncreaseRate(p.getSkillDamageIncreaseRate() - percent);
//			p.setDefenceC((p.getDefenceC() - percent));
//			p.setResistanceC((p.getResistanceC()- percent));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
//			s.setTotalHpC(s.getTotalHpC() - percent);
//			s.setSkillDamageIncreaseRate(s.getSkillDamageIncreaseRate() - percent);
//			s.setDefenceC((s.getDefenceC() - percent));
//			s.setResistanceC((s.getResistanceC()- percent));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
