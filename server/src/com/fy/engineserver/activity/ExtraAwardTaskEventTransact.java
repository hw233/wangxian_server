package com.fy.engineserver.activity;

import java.util.Arrays;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.activity.exchange.ExchangeActivityManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;

public class ExtraAwardTaskEventTransact extends AbsTaskEventTransact {

	private String[] taskNames;
	public static ExtraAwardTaskEventTransact instance;

	public String[] getTaskNames() {
		return taskNames;
	}

	public void setTaskNames(String[] taskNames) {
		this.taskNames = taskNames;
	}

	public static ExtraAwardTaskEventTransact getInstance() {
		return instance;
	}

	public static void setInstance(ExtraAwardTaskEventTransact instance) {
		ExtraAwardTaskEventTransact.instance = instance;
	}

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
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		ExtraAwardActivity extraAwardActivity = ExchangeActivityManager.getInstance().getRightExtraAwardActivity(task.getName());
		if (extraAwardActivity != null) {
			if (extraAwardActivity.isThisServerFit() == null) {// 该服是否开放
				if (player.getLevel() > extraAwardActivity.getLevelLimit()) {// 判断玩家等级是否符合
					if (extraAwardActivity.isNeedScore()) {// 是否需要评分
						int score = extraAwardActivity.getScore();
						ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [任务额外奖励活动] [score:" + score + "] [taskId:" + task.getId() + "]");
						TaskEntity entity = player.getTaskEntity(task.getId());
						if (entity != null) {
							int color = entity.getScore();
							ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [任务额外奖励活动] [Exact:" + extraAwardActivity.isExact() + "]");
							if (extraAwardActivity.isExact()) {// 精确匹配
								ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [任务额外奖励活动] [" + task.getName_stat() + "] [精确匹配]");
								ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [任务额外奖励活动] [" + task.getName_stat() + "] [所需评分:" + score + "] [实际评分:" + color + "]");
								if (score == color) {
									extraAwardActivity.doAward(player);
									ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [任务额外奖励活动] [发送奖励成功] [" + task.getName_stat() + "] [所需评分:" + score + "] [实际评分:" + color + "]");
								}
							} else {
								ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [任务额外奖励活动] [模糊匹配]");
								ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [任务额外奖励活动] [" + task.getName_stat() + "] [所需评分:" + score + "] [实际评分:" + color + "]");
								if (score < color || score == color) {
									extraAwardActivity.doAward(player);
									ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [任务额外奖励活动] [发送奖励成功] [" + task.getName_stat() + "] [所需评分:" + score + "] [实际评分:" + color + "]");
								}
							}
						}
					} else {
						extraAwardActivity.doAward(player);
						ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [任务额外奖励活动] [发送奖励成功] [" + task.getName_stat() + "] [不需评分]");
					}
				}
			}
		}

	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

	public void init() {
		
		instance = this;
		taskNames = ExchangeActivityManager.getInstance().getAllExtraAwardTaskNames();
		ActivitySubSystem.logger.warn("任务额外奖励活动] [任务名:" + Arrays.toString(taskNames) + "]");
		// List<String> taskNameList = new LinkedList<String>();
		// if (extraAwardActivity.getType().equals("group")) {
		// String[] groupName = extraAwardActivity.getName();
		// ActivitySubSystem.logger.warn("任务额外奖励活动] [任务组名:"+Arrays.toString(groupName)+"]");
		// if (groupName != null) {
		// for (String group : groupName) {
		// ActivitySubSystem.logger.warn("任务额外奖励活动] [循环任务组名:"+group+"]");
		// List<Task> tasks = TaskManager.getInstance().getTaskCollectionsByName(group);
		// if (tasks != null) {
		// for (Task task : tasks) {
		// ActivitySubSystem.logger.warn("任务额外奖励活动] [获取任务名:"+task.getName()+"]");
		// taskNameList.add(task.getName());
		// }
		// }
		// }
		// ActivitySubSystem.logger.warn("任务额外奖励活动] [任务数量:"+taskNameList.size()+"]");
		// }
		// taskNames=new String[taskNameList.size()];
		// for(int i=0;i<taskNameList.size();i++){
		// taskNames[i]=taskNameList.get(i);
		// }
		// } else if (extraAwardActivity.getType().equals("task")) {
		// taskNames = extraAwardActivity.getName();
		// }
		// ActivitySubSystem.logger.warn("任务额外奖励活动] [任务名:"+Arrays.toString(taskNames)+"]");
		ServiceStartRecord.startLog(this);
	}

}
