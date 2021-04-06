package com.fy.engineserver.activity;

import java.util.Arrays;
import java.util.Calendar;

import com.xuanzhi.boss.game.GameConstants;

/**
 * 活动介绍数据
 * 
 */
public class ActivityIntroduce {

	private int id;
	private String icon;
	private int showType;
	private String openTimeDistance;
	private String name;
	private String startGame;
	private String startGameCnName;
	private String startNpc;
	private int startX;
	private int startY;
	private String des;
	private int activityAdd;
	private String[] lables = new String[0];
	private int levelLimit;
	private String taskGroupName;
	private int dailyNum;
	/** 周几的活动 */
	private Integer[] weekDay;
	/** 开始时间 */
	private Calendar start;
	/** 结束时间 */
	private Calendar end;

	private String isShowAccordTime;

	private String[] showServernames;

	private String[] limitServernames;

	/** 活动期限 */
	private long totalStart;
	private long totalEnd;
	//星
	private int star;

	
	private int activityType;
	private int activityState;
	
	
	private String [] startGames;
	private String [] startGameCnNames;
	private int [] startXs;
	private int [] startYs;
	private int doType;
	
	
	public int getDoType() {
		return doType;
	}

	public void setDoType(int doType) {
		this.doType = doType;
	}

	public String[] getStartGames() {
		return startGames;
	}

	public void setStartGames(String[] startGames) {
		this.startGames = startGames;
	}

	public String[] getStartGameCnNames() {
		return startGameCnNames;
	}

	public void setStartGameCnNames(String[] startGameCnNames) {
		this.startGameCnNames = startGameCnNames;
	}

	public int[] getStartXs() {
		return startXs;
	}

	public void setStartXs(int[] startXs) {
		this.startXs = startXs;
	}

	public int[] getStartYs() {
		return startYs;
	}

	public void setStartYs(int[] startYs) {
		this.startYs = startYs;
	}

	public ActivityIntroduce() {
		// TODO Auto-generated constructor stub
	}

