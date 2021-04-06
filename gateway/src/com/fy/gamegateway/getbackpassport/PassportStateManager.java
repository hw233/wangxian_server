package com.fy.gamegateway.getbackpassport;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class PassportStateManager {

	static Logger logger = Logger.getLogger(RecordManager.class);
	
	static PassportStateManager self;
	
	public static PassportStateManager getInstance(){
		return self;
	}
	
	public SimpleEntityManager<PassportState> em4PassportStateManager = null;
	
	public void init() throws Exception{
		em4PassportStateManager = SimpleEntityManagerFactory.getSimpleEntityManager(PassportState.class);
		self = this;
	}
	
	public void update(String username,long date){
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em4PassportStateManager.queryIds(PassportState.class, " username = '" +username+"'");
			PassportState ps = em4PassportStateManager.find(ids[0]);
			long oldvalue = ps.getValue();
			long oldDate = ps.getLastUpdateTime();
			ps.setValue(oldvalue+1);
			ps.setLastUpdateTime(date);
			
			em4PassportStateManager.flush(ps);
			if (logger.isInfoEnabled()) {
				logger.info("[更新密保记录stat] [成功] [oldvalue:"+oldvalue+"] [newDate:"+date+"] [oldDate:"+oldDate+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("[更新密保记录stat] [异常] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}
	
	public void save(PassportState passportState){
		long startTime = System.currentTimeMillis();
		try {
			
			long id = em4PassportStateManager.nextId();
			passportState.setId(id);
			em4PassportStateManager.flush(passportState);
			if (logger.isInfoEnabled()) {
				logger.info("[保存密保记录stat] [成功] [username:"+passportState.getUsername()+"] [value:"+passportState.getValue()+"] [date:"+passportState.getLastUpdateTime()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("[保存密保记录stat] [异常] [username:"+passportState.getUsername()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}
	
	public PassportState getPassportState(String username){
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em4PassportStateManager.queryIds(PassportState.class, " username = '" +username+"'");
			PassportState ps = em4PassportStateManager.find(ids[0]);
			if (logger.isInfoEnabled()) {
				logger.info("[获得密保记录stat] [成功] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return ps;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[获得密保记录stat] [异常] [username:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}
	
	public static boolean isSameDay(long time1,long time2){
		Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(time1);
		int year1=ca.get(Calendar.YEAR);
		int month1=ca.get(Calendar.MONTH);
		int day1=ca.get(Calendar.DAY_OF_MONTH);
		
		ca.setTimeInMillis(time2);
		int year2=ca.get(Calendar.YEAR);
		int month2=ca.get(Calendar.MONTH);
		int day2=ca.get(Calendar.DAY_OF_MONTH);
		
		return year1==year2&&month1==month2&&day1==day2; 
	}
	
}
