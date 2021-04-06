package com.fy.engineserver.activity.xianling;

import com.fy.engineserver.activity.shop.ActivityProp;

/**
 * 仙灵积分奖励
 * @author Administrator
 * 
 */
public class ScorePrize {
	private int needScore; // 积分要求
	private ActivityProp[] prizeProps;
	private long[] tempIds; // 读表时创建临时物品id

	public ScorePrize(int needScore, ActivityProp[] prizeProps, long[] tempIds) {
		this.needScore = needScore;
		this.prizeProps = prizeProps;
		this.tempIds = tempIds;
	}

	public int getNeedScore() {
		return needScore;
	}

	public void setNeedScore(int needScore) {
		this.needScore = needScore;
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
