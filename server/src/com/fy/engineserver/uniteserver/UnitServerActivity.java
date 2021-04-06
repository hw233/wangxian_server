package com.fy.engineserver.uniteserver;

import com.fy.engineserver.activity.BaseActivityInstance;

public class UnitServerActivity extends BaseActivityInstance {
	private String activityName;

	public UnitServerActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}

	public void setOtherVar(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		return "合服-" + activityName;
	}
}
