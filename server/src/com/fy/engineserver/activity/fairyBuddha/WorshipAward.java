package com.fy.engineserver.activity.fairyBuddha;

import java.util.Map;

import com.fy.engineserver.util.config.ServerFit4Activity;

public class WorshipAward {
	private String startTime;
	private String endTime;
	private ServerFit4Activity sf4a;
	private Map<Byte, String> awardNameMap;
	private Map<Byte, String> desMap;
	private Map<Byte, Long> priceMap;
	private Map<Byte, Map<Byte, WeekdayAward>> levelAward;// <档次,<一周第几天,奖励>>

	public WorshipAward() {
	}

	public WorshipAward(String startTime, String endTime, ServerFit4Activity sf4a, Map<Byte, String> awardNameMap, Map<Byte, String> desMap, Map<Byte, Long> priceMap, Map<Byte, Map<Byte, WeekdayAward>> levelAward) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.sf4a = sf4a;
		this.awardNameMap = awardNameMap;
		this.desMap = desMap;
		this.priceMap = priceMap;
		this.levelAward = levelAward;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public ServerFit4Activity getSf4a() {
		return sf4a;
	}

	public void setSf4a(ServerFit4Activity sf4a) {
		this.sf4a = sf4a;
	}

	public Map<Byte, String> getAwardNameMap() {
		return awardNameMap;
	}

	public void setAwardNameMap(Map<Byte, String> awardNameMap) {
		this.awardNameMap = awardNameMap;
	}

	public Map<Byte, String> getDesMap() {
		return desMap;
	}

	public void setDesMap(Map<Byte, String> desMap) {
		this.desMap = desMap;
	}

	public Map<Byte, Long> getPriceMap() {
		return priceMap;
	}

	public void setPriceMap(Map<Byte, Long> priceMap) {
		this.priceMap = priceMap;
	}

	public Map<Byte, Map<Byte, WeekdayAward>> getLevelAward() {
		return levelAward;
	}

	public void setLevelAward(Map<Byte, Map<Byte, WeekdayAward>> levelAward) {
		this.levelAward = levelAward;
	}

	@Override
	public String toString() {
		return "WorshipAward [awardNameMap=" + awardNameMap + ", desMap=" + desMap + ", endTime=" + endTime + ", levelAward=" + levelAward + ", priceMap=" + priceMap + ", sf4a=" + sf4a + ", startTime=" + startTime + "]";
	}

}
