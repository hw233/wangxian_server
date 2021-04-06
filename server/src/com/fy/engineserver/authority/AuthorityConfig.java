package com.fy.engineserver.authority;

/**
 *	玩家权利控制模块配置
	权利配置：
		type相当于id，通过type索引配置
		defaultNum为每次刷新有所有玩家都拥有的次数，譬如急行军3次，千层塔2次等等。
		maxNum为当次周期通过道具增加能达到的最大次数，譬如千层塔6次，竞技场20次，不限次数的，设置为大数即可
		vipAddNum为给vip玩家增加的次数，格式为每个vip等级对应一个数
		refreshType为刷新类型，0-每小时刷新, 1-每日刷新, 2-每周刷新, 3-每月刷新
		
 * 
 */
public class AuthorityConfig {
	
	public static final int 每小时刷新 = 0;
	public static final int 每日刷新 = 1;
	public static final int 每周刷新 = 2;
	public static final int 每月刷新 = 3;
	
	public static final int type_千层塔1道 = 0;
	public static final int type_每日境界任务 = 1;
	public static final int type_千层塔2道 = 2;
	public static final int type_千层塔3道 = 3;
	public static final int type_千层塔4道 = 4;
	public static final int type_千层塔5道 = 5;
	public static final int type_千层塔6道 = 6;
	
	public static final int type_困难千层塔1道 = 7;
	public static final int type_困难千层塔2道 = 8;
	public static final int type_困难千层塔3道 = 9;
	public static final int type_困难千层塔4道 = 10;
	public static final int type_困难千层塔5道 = 11;
	public static final int type_困难千层塔6道 = 12;
	
	public static final int type_深渊千层塔1道 = 13;
	public static final int type_深渊千层塔2道 = 14;
	public static final int type_深渊千层塔3道 = 15;
	public static final int type_深渊千层塔4道 = 16;
	public static final int type_深渊千层塔5道 = 17;
	public static final int type_深渊千层塔6道 = 18;
	
	/**
	 * 权利
	 */
	public int type;
	
	/**
	 * 刷新类型
	 */
	public int refreshType;
	
	/**
	 * 默认拥有的次数
	 */
	public int defaultNum;
	
	/**
	 * 最大次数
	 */
	public int maxNum[];
	
	/**
	 * VIP增加的次数
	 */
	public int vipAddNum[];
	
	/**
	 * 未行使的是否积攒
	 */
	public boolean accumulate;
	
	/**
	 * 积攒的最大值
	 */
	public int accumulateMax;
	
	/**
	 * 描述
	 */
	public String desp;
}
