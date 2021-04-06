package com.xuanzhi.tools.mmotest;

/**
 * 代表着一个路径，一个生物会沿着这个路径，以一定的速率移动
 * 路经是空间多个点形成的一个折线。
 * 
 * @author <a href='myzdf.bj@gmail.com'>yugang.wang</a>
 *
 */
public interface MoveTraceInterface {

	public void heartbeat(float deltaT);
}
