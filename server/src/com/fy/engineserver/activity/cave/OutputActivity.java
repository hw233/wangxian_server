package com.fy.engineserver.activity.cave;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.activity.expactivity.DayilyTimeDistance;
import com.fy.engineserver.util.CompoundReturn;

public class OutputActivity  extends BaseActivityInstance {

	private double rate;
	// 所有配置的时间段
	private List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();
	
	public OutputActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	
	public void setOtherVar(double rate, List<DayilyTimeDistance> times) {
		this.setRate(rate);
		this.times = times;
	}
	
	public CompoundReturn getOutputActivity(Calendar calendar) {
		if (this.isThisServerFit(calendar.getTimeInMillis()) != null ) {
			ActivitySubSystem.logger.warn("不开放--------1");
			return CompoundReturn.createCompoundReturn().setBooleanValue(false);
		}
		for (DayilyTimeDistance dayilyTimeDistance : times) {
			if (dayilyTimeDistance.inthisTimeDistance(calendar)) {
				ActivitySubSystem.logger.warn("开放--------");
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setDoubleValue(this.getRate());
			}
		}
		ActivitySubSystem.logger.warn("不开放--------2");
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public List<DayilyTimeDistance> getTimes() {
		return times;
	}

	public void setTimes(List<DayilyTimeDistance> times) {
		this.times = times;
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		return "固定时间庄园种植收益加倍-"+rate;
	}
	
}
