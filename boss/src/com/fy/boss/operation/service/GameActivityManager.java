package com.fy.boss.operation.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.operation.model.GameActivity;
import com.fy.boss.operation.service.GameActivityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.DateUtil;


public class GameActivityManager {

	private static final Logger logger = Logger.getLogger(GameActivityManager.class);
	// property constants
	public static final Class pojoClass = GameActivity.class;

	public SimpleEntityManager<GameActivity> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);

	protected static GameActivityManager self;

	public static GameActivityManager getInstance() {
		if (self == null) {
			synchronized (GameActivityManager.class) {
				if (self == null)
					self = new GameActivityManager();
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



	public void saveOrUpdate(GameActivity gameActivity)
	{
		long startTime = System.currentTimeMillis();
		try {
			if(gameActivity.getId() == 0l)
			{
				long id = em.nextId();
				gameActivity.setId(id);
			}
			em.flush(gameActivity);
			if(logger.isInfoEnabled())
				logger.info("[flush gameactivity] [成功] "+gameActivity.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush gameactivity] [失败] "+gameActivity.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}

	//根据日期查询 无分页
	/**
	 * 此方法是根据时间来查活动 
	 * 活动是有开始和结束时间 此方法有两个参数 都为时间 在这个时间区域之内 如果
	 * 活动的开始时间或者结束时间在这个区间之内 则都会被查出
	 * select from gameactivity g where (g.begindate >= begindate and g.begindate  <= enddate) or (g.endDate >=beginDate and g.endDate <= enddate) 
	 * @return
	 */
	public List<GameActivity> findGameActivitiesByDate(Date beginDate, Date endDate)
	{
		long startTime = System.currentTimeMillis();
		try
		{	
			long beginTime = beginDate.getTime();
			long endTime = endDate.getTime();

			String startDateStr = DateUtil.formatDateSafely(beginDate, "yyyy-MM-dd HH:mm:ss");
			String endDateStr = DateUtil.formatDateSafely(endDate, "yyyy-MM-dd HH:mm:ss");
			
			String whereSql = " (startTime >= ?  and startTime <= ? ) or" +
					" (endTime >= ?  and endTime <= ? )";
			
			Object[] os = new Object[]{beginTime,beginTime,endTime,endTime};

			List<GameActivity> lst  = queryForWhere(whereSql,os);
			if(lst != null)
			{
				if(logger.isInfoEnabled())
				{
					logger.info("[根据时间查询游戏活动] [成功] ["+beginTime+"] ["+startDateStr+"] ["+endTime+"] ["+endDateStr+"] ["+whereSql+"] [size:"+lst.size()+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
				}
			}
			else
			{
				logger.warn("[根据时间查询游戏活动] [失败] ["+beginTime+"] ["+startDateStr+"] ["+endTime+"] ["+endDateStr+"] ["+whereSql+"] [size:--] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return 	lst;	
		}
		catch(Exception e)
		{
			logger.error("[根据时间查询游戏活动] [失败] [--] [--] [--] [--] [--] [size:--] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;

	}


	//根据日期查询 有分页


	//根据id 查询单个活动对象
	public GameActivity find(long id)
	{
		long startTime = System.currentTimeMillis();
		
		try
		{
			GameActivity gameActivity = em.find(id);
			
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

	//根据活动名称查询单个活动对象


	//根据平台查询活动对象



	//根据自定义条件查询活动对象 无分页
	public List<GameActivity> queryForWhere(String preparedWhereSql,Object[] params) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(GameActivity.class, preparedWhereSql,params);

			List<GameActivity> lst = new ArrayList<GameActivity>();

			for(long id : ids)
			{
				GameActivity customServicer = em.find(id);
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
	public List<GameActivity> queryForWhere(String preparedWhereSql,Object[] params, String orderBySql, long start, long len) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {

			long []ids = em.queryIds(GameActivity.class, preparedWhereSql ,params, orderBySql, start, start+len);

			List<GameActivity> lst = new ArrayList<GameActivity>();

			for(long id : ids)
			{
				GameActivity customServicer = em.find(id);
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


