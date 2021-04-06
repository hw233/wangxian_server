package com.fy.engineserver.activity.xianling;

/**
 * 给客户端发临时物品id和数量
 * @author Administrator
 * 
 */
public class TempPrize {
	private long[] tempIds;
	private String[] articleNames;
	private int[] nums;
	private boolean[] bind;

	public TempPrize() {
	}

	public TempPrize(long[] tempIds, int[] nums) {
		this.tempIds = tempIds;
		this.nums = nums;
	}

	public TempPrize(long[] tempIds, String[] articleNames, int[] nums, boolean[] bind) {
		super();
		this.tempIds = tempIds;
		this.articleNames = articleNames;
		this.nums = nums;
		this.bind = bind;
	}

	public long[] getTempIds() {
		return tempIds;
	}

	public void setTempIds(long[] tempIds) {
		this.tempIds = tempIds;
	}

	public int[] getNums() {
		return nums;
	}

	public void setNums(int[] nums) {
		this.nums = nums;
	}

	public String[] getArticleNames() {
		return articleNames;
	}

	public void setArticleNames(String[] articleNames) {
		this.articleNames = articleNames;
	}

	public boolean[] getBind() {
		return bind;
	}

	public void setBind(boolean[] bind) {
		this.bind = bind;
	}

}
