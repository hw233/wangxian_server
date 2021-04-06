package com.fy.boss.finance.service;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.fy.boss.authorize.model.Passport;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.SavingPeriodManager;

public class SavingPeriodManager {
	public static Logger logger = Logger.getLogger(SavingPeriodManager.class);
	
	public int 五分钟限次 = 3;
	public int 一小时限次 = 6;
	
	private static SavingPeriodManager self;
	
	public static SavingPeriodManager getInstance() {
		if(self == null) {
			self = new SavingPeriodManager();
		}
		return self;
	}
	
	/**
	 * 是否频率过快
	 * @param channel
	 * @param daySavingAmount
	 */
	public boolean isPeriodPermit(Passport passport, String code) {
		Calendar cal = Calendar.getInstance();
		long end = cal.getTimeInMillis();
		cal.add(Calendar.MINUTE, -5);
		long startMin = cal.getTimeInMillis();
		cal.add(Calendar.MINUTE,  -55);
		long startHour = cal.getTimeInMillis();
		long count = 0;
		try {
			count = OrderFormManager.getInstance().getPeriodSavingCount(code, startMin, end);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("检查玩家充值频率是否过快时异常", e);
		}
		if(count >= 五分钟限次) {
			logger.warn("[检查玩家充值频率] [超过五分钟限:"+count+">="+五分钟限次+"] ["+code+"] ["+(System.currentTimeMillis()-end)+"ms]");
			return false;
		} 
		try {
			count = OrderFormManager.getInstance().getPeriodSavingCount(code, startHour, end);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("检查玩家充值频率是否过快时异常", e);
		}
		if(count >= 一小时限次) {
			logger.warn("[检查玩家充值频率] [超过一小时限:"+count+">="+一小时限次+"] ["+code+"] ["+(System.currentTimeMillis()-end)+"ms]");
			return false;
		}
		logger.info("[检查玩家充值频率] [正常] ["+code+"] ["+(System.currentTimeMillis()-end)+"ms]");
		return true;
	}
}
