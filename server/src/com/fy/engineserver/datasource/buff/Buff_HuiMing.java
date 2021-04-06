package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 灰名
 *
 */
@SimpleEmbeddable
public class Buff_HuiMing extends Buff{

	protected String battleFieldName = "";

	public String getBattleFieldName() {
		return battleFieldName;
	}

	public void setBattleFieldName(String battleFieldName) {
		this.battleFieldName = battleFieldName;
	}

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.activePk = true;
			if(p.getNameColorType() <= 0){
				p.setNameColorType((byte)1);
			}
		}
	}

	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			p.activePk = false;
			if(p.getNameColorType() == 1){
				p.setNameColorType((byte)0);
			}
		}
	}

	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

	}

}
