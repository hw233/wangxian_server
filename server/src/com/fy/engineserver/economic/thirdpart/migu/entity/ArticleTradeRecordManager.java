package com.fy.engineserver.economic.thirdpart.migu.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;



public class ArticleTradeRecordManager  {

	public static final Logger logger = MiGuTradeServiceWorker.logger;
	
	protected static ArticleTradeRecordManager self;

	public static ArticleTradeRecordManager getInstance() {
		if (self == null) {
			synchronized (ArticleTradeRecordManager.class) {
				if (self == null)
					self = new ArticleTradeRecordManager();
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
	
	// property constants
	public static final Class pojoClass = ArticleTradeRecord.class;
	
	private SimpleEntityManager<ArticleTradeRecord> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);
	
	public void notifyNew(ArticleTradeRecord articleTradeRecord) {
		long startTime = System.currentTimeMillis();
		try {
			long id = em.nextId();
			articleTradeRecord.setId(id);
			em.notifyNewObject(articleTradeRecord);
			if(logger.isInfoEnabled())
				logger.info("[异步存储道具交易记录实体] [成功] "+articleTradeRecord.getLogStr()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[异步存储道具交易记录实体] [失败] "+articleTradeRecord.getLogStr()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
	}
	
	
	public void saveNew(ArticleTradeRecord articleTradeRecord) {
		long startTime = System.currentTimeMillis();
		try {
			long id = em.nextId();
			articleTradeRecord.setId(id);
			em.save(articleTradeRecord);
			if(logger.isInfoEnabled())
				logger.info("[存储道具交易记录实体] [成功] "+articleTradeRecord.getLogStr()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[存储道具交易记录实体] [失败] "+articleTradeRecord.getLogStr()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
	}

	
	public ArticleTradeRecord getById(long id) {
		long startTime = System.currentTimeMillis();
		try {
			ArticleTradeRecord articleTradeRecord = em.find(id);
			if(articleTradeRecord != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询道具交易记录实体] [成功] "+articleTradeRecord.getLogStr()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询道具交易记录实体] [成功] [无对应此id对象] "+articleTradeRecord.getLogStr()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return articleTradeRecord;
		} catch (Exception e) {
			logger.error("[根据id查询道具交易记录实体] [失败] [id:"+id+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	
	public ArticleTradeRecord getByOrderid(String saleId) {
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em.queryIds(ArticleTradeRecord.class, " saleId = '" +saleId+"'");
			ArticleTradeRecord articleTradeRecord = em.find(ids[0]);
			if(articleTradeRecord != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据saleId查询道具交易记录实体] [成功] "+articleTradeRecord.getLogStr()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据saleId查询道具交易记录实体] [成功] [无对应此saleId对象] "+articleTradeRecord.getLogStr()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return articleTradeRecord;
		} catch (Exception e) {
			logger.error("[根据saleId查询道具交易记录实体] [失败] [saleId:"+saleId+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	
	public long getCount() {
		long startTime = System.currentTimeMillis();
		try {
			long count = 0l;
			count = em.count();
			if(logger.isInfoEnabled())
				logger.info("[道具交易记录count统计] [成功] [count:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return count;
		} catch (Exception e) {
			logger.error("[道具交易记录count统计] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return 0l;
	}

	
	public long getCount(long passportId) {
		
		long startTime = System.currentTimeMillis();
		try {
			long count = 0l;
			count = em.count(ArticleTradeRecord.class, " passportId="+passportId );
			if(logger.isInfoEnabled())
				logger.info("[道具交易记录根据passportIdcount统计] [成功] [passportId:"+passportId+"] [count:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return count;
		} catch (Exception e) {
			logger.error("[道具交易记录根据passportIdcount统计] [失败] [passportId:"+passportId+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return 0l;
	}


	

	
	public List getUserPageRows(long passportId, int start, int len) {
		long startTime = System.currentTimeMillis();
		start = start + 1;
		try {
			List<ArticleTradeRecord> lst = em.query(ArticleTradeRecord.class, " passportId = "+passportId, "createTime desc", start, start + len );
			if(logger.isInfoEnabled())
				logger.info("[分页获取某个帐号的道具交易记录，按时间逆序] [成功] [passportId:"+passportId+"] [数量:"+lst.size()+"] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return lst;
		} catch (Exception e) {
			logger.error("[分页获取某个帐号的道具交易记录，按时间逆序] [失败] [passportId:"+passportId+"] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return new ArrayList();
	}

	
	public List getPageRows(int start, int len) {
		
		long startTime = System.currentTimeMillis();
		start += 1;
		try {
			List<ArticleTradeRecord> lst = em.query(ArticleTradeRecord.class, "" ,"createTime desc", start, start + len);
			if(logger.isInfoEnabled())
				logger.info("[分页获得所有道具交易记录，按逆序] [成功] [数量:"+lst.size()+"] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return lst;
		} catch (Exception e) {
			logger.error("[分页获得所有道具交易记录，按逆序] [失败] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}

	
	

	
	public void update(ArticleTradeRecord articleTradeRecordForm,String fieldName) {
		long startTime = System.currentTimeMillis();
		try {
			em.notifyFieldChange(articleTradeRecordForm, fieldName);
			em.save(articleTradeRecordForm);
			if(logger.isInfoEnabled())
				logger.info("[更新道具交易记录实体单个字段] [成功] [id:"+articleTradeRecordForm.getId()+"] " +articleTradeRecordForm.getLogStr()+ " [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[更新道具交易记录实体单个字段] [失败] [id:"+articleTradeRecordForm.getId()+"] " +articleTradeRecordForm.getLogStr()+ " [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}

	
	public void delete(ArticleTradeRecord articleTradeRecordForm) {
		
	}
	
	/**
	 * 一个公用方法，用以批量修改传入的字段
	 * 
	 * @param fieldNames 传入字段名列表
	 * @throws Exception 
	 * added by liuyang at 2012-05-29
	 */
	public void batch_updateField(ArticleTradeRecord articleTradeRecordForm,List<String> fieldNames) throws Exception
	{
		long startTime = System.currentTimeMillis();
		if(fieldNames == null)
		{
			throw new NullArgumentException("传入的字段名列表(fieldNames[List<String>]为空值！)");
		}
		
		Iterator<String> it = fieldNames.iterator();
		while(it.hasNext())
		{
			String fieldName = it.next();
			if(StringUtils.isEmpty(fieldName))
			{
				logger.warn("[更新道具交易记录] [更新字段] [字段名有空值] " +articleTradeRecordForm.getLogStr()+ " [fieldName:"+fieldName+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			em.notifyFieldChange(articleTradeRecordForm, fieldName);
			if(logger.isInfoEnabled())
			{
				logger.info("[更新道具交易记录] [更新字段] [成功] " +articleTradeRecordForm.getLogStr()+ " [fieldName:"+fieldName+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
		}
		em.save(articleTradeRecordForm);
	}

	public SimpleEntityManager<ArticleTradeRecord> getEm() {
		return em;
	}

	public void setEm(SimpleEntityManager<ArticleTradeRecord> em) {
		this.em = em;
	}

	
	public long count(String whereSql) throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			long count = 0l;
			count = em.count(ArticleTradeRecord.class,  whereSql );
			if(logger.isInfoEnabled())
				logger.info("[道具交易记录根据Where条件进行查询] [成功] [Where条件:"+whereSql+"] [count:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return count;
		} catch (Exception e) {
			logger.error("[道具交易记录根据passportIdcount统计] [失败] [Where条件:"+whereSql+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			throw new Exception("根据条件查询道具交易记录失败！");
		}
	}
	
	public List<ArticleTradeRecord> queryForWhere(String whereSql) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(ArticleTradeRecord.class, whereSql);
			if(logger.isInfoEnabled())
			{
				logger.info("[根据where条件查询道具交易记录] [成功] [查询id] [where条件:"+whereSql+"] [数量:"+ids.length+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			List<ArticleTradeRecord> lst = new ArrayList<ArticleTradeRecord>();
			
			for(long id : ids)
			{
				ArticleTradeRecord articleTradeRecord = em.find(id);
				lst.add(articleTradeRecord);
				if(logger.isDebugEnabled())
					logger.debug("[根据where条件查询道具交易记录] [成功] [加入到List中] [where条件:"+whereSql+"] "+articleTradeRecord.getLogStr()+" [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			if(logger.isInfoEnabled())
			{
				logger.info("[根据where条件查询道具交易记录] [成功] [where条件:"+whereSql+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst;
		} catch (Exception e) {
			logger.error("[根据where条件查询道具交易记录] [失败] [where条件:"+whereSql+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}
	
	public List<ArticleTradeRecord> queryForWhere(String preparedWhereSql,Object[] paramValues) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(ArticleTradeRecord.class, preparedWhereSql, paramValues);
			if(logger.isInfoEnabled())
			{
				logger.info("[根据where条件查询道具交易记录] [成功] [查询id] [where条件:"+preparedWhereSql+"] [数量:"+ids.length+"] [参数:"+printObjects(paramValues)+"] [结果:"+printIds(ids)+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			List<ArticleTradeRecord> lst = new ArrayList<ArticleTradeRecord>();
			
			for(long id : ids)
			{
				ArticleTradeRecord articleTradeRecord = em.find(id);
				lst.add(articleTradeRecord);
				if(articleTradeRecord != null)
					logger.info("[根据where条件查询道具交易记录] [成功] [加入到List中] [where条件:"+preparedWhereSql+"] "+articleTradeRecord.getLogStr()+"[参数:"+printObjects(paramValues)+"] [id:"+id+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				else
					logger.info("[根据where条件查询道具交易记录] [成功] [加入到List中] [where条件:"+preparedWhereSql+"] "+"[参数:"+printObjects(paramValues)+"] [id:"+id+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			if(logger.isInfoEnabled())
			{
				logger.info("[根据where条件查询道具交易记录] [成功] [where条件:"+preparedWhereSql+"] [参数:"+printObjects(paramValues)+"] [size:"+lst.size()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst;
		} catch (Exception e) {
			logger.error("[根据where条件查询道具交易记录] [失败] [where条件:"+preparedWhereSql+"] [参数:"+printObjects(paramValues)+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}
	
	
	public void update(ArticleTradeRecord articleTradeRecord) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			em.flush(articleTradeRecord);
			if(logger.isInfoEnabled())
				logger.info("[flush道具交易记录] [成功] [道具交易记录 Id:"+articleTradeRecord.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush道具交易记录] [失败] [道具交易记录 Id:"+articleTradeRecord.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			throw new Exception("更新道具交易记录失败！");
		}
	}

//	
	public ArticleTradeRecord getByChannelOrderid(String channelOrderId,String channel) {
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em.queryIds(ArticleTradeRecord.class, " channelOrderId = '" +channelOrderId+"' and userChannel = '" +channel+ "'");
			ArticleTradeRecord articleTradeRecord = em.find(ids[0]);
			if(articleTradeRecord != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据channelOrderId查询道具交易记录实体] [成功] [id:"+articleTradeRecord.getId()+"] " +articleTradeRecord.getLogStr()+ " [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据channelOrderId查询道具交易记录实体] [成功] [无对应此channelOrderId对象] " +articleTradeRecord.getLogStr()+ " [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return articleTradeRecord;
		} catch (Exception e) {
			logger.error("[根据channelOrderId查询道具交易记录实体] [失败] [saleId:"+channelOrderId+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}
	
	private String printObjects(Object[] os)
	{
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < os.length; i++)
		{
			if( i == 0)
			{
				buffer.append(os[i]);
			}
			else
			{
				buffer.append(",");
				buffer.append(os[i]);
			}
		}
		
		return buffer.toString();
	}
	
	
	private String printIds(long[] ids)
	{
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < ids.length; i++)
		{
			if( i == 0)
			{
				buffer.append(ids[i]);
			}
			else
			{
				buffer.append(",");
				buffer.append(ids[i]);
			}
		}
		
		return buffer.toString();
	}
}
