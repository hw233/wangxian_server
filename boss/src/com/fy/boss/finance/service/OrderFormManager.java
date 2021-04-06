package com.fy.boss.finance.service;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.fy.boss.finance.dao.OrderFormDAO;
import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

public class OrderFormManager {

	protected static OrderFormManager m_self = null;
    
	protected static final Logger log = Logger.getLogger(OrderFormManager.class);
	    
    protected OrderFormDAO orderFormDAO;
        
    public static OrderFormManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		m_self = this;
		System.out.println("["+OrderFormManager.class.getName()+"] [initialized]");
		log.info("["+OrderFormManager.class.getName()+"] [initialized]");
	}
	
	/**
	 * 创建一个订单
	 * @param orderForm
	 * @return
	 */
	public OrderForm createOrderForm(OrderForm orderForm) {
		orderFormDAO.saveNew(orderForm);
		log.warn("[创建订单] " + orderForm.getLogStr());
		return orderForm;
	}

	/**
	 * 通过id获得一个订单
	 * @param id
	 * @return
	 */
	public OrderForm getOrderForm(long id) {
		OrderForm orderForm = orderFormDAO.getById(id);
		return orderForm;
	}
	
	/**
	 * 通过订单号获得一个订单
	 * @param orderId
	 * @return
	 */
	public OrderForm getOrderForm(String orderId) {
		return orderFormDAO.getByOrderid(orderId);
	}
	

//	 * 通过渠道订单号获得一个订单
//	 * @param orderId
//	 * @return
//	 */
	public OrderForm getByChannelOrderid(String channelOrderId,String userChannel) {
		return orderFormDAO.getByChannelOrderid(channelOrderId,userChannel);
	}
	
	/**
	 * 得到所有订单数量
	 * @return
	 */
	public long getCount() {
		return orderFormDAO.getCount();
	}
	
	/**
	 * 分页获得玩家成功订单
	 * @param passportId
	 * @param start
	 * @param length
	 * @return
	 */
	public List getUserSuccessOrderForms(long passportId, int start, int length) {
		return orderFormDAO.getUserSuccPageRows(passportId, start, length);
	}
	
	/**
	 * 获得用户没有response的订单
	 * @param passportId
	 * @return
	 */
	public List getUserUnbackedOrderForms(long passportId) {
		return orderFormDAO.getUserUnbackedPageRows(passportId);
	}
	
	/**
	 * 分页获得用户订单
	 * @param passportId
	 * @param start
	 * @param length
	 * @return
	 */
	public List getUserOrderForms(long passportId, int start, int length) {
		return orderFormDAO.getUserPageRows(passportId, start, length);
	}
	
	/**
	 * 获得用户所有订单数量
	 * @param passportId
	 * @return
	 */
	public long getUserOrderFormCount(long passportId) {
		return orderFormDAO.getCount(passportId);
	}
	
	/**
	 * 分页获得订单
	 * @param start
	 * @param length
	 * @return
	 */
	public List getOrderForms(int start, int length) {
		return orderFormDAO.getPageRows(start, length);
	}
	
	/**
	 * 获得成功充值但是没有通知服务器的订单
	 * @return
	 */
	public List getSuccAndUnnotifiedOrderForms() {
		return orderFormDAO.getSuccUnnotifiedOrderForms();
	}
	
	/**
	 * 获得所有失败的订单
	 * 失败的定义为：1-调用响应为失败的， 2-回调为失败的， 3-通知服务器失败的
	 * @return
	 */
	public List getFailedOrderForms(int start, int len) {
		return orderFormDAO.getFailedOrderForms(start, len);
	}
	
	public long getFailedOrderFormsCount() {
		return orderFormDAO.getFailedOrderFormsCount();
	}
	
	/*/**
	 * 更新一个订单
	 * @param d
	 */
