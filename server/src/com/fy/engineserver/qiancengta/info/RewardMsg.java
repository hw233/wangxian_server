package com.fy.engineserver.qiancengta.info;

//客户端奖励数据，因为不能做2维数组，所以建了个对象
public class RewardMsg {
	private int cengIndex;
	
	private long[] rewardid;
	
	private int[] nums;

	public int getCengIndex() {
		return cengIndex;
	}

	public void setCengIndex(int cengIndex) {
		this.cengIndex = cengIndex;
	}

	public long[] getRewardid() {
		return rewardid;
	}

	public void setRewardid(long[] rewardid) {
		this.rewardid = rewardid;
	}

	public void setNums(int[] nums) {
		this.nums = nums;
	}

	public int[] getNums() {
		return nums;
	}
	
}
