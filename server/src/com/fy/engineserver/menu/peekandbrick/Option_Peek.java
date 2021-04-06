package com.fy.engineserver.menu.peekandbrick;

import com.fy.engineserver.activity.peekandbrick.PeekAndBrickManager;
import com.fy.engineserver.activity.peekandbrick.PeekTimeBar;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.util.TimeTool;

/**
 * 刺探
 * 
 * 
 */
public class Option_Peek extends Option {

	@Override
	public void doSelect(Game game, Player player) {
		try {
			byte currectCountry = game.country;// 当前地图的国家
			if (PeekAndBrickManager.logger.isDebugEnabled()) {
				PeekAndBrickManager.logger.debug(player.getLogString() + "[点击刺探,当前国家:{}][角色国家:{}]", new Object[] { currectCountry, player.getCountry() });
			}

			if (player.getCountry() == currectCountry) {
				player.sendError(Translate.text_peekAndBrick_005);
				return;
			}
			/** 离上次刺探的时间 */
			long lastPeekDiff = SystemTime.currentTimeMillis() - player.getLastPeekTime();

			long left = player.getRepeekCD() - lastPeekDiff;

			if (left > 0) {
				int secondLeft = (int) ((player.getRepeekCD() - lastPeekDiff) / TimeTool.TimeDistance.SECOND.getTimeMillis());
				secondLeft = secondLeft == 0 ? 1 : secondLeft;
				player.sendError(Translate.translateString(Translate.text_peekAndBrick_006, new String[][] { { Translate.STRING_1, String.valueOf(secondLeft) } }));
				return;
			}

			PeekAndBrickManager manager = PeekAndBrickManager.getInstance();
			String[] npcTask = manager.getCountryBepeektask()[currectCountry - 1];
			boolean hasNpcTask = false;
			String playerTaskName = "";

			for (String taskName : npcTask) {
				Task task = TaskManager.getInstance().getTask(taskName,player.getCountry());
				if (task != null) {
					int state = player.getTaskStatus(task);
					if (PeekAndBrickManager.logger.isDebugEnabled()) {
						PeekAndBrickManager.logger.debug(player.getLogString() + "[点击刺探][刺探任务:{}][状态:{}]]", new Object[] { taskName, state });
					}
					if (state == 1 || state == 2) {// 有接受状态的这个任务
						hasNpcTask = true;
						playerTaskName = taskName;
						break;
					}
				} else {
					if (PeekAndBrickManager.logger.isInfoEnabled()) PeekAndBrickManager.logger.info(player.getLogString() + "[点击刺探][任务不存在 :{}]", new Object[] { taskName });
				}
			}
			if (PeekAndBrickManager.logger.isDebugEnabled()) {
				PeekAndBrickManager.logger.debug(player.getLogString() + "[点击刺探][已有刺探任务:{}]", new Object[] { playerTaskName });
			}
			if (hasNpcTask) {
				NOTICE_CLIENT_READ_TIMEBAR_REQ req = new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), manager.getPeekTimeBarDelay(), Translate.刺探情报);
				player.addMessageToRightBag(req);
				// 读条获取BUFF
				PeekTimeBar timeBar = new PeekTimeBar(player, playerTaskName);
				player.getTimerTaskAgent().createTimerTask(timeBar, manager.getPeekTimeBarDelay(), TimerTask.type_领取BUFF);
				if (PeekAndBrickManager.logger.isInfoEnabled()) PeekAndBrickManager.logger.info(player.getLogString() + "[刺探成功---正在读条]");

			} else {
				player.sendError(Translate.text_peekAndBrick_007);
				return;
			}
		} catch (Exception e) {
			PeekAndBrickManager.logger.error("[刺探异常]", e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
