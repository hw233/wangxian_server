package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfGetArticleAndDelete extends TaskTarget implements TaskConfig {

	private int colorValue;

	public TaskTargetOfGetArticleAndDelete() {

	}

	public TaskTargetOfGetArticleAndDelete(String[] targetName, int targetColor, int targetNum) {
		setTargetType(TargetType.GET_ARTICLE_AND_DELETE);
		setTargetByteType(getTargetType().getIndex());
		setTargetName(targetName);
		
		setTargetColor(targetColor);
		setTargetNum(targetNum);

		String[] mapName = new String[targetName.length];
		String[] mapResName = new String[targetName.length];
		String[] showMonsterNames = new String[targetName.length];
		for (int i = 0; i < targetName.length; i++) {
			mapName[i] = "";
			mapResName[i] = "";
			showMonsterNames[i] = "";
		}
		int[] x = new int[targetName.length];
		int[] y = new int[targetName.length];
		mapName = mapName == null ? new String[0] : mapName;
		setResMapName(mapResName);
		setMapName(mapName);
		setShowMonsterNames(showMonsterNames);
		setX(x);
		setY(y);
		initNotic();
	}

	@Override
	public void initNotic() {
		super.initNotic();
		setNoticeName(Translate.text_task_032 + getNoticeName());
	}

	@Override
	public CompoundReturn dealOnGet(Player player, Task task) {
		int[] reachCount = new int[getTargetName().length];
		Article article = null;
		ArticleManager am = ArticleManager.getInstance();
		int maxIndex = -1;
		for (int i = 0; i < getTargetName().length; i++) {
			article = am.getArticle(getTargetName()[i]);
			if (article != null) {
				reachCount[i] = player.getArticleNum(article.getName(), getTargetColor(), BindType.BOTH);// (article.getName(), true, getTargetColor());//
																											// .getArticleEntityNum(article.getName());
				if (TaskSubSystem.logger.isInfoEnabled()) {
					TaskSubSystem.logger.info(player.getLogString() + "[接受任务:{}] [目标:{}] [颜色:{}] [已有:{}]", new Object[] { task.getName(), article.getName(), getTargetColor(), reachCount[i] });
				}
			}
			if (i == 0) {
				maxIndex = 0;
			} else {
				maxIndex = reachCount[i] > reachCount[i - 1] ? i : maxIndex;
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(reachCount[maxIndex] > 0).setIntValue(reachCount[maxIndex]);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		return super.dealAction(action);
	}

	/**
	 * 目标所需的物品是否都在身上了
	 * @param player
	 * @return
	 */
	public CompoundReturn articleInPackage(Player player) {
		for (int i = 0; i < getTargetName().length; i++) {
			String articleName = getTargetName()[i];
			int hasNum = player.getArticleNum(articleName, getTargetColor(), BindType.BOTH);
			if (hasNum < getTargetNum()) {
				String article = articleName;
				String notice = Translate.translateString(Translate.text_task_043, new String[][] { { Translate.STRING_1, article }, { Translate.COUNT_1, String.valueOf(getTargetNum()) }, { Translate.COUNT_2, String.valueOf(hasNum) } });
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(notice);
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	public void deleteTarget(Player player) {
		for (int i = 0; i < getTargetNum(); i++) {
			ArticleEntity ae = player.removeArticleByNameColorAndBind(getTargetName()[0], getTargetColor(), BindType.BOTH, "任务", true);
			ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "任务目标扣除", "");
			if (TaskSubSystem.logger.isInfoEnabled()) {
				TaskSubSystem.logger.info(player.getLogString() + "[完成任务,移除道具] [名字:{}] [颜色:{}] [对象:{}]", new Object[] { getTargetName()[0], getTargetColor(), ae });
			}
		}
	}
	
	public String gettarDes() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < getTargetName().length; i++) {
			if (getTargetName()[i].indexOf(Translate.宝石) < 0) {
				sb.append(getTargetName()[i]);
			} else {
				sb.append(Translate.收集宝石);
			}
			sb.append("(0/1)");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer(getTargetType().getName());
		sbf.append("\n-------------------\n");
		sbf.append("------新目标------\n");
		sbf.append("\n 颜色").append(getTargetColor()).append("数量").append(getTargetNum());
		return sbf.toString();
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr><td>");
		sbf.append(getTargetType().getName()).append("</td></tr><tr>");

		sbf.append("<td>颜色</td><td>").append(getTargetColor()).append("</td><td>数量</td><td>").append(getTargetNum()).append("</td></tr>");

		sbf.append("</table>");
		return sbf.toString();
	}
}
