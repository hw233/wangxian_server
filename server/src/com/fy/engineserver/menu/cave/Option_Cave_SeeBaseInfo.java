package com.fy.engineserver.menu.cave;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.exceptions.OutOfMaxLevelException;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CAVE_INFO_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 查看洞府基本信息
 * 
 * 
 */
public class Option_Cave_SeeBaseInfo extends CaveOption implements FaeryConfig, NeedCheckPurview {

	public Option_Cave_SeeBaseInfo() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSelect(Game game, Player player) {
		Cave cave = getNpc().getCave();
		if (cave == null) {
			return;
		}
		String[] buildName = new String[cave.getBuildings().size()];
		String[] depend = new String[cave.getBuildings().size()];
		int[] types = new int[cave.getBuildings().size()];
		int[] grades = new int[cave.getBuildings().size()];
		int index = 0;

		ResourceCollection[] lvupCost = new ResourceCollection[cave.getBuildings().size()];

		List<CaveBuilding> order = new ArrayList<CaveBuilding>();

		for (Iterator<Long> itor = cave.getBuildings().keySet().iterator(); itor.hasNext();) {
			long id = itor.next();
			CaveBuilding building = cave.getBuildings().get(id);
			order.add(building);
		}

		Collections.sort(order);
		try {
			for (CaveBuilding building : order) {
				buildName[index] = CAVE_BUILDING_NAMES[building.getType()];
				grades[index] = building.getGrade();
				types[index] = building.getType();
				CompoundReturn info = FaeryManager.getInstance().getLvUpInfo(building.getType(), building.getGrade());
				lvupCost[index] = (ResourceCollection) info.getObjValue();
				depend[index] = info.getStringValue();
				index++;
			}
		} catch (OutOfMaxLevelException e) {
			FaeryManager.logger.error(player.getLogString() + "[查看门牌] [异常]", e);
		} catch (Exception e) {
			FaeryManager.logger.error(player.getLogString() + "[查看门牌] [异常]", e);
		}
		CAVE_INFO_RES res = new CAVE_INFO_RES(GameMessageFactory.nextSequnceNum(), cave.getCurrRes(), cave.getMaintenanceCost(), cave.getCurrMaxResource(), buildName, types, grades, lvupCost, depend);
		player.addMessageToRightBag(res);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		return FaeryManager.isSelfCave(player, getNpc().getId());
	}
}
