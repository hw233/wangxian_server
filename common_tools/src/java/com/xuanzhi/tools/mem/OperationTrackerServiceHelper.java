package com.xuanzhi.tools.mem;

import com.xuanzhi.tools.mem.ObjectTrackerService.ConfigItem;

public class OperationTrackerServiceHelper {

	public static boolean isEnabled(){
		return OperatoinTrackerService.enable;
	}
	
	public static int getTrackDurationInSeconds(){
		return OperatoinTrackerService.trackDurationInSeconds;
	}
	
	public static void config(boolean enable,int seconds){
		OperatoinTrackerService.config(enable, seconds);
	}
	
	public static String[] getTrackerNames(){
		return OperatoinTrackerService.map.keySet().toArray(new String[0]);
	}
	
	public static ReadAndWriteOperationTracker getTracker(String name){
		return OperatoinTrackerService.map.get(name);
	}
}
