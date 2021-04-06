package com.fy.engineserver.core.client;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.npc.NPC;

/**
 * 功能性NPC信息
 */
public class FunctionNPC implements TaskConfig {

	public long id;
	public String mapName;
	public String name;
	public String title;

	public byte functionType;

	public int x;

	public int y;

	// 可接的任务列表
	public long avaiableTaskIds[] = new long[0];

	// 可接任务的类型 是否日常
	public byte avaiableTaskTypes[] = new byte[0];

	public String avaiableTaskNames[] = new String[0];
	public int avaiableTaskGrades[] = new int[0];

	private Game currentGame;

	public FunctionNPC() {

	}

	@Override
	protected void finalize() throws Throwable {

	}

	public FunctionNPC(Game currentGame, NPC npc) {
		this();
		this.currentGame = currentGame;
		this.id = npc.getId();
		this.name = npc.getName();
		this.mapName = currentGame.gi.displayName;
		this.functionType = npc.getNpcType();

		if (this.functionType < 0 || this.functionType >= Sprite.NPC_SHORT_NAMES.length) this.title = "";
		else this.title = Sprite.NPC_SHORT_NAMES[this.functionType];

		this.x = (int) npc.getX();
		this.y = (int) npc.getY();
	}

	// 所有玩家暂时不能接的任务列表(等待变化的列表)
	private Hashtable<ModifyType, ArrayList<Long>> waitForChange = new Hashtable<ModifyType, ArrayList<Long>>();
	private Hashtable<ModifyType, ArrayList<Long>> removeForChange = new Hashtable<ModifyType, ArrayList<Long>>();

	public void addTask2Wait(ModifyType modifyType, Task task) {
		try {
			// 先在其他列表中移除当前这个任务
			for (Iterator<ModifyType> itor = this.getWaitForChange().keySet().iterator(); itor.hasNext();) {
				ModifyType type = itor.next();
				if (modifyType.equals(type)) {
					continue;
				}
				if (this.getWaitForChange().get(type).contains(task.getId())) {
					if (!removeForChange.containsKey(type)) {
						removeForChange.put(type, new ArrayList<Long>());
					}
					removeForChange.get(type).add(task.getId());
					// if (TaskSubSystem.logger.isDebugEnabled()) {
					// TaskSubSystem.logger.debug("[任务等待列表:{}][包含任务:{}][移除后:{}][要加入到列表:{}]", new Object[] { type.getName(), task.getName(), modifyType.getName() });
					// }
				}
			}
			List<Long> waitTasks = this.getWaitForChange().get(modifyType);
			if (waitTasks == null) {
				this.getWaitForChange().put(modifyType, new ArrayList<Long>());
			}
			if (!this.getWaitForChange().get(modifyType).contains(task.getId())) {
				this.getWaitForChange().get(modifyType).add(task.getId());
			}
		} catch (Exception e) {
			TaskSubSystem.logger.error("[增加等待列表异常]", e);
		}
	}

	// Long >>> 任务ID value
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long[] getAvaiableTaskIds() {
		return avaiableTaskIds;
	}

	public void setAvaiableTaskIds(long[] avaiableTaskIds) {
		this.avaiableTaskIds = avaiableTaskIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public byte getFunctionType() {
		return functionType;
	}

	public void setFunctionType(byte functionType) {
		this.functionType = functionType;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public byte[] getAvaiableTaskTypes() {
		return avaiableTaskTypes;
	}

	public void setAvaiableTaskTypes(byte[] avaiableTaskTypes) {
		this.avaiableTaskTypes = avaiableTaskTypes;
	}

	public Hashtable<ModifyType, ArrayList<Long>> getWaitForChange() {
		return waitForChange;
	}

	public void setWaitForChange(Hashtable<ModifyType, ArrayList<Long>> waitForChange) {
		this.waitForChange = waitForChange;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String[] getAvaiableTaskNames() {
		return avaiableTaskNames;
	}

	public void setAvaiableTaskNames(String[] avaiableTaskNames) {
		this.avaiableTaskNames = avaiableTaskNames;
	}

	public int[] getAvaiableTaskGrades() {
		return avaiableTaskGrades;
	}

	public void setAvaiableTaskGrades(int[] avaiableTaskGrades) {
		this.avaiableTaskGrades = avaiableTaskGrades;
	}

	public Game getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(Game currentGame) {
		this.currentGame = currentGame;
	}

	public Hashtable<ModifyType, ArrayList<Long>> getRemoveForChange() {
		return removeForChange;
	}

	public void setRemoveForChange(Hashtable<ModifyType, ArrayList<Long>> removeForChange) {
		this.removeForChange = removeForChange;
	}

	@Override
	public String toString() {
		return "FunctionNPC [id=" + id + ", mapName=" + mapName + ", name=" + name + ", title=" + title + ", functionType=" + functionType + ", x=" + x + ", y=" + y + "]";
	}

}
