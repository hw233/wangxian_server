package com.fy.engineserver.activity.newChongZhiActivity;

public class RmbRewardClientData {

	private int dataID;								//活动ID
	private long needMoney;							//活动金额
	private long[] entityID;						//物品ID
	private int[] entityNums;						//数目
	private boolean[] rewardRare;					//是否珍贵
	private boolean isGetBefore;					//是否领取过
	private int showType;							//显示类型
	private long showMoney;							//显示数值
	
	public void setDataID(int dataID) {
		this.dataID = dataID;
	}
	public int getDataID() {
		return dataID;
	}
	public void setNeedMoney(long needMoney) {
		this.needMoney = needMoney;
	}
	public long getNeedMoney() {
		return needMoney;
	}
	public void setEntityID(long[] entityID) {
		this.entityID = entityID;
	}
	public long[] getEntityID() {
		return entityID;
	}
	public void setEntityNums(int[] entityNums) {
		this.entityNums = entityNums;
	}
	public int[] getEntityNums() {
		return entityNums;
	}
	public void setRewardRare(boolean[] rewardRare) {
		this.rewardRare = rewardRare;
	}
	public boolean[] getRewardRare() {
		return rewardRare;
	}
	public void setIsGetBefore(boolean isGetBefore) {
		this.isGetBefore = isGetBefore;
	}
	public boolean isIsGetBefore() {
		return isGetBefore;
	}
	public void setShowType(int showType) {
		this.showType = showType;
	}
	public int getShowType() {
		return showType;
	}
	public void setShowMoney(long showMoney) {
		this.showMoney = showMoney;
	}
	public long getShowMoney() {
		return showMoney;
	}
	
}