	public ActivityIntroduce(int id, String icon, int showType, String openTimeDistance, String name, String startGame, String startGameCnName, String startNpc, int startX, int startY, String des, int activityAdd, String[] lables, int levelLimit, String taskGroupName, int dailyNum, Integer[] weekDay, Calendar start, Calendar end, String isShowAccordTime, String[] showServernames, String[] limitServernames, long totalStart, long totalEnd) {
		this.id = id;
		this.icon = icon;
		this.showType = showType;
		this.openTimeDistance = openTimeDistance;
		this.name = name;
		this.startGame = startGame;
		this.startGameCnName = startGameCnName;
		this.startNpc = startNpc;
		this.startX = startX;
		this.startY = startY;
		this.des = des;
		this.activityAdd = activityAdd;
		this.lables = lables;
		this.levelLimit = levelLimit;
		this.taskGroupName = taskGroupName;
		this.dailyNum = dailyNum;
		this.weekDay = weekDay;
		this.start = start;
		this.end = end;
		this.isShowAccordTime = isShowAccordTime;
		this.showServernames = showServernames;
		this.limitServernames = limitServernames;
		this.totalStart = totalStart;
		this.totalEnd = totalEnd;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public int getActivityState() {
		return activityState;
	}

	public void setActivityState(int activityState) {
		this.activityState = activityState;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public String getOpenTimeDistance() {
		return openTimeDistance;
	}

	public void setOpenTimeDistance(String openTimeDistance) {
		this.openTimeDistance = openTimeDistance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartGame() {
		return startGame;
	}

	public void setStartGame(String startGame) {
		this.startGame = startGame;
	}

	public String getStartNpc() {
		return startNpc;
	}

	public void setStartNpc(String startNpc) {
		this.startNpc = startNpc;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getActivityAdd() {
		return activityAdd;
	}

	public void setActivityAdd(int activityAdd) {
		this.activityAdd = activityAdd;
	}

	public String[] getLables() {
		return lables;
	}

	public void setLables(String[] lables) {
		this.lables = lables;
	}

	public String getStartGameCnName() {
		return startGameCnName;
	}

	public void setStartGameCnName(String startGameCnName) {
		this.startGameCnName = startGameCnName;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	public int getDailyNum() {
		return dailyNum;
	}

	public void setDailyNum(int dailyNum) {
		this.dailyNum = dailyNum;
	}

	public String getTaskGroupName() {
		return taskGroupName;
	}

	public void setTaskGroupName(String taskGroupName) {
		this.taskGroupName = taskGroupName;
	}

	public Integer[] getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(Integer[] weekDay) {
		this.weekDay = weekDay;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public String getIsShowAccordTime() {
		return isShowAccordTime;
	}

	public void setIsShowAccordTime(String isShowAccordTime) {
		this.isShowAccordTime = isShowAccordTime;
	}

	public String[] getShowServernames() {
		return showServernames;
	}

	public void setShowServernames(String[] showServernames) {
		this.showServernames = showServernames;
	}

	public String[] getLimitServernames() {
		return limitServernames;
	}

	public void setLimitServernames(String[] limitServernames) {
		this.limitServernames = limitServernames;
	}

	public long getTotalStart() {
		return totalStart;
	}

	public void setTotalStart(long totalStart) {
		this.totalStart = totalStart;
	}

	public long getTotalEnd() {
		return totalEnd;
	}

	public void setTotalEnd(long totalEnd) {
		this.totalEnd = totalEnd;
	}

	public boolean isIncycleForTotal(Calendar now) {
		int week = now.get(Calendar.DAY_OF_WEEK);
		boolean findWeek = false;
		for (int i = 0; i < weekDay.length; i++) {
			if (week == weekDay[i]) {
				findWeek = true;
				break;
			}
		}
		if (!findWeek) {// 星期不符
			return false;
		}
		long nowTimes = System.currentTimeMillis();
		if (nowTimes < totalStart || nowTimes > totalEnd) {
			return false;
		}
		return true;
	}
	
	Calendar isincycle = Calendar.getInstance();
	public int getAState(){
		if (isIncycleForTotal(isincycle) && isServerShow() && isShowAccordStartAndEndTime()) {
			if(isIncycle(isincycle)){
				return 2;
			}
			if(半小时即将开启()){
				return 3;
			}
		}
		return 0;
	}
	
	public boolean 半小时即将开启(){
		int startMinute = start.get(Calendar.HOUR_OF_DAY) * 60 + start.get(Calendar.MINUTE);
		int nowMinute = isincycle.get(Calendar.HOUR_OF_DAY) * 60 + isincycle.get(Calendar.MINUTE) + 30;
		return nowMinute <= startMinute;
	}

	public boolean isIncycle(Calendar now) {
		int week = now.get(Calendar.DAY_OF_WEEK);
		boolean findWeek = false;
		for (int i = 0; i < weekDay.length; i++) {
			if (week == weekDay[i]) {
				findWeek = true;
				break;
			}
		}
		if (!findWeek) {// 星期不符
			return false;
		}
		// 计算分钟数字
		int nowMinute = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
		int startMinute = start.get(Calendar.HOUR_OF_DAY) * 60 + start.get(Calendar.MINUTE);
		int endMinute = end.get(Calendar.HOUR_OF_DAY) * 60 + end.get(Calendar.MINUTE);
		return nowMinute >= startMinute && nowMinute <= endMinute;
	}

	// 该活动在游戏服是否开放
	public boolean isServerShow() {
		String servername = GameConstants.getInstance().getServerName();
		if (showServernames != null && showServernames.length > 0) {
			for (int i = 0; i < showServernames.length; i++) {
				if (servername.trim().equals(showServernames[i].trim())) {
					return true;
				}
			}
			return false;
		} else if (limitServernames != null && limitServernames.length > 0) {
			for (int j = 0; j < limitServernames.length; j++) {
				if (servername.trim().equals(limitServernames[j].trim())) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isShowAccordStartAndEndTime() {
		Calendar isincycle = Calendar.getInstance();
		if (!this.isIncycleForTotal(isincycle)) {
			return false;
		}

		long now = System.currentTimeMillis();
		if ("是".equals(isShowAccordTime)) {
			if (now < totalStart || now > totalEnd) {
				return false;
			}
		} else {
			return now < totalEnd;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ActivityIntroduce [activityAdd=" + activityAdd
				+ ", activityState=" + activityState + ", activityType="
				+ activityType + ", dailyNum=" + dailyNum + ", des=" + des
				+ ", end=" + end + ", icon=" + icon + ", id=" + id
				+ ", isShowAccordTime=" + isShowAccordTime + ", isincycle="
				+ isincycle + ", lables=" + Arrays.toString(lables)
				+ ", levelLimit=" + levelLimit + ", limitServernames="
				+ Arrays.toString(limitServernames) + ", name=" + name
				+ ", openTimeDistance=" + openTimeDistance
				+ ", showServernames=" + Arrays.toString(showServernames)
				+ ", showType=" + showType + ", star=" + star + ", start="
				+ start + ", startGame=" + startGame + ", startGameCnName="
				+ startGameCnName + ", startNpc=" + startNpc + ", startX="
				+ startX + ", startY=" + startY + ", taskGroupName="
				+ taskGroupName + ", totalEnd=" + totalEnd + ", totalStart="
				+ totalStart + ", weekDay=" + Arrays.toString(weekDay) + "]";
	}

}