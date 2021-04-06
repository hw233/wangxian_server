package com.fy.boss.operation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fy.boss.operation.model.Platform;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


public class PlatformManager {
	private static final Logger logger = Logger.getLogger(PlatformManager.class);
	// property constants
	public static final Class pojoClass = Platform.class;

	public SimpleEntityManager<Platform> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);

	protected static PlatformManager self;

	public static PlatformManager getInstance() {
		if (self == null) {
			synchronized (PlatformManager.class) {
				if (self == null)
					self = new PlatformManager();
			}
		}
		return self;
	}
	
	public static enum Platforms{
		sqage,tencent,taiwan,korea,malai;
		
		public static Map<Platforms,String> map =  new ConcurrentHashMap<Platforms,String>();
		static
		{
			map.put(sqage, "简体中文");
			map.put(tencent, "简体中文");
			map.put(taiwan, "繁体中文");
			map.put(korea, "韩文");
			map.put(malai, "简体中文");
			
		}
		
		public static String getChineseName(Platforms platforms)
		{
			switch (platforms) {
			case sqage:
				return "国服";	
			case tencent:
				return "腾讯";	
			case taiwan:
				return "台服";	
			case korea:
				return "韩服";	
			case malai:
				return "马来";	
				
			default:
				return "国服";
			}
		}
		
	}
	
	public void initialize() throws Exception {
		long now = System.currentTimeMillis();
		self = this;
		System.out.println(this.getClass().getName() + " initialize successfully [cost:" + (System.currentTimeMillis() - now) + "ms]");
		logger.info(this.getClass().getName() + " initialize successfully [" + (System.currentTimeMillis() - now) + "]");
	}



	public void saveOrUpdate(Platform platform)
	{
		long startTime = System.currentTimeMillis();
		try {
			if(platform.getId() == 0l)
			{
				long id = em.nextId();
				platform.setId(id);
			}
			em.flush(platform);
			if(logger.isInfoEnabled())
				logger.info("[flush platform] [成功] "+platform.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush platform] [失败] "+platform.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}



	//根据日期查询 有分页


	//根据id 查询单个活动对象
	public Platform find(long id)
	{
		long startTime = System.currentTimeMillis();
		
		try
		{
			Platform platform = em.find(id);
			
			if(platform != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询对象] [成功] ["+id+"] "+platform.getLogString()+" [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询对象] [失败] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return platform;
		}
		catch(Exception e)
		{
			logger.error("[根据id查询对象] [失败] [出现异常] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	//根据活动名称查询单个活动对象





	//根据自定义条件查询活动对象 无分页
	public List<Platform> queryForWhere(String preparedWhereSql,Object[] params) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(Platform.class, preparedWhereSql,params);

			List<Platform> lst = new ArrayList<Platform>();

			for(long id : ids)
			{
				Platform customServicer = em.find(id);
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
	public List<Platform> queryForWhere(String preparedWhereSql,Object[] params, String orderBySql, long start, long len) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {

			long []ids = em.queryIds(Platform.class, preparedWhereSql ,params, orderBySql, start, start+len);

			List<Platform> lst = new ArrayList<Platform>();

			for(long id : ids)
			{
				Platform platform = em.find(id);
				lst.add(platform);
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


