package com.fy.engineserver.util;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 统计服务器启动消耗
 * 
 * 
 */
public class ServiceStartRecord {

	private static Map<Class<?>, ServiceStartTime> startTimeMap = new LinkedHashMap<Class<?>, ServiceStartTime>();

	private static DiskCache diskCache = null;

	private String recordFilePath;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static int loadNums;
	
	private static ServiceStartRecord instance;

	private ServiceStartRecord() {

	}
	public static String dataKey = "STARTKEY";
	public static ServiceStartRecord getInstance() {
		return instance;
	}
	public static int startNum = 0; 
	public static void startLog(Object obj) {
		startNum++;
		Integer num = (Integer)diskCache.get(dataKey);
		if(loadNums == 0 && num != null){
			loadNums = num.intValue();;
		}
		
		if(obj.getClass().toString().contains("ServerStartStatus") && loadNums != startNum){
			System.out.println("=============重新计算loadNums："+loadNums+"---startNum:"+startNum);
			diskCache.put(dataKey, (Serializable) startNum);
		}
		
		GameConstants constants = GameConstants.getInstance();
		String serverName = "--";
		if (constants != null) {
			serverName = constants.getServerName();
		}
		System.out.println("["+sdf.format(new Date())+ "] [服务器启动:"+serverName+"] [加载进度：" + startNum +"/"+loadNums+ "] [" + obj.getClass().getSimpleName() + "]");
	}

	public static void print() {
		Iterator<Class<?>> itor = startTimeMap.keySet().iterator();
		while (itor.hasNext()) {
			Class<?> clazz = itor.next();
			ServiceStartTime time = startTimeMap.get(clazz);
			MemoryControler.logger.warn("[service start] [class:" + clazz.getName() + "] [cost:" + (time.getEndTime() - time.getStartTime()) + "]");
		}
	}

	public static Map<Class<?>, ServiceStartTime> getStartTimeMap() {
		return startTimeMap;
	}

	public static void setStartTimeMap(Map<Class<?>, ServiceStartTime> startTimeMap) {
		ServiceStartRecord.startTimeMap = startTimeMap;
	}

	public String getRecordFilePath() {
		return recordFilePath;
	}

	public void setRecordFilePath(String recordFilePath) {
		this.recordFilePath = recordFilePath;
	}

	public void init() {
		instance = this;
		File recordFile = new File(getRecordFilePath());
		diskCache = new DefaultDiskCache(recordFile, null, 1000 * 60 * 60 * 24 * 365L);
	}
}
