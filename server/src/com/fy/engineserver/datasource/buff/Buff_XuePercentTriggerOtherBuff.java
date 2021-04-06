package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 
 * 根据剩余血量百分比触发另一个buff
 * 当触发另一个buff后，此buff消失
 * 
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_XuePercentTriggerOtherBuff extends Buff{

	/**
	 * 触发buff持续的时间
	 */
	private long LastingTime;

	public void setLastingTime(long lastingTime) {
		LastingTime = lastingTime;
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

	}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
		if(owner instanceof Player){
			Player p = (Player)owner;
			BuffTemplate_XuePercentTriggerOtherBuff bt = (BuffTemplate_XuePercentTriggerOtherBuff)this.getTemplate();
			if(bt.hpPercent > 0){
				if(p.getHp() <= p.getMaxHP()*bt.hpPercent/100){
					BuffTemplateManager btm = BuffTemplateManager.getInstance();
					if(btm != null){
						BuffTemplate btt = btm.getBuffTemplateByName(bt.triggerBuffName);
						if(btt != null){
							Buff buff = btt.createBuff(getLevel());
							if(buff != null){
								buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
								buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+LastingTime);
								buff.setCauser(p);
								p.placeBuff(buff);
								this.setInvalidTime(0);
							}
						}
					}
				}
			}
		}else if(owner instanceof Sprite){
			Sprite s = (Sprite)owner;
			BuffTemplate_XuePercentTriggerOtherBuff bt = (BuffTemplate_XuePercentTriggerOtherBuff)this.getTemplate();
			if(bt.hpPercent > 0){
				if(s.getHp() <= s.getMaxHP()*bt.hpPercent/100){
					BuffTemplateManager btm = BuffTemplateManager.getInstance();
					if(btm != null){
						BuffTemplate btt = btm.getBuffTemplateByName(bt.triggerBuffName);
						if(btt != null){
							Buff buff = btt.createBuff(getLevel());
							if(buff != null){
								buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
								buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+LastingTime);
								buff.setCauser(s);
								s.placeBuff(buff);
								this.setInvalidTime(0);
							}
						}
					}
				}
			}
		}
	}

}
