package com.xuanzhi.tools.queue;

/**
 * 
 */
public interface SelectableQueue extends Queue {

	/**
	 * 队列中多少后消息才能被select
	 * @return
	 */
	public int getReadyNum();
	
	/**
	 * 队列中多少后消息才能被select，此值必须大于等于1
	 * @return
	 */
	public void setReadyNum(int num);
	
	/**
	 * 等队列中有消息，并且没有达到ReadyNum数量，
	 * 那么消息最多等待多长时间，时间到后，就可以被select
	 * @return
	 */
	public long getReadyTimeout();
	
	/**
	 * 等队列中有消息，并且没有达到ReadyNum数量，
	 * 那么消息最多等待多长时间，时间到后，就可以被select
	 * @return
	 */
	public void setReadyTimeout(long timeout);
	
	/**
	 * Retrieves the object upon which the push methods synchronize. 
	 * This is often useful in the implementation of adaptors that require a specific blocking mode to be maintained for a short period of time.
	 * @return push lock
	 */
	public Object pushLock();
	
	/**
	 * Tells whether or not this queue is currently registered with any selectors. 
	 * A newly-created queue is not registered.
	 * Due to the inherent delay between key cancellation and queue deregistration, 
	 * a queue may remain registered for some time after all of its keys have been cancelled. 
	 * A queue may also remain registered for some time after it is destoryed.
	 *  
	 * @return true if, and only if, this queue is registered
	 */
	public boolean isRegistered();
	
	/**
	 * un register this queue from the selector.
	 * the queue will be put into cancelling set and will be canceled next select operator.
	 * 
	 * if the queue is not register to any selector. the method just does nothing.
	 */
	public void unregister();
	
	/**
	 * register queue to selector. the queue will be put into selecting set.
	 * 
	 * if the queue is already register to this selector. the method just does nothing.
	 * if the queue is already register to other selector, the method will throw IllegalStateException
	 * @param sel
	 * @throws java.lang.IllegalStateException
	 */
	public void register(AbstractQueueSelector sel);
	
	/**
	 *  move the queue from ready set to selecting set.
	 *  most case to  call the method is all objects in queue had been poped and prepare to wait more other objects.
	 */
	public void returnToSelector();
	
	/**
	 * set attachment
	 * @param att
	 */
	public void setAttachment(Object att);
	
	/**
	 * get attachment
	 * @return
	 */
	public Object getAttachment();
}
