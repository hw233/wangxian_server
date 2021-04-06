package com.fy.engineserver.menu.peoplesearch;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.peoplesearch.PeopleSearch;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchManager;
import com.fy.engineserver.activity.peoplesearch.PeopleTemplet;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.silvercar.Option_Refresh_CarColor;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;

/**
 * 接取一个斩妖降魔确认框
 * 
 * 
 */
public class Option_Accept_PeopleSearch_Confirm extends Option {

	private NPC npc;

	public Option_Accept_PeopleSearch_Confirm(NPC npc) {
		this.npc = npc;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接取一个斩妖降魔] [提示框] [进入]");
		}
		try {
			if (!PeopleSearchManager.systemOpen) {
				player.sendError(Translate.系统暂未开放更多精彩敬请期待);
				return;
			}
			if (!PeopleSearchManager.getInstance().isOpen()) {
				return ;
			}
			if (player.getLevel() < PeopleSearchManager.levelLimit) {
				player.sendError(Translate.translateString(Translate.你的等级不足斩妖除魔, new String[][] { { Translate.COUNT_1, String.valueOf(PeopleSearchManager.levelLimit) } }));
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接取一个斩妖降魔] [等级不足,不能接取]");
				}
				return;
			}

			if (player.getPeopleSearch() != null) {
				player.sendNotice(Translate.你已经接取了斩妖降魔);
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接取一个斩妖降魔] [已经有斩妖降魔了]");
				}
				return;
			}
			if (player.getTodaySearchePeopleNum() >= PeopleSearchManager.dailyNum) {
				player.sendError(Translate.你今天的斩妖降魔均已完成明天再来吧);
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接取一个斩妖降魔] [今天已经完成过了]");
				}
				return;
			}

			PeopleTemplet peopleTemplet = PeopleSearchManager.getInstance().getRandomPeopleTemplet(player.getCountry());
			if (peopleTemplet == null) {
				player.sendNotice(Translate.没有匹配的记录);
				ActivitySubSystem.logger.error(player.getLogString() + "[斩妖降魔] [接取一个斩妖降魔] [系统返回空] [国家:" + player.getCountry() + "]");
				return;
			}
			synchronized (player) {
				if (player.getPeopleSearch() != null) {
					player.sendNotice(Translate.你已经接取了斩妖降魔);
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接取一个斩妖降魔] [已经有斩妖降魔了,怀疑是重复点击菜单]");
					}
					return;
				}
				if (player.getVitality() < PeopleSearchManager.thew) {
					player.sendError(Translate.translateString(Translate.你的体力不足斩妖除魔, new String[][]{{Translate.COUNT_1,String.valueOf(PeopleSearchManager.thew)}}));
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接取一个斩妖降魔] [体力不足] [当前体力:" + player.getVitality() + "]");
					}
					return;
				}
				player.subVitality(PeopleSearchManager.thew, "斩妖除魔");
				player.setPeopleSearch(new PeopleSearch(peopleTemplet, player));
				player.setLastSearchPeopleTime(SystemTime.currentTimeMillis());
				player.addPeopleSearchNum();

				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60000);
				mw.setTitle(Translate.你可以经常向我询问寻人进度);
				mw.setDescriptionInUUB(player.getPeopleSearch().getNotice());
				mw.setNpcId(npc.getId());
				mw.setOptions(new Option[0]);
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接受一个斩妖降魔成功] [扣除体力:" + PeopleSearchManager.thew + "] [剩余体力:" + player.getVitality() + "] [" + player.getPeopleSearch().getPeopleTemplet().getTarget().toString() + "]");
				}
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接取异常]", e);
		}
	}

	public NPC getNPC() {
		return npc;
	}

	public void setNPC(NPC npc) {
		this.npc = npc;
	}

	@Override
	public Option copy(Game game, Player p) {
		Option_Accept_PeopleSearch_Confirm o = new Option_Accept_PeopleSearch_Confirm(this.getNPC());
		o.setOptionId(this.getOptionId());
		o.setIconId(this.getIconId());
		o.setColor(this.getColor());
		o.setText(this.getText());
		o.setOId(this.getOId());
		return o;
	}
}