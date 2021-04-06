package com.xuanzhi.tools.mem;


public class HeartbeatTrackerServiceHelper2 {

	public static boolean isEnabled(){
		return HeartbeatTrackerService2.enable;
	}
	
	public static int getTrackDurationInSeconds(){
		return HeartbeatTrackerService2.trackDurationInSeconds;
	}
	
	public static void config(boolean enable,int seconds){
		HeartbeatTrackerService2.config(enable, seconds);
	}
	
	public static String[] getTrackerNames(){
		return HeartbeatTrackerService2.map.keySet().toArray(new String[0]);
	}
	
	public static HeartBeatTracker2 getTracker(String name){
		return HeartbeatTrackerService2.map.get(name);
	}
}
