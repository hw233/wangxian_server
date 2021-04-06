package com.fy.engineserver.menu.activity.godDown;

import com.fy.engineserver.activity.godDown.GodDwonManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 天神下凡，点击npc领取物品
 * @author Administrator
 *
 */
public class Option_GodDown_Receive extends Option {
	
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		GodDwonManager.getInstance().receiveGodDown(player);
		
	}

	
	
}


