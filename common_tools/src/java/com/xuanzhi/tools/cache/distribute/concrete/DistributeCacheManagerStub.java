package com.xuanzhi.tools.cache.distribute.concrete;

import java.net.InetAddress;

/**
 * 分布式CacheManager的一个存根，此存根对应其他一台机器上的一个分布式CacheManager，
 * 并记录了这台机器中存在的分布式Cache以及机器的状态
 * 
 *
 */
public class DistributeCacheManagerStub {

	protected int maxHeartBeatNum;
	
	protected InetAddress ip;
	
	public void setMaxHeartBeatNum(int n){
		maxHeartBeatNum = n;
	}
	
	public void heartBeat(){
		maxHeartBeatNum --;
	}
	
	public boolean isAlive(){
		return (maxHeartBeatNum > 0);
	}
	
	public void setInetAddress(InetAddress ip){
		this.ip = ip;
	}
	
	public InetAddress getInetAddress(){
		return ip;
	}
	

}
