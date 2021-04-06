package com.fy.engineserver.newtask.targets;

import java.util.Arrays;

import com.fy.engineserver.core.Position;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.worldmap.WorldMapManager;

public class TaskTargetOfGetArticle extends TaskTarget implements TaskConfig {

	/** 掉物品的怪 */
	private String[][] monsterNames = new String[0][];
	/** 掉物品的怪颜色 */
	private int[][] monsterColors = new int[0][];
	/** 掉率 */
	private double[][] drops = new double[0][];

	public TaskTargetOfGetArticle() {

	}

	public TaskTargetOfGetArticle(String[] targetName, int targetColor, int targetNum, String[][] monsterNames, int[][] monsterColors, double[][] drops,int country) {
		setTargetType(TargetType.GET_ARTICLE);
		setTargetByteType(getTargetType().getIndex());
		setTargetName(targetName);
		setTargetColor(targetColor);
		setTargetNum(targetNum);

		setMonsterNames(monsterNames);
		setMonsterColors(monsterColors);
		setDrops(drops);

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
		for (int i = 0; i < targetName.length; i++) {
			if (monsterNames[i] != null && monsterNames[i].length > 0) {
				String name = monsterNames[i][0];
				Position position = null;
				if(WorldMapManager.monsterPositions2.get(name) != null){
					position = WorldMapManager.monsterPositions2.get(name).get(country);
				}
				if(position == null){
					position = WorldMapManager.monsterPositions.get(name);
				}
//				Position position = WorldMapManager.monsterPositions.get(monsterNames[i][0]);// 测试 ,先找第0个
				if (position != null) {
					mapName[i] = position.getMapCName() == null ? "" : position.getMapCName();
					mapResName[i] = position.getMapEName();
					x[i] = position.getX();
					y[i] = position.getY();
					showMonsterNames[i] = monsterNames[i][0];
					// });
				} else {
					TaskManager.notices.append("[获得物品任务][打怪获得][怪物位置不存在][ID:").append(TaskManager.currentLoadId).append("][怪物名字:").append(monsterNames[i][0]).append("]<BR/>");
					mapName[i] = "";
					mapResName[i] = "";
				}
			} else {
				if (monsterNames[i] != null) {
					TaskManager.notices.append("[获得物品任务][打怪获得][怪物不存在][ID:").append(TaskManager.currentLoadId).append("][怪物名字:").append(Arrays.toString(monsterNames[i])).append("]<BR/>");
				}
			}
		}
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

	public String[][] getMonsterNames() {
		return monsterNames;
	}

	public void setMonsterNames(String[][] monsterNames) {
		this.monsterNames = monsterNames;
	}

	public int[][] getMonsterColors() {
		return monsterColors;
	}

	public void setMonsterColors(int[][] monsterColors) {
		this.monsterColors = monsterColors;
	}

	public double[][] getDrops() {
		return drops;
	}

	public void setDrops(double[][] drops) {
		this.drops = drops;
	}

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer(getTargetType().getName());
		sbf.append("\n-------------------\n");
		sbf.append("------新目标------\n");
		for (int i = 0; i < getTargetName().length; i++) {
			String name = getTargetName()[i];
			sbf.append(name).append("\n怪物").append(i).append("\n");
			if (getMonsterNames()[i] == null || getMonsterNames()[i].length == 0) {
				sbf.append("不是打怪获得\n");
			} else {
				for (int j = 0; j < getMonsterNames()[i].length; j++) {
					sbf.append("名字:").append(getMonsterNames()[i][j]).append(",颜色").append(getMonsterColors()[i][j]).append(",掉率").append(getDrops()[i][j]);
				}
			}
		}
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
		for (int i = 0; i < getTargetName().length; i++) {
			sbf.append("<tr><td>[或]</td><td>").append(getTargetName()[i]).append("</td>");
			if (getMonsterNames()[i] == null || getMonsterNames()[i].length == 0) {
				sbf.append("<td>").append("不是打怪获得</td><td><tr>");
			} else {
				sbf.append("是打怪获得</td><td>");
				for (int j = 0; j < getMonsterNames()[i].length; j++) {
					sbf.append("怪物名字:").append(getMonsterNames()[i][j]).append(",怪物颜色").append(getMonsterColors()[i][j]).append(",物品掉率").append(getDrops()[i][j]).append("</td></tr>");
				}
			}
		}

		sbf.append("</table>");
		return sbf.toString();
	}
}
