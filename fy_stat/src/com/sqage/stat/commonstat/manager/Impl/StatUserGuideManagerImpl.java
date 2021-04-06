package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sqage.stat.commonstat.dao.Impl.StatUserGuideDaoImpl;
import com.sqage.stat.commonstat.entity.StatUserGuide;
import com.sqage.stat.commonstat.manager.StatUserGuideManager;

public class StatUserGuideManagerImpl implements StatUserGuideManager {

	StatUserGuideDaoImpl statUserGuideDao;
	static StatUserGuideManagerImpl self;
	public void init(){ self = this; }
	
	public static StatUserGuideManagerImpl getInstance() {
			return self;
		}

	@Override
	public StatUserGuide add(StatUserGuide statUserGuide) {
		return statUserGuideDao.add(statUserGuide);
	}

	@Override
	public ArrayList<StatUserGuide> getBySql(String sql) {
		return statUserGuideDao.getBySql(sql);
	}

	@Override
	public List<StatUserGuide> getByUserNameAndFenQu(String userName, String fenQu) {
		return statUserGuideDao.getByUserNameAndFenQu(userName, fenQu);
	}

	@Override
	public boolean update(String userName, String fenQu, String step, String action) {
		return statUserGuideDao.update(userName, fenQu, step, action);
	}
	
	@Override
	public boolean delete(String userName, String fenQu) {
		return statUserGuideDao.delete(userName, fenQu);
	}
	public StatUserGuideDaoImpl getStatUserGuideDao() {
		return statUserGuideDao;
	}

	public void setStatUserGuideDao(StatUserGuideDaoImpl statUserGuideDao) {
		this.statUserGuideDao = statUserGuideDao;
	}

	@Override
	public Map search(String regStartDateStr, String regEndDateStr, String statStartdateStr, String statEnddateStr, String quDao, String fenQu) {
		return statUserGuideDao.search(regStartDateStr, regEndDateStr, statStartdateStr, statEnddateStr, quDao, fenQu);
	}
}
