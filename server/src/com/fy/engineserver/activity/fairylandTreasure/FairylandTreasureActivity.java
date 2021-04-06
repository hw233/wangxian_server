package com.fy.engineserver.activity.fairylandTreasure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.activity.expactivity.DayilyTimeDistance;
import com.fy.engineserver.util.CompoundReturn;

public class FairylandTreasureActivity extends BaseActivityInstance {
	// 所有配置的时间段
	private List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();

	private int lastingTime; // 持续存在时间,单位分钟
	private int refreshSpace; // 刷新时间间隔,单位分钟

	public FairylandTreasureActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}

	public void setOtherVar(List<DayilyTimeDistance> times, int lastingTime, int refreshSpace) {
		this.times = times;
		this.lastingTime = lastingTime;
		this.refreshSpace = refreshSpace;
	}
	
	public CompoundReturn getActivityOpen(Calendar calendar) {
		if (this.isThisServerFit(calendar.getTimeInMillis()) != null ) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false);
		}
		for (DayilyTimeDistance dayilyTimeDistance : times) {
			if (dayilyTimeDistance.inthisTimeDistance(calendar)) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true);
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}
	
	public DayilyTimeDistance getNowDayilyTimeDistance(){
		Calendar ca=Calendar.getInstance();
		for(DayilyTimeDistance dtd:times){
			if(dtd.inthisTimeDistance(ca)){
				return dtd;
			}
		}
		return null;
	}

	public List<DayilyTimeDistance> getTimes() {
		return times;
	}

	public void setTimes(List<DayilyTimeDistance> times) {
		this.times = times;
	}

	public int getLastingTime() {
		return lastingTime;
	}

	public void setLastingTime(int lastingTime) {
		this.lastingTime = lastingTime;
	}

	public int getRefreshSpace() {
		return refreshSpace;
	}

	public void setRefreshSpace(int refreshSpace) {
		this.refreshSpace = refreshSpace;
	}

	@Override
	public String getInfoShow() {
		return "仙界宝箱活动";
	}
}
