package com.xuanzhi.tools.cache.lateral;

import com.xuanzhi.tools.cache.Cache;
import com.xuanzhi.tools.cache.Cacheable;

/**
 * 分布式cache，此Cache内部封装一个Cache，同时他对应其他虚拟机中的Cache。
 * 如果一个对象在本地Cache中没有，那么它首先会去检查其他虚拟机中的对应Cache中
 * 是否存在，如果存在，就获取对应的对象，如果其他虚拟机中也都没有，才返回null
 * 
 * 我们把操作分为三种：
 * 	  put，modify，remove
 *		put是指向cache中加入对象（key，value），（隐含的意思是从database中读取了一个对象）
 *		modify是指在cache中的某个对象，被修改了。（已经将修改保存到database中）
 *		remove是指将某个从cache中删除，（隐含的意思是从database中删除了）
 * 
 * 几种处理模式：
 *    强覆盖模式：
 *    	put：附带对象（key，value）对其他lateral cache进行广播，要求其他lateral cache用收到的（key，value）覆盖本地的（key，value）
 *      modify：附带对象（key，value）对其他lateral cache进行广播，要求其他lateral cache用收到的（key，value）覆盖本地的（key，value）
 *      remove：附带键值（key）对其他lateral cache进行广播，要求其他lateral cache清除对应的对象
 *
 *    弱覆盖模式：
 *    	put：不做任何事情
 *      modify：附带对象（key，value）对其他lateral cache进行广播，要求其他lateral cache用收到的（key，value）覆盖本地的（key，value）
 *      remove：附带键值（key）对其他lateral cache进行广播，要求其他lateral cache清除对应的对象
 *
 *    强引用模式：
 *      put：附带键值（key）对其他lateral cache进行广播，要求其他lateral cache用收到的（key，value引用）覆盖本地的（key，value）
 *      modify：附带键值（key）对其他lateral cache进行广播，要求其他lateral cache用收到的（key，value引用）覆盖本地的（key，value）
 *      remove：附带键值（key）对其他lateral cache进行广播，要求其他lateral cache清除对应的对象
 *      所谓value引用是指，本地cache记录key对应的对象在那个lateral cache中有，这样可以随时从那个lateral cache中获取这个对象
 *
 *    弱引用模式：
 *      put：附带键值（key）对其他lateral cache进行广播，要求其他lateral cache用收到的（key，value引用）后，进行判断，如果
 *           本地没有（key，value）存在，保存引用。如果有，就不做任何事情。 
 *      modify：附带键值（key）对其他lateral cache进行广播，要求其他lateral cache用收到的（key，value引用）覆盖本地的（key，value）
 *      remove：附带键值（key）对其他lateral cache进行广播，要求其他lateral cache清除对应的对象
 *      所谓value引用是指，本地cache记录key对应的对象在那个lateral cache中有，这样可以随时从那个lateral cache中获取这个对象

 *    重装模式：
 *      put：不做任何事情
 *      modify：附带键值（key）对其他lateral cache进行广播，要求其他lateral cache清除对应的对象
 *      remove：附带键值（key）对其他lateral cache进行广播，要求其他lateral cache清除对应的对象
 *    
 *    无为模式：  
 *      put：不做任何事情
 *      modify：不做任何事情
 *      remove：不做任何事情
 *   默认为 无为模式
 *
 */
public interface LateralCache extends Cache {
	
	/**
	 * 强覆盖模式：
	 */
	public static final int STRONG_OVERWRITE_MODEL = 1;
	
	/**
	 * 弱覆盖模式：
	 */
	public static final int WEAK_OVERWRITE_MODEL = 2;
	
	/**
	 * 强引用模式：
	 */
	public static final int STRONG_REFERENCE_MODEL = 3;
	
	/**
	 * 弱引用模式：
	 */
	public static final int WEAK_REFERENCE_MODEL = 4;

	/**
	 * 重装模式：
	 */
	public static final int RELOAD_MODEL = 5;
	
	/**
	 *  无为模式：  
	 */
	public static final int NOTHING_MODEL = 6;

	/**
	 * 分布式Cache的名称，只有同一名称的分布式Cache才能互相通信，互相传输对象。
	 * 不同虚拟机中的同一名称的分布式Cache，我们成为CacheGroup。
	 * 此名称一旦设置，就不能被修改。
	 */
	public String getName();
	
	/**
	 * 处理模式
	 * @return
	 */
	public int getHandleModel();
	
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
     * 要求其他虚拟机上对应的cache做相应的处理
     * @param key
     */
    public void update(Object key);
	
	
}
