package com.xuanzhi.tools.cache.distribute.concrete;

import java.net.InetAddress;
import java.util.*;


import com.xuanzhi.tools.cache.distribute.*;
import com.xuanzhi.tools.cache.Cacheable;

/**
 * CacheGroup对应本地一个分布式Cache和一组分布式Cache存根，
 * 所有这些分布式Cache和分布式Cache存根都有相同的名字。
 * 
 *
 */
public class DistributeCacheGroup {

	protected DistributeCache cache;
	protected DefaultDistributeCacheManager manager;
	protected List<DistributeCacheStub> stubs = Collections.synchronizedList(new ArrayList<DistributeCacheStub>());
	
	protected DistributeCacheGroup(DefaultDistributeCacheManager manager,DistributeCache cache){
		this.cache = cache;
		this.manager = manager;
	}
	
	public DistributeCache getCache(){
		return cache;
	}
	public String getName(){
		return cache.getName();
	}
	
	public void addStub(DistributeCacheStub stub){
		boolean b = false;
		for(int i = 0 ; b==false && i < stubs.size() ; i++){
			DistributeCacheStub s = stubs.get(i);
			if(s.getInetAddress().equals(stub.getInetAddress()))
				b = true;
		}
		if(!b){
			stubs.add(stub);
		}
	}
	
	public void removeStub(InetAddress ip){
		DistributeCacheStub s = getStub(ip);
		if(s != null){
			stubs.remove(s);
		}
	}
	
	public DistributeCacheStub getStub(InetAddress ip){
		for(int i = 0 ; i < stubs.size() ; i++){
			DistributeCacheStub s = stubs.get(i);
			if(s.getInetAddress().equals(ip))
				return s;
		}
		return null;
	}
	
	protected List<DistributeCacheStub> getStubs(){
		return stubs;
	}
	
	public boolean containsKeyInStubs(Object key){
		for(int i = 0 ; i < stubs.size() ; i++){
			DistributeCacheStub s = stubs.get(i);
			if(s.contains(key))
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public DistributeCacheStub getStubContainsKey(Object key){
		for(int i = 0 ; i < stubs.size() ; i++){
			DistributeCacheStub s = stubs.get(i);
			if(s.contains(key))
				return s;
		}
		return null;
	}
	
	/**
	 * 获得运行最好的包含指定key的存根，我们会选择这台机器获取我们需要的对象
	 * 如果远程服务器上没有对应的对象，返回null。
	 * 如果Key不符合JavaBean规范或者不是primitive类型，直接返回null。
	 * @param key
	 * @param timeout 指定超时的时间，规定时间内不能获取对象，也返回null。单位为毫秒，而且此参数必须是大于0的一个整数 
	 * @return
	 */
	public Cacheable getObjectFromBestStub(Object key,long timeout){
		DistributeCacheStub stub = getStubContainsKey(key);
		if(stub != null)
			return manager.getObjectFromStub(stub,key,timeout);
		return null;
	}
	
	/**
	 *  通过广播从运程的服务器上获取对象，如果远程服务器上没有对应的对象，返回null。
	 *  如果Key不符合JavaBean规范或者不是primitive类型，直接返回null。
	 * @param key
	 * @param timeout 指定超时的时间，规定时间内不能获取对象，也返回null。单位为毫秒，而且此参数必须是大于0的一个整数 
	 * @return 
	 */
	public Cacheable findObjectByBroadcast(Object key,long timeout){
		return manager.findObjectByBroadcast(this,key,timeout);
	}
	
   /**
     * 广播通知各个机器上的Cache，某个对象值发生了变化
     * 要求其他虚拟机上对应的cache重新装载这个对象
     * @param key
     */
	public void updateObjectByBroadcast(Object key){
		manager.updateObjectByBroadcast(this,key);
	}

	/**
	 * 通知本地cache在其他机器上的存根，remove了一个对象，
	 * 这个通知信息首先是进入队列，然后有专门发送通知的线程向外广播。
	 * 不保证此信息能及时到达其他机器上的存根
	 * @param key
	 */
	public void notifyRemoveObject(Object key){
		manager.notifyRemoveObject(this,key);
	}
	
	/**
	 * 通知本地cache在其他机器上的存根，put了一个新的对象到本地cache中，
	 * 这个通知信息首先是进入队列，然后有专门发送通知的线程向外广播。
	 * 不保证此信息能及时到达其他机器上的存根
	 * @param key
	 */
	public void notifyPutObject(Object key){
		manager.notifyPutObject(this,key);
	}
	/**
	 * 通知本地cache在其他机器上的存根，cache被清空了，
	 * 这个通知信息首先是进入队列，然后有专门发送通知的线程向外广播。
	 * 不保证此信息能及时到达其他机器上的存根
	 * @param key
	 */
	public void notifyClearCache(){
		manager.notifyClearCache(this);
	}
	
	public String toString(){
		return this.getName();
	}
}
