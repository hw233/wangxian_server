package com.fy.engineserver.activity.dig;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.props.DigProps;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;

public class DigActivityTaskEventTransact extends AbsTaskEventTransact {

	public static long[] needSilver = { 100000 };

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case accept:
			// TODO 加翻译
			return new String[] { "挖宝" };
		case drop:
			return new String[] { "挖宝" };
		}
		;
		return null;
	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
//		ArticleEntityManager aem = ArticleEntityManager.getInstance();
//		DigManager dm = DigManager.getInstance();
//		String[] taskNames = getWannaDealWithTaskNames(Taskoperation.accept);
//		if (taskNames != null) {
//			for (int i = 0; i < taskNames.length; i++) {
//				if (task.getName().equals(taskNames[i])) {
//					Article a = ArticleManager.getInstance().getArticle("挖宝图");
//					if (a != null && a instanceof DigProps) {
//						if (player.getSilver() >= needSilver[i]) {
//							if (!player.getKnapsack_common().isFull()) {
//								try {
//									DigPropsEntity dpe = (DigPropsEntity)aem.createEntity((DigProps) a, true, ArticleEntityManager.挖宝, player, 1, 1, true);
//									player.putToKnapsacks(dpe, "挖宝");
//									BillingCenter.getInstance().playerExpense(player, needSilver[i], CurrencyType.YINZI, ExpenseReasonType.接取任务, "挖宝");
//									ArticleStatManager.addToArticleStat(player, null, dpe, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "挖宝", null);
//
//									TaskSubSystem.logger.warn("[放入挖宝图到普通背包成功] [" + player.getLogString() + "]");
//								} catch (Exception ex) {
//									TaskSubSystem.logger.error("[放入挖宝图到普通背包异常] [" + player.getLogString() + "]", ex);
//								}
//							} else if (!player.getKnapsack_fangbao().isFull()) {//TODO 判断一下防爆包到期
//								try {
//									DigPropsEntity dpe = (DigPropsEntity)aem.createEntity((DigProps) a, true, ArticleEntityManager.挖宝, player, 1, 1, true);
//									player.getKnapsack_fangbao().put(dpe, "挖宝");
//									BillingCenter.getInstance().playerExpense(player, needSilver[i], CurrencyType.YINZI, ExpenseReasonType.接取任务, "挖宝");
//									ArticleStatManager.addToArticleStat(player, null, dpe, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "挖宝", null);
//
//									TaskSubSystem.logger.warn("[放入挖宝图到防爆背包成功] [" + player.getLogString() + "]");
//								} catch (Exception ex) {
//									TaskSubSystem.logger.error("[放入挖宝图到防爆背包异常] [" + player.getLogString() + "]", ex);
//								}
//							} else {
//								// TODO
//								player.sendError("背包已满");
//							}
//						} else {
//							// TODO
//							player.sendError("银子不足");
//						}
//					} else {
//						// TODO
//						TaskSubSystem.logger.warn("[不是挖宝道具] [" + player.getLogString() + "]");
//					}
//				}
//			}
//		}

	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

}
