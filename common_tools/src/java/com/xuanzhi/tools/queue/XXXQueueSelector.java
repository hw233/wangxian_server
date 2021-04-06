package com.xuanzhi.tools.queue;


/**
 * 队列选择器
 * <pre>
 * 		此选择器实现的目的主要是解决这样的问题：
 * 			现有一组队列，希望用一个线程来处理这一组队列，当没有任何消息时，
 * 			此线程等待，当有一个队列或者几个队列有消息到达时，则通知此线程
 * 			有消息到达。并且可以获得是哪些队列有消息到达。
 * 		
 * 		这种问题非常类似SocketChannel和Selector的方式。
 * 
 * 		具体设计是这样的：
 * 
 * </pre>
 */
public class XXXQueueSelector {

	public static final int BIT = 4;
	public static final int BITNUM = 1<<BIT;
	
	protected int maxQueueNum = 1;
	
	protected XXXSelectableQueue queues[];
	protected int xxxCount[][];
	
	private int layerNum = 0;
	
	public XXXQueueSelector(int maxQueueNum){
		if(maxQueueNum < (1<<BIT)) maxQueueNum = (1<<BIT);
		this.maxQueueNum = maxQueueNum;
		queues = new XXXSelectableQueue[maxQueueNum];
		
		
		int c = 0;
		int k = 1;
		while(true){
			k = k << BIT;
			c++;
			if(k >= maxQueueNum){
				break;
			}
		}
		layerNum = c+1;
		xxxCount = new int[c+1][];
		xxxCount[c] = new int[maxQueueNum];
		
		for(int i = 0 ; i < c ; i++){
			xxxCount[i] = new int[(1<<(i*BIT))];
		}
	}
	/**
	 * 
	 * @param layer
	 * @param index
	 * @return
	 */
	private int find(){
		int layer = 0;
		int index = 0;
		while(xxxCount[layer][index] > 0){
			
			//查看下一层对应的单元，找到一个存在消息的队列
			int r = -1;
			for(int i = 0 ; i < BITNUM ; i++){
				if(xxxCount[layer+1][index*BITNUM + i] > 0){
					r= index*BITNUM + i;
					break;
				}
			}
			if(r == -1){
				xxxCount[layer][index] = 0;
				return -1;
			}
			
			layer++;
			index=r;
			
			if(layer == layerNum -1){
				
				//向上清空各个对应层上的位置
				int k = index>>BIT;
				for(int i = layer -1; i >= 0 ; i--){
					xxxCount[i][k]--;
					k = k >>BIT;
				}
				
				if(queues[index] != null){
					xxxCount[layer][index] = -1;//标识队列被取出去了
					return index;
				}else{
					xxxCount[layer][index] = 0; //队列不存在或者没有消息
					return -1;
				}
			}
		}
		return -1;
	}
	/**
	 * 等待此Selector中，任何一个队列有消息到达，
	 * 如果某个队列在此操作前有消息已经到达，那么此方法立即返回。
	 * 否则将等待直到有消息到达，或者超时。
	 * 
	 * 此方法支持多个线程并发调用
	 * 
	 * @param timeout 如果timeout为0 标识一直等待
	 * @return 有几个队列已经准备好，即有消息需要处理
	 */
	public synchronized XXXSelectableQueue select(long timeout){
		if(timeout < 0) throw new java.lang.IllegalArgumentException("timeout < 0");
		
		int r = find();
		if(r >= 0){
			return queues[r];
		}
		
		long now = System.currentTimeMillis();
		long l = now;
		while(timeout == 0 || now - l < timeout){
			r = find();
			if(r >= 0){
				return queues[r];
			}
			try{
				if(timeout == 0)
					wait();
				else
					wait(l + timeout - now);
			}catch(InterruptedException e){
				e.printStackTrace();
				return null;
			}
			r = find();
			if(r >= 0){
				return queues[r];
			}
			
			if(wakeupFlag){
				wakeupFlag = false;
				return null;
			}
			now = System.currentTimeMillis();
		}
		return null;
	}
	
	/**
	 * 查看队列准备好的情况 
	 * @return
	 */
	public int selectNow(){
		return xxxCount[0][0];
	}
	private boolean wakeupFlag = false;
	
	public void wakeup(){
		wakeupFlag = true;
		synchronized(this){
				notify();
			}
	}
	
	public int size(){
		int size = 0;
		
		for(int i = 0 ; i < queues.length ; i++){
			if(queues[i] != null)
				size += queues[i].size();
		}
	
		return size;
	}
	
	protected void register(XXXSelectableQueue queue){
		
		for(int i = 0 ; i < queues.length ; i++){
			if(queues[i] == null){
				queues[i] = queue;
				queues[i].indexOf = i;
				return;
			}
		}
		throw new RuntimeException("no empty space for new regist queue. please increase max queue size.");
	}
	
	protected void unregister(XXXSelectableQueue queue){
		if(queue.indexOf >= 0){
			if(queues[queue.indexOf] == queue){
				queues[queue.indexOf] = null;
				queue.indexOf = -1;
			}
		}
	}
	
	protected boolean isRegister(XXXSelectableQueue queue){
		if(queue.indexOf >= 0){
			if(queues[queue.indexOf] == queue){
				return true;
			}
		}
		return false;
	}
	
	protected void returnToSelector(XXXSelectableQueue queue){
		if(isRegister(queue) == false) return;
		
		if(queue.size() > 0){
			xxxCount[layerNum-1][queue.indexOf] = 1;
			//向上清空各个对应层上的位置
			int k = queue.indexOf>>BIT;
			for(int i = layerNum - 2; i >= 0 ; i--){
				xxxCount[i][k]++;
				k = k >>BIT;
			}
			synchronized(this){
				notify();
			}
		}else{
			xxxCount[layerNum-1][queue.indexOf] = 0;
		}
		
	}
	
	protected void messagePushed(XXXSelectableQueue queue){
		if(isRegister(queue) == false) return;
		
		if(queue.size() > 0 && xxxCount[layerNum-1][queue.indexOf] == 0){
			xxxCount[layerNum-1][queue.indexOf] = 1;
			//向上清空各个对应层上的位置
			int k = queue.indexOf>>BIT;
			for(int i = layerNum - 2; i >= 0 ; i--){
				xxxCount[i][k]++;
				k = k >>BIT;
			}
			synchronized(this){
				notify();
			}
		}
	}


}
