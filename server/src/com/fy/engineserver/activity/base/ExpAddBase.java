package com.fy.engineserver.activity.base;

import com.fy.engineserver.sprite.Player;

/**
 * 经验增加对象
 *
 */
public class ExpAddBase extends ExpAddAbstract {

	public ExpAddBase(long id, long startTime, long endTime, int addReason,
			int addType, int addParameter) {
		super(id, startTime, endTime, addReason, addType, addParameter);
	}

	public boolean canAdd(Player player, int addReason, Object...is) {
		if (this.addReason == addReason) {
			return true;
		}
		return false;
	}

	@Override
	public String getParmeter() {
		return "一般活动";
	}
}
