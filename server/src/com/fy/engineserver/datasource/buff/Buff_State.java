package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 单纯靠名字区分的buff，如囚禁，禁言
 * 
 * 
 *
 */
@SimpleEmbeddable
public class Buff_State extends Buff{

	/**
	 * 通知此buff，生命开始，此方法调用后，才会调用心跳函数
	 */
	public void start(Fighter owner){

	}

	/**
	 * 通知此buff，生命结束，此方法调用后，就不会再调用心跳函数了
	 */
	public void end(Fighter owner){
		if(owner instanceof Player){
			Player p = (Player)owner;
			if(CountryManager.囚禁buff名称.equals(getTemplateName())){
				if(p.isInPrison){
					p.isInPrison = false;
				}
			}
		}
	}

	/**
	 * 心跳函数
	 */
	public void heartbeat(Fighter owner,long heartBeatStartTime, long interval, Game game){
		super.heartbeat(owner, heartBeatStartTime, interval, game);

		if(owner instanceof Player){
			Player p = (Player)owner;
			if(CountryManager.囚禁地图名称.equals(game.gi.name) && CountryManager.囚禁buff名称.equals(getTemplateName())){
				if(!p.isInPrison){
					p.isInPrison = true;
				}
			}
		}
	}

}
