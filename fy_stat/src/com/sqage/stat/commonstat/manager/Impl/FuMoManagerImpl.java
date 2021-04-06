package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;

import com.sqage.stat.commonstat.dao.Impl.FuMoDaoImpl;
import com.sqage.stat.commonstat.entity.FuMo;
import com.sqage.stat.commonstat.manager.FuMoManager;

public class FuMoManagerImpl implements FuMoManager {

	FuMoDaoImpl  fuMoDao;
	static FuMoManagerImpl self;
	public void init(){
		self = this;
	}
	public static FuMoManagerImpl getInstance() {
		return self;
	}
	@Override
	public boolean addFinishFuMoList(ArrayList<FuMo> fuMoList) {
		return fuMoDao.addFinishFuMoList(fuMoList);
	}
	@Override
	public boolean addUseFoMoList(ArrayList<FuMo> fuMoList) {
		return fuMoDao.addUseFoMoList(fuMoList);
	}
	@Override
	public ArrayList<FuMo> getBySql(String sql) {
		return fuMoDao.getBySql(sql);
	}
	@Override
	public ArrayList<String[]> getFuMoData(String sql, String[] strs) {
		return fuMoDao.getFuMoData(sql, strs);
	}
	public FuMoDaoImpl getFuMoDao() {
		return fuMoDao;
	}
	public void setFuMoDao(FuMoDaoImpl fuMoDao) {
		this.fuMoDao = fuMoDao;
	}
}
