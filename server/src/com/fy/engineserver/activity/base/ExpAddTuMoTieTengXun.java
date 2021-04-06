package com.fy.engineserver.activity.base;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tengxun.TengXunDataManager;

/**
 *  腾讯魔钻 蓝色封魔录
 *
 */
public class ExpAddTuMoTieTengXun extends ExpAddAbstract {

	private int tieColorType;
	
	public ExpAddTuMoTieTengXun(long id, long startTime, long endTime, int addType, int addParameter, int tieColorType) {
		super(id, startTime, endTime, 100000, addType, addParameter);
		this.tieColorType = tieColorType;
	}

	@Override
	public boolean canAdd(Player player, int addReason, Object...is) {
		if (is.length > 0) {
			int colorType = Integer.parseInt(is[0].toString());
			if (TengXunDataManager.instance.getGameLevel(player.getId()) > 0 && addReason == this.addReason){
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
		return "魔钻封魔录" + "color=" + tieColorType;
	}
}
