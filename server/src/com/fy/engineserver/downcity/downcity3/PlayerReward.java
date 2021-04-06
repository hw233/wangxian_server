package com.fy.engineserver.downcity.downcity3;

import java.util.Arrays;


public class PlayerReward {

	private String canYuReward [];
	private String rankRewards [] [];
	private String showRewards [];
	
	public String[] getCanYuReward() {
		return canYuReward;
	}
	public void setCanYuReward(String[] canYuReward) {
		this.canYuReward = canYuReward;
	}
	public String[][] getRankRewards() {
		return rankRewards;
	}
	public void setRankRewards(String[][] rankRewards) {
		this.rankRewards = rankRewards;
	}
	
	public String[] getShowRewards() {
		return showRewards;
	}
	public void setShowRewards(String[] showRewards) {
		this.showRewards = showRewards;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<rankRewards.length;i++){
			if(rankRewards[i] != null){
				sb.append(Arrays.toString(rankRewards[i]));
			}
		}
		return "[canYuReward=" + Arrays.toString(canYuReward) + ", rankRewards=" + sb.toString()+ "]";
	}
	
}
