package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class Buff_MeetMonster extends Buff {
	private int buffId; // 从1开始

	public void start(Fighter owner) {
		if (owner instanceof Player) {
			buffId = ((BuffTemplate_MeetMonster) this.getTemplate()).getBuffIds()[this.getLevel()];
			Player p = (Player) owner;
			if (buffId > 0) {
				if (XianLingManager.logger.isDebugEnabled()) {
					XianLingManager.logger.debug("[" + p.getLogString() + "] [加buff] [buffId:" + buffId + "] [buffLevel:" + this.getLevel() + "]");
				}
				p.setMeetMonsterBuffId(buffId);
			}
		}
	}

	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner) {
		if (owner instanceof Player) {
			Player p = (Player) owner;
			p.setMeetMonsterBuffId(0);
		}
	}

	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner, long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}
}
