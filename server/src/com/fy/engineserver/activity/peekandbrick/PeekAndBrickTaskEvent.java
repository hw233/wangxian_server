package com.fy.engineserver.activity.peekandbrick;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.activity.silvercar.SilvercarManager;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.levelExpTag.ActivityType;
import com.fy.engineserver.levelExpTag.LevelExpTagManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.newtask.service.TaskConfig.TargetType;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.config.ServerFit;

public class PeekAndBrickTaskEvent extends AbsTaskEventTransact {

	private String[] tasks;

	private TaskManager taskManager;

	private List<PeekActivity> activities = new ArrayList<PeekActivity>();

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case deliver:
			return tasks;
		default:
			break;
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
		// 移除buff,给予奖励
		String buffName = "";
		int color = -1;
		try {
			buffName = task.getTargetByType(TargetType.BUFF).get(0).getTargetName()[0];
		} catch (Exception e) {
			TaskManager.logger.error(player.getLogString() + "[偷砖和刺探] [异常]", e);
		}
//		TaskSubSystem.logger.warn("[刺探任务测试] [task:"+task.getName()+"] ["+task.getCollections()+"] [tasks:"+(tasks!=null?Arrays.toString(tasks):"nul")+"] [playerName:"+player.getName()+"]");
		if (!"".equals(buffName)) {
			Buff buff = player.getBuffByName(buffName);
			if (buff != null) {
				color = buff.getLevel() - 1;
				buff.setInvalidTime(0);
			}
		}
		color = color < 0 ? 1 : color;
		long tiliExp = 0;
		try {
			if (Translate.刺探.equals(task.getCollections())) {
				tiliExp = LevelExpTagManager.getInstance().getValueByLevelAndActivity(player.getLevel(), ActivityType.国外刺探);
				tiliExp *= (1 + PeekAndBrickManager.getInstance().getExpRate()[color]);
				AchievementManager.getInstance().record(player, RecordAction.刺探完成次数);
				ActivenessManager.getInstance().record(player, ActivenessType.刺探);
				if (color == 3) {// 紫色buff
					AchievementManager.getInstance().record(player, RecordAction.刺探紫BUFF次数);
				} else if (color == 4) {
					AchievementManager.getInstance().record(player, RecordAction.刺探橙BUFF次数);
				}
				int score = getScore(color);

				BillboardStatDate bsd = BillboardStatDateManager.getInstance().getBillboardStatDate(player.getId());
				if (bsd != null) {
					// bsd.setBrickNum(bsd.getBrickNum() + score);
					bsd.setPeekNum(bsd.getPeekNum() + score);
				} else {
					CoreSubSystem.logger.error(player.getLogString() + "[完成任务:" + task.getName() + "] [排行榜数据不存在]");
				}
				Country country = CountryManager.getInstance().getCountryMap().get(player.getCountry());
				partakeActivity(player, country.isGuotan(), color);
//				TaskSubSystem.logger.warn("[刺探任务测试2] [task:"+task.getName()+"] ["+task.getCollections()+"] [country:"+country+"] [tiliExp:"+tiliExp+"] [color:"+color+"] [score:"+score+"] [playerName:"+player.getName()+"]");
			} else if (Translate.偷砖.equals(task.getCollections())) {
				tiliExp = LevelExpTagManager.getInstance().getValueByLevelAndActivity(player.getLevel(), ActivityType.国外宝藏);
				tiliExp *= (1 + PeekAndBrickManager.getInstance().getExpRate()[color]);
				AchievementManager.getInstance().record(player, RecordAction.偷砖完成次数);
				ActivenessManager.getInstance().record(player, ActivenessType.偷砖);
				

				if (color == 3) {// 紫色
					AchievementManager.getInstance().record(player, RecordAction.偷砖紫BUFF次数);
				} else if (color == 4) {// 橙色
					AchievementManager.getInstance().record(player, RecordAction.偷砖橙BUFF次数);
				}

				int score = getScore(color);

				BillboardStatDate bsd = BillboardStatDateManager.getInstance().getBillboardStatDate(player.getId());
				if (bsd != null) {
					// bsd.setPeekNum(bsd.getPeekNum() + score);
					bsd.setBrickNum(bsd.getBrickNum() + score);
				} else {
					CoreSubSystem.logger.error(player.getLogString() + "[完成任务:" + task.getName() + "] [排行榜数据不存在]");
				}
//				TaskSubSystem.logger.warn("[刺探任务测试3] [task:"+task.getName()+"] ["+task.getCollections()+"] [tiliExp:"+tiliExp+"] [color:"+color+"] [score:"+score+"] [playerName:"+player.getName()+"]");
			}
		} catch (Exception e) {
			PeekAndBrickManager.logger.error("[给予经验的时候]", e);
		}
		if (tiliExp > 0) {
			player.addExp(tiliExp, ExperienceManager.ADDEXP_REASON_TILI);
			CountryManager.getInstance().addExtraExp(player, tiliExp);
			
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn(player.getLogString() + "[交付活动任务:" + task.getCollections() + "] [颜色:" + color + "] [获得经验:" + tiliExp + "]");
			}
		}
		// if (color >= 0) {
		// long colorExp = (long) (PeekAndBrickManager.getInstance().getExpRate()[color] * task.getPrizeByType(PrizeType.EXP).get(0).getPrizeNum()[0]);
		// player.addExp(colorExp, ExperienceManager.ADDEXP_REASON_PEEKANDBRICK);
		// }

	}

	public void partakeActivity(Player player, boolean isGuoyun, int carColor) {
		long time = SystemTime.currentTimeMillis();

		PeekActivity activity = null;
		for (PeekActivity sa : activities) {
			if (sa.canPartakeActivity(time, carColor, isGuoyun)) {
				activity = sa;
				break;
			}
		}
		SilvercarManager.logger.warn(player.getLogString() + " [完成刺探任务] [是否有活动:" + (activity != null) + "]");
		if (activity != null) {
			activity.prizePlayer(player);
		}
	}

	private static int[] scores = { 1, 2, 5, 8, 10 };

	private static int getScore(int color) {
		return scores[color];
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {

	}

	public void init() {
		
		List<Task> all = new ArrayList<Task>();
		List<Task> peekList = taskManager.getTaskCollectionsByName(Translate.刺探);
		if (peekList != null) {
			all.addAll(peekList);
		}
		List<Task> brickList = taskManager.getTaskCollectionsByName(Translate.偷砖);
		if (brickList != null) {
			all.addAll(brickList);
		}
		tasks = new String[all.size()];
		for (int i = 0; i < all.size(); i++) {
			tasks[i] = all.get(i).getName();
		}
		initActivity();
		ServiceStartRecord.startLog(this);
	}

	private void initActivity() {
		List<ActivityProp> prizes = new ArrayList<ActivityProp>();
		prizes.add(new ActivityProp("刺探锦囊", 3, 1, true));

		activities.add(new PeekActivity(prizes, 0, Boolean.TRUE, TimeTool.formatter.varChar19.parse("2013-09-20 00:00:00"), TimeTool.formatter.varChar19.parse("2013-09-25 23:59:59"), new ServerFit() {

			@Override
			public boolean thisServerOpen() {
				if (PlatformManager.getInstance().isPlatformOf(Platform.官方, Platform.腾讯)) {
					return true;
				}
				return false;
			}
		}, "恭喜您获得谍影重重奖励", "恭喜您在谍影重重活动中获得“刺探锦囊”，请查收附件！祝您游戏愉快！"));
	}
}

