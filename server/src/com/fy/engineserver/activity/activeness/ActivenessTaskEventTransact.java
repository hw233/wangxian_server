package com.fy.engineserver.activity.activeness;

import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.AbsTaskEventTransact;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.Taskoperation;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;

public class ActivenessTaskEventTransact extends AbsTaskEventTransact {

	private String[] fubenTaskNames;//副本任务
	private String[] pdsfTaskNames; //平定四方任务
	
	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case done:
			return fubenTaskNames;
		case deliver:
			return pdsfTaskNames;
		default:
			break;
		}
		return null;

	}

	public String[] getFubenTaskNames() {
		return fubenTaskNames;
	}


	public void setFubenTaskNames(String[] fubenTaskNames) {
		this.fubenTaskNames = fubenTaskNames;
	}


	public String[] getPdsfTaskNames() {
		return pdsfTaskNames;
	}

	public void setPdsfTaskNames(String[] pdsfTaskNames) {
		this.pdsfTaskNames = pdsfTaskNames;
	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		// TODO Auto-generated method stub
		ActivitySubSystem.logger.warn("活跃度 进入处理接取平定四方的方法");
		if(task.getName_stat().matches("平定四方.*")){
			ActivenessManager.getInstance().record(player, ActivenessType.平定四方);
		}

	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		// TODO Auto-generated method stub
		/** 活跃度统计，这里要求任务名字符合某个规则*/
		ActivitySubSystem.logger.warn("活跃度 进入处理副本完成的方法");
		if(task.getName_stat().matches(".*级.*元神.*")){
			ActivenessManager.getInstance().record(player, ActivenessType.通关元神副本);
		}else if(task.getName_stat().matches(".*级.*")){
			ActivenessManager.getInstance().record(player, ActivenessType.通关本尊副本);
		}
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
		// TODO Auto-generated method stub

	}
	public void init() throws Exception {
		
		ActivitySubSystem.logger.warn("活跃度 进入副本统计次数init方法");
		String fubenName = Translate.副本;
		String pingdingsifangName = Translate.除妖;
		TaskManager taskManager = TaskManager.getInstance();
		List<Task> fubenTasks = taskManager.getTaskBigCollections().get(fubenName);
		List<Task> pdsfTasks = taskManager.getTaskCollectionsByName(pingdingsifangName);
		if(pdsfTasks!=null){
			pdsfTaskNames = new String[pdsfTasks.size()];
			for(int i=0;i<pdsfTaskNames.length;i++){
				pdsfTaskNames[i] = pdsfTasks.get(i).getName();
			}
		}else{
			ActivitySubSystem.logger.warn("没有平定四方任务]");
		}
		if(fubenTasks!=null){
			fubenTaskNames = new String[fubenTasks.size()];
			for(int i=0;i<fubenTaskNames.length;i++){
				fubenTaskNames[i] = fubenTasks.get(i).getName();
			}
		}else{
			ActivitySubSystem.logger.warn("没有副本任务]");
		}
		ServiceStartRecord.startLog(this);
	}

}
