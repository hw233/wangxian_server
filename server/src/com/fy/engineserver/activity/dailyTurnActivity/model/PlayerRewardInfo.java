package com.fy.engineserver.activity.dailyTurnActivity.model;

import java.io.Serializable;
import java.util.List;

import com.fy.engineserver.activity.dailyTurnActivity.DailyTurnManager;

public class PlayerRewardInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 转盘类型 */
	private int turnType;
	/** 转盘名 */
	private String turnName;
	/** 抽取到的奖励列表  (rewardIds.size()为玩家当日参与此类型转盘的次数) 此值可能为null*/
	private List<Integer> rewardIds;
	/** 周期内玩家购买此类抽奖次数 -- 计算玩家周期内可抽取奖品次数时需要加上此数*/
	private int purchaseTimes = 0;
	/** 抽奖次数使用状态   0未使用   2已使用。。顺序，与客户端显示的抽奖次数获取条件对应 */
	private int[] useStatus ;
	
	public PlayerRewardInfo (int turnType, String turnName) {
		this.turnName = turnName;
		this.turnType = turnType;
		TurnModel tm = DailyTurnManager.instance.turnMaps.get(turnType);
		setUseStatus(new int[tm.getMaxPlayTimes()]);
	}
	
	public void reset() {
		this.setRewardIds(null);
		this.setPurchaseTimes(0);
		TurnModel tm = DailyTurnManager.instance.turnMaps.get(turnType);
		setUseStatus(new int[tm.getMaxPlayTimes()]);
	}
	/**
	 * 需要根据玩家获取到次数的条件来做判断
	 */
	public boolean changeUseStatus(int index) {
		if (index >= useStatus.length) {
			return false;
		}
		if (useStatus[index] == DailyTurnManager.获取已用) {
			return false;
		}
		useStatus[index] = DailyTurnManager.获取已用;
		return true;
	}
	
	
	/**
	 * 是否已经获得过此奖励
	 * @param goodId
	 * @return
	 */
	public boolean alreadHasGood(int goodId) {
		if (rewardIds == null) {
			return false;
		}
		return rewardIds.contains(goodId);
	}
	
	public int getCyclePartTime() {
		if (rewardIds == null) {
			return 0;
		}
		return rewardIds.size();
	}
	
	@Override
	public String toString() {
		return "PlayerRewardInfo [turnType=" + turnType + ", turnName=" + turnName + ", rewardIds=" + rewardIds + "]";
	}
	
	public int getTurnType() {
		return turnType;
	}
	public void setTurnType(int turnType) {
		this.turnType = turnType;
	}
	public String getTurnName() {
		return turnName;
	}
	public void setTurnName(String turnName) {
		this.turnName = turnName;
	}
	public List<Integer> getRewardIds() {
		return rewardIds;
	}
	public void setRewardIds(List<Integer> rewardIds) {
		this.rewardIds = rewardIds;
	}

	public int getPurchaseTimes() {
		return purchaseTimes;
	}

	public void setPurchaseTimes(int purchaseTimes) {
		this.purchaseTimes = purchaseTimes;
	}

	public int[] getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(int[] useStatus) {
		this.useStatus = useStatus;
	}
	
	
}
