package com.fy.engineserver.sprite.petdao;

import java.util.*;

/**
 * 迷城任务
 * 
 *
 */
public class PetDao_Thread implements Runnable{
	
	public List<PetDao> allTasks =	new ArrayList<PetDao>();
	
	public List<PetDao> allStopTasks =	new ArrayList<PetDao>();	//无效的任务
	
	private static long sleeptime = 100;
	
	private boolean isstart = true;
	
	public void addTask(PetDao pd){
		allTasks.add(pd);
	}

	public void start(String name){
		new Thread(this,name).start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isstart){
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(allTasks.size()>0){
				for(int i=0;i<allTasks.size();i++){
					//某个线程异常不会影响整个线程队列
					try{
						PetDao pd = allTasks.get(i);
						if(pd.getMc().STAT!=0){
							allStopTasks.add(pd);
//							pd.heartbeat();
							if(pd.getMc().getContinuetime()<=0){
								pd=null;
							}
						}else{
							pd.heartbeat();
						}
					}catch(Throwable e){
						PetDaoManager.log.warn("[宠物迷城线程] ["+Thread.currentThread().getName()+"] [在队列位置："+i+"] [队列长度："+allTasks.size()+"]");
					}
				}
				try{
					if(allStopTasks.size()>0){
						PetDaoManager.log.warn("[宠物迷城线程] ["+Thread.currentThread().getName()+"] [删除无效的任务] [总任务："+allTasks.size()+"] [删除的任务数："+allStopTasks.size()+"]");
						allTasks.removeAll(allStopTasks);
						allStopTasks.clear();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
