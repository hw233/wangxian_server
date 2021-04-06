package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskConfig.TargetType;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 任务目标 ：采集
 * 
 * 
 */
public class TaskTargetOfCollection extends TaskTarget {

	public TaskTargetOfCollection(String[] mapName, String[] targetName, int targetColor, int targetNum) {
		try {
			setTargetType(TargetType.COLLECTION);
			setTargetByteType(getTargetType().getIndex());
			setTargetColor(targetColor);
			setTargetName(targetName);
			setTargetNum(targetNum);

			int[] x = new int[targetName.length];
			int[] y = new int[targetName.length];
			String[] resMapName = new String[targetName.length];

			for (int i = 0; i < resMapName.length; i++) {
				resMapName[i] = "";
			}
			for (int i = 0; i < mapName.length; i++) {
				Game game = GameManager.getInstance().getGameByDisplayName(mapName[i], CountryManager.国家A);
				if (game == null) {
					game = GameManager.getInstance().getGameByDisplayName(mapName[i], CountryManager.中立);
				}
				if (game != null) {
					CompoundReturn cr = TaskManager.getInstance().getNPCInfoByGameAndName(mapName[i], targetName[i]);
					if (cr.getBooleanValue()) {
						x[i] = cr.getIntValues()[0];
						y[i] = cr.getIntValues()[1];
					}
					resMapName[i] = game.gi.name;
				} else {
					TaskManager.logger.error("[采集目标][地图没取到:{}]", new Object[] { mapName[i] });
					TaskManager.notices.append("[采集目标地图没取到][任务ID:").append(TaskManager.currentLoadId).append("][地图名字:").append(mapName[i]).append("]<BR/>");
				}
			}
			setX(x);
			setY(y);
			setResMapName(resMapName);
			setMapName(mapName);
			initNotic();
		} catch (Exception e) {
			TaskManager.logger.error("[采集目标]", e);
		}
	}

	@Override
	public void initNotic() {
		super.initNotic();
		setNoticeName(Translate.text_task_032 + getNoticeName());
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		return super.dealAction(action);
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr><td>");
		sbf.append(getTargetType().getName()).append("</td></tr><tr>");
		sbf.append("<td>");
		sbf.append(getTargetName()[0]).append("[颜色:]").append(getTargetColor()).append("[数量]").append("</td></tr>").append("</table>");
		return sbf.toString();
	}
}
