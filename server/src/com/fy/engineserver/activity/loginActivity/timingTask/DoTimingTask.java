package com.fy.engineserver.activity.loginActivity.timingTask;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.util.ServiceStartRecord;

/**
 * 执行定时任务
 * 
 *
 */
public class DoTimingTask implements Runnable{

	private static Set<TaskTarget> targets = new HashSet<TaskTarget>();
	
	static{
		targets.add(new ContinueActivityTarget(new String[]{"0:0"}));
	}
	
	private static DoTimingTask self;
	
	public static DoTimingTask getInstance(){
		return self;
	}
	
	private boolean isstart;
	
	public void init(){
		
		self = this;
		isstart = true;
		new Thread(self,"执行定时任务DoTimingTask").start();
		ServiceStartRecord.startLog(this);
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(isstart){
			try {
				Thread.sleep(30*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			for(TaskTarget tt : targets){
				if(tt.isEffection()){
					tt.doTarget();
				}
			}
		}
	}

	public boolean isIsstart() {
		return isstart;
	}

	public void setIsstart(boolean isstart) {
		this.isstart = isstart;
	}

	public Set<TaskTarget> getTargets() {
		return targets;
	}

	public static DoTimingTask getSelf() {
		return self;
	}

	public static void setSelf(DoTimingTask self) {
		DoTimingTask.self = self;
	}
	
}
