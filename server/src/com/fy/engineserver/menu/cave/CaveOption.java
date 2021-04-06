package com.fy.engineserver.menu.cave;

import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.CaveNPC;

public class CaveOption extends Option implements NeedCheckPurview, Cloneable {

	protected CaveNPC npc;

	public CaveNPC getNpc() {
		return npc;
	}

	public void setNpc(CaveNPC npc) {
		this.npc = npc;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
