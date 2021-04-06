package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;

import com.sqage.stat.commonstat.dao.Impl.LibaoDaoImpl;
import com.sqage.stat.commonstat.entity.Libao;
import com.sqage.stat.commonstat.manager.LibaoManager;

public class LibaoManagerImpl implements LibaoManager {
	
	LibaoDaoImpl libaoDao;
	static LibaoManagerImpl self;
	public void init(){
		self = this;
	}
	
	public static LibaoManagerImpl getInstance() {
		return self;
	}
	
	@Override
	public boolean addList(ArrayList<Libao> libaoList) {
		return libaoDao.addList(libaoList);
	}

	@Override
	public ArrayList<Libao> getBySql(String sql) {
		return libaoDao.getBySql(sql);
	}
	
	@Override
	public ArrayList<String[]> getliBaoData(String sql, String[] strs) {
		return libaoDao.getliBaoData(sql, strs);
	}

	public LibaoDaoImpl getLibaoDao() {
		return libaoDao;
	}

	public void setLibaoDao(LibaoDaoImpl libaoDao) {
		this.libaoDao = libaoDao;
	}

	
}
