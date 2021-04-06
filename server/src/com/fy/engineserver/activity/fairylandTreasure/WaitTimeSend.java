package com.fy.engineserver.activity.fairylandTreasure;

import com.fy.engineserver.sprite.Player;

public class WaitTimeSend {
	private Player player;
	private String boxName;
	private ArticleForDraw afd;
	private long sendTime;

	public WaitTimeSend(Player player, String boxName, ArticleForDraw afd, long sendTime) {
		this.player = player;
		this.boxName = boxName;
		this.afd = afd;
		this.sendTime = sendTime;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getBoxName() {
		return boxName;
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	public ArticleForDraw getAfd() {
		return afd;
	}

	public void setAfd(ArticleForDraw afd) {
		this.afd = afd;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

}
