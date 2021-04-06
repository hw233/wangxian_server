package com.fy.engineserver.menu.activity;

import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_TASK_JIEQU_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class Option_TakeOne_Task extends Option {
	
	private long taskId;
	
	static Random RANDOM = new Random();

	@Override
	public void doSelect(Game game, Player player) {
		
		Task task = TaskManager.getInstance().getTask(taskId);
		if (task == null) {
			player.sendError(Translate.text_task_016);
			return ;
		}
		CompoundReturn cr = player.takeOneTask(task, true, null);
		if (!cr.getBooleanValue()) {
			player.sendError(TaskSubSystem.getInstance().getInfo(cr.getIntValue()));
		} else {
			if (!JiazuManager2.instance.taskTask(player, taskId)) {
				player.sendError(Translate.任务不在列表内);
				return ;
			}
			player.addTaskByServer(task);
			byte res = 1;
			JiazuManager2.instance.refreshTaskList(player, true);		//刷新任务改为接取刷新
			JIAZU_TASK_JIEQU_RES resp = new JIAZU_TASK_JIEQU_RES(GameMessageFactory.nextSequnceNum(), res);
			player.addMessageToRightBag(resp);
		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

}
