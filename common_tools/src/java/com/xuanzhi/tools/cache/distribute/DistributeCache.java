package com.xuanzhi.tools.cache.distribute;

import com.xuanzhi.tools.cache.Cache;
import com.xuanzhi.tools.cache.Cacheable;
/**
 * 分布式cache，此Cache内部封装一个Cache，同时他对应其他虚拟机中的Cache。
 * 如果一个对象在本地Cache中没有，那么它首先会去检查其他虚拟机中的对应Cache中
 * 是否存在，如果存在，就获取对应的对象，如果其他虚拟机中也都没有，才返回null
 * 
 *
 */
public interface DistributeCache extends Cache {
	
	/**
	 * 分布式Cache的名称，只有同一名称的分布式Cache才能互相通信，互相传输对象。
	 * 不同虚拟机中的同一名称的分布式Cache，我们成为CacheGroup。
	 * 此名称一旦设置，就不能被修改。
	 */
	public String getName();
	
	/**
     * Adds a new Cacheable object to the cache. The key must be unique.
     *
     * @param key a unique key for the object being put into cache.
     * @param object the  object to put into cache.
     * @return return the old object in cache
     * Note: 分布式cache为了能在不同的机器之间传输key和object，要求key和object都符合JavaBean的标准，
     * 或者都是Java的primitive类型。如果key和object有一个不符合标准，将不能在不同的Cache之间传递。
     */
    public Cacheable put(Object key, Cacheable object); 
  
    /**
     * Gets an object from cache. This method will return null under two
     * conditions:<ul>
     *    <li>The object referenced by the key was never added to cache.
     *    <li>The object referenced by the key has expired from cache.</ul>
     *
     * @param key the unique key of the object to get.
     * @return the object corresponding to unique key.
     * Note: 分布式cache为了能在不同的机器之间传输key和object，要求key和object都符合JavaBean的标准，
     * 或者都是Java的primitive类型。
     */
    public Cacheable get(Object key) ;
    
    /**
     * 判断本地的Cache是否包含给定的Key，此方法就相当与非分布式Cache的containsKey方法。
     * 
     * @param key
     * @return
     */
    public boolean containsKeyOnLocal(Object key);
    
    /**
     * 通知Cache，此cache中的某个对象的值发生的改变，
     * 要求其他虚拟机上对应的cache重新装载这个对象
     * @param key
     */
    public void update(Object key);
	
	
}
