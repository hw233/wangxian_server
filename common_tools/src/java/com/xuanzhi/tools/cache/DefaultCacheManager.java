package com.xuanzhi.tools.cache;

import java.util.*;

import com.xuanzhi.tools.cache.LruMapCache.RemoveItem;

/**
 * 默认的CacheManager.
 * 此类为Singleton模式，并且提供创建cache方法和获取cache方法。
 * 
 * 要用cache，可以先调用创建cache的方法，然后再调用获取cache的方法，获取cache。
 * 这个cachemanager中的所有的cache共用一个checkThread。
 * 
 *
 */
public class DefaultCacheManager implements CacheManager,Runnable{

	protected static DefaultCacheManager m_self = null;
	
	/**
	 * 得到此类的唯一实例
	 * @return
	 */
	public static DefaultCacheManager getInstance(){
		if(m_self != null) return m_self;
		synchronized(DefaultCacheManager.class){
			if(m_self != null) return m_self;
			m_self = new DefaultCacheManager();
			return m_self;
		}
	}
	
	protected Hashtable<String,LruMapCache> tables = new Hashtable<String,LruMapCache>();
	
	protected Thread thread = null;
	
	protected DefaultCacheManager(){
		thread = new Thread(this,"DefaultCacheManager-Check-Thread");
		thread.start();
	}
	
	/**
	 * 创建一个新的cache，如果给定的name的cache已经存在，那么就重新设定此cache的maxSize和maxLifeTime，
	 * 否则创建出一个新的cache并返回。
	 * 
	 * @param name
	 * @param maxSize
	 * @param maxLifeTime
	 * @return
	 */
	public synchronized Cache createCache(String name,int maxSize,long maxLifeTime){
		LruMapCache c = tables.get(name);
		if(c != null){
			c.maxSize = maxSize;
			c.maxLifetime = maxLifeTime;
			return c;
		}
		c = new LruMapCache(maxSize,maxLifeTime,false,name);
		tables.put(name,c);
		return c;
	}
	
	/**
	 * 获得一个指定名称的cache，如果这个指定名称的cache没有调用创建方法创建，
	 * 那么返回null
	 */
	public synchronized Cache getCache(String name) {
		 return tables.get(name);
	}

	protected void check(){
		Object os[] = tables.values().toArray();
		for(int i = 0 ; i < os.length ; i++){
			LruMapCache c = (LruMapCache)os[i];
			
			c.deleteExpiredEntries();
			c.cullCache();
			try{
				while(c.removeQueue.size() > 0){
					RemoveItem ri = (RemoveItem)c.removeQueue.remove(0);
					ri.cl.remove(ri.type);
					System.err.println("In cache ["+c.name+"], call object feeback method with key ["+ri.key+"] type ["+ri.type+"], remove queue size ["+c.removeQueue.size()+" ]");
				}
			}catch(Exception e){
				System.err.println("In cache ["+c.name+"], call object feeback method ERROR, remove queue size ["+c.removeQueue.size()+" ]");

				e.printStackTrace();
			}
		}
	}
	
	public void run() {
		while(thread != null && thread.isInterrupted() == false){
			try{
				Thread.sleep(1000L);
				check();
			}catch(Exception e){
				System.err.println("In default cache manager check thread, catch error:");
				e.printStackTrace();
			}
		}
		
	}

}
