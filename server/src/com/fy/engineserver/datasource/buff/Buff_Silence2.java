package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 沉默
 */
@SimpleEmbeddable
public class Buff_Silence2 extends Buff{

	
	/**
	 *  沉默级别
	 *  0:没有沉默，可以发言
	 *  1:普通沉默，只用于限制玩家发言
	 *  2:2级沉默，用于限制该玩家的交易，拍卖，摆摊，邮件等操作
	 */
	private int silenceLevel;
	
	public int getSilenceLevel() {
		return silenceLevel;
	}

	public void setSilenceLevel(int silenceLevel) {
		this.silenceLevel = silenceLevel;
	}
	
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){

	}

	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		super.end(owner);
		if (Game.logger.isWarnEnabled()) {
			Game.logger.warn("[检查玩家是否沉默] [沉默测试] [buff结束] [playerid:{}] [buff:{}]", new Object[] {owner==null?"null":owner.getId(),this.getTemplateName()});
		}
	}

	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		if(silenceLevel>0){
			long now = System.currentTimeMillis();
			super.heartbeat(owner, heartBeatStartTime, interval, game);
			if(this.getInvalidTime() < now){
				end(owner);
				if (Game.logger.isWarnEnabled()) {
					Game.logger.warn("[检查玩家是否沉默] [沉默测试] [buff结束heartbeat] [playerid:{}] [buff:{}]", new Object[] {owner==null?"null":owner.getId()});
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Buff_Silence2 [silenceLevel=" + silenceLevel + "] "+super.toString();
	}
	
}
