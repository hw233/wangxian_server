package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;

import com.sqage.stat.commonstat.dao.Impl.BattleMSGDaoImpl;
import com.sqage.stat.commonstat.entity.Battle_PlayerStat;
import com.sqage.stat.commonstat.entity.Battle_TeamStat;
import com.sqage.stat.commonstat.entity.Battle_costTime;
import com.sqage.stat.commonstat.manager.BattleMSGManager;

public class BattleMSGManagerImpl implements BattleMSGManager {

	BattleMSGDaoImpl  battleMSGDao;
	static BattleMSGManagerImpl self;
	public void init(){
		self = this;
	}
	public static BattleMSGManagerImpl getInstance() {
		return self;
	}
	@Override
	public void addBattle_PlayerStatLis(ArrayList<Battle_PlayerStat> battlePlayerStatList) {
		battleMSGDao.addBattle_PlayerStatLis(battlePlayerStatList);
	}

	@Override
	public void addBattle_TeamStatList(ArrayList<Battle_TeamStat> battleTeamStatList) {
		battleMSGDao.addBattle_TeamStatList(battleTeamStatList);
	}

	@Override
	public void addBattle_costTimeList(ArrayList<Battle_costTime> battleCostTimeList) {
		battleMSGDao.addBattle_costTimeList(battleCostTimeList);
	}

	@Override
	public ArrayList<String[]> getBattleMSG(String sql, String[] columusEnums) {
		return battleMSGDao.getBattleMSG(sql, columusEnums);
	}
	public BattleMSGDaoImpl getBattleMSGDao() {
		return battleMSGDao;
	}
	public void setBattleMSGDao(BattleMSGDaoImpl battleMSGDao) {
		this.battleMSGDao = battleMSGDao;
	}
	
}
