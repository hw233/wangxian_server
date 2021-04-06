package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;

import com.sqage.stat.commonstat.dao.Impl.Transaction_FaceDaoImpl;
import com.sqage.stat.commonstat.entity.Transaction_Face;
import com.sqage.stat.commonstat.entity.Transfer_Platform;
import com.sqage.stat.commonstat.manager.Transaction_FaceManager;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class Transaction_FaceManagerImpl implements Transaction_FaceManager {

	Transaction_FaceDaoImpl  transaction_FaceDao;
	static Transaction_FaceManagerImpl self;
	public void init(){
		self = this;
	}
	public static Transaction_FaceManagerImpl getInstance() {
		return self;
	}
	@Override
	public void addList(ArrayList<Transaction_Face> transaction_FaceList) {
		transaction_FaceDao.addList(transaction_FaceList);
		
	}
	@Override
	public ArrayList<String[]> getTransaction_Face(String sql, String[] columusEnums) {
		return transaction_FaceDao.getTransaction_Face(sql, columusEnums);
	}
	
	@Override
	public void addTransferFormList(ArrayList<Transfer_Platform> transferPlatformList) {
		transaction_FaceDao.addTransferFormList(transferPlatformList);
	}
	
	@Override
	public void addTransferFormList_2(ArrayList<Transfer_Platform> transferPlatformList) {
		transaction_FaceDao.addTransferFormList_2(transferPlatformList);
	}
	
	@Override
	public void addTransferFormList_3(ArrayList<Transfer_Platform> transferPlatformList) {
		transaction_FaceDao.addTransferFormList_3(transferPlatformList);
	}
	public DataSourceManager getDataSourceManager() {
		return transaction_FaceDao.getDataSourceManager();
	}
	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		transaction_FaceDao.setDataSourceManager(dataSourceManager);
	}

	
	public Transaction_FaceDaoImpl getTransaction_FaceDao() {
		return transaction_FaceDao;
	}
	public void setTransaction_FaceDao(Transaction_FaceDaoImpl transactionFaceDao) {
		transaction_FaceDao = transactionFaceDao;
	}

	
	
}
