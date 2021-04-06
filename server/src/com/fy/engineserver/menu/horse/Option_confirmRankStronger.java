package com.fy.engineserver.menu.horse;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HORSE_RANKSTAR_STRONG_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity;
import com.fy.engineserver.sprite.horse2.manager.Horse2EntityManager;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;

public class Option_confirmRankStronger extends Option{
	
	private long horseId;
	
	private byte peiyangType;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		byte result = Horse2EntityManager.instance.rankStarStrong(player, horseId, peiyangType, true);
		Horse2RelevantEntity entity = Horse2EntityManager.instance.getEntity(player.getId());
		int leftTime = 0;
		if (entity != null) {
			leftTime = Horse2Manager.instance.baseModel.getFreeTimes4Rank() - entity.getCultureTime4Free();
			if (leftTime < 0) {
				leftTime = 0;
			}
		}
		HORSE_RANKSTAR_STRONG_RES resp = new HORSE_RANKSTAR_STRONG_RES(GameMessageFactory.nextSequnceNum(), leftTime, result);
		player.addMessageToRightBag(resp);
	}

	public long getHorseId() {
		return horseId;
	}

	public void setHorseId(long horseId) {
		this.horseId = horseId;
	}

	public byte getPeiyangType() {
		return peiyangType;
	}

	public void setPeiyangType(byte peiyangType) {
		this.peiyangType = peiyangType;
	}

}
