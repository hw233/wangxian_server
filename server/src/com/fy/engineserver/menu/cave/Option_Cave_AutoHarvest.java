package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.AutoHarvestTimeBar;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.cave.PlantStatus;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.util.TimeTool;

public class Option_Cave_AutoHarvest extends CaveOption implements NeedCheckPurview {

	@Override
	public void doSelect(Game game, Player player) {
		try {
			Cave cave = getNpc().getCave();
			if (cave == null) {
				player.sendError(Translate.text_cave_047);
				return;
			}
			boolean isSelfCave = FaeryManager.isSelfCave(player, getNpc().getId());
			if (!isSelfCave) {
				player.sendError(Translate.text_cave_060);
			} else {
				CaveBuilding cBuilding = cave.getCaveBuildingByNPCId(getNpc().getId());
				CaveField caveField = (CaveField) cBuilding;
				PlantStatus plantStatus = caveField.getPlantStatus();
				plantStatus.setAutoFail(false);
				AutoHarvestTimeBar autoHarvestTimeBar = new AutoHarvestTimeBar(cave, getNpc(), player);
				player.getTimerTaskAgent().createTimerTask(autoHarvestTimeBar, TimeTool.TimeDistance.SECOND.getTimeMillis(), TimerTask.type_采集);
				player.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), TimeTool.TimeDistance.SECOND.getTimeMillis(), Translate.一键收获));
			}
		} catch (Exception e) {
			CaveSubSystem.logger.error(player.getLogString() + "[一键收获]", e);
		}

	}

	@Override
	public boolean canSee(Player player) {
		boolean isSelfCave = FaeryManager.isSelfCave(player, getNpc().getId());
		return isSelfCave;
	}
}
