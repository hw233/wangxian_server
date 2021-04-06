package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sqage.stat.commonstat.entity.User;

public interface UserManager {
	
	   public User getById(Long id);
		
		public List<User> getBySql(String sql);
		
		public  List<User> getByUserName(String userName);
		
		public boolean deleteById(Long id);
		
		public User add(User user);
		
		public boolean update(User user);
		
		public ArrayList<String> getQudao(Date date);
		
		public boolean delFenQu(String fenquname);
		public boolean addFenQu(String strs);
		
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

		
		public boolean updateList(ArrayList<User> userList);
		
		
		
		/**
		 * 获得角色的国家分布
		 * @param startDateStr开始日期
		 * @param endDateStr终止日期
		 * @return
		 */
		public List<String[]> getJueSeGuoJiaFenBu(String startDateStr, String endDateStr,String flag,String qudao,String fenQu);
		
		/**
		 * 获取注册但是没有创建角色的用户数
		 * @param statDateStr 开始日期
		 * @param endDateStr  终止日期
		 * @return
		 */
		Long getZhuCeNOCreatPlayer(String startDateStr, String endDateStr, String quDao, String game);
		
		public ArrayList<String> getQudaoRegistUserCount(String dateStr);
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
		public Long getYouXiaoUerCount(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing);
		
		/**
		 * 获得注册用户数 
		 * 
		 * @param startDateStr开始日期
		 * @param endDateStr终止日期
		 * @return
		 */
		public Long getRegistUerCount(String startDateStr, String endDateStr,String qudao,String fenQu,String game);
		
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
		 * 统计一天24小时每小时最高在线用户数和平均用户数
		 * added by liuyang
		 */
		public List<Object[]> getMaxAndAvgUserNumByHoursWholeDay(Date day,String fenqu,String qudao);

		
		/**
		 * 获取注册用户的级别分布
		 * @param dateStr
		 * @return
		 */
		public List<String[]> getregestUserLevel(String regStartDateStr,String regEndDateStr,String statStartdateStr,String quDao,String fenQu);
		
		
		
		
		
		/////////////////////////////////add by liuyang////////////////////////

		
		
		
		/**
		 * 统计指定时间段内的登陆用户数，和指定时间的流失人数
		 * @param beginDate 查询时间段的开始时间
		 * @param endDate   查询时间段的结束时间
		 * @param afterDay  结束时间后的天数
		 * @param fenqu     分区
		 * @param qudao     渠道
		 * added by liuyang at 2012-05-14
		 */
		public Object[] countLoginAndRunOff(String beginDate,String endDate,Integer afterDay,String fenqu, String qudao) throws Exception;
		
		/**
		 * 统计时间段内的流失用户的信息
		 * @param beginDate
		 * @param endDate
		 * @param afterDay  结束时间后的天数
		 * @param fenqu
		 * @param qudao
		 * @return Map key:用户名 value:等级
		 * added by liuyang at 2012-05-14
		 */
		public Map<String,Integer> countRunOff(String beginDate,String endDate,Integer afterDay,String fenqu, String qudao) throws Exception;
		
		
		/**
		 * 统计时间段内的流失用户的等级分布
		 * @param beginDate
		 * @param endDate
		 * @param afterDay  结束时间后的天数
		 * @param fenqu
		 * @param qudao
		 * @return Map key:等级 value:人数
		 * added by liuyang at 2012-05-14
		 */
		public Map<Integer, Integer> countRunOffLevelSpread(String beginDate, String endDate, Integer afterDay,
				String fenqu, String qudao) throws Exception; 
		
		/**
		 * 统计时间段内的登陆人数和充值人数等信息
		 * @param beginDate
		 * @param endDate
		 * @param afterDay  结束时间后的天数
		 * @param fenqu
		 * @param qudao
		 * @return Object[]
		 * 			下标:
		 * 				0  充值人数
		 * 				1  充值后连续几天未登录人数
		 * 				2 充值后连续几天未付费人数
		 * added by liuyang at 2012-05-15
		 */
		public Object[] countLoginAndCharge(String beginDate, String endDate, Integer afterDay,
				String fenqu, String qudao) throws Exception; 

		/**
		 * 统计时间段内的未登录人数的等级分布和n天未充值人数前次充值金额的人数分布
		 * @param beginDate
		 * @param endDate
		 * @param afterDay  结束时间后的天数
		 * @param fenqu
		 * @param qudao
		 * @return Object[]
		 * 			下标:
		 * 				0  Map key 等级  value 人数
		 * 				1  Map key 金额  value 人数
		 * added by liuyang at 2012-05-16
		 */
		public Object[] countNotLoginAndChargeSpread(String beginDate, String endDate, Integer afterDay,
				String fenqu, String qudao) throws Exception; 
}
