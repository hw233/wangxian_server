package com.fy.engineserver.sifang.info;

public class SiFangOverInfo {

	private long jiaZuId;
	
	private String JiaZuName;
	
	private int leftNum;
	
	private int killNum;
	
	private int reward;

	public void setJiaZuName(String jiaZuName) {
		JiaZuName = jiaZuName;
	}

	public String getJiaZuName() {
		return JiaZuName;
	}

	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}

	public int getLeftNum() {
		return leftNum;
	}

	public void setKillNum(int killNum) {
		this.killNum = killNum;
	}

	public int getKillNum() {
		return killNum;
	}

	public void setJiaZuId(long jiaZuId) {
		this.jiaZuId = jiaZuId;
	}

	public long getJiaZuId() {
		return jiaZuId;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public int getReward() {
		return reward;
	}
	
}
