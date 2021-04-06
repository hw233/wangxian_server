package com.fy.engineserver.menu.hunshi;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.HunshiEntity;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_hunshi_jianding2 extends Option implements NeedCheckPurview {
	private HunshiEntity he;
	private long materialId;

	public Option_hunshi_jianding2() {

	}

	public Option_hunshi_jianding2(HunshiEntity he, long materialId) {
		this.he = he;
		this.materialId = materialId;
	}

	@Override
	public void doSelect(Game game, Player player) {
		HunshiManager.getInstance().jianDingConfirm2(player, he, materialId, true);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (player.getLevel() < 151) {
			return false;
		}
		return true;
	}
}
