package com.fy.engineserver.newtask.service;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public abstract class AbsTaskEventTransact implements TaskEventTransact {

	@Override
	public CompoundReturn dealWithTask(Taskoperation action, Player player, Task task, Game game) {
		switch (action) {
		case accept:
			handleAccept(player, task, game, action);
			break;
		case done:
			handleDone(player, task, game, action);
			break;
		case drop:
			handleDrop(player, task, game, action);
			break;
		case deliver:
			handleDeliver(player, task, game, action);
			break;
		}
		return null;
	}

	public abstract String[] getWannaDealWithTaskNames(Taskoperation action);

	private void handleAccept(Player player, Task task, Game game, Taskoperation action) {
		if (getWannaDealWithTaskNames(action) != null) {
			for (String taskName : getWannaDealWithTaskNames(action)) {
				if (taskName.equals(task.getName())) {
					handleAccept(player, task, game);
					break;
				}
			}
		}
	}

	private void handleDone(Player player, Task task, Game game, Taskoperation action) {
		if (getWannaDealWithTaskNames(action) != null) {
			for (String taskName : getWannaDealWithTaskNames(action)) {
				if (taskName.equals(task.getName())) {
					handleDone(player, task, game);
					break;
				}
			}
		}

	}

	private void handleDeliver(Player player, Task task, Game game, Taskoperation action) {
//		TaskSubSystem.logger.warn("[刺探任务测试0] [task:"+task.getName()+"] ["+task.getCollections()+"] ["+(getWannaDealWithTaskNames(action)!=null?Arrays.toString(getWannaDealWithTaskNames(action)):"null")+"] [playerName:"+player.getName()+"]");
		if (getWannaDealWithTaskNames(action) != null) {
			for (String taskName : getWannaDealWithTaskNames(action)) {
				if (taskName.equals(task.getName())) {
					handleDeliver(player, task, game);
					break;
				}
			}
		}
	}

	private void handleDrop(Player player, Task task, Game game, Taskoperation action) {
		if (getWannaDealWithTaskNames(action) != null) {
			for (String taskName : getWannaDealWithTaskNames(action)) {
				if (taskName.equals(task.getName())) {
					handleDrop(player, task, game);
					break;
				}
			}
		}
	}

	public abstract void handleAccept(Player player, Task task, Game game);

	public abstract void handleDone(Player player, Task task, Game game);

	public abstract void handleDeliver(Player player, Task task, Game game);

	public abstract void handleDrop(Player player, Task task, Game game);
}
