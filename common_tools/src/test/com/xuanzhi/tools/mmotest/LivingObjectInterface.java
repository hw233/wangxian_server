package com.xuanzhi.tools.mmotest;

/**
 * 表达一个游戏中的生物，
 * 可以是一个NPC，也可以是一个怪，也可以是一个玩家。
 * NPC中还可以是一个人，一扇门，一个箱子，一个可以采集的草等等。
 * 
 * 生物最基本的特性包含在这个接口中。
 * 
 * 比如：
 * 1. 位置信息
 * 2. 是否需要继续存留，如果不需要，系统将在下一个心跳将其从场景中清除。
 * 3. 心跳函数等。
 * 4. 类型等
 * 5. 定义AroundChange类型的变量
 * 6. 定义SelfChange类型的变量
 * 
 * @author <a href='myzdf.bj@gmail.com'>yugang.wang</a>
 *
 */
public interface LivingObjectInterface {

	/**
	 * 生物的名称
	 * @return
	 */
	public String getName();
	
	/**
	 * 是否存活。如果为false，系统将在下一个心跳将其从场景中清除。
	 * @return
	 */
	public boolean isAlive();
	
	/**
	 * 生物的心跳函数
	 * @param deltaT
	 */
	public void heartbeat(float deltaT);
}
