package com.xuanzhi.tools.mmotest;

import com.xuanzhi.tools.transport2.Connection2;

/**
 * 代表一个玩家
 * 
 * 有玩家最基本的信息
 * 
 * @author <a href='myzdf.bj@gmail.com'>yugang.wang</a>
 *
 */
public interface PlayerInterface extends LivingObjectInterface{

	/**
	 * 玩家对应的网络链接，每个玩家都需要一个链接。
	 * 
	 * @return
	 */
	public Connection2 getConnection();
}
