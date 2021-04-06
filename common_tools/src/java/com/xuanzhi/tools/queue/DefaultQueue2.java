package com.xuanzhi.tools.queue;

import java.util.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 *
 */
		
public class DefaultQueue2 implements Queue {

		protected LinkedList<Object> list ;
		private long pushNum = 0;
		private long popNum = 0;
		
		private final ReentrantLock lock = new ReentrantLock();
		private final Condition condition = lock.newCondition();
		private long popWaitingCount = 0;
		
		public DefaultQueue2()
		{
			list = new LinkedList<Object>();
		}
		
		/**
		 * add one new object into the tailer of this queue.
		 * 
		 */
		public Object push(Object ob)
		{
			if(ob == null) return null;
			lock.lock();
			try{
				list.add(ob);
				pushNum++;
				
				if(popWaitingCount > 0){
					condition.signal();
				}
			}finally{
				lock.unlock();
			}
			return null;
		}
		
		/**
		 * 将对象加入到队列尾，
		 * 一般情况下，此方法立即返回，
		 * 如果队列满了，就等待一段时间。
		 * @param object
		 * @param timeout 如果小于等于0，就一直等待
		 * @return true表示加入到队列尾部，否则没有加入
		 */
		public boolean push(Object ob,long timeout){
			push(ob);
			return true;
		}
		
		
		/**
		 *  return the queue's size
		 */
		public int size()
		{
			return list.size();
		}
		
		/**
		 * clear
		 */
		public void clear()
		{
			pushNum = popNum = 0;
			list.clear();
		}
		
		/**
		 * return size() == 0
		 */
		public boolean isEmpty()
		{
			return size() == 0;
		}
		
		/**
		 *  return size() == getMaxSize()
		 */
		public boolean isFull()
		{
			return false;
		}
		
		/**
		 *  obtain the header of this queue
		 *  if no object in queue,it will return null.
		 */
		public Object pop()
		{
			if(list.size() == 0) return null;
			lock.lock();
			try{
				if(list.size() > 0)
				{
					this.popNum++;
					return list.remove(0);
				}else{
					return null;
				}
			}finally{
				lock.unlock();
			}
		}
		
		/**
		 *  obtain the header of this queue
		 *  if no object in queue,it will return null.
		 */
		public Object pop(long timeout)
		{
			if(timeout < 0) timeout = 0L;
			Object ret = null;
			lock.lock();
			this.popWaitingCount ++;
			try{
				if(list.size() > 0)
				{
					this.popNum++;
					return list.removeFirst();
				}else{
					long tt = 0L;
					
					while(ret == null){
						if(timeout > 0 && tt >= timeout){
							return null;
						}
		
						long st = System.currentTimeMillis();
						try{
							if(timeout == 0)
								condition.await();
							else
								condition.await(timeout - tt,TimeUnit.MILLISECONDS);
						}catch(Exception e){
							e.printStackTrace();
						}
						if(list.size() > 0){
							this.popNum++;
							ret = list.removeFirst();
							return ret;
						}
						tt += System.currentTimeMillis() - st;
					}
					return ret;
				}
			}finally{
				this.popWaitingCount --;
				lock.unlock();
			}
		}
		
		/**
		 *  obtain the header of this queue
		 *  if no object in queue,it will return null.
		 */
		public Object peek()
		{
			lock.lock();
			try{
				if(list.size() > 0){
					return list.getFirst();
				}else{
					return null;
				}
			}finally{
				lock.unlock();
			}
			
		}

		public long getPopNum() {
			return popNum;
		}

		public long getPushNum() {
			return pushNum;
		}
}

