package com.fy.engineserver.activity.explore;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;

/**
 * 韩服仙缘论道任务活动
 * 
 */
public class KoreaSocietyTaskEventTransact extends AbsTaskEventTransact {

	public static long startTime = TimeTool.formatter.varChar19.parse("2013-07-20 00:00:00");
	public static long endTime = TimeTool.formatter.varChar19.parse("2013-07-24 00:00:00");

	public static String prizeNames = "인연자주 퀘스트 보상";

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case deliver:
			return new String[] { "국내인연(15체)", "국내인연(30체)", "국내인연(60체)", "국외인연(20체)", "국외인연(40체)", "국외인연(80체)", "국내자주(15체)", "국내자주(30체)", "국내자주(60체)", "국외자주(20체)", "국외자주(40체)", "국외자주(80체)" };

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
	public void handleDone(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		ActivitySubSystem.logger.warn(player.getLogString() + " [完成仙缘论道任务:" + task.toString() + "] [平台:" + PlatformManager.getInstance().getPlatform().toString() + "]");
		if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			return;
		}
		long now = SystemTime.currentTimeMillis();
		if (startTime < now && now < endTime) {
			int leftNum = player.getTodayDoneTimes(task.getGroupName());
			if (leftNum == 0) {
				// 今天此类任务完成了,
				ActivitySubSystem.logger.warn(player.getLogString() + " [完成任务:" + task.toString() + "] [今天任务已经完成]");
				boolean isQingyuan = isQingyuan(task);
				String[] checkTask = null;
				if (isQingyuan) {
					// 是仙缘,获取论道任务
					checkTask = getZhujiuTasks();
				} else {
					checkTask = getQingyuanTasks();
				}
				if (checkTask == null) {
					return;
				}
				boolean doneAll = false;
				for (String taskName : checkTask) {
					Task temp = TaskManager.getInstance().getTask(taskName,player.getCountry());
					if (temp != null) {
						TaskEntity te = player.getTaskEntity(temp.getId());
						if (te != null) {
							int otherLeftNum = player.getTodayDoneTimes(temp.getGroupName());
							if (otherLeftNum == 0) {
								doneAll = true;
							}
							break;
						}
					}
				}
				if (doneAll) {
					// 发道具
					Article article = ArticleManager.getInstance().getArticle(prizeNames);
					try {
						if (article != null) {
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, 1, player, article.getColorType(), 1, true);
							if (ae != null) {
								MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, "인연,자주 퀘스트 이벤트", "인연,자주 퀘스트 이벤트에 참여해 주셔서 감사합니다. 보상은 첨부파일에서 확인하세요.", 0L, 0L, 0L, "韩国仙缘论道活动");
								ActivitySubSystem.logger.error(player.getLogString() + " [完成仙缘论道任务] [奖励物品发放成功:" + prizeNames + "] [任务:" + task.toString() + "]");
							}
						} else {
							ActivitySubSystem.logger.error(player.getLogString() + " [完成仙缘论道任务] [奖励物品不存在:" + prizeNames + "] [任务:" + task.toString() + "]");
						}
					} catch (Exception e) {
						ActivitySubSystem.logger.error(player.getLogString() + " [完成仙缘论道任务] [奖励异常] [任务:" + task.toString() + "]", e);
					}
				}
			}
		}
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

	public boolean isQingyuan(Task task) {
		return "인연".equals(task.getGroupName());
	}

	public boolean isZhujiu(Task task) {
		return "자주".equals(task.getGroupName());
	}

	public String[] getQingyuanTasks() {
		return new String[] { "국내인연(15체)", "국내인연(30체)", "국내인연(60체)", "국외인연(20체)", "국외인연(40체)", "국외인연(80체)" };
	}

	public String[] getZhujiuTasks() {
		return new String[] { "국내자주(15체)", "국내자주(30체)", "국내자주(60체)", "국외자주(20체)", "국외자주(40체)", "국외자주(80체)", };
	}

	// 此处什么都不做,以后根据需求可配置不同平台的数据
	public void init() {

	}
}
