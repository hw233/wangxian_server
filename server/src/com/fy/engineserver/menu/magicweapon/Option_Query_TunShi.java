package com.fy.engineserver.menu.magicweapon;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_MAGICWEAPON_OPTION_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Query_TunShi extends Option {

	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		String[] cm = null;
		if(ArticleManager.getInstance().magicweaponEatProps == null) {
			cm = new String[0];
		} else {
			cm = ArticleManager.getInstance().magicweaponEatProps.toArray(new String[0]);
		}
		if(game!=null && player!=null){
			OPEN_MAGICWEAPON_OPTION_RES res = new OPEN_MAGICWEAPON_OPTION_RES(GameMessageFactory.nextSequnceNum(),1,cm);
			player.addMessageToRightBag(res);
		}else{
			player.sendError(Translate.服务器出现错误);
		}
	}

}
