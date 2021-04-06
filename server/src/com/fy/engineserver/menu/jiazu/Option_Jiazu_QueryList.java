package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_SHOW_JIAZU_FUNCTION_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 申请加入家族(查看家族列表)
 * 
 * 
 */
public class Option_Jiazu_QueryList extends Option implements NeedCheckPurview {

	public Option_Jiazu_QueryList() {

	}

	@Override
	public void doSelect(Game game, Player player) {
		JIAZU_SHOW_JIAZU_FUNCTION_RES res = new JIAZU_SHOW_JIAZU_FUNCTION_RES(GameMessageFactory.nextSequnceNum(), (byte) 1);
		player.addMessageToRightBag(res);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		return true;// player.getJiazuId() <= 0;
	}
}
