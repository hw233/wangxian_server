/**
 * 
 */
package com.fy.engineserver.downcity.city;

import java.util.Map;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.Monster;

/**
 * @author Administrator
 *
 */
public interface CityAction {
	
	public Map<Long, Player> pMap();
	
	/**
	 * 副本ID,查看副本log检索
	 */
	public int getId();
	
	/**
	 * 副本的名字
	 */
	public String getName();
	
	/**
	 * 副本是否结束
	 */
	public boolean isEnd();
	
	/**
	 * 副本心跳
	 */
	public void heartbeat();
	
	/**
	 * 销毁
	 */
	public boolean isDestroy();
	
	public void destory();
	
	/**
	 * 怪物被击杀
	 */
	public void killMonster(Monster m);
	
	/**
	 * 玩家在副本中被击杀
	 * 不同的副本，复活策略不一样
	 */
	public void killPlayer(Player p);
	
	public Game getGame();
	/**
	 * 玩家是否在副本中
	 */
	public boolean playerInGame(Player p);
}
