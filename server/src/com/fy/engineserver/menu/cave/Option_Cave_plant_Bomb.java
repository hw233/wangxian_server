package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.cave.CaveFieldBombConfig;
import com.fy.engineserver.homestead.cave.PlantStatus;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 放置炸弹
 * 存在于多个列表中
 * @author Administrator
 *         2014-5-14
 * 
 */
public class Option_Cave_plant_Bomb extends CaveOption implements NeedCheckPurview {

	private CaveFieldBombConfig bombConfig;

	public Option_Cave_plant_Bomb(CaveFieldBombConfig bombConfig) {
		super();
		this.bombConfig = bombConfig;
	}

	public CaveFieldBombConfig getBombConfig() {
		return bombConfig;
	}

	public void setBombConfig(CaveFieldBombConfig bombConfig) {
		this.bombConfig = bombConfig;
	}

	@Override
	public void doSelect(Game game, Player player) {
		Cave cave = FaeryManager.getInstance().getCave(player);
		CaveBuilding caveBuilding = cave.getCaveBuildingByNPCId(getNpc().getId());
		CaveField caveField = (CaveField) caveBuilding;
		CompoundReturn plantBomb = caveField.plantBomb(bombConfig,player);

		player.sendNotice(plantBomb.getStringValue());
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
		if (caveField.getPlantStatus() == null) {
			return false;
		}
		if (caveField.getPlantStatus().getOutShowStatus() == FaeryConfig.PLANT_AVATA_STATUS_GROWNUP) {
			return false;
		}
		return true;
	}
}
