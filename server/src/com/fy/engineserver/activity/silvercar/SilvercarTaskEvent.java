package com.fy.engineserver.activity.silvercar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiaZuLivenessType;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.jiazu2.model.BiaocheRewardModel;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfExp;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.newtask.service.TaskConfig.PrizeType;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.BiaoCheNpc;
import com.fy.engineserver.sprite.npc.FollowableNPC;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.RandomTool.RandomType;
import com.fy.engineserver.util.config.ServerFit;

public class SilvercarTaskEvent extends AbsTaskEventTransact {
	static double[] color_exp_rate = { 1, 2, 4, 8, 16 };
	static final double[] color_exp_rate_4_jiazu = {0, 0.5, 0.5, 1, 2, 3};		//破碎、白、绿、蓝、紫、橙
	static int getpeize_radius = 750;
	/**
	 * 所有运镖的任务
	 */
	private String[] silvercarTasks;
	/**
	 * 家族运镖任务
	 */
	private String[] jiazuSilvercarTask;

	private SilvercarManager silvercarManager;
	private TaskManager taskManager;

	private static SilvercarTaskEvent instance;

	private List<SilvercarActivity> activities = new ArrayList<SilvercarActivity>();

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case deliver:
			return silvercarTasks;
		case done:
			return jiazuSilvercarTask;
		case drop:
			return silvercarTasks;
		default:
			break;
		}
		return null;
	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

	public static SilvercarTaskEvent getInstance() {
		return instance;
	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		try {
			ArticleManager articleManager = ArticleManager.getInstance();
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu != null) {
				TaskEntity entity = player.getTaskEntity(task.getId());
				if (entity != null) {
					int biaocheColor = entity.getScore() + 1;
					if (player.getFollowableNPC() != null) {
						biaocheColor = player.getFollowableNPC().getGrade() + 1;
						if (JiazuSubSystem.logger.isDebugEnabled()) {
							JiazuSubSystem.logger.debug("[家族运镖] [镖车颜色:" + biaocheColor + "] [" + player.getLogString() + "]");
						}
					}
					long baseExp = ((TaskPrizeOfExp) task.getPrizeByType(PrizeType.EXP).get(0)).getExp(player);
					long expAdd = (long) (baseExp * color_exp_rate[entity.getScore()]);// TODO 和龙图阁研究有关的系数
					expAdd = (long) (expAdd * (1 + color_exp_rate_4_jiazu[biaocheColor]));
//					if (JiazuSubSystem.logger.isDebugEnabled()) {
//						JiazuSubSystem.logger.debug("[家族运镖] [镖车颜色:" + biaocheColor + "] [getscore:" + entity.getScore() + "] [baseExp : " + baseExp + "] [" + player.getLogString() + "]");
//					}
					if (biaocheColor <= 0) {		//碎了要发奖励给击杀者
						
					} else {
						BiaocheRewardModel bcr = JiazuManager2.instance.biaocheRewards.get(jiazu.getLevel());
						if (bcr != null && bcr.getRewardMoney() > 0) {
							String result = jiazu.addJiazuMoney(bcr.getRewardMoney());
							JiazuSubSystem.logger.warn("[家族运镖] [完成] [增加家族资金] [成功] [增加数量:" + bcr.getRewardMoney() + "] [" + jiazu.getLogString() + "]");
							if (result != null) {
								player.sendError(result);
							}
							if(biaocheColor == 1){
								JiazuManager2.instance.addLiveness(player, JiaZuLivenessType.家族押镖_白);
							}else if(biaocheColor == 2){
								JiazuManager2.instance.addLiveness(player, JiaZuLivenessType.家族押镖_绿);
							}else if(biaocheColor == 3){
								JiazuManager2.instance.addLiveness(player, JiaZuLivenessType.家族押镖_蓝);
							}else if(biaocheColor == 4){
								JiazuManager2.instance.addLiveness(player, JiaZuLivenessType.家族押镖_紫);
							}else if(biaocheColor == 5){
								JiazuManager2.instance.addLiveness(player, JiaZuLivenessType.家族押镖_橙);
							}
						} else {
							JiazuSubSystem.logger.warn("[家族运镖] [完成] [增加家族资金] [失败] [" + bcr + "] [" + jiazu.getLogString() + "] [jiazuLevel:" + jiazu.getLevel() + "]");
						}
					}
					List<Player> onLinePlayers = jiazu.getOnLineMembers();
					Article article = articleManager.getArticle(SilvercarManager.getInstance().getJiazuSilvercarOtherPrize());
					for (Player member : onLinePlayers) {
						if (member == null || member.getGame() == null) {
							continue;
						}
						try {
							if (member.getGame().equals(player.getGame())) {
								if (FollowableNPC.isNear(player.getX(), player.getY(), member.getX(), member.getY(), getpeize_radius)) {
									//活跃度统计
									
									
									ActivenessManager.getInstance().record(member, ActivenessType.家族押镖);
									ActivenessManager.getInstance().record(player, ActivenessType.家族押镖);
									if (SilvercarManager.logger.isWarnEnabled()) SilvercarManager.logger.warn(player.getLogString() + "[家族名字:" + jiazu.getName() + "] [家族ID:" + jiazu.getJiazuID() + "] [完成了家族运镖任务:{}] [成员:{}在附近]", new Object[] { task.getName(), member.getName() });
									member.addExp(expAdd, ExperienceManager.ADDEXP_REASON_SILVERCAR);
									member.sendNotice(Translate.translateString(Translate.text_silverCar_009, new String[][] { { Translate.STRING_1, String.valueOf(expAdd) } }));
									if (article != null) {
										double[] rates = SilvercarManager.getInstance().getRateByColor(biaocheColor);
//										int color = RandomTool.getResultIndexs(RandomType.groupRandom, SilvercarManager.getInstance().getJiazuSilvercarOtherPrizeRate(), 1).get(0);
										int color = RandomTool.getResultIndexs(RandomType.groupRandom, rates, 1).get(0);
										try {
											ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_silvercar, player, color, 1, true);
											if (ae != null) {
												boolean succ = member.putToKnapsacks(ae, "运镖获取");
												if (!succ) {// 没成功
													MailManager.getInstance().sendMail(member.getId(), new ArticleEntity[] { ae }, Translate.家族运镖奖励, "", 0, 0, 0, Translate.家族运镖奖励);
												}
												member.noticeGetArticle(ae);
												ArticleStatManager.addToArticleStat(member, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "参加家族运镖获取", "");

											}
											JiazuMember jiazuMember = JiazuManager.getInstance().getJiazuMember(member.getId(), jiazu.getJiazuID());
											jiazuMember.setCurrentWeekContribution(jiazuMember.getCurrentWeekContribution() + JiazuManager.CONTRIBUTION_ADD_BY_JIAZU_SILVERCAR);
											jiazu.initMember4Client();
											if (JiazuSubSystem.logger.isWarnEnabled()) {
												JiazuSubSystem.logger.warn(member.getLogString() + "[家族名字:" + jiazu.getName() + "] [家族ID:" + jiazu.getJiazuID() + "] [获得贡献度] [参与了家族运镖] [获得经验:{}] [增加个人贡献度:{}] [增加后贡献度:{}]", new Object[] { expAdd, JiazuManager.CONTRIBUTION_ADD_BY_JIAZU_SILVERCAR, jiazuMember.getCurrentWeekContribution() });
											}
										} catch (Exception ex) {
											SilvercarManager.logger.error(jiazu.getName() + "[家族运镖] [异常]", ex);
										}
									} else {
										if (SilvercarManager.logger.isWarnEnabled()) SilvercarManager.logger.warn(player.getLogString() + "[家族名字:" + jiazu.getName() + "] [家族ID:" + jiazu.getJiazuID() + "] [完成了家族运镖任务:{}] [成员:{}在附近][物品:{}不存在]", new Object[] { task.getName(), member.getName(), SilvercarManager.getInstance().getJiazuSilvercarOtherPrize() });
									}
								} else {
									if (SilvercarManager.logger.isWarnEnabled()) SilvercarManager.logger.warn(player.getLogString() + "[家族名字:" + jiazu.getName() + "] [家族ID:" + jiazu.getJiazuID() + "] [完成了家族运镖任务:{}] [成员:{}不在附近]", new Object[] { task.getName(), member.getName() });
								}
							} else {
								if (SilvercarManager.logger.isWarnEnabled()) SilvercarManager.logger.warn(player.getLogString() + "[完成了家族运镖任务:{}] [成员:{}不在同一幅地图]", new Object[] { task.getName(), member.getName() });
							}
						} catch (Exception e) {
							SilvercarManager.logger.error(player.getLogString() + "[完成了家族运镖任务:" + task.getName() + "][异常]", e);
						}
					}
				} else {
					if (SilvercarManager.logger.isWarnEnabled()) SilvercarManager.logger.warn(player.getLogString() + "[完成了家族运镖任务:{}] [实体不存在 ]", new Object[] { task.getName() });
				}
			}
		} catch (Exception e) {
			SilvercarManager.logger.error(player.getLogString() + "[完成了家族运镖任务:" + task.getName() + "][异常]", e);
		}
	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		try {
			if (SilvercarManager.logger.isWarnEnabled()) {
				SilvercarManager.logger.warn(player.getLogString() + "[完成了运镖任务:{}]", new Object[] { task.getName() });
			}
			TaskEntity entity = player.getTaskEntity(task.getId());
			if (entity != null) {
				int color = entity.getScore();
				long basePrize = ((TaskPrizeOfExp) task.getPrizeByType(PrizeType.EXP).get(0)).getExp(player);// 基础经验奖励
				double colorRate = color_exp_rate[color];
				Country country = CountryManager.getInstance().getCountryMap().get(player.getCountry());
				double guoyunRate = 1;
				if (country.guoyun) { // 在国运状态下
					guoyunRate = silvercarManager.getCountryTrafficRate();
				}
				long expAdd = (long) (basePrize * colorRate * guoyunRate);

				player.addExp(expAdd, ExperienceManager.ADDEXP_REASON_SILVERCAR);

				boolean isJiazu = false;

				for (String tName : jiazuSilvercarTask) {
					if (tName.equals(task.getName())) {
						isJiazu = true;
						break;
					}
				}
				SilvercarManager.logger.warn(player.getLogString() + " [完成任务:" + task.getName() + "] [是否是家族任务:" + isJiazu + "] [所有家族任务:" + Arrays.toString(jiazuSilvercarTask) + "]");

				if (!isJiazu) {
					partakeActivity(player, country.guoyun, color);
				}

				if (SilvercarManager.logger.isWarnEnabled()) {
					SilvercarManager.logger.warn(player.getLogString() + "[完成了运镖任务:{}] [颜色:{}] [颜色加成:{}] [国运加成:{}] [额外获得经验:{}] [基本经验:{}]", new Object[] { task.getName(), color, colorRate, guoyunRate, expAdd, basePrize });
				}
			} else {
				if (SilvercarManager.logger.isWarnEnabled()) {
					SilvercarManager.logger.warn(player.getLogString() + "[完成了运镖任务:{}] [实体不存在]", new Object[] { task.getName() });
				}
			}
		} catch (Exception e) {
			SilvercarManager.logger.error(player.getLogString() + "[完成了运镖任务:{}] [异常]", new Object[] { task.getName() });
		}
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		FollowableNPC npc = player.getFollowableNPC();
		if (npc != null && npc instanceof BiaoCheNpc) {
			npc.getCurrentGame().removeSprite(npc);
			player.setFollowableNPC(null);
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn(player.getLogString() + "[放弃任务:" + task.getName() + "] [移除镖车:" + npc.getName() + "] [镖车所在地图:" + (npc.getCurrentGame() == null ? "--" : npc.getCurrentGame().gi.displayName) + "] [成功]");
			}
		} else {
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn(player.getLogString() + "[放弃任务:" + task.getName() + "] [移除镖车] [失败] [" + npc + "]");
			}
		}
	}

	public SilvercarManager getSilvercarManager() {
		return silvercarManager;
	}

	public void setSilvercarManager(SilvercarManager silvercarManager) {
		this.silvercarManager = silvercarManager;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public String[] getSilvercarTasks() {
		return silvercarTasks;
	}

	public void setSilvercarTasks(String[] silvercarTasks) {
		this.silvercarTasks = silvercarTasks;
	}

	public String[] getJiazuSilvercarTask() {
		return jiazuSilvercarTask;
	}

	public void setJiazuSilvercarTask(String[] jiazuSilvercarTask) {
		this.jiazuSilvercarTask = jiazuSilvercarTask;
	}

	/**
	 * 参与运镖活动,如果可以参加直接发奖
	 * @param player
	 * @param isGuoyun
	 * @param carColor
	 */
	public void partakeActivity(Player player, boolean isGuoyun, int carColor) {
		long time = SystemTime.currentTimeMillis();

		SilvercarActivity activity = null;
		for (SilvercarActivity sa : activities) {
			if (sa.canPartakeActivity(time, carColor, isGuoyun)) {
				activity = sa;
				break;
			}
		}
		SilvercarManager.logger.warn(player.getLogString() + " [完成运镖任务] [是否有活动:" + (activity != null) + "]");
		if (activity != null) {
			activity.prizePlayer(player);
		}
	}

	public void init() throws Exception {
		
		String selfCarTaskGroupName = Translate.国运;
		String jiazuCarTaskGroupName = Translate.家族运镖;
		List<Task> allSilvercarTasks = taskManager.getTaskCollectionsByName(selfCarTaskGroupName);
		List<Task> jiazuSilvercarTasks = taskManager.getTaskCollectionsByName(jiazuCarTaskGroupName);
		if (allSilvercarTasks == null || jiazuSilvercarTasks == null) {
			throw new Exception("[家族运镖或个人运镖不存在] [" + selfCarTaskGroupName + "] [" + jiazuCarTaskGroupName + "]");
		}
		if (jiazuSilvercarTasks != null) {
			jiazuSilvercarTask = new String[jiazuSilvercarTasks.size()];
			for (int i = 0; i < jiazuSilvercarTasks.size(); i++) {
				jiazuSilvercarTask[i] = jiazuSilvercarTasks.get(i).getName();
			}
		}
		if (allSilvercarTasks != null) {
			allSilvercarTasks.addAll(jiazuSilvercarTasks);
			silvercarTasks = new String[allSilvercarTasks.size()];
			for (int i = 0; i < allSilvercarTasks.size(); i++) {
				silvercarTasks[i] = allSilvercarTasks.get(i).getName();
			}
		}
		initActivity();
		instance = this;
		ServiceStartRecord.startLog(this);
	}

	private void initActivity() {
		List<ActivityProp> prizes = new ArrayList<ActivityProp>();
		prizes.add(new ActivityProp("运镖锦囊", 3, 1, true));

		activities.add(new SilvercarActivity(prizes, 0, Boolean.TRUE, TimeTool.formatter.varChar19.parse("2013-09-26 00:00:00"), TimeTool.formatter.varChar19.parse("2013-09-29 23:59:59"), new ServerFit() {

			@Override
			public boolean thisServerOpen() {
				if (PlatformManager.getInstance().isPlatformOf(Platform.官方, Platform.腾讯)) {
					return true;
				}
				return false;
			}
		}, "恭喜您获得神牛送福奖励", "恭喜您在神牛送福活动中获得“运镖锦囊”，请查收附件！祝您游戏愉快！"));
	}
}

