package com.fy.engineserver.homestead.cave;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.sprite.TimerTaskAgent;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.TimeTool;

public class AutoHarvestTimeBar extends AbsCallbackable {
	Cave cave;
	CaveNPC npc;
	Player player;

	public AutoHarvestTimeBar(Cave cave, CaveNPC npc, Player player) {
		this.cave = cave;
		this.npc = npc;
		this.player = player;
	}

	@Override
	public void callback() {
		CompoundReturn cr = cave.pickFruit(player, npc.getId());
		String failreason = Translate.text_cave_001;
		CaveBuilding cBuilding = cave.getCaveBuildingByNPCId(npc.getId());
		CaveField caveField = (CaveField) cBuilding;
		if (caveField == null) {
			return;
		}
		PlantStatus plantStatus = caveField.getPlantStatus();
		if (!cr.getBooleanValue()) {
			plantStatus.setAutoFail(true);
			switch (cr.getIntValue()) {
			case 1:
				failreason = Translate.text_cave_002;
				break;
			case 2:

				failreason = Translate.text_cave_021;
				break;
			case 3:
				failreason = Translate.text_cave_050;

				break;
			case 4:
				failreason = Translate.text_cave_050;

				break;
			case 5:
				failreason = Translate.text_cave_051;

				break;
			case 6:
				failreason = Translate.text_cave_052;

				break;
			case 7:
				failreason = Translate.text_cave_053;

				break;

			default:
				break;
			}
		}
		if (!Translate.text_cave_001.equals(failreason)) {//失败才提示
			player.sendNotice(failreason);
			// CaveBuilding cBuilding = cave.getCaveBuildingByNPCId(npc.getId());
			// CaveField caveField = (CaveField) cBuilding;
			// PlantStatus plantStatus = caveField.getPlantStatus();
			// int leftNum = plantStatus.getLeftOutput();
			// if (leftNum > 0) {
			// AutoHarvestTimeBar next = new AutoHarvestTimeBar(cave, npc, player);
			// player.getTimerTaskAgent().createTimerTask(next, TimeTool.TimeDistance.SECOND.getTimeMillis(), TimerTask.type_采集);
			// player.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), TimeTool.TimeDistance.SECOND.getTimeMillis(), ""));
			// }
			player.sendError(failreason);
		}
	}

	@Override
	public void doNext() {
		if(TimerTaskAgent.logger.isInfoEnabled())
			TimerTaskAgent.logger.info("----------------[执行了下一个]-----------------");
		CaveBuilding cBuilding = cave.getCaveBuildingByNPCId(npc.getId());
		if (cBuilding == null) {
			return;
		}
		CaveField caveField = (CaveField) cBuilding;
		PlantStatus plantStatus = caveField.getPlantStatus();
		if (plantStatus.getLeftOutput() > 0 && !plantStatus.isAutoFail()) {
			AutoHarvestTimeBar autoHarvestTimeBar = new AutoHarvestTimeBar(cave, npc, player);
			player.getTimerTaskAgent().createTimerTask(autoHarvestTimeBar, TimeTool.TimeDistance.SECOND.getTimeMillis(), TimerTask.type_采集);
			player.addMessageToRightBag(new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), TimeTool.TimeDistance.SECOND.getTimeMillis(), Translate.一键收获));
		} else {
			if(TimerTaskAgent.logger.isInfoEnabled())
				TimerTaskAgent.logger.info("----------------[执行了下一个]-----------------剩余大于0:{}采集失败{}", new Object[] { plantStatus.getLeftOutput() > 0 });// , !plantStatus.isAutoFail()
																																					// });
		}
	}
}
