package com.fy.boss.finance.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fy.boss.finance.dao.OrderFormDAO;
import com.fy.boss.finance.model.OrderForm;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;



public class OrderFormDAOImpl implements OrderFormDAO {

	private static final Log logger = LogFactory.getLog(OrderFormDAOImpl.class);
	
	protected static OrderFormDAOImpl self;

	public static OrderFormDAOImpl getInstance() {
		if (self == null) {
			synchronized (OrderFormDAOImpl.class) {
				if (self == null)
					self = new OrderFormDAOImpl();
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
	public static final Class pojoClass = OrderForm.class;
	
	private SimpleEntityManager<OrderForm> em = SimpleEntityManagerFactory.getSimpleEntityManager(OrderForm.class);
	
	@Override
	public void saveNew(OrderForm order) {
		long startTime = System.currentTimeMillis();
		try {
			long id = em.nextId();
			order.setId(id);
			em.save(order);
			if(logger.isInfoEnabled())
				logger.info("[存储订单实体] [成功] [id:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[存储订单实体] [失败] [id:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
	}

	@Override
	public OrderForm getById(long id) {
		long startTime = System.currentTimeMillis();
		try {
			OrderForm order = em.find(id);
			if(order != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据id查询订单实体] [成功] [id:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据id查询订单实体] [成功] [无对应此id对象] [id:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return order;
		} catch (Exception e) {
			logger.error("[根据id查询订单实体] [失败] [id:"+id+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}

	@Override
	public OrderForm getByOrderid(String orderId) {
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em.queryIds(OrderForm.class, " orderId = '" +orderId+"'");
			OrderForm order = em.find(ids[0]);
			if(order != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据orderId查询订单实体] [成功] [id:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据orderId查询订单实体] [成功] [无对应此orderId对象] [id:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return order;
		} catch (Exception e) {
			logger.error("[根据orderId查询订单实体] [失败] [orderId:"+orderId+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
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
			count = em.count(OrderForm.class, " passportId="+passportId );
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
			List<OrderForm> lst = em.query(OrderForm.class, " responseResult = "+OrderForm.RESPONSE_SUCC+" ", "responseTime desc", start, start + len);
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
			long []ids = em.queryIds(OrderForm.class, " handleResult = "+OrderForm.HANDLE_RESULT_NOBACK+" ");
			if(logger.isInfoEnabled())
			{
				logger.info("[获得某个账户没有从充值平台返回的订单] [成功] [查询id] [passportId:"+passportId+"] [数量:"+ids.length+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			List<OrderForm> lst = new ArrayList<OrderForm>();
			
			for(long id : ids)
			{
				OrderForm order = em.find(id);
				lst.add(order);
				if(logger.isDebugEnabled())
					logger.debug("[获得某个账户没有从充值平台返回的订单] [成功] [加入到List中] [passportId:"+passportId+"] [id:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
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
			List<OrderForm> lst = em.query(OrderForm.class, " passportId = "+passportId, "createTime desc", start, start + len );
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
			List<OrderForm> lst = em.query(OrderForm.class, "" ,"createTime desc", start, start + len);
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
	public List getSuccUnnotifiedOrderForms() {
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(OrderForm.class, " handleResult = "+OrderForm.HANDLE_RESULT_SUCC+" and responseResult = "+OrderForm.RESPONSE_SUCC+" and notified = 'F' ");
			if(logger.isInfoEnabled())
			{
//				logger.info("[获得成功充值且没有通知服务器的订单] [成功] [查询id] [数量:"+ids.length+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			List<OrderForm> lst = new ArrayList<OrderForm>();
			for(long id : ids)
			{
				OrderForm order = em.find(id);
				lst.add(order);
				if(logger.isDebugEnabled())
					logger.debug("[获得成功充值且没有通知服务器的订单] [成功] [加入到List中] [id:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			if(logger.isInfoEnabled())
			{
//				logger.info("[获得成功充值且没有通知服务器的订单] [成功] [数量:"+lst.size()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst;
			
		} catch (Exception e) {
			logger.error("[获得成功充值且没有通知服务器的订单] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}
	
	@Override
	public long getFailedOrderFormsCount() {
		long startTime = System.currentTimeMillis();
		try {
			long count = em.count(OrderForm.class, " handleResult = "+OrderForm.HANDLE_RESULT_FAILED+" or responseResult = "+OrderForm.RESPONSE_FAILED+" or (notified = 'T' and notifySucc = 'F')");
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
	public List getFailedOrderForms(int start, int len) {
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(OrderForm.class, " handleResult = "+OrderForm.HANDLE_RESULT_FAILED+" or responseResult = "+OrderForm.RESPONSE_FAILED+" or (notified = 'T' and notifySucc = 'F')",  "createTime DESC", start+1, start+1+len);
			if(logger.isInfoEnabled())
			{
				logger.info("[获得失败的订单] [成功] [查询id] [数量:"+ids.length+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			List<OrderForm> lst = new ArrayList<OrderForm>();
			for(long id : ids)
			{
				OrderForm order = em.find(id);
				lst.add(order);
				if(logger.isDebugEnabled())
					logger.debug("[获得失败的订单] [成功] [加入到List中] [id:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
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
	public void update(OrderForm orderForm,String fieldName) {
		long startTime = System.currentTimeMillis();
		try {
			em.notifyFieldChange(orderForm, fieldName);
			em.save(orderForm);
			if(logger.isInfoEnabled())
				logger.info("[更新订单实体单个字段] [成功] [id:"+orderForm.getId()+"] [orderId:"+orderForm.getOrderId()+"] [fieldName:"+fieldName+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[更新订单实体单个字段] [失败] [id:"+orderForm.getId()+"] [orderId:"+orderForm.getOrderId()+"] [fieldName:"+fieldName+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
	}

	@Override
	public void delete(OrderForm orderForm) {
		
	}
	
	/**
	 * 一个公用方法，用以批量修改传入的字段
	 * 
	 * @param fieldNames 传入字段名列表
	 * @throws Exception 
	 * added by liuyang at 2012-05-29
	 */
	public void batch_updateField(OrderForm orderForm,List<String> fieldNames) throws Exception
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
				logger.warn("[更新订单] [更新字段] [字段名有空值] [Id:"+orderForm.getId()+"] [orderId:"+orderForm.getOrderId()+"] [fieldName:"+fieldName+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			em.notifyFieldChange(orderForm, fieldName);
			if(logger.isInfoEnabled())
			{
				logger.info("[更新订单] [更新字段] [成功] [Id:"+orderForm.getId()+"] [orderId:"+orderForm.getOrderId()+"] [fieldName:"+fieldName+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
		}
		em.save(orderForm);
	}

	public SimpleEntityManager<OrderForm> getEm() {
		return em;
	}

	public void setEm(SimpleEntityManager<OrderForm> em) {
		this.em = em;
	}

	@Override
	public long count(String whereSql) throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			long count = 0l;
			count = em.count(OrderForm.class,  whereSql );
			if(logger.isInfoEnabled())
				logger.info("[订单根据Where条件进行查询] [成功] [Where条件:"+whereSql+"] [count:"+count+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			return count;
		} catch (Exception e) {
			logger.error("[订单根据passportIdcount统计] [失败] [Where条件:"+whereSql+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			throw new Exception("根据条件查询订单失败！");
		}
	}
	
	public List<OrderForm> queryForWhere(String whereSql) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			long []ids = em.queryIds(OrderForm.class, whereSql);
			if(logger.isInfoEnabled())
			{
				logger.info("[根据where条件查询订单] [成功] [查询id] [where条件:"+whereSql+"] [数量:"+ids.length+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			List<OrderForm> lst = new ArrayList<OrderForm>();
			
			for(long id : ids)
			{
				OrderForm order = em.find(id);
				lst.add(order);
				if(logger.isDebugEnabled())
					logger.debug("[根据where条件查询订单] [成功] [加入到List中] [where条件:"+whereSql+"] [id:"+order.getId()+"] [orderId:"+order.getOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
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
	
	public void update(OrderForm order) throws Exception
	{
		long startTime = System.currentTimeMillis();
		try {
			em.flush(order);
			if(logger.isInfoEnabled())
				logger.info("[flush订单] [成功] [订单 Id:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
		} catch (Exception e) {
			logger.error("[flush订单] [失败] [订单 Id:"+order.getId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			throw new Exception("更新订单失败！");
		}
	}

//	@Override
	public OrderForm getByChannelOrderid(String channelOrderId,String channel) {
		long startTime = System.currentTimeMillis();
		try {
			long ids[] = em.queryIds(OrderForm.class, " channelOrderId = '" +channelOrderId+"' and userChannel = '" +channel+ "'");
			OrderForm order = em.find(ids[0]);
			if(order != null)
			{
				if(logger.isInfoEnabled())
					logger.info("[根据channelOrderId查询订单实体] [成功] [id:"+order.getId()+"] [Channel orderId:"+order.getChannelOrderId()+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				logger.warn("[根据channelOrderId查询订单实体] [成功] [无对应此channelOrderId对象] [id:"+order.getId()+"] [渠道:"+channel+"] [Channel orderId:"+order.getChannelOrderId()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			
			return order;
		} catch (Exception e) {
			logger.error("[根据channelOrderId查询订单实体] [失败] [orderId:"+channelOrderId+"] [渠道:"+channel+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
		}
		
		return null;
	}
	
	
	
}
