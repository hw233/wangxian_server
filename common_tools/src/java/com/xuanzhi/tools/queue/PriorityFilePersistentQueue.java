package com.xuanzhi.tools.queue;

import java.io.*;

import com.xuanzhi.tools.text.StringUtil;
import org.apache.log4j.Logger;

/**
 * 用文件队列实现优先级队列，每个优先级别对应一个文件队列。
 * 所以，请注意优先级别的数目。
 * 
 * 本实现要求，优先级从1开始，然后依次是2，3，4，。。。，n.
 * 1为优先级最底，n为优先级最高。n不能大于16.
 * 
 *
 */
public class PriorityFilePersistentQueue implements PriorityQueue {

	protected File dir;
	protected int maxFileNum;
	protected long fileCapacity;
	protected int maxPriority;
	
	protected AdvancedFilePersistentQueue queues[] = null;
	
	/**
	 * 构造函数
	 * @param maxPriority 最大优先级，不能超过16
	 * @param dir 目录名称，此目录必须事先创建好
	 * @param maxfileNumOfEachPriority 每个优先级别下，最多保存多少文件
	 * @param fileCapacityOfEachPriority 每个优先级别下，文件的最大容量
	 */
	public PriorityFilePersistentQueue(int maxPriority,File dir,
			int maxfileNumOfEachPriority,long fileCapacityOfEachPriority){
		if(dir == null || !dir.exists() || !dir.isDirectory()){
			throw new IllegalArgumentException("dir ["+dir+"] not exists.");
		}
		
		if(maxPriority < 1 || maxPriority > 16){
			throw new IllegalArgumentException("max priority ["+maxPriority+"] is invalid.");
		}
		
		long startTime = System.currentTimeMillis();
		this.maxPriority = maxPriority;
		this.dir = dir;
		this.maxFileNum = maxfileNumOfEachPriority;
		this.fileCapacity = fileCapacityOfEachPriority;
		
		queues = new AdvancedFilePersistentQueue[maxPriority];
	
		for(int i = 0 ; i < queues.length ; i++){
			File d = new File(dir,"pri_"+(i+1));
			if(!d.exists()){
				d.mkdir();
			}
			queues[i] = new AdvancedFilePersistentQueue(d,maxFileNum,fileCapacity);
		}
	}
	
	public boolean push(Serializable object, int priority) {
		if(priority < 1) priority = 1;
		if(priority > this.maxPriority) 
			priority = maxPriority;
		
		priority = priority - 1;
		while(priority >= 0){
			boolean b =  queues[priority].push(object);
			if(b){
				synchronized(this){
					notifyAll();
				}
				return true;
			}
			priority = priority - 1;
		}
		return false;
	}

	public Serializable pop() {
		for(int i = queues.length -1 ; i >=0 ; i--){
			if(queues[i].isEmpty()) continue;
			Serializable s = queues[i].pop();
			if(s != null)
				return s;
		}
		return null;
	}
	
	public Serializable pop(long timeout) {
		if(timeout < 0 ) timeout = 0;
		long endTime = System.currentTimeMillis() + timeout;
		
		Serializable ret = pop();
		if(ret != null){
			return ret;
		}
		
		while(true){
			long now = System.currentTimeMillis();
			if(timeout > 0 && now >= endTime){
				return null;
			}
			
			synchronized(this){
				try{
					if(timeout == 0)
						wait();
					else
						wait(endTime - now);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			ret = pop();
			if(ret != null){
				return ret;
			}
		}
	}

	public Serializable peek() {
		for(int i = queues.length -1 ; i >=0 ; i--){
			if(queues[i].isEmpty()) continue;
			Serializable s = queues[i].peek();
			if(s != null)
				return s;
		}
		return null;
	}

	public int size() {
		int s = 0;
		for(int i = queues.length -1 ; i >=0 ; i--){
			s += queues[i].size();
		}
		return s;
	}

	public int capacity() {
		int s = 0;
		for(int i = queues.length -1 ; i >=0 ; i--){
			s += queues[i].capacity();
		}
		return s;
	}
	
	public int elementNum(){
		int s = 0;
		for(int i = queues.length -1 ; i >=0 ; i--){
			s += queues[i].elementNum();
		}
		return s;
	}
	
	
	public int elementNumInMemQueue(){
		int s = -1;
		for(int i = queues.length -1 ; i >=0 ; i--){
			int t = queues[i].elementNumInMemQueue();
			if(t > 0){
				if(s == -1){
					s = 0;
				}
				s += t;
			}
		}
		return s;
	}
	
	public int pushNum(){
		int s = 0;
		for(int i = queues.length -1 ; i >=0 ; i--){
			s += queues[i].pushNum();
		}
		return s;
	}
	
	public int popNum(){
		int s = 0;
		for(int i = queues.length -1 ; i >=0 ; i--){
			s += queues[i].popNum();
		}
		return s;
	}

	public void clear() {
		for(int i = queues.length -1 ; i >=0 ; i--){
			queues[i].clear();
		}
	}

	public boolean isEmpty() {
		for(int i = queues.length -1 ; i >=0 ; i--){
			if(queues[i].isEmpty() == false)
				return false;
		}
		return true;
	}

	public boolean isFull() {
		for(int i = queues.length -1 ; i >=0 ; i--){
			if(queues[i].isFull() == false)
				return false;
		}
		return true;
	}
	
	public int size(int priority) {
		if(priority < 1) priority = 1;
		if(priority > this.maxPriority) 
			priority = maxPriority;
		
		priority = priority - 1;
		
		return queues[priority].size();
	}

	public int capacity(int priority) {
		if(priority < 1) priority = 1;
		if(priority > this.maxPriority) 
			priority = maxPriority;
		
		priority = priority - 1;
		return queues[priority].capacity();
	}
	
	public int elementNum(int priority){
		if(priority < 1) priority = 1;
		if(priority > this.maxPriority) 
			priority = maxPriority;
		
		priority = priority - 1;
		return queues[priority].elementNum();
	}
	
	public int elementNumInMemQueue(int priority){
		if(priority < 1) priority = 1;
		if(priority > this.maxPriority) 
			priority = maxPriority;
		
		priority = priority - 1;
		return queues[priority].elementNumInMemQueue();
	}
	

	public long pushNum(int priority){
		if(priority < 1) priority = 1;
		if(priority > this.maxPriority) 
			priority = maxPriority;
		
		priority = priority - 1;
		return queues[priority].pushNum();
	}
	
	public long popNum(int priority){
		if(priority < 1) priority = 1;
		if(priority > this.maxPriority) 
			priority = maxPriority;
		
		priority = priority - 1;
		return queues[priority].popNum();
	}
	
	public void clear(int priority) {
		if(priority < 1) priority = 1;
		if(priority > this.maxPriority) 
			priority = maxPriority;
		
		priority = priority - 1;
		queues[priority].clear();
	}
	
}
