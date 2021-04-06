package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CAVE_QUERY_SELFFAERY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 查询自己庄园
 * 
 * 
 */
public class Option_Cave_QuerySelfFaery extends CaveOption implements FaeryConfig, NeedCheckPurview {

	// private Boolean selected = false;

	public Option_Cave_QuerySelfFaery() {

	}

	@Override
	public void doSelect(Game game, Player player) {
		if (player.getCurrentGame() == null || FaeryManager.GAME_NAME.equals(player.getCurrentGame().gi.getName())) {
			FaeryManager.logger.error(player.getLogString() + "[要进入仙府,其实已经在仙府内了,直接返回不处理]");
			return;
		}
		Cave cave = FaeryManager.getInstance().getCave(player);
		if (cave == null) {
			player.sendError(Translate.text_cave_007);
			return;
		}

		if (cave.getStatus() != CAVE_STATUS_OPEN && cave.getStatus() != CAVE_STATUS_LOCKED) {
			player.sendError(Translate.text_cave_048);
			return;
		}

		if (player.isFlying()) {
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(60);
			mw.setDescriptionInUUB(Translate.飞行坐骑不能进仙府);
			Option_Enter_Cave_Confirm confirm = new Option_Enter_Cave_Confirm(cave,null);
			confirm.setText(Translate.确定);
			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.取消);
			Option[] options = new Option[] { confirm, cancel };
			mw.setOptions(options);
			player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
		} else {
			player.markLastGameInfo();
			if (FaeryManager.logger.isWarnEnabled()) {
				FaeryManager.logger.warn(player.getLogString() + "[要进入自己家园] [mapResName:{}] [mapName:{}]", new Object[] { cave.getFaery().getGameName(), cave.getFaery().getMemoryMapName() });
			}
			game.playerLeaveGame(player);
			CAVE_QUERY_SELFFAERY_RES res = new CAVE_QUERY_SELFFAERY_RES(GameMessageFactory.nextSequnceNum(), cave.getFaery().getGameName(), cave.getFaery().getMemoryMapName());
			player.addMessageToRightBag(res);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (player.getCaveId() <= 0) {
			return false;
		}
		Cave cave = FaeryManager.getInstance().getCave(player);
		return cave != null;
	}
}
