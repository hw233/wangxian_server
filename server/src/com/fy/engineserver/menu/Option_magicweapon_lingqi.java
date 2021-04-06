package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_MAGICWEAPON_ZHULING_RES;
import com.fy.engineserver.sprite.Player;

public class Option_magicweapon_lingqi extends Option{

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		OPEN_MAGICWEAPON_ZHULING_RES res = new OPEN_MAGICWEAPON_ZHULING_RES(GameMessageFactory.nextSequnceNum(), Translate.冲灵描述, MagicWeaponConstant.lingqiProps, MagicWeaponConstant.lingqiNum);
		player.addMessageToRightBag(res);
	}
	
	
}
