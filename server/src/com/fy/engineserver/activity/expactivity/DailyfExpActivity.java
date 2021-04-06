package com.fy.engineserver.activity.expactivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fy.engineserver.util.CompoundReturn;

/**
 * 经验活动-每日固定时间段开启
 * 
 */
public class DailyfExpActivity extends ExpActivity {
	
	private String activityType;
	
	public String toString() {
		return super.toString();
	}

	public DailyfExpActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	// 所有配置的时间段
	private List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();
	
	public void setOtherVar(String activityType, double addRate, String name, List<DayilyTimeDistance> times) {
		this.activityType = activityType;
		this.setExpRate(addRate);
		this.setName(name);
		this.times = times;
	}

	@Override
	public CompoundReturn getExpActivityMultiple(Calendar calendar) {
		if (this.isThisServerFit() != null ) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false);
		}
		for (DayilyTimeDistance dayilyTimeDistance : times) {
			if (dayilyTimeDistance.inthisTimeDistance(calendar)) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setDoubleValue(this.getExpRate()).setStringValues(this.getLimitmaps().toArray(new String[]{}));
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[活动类型 :" +activityType+ "]");
		sb.append("[活动名 :" + this.getName() + "]");
		sb.append("[活动生效时间 :");
		for(int i=0; i<times.size(); i++) {
			DayilyTimeDistance dayilyTimeDistance = times.get(i);
			sb.append(dayilyTimeDistance.getInfoString() + "||");
		}
		sb.append("] [经验倍数倍数 : " + this.getExpRate() + "]");
		return sb.toString();
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
}
