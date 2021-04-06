package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 增加双防、物防、闪避以及免暴属性
 * 
 * 
 */

@SimpleEmbeddable
public class Buff_WangZheZhuFu extends Buff{
	
	int shuangfang;
	int dodge;
	int criticalDefance;
	/** 大师级能会增加技能效果 */
	private double extraRate;
	
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_WangZheZhuFu bt = (BuffTemplate_WangZheZhuFu)this.getTemplate();
			if(bt.shuangFang != null && bt.shuangFang.length > this.getLevel()){
				shuangfang = bt.shuangFang[getLevel()];
			}
			
			if(bt.dodgePercent != null && bt.dodgePercent.length > getLevel()){
				dodge = bt.dodgePercent[getLevel()];
			}
			if (bt.criticalDefance != null && bt.criticalDefance.length > getLevel()) {
				criticalDefance = bt.criticalDefance[getLevel()];
			}
			p.setPhyDefenceC(p.getPhyDefenceC() + shuangfang + extraRate);
			p.setMagicDefenceC(p.getMagicDefenceC() + shuangfang + extraRate);
			p.setDodgeC(p.getDodgeC() + dodge/* + extraRate*/);
			p.setCriticalDefenceC(p.getCriticalDefenceC() + criticalDefance + extraRate);
			if (extraRate > 0) {
				this.setDescription(this.getTempDes());
			}
		}
	}
	
	public void calc () {
		BuffTemplate_WangZheZhuFu bt = (BuffTemplate_WangZheZhuFu)this.getTemplate();
		if(bt.shuangFang != null && bt.shuangFang.length > this.getLevel()){
			shuangfang = bt.shuangFang[getLevel()];
		}
		
		if(bt.dodgePercent != null && bt.dodgePercent.length > getLevel()){
			dodge = bt.dodgePercent[getLevel()];
		}
		if (bt.criticalDefance != null && bt.criticalDefance.length > getLevel()) {
			criticalDefance = bt.criticalDefance[getLevel()];
		}
	}
	
	public String getTempDes() {
		StringBuffer sb = new StringBuffer();
		sb.append(Translate.translateString(Translate.增加双防百分比详细, new String[][]{{Translate.STRING_1, shuangfang+"%+("+extraRate+"%)"}}));
		sb.append("，");
		if (dodge > 0) {
			sb.append(String.format(Translate.增加闪避比例, dodge+"%"));
			sb.append("，");
		}
		sb.append(String.format(Translate.增加免暴比例, criticalDefance+"%+("+extraRate+"%)"));
		return sb.toString();
//		StringBuffer sb = new StringBuffer();
//		sb.append(Translate.translateString(Translate.增加双防百分比详细, new String[][]{{Translate.STRING_1,(shuangfang+extraRate)+"%"}}));
//		sb.append("，");
//		sb.append(String.format(Translate.增加闪避比例, (dodge+extraRate)+"%"));
//		sb.append("，");
//		sb.append(String.format(Translate.增加免暴比例, (criticalDefance+extraRate)+"%"));
//		return sb.toString();
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setPhyDefenceC(p.getPhyDefenceC() - shuangfang - extraRate);
			p.setMagicDefenceC(p.getMagicDefenceC() - shuangfang - extraRate);
			p.setDodgeC(p.getDodgeC() - dodge /*- extraRate*/);
			p.setCriticalDefenceC(p.getCriticalDefenceC() - criticalDefance - extraRate);
		}
	}
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}


	public double getExtraRate() {
		return extraRate;
	}


	public void setExtraRate(double extraRate) {
		this.extraRate = extraRate;
	}
	
}
