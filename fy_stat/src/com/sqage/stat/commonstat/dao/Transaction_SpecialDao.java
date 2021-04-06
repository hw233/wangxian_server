package com.sqage.stat.commonstat.dao;

import java.util.List;
import com.sqage.stat.commonstat.entity.Transaction_Special;
import com.sqage.stat.commonstat.entity.Transaction_yinzi;

public interface Transaction_SpecialDao {

	public List<Transaction_Special> getBySql(String sql);
	
	public Transaction_Special add(Transaction_Special transaction_Special);
	/**
	 * 数据库定时执行sql
	 * @param sql
	 * @return
	 */
	public boolean excute(String sql);
	/**
	 * 获得银子数据
	 * @param sql
	 * @return
	 */
	public List<Transaction_yinzi> getTransaction_yinziBySql(String sql);
	
	/**
	 * 特殊交易的汇总
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	public List<String []> getTransactionSpec_Total(String startDateStr, String endDateStr,String fenQu); 
	/**
	 * 特殊交易的按分区汇总
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	public List<String[]> getTransactionSpec_Total_fenQu2(String startDateStr, String endDateStr,String fenQu);
	/**
	 * 特殊交易按分区的汇总
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	public List<String []> getTransactionSpec_Total_fenqu(String startDateStr, String endDateStr,String fenQu); 
}
