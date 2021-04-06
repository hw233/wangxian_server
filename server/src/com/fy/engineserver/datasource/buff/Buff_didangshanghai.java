package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class Buff_didangshanghai extends Buff{
	//抵挡次数
	private int amount = 0;
	@Override
	public void start(Fighter owner) {
		// TODO Auto-generated method stub
		if(owner instanceof Player){
			BuffTemplate_didangshanghai bt = (BuffTemplate_didangshanghai) getTemplate();
			if(getLevel() > bt.getAmount().length){
				return;
			}
			amount = bt.getAmount()[this.getLevel()];
		}
	}
	/**
	 * 每次攻击时候调用，用来计算次数
	 */
	public boolean checkBuffAct(Fighter owner){
		if(owner instanceof Player){
			amount --;
			TransitRobberyManager.logger.info("受到攻击---抵挡次数减少1，当前可抵挡次数=" + amount);
			if(amount <= 0){
				return false;
			}
		}
		return true;
	}

}
