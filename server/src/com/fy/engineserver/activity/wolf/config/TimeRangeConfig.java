package com.fy.engineserver.activity.wolf.config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fy.engineserver.activity.expactivity.DayilyTimeDistance;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

public class TimeRangeConfig implements ActivityConfig{

	/** 
	 * 活动开始,结束时间 
	 */
	private long startTime;
	private long endTime;
	
	private Platform[] platForms ;
	
	private List<String> openServerNames = new ArrayList<String>();
	
	private List<String> notOpenServerNames = new ArrayList<String>();
	
	/**
	 * 不开启的原因
	 */
	private String closeReason = "";
	
	private String timeShow = "";
	/**
	 * 所有配置的时间段
	 */
	private List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();
	
	@Override
	public boolean isStart() {
		if(platFit()){
			return hourFit();
		}
		return  false;
	}
	
	public boolean platFit(){
		if(System.currentTimeMillis() > endTime || System.currentTimeMillis() < startTime){
			closeReason = "开启时间不满足";
			return false;
		}
		if(!PlatformManager.getInstance().isPlatformOf(platForms)){
			closeReason = "平台不满足";
			return false;
		}
		String serverName = GameConstants.getInstance().getServerName();
		if(serverName == null || serverName.isEmpty()){
			closeReason = "服务器不存在";
			return false;
		}
		if(openServerNames.size() > 0 && !openServerNames.contains(serverName)){
			closeReason = "不在开放服务器配置中";
			return false;
		}
		if(notOpenServerNames.size() > 0 && notOpenServerNames.contains(serverName)){
			closeReason = "在不开放服务器配置中";
			return false;
		}
		return true;
	}
	
	public boolean hourFit(){
		Calendar cl = Calendar.getInstance();
		for (DayilyTimeDistance dayilyTimeDistance : times) {
			if (dayilyTimeDistance.inthisTimeDistance(cl)) {
				return true;
			}
		}
		closeReason = "开始结束时间点不对";
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[开始时间:").append(TimeTool.formatter.varChar19.format(startTime)).append("] ");
		sb.append("[结束时间:").append(TimeTool.formatter.varChar19.format(endTime)).append("] ");
		sb.append("[开放服务器:").append(openServerNames).append("] ");
		sb.append("[限制服务器:").append(notOpenServerNames).append("] ");
		sb.append("[生效时间段:");
		for(int i=0; i<times.size(); i++) {
			DayilyTimeDistance dayilyTimeDistance = times.get(i);
			sb.append(dayilyTimeDistance.getInfoString() + "||");
		}
		sb.append("]");
		sb.append("[原因:").append(closeReason).append("]");
		return sb.toString();
	}

	@Override
	public boolean notice10Minute() {
		if(platFit()){
			for (DayilyTimeDistance dayilyTimeDistance : times) {
				if (dayilyTimeDistance.noticeActivityStart(10)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean notice1Minute() {
		if(platFit()){
			for (DayilyTimeDistance dayilyTimeDistance : times) {
				if (dayilyTimeDistance.noticeActivityStart(1)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean notice5Minute() {
		if(platFit()){
			for (DayilyTimeDistance dayilyTimeDistance : times) {
				if (dayilyTimeDistance.noticeActivityStart(5)) {
					return true;
				}
			}
		}
		return false;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public Platform[] getPlatForms() {
		return platForms;
	}

	public void setPlatForms(Platform[] platForms) {
		this.platForms = platForms;
	}

	public List<String> getOpenServerNames() {
		return openServerNames;
	}

	public void setOpenServerNames(List<String> openServerNames) {
		this.openServerNames = openServerNames;
	}

	public List<String> getNotOpenServerNames() {
		return notOpenServerNames;
	}

	public void setNotOpenServerNames(List<String> notOpenServerNames) {
		this.notOpenServerNames = notOpenServerNames;
	}

	public String getCloseReason() {
		return closeReason;
	}

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}

	public List<DayilyTimeDistance> getTimes() {
		return times;
	}

	public void setTimes(List<DayilyTimeDistance> times) {
		this.times = times;
	}

	public String getTimeShow() {
		return timeShow;
	}

	public void setTimeShow(String timeShow) {
		this.timeShow = timeShow;
	}

	@Override
	public String getOpenTimeMess() {
		return timeShow;
	}

	@Override
	public String getCurrOpenFlag() {
		return null;
	}

}
