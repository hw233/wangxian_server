package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.MARRIAGE_MENU;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.TradeManager;

public class Option_getRing extends Option{

	public Option_getRing(){
		super();
	}
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.取消);
		Option_ReallGetRing getRing = new Option_ReallGetRing();
		getRing.setText(Translate.确定);
		mw.setOptions(new Option[]{cancel, getRing});
		String message = Translate.text_marriage_124 + TradeManager.putMoneyToMyText(MarriageManager.GET_RING_MONEY);
		MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
		player.addMessageToRightBag(menu);
	}
}
