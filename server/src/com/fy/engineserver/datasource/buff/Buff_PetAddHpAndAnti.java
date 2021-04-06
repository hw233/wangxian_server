package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.pet.Pet;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
/**
 * 增加宠物血量和反伤比例（反正只对宠物和玩家有效）
 * @author Administrator
 *
 */
@SimpleEmbeddable
public class Buff_PetAddHpAndAnti extends Buff {
	/** 百分比 */
	private int antiInjuryRate;
	/** 百分比 */
	private int hpRate;
	
	public void start(Fighter owner){
		if(owner instanceof Pet){
			Pet pet = (Pet) owner;
			antiInjuryRate = ((BuffTemplate_petAddHpAndAnti)this.getTemplate()).getAntiRates()[this.getLevel()];
			hpRate = ((BuffTemplate_petAddHpAndAnti)this.getTemplate()).getHpRates()[this.getLevel()];
			if (antiInjuryRate > 0) {
				pet.setAntiInjuryRate(pet.getAntiInjuryRate() + antiInjuryRate);
			}
			if (hpRate > 0) {
				pet.setMaxHPC(pet.getMaxHPC() + hpRate);
			}
		}
	}


	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if (owner instanceof Pet) {
			Pet pet = (Pet) owner;
			if (antiInjuryRate > 0) {
				pet.setAntiInjuryRate(pet.getAntiInjuryRate() - antiInjuryRate);
			}
			if (hpRate > 0) {
				pet.setMaxHPC(pet.getMaxHPC() - hpRate);
			}
		}
	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}


	public int getAntiInjuryRate() {
		return antiInjuryRate;
	}


	public void setAntiInjuryRate(int antiInjuryRate) {
		this.antiInjuryRate = antiInjuryRate;
	}


	public int getHpRate() {
		return hpRate;
	}


	public void setHpRate(int hpRate) {
		this.hpRate = hpRate;
	}
	
	
	
}
