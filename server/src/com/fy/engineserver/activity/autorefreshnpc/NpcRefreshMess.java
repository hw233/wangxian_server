package com.fy.engineserver.activity.autorefreshnpc;

import com.fy.engineserver.core.Game;

/**
 * 代表一条要刷新的npc配置信息
 * 
 *
 */
public class NpcRefreshMess {

	//NPC|怪物
	private int spritetype;
	
	//是否是中立
	private boolean isPeace;
	
	//地图名中文(可以多个)
	private String mapname;
	
	//地图名英文
	private String mapnameen;
	
	//哪一天，多天，相隔
	private String days;
	
	//某天的刷新时间段
	private String times;
	
	//要刷新的NPC，多个,相隔
	private String npcs;
	
	//npc刷新间隔
	private int npctimes;
	
	//是否全地图随机
	private boolean isallmap;
	
	//国家编号为1 2 3
	private String country;
	
	//即将刷新的怪物id
	private int nextRefreshSptiteId;
	
	private int lastRefreshSptiteId;
	
	private long lastRefreshTime;
	
	//lastgame
	private Game lastgame;

	public int getSpritetype() {
		return spritetype;
	}

	public void setSpritetype(int spritetype) {
		this.spritetype = spritetype;
	}

	public boolean isPeace() {
		return isPeace;
	}

	public void setPeace(boolean isPeace) {
		this.isPeace = isPeace;
	}

	public String getMapname() {
		return mapname;
	}

	public void setMapname(String mapname) {
		this.mapname = mapname;
	}

	public String getMapnameen() {
		return mapnameen;
	}

	public void setMapnameen(String mapnameen) {
		this.mapnameen = mapnameen;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getNpcs() {
		return npcs;
	}

	public void setNpcs(String npcs) {
		this.npcs = npcs;
	}

	public int getNpctimes() {
		return npctimes;
	}

	public void setNpctimes(int npctimes) {
		this.npctimes = npctimes;
	}

	public boolean isIsallmap() {
		return isallmap;
	}

	public void setIsallmap(boolean isallmap) {
		this.isallmap = isallmap;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getNextRefreshSptiteId() {
		return nextRefreshSptiteId;
	}

	public void setNextRefreshSptiteId(int nextRefreshSptiteId) {
		this.nextRefreshSptiteId = nextRefreshSptiteId;
	}

	public Game getLastgame() {
		return lastgame;
	}

	public void setLastgame(Game lastgame) {
		this.lastgame = lastgame;
	}

	public int getLastRefreshSptiteId() {
		return lastRefreshSptiteId;
	}

	public void setLastRefreshSptiteId(int lastRefreshSptiteId) {
		this.lastRefreshSptiteId = lastRefreshSptiteId;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime(long lastRefreshTime) {
		this.lastRefreshTime = lastRefreshTime;
	}

	@Override
	public String toString() {
		return "NpcRefreshMess [spritetype=" + spritetype + ", isPeace="
				+ isPeace + ", mapname=" + mapname + ", mapnameen=" + mapnameen
				+ ", days=" + days + ", times=" + times + ", npcs=" + npcs
				+ ", npctimes=" + npctimes + ", isallmap=" + isallmap
				+ ", country=" + country + ", nextRefreshSptiteId="
				+ nextRefreshSptiteId + "]";
	}
	
}
