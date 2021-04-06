package com.fy.engineserver.menu.peekandbrick;

import java.util.Arrays;

import com.fy.engineserver.activity.peekandbrick.BrickTimeBar;
import com.fy.engineserver.activity.peekandbrick.PeekAndBrickManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;

/**
 * 偷砖
 * 
 * 
 */
public class Option_Brick extends Option {

	/**
	 * 在有没有任务的情况下也可以<BR/>
	 * 但是有些条件<BR/>
	 * 1.没有任何偷砖任务的时候，随便搞;<BR/>
	 * 2.当有任务，且任务完成了。只能偷指定NPC的砖<BR/>
	 */
	@Override
	public void doSelect(Game game, Player player) {
		try {
			int currectCountry = player.getCurrentGameCountry();
			int playerCountry = player.getCountry();

			if (currectCountry == playerCountry) {
				player.sendError(Translate.text_peekAndBrick_003);
				return;
			}

			// 做能不能偷 的判断
			PeekAndBrickManager manager = PeekAndBrickManager.getInstance();
			String[] thisCountryTasks = manager.getCountryBebricktask()[currectCountry - 1];

			/** 是否已经有完成状态的偷砖任务 */
			boolean hasDoneTask = false;
			String doneTaskName = "";

			String[] canAcceptTasks = manager.getCountryBrickTasks().get((byte) (playerCountry));
			if(PeekAndBrickManager.logger.isInfoEnabled())
				PeekAndBrickManager.logger.info(player.getLogString() + "[在国家{}][可能接到的任务列表:{}]", new Object[] { currectCountry, Arrays.toString(canAcceptTasks) });

			for (String taskName : canAcceptTasks) {
				Task task = TaskManager.getInstance().getTask(taskName,player.getCountry());
				if (task != null) {
					int stat = player.getTaskStatus(task);
					if(PeekAndBrickManager.logger.isInfoEnabled())
						PeekAndBrickManager.logger.info(player.getLogString() + "[在国家{}][任务:{}][状态:{}]", new Object[] { currectCountry, taskName, stat });
					if (stat == TaskConfig.TASK_STATUS_COMPLETE) {
						hasDoneTask = true;
						doneTaskName = task.getName();
						break;
					}
				}

			}
			if(PeekAndBrickManager.logger.isInfoEnabled())
				PeekAndBrickManager.logger.info(player.getLogString() + "[偷砖][是否有已经完成的偷砖任务:{}][任务名字 ：{}]", new Object[] { hasDoneTask, doneTaskName });
			if (hasDoneTask) {
				boolean isDoneThisCountryTask = false;// 是否已经完成的是当前这个国家的偷砖任务。如果是，则可以再偷，如果不是，则不能偷了
				for (String thisCountryTask : thisCountryTasks) {
					if (thisCountryTask.equals(doneTaskName)) {
						isDoneThisCountryTask = true;
						break;
					}
				}
				if (!isDoneThisCountryTask) {
					player.sendError(Translate.text_peekAndBrick_004);
					return;
				}
			}

			BrickTimeBar timeBar = new BrickTimeBar(player,doneTaskName);
			player.getTimerTaskAgent().createTimerTask(timeBar, manager.getBrickTimeBarDelay(), TimerTask.type_领取BUFF);

			NOTICE_CLIENT_READ_TIMEBAR_REQ req = new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), manager.getBrickTimeBarDelay(), Translate.挖墙脚等红杏);
			player.addMessageToRightBag(req);
		} catch (Exception e) {
			PeekAndBrickManager.logger.error(player.getLogString() + "[点击偷砖异常]", e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
