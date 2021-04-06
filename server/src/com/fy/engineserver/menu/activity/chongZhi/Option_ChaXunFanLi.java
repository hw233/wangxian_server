package com.fy.engineserver.menu.activity.chongZhi;

import com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivity;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_ChaXunFanLi extends Option implements NeedCheckPurview {

	public Option_ChaXunFanLi(){};
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
//		ChongZhiActivity.getInstance().option_FanLi_ChaXun(player);
	}

	@Override
	public boolean canSee(Player player) {
		//已经废弃
//		if (ChongZhiActivity.getInstance().fanliActivity == null) {
//			return false;
//		}
//		long now = System.currentTimeMillis();
//		if (ChongZhiActivity.getInstance().fanliActivity.getStart_time() >= now || now >= ChongZhiActivity.getInstance().fanliActivity.getLingqu_Time()) {
//			return false;
//		}
		return false;
	}
}