class SilvercarActivity {
	private List<ActivityProp> prizes;
	private int lowColor;
	private boolean mustGuoyun;
	private long startTime;
	private long endTime;
	private ServerFit serverFit;

	private String mailTitle;
	private String mailContent;

	public SilvercarActivity(List<ActivityProp> prizes, int lowColor, boolean mustGuoyun, long startTime, long endTime, ServerFit serverFit, String mailTitle, String mailContent) {
		super();
		this.prizes = prizes;
		this.lowColor = lowColor;
		this.mustGuoyun = mustGuoyun;
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
		ActivitySubSystem.logger.warn("[押镖完成] [时间是否匹配:" + (startTime <= time && time <= endTime) + "] [服务器是否开放:" + (serverFit.thisServerOpen()) + "] [颜色:" + carColor + ",颜色是否匹配:" + (carColor >= this.lowColor) + "] [国运要求:" + this.mustGuoyun + ",是否匹配" + (inGuoyun == this.mustGuoyun) + "]");
		if (startTime <= time && time <= endTime) {
			if (serverFit.thisServerOpen()) {
				if (carColor >= this.lowColor) {
					if (inGuoyun == this.mustGuoyun) {
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
		return mustGuoyun;
	}

	public void setMustGuoyun(boolean mustGuoyun) {
		this.mustGuoyun = mustGuoyun;
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
		return "SilvercarActivity [prizes=" + prizes + ", lowColor=" + lowColor + ", mustGuoyun=" + mustGuoyun + ", startTime=" + startTime + ", endTime=" + endTime + ", serverFit=" + serverFit + ", mailTitle=" + mailTitle + ", mailContent=" + mailContent + "]";
	}
}
