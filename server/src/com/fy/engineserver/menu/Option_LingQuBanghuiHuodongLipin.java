package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

public class Option_LingQuBanghuiHuodongLipin extends Option {
	public void doInput(Game game, Player player, String serial) {}

	public void doSelect(Game game, Player player) {
		if(GameConstants.getInstance().getServerName().equals(Translate.text_5316)||GameConstants.getInstance().getServerName().equals(Translate.text_5317)||GameConstants.getInstance().getServerName().equals(Translate.text_5318)||GameConstants.getInstance().getServerName().equals(Translate.text_5319)||GameConstants.getInstance().getServerName().equals(Translate.text_5320)){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,Translate.text_5321);
			player.addMessageToRightBag(hreq);
			return;
		}
		WindowManager wm = WindowManager.getInstance();
		
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_LingQuBanghuiHuodongLipin option = new Option_LingQuBanghuiHuodongLipin();
		
		option.setText(Translate.text_5322);
		option.setIconId("155");
		
		mw.setOptions(new Option[]{option});
		INPUT_WINDOW_REQ req=new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),Translate.text_5322,Translate.text_5323,(byte)2,(byte)24,Translate.在这里输入,Translate.text_5324,Translate.text_364, new byte[0]);
		player.addMessageToRightBag(req);
	}
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_领取帮会活动新手礼品;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
