package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

/**
 * 任务奖励类型-物品
 * 
 * 
 */
public class TaskPrizeOfArticle extends TaskPrize implements TaskConfig {
	private int[] colorType;

	private TaskPrizeOfArticle() {
		setPrizeType(PrizeType.ARTICLE);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int[] prizeColor, String[] prizeName, long[] prizeNum, int totalNum) {
		TaskPrizeOfArticle prize = new TaskPrizeOfArticle();
		prize.setPrizeName(prizeName);
		prize.setPrizeNum(prizeNum);
		prize.setTotalNum(totalNum);
		prize.setColorType(prizeColor);
		prize.setPrizeColor(new int[prizeColor.length]);
		return prize;
	}

	/**
	 * 发任务奖励, index 是玩家选择的任务奖励
	 * index == null 则发奖励[0]位置的奖励
	 * [1,2,5]第125位置的奖励
	 */
	@Override
	public void doPrize(Player player, int[] index, Task task) {
		try {
			if (index == null) {
				if (getPrizeName() != null && getPrizeName().length == 1) {// 单一奖励
					Article a = ArticleManager.getInstance().getArticle(getPrizeName()[0]);
					if (a != null) {
						TaskManager.getInstance().addArticleAndNoticeClient(player, a, getPrizeNum()[0], getColorType()[0]);
						if (TaskSubSystem.logger.isWarnEnabled()) {
							TaskSubSystem.logger.warn(player.getLogString() + "[完成任务] [获得物品:{}] [数量:{}] [颜色:{}]", new Object[] { (a == null ? "NULL" : a.getName()), getPrizeNum()[0], getColorType()[0] });
						}
					} else {
						TaskSubSystem.logger.error(player.getLogString() + " [选择奖励:{}不存在]", new Object[] { getPrizeName() });
					}
				}
			} else {
				if (index.length > getTotalNum() || !choiceAble()) {
					if (TaskSubSystem.logger.isWarnEnabled()) {
						TaskSubSystem.logger.warn(player.getLogString() + "[选择奖励和奖励上限不匹配]");
					}
					return;
				}
				for (int i = 0; i < index.length; i++) {
					String name = getPrizeName()[index[i]];
					int color = getColorType()[index[i]];
					long num = getPrizeNum()[index[i]];
					Article a = ArticleManager.getInstance().getArticle(name);
					if (a != null) {
						if (TaskSubSystem.logger.isDebugEnabled()) {
							TaskSubSystem.logger.debug(player.getLogString() + "[选择奖励:{}]", new Object[] { a.getName() });
						}
						TaskManager.getInstance().addArticleAndNoticeClient(player, a, num, color);
					} else {
						TaskSubSystem.logger.error(player.getLogString() + " [选择奖励:{}不存在]", new Object[] { name });
						return;
					}
				}
			}
		} catch (Exception e) {
			TaskSubSystem.logger.error("[奖励物品] [异常]", e);
		}
	}

	/**
	 * 是否是可选任务奖励
	 * @return
	 */
	public boolean choiceAble() {
		return getTotalNum() > 0;
	}

	@Override
	public void initArticle(Task task) {
		prizeId = new long[getPrizeName().length];
		for (int i = 0; i < getPrizeName().length; i++) {
			String name = getPrizeName()[i];
			Article article = ArticleManager.getInstance().getArticle(name);
			if (article != null) {
				try {
					ArticleEntity ae = TaskManager.getInstance().getEntityManager().createTempEntity(article, true, ArticleEntityManager.CREATE_REASON_TASK_REWARD, null, getColorType()[i]);
					if (ae != null) {
						getPrizeColor()[i] = ArticleManager.getColorValue(article, ae.getColorType());
						if (TaskManager.logger.isWarnEnabled()) TaskManager.logger.warn("[任务系统初始化] [init任务奖励物品:{}] [ID:{}] [ICON:{}] [颜色:{}]", new Object[] { ae.getArticleName(), ae.getId(), article.getIconId(), getPrizeColor()[i] });
					} else {
						if (TaskManager.logger.isWarnEnabled()) TaskManager.logger.warn("[任务系统初始化] [init任务奖励物品:{}]", new Object[] { ae });
					}
					prizeId[i] = ae.getId();
				} catch (Exception ex) {
					TaskManager.logger.error("[任务奖励初始化物品异常]", ex);
				}
			} else {
				TaskManager.logger.error("[任务系统初始化] [init任务奖励物品不存在:{}]", new Object[] { name });
				TaskManager.notices.append("[任务奖励物品不存在] [" + task.getId() + "]").append("[").append(name).append("]<BR/>");
			}
		}

	}

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer(getPrizeType().getName());
		sbf.append("\n--------\n");
		sbf.append(choiceAble() ? "可选奖励个数:" : "固定奖励").append(choiceAble() ? getTotalNum() : "").append("\n");
		for (int i = 0; i < getPrizeColor().length; i++) {
			sbf.append("颜色[<font color=").append(getPrizeColor()[i]).append(">").append(getPrizeColor()[i]).append("]</font>名字 [").append(getPrizeName()[i]).append("]数量[").append(getPrizeNum()[i]).append("]").append(i < getPrizeColor().length - 1 ? "【或者】" : "");
		}
		return sbf.toString();
	}

	public int[] getColorType() {
		return colorType;
	}

	public void setColorType(int[] colorType) {
		this.colorType = colorType;
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr>");
		sbf.append("<td>");
		sbf.append(getPrizeType().getName());
		sbf.append("</td>");
		sbf.append("<td>");
		sbf.append(choiceAble() ? "可选奖励个数:" : "固定奖励").append(choiceAble() ? getTotalNum() : "");
		for (int i = 0; i < getPrizeColor().length; i++) {
			sbf.append("颜色[").append(getPrizeColor()[i]).append("]名字 [").append(getPrizeName()[i]).append("]数量[").append(getPrizeNum()[i]).append("]").append(i < getPrizeColor().length - 1 ? "【或者】" : "");
		}
		sbf.append("</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskPrize article = TaskPrizeOfArticle.createTaskPrize(new int[] { 1, 2, 3 }, new String[] { "11", "22", "33" }, new long[] { 111, 222, 333 }, 2);
	}
}
