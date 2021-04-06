package com.fy.engineserver.activity.xianling;

import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.util.config.ServerFit4Activity;

public class XianLing extends BaseActivityInstance {
	private int activityId;

	public XianLing(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		return "仙灵-" + activityId;
	}

}
