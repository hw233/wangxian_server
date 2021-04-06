package com.fy.gamegateway.mieshi.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class DenyUserEntityManager {
	//分页查询
	
	public static final Logger logger = MieshiGatewaySubSystem.logger;
	// property constants
	public static final Class pojoClass = DenyUserEntity.class;

	public SimpleEntityManager<DenyUserEntity> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);

	protected static DenyUserEntityManager self;

	public static DenyUserEntityManager getInstance() {
		if (self == null) {
			synchronized (DenyUserEntityManager.class) {
				if (self == null)
					self = new DenyUserEntityManager();
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
	
	public void saveOrUpdate(DenyUserEntity denyUserEntity)
	{
		long startTime = System.currentTimeMillis();
		try {
			if(denyUserEntity.getId() == 0l)
			{
				long id = em.nextId();
				denyUserEntity.setId(id);
			}
			em.flush(denyUserEntity);
			if(logger.isInfoEnabled())
				logger.info("[flush DenyUserEntity] [成功] "+denyUserEntity.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush DenyUserEntity] [失败] "+denyUserEntity.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}


	//根据id 查询单个活动对象
	public DenyUserEntity find(long id)
	{
		long startTime = System.currentTimeMillis();
		
		try
		{
			DenyUserEntity denyUserEntity = em.find(id);
			
			if(denyUserEntity != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询封禁用户] [成功] ["+id+"] "+denyUserEntity.getLogString()+" [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询封禁用户] [失败] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return denyUserEntity;
		}
		catch(Exception e)
		{
			logger.error("[根据id查询封禁用户] [失败] [出现异常] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}




	//根据自定义条件查询活动对象 无分页
	public List<DenyUserEntity> queryForWhere(String preparedWhereSql,Object[] params) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(DenyUserEntity.class, preparedWhereSql,params);

			List<DenyUserEntity> lst = new ArrayList<DenyUserEntity>();

			for(long id : ids)
			{
				DenyUserEntity DenyUserEntity = em.find(id);
				lst.add(DenyUserEntity);
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
	public List<DenyUserEntity> queryForWhere(String preparedWhereSql,Object[] params, String orderBySql, long start, long len) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {

			long []ids = em.queryIds(DenyUserEntity.class, preparedWhereSql ,params, orderBySql, start, start+len);

			List<DenyUserEntity> lst = new ArrayList<DenyUserEntity>();

			for(long id : ids)
			{
				DenyUserEntity denyUserEntity = em.find(id);
				lst.add(denyUserEntity);
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
