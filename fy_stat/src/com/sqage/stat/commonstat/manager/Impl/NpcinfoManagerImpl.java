package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;

import com.sqage.stat.commonstat.dao.Impl.NpcinfoDaoImpl;
import com.sqage.stat.commonstat.entity.Npcinfo;
import com.sqage.stat.commonstat.manager.NpcinfoManager;

public class NpcinfoManagerImpl implements NpcinfoManager {

	NpcinfoDaoImpl npcinfoDao;
    static NpcinfoManagerImpl self;
	public void init(){
		self = this;
	}
	public static NpcinfoManagerImpl getInstance() {
		return self;
	}
	
	@Override
	public boolean addList(ArrayList<Npcinfo> npcinfoList) {
		return npcinfoDao.addList(npcinfoList);
	}
	@Override
	public ArrayList<String[]> getNpcinfo(String sql, String[] columusEnums) {
		return npcinfoDao.getNpcinfo(sql, columusEnums);
	}
	public NpcinfoDaoImpl getNpcinfoDao() {
		return npcinfoDao;
	}
	public void setNpcinfoDao(NpcinfoDaoImpl npcinfoDao) {
		this.npcinfoDao = npcinfoDao;
	}
}
