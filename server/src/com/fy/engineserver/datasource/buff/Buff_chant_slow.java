package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class Buff_chant_slow extends Buff {

	int chant_slow_rate = 0;

	public Buff_chant_slow() {
	}

	public int getChant_slow_rate() {
		return chant_slow_rate;
	}

	public void setChant_slow_rate(int chant_slow_rate) {
		this.chant_slow_rate = chant_slow_rate;
	}

	@Override
	public void start(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			BuffTemplate_chant_slow bt = (BuffTemplate_chant_slow) this.getTemplate();
			if (bt != null && bt.getChant_slow_rate().length > this.getLevel()) {
				this.chant_slow_rate = bt.getChant_slow_rate()[this.getLevel()];
			}
		}
	}

	@Override
	public void end(Fighter owner) {
		super.end(owner);
	}

	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner, long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	}
}
