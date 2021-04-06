package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.GameChongZhiDaoImpl;
import com.sqage.stat.commonstat.entity.GameChongZhi;
import com.sqage.stat.commonstat.manager.GameChongZhiManager;

public class GameChongZhiManagerImpl implements GameChongZhiManager {

	
	GameChongZhiDaoImpl gameChongZhiDao;
	
	static GameChongZhiManagerImpl self;
		
	public void init(){ self = this; }
	
	public static GameChongZhiManagerImpl getInstance() {
			return self;
		}
	@Override
	public boolean addList(ArrayList<GameChongZhi> gameChongZhiList) {
		return gameChongZhiDao.addList(gameChongZhiList);
	}
	
	@Override
	public boolean addList_jifen(ArrayList<GameChongZhi> gameChongZhiList) {
		return gameChongZhiDao.addList_jifen(gameChongZhiList);
	}

	@Override
	public GameChongZhi add(GameChongZhi gameChongZhi) {
		return gameChongZhiDao.add(gameChongZhi);
	}

	@Override
	public List<GameChongZhi> getByGameChongZhi(GameChongZhi gameChongZhi) {
		return gameChongZhiDao.getByGameChongZhi(gameChongZhi);
	}

	@Override
	public ArrayList<String[]> getGameChongZhiYinZi(String startDateStr) {
		return gameChongZhiDao.getGameChongZhiYinZi(startDateStr);
	}
	
	@Override
	public ArrayList<String[]> getGameChongZhiYinPiao(String startDateStr) {
		return gameChongZhiDao.getGameChongZhiYinPiao(startDateStr);
	}

	@Override
	public boolean update(GameChongZhi gameChongZhi) {
		return gameChongZhiDao.update(gameChongZhi);
	}

	@Override
	public boolean deleteById(Long id) {
		return gameChongZhiDao.deleteById(id);
	}

	@Override
	public GameChongZhi getById(Long id) {
		return gameChongZhiDao.getById(id);
	}

	@Override
	public ArrayList<GameChongZhi> getBySql(String sql) {
		return gameChongZhiDao.getBySql(sql);
	}

	public GameChongZhiDaoImpl getGameChongZhiDao() {
		return gameChongZhiDao;
	}

	public void setGameChongZhiDao(GameChongZhiDaoImpl gameChongZhiDao) {
		this.gameChongZhiDao = gameChongZhiDao;
	}

	@Override
	public List<String[]> getChongZhiStat(String startDateStr, String endDateStr, String fenQu, String gameLevel, String currencyType,
			String reasontype, String action) {
		return gameChongZhiDao.getChongZhiStat(startDateStr, endDateStr, fenQu, gameLevel, currencyType, reasontype, action);
	}

	@Override
	public List<String[]> getChongZhiStat_reasontype(String startDateStr, String endDateStr, String fenQu, String gameLevel, String currencyType,
			String reasontype, String action,String username) {
		return gameChongZhiDao.getChongZhiStat_reasontype(startDateStr, endDateStr, fenQu, gameLevel, currencyType, reasontype, action,username);
	}

	@Override
	public List<String[]> getPlayerActionWatch(String startDateStr, String endDateStr, String startNum, String endNum, String fenQu, String quDao,
			String gameLevel, String currencyType, String reasontype, String action) {
		return gameChongZhiDao.getPlayerActionWatch(startDateStr, endDateStr, startNum, endNum, fenQu, quDao, gameLevel, currencyType, reasontype, action);
	}

	@Override
	public ArrayList<String []> getCurrencyType() {
		return gameChongZhiDao.getCurrencyType();
	}

	@Override
	public ArrayList<String []> getReasontypeType() {
		return gameChongZhiDao.getReasontypeType();
	}

}
