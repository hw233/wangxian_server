package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.core.Position;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager.MonsterTempalte;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.worldmap.WorldMapManager;

public class TaskTargetOfMonster extends TaskTarget implements TaskConfig {

	/**
	 * 
	 * @param targetName目标名
	 * @param targetColor目标颜色
	 * @param targetNum目标数量
	 */
	public TaskTargetOfMonster(String[] targetName, int targetColor, int targetNum,int country) {
		// TODO targetColor 忽略颜色,都默认改成-1
		targetColor = -1;
		setTargetType(TargetType.MONSTER);
		setTargetByteType(getTargetType().getIndex());
		setTargetName(targetName);
		setTargetColor(targetColor);
		setTargetNum(targetNum);

		String[] mapName = new String[targetName.length];
		String[] resMapName = new String[targetName.length];
		int[] x = new int[targetName.length];
		int[] y = new int[targetName.length];
		for (int i = 0; i < resMapName.length; i++) {
			resMapName[i] = "";
		}
		for (int i = 0; i < targetName.length; i++) {
			String name = targetName[i];
			Position position = null;
			if(WorldMapManager.monsterPositions2.get(name) != null){
				position = WorldMapManager.monsterPositions2.get(name).get(country);
			}
			if(position == null){
				position = WorldMapManager.monsterPositions.get(name);
			}
			if (position != null) {
				mapName[i] = position.getMapCName();
				resMapName[i] = position.getMapEName();
				x[i] = position.getX();
				y[i] = position.getY();
			} else {
				if(TaskManager.logger.isInfoEnabled())
					TaskManager.logger.info("[设置任务目标位置>NULL]{}{}", new Object[] { country,name });
				TaskManager.notices.append("[杀确定的怪][怪物不存在或者位置不存在][ID:").append(TaskManager.currentLoadId).append("][怪物:").append(name).append("][位置:").append(position).append("]<BR/>");
				mapName[i] = "";
				resMapName[i] = "";
				x[i] = 0;
				y[i] = 0;
			}
		}
		setResMapName(resMapName);
		setMapName(mapName);
		setX(x);
		setY(y);

		initNotic();
	}

	@Override
	public void initNotic() {
		super.initNotic();
		setNoticeName(Translate.text_task_030 + getNoticeName());
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		return super.dealAction(action);
	}
	
	public int gettarLevel() {
		try {
			if (getTargetName() != null && getTargetName().length > 0) {
				 Integer monsterId = ((MemoryMonsterManager)MemoryMonsterManager.getMonsterManager()).monsterName.get(getTargetName()[0]);
				 MonsterTempalte mt = ((MemoryMonsterManager)MemoryMonsterManager.getMonsterManager()).getTemplates().get(monsterId);
				 return mt.monster.getLevel();
			}
		} catch (Exception e) {
			JiazuManager2.logger.warn("[家族任务] [获取目标怪物等级] [异常] [getTargetName() : " + getTargetName() + "]", e);
		}
		return -1;
	}
	
	@Override
	public String gettarDes() {
		StringBuffer sb = new StringBuffer();
		if (getTargetName() != null && getTargetName().length > 0) {
			sb.append(getTargetName()[0]);
		}
		sb.append("(0/").append(getTargetNum()).append(")");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer(getTargetType().getName());
		sbf.append("\n-----------\n");
		sbf.append("怪物名");
		for (int i = 0; i < getTargetName().length; i++) {
			sbf.append("[").append(getTargetName()[i]).append("]颜色[").append(getTargetColor()).append("]数量[").append(getTargetNum()).append("]").append(i < getTargetName().length - 1 ? "【或者】" : "");
		}
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
			sbf.append("[").append(getTargetName()[i]).append("]颜色[").append(getTargetColor()).append("]数量[").append(getTargetNum()).append("]").append(i < getTargetName().length - 1 ? "【或者】" : "");
		}
		sbf.append("</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
//		TaskTargetOfMonster monster = new TaskTargetOfMonster(new String[] { "M1", "M2" }, 1, 22);
//		System.out.println(monster.toHtmlString("SS"));
	}
}
