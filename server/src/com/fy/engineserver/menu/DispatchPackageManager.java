package com.fy.engineserver.menu;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class DispatchPackageManager {
	static DispatchPackageManager instance;
	DefaultDiskCache ddc;
	File dataFile;
	
	public DefaultDiskCache getDdc() {
		return ddc;
	}

	public void setDdc(DefaultDiskCache ddc) {
		this.ddc = ddc;
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	public static DispatchPackageManager getInstance(){
		return instance;
	}
	
	public void init() throws Exception{
		instance = this;
		
		ddc = new DefaultDiskCache(dataFile, null,
				"DispatchPackageManager", 100L * 365 * 24 * 3600 * 1000L);
	}
	
	public boolean isDailyPackageDispatched(long playerId , String packageName , Date date){
		DispatchPackageRecord record = (DispatchPackageRecord)ddc.get(playerId);
		if(record == null){
			return false;
		}
		
		Date d = record.dailyRecords.get(packageName);
		if(d == null){
			return false;
		}
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d);
		
		int y1 = c1.get(Calendar.YEAR);
		int m1 = c1.get(Calendar.MONTH);
		int d1 = c1.get(Calendar.DAY_OF_MONTH);
		
		c1.set(y1, m1, d1, 0, 0, 0);
		
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date);
		
		int y2 = c2.get(Calendar.YEAR);
		int m2 = c2.get(Calendar.MONTH);
		int d2 = c2.get(Calendar.DAY_OF_MONTH);
		
		c2.set(y2, m2, d2, 0, 0, 0);
		
		int r = c1.compareTo(c2);
		return r >= 0;
	}
	
	public boolean isLevelLimitPackageDispatched(long playerId , String packageName , int level){
		DispatchPackageRecord record = (DispatchPackageRecord)ddc.get(playerId);
		if(record == null){
			return false;
		}
		
		Set<Integer> set = record.levelRecords.get(packageName);
		if(set == null){
			return false;
		}
		
		return set.contains(level);
	}
	
	public void recordDailyDispatchedPackage(long playerId , String packageName , Date date){
		DispatchPackageRecord record = (DispatchPackageRecord)ddc.get(playerId);
		
		if(record == null){
			record = new DispatchPackageRecord();
			record.setPlayerId(playerId);
			
			ddc.put(playerId , record);
		}
		
		record.dailyRecords.put(packageName, date);
		
		ddc.put(playerId, record);
	}
	
	public void recordLevelLimitDispatchedPackage(long playerId , String packageName , int level){
		DispatchPackageRecord record = (DispatchPackageRecord)ddc.get(playerId);
		
		if(record == null){
			record = new DispatchPackageRecord();
			record.setPlayerId(playerId);
			
			ddc.put(playerId , record);
		}
		
		Set<Integer> set = record.levelRecords.get(packageName);
		if(set == null){
			set = new HashSet<Integer>();
			record.levelRecords.put(packageName, set);
		}
		
		set.add(level);
		
		ddc.put(playerId, record);
	}
}
