package com.sqage.stat.commonstat.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sqage.stat.commonstat.entity.PlayGame;

public interface PlayGameManager {
	
	public PlayGame getById(Long id);
	
	public List<PlayGame> getBySql(String sql);
	
	public ArrayList<String[]> getPlayGameData(String sql,String [] strs);
	
	public boolean deleteById(Long id);
	
	public PlayGame add(PlayGame playGame);
	
	public boolean update(PlayGame playGame);
	
	
	public boolean merge(ArrayList<PlayGame> playGameList);
	
	
	
	public ArrayList<String> getGuojiaActivityUserCount(String statdateStr);
	
	public boolean addPlayGameList(ArrayList<PlayGame> playGameList);

	public ArrayList<String> getQuDaoReg_LoginUserCount(String dateStr);
	
	public HashMap<String,Integer> getQuDaoRetainUserCount_reg(String registDateStr,String statdateStr,String fenqu,String jixing);
	
	
	public ArrayList<String> getQuDaoRetainUserCount_createplayer(String registDateStr,String statdateStr,String fenqu,String jixing);
	
	public ArrayList<String> getQuDaoRetainUserCount(String registDateStr,String statdateStr,String qudao,String jixing);
	/**
	 * 获取进入的用户数分天(排重)
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
   public List<String []> getEnterGameUserCount_paichong_day(String startDateStr, String endDateStr,String quDao,String fenqu,String jixing);
	
	/**
	 *获取指定日期各个渠道在每一天的留存人数
	 * @return
	 */
	public ArrayList<String> getFenQuRetainUserCount(String registDateStr,String statdateStr,String qudao);
	
	/**
	 * 指定分区的留存
	 * @param registDateStr
	 * @param statdateStr
	 * @param fenqu
	 * @return
	 */
	public ArrayList<String> getSepFenQuRetainUserCount(String registDateStr, String statdateStr, String fenqu,String quDao,String jixing);
	
	public ArrayList<String> getQuDaoActivityOldUserCount(String dateStr,String quDao);
	
	public ArrayList<String> getQuDaoActivityUserCount(String  dateStr);
	

	public boolean updatePlayGameList(ArrayList<PlayGame> playGameList);

	/**
	 * 获取进入的用户数(排重)
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	public Long getEnterGameUserCount_paichong(String startDateStr, String endDateStr,String quDao,String fenqu,String jixing);
	/**
	 * 获取进入的用户数
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @return
	 */
	public Long getEnterGameUserCount(String startDateStr, String endDateStr,String quDao,String fenqu,String jixing);
	/**
	 *dateStr天 注册并登陆的用户数
	 * @param dateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getReg_LoginUserCount_temp2(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing);
	
	/**
	 *dateStr天 注册并登陆的用户数
	 * @param dateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getReg_LoginUserCount(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing);
	/**
	 * 平均在线时间
	 * @param dateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getAvgOnLineTime(String startDateStr, String endDateStr,String qudao,String fenQu,String jixing);
	/**
	 * 剩余元宝数量
	 * @param startDateStr
	 * @param endDateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getLeftYuanbaoCount(String startDateStr,String qudao,String fenQu,String game);
	/**
	 * 剩余游戏币数量
	 * @param startDateStr
	 * @param qudao
	 * @param fenQu
	 * @param game
	 * @return
	 */
	public Long getLeftYouXIBiCount(String startDateStr,String qudao,String fenQu,String game);
	
	/**
	 * 按在线时长分布
	 * @param startDateStr
	 * @param qudao
	 * @param fenQu
	 * @param time 统计的时间粒度，单位分钟，默认30分钟
	 * @return
	 */
	
	public List<String[]> getZaiXianShiChangFenBu(String startDateStr,String qudao,String fenQu,String time);
	/**
	 * 新用户按在线时长分布
	 * @param startDateStr
	 * @param qudao
	 * @param fenQu
	 * @param time 统计的时间粒度，单位分钟，默认30分钟
	 * @return
	 */
	public List<String[]> getZaiXianShiChangFenBu_new(String startDateStr, String qudao, String fenQu,String time) ;
	
