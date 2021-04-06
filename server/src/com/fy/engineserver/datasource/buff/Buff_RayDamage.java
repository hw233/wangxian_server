package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class Buff_RayDamage extends Buff{
	int offsetRate;
	public int offsetTimes;

	@Override
	public void start(Fighter owner) {
		// TODO Auto-generated method stub
		if(owner instanceof Player){
			TransitRobberyManager.logger.info("种植防雷击buff成功1");
			Player p = (Player) owner;
			BuffTemplate_RayDamage bt = (BuffTemplate_RayDamage) getTemplate();
			if(getLevel() > bt.getRate().length){
				return;
			}
			offsetTimes = bt.getDdAmount()[this.getLevel()];
			offsetRate = bt.getRate()[getLevel()];
			TransitRobberyManager.logger.info("种植防雷击buff成功2  " + offsetRate + "  次数=" + offsetTimes);
			p.setOffsetPercent(p.getOffsetPercent() + offsetRate);
		}
	}
	/**
	 * 每次攻击时候调用，用来计算次数
	 */
	public boolean checkBuffAct(Fighter owner){
		if(owner instanceof Player){
			offsetTimes --;
			TransitRobberyManager.logger.info("受到雷击---抵挡次数减少1，当前可抵挡次数=" + offsetTimes);
			if(offsetTimes <= 0){
				return false;
			}
		}
		return true;
	}

	@Override
	public void end(Fighter owner) {
		// TODO Auto-generated method stub
		if(owner instanceof Player){
			Player p = (Player) owner;
			p.setOffsetPercent(p.getOffsetPercent() - offsetRate);
		}
	}

}
