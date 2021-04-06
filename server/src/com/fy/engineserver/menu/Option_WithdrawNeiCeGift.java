package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_WithdrawNeiCeGift extends Option{
	String articleName;

	public void doInput(Game game, Player player, String inputContent) {}
	
	
	public void doSelect(Game game, Player player) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(3600);
		Option_WithdrawNeiCeGift option = new Option_WithdrawNeiCeGift();
		option.setArticleName(articleName);
		option.setText(Translate.text_5322);
		option.setIconId("155");
		
		mw.setOptions(new Option[]{option});
		INPUT_WINDOW_REQ req=new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId(),Translate.text_5322,Translate.text_5323,(byte)2,(byte)20,Translate.在这里输入,Translate.text_5324,Translate.text_364, new byte[0]);
		player.addMessageToRightBag(req);
	}



	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_领取内测礼品;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
