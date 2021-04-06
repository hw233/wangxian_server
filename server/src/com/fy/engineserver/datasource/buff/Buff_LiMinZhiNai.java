package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_LiMinZhiNai extends Buff {
	int strengthB = 0;
	int dexterityB;
	int spellpowerB;
	int constitutionB;
	int speedC;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_LiMinZhiNai bt = (BuffTemplate_LiMinZhiNai)this.getTemplate();
				if(bt.strengthB != null && bt.strengthB.length > getLevel()){
					strengthB = bt.strengthB[getLevel()];
				}
				p.setStrengthB((p.getStrengthB() + strengthB));
				if(bt.dexterityB != null && bt.dexterityB.length > this.getLevel()){
					dexterityB = bt.dexterityB[getLevel()];
				}
				p.setDexterityB((p.getDexterityB() + dexterityB));
				if(bt.spellpowerB != null && bt.spellpowerB.length > getLevel()){
					spellpowerB = bt.spellpowerB[getLevel()];
				}
				p.setSpellpowerB((p.getSpellpowerB() + spellpowerB));
				if(bt.constitutionB != null && bt.constitutionB.length > getLevel()){
					constitutionB = bt.constitutionB[getLevel()];
				}
				p.setConstitutionB((p.getConstitutionB() + constitutionB));
				if(bt.speedC != null && bt.speedC.length > getLevel()){
					speedC = bt.speedC[getLevel()];
				}
				p.setSpeedC((p.getSpeedC() + speedC));
			
		}else if(owner instanceof Sprite){
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setStrengthB((p.getStrengthB() - strengthB));
			p.setDexterityB((p.getDexterityB() - dexterityB));
			p.setSpellpowerB((p.getSpellpowerB() - spellpowerB));
			p.setConstitutionB((p.getConstitutionB() - constitutionB));
			p.setSpeedC((p.getSpeedC() - speedC));
		}else if(owner instanceof Sprite){
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}
}
