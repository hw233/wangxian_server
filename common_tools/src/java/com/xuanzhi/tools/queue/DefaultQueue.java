package com.xuanzhi.tools.queue;

import java.util.*;

/**
 * 
 *
 */
		
public class DefaultQueue implements Queue {

	
		private int size = 0;
		protected List list ;
		private long pushNum = 0;
		private long popNum = 0;
		protected Object pushLock = new Object(){};
		protected Object popLock = new Object(){};
		
		public DefaultQueue(int size)
		{
			if(size < 0 ) throw new IllegalArgumentException("create queue fail cause size < 0");  	
			this.size = size;
			list = Collections.synchronizedList(new LinkedList());
		}
		
		/**
		 * add one new object into the tailer of this queue.
		 * 
		 */
		public Object push(Object ob)
		{
			if(ob == null) return null;
			Object ret = null;
			synchronized(pushLock){
				if(list.size() < size)
				{
					list.add(ob);
				}
				else
				{
					ret = list.remove(0);
					list.add(ob);
				}
				pushNum++;
			}
			
			if(ret == null){
				synchronized(popLock){
					popLock.notify();
				}
			}
			return ret;
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
			if(ob == null) return false;
			if(timeout < 0) timeout = 0L;
			boolean b = false;
			synchronized(pushLock){
				if(list.size() < size)
				{
					list.add(ob);
					b = true;
				}else{
					long tt = 0L;
					while(b == false){
						if(timeout > 0 && tt >= timeout){
							return false;
						}
						long st = System.currentTimeMillis();
						try{
							if(timeout == 0)
								pushLock.wait();
							else
								pushLock.wait(timeout - tt);
						}catch(Exception e){
							e.printStackTrace();
						}
						if(list.size() < size){
							list.add(ob);
							b = true;
						}
						tt += System.currentTimeMillis() - st;
					}
				}
			}
			if(b){
				synchronized(popLock){
					pushNum++;
					popLock.notify();
				}
			}
			return b;
		}
		
		
		/**
		 * iterator this queue from header to tailer.
		 * the header is the last object putted.
		 */
		public Iterator iterator()
		{
			return new Iterator()
			{
				List l = list;
				int s = l.size();
				
				public boolean hasNext()
				{
					if( s > 0 && l.size() > 0)
						return true;
					return false;
				}
				
				public Object next()
				{
					if (l.size() > 0)
					{
						if (s > l.size())
						{
							s = l.size();
						}
						
						--s;
						Object o = l.get(s);
						return o;
					}
					throw new ArrayIndexOutOfBoundsException();
				}
				
				public void remove()
				{
					if (s >= 0 && l.size() > s)
					{
						list.remove(s);
					}
				}
			};
		}
		
		/**
		 * interator this queue from tailer to header
		 * the header is the last object putted.
		 */
		public Iterator invert_iterator()
		{
			return list.iterator();
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
		 *  return the max size of queue
		 */
		public int getMaxSize()
		{
			return size;
		}
		
		public void setMaxSize(int s){
			size = s;
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
			return size() == getMaxSize();
		}
		
		/**
		 *  obtain the header of this queue
		 *  if no object in queue,it will return null.
		 */
		public Object pop()
		{
			Object o = null;
			synchronized(popLock){
				if(list.size() > 0)
				{
					o = list.remove(0);
				}
			}
			if(o != null){
				synchronized(pushLock){
					popNum++;
					pushLock.notify();
				}
			}
			return o;
			
		}
		
		/**
		 *  obtain the header of this queue
		 *  if no object in queue,it will return null.
		 */
		public Object pop(long timeout)
		{
			if(timeout < 0) timeout = 0L;
			Object ret = null;
			synchronized(popLock){
				if(list.size() > 0){
					ret = list.remove(0);
				}else{
					long tt = 0L;
					
					while(ret == null){
						if(timeout > 0 && tt >= timeout){
							return null;
						}
		
						long st = System.currentTimeMillis();
						try{
							if(timeout == 0)
								popLock.wait();
							else
								popLock.wait(timeout - tt);
						}catch(Exception e){
							e.printStackTrace();
						}
						if(list.size() > 0){
							ret = list.remove(0);
						}
						tt += System.currentTimeMillis() - st;
					}
				}
			}
			if(ret != null){
				synchronized(pushLock){
					popNum++;
					pushLock.notify();
				}
			}
			return ret;
		}
		
		/**
		 *  obtain the header of this queue
		 *  if no object in queue,it will return null.
		 */
		public Object peek()
		{
			synchronized(popLock){
			if(list.size() > 0)
			{
				return list.get(0);
			}
			else
			{
				return null;
			}
			}
		}

		public long getPopNum() {
			return popNum;
		}

		public long getPushNum() {
			return pushNum;
		}
}

