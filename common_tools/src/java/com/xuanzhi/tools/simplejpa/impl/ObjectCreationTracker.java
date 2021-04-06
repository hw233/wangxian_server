package com.xuanzhi.tools.simplejpa.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.DateUtil;


/**
 * 此类是方便跟踪对象的创建
 * 
 * 需要小心的是，但有大量对象创建或者删除时，此类会占用大量内存
 * 
 *
 */
public class ObjectCreationTracker {
	
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

	
	//当天00：00开始的毫秒数
	transient long todayBeginTime;
	transient String todayStr;
	
	transient long lastSavedTime = System.currentTimeMillis();

	public transient DefaultDiskCache cache;
	
	ObjectCreationTracker(Class cl,DefaultDiskCache cache){

		this.trackObjectClass = cl;

		
		if(cache != null){
			todayStr = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
			todayBeginTime = DateUtil.parseDate(todayStr+" 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
			
			String key = trackObjectClass.getName()+"#" + todayStr + "#" +"everyMinuteStatNum"; 
			everyMinuteStatNum = (long[][])cache.get(key);
			if(everyMinuteStatNum == null){
				everyMinuteStatNum = new long[24*60][3];
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
			
		}
		if(now - lastSavedTime > 300 * 1000){
			save();
		}
	}
	
	void switch_to_newday(){
		String key = trackObjectClass.getName()+"#" + todayStr + "#"+ "everyMinuteStatNum"; 
		if(cache != null){
			cache.put(key, everyMinuteStatNum);
		}
		everyMinuteStatNum = new long[24*60][3];
	
		
		todayStr = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
		todayBeginTime = DateUtil.parseDate(todayStr+" 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
		lastSavedTime = System.currentTimeMillis();
	}
	
	void save(){
		lastSavedTime = System.currentTimeMillis();
		
		String key = trackObjectClass.getName()+"#" + todayStr + "#"+ "everyMinuteStatNum"; 
		if(cache != null){
			cache.put(key, everyMinuteStatNum);
		}
		
	}
	
	void object_destroy(){
		long now = System.currentTimeMillis();

		synchronized(this){
			int m = (int)(now - todayBeginTime)/60000;
			if(m>=24*60){
				switch_to_newday();
				m = (int)(now - todayBeginTime)/60000;
			}
			currentNumInMem--;
			everyMinuteStatNum[m][2]++;
		
			
		}
		
		
		
		if(now - lastSavedTime > 300 * 1000){
			save();
		}
	}
	
	void object_create(){
		long now = System.currentTimeMillis();
		
		synchronized(this){
			int m = (int)(now - todayBeginTime)/60000;
			if(m>=24*60){
				switch_to_newday();
				m = (int)(now - todayBeginTime)/60000;
			}

			totalCreatedNum++;
			currentNumInMem++;
			if(peakNumInMem < currentNumInMem) peakNumInMem = currentNumInMem;
			
			everyMinuteStatNum[m][0]++;
			if(everyMinuteStatNum[m][1] < currentNumInMem){
				everyMinuteStatNum[m][1] = currentNumInMem;
			}
		}
		
	
		
		if(now - lastSavedTime > 300 * 1000){
			save();
		}
	}
	
}
