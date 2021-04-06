package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;

public class Buff_XianlingScore extends Buff {
	private int rate;//从1开始

	public void start(Fighter owner) {
		if (owner instanceof Player) {
			rate = ((BuffTemplate_XianlingScore) this.getTemplate()).getRate()[this.getLevel()];
			Player p = (Player) owner;
			p.setScoreBuff(rate);
		} else if (owner instanceof Sprite) {
		}
	}

	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			p.setScoreBuff(0);
		} else if (owner instanceof Sprite) {
		}
	}

	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner, long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

}
