package com.fy.engineserver.operating.activitybuff;

import java.util.Calendar;
import java.util.Date;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 一个活动BUFF的条目
 * 
 * 
 *
 */
public class ActivityBuffItem {

	public static final int TIME_TYPE_EVERYDAY = 0;
	
	public static final int TIME_TYPE_EVERYWEEKDAY = 1;
	
	public static final int TIME_TYPE_FIXDAY = 2;
	
	/**
	 * 从16日开始到18日结束
	 */
	public static final int TIME_TYPE_FIXDAY_RANGE = 3;
	
	public static final String TIME_TYPE_NAMES[] = new String[]{Translate.text_5544,Translate.text_5545,Translate.text_5546,Translate.text_5547};
	
	
	int id;
	
	
	/**
	 *  时间类型，分为每天，每周几，和固定日期
	 */
	protected int timeType = 0;
	
	/**
	 * 星期几，只有在 timeType == TIME_TYPE_EVERYWEEKDAY 时有意义
	 * 0  周日
	 * 1  周一
	 * 2  周二 
	 * 3  周三
	 * 4  周四
	 * 5  周五
	 * 6  周六
	 * 
	 */
	protected int weekDay = 0;
	
	/**
	 * 固定日期,格式如下：yyyy-MM-dd
	 * 比如 2010-04-29
	 */
	protected String fixDay = "2010-04-20";
	
	/**
	 * 固定时间段,格式如下：yyyyMMdd~yyyyMMdd
	 * 比如 20100429~20100429
	 */
	protected String fixDayRange = "20100420~20100429";
	
	/**
	 * 开始时间，格式如下：HH:mm
	 */
	protected String startTimeInDay = "00:00";
	
	/**
	 * 结束时间,格式如下：HH:mm
	 */
	protected String endTimeInDay =  "23:59";
	
	/**
	 * 玩家等级限制，必须大于等于此级别才行
	 */
	protected int playerLevelLimit = 0;
	
	/**
	 * 玩家阵营限制，0标识没有限制，其他值对应紫薇宫和日月盟
	 */
	protected int playerPoliticalCampLimit = 0;
	
	
	protected boolean enableMapLimit = false;
	
	/**
	 * 地图限制
	 */
	protected String mapLimit = "";
	
	/**
	 * buff的名字
	 */
	protected String buffName;
	
	/**
	 * 从1开始，
	 * 
	 * 但是在服务器编程时，需要注意，buff内部配置数据，第一级别对应的数组下标为0
	 */
	protected int buffLevel = 1;

	public boolean isAvaiableForPlayer(Player p){
		if(p.getLevel() < playerLevelLimit) return false;
		if(playerPoliticalCampLimit != 0){
			if(p.getCountry() != playerPoliticalCampLimit){
				return false;
			}
		}
		if(enableMapLimit){
			if(p.getGame() == null || !p.getGame().equals(mapLimit)){
				return false;
			}
				
		}
		return true;
	}
	
	public boolean isActive(long now){
		Calendar cal = Calendar.getInstance();
		String dayStr = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		Date startTime = DateUtil.parseDate(dayStr+" " + this.startTimeInDay,"yyyy-MM-dd HH:mm");
		Date endTime = DateUtil.parseDate(dayStr+" " + this.endTimeInDay,"yyyy-MM-dd HH:mm");
		if(this.getTimeType() == ActivityBuffItem.TIME_TYPE_FIXDAY_RANGE){
			if(this.getFixDayRange() != null){
				String[] dayStrs = this.getFixDayRange().split("~");
				if(dayStrs.length != 2){
					dayStrs = this.getFixDayRange().split("~");
				}
				if(dayStrs.length == 2){
					try{
						startTime = DateUtil.parseDate(dayStrs[0]+" " + this.startTimeInDay,"yyyyMMdd HH:mm");
						endTime = DateUtil.parseDate(dayStrs[1]+" " + this.endTimeInDay,"yyyyMMdd HH:mm");
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		}
		if(startTime != null && endTime != null){
			if(startTime.getTime() <= now && now <= endTime.getTime()){
				
				if(timeType == TIME_TYPE_EVERYDAY){
					return true;
				}else if(timeType == TIME_TYPE_EVERYWEEKDAY){
					cal.setTimeInMillis(now);
					int d1 = cal.get(Calendar.DAY_OF_WEEK) -1;
					if(d1 == this.weekDay){
						return true;
					}
				}else if(timeType == TIME_TYPE_FIXDAY){
					Date d = DateUtil.parseDate(fixDay, "yyyy-MM-dd");
					if(d != null){
						cal.setTimeInMillis(now);
						int d1 = cal.get(Calendar.DAY_OF_YEAR);
						cal.setTime(d);
						int d2 = cal.get(Calendar.DAY_OF_YEAR);
						if(d1 == d2){
							return true;
						}
					}
				}else if(timeType == TIME_TYPE_FIXDAY_RANGE){
					try{
						String[] dates = fixDayRange.split("~");
						if(dates.length != 2){
							dates = fixDayRange.split("~");
						}
						if(dates.length == 2){
							cal.setTimeInMillis(now);
							int startDay = Integer.parseInt(dates[0]);
							int endDay = Integer.parseInt(dates[1]);
							int date = Integer.parseInt(DateUtil.formatDate(cal.getTime(), "yyyyMMdd"));
							if(date >= startDay && date <= endDay){
								return true;
							}
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
					return false;
				}
			}
		}
		return false;
	}
	
	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public String getFixDay() {
		return fixDay;
	}

	public void setFixDay(String fixDay) {
		this.fixDay = fixDay;
	}

	public String getFixDayRange() {
		return fixDayRange;
	}

	public void setFixDayRange(String fixDayRange) {
		this.fixDayRange = fixDayRange;
	}

	public String getStartTimeInDay() {
		return startTimeInDay;
	}

	public void setStartTimeInDay(String startTimeInDay) {
		this.startTimeInDay = startTimeInDay;
	}

	public String getEndTimeInDay() {
		return endTimeInDay;
	}

	public void setEndTimeInDay(String endTimeInDay) {
		this.endTimeInDay = endTimeInDay;
	}

	public int getPlayerLevelLimit() {
		return playerLevelLimit;
	}

	public void setPlayerLevelLimit(int playerLevelLimit) {
		this.playerLevelLimit = playerLevelLimit;
	}

	public int getPlayerPoliticalCampLimit() {
		return playerPoliticalCampLimit;
	}

	public void setPlayerPoliticalCampLimit(int playerPoliticalCampLimit) {
		this.playerPoliticalCampLimit = playerPoliticalCampLimit;
	}

	public boolean isEnableMapLimit() {
		return enableMapLimit;
	}

	public void setEnableMapLimit(boolean enableMapLimit) {
		this.enableMapLimit = enableMapLimit;
	}

	public String getMapLimit() {
		return mapLimit;
	}

	public void setMapLimit(String mapLimit) {
		this.mapLimit = mapLimit;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public int getBuffLevel() {
		return buffLevel;
	}

	public void setBuffLevel(int buffLevel) {
		this.buffLevel = buffLevel;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
