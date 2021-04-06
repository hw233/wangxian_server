package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.resource.ResourceLevelCfg;
import com.fy.engineserver.homestead.cave.resource.StorehouseCfg;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 提升仓库的是食物等级
 * 
 * 
 */
public class Option_Cave_StoreSizeUp_Food extends CaveOption implements FaeryConfig,
		NeedCheckPurview {

	public Option_Cave_StoreSizeUp_Food() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSelect(Game game, Player player) {

		try {
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

			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);

			int resLevel = cave.getStorehouse().getCurrResourceLevel(FaeryConfig.FRUIT_RES_FOOD);
			if (resLevel >= FaeryConfig.BUILDING_MAX_LEVEL) {
				player.sendError(Translate.text_cave_110);
				return;
			}
			try {
				int tL = cave.getCaveBuilding(Cave.CAVE_BUILDING_TYPE_STORE).getGrade();
				int reLv = FaeryManager.getInstance().getStoreCfgs()[tL].getCurrResourceLevel(FaeryConfig.FRUIT_RES_FOOD);
				if (resLevel >= reLv ) {
					player.sendError(Translate.仓库等级不足);
					return;
				}
			} catch (Exception e) {
				CaveSubSystem.logger.warn("["+player.getLogString()+"] [升级食物存储空间] [异常]", e);
			}
			ResourceLevelCfg cfg = FaeryManager.getInstance().getResLvCfg()[resLevel - 1];

			mw.setDescriptionInUUB(Translate.translateString(Translate.text_cave_111, new String[][] { { Translate.STRING_1, FaeryConfig.FRUIT_RES_NAMES[FaeryConfig.FRUIT_RES_FOOD] }, { Translate.COUNT_1, String.valueOf((++resLevel)) }, { Translate.STRING_2, cfg.getLvUpCost().toString() } }));

			Option_Cave_StoreSizeUp_Res_Confirm confirm = new Option_Cave_StoreSizeUp_Res_Confirm(getNpc(), FaeryConfig.FRUIT_RES_FOOD);
			confirm.setText(Translate.确定);
			Option_Cancel cancel = new Option_Cancel();
			mw.setOptions(new Option[] { confirm, cancel });

			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
		} catch (Exception e) {
			CaveSubSystem.logger.error(player.getLogString() + "[]", e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		return FaeryManager.isSelfCave(player, getNpc().getId());
	}
}
