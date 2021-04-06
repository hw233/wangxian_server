package com.xuanzhi.tools.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.ds.Int2IntMap;
import com.xuanzhi.tools.mem.ObjectTrackerService.ConfigItem;
import com.xuanzhi.tools.text.DateUtil;


/**
 * 此类是方便跟踪对象的创建
 * 
 * 需要小心的是，但有大量对象创建或者删除时，此类会占用大量内存
 * 
 *
 */
public class ObjectCreationTracker {
	
	//创建一个对象的调用堆栈
	public static class CallTrace{
		public StackTraceElement[] elements;
		
		//一共创建对象的数量
		public transient long totalCreatedNum;
		
		//当前内存中的数量
		public transient long currentNumInMem;
		
		//内存中数量的峰值
		public transient long peakNumInMem;
		
		//每分钟统计的数，第一维下标为分钟数，从当天的00:00开始
		//第二维为统计数，统计3个数，创建的数量，销毁的数量和当前内存中的数量
		public transient long[][] everyMinuteStatNum;
		
		protected transient int callTraceIndex;
		
		public boolean eq(StackTraceElement[] e){
			if(elements == e) return true;
			if(elements == null || e == null) return false;			
			if(elements.length != e.length) return false;
			for(int i = 0 ; i < elements.length ;i++){
				if(!elements[i].getClassName().equals(e[i].getClassName()) || !elements[i].getMethodName().equals(e[i].getMethodName()) ) return false;
			}
			return true;
		}
		
		public static String toString(StackTraceElement[] e){
			StringBuffer sb = new StringBuffer();
			for(int i = 0 ; i < e.length ;i++){
				sb.append(e[i].getClassName()+"#" + e[i].getMethodName()+"\n");
			}
			return sb.toString();
		}
	}
	
	//跟踪的对象的类
	public Class trackObjectClass;
	
	//一共创建对象的数量
	public long totalCreatedNum;
	
	//当前内存中的数量
	public long currentNumInMem;
	
	//内存中数量的峰值
	public long peakNumInMem;
	
	//每分钟统计的数，第一维下标为分钟数，从当天的00:00开始
	//第二维为统计数，统计3个数，创建的数量,销毁的数量和当前内存中的数量
	public long[][] everyMinuteStatNum;
	
	public ArrayList<CallTrace> callTraceList = new ArrayList<CallTrace>();
	public HashMap<String,CallTrace> callTraceMap = new HashMap<String,CallTrace>();
	
	public Int2IntMap map = new Int2IntMap();
	
	transient DefaultDiskCache cache;
	transient org.slf4j.Logger logback;
	transient org.apache.log4j.Logger log4j;
	
	//当天00：00开始的毫秒数
	transient long todayBeginTime;
	transient String todayStr;
	
	transient long lastSavedTime = System.currentTimeMillis();

