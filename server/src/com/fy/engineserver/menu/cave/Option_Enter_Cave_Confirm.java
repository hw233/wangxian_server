package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CAVE_QUERY_SELFFAERY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

/**
 * 回仙府下飞行坐骑确认
 * @author Administrator
 *         2014-6-4
 * 
 */
public class Option_Enter_Cave_Confirm extends Option {

	private Cave cave;

	private String articleName;

	public Option_Enter_Cave_Confirm(Cave cave, String articleName) {
		this.cave = cave;
		this.articleName = articleName;
	}

	@Override
	public void doSelect(Game game, Player player) {
		boolean canEnter = false;

		if (articleName == null) {
			canEnter = true;
		} else {
			canEnter = player.removeArticle(articleName, "进入仙府删除");
		}
		if (canEnter) {
			player.markLastGameInfo();
			if (FaeryManager.logger.isWarnEnabled()) {
				FaeryManager.logger.warn(player.getLogString() + "[要进入自己家园] [mapResName:{}] [mapName:{}]", new Object[] { cave.getFaery().getGameName(), cave.getFaery().getMemoryMapName() });
			}
			game.playerLeaveGame(player);
			CAVE_QUERY_SELFFAERY_RES res = new CAVE_QUERY_SELFFAERY_RES(GameMessageFactory.nextSequnceNum(), cave.getFaery().getGameName(), cave.getFaery().getMemoryMapName());
			player.addMessageToRightBag(res);
		} else {
			player.sendError(Translate.translateString(Translate.text_cave_101, new String[][] { { Translate.STRING_1, articleName } }));
		}
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
