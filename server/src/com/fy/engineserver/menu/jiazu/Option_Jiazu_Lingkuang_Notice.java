package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
/**
 * 灵矿战开始玩家选择传送
 * @author Administrator
 *
 */
public class Option_Jiazu_Lingkuang_Notice extends Option {
	/** 目标国家 */
	private byte country;
	/** 目标地图 */
	private String mapName;
	/** x坐标 */
	private int x;
	/** y坐标 */
	private int y;

	@Override
	public void doSelect(Game game, Player player) {
		try {
			if (player.getJiazuId() == 0) {
				player.sendError(Translate.你没有家族);
				return;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				player.sendError(Translate.你没有家族);
				return;
			}
			Game game1 = GameManager.getInstance().getGameByName(mapName, country);
			if (JiazuManager2.logger.isDebugEnabled()) {
				JiazuManager2.logger.debug("[新家族] [灵矿战开始传送玩家到灵矿地方] [game1 : " + game1 + "] [" + player.getLogString() + "]");
			}
			if (game1 != null) {
				game.transferGame(player, new TransportData(0, 0, 0, 0, game1.gi.name, x, y));
				if (JiazuManager2.logger.isDebugEnabled()) {
					JiazuManager2.logger.debug("[新家族] [灵矿战开始传送玩家到灵矿地方] [成功] [game1 : " + game1.gi.name + "] [" + player.getLogString() + "]");
				}
			}
		} catch (Exception e) {
			JiazuManager2.logger.error("[新家族] [灵矿战开始传送玩家] [异常] [" + player.getLogString() + "]", e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
