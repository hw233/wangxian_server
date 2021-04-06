package com.fy.engineserver.downcity.downcity3;

import java.util.ArrayList;
import java.util.List;

public class CityThread implements Runnable{

	private List<CityGame> tasks = new ArrayList<CityGame>();
	private List<CityGame> retasks = new ArrayList<CityGame>();
	
	public String tName;
	
	public Object lock = new Object();
	
	public CityThread(String tName){
		this.tName = tName;
		BossCityManager.logger.warn("[boss副本] [线程相关] [创建任务处理] [{}] [threadPool:{}]", new Object[]{tName,BossCityManager.getInstance().threadPool.size()});
	}
	
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
			try {
				Thread.sleep(BossCityManager.GAME_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (lock) {
				try {
					for(CityGame g : tasks){
						if(g == null) {
							retasks.add(g);
							continue;
						}
						g.heartbeat();
						if(g != null && g.state == 1){
							retasks.add(g);
						}
					}
					if(retasks.size() > 0){
						for(CityGame g : retasks){
							if(g == null){
								continue;
							}
							g.destory();
							BossCityManager.logger.warn("[全民boss副本] [线程相关] [删除失效副本] [副本id:{}] [{}] [tasks:{}] [retasks:{}] [threadPool:{}]", new Object[]{g.getId(), tName,tasks.size(),retasks.size(),BossCityManager.getInstance().threadPool.size()});
						}
						tasks.removeAll(retasks);
						retasks.clear();
					}
				} catch (Exception e) {
					e.printStackTrace();
					BossCityManager.logger.warn("[全民boss副本] [异常]",e);
				}
			}
		}
	}

	public void addTask(CityGame game){
		synchronized (lock) {
			tasks.add(game);
		}
		BossCityManager.logger.warn("[全民boss副本] [线程相关] [添加副本] [副本id:{}] [{}] [tasks:{}] [retasks:{}] [threadPool:{}]", new Object[]{game.getId(), tName,tasks.size(),retasks.size(),BossCityManager.getInstance().threadPool.size()});
	}
	
	public List<CityGame> getTasks() {
		return tasks;
	}

	public List<CityGame> getRetasks() {
		return retasks;
	}

	public String gettName() {
		return tName;
	}
	
	public boolean isEffect(){
		return tasks.size() < BossCityManager.getInstance().MAX_HANDLE_TASKS;
	}
	
	public boolean isEmpty(){
		return tasks.size() == 0;
	}
	
	public void destory(){
		Thread.currentThread().interrupt();
		BossCityManager.logger.warn("[boss副本] [线程相关] [处理任务终止] [{}] [任务数:{}] [删除任务:{}] [线程池线程数:{}]", new Object[]{tName,tasks.size(),retasks.size(),BossCityManager.getInstance().threadPool.size()});
	}

	@Override
	public String toString() {
		return "["+tName+"] [任务:"+tasks.size()+"]";
	}
	
}
