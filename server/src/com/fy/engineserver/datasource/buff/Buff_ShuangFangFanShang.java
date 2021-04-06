package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 双防反伤
 * 
 *
 */
@SimpleEmbeddable
public class Buff_ShuangFangFanShang extends Buff{

	int shuangFang = 0;
	int ironMaidenPercent = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_ShuangFangFanShang bt = (BuffTemplate_ShuangFangFanShang)this.getTemplate();
			if(bt.shuangFang != null && bt.shuangFang.length > this.getLevel()){
				shuangFang = bt.shuangFang[getLevel()];
			}
			
			if(bt.ironMaidenPercent != null && bt.ironMaidenPercent.length > getLevel()){
				ironMaidenPercent = bt.ironMaidenPercent[getLevel()];
			}
			p.setPhyDefenceC(p.getPhyDefenceC() + shuangFang);
			p.setMagicDefenceC(p.getMagicDefenceC() + shuangFang);
			p.setIronMaidenPercent((p.getIronMaidenPercent() + ironMaidenPercent));

		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			BuffTemplate_ShuangFangFanShang bt = (BuffTemplate_ShuangFangFanShang)this.getTemplate();
			if(bt.shuangFang != null && bt.shuangFang.length > this.getLevel()){
				shuangFang = bt.shuangFang[getLevel()];
			}

			if(bt.ironMaidenPercent != null && bt.ironMaidenPercent.length > getLevel()){
				ironMaidenPercent = bt.ironMaidenPercent[getLevel()];
			}
			s.setPhyDefenceC(s.getPhyDefenceC() + shuangFang);
			s.setMagicDefenceC(s.getMagicDefenceC() + shuangFang);
			s.setIronMaidenPercent((short)(s.getIronMaidenPercent() + ironMaidenPercent));
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
			p.setIronMaidenPercent((p.getIronMaidenPercent() - ironMaidenPercent));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			s.setPhyDefenceC(s.getPhyDefenceC() - shuangFang);
			s.setMagicDefenceC(s.getMagicDefenceC() - shuangFang);
			s.setIronMaidenPercent((short)(s.getIronMaidenPercent() - ironMaidenPercent));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

}
