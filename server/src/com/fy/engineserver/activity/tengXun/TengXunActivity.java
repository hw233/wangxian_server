package com.fy.engineserver.activity.tengXun;

public abstract class TengXunActivity {

	private String name;
	
	private long startTime_Long;
	private long endTime_Long;
	
	private String activityMsg;
	
	public boolean isStart = false;			//是否执行过start
	
	public TengXunActivity(long startTime_Long, long endTime_Long, String activityMsg) {
		this.startTime_Long = startTime_Long;
		this.endTime_Long = endTime_Long;
		this.activityMsg = activityMsg;
	}	
	
	public boolean isStart() {
		long now = System.currentTimeMillis();
		if (now >= startTime_Long && !isStart) {
			return true;
		}
		return false;
	}
	
	public boolean isEnd() {
		long now = System.currentTimeMillis();
		if (now >= endTime_Long) {
			return true;
		}
		return false;
	}
	
	protected abstract void onStart();
	protected abstract void onEnd();
	
	public void onActivityStart() {
		if (!isStart) {
			onStart();
			isStart = true;
		}
	}
	
	public void onActivityEnd() {
		onEnd();
		startTime_Long += TengXunActivityManager.instance.ACTIVITY_SPACETIME_MOULD;
		endTime_Long += TengXunActivityManager.instance.ACTIVITY_SPACETIME_MOULD;
		isStart = false;
	}
	
	public void setStartTime_Long(long startTime_Long) {
		this.startTime_Long = startTime_Long;
	}
	public long getStartTime_Long() {
		return startTime_Long;
	}
	public void setEndTime_Long(long endTime_Long) {
		this.endTime_Long = endTime_Long;
	}
	public long getEndTime_Long() {
		return endTime_Long;
	}
	public void setActivityMsg(String activityMsg) {
		this.activityMsg = activityMsg;
	}
	public String getActivityMsg() {
		return activityMsg;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
