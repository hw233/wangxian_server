package com.fy.gamegateway.thirdpartner;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


public class ThirdPartDataEntityManager {
	//分页查询
	
	private static final Logger logger = Logger.getLogger(ThirdPartDataEntityManager.class);
	// property constants
	public static final Class pojoClass = ThirdPartDataEntity.class;

	public SimpleEntityManager<ThirdPartDataEntity> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);

	protected static ThirdPartDataEntityManager self;

	public static ThirdPartDataEntityManager getInstance() {
		if (self == null) {
			synchronized (ThirdPartDataEntityManager.class) {
				if (self == null)
					self = new ThirdPartDataEntityManager();
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
	
	public void saveOrUpdate(ThirdPartDataEntity thirdPartDataEntity)
	{
		long startTime = System.currentTimeMillis();
		try {
			if(thirdPartDataEntity.getId() == 0l)
			{
				long id = em.nextId();
				thirdPartDataEntity.setId(id);
			}
			em.flush(thirdPartDataEntity);
			if(logger.isInfoEnabled())
				logger.info("[flush thirdPartDataEntity] [成功] "+thirdPartDataEntity.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush thirdPartDataEntity] [失败] "+thirdPartDataEntity.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}
	
	public void saveOrUpdate(ThirdPartDataEntity thirdPartDataEntity,boolean isRsync,String[]changedFields)
	{
		long startTime = System.currentTimeMillis();
		boolean isSave = false;
		try {
			if(thirdPartDataEntity.getId() == 0l)
			{
				long id = em.nextId();
				thirdPartDataEntity.setId(id);
				isSave = true;
				
			}
			if(isRsync)
				em.flush(thirdPartDataEntity);
			else
			{
				if(isSave)
					em.notifyNewObject(thirdPartDataEntity);
				else
				{
					if(changedFields != null && changedFields.length > 0)
					{
						for(int i=0; i<changedFields.length; i++)
						{
							em.notifyFieldChange(thirdPartDataEntity, changedFields[i]);
						}
					}
						
				}
			}
			if(logger.isInfoEnabled())
				logger.info("[flush thirdPartDataEntity] [成功] "+thirdPartDataEntity.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush thirdPartDataEntity] [失败] "+thirdPartDataEntity.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}


	//根据id 查询单个活动对象
	public ThirdPartDataEntity find(long id)
	{
		long startTime = System.currentTimeMillis();
		
		try
		{
			ThirdPartDataEntity thirdPartDataEntity = em.find(id);
			
			if(thirdPartDataEntity != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询游戏活动] [成功] ["+id+"] "+thirdPartDataEntity.getLogString()+" [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询游戏活动] [失败] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return thirdPartDataEntity;
		}
		catch(Exception e)
		{
			logger.error("[根据id查询游戏活动] [失败] [出现异常] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}




	//根据自定义条件查询活动对象 无分页
	public List<ThirdPartDataEntity> queryForWhere(String preparedWhereSql,Object[] params) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(ThirdPartDataEntity.class, preparedWhereSql,params);

			List<ThirdPartDataEntity> lst = new ArrayList<ThirdPartDataEntity>();

			for(long id : ids)
			{
				ThirdPartDataEntity thirdPartDataEntity = em.find(id);
				lst.add(thirdPartDataEntity);
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
	public List<ThirdPartDataEntity> queryForWhere(String preparedWhereSql,Object[] params, String orderBySql, long start, long len) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {

			long []ids = em.queryIds(ThirdPartDataEntity.class, preparedWhereSql ,params, orderBySql, start, start+len);

			List<ThirdPartDataEntity> lst = new ArrayList<ThirdPartDataEntity>();

			for(long id : ids)
			{
				ThirdPartDataEntity thirdPartDataEntity = em.find(id);
				lst.add(thirdPartDataEntity);
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
