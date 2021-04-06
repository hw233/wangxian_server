package com.fy.engineserver.newtask.service;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.tengxun.TengXunDataManager;
import com.fy.engineserver.util.ServiceStartRecord;

/**
 * 开启体力值任务
 * 
 * 
 */
public class ThewOpenTaskEventTransact extends AbsTaskEventTransact {

	private String[] taskNames;

	private TaskManager taskManager;

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case deliver:
			return taskNames;
		default:
			break;
		}
		return null;
	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {

	}

	@Override
	public void handleDone(Player player, Task task, Game game) {

	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		player.setNeedAddVitality(true);
		player.addVitality(PlayerManager.VITALITY_EVERYDAY_ADD + TengXunDataManager.instance.getAddVitality(player));
		player.setLastAddVitalityTime(SystemTime.currentTimeMillis());
		player.sendError(Translate.translateString(Translate.text_task_041, new String[][] { { Translate.COUNT_1, String.valueOf(PlayerManager.VITALITY_EVERYDAY_ADD) } }));

		// 光效:

		ParticleData[] particleDatas = new ParticleData[1];
		particleDatas[0] = new ParticleData(player.getId(), "任务光效/激活体力文字", -1, 2, 1, 1);
		// 任务光效/激活体力文字
		NOTICE_CLIENT_PLAY_PARTICLE_RES res = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
		player.addMessageToRightBag(res);

		if (GamePlayerManager.logger.isInfoEnabled()) {
			GamePlayerManager.logger.info(player.getLogString() + "[完成任务:{}] [激活了体力值] [获得体力值:{}]", new Object[] { task.getName(), PlayerManager.VITALITY_EVERYDAY_ADD });
		}
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {

	}

	public String[] getTaskNames() {
		return taskNames;
	}

	public void setTaskNames(String[] taskNames) {
		this.taskNames = taskNames;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public void init() {
		
		taskNames = new String[] { taskManager.openThewTaskName };
		ServiceStartRecord.startLog(this);
	}
}
