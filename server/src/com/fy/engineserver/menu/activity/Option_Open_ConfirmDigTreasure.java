package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.treasure.TreasureActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAY_TREASUREACTIVITY_RES;
import com.fy.engineserver.sprite.Player;
/**
 * 寻宝二次确认
 * @author Administrator
 *
 */
public class Option_Open_ConfirmDigTreasure extends Option{
	private int treasureId;
	
	private int digTime;
	
	@Override
	public void doSelect(Game game, Player player) {
		long[] takeArticleIds = TreasureActivityManager.instance.digTreasure(player, treasureId, digTime, true);
		if (takeArticleIds == null) {
			return ;
		}
		PLAY_TREASUREACTIVITY_RES resp = new PLAY_TREASUREACTIVITY_RES(GameMessageFactory.nextSequnceNum(), treasureId, takeArticleIds);
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getTreasureId() {
		return treasureId;
	}

	public void setTreasureId(int treasureId) {
		this.treasureId = treasureId;
	}

	public int getDigTime() {
		return digTime;
	}

	public void setDigTime(int digTime) {
		this.digTime = digTime;
	}

}
