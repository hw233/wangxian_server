package com.fy.engineserver.menu.magicweapon;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_MAGICWEAPON_BREAK_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

public class Option_OpenBreakRankWindow extends Option implements NeedCheckPurview{

	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		if (MagicWeaponManager.法宝阶飞升是否开启 ) {
			OPEN_MAGICWEAPON_BREAK_WINDOW_RES res = new OPEN_MAGICWEAPON_BREAK_WINDOW_RES(GameMessageFactory.nextSequnceNum(), Translate.法宝突破界面描述);
			player.addMessageToRightBag(res);
		}
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		return MagicWeaponManager.法宝阶飞升是否开启;
	}

}
