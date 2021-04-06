package com.fy.engineserver.activity.taskDeliver;

import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.activity.shop.UseStat;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;

public class TaskDeliverAct extends BaseActivityInstance{
	
	/** 需要完成几次 */
	private int needDeliverTimes;
	/** group或task */
	private String taskType;
	/** 任务名或者任务组名 */
	private String taskName;
	/** 赠送的物品 */
	private ActivityProp giveProp;
	/** 邮件标题 */
	private String mailTitle;
	/** 邮件内容 */
	private String mailContent;

	public TaskDeliverAct(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}
	
	public void setOtherVar(int deliverTimes, String taskType, String taskName, String mailTitle, String mailConteng, ActivityProp giveProp) {
		this.needDeliverTimes = deliverTimes;
		this.taskType = taskType;
		this.taskName = taskName;
		this.mailTitle = mailTitle;
		this.mailContent = mailConteng;
		this.giveProp = giveProp;
	}
	/**
	 * 完成任务
	 */
	public void notifyDeliverTask(Player player, String tkName) {
		if(tkName == null || tkName.isEmpty()) {
			if(ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn("[完成N次任务活动][完成的任务名为空][" + player.getLogString() + "]");
			}
			return;
		}
		boolean isTaskFit = false;
		if (this.taskType.equals("group")) {
		List<Task> tasks = TaskManager.getInstance().getTaskCollectionsByName(taskName);
		if (tasks != null) {
			for (Task tk : tasks) {
				if (tk.getName().equals(tkName)) {
					isTaskFit = true;
					break;
					}
				}
			}
		} else if (this.taskType.equals("task")) {
			if (taskName.equals(tkName)) {
				isTaskFit = true;
			}
		}
		if(!isTaskFit) {
			if(ActivitySubSystem.logger.isDebugEnabled()) {
				ActivitySubSystem.logger.debug("[完成N次任务活动][完成的任务不匹配][" + player.getLogString() + "][完成的taskName:" + tkName +"][需要的taskname:" + taskName + "]");
			}
			return;
		}
		
		
		try {
			UseStat stat = (UseStat) ActivityManagers.getInstance().getDdc().get(player.getId() + "taskDeliverAct" + taskName + needDeliverTimes);
			if (stat == null) {
				UseStat st = new UseStat(new int[] {0});
				ActivityManagers.getInstance().getDdc().put(player.getId() + "taskDeliverAct" + taskName + needDeliverTimes, st);// 白，绿，蓝....
				stat = (UseStat) ActivityManagers.getInstance().getDdc().get(player.getId() + "taskDeliverAct" + taskName + needDeliverTimes);
			}
			int oldvalue = stat.colorvalue[0];
			int currvalue = oldvalue + 1;				//任务不可能一次完成多个
			ActivitySubSystem.logger.warn("[完成N次任务赠送活动] [成功] [之前完成次数：" + oldvalue + "] [新值：" + currvalue + "]  [任务名：" + taskName + "] [" + player.getLogString() + "]");
			if(currvalue >= needDeliverTimes) {
				Article prize = ArticleManager.getInstance().getArticleByCNname(giveProp.getArticleCNName());
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(prize, giveProp.isBind(), 1, player, giveProp.getArticleColor(), giveProp.getArticleNum(), true);
				long mailId = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { giveProp.getArticleNum() }, this.getMailTitle(), this.getMailContent(), 0L, 0L, 0L, "完成N次任务活动");
				ActivitySubSystem.logger.warn("[完成N次任务赠送活动] [成功] [之前完成次数：" + oldvalue + "] [新值：" + currvalue + "]  [任务名：" + taskName + "] [奖励物品:" + giveProp.toString() + "] [发送邮件成功:" + mailId + "] [" + player.getLogString() + "]");
				currvalue -= needDeliverTimes;
			}
			stat.colorvalue[0] = currvalue;
			ActivityManagers.getInstance().getDdc().put(player.getId() + "taskDeliverAct" + taskName + needDeliverTimes, stat);
		} catch(Exception e) {
			ActivitySubSystem.logger.error("[完成N次任务赠送活动][异常][" + player.getLogString() + "][taskname:" + tkName + "]", e);
		}
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[完成N次任务赠送活动] [完成任务类型:" + taskType + "] [任务名:" + taskName + "][次数:"+needDeliverTimes+"]<br>");
		sb.append("[赠送物品][" + giveProp + "]");
		return sb.toString();
	}

	
	@Override
	public String toString() {
		return "TaskDeliverAct [needDeliverTimes=" + needDeliverTimes + ", taskType=" + taskType + ", taskName=" + taskName + ", giveProp=" + giveProp + ", mailTitle=" + mailTitle + ", mailContent=" + mailContent + "]";
	}

	public int getNeedDeliverTimes() {
		return needDeliverTimes;
	}

	public void setNeedDeliverTimes(int needDeliverTimes) {
		this.needDeliverTimes = needDeliverTimes;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public ActivityProp getGiveProp() {
		return giveProp;
	}

	public void setGiveProp(ActivityProp giveProp) {
		this.giveProp = giveProp;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

}
