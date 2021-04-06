package com.fy.engineserver.activity.minigame;

import java.sql.Timestamp;

public class MinigameActivityModel {
	/** 活动id */
	private int id;
	/** 描述 */
	private String tips;
	/** 开始时间 */
	private Timestamp startTime;
	/** 结束时间 */
	private Timestamp endTime;
	/** 基础勾玉数增加 */
	private int addAmount;
	/** 等级限制 */
	private int levelLimit;
	
	public String toString(){
		return "id=" + id + "  tips=" + tips + "  startTime=" + startTime + "  endTime=" + endTime + "  addAmount=" + addAmount + " levelLimit=" + levelLimit;
	} 
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public int getAddAmount() {
		return addAmount;
	}
	public void setAddAmount(int addAmount) {
		this.addAmount = addAmount;
	}
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	
	
}
