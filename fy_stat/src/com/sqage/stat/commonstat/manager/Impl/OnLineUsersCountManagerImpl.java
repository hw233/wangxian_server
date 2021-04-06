package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.OnLineUsersCountDaoImpl;
import com.sqage.stat.commonstat.entity.OnLineUsersCount;
import com.sqage.stat.commonstat.manager.OnLineUsersCountManager;

public class OnLineUsersCountManagerImpl implements OnLineUsersCountManager {

	OnLineUsersCountDaoImpl onLineUsersCountDao;
	
    static OnLineUsersCountManagerImpl self;
	
	public void init(){
		self = this;
	}
	public static OnLineUsersCountManagerImpl getInstance() {
		return self;
	}
	
	public OnLineUsersCountDaoImpl getOnLineUsersCountDao() {
		return onLineUsersCountDao;
	}

	public void setOnLineUsersCountDao(OnLineUsersCountDaoImpl onLineUsersCountDao) {
		this.onLineUsersCountDao = onLineUsersCountDao;
	}

	@Override
	public OnLineUsersCount add(OnLineUsersCount OnLineUsersCount) {
		return onLineUsersCountDao.add(OnLineUsersCount);
	}

	@Override
	public boolean deleteById(Long id) {
		return onLineUsersCountDao.deleteById(id);
	}

	@Override
	public OnLineUsersCount getById(Long id) {
		return onLineUsersCountDao.getById(id);
	}

	@Override
	public List<OnLineUsersCount> getBySql(String sql) {
		return onLineUsersCountDao.getBySql(sql);
	}

	@Override
	public boolean addList(ArrayList<OnLineUsersCount> onLineUsersCountList) {
		return onLineUsersCountDao.addList(onLineUsersCountList);
	}

	@Override
	public boolean update(OnLineUsersCount OnLineUsersCount) {
		return onLineUsersCountDao.update(OnLineUsersCount);
	}

	@Override
	public Long getAvgOnlineUserCount(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return onLineUsersCountDao.getAvgOnlineUserCount(startDateStr,endDateStr, qudao, fenQu, jixing);
	}

	@Override
	public Long getMaxOnlineUserCount(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return onLineUsersCountDao.getMaxOnlineUserCount(startDateStr,endDateStr, qudao, fenQu, jixing);
	}
	@Override
	public boolean updateChongZhiQuDao(String oldQuDao, String userName, String newQuDao) {
		return onLineUsersCountDao.updateChongZhiQuDao(oldQuDao, userName, newQuDao);
	}
	@Override
	public boolean updatePlayGameQuDao(String oldQuDao, String userName, String newQuDao) {
		return onLineUsersCountDao.updatePlayGameQuDao(oldQuDao, userName, newQuDao);
	}
	@Override
	public boolean updateRegistQuDao(String oldQuDao, String userName, String newQuDao) {
		return onLineUsersCountDao.updateRegistQuDao(oldQuDao, userName, newQuDao);
	}
	@Override
	public ArrayList<String[]> getOnlineInfo(String sql, String[] columusEnums) {
		return onLineUsersCountDao.getOnlineInfo(sql, columusEnums);
	}
	
	
}
