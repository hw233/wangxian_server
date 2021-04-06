package com.xuanzhi.tools.queue;

import java.util.*;

import com.xuanzhi.tools.ds.AvlTree;
import com.xuanzhi.tools.ds.AvlTree.TreeNode;

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
 * 		Selector内部有3个Hash Set：
 * 			selecting set 用于存放等待消息的队列
 * 			ready set 用于存放有消息到达的队列
 * 			cancelling set 用于暂存要从Selector中移走的队列
 * 		和一个AvlTree
 * 			preReadySet 用于准放准消息到达的队列
 * 
 * 		当一个新的队列被注册(register)时，会加入到selecting set中，
 * 		当一个队列有消息达到时(push)，会检查此队列，是从selecting set中移到ready set中还是从selecting set移动到preReadySet中
 * 		当一个队列被注销（un register）时，会加入到cancelling set中。
 * 		当一个队列被放回时(returnToSelectingSet)，会根据queue中是否有消息选择会加入到selecting set中，还是ready set中，还是preReadySet中。
 * 
 * 		select操作就是检测ready set是否有队列，有队列返回这些队列，并且清空readySet，无队列等待。
 * 
 * 		在cancelling set中的队列，在下一次select操作时，会彻底从selector中清除。
 * 		
 * 		这三个set外部是无法直接操作的。
 * 
 * 新功能：
 * 
 * 		增加了每个队列可以设置自己的ReadyNum和ReadyTimeout。
 * 
 * </pre>
 */
public class QueueSelector extends AbstractQueueSelector implements Runnable{

	protected Set<SelectableQueue> selectingSet = Collections.synchronizedSet(new HashSet<SelectableQueue>());
	
	protected Set<SelectableQueue> readySet = Collections.synchronizedSet(new HashSet<SelectableQueue>());
	
	protected Set<SelectableQueue> cancellingSet = Collections.synchronizedSet(new HashSet<SelectableQueue>());
	
	/**
	 * 每次select方法返回的最大队列个数，此值必须大于0 
	 */
	protected int maxSelectReadyQueueNum = 1;
	
	protected int readyNum = 1;
	protected long readyTimeout = 0;
	Thread timeThread = null;
	
	protected AvlTree preReadySet = new AvlTree(new Comparator(){

		public int compare(Object o1, Object o2) {
			DefaultSelectableQueue q1 = (DefaultSelectableQueue)o1;
			DefaultSelectableQueue q2 = (DefaultSelectableQueue)o2;
			if(q1 == q2) return 0;
			if(q1.nextTimeout < q2.nextTimeout) return -1;
			if(q1.nextTimeout > q2.nextTimeout) return 1;
			if(q1.id < q2.id) return -1;
			if(q1.id > q2.id) return 1;
			return 0;
			
		}});
	
	public QueueSelector(){
		this(1,0);
	}
	
