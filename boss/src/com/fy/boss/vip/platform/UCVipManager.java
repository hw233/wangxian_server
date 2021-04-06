package com.fy.boss.vip.platform;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.vip.platform.model.UcVipRecord;
import com.fy.boss.vip.platform.UCVipManager;
import com.fy.boss.vip.platform.VipPlayerInfoManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


public class UCVipManager {
	
	
	//分页查询
	
	private static final Logger logger = Logger.getLogger(VipPlayerInfoManager.class);
	// property constants
	public static final Class pojoClass = UcVipRecord.class;

	public SimpleEntityManager<UcVipRecord> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);

	protected static UCVipManager self;

	public static UCVipManager getInstance() {
		if (self == null) {
			synchronized (UCVipManager.class) {
				if (self == null)
					self = new UCVipManager();
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
	
	public void saveOrUpdate(UcVipRecord ucVipRecord)
	{
		long startTime = System.currentTimeMillis();
		try {
			if(ucVipRecord.getId() == 0l)
			{
				long id = em.nextId();
				ucVipRecord.setId(id);
			}
			em.flush(ucVipRecord);
			if(logger.isInfoEnabled())
				logger.info("[flush ucVipRecord] [成功] "+ucVipRecord.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush ucVipRecord] [失败] "+ucVipRecord.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}


	//根据id 查询单个活动对象
	public UcVipRecord find(long id)
	{
		long startTime = System.currentTimeMillis();
		
		try
		{
			UcVipRecord gameActivity = em.find(id);
			
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
	public List<UcVipRecord> queryForWhere(String preparedWhereSql,Object[] params) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(UcVipRecord.class, preparedWhereSql,params);

			List<UcVipRecord> lst = new ArrayList<UcVipRecord>();

			for(long id : ids)
			{
				UcVipRecord ucVipRecord = em.find(id);
				lst.add(ucVipRecord);
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
	public List<UcVipRecord> queryForWhere(String preparedWhereSql,Object[] params, String orderBySql, long start, long len) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {

			long []ids = em.queryIds(UcVipRecord.class, preparedWhereSql ,params, orderBySql, start, start+len);

			List<UcVipRecord> lst = new ArrayList<UcVipRecord>();

			for(long id : ids)
			{
				UcVipRecord ucVipRecord = em.find(id);
				lst.add(ucVipRecord);
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
