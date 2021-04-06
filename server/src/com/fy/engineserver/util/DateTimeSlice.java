package com.fy.engineserver.util;

import java.util.Date;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 按照具体时间点来，精确到时间点
 * 
 */
public class DateTimeSlice extends TimeSlice {
	/**
	 * 起始时间点精确到小时
	 */
	long startTime;
	/**
	 * 终止时间点精确到小时
	 */
	long endTime;

	public DateTimeSlice(long endTime, long startTime) {
		this.endTime = endTime;
		this.startTime = startTime;
	}

	public boolean isValid(Date time) {
		boolean ok = false;
		try{
			int times = Integer.parseInt(DateUtil.formatDate(time, "yyyyMMddHH"));
			if (startTime <= times && times < endTime) {
				ok = true;
			}else{
				ok = false;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			ok = false;
		}
		if(Game.logger.isInfoEnabled()) {
//			Game.logger.info("[DateTimeSlice] [时间点是否允许] ["+(DateUtil.formatDate(time, "yyMMdd HH:mm"))+"] ["+ok+"] [条件:"+this.toString()+"]");
			if(Game.logger.isInfoEnabled())
				Game.logger.info("[DateTimeSlice] [时间点是否允许] [{}] [{}] [条件:{}]", new Object[]{(DateUtil.formatDate(time, "yyMMdd HH:mm")),ok,this.toString()});
		}
		return ok;
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

	public String toString(){
		String startTimeStr = startTime+"";
		String yearStart = startTimeStr.substring(0,4);
		String monthStart = startTimeStr.substring(4,6);
		String dayStart = startTimeStr.substring(6,8);
		String hourStart = startTimeStr.substring(8,10);
		String endTimeStr = endTime+"";
		String yearEnd = endTimeStr.substring(0,4);
		String monthEnd = endTimeStr.substring(4,6);
		String dayEnd = endTimeStr.substring(6,8);
		String hourEnd = endTimeStr.substring(8,10);
		return Translate.text_5742+yearStart+Translate.text_255+monthStart+Translate.text_256+dayStart+Translate.text_143+hourStart+Translate.text_5743+Translate.text_5744+yearEnd+Translate.text_255+monthEnd+Translate.text_256+dayEnd+Translate.text_143+hourEnd+Translate.text_5743;
	}
}
