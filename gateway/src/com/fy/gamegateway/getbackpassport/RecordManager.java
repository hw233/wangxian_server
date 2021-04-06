package com.fy.gamegateway.getbackpassport;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * 密保记录管理
 * @author wtx
 *
 */
public class RecordManager {
	
	static Logger logger = Logger.getLogger(RecordManager.class);
	
	static RecordManager self;
	
	public static RecordManager getInstance(){
		return self;
	}
	
	public SimpleEntityManager<PassportRecord> em4RecordManager = null;
	
	public void init() throws Exception{
		em4RecordManager = SimpleEntityManagerFactory.getSimpleEntityManager(PassportRecord.class);
		self = this;
	}
	
	//分页获得所有密保记录
	public List<PassportRecord> getPassportRecords(int start, int len){
		long startTime = System.currentTimeMillis();
		start = start + 1;
		try {
			List<PassportRecord> lst = em4RecordManager.query(PassportRecord.class,"", "committime desc", start, start + len );
			if(logger.isInfoEnabled())
				logger.info("[分页获取所有密保记录] [成功] [数量:"+lst.size()+"] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return lst;
		} catch (Exception e) {
			logger.error("[分页获取所有密保记录] [失败] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return new ArrayList<PassportRecord>();
	}
	
	//保存一条新的密保记录
	public void savePassportRecord(PassportRecord p){
		long startTime = System.currentTimeMillis();
		try {
			if (getPassportRecordById(p.getId())==null) {
				long id = em4RecordManager.nextId();
				p.setId(id);
			}
			
			em4RecordManager.flush(p);
			if(logger.isInfoEnabled())
				logger.info("[存储密保记录] [成功] [id:"+p.getId()+"] [stat:"+p.getState()+"] [passportid:"+p.getPassportid()+"] [username:"+p.getUsername()+"] [telnumber:"+p.getTelnumber()+"] [playername:"+p.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[存储密保记录] [失败] [id:"+p.getId()+"] [stat:"+p.getState()+"] [passportid:"+p.getPassportid()+"] [username:"+p.getUsername()+"] [telnumber:"+p.getTelnumber()+"] [playername:"+p.getName()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}
	
	//获得记录总数
	public long getRecordAmounts(){
		long startTime = System.currentTimeMillis();
		long num = 0;
		try {
			num = em4RecordManager.count(PassportRecord.class, "");
			if(logger.isInfoEnabled())
				logger.info("[获得所有密保记录数量] [成功] [数量:"+num+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return num;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[获得所有密保记录数量] [异常] [数量:"+num+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return num;
		}
	}
	
	
	public PassportRecord getPassportRecordById(long id){
		long startTime = System.currentTimeMillis();
		try {
			PassportRecord pr = em4RecordManager.find(id);
			if(logger.isInfoEnabled())
				logger.info("[通过id获得密保记录] [成功] [id:"+id+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return pr;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[通过id获得密保记录] [异常] [id:"+id+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		}
		return null;
	}
	
	public List<PassportRecord> getPassportRecordByTel(String telnumber,int start,long len){
		long startTime = System.currentTimeMillis();
		
		try {
			List<PassportRecord> pr = em4RecordManager.query(PassportRecord.class, " telnumber = "+telnumber, "committime desc", start, start + len );
			if(logger.isInfoEnabled())
			logger.info("[通过电话获得所有密保记录] [成功] [电话:"+telnumber+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return pr;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[通过电话获得所有密保记录] [异常] [电话:"+telnumber+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}
	
	public List<PassportRecord> getPassportRecordByStat(String stat,int start,long len){
		long startTime = System.currentTimeMillis();
		
		try {
			List<PassportRecord> pr = em4RecordManager.query(PassportRecord.class, " state = ?", new Object[]{stat}, "committime desc", start, start + len);
//			List<PassportRecord> pr = em4RecordManager.query(PassportRecord.class, " state = "+stat, "committime desc", start, start + len );
			if(logger.isInfoEnabled())
			logger.info("[通过状态获得所有密保记录] [成功] [状态:"+stat+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return pr;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[通过状态获得所有密保记录] [异常] [状态:"+stat+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}

	public List<PassportRecord> getPassportRecordByusername(String username,int start,long len){
		long startTime = System.currentTimeMillis();
		
		try {
			List<PassportRecord> pr =em4RecordManager.query(PassportRecord.class, " username = ?" , new Object[]{username}, "committime desc", start, start + len );
			//List<PassportRecord> pr = em4RecordManager.query(PassportRecord.class, " username = "+username, "committime desc", start, start + len );
			if(logger.isInfoEnabled())
			logger.info("[通过用户名获得所有密保记录] [成功] [用户名:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return pr;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[通过用户名获得所有密保记录] [异常] [用户名:"+username+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}
	
	public List<PassportRecord> getPassportRecordByquestion(String passportquestion,int start,long len){
		long startTime = System.currentTimeMillis();
		
		try {
			List<PassportRecord> pr = em4RecordManager.query(PassportRecord.class, " passportquestion = ?", new Object[]{passportquestion}, "committime desc", start, start + len);
//			List<PassportRecord> pr = em4RecordManager.query(PassportRecord.class, " passportquestion = "+passportquestion, "committime desc", start, start + len );
			if(logger.isInfoEnabled())
			logger.info("[通过密保问题获得所有密保记录] [成功] [密保问题:"+passportquestion+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return pr;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[通过密保问题获得所有密保记录] [异常] [密保问题:"+passportquestion+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}
//	public void updateValue(String clientId,int value){
//		try {
//			long startTime = System.currentTimeMillis();
//			long ids[] = em4RecordManager.queryIds(PassportRecord.class, " clientid = '" +clientId+"'");
//			PassportRecord pr = em4RecordManager.find(ids[0]);
//			long oldvalue = pr.getValue();
//			long oldDate = pr.getLastkickdate();
//			long newDate = System.currentTimeMillis();
//			pr.setValue(oldvalue+value);
//			pr.setLastkickdate(newDate);
//			if (logger.isInfoEnabled()) {
//				logger.info("[更新提交次数] [成功] [oldvalue:"+oldvalue+"] [newvalue:"+pr.getValue()+"] [newDate:"+newDate+"] [oldDate:"+oldDate+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");	
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.info("[更新提交次数] [异常] ",e);
//		}
//	}
//	
//	public static boolean isSameDay(long time1,long time2){
//		Calendar ca=Calendar.getInstance();
//		ca.setTimeInMillis(time1);
//		int year1=ca.get(Calendar.YEAR);
//		int month1=ca.get(Calendar.MONTH);
//		int day1=ca.get(Calendar.DAY_OF_MONTH);
//		
//		ca.setTimeInMillis(time2);
//		int year2=ca.get(Calendar.YEAR);
//		int month2=ca.get(Calendar.MONTH);
//		int day2=ca.get(Calendar.DAY_OF_MONTH);
//		
//		return year1==year2&&month1==month2&&day1==day2; 
//	}
//	
//	public PassportRecord getPassportRecord(long clientid){
//		try {
//			long startTime = System.currentTimeMillis();
//			long ids[] = em4RecordManager.queryIds(PassportRecord.class, " clientid = '" +clientid+"'");
//			PassportRecord ps = em4RecordManager.find(ids[0]);
//			if (logger.isInfoEnabled()) {
//				logger.info("[通过clientid获得密保记录] [成功] [clientid:"+clientid+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
//			}
//			return ps;
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.info("[通过clientid获得密保记录] [异常] ",e);
//			return null;
//		}
//	}
	
}
