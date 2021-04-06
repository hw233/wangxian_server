package com.xuanzhi.tools.queue;


/**
 * 先进先出队列
 * 
 *
 */
public interface Queue {
	
	/**
	 * 将对象加入到队列尾，
	 * 一般情况下，此方法立即返回，如果队列满了，就将队列头的挤出队列。
	 * @param object
	 * @return 如果队列头被挤出，返回这个对象。否则返回null
	 */
	public Object push(Object object);
	
	/**
	 * 将对象加入到队列尾，
	 * 一般情况下，此方法立即返回，
	 * 如果队列满了，就等待一段时间。
	 * @param object
	 * @param timeout 如果小于等于0，就一直等待
	 * @return true表示加入到队列尾部，否则没有加入
	 */
	public boolean push(Object object,long timeout);
	
	/**
	 * 取出队列头的对象，如果队列中没有对象，返回null
	 */
	public Object pop();

	/**
	 * 取出队列头的对象，如果队列中没有对象，就等待，
	 * 直到队列中有对象，或者超时
	 * @param timeout 如果小于等于0，就一直等到队列中有对象
	 */
	public Object pop(long timeout);

	/**
	 * 查看队列头的对象，但是此对象不从队列中移走，
	 * 如果队列中没有对象，返回null
	 */
	public Object peek();
	
	/**
	 * 队列中缓存的对象的个数
	 * @return
	 */
	public int size();
	
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