	/**
	 * 用户级别分布三级汇总
	 * @param startRegStr
	 * @param endRegStr
	 * @return
	 */
	public List<String[]> getPlayerLevelFenBu_sum(String sql) ;
	
	
	/**
	 * 用户级别分布
	 * @param startRegStr
	 * @param endRegStr
	 * @param statDateStr
	 * @param qudao
	 * @param fenQu
	 * @param time
	 * @return
	 */
	public List<String[]> getPlayerLevelFenBu(String startRegStr,String endRegStr,String statDateStr, String fenQu,String sex,String nation) ;
	
	/**
	 * 流失角色级别分布
	 * @param startRegStr
	 * @param endRegStr
	 * @param fenQu
	 * @param qudao
	 * @param nation
	 * @param dayNum  联系未登录天数
	 * @return
	 */
	public List<String[]> getLostPlayerLevelFenBu(String startRegStr,String endRegStr, String fenQu,String qudao,String nation,String dayNum) ;
	
	
	//以下是周活跃用户相关统计
	//周活跃用户定义：等级大于5级，1周内独立登录3次或以上，或累计在线时长达到6小时或以上
	
	/**
	 * 活跃用户数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userCount(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 活跃用户登录次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userLoginTimes(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 或用用户平均在线时长
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userAVGOnLineTime(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 独立付费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_userCount(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 付费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_Money(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 平均付费次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_AVGTimes(String startDateStr,String endDateStr,String fenQu,String qudao);
	
	
/////周留存用户信息统计 start/////////////////////////////////

	/**
	 * 周留存活跃用户数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userCount_wliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 周留存活跃用户登录次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userLoginTimes_wliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 周留存或用用户平均在线时长
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userAVGOnLineTime_wliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 周留存独立付费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_userCount_wliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *周留存 付费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_Money_wliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *周留存 平均付费次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_AVGTimes_wliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	
	/////周留存用户信息统计 start/////////////////////////////////
	
	
///////////////周回流活跃用户信息统计 start/////////////////////////////////
	///////////////周回流活跃用户定义：W0活跃，W1不活跃，W2活跃，则视为W2的回流活跃用户

	/**
	 * 周回流活跃用户数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userCount_whuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 周回流活跃用户登录次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userLoginTimes_whuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 周回流活跃用户平均在线时长
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userAVGOnLineTime_whuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 周回流活跃用户独立付费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_userCount_whuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *周回流活跃用户 付费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_Money_whuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *周回流活跃用户 平均付费次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_AVGTimes_whuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 周回流活跃用户独立消费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_userCount_whuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *周回流活跃用户消费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_money_whuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *周回流活跃用户 元宝库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_yuanbao_whuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *周回流活跃用户游戏币库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_youxibi_whuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	
	 ///////////////周回流活跃用户信息统计 end/////////////////////////////////
	
	
	
///////////////本周激活用户信息统计 start/////////////////////////////////
	///////////////本周激活用户定义：之前判定为未激活，在本周内激活的用户数////////////

	/**
	 * 本周周激活用户数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userCount_wjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 本周周激活用户登录次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userLoginTimes_wjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 本周周激活用户平均在线时长
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userAVGOnLineTime_wjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 本周周激活用户独立付费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_userCount_wjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *本周周激活用户 付费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_Money_wjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *本周周激活用户平均付费次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_AVGTimes_wjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 本周周激活用户独立消费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_userCount_wjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *本周周激活用户 消费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_money_wjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *本周周激活用户 元宝库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_yuanbao_wjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *本周周激活用户 游戏币库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_youxibi_wjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	
	 ///////////////本周激活用户信息统计 end/////////////////////////////////
	
	

	//以下是月活跃用户相关统计
	//月活跃用户定义： 1月内独立登录12次或以上，或累计在线时长达到24小时或以上
	
	/**
	 * 月活跃用户数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_MuserCount(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月活跃用户登录次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_MuserLoginTimes(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月活跃用户平均在线时长
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_MuserAVGOnLineTime(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月活跃独立付费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_MuserCount(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月活跃付费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_MMoney(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月活跃平均付费次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_MAVGTimes(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月活跃独立消费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_MuserCount(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月活跃消费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_Mmoney(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 元宝库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_Myuanbao(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月活跃游戏币库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_Myouxibi(String startDateStr,String endDateStr,String fenQu,String qudao);
	
	///////////////月留存用户信息统计 start/////////////////////////////////
	///////////////月留存定义：上月达到留存标准，这月仍然达到活跃标准的用户

	/**
	 * 月留存活跃用户数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userCount_Mliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月留存活跃用户登录次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userLoginTimes_Mliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月留存或用用户平均在线时长
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userAVGOnLineTime_Mliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月留存独立付费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_userCount_Mliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *月留存 付费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_Money_Mliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *月留存 平均付费次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_AVGTimes_Mliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月留存独立消费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_userCount_Mliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *月留存 消费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_money_Mliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *月留存 元宝库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_yuanbao_Mliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *月留存 游戏币库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_youxibi_Mliucun(String startDateStr,String endDateStr,String fenQu,String qudao);
	
	/////月留存用户信息统计end/////////////////////////////////
	
     ///////////////月回流活跃用户信息统计 start/////////////////////////////////
	///////////////月回流活跃用户定义：W0活跃，W1不活跃，W2活跃，则视为W2的回流活跃用户

	/**
	 * 月回流活跃用户数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userCount_Mhuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月回流活跃用户登录次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userLoginTimes_Mhuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月回流用户平均在线时长
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userAVGOnLineTime_Mhuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月回流独立付费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_userCount_Mhuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *月回流 付费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_Money_Mhuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *月回流平均付费次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_AVGTimes_Mhuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 月回流独立消费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_userCount_Mhuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *月回流 消费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_money_Mhuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *月回流 元宝库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_yuanbao_Mhuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *月回流 游戏币库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_youxibi_Mhuiliu(String startDateStr,String endDateStr,String fenQu,String qudao);
	
	 ///////////////月回流活跃用户信息统计 end/////////////////////////////////
	
	///////////////本月激活用户信息统计 start/////////////////////////////////
	///////////////本月激活用户定义：之前判定为未激活，在本月内激活的用户数////////////

	/**
	 * 本月月激活用户数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userCount_Mjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 本月月激活用户登录次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userLoginTimes_Mjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 本月月激活用户平均在线时长
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_userAVGOnLineTime_Mjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 本月月激活用户独立付费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_userCount_Mjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *本月月激活用户 付费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_Money_Mjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *本月月激活用户平均付费次数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_pay_AVGTimes_Mjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 * 本月月激活用户独立消费人数
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_userCount_Mjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *本月月激活用户 消费金额
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_spend_money_Mjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *本月月激活用户 元宝库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_yuanbao_Mjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	/**
	 *本月月激活用户 游戏币库存
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String active_left_youxibi_Mjihuo(String startDateStr,String endDateStr,String fenQu,String qudao);
	
	 ///////////////本月激活用户信息统计 end/////////////////////////////////	
	
	
	 /**
     * 在startDate 到endDate登录过的用户，在startDateStat到endDateStat有登录行为的用户数
     */
	public  String getLiuCun(String startDate,String endDate ,String startDateStat,String endDateStat,String fenQu,String qudao,String jixing);
	
	
	
	/**
	 * 获得指定时间内登录过的用户的数量
	 * @param startDateStr
	 * @param endDateStr
	 * @param fenQu
	 * @return
	 */
	public String getMonthLoginUsercout(String startDateStr, String endDateStr, String fenQu,String qudao,String jixing);
}
