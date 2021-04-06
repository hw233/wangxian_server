package com.xuanzhi.tools.mem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 跟踪对象创建，销毁服务，此服务是单例对象，请直接用此对象的静态方法
 * 
 * 两个核心的方法：
 * 		void objectCreate(Object o);
 * 		void objectDestroy(Object o);
 * 分别在要跟踪的类的构造函数和Finalize函数中调用。
 * 例如：
 * 		public class NeedTraceClass{
 * 
 * 			public NeedTraceClass(){
 * 				ObjectTrackerService.objectCreate(this);
 * 			}
 * 
 * 			public NeedTraceClass(....){
 * 				ObjectTrackerService.objectCreate(this);
 * 			}
 * 
 * 			protected void finalize() throw Throwable{
 * 				ObjectTrackerService.objectDestroy(this);
 * 			}
 * 		}
 * 
 * 如果需要将创建对象，销毁对象打印到文件中，可以用下面的方法：
 *      config(String _cacheFile,int _timeoutDays,String _logbackName,String _log4jName);
 * 
 * 如果要对某个类型的对象，进行更加细致的配置，可以用下面的方法
 *      config(Class clazz,boolean enableTrack,
			boolean enableTrackCallTrace,
			boolean enableLogger,boolean enableHistoryData,int saveStepTime)
 *
 * 这两个方法都有默认的配置。
 * 
 * 此实现中，默认开启了enableTrackCallTrace，此配置需要跟踪每个对象是哪个效用堆栈创建的。
 * 已经进行了极致的优化，但是还需要消耗一些内存。
 * 消耗的比例大概为：跟踪1个对象需要12个字节，同时我们采用了比较先进的Int2IntMap，
 *                跟踪1000000个内存中的对象，需要耗费25M内存（hash的方式，最多最多浪费一倍的内存）。
 *                如果采用HashMap跟踪内存中的对象，需要耗费75M内存。 	
 *                		     
 */
public class ObjectTrackerService {

	public static class ConfigItem implements java.io.Serializable{

		private static final long serialVersionUID = -7534558822608106292L;
		
		protected boolean auto_create = true;
		
		ConfigItem(Class cl,boolean enableTrack,boolean  enableTrackCallTrace,boolean  enableLogger,boolean enableHistoryData,long saveStepTime){
			this.clazz = cl;
			this.enableTrack = enableTrack;
			this.enableTrackCallTrace = enableTrackCallTrace;
			this.enableLogger = enableLogger;
			this.enableHistoryData = enableHistoryData;
			this.saveStepTime = saveStepTime;
			auto_create = true;
		}
		public Class clazz;
		public boolean enableTrack;
		public boolean enableTrackCallTrace;
		public boolean enableLogger;
		public boolean enableHistoryData;
		public long saveStepTime;
	}

	
	static ArrayList<ConfigItem> config = new ArrayList<ConfigItem>();
	static HashMap<Class,ObjectCreationTracker> map = new HashMap<Class,ObjectCreationTracker>();
	
	static String path;
	static int timeoutDays = 3;
	
	static DefaultDiskCache cache ;
	static boolean initialized = false;
	
	static boolean enableObjectTrackerService = false;
	
	static String logbackName;
	static String log4jName;
	static org.slf4j.Logger logback;
	static org.apache.log4j.Logger log4j;
	
	static Thread thread;
	
	public static final long serviceStartTime = System.currentTimeMillis();
	
	static synchronized void init(){
		if(initialized) return;
		initialized = true;
		if(path == null){
			path = System.getProperty("com.xuanzhi.tools.mem.ObjectTrackerService.cache");
			if(path == null){
				path = System.getProperty("user.dir") + "/objectTrackerService.ddc";
			}
		}
		cache = new DefaultDiskCache(new File(path),"ObjectTrackerService",1L*timeoutDays*24L*3600L*1000L);
		
		Boolean b = (Boolean)cache.get("enableObjectTrackerService");
		if(b != null){
			enableObjectTrackerService = b.booleanValue();
		}
		
		ArrayList<ConfigItem> al = (ArrayList<ConfigItem>)cache.get("config.item");
		if(al != null){
			config.addAll(al);
		}
		
		if(logbackName != null && logbackName.length() > 0){
			logback = LoggerFactory.getLogger(logbackName);
		}
		if(log4jName != null && log4jName.length() > 0){
			log4j = org.apache.log4j.Logger.getLogger(log4jName);
		}
		if(thread != null){
			thread.interrupt();
		}
		thread = new Thread(new Runnable(){

			public void run() {
				while(Thread.currentThread().isInterrupted() == false){
					try{
						ObjectTrackerService.heartbeat();
						Thread.sleep(30000);
					}catch(Throwable e){
						e.printStackTrace();
					}
				}
			}},"ObjectTrackerService-Heartbeat-Thread");
		thread.start();
	}
	
