package com.fy.engineserver.menu.activity;

import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.npc.ForLuckFruitNPC;

public class Option_ForLuck extends Option implements Cloneable {
	private ForLuckFruitNPC npc;

	public ForLuckFruitNPC getNpc() {
		return npc;
	}

	public void setNpc(ForLuckFruitNPC npc) {
		this.npc = npc;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
