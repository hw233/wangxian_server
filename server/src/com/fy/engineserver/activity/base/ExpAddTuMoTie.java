package com.fy.engineserver.activity.base;

import com.fy.engineserver.sprite.Player;

/**
 *  蓝色封魔录
 *
 */
public class ExpAddTuMoTie extends ExpAddAbstract {

	private int tieColorType;
	
	public ExpAddTuMoTie(long id, long startTime, long endTime, int addType, int addParameter, int tieColorType) {
		super(id, startTime, endTime, 100000, addType, addParameter);
		this.tieColorType = tieColorType;
	}

	@Override
	public boolean canAdd(Player player, int addReason, Object...is) {
		if (is.length > 0) {
			int colorType = Integer.parseInt(is[0].toString());
			if (addReason == this.addReason){
				if (colorType >= tieColorType) {
					return true;
				}
			}
		}
		return false;
	}

	public void setTieColorType(int tieColorType) {
		this.tieColorType = tieColorType;
	}

	public int getTieColorType() {
		return tieColorType;
	}
	
	@Override
	public String getParmeter() {
		return "封魔录" + "color=" + tieColorType;
	}

}
