package com.fy.boss.vip.platform;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.vip.platform.model.CustomServicer;
import com.fy.boss.vip.platform.CustomServicerManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


public class CustomServicerManager {
	
	
	//分页查询
	
	private static final Logger logger = Logger.getLogger(CustomServicerManager.class);
	// property constants
	public static final Class<CustomServicer> pojoClass = CustomServicer.class;

	public SimpleEntityManager<CustomServicer> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);

	protected static CustomServicerManager self;

	public static CustomServicerManager getInstance() {
		if (self == null) {
			synchronized (CustomServicerManager.class) {
				if (self == null)
					self = new CustomServicerManager();
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
	
	public void saveOrUpdate(CustomServicer customServicer)
	{
		long startTime = System.currentTimeMillis();
		try {
			if(customServicer.getId() == 0l)
			{
				long id = em.nextId();
				customServicer.setId(id);
			}
			em.flush(customServicer);
			if(logger.isInfoEnabled())
				logger.info("[flush CustomServicer] [成功] "+customServicer.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush CustomServicer] [失败] "+customServicer.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}


	//根据id 查询单个活动对象
	public CustomServicer find(long id)
	{
		long startTime = System.currentTimeMillis();
		
		try
		{
			CustomServicer customServicer = em.find(id);
			
			if(customServicer != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询对象] [成功] ["+id+"] "+customServicer.getLogString()+" [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询对象] [失败] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return customServicer;
		}
		catch(Exception e)
		{
			logger.error("[根据id查询对象] [失败] [出现异常] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}




	//根据自定义条件查询活动对象 无分页
	public List<CustomServicer> queryForWhere(String preparedWhereSql,Object[] params) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(CustomServicer.class, preparedWhereSql,params);

			List<CustomServicer> lst = new ArrayList<CustomServicer>();

			for(long id : ids)
			{
				CustomServicer customServicer = em.find(id);
				lst.add(customServicer);
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
	public List<CustomServicer> queryForWhere(String preparedWhereSql,Object[] params, String orderBySql, long start, long len) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {

			long []ids = em.queryIds(CustomServicer.class, preparedWhereSql ,params, orderBySql, start, start+len);

			List<CustomServicer> lst = new ArrayList<CustomServicer>();

			for(long id : ids)
			{
				CustomServicer customServicer = em.find(id);
				lst.add(customServicer);
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
