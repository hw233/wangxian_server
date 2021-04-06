package com.fy.engineserver.economic.thirdpart.migu.entity;

import java.util.List;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;



public interface SaleRecordDAO {
	/**
	 * 创建一个新订单
	 * @param saleRecord
	 */
	public void saveNew(SaleRecord saleRecord);
	
	/**
	 * 通过id获得订单
	 * @param id
	 * @return
	 */
	public SaleRecord getById(long id);
	
	/**
	 * 通过订单号获得订单
	 * @param saleRecordId
	 * @return
	 */
	public SaleRecord getByOrderid(String saleRecordId);
	

	/**
	 * 通过渠道订单号获得订单
	 */
	public SaleRecord getByChannelOrderid(String channelOrderId,String channel);

	
	/**
	 * 获得所有订单数量
	 * @return
	 */
	public long getCount();
	
	/**
	 * 获得某个帐号的订单数量
	 * @param passportId
	 * @return
	 */
	public long getCount(long passportId);
	
	/**
	 * 获得某个帐号成功完成充值的订单
	 * @param passportId
	 * @param start
	 * @param len
	 * @return
	 */
	public List getUserSuccPageRows(long passportId, int start, int len);
	
	/**
	 * 获得某个账户没有从充值平台返回的订单
	 * @param passportId
	 * @return
	 */
	public List getUserUnbackedPageRows(long passportId);
	
	/**
	 * 分页获取某个帐号的订单，按时间逆序
	 * @param passportId
	 * @param start
	 * @param len
	 * @return
	 */
	public List getUserPageRows(long passportId, int start, int len);
	
	/**
	 * 分页获得所有订单，按逆序
	 * @param start
	 * @param len
	 * @return
	 */
	public List getPageRows(int start, int len);
	
	/**
	 * 获得成功充值且没有通知服务器的订单
	 * 要求为成功调用(handleResult=1)、成功返回(responseResult=1)、没有通知服务器(notified=false)
	 * @return
	 */
	public List getSuccUnnotifiedSaleRecords();
	
	public List getFailedSaleRecords(int start, int len);
	
	public long getFailedSaleRecordsCount();
	
	/**
	 * 更新一个订单
	 * @param saleRecordForm
	 */
	public void update(SaleRecord saleRecordForm,String fieldName);
	
	/**
	 * 删除一个订单
	 * @param saleRecordForm
	 */
	public void delete(SaleRecord saleRecordForm);
	
	/**
	 * 批量更新指定字段
	 */
	
	public void batch_updateField(SaleRecord saleRecordForm,List<String> fieldNames) throws Exception;
	
	/**
	 * 获取SimpleEntity对象
	 */
	public SimpleEntityManager<SaleRecord> getEm();
	
	/**
	 * 根据where 条件统计 订单
	 * 
	 */
	public long count(String whereSql) throws Exception;
	
	
	public long count(String preparedWhereSql,Object[] paramvalues) throws Exception;
	
	/**
	 * 直接flush到数据库中
	 * @param saleRecord
	 * @throws Exception
	 */
	public void update(SaleRecord saleRecord) throws Exception;
	
	/**
	 * 根据where条件查询订单
	 * @param whereSql
	 * @return
	 * @throws Exception
	 */
	public List<SaleRecord> queryForWhere(String whereSql) throws Exception;
	
	public boolean isRoleSaling(long userId) throws Exception;
	
	
}
