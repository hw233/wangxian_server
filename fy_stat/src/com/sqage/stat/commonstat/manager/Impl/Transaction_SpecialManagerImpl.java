package com.sqage.stat.commonstat.manager.Impl;

import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.Transaction_SpecialDaoImpl;
import com.sqage.stat.commonstat.entity.Transaction_Special;
import com.sqage.stat.commonstat.entity.Transaction_yinzi;
import com.sqage.stat.commonstat.manager.Transaction_SpecialManager;

public class Transaction_SpecialManagerImpl implements Transaction_SpecialManager {

	Transaction_SpecialDaoImpl  transaction_SpecialDao;
	static Transaction_SpecialManagerImpl self;
	public void init(){
		self = this;
	}
	public static Transaction_SpecialManagerImpl getInstance() {
		return self;
	}
	
	@Override
	public Transaction_Special add(Transaction_Special transaction_Special) {
		return transaction_SpecialDao.add(transaction_Special);
	}

	@Override
	public boolean excute(String sql) {
		return transaction_SpecialDao.excute(sql);
	}
	@Override
	public List<Transaction_Special> getBySql(String sql) {
		return transaction_SpecialDao.getBySql(sql);
	}
	
	@Override
	public List<Transaction_yinzi> getTransaction_yinziBySql(String sql) {
		return transaction_SpecialDao.getTransaction_yinziBySql(sql);
	}
	public Transaction_SpecialDaoImpl getTransaction_SpecialDao() {
		return transaction_SpecialDao;
	}
	
	@Override
	public List<String[]> getTransactionSpec_Total(String startDateStr, String endDateStr,String fenQu) {
		return transaction_SpecialDao.getTransactionSpec_Total(startDateStr, endDateStr,fenQu);
	}
	
	
	public void setTransaction_SpecialDao(Transaction_SpecialDaoImpl transactionSpecialDao) {
		transaction_SpecialDao = transactionSpecialDao;
	}
	@Override
	public List<String[]> getTransactionSpec_Total_fenqu(String startDateStr, String endDateStr, String fenQu) {
		return transaction_SpecialDao.getTransactionSpec_Total_fenqu(startDateStr, endDateStr, fenQu);
	}
	
	@Override
	public List<String[]> getTransactionSpec_Total_fenQu2(String startDateStr, String endDateStr, String fenQu) {
		return transaction_SpecialDao.getTransactionSpec_Total_fenQu2(startDateStr, endDateStr, fenQu);
	}

}