class PeekActivity {
	private List<ActivityProp> prizes;
	private int lowColor;
	private boolean mustGuoTan;
	private long startTime;
	private long endTime;
	private ServerFit serverFit;

	private String mailTitle;
	private String mailContent;

	public PeekActivity(List<ActivityProp> prizes, int lowColor, boolean mustGuoTan, long startTime, long endTime, ServerFit serverFit, String mailTitle, String mailContent) {
		super();
		this.prizes = prizes;
		this.lowColor = lowColor;
		this.mustGuoTan = mustGuoTan;
		this.startTime = startTime;
		this.endTime = endTime;
		this.serverFit = serverFit;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
	}

	public void prizePlayer(Player player) {
		if (prizes != null) {
			for (ActivityProp ap : prizes) {
				Article article = ArticleManager.getInstance().getArticleByCNname(ap.getArticleCNName());
				if (article != null) {
					try {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, ap.isBind(), ArticleEntityManager.活动, player, ap.getArticleColor(), ap.getArticleNum(), true);
						if (ae != null) {
							long mailId = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { ap.getArticleNum() }, this.getMailTitle(), this.getMailContent(), 0L, 0L, 0L, "运镖活动");
							ActivitySubSystem.logger.warn(player.getLogString() + " [完成运镖活动] [获得奖励:" + article.getLogString() + "] [mailId:" + mailId + "]");
						}
					} catch (Exception e) {
						ActivitySubSystem.logger.warn(player.getLogString() + "[运镖活动] [异常]", e);
					}

				} else {
					ActivitySubSystem.logger.warn("[运镖活动] [物品不存在:" + ap.getArticleCNName() + "]");
				}
			}
		}
	}

	public boolean canPartakeActivity(long time, int carColor, boolean inGuoyun) {
		ActivitySubSystem.logger.warn("[押镖完成] [时间是否匹配:" + (startTime <= time && time <= endTime) + "] [服务器是否开放:" + (serverFit.thisServerOpen()) + "] [颜色:" + carColor + ",颜色是否匹配:" + (carColor >= this.lowColor) + "] [国运要求:" + this.mustGuoTan + ",是否匹配" + (inGuoyun == this.mustGuoTan) + "]");
		if (startTime <= time && time <= endTime) {
			if (serverFit.thisServerOpen()) {
				if (carColor >= this.lowColor) {
					if (inGuoyun == this.mustGuoTan) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public List<ActivityProp> getPrizes() {
		return prizes;
	}

	public void setPrizes(List<ActivityProp> prizes) {
		this.prizes = prizes;
	}

	public int getLowColor() {
		return lowColor;
	}

	public void setLowColor(int lowColor) {
		this.lowColor = lowColor;
	}

	public boolean isMustGuoyun() {
		return mustGuoTan;
	}

	public void setMustGuoyun(boolean mustGuoyun) {
		this.mustGuoTan = mustGuoyun;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public ServerFit getServerFit() {
		return serverFit;
	}

	public void setServerFit(ServerFit serverFit) {
		this.serverFit = serverFit;
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

	@Override
	public String toString() {
		return "SilvercarActivity [prizes=" + prizes + ", lowColor=" + lowColor + ", mustGuoyun=" + mustGuoTan + ", startTime=" + startTime + ", endTime=" + endTime + ", serverFit=" + serverFit + ", mailTitle=" + mailTitle + ", mailContent=" + mailContent + "]";
	}
}