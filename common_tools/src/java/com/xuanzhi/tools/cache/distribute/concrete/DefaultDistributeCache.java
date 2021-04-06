package com.xuanzhi.tools.cache.distribute.concrete;

import java.util.Collection;
import java.util.Set;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.distribute.DistributeCache;
import com.xuanzhi.tools.cache.LruMapCache;

public class DefaultDistributeCache implements DistributeCache {

	protected String m_name;
	protected LruMapCache m_cache;
	protected DistributeCacheGroup group;
	protected long timeout = 1000L;
	
	protected float loadFactor = 0.5f;
	protected long maxLifeTime;
	
	public DefaultDistributeCache(String name,int maxSize,long maxLifeTime,float loadFactor,long invokeTimeout){
		m_cache = new LruMapCache(maxSize,maxLifeTime,false,name);
		m_name = name;
		this.loadFactor = loadFactor;
		this.maxLifeTime = maxLifeTime/1000;
		this.timeout = invokeTimeout;
	}
	
	public void setDistributeCacheGroup(DistributeCacheGroup group){
		this.group = group;
	}
	
	public String getName() {
		return m_name;
	}

	public Cacheable put(Object key, Cacheable object) {
		Cacheable value = m_cache.get(key);
		if(value == null || !value.equals(object)){
			Cacheable r = m_cache.put(key,object);
			if(group != null)
				group.notifyPutObject(key);
			return r;
		}
		return null;
	}

	public Cacheable get(Object key) {
		return get(key,true);
	}
	/**
     * 判断本地的Cache是否包含给定的Key，此方法就相当与非分布式Cache的containsKey方法。
     * 
     * @param key
     * @return
     */
    public boolean containsKeyOnLocal(Object key){
    	Cacheable value = m_cache.get(key);
    	if(value != null)
    		return true;
    	return false;
    }
    
	protected Cacheable get(Object key,boolean findFromStub) {
		Cacheable r = m_cache.get(key);
		if(r != null)
			return r;
		
		if(findFromStub && group != null){
			r = group.getObjectFromBestStub(key,timeout);
			if(r != null){
				if(m_cache.getSize() < m_cache.getMaxSize() * loadFactor){
					m_cache.put(key,r);
				}
				return r;
			}
			r = group.findObjectByBroadcast(key,timeout);
			if(r != null){
				if(m_cache.getSize() < m_cache.getMaxSize() * loadFactor){
					m_cache.put(key,r);
				}
				return r;
			}
		}
		return null;
		
	}
	
	/**
     * 通知Cache，此cache中的某个对象的值发生的改变，
     * 要求其他虚拟机上对应的cache重新装载这个对象
     * @param key
     */
    public void update(Object key){
    	if(group != null){
    		group.updateObjectByBroadcast(key);
    	}
    }
    
	

	public int getMaxSize() {
		return m_cache.getMaxSize();
	}

	public long getMaxLifeTime(){
		return this.maxLifeTime;
	}
	public float getLoadFactor(){
		return loadFactor;
	}
	public void setMaxSize(int arg0) {
		m_cache.setMaxSize(arg0);

	}

	public int getNumElements() {
		return m_cache.getNumElements();
	}

	public void remove(Object key) {
		remove(key,true);
	}

	public void remove(Object key,boolean notifyStub) {
		m_cache.remove(key);
		if(notifyStub && group != null){
			group.notifyRemoveObject(key);
		}
	}
	
	public void clear() {
		m_cache.clear();
		if(group != null){
			group.notifyClearCache();
		}
	}

	public Collection values() {
		return m_cache.values();
	}

	public Set entrySet() {
		return m_cache.entrySet();
	}

	public long getCacheHits() {
		return m_cache.getCacheHits();
	}

	public long getCacheMisses() {
		return m_cache.getCacheMisses();
	}

	public int getSize() {
		return m_cache.getSize();
	}

}
