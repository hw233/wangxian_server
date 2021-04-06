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
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.silvercar.Option_Refresh_CarColor;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;

/**
 * 接取一个斩妖降魔
 * 
 * 
 */
public class Option_Accept_PeopleSearch extends Option implements NeedCheckPurview, NeedRecordNPC {

	private NPC npc;

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接取一个斩妖降魔] [进入]");
		}
		try {
			if (!PeopleSearchManager.systemOpen) {
				player.sendError(Translate.系统暂未开放更多精彩敬请期待);
				return;
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
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60);

			Option_Accept_PeopleSearch_Confirm confirm = new Option_Accept_PeopleSearch_Confirm(npc);
			confirm.setText(Translate.当然经验巨多);
			mw.setDescriptionInUUB(Translate.translateString(Translate.你确定消耗体力进行斩妖降魔么, new String [][]{{Translate.COUNT_1,String.valueOf(PeopleSearchManager.thew)}}));
			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.算了我没体力);
			mw.setOptions(new Option[] { confirm, cancel });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);

			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接受一个斩妖降魔] [弹出提示框] [剩余体力:" + player.getVitality() + "]");
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [接取异常]", e);
		}
	}

	@Override
	public boolean canSee(Player player) {
		if (PeopleSearchManager.getInstance().isOpen()) {
			return PeopleSearchManager.systemOpen && player.getPeopleSearch() == null;
		}
		return false;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}

	@Override
	public Option copy(Game game, Player p) {
		Option_Accept_PeopleSearch o = new Option_Accept_PeopleSearch();
		o.setOptionId(this.getOptionId());
		o.setIconId(this.getIconId());
		o.setColor(this.getColor());
		o.setText(this.getText());
		o.setOId(this.getOId());
		o.setNPC(this.getNPC());
		return o;
	}
}