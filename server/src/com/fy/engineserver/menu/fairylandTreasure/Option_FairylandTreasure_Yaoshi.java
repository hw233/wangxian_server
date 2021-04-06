package com.fy.engineserver.menu.fairylandTreasure;

import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasure;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_FairylandTreasure_Yaoshi extends Option {

	private FairylandTreasure fairylandTreasure;
	private String boxKeyName;

	public Option_FairylandTreasure_Yaoshi() {
	}

	public Option_FairylandTreasure_Yaoshi(FairylandTreasure fairylandTreasure, String boxKeyName) {
		super();
		this.fairylandTreasure = fairylandTreasure;
		this.boxKeyName = boxKeyName;
	}

	@Override
	public void doSelect(Game game, Player player) {
		FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
		if (fairylandTreasure.isDisappear()) {
			player.sendError(Translate.宝箱消失);
			return;
		}
		boolean succ = player.removeArticle(boxKeyName, "仙界宝箱");
		if (succ) {
			if (fairylandTreasure.npc != null) {
				game.removeSprite(fairylandTreasure.npc);
				fairylandTreasure.setDisappear(true);
			} else {
				FairylandTreasureManager.logger.warn("[仙界宝箱删除npc错误] [npc null]");
			}
			fairylandTreasure.setEffect(false);
			fairylandTreasure.setPickingPlayerId(player.getId());
			ftm.send_START_DRAW_RES(player, ftm.getFairylandBoxList().get(fairylandTreasure.getLevel()));
		} else {
			player.sendError(Translate.开宝箱失败);
		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public FairylandTreasure getFairylandTreasure() {
		return fairylandTreasure;
	}

	public void setFairylandTreasure(FairylandTreasure fairylandTreasure) {
		this.fairylandTreasure = fairylandTreasure;
	}

	public String getBoxKeyName() {
		return boxKeyName;
	}

	public void setBoxKeyName(String boxKeyName) {
		this.boxKeyName = boxKeyName;
	}

}
