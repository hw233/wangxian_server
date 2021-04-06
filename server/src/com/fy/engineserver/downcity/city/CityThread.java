/**
 * 
 */
package com.fy.engineserver.downcity.city;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class CityThread implements Runnable{

	private List<CityAction> tasks = new ArrayList<CityAction>();
	private List<CityAction> rmtasks = new ArrayList<CityAction>();
	
	String name;
	public CityThread(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void start() {
		new Thread(this,name).start();
	}
	
	public boolean isFull(){
		return tasks.size() >= CityConstant.MAX_TASK_NUMS_OF_WUDI;
	}
	
	public void addTask(CityAction task){
		synchronized (tasks) {
			for(CityAction t : tasks){
				if(t== null){
					rmtasks.add(task);
					continue;
				}
				if(t.getId() == task.getId()){
					rmtasks.add(task);
					continue;
				}
				if(t.isDestroy()){
					t.destory();
					rmtasks.add(task);
					continue;
				}
			}
			tasks.add(task);
		}
		if(rmtasks.size() > 0){
			tasks.removeAll(rmtasks);
		}
		CityConstant.logger.warn("[新增副本] [线程:{}] [副本id:{}] [副本name:{}] [任务数:{}] [失效任务数:{}]",
				new Object[]{name,task.getId(),task.getName(), tasks.size(),rmtasks.size()});
		rmtasks.clear();
	}
	
	
	@Override
	public void run() {
		while(CityConstant.START_RUN_OF_WUDI){
			try {
				Thread.sleep(CityConstant.SLEEP_TIME_OF_WUDI);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				for(CityAction t : tasks){
					if(t== null){
						rmtasks.add(t);
						continue;
					}
					if(t.isDestroy()){
						t.destory();
						rmtasks.add(t);
						continue;
					}
					t.heartbeat();
				}
				if(rmtasks.size() > 0){
					tasks.removeAll(rmtasks);
					CityConstant.logger.warn("[删除失效副本] [线程:{}] [任务数:{}] [失效任务数:{}]",new Object[]{name,tasks.size(),rmtasks.size()});
					rmtasks.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
