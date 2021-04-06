package com.fy.engineserver.activity.farming;

import java.util.List;
import java.util.Random;

import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.ACTIVITY_FARMING_PLATE_REQ;
import com.fy.engineserver.message.FIND_WAY2TASK_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;

/**
 * 神农活动
 * 
 */
public class FarmingTaskEvent extends AbsTaskEventTransact {

	private String[] taskNames = null;

	private FarmingManager farmingManager;
	private TaskManager taskManager;

	static Random RANDOM = new Random();

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case accept:
		case deliver:
		case done:
			return taskNames;
		case drop:
			return new String[] {};
		default:
			break;
		}
		return null;
	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
		// 接到任务自动寻路到目标
		if (!player.getTaskEntity(task.getId()).taskComplete()) {
			FIND_WAY2TASK_RES res = new FIND_WAY2TASK_RES(GameMessageFactory.nextSequnceNum(), task.getId(), TaskConfig.TASK_STATUS_GET);
			player.addMessageToRightBag(res);
			if (FarmingManager.logger.isWarnEnabled()) FarmingManager.logger.warn(player.getLogString() + "[接受任务:{}] [自动寻路]", new Object[] { task.getName() });
		}
	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		FIND_WAY2TASK_RES res = new FIND_WAY2TASK_RES(GameMessageFactory.nextSequnceNum(), task.getId(), TaskConfig.TASK_STATUS_COMPLETE);
		player.addMessageToRightBag(res);
		if (FarmingManager.logger.isWarnEnabled()) FarmingManager.logger.warn(player.getLogString() + "[完成任务:{}] [自动寻路]", new Object[] { task.getName() });
	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		// 自动接取下一个任务
		try {
			List<Task> nextList = TaskManager.getInstance().getnextTask(task.getGroupName());
			if (FarmingManager.logger.isWarnEnabled()) FarmingManager.logger.warn(player.getLogString() + "[完成了神农任务:{}] [后续列表:{}]", new Object[] { task.getName(), nextList });
			if (nextList != null && nextList.size() > 0) {
				Task next = null;
				for(Task t : nextList){
					if(t != null && t.getCountryLimit() == task.getCountryLimit()){
						next = t;
					}
				}
//				Task next = nextList.get(0);
				player.addTaskByServer(next);
				if (FarmingManager.logger.isWarnEnabled()) FarmingManager.logger.warn(player.getLogString() + "[完成了神农任务:{}] [增加任务:{}] [id:{}]", new Object[] { task.getName(), next.getName(),next.getId() });
			}
			{
				boolean needSendplant = false;
				int taskIndex = -1;
				for (int i = 0; i < farmingManager.getNeedSendRadomplateTasks().length; i++) {
					String taskName = farmingManager.getNeedSendRadomplateTasks()[i];
					if (task.getName().equals(taskName)) {
						needSendplant = true;
						taskIndex = i;
						break;
					}
				}
				if (FarmingManager.logger.isWarnEnabled()) {
					FarmingManager.logger.warn(player.getLogString() + "[完成了神农任务:{}] [是否要弹出盘子:{}]", new Object[] { task.getName(), needSendplant });
				}
				if (needSendplant) {
					// send弹出转盘--------------->
					if (task.isHasExcess()) {
						String articleName = task.getExcessTarget();
						Article article = ArticleManager.getInstance().getArticle(articleName);
						PlateProject plateProject = null;
						int randomId = -1;
						if (article != null) {
							ArticleEntity ae = player.getArticleEntity(articleName);
							if (ae == null) {// 没有得到了额外的需求
								plateProject = farmingManager.getProjects().get(1);// 第一套方案
								FarmingManager.logger.error(player.getLogString() + "[完成了神农任务:" + task.getName() + "] [没有得到:" + articleName + "]");
							} else {
								player.removeFromKnapsacks(ae, "神农", true);// 删除物品
								randomId = RANDOM.nextInt(2) + 2;// 第2/3套方案中取一套
								plateProject = farmingManager.getProjects().get(randomId);
								FarmingManager.logger.error(player.getLogString() + "[完成了神农任务:" + task.getName() + "] [得到了:" + articleName + "]");
							}
							if (plateProject != null) {// 选出了一套弹出的方案。计算一下结果 并发给客户端
								int resultIndex = plateProject.getOnceResultIndex();// 索引
								int fruitId = plateProject.getFruitIds()[resultIndex];// 水果的ID
								FarmingFruit fruit = farmingManager.getFruits().get(fruitId);
								if (FarmingManager.logger.isWarnEnabled()) {
									FarmingManager.logger.warn(player.getLogString() + "[完成了神农任务:{}] [弹出了盘子:{}] [resultIndex:{}] [resId:{}] [果实:{}] [是否有金剪刀:{}]", new Object[] { task.getName(), plateProject, resultIndex, fruit.getName(), fruit, ae != null });
								}
								if (fruit != null) {
									ACTIVITY_FARMING_PLATE_REQ req = new ACTIVITY_FARMING_PLATE_REQ(GameMessageFactory.nextSequnceNum(), fruit.getId(), plateProject.getFruitIds(), plateProject.getFruitNames(), taskIndex, FarmingManager.runTime);
									player.addMessageToRightBag(req);
									player.setFarmingPlateResult(fruit.getId());
									{
										// 直接接任务了
										String[] taskName = fruit.getTaskName();
										if (taskIndex < 0 || taskIndex >= taskName.length) {
											player.sendError(Translate.translateString(Translate.text_feed_silkworm_003, new String[][] { { Translate.STRING_1, String.valueOf(taskIndex) } }));
											FarmingManager.logger.error(player.getJiazuLogString() + "[直接接任务] [神农活动] [要接的任务异常] [taskIndex:{}]", new Object[] { taskIndex });
											return;
										}
										Task taskFruit = TaskManager.getInstance().getTask(taskName[taskIndex],player.getCountry());
										if (taskFruit == null) {
											if (FarmingManager.logger.isWarnEnabled()) {
												FarmingManager.logger.warn(player.getLogString() + "[完成了神农任务:" + task.getName() + "] [fruit.getId():" + fruit.getId() + "] [plateProject.getFruitIds():" + plateProject.getFruitIds() + "] [plateProject.getFruitNames():" + plateProject.getFruitNames() + "] [taskIndex:" + taskIndex + "] [直接接取任务失败,任务不存在:" + taskName[taskIndex] + "]");
											}
											return;
										}
										CompoundReturn addTaskByServer = player.addTaskByServer(taskFruit);
										if (addTaskByServer.getBooleanValue()) {
											player.sendNotice(Translate.translateString(Translate.text_farming_001, new String[][] { { Translate.ARTICLE_NAME_1, fruit.getName() } }));
										}
										if (FarmingManager.logger.isWarnEnabled()) {
											FarmingManager.logger.warn(player.getLogString() + "[完成了神农任务:" + task.getName() + "] [fruit.getId():" + fruit.getId() + "] [plateProject.getFruitIds():" + plateProject.getFruitIds() + "] [plateProject.getFruitNames():" + plateProject.getFruitNames() + "] [taskIndex:" + taskIndex + "] [直接接取任务 :" + taskFruit.getName() + "] [直接截取任务返回:" + addTaskByServer.getBooleanValue() + "] [intValue:" + addTaskByServer.getIntValue() + "] [" + TaskSubSystem.getInstance().getInfo(addTaskByServer.getIntValue()) + "]");
										}
									}
									if (FarmingManager.logger.isWarnEnabled()) {
										FarmingManager.logger.warn(player.getLogString() + "[完成了神农任务:" + task.getName() + "] [fruit.getId():" + fruit.getId() + "] [plateProject.getFruitIds():" + plateProject.getFruitIds() + "] [plateProject.getFruitNames():" + plateProject.getFruitNames() + "] [taskIndex:" + taskIndex + "] [真正弹出了盘子]");
									}
									// 活跃度统计
									ActivenessManager.getInstance().record(player, ActivenessType.神农);
								} else {
									if (FarmingManager.logger.isWarnEnabled()) {
										FarmingManager.logger.warn(player.getLogString() + "[完成了神农任务:" + task.getName() + "] [水果不存在:" + fruitId + "] [resultIndex:" + resultIndex + "] [没有进行下去]");
									}
								}
							} else {
								if (FarmingManager.logger.isWarnEnabled()) {
									FarmingManager.logger.warn(player.getLogString() + "[完成了神农任务:" + task.getName() + "] [掉落方案不存在,randomId:" + randomId + "] [没有进行下去]");
								}
							}
						} else {
							FarmingManager.logger.error(player.getLogString() + "[完成了神农任务] [任务道具未录入] [任务:{}] [物品:{}]", new Object[] { task.getName(), task.getExcessTarget() });
						}
					} else {
						FarmingManager.logger.error(player.getLogString() + "[完成了神农任务:" + task.getName() + "] [没有额外目标]");
					}
				}

			}
		} catch (Exception e) {
			FarmingManager.logger.error(player.getLogString() + "[神农任务完成异常]", e);
		}
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		// 不做操作

	}

	public FarmingManager getFarmingManager() {
		return farmingManager;
	}

	public void setFarmingManager(FarmingManager farmingManager) {
		this.farmingManager = farmingManager;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public void init() {
		
		long start = SystemTime.currentTimeMillis();
//		System.out.println("-------------------开始加载神农-------------------集合:" + farmingManager.getTaskCollectionName());
		String collectionName = farmingManager.getTaskCollectionName();
		List<Task> tasks = taskManager.getTaskCollectionsByName(collectionName);
		if (tasks != null && !tasks.isEmpty()) {
			taskNames = new String[tasks.size()];
			for (int i = 0; i < tasks.size(); i++) {
				taskNames[i] = tasks.get(i).getName();
			}
//			System.out.println("-------------------加载神农完毕-------------------[耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");

		}
		ServiceStartRecord.startLog(this);

	}
}
