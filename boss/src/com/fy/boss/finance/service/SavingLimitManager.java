package com.fy.boss.finance.service;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.SavingLimitManager;
public class SavingLimitManager {
	public static Logger logger = Logger.getLogger(SavingLimitManager.class);
	
	public static HashMap<String, Long> limitMap  = new HashMap<String,Long>();
	
	static {
		limitMap.put("APPSTORE_XUNXIAN", 588000L);
	}
	
	private static SavingLimitManager self;
	
	public static SavingLimitManager getInstance() {
		if(self == null) {
			self = new SavingLimitManager();
		}
		return self;
	}
	
	/**
	 * 是否已到今日上限
	 * @param channel
	 * @param daySavingAmount
	 */
	public boolean isLimit(Passport passport, String code, String channel) {
		long start = System.currentTimeMillis();
		if(channel == null) {
			return false;
		}
		Long limit = limitMap.get(channel);
		if(limit != null) {
			long amount = OrderFormManager.getInstance().getDeviceTodaySavingAmount(code);
			if(amount >= limit) {
				logger.info("[检查用户充值是否查过限额] [被限费:"+amount+">="+limit+"] ["+passport.getUserName()+"] ["+code+"] ["+channel+"] ["+(System.currentTimeMillis()-start)+"ms]");
				return true;
			}
		}
		logger.info("[检查用户充值是否查过限额] [否] ["+passport.getUserName()+"] ["+code+"] ["+channel+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return false;
	}
}
