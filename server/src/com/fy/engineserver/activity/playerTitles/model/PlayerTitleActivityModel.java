package com.fy.engineserver.activity.playerTitles.model;

import com.fy.engineserver.util.config.ServerFit4Activity;

public class PlayerTitleActivityModel {
	/** 索引id */
	private int id;
	/** 活动开启时间 */
	private long startTime;
	/** 活动截止时间 */
	private long endTime;
	/** 活动匹配 */
	private ServerFit4Activity fit;
	/** 兑换所需物品名 */
	private String needArticle;
	/** 兑换所需物品颜色 */
	private int needColorType;
	/** 兑换所需物品数量 */
	private int needNum;
	/** 兑换获得称号名 */
	private String targetTitleName;
	
	@Override
	public String toString() {
		return "PlayerTitleActivityModel [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", fit=" + fit + ", needArticle=" + needArticle + ", needColorType=" + needColorType + ", needNum=" + needNum + ", targetTitleName=" + targetTitleName + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public ServerFit4Activity getFit() {
		return fit;
	}
	public void setFit(ServerFit4Activity fit) {
		this.fit = fit;
	}
	public String getNeedArticle() {
		return needArticle;
	}
	public void setNeedArticle(String needArticle) {
		this.needArticle = needArticle;
	}
	public int getNeedColorType() {
		return needColorType;
	}
	public void setNeedColorType(int needColorType) {
		this.needColorType = needColorType;
	}
	public int getNeedNum() {
		return needNum;
	}
	public void setNeedNum(int needNum) {
		this.needNum = needNum;
	}
	public String getTargetTitleName() {
		return targetTitleName;
	}
	public void setTargetTitleName(String targetTitleName) {
		this.targetTitleName = targetTitleName;
	}
	
	
	
}
