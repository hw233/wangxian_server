package com.sqage.stat.commonstat.dao;

import java.util.ArrayList;

import com.sqage.stat.commonstat.entity.Battle_PlayerStat;
import com.sqage.stat.commonstat.entity.Battle_TeamStat;
import com.sqage.stat.commonstat.entity.Battle_costTime;

public interface BattleMSGDao {

	public ArrayList<String[]> getBattleMSG(String sql, String[] columusEnums);
	
	
	/**
	 * 对战团队功勋信息
	 * @param
	 */
	public void addBattle_TeamStatList(ArrayList<Battle_TeamStat> battle_TeamStatList);
	/**
	 * 对战个人功勋信息
	 */
	public void addBattle_PlayerStatLis(ArrayList<Battle_PlayerStat> battle_PlayerStatList);
	
	/**
	 * 对战消耗时间功勋信息
	 */
	public void addBattle_costTimeList(ArrayList<Battle_costTime> battle_costTimeList);
	
	
	
}
