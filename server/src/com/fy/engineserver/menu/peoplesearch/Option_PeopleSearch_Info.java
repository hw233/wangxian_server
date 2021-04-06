package com.fy.engineserver.menu.peoplesearch;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.peoplesearch.PeopleSearch;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.TimeTool;

/**
 * 查看当前斩妖降魔的具体信息
 * 
 * 
 */
public class Option_PeopleSearch_Info extends Option implements NeedCheckPurview, NeedRecordNPC {

	private NPC npc;

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			if (!PeopleSearchManager.systemOpen) {
				player.sendError(Translate.系统暂未开放更多精彩敬请期待);
				return;
			}

			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [查看斩妖降魔] [进入]");
			}
			PeopleSearch peopleSearch = player.getPeopleSearch();
			if (peopleSearch == null) {
				player.sendNotice("你还没有进行中的斩妖降魔");
				return;
			}

			MenuWindow wm = WindowManager.getInstance().createTempMenuWindow((int) TimeTool.TimeDistance.MINUTE.getTimeMillis());
			wm.setTitle("你可以经常向我询问寻人进度");
			wm.setDescriptionInUUB(peopleSearch.getNotice());
			wm.setNpcId(npc.getId());
			wm.setOptions(new Option[0]);

			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), wm, wm.getOptions());
			player.addMessageToRightBag(res);
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [查看斩妖降魔成功]");
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [查看斩妖降魔] [异常]", e);
		}
	}

	@Override
	public boolean canSee(Player player) {
		return PeopleSearchManager.systemOpen && player.getPeopleSearch() != null;
	}

	@Override
	public Option copy(Game game, Player p) {
		Option_PeopleSearch_Info o = new Option_PeopleSearch_Info();
		o.setOptionId(this.getOptionId());
		o.setIconId(this.getIconId());
		o.setColor(this.getColor());
		o.setText(this.getText());
		o.setOId(this.getOId());
		o.setNPC(this.getNPC());
		return o;
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}
}