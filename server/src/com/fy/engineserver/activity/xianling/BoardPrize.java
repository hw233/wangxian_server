package com.fy.engineserver.activity.xianling;

import com.fy.engineserver.activity.shop.ActivityProp;

public class BoardPrize {
	private int startIndex; // 起始名次
	private int endIndex; // 截止名次
	private ActivityProp[] prizeProps;
	private long[] tempIds;

	public BoardPrize(int startIndex, int endIndex, ActivityProp[] prizeProps, long[] tempIds) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.prizeProps = prizeProps;
		this.tempIds = tempIds;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public ActivityProp[] getPrizeProps() {
		return prizeProps;
	}

	public void setPrizeProps(ActivityProp[] prizeProps) {
		this.prizeProps = prizeProps;
	}

	public long[] getTempIds() {
		return tempIds;
	}

	public void setTempIds(long[] tempIds) {
		this.tempIds = tempIds;
	}

}
