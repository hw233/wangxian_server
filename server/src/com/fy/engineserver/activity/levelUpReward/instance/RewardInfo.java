package com.fy.engineserver.activity.levelUpReward.instance;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class RewardInfo {
	/** 返利id，对应配表中 */
	private int type;
	/** 已经领取奖励的次数(此奖励只会顺序发放) */
	private int receiveTimes;
	
	@Override
	public String toString() {
		return "RewardInfo [type=" + type + ", receiveTimes=" + receiveTimes + "]";
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getReceiveTimes() {
		return receiveTimes;
	}
	public void setReceiveTimes(int receiveTimes) {
		this.receiveTimes = receiveTimes;
	}
	
	
}
