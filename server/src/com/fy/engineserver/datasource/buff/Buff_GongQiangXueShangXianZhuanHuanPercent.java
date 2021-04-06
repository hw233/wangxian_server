package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 攻击强度百分比血上限百分比转换
 *
 */
@SimpleEmbeddable
public class Buff_GongQiangXueShangXianZhuanHuanPercent extends Buff{

	int gongQiangC = 0;

	int totalHPC = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_GongQiangXueShangXianZhuanHuanPercent bt = (BuffTemplate_GongQiangXueShangXianZhuanHuanPercent)this.getTemplate();
				if(bt.gongQiang != null && bt.gongQiang.length > getLevel()){
					gongQiangC = bt.gongQiang[getLevel()];
				}
				if(bt.totalHPC != null && bt.totalHPC.length > getLevel()){
					totalHPC = bt.totalHPC[getLevel()];
				}
//				p.setMeleeAttackIntensityC((p.getMeleeAttackIntensityC() + gongQiangC));
//				p.setSpellAttackIntensityC((p.getSpellAttackIntensityC() + gongQiangC));
//				p.setTotalHpC(p.getTotalHpC() + totalHPC);
			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_GongQiangXueShangXianZhuanHuanPercent bt = (BuffTemplate_GongQiangXueShangXianZhuanHuanPercent)this.getTemplate();
			if(bt.gongQiang != null && bt.gongQiang.length > getLevel()){
				gongQiangC = bt.gongQiang[getLevel()];
			}
			if(bt.totalHPC != null && bt.totalHPC.length > getLevel()){
				totalHPC = bt.totalHPC[getLevel()];
			}
//			s.setMeleeAttackIntensityC((s.getMeleeAttackIntensityC() + gongQiangC));
//			s.setSpellAttackIntensityC((s.getSpellAttackIntensityC() + gongQiangC));
//			s.setTotalHpC(s.getTotalHpC() + totalHPC);
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
//			p.setMeleeAttackIntensityC((p.getMeleeAttackIntensityC() - gongQiangC));
//			p.setSpellAttackIntensityC((p.getSpellAttackIntensityC() - gongQiangC));
//			p.setTotalHpC(p.getTotalHpC() - totalHPC);
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
//			s.setMeleeAttackIntensityC((s.getMeleeAttackIntensityC() - gongQiangC));
//			s.setSpellAttackIntensityC((s.getSpellAttackIntensityC() - gongQiangC));
//			s.setTotalHpC(s.getTotalHpC() - totalHPC);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
