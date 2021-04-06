package com.fy.boss.gm.dbserver;

import java.util.*;

import org.apache.log4j.Logger;

import com.fy.boss.gm.dbserver.DbServer;
import com.fy.boss.gm.dbserver.DbServerManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class DbServerManager {

	public SimpleEntityManager<DbServer> em;
	
	private static DbServerManager self;
	
	private static Logger logger = Logger.getLogger(DbServerManager.class.getName());;
	
	public void init(){
		self = this;
		long now = System.currentTimeMillis();
		em = SimpleEntityManagerFactory.getSimpleEntityManager(DbServer.class);
		System.out.println("[DbServerManager] [初始化] [成功] [耗时："+(System.currentTimeMillis()-now)+"ms]");
	}
	
	public static DbServerManager getInstance(){
		return self;
	}
	
	public boolean addNewDbServer(DbServer config){
		long now = System.currentTimeMillis();
		try {
			long id = em.nextId();
			config.setId(id);
			em.save(config);
			logger.warn("[添加一条新机柜的配置] [成功] [添加人："+config.getRecorder()+"] [添加时间："+config.getRecordtime()+"] [机柜名称："+config.getDbname()+"] [机柜描述："+config.getDbmess()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[添加一条新机柜的配置] [异常] [添加人："+config.getRecorder()+"] [添加时间："+config.getRecordtime()+"] [机柜名称："+config.getDbname()+"] [机柜描述："+config.getDbmess()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return false;
	}
	
	public List<DbServer> getdbservers(){
		long now = System.currentTimeMillis();
		List<DbServer> list = new ArrayList<DbServer>();
		try {
			list = em.query(DbServer.class, "", "recordtime desc", 1, 1000);
			logger.warn("[获得所有机柜的配置] [成功] [长度："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[获得所有机柜的配置] [异常] [长度："+list.size()+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		return list;
	}
	
	public void deldbservers(long id){
		long now = System.currentTimeMillis();
		try {
			DbServer dd = getdbserversbyid(id);
			if(dd!=null){
				em.remove(dd);
				logger.warn("[删除机柜的配置] [成功] [id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[删除机柜的配置] [异常] [id："+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
	}
	
	public DbServer getdbserversbyid(long id){
		long now = System.currentTimeMillis();
		List<DbServer> list = new ArrayList<DbServer>();
		try {
			list = em.query(DbServer.class, " id ="+id, "", 1, 10);
			logger.warn("[通过id获得机柜的配置] [成功] [长度："+list.size()+"] [id:"+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]");
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[通过id获得机柜的配置] [异常] [长度："+list.size()+"] [id:"+id+"] [耗时："+(System.currentTimeMillis()-now)+"ms]",e);
		}
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
}
