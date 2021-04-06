package com.fy.gamegateway.mieshi.server;

import java.io.*;

import java.util.*;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.DateUtil;

public class GateWayHandleOverTimeInfoManager{
	
	static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);
	
	private static GateWayHandleOverTimeInfoManager self;
	
	public static GateWayHandleOverTimeInfoManager getInstance(){
		return self;
	}
	String cacheFile ;

	DefaultDiskCache cache ;

	public void init(){
		cache = new DefaultDiskCache(new File(cacheFile),"协议处理超时",10L*365*24*3600*1000L);
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

	public synchronized void notifyGateWayHandleOvertInfo(String serverName,String messName,long costTime){
		String day = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
		HashMap<String,GateWayHandleOverTimeInfo> map = (HashMap<String,GateWayHandleOverTimeInfo>)cache.get(day);
		if(map == null){
			map = new HashMap<String,GateWayHandleOverTimeInfo>();
		}
		GateWayHandleOverTimeInfo bi = map.get(serverName);
		if(bi == null){
			bi = new GateWayHandleOverTimeInfo();
		}
		map.put(serverName, bi);
		bi.serverName = serverName;
		int k = -1;
		if( costTime>= 1000 && costTime < 5000){
			k = 0;
		}else if(costTime < 10000){
			k = 1;
		}else if(costTime < 30000){
			k = 2;
		}else if(costTime >= 30000){
			k = 3;
		}
		if(k >= 0){
			bi.delayTimes[k]++;
//			bi.totalDelayTime[k] += costTime;
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
			HashMap<String,GateWayHandleOverTimeInfo> map = (HashMap<String,GateWayHandleOverTimeInfo>)cache.get(dayList.get(i));
			int dd = 0;
			if(map != null){
				GateWayHandleOverTimeInfo bi = map.get(serverName);
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
