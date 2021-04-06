package com.fy.engineserver.activity.base;

import com.fy.engineserver.activity.BaseActivityInstance;

/**
 * 倍数活动
 * 
 */
public class AddRateActivity extends BaseActivityInstance{

	private double addRate;
	
	private String activityType;
	
	public AddRateActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	
	public void setOtherVar(double addRate, String activityType) {
		this.addRate = addRate;
		this.activityType = activityType;
	}

	public boolean inActivity(long now) {
		return (this.isThisServerFit() == null);
	}

	public double getAddRate() {
		return addRate;
	}

	public void setAddRate(double addRate) {
		this.addRate = addRate;
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[活动类型:" + activityType + "]");
		sb.append("[倍数(额外增加):" + this.addRate + "]");
		return sb.toString();
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
}
