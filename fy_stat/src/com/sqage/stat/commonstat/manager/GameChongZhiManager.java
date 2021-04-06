package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.entity.GameChongZhi;

public interface GameChongZhiManager {
	
	public GameChongZhi getById(Long id);

	public ArrayList<GameChongZhi> getBySql(String sql);
	
	public List<GameChongZhi> getByGameChongZhi(GameChongZhi gameChongZhi);
	
	public ArrayList<String[]> getGameChongZhiYinZi(String startDateStr);
	
	public ArrayList<String[]> getGameChongZhiYinPiao(String startDateStr);

	public boolean deleteById(Long id);

	public boolean addList(ArrayList<GameChongZhi> gameChongZhiList);
	
	public boolean addList_jifen(ArrayList<GameChongZhi> gameChongZhiList);
	
	
	public GameChongZhi add(GameChongZhi gameChongZhi);
	
	public boolean update(GameChongZhi gameChongZhi);
	
	/**
	 * 活动货币类型
	 * @return
	 */
	public ArrayList<String []> getCurrencyType();
	/**
	 * 消耗货币原因
	 * @return
	 */
	public ArrayList<String []> getReasontypeType();
	
	/**
	 * 货币数量统计
	 * @return
	 */
	public List<String[]> getChongZhiStat(String startDateStr,String endDateStr,String fenQu,String gameLevel, String currencyType,String reasontype,String action);
	/**
	 * 按游戏币的产生原因获取游戏充值的 
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param gameLevel
	 * @param currencyType
	 * @param reasontype
	 * @param action
	 * @return
	 */
	
	public List<String[]> getChongZhiStat_reasontype(String startDateStr,String endDateStr,String fenQu,String gameLevel, String currencyType,String reasontype,String action,String userName);
	/**
	 * 用户行为信息获取
	 * @param startDateStr
	 * @param endDateStr
	 * @param startNum
	 * @param endNum
	 * @param fenQu
	 * @param quDao
	 * @param gameLevel
	 * @param currencyType
	 * @param reasontype
	 * @param action
	 * @return
	 */
	public List<String[]> getPlayerActionWatch(String startDateStr, String endDateStr, String startNum, String endNum, String fenQu, String quDao,
			String gameLevel, String currencyType, String reasontype, String action);

}
