package com.fy.engineserver.menu.pet;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PET2_LIAN_DAN_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet2.Pet2Manager;

public class OptionPetChiHun  extends Option{
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		//-2000 开启宠物吃魂界面
		PET2_LIAN_DAN_RES res = new PET2_LIAN_DAN_RES(GameMessageFactory.nextSequnceNum(), -1000);
		player.addMessageToRightBag(res);
		Pet2Manager.log.info("{} by {}",getClass().getSimpleName(),player.getName());
	}
}
