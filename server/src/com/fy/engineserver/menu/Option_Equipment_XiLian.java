package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.XILIAN_EQUIPMENT_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Equipment_XiLian extends Option implements NeedCheckPurview{
	@Override
	public void doSelect(Game game, Player player) {
		String xiLianDes=Translate.洗炼说明;
		String material=Translate.洗炼符;
		
		player.addMessageToRightBag(new XILIAN_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), material, xiLianDes));
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		return true;
	}
}
