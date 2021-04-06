package com.fy.engineserver.menu.activity.silvercar;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.silvercar.SilvercarManager;
import com.fy.engineserver.activity.silvercar.SilvercarTaskCfg;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.BiaoCheNpc;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.RandomTool.RandomType;

/**
 * 可接任务选项/非族长的
 * 
 * 
 */
public class Option_Car_Task extends Option {

	private String taskName;

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			if (SilvercarManager.logger.isInfoEnabled()) SilvercarManager.logger.info(player.getLogString() + "[点击任务:{}]", new Object[] { taskName });
			SilvercarManager manager = SilvercarManager.getInstance();
			SilvercarTaskCfg taskCfg = manager.getTaskCfgMap().get(taskName);
			if (taskCfg == null) {
				player.sendError(Translate.text_silverCar_003);
				SilvercarManager.logger.error(player.getLogString() + "[任务:{}的相关配置不存在]", new Object[] { taskName });
				return;
			}
			Task task = TaskManager.getInstance().getTask(taskName,player.getCountry());
			if (task == null) {
				player.sendError(Translate.text_silverCar_004);
				SilvercarManager.logger.error(player.getLogString() + "[任务不存在:{}]", new Object[] { taskName });
				return;
			}

			int doneNum = player.getTodayDoneTimes(task.getGroupName());

			long jiazuId = player.getJiazuId();
			if (doneNum == 1) {
				if (jiazuId <= 0) {
					player.sendError(Translate.text_silverCar_005);
					return;
				}
			} else if (doneNum >= 2) {
				player.sendError(Translate.text_silverCar_006);
				return;
			}
			synchronized (player) {
				int articleColor = taskCfg.getNeedArticleColor();
				String articleName = taskCfg.getNeedArticleName();
				if (player.getFollowableNPC() != null) {
					player.sendError(Translate.text_task_037);
					if (SilvercarManager.logger.isInfoEnabled()) SilvercarManager.logger.info(player.getLogString() + "[接取押镖任务] [已经有护送NPC：{}]", new Object[] { player.getFollowableNPC().getName() });
					return;
				}
				CompoundReturn cr = player.takeOneTask(task, true, null);
				if (SilvercarManager.logger.isInfoEnabled()) {
					SilvercarManager.logger.info(player.getLogString() + "[接取押镖任务] [{}] [是否接取成功{}] [结果:{}]", new Object[] { task.getName(), cr.getBooleanValue(), cr.getIntValue() });
				}
				if (cr.getBooleanValue()) {
					int hasNum = player.getArticleNum(articleName, articleColor, BindType.BOTH);
					if (hasNum >= 1) {// 有物品
						// 看任务是否可接
						if (SilvercarManager.logger.isWarnEnabled()) {
							SilvercarManager.logger.warn(player.getLogString() + "[基础判断通过] [有物品:" + articleName + "] [颜色:" + articleColor + "] [数量:" + hasNum + "]");
						}
						if (player.getSilver() + player.getShopSilver() < taskCfg.getNeedMoney()) {
							player.sendError(Translate.translateString(Translate.text_silverCar_013, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(taskCfg.getNeedMoney()) } }));
							return;
						}
						BillingCenter.getInstance().playerExpense(player, taskCfg.getNeedMoney(), CurrencyType.SHOPYINZI, ExpenseReasonType.SILVER_CAR, "", -1);
						if (SilvercarManager.logger.isInfoEnabled()) {
							SilvercarManager.logger.info(player.getLogString() + "[基础判断通过] [增加任务成功]");
						}
						{
							player.sendError(Translate.translateString(Translate.text_silverCar_014, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(taskCfg.getNeedMoney()) } }));
							// 执行接取任务 这里会设置NPC
							player.addTaskByServer(task);
							if (player.getFollowableNPC() == null || !(player.getFollowableNPC() instanceof BiaoCheNpc)) {
								SilvercarManager.logger.error(player.getLogString() + "[致命错误] [接取押镖任务:" + taskName + "] [没有镖车NPC] [" + (player.getFollowableNPC() == null ? "null" : player.getFollowableNPC().getClass()) + "]");
								return;
							}
						}
						ArticleEntity ae = player.removeArticleByNameColorAndBind(articleName, articleColor, BindType.BIND, "押镖任务", true);
						if (ae == null) {
							ae = player.removeArticleByNameColorAndBind(articleName, articleColor, BindType.NOT_BIND, "押镖任务", true);
						}
						int color = 0;
						boolean isBiaoZhangReward = CountryManager.getInstance().isBiaoZhangReward(player);
						if (isBiaoZhangReward) {
							color = 4;
						} else {
							color = RandomTool.getResultIndexs(RandomType.groupRandom, manager.getRefreshRate(), 1).get(0);// 设置颜色
						}
						// player.getFollowableNPC().setObjectColor(ArticleManager.color_article[color]);
						player.getFollowableNPC().setObjectScale((short) (1000 * SilvercarManager.getInstance().getCarSize()[color]));
						player.getFollowableNPC().setNameColor(ArticleManager.color_article[color]);
						player.getFollowableNPC().setGrade(color);
						player.getFollowableNPC().setTitle("<f color='0x71EAF0'>" + player.getName() + "</f>");
						((BiaoCheNpc) player.getFollowableNPC()).setCfg(taskCfg);
						((BiaoCheNpc) player.getFollowableNPC()).setJiazuCar(false);
						((BiaoCheNpc) player.getFollowableNPC()).setMaxColor(((BiaoCheNpc) player.getFollowableNPC()).getGrade());

						{
							// 统计
							if (color == 3) {// 紫色
								AchievementManager.getInstance().record(player, RecordAction.个人运镖紫BUFF次数);
							} else if (color == 4) {// 橙色
								AchievementManager.getInstance().record(player, RecordAction.个人运镖橙BUFF次数);
							}
						}
						if (SilvercarManager.logger.isWarnEnabled()) {
							SilvercarManager.logger.warn(player.getLogString() + "[接取运镖任务{" + task.getName() + "}] [成功] [镖车颜色:" + color + "] [大小:" + player.getFollowableNPC().getObjectScale() + "] [是否是表彰:" + isBiaoZhangReward + "]");
						}
						return;
					} else {
						player.sendError(Translate.translateString(Translate.text_silverCar_015, new String[][] { { Translate.STRING_1, String.valueOf(ArticleManager.color_article[taskCfg.getNeedArticleColor()]) }, { Translate.STRING_2, articleName } }));
						if (SilvercarManager.logger.isInfoEnabled()) {
							SilvercarManager.logger.info(player.getLogString() + "[接取运镖任务{}] [物品数量不足 :{}] [颜色:{}]", new Object[] { taskName, articleName, articleColor });
						}
						return;
					}
				} else {
					player.sendError(TaskSubSystem.getInstance().getInfo(cr.getIntValue()));
					if (SilvercarManager.logger.isInfoEnabled()) {
						SilvercarManager.logger.info(player.getLogString() + "[接取运镖任务:{}] [基础判断通过] [增加任务失败{}]", new Object[] { taskName, TaskSubSystem.getInstance().getInfo(cr.getIntValue()) });
					}
					return;
				}
			}
		} catch (Exception e) {
			SilvercarManager.logger.error(player.getLogString() + "[x]", e);
		}
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
