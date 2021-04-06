package com.sqage.stat.commonstat.manager.Impl;

import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.Stat_base_onlineDaoImpl;
import com.sqage.stat.commonstat.manager.Stat_base_onlineManager;

public class Stat_base_onlineManagerImpl implements Stat_base_onlineManager {
	
	Stat_base_onlineDaoImpl stat_base_onlineDao;
	
	static Stat_base_onlineManagerImpl self;
	 
	 public void init(){
		self = this;
	}
	public static Stat_base_onlineManagerImpl getInstance() {
			return self;
		}
	@Override
	public boolean addStat_base_online(String[] data) {
		return stat_base_onlineDao.addStat_base_online(data);
	}

	@Override
	public List<String[]> getStat_base_online(String dateStr) {
		return stat_base_onlineDao.getStat_base_online(dateStr);
	}

	public Stat_base_onlineDaoImpl getStat_base_onlineDao() {
		return stat_base_onlineDao;
	}

	public void setStat_base_onlineDao(Stat_base_onlineDaoImpl statBaseOnlineDao) {
		stat_base_onlineDao = statBaseOnlineDao;
	}

}
