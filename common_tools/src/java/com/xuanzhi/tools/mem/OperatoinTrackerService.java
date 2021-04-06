package com.xuanzhi.tools.mem;

import java.util.HashMap;

public class OperatoinTrackerService {
	public static final int ACTION_READ = 0;
	
	public static final int ACTION_WRITE = 1;
	
	static int trackDurationInSeconds = 10 * 60;
	
	static HashMap<String,ReadAndWriteOperationTracker> map = new HashMap<String,ReadAndWriteOperationTracker>();
	
	static boolean enable = false;
	
	public static boolean isEnabled(){
		return enable;
	}
	
	static void config(boolean _enable,int _trackDurationInSeconds){
		enable = _enable;
		trackDurationInSeconds = _trackDurationInSeconds;
		ReadAndWriteOperationTracker r[] = map.values().toArray(new ReadAndWriteOperationTracker[0]);
		for(int i = 0 ; i < r.length ; i++){
			r[i].trackDurationInSeconds = trackDurationInSeconds;
		}
	}
	
	public static void operationEnd(String name,Class clazz,int action,int num,int costTime){
		operationEnd(name,clazz,action,num,0L,costTime);
	}
	
	/**
	 * 汇报某一个操作完成
	 * @param name 跟踪的名字，此名字用于同一的跟踪显示，相同的跟踪必须用用一个名字
	 * @param clazz 操作的对象类
	 * @param action 操作读还是写
	 * @param num 操作的数量
	 * @param costTime 耗费的时间
	 */
	public static void operationEnd(String name,Class clazz,int action,int num,long bytes,int costTime){
		if(enable == false) return;
		ReadAndWriteOperationTracker tracker = null;
		synchronized(map){
			tracker = map.get(name);
			if(tracker == null){
				tracker = new ReadAndWriteOperationTracker();
				tracker.name = name;
				tracker.trackDurationInSeconds = trackDurationInSeconds;
				map.put(name, tracker);
			}
		}
		tracker.operateEnd(clazz.getName(), action, num, bytes,costTime);
	}
	
	/**
	 * 汇报某一个操作完成
	 * @param name 跟踪的名字，此名字用于同一的跟踪显示，相同的跟踪必须用用一个名字
	 * @param clazz 操作的对象类
	 * @param action 操作读还是写
	 * @param num 操作的数量
	 * @param costTime 耗费的时间
	 */
	public static void operationEnd(String name,String desp,int action,int num,long bytes,int costTime){
		if(enable == false) return;
		ReadAndWriteOperationTracker tracker = null;
		synchronized(map){
			tracker = map.get(name);
			if(tracker == null){
				tracker = new ReadAndWriteOperationTracker();
				tracker.name = name;
				tracker.trackDurationInSeconds = trackDurationInSeconds;
				map.put(name, tracker);
			}
		}
		tracker.operateEnd(desp, action, num, bytes,costTime);
	}
}
