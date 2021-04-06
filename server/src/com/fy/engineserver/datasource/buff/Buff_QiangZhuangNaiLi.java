package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 强壮，增加物理防御和法术防御力
 * 
 *
 */
@SimpleEmbeddable
public class Buff_QiangZhuangNaiLi extends Buff{

	int defence = 0;
	int resistance = 0;
	int constitution = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		
		if(owner instanceof Player){
			Player p = (Player)owner;
			
			BuffTemplate_QiangZhuangNaiLi bt = (BuffTemplate_QiangZhuangNaiLi)this.getTemplate();
			if(bt.defence != null && bt.defence.length > this.getLevel()){
				defence = bt.defence[getLevel()];
			}
			if(bt.resistance != null && bt.resistance.length > this.getLevel()){
				resistance = bt.resistance[getLevel()];
			}
			if(bt.constitution != null && bt.constitution.length > this.getLevel()){
				constitution = bt.constitution[getLevel()];
			}
//			p.setDefenceC((p.getDefenceC() + defence));
//			p.setResistanceC((p.getResistanceC()+ resistance));
			p.setConstitutionC((p.getConstitutionC()+constitution));
			
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			
			BuffTemplate_QiangZhuangNaiLi bt = (BuffTemplate_QiangZhuangNaiLi)this.getTemplate();
			if(bt.defence != null && bt.defence.length > this.getLevel()){
				defence = bt.defence[getLevel()];
			}
			if(bt.resistance != null && bt.resistance.length > this.getLevel()){
				resistance = bt.resistance[getLevel()];
			}
//			s.setDefenceC((s.getDefenceC() + defence));
//			s.setResistanceC((s.getResistanceC() + resistance));
			
		}
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
//			p.setDefenceC((p.getDefenceC() - defence));
//			p.setResistanceC((p.getResistanceC() - resistance));
			p.setConstitutionC((p.getConstitutionC()-constitution));
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
//			s.setDefenceC((s.getDefenceC() - defence));
//			s.setResistanceC((s.getResistanceC() - resistance));
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}

}
