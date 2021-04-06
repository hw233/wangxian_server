package com.fy.engineserver.carbon.devilSquare.model;
/**
 * 用来记录玩家在副本中的行为，方便加限制
 * @author Administrator
 *
 */
public class DsPlayerDataModel {
	/** 复活次数 */
	private int reliveCount;
	/** 副本中获得的积分 */
	private long carbonScores;
	
	public DsPlayerDataModel(int reliveCount) {
		this.reliveCount = reliveCount;
	}

	public int getReliveCount() {
		return reliveCount;
	}

	public void setReliveCount(int reliveCount) {
		this.reliveCount = reliveCount;
	}

	public long getCarbonScores() {
		return carbonScores;
	}

	public void setCarbonScores(long carbonScores) {
		this.carbonScores = carbonScores;
	}
	
}
