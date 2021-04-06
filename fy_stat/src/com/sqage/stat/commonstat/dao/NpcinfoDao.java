/**
 * 
 */
package com.sqage.stat.commonstat.dao;

import java.util.ArrayList;

import com.sqage.stat.commonstat.entity.Npcinfo;

/**
 * 
 */
public interface NpcinfoDao {
	
	
	///public List<OnLineUsersCount> getBySql(String sql);
	
	//public OnLineUsersCount add(OnLineUsersCount onLineUsersCount);
	
	public  boolean addList(ArrayList<Npcinfo> NpcinfoList);

	public ArrayList<String[]> getNpcinfo(String sql, String[] columusEnums);
}
