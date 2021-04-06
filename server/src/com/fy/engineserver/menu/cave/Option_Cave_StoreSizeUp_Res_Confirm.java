package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 仙府仓库资源上限提示
 * 
 * 
 */
public class Option_Cave_StoreSizeUp_Res_Confirm extends Option implements FaeryConfig {

	protected CaveNPC npc;

	protected int resType;

	public Option_Cave_StoreSizeUp_Res_Confirm(CaveNPC npc, int resType) {
		this.npc = npc;
		this.resType = resType;
	}

	@Override
	public void doSelect(Game game, Player player) {

		if (!FaeryManager.isSelfCave(player, getNpc().getId())) {// 不是自己的庄园不能点击
			HINT_REQ hint_REQ = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_cave_010 + ":" + getNpc().getId());
			player.addMessageToRightBag(hint_REQ);
			return;
		}

		Cave cave = getNpc().getCave();
		if (cave == null) {
			HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_cave_007);
			player.addMessageToRightBag(hint);
			return;
		}
		CompoundReturn cr = cave.storeSizeUp(resType,player);
		String failreason = Translate.text_cave_001;
		if (!cr.getBooleanValue()) {
			switch (cr.getIntValue()) {
			case 1:
				failreason = Translate.text_cave_031;
				break;
			case 2:
				failreason = Translate.text_cave_013;
				break;
			case 3:
				failreason = Translate.text_cave_032;
				break;
			case 4:
				failreason = Translate.text_jiazu_095;
				break;
			default:
				break;
			}
		}
		player.sendError(failreason);
		if (CaveSubSystem.logger.isWarnEnabled()) {
			CaveSubSystem.logger.warn(player.getLogString() + "[提升" + FaeryConfig.FRUIT_RES_NAMES[resType] + "等级] [当前等级:" + cave.getStorehouse().getCurrResourceLevel(resType) + "] [" + failreason + "]");
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	
	public CaveNPC getNpc() {
		return npc;
	}

	public void setNpc(CaveNPC npc) {
		this.npc = npc;
	}
}