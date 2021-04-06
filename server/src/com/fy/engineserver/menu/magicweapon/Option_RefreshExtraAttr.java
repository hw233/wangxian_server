package com.fy.engineserver.menu.magicweapon;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_MAGICWEAPON_EXTRAATTR_RES;
import com.fy.engineserver.sprite.Player;

public class Option_RefreshExtraAttr extends Option implements NeedCheckPurview{
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		OPEN_MAGICWEAPON_EXTRAATTR_RES req = new OPEN_MAGICWEAPON_EXTRAATTR_RES(GameMessageFactory.nextSequnceNum(), "");
		player.addMessageToRightBag(req);
	}

	@Override
	public boolean canSee(Player player) {
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		return true;
	}
}