	public transient ConfigItem ci;
	ObjectCreationTracker(Class cl,ConfigItem ci,DefaultDiskCache cache,org.slf4j.Logger logger,org.apache.log4j.Logger logger2){
		this.cache = cache;
		this.logback = logger;
		this.log4j = logger2;
		this.trackObjectClass = cl;
		
		this.ci = ci;
		if(ci.saveStepTime < 60*1000) ci.saveStepTime = 60*1000;
		
		if(cache != null && ci.enableHistoryData){
			todayStr = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
			todayBeginTime = DateUtil.parseDate(todayStr+" 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
			
			String key = trackObjectClass.getName()+"#" + todayStr + "#" +"everyMinuteStatNum"; 
			everyMinuteStatNum = (long[][])cache.get(key);
			if(everyMinuteStatNum == null){
				everyMinuteStatNum = new long[24*60][3];
			}
			if(ci.enableTrackCallTrace){
				key = trackObjectClass.getName()+"#"+ todayStr + "#" + "CallTrace";
				ArrayList<StackTraceElement[]> al = (ArrayList<StackTraceElement[]>)cache.get(key);
				if(al == null) al = new ArrayList<StackTraceElement[]>();
				for(int i = 0 ; i < al.size() ;i++){
					CallTrace ct = new CallTrace();
					ct.elements = al.get(i);
					ct.callTraceIndex = callTraceList.size();
					callTraceList.add(ct);
					callTraceMap.put(CallTrace.toString(ct.elements), ct);
					
					
					key = trackObjectClass.getName()+"#" + todayStr + "#"+ "everyMinuteStatNum" + "#" + i + "#" ;
					ct.everyMinuteStatNum = (long[][])cache.get(key); 
					if(ct.everyMinuteStatNum == null) {
						ct.everyMinuteStatNum = new long[24*60][3]; 
					}
					
				}
			}
		}else{
			todayStr = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
			todayBeginTime = DateUtil.parseDate(todayStr+" 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
			everyMinuteStatNum = new long[24*60][3];
		}
	}
	
	/**
	 * 
	 * 
	 * @param day
	 * @param index 0 每分钟创建的数量， 1 1分钟内存量的最大值，2标识每分钟销毁的数量
	 * @return
	 */
	public long[] getStatNumByeveryMinute(String day,int index){
		if(day.equals(todayStr)){
			long[] r = new long[24*60];

			for(int i = 0 ; i < r.length ; i++){
				r[i] = everyMinuteStatNum[i][index];
			}
			return r;
		}else{
			String key = trackObjectClass.getName()+"#" + day + "#"+ "everyMinuteStatNum"; 
			if(cache != null){
				long[][] data = (long[][])cache.get(key);
				long[] r = new long[24*60];
				for(int i = 0 ; i < r.length ; i++){
					if(data != null && i < data.length)
					r[i] = data[i][index];
				}
				return r;
			}
			return  new long[24*60];
		}
	}
	
	/**
	 * 
	 * 
	 * @param day
	 * @param index 0 每分钟创建的数量， 1 1分钟内存量的最大值，2标识每分钟销毁的数量
	 * @return
	 */
	public long[] getCallTraceStatNumByeveryMinute(int calltrace,String day,int index){
		
		if(day.equals(todayStr)){
			CallTrace ct = this.callTraceList.get(calltrace);
			long[] r = new long[24*60];
			for(int i = 0 ; i < r.length ; i++){
				r[i] = ct.everyMinuteStatNum[i][index];
			}
			return r;
		}else{
			String key = trackObjectClass.getName()+"#" + day + "#"+ "everyMinuteStatNum" + "#" + calltrace + "#" ;
			if(cache != null){
				long[][] data = (long[][])cache.get(key);
				long[] r = new long[24*60];
				for(int i = 0 ; i < r.length ; i++){
					if(data != null && i < data.length)
					r[i] = data[i][index];
				}
				return r;
			}
			return  new long[24*60];
		}
	}
	
	void heartbeat(){
		long now = System.currentTimeMillis();
		synchronized(this){
			int m = (int)(now - todayBeginTime)/60000;
			if(m>=24*60){
				switch_to_newday();
				m = (int)(now - todayBeginTime)/60000;
			}
			if(everyMinuteStatNum[m][1] < currentNumInMem){
				everyMinuteStatNum[m][1] = currentNumInMem;
			}
			for(int i = 0 ; i < callTraceList.size() ;i++){
				CallTrace ct = callTraceList.get(i);
				if(ct.everyMinuteStatNum[m][1] < ct.currentNumInMem){
					ct.everyMinuteStatNum[m][1] = ct.currentNumInMem;
				}
			}
		}
		if(now - lastSavedTime > ci.saveStepTime){
			save();
		}
	}
	
	void switch_to_newday(){
		String key = trackObjectClass.getName()+"#" + todayStr + "#"+ "everyMinuteStatNum"; 
		if(cache != null && ci.enableHistoryData){
			cache.put(key, everyMinuteStatNum);
		}
		everyMinuteStatNum = new long[24*60][3];
	
		ArrayList<StackTraceElement[]> al = new ArrayList<StackTraceElement[]>();
		for(int i = 0 ; i < callTraceList.size() ;i++){
			CallTrace ct = callTraceList.get(i);
			al.add(ct.elements);
			key = trackObjectClass.getName()+"#" + todayStr + "#"+ "everyMinuteStatNum" + "#" + i + "#" ;
			if(cache != null&& ci.enableHistoryData){
				cache.put(key, ct.everyMinuteStatNum);
			}
			ct.everyMinuteStatNum = new long[24*60][3]; 
		}
		key = trackObjectClass.getName()+"#" + todayStr + "#"+ "CallTrace";
		if(cache != null&& ci.enableHistoryData){
			cache.put(key, al);
		}
		todayStr = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
		todayBeginTime = DateUtil.parseDate(todayStr+" 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
		lastSavedTime = System.currentTimeMillis();
	}
	
	void save(){
		lastSavedTime = System.currentTimeMillis();
		
		String key = trackObjectClass.getName()+"#" + todayStr + "#"+ "everyMinuteStatNum"; 
		if(cache != null && ci.enableHistoryData){
			cache.put(key, everyMinuteStatNum);
		}
		ArrayList<StackTraceElement[]> al = new ArrayList<StackTraceElement[]>();
		for(int i = 0 ; i < callTraceList.size() ;i++){
			CallTrace ct = callTraceList.get(i);
			al.add(ct.elements);
			key = trackObjectClass.getName()+"#" + todayStr + "#"+ "everyMinuteStatNum" + "#" + i + "#" ;
			if(cache != null && ci.enableHistoryData){
				cache.put(key, ct.everyMinuteStatNum);
			}
		}
		key = trackObjectClass.getName()+"#" + todayStr + "#"+ "CallTrace";
		if(cache != null&& ci.enableHistoryData){
			cache.put(key, al);
		}
		
	}
	
	void object_destroy(int hashcode){
		long now = System.currentTimeMillis();
		
		int calltrace = -1;
		synchronized(this){
			int m = (int)(now - todayBeginTime)/60000;
			if(m>=24*60){
				switch_to_newday();
				m = (int)(now - todayBeginTime)/60000;
			}
			currentNumInMem--;
			everyMinuteStatNum[m][2]++;
		
			if(ci.enableTrackCallTrace){
				if(map.containsKey(hashcode)){
					calltrace = map.get(hashcode);
					CallTrace ct = callTraceList.get(calltrace);
					ct.currentNumInMem--;
					ct.everyMinuteStatNum[m][2]++;
					
				}else{
					if(ci.enableLogger && logback != null && logback.isInfoEnabled()){
						logback.info("[object_tracker_miss_calltrace] ["+trackObjectClass.getName()+"#"+hashcode+"] [seq:"+totalCreatedNum+"] [num:"+currentNumInMem+"] [trace:"+calltrace+"]");
					}
					
					if(ci.enableLogger && log4j != null && log4j.isInfoEnabled()){
						log4j.info("[object_tracker_miss_calltrace] ["+trackObjectClass.getName()+"#"+hashcode+"] [seq:"+totalCreatedNum+"] [num:"+currentNumInMem+"] [trace:"+calltrace+"]");
					}
				}
				map.remove(hashcode);
			}
		}
		
		if(ci.enableLogger && logback != null && logback.isDebugEnabled()){
			logback.debug("[object_tracker_destroy] ["+trackObjectClass.getName()+"#"+hashcode+"] [seq:"+totalCreatedNum+"] [num:"+currentNumInMem+"] [trace:"+calltrace+"]");
		}
		
		if(ci.enableLogger && log4j != null && log4j.isDebugEnabled()){
			log4j.debug("[object_tracker_destroy] ["+trackObjectClass.getName()+"#"+hashcode+"] [seq:"+totalCreatedNum+"] [num:"+currentNumInMem+"] [trace:"+calltrace+"]");
		}
		
		if(now - lastSavedTime > ci.saveStepTime){
			save();
		}
	}
	
	void object_create(int hashcode){
		long now = System.currentTimeMillis();
		
		int calltrace = -1;
		synchronized(this){
			int m = (int)(now - todayBeginTime)/60000;
			if(m>=24*60){
				switch_to_newday();
				m = (int)(now - todayBeginTime)/60000;
			}
			
			if(ci.enableTrackCallTrace){
				StackTraceElement[] eles = Thread.currentThread().getStackTrace();
				
				String key = CallTrace.toString(eles);
				CallTrace ct = callTraceMap.get(key);
				if(ct == null){
					ct = new CallTrace();
					ct.elements = eles;
					ct.everyMinuteStatNum = new long[24*60][3]; 
					ct.callTraceIndex = callTraceList.size();
					callTraceList.add(ct);
					callTraceMap.put(key, ct);
				}
				calltrace = ct.callTraceIndex;
				ct.totalCreatedNum++;
				ct.currentNumInMem++;
				if(ct.peakNumInMem < ct.currentNumInMem) ct.peakNumInMem = ct.currentNumInMem;
				
				ct.everyMinuteStatNum[m][0]++;
				if(ct.everyMinuteStatNum[m][1] < ct.currentNumInMem){
					ct.everyMinuteStatNum[m][1] = ct.currentNumInMem;
				}
				map.put(hashcode, calltrace);
			}
			
			totalCreatedNum++;
			currentNumInMem++;
			if(peakNumInMem < currentNumInMem) peakNumInMem = currentNumInMem;
			
			everyMinuteStatNum[m][0]++;
			if(everyMinuteStatNum[m][1] < currentNumInMem){
				everyMinuteStatNum[m][1] = currentNumInMem;
			}
		}
		
		if(ci.enableLogger && logback != null && logback.isDebugEnabled()){
			logback.debug("[object_tracker_create] ["+trackObjectClass.getName()+"#"+hashcode+"] [seq:"+totalCreatedNum+"] [num:"+currentNumInMem+"] [trace:"+calltrace+"]");
		}
		if(ci.enableLogger && log4j != null && log4j.isDebugEnabled()){
			log4j.debug("[object_tracker_create] ["+trackObjectClass.getName()+"#"+hashcode+"] [seq:"+totalCreatedNum+"] [num:"+currentNumInMem+"] [trace:"+calltrace+"]");
		}
		
		if(now - lastSavedTime > ci.saveStepTime){
			save();
		}
	}
	
}