	/**
	 * 
	 * @param readyNum
	 * @param readyTimeout
	 */
	public QueueSelector(int readyNum,long readyTimeout){
		this.readyNum = readyNum;
		this.readyTimeout = readyTimeout;
		
		timeThread = new Thread(this,"QueueSelector-Timer-Thread");
		timeThread.start();
		
		
	}
	/**
	 *  取得ReadySet的一份拷贝。返回的Set中的Queue已经从Selector取出，
	 *  必须调用Queue的returnToSelector才能使得Queue放回到Selector中。
	 * 
	 * @return
	 */
	protected Collection<SelectableQueue> getReadySet(){
		synchronized(readySet){
			Collection<SelectableQueue> ss = new ArrayList<SelectableQueue>();
			
			SelectableQueue queues[] = readySet.toArray(new SelectableQueue[0]);
			readySet.clear();
			
			if(queues.length <= maxSelectReadyQueueNum){
				for(int i = 0 ; i < queues.length ; i++){
					ss.add(queues[i]);
				}
			}else{
				for(int i = 0 ; i < maxSelectReadyQueueNum ; i++){
					ss.add(queues[i]);
				}
				for(int i = maxSelectReadyQueueNum ; i < queues.length ; i++){
					readySet.add(queues[i]);
				}
			}
			return ss;
		}
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
	public synchronized Collection<SelectableQueue> select(long timeout){
		if(timeout < 0) throw new java.lang.IllegalArgumentException("timeout < 0");
		
		if(cancellingSet.size() > 0){
			synchronized(cancellingSet){
				if(cancellingSet.size() > 0){
					Iterator<SelectableQueue> it = cancellingSet.iterator();
					while(it.hasNext()){
						SelectableQueue sq = it.next();
						this.selectingSet.remove(sq);
						this.readySet.remove(sq);
					}
					cancellingSet.clear();
				}
			}
		}
		
		if(readySet.size() > 0){
			Collection<SelectableQueue> ss = getReadySet();
			if(readySet.size() > 0){
				notify();
			}
			return ss;
		}
		long now = System.currentTimeMillis();
		long l = now;
		while(timeout == 0 || now - l < timeout){
			if(readySet.size() > 0){
				Collection<SelectableQueue> ss = getReadySet();
				if(readySet.size() > 0){
					notify();
				}
				return ss;
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
			if(readySet.size() > 0){
				Collection<SelectableQueue> ss = getReadySet();
				if(readySet.size() > 0){
					notify();
				}
				return ss;
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
		return Math.min(maxSelectReadyQueueNum, readySet.size());
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
		SelectableQueue q[] = selectingSet.toArray(new SelectableQueue[0]);
		for(int i = 0 ; i < q.length ; i++){
			size += q[i].size();
		}
		q = readySet.toArray(new SelectableQueue[0]);
		for(int i = 0 ; i < q.length ; i++){
			size += q[i].size();
		}
		return size;
	}
	
	protected void register(DefaultSelectableQueue queue){
		if(cancellingSet.contains(queue)){ 
			cancellingSet.remove(queue);
		}
		if(selectingSet.contains(queue) || readySet.contains(queue)) return;
		
		selectingSet.add(queue);
		
		if(queue.getReadyNum() < this.readyNum){
			queue.setReadyNum(this.readyNum);
			queue.setReadyTimeout(this.readyTimeout);
		}
		//logger.put(queue, new LinkedList<String>());
		//logger.get(queue).add("register queue["+queue.getAttachment()+","+queue.size()+"] and add to selectingSet");
	}
	
	protected void unregister(DefaultSelectableQueue queue){
		cancellingSet.add(queue);
	}
	
	protected boolean isRegister(DefaultSelectableQueue queue){
		if(selectingSet.contains(queue) || readySet.contains(queue)) return true;
		return false;
	}
	
	protected void returnToSelector(DefaultSelectableQueue queue){
		if(queue.isRegistered() == false) return;
		synchronized(queue.pushLock()){
			if(queue.size() == 0){
				selectingSet.add(queue);
				
	//			logger.get(queue).add("add queue["+queue.getAttachment()+","+queue.size()+"] to selectingSet in returnToSelector");
	//			if(queue.size() != 0){
	//				System.out.println("error:queue[" + queue.getAttachment()+","+queue.size()+"] returnToSelector to selectingSet but queue.size()="+queue.size());
	//			}
			}else{
				if(queue.size() >= queue.getReadyNum()){
					readySet.add(queue);
		//			logger.get(queue).add("add queue["+queue.getAttachment()+","+queue.size()+"] to readySet in returnToSelector");
					synchronized(this){
						notify();
					}
				}else{
					selectingSet.add(queue);
			//		logger.get(queue).add("add queue["+queue.getAttachment()+","+queue.size()+"] to selectingSet in returnToSelector and ...");
					synchronized(preReadySet){
						if(queue.getReadyTimeout() > 0){
							queue.nextTimeout = System.currentTimeMillis() + queue.getReadyTimeout();
						}else{
							queue.nextTimeout = System.currentTimeMillis() + 60000L;
						}
						
						preReadySet.insert(queue);
		//				logger.get(queue).add("add queue["+queue.getAttachment()+","+queue.size()+"] to preReadySet("+preReadySet.size()+","+preReadySet.find(queue)+") in returnToSelector");
						if(preReadySet.size() == 1)
							preReadySet.notify();
					}
				}
			}
		}
	}
	
	//Hashtable<DefaultSelectableQueue,LinkedList<String>> logger = new Hashtable<DefaultSelectableQueue,LinkedList<String>>(); 
	
	protected void messagePushed(DefaultSelectableQueue queue){
		if(queue.isRegistered() == false) return;
		if(!selectingSet.contains(queue)){
		//	logger.get(queue).add("just return in messagePushed cause queue["+queue.getAttachment()+","+queue.size()+"] not in selectingSet");
			return;
		}
		if(queue.size() >= queue.getReadyNum()){
			selectingSet.remove(queue);
		//	logger.get(queue).add("remove queue["+queue.getAttachment()+","+queue.size()+"] from selectingSet in messagePushed");
			if(queue.getReadyNum() > 1){
				synchronized(preReadySet){
					preReadySet.remove(queue);
			//		logger.get(queue).add("remove queue["+queue.getAttachment()+","+queue.size()+"] from preReadySet("+preReadySet.size()+","+preReadySet.find(queue)+") in messagePushed");
					preReadySet.notify();
				}
			}
			readySet.add(queue);
		//	logger.get(queue).add("add queue["+queue.getAttachment()+","+queue.size()+"] to readySet in messagePushed");
			synchronized(this){
				notify();
			}
		}else if(queue.size() > 0){
			synchronized(preReadySet){
				TreeNode tn = preReadySet.find(queue);
				if(tn == null){
					if(readyTimeout > 0){
						queue.nextTimeout = System.currentTimeMillis() + queue.getReadyTimeout();
					}else{
						queue.nextTimeout = System.currentTimeMillis() + 60000L;
					}
					preReadySet.insert(queue);
					
			//		logger.get(queue).add("add queue["+queue.getAttachment()+","+queue.size()+"] to preReadySet("+preReadySet.size()+","+preReadySet.find(queue)+") in messagePushed");
					
					if(preReadySet.size() == 1)
						preReadySet.notify();
				}else{
					
			//		logger.get(queue).add("do nothing queue["+queue.getAttachment()+","+queue.size()+"] already in preReadySet("+preReadySet.size()+","+preReadySet.find(queue)+") in messagePushed");
					
			//		if(queue.size() == 1){
			//			System.out.println("error:queue[" + queue.getAttachment()+"] messagePushed but finded in preReadySet and queue.size()="+queue.size());
			//		}
				}
			}
		}else{
		//	System.out.println("error:queue[" + queue.getAttachment()+"] messagePushed but queue.size()="+queue.size());
		}
	}

	public void run() {
		
		while(Thread.currentThread().isInterrupted() == false){
			try{
				DefaultSelectableQueue dq = null;
				synchronized(preReadySet){
					TreeNode tn = preReadySet.minimum();
					if(tn != null){
						dq = (DefaultSelectableQueue)tn.getObject();
					}
				}
				if(dq == null){
					synchronized(preReadySet){
						preReadySet.wait();
					}
				}else if(dq.isRegistered()){
					long now = System.currentTimeMillis();
					if(dq.size() >= dq.getReadyNum() || dq.nextTimeout <= now){
						synchronized(dq.pushLock()){
							boolean b = false;
							synchronized(preReadySet){
								TreeNode tn = preReadySet.minimum();
								if(tn != null && tn.getObject() == dq){
									preReadySet.remove(dq);
			//						logger.get(dq).add("remove queue["+dq.getAttachment()+","+dq.size()+"] from preReadySet("+preReadySet.size()+","+preReadySet.find(dq)+") in timer");
									b = true;
								}
							}
							if(b){
								selectingSet.remove(dq);
				//				logger.get(dq).add("remove queue["+dq.getAttachment()+","+dq.size()+"] from selectingSet in timer");
								readySet.add(dq);
					//			logger.get(dq).add("add queue["+dq.getAttachment()+","+dq.size()+"] to readySet in timer");
								synchronized(this){
									notify();
								}
							}
						}
					}else{
						synchronized(preReadySet){
							preReadySet.wait(dq.nextTimeout - now);
						}
					}
			}else{
					synchronized(preReadySet){
						preReadySet.remove(dq);
			//			System.out.println("error remove queue: " + dq.getAttachment());
					}
				}
			}catch(Throwable e){
				System.err.println("["+new Date()+"] [IN "+Thread.currentThread().getName()+"], catch exception:");
				e.printStackTrace();
			}
		}
		
	}

	public int getMaxSelectReadyQueueNum() {
		return maxSelectReadyQueueNum;
	}

	public void setMaxSelectReadyQueueNum(int maxSelectReadyQueueNum) {
		this.maxSelectReadyQueueNum = maxSelectReadyQueueNum;
	}
}
