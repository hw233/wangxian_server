package com.fy.engineserver.activity.newChongZhiActivity;

public class WeekClientActivity {

	private String title;
	
	private long[] rewardEntityIDs;
	private int[] rewardEntityNums;
	
	private long needValue;

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setRewardEntityIDs(long[] rewardEntityIDs) {
		this.rewardEntityIDs = rewardEntityIDs;
	}

	public long[] getRewardEntityIDs() {
		return rewardEntityIDs;
	}

	public void setRewardEntityNums(int[] rewardEntityNums) {
		this.rewardEntityNums = rewardEntityNums;
	}

	public int[] getRewardEntityNums() {
		return rewardEntityNums;
	}

	public void setNeedValue(long needValue) {
		this.needValue = needValue;
	}

	public long getNeedValue() {
		return needValue;
	}
}
