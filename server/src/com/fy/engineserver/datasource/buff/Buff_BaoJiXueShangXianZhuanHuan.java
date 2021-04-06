package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 血上限点数暴击点数转换
 *
 */
@SimpleEmbeddable
public class Buff_BaoJiXueShangXianZhuanHuan extends Buff{

	int totalHPB = 0;

	int criticalHitB = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_BaoJiXueShangXianZhuanHuan bt = (BuffTemplate_BaoJiXueShangXianZhuanHuan)this.getTemplate();
				if(bt.totalHPB != null && bt.totalHPB.length > getLevel()){
					totalHPB = bt.totalHPB[getLevel()];
				}
				if(bt.criticalHitB != null && bt.criticalHitB.length > getLevel()){
					criticalHitB = bt.criticalHitB[getLevel()];
				}
				p.setMaxHPB(p.getMaxHPB() + totalHPB);
				p.setCriticalHitB((p.getCriticalHitB() + criticalHitB));
			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_BaoJiXueShangXianZhuanHuan bt = (BuffTemplate_BaoJiXueShangXianZhuanHuan)this.getTemplate();
			if(bt.totalHPB != null && bt.totalHPB.length > getLevel()){
				totalHPB = bt.totalHPB[getLevel()];
			}
			if(bt.criticalHitB != null && bt.criticalHitB.length > getLevel()){
				criticalHitB = bt.criticalHitB[getLevel()];
			}
			s.setMaxHPB(s.getMaxHPB() + totalHPB);
			s.setCriticalHitB((s.getCriticalHitB() + criticalHitB));
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setMaxHPB(p.getMaxHPB() - totalHPB);
			p.setCriticalHitB((p.getCriticalHitB() - criticalHitB));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setMaxHPB(s.getMaxHPB() - totalHPB);
			s.setCriticalHitB((s.getCriticalHitB() - criticalHitB));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
