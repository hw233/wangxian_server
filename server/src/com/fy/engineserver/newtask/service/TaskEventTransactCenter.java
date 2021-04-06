package com.fy.engineserver.newtask.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;

/**
 * 任务事件处理中心
 * 
 * 
 */
public class TaskEventTransactCenter {

	private static TaskEventTransactCenter instance;

	/** 由game_server_context配置 */
	protected List<AbsTaskEventTransact> eventTransacts = new ArrayList<AbsTaskEventTransact>();
	/** 所有需要触发特殊事件的任务名和处理对象的映射<任务名,> */
	private Map<String, List<AbsTaskEventTransact>> taskEventTransactMap = new HashMap<String, List<AbsTaskEventTransact>>();

	public static TaskEventTransactCenter getInstance() {
		return instance;
	}

	public void setEventTransacts(List<AbsTaskEventTransact> eventTransacts) {
		String[] taskNames = null;
		for (AbsTaskEventTransact eventTransact : eventTransacts) {
			for (Taskoperation action : Taskoperation.values()) {
				taskNames = eventTransact.getWannaDealWithTaskNames(action);
				if (taskNames != null) {
					for (String taskName : taskNames) {
						if (!taskEventTransactMap.containsKey(taskName)) {
							taskEventTransactMap.put(taskName, new ArrayList<AbsTaskEventTransact>());
						}
						if (!taskEventTransactMap.get(taskName).contains(eventTransact)) {
							taskEventTransactMap.get(taskName).add(eventTransact);
						}
					}
				}
			}
		}
	}

	public void init() {
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}

	/**
	 * 处理任务
	 * @param action
	 * @param task
	 * @param player
	 * @param game
	 */
	public void dealWithTask(Taskoperation action, Task task, Player player, Game game) {
		List<AbsTaskEventTransact> eventTransacts = taskEventTransactMap.get(task.getName());
//		TaskSubSystem.logger.warn("[刺探任务测试-1] [task:"+task.getName()+"] ["+task.getCollections()+"] ["+eventTransacts+"] [playerName:"+player.getName()+"]");
		if (eventTransacts != null) {
			for (AbsTaskEventTransact eventTransact : eventTransacts) {
				eventTransact.dealWithTask(action, player, task, game);
			}
		}
	}
}
