package com.fy.engineserver.menu.vip;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.FIND_WAY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.vip.VipManager;

/**
 * 寻路Option
 */
public class FindNpc_Option extends Option {

	String mapName;
	int x;
	int y;

	public FindNpc_Option() {
		// TODO Auto-generated constructor stub
	}

	public FindNpc_Option(String mapName, int x, int y) {
		super();
		this.mapName = mapName;
		this.x = x;
		this.y = y;
	}

	public FindNpc_Option(String mapName, NPC npc) {
		this(mapName, npc.getX(), npc.getY());
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		super.doSelect(game, player);
		FIND_WAY_RES res = new FIND_WAY_RES(GameMessageFactory.nextSequnceNum(), mapName, x, y);
		player.addMessageToRightBag(res);
		if (VipManager.logger.isWarnEnabled()) {
			VipManager.logger.warn("[VIP] [记录信息] [" + player.getLogString() + "] [" + this.toString() + "]");
		}
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

	@Override
	public String toString() {
		return "FindNpc_Option [mapName=" + mapName + ", x=" + x + ", y=" + y + "]";
	}

}
