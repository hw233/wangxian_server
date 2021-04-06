package com.fy.engineserver.operating.activitybuff;

import java.util.Calendar;
import java.util.Date;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 一个活动 BOSS 的条目
 * 
 * 
 *
 */
public class ActivityFlushBossItem {

	public static final int TIME_TYPE_EVERYDAY = 0;
	
	public static final int TIME_TYPE_EVERYWEEKDAY = 1;
	
	public static final int TIME_TYPE_FIXDAY = 2;
	
	/**
	 * 某个时间段，每隔一段时间刷一次怪，如26号到28号每隔1小时刷一次怪
	 */
	public static final int TIME_TYPE_BETWEEN_TIME = 3;
	
	public static final String TIME_TYPE_NAMES[] = new String[]{Translate.text_5544,Translate.text_5545,Translate.text_5546,Translate.text_5550};
	
	
	int id;
	
	
	/**
	 *  时间类型，分为每天，每周几，和固定日期，每隔一段时间
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
	 * 每隔一段时间,格式如下：
	 * yyyy-MM-dd-HH-mm~yyyy-MM-dd-HH-mm内每隔多少秒刷怪
	 * 比如 2010-04-29-10-10~2010-06-29-10-10 每隔60秒刷怪
	 * 用到此变量时需要结合intervalTimeForBetweenTime
	 */
	protected String betweenTime = "2010-04-29-10-10~2010-06-29-10-10";

	/**
	 * 间隔多长时间刷一次怪，单位为秒
	 */
	protected long intervalTimeForBetweenTime = 60;

	/**
	 * 开始时间，格式如下：HH:mm
	 */
	protected String startTimeInDay = "00:00";
	
	/**
	 * 结束时间,格式如下：HH:mm
	 */
	protected String endTimeInDay =  "23:59";
	
	/**
	 * boss刷出的地图
	 */
	protected String mapName = "";
	
	/**
	 * boss刷出的坐标(数组第一维表示坐标点集的个数，必须和y一致)
	 * 第二维表示boss刷新的坐标点。
	 * 系统会随机选择一个坐标点集，然后开始刷新boss
	 */
	protected int x[][];

	protected int y[][];
	
	/**
	 * boss的id
	 */
	protected int bossId;

	protected boolean monsterFlag = true;
	
	/**
	 * boss刷出的时候的说话
	 */
	protected String sayContentToWorld = "";
	
	/**
	 * 上一次刷新的时间，此变量只能查看，不能修改
	 */
	protected long lastFlushTime = 0;
	
	protected String desp;
	
	
	protected Sprite sprite = null;
	
	
	//广播的次数
	public int broadcastTimes = 0;
	public long lastBroadcastTime = 0;
	
	public String getBetweenTime() {
		return betweenTime;
	}

	public void setBetweenTime(String betweenTime) {
		this.betweenTime = betweenTime;
	}

	public long getIntervalTimeForBetweenTime() {
		return intervalTimeForBetweenTime;
	}

	public void setIntervalTimeForBetweenTime(long intervalTimeForBetweenTime) {
		this.intervalTimeForBetweenTime = intervalTimeForBetweenTime;
	}

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

	public long getLastFlushTime(){
		return lastFlushTime;
	}
	
	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public int[][] getX() {
		return x;
	}

	public void setX(int[][] x) {
		this.x = x;
	}

	public int[][] getY() {
		return y;
	}

	public void setY(int[][] y) {
		this.y = y;
	}

	public int getBossId() {
		return bossId;
	}

	public void setBossId(int bossId) {
		this.bossId = bossId;
	}

	public String getSayContentToWorld() {
		return sayContentToWorld;
	}

	public void setSayContentToWorld(String sayContentToWorld) {
		this.sayContentToWorld = sayContentToWorld;
	}

	public boolean isActive(long now){
		Calendar cal = Calendar.getInstance();
		String dayStr = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		Date startTime = DateUtil.parseDate(dayStr+" " + this.startTimeInDay,"yyyy-MM-dd HH:mm");
		Date endTime = DateUtil.parseDate(dayStr+" " + this.endTimeInDay,"yyyy-MM-dd HH:mm");
		
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
				}
			}
		}
		if(timeType == TIME_TYPE_BETWEEN_TIME){
			String[] timeStr = betweenTime.split("~");
			if(timeStr != null && timeStr.length == 2){
				String startTimeStr = timeStr[0];
				String endTimeStr = timeStr[1];
				Date startD = DateUtil.parseDate(startTimeStr, "yyyy-MM-dd-HH-mm");
				Date endD = DateUtil.parseDate(endTimeStr, "yyyy-MM-dd-HH-mm");
				cal.setTime(startD);
				long startl = cal.getTimeInMillis();
				cal.setTime(endD);
				long endl = cal.getTimeInMillis();
				if( startl <= now && now <= endl){
					return true;
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


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isMonsterFlag() {
		return monsterFlag;
	}

	public void setMonsterFlag(boolean monsterFlag) {
		this.monsterFlag = monsterFlag;
	}
	
	
}