	/**
	 * 配置此服务的一些基本属性，包括cache文件的位置，保留数据的天数，日志的名字
	 * 日志的名字为null，标识不需要日志
	 * 
	 * 此配置必须在第一个对象创建前，被调用才生效
	 * 
	 * @param _cacheFile 默认在user.dir下
	 * @param _timeoutDays 默认30天
	 * @param _logbackName 默认为null
	 * @param _log4jName 默认为null
	 */
	public static void config(String _cacheFile,int _timeoutDays,String _logbackName,String _log4jName){
		if(initialized == false){
			path = _cacheFile;
			timeoutDays = _timeoutDays;
			logbackName = _logbackName;
			log4jName = _log4jName;
		}else{
			logbackName = _logbackName;
			log4jName = _log4jName;
			
			if(logbackName != null && logbackName.length() > 0){
				logback = LoggerFactory.getLogger(logbackName);
			}else{
				logback = null;
			}
			if(log4jName != null && log4jName.length() > 0){
				log4j = org.apache.log4j.Logger.getLogger(log4jName);
			}else{
				log4j = null;
			}
			
			ObjectCreationTracker trackers[] = map.values().toArray(new ObjectCreationTracker[0]);
			for(int i = 0 ; i < trackers.length ; i++){
				trackers[i].logback = logback;
				trackers[i].log4j = log4j;
				
			}
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
	
	/**
	 * 配置某一种类型的对象的配置，此方法可以随时效用，随时生效
	 * 
	 * @param clazz
	 * @param enableTrack 跟踪对象创建，销毁过程，默认为true
	 * @param enableTrackCallTrace 跟踪每个对象创建的调用堆栈，默认为true
	 * @param enableLogger 打日志，默认为false
	 * @param enableHistoryData 保存历史数据，默认为true
	 * @param saveStepTime 历史数据存盘间隔，默认为5分钟
	 */
	public static void config(Class clazz,boolean enableTrack,
			boolean enableTrackCallTrace,
			boolean enableLogger,boolean enableHistoryData,int saveStepTime){
		
		if(!initialized){
			init();
		}
		
		int index = -1;
		for(int i = 0 ; i < config.size() ; i++){
			ConfigItem ci = config.get(i);
			if(ci.auto_create && clazz.isAssignableFrom(ci.clazz)){
				ci.enableTrack = enableTrack;
				ci.enableTrackCallTrace = enableTrackCallTrace;
				ci.enableLogger = enableLogger;
				ci.enableHistoryData = enableHistoryData;
				ci.saveStepTime = saveStepTime;
				if(ci.saveStepTime < 60000) ci.saveStepTime = 60000;
				ci.auto_create = false;
			}
			
			if(ci.clazz == clazz){
				index = i;
			}
		}
		if(index == -1){
			ConfigItem ci = new ConfigItem(clazz,enableTrack,enableTrackCallTrace,enableLogger,enableHistoryData,saveStepTime);
			ci.auto_create =false;
			config.add(ci);
		}
		ArrayList<ConfigItem> al = new ArrayList<ConfigItem>();
		for(int i = 0 ; i < config.size() ; i++){
			ConfigItem ci = config.get(i);
			if(ci.auto_create ==false){
				al.add(ci);
			}
		}
		if(cache != null && al.size() > 0){
			cache.put("config.item", al);
		}
	}
	
	static ConfigItem getConfig(Class cl){
		for(int i = 0 ; i < config.size() ; i++){
			ConfigItem ci = config.get(i);
			if(ci.clazz == cl){
				return ci;
			}
		}
		for(int i = 0 ; i < config.size() ; i++){
			ConfigItem ci = config.get(i);
			if(ci.clazz.isAssignableFrom(cl)){
				return ci;
			}
		}
		
		return null;
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
	public static void objectCreate(Object o){
		try{
			if(!initialized){
				init();
			}
			if(!enableObjectTrackerService)
				return;
			
			Class clazz = o.getClass();
			ObjectCreationTracker oct = map.get(clazz);
			if(oct == null){
				synchronized(clazz){
					oct = map.get(clazz);
					if(oct == null){
						ConfigItem ci = getConfig(clazz);
						if(ci == null){ 
							ci = new ConfigItem(clazz,true,true,true,true,5*60*1000);
							config.add(ci);
						}
						oct = new ObjectCreationTracker(clazz,ci,cache,logback,log4j);
						map.put(clazz, oct);
					}
				}
			}
			if(oct.ci.enableTrack)
				oct.object_create(System.identityHashCode(o));
		}catch(Throwable e){
			if(logback != null){
				logback.warn("[objectCreate] [error]",e);
			}
			if(log4j != null){
				log4j.warn("[objectCreate] [error]",e);
			}
		}
	}
	
	/**
	 * 通知跟踪服务，某个对象被JVM销毁
	 * @param o
	 */
	public static void objectDestroy(Object o){
		try{
			if(!initialized){
				init();
			}
			
			if(!enableObjectTrackerService)
				return;
			
			Class clazz = o.getClass();
			ObjectCreationTracker oct = map.get(clazz);
			if(oct == null){
				synchronized(clazz){
					oct = map.get(clazz);
					if(oct == null){
						ConfigItem ci = getConfig(clazz);
						if(ci == null){ 
							ci = new ConfigItem(clazz,true,true,true,true,5*60*1000);
							config.add(ci);
						}
						oct = new ObjectCreationTracker(clazz,ci,cache,logback,log4j);
						map.put(clazz, oct);
					}
				}
			}
			if(oct.ci.enableTrack)
				oct.object_destroy(System.identityHashCode(o));
		}catch(Throwable e){
			if(logback != null){
				logback.warn("[objectDestroy] [error]",e);
			}
			if(log4j != null){
				log4j.warn("[objectCreate] [error]",e);
			}
		}
	}
}
