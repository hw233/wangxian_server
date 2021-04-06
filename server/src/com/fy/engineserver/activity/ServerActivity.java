package com.fy.engineserver.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.util.TimeTool;

/**
 * 分服务器的活动
 * 
 */
public class ServerActivity {

	public static Map<Integer, String> map = new HashMap<Integer, String>();
	static {
		map.put(10, "飘渺宝匣");
		map.put(20, "飘渺神匣");
	}
	/** 游戏服平台 */
	private Platform platform;
	/** 游戏服名字 */
	private String serverName;
	/** 开始时间 */
	private long startTime;
	/** 结束时间 */
	private long endTime;
	
	//private String notice;//@player@level

	private Map<Integer, String> levelPrizeMap;
	
	private Map<Integer, List<String>> levelNoticeMap;
	
	private Map<Integer, String> noticeMap;

	public ServerActivity(Platform platform, String serverName, String startTimeStr, String endTimeStr) {
		this(platform, serverName, startTimeStr, endTimeStr, map);
	}

	public ServerActivity(Platform platform, String serverName, String startTimeStr, String endTimeStr, Map<Integer, String> levelPrizeMap) {
		this.platform = platform;
		this.serverName = serverName;
		this.startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		this.endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		this.levelPrizeMap = levelPrizeMap;
	}

	/*
	 * 玩家获得礼包的同时发广播
	 */
	public ServerActivity(Platform platform, String serverName, String startTimeStr, String endTimeStr, Map<Integer, List<String>> levelNoticeMap, Map<Integer, String> noticeMap) {
		this.platform = platform;
		this.serverName = serverName;
		this.startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		this.endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		this.levelNoticeMap = levelNoticeMap;
		this.noticeMap = noticeMap;
	}
	
	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
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

	public Map<Integer, String> getLevelPrizeMap() {
		return levelPrizeMap;
	}

	public void setLevelPrizeMap(Map<Integer, String> levelPrizeMap) {
		this.levelPrizeMap = levelPrizeMap;
	}

	public Map<Integer, String> getNoticeMap() {
		return noticeMap;
	}

	public void setNoticeMap(Map<Integer, String> noticeMap) {
		this.noticeMap = noticeMap;
	}

	public Map<Integer, List<String>> getLevelNoticeMap() {
		return levelNoticeMap;
	}

	public void setLevelNoticeMap(Map<Integer, List<String>> levelNoticeMap) {
		this.levelNoticeMap = levelNoticeMap;
	}

}
