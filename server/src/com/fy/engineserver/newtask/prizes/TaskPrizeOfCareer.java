package com.fy.engineserver.newtask.prizes;

import java.util.Arrays;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;

public class TaskPrizeOfCareer extends TaskPrize {

	private int[] colorType;

	private TaskPrizeOfCareer() {
		setPrizeType(PrizeType.CAREER_ARTICLE);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(String[] prizeName, int[] prizeColor, long[] prizeNum) {
		TaskPrizeOfCareer prize = new TaskPrizeOfCareer();
		prize.setPrizeName(prizeName);
		prize.setColorType(prizeColor);
		prize.setPrizeColor(new int[prizeColor.length]);
		prize.setPrizeNum(prizeNum);
		return prize;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		if (TaskSubSystem.logger.isInfoEnabled()) {
			TaskSubSystem.logger.info(player.getLogString() + "[开始发放职业奖励]" + Arrays.toString(getPrizeName()) + "," + Arrays.toString(getPrizeNum()));
		}
		byte playerCareer = player.getMainCareer();
		if (getPrizeName() != null) {
			String articleName = getPrizeName()[playerCareer - 1];
			Article article = ArticleManager.getInstance().getArticle(articleName);
			if (article != null) {
				for (int num = 0; num < getPrizeNum()[playerCareer - 1]; num++) {
					try {
						ArticleEntity articleEntity = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_TASK_REWARD, player, getColorType()[playerCareer - 1], 1, true);
						if (articleEntity != null) {
							player.putToKnapsacks(articleEntity, "任务");
							player.noticeGetArticle(articleEntity);
							ArticleStatManager.addToArticleStat(player, null, articleEntity, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "任务", "");
							if (TaskSubSystem.logger.isInfoEnabled()) {
								TaskSubSystem.logger.info(player.getLogString() + "[职业任务奖励] [确定发放职业奖励] [成功] [物品:{}] [颜色:{}]", new Object[] { article.getName(), article.getColorType() });
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} else {
				TaskSubSystem.logger.error(player.getLogString() + "[职业任务奖励] [严重错误] [任务按职业给予物品] [物品不存在] [{}]", new Object[] { articleName });
			}
		}
	}

	public int[] getColorType() {
		return colorType;
	}

	public void setColorType(int[] colorType) {
		this.colorType = colorType;
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
					prizeId[i] = ae.getId();
					getPrizeColor()[i] = ArticleManager.getColorValue(article, ae.getColorType());
					if (TaskManager.logger.isInfoEnabled()) TaskManager.logger.info("[][创建临时物品] [Id:{}] [name:{}][class:{}] [配置颜色:{}] [生成的颜色:{}]", new Object[] { ae.getId(), ae.getArticleName(), ae.getClass(), getPrizeColor()[i], ae.getColorType() });
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				TaskManager.logger.error("任务奖励物品不存在:{}", new Object[] { name });
				TaskManager.notices.append("[任务奖励] [职业物品不存在] [" + task.getId() + "]").append("[").append(name).append("]<BR/>");
			}
		}
	}

	@Override
	public String toHtmlString(String cssClass) {
		return super.toHtmlString(cssClass);
	}
}
