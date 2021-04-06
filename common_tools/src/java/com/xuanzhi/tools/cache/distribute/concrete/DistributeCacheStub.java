package com.xuanzhi.tools.cache.distribute.concrete;

import java.net.*;
import java.util.HashMap;
import com.xuanzhi.tools.cache.Cacheable;
/**
 * 分布式Cache的一个存根，此存根对应其他一台机器上的一个分布式Cache，
 * 并记录了部分这个分布式Cache中包含了哪些对象。<p>
 * 
 * 这个记录是有限制了，如果一旦超过限制，会按照LRU的原则，
 * 丢弃一些很久没有使用的Key。<p>
 *
 * 目前这一限制是key的个数和没有被使用的时间。<p>
 * 
 *
 */
public class DistributeCacheStub {

	protected DistributeCacheGroup group;
	protected DistributeCacheManagerStub managerStub;
	protected int max_key_num = 8192;
	protected long max_life_time = 1800000L;
	
	protected HashMap<Object,LinkedListNode> keys = new HashMap<Object,LinkedListNode>();
	protected LinkedList accessList = new LinkedList();
	
	/**
	 * 构造函数
	 * @param ip 对应的机器的Ip
	 * @param maxKeyNum 最多可以存放多少个Key
	 * @param maxLifeTime 每个Key最多可以存活的时间，按毫秒计算
	 */
	public DistributeCacheStub(DistributeCacheManagerStub managerStub,int maxKeyNum,long maxLifeTime){
		this.managerStub = managerStub;
		this.max_key_num = maxKeyNum;
		this.max_life_time = maxLifeTime;
	}
	
	public void setDistributeCacheGroup(DistributeCacheGroup group){
		this.group = group;
	}
	
	public DistributeCacheGroup getGroup(){
		return group;
	}
	
	public InetAddress getInetAddress(){
		return managerStub.getInetAddress();
	}
	
	public void setMaxKeyNum(int n){
		this.max_key_num = n;
	}
	
	public int getMaxKeyNum(){
		return this.max_key_num;
	}
	
	public void setMaxLifeTime(long l){
		this.max_life_time = l;
	}
	
	public long getMaxLifeTime(){
		return max_life_time;
	}
	
	public int getKeyNum(){
		return keys.size();
	}
	
	public synchronized boolean contains(Object key){
		boolean b = keys.containsKey(key);
		if(b){
			LinkedListNode node = keys.get(key);
			if(node != null){
				node.remove();
				accessList.addFirst(node);
				node.timestamp = System.currentTimeMillis();
			}
			
			check();
		}
		return b;
	}

	public java.util.Set keySet(){
		return java.util.Collections.unmodifiableSet(keys.keySet());
	}
	/**
	 * 增加一个Key，表明本台机器收到了一条来自远程机器发来的通知，
	 * 通知其cache了一个新的对象
	 * @param key
	 */
	public synchronized void addKey(Object key){
		if(keys.containsKey(key)){
			LinkedListNode node = keys.get(key);
			if(node != null){
				node.remove();
				accessList.addFirst(node);
				node.timestamp = System.currentTimeMillis();
			}
		}else{
			LinkedListNode node = new LinkedListNode(key,null,null);
			accessList.addFirst(node);
			node.timestamp = System.currentTimeMillis();
			keys.put(key,node);
		}
		check();
	}
	
	public synchronized void removeKey(Object key){
		LinkedListNode node = keys.remove(key);
		if(node != null){
			node.remove();
		}
	}
	
	public synchronized void clear(){
		keys.clear();
		accessList.clear();
	}
	
	/**
	 * 检查是否有key超时，或者是否已经超过个数限制
	 */
	protected void check(){
		deleteExpiredKeys();
		cullCache();
	}
	
	protected void deleteExpiredKeys(){
		if(this.max_life_time <= 0)
			return;
		
		long expireTime = System.currentTimeMillis() - this.max_life_time;
		LinkedListNode node = accessList.getLast();
		if(node == null)
			return;
		while(expireTime > node.timestamp){
			node.remove();
			keys.remove(node.object);
			node = accessList.getLast();
			if(node == null)
				break;
		}
	}
	
	protected void cullCache(){
		if(this.max_life_time <= 0)
			return;
		
		if(keys.size() > max_life_time * 0.97){
			LinkedListNode node = accessList.getLast();
			if(node == null)
				return;
			int desiredSize = (int)(max_life_time * .90);
			while(keys.size() > desiredSize){
				node.remove();
				keys.remove(node.object);
				node = accessList.getLast();
				if(node == null)
					break;
			}
		}
	}
	
	public String toString(){
		return getInetAddress()+"/" + group.getName();
	}
	
}
