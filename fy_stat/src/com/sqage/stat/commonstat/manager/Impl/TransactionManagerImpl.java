package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.TransactionDaoImpl;
import com.sqage.stat.commonstat.entity.Transaction;
import com.sqage.stat.commonstat.manager.TransactionManager;

public class TransactionManagerImpl implements TransactionManager {

	TransactionDaoImpl  transactionDao;
	static TransactionManagerImpl self;
	public void init(){
		self = this;
	}
	public static TransactionManagerImpl getInstance() {
		return self;
	}
	
	@Override
	public Transaction add(Transaction transaction) {
		return transactionDao.add(transaction);
	}

	@Override
	public void addList(ArrayList<Transaction> transactionList) {
		 transactionDao.addList(transactionList);
	}
	@Override
	public boolean deleteById(Long id) {
		return transactionDao.deleteById(id);
	}

	@Override
	public Transaction getById(Long id) {
		return transactionDao.getById(id);
	}

	@Override
	public List<Transaction> getBySql(String sql) {
		return transactionDao.getBySql(sql);
	}

	@Override
	public boolean update(Transaction transaction) {
		return transactionDao.update(transaction);
	}

	@Override
	public boolean updateList(ArrayList<Transaction> transactionList) {
		return transactionDao.updateList(transactionList);
	}
	public TransactionDaoImpl getTransactionDao() {
		return transactionDao;
	}
	public void setTransactionDao(TransactionDaoImpl transactionDao) {
		this.transactionDao = transactionDao;
	}
	@Override
	public ArrayList<String> getTransactiontype(Date date) {
		return transactionDao.getTransactiontype(date);
	}
	@Override
	public ArrayList<String[]> getDaoJuJiaoYi(String startDateStr, String endDateStr, String fenQu) {
		return transactionDao.getDaoJuJiaoYi(startDateStr, endDateStr, fenQu);
	}
	
	@Override
	public ArrayList<String[]> getJiaoYiHuiZong(String startDateStr) {
		return transactionDao.getJiaoYiHuiZong(startDateStr);
	}
	@Override
	public ArrayList<String[]> getDaoJuGetType(String startDateStr, String endDateStr, String fenQu) {
		return transactionDao.getDaoJuGetType(startDateStr, endDateStr, fenQu);
	}
	@Override
	public ArrayList<String[]> getDaoJuName(String startDateStr, String endDateStr, String fenQu) {
		return transactionDao.getDaoJuName(startDateStr, endDateStr, fenQu);
	}
	@Override
	public ArrayList<String[]> getJiuHuiZong(String startDateStr, String jiuName) {
		return transactionDao.getJiuHuiZong(startDateStr, jiuName);
	}
	@Override
	public ArrayList<String[]> getDaoJuinfo(String sql, String[] columusEnums) {
		return transactionDao.getDaoJuinfo(sql, columusEnums);
	}

	
}
