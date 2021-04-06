package com.fy.boss.finance.service;

import java.io.File;

import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.service.SavingForbidManager;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class SavingForbidManager {
	public static Logger logger = Logger.getLogger(SavingForbidManager.class);
	
	private String cacheFile;
	
	private static SavingForbidManager self;
	
	public static SavingForbidManager getInstance() {
		return self;
	}
	
	public DiskCache cache = null;
	
	public void init() {
		cache = new DefaultDiskCache(new File(cacheFile),"充值禁止",10*365*24*60*60*1000);
		self = this;
		System.out.println("[初始化充值禁止服务] [禁止数量:"+cache.getNumElements()+"]");
	}
	
	public String getCacheFile() {
		return cacheFile;
	}

	public void setCacheFile(String cacheFile) {
		this.cacheFile = cacheFile;
	}

	public DiskCache getCache() {
		return cache;
	}

	public void setCache(DiskCache cache) {
		this.cache = cache;
	}

	/**
	 * 增加禁止充值，code为设备号，days为禁止天数
	 * @param code
	 * @param days
	 */
	public void addForbid(String code, int days) {
		long forbidEndTime = System.currentTimeMillis() + days*24*60*60*1000;
		cache.put(code, forbidEndTime);
		logger.info("[增加禁止充值] [code:"+code+"] [days:"+days+"] [elemNum:"+cache.getNumElements()+"]");
	}
	
	/**
	 * 删除禁止
	 * @param code
	 */
	public void removeForbid(String code) {
		cache.remove(code);
		logger.info("[删除禁止充值] [code:"+code+"] [elemNum:"+cache.getNumElements()+"]");
	}
	
	/**
	 * 是否禁止
	 * @param code
	 * @return
	 */
	public boolean isForbid(Passport passport, String code) {
		long start = System.currentTimeMillis();
		if(code == null) {
			return false;
		}
		Long endTime = (Long)cache.get(code);
		if(endTime != null) {
			if(endTime > System.currentTimeMillis()) {
				logger.info("[检查玩家是否被禁止充值] [是] ["+passport.getUserName()+"] [deviceCode:"+code+"] ["+(System.currentTimeMillis()-start)+"ms]");
				return true;
			} else {
				cache.remove(code);
			}
		}
		logger.info("[检查玩家是否被禁止充值] [否] ["+passport.getUserName()+"] [deviceCode:"+code+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return false;
	}
}
