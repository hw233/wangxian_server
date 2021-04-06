package com.fy.engineserver.menu.xianling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.activity.xianling.PlayerXianLingData;
import com.fy.engineserver.activity.xianling.TimedTask;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_USER_ENTER_SERVER_REQ;
import com.fy.engineserver.message.XL_TIMEDTASK_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.NpcinfoFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.transport.Connection;

public class Option_Confirm_SilverRefresh extends Option {
	private PlayerXianLingData xianlingData;
	private TimedTask task;

	public Option_Confirm_SilverRefresh() {
	}

	public Option_Confirm_SilverRefresh(PlayerXianLingData xianlingData, TimedTask task) {
		this.xianlingData = xianlingData;
		this.task = task;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if(UnitServerFunctionManager.needCloseFunctuin(Function.仙灵大会)) {
			player.sendError(Translate.合服功能关闭提示);
			return ;
		}
		try {
			if (XianLingManager.instance.REFRESH_TIMEDTASK_COST > player.getSilver()) {
				player.sendError(Translate.银子不足);
				return;
			}
			BillingCenter.getInstance().playerExpense(player, XianLingManager.instance.REFRESH_TIMEDTASK_COST, CurrencyType.YINZI, ExpenseReasonType.刷新仙灵限时任务, "仙灵刷新限时任务");
			xianlingData.setTaskId(task.getTaskId());
			xianlingData.setTaskState((byte) 1);
			xianlingData.setTakePrize(false);
			xianlingData.setNextRefreshTime(System.currentTimeMillis() + XianLingManager.instance.TIMEDTASK_REFRESHTIME);
			try {
				//统计
				StatClientService statClientService = StatClientService.getInstance();
				NpcinfoFlow npcinfoFlow = new NpcinfoFlow();
				npcinfoFlow.setAward("0");
				CareerManager cm = CareerManager.getInstance();
				npcinfoFlow.setCareer(cm.getCareer(player.getCareer()).getName());
				npcinfoFlow.setColumn1(player.getName());
				npcinfoFlow.setColumn2("");
				npcinfoFlow.setCreateDate(new Date().getTime());
				npcinfoFlow.setFenQu(GameConstants.getInstance().getServerName());
				npcinfoFlow.setGameLevel(player.getLevel());
				npcinfoFlow.setGetDaoJu(0);
				npcinfoFlow.setGetWuPin(0);
				npcinfoFlow.setGetYOuXiBi(0);
				Connection conn = player.getConn();
				if (conn != null) {
					NEW_USER_ENTER_SERVER_REQ mess = (NEW_USER_ENTER_SERVER_REQ) conn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
					if (mess != null) {
						npcinfoFlow.setJixing(mess.getPhoneType());
					}
				}
				npcinfoFlow.setNpcName("仙灵大会");
				npcinfoFlow.setTaskType("刷新限时任务");
				npcinfoFlow.setUserName(player.getUsername());
				statClientService.sendNpcinfoFlow("", npcinfoFlow);
			} catch (Exception e) {
				if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.warn("[仙灵] [刷新限时任务统计]", e);
			}
			int num = XianLingManager.instance.taskPrizeNum[task.getType()];
			List<ActivityProp> prizeList = XianLingManager.instance.timedTaskPrizeMap.get(task.getType());
			if (XianLingManager.logger.isDebugEnabled()) XianLingManager.logger.debug("[仙灵] [刷新限时任务] [XianLingManager.refreshTimedTask] [task.getType()" + task.getType() + "] [" + player.getLogString() + "] [限时任务id:" + task.getTaskId() + "] [num:" + num + "] [prizeList:" + prizeList.size() + "]");
			if (prizeList != null) {
				if (prizeList.size() > num) {
					String[] articleCNName = new String[num];
					int[] articleColor = new int[num];
					int[] articleNum = new int[num];
					boolean[] bind = new boolean[num];
					List<Integer> randomList = new ArrayList<Integer>();
					Random random = new Random();
					for (int i = 0; i < num; i++) {
						boolean find = false;
						while (!find) {
							int order = random.nextInt(prizeList.size());
							if (!randomList.contains(order)) {
								find = true;
								randomList.add(order);
								articleCNName[i] = prizeList.get(order).getArticleCNName();
								articleColor[i] = prizeList.get(order).getArticleColor();
								articleNum[i] = prizeList.get(order).getArticleNum();
								bind[i] = prizeList.get(order).isBind();
								if (XianLingManager.logger.isInfoEnabled()) XianLingManager.logger.info("[仙灵] [刷新限时任务] [XianLingManager.refreshTimedTask] [" + player.getLogString() + "] [限时任务id:" + task.getTaskId() + "] [order:" + order + "] [奖励物品名:" + articleCNName[i] + "] [颜色：" + articleColor[i] + "] [个数:" + Arrays.toString(articleNum) + "] [是否绑定:" + bind[i] + "]");
							}
						}

					}
					xianlingData.setArticleCNName(articleCNName);
					xianlingData.setArticleColor(articleColor);
					xianlingData.setArticleNum(articleNum);
					xianlingData.setBind(bind);
					if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.info("[仙灵] [银子刷新限时任务] [XianLingManager.Option_Confirm_SilverRefresh]" + player.getLogString() + "[限时任务id:" + task.getTaskId() + "] [奖励物品名:" + Arrays.toString(articleCNName) + "] [颜色：" + Arrays.toString(articleColor) + "] [个数:" + Arrays.toString(articleNum) + "] [是否绑定:" + Arrays.toString(bind) + "]");
				} else {
					String[] articleCNName = new String[num];
					int[] articleColor = new int[num];
					int[] articleNum = new int[num];
					boolean[] bind = new boolean[num];
					for (int i = 0; i < prizeList.size(); i++) {
						articleCNName[i] = prizeList.get(i).getArticleCNName();
						articleColor[i] = prizeList.get(i).getArticleColor();
						articleNum[i] = prizeList.get(i).getArticleNum();
						bind[i] = prizeList.get(i).isBind();
						if (XianLingManager.logger.isInfoEnabled()) XianLingManager.logger.info("[仙灵] [刷新限时任务] [XianLingManager.refreshTimedTask] [" + player.getLogString() + "] [限时任务id:" + task.getTaskId() + "] [奖励物品名:" + articleCNName[i] + "] [颜色：" + articleColor[i] + "] [个数:" + Arrays.toString(articleNum) + "] [是否绑定:" + bind[i] + "]");
					}
					xianlingData.setArticleCNName(articleCNName);
					xianlingData.setArticleColor(articleColor);
					xianlingData.setArticleNum(articleNum);
					xianlingData.setBind(bind);
					if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.info("[仙灵] [银子刷新限时任务] [XianLingManager.Option_Confirm_SilverRefresh]" + player.getLogString() + "[限时任务id:" + task.getTaskId() + "] [奖励物品名:" + Arrays.toString(articleCNName) + "] [颜色：" + Arrays.toString(articleColor) + "] [个数:" + Arrays.toString(articleNum) + "] [是否绑定:" + Arrays.toString(bind) + "]");
				}
			}
			XL_TIMEDTASK_RES res = XianLingManager.instance.send_TIMEDTASK_RES(task, xianlingData, player);
			player.addMessageToRightBag(res);
		} catch (NoEnoughMoneyException e) {
			if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [银子刷新限时任务] [异常] [Option_Confirm_SilverRefresh]" + player.getLogString(), e);
			e.printStackTrace();
		} catch (BillFailedException e) {
			if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [银子刷新限时任务] [异常] [Option_Confirm_SilverRefresh]" + player.getLogString(), e);
			e.printStackTrace();
		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
