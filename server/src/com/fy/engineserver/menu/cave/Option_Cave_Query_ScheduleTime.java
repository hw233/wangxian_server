package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;

/**
 * 查看增加队列剩余时间
 * 
 *
 */
public class Option_Cave_Query_ScheduleTime extends CaveOption implements NeedCheckPurview {

	public Option_Cave_Query_ScheduleTime() {

	}

	@Override
	public void doSelect(Game game, Player player) {
		boolean isSelf = FaeryManager.isSelfCave(player, getNpc().getId());
		if (!isSelf) {
			player.sendError(Translate.text_cave_016);
			return;
		}
		Cave cave = FaeryManager.getInstance().getCave(player);

		if (cave.getIncreaseScheduleNum() <= 0) {
			player.sendError(Translate.text_cave_082);
			return;
		}
		String time = TimeTool.instance.getShowTime((cave.getIncreaseScheduleStart() + cave.getIncreaseScheduleLast()) - SystemTime.currentTimeMillis(),TimeDistance.DAY);
		player.sendError(Translate.text_cave_083 + time);
	}

	@Override
	public boolean canSee(Player player) {
		boolean isSelf = FaeryManager.isSelfCave(player, getNpc().getId());
		if (!isSelf) {
			return false;
		}
		Cave cave = FaeryManager.getInstance().getCave(player);
		if (cave.getIncreaseScheduleNum() > 0) {
			return true;
		}
		return false;
	}
}
