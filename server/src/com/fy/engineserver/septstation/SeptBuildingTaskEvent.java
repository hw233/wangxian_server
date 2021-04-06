package com.fy.engineserver.septstation;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;

/**
 * 家族建设任务
 * 
 * 
 */
public class SeptBuildingTaskEvent extends AbsTaskEventTransact {

	private TaskManager taskManager;
	private SeptStationMapTemplet stationMapTemplet;

	private String[] tasks;

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case deliver:
			return tasks;
		default:
			break;
		}
		return null;
	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		
		/**
		 * 家族升级用倒计时来代替完成任务
		 */
//		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
//		if (jiazu != null) {
//			SeptBuildingEntity sbe = jiazu.getInbuildingEntity();
//			if (sbe != null) {
//				sbe.onDeliverTask(player);
//			}
//			if (JiazuSubSystem.logger.isInfoEnabled()) {
//				JiazuSubSystem.logger.info(player.getLogString() + "[完成艰建设任务:{}][家族:{}]", new Object[] { task.getName(), jiazu.getName() });
//			}
//		} else {
//			JiazuSubSystem.logger.error(player.getLogString() + "[完成艰建设任务:{}][家族不存在]", new Object[] { task.getName() });
//		}
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public SeptStationMapTemplet getStationMapTemplet() {
		return stationMapTemplet;
	}

	public void setStationMapTemplet(SeptStationMapTemplet stationMapTemplet) {
		this.stationMapTemplet = stationMapTemplet;
	}

	public void init() {
		if(false){
			
			long start = SystemTime.currentTimeMillis();
			List<Task> collections = taskManager.getTaskCollectionsByName(stationMapTemplet.getBuildingTaskCollection());
			if (collections != null) {
				tasks = new String[collections.size()];
				for (int i = 0; i < collections.size(); i++) {
					tasks[i] = collections.get(i).getName();
				}
//			System.out.println("-----------------[家族建设任务完成 ]--------------------[耗时:"  + (SystemTime.currentTimeMillis() - start)+ "ms]");
			} else {
				System.out.println("-----------------[家族建设任务异常]--------------------任务集合[" + stationMapTemplet.getBuildingTaskCollection() + "] [不存在] [耗时:"  + (SystemTime.currentTimeMillis() - start)+ "ms]");
				
			}
			ServiceStartRecord.startLog(this);
		}
	}
}
