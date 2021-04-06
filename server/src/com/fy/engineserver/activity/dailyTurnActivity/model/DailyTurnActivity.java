package com.fy.engineserver.activity.dailyTurnActivity.model;

import com.fy.engineserver.activity.BaseActivityInstance;

public class DailyTurnActivity extends BaseActivityInstance{

	public DailyTurnActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("每日登陆转盘活动");
		return sb.toString();
	}

}
