/**
 * 
 */
package com.sqage.stat.commonstat.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sqage.stat.commonstat.entity.User;

/**
 * 
 *
 */
public interface UserDao {

	
   public User getById(Long id);
	
	public List<User> getBySql(String sql);
	
	
	
	
	public  List<User> getByUserName(String userName);
	
	public boolean deleteById(Long id);
	
	public User add(User user);
	
	public boolean update(User user);
	
	public boolean updateList(ArrayList<User> userList);
	
	public ArrayList<String> getQudao(Date date);
	
	
	
	public boolean delFenQu(String fenquname);
	public boolean addFenQu(String fenqu);
	public boolean updateFenQu(String fenqu,String dateStr);
	
	public ArrayList<String []> getFenQu(Date date);
	
	/**
	 * 根据不同状态获取分区信息
	 * 服务器状态： 0正常    1合并掉     2 测试服
	 * @param type
	 * @return
	 */
	public ArrayList<String []> getFenQuByStatus(String status);
	/**
	 * 获取的分组信息
	 * @param date
	 * @return
	 */
	public ArrayList<String []> getFenQuGroup(Date date);
	/**
	 * 获取带分组的分区信息
	 * @param date
	 * @return
	 */
	public ArrayList<String []> getFenQu_Group(Date date);
	
	
	public boolean addCurrencyType(String currencyType);
	public ArrayList<String []> getCurrencyType();
	
	public boolean addDaoJuColor(String daoJuColor);
	public ArrayList<String []> getDaoJuColor();
	
	public boolean addDaoJuName(String daoJuName);
	public ArrayList<String []> getDaoJuName();

	public boolean addGetType(String getType);
	public ArrayList<String []> getGetType();
	
	public boolean addReasonType(String reasonType);
	public ArrayList<String []> getReasonType();
	
	
	/**
	 * 注册用户按月充值
	 * @param regStartDateStr
	 * @param regEndDateStr
	 * @param statStartdateStr
	 * @param quDao
	 * @param fenQu
	 * @return
	 */
	public ArrayList<String []> getZhuCeUserChongzhi_Mon(String regStartDateStr,String regEndDateStr,String statStartdateStr,String statEnddateStr,String quDao,String zhouqi,String jiXing);

	
	
	public ArrayList<String> getQudaoRegistUserCount(String dateStr);
	
	/**
	 * 获取注册但是没有创建角色的用户数
	 * @param statDateStr 开始日期
	 * @param endDateStr  终止日期
	 * @return
	 */
	public Long getZhuCeNOCreatPlayer(String startDateStr, String endDateStr,String quDao,String game);

	
	/**
	 * 获得角色的国家分布
	 * @param startDateStr开始日期
	 * @param endDateStr终止日期
	 * @return
	 */
	public List<String[]> getJueSeGuoJiaFenBu(String startDateStr, String endDateStr,String flag,String qudao,String fenQu);
	
	/**
	 * 获得有效用户数平均在线时长
	 * 有效用户定义：达到5级且在线达到15分钟以上，包括15分钟
	 * @param startDateStr开始日期
	 * @param endDateStr终止日期
	 * @return
	 */
	public Long getYouXiaoUerAVGOnLineTime(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing);
	/**
	 * 获得有效用户数 有效用户定义：达到5级且在线达到15分钟以上，包括15分钟
	 * 
	 * @param startDateStr开始日期
	 * @param endDateStr终止日期
	 * @return
	 */
	public Long getYouXiaoUerCount(String startDateStr, String endDateStr,String qudao,String fenQu,String game);
	
	/**
	 * 获得注册用户数 
	 * 
	 * @param startDateStr开始日期
	 * @param endDateStr终止日期
	 * @return
	 */
	public Long getRegistUerCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing);
	/**
	 * 按时间获取周报，日报，活月报
	 * @return
	 */
	public List<String[]> getstat_common(String dateStr);
	/**
	 * 按时间获取周报，日报，活月报
	 * @return
	 */
	public List<String[]> getstat_common(String dateStr,String orderIndex);
	
	/**
	 * 添加周报，日报，活月报
	 * @return
	 */
	public boolean addstat_common(String dateStr,String[] data);
	/**
	 * 获取注册用户的级别分布
	 * @param dateStr
	 * @return
	 */
	public List<String[]> getregestUserLevel(String regStartDateStr,String regEndDateStr,String statStartdateStr,String quDao,String fenQu);
	
}
