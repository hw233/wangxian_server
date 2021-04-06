package com.fy.engineserver.menu.pet;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PET2_LIAN_DAN_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet2.Pet2Manager;

public class OptionPetLianDan  extends Option{
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		//-1000 开启宠物变道具界面
		PET2_LIAN_DAN_RES res = new PET2_LIAN_DAN_RES(GameMessageFactory.nextSequnceNum(), -2000);
		player.addMessageToRightBag(res);
		Pet2Manager.log.info("{} by {}",getClass().getSimpleName(),player.getName());
	}
}
