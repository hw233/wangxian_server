package com.fy.engineserver.menu.activity.silvercar;

import com.fy.engineserver.activity.silvercar.SilvercarManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.BiaoCheNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;

/**
 * 镖车求救
 * 
 * 
 */
public class Option_SilverCar_Help extends Option {

	/** 呼叫的npc */
	private long npcId;

	public Option_SilverCar_Help(long npcId) {
		this.npcId = npcId;
	}

	@Override
	public void doSelect(Game game, Player player) {
		NPC npc = MemoryNPCManager.getNPCManager().getNPC(npcId);
		if (npc == null) {
			SilvercarManager.logger.error(player.getLogString() + "[去救助NPC的时候,NPC不在了] [npcId:{}]", new Object[] { npcId });
			return;
		}
		if (npc instanceof BiaoCheNpc) {
			BiaoCheNpc biaoCheNpc = (BiaoCheNpc) npc;
			int country = player.getCountry();
			if (((BiaoCheNpc) npc).getCurrentGame() != null) {
				country = ((BiaoCheNpc) npc).getCurrentGame().country;
			}
			game.transferGame(player, new TransportData(0, 0, 0, 0, biaoCheNpc.getGameName(), biaoCheNpc.getX(), biaoCheNpc.getY()));

			player.setTransferGameCountry(country);

			SilvercarManager.logger.error(player.getLogString() + "[去救助NPC] [操作成功] [目标地图:{}] [目标国家:{}] [目标X:{}] [目标Y:{}]", new Object[] { biaoCheNpc.getGameName(), player.getTransferGameCountry(), biaoCheNpc.getX(), biaoCheNpc.getY() });
		} else {
			SilvercarManager.logger.error(player.getLogString() + "[去救助NPC的时候,NPC不是镖车] [NPC:{}]", new Object[] { npc.getClass() });
			return;
		}
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
}
