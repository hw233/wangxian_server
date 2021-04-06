package com.fy.engineserver.menu.qianCeng;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_QianCengTa_Help extends Option implements NeedCheckPurview{

	public Option_QianCengTa_Help(){
		super();
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.getWindowById(1109);
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);
	}

	@Override
	public boolean canSee(Player player) {
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		return true;
	}
}
