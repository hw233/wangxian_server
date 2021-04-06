package com.fy.engineserver.newtask.service;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.sprite.Player;

public class TestEventTransact extends AbsTaskEventTransact {

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case accept:
			return new String[] { "仙缘" };
			// case deliver:
			// return new String[] { "同门之争", "明月的作为" };
			// case done:
			// return new String[] { "明月的作为" };
			// case drop:
			// return new String[] {};
		}
		return null;
	}

	public void init() {

	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
		String[] taskNames = getWannaDealWithTaskNames(Taskoperation.accept);
		if (taskNames != null) {
			for (String taskName : taskNames) {
				if (task.getName().equals(taskName)) {
					player.sendError("你接受了任务:" + taskName);
				}
			}
		}
	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		String[] taskNames = getWannaDealWithTaskNames(Taskoperation.accept);
		if (taskNames != null) {
			for (String taskName : taskNames) {
				if (task.getName().equals(taskName)) {
					player.sendError("你完成了任务[" + taskName + "]快去找[" + task.getEndNpc() + "]交付任务");
				}
			}
		}

	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {

	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}
}
