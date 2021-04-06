package com.fy.engineserver.newtask.targets;

import java.util.Arrays;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfUseArticle;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfUseArticle extends TaskTarget implements TaskConfig {

	private String[] targetArea = new String[0];
	
	private int[] targetPoint = new int[0];

	/**
	 * 
	 * @param targetName目标名
	 * @param mapName目标所在地图
	 * @param targetX目标X坐标
	 * @param targetY目标Y坐标
	 * @param targetColor目标颜色
	 * @param targetNum目标数量
	 */
	public TaskTargetOfUseArticle(String[] targetName, int targetColor, int targetNum, String[] targetArea, String[] mapName) {
		setTargetType(TargetType.USE_ARTICLE);
		setTargetByteType(getTargetType().getIndex());
		setTargetName(targetName);
		setTargetColor(targetColor);
		setTargetNum(targetNum);
		setTargetArea(targetArea);
		setMapName(mapName);
		int[] x = new int [targetArea.length];
		int[] y = new int [targetArea.length];
		setX(x);
		setY(y);
	}
	public TaskTargetOfUseArticle(String[] targetName, int targetColor, int targetNum, int x, int y, String[] mapName) {
		setTargetType(TargetType.USE_ARTICLE);
		setTargetByteType(getTargetType().getIndex());
		setTargetName(targetName);
		setTargetColor(targetColor);
		setTargetNum(targetNum);
		setTargetArea(targetArea);
		setMapName(mapName);
		setTargetPoint(new int[]{x, y});
	}

	public String[] getTargetArea() {
		return targetArea;
	}

	public void setTargetArea(String[] targetArea) {
		this.targetArea = targetArea;
	}

	public TaskTargetOfUseArticle() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		return super.dealAction(action);
//		if (isSameType(action)) {
//			TaskActionOfUseArticle tad = (TaskActionOfUseArticle) action;
//			Game game = tad.getSelf().getCurrentGame();
//			Player p = tad.getSelf();
//			if (game != null) {
//				for (String mapName : getMapName()) {
//					if (inTargetNames(mapName) && colorFit(action) && game.country == tad.getSelf().getCountry() && p.getX() == targetPoint[0] && p.getY() == targetPoint[1]) {
//						return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(1);
//					}
//				}
//			}
//		}
//		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}
	@Override
	public String gettarDes() {
		StringBuffer sb = new StringBuffer();
		sb.append(Translate.text_436);
		if (getTargetName() != null && getTargetName().length > 0) {
			sb.append(getTargetName()[0]);
		}
		sb.append("(0/1)");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer(getTargetType().getName());
		sbf.append("\n---------------------------\n");
		sbf.append("物品名>>>>");
		for (int i = 0; i < getTargetName().length; i++) {
			String s = getTargetName()[i];
			sbf.append(s).append(",地图[").append(getMapName()[i]).append("]区域[").append(getTargetArea()[i]).append("]").append(i < getTargetName().length - 1 ? "【或者】" : "");
		}
		sbf.append(",[坐标:" + Arrays.toString(targetPoint) + "]");
		sbf.append("\n颜色[").append(getTargetColor()).append("]数量[").append(getTargetNum()).append("]");
		return sbf.toString();
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr><td>");
		sbf.append(getTargetType().getName()).append("</td></tr><tr>");
		sbf.append("<td>");

		for (int i = 0; i < getTargetName().length; i++) {
			String s = getTargetName()[i];
			sbf.append(s).append(",在地图[").append(getMapName()[i]).append("]区域[").append(getTargetArea()[i]).append("]").append(i < getTargetName().length - 1 ? "【或者】" : "");
		}
		sbf.append(",[坐标:" + Arrays.toString(targetPoint) + "]");
		sbf.append("\n颜色[").append(getTargetColor()).append("]数量[").append(getTargetNum()).append("]</td>");

		sbf.append("</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskTargetOfUseArticle useArticle = new TaskTargetOfUseArticle(new String[] { "A1", "A2" }, 1, 2, new String[] { "P1", "P2" }, new String[] { "M1", "M2" });
//		System.out.println(useArticle.toHtmlString("UU"));
	}
	public int[] getTargetPoint() {
		return targetPoint;
	}
	public void setTargetPoint(int[] targetPoint) {
		this.targetPoint = targetPoint;
	}
}
