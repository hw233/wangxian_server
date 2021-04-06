package com.fy.engineserver.activity.loginActivity.timingTask;
/*
 * 代表一个定时任务
 * eg:每天的12:30点和14:20点做什么
 */
public abstract class TaskTarget {

	public String [] times;
	
	public abstract boolean isEffection();
	
	public abstract void doTarget();
	
}
