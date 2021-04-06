package com.fy.gamegateway.announce;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;


public class MessageEntityManager {
	
	//分页查询
	
	private static final Logger logger = Logger.getLogger(MessageEntityManager.class);
	// property constants
	public static final Class pojoClass = MessageEntity.class;

	public SimpleEntityManager<MessageEntity> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);

	protected static MessageEntityManager self;

	public static MessageEntityManager getInstance() {
		if (self == null) {
			synchronized (MessageEntityManager.class) {
				if (self == null)
					self = new MessageEntityManager();
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
	
	public void saveOrUpdate(MessageEntity messageEntity)
	{
		long startTime = System.currentTimeMillis();
		try {
			if(messageEntity.getId() == 0l)
			{
				long id = em.nextId();
				messageEntity.setId(id);
			}
			em.flush(messageEntity);
			if(logger.isInfoEnabled())
				logger.info("[flush MessageEntity] [成功] "+messageEntity.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush MessageEntity] [失败] "+messageEntity.getLogString()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}


	//根据id 查询单个活动对象
	public MessageEntity find(long id)
	{
		long startTime = System.currentTimeMillis();
		
		try
		{
			MessageEntity messageEntity = em.find(id);
			
			if(messageEntity != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询消息实体] [成功] ["+id+"] "+messageEntity.getLogString()+" [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询消息实体] [失败] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return messageEntity;
		}
		catch(Exception e)
		{
			logger.error("[根据id查询消息实体] [失败] [出现异常] ["+id+"] [cost:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}




	//根据自定义条件查询活动对象 无分页
	public List<MessageEntity> queryForWhere(String preparedWhereSql,Object[] params) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(MessageEntity.class, preparedWhereSql,params);

			List<MessageEntity> lst = new ArrayList<MessageEntity>();

			for(long id : ids)
			{
				MessageEntity messageEntity = em.find(id);
				lst.add(messageEntity);
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
	public List<MessageEntity> queryForWhere(String preparedWhereSql,Object[] params, String orderBySql, long start, long len) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {

			long []ids = em.queryIds(MessageEntity.class, preparedWhereSql ,params, orderBySql, start, start+len);

			List<MessageEntity> lst = new ArrayList<MessageEntity>();

			for(long id : ids)
			{
				MessageEntity messageEntity = em.find(id);
				lst.add(messageEntity);
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
