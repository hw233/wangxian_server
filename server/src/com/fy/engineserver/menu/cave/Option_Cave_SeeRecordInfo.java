package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveDynamic;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.CaveNPC;

/**
 * 查看驻地的动态.包括建筑成功,果实成熟,被偷记录
 * 
 * 
 */
public class Option_Cave_SeeRecordInfo extends CaveOption implements NeedCheckPurview {

	@Override
	public void doSelect(Game game, Player player) {

		CaveNPC npc = getNpc();
		if (npc == null) {
			if (FaeryManager.logger.isWarnEnabled()) {
				FaeryManager.logger.warn(player.getLogString() + "[查看庄园动态] [NPC不存在]");
			}
			return;
		}

		Cave cave = npc.getCave();

		CaveBuilding cbBuilding = cave.getCaveBuildingByNPCId(npc.getId());
		if (cbBuilding.getType() != FaeryConfig.CAVE_BUILDING_TYPE_DOORPLATE) {
			if (FaeryManager.logger.isWarnEnabled()) {
				FaeryManager.logger.warn(player.getLogString() + "[查看庄园动态] [NPC不是门牌] [{}]", new Object[] { npc.getName() });
			}
			return;
		}
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setNpcId(npc.getId());
		mw.setOptions(new Option[0]);
		StringBuffer descriptionInUUB = new StringBuffer();
		if (cave.getRecordList() == null || cave.getRecordList().size() == 0) {
			mw.setDescriptionInUUB(Translate.text_cave_097);
		} else {
			for (CaveDynamic record : cave.getRecordList()) {
				descriptionInUUB.append(record.getDynamic()).append("\n");
			}
			mw.setDescriptionInUUB(descriptionInUUB.toString());
		}
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		return true;
	}

}
