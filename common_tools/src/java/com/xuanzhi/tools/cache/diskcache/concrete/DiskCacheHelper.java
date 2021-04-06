package com.xuanzhi.tools.cache.diskcache.concrete;

import java.lang.ref.WeakReference;
import java.util.*;

public class DiskCacheHelper {

	public static LinkedList<WeakReference<AbstractDiskCache>> caches = new LinkedList<WeakReference<AbstractDiskCache>>();
	
	protected static void addDiskCache(AbstractDiskCache cache){
		caches.add(new WeakReference<AbstractDiskCache>(cache));
	}
	
	protected static void removeDiskCache(AbstractDiskCache cache){
		Iterator<WeakReference<AbstractDiskCache>> it = caches.iterator();
		while(it.hasNext()){
			WeakReference<AbstractDiskCache> a = it.next();
			AbstractDiskCache c = a.get();
			if(c != null && c == cache){
				it.remove();
				return;
			}
		}
	}
	
	public static AbstractDiskCache[] getAllDiskCache(){
		ArrayList<AbstractDiskCache> al = new ArrayList<AbstractDiskCache>();
		Iterator<WeakReference<AbstractDiskCache>> it = caches.iterator();
		while(it.hasNext()){
			WeakReference<AbstractDiskCache> a = it.next();
			AbstractDiskCache cache = a.get();
			if(cache != null){
				al.add(cache);
			}
		}
		return al.toArray(new AbstractDiskCache[0]);
	}
	
	/**
	 * 读取某个快的数据
	 * @param cache
	 * @param db
	 * @throws Exception
	 */
	public static Object[] readData(DefaultDiskCache cache,DataBlock db) throws Exception{
		synchronized(cache){
			db.ddc = cache;
			db.readAllData(cache.reader);
			Object ret[] = new Object[2];
			ret[0] = db.key;
			ret[1] = ((DefaultDiskCache.ObjectWrapper)(db.value)).value;
			db.key = null;
			db.value = null;
			cache.addCurrentMemorySize(-(db.keyLength+db.valueLength));
			return ret;
		}
	}
}
