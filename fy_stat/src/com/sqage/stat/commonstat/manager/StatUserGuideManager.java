package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sqage.stat.commonstat.entity.StatUserGuide;

public interface StatUserGuideManager {

	
	public ArrayList<StatUserGuide> getBySql(String sql);
	public StatUserGuide add(StatUserGuide statUserGuide);
	//public boolean update(StatUserGuide statUserGuide);
	/**
	 * 
	 * @param step   //更新的步骤
	 * @param action // 该步骤的操作
	 * @return
	 */
	public boolean update(String userName,String fenQu,String step, String action);
	/**
	 * 按用户名和分区查询用户
	 * @param userName
	 * @param fenQu
	 * @return
	 */
	public List<StatUserGuide> getByUserNameAndFenQu(String userName,String fenQu);
	
	/**
	 * 删除
	 * @param userName
	 * @param fenQu
	 * @return
	 */
	public boolean delete(String userName, String fenQu);
	/**
	 * 查询新手引导信息
	 * @param regStartDateStr
	 * @param regEndDateStr
	 * @param statStartdateStr
	 * @param statEnddateStr
	 * @param quDao
	 * @param fenQu
	 * @return
	 */
	public Map search(String regStartDateStr,String regEndDateStr,String statStartdateStr,String statEnddateStr,String quDao,String fenQu);

}
