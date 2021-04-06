package com.fy.engineserver.newtask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.newtask.service.DeliverTaskManager;

public class DeliverTaskRemoveThread extends Thread{
	
	public static boolean isStart = true;
	
	public static int heartBeatTime = 5000;
	
	private Set<DeliverTask> needRemoveList = new HashSet<DeliverTask>();
	private Set<TaskEntity> teRemoveList = new HashSet<TaskEntity>();
	/**
	 * 服务器关闭时调用
	 */
	public void notifyServerClosed() {
		if(needRemoveList != null && needRemoveList.size() > 0) {
			for(DeliverTask dt : needRemoveList) {
				DeliverTaskManager.getInstance().notifyDeleteFromCache(dt);
			}
		}
		needRemoveList.clear();
		if(teRemoveList != null && teRemoveList.size() > 0) {
			for(TaskEntity te : teRemoveList) {
				TaskEntityManager.getInstance().notifyDeleteFromCache(te);
			}
		}
		teRemoveList.clear();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
		if(ndtm != null && ndtm.isNewDeliverTaskAct) {
			NewDeliverTaskManager.logger.error("[旧的任务库删除线程启动成功][" + this.getName() + "]");
			while(isStart) {
				int count1 = 0;
				int count2 = 0;
				if(needRemoveList != null && needRemoveList.size() > 0) {
					List<DeliverTask> rmovedList = new ArrayList<DeliverTask>();
					for(DeliverTask dt : needRemoveList) {
						DeliverTaskManager.getInstance().notifyDeleteFromCache(dt);
						rmovedList.add(dt);
						count1++;
						if(count1 >= 300) {
							break;
						}
					}
					synchronized (this.needRemoveList) {
						for(DeliverTask dt : rmovedList) {
							needRemoveList.remove(dt);
						}
					}
					try {
						Thread.sleep(heartBeatTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						TransitRobberyManager.logger.error("删除旧的已完成列表线程出错1：", e);
					}
				}
				if(teRemoveList != null && teRemoveList.size() > 0) {
					List<TaskEntity> teRemovedList = new ArrayList<TaskEntity>();
					for(TaskEntity te : teRemoveList) {
						TaskEntityManager.getInstance().notifyDeleteFromCache(te);
						teRemovedList.add(te);
						count2++;
						if(count2 >= 100) {
							break;
						}
					}
					synchronized (this.teRemoveList) {
						for(TaskEntity te : teRemovedList) {
							teRemoveList.remove(te);
						}
					}
				} else{
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						TransitRobberyManager.logger.error("删除旧的已完成列表线程出错2：", e);
					}
				}
			}
		}
		TransitRobberyManager.logger.error("[旧的任务库删除线程关闭][" + this.getName() + "]");
	}

	public Set<DeliverTask> getNeedRemoveList() {
		return needRemoveList;
	}
	
	public Set<TaskEntity> getTeRemoveList() {
		return teRemoveList;
	}
	
	
	public void add2RemoveList(List<DeliverTask> needRemoveList) {
		if(needRemoveList == null || needRemoveList.size() <= 0) {
			return;
		}
		synchronized (this.needRemoveList) {
			for(DeliverTask dt : needRemoveList){
				this.needRemoveList.add(dt);
			}
		}
	}
	public void add2teRemoveList(List<TaskEntity> nmL) {
		if(teRemoveList == null || teRemoveList.size() <= 0) {
			return;
		}
		synchronized (this.needRemoveList) {
			for(TaskEntity dt : nmL){
				this.teRemoveList.add(dt);
			}
		}
	}

}
