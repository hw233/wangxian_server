package com.fy.engineserver.sprite.monster;

import java.util.Date;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.util.TimeSlice;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 按照具体时间点来，精确到时间点
 * 
 */
public class DateTimePreciseMinuteSlice extends TimeSlice {
	/**
	 * 起始时间点精确到分钟
	 */
	long startTime;
	/**
	 * 终止时间点精确到分钟
	 */
	long endTime;
	
	/**
	 * 开启时间限制，默认不开启，即没有时间限制，只有此值为true时，时间限制才生效
	 */
	boolean openTimeLimit = false;

	public DateTimePreciseMinuteSlice(long startTime, long endTime, boolean open) {
		this.endTime = endTime;
		this.startTime = startTime;
		this.openTimeLimit = open;
	}

	public boolean isValid(Date time) {
		if(!openTimeLimit){
			return true;
		}
		boolean ok = false;
		try{
			long times = Long.parseLong(DateUtil.formatDate(time, "yyyyMMddHHmm"));
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
		if(!openTimeLimit){
			return Translate.text_5741;
		}
		String startTimeStr = startTime+"";
		String yearStart = startTimeStr.substring(0,4);
		String monthStart = startTimeStr.substring(4,6);
		String dayStart = startTimeStr.substring(6,8);
		String hourStart = startTimeStr.substring(8,10);
		String minStart = startTimeStr.substring(10,12);
		String endTimeStr = endTime+"";
		String yearEnd = endTimeStr.substring(0,4);
		String monthEnd = endTimeStr.substring(4,6);
		String dayEnd = endTimeStr.substring(6,8);
		String hourEnd = endTimeStr.substring(8,10);
		String minEnd = endTimeStr.substring(10,12);
		return Translate.text_5742+yearStart+Translate.text_255+monthStart+Translate.text_256+dayStart+Translate.text_143+hourStart+Translate.text_5743+minStart+Translate.text_147+Translate.text_5744+yearEnd+Translate.text_255+monthEnd+Translate.text_256+dayEnd+Translate.text_143+hourEnd+Translate.text_5743+minEnd+Translate.text_147;
	}
}
