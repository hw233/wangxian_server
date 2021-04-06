package com.fy.boss.vip.platform;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.vip.platform.model.VipNewPlayerInfoRecord;
import com.fy.boss.vip.platform.model.VipPlayerInfoRecord;
import com.fy.boss.vip.platform.VipPlayerInfoManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


public class VipPlayerInfoManager {
	
	
	//分页查询
	
	private static final Logger logger = Logger.getLogger(VipPlayerInfoManager.class);
	// property constants
	public static final Class pojoClass = VipPlayerInfoRecord.class;

	public SimpleEntityManager<VipPlayerInfoRecord> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);
	public SimpleEntityManager<VipNewPlayerInfoRecord> em2 = SimpleEntityManagerFactory.getSimpleEntityManager(VipNewPlayerInfoRecord.class);

	protected static VipPlayerInfoManager self;

	public static VipPlayerInfoManager getInstance() {
		if (self == null) {
			synchronized (VipPlayerInfoManager.class) {
				if (self == null)
					self = new VipPlayerInfoManager();
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
	
	public void saveOrUpdate(VipPlayerInfoRecord vipPlayerInfoRecord)
	{
		long startTime = System.currentTimeMillis();
		try {
			if(vipPlayerInfoRecord.getId() == 0l)
			{
				long id = em.nextId();
				vipPlayerInfoRecord.setId(id);
			}
			em.flush(vipPlayerInfoRecord);
			if(logger.isInfoEnabled())
				logger.info("[flush vipPlayerInfoRecord] [成功] "+vipPlayerInfoRecord.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush vipPlayerInfoRecord] [失败] "+vipPlayerInfoRecord.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}

	public void saveOrUpdate(VipNewPlayerInfoRecord vipPlayerInfoRecord)
	{
		long startTime = System.currentTimeMillis();
		try {
			if(vipPlayerInfoRecord.getId() == 0l)
			{
				long id = em2.nextId();
				vipPlayerInfoRecord.setId(id);
			}
			em2.flush(vipPlayerInfoRecord);
			if(logger.isInfoEnabled())
				logger.info("[flush newVipPlayerInfoRecord] [成功] "+vipPlayerInfoRecord.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush newVipPlayerInfoRecord] [失败] "+vipPlayerInfoRecord.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}

	//根据id 查询单个活动对象
	public VipPlayerInfoRecord find(long id)
	{
		long startTime = System.currentTimeMillis();
		
		try
		{
			VipPlayerInfoRecord gameActivity = em.find(id);
			
			if(gameActivity != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询游戏活动] [成功] ["+id+"] "+gameActivity.getLogString()+" [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询游戏活动] [失败] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return gameActivity;
		}
		catch(Exception e)
		{
			logger.error("[根据id查询游戏活动] [失败] [出现异常] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}




	//根据自定义条件查询活动对象 无分页
	public List<VipPlayerInfoRecord> queryForWhere(String preparedWhereSql,Object[] params) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(VipPlayerInfoRecord.class, preparedWhereSql,params);

			List<VipPlayerInfoRecord> lst = new ArrayList<VipPlayerInfoRecord>();

			for(long id : ids)
			{
				VipPlayerInfoRecord vipPlayerInfoRecord = em.find(id);
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
	public List<VipPlayerInfoRecord> queryForWhere(String preparedWhereSql,Object[] params, String orderBySql, long start, long len) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {

			long []ids = em.queryIds(VipPlayerInfoRecord.class, preparedWhereSql ,params, orderBySql, start, start+len);

			List<VipPlayerInfoRecord> lst = new ArrayList<VipPlayerInfoRecord>();

			for(long id : ids)
			{
				VipPlayerInfoRecord vipPlayerInfoRecord = em.find(id);
				lst.add(vipPlayerInfoRecord);
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
