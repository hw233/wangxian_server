package com.fy.gamegateway.mieshi.server;

import java.io.*;

import java.util.*;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.DateUtil;
/**
 * 
 * 
 *
 */
public class MieshiServerHeartBeatInfoManager{
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);
	
	private static MieshiServerHeartBeatInfoManager self;
	
	public static MieshiServerHeartBeatInfoManager getInstance(){
		return self;
	}
	String cacheFile ;

	DefaultDiskCache cache ;

	public void init(){
		cache = new DefaultDiskCache(new File(cacheFile),"mieshi-server-heartbeat-info-cache",10L*365*24*3600*1000L);
		cache.setMaxDiskSize(10L*1024*1024*1024);
		cache.setMaxMemorySize(64*1024*1024);
		cache.setMaxElementNum(1024*1024*1024);
		self = this;
	}
	
	public String getCacheFile() {
		return cacheFile;
	}

	public void setCacheFile(String cacheFile) {
		this.cacheFile = cacheFile;
	}

	public synchronized void notifyHeartbeatInfo(String serverName,String mapName,long delayInMillis){
		String day = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
		HashMap<String,MieshiServerHeartBeatInfo> map = (HashMap<String,MieshiServerHeartBeatInfo>)cache.get(day);
		if(map == null){
			map = new HashMap<String,MieshiServerHeartBeatInfo>();
		}
		MieshiServerHeartBeatInfo bi = map.get(serverName);
		if(bi == null){
			bi = new MieshiServerHeartBeatInfo();
		}
		map.put(serverName, bi);
		bi.serverName = serverName;
		int k = -1;
		if( delayInMillis>= 1000 && delayInMillis < 5000){
			k = 0;
		}else if(delayInMillis < 10000){
			k = 1;
		}else if(delayInMillis < 30000){
			k = 2;
		}else if(delayInMillis >= 30000){
			k = 3;
		}
		if(k >= 0){
			bi.delayTimes[k]++;
			bi.totalDelayTime[k] += delayInMillis;
			cache.put(day, map);
		}
	}
	
	
	/**
	 * delayType: 0 表示 1~5秒超时次数
	 *            1 表示 5~10秒超时次数
	 *            2 表示 10~30秒超时次数
	 *            3 表示 30秒超时次数 
	 * @param serverName
	 * @param dayList  2012-10-01 .....
	 * @return
	 */
	public int[] getDelayTimesByDayAndServer(String serverName,int delayType,ArrayList<String> dayList){
		int ret[] = new int[dayList.size()];
		for(int i = 0 ; i < ret.length ; i++){
			HashMap<String,MieshiServerHeartBeatInfo> map = (HashMap<String,MieshiServerHeartBeatInfo>)cache.get(dayList.get(i));
			int dd = 0;
			if(map != null){
				MieshiServerHeartBeatInfo bi = map.get(serverName);
				if(bi != null){
					if(delayType >= 0 && delayType <= bi.delayTimes.length){
						dd = bi.delayTimes[delayType];
					}
				}
			}
			ret[i] = dd;
		}
		
		return ret;
	}
	

}
