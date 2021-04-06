package com.fy.engineserver.menu.hunshi;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_hunshi_merge_bind extends Option {
	private long aimId;
	private long[] materialIds;
	private long silver;
	private int ronghezhi;
	private int silverType;
	private boolean isBind;

	public Option_hunshi_merge_bind() {
	}

	public Option_hunshi_merge_bind(long aimId, long[] materialIds, long silver, int ronghezhi, int silverType, boolean isBind) {
		super();
		this.aimId = aimId;
		this.materialIds = materialIds;
		this.silver = silver;
		this.ronghezhi = ronghezhi;
		this.silverType = silverType;
		this.isBind = isBind;
	}

	@Override
	public void doSelect(Game game, Player player) {
		HunshiManager.getInstance().hunshiMergeSure(player, aimId, materialIds, silver, ronghezhi, silverType, isBind);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
