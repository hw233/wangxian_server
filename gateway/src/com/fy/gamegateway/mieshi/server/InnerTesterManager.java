package com.fy.gamegateway.mieshi.server;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class InnerTesterManager {
	static Logger logger = Logger.getLogger(MieshiGatewayServer.class);
	private static InnerTesterManager self;
	
	private InnerTesterManager(){
		
	}
	
	public static InnerTesterManager getInstance(){
		return self;
	}
	
	String cacheFile ;
	DefaultDiskCache cache ;
	
	public static final String allKey = "allKey";
	
	private ArrayList<String> keys = new ArrayList<String>();
	
	public boolean valid = true;
	
	public void init(){
		
		cache = new DefaultDiskCache(new File(cacheFile),"mieshi-inner-tester-info-cache",10L*365*24*3600*1000L);
		
		cache.setMaxDiskSize(10L*1024*1024*1024);
		cache.setMaxMemorySize(64*1024*1024);
		cache.setMaxElementNum(1024*1024*1024);
		
		self = this;
	}
	
	public boolean isInnerTester(String clientId){
		if(!valid){
			return false;
		}
		InnerTesterInfo iti = (InnerTesterInfo)cache.get(clientId);
		if(iti != null && iti.getState() == 0){
			return true;
		}
		return false;
	}
	
	public InnerTesterInfo getInnerTesterInfo(String clientId){
		return (InnerTesterInfo)cache.get(clientId);
	}
	
	/**
	 * 更新内部测试人员的信息
	 * 
	 * @param clientId
	 * @param actionType 0为更新，1为删除
	 */
	public void updateMieshiInnerTesterInfo(String clientId, String name, byte state, int actionType){
		InnerTesterInfo iti = (InnerTesterInfo)cache.get(clientId);
		ArrayList<String> keys = (ArrayList)cache.get(allKey);
		if(keys == null){
			keys = new ArrayList<String>();
		}
		if(actionType != 1){
			if(iti == null){
				iti = new InnerTesterInfo();
				iti.setClientId(clientId);
				iti.setTesterName(name);
				if(!keys.contains(clientId)){
					keys.add(clientId);
				}
			}
			iti.setTesterName(name);
			iti.setState(state);
			cache.put(clientId, iti);
			cache.put(allKey, keys);
		}else{
			if(iti != null){
				cache.remove(clientId);
				if(keys.contains(clientId)){
					keys.remove(clientId);
				}
				cache.put(allKey, keys);
			}
		}
		logger.warn("[更新网关内部测试人员信息] ["+clientId+"] ["+name+"] ["+state+"] [actionType:"+actionType+"]");
	}
	
	/**
	 * 更新内部测试人员的信息
	 * 
	 * @param clientId
	 * @param actionType 0为更新，1为删除
	 */
	public void updateMieshiInnerTesterInfo(String clientId, byte state, int actionType){
		InnerTesterInfo iti = (InnerTesterInfo)cache.get(clientId);
		ArrayList<String> keys = (ArrayList)cache.get(allKey);
		if(keys == null){
			keys = new ArrayList<String>();
		}
		if(actionType != 1){
			if(iti == null){
				iti = new InnerTesterInfo();
				iti.setClientId(clientId);
				iti.setTesterName("");
				if(!keys.contains(clientId)){
					keys.add(clientId);
				}
			}
			iti.setState(state);
			cache.put(clientId, iti);
			cache.put(allKey, keys);
		}else{
			if(iti != null){
				cache.remove(clientId);
				if(keys.contains(clientId)){
					keys.remove(clientId);
				}
				cache.put(allKey, keys);
			}
		}
		logger.warn("[更新网关内部测试人员信息] ["+clientId+"] ["+state+"] [actionType:"+actionType+"]");
	}

	public String getCacheFile() {
		return cacheFile;
	}

	public void setCacheFile(String cacheFile) {
		this.cacheFile = cacheFile;
	}

	public DefaultDiskCache getCache() {
		return cache;
	}

	public void setCache(DefaultDiskCache cache) {
		this.cache = cache;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public InnerTesterInfo[] getAllInnerTesterInfos(){
		ArrayList<String> keys = (ArrayList)cache.get(allKey);
		ArrayList<InnerTesterInfo> innerTesterList = new ArrayList<InnerTesterInfo>();
		if(keys != null){
			for(String key : keys){
				InnerTesterInfo info = (InnerTesterInfo)cache.get(key);
				if(info != null){
					innerTesterList.add(info);
				}
			}
		}
		return innerTesterList.toArray(new InnerTesterInfo[0]);
	}
}
