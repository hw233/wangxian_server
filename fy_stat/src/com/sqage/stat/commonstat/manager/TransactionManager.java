package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.entity.Transaction;

public interface TransactionManager {

	public Transaction getById(Long id);
	
	public List<Transaction> getBySql(String sql);
	
	public boolean deleteById(Long id);
	
	public Transaction add(Transaction transaction);
	
	public void addList(ArrayList<Transaction> transactionList);
	
	public boolean update(Transaction transaction);
	
	public boolean updateList(ArrayList<Transaction> transactionList);
	
	/**
	 * 获取交易类型
	 * @param date
	 * @return
	 */
	public ArrayList<String> getTransactiontype(java.util.Date date);
	
	
	/**
	 * 道具交易信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public ArrayList<String[]> getDaoJuJiaoYi(String startDateStr, String endDateStr, String fenQu);
	
	/**
	 * 交易信息汇总
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	public ArrayList<String[]> getJiaoYiHuiZong(String startDateStr);
	
	/**
	 * 道具交易信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	
	public ArrayList<String[]> getDaoJuGetType(String startDateStr, String endDateStr, String fenQu);
	/**
	 * 根据道具名称汇总
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public ArrayList<String[]> getDaoJuName(String startDateStr, String endDateStr, String fenQu);
	
	
	/**
	 * 酒的快速交易信息
	 * @param startDateStr
	 * @param jiuName
	 * @return
	 */
	public ArrayList<String[]> getJiuHuiZong(String startDateStr,String jiuName);
	
	
	/**
	 * @param sql
	 * @param columusEnums sql中要获取的数字段
	 * @return
	 */
	
   public ArrayList<String[]> getDaoJuinfo(String sql,String[] columusEnums);
}
