package com.xuanzhi.tools.cache.diskcache;

import java.io.Serializable;

/**
 * 基本功能：具有memory cache的特性，可以将（key,value）加入到cache中，以后可以多次用key获得value，而且效率很高。
 * 除memory cache的特性外，提供如下特性：
 * 1. cache的容量很大，可以cache很多对象
 * 2. 每个（key,value）可以独立设置最大生命周期（lifetime）和最大空闲时间(idletime)
 * 3. 系统崩溃后恢复，cache中的（key,value）不会丢失，同时cache的状态信息也不会丢失（包括lifetime和idletime）
 * 
 */
public interface DiskCache {

    /**
     * Returns the maximum size of the cache in bytes. If the cache grows too
     * large, the least frequently used items will automatically be deleted so
     * that the cache size doesn't exceed the maximum.
     *
     * @return the maximum size of the cache in bytes.
     */
    public long getMaxDiskSize() ;
    
    /**
     * Sets the maximum size of the cache in bytes. If the cache grows too
     * large, the least frequently used items will automatically be deleted so
     * that the cache size doesn't exceed the maximum.
     *
     * @param maxSize the maximum size of the cache in bytes.
     */
    public void setMaxDiskSize(long maxSize) ;

    /**
     * Returns the maximum size of the cache in bytes. If the cache grows too
     * large, the least frequently used items will automatically be deleted so
     * that the cache size doesn't exceed the maximum.
     *
     * @return the maximum size of the cache in bytes.
     */
    public int getMaxMemorySize() ;
    
    /**
     * Sets the maximum size of the cache in bytes. If the cache grows too
     * large, the least frequently used items will automatically be deleted so
     * that the cache size doesn't exceed the maximum.
     *
     * @param maxSize the maximum size of the cache in bytes.
     */
    public void setMaxMemorySize(int maxSize) ;
    
    /**
     * Returns the current used memory size of the cache in bytes.
     * @return
     */
    public int getCurrentMemorySize();
    
    /**
     * Returns the current used disk size of the cache in bytes.
     * @return
     */
    public long getCurrentDiskSize();

    /**
     * Returns the number of objects in the cache.
     *
     * @return the number of objects in the cache.
     */
    public int getNumElements() ;
  
    /**
     * Adds a new Cacheable object to the cache. The key must be unique.
     *
     * @param key a unique key for the object being put into cache.
     * @param object the  object to put into cache.
     * @return return the old object in cache
     */
    public void put(Serializable key, Serializable object); 
    
    /**
     * Adds a new Cacheable object to the cache. The key must be unique.
     * 
     *
     * @param key a unique key for the object being put into cache.
     * @param object the  object to put into cache.
     * @param lifeTimeout the life time of this object from now.ex: 1800000 means next half an hour,the object will be removed. zero means never timeout
     * @param idleTimeout the idle time of this object from now.ex: 1800000 means if next half an hour,no one get this object ,this object will be removed. zero means never timeout
     * @return return the old object in cache
     */
    public void put(Serializable key, Serializable object,long lifeTimeout,long idleTimeout); 
  
    /**
     * Gets an object from cache. This method will return null under two
     * conditions:<ul>
     *    <li>The object referenced by the key was never added to cache.
     *    <li>The object referenced by the key has expired from cache.</ul>
     *
     * @param key the unique key of the object to get.
     * @return the object corresponding to unique key.
     */
    public Serializable get(Serializable key) ;
       
    /**
     * Removes an object from cache.
     *
     * @param key the unique key of the object to remove.
     */
    public void remove(Serializable key) ;
	
    /**
     * Clears the cache of all objects. The size of the cache is reset to 0.
     */
    public void clear() ;
    
	
  
    /**
     * Returns the number of cache hits. A cache hit occurs every
     * time the get method is called and the cache contains the requested
     * object.<p>
     *
     * Keeping track of cache hits and misses lets one measure how efficient
     * the cache is; the higher the percentage of hits, the more efficient.
     *
     * @return the number of cache hits.
     */
    public long getCacheHits(); 
 
    /**
     * Returns the number of cache misses. A cache miss occurs every
     * time the get method is called and the cache does not contain the
     * requested object.<p>
     *
     * Keeping track of cache hits and misses lets one measure how efficient
     * the cache is; the higher the percentage of hits, the more efficient.
     *
     * @return the number of cache hits.
     */
    public long getCacheMisses() ;
}
