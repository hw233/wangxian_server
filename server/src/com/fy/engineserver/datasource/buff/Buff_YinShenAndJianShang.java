package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 隐身且减少受到伤害
 * 
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_YinShenAndJianShang extends Buff{

	public int decreaseDamage;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setObjectOpacity(true);
			calc();
			p.setPhyDefenceRateOther(p.getPhyDefenceRateOther() + decreaseDamage);
			p.setMagicDefenceRateOther(p.getMagicDefenceRateOther() + decreaseDamage);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setObjectOpacity(true);
			calc();
			p.setPhyDefenceRateOther(p.getPhyDefenceRateOther() + decreaseDamage);
			p.setMagicDefenceRateOther(p.getMagicDefenceRateOther() + decreaseDamage);
		}
	}


	public void calc() {
		if(decreaseDamage != 0){
			return;
		}
		BuffTemplate_YinShenAndJianShang bt = (BuffTemplate_YinShenAndJianShang)this.getTemplate();
		if(bt.decreaseDamage != null && bt.decreaseDamage.length > getLevel()){
			decreaseDamage = bt.decreaseDamage[getLevel()];
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setObjectOpacity(false);
			p.setPhyDefenceRateOther(p.getPhyDefenceRateOther() - decreaseDamage);
			p.setMagicDefenceRateOther(p.getMagicDefenceRateOther() - decreaseDamage);
		}else if(owner instanceof Sprite){
			Sprite p = (Sprite)owner;
			p.setObjectOpacity(false);
			p.setPhyDefenceRateOther(p.getPhyDefenceRateOther() - decreaseDamage);
			p.setMagicDefenceRateOther(p.getMagicDefenceRateOther() - decreaseDamage);
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

}
