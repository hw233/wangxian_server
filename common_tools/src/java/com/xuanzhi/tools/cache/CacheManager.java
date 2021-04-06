package com.xuanzhi.tools.cache;


/**
 */
public interface CacheManager {

	/**
	 * 获取一个给定名称的Cache，如果此cache没有配置，返回null
	 * @param name
	 * @return
	 */
	public Cache getCache(String name);
}
