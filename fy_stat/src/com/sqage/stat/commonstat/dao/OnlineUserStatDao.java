package com.sqage.stat.commonstat.dao;

import java.util.Date;
import java.util.List;

public interface OnlineUserStatDao 
{
	/**
	 * 获得一天每小时最大在线用户数
	 * @param day 查询的日期 不能为空
	 * @param fenqu 游戏分区 可为null 表示不分区
	 * @param qudao 渠道 可为null 表示不分渠道
	 * @return List<Object[]> 返回一个包含Object数组的List，
	 * 在Object数组中 下标：
	 * 				0  为统计的时间 不能为空
	 * 				1 为一小时内的最大用户数
	 */
	public List<Object[]> getMaxUserOnlineNumByHoursInWholeDay(Date day,String fenqu,String qudao);
	
	/**
	 * 获得一天每小时平均在线用户数
	 * @param day 查询的日期 不能为空
	 * @param fenqu 游戏分区 可为null 表示不分区
	 * @param qudao 渠道 可为null 表示不分渠道
	 * @return List<Object[]> 返回一个包含Object数组的List，
	 * 在Object数组中 下标：
	 * 				0  为统计的时间 不能为空
	 * 				1 为一小时内的平均用户数

	 */
	
	public List<Object[]> getAvgUserOnlineNumByHoursInWholeDay(Date day,String fenqu,String qudao);
	
	/**
	 * 获得一天每小时最大在线用户数和平均在线用户数
	 * @param day 查询的日期 不能为空
	 * @param fenqu 游戏分区 可为null 表示不分区
	 * @param qudao 渠道 可为null 表示不分渠道
	 * @return List<Object[]> 返回一个包含Object数组的List，
	 * 在Object数组中 下标：
	 * 				0  为统计的时间 不能为空
	 * 				1为一小时内的最大用户数
	 * 				2 为一小时内的平均用户数

	 */
	
	public List<Object[]> getMaxAndAvgUserOnlineNumByHoursInWholeDay(Date day,String fenqu,String qudao);

}
