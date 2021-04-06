package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.chuangong.ChuanGongManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_OPEN_URL_REQ;
import com.fy.engineserver.sprite.Player;

/**
 *台服广告
 *
 */
public class Option_taiwan_notice extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	
	@Override
	public void doSelect(Game game, Player player) {
		
		try {
			NOTICE_OPEN_URL_REQ req = new NOTICE_OPEN_URL_REQ(GameMessageFactory.nextSequnceNum(), RobberyConstant.台服广告url);
			player.addMessageToRightBag(req);
		} catch (Exception e) {
			ChuanGongManager.logger.error("[台服广告错误] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
	}
	
}
