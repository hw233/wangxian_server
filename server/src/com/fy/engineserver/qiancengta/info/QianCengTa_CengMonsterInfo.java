package com.fy.engineserver.qiancengta.info;

import java.util.ArrayList;

public class QianCengTa_CengMonsterInfo {

	private int monsterID;			//怪物ID
	
	private ArrayList<int[]> poss = new ArrayList<int[]>();			//怪物位置
	
	private long refTime;			//刷新时间

	public void setMonsterID(int monsterID) {
		this.monsterID = monsterID;
	}

	public int getMonsterID() {
		return monsterID;
	}

	public void setRefTime(long refTime) {
		this.refTime = refTime;
	}

	public long getRefTime() {
		return refTime;
	}

	public void setPoss(ArrayList<int[]> poss) {
		this.poss = poss;
	}

	public ArrayList<int[]> getPoss() {
		return poss;
	}
	
}
