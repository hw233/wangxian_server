package com.sqage.stat.commonstat.dao;

import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.entity.Taskinfo;

public interface TaskinfoDao {
	
	public Taskinfo getById(Long id);
	
	public List<Taskinfo> getBySql(String sql);
	
	public boolean deleteById(Long id);
	
	public Taskinfo add(Taskinfo taskinfo);
	
	public boolean addList(ArrayList<Taskinfo> taskinfoList);
	
	public boolean update(Taskinfo taskinfo);
	
	public boolean updateList(ArrayList<Taskinfo> taskinfoList);
	
	
	
	/**
	 * 获取任务的接受和完成信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param taskName
	 * @param tasktype
	 * @return
	 */
  public List<String[]> getTaskStat(String startDateStr, String endDateStr, String fenQu,String taskName,String tasktype,String status);
  /**
   * 完成任务收益
   * @param startDateStr
   * @param endDateStr
   * @param fenQu
   * @param taskName
   * @param gamelevel
   * @return
   */
  public List<String[]> getTaskShouYi(String startDateStr, String endDateStr, String fenQu,String taskName,String gamelevel);
  
  /**
	 * 获取任务类型
	 * @param date
	 * @return
	 */
	public ArrayList<String> gettaskType(java.util.Date date);
	
	/**
	 * 获取任务
	 * @param date
	 * @return
	 */
	public ArrayList<String> gettasks(java.util.Date date);
}
