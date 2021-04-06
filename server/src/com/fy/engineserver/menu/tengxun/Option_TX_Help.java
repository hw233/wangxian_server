package com.fy.engineserver.menu.tengxun;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_TX_Help extends Option {

	private int optionID;
	
	public Option_TX_Help() {
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.getWindowById(optionID);
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);
	}

	public void setOptionID(int optionID) {
		this.optionID = optionID;
	}

	public int getOptionID() {
		return optionID;
	}
}
