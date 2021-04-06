package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 物理攻击和法术攻击
 * 
 *
 */
@SimpleEmbeddable
public class Buff_GongQiangFangYu_Percent extends Buff{

	int physicalDamage = 0;
	int magicDamage = 0;
	int phyDefence = 0;
	int magicDefence = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(p.isImmunity()){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_GongQiangFangYu_Percent bt = (BuffTemplate_GongQiangFangYu_Percent)this.getTemplate();
				if(bt.physicalDamage_percent != null && bt.physicalDamage_percent.length > this.getLevel()){
					physicalDamage = bt.physicalDamage_percent[getLevel()];
				}
				if(bt.phyDefence_percent != null && bt.phyDefence_percent.length > this.getLevel()){
					phyDefence = bt.phyDefence_percent[getLevel()];
				}
				if(bt.magicDamage_percent != null && bt.magicDamage_percent.length > this.getLevel()){
					magicDamage = bt.magicDamage_percent[getLevel()];
				}
				if(bt.magicDefence_percent != null && bt.magicDefence_percent.length > this.getLevel()){
					magicDefence = bt.magicDefence_percent[getLevel()];
				}
				if(physicalDamage != 0){
					p.setPhyAttackC((p.getPhyAttackC() + physicalDamage));
				}
				if(phyDefence != 0){
					p.setPhyDefenceC((p.getPhyDefenceC() + phyDefence));
				}
				if(magicDamage != 0){
					p.setMagicAttackC((p.getMagicAttackC() + magicDamage));
				}
				if(magicDefence != 0){
					p.setMagicDefenceC((p.getMagicDefenceC() + magicDefence));
				}
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(s.isImmunity()){
				this.setInvalidTime(0);
			}else{
				BuffTemplate_GongQiangFangYu_Percent bt = (BuffTemplate_GongQiangFangYu_Percent)this.getTemplate();
				if(bt.physicalDamage_percent != null && bt.physicalDamage_percent.length > this.getLevel()){
					physicalDamage = bt.physicalDamage_percent[getLevel()];
				}
				if(bt.phyDefence_percent != null && bt.phyDefence_percent.length > this.getLevel()){
					phyDefence = bt.phyDefence_percent[getLevel()];
				}
				if(bt.magicDamage_percent != null && bt.magicDamage_percent.length > this.getLevel()){
					magicDamage = bt.magicDamage_percent[getLevel()];
				}
				if(bt.magicDefence_percent != null && bt.magicDefence_percent.length > this.getLevel()){
					magicDefence = bt.magicDefence_percent[getLevel()];
				}
				if(physicalDamage != 0){
					s.setPhyAttackC((s.getPhyAttackC() + physicalDamage));
				}
				if(phyDefence != 0){
					s.setPhyDefenceC((s.getPhyDefenceC() + phyDefence));
				}
				if(magicDamage != 0){
					s.setMagicAttackC((s.getMagicAttackC() + magicDamage));
				}
				if(magicDefence != 0){
					s.setMagicDefenceC((s.getMagicDefenceC() + magicDefence));
				}
			}
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(physicalDamage != 0){
				p.setPhyAttackC((p.getPhyAttackC() - physicalDamage));
			}
			if(phyDefence != 0){
				p.setPhyDefenceC((p.getPhyDefenceC() - phyDefence));
			}
			if(magicDamage != 0){
				p.setMagicAttackC((p.getMagicAttackC() - magicDamage));
			}
			if(magicDefence != 0){
				p.setMagicDefenceC((p.getMagicDefenceC() - magicDefence));
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			if(physicalDamage != 0){
				s.setPhyAttackC((s.getPhyAttackC() - physicalDamage));
			}
			if(phyDefence != 0){
				s.setPhyDefenceC((s.getPhyDefenceC() - phyDefence));
			}
			if(magicDamage != 0){
				s.setMagicAttackC((s.getMagicAttackC() - magicDamage));
			}
			if(magicDefence != 0){
				s.setMagicDefenceC((s.getMagicDefenceC() - magicDefence));
			}
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

}
