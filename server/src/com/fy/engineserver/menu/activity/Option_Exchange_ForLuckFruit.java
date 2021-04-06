package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.luckfruit.ForLuckFruitManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_EXCHANGE_FORLUCK_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 吃丹药
 * 
 * 
 */
public class Option_Exchange_ForLuckFruit extends Option implements NeedCheckPurview {

	@Override
	public void doSelect(Game game, Player player) {
		// super.doSelect(game, player);发协议通知客户端弹出吃丹药的窗口
		NOTICE_CLIENT_EXCHANGE_FORLUCK_REQ req = new NOTICE_CLIENT_EXCHANGE_FORLUCK_REQ(GameMessageFactory.nextSequnceNum(), ForLuckFruitManager.getInstance().getFruitName());
		player.addMessageToRightBag(req);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null || jiazu.getLevel() <= 1) {
			return false;
		}
		return player.inSelfSeptStation();
	}
}
