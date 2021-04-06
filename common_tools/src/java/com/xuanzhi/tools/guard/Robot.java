package com.xuanzhi.tools.guard;

/**
 *
 * 
 */
public interface Robot {
	
	public static final int STATE_RUNNING = 0;
	public static final int STATE_STOP = 1;
	
	public static final String[] STATE_DESP = new String[]{"进行中", "停止"};
	
	/**
	 * 机器人的名字
	 * @return
	 */
	public String getName();
	
	/**
	 * 当前状态, 0-进行中, 1-结束
	 * @return
	 */
	public int getState();
	
	/**
	 * 正在进行的操作
	 * @return
	 */
	public String getAction();
	
	/**
	 * 执行命令的序列
	 * @return
	 */
	public long getSequence();
	
	/**
	 * 上一次执行操作的时间
	 * @return
	 */
	public long getLastActionTime();
	
	/**
	 * 得到创建时间
	 */
	public long getCreateTime();
}
