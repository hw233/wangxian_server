package com.xuanzhi.tools.cache.lateral.concrete;

import java.util.Collection;
import java.util.Set;

import com.xuanzhi.tools.cache.Cacheable;

import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.cache.lateral.LateralCache;

public class DefaultLateralCache implements LateralCache {

	static String[] MODEL_NAMES = new String[]{"UNKNOWN","STRONG_OVERWRITE","WEAK_OVERWRITE",
		"STRONG_REFERENCE","WEAK_REFERENCE","RELOAD_MODEL","NOTHING_MODEL"};
	public static String getStringOfModel(int model){
		if(model >0 && model <= MODEL_NAMES.length)
			return MODEL_NAMES[model];
		return "INVALID_MODEL";
	}
	
	public static int getTypeOfModel(String modelStr){
		for(int i = 1 ; i < MODEL_NAMES.length ; i++){
			if(MODEL_NAMES[i].equalsIgnoreCase(modelStr))
				return i;
		}
		throw new IllegalArgumentException("unknown modle ["+modelStr+"]");
	}
	
	protected String m_name;
	protected LruMapCache m_cache;
	protected LruMapCache m_refcache;
	protected ClientAdapter ca;
	protected long timeout = 1000L;
	
	protected float loadFactor = 0.5f;
	protected long maxLifeTime;
	protected int handleModel;
	
	protected int remoteGetHitNum = 0;
	protected int remoteGetMissNum = 0;
	protected int putNum = 0;
	protected int modifyNum = 0;
	protected int removeNum = 0;
	
	public DefaultLateralCache(ClientAdapter ca,String name,int handleModel,int maxSize,long maxLifeTime,int maxRefNum,long maxRefLifeTime){
		m_cache = new LruMapCache(maxSize,maxLifeTime,false,name);
		m_refcache = new LruMapCache(maxRefNum,maxRefLifeTime,false,name+".ref");
		m_name = name;
		this.ca = ca;
		this.maxLifeTime = maxLifeTime/1000;
		this.handleModel = handleModel;

	}
	
	public String getName() {
		return m_name;
	}

	public Cacheable put(Object key, Cacheable object) {
		Cacheable value = m_cache.get(key);
		if(value == null || !value.equals(object)){
			Cacheable r = m_cache.put(key,object);
			m_refcache.remove(key);
			if(ca != null)
				ca.sendPut(this,key,object);
			this.putNum++;
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
		
		if(findFromStub && ca != null){
			ObjectReference or = (ObjectReference)m_refcache.get(key);
			if(or != null){
				r = (Cacheable)ca.getObjectFromRemoteCache(or.getRemote(),this,key);
				
				if(r != null){
					m_cache.put(key,r);
					m_refcache.remove(key);
					this.remoteGetHitNum++;
					return r;
				}else{
					this.remoteGetMissNum++;
				}
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
    	Object obj = m_cache.get(key);
    	if(ca != null && obj != null){
    		this.modifyNum++;
    		ca.sendModify(this,key,obj);
    		
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

	public int getNumReferences(){
		return m_refcache.getNumElements();
	}
	
	public void remove(Object key) {
		remove(key,true);
	}

	public void remove(Object key,boolean notifyStub) {
		m_cache.remove(key);
		if(notifyStub && ca != null){
			this.removeNum++;
			ca.sendRemove(this,key);
		}
	}
	
	public void clear() {
		m_cache.clear();
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

	public static class ObjectReference implements Cacheable{
		String remote;
		
		public ObjectReference(String remote){
			this.remote = remote;
		}
		
		public String getRemote(){
			return remote;
		}
		
		public int getSize() {
			return 1;
		}
		public String toString(){
			return "[" + remote +"]";
		}
	}

	public int getHandleModel() {
		return handleModel;
	}

	public int getModifyNum() {
		return modifyNum;
	}

	public int getPutNum() {
		return putNum;
	}

	public int getRemoteGetHitNum() {
		return remoteGetHitNum;
	}

	public int getRemoteGetMissNum() {
		return remoteGetMissNum;
	}

	public int getRemoveNum() {
		return removeNum;
	}
}
