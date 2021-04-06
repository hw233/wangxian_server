package com.fy.gamegateway.mieshi.server.pub;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


public class ServerPubRecordManager {
	
	
	//分页查询
	
	private static final Logger logger = Logger.getLogger(ServerPubRecordManager.class);
	// property constants
	public static final Class pojoClass = ServerPubRecord.class;

	public SimpleEntityManager<ServerPubRecord> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);

	protected static ServerPubRecordManager self;

	public static ServerPubRecordManager getInstance() {
		if (self == null) {
			synchronized (ServerPubRecordManager.class) {
				if (self == null)
					self = new ServerPubRecordManager();
			}
		}
		return self;
	}

	public void initialize() throws Exception {
		long now = System.currentTimeMillis();
		self = this;
		System.out.println(this.getClass().getName() + " initialize successfully [cost:" + (System.currentTimeMillis() - now) + "ms]");
		logger.info(this.getClass().getName() + " initialize successfully [" + (System.currentTimeMillis() - now) + "]");
	}


	//存储
	
	public void saveOrUpdate(ServerPubRecord serverPubRecord)
	{
		long startTime = System.currentTimeMillis();
		try {
			if(serverPubRecord.getId() == 0l)
			{
				long id = em.nextId();
				serverPubRecord.setId(id);
			}
			em.flush(serverPubRecord);
			if(logger.isInfoEnabled())
				logger.info("[flush vipPlayerInfoRecord] [成功] "+serverPubRecord.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush vipPlayerInfoRecord] [失败] "+serverPubRecord.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}


	//根据id 查询单个
	public ServerPubRecord find(long id)
	{
		long startTime = System.currentTimeMillis();
		
		try
		{
			ServerPubRecord serverPubRecord = em.find(id);
			
			if(serverPubRecord != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询游戏活动] [成功] ["+id+"] "+serverPubRecord.getLogString()+" [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询游戏活动] [失败] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return serverPubRecord;
		}
		catch(Exception e)
		{
			logger.error("[根据id查询游戏活动] [失败] [出现异常] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}




	//根据自定义条件查询活动对象 无分页
	public List<ServerPubRecord> queryForWhere(String preparedWhereSql,Object[] params) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(ServerPubRecord.class, preparedWhereSql,params);

			List<ServerPubRecord> lst = new ArrayList<ServerPubRecord>();

			for(long id : ids)
			{
				ServerPubRecord vipPlayerInfoRecord = em.find(id);
				lst.add(vipPlayerInfoRecord);
			}

			if(logger.isInfoEnabled())
			{
				logger.info("[根据自定义条件查询] [成功] ["+preparedWhereSql+"] [size:"+lst.size()+"] [cost:"+(System.currentTimeMillis()-startTime)+"]");
			}

			return lst;
		} catch (Exception e) {
			logger.error("[根据自定义条件查询] [失败] [出现异常] ["+preparedWhereSql+"] [size:--] [cost:"+(System.currentTimeMillis()-startTime)+"]",e);
		}

		return null;
	}


	//根据自定义条件查询对象 有分页
	/**
	 * 查询从1 开始 len代表查询多少条
	 * @param whereSql
	 * @param orderBySql
	 * @param start
	 * @param len
	 * @return
	 * @throws Exception
	 */
	public List<ServerPubRecord> queryForWhere(String preparedWhereSql,Object[] params, String orderBySql, long start, long len) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {

			long []ids = em.queryIds(ServerPubRecord.class, preparedWhereSql ,params, orderBySql, start, start+len);

			List<ServerPubRecord> lst = new ArrayList<ServerPubRecord>();

			for(long id : ids)
			{
				ServerPubRecord serverPubRecord = em.find(id);
				lst.add(serverPubRecord);
			}

			if(logger.isInfoEnabled())
			{
				logger.info("[根据自定义条件带分页查询] [成功] ["+preparedWhereSql+"] ["+orderBySql+"] ["+start+"] ["+len+"] [size:"+lst.size()+"] [cost:"+(System.currentTimeMillis()-startTime)+"]");
			}

			return lst;
		} catch (Exception e) {
			logger.error("[根据自定义条件带分页查询] [失败] [出现异常] ["+preparedWhereSql+"] ["+orderBySql+"] ["+start+"] ["+len+"] [size:--] [cost:"+(System.currentTimeMillis()-startTime)+"]",e);
		}

		return null;
	}
	
	
}
