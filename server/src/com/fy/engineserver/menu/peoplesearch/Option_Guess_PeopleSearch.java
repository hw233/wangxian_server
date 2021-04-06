package com.fy.engineserver.menu.peoplesearch;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.peoplesearch.CountryNpc;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;

/**
 * 猜猜就是你
 * 
 * 
 */
public class Option_Guess_PeopleSearch extends Option {

	private NPC npc;

	public Option_Guess_PeopleSearch(NPC npc) {
		super();
		this.npc = npc;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (player.getPeopleSearch() == null) {
			player.sendNotice(Translate.你还没有接取斩妖降魔);
			return;
		}
		if (!PeopleSearchManager.systemOpen) {
			player.sendError(Translate.系统暂未开放更多精彩敬请期待);
			return;
		}
		synchronized (player) {
			if (player.getPeopleSearch() == null) {
				player.sendNotice(Translate.目前你还没有接取斩妖降魔);
				return;
			}
			CountryNpc countryNpc = new CountryNpc(npc, game);
			if(player.getPeopleSearch().getVisitedNpc().contains(countryNpc ) && !player.getPeopleSearch().isTarget(countryNpc)){
				player.sendError(Translate.寻人提示_你已经找过我一次了);
				return;
			}
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(10);
			mw.setDescriptionInUUB(Translate.translateString(Translate.斩妖提示, new String[][] { { Translate.STRING_1, npc.getName() }, { Translate.COUNT_1, String.valueOf(player.getPeopleSearch().getScore()) }, { Translate.COUNT_2, String.valueOf(PeopleSearchManager.maxScore) } }));
			Option_Guess_PeopleSearch_Confirm confirm = new Option_Guess_PeopleSearch_Confirm(npc);
			confirm.setText(Translate.恩就是他);
			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.我再想想);
			mw.setOptions(new Option[] { confirm, cancel });

			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);

			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [点击NPC:" + npc.getName() + "] [要接受一个寻人] [弹出确认框]");
			}
		}

	}
}