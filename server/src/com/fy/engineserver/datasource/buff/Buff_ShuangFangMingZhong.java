package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 双防命中
 * 
 *
 */
@SimpleEmbeddable
public class Buff_ShuangFangMingZhong extends Buff{

	int shuangFang = 0;
	int attackRating = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_ShuangFangMingZhong bt = (BuffTemplate_ShuangFangMingZhong)this.getTemplate();
			if(bt.shuangFang != null && bt.shuangFang.length > this.getLevel()){
				shuangFang = bt.shuangFang[getLevel()];
			}
			
			if(bt.attackRating != null && bt.attackRating.length > getLevel()){
				attackRating = bt.attackRating[getLevel()]*10;
			}
			p.setPhyDefenceC(p.getPhyDefenceC() + shuangFang);
			p.setMagicDefenceC(p.getMagicDefenceC() + shuangFang);
			p.setHitRateOther((p.getHitRateOther() + attackRating));

		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			BuffTemplate_ShuangFangMingZhong bt = (BuffTemplate_ShuangFangMingZhong)this.getTemplate();
			if(bt.shuangFang != null && bt.shuangFang.length > this.getLevel()){
				shuangFang = bt.shuangFang[getLevel()];
			}

			if(bt.attackRating != null && bt.attackRating.length > getLevel()){
				attackRating = bt.attackRating[getLevel()]*10;
			}
			s.setPhyDefenceC(s.getPhyDefenceC() + shuangFang);
			s.setMagicDefenceC(s.getMagicDefenceC() + shuangFang);
			s.setHitRateOther((s.getHitRateOther() + attackRating));
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setPhyDefenceC(p.getPhyDefenceC() - shuangFang);
			p.setMagicDefenceC(p.getMagicDefenceC() - shuangFang);
			p.setHitRateOther((p.getHitRateOther() - attackRating));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setPhyDefenceC(s.getPhyDefenceC() - shuangFang);
			s.setMagicDefenceC(s.getMagicDefenceC() - shuangFang);
			s.setHitRateOther((s.getHitRateOther() - attackRating));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

}
