package com.fy.engineserver.newtask.service;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.hotspot.Hotspot;
import com.fy.engineserver.hotspot.HotspotManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.sprite.Player;

//热点完成条件的任务
public class HotspotTaskEventTransact extends AbsTaskEventTransact{

	public static String[] deliver_TaskName;
	
	private HotspotManager manager;
	
	public void init(){}
	
	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch(action){
		case deliver:
			return deliver_TaskName;
		}
		return null;
	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		HotspotManager.getInstance().overHotspot(player, Hotspot.OVERTYPE_TASK, task.getName());
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
	}

	public void setManager(HotspotManager manager) {
		this.manager = manager;
	}

	public HotspotManager getManager() {
		return manager;
	}

}