/*	public void updateOrderForm(OrderForm d) {
		orderFormDAO.update(d);
		log.warn("[更新订单] " + d.getLogStr());
	}*/
	
	
	public void updateOrderForm(OrderForm d,String fieldName) {
		orderFormDAO.update(d,fieldName);
		log.warn("[更新订单单个字段] [字段名:"+fieldName+"] " + d.getLogStr());
	}
	
	public void batch_updateField(OrderForm orderForm,List<String> fieldNames)
	{
		try {
			orderFormDAO.batch_updateField(orderForm, fieldNames);
		} catch (Exception e) {
			log.error("[批量更新字段] [失败] ",e);
		}
	}
	

	public OrderFormDAO getOrderFormDAO() {
		return orderFormDAO;
	}

	public void setOrderFormDAO(OrderFormDAO orderFormDAO) {
		this.orderFormDAO = orderFormDAO;
	}
	
	public SimpleEntityManager<OrderForm> getEm()
	{
		return this.orderFormDAO.getEm();
	}
	
	//根据卡号和充值类型统计订单
	public long getCountForCardNoAndPayType(String cardNo,String payType) throws Exception
	{
		long count = this.orderFormDAO.count(" mediumInfo='"+cardNo+"' and savingMedium='"+payType+"'" );
		return count;
	}
	
	public void update(OrderForm order) throws Exception
	{
		this.orderFormDAO.update(order);
	}
	
	//根据cardNo和充值类型查询订单
	public List<OrderForm> getOrderFormForCardNoAndPayType(String cardNo,String payType)
	{	
		long startTime = System.currentTimeMillis();
		try {
			List<OrderForm> lst = this.orderFormDAO.queryForWhere(" mediumInfo='"+cardNo+"' and savingMedium='"+payType+"'");
			if(lst != null)
			{
				if(log.isInfoEnabled())
					log.info("[根据充值卡号和充值类型查询订单] [成功] [充值卡号:"+cardNo+"] [充值类型:"+payType+"] [结果集大小:"+lst.size()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				log.error("[根据充值卡号和充值类型查询订单] [失败] [充值卡号:"+cardNo+"] [充值类型:"+payType+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst; 
		} catch (Exception e) {
			log.error("[根据充值卡号和充值类型查询订单] [失败] [充值卡号:"+cardNo+"] [充值类型:"+payType+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		}
	}
	//根据条件查询订单
	public List<OrderForm> getOrderFormsByCondition(String whereSql)
	{	
		long startTime = System.currentTimeMillis();
		try {
			List<OrderForm> lst = this.orderFormDAO.queryForWhere(whereSql);
			if(lst != null)
			{
				if(log.isInfoEnabled())
					log.info("[根据条件查询订单] [成功] [查询条件:"+whereSql+"] [结果集大小:"+lst.size()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			else
			{
				log.error("[根据条件查询订单] [失败] [查询条件:"+whereSql+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
			}
			return lst; 
		} catch (Exception e) {
			log.error("[根据条件查询订单] [失败] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
			return null;
		}
	}
	
	/**
	 * 获得一段时间内充值成功的次数
	 * @param deviceCode
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public long getPeriodSavingCount(String deviceCode, long start, long end) throws Exception {
		long count = this.orderFormDAO.count(" notifySucc='T' and deviceCode='"+deviceCode+"' and createTime>="+start+" and createTime<" + end);
		return count;
	}
	
	/**
	 * 获得设备今日的充值总额
	 * @param deviceCode
	 * @return
	 */
	public long getDeviceTodaySavingAmount(String deviceCode) {
		Calendar cal = Calendar.getInstance();
		long end = cal.getTimeInMillis();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long start = cal.getTimeInMillis();
		
		try {
			List<OrderForm> list = orderFormDAO.queryForWhere(" notifySucc='T' and deviceCode like '"+deviceCode+"%' and createTime>="+start+" and createTime<" + end);
			long amount = 0;
			if(list != null) {
				for(OrderForm order : list) {
					amount  += order.getPayMoney();
				}
			}
			return amount;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("查询设备今日充值时发生异常：deviceCode=("+deviceCode+")", e);
		}
		return 0;
	}
	
	
	/**
	 * 
	 */
	
	/**
	 * 获得一段时间内充值成功的次数
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public long getPeriodSavingAmountByPlayer(long playerId,long start, long end) throws Exception {
		List<OrderForm> orderList = this.orderFormDAO.queryForWhere(" notifySucc='T' and createTime>="+start+" and createTime<" + end +" and memo1='"+playerId+"'");
		long savingAmount = 0l;
		
		if(orderList != null)
		{
			Iterator<OrderForm> it = orderList.iterator();
			while(it.hasNext())
			{
				OrderForm orderForm = it.next();
				if(orderForm != null)
				{
					savingAmount+= orderForm.getPayMoney();
				}
			}
		}
		
		return savingAmount;
	}
	
}
