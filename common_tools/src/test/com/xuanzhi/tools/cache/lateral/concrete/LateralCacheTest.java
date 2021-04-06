package com.xuanzhi.tools.cache.lateral.concrete;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.lateral.LateralCache;
import com.xuanzhi.tools.cache.lateral.concrete.LateralCacheTestMain.CacheItem;
import com.xuanzhi.tools.text.StringUtil;

public class LateralCacheTest implements Runnable{

	DefaultLateralCacheManager manager;
	
	public void setCacheManager(DefaultLateralCacheManager manager){
		this.manager = manager;
	}
	
	public void setStep(long l){
		this.step = l;
	}
	Thread thread;
	public void init(){
		thread = new Thread(this);
		thread.start();
	}
	
	protected long step;
	
	protected ArrayList<String> keys = new ArrayList<String>();
	
	
	public Cacheable loadObject(String key){
		return new CacheItem(key);
	}
	
	public void run(){
		Random r = new Random(System.currentTimeMillis());
		while(true){
			try {
				Thread.sleep(step);
			
				DefaultLateralCache cache = null;
				String keywords[] = manager.getCacheMap().keySet().toArray(new String[0]);
				if(keywords.length == 0) continue;
				int p = r.nextInt(keywords.length);
				if(p<0 || p>=keywords.length) p = 0;
				cache = (DefaultLateralCache)manager.getCache(keywords[p]);
				int k = r.nextInt(102400) % 13;
				if(k<3){
					String key = StringUtil.randomString(5);
					Cacheable o = loadObject(key);
					cache.put(key,o);
					keys.add(key);
				}else if(k==3 && keys.size() > 10){
					int i = r.nextInt(keys.size());
					cache.remove(keys.get(i));
				}else if(k < 8&& keys.size() > 10){
					int i = r.nextInt(keys.size());
					cache.update(keys.get(i));
				}else if(k < 11 && keys.size() > 10){
					int i = r.nextInt(keys.size());
					cache.get(keys.get(i));
				}else{
					Object objs[] = cache.m_refcache.entrySet().toArray();
					p = r.nextInt(objs.length);
					Map.Entry me = (Map.Entry)objs[p];
					cache.get(me.getKey());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
