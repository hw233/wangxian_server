package com.fy.engineserver.activity.chongzhiActivity;

public class MonthCardInfo {

	private String cardName = "";
	private String cardId = "";
	private String chargeMoney = "";
	private long [] chargeRewardIds = new long[0];	
	private int [] chargeRewardCounts = new int[0];		
	private long [] dayRewardIds = new long[0];	
	private int [] dayRewardCounts = new int[0];	
	private String dayRewardInfo = "";
	private long hasDayMess;
	private boolean canReward;
	private String str1 = "";
	
	public MonthCardInfo(){}
	private MonthCardInfo(String cardName, String chargeMoney, long[] chargeRewardIds, int[] chargeRewardCounts, String dayRewardInfo, long hasDayMess, boolean canReward,long [] dayRewardIds,int []dayRewardCounts) {
		super();
		this.cardName = cardName;
		this.chargeMoney = chargeMoney;
		this.chargeRewardIds = chargeRewardIds;
		this.chargeRewardCounts = chargeRewardCounts;
		this.dayRewardInfo = dayRewardInfo;
		this.hasDayMess = hasDayMess;
		this.canReward = canReward;
		this.dayRewardIds = dayRewardIds;
		this.dayRewardCounts = dayRewardCounts;
	}
	
	public String getCardId() {
		return this.cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCardName() {
		return this.cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getChargeMoney() {
		return this.chargeMoney;
	}
	public void setChargeMoney(String chargeMoney) {
		this.chargeMoney = chargeMoney;
	}
	public long[] getChargeRewardIds() {
		return this.chargeRewardIds;
	}
	public void setChargeRewardIds(long[] chargeRewardIds) {
		this.chargeRewardIds = chargeRewardIds;
	}
	public int[] getChargeRewardCounts() {
		return this.chargeRewardCounts;
	}
	public void setChargeRewardCounts(int[] chargeRewardCounts) {
		this.chargeRewardCounts = chargeRewardCounts;
	}
	public String getDayRewardInfo() {
		return this.dayRewardInfo;
	}
	public void setDayRewardInfo(String dayRewardInfo) {
		this.dayRewardInfo = dayRewardInfo;
	}

	public long getHasDayMess() {
		return this.hasDayMess;
	}
	public void setHasDayMess(long hasDayMess) {
		this.hasDayMess = hasDayMess;
	}
	public boolean isCanReward() {
		return this.canReward;
	}
	public void setCanReward(boolean canReward) {
		this.canReward = canReward;
	}
	public String getStr1() {
		return this.str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public long[] getDayRewardIds() {
		return this.dayRewardIds;
	}
	public void setDayRewardIds(long[] dayRewardIds) {
		this.dayRewardIds = dayRewardIds;
	}
	public int[] getDayRewardCounts() {
		return this.dayRewardCounts;
	}
	public void setDayRewardCounts(int[] dayRewardCounts) {
		this.dayRewardCounts = dayRewardCounts;
	}
	
}
