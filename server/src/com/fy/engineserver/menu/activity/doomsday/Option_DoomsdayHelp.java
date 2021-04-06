package com.fy.engineserver.menu.activity.doomsday;

import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_DoomsdayHelp extends Option {
	
	private int windowID;

	public Option_DoomsdayHelp() {
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		try{
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.getWindowById(windowID);
			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
			player.addMessageToRightBag(req);
		}catch(Exception e) {
			DoomsdayManager.logger.error("Option_DoomsdayHelp", e);
		}
	}

	public void setWindowID(int windowID) {
		this.windowID = windowID;
	}

	public int getWindowID() {
		return windowID;
	}
}
