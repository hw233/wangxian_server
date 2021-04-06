package com.xuanzhi.tools.queue;

import java.io.Serializable;

/**
 * 先进先出队列
 * 
 *
 */
public interface PriorityQueue {
	

	/**
	 * 将对象加入到队列尾，
	 * 一般情况下，此方法立即返回true，
	 * 当队列满的时候，返回false，对象没有放入队列中
	 * @param object
	 */
	public boolean push(Serializable object,int priority);
	
	/**
	 * 取出队列头的对象，如果队列中没有对象，返回null
	 */
	public Serializable pop();
	
	/**
	 * 取出队列头的对象，如果队列中没有对象，就等待，
	 * 直到队列中有对象，或者超时
	 * @param timeout 如果小于等于0，就一直等到队列中有对象
	 */
	public Serializable pop(long timeout);

	/**
	 * 查看队列头的对象，但是此对象不从队列中移走，
	 * 如果队列中没有对象，返回null
	 */
	public Serializable peek();
	
	/**
	 * 队列中缓存的对象的个数
	 * @return
	 */
	public int size();
	
	/**
	 * 得到队列中最大缓存对象的个数
	 * @return
	 */
	public int capacity();
	
	/**
	 * 清空队列
	 */
	public void clear();
	
	/**
	 * 判断队列是否为空
	 */
	public boolean isEmpty();
	
	/**
	 * 判断队列是否已满
	 * @return
	 */
	public boolean isFull();

}
