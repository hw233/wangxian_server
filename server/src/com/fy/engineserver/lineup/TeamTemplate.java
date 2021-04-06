package com.fy.engineserver.lineup;

import java.util.List;

/**
 * 组合模板接口
 * 
 *
 */
public interface TeamTemplate {
	
	/**
	 * 加入这个组合，如果可以加入，返回true
	 * @param dcplayer
	 * @return
	 */
	public boolean takein(LineupPlayer luplayer);
	
	/**
	 * 这个队是否满了
	 * @return
	 */
	public boolean isFull();
	
	/**
	 * 返回组合中的玩家
	 * @return
	 */
	public List<LineupPlayer> getTeamplayers();
	
	
}
