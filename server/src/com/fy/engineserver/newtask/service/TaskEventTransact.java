package com.fy.engineserver.newtask.service;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public interface TaskEventTransact {

	/** 当任务触发某个状态是处理相关操作 */
	CompoundReturn dealWithTask(Taskoperation action, Player player, Task task, Game game);

	/** 得到想要处理的任务名字 */
	String[] getWannaDealWithTaskNames(Taskoperation action);

	/** 处理接任务接取事件 */
	void handleAccept(Player player, Task task, Game game);

	/** 处理接任务完成事件 */
	void handleDone(Player player, Task task, Game game);

	/** 处理接任务交付事件 */
	void handleDeliver(Player player, Task task, Game game);

	/** 处理接任务放弃事件 */
	void handleDrop(Player player, Task task, Game game);
}
