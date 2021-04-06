package com.fy.engineserver.menu.peoplesearch;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.peoplesearch.CountryNpc;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 猜猜就是你
 * 
 * 
 */
public class Option_Guess_PeopleSearch_Confirm extends Option {

	private NPC npc;

	public Option_Guess_PeopleSearch_Confirm(NPC npc) {
		super();
		this.npc = npc;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (player.getPeopleSearch() == null) {
			player.sendNotice("你还没有要寻找的人!");
			return;
		}

		if (!PeopleSearchManager.systemOpen) {
			player.sendError(Translate.系统暂未开放更多精彩敬请期待);
			return;
		}

		CountryNpc countryNpc = new CountryNpc(npc, game);
		synchronized (player) {
			if (player.getPeopleSearch() == null) {
				player.sendNotice("目前你还没有要寻找的人!");
				return;
			}
			
			CompoundReturn cr = player.getPeopleSearch().guess(npc, game);
			player.sendError(cr.getStringValue());
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [点击NPC:" + npc.getName() + "] [结果:" + (cr.getBooleanValue() ? "正确" : "错误") + "] [返回:" + cr.getStringValue() + "]");
			}
		}
	}
}