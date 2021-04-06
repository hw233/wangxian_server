package com.fy.engineserver.newtask.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEXT_TASK_OPEN;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;

public class MastLineTaskOpenNextTaskEventTransact extends AbsTaskEventTransact implements
		TaskConfig {

	private static MastLineTaskOpenNextTaskEventTransact instance;
	private TaskManager taskManager;

	public TaskManager getTaskManager() {
		return taskManager;
	}

	private String[] mastLineTaskName;

	public static MastLineTaskOpenNextTaskEventTransact getInstance() {
		return instance;
	}

	public void init() {
		
		List<String> taskNames = new ArrayList<String>();
		for (Iterator<Long> itor = taskManager.getTaskIdMap().keySet().iterator(); itor.hasNext();) {
			Task task = taskManager.getTaskIdMap().get(itor.next());
			if (task != null && task.getShowType() == TASK_SHOW_TYPE_MAIN) {// 是主线任务
				List<Task> nextList = taskManager.getnextTask(task.getName());
				if (nextList != null && nextList.size() > 0) {
					Task next = nextList.get(0);
					if (next == null) {
						if(TaskManager.logger.isWarnEnabled())
							TaskManager.logger.warn("[加载主线任务串][{}:的后续任务不存在]", new Object[] { task.getName() });
						continue;
					}
					try {
						if (TaskManager.disconnectionMainTask.contains(next.getName())) {
							if(TaskManager.logger.isInfoEnabled())
								TaskManager.logger.info("[任务断开提示下一个:{}]", new Object[] { next.getName() });
							continue;
						}
						if (next.getStartMap() != null && !next.getStartMap().isEmpty()) {
							if (next.getStartMap().equals(task.getEndMap()) && next.getStartNpc().equals(task.getEndNpc())) {
								taskNames.add(task.getName());
							}
						}
					} catch (Exception e) {
						TaskManager.logger.error("[加载任务异常]", e);
						continue;
					}
				}

			}
		}
		setMastLineTaskName(taskNames.toArray(new String[0]));
		instance = this;
		ServiceStartRecord.startLog(this);
	}

	public String[] getMastLineTaskName() {
		return mastLineTaskName;
	}

	public void setMastLineTaskName(String[] mastLineTaskName) {
		this.mastLineTaskName = mastLineTaskName;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		if (action.equals(Taskoperation.deliver)) {
			return mastLineTaskName;
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
		List<Task> list = taskManager.getnextTask(task.getName());
		if (list != null && list.size() > 0) {
			Task task2 = null;
			for (Task tk : list) {
				if (TaskManager.workFit(player, tk)) {
					if(TaskManager.countryFit(player, tk)){
						task2 = tk;
						break;
					}
				}
			}
			if (task2 != null) {
				NEXT_TASK_OPEN res = new NEXT_TASK_OPEN(GameMessageFactory.nextSequnceNum(), task2, task2.getTargets(), task2.getPrizes());
				player.addMessageToRightBag(res);
				if (TaskSubSystem.logger.isInfoEnabled()) {
					TaskSubSystem.logger.info(player.getLogString() + "[弹出任务][完成了:{}][弹出了:{}]", new Object[] { task.getName(), task2.getName() });
				}
			} else {
				if (TaskSubSystem.logger.isInfoEnabled()) {
					TaskSubSystem.logger.info(player.getLogString() + "[弹出任务] [错误] [没有职业符合的任务]");
				}
			}
		}
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {

	}

}
