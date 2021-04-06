package com.xuanzhi.tools.queue;

public class DefaultSelectableQueue extends DefaultQueue 
	implements SelectableQueue {

	
	protected long id;
	protected long nextTimeout = System.currentTimeMillis();
	
	protected Object att;
	protected AbstractQueueSelector selector;
	
	protected int readyNum = 1;
	protected long readyTimeout = 0;
	
	protected boolean inSelector = false;
	
	public DefaultSelectableQueue(int size) {
		super(size);

	}

	/**
	 * 队列中多少后消息才能被select
	 * @return
	 */
	public int getReadyNum(){
		return readyNum;
	}
	
	/**
	 * 队列中多少后消息才能被select，此值必须大于等于1
	 * @return
	 */
	public void setReadyNum(int num){
		readyNum = num;
	}
	
	/**
	 * 等队列中有消息，并且没有达到ReadyNum数量，
	 * 那么消息最多等待多长时间，时间到后，就可以被select
	 * @return
	 */
	public long getReadyTimeout(){
		return readyTimeout;
	}
	
	/**
	 * 等队列中有消息，并且没有达到ReadyNum数量，
	 * 那么消息最多等待多长时间，时间到后，就可以被select
	 * @return
	 */
	public void setReadyTimeout(long timeout){
		readyTimeout = timeout;
	}
	
	public Object getAttachment() {
		return att;
	}

	public boolean isRegistered() {
		return (selector != null);
	}

	public Object pushLock() {
		return pushLock;
	}

	public void register(AbstractQueueSelector sel) {
		if(isRegistered() && this.selector != sel){
			throw new java.lang.IllegalStateException("queue alreay register to another selector.");
		}
		this.selector = sel;
		this.selector.register(this);
	}

	public void returnToSelector() {
		if(this.selector != null){
			this.selector.returnToSelector(this);
		}
	}

	public void setAttachment(Object att) {
		this.att = att;
	}

	public void unregister() {
		if(this.selector != null){
			this.selector.unregister(this);
			this.selector = null;
		}

	}

	/**
	 * 将消息放回到消息队列的头部，
	 * 注意：
	 *     消息放回到消息队列的循序是倒序的，即数组的尾部（下标大）先放入，数组的头部（下标小）后放入
	 *     
	 * @param objs
	 */
	public void pushBackToHeader(Object objs[],int offset,int len){
		synchronized(pushLock){
			for(int i = offset+len -1 ;i >= offset ; i--){
				if(objs[i] != null)
					list.add(0, objs[i]);
			}
		}
	}

	public Object push(Object object) {
		if(object == null) return null;
		synchronized(pushLock){
			Object ret = super.push(object);
			if(ret == null){
					if(this.selector != null)
						this.selector.messagePushed(this);
			}
			return ret;
		}
	}

	public boolean push(Object object, long timeout) {
		if(object == null) return false;
		synchronized(pushLock){
			boolean b = super.push(object,timeout);
			if(b){
				if(this.selector != null)
					this.selector.messagePushed(this);
			}
			return b;
		}
		
	}

}
