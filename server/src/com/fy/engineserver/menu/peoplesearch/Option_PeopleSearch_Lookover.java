package com.fy.engineserver.menu.peoplesearch;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.peoplesearch.CountryNpc;
import com.fy.engineserver.activity.peoplesearch.PeopleSearch;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;

/**
 * 打探消息
 * 
 * 
 */
public class Option_PeopleSearch_Lookover extends Option {

	private NPC npc;

	public Option_PeopleSearch_Lookover(NPC npc) {
		super();
		this.npc = npc;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (player.getPeopleSearch() == null) {
			player.sendNotice(Translate.目前你没有任何目标要寻找);
			return;
		}
		if (!PeopleSearchManager.systemOpen) {
			player.sendError(Translate.系统暂未开放更多精彩敬请期待);
			return;
		}
		PeopleSearch peopleSearch = player.getPeopleSearch();
		CountryNpc countryNpc = new CountryNpc(npc, game);
		for (int i = 0; i < peopleSearch.getMessageNpc().length; i++) {
			if (peopleSearch.getMessageNpc()[i] != null && peopleSearch.getMessageNpc()[i].equals(countryNpc)) {
				// 找到了消息npc
				peopleSearch.getSnooped()[i] = true;
				peopleSearch.setDirty();
				int messageIndex = peopleSearch.getMessageIndex()[i];
				String message = peopleSearch.getPeopleTemplet().getDes()[i][messageIndex];
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(180);
				mw.setNpcId(npc.getId());
				StringBuffer sbf = new StringBuffer();
				sbf.append(Translate.最新情报);
				sbf.append("<f color='0x33CC00'>" + message + "</f>\n");
				sbf.append("<f>"+Translate.当前获得的所有情报+":</f>\n");
				for (int k = 0; k < peopleSearch.getSnooped().length; k++) {
					CountryNpc messageNPC = peopleSearch.getMessageNpc()[k];
					sbf.append("<f color='0xFF6600'>").append(CountryManager.得到国家名(messageNPC.getCountry())).append("</f><f color='0x33CC00'>").append(messageNPC.getMapName()).append("</f>");
					sbf.append("的<f color='0xFFFF00'>").append(messageNPC.getName()).append("</f>\n");
					if (peopleSearch.getSnooped()[k]) {// 已经打探到消息了
						sbf.append("<f color='0x33FF00'>").append(peopleSearch.getPeopleTemplet().getDes()[k][peopleSearch.getMessageIndex()[k]]).append("</f>\n");
					} else {
						sbf.append("<f color='0xFF0000'>还未打探</f>\n");
					}
				}
				mw.setDescriptionInUUB(sbf.toString());
				mw.setOptions(new Option[0]);

				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);

				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [找到了第" + i + "个消息NPC] [" + countryNpc.toString() + "] [消息索引是:" + messageIndex + "] [探听到的消息是:" + message + "]");
				}
				return;
			}
		}
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [在NPC身上没打探到任何消息] [" + countryNpc.toString() + "] [" + peopleSearch.toString() + "]");
		}
	}
}