package com.xuanzhi.tools.mem;

import java.util.HashMap;

public class HeartbeatTrackerService2 {

	
	static int trackDurationInSeconds = 10 * 60;
	
	static HashMap<String,HeartBeatTracker2> map = new HashMap<String,HeartBeatTracker2>();
	
	static boolean enable = false;
	
	static void config(boolean _enable,int _trackDurationInSeconds){
		enable = _enable;
		trackDurationInSeconds = _trackDurationInSeconds;
		HeartBeatTracker2 r[] = map.values().toArray(new HeartBeatTracker2[0]);
		for(int i = 0 ; i < r.length ; i++){
			r[i].trackDurationInSeconds = trackDurationInSeconds;
		}
	}
	
	
	/**
	 * 汇报某一个操作完成
	 * @param name 跟踪的名字，此名字用于同一的跟踪显示，相同的跟踪必须用用一个名字
	 * @param clazz 操作的对象类
	 * @param action 操作读还是写
	 * @param num 操作的数量
	 * @param costTime 耗费的时间
	 */
	public static void operationEnd(String name,String description,long waitTimeInNano,long costTimeInNano){
		if(enable == false) return;
		HeartBeatTracker2 tracker = null;
		synchronized(map){
			tracker = map.get(name);
			if(tracker == null){
				tracker = new HeartBeatTracker2();
				tracker.name = name;
				tracker.trackDurationInSeconds = trackDurationInSeconds;
				map.put(name, tracker);
			}
		}
		tracker.operateEnd(description,waitTimeInNano,costTimeInNano);
	}
	
	
}
