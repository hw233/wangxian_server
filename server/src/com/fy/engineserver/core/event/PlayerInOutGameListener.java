package com.fy.engineserver.core.event;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 玩家进入游戏，离开游戏监听器
 * 
 */
public interface PlayerInOutGameListener {

	/**
	 * 玩家进入某个游戏
	 * 
	 * @param game
	 * @param player
	 */
	public void enterGame(Game game,Player player);
	
	/**
	 * 玩家离开某个游戏
	 * 
	 * @param game
	 * @param player
	 */
	public void leaveGame(Game game,Player player);
}
