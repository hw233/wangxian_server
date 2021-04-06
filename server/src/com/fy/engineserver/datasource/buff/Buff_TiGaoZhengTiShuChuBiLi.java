package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 增加整体输出伤害比例
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_TiGaoZhengTiShuChuBiLi extends Buff{

	int skillDamageIncreaseRate = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_TiGaoZhengTiShuChuBiLi bt = (BuffTemplate_TiGaoZhengTiShuChuBiLi)this.getTemplate();
				if(bt.skillDamageIncreaseRate != null && bt.skillDamageIncreaseRate.length > getLevel()){
					skillDamageIncreaseRate = bt.skillDamageIncreaseRate[getLevel()];
				}
//				p.setSkillDamageIncreaseRate((p.getSkillDamageIncreaseRate() + skillDamageIncreaseRate));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_TiGaoZhengTiShuChuBiLi bt = (BuffTemplate_TiGaoZhengTiShuChuBiLi)this.getTemplate();
				if(bt.skillDamageIncreaseRate != null && bt.skillDamageIncreaseRate.length > getLevel()){
					skillDamageIncreaseRate = bt.skillDamageIncreaseRate[getLevel()];
				}
				s.setSkillDamageIncreaseRate((s.getSkillDamageIncreaseRate() + skillDamageIncreaseRate));
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
//			p.setSkillDamageIncreaseRate((p.getSkillDamageIncreaseRate() - skillDamageIncreaseRate));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setSkillDamageIncreaseRate((s.getSkillDamageIncreaseRate() - skillDamageIncreaseRate));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
