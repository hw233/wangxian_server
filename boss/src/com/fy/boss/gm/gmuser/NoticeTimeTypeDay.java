package com.fy.boss.gm.gmuser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.fy.boss.gm.gmuser.server.GmSystemNoticeManager;
import com.fy.boss.gm.gmuser.NoticeTimeType;
public class NoticeTimeTypeDay extends NoticeTimeType {

	private String[] days;
	
	private String limitBeginTime;
	
	private String limitEndTime;
	
	@Override
	public boolean needSendNotice(long now) {
		try{
			if(days!=null&&days.length>0){
				for(String day:days){
					if(isSameDay(day)){
						if(limitBeginTime!=null&&limitEndTime!=null){
							if(now>=GmSystemNoticeManager.获得毫秒数(day+" "+limitBeginTime)&&now<=GmSystemNoticeManager.获得毫秒数(day+" "+limitEndTime)){
								return true;
							}
						}
					}
				}
			}
		}catch(Exception e){
			GmSystemNoticeManager.logger.warn("[in needSendNotice..] [异常]",e);
		}
		
		return false;
	}

	public boolean isSameDay(String daytime){
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		long date;
		Calendar cl = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			date = dateformat.parse(daytime).getTime();
			cl.setTimeInMillis(date);
			c2.setTimeInMillis(System.currentTimeMillis());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int year = cl.get(Calendar.YEAR);
		int month = cl.get(Calendar.MONTH);
		int day = cl.get(Calendar.DAY_OF_MONTH);
		int curr_year = c2.get(Calendar.YEAR);
		int curr_month = c2.get(Calendar.MONTH);
		int curr_day = c2.get(Calendar.DAY_OF_MONTH);
		return year==curr_year&&day==curr_day&&curr_month==month;
	}
	
	
	public String[] getDays() {
		return days;
	}

	public void setDays(String[] days) {
		this.days = days;
	}

	public String getLimitBeginTime() {
		return limitBeginTime;
	}

	public void setLimitBeginTime(String limitBeginTime) {
		this.limitBeginTime = limitBeginTime;
	}

	public String getLimitEndTime() {
		return limitEndTime;
	}

	public void setLimitEndTime(String limitEndTime) {
		this.limitEndTime = limitEndTime;
	}

	@Override
	public boolean isvalid(long now) {
		if(days!=null && days.length>0){
			try{
				String maxDay = days[days.length-1]+" 23:59:00";
				GmSystemNoticeManager.logger.warn("[是否有效] [maxDay:"+maxDay+"] [是否有效："+(now > GmSystemNoticeManager.获得毫秒数(maxDay))+"]");
				if(now > GmSystemNoticeManager.获得毫秒数(maxDay)){
					return false;
				}
			}catch(Exception e){
				e.printStackTrace();
				GmSystemNoticeManager.logger.warn("[是否有效] [异常]",e);
			}
		}
		return true;
	}
}
