package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_chant_slow;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.HarvestTimeBar;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.TimeTool;

/**
 * 收获
 * 
 * 
 */
public class Option_Cave_Harvest extends CaveOption implements NeedCheckPurview {

	public Option_Cave_Harvest() {

	}

	@Override
	public void doSelect(Game game, Player player) {
		Cave cave = getNpc().getCave();
		if (cave == null) {
			player.sendError(Translate.text_cave_047);
			return;
		}
		cave.setRewardTimes(1);
		boolean isSelfCave = FaeryManager.isSelfCave(player, getNpc().getId());
		if (!isSelfCave) {
			HarvestTimeBar harvest = new HarvestTimeBar(cave, getNpc(), player, null);
			long time = FaeryManager.getInstance().getStealTime(player);
			player.getTimerTaskAgent().createTimerTask(harvest, FaeryManager.getInstance().getStealTime(player), TimerTask.type_采集);
			NOTICE_CLIENT_READ_TIMEBAR_REQ timebar_REQ = new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), time, Translate.偷偷);
			player.addMessageToRightBag(timebar_REQ);
		} else {
			CompoundReturn cr = cave.pickFruit(player, getNpc().getId());
			String failreason = Translate.text_cave_001;
			if (!cr.getBooleanValue()) {
				switch (cr.getIntValue()) {
				case 1:
					failreason = Translate.text_cave_002;
					break;
				case 2:

					failreason = Translate.text_cave_021;
					break;
				case 3:
					failreason = Translate.text_cave_050;

					break;
				case 4:
					failreason = Translate.text_cave_050;

					break;
				case 5:
					failreason = Translate.text_cave_051;

					break;
				case 6:
					failreason = Translate.text_cave_052;

					break;
				case 7:
					failreason = Translate.text_cave_053;
					break;
				case 8:
					failreason = Translate.text_cave_099;
					break;
				default:
					break;
				}
			}
			if (Translate.text_cave_001.equals(failreason)) {
				player.sendNotice(failreason);
			} else {
				player.sendError(failreason);
			}
		}

	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		return true;
	}
}
