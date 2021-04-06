package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;
import java.util.List;

import com.sqage.stat.commonstat.entity.HuoDonginfo;

public interface HuoDonginfoManager {
	
	public HuoDonginfo getById(Long id);
	
	public List<HuoDonginfo> getBySql(String sql);
	
	public boolean deleteById(Long id);
	
	public HuoDonginfo add(HuoDonginfo huoDonginfo);
	
	public boolean addList(List<HuoDonginfo> ls);
	
	public boolean update(HuoDonginfo huoDonginfo);
	
	public boolean updateList(ArrayList<HuoDonginfo> huoDonginfoList);
	
	
	
	/**
	 * 获取活动的接受和完成信息
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @param taskName
	 * @param tasktype
	 * @return
	 */
  public List<String[]> gethuoDonginfoStat(String startDateStr, String endDateStr, String fenQu,String taskName,String tasktype,String status);
  /**
   * 完成活动收益
   * @param startDateStr
   * @param endDateStr
   * @param fenQu
   * @param taskName
   * @param gamelevel
   * @return
   */
  public List<String[]> gethuoDonginfoShouYi(String startDateStr, String endDateStr, String fenQu,String taskName,String gamelevel);
  /**
   * 查询完成活动的信息信息
   * @param startDateStr
   * @param endDateStr
   * @param fenQu
   * @param taskName
   * @param gamelevel
   * @return
   */
  public List<HuoDonginfo> gethuoDonginfoShouYiDetail(String startDateStr, String endDateStr, String fenQu,String taskName,String gamelevel);
  /**
	 * 获取活动类型
	 * @param date
	 * @return
	 */
	public ArrayList<String> gethuoDonginfoType(java.util.Date date);
	
	/**
	 * 获取活动
	 * @param date
	 * @return
	 */
	public ArrayList<String> gethuoDonginfos(java.util.Date date);
}
