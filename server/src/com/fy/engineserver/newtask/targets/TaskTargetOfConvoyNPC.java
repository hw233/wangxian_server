package com.fy.engineserver.newtask.targets;

import java.util.Arrays;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfConvoyNPC;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.TaskFollowableNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfConvoyNPC extends TaskTarget implements TaskConfig {

	/** 刷出来的NPCID */
	private int refreshNPCId;

	public TaskTargetOfConvoyNPC(int refreshNPCId, String[] targetName, String[] targetMapName) {
		setTargetType(TargetType.CONVOY);
		setTargetByteType(getTargetType().getIndex());
		setRefreshNPCId(refreshNPCId);
		//
		NPCTempalte tempalte = ((MemoryNPCManager) MemoryNPCManager.getNPCManager()).getNPCTempalteByCategoryId(refreshNPCId);
		setTargetName(new String[] { tempalte.npc.getName() });

		setMapName(targetMapName);
		setTargetNum(1);

		int[] x = new int[targetName.length];
		int[] y = new int[targetName.length];
		String[] resMapName = new String[targetName.length];
		for (int i = 0; i < targetMapName.length; i++) {
			resMapName[i] = "";
		}

		for (int i = 0; i < targetName.length; i++) {
			String npcName = targetName[i];
			CompoundReturn cr = TaskManager.getInstance().getNPCInfoByGameAndName(targetMapName[i], npcName);
			if (!cr.getBooleanValue()) {
				TaskManager.notices.append("[任务目标-护送npc][异常][ID:").append(TaskManager.currentLoadId).append("][地图名字:").append(targetMapName[i]).append("][NPC名字:").append(npcName).append("]<BR/>");
				TaskManager.logger.error("[任务目标-护送npc][异常][地图名:{}][NPC名:{}]", new Object[] { targetMapName[i], npcName });
				continue;
			}
			x[i] = cr.getIntValues()[0];
			y[i] = cr.getIntValues()[1];
		}
		setResMapName(resMapName);
		setX(x);
		setY(y);

		initNotic();
	}

	public long getRefreshNPCId() {
		return refreshNPCId;
	}

	public void setRefreshNPCId(int refreshNPCId) {
		this.refreshNPCId = refreshNPCId;
	}

	@Override
	public CompoundReturn dealOnGet(Player player, Task task) {
		if (TaskSubSystem.logger.isDebugEnabled()) {
			TaskSubSystem.logger.info(player.getLogString() + "[接受了有护送的任务]" + task.getName());
		}
		TaskFollowableNPC followableNPC = (TaskFollowableNPC) MemoryNPCManager.getNPCManager().createNPC(refreshNPCId);
		if (followableNPC == null) {
			if (TaskSubSystem.logger.isWarnEnabled()) TaskSubSystem.logger.warn(player.getLogString() + "[接受任务:{}][护送NPC不存在:{}]", new Object[] { task.getName(), refreshNPCId });
			return super.dealOnGet(player, task);
		}
//		if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
//			followableNPC.setX(3425 );
//			followableNPC.setY(1555);
//		} else {
			followableNPC.setX(player.getX());
			followableNPC.setY(player.getY());
//		}
		followableNPC.setCurrentGame(player.getCurrentGame());
		followableNPC.setBornTime(SystemTime.currentTimeMillis());
		Game game = player.getCurrentGame();
		if (game != null) {								//2015年9月6日  因为有玩家在切图时接任务，做容错
			followableNPC.setGameNames(game.gi);
		} else {										//地图为空，默认扔到镖师旁边
			game = GameManager.getInstance().getGameByName("kunlunshengdian", player.getCountry());
			followableNPC.setX(2681);
			followableNPC.setY(1979);
			followableNPC.setGameNames(game.gi);
		}
		followableNPC.setOwnerId(player.getId());
		followableNPC.setDeadTime(followableNPC.getBornTime() + followableNPC.getLife());

		followableNPC.setTargetMap(getMapName()[0]);
		followableNPC.setTargetX(getX()[0]);
		followableNPC.setTargetY(getY()[0]);

		followableNPC.setTaskName(task.getName());
		followableNPC.setCountry(player.getCountry());

		player.getCurrentGame().addSprite(followableNPC);
		player.setFollowableNPC(followableNPC);
		if (TaskSubSystem.logger.isWarnEnabled()) {
			TaskSubSystem.logger.warn(player.getLogString() + "[召唤了NPC:{}]", new Object[] { followableNPC.getName() });
		}
		return super.dealOnGet(player, task);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		if (isSameType(action)) {
			TaskActionOfConvoyNPC convoyNPC = (TaskActionOfConvoyNPC) action;
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn("[convoyNPC.getName(){}] [targetName{}]", new Object[] { convoyNPC.getName(), Arrays.toString(getTargetName()) });
			}
			if (inTargetNames(convoyNPC.getName())) {
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
		sbf.append(getTargetType().getName()).append("</td></tr><tr>");
		sbf.append("<td>");
		for (int i = 0; i < getTargetName().length; i++) {
			sbf.append(getTargetName()[i]).append(i < getTargetName().length - 1 ? " 或者 " : "");
		}
		sbf.append("</td></tr>");
		sbf.append("</table>");
		return sbf.toString();
	}
}
