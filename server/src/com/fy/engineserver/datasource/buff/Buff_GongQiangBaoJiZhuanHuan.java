package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 攻击强度点数暴击点数转换
 *
 */
@SimpleEmbeddable
public class Buff_GongQiangBaoJiZhuanHuan extends Buff{

	int gongQiangB = 0;

	int criticalHitB = 0;
	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){}
	
	
	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){}
	
	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);
	
	}

}
