package com.fy.engineserver.newtask.targets;

import java.util.Arrays;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfDiscovery;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfDiscovery extends TaskTarget implements TaskConfig {

	public TaskTargetOfDiscovery(String[] mapName, String[] targetArea) {
		TaskSubSystem.logger.error("[初始化探索任务] [mapName:" + Arrays.toString(mapName) + "] [targetArea:" + Arrays.toString(targetArea) + "]");
		setTargetType(TargetType.DISCOVERY);
		setTargetByteType(getTargetType().getIndex());
		setMapName(mapName);
		setTargetName(targetArea);
		setTargetNum(1);

		int[] x = new int[mapName.length];
		int[] y = new int[mapName.length];
		String[] resMapName = new String[mapName.length];
		for (int i = 0; i < resMapName.length; i++) {
			resMapName[i] = "";
		}
		for (int i = 0; i < mapName.length; i++) {
			Game game = GameManager.getInstance().getGameByDisplayName(mapName[i], CountryManager.国家A);
			if (game == null) {
				game = GameManager.getInstance().getGameByDisplayName(mapName[i], CountryManager.中立);
			}
			if (game != null) {
				MapArea ma = game.gi.getMapAreaByName(targetArea[i]);
				TaskSubSystem.logger.error("[初始化探索任务] [mapName:" + mapName[i] + "] [targetArea:" + targetArea[i] + "] [MapArea" + ma + "]");
				if (ma != null) {
					x[i] = ma.getX();
					y[i] = ma.getY();
					resMapName[i] = game.gi.name;
				} else {
					TaskManager.notices.append("[探索任务][区域不存在][ID:").append(TaskManager.currentLoadId).append("][区域:").append(targetArea[i]).append("]<BR/>");
					continue;
				}
			} else {
				TaskManager.logger.error("[加载任务][错误][探索任务][地图名:{}][区域名:{}]", new Object[] { mapName[i], targetArea[i] });
				TaskManager.notices.append("[探索任务][地图不存在][ID:").append(TaskManager.currentLoadId).append("][地图:").append(mapName[i]).append("]<BR/>");
			}
		}

		setX(x);
		setY(y);
		setResMapName(resMapName);
	}
	@Override
	public String gettarDes() {
		StringBuffer sb = new StringBuffer();
		sb.append(Translate.家族探索);
		sb.append("(0/1)");
		return sb.toString();
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		if (isSameType(action)) {
			TaskActionOfDiscovery tad = (TaskActionOfDiscovery) action;
			String[] currAreas = tad.getSelf().getCurrentMapAreaNames();
			if (currAreas != null) {
				for (String currArea : currAreas) {
					if (inTargetNames(currArea)) {
						return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(1);
					}
				}
			}
			String currArea = tad.getSelf().getCurrentMapAreaName();
			if (inTargetNames(currArea)) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(1);
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr><td>");
		sbf.append(getTargetType().getName()).append("</td></tr>");
		sbf.append("<tr>");
		for (int i = 0; i < getTargetName().length; i++) {
			sbf.append(getMapName()[i]).append(" 的区域 ").append(getTargetName()[i]).append(i < getTargetName().length - 1 ? " 或者 " : "");
		}
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskTargetOfDiscovery discovery = new TaskTargetOfDiscovery(new String[] { "a", "b" }, new String[] { "AA", "BB" });
		// System.out.println(discovery.toHtmlString("OOO"));
	}
}
