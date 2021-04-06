package com.fy.engineserver.activity.fateActivity;

public class FateTemplate {

	
//	活动名	地图名	区域名	持续多长时间	俩人距离	经验变化间隔	刷新时间间隔	道具名	
//	刷新没有跟自己做过的个数	刷新跟自己做过的个数

	private long id;
	private String activityName;
	private String mapName;
	private String regionName;
	
	private int duration;
	private int distance;
	private double expInterval;
	private int flushInterval;
	
	private String propsName;
	
	private int undoNum;
	private int doNum;
	
	private int perNum;
	private int country;
	
	private byte type;

	private String articles;

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public double getExpInterval() {
		return expInterval;
	}

	public void setExpInterval(double expInterval) {
		this.expInterval = expInterval;
	}

	public int getFlushInterval() {
		return flushInterval;
	}

	public void setFlushInterval(int flushInterval) {
		this.flushInterval = flushInterval;
	}

	public String getPropsName() {
		return propsName;
	}

	public void setPropsName(String propsName) {
		this.propsName = propsName;
	}

	public int getUndoNum() {
		return undoNum;
	}

	public void setUndoNum(int undoNum) {
		this.undoNum = undoNum;
	}

	public int getDoNum() {
		return doNum;
	}

	public void setDoNum(int doNum) {
		this.doNum = doNum;
	}
	public int getPerNum() {
		return perNum;
	}

	public void setPerNum(int perNum) {
		this.perNum = perNum;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getArticles() {
		return articles;
	}

	public void setArticles(String articles) {
		this.articles = articles;
	}
	
	
}
