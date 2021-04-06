package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.exceptions.OutOfMaxLevelException;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CAVE_LEVEL_UP_MOTICE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class Option_Cave_LevelUp extends CaveOption implements FaeryConfig, NeedCheckPurview {

	boolean isDoor = false;
	public Option_Cave_LevelUp() {
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			Cave cave = getNpc().getCave();

			if (!FaeryManager.isSelfCave(player, getNpc().getId())) {// 不是自己的庄园不能点击升级
				HINT_REQ hint_REQ = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_cave_010 + ":" + getNpc().getId());
				player.addMessageToRightBag(hint_REQ);
				return;
			}
			CaveBuilding cBuilding = cave.getCaveBuildingByNPCId(getNpc().getId());
			// 是自己的庄园

			if (isDoor) {
				CompoundReturn info = null;
				try {
					info = FaeryManager.getInstance().getLvUpInfo(cave.getFence().getType(), cave.getFence().getGrade());
				} catch (OutOfMaxLevelException e) {
					HINT_REQ hint_REQ = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_cave_011);
					player.addMessageToRightBag(hint_REQ);
					return;
				}
				if (Cave.logger.isDebugEnabled()) {
					Cave.logger.debug(player.getLogString() + "[升级庄园建筑] [{}] [时间:{}] [NPCID:{}]", new Object[] { cave.getFence().getNpc().getName(), info.getIntValue() * 1000, cave.getFence().getNpc().getId() });
				}
				CAVE_LEVEL_UP_MOTICE_RES res = new CAVE_LEVEL_UP_MOTICE_RES(GameMessageFactory.nextSequnceNum(), cave.getFence().getNpc().getId(), FaeryManager.getBasename(cave.getFence().getNpc().getName()), cave.getFence().getGrade(), cave.getCurrRes(), (ResourceCollection) info.getObjValue(), info.getIntValue() * 1000, info.getStringValue());
				player.addMessageToRightBag(res);
			} else {
				CompoundReturn info = null;
				try {
					info = FaeryManager.getInstance().getLvUpInfo(cBuilding.getType(), cBuilding.getGrade());
				} catch (OutOfMaxLevelException e) {
					HINT_REQ hint_REQ = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_cave_011);
					player.addMessageToRightBag(hint_REQ);
					return;
				}
				if (Cave.logger.isDebugEnabled()) {
					Cave.logger.debug(player.getLogString() + "[升级庄园建筑] [{}] [时间:{}] [NPCID:{}]", new Object[] { cBuilding.getNpc().getName(), info.getIntValue() * 1000, getNpc().getId() });
				}
				CAVE_LEVEL_UP_MOTICE_RES res = new CAVE_LEVEL_UP_MOTICE_RES(GameMessageFactory.nextSequnceNum(), getNpc().getId(), FaeryManager.getBasename(getNpc().getName()), cBuilding.getGrade(), cave.getCurrRes(), (ResourceCollection) info.getObjValue(), info.getIntValue() * 1000, info.getStringValue());
				player.addMessageToRightBag(res);
			}

		} catch (Exception e) {
			CaveSubSystem.logger.error("[升级] [异常]", e);
		}
	}

	public boolean isDoor() {
		return isDoor;
	}

	public void setIsDoor(boolean isDoor) {
		this.isDoor = isDoor;
	}

	@Override
	public boolean canSee(Player player) {

		if (!FaeryManager.isSelfCave(player, getNpc().getId())) {
			return false;
		}
		Cave cave = FaeryManager.getInstance().getCave(player);
		CaveBuilding caveBuilding = cave.getCaveBuildingByNPCId(getNpc().getId());
		if (caveBuilding.getGrade() >= FaeryManager.getInstance().getMainCfgs().length) {
			return false;
		}
		if (caveBuilding instanceof CaveField) {
			if (((CaveField) caveBuilding).getAssartStatus() != FaeryConfig.FIELD_STATUS_UNPLANTING) {
				return false;
			}
		}
		return true;
	}
}
