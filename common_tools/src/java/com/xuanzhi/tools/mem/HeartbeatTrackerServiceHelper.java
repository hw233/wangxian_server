package com.xuanzhi.tools.mem;

import com.xuanzhi.tools.mem.ObjectTrackerService.ConfigItem;

public class HeartbeatTrackerServiceHelper {

	public static boolean isEnabled(){
		return HeartbeatTrackerService.enable;
	}
	
	public static int getTrackDurationInSeconds(){
		return HeartbeatTrackerService.trackDurationInSeconds;
	}
	
	public static void config(boolean enable,int seconds){
		HeartbeatTrackerService.config(enable, seconds);
	}
	
	public static String[] getTrackerNames(){
		return HeartbeatTrackerService.map.keySet().toArray(new String[0]);
	}
	
	public static HeartBeatTracker getTracker(String name){
		return HeartbeatTrackerService.map.get(name);
	}
}
