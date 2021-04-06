package com.fy.engineserver.menu.fairylandTreasure;

import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

public class Option_pray extends Option implements NeedCheckPurview {
	private byte prayType;
	private String buffName;

	@Override
	public void doSelect(Game game, Player player) {
		FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setDescriptionInUUB(Translate.translateString(Translate.花费银子祈福提示, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(ftm.getPrayCost()) } }));
		Option_Pray_Sure option_sure = new Option_Pray_Sure(prayType, buffName);
		option_sure.setText(Translate.确定);
		Option_Cancel option_cancle = new Option_Cancel();
		option_cancle.setText(Translate.取消);
		mw.setOptions(new Option[] { option_sure, option_cancle });
		QUERY_WINDOW_RES req = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(req);
	}

	@Override
	public boolean canSee(Player player) {
		return true;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public byte getPrayType() {
		return prayType;
	}

	public void setPrayType(byte prayType) {
		this.prayType = prayType;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

}
