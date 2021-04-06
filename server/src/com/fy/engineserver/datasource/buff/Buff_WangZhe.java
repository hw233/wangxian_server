package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_WangZhe extends Buff {
	int strength = 0;
	int dexterity;
	int spellpower;
	int constitution;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_WangZhe bt = (BuffTemplate_WangZhe)this.getTemplate();
				if(bt.strength != null && bt.strength.length > getLevel()){
					strength = bt.strength[getLevel()];
				}
				p.setStrengthC((p.getStrengthC() + strength));
				if(bt.dexterity != null && bt.dexterity.length > this.getLevel()){
					dexterity = bt.dexterity[getLevel()];
				}
				p.setDexterityC((p.getDexterityC() + dexterity));
				if(bt.spellpower != null && bt.spellpower.length > getLevel()){
					spellpower = bt.spellpower[getLevel()];
				}
				p.setSpellpowerC((p.getSpellpowerC() + spellpower));
				if(bt.constitution != null && bt.constitution.length > getLevel()){
					constitution = bt.constitution[getLevel()];
				}
				p.setConstitutionC((p.getConstitutionC() + constitution));
			
		}else if(owner instanceof Sprite){
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setStrengthC((p.getStrengthC() - strength));
			p.setDexterityC((p.getDexterityC() - dexterity));
			p.setSpellpowerC((p.getSpellpowerC() - spellpower));
			p.setConstitutionC((p.getConstitutionC() - constitution));
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
