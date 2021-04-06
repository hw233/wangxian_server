package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
/**
 * 抵挡并吸收伤害护盾
 * @author Administrator
 *
 */
public class Buff_RecoverShield extends Buff{
	//护盾吸收伤害值
	private int amount = 0;
	//大师级能额外会增加百分比
	private double extraPer;
	
	@Override
	public void start(Fighter owner) {
		// TODO Auto-generated method stub
		if(owner instanceof Player){
			Player p = (Player) owner;
			BuffTemplate_RecoverShield bt = (BuffTemplate_RecoverShield) getTemplate();
			if(getLevel() > bt.getAmount().length){
				return;
			}
			int maxHp = p.getMaxHP();
			long temp = (long) ((long)maxHp * (double)(bt.getAmount()[this.getLevel()]+extraPer) / 100L);
			amount = (int) (temp > Integer.MAX_VALUE ? Integer.MAX_VALUE : temp);
			p.setRecoverHpHuDun(amount);
			double dd = amount + extraPer;
			this.setDescription(String.format(Translate.吸收伤害并转换成生命值, dd + ""));
		}
	}
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.setRecoverHpHuDun(0);
		}
	}
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

	public double getExtraPer() {
		return extraPer;
	}

	public void setExtraPer(double extraPer) {
		this.extraPer = extraPer;
	}

}
