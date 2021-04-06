package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;

import com.sqage.stat.commonstat.dao.Impl.YinZiKuCunDaoImpl;
import com.sqage.stat.commonstat.entity.YinZiKuCun;
import com.sqage.stat.commonstat.manager.YinZiKuCunManager;

public class YinZiKuCunManagerImpl implements YinZiKuCunManager {

	YinZiKuCunDaoImpl  yinZiKuCunDao;
	static YinZiKuCunManagerImpl self;
	public void init(){
		self = this;
	}
	public static YinZiKuCunManagerImpl getInstance() {
		return self;
	}
	@Override
	public void addList(ArrayList<YinZiKuCun> yinZiKuCunList) {
		yinZiKuCunDao.addList(yinZiKuCunList);
	}

	@Override
	public ArrayList<YinZiKuCun> getBySql(String sql) {
		return yinZiKuCunDao.getBySql(sql);
	}
	
	public YinZiKuCunDaoImpl getYinZiKuCunDao() {
		return yinZiKuCunDao;
	}
	public void setYinZiKuCunDao(YinZiKuCunDaoImpl yinZiKuCunDao) {
		this.yinZiKuCunDao = yinZiKuCunDao;
	}
	
}
