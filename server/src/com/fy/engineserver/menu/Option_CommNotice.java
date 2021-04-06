package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_CommNotice extends Option implements NeedCheckPurview{
	private String notice;

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	@Override
	public void doSelect(Game game, Player player) {
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(6000);
		mw.setDescriptionInUUB(notice);
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.text_284);
		mw.setOptions(new Option[]{cancel});
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.descriptionInUUB, mw.getOptions());
		player.addMessageToRightBag(req);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		return true;
	}
}
