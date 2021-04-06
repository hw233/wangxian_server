package com.xuanzhi.tools.async;

import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;

/**
 * 可存储的对象接口，此对象首先必须是可缓存的
 * 
 */
public interface Storable extends Cacheable,CacheListener {
	
	public boolean isDirty();
	
	public void setDirty();
	
	public long getLastUpdateTime();
	
	public void setLastUpdateTime(long time);
	
	public boolean isNew();
	
}
