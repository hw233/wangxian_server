package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 攻击强度点数血上限点数转换
 *
 */
@SimpleEmbeddable
public class Buff_GongQiangXueShangXianZhuanHuan extends Buff{

	int gongQiangB = 0;

	int totalHPB = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_GongQiangXueShangXianZhuanHuan bt = (BuffTemplate_GongQiangXueShangXianZhuanHuan)this.getTemplate();
				if(bt.gongQiangB != null && bt.gongQiangB.length > getLevel()){
					gongQiangB = bt.gongQiangB[getLevel()];
				}
				if(bt.totalHPB != null && bt.totalHPB.length > getLevel()){
					totalHPB = bt.totalHPB[getLevel()];
				}
				p.setMagicAttackB((p.getMagicAttackB() + gongQiangB));
				p.setPhyAttackB((p.getPhyAttackB() + gongQiangB));
				p.setMaxHPB(p.getMaxHPB() + totalHPB);
			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_GongQiangXueShangXianZhuanHuan bt = (BuffTemplate_GongQiangXueShangXianZhuanHuan)this.getTemplate();
			if(bt.gongQiangB != null && bt.gongQiangB.length > getLevel()){
				gongQiangB = bt.gongQiangB[getLevel()];
			}
			if(bt.totalHPB != null && bt.totalHPB.length > getLevel()){
				totalHPB = bt.totalHPB[getLevel()];
			}
			s.setMagicAttackB(s.getMagicAttackB() + gongQiangB);
			s.setPhyAttackB(s.getPhyAttackB() + gongQiangB);
			s.setMaxHPB(s.getMaxHPB() + totalHPB);
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setMagicAttackB((p.getMagicAttackB() - gongQiangB));
			p.setPhyAttackB((p.getPhyAttackB() - gongQiangB));
			p.setMaxHPB(p.getMaxHPB() - totalHPB);
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setMagicAttackB(s.getMagicAttackB() - gongQiangB);
			s.setPhyAttackB(s.getPhyAttackB() - gongQiangB);
			s.setMaxHPB(s.getMaxHPB() - totalHPB);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
