package com.fy.engineserver.activity.dailyTurnActivity.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TurnModel {
	/** 转盘id */
	private int turnId;
	/** 转盘名 */
	private String turnName;
	/** 参与等级限制 */
	private int levelLimit;
	/** 每天最多可抽奖次数 */
	private int maxPlayTimes;
	/** 免费次数需要vip等级 */
	private int vipLimit;
	/** 获得额外抽取次数所需充值数(RMB  单位:元) */
	private long chongzhiLimit;
	/** 购买额外抽取次数需要周期内重置数（RMB 单位：元） */
//	private long limit4Pruchase;
	/** 购买次数所需银子数量 */
	private long costSiliverNum;
	/** 周期内最大可购买次数 */
	private int cycleMaxBuy = 1;
	/** 转盘物品列表 */
	private List<Integer> goodIds = new ArrayList<Integer>();
	/** 公告描述 */
	private String description;
	/** 次数获取条件显示 */
	private String[] conditions;
	
	@Override
	public String toString() {
		return "TurnModel [turnId=" + turnId + ", turnName=" + turnName + ", levelLimit=" + levelLimit + ", maxPlayTimes=" + maxPlayTimes + ", vipLimit=" + vipLimit + ", chongzhiLimit=" + chongzhiLimit + ", costSiliverNum=" + costSiliverNum + ", cycleMaxBuy=" + cycleMaxBuy + ", goodIds=" + goodIds + ", description=" + description + ", conditions=" + Arrays.toString(conditions) + "]";
	}
	public int getTurnId() {
		return turnId;
	}
	public void setTurnId(int turnId) {
		this.turnId = turnId;
	}
	public String getTurnName() {
		return turnName;
	}
	public void setTurnName(String turnName) {
		this.turnName = turnName;
	}
	public int getMaxPlayTimes() {
		return maxPlayTimes;
	}
	public void setMaxPlayTimes(int maxPlayTimes) {
		this.maxPlayTimes = maxPlayTimes;
	}
	public int getVipLimit() {
		return vipLimit;
	}
	public void setVipLimit(int vipLimit) {
		this.vipLimit = vipLimit;
	}
	public long getChongzhiLimit() {
		return chongzhiLimit;
	}
	public void setChongzhiLimit(long chongzhiLimit) {
		this.chongzhiLimit = chongzhiLimit;
	}
	public long getCostSiliverNum() {
		return costSiliverNum;
	}
	public void setCostSiliverNum(long costSiliverNum) {
		this.costSiliverNum = costSiliverNum;
	}
	public List<Integer> getGoodIds() {
		return goodIds;
	}
	public void setGoodIds(List<Integer> goodIds) {
		this.goodIds = goodIds;
	}
//	public long getLimit4Pruchase() {
//		return limit4Pruchase;
//	}
//	public void setLimit4Pruchase(long limit4Pruchase) {
//		this.limit4Pruchase = limit4Pruchase;
//	}
	public int getCycleMaxBuy() {
		return cycleMaxBuy;
	}
	public void setCycleMaxBuy(int cycleMaxBuy) {
		this.cycleMaxBuy = cycleMaxBuy;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getConditions() {
		return conditions;
	}
	public void setConditions(String[] conditions) {
		this.conditions = conditions;
	}
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	
	
	
}
