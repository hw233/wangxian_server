/**
 * 
 */
package com.sqage.stat.commonstat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.entity.OnLineUsersCount;

/**
 * 
 *
 */
public interface OnLineUsersCountDao {
	
	
    public OnLineUsersCount getById(Long id);
	
	public List<OnLineUsersCount> getBySql(String sql);
	
	public boolean deleteById(Long id);
	
	public OnLineUsersCount add(OnLineUsersCount onLineUsersCount);
	
	public  boolean addList(ArrayList<OnLineUsersCount> onLineUsersCountList);
	
	public boolean update(OnLineUsersCount onLineUsersCount);
	
	
	
	/**
	 *平均每分钟在线人数
	 * @param dateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getAvgOnlineUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing);
	
	/**
	 *最高在线人数
	 * @param dateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getMaxOnlineUserCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing);
	
	
	/**
	 * 更新注册渠道
	 * @return
	 */
	public boolean updateRegistQuDao(String oldQuDao,String userName,String newQuDao);
	
	/**
	 * 更进入渠道
	 * @return
	 */
	public boolean updatePlayGameQuDao(String oldQuDao,String userName,String newQuDao);
	
	/**
	 * 更充值渠道
	 * @return
	 */
	public boolean updateChongZhiQuDao(String oldQuDao,String userName,String newQuDao);

	public ArrayList<String[]> getOnlineInfo(String sql, String[] columusEnums);
}
