package com.fy.engineserver.activity.wolf.runtask;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.wolf.ActivityState;
import com.fy.engineserver.activity.wolf.WolfGame;
import com.fy.engineserver.activity.wolf.WolfManager;

public class WolfGameThread implements Runnable{

	private List<WolfGame> tasks = new ArrayList<WolfGame>();
	private List<WolfGame> retasks = new ArrayList<WolfGame>();
	
	public String tName;
	
	@Override
	public void run() {
		while(WolfManager.START_RUN){
			try {
				Thread.sleep(WolfManager.GAME_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(WolfGame g : tasks){
				if(g != null && g.isEffect()){
					g.heartbeat();
				}
				if(g != null && g.state == ActivityState.战斗结束){
					retasks.add(g);
				}
			}
			if(retasks.size() > 0){
				for(WolfGame g : retasks){
					g.destory();
				}
				tasks.removeAll(retasks);
				retasks.clear();
				if(WolfManager.logger.isWarnEnabled()){
					WolfManager.logger.warn("[小羊快跑] [线程情况] [{}] [删除失效副本] [任务数:{}] [删除任务:{}]", new Object[]{tName,tasks.size(),retasks.size()});
				}
			}
		}
	}

	public void addTask(WolfGame game){
		tasks.add(game);
	}
	
	public int getTaskNums(){
		return tasks.size();
	}

	@Override
	public String toString() {
		return "["+tName+"] [任务:"+tasks.size()+"]";
	}
	
}
