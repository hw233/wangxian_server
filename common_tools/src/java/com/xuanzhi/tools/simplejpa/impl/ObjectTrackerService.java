package com.xuanzhi.tools.simplejpa.impl;

import java.io.File;

import java.util.HashMap;


import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;


/**
 * 跟踪对象创建，销毁服务，此服务是单例对象，请直接用此对象的静态方法
 * 
	
 *                		     
 */
public class ObjectTrackerService {

	public static Class[] getTrackedClass(){
		return map.keySet().toArray(new Class[0]);
	}
	
	public static ObjectCreationTracker getObjectCreationTracker(Class cl){
		return map.get(cl);
	}
	
	static HashMap<Class,ObjectCreationTracker> map = new HashMap<Class,ObjectCreationTracker>();
	
	static String path;
	static int timeoutDays = 3;
	
	static DefaultDiskCache cache ;
	
	
	static boolean initialized = false;
	
	static boolean enableObjectTrackerService = true;
	
	public static final long serviceStartTime = System.currentTimeMillis();
	
	static synchronized void init(){
		if(initialized) return;
		initialized = true;
		if(path == null){
			path = System.getProperty("simpleEMFTrackerService.ddc");
			if(path == null){
				path = System.getProperty("user.dir") + "/simpleEMFTrackerService.ddc";
			}
		}
		cache = new DefaultDiskCache(new File(path),"simpleEMFTrackerService",1L*timeoutDays*24L*3600L*1000L);
		
		Boolean b = (Boolean)cache.get("enableObjectTrackerService");
		if(b != null){
			enableObjectTrackerService = b.booleanValue();
		}
		
	}
	
	
	
	public static void setEnableObjectTrackerService(boolean b){
		enableObjectTrackerService = b;
	
		if(cache != null){
			cache.put("enableObjectTrackerService", Boolean.valueOf(enableObjectTrackerService));
		}
	}
	
	public static boolean isEnableObjectTrackerService(){
		return enableObjectTrackerService;
	}
	
	
	static void heartbeat(){
		ObjectCreationTracker trackers[] = map.values().toArray(new ObjectCreationTracker[0]);
		for(int i = 0 ; i < trackers.length ; i++){
			trackers[i].heartbeat();
		}
	}
	
	/**
	 * 通知跟踪服务，某个对象被JVM创建出来
	 * @param o
	 */
	public static void objectCreate(Class clazz){
		try{
			if(!initialized){
				init();
			}
			if(!enableObjectTrackerService)
				return;
			
			ObjectCreationTracker oct = map.get(clazz);
			if(oct == null){
				synchronized(clazz){
					oct = map.get(clazz);
					if(oct == null){
						oct = new ObjectCreationTracker(clazz,cache);
						map.put(clazz, oct);
					}
				}
			}
			
			oct.object_create();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 通知跟踪服务，某个对象被JVM销毁
	 * @param o
	 */
	public static void objectDestroy(Class clazz){
		try{
			if(!initialized){
				init();
			}
			
			if(!enableObjectTrackerService)
				return;
			
			ObjectCreationTracker oct = map.get(clazz);
			if(oct == null){
				synchronized(clazz){
					oct = map.get(clazz);
					if(oct == null){
						oct = new ObjectCreationTracker(clazz,cache);
						map.put(clazz, oct);
					}
				}
			}
			
			oct.object_destroy();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
}
