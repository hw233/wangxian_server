package com.fy.engineserver.activity.cave;

import com.fy.engineserver.activity.BaseActivityInstance;

public class MaintanceActivity extends BaseActivityInstance {
	private double rate;

	public MaintanceActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		return "固定时间内庄园维护费用按比例减少-"+rate;
	}
}
