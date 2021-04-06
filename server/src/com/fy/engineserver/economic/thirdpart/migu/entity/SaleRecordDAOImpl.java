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



public class SaleRecordDAOImpl implements SaleRecordDAO {

	private static final Logger logger = MiGuTradeServiceWorker.logger;
	
	protected static SaleRecordDAOImpl self;

	public static SaleRecordDAOImpl getInstance() {
		if (self == null) {
			synchronized (SaleRecordDAOImpl.class) {
				if (self == null)
					self = new SaleRecordDAOImpl();
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
	public static final Class pojoClass = SaleRecord.class;
	
	private SimpleEntityManager<SaleRecord> em = SimpleEntityManagerFactory.getSimpleEntityManager(pojoClass);
	
	@Override
	public void saveNew(SaleRecord saleRecord) {
		long startTime = System.currentTimeMillis();
		try {
			long id = em.nextId();
			saleRecord.setId(id);
			em.save(saleRecord);
			if(logger.isInfoEnabled())
				logger.info("[存储订单实体] [成功] [id:"+saleRecord.getId()+"] [saleId:"+saleRecord.getSaleId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[存储订单实体] [失败] [id:"+saleRecord.getId()+"] [saleId:"+saleRecord.getSaleId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
	}

	@Override
	public SaleRecord getById(long id) {
		long startTime = System.currentTimeMillis();
		try {
			SaleRecord saleRecord = em.find(id);
			if(saleRecord != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询订单实体] [成功] [id:"+saleRecord.getId()+"] [saleId:"+saleRecord.getSaleId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询订单实体] [成功] [无对应此id对象] [id:"+id+"]  [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return saleRecord;
		} catch (Exception e) {
			logger.error("[根据id查询订单实体] [失败] [id:"+id+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	@Override
	public SaleRecord getByOrderid(String saleId) {
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em.queryIds(SaleRecord.class, " saleId = '" +saleId+"'");
			SaleRecord saleRecord = em.find(ids[0]);
			if(saleRecord != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据saleId查询订单实体] [成功] [id:"+saleRecord.getId()+"] [saleId:"+saleRecord.getSaleId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据saleId查询订单实体] [成功] [无对应此saleId对象] [id:"+saleRecord.getId()+"] [saleId:"+saleRecord.getSaleId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return saleRecord;
		} catch (Exception e) {
			logger.error("[根据saleId查询订单实体] [失败] [saleId:"+saleId+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	@Override
	public long getCount() {
		long startTime = System.currentTimeMillis();
		try {
			long count = 0l;
			count = em.count();
			if(logger.isInfoEnabled())
				logger.info("[订单count统计] [成功] [count:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return count;
		} catch (Exception e) {
			logger.error("[订单count统计] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return 0l;
	}

	@Override
	public long getCount(long passportId) {
		
		long startTime = System.currentTimeMillis();
		try {
			long count = 0l;
			count = em.count(SaleRecord.class, " passportId="+passportId );
			if(logger.isInfoEnabled())
				logger.info("[订单根据passportIdcount统计] [成功] [passportId:"+passportId+"] [count:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return count;
		} catch (Exception e) {
			logger.error("[订单根据passportIdcount统计] [失败] [passportId:"+passportId+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return 0l;
	}

	@Override
	public List getUserSuccPageRows(long passportId, int start, int len) {
		long startTime = System.currentTimeMillis();
		try {
			//不包含end
			start = start + 1;
			List<SaleRecord> lst = em.query(SaleRecord.class, " responseResult = "+SaleRecord.RESPONSE_SUCC+" ", "responseTime desc", start, start + len);
			if(logger.isInfoEnabled())
				logger.info("[获得某个帐号成功完成充值的订单] [成功] [passportId:"+passportId+"] [数量:"+lst.size()+"] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return lst;
		} catch (Exception e) {
			logger.error("[获得某个帐号成功完成充值的订单] [失败] [passportId:"+passportId+"] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	@Override
	public List getUserUnbackedPageRows(long passportId) {
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(SaleRecord.class, " handleResult = "+SaleRecord.HANDLE_RESULT_NOBACK+" ");
			if(logger.isInfoEnabled())
			{
				logger.info("[获得某个账户没有从充值平台返回的订单] [成功] [查询id] [passportId:"+passportId+"] [数量:"+ids.length+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			List<SaleRecord> lst = new ArrayList<SaleRecord>();
			
			for(long id : ids)
			{
				SaleRecord saleRecord = em.find(id);
				lst.add(saleRecord);
				if(logger.isDebugEnabled())
					logger.debug("[获得某个账户没有从充值平台返回的订单] [成功] [加入到List中] [passportId:"+passportId+"] [id:"+saleRecord.getId()+"] [saleId:"+saleRecord.getSaleId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			if(logger.isInfoEnabled())
			{
				logger.info("[获得某个账户没有从充值平台返回的订单] [成功] [passportId:"+passportId+"] [数量:"+lst.size()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst;
			
		} catch (Exception e) {
			logger.error("[获得某个账户没有从充值平台返回的订单] [失败] [passportId:"+passportId+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	@Override
	public List getUserPageRows(long passportId, int start, int len) {
		long startTime = System.currentTimeMillis();
		start = start + 1;
		try {
			List<SaleRecord> lst = em.query(SaleRecord.class, " passportId = "+passportId, "createTime desc", start, start + len );
			if(logger.isInfoEnabled())
				logger.info("[分页获取某个帐号的订单，按时间逆序] [成功] [passportId:"+passportId+"] [数量:"+lst.size()+"] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return lst;
		} catch (Exception e) {
			logger.error("[分页获取某个帐号的订单，按时间逆序] [失败] [passportId:"+passportId+"] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return new ArrayList();
	}

	@Override
	public List getPageRows(int start, int len) {
		
		long startTime = System.currentTimeMillis();
		start += 1;
		try {
			List<SaleRecord> lst = em.query(SaleRecord.class, "" ,"createTime desc", start, start + len);
			if(logger.isInfoEnabled())
				logger.info("[分页获得所有订单，按逆序] [成功] [数量:"+lst.size()+"] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return lst;
		} catch (Exception e) {
			logger.error("[分页获得所有订单，按逆序] [失败] [start:"+start+"] [len:"+len+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		return null;
	}

	/**
	 * 获得成功充值且没有通知服务器的订单
	 * 要求为成功调用(handleResult=1)、成功返回(responseResult=1)、没有通知服务器(notified=false)
	 * @return
	 */
	@Override
	public List getSuccUnnotifiedSaleRecords() {
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(SaleRecord.class, " handleResult = "+SaleRecord.HANDLE_RESULT_SUCC+" and responseResult = "+SaleRecord.RESPONSE_SUCC+" and notified = 'F' ");
			if(logger.isInfoEnabled())
			{
				logger.info("[获得成功充值且没有通知服务器的订单] [成功] [查询id] [数量:"+ids.length+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			List<SaleRecord> lst = new ArrayList<SaleRecord>();
			for(long id : ids)
			{
				SaleRecord saleRecord = em.find(id);
				lst.add(saleRecord);
				if(logger.isDebugEnabled())
					logger.debug("[获得成功充值且没有通知服务器的订单] [成功] [加入到List中] [id:"+saleRecord.getId()+"] [saleId:"+saleRecord.getSaleId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			if(logger.isInfoEnabled())
			{
				logger.info("[获得成功充值且没有通知服务器的订单] [成功] [数量:"+lst.size()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst;
			
		} catch (Exception e) {
			logger.error("[获得成功充值且没有通知服务器的订单] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}
	
	@Override
	public long getFailedSaleRecordsCount() {
		long startTime = System.currentTimeMillis();
		try {
			long count = em.count(SaleRecord.class, " handleResult = "+SaleRecord.HANDLE_RESULT_FAILED+" or responseResult = "+SaleRecord.RESPONSE_FAILED+" or (notified = 'T' and notifySucc = 'F')");
			if(logger.isInfoEnabled())
			{
				logger.info("[获得失败的订单数量] [成功] [查询id] [数量:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
		
			return count;
		} catch (Exception e) {
			logger.error("[获得失败的订单数量] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return 0;
	}
	
	@Override
	public List getFailedSaleRecords(int start, int len) {
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(SaleRecord.class, " handleResult = "+SaleRecord.HANDLE_RESULT_FAILED+" or responseResult = "+SaleRecord.RESPONSE_FAILED+" or (notified = 'T' and notifySucc = 'F')",  "createTime DESC", start+1, start+1+len);
			if(logger.isInfoEnabled())
			{
				logger.info("[获得失败的订单] [成功] [查询id] [数量:"+ids.length+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			List<SaleRecord> lst = new ArrayList<SaleRecord>();
			for(long id : ids)
			{
				SaleRecord saleRecord = em.find(id);
				lst.add(saleRecord);
				if(logger.isDebugEnabled())
					logger.debug("[获得失败的订单] [成功] [加入到List中] [id:"+saleRecord.getId()+"] [saleId:"+saleRecord.getSaleId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			if(logger.isInfoEnabled())
			{
				logger.info("[获得失败的订单] [成功] [数量:"+lst.size()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst;
			
		} catch (Exception e) {
			logger.error("[获得失败的订单] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	@Override
	public void update(SaleRecord saleRecordForm,String fieldName) {
		long startTime = System.currentTimeMillis();
		try {
			em.notifyFieldChange(saleRecordForm, fieldName);
			em.save(saleRecordForm);
			if(logger.isInfoEnabled())
				logger.info("[更新订单实体单个字段] [成功] [id:"+saleRecordForm.getId()+"] [saleId:"+saleRecordForm.getSaleId()+"] [fieldName:"+fieldName+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[更新订单实体单个字段] [失败] [id:"+saleRecordForm.getId()+"] [saleId:"+saleRecordForm.getSaleId()+"] [fieldName:"+fieldName+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}

	@Override
	public void delete(SaleRecord saleRecordForm) {
		
	}
	
	/**
	 * 一个公用方法，用以批量修改传入的字段
	 * 
	 * @param fieldNames 传入字段名列表
	 * @throws Exception 
	 * added by liuyang at 2012-05-29
	 */
	public void batch_updateField(SaleRecord saleRecordForm,List<String> fieldNames) throws Exception
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
				logger.warn("[更新订单] [更新字段] [字段名有空值] [Id:"+saleRecordForm.getId()+"] [saleId:"+saleRecordForm.getSaleId()+"] [fieldName:"+fieldName+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			em.notifyFieldChange(saleRecordForm, fieldName);
			if(logger.isInfoEnabled())
			{
				logger.info("[更新订单] [更新字段] [成功] [Id:"+saleRecordForm.getId()+"] [saleId:"+saleRecordForm.getSaleId()+"] [fieldName:"+fieldName+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
		}
		em.save(saleRecordForm);
	}

	public SimpleEntityManager<SaleRecord> getEm() {
		return em;
	}

	public void setEm(SimpleEntityManager<SaleRecord> em) {
		this.em = em;
	}

	@Override
	public long count(String whereSql) throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			long count = 0l;
			count = em.count(SaleRecord.class,  whereSql );
			if(logger.isInfoEnabled())
				logger.info("[订单根据Where条件进行count] [成功] [Where条件:"+whereSql+"] [count:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return count;
		} catch (Exception e) {
			logger.error("[订单根据Where条件count统计] [失败] [Where条件:"+whereSql+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			throw e;
		}
	}
	
	public long count(String preparedWhereSql,Object[] paramvalues) throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			long count = 0l;
			count = em.count(SaleRecord.class,  preparedWhereSql,paramvalues );
			if(logger.isInfoEnabled())
				logger.info("[订单根据预编译Where条件进行count] [成功] [Where条件:"+preparedWhereSql+"] [params:"+printObjects(paramvalues)+"] [count:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return count;
		} catch (Exception e) {
			logger.error("[订单根据预编译Where条件count统计] [失败] [Where条件:"+preparedWhereSql+"] [params:"+printObjects(paramvalues)+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			throw e;
		}
	}
	
	
	public List<SaleRecord> queryForWhere(String whereSql) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(SaleRecord.class, whereSql);
			if(logger.isInfoEnabled())
			{
				logger.info("[根据where条件查询订单] [成功] [查询id] [where条件:"+whereSql+"] [数量:"+ids.length+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			List<SaleRecord> lst = new ArrayList<SaleRecord>();
			
			for(long id : ids)
			{
				SaleRecord saleRecord = em.find(id);
				lst.add(saleRecord);
				if(logger.isDebugEnabled())
					logger.debug("[根据where条件查询订单] [成功] [加入到List中] [where条件:"+whereSql+"] [id:"+saleRecord.getId()+"] [saleId:"+saleRecord.getSaleId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			if(logger.isInfoEnabled())
			{
				logger.info("[根据where条件查询订单] [成功] [where条件:"+whereSql+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst;
		} catch (Exception e) {
			logger.error("[根据where条件查询订单] [失败] [where条件:"+whereSql+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}
	
	public void update(SaleRecord saleRecord) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			em.flush(saleRecord);
			if(logger.isInfoEnabled())
				logger.info("[flush订单] [成功] [订单 Id:"+saleRecord.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush订单] [失败] [订单 Id:"+saleRecord.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			throw new Exception("更新订单失败！");
		}
	}

//	@Override
	public SaleRecord getByChannelOrderid(String channelOrderId,String channel) {
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em.queryIds(SaleRecord.class, " channelOrderId = '" +channelOrderId+"' and userChannel = '" +channel+ "'");
			SaleRecord saleRecord = em.find(ids[0]);
			if(saleRecord != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据channelOrderId查询订单实体] [成功] [id:"+saleRecord.getId()+"] [Channel saleId:"+saleRecord.getChannelOrderId()+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据channelOrderId查询订单实体] [成功] [无对应此channelOrderId对象] [id:"+saleRecord.getId()+"] [渠道:"+channel+"] [Channel saleId:"+saleRecord.getChannelOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return saleRecord;
		} catch (Exception e) {
			logger.error("[根据channelOrderId查询订单实体] [失败] [saleId:"+channelOrderId+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
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

	@Override
	public boolean isRoleSaling(long userId) throws Exception {
		// TODO Auto-generated method stub1
		long count = em.count(SaleRecord.class, " articleId = '" + userId + "' and tradeType = '3' and responseResult = '-1'");
		return count > 0;
	}
	
}
