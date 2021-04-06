package com.fy.engineserver.activity.fairylandTreasure;

import com.fy.engineserver.sprite.Player;

public class WaitForSend {
	private Player player;
	private ArticleForDraw afd;

	public WaitForSend(Player player, ArticleForDraw afd) {
		this.player = player;
		this.afd = afd;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArticleForDraw getAfd() {
		return afd;
	}

	public void setAfd(ArticleForDraw afd) {
		this.afd = afd;
	}

}
