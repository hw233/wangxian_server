package com.sqage.stat.commonstat.dao;

import java.util.Map;

public interface LoginAndChargeCountDao 
{
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
