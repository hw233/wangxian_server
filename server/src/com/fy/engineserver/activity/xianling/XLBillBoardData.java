package com.fy.engineserver.activity.xianling;

import java.io.Serializable;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class XLBillBoardData implements Serializable {
	private transient static final long serialVersionUID = 1L;
	private int activityId;// 活动id
	// private transient int crossServerRank; // 跨服排行
	private String serverName;
	private String playerName;
	private String userName;
	private int score; // 积分
	private int crossServerRank; // 跨服排行
	private long lastUpdateTime;// 上次更新积分时间

	public XLBillBoardData(int activityId, String serverName, String playerName, String userName, int score, long lastUpdateTime) {
		super();
		this.activityId = activityId;
		this.serverName = serverName;
		this.playerName = playerName;
		this.userName = userName;
		this.score = score;
		this.crossServerRank = -1;
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getCrossServerRank() {
		return crossServerRank;
	}

	public void setCrossServerRank(int crossServerRank) {
		this.crossServerRank = crossServerRank;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public XLBillBoardData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "XLBillBoardData:activityId=" + activityId + ",serverName=" + serverName + ",score=" + score + ",lastUpdateTime=" + lastUpdateTime;
	}
}
