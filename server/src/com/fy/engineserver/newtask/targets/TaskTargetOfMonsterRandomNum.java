package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.core.Position;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.worldmap.WorldMapManager;

/**
 * 杀随机个数的怪
 * 
 * 
 */
public class TaskTargetOfMonsterRandomNum extends TaskTarget implements TaskConfig, RandomNum {

	/** 最小数量 */
	private int minNum;
	/** 最大数量 */
	private int maxNum;

	public TaskTargetOfMonsterRandomNum(int targetColor, String targetName, int minNum, int maxNum) {
		
		
		setTargetName(new String[] { targetName });
		
		setTargetType(TargetType.MONSTER_RANDOM_NUM);
		setTargetByteType(getTargetType().getIndex());
		
		setTargetColor(targetColor);
		setMinNum(minNum);
		setMaxNum(maxNum);

		String[] mapName = new String[1];
		String[] resMapName = new String[1];
		int[] x = new int[1];
		int[] y = new int[1];
		for (int i = 0; i < resMapName.length; i++) {
			resMapName[i] = "";
		}
		String name = targetName;
		Position position = WorldMapManager.monsterPositions.get(name);
		if (position != null) {
			mapName[0] = position.getMapCName();
			resMapName[0] = position.getMapEName();
			x[0] = position.getX();
			y[0] = position.getY();
			if(TaskManager.logger.isInfoEnabled())
				TaskManager.logger.info("[随机怪个数 设置任务目标]{}", new Object[]{position.toString()});
		} else {
			TaskManager.notices.append("[随机怪物个数][怪物不存在或者位置没找到][ID:").append(TaskManager.currentLoadId).append("]<BR/>");
		}
		setResMapName(resMapName);
		setMapName(mapName);
		setX(x);
		setY(y);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		return super.dealAction(action);
	}

	@Override
	public boolean isRandomNum() {
		return true;
	}

	public int getMinNum() {
		return minNum;
	}

	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	@Override
	public String toString() {
		return "TaskTargetOfMonsterRandomNum [minNum=" + minNum + ", maxNum=" + maxNum + "]";
	}
}
