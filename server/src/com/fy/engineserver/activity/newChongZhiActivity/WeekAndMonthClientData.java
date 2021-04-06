package com.fy.engineserver.activity.newChongZhiActivity;

public class WeekAndMonthClientData {

	private int dataID;									//活动ID
	private int type;									//活动类型
	private long showRMBMoney;							//显示的RMB金额
	private long needMoney;								//需要金额
	private String startTime;							//开始时间
	private String endTime;								//结束时间
	private long[] rewardEntityIDs;						//奖励IDs
	private int[] rewardEntityNums;						//奖励物品数目
	private boolean[] rewardRare;						//是否珍贵
	private long[] buyEntityIDs;						//购买IDs
	private int[] buyEntityNums;						//购买物品数目
	private boolean[] buyRare;							//是否珍贵
	private long buyPrice;								//购买价钱
	private long totalMoney;							//累计充值金额
	private long totalRMB;								//累计充值RMB
	private int getValue;								//-1代表不能领取，0代表能领取，1代表已经领取
	private int canBuy;									//-1代表不能购买, 0代表能购买, 1代表已经购买
	
	public void setDataID(int dataID) {
		this.dataID = dataID;
	}
	public int getDataID() {
		return dataID;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public void setShowRMBMoney(long showRMBMoney) {
		this.showRMBMoney = showRMBMoney;
	}
	public long getShowRMBMoney() {
		return showRMBMoney;
	}
	public void setNeedMoney(long needMoney) {
		this.needMoney = needMoney;
	}
	public long getNeedMoney() {
		return needMoney;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setRewardEntityIDs(long[] rewardEntityIDs) {
		this.rewardEntityIDs = rewardEntityIDs;
	}
	public long[] getRewardEntityIDs() {
		return rewardEntityIDs;
	}
	public void setRewardRare(boolean[] rewardRare) {
		this.rewardRare = rewardRare;
	}
	public boolean[] getRewardRare() {
		return rewardRare;
	}
	public void setBuyEntityIDs(long[] buyEntityIDs) {
		this.buyEntityIDs = buyEntityIDs;
	}
	public long[] getBuyEntityIDs() {
		return buyEntityIDs;
	}
	public void setBuyRare(boolean[] buyRare) {
		this.buyRare = buyRare;
	}
	public boolean[] getBuyRare() {
		return buyRare;
	}
	public void setBuyPrice(long buyPrice) {
		this.buyPrice = buyPrice;
	}
	public long getBuyPrice() {
		return buyPrice;
	}
	public void setTotalMoney(long totalMoney) {
		this.totalMoney = totalMoney;
	}
	public long getTotalMoney() {
		return totalMoney;
	}
	public void setGetValue(int getValue) {
		this.getValue = getValue;
	}
	public int getGetValue() {
		return getValue;
	}
	public void setCanBuy(int canBuy) {
		this.canBuy = canBuy;
	}
	public int getCanBuy() {
		return canBuy;
	}
	public void setRewardEntityNums(int[] rewardEntityNums) {
		this.rewardEntityNums = rewardEntityNums;
	}
	public int[] getRewardEntityNums() {
		return rewardEntityNums;
	}
	public void setBuyEntityNums(int[] buyEntityNums) {
		this.buyEntityNums = buyEntityNums;
	}
	public int[] getBuyEntityNums() {
		return buyEntityNums;
	}
	public void setTotalRMB(long totalRMB) {
		this.totalRMB = totalRMB;
	}
	public long getTotalRMB() {
		return totalRMB;
	}
	
	
}
