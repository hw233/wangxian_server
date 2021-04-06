package com.xuanzhi.tools.queue;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.xuanzhi.tools.ds.AvlTree;
import com.xuanzhi.tools.ds.AvlTree.TreeNode;

/**
 * 队列选择器，此实现考虑了同步锁的问题，采用了ReentrantLock，以提高效率。
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
 * 		
 * 
 * 		当一个新的队列被注册(register)时，会加入到selecting set中，
 * 		当一个队列有消息达到时(push)，会检查此队列，从selecting set中移到ready set中
 * 		当一个队列被注销（un register）时，会加入到cancelling set中。
 * 		当一个队列被放回时(returnToSelectingSet)，会根据queue中是否有消息选择会加入到selecting set中，还是ready set中。
 * 
 * 		select操作就是检测ready set是否有队列，有队列返回这些队列，并且清空readySet，无队列等待。
 * 
 * 		在cancelling set中的队列，在下一次select操作时，会彻底从selector中清除。
 * 		
 * 		这三个set外部是无法直接操作的。
 * 
 *      
 * 
 * </pre>
 */
public class RoundrobinQueueSelector extends AbstractQueueSelector implements Runnable{

	protected ArrayList<SelectableQueue> selectingList = new ArrayList<SelectableQueue>();
	
	protected LinkedList<SelectableQueue> readyList = new LinkedList<SelectableQueue>();
	
	protected ArrayList<SelectableQueue> cancellingList = new ArrayList<SelectableQueue>();
	
	final Lock readyListLock = new ReentrantLock();
	final Condition readySetNotEmpty = readyListLock.newCondition();
	
	final Lock selectingSetLock = new ReentrantLock();
	
	private boolean running = true;
	
	private long step = 10L;
	
	Thread thread ;
	
	/**
	 * 
	 * @param readyNum
	 * @param readyTimeout
	 */
	public RoundrobinQueueSelector(long step){
		this.step = step;
		if(step < 1) step = 1;
		
		thread = new Thread(this,"RoundrobinQueueSelector-Thread");
		thread.start();
	}
	
	/**
	 * 等待此Selector中，任何一个队列有消息到达，
	 * 如果某个队列在此操作前有消息已经到达，那么此方法立即返回。
	 * 否则将等待直到有消息到达，或者超时。
	 * 
	 * 此方法支持多个线程并发调用
	 * 
	 * timeout == 0时没有System.currentTimeMillis()的调用，在大量消息并发时，可以提高效率。
	 * 
	 * @param timeout 如果timeout为0 标识一直等待
	 * @return 有几个队列已经准备好，即有消息需要处理
	 */
	public SelectableQueue select(long timeout){
		if(timeout < 0) throw new java.lang.IllegalArgumentException("timeout < 0");
		
		if(readyList.size() > 0){
			readyListLock.lock();
			try{
				if(readyList.size() > 0){
					SelectableQueue sq =  readyList.removeFirst();
					return sq;
				}
			}finally{
				readyListLock.unlock();
			}
		}
		
		long now = 0;
		if(timeout > 0)
			now = System.currentTimeMillis();
		long l = now;

		readyListLock.lock();
		try{
			while(timeout == 0 || now - l < timeout){
				if(readyList.size() > 0){
					SelectableQueue sq =  readyList.removeFirst();
					return sq;
				}
				try{
					if(timeout == 0)
						readySetNotEmpty.await();
					else
						readySetNotEmpty.await(l + timeout - now,TimeUnit.MILLISECONDS);
				}catch(InterruptedException e){
					e.printStackTrace();
					return null;
				}
				
				if(readyList.size() > 0){
					SelectableQueue sq =  readyList.removeFirst();
					return sq;
				}
				
				if(wakeupFlag){
					wakeupFlag = false;
					return null;
				}
				if(timeout > 0)
					now = System.currentTimeMillis();
			}
		}finally{
			readyListLock.unlock();
		}
		return null;
	}
	
	/**
	 * 查看队列准备好的情况 
	 * @return
	 */
	public int selectNow(){
		return readyList.size();
	}
	
	private boolean wakeupFlag = false;
	public void wakeup(){
		readyListLock.lock();
		try{
			wakeupFlag = true;
			readySetNotEmpty.signal();
		}finally{
			readyListLock.unlock();
		}
	}
	
	public int size(){
		selectingSetLock.lock();
		try{
			int size = 0;
			SelectableQueue q[] = selectingList.toArray(new SelectableQueue[0]);
			for(int i = 0 ; i < q.length ; i++){
				size += q[i].size();
			}
		}finally{
			selectingSetLock.unlock();
		}
		
		readyListLock.lock();
		try{
			int size = 0;
			SelectableQueue q[] = readyList.toArray(new SelectableQueue[0]);
			for(int i = 0 ; i < q.length ; i++){
				size += q[i].size();
			}
			return size;
		}finally{
			readyListLock.unlock();
		}
	
	}
	
	protected void register(DefaultSelectableQueue queue){
		selectingSetLock.lock();
		try{
			if(cancellingList.contains(queue)){ 
				cancellingList.remove(queue);
			}
			if(selectingList.contains(queue) || readyList.contains(queue)) return;
			
			selectingList.add(queue);
			queue.inSelector = true;
		}finally{
			selectingSetLock.unlock();
		}
	}
	
	protected void unregister(DefaultSelectableQueue queue){
		selectingSetLock.lock();
		try{
			cancellingList.add(queue);
		}finally{
			selectingSetLock.unlock();
		}
	}
	
	protected void returnToSelector(DefaultSelectableQueue queue){
		selectingSetLock.lock();
		try{
			if(queue.inSelector == false && queue.isRegistered()){
				selectingList.add(queue);
				queue.inSelector = true;
			}
		}finally{
			selectingSetLock.unlock();
		}
	}
	
	protected void messagePushed(DefaultSelectableQueue queue){
		
	}

	public void run() {
		// TODO Auto-generated method stub
		while(running){
			try{
				Thread.sleep(step);

				ArrayList<DefaultSelectableQueue> al = new ArrayList<DefaultSelectableQueue>();

				selectingSetLock.lock();
				try{
					for(int i = 0 ; i < cancellingList.size() ;i++){
						DefaultSelectableQueue sq = (DefaultSelectableQueue)cancellingList.get(i);
						selectingList.remove(sq);
						sq.inSelector = false;
					}
					cancellingList.clear();
					
					
					int i = 0;
					while(i < selectingList.size()){
						DefaultSelectableQueue q = (DefaultSelectableQueue)selectingList.get(i);
						if(q.size() > 0){
							
							al.add(q);
							q.inSelector = false;
							
							SelectableQueue p = selectingList.remove(selectingList.size()-1);
							if(p != q){
								selectingList.set(i,p);
							}else{
								i++;
							}
						}else{
							i++;
						}
					}
				}finally{
					selectingSetLock.unlock();
				}
				
				readyListLock.lock();
				try{
					if(al.size() > 0)
						readyList.addAll(al);
					
					if(readyList.size() > 0){
						readySetNotEmpty.signalAll();
					}
				}finally{
					readyListLock.unlock();
				}
				
			}catch(Throwable e){
				e.printStackTrace(System.out);
			}
		}
	}

}
