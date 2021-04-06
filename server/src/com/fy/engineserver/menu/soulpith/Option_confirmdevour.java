package com.fy.engineserver.menu.soulpith;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.DEVOUR_SOULPITH_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.soulpith.SoulPithEntityManager;
import com.fy.engineserver.sprite.Player;

public class Option_confirmdevour extends Option{
	
	private long meterials;
	
	private long mainId;
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public void doSelect(Game game, Player player) {
		boolean b = SoulPithEntityManager.getInst().devour(player, mainId, meterials, true);
		if (b) {
			DEVOUR_SOULPITH_RES resp = new DEVOUR_SOULPITH_RES(GameMessageFactory.nextSequnceNum(), 1);
			player.addMessageToRightBag(resp);
		}
	}
	public long getMeterials() {
		return meterials;
	}
	public void setMeterials(long meterials) {
		this.meterials = meterials;
	}
	public long getMainId() {
		return mainId;
	}
	public void setMainId(long mainId) {
		this.mainId = mainId;
	}
}
