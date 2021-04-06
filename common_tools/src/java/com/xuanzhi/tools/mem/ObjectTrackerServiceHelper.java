package com.xuanzhi.tools.mem;

import com.xuanzhi.tools.mem.ObjectTrackerService.ConfigItem;

public class ObjectTrackerServiceHelper {

	public static String getPath(){
		return ObjectTrackerService.path;
	}
	public static String getLogbackName(){
		return ObjectTrackerService.logbackName;
	}
	public static String getLog4jName(){
		return ObjectTrackerService.log4jName;
	}
	public static int getTimeoutDays(){
		return ObjectTrackerService.timeoutDays;
	}
	
	
	public static ConfigItem[] getConfigItems(){
		return ObjectTrackerService.config.toArray(new ConfigItem[0]);
	}

	public static Class[] getTrackedClass(){
		return ObjectTrackerService.map.keySet().toArray(new Class[0]);
	}
	
	public static ObjectCreationTracker getObjectCreationTracker(Class cl){
		return ObjectTrackerService.map.get(cl);
	}
	
}
