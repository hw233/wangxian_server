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
 */

@SimpleEmbeddable
public class Buff_LiLiangZhuFu extends Buff{
	
	int magicAttack;
	int breakDefence;
	int critical;
	int hitRate;
	int jingzhun;
	
	private double extraRate;
	
	
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_LiLiangZhuFu bt = (BuffTemplate_LiLiangZhuFu)this.getTemplate();
			if(bt.magicAttack != null && bt.magicAttack.length > this.getLevel()){
				magicAttack = bt.magicAttack[getLevel()];
			}
			if(bt.breakDefence != null && bt.breakDefence.length > getLevel()){
				breakDefence = bt.breakDefence[getLevel()];
			}
			if (bt.critical != null && bt.critical.length > getLevel()) {
				critical = bt.critical[getLevel()];
			}
			if (bt.hitRate != null && bt.hitRate.length > getLevel()) {
				hitRate = bt.hitRate[getLevel()];
			}
			if (bt.getJingzhun() != null && bt.getJingzhun().length > getLevel()) {
				jingzhun = bt.getJingzhun()[getLevel()];
			}
			p.setMagicAttackC(p.getMagicAttackC() + magicAttack /*+ extraRate*/);
			p.setBreakDefenceC(p.getBreakDefenceC() + breakDefence + extraRate);
			p.setCriticalHitC(p.getCriticalHitC() + critical + extraRate);
			p.setHitC(p.getHitC() + hitRate + extraRate);
			p.setAccurateC(p.getAccurateC() + jingzhun + extraRate);
			if (extraRate > 0) {
				this.setDescription(this.getTempDes());
			}
		}
	}
	
	public void calc() {
		BuffTemplate_LiLiangZhuFu bt = (BuffTemplate_LiLiangZhuFu)this.getTemplate();
		if(bt.magicAttack != null && bt.magicAttack.length > this.getLevel()){
			magicAttack = bt.magicAttack[getLevel()];
		}
		if(bt.breakDefence != null && bt.breakDefence.length > getLevel()){
			breakDefence = bt.breakDefence[getLevel()];
		}
		if (bt.critical != null && bt.critical.length > getLevel()) {
			critical = bt.critical[getLevel()];
		}
		if (bt.hitRate != null && bt.hitRate.length > getLevel()) {
			hitRate = bt.hitRate[getLevel()];
		}
		if (bt.getJingzhun() != null && bt.getJingzhun().length > getLevel()) {
			jingzhun = bt.getJingzhun()[getLevel()];
		}
	}
	
	public String getTempDes() {
		StringBuffer sb = new StringBuffer();
		if (magicAttack > 0) {
			sb.append(String.format(Translate.增加法攻比例, magicAttack+"%"));
			sb.append("，");
		}
		if (breakDefence + extraRate > 0) {
			sb.append(String.format(Translate.增加破甲比例, breakDefence+"%+("+extraRate+"%)"));
			sb.append("，");
		}
		sb.append(String.format(Translate.增加暴击比例, critical+"%+("+extraRate+"%)"));
		sb.append("，");
		sb.append(String.format(Translate.增加命中比例, hitRate+"%+("+extraRate+"%)"));
		sb.append("，");
		sb.append(String.format(Translate.增加精准比例, jingzhun +"%+("+extraRate+"%)"));
		return sb.toString();
//		StringBuffer sb = new StringBuffer();
//		sb.append(String.format(Translate.增加法攻比例, (magicAttack + extraRate)+"%"));
//		sb.append("，");
//		sb.append(String.format(Translate.增加破甲比例, (breakDefence + extraRate)+"%"));
//		sb.append("，");
//		sb.append(String.format(Translate.增加暴击比例, (critical + extraRate)+"%"));
//		sb.append("，");
//		sb.append(String.format(Translate.增加命中比例, (hitRate + extraRate)+"%"));
//		sb.append("，");
//		sb.append(String.format(Translate.增加精准比例, (jingzhun + extraRate)+"%"));
//		return sb.toString();
	}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setMagicAttackC(p.getMagicAttackC() - magicAttack /*- extraRate*/);
			p.setBreakDefenceC(p.getBreakDefenceC() - breakDefence - extraRate);
			p.setCriticalHitC(p.getCriticalHitC() - critical - extraRate);
			p.setHitC(p.getHitC() - hitRate - extraRate);
			p.setAccurateC(p.getAccurateC() - jingzhun - extraRate);
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
