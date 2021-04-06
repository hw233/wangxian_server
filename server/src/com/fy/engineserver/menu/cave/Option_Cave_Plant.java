package com.fy.engineserver.menu.cave;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.cave.resource.PlantCfg;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CAVE_QUERY_ALL_PLANT_RES;
import com.fy.engineserver.message.CAVE_QUERY_PLANT_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_Cave_Plant extends CaveOption implements NeedCheckPurview {
	public Option_Cave_Plant() {
		// TODO Auto-generated constructor stub
	}

	public static boolean useNew = true;

	@Override
	public void doSelect(Game game, Player player) {

		try {
			if (!FaeryManager.isSelfCave(player, getNpc().getId())) {// 不是自己的庄园不能点击种植
				HINT_REQ hint_REQ = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_cave_010 + ":" + getNpc().getId());
				player.addMessageToRightBag(hint_REQ);
				return;
			}
			// 是自己的庄园 则弹出种植列表
			Cave cave = getNpc().getCave();

			if (cave == null) {
				HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_cave_007);
				player.addMessageToRightBag(hint);
				return;
			}

			CaveBuilding cBuilding = cave.getCaveBuildingByNPCId(getNpc().getId());

			if (!(cBuilding instanceof CaveField)) {
				player.sendError(Translate.text_cave_017);
				return;
			}
			if (!useNew) {

				List<PlantCfg> fitPlant = new ArrayList<PlantCfg>();
				List<ResourceCollection> collections = new ArrayList<ResourceCollection>();
				for (Iterator<Integer> itor = FaeryManager.getInstance().getPlantGradeMap().keySet().iterator(); itor.hasNext();) {
					int grade = itor.next();
					for (PlantCfg cfg : FaeryManager.getInstance().getPlantGradeMap().get(grade)) {
						fitPlant.add(cfg);
						collections.add(cfg.getCost());
					}
				}

				CAVE_QUERY_ALL_PLANT_RES res = new CAVE_QUERY_ALL_PLANT_RES(GameMessageFactory.nextSequnceNum(), getNpc().getId(), fitPlant.toArray(new PlantCfg[0]), collections.toArray(new ResourceCollection[0]));
				player.addMessageToRightBag(res);
			} else {
				int fieldGrade = getNpc().getGrade();
				List<PlantCfg> fitPlant = new ArrayList<PlantCfg>();
				List<String> costs = new ArrayList<String>();
				for (Iterator<Integer> itor = FaeryManager.getInstance().getPlantGradeMap().keySet().iterator(); itor.hasNext();) {
					int grade = itor.next();
					for (PlantCfg cfg : FaeryManager.getInstance().getPlantGradeMap().get(grade)) {
						fitPlant.add(cfg);
						String moneyShow = "";
						if (cfg.getMoneyCost() > 0) {
							String color = "#FFFFFF";
							if (player.getSilver() < cfg.getMoneyCost()) {
								color = "#FF0000";
							}
							moneyShow = "<f color='" + color + "'>" +Translate.银子  + ":" + BillingCenter.得到带单位的银两(cfg.getMoneyCost()) +"</f>\n";
						}
						costs.add(moneyShow + cave.getCurrRes().getCompareShowColor(cfg.getCost()));
					}
				}

				CAVE_QUERY_PLANT_RES res = new CAVE_QUERY_PLANT_RES(GameMessageFactory.nextSequnceNum(), getNpc().getId(), fieldGrade, fitPlant.toArray(new PlantCfg[0]), costs.toArray(new String[0]));
				player.addMessageToRightBag(res);
			}
		} catch (Exception e) {
			FaeryManager.logger.error(player.getLogString() + "[Option_Cave_Plant][种植异常]", e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {

		if (!FaeryManager.isSelfCave(player, getNpc().getId())) {
			return false;
		}
		Cave cave = FaeryManager.getInstance().getCave(player);
		CaveBuilding caveBuilding = cave.getCaveBuildingByNPCId(getNpc().getId());
		if (caveBuilding == null || !(caveBuilding instanceof CaveField)) {
			return false;
		}

		CaveField caveField = (CaveField) caveBuilding;
		if (caveField.getAssartStatus() != FaeryConfig.FIELD_STATUS_UNPLANTING) {
			return false;
		}
		return true;
	}
}
