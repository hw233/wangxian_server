package com.fy.engineserver.menu.soulpith;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.ARTIFICE_SOULPITH_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.soulpith.SoulPithEntityManager;
import com.fy.engineserver.sprite.Player;
/**
 * 进入金猴天灾活动
 * 
 * @date on create 2016年3月8日 下午2:29:28
 */
public class Option_confirmartifice extends Option{
	
	private long[] meterialIds;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public void doSelect(Game game, Player player) {
		long id = SoulPithEntityManager.getInst().artifice(player, meterialIds, true);
		if (id > 0) {
			ARTIFICE_SOULPITH_RES resp = new ARTIFICE_SOULPITH_RES(GameMessageFactory.nextSequnceNum(), id);
			player.addMessageToRightBag(resp);
		}
	}
	public long[] getMeterialIds() {
		return meterialIds;
	}
	public void setMeterialIds(long[] meterialIds) {
		this.meterialIds = meterialIds;
	}
}
