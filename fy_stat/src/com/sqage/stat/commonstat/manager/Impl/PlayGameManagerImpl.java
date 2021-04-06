package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sqage.stat.commonstat.dao.Impl.PlayGameDaoImpl;
import com.sqage.stat.commonstat.dao.Impl.UserDaoImpl;
import com.sqage.stat.commonstat.entity.PlayGame;
import com.sqage.stat.commonstat.manager.PlayGameManager;

public class PlayGameManagerImpl implements PlayGameManager {

	PlayGameDaoImpl playGameDao;
	UserDaoImpl userDao;

	static PlayGameManagerImpl self;
	
	public void init(){
		self = this;
	}
	public static PlayGameManagerImpl getInstance() {
		return self;
	}
	public UserDaoImpl getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	public PlayGameDaoImpl getPlayGameDao() {
		return playGameDao;
	}

	public void setPlayGameDao(PlayGameDaoImpl playGameDao) {
		this.playGameDao = playGameDao;
	}

	@Override
	public PlayGame add(PlayGame playGame) {
		return playGameDao.add(playGame);
	}

	@Override
	public boolean deleteById(Long id) {
		return playGameDao.deleteById(id);
	}

	@Override
	public PlayGame getById(Long id) {
		return playGameDao.getById(id);
	}

	@Override
	public List<PlayGame> getBySql(String sql) {
		return playGameDao.getBySql(sql);
	}
	
	@Override
	public ArrayList<String[]> getPlayGameData(String sql, String[] strs) {
		return playGameDao.getPlayGameData(sql, strs);
	}
	@Override
	public boolean update(PlayGame playGame) {
		return playGameDao.update(playGame);
	}

	
	
	
	@Override
	public boolean merge(ArrayList<PlayGame> playGameList) {
		return playGameDao.merge(playGameList);
	}
	/**
	 *获取指定日期各个渠道的活跃人数
	 * @return
	 */
	public ArrayList<String> getQuDaoActivityUserCount(String  dateStr)
	{
		return playGameDao.getQuDaoActivityUserCount(dateStr);
	}
	/**
	 *获取指定日期各个渠道的活跃老用户人数
	 * @return
	 */
	public ArrayList<String> getQuDaoActivityOldUserCount(String dateStr,String quDao)
	{
		return playGameDao.getQuDaoActivityOldUserCount(dateStr,quDao);
	}
	/**
	 *获取指定日期各个渠道注册并进入游戏用户人数
	 * @return
	 */
	public ArrayList<String> getQuDaoReg_LoginUserCount(String dateStr)
	{
      return playGameDao.getQuDaoReg_LoginUserCount(dateStr);
	}
	
	@Override
	public HashMap<String,Integer> getQuDaoRetainUserCount_reg(String registDateStr, String statdateStr, String fenqu, String jixing) {
		return playGameDao.getQuDaoRetainUserCount_reg(registDateStr, statdateStr, fenqu, jixing);
	}
	
	/**
	 *获取指定日期各个渠道在每一天的留存人数
	 * @return
	 */
	public ArrayList<String> getFenQuRetainUserCount(String registDateStr,String statdateStr,String qudao)
	{
		if(registDateStr.compareTo(statdateStr)>0)
		{
			return null;
		}
		return playGameDao.getFenQuRetainUserCount(registDateStr, statdateStr,qudao);
	}
	
	@Override
	public ArrayList<String> getSepFenQuRetainUserCount(String registDateStr, String statdateStr, String fenqu,String quDao,String jixing) {
		return playGameDao.getSepFenQuRetainUserCount(registDateStr, statdateStr, fenqu, quDao, jixing);
	}
	public ArrayList<String> getQuDaoRetainUserCount_createplayer(String registDateStr,String statdateStr,String fenqu,String jixing)
	{
		if(registDateStr.compareTo(statdateStr)>0)
		{
			return null;
		}
		return playGameDao.getQuDaoRetainUserCount_createplayer(registDateStr, statdateStr,fenqu,jixing);
	}
	
	/**
	 *获取指定日期各个渠道在每一天的留存人数
	 * @return
	 */
	public ArrayList<String> getQuDaoRetainUserCount(String registDateStr,String statdateStr,String fenqu,String jixing)
	{
		if(registDateStr.compareTo(statdateStr)>0)
		{
			return null;
		}
		return playGameDao.getQuDaoRetainUserCount(registDateStr, statdateStr,fenqu,jixing);
	}
	/**
	 * 活跃的国家数
	 * @param statdateStr
	 * @return
	 */
	public ArrayList<String> getGuojiaActivityUserCount(String statdateStr)
	{
		return playGameDao.getGuojiaActivityUserCount(statdateStr);
	}
	
	
	public boolean addPlayGameList(ArrayList<PlayGame> playGameList) 
	{
		return playGameDao.addPlayGameList(playGameList);
	}
	
	public boolean updatePlayGameList(ArrayList<PlayGame> playGameList)
	{
		return playGameDao.updatePlayGameList(playGameList);
		
	}
	
	@Override
	public Long getAvgOnLineTime(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return playGameDao.getAvgOnLineTime(startDateStr, endDateStr, qudao, fenQu, jixing);
	}
	
	@Override
	public List<String[]> getEnterGameUserCount_paichong_day(String startDateStr, String endDateStr, String quDao, String fenqu, String jixing) {
		return playGameDao.getEnterGameUserCount_paichong_day(startDateStr, endDateStr, quDao, fenqu, jixing);
	}
	@Override
	public Long getEnterGameUserCount_paichong(String startDateStr, String endDateStr, String quDao, String fenqu, String jixing) {
		return playGameDao.getEnterGameUserCount_paichong(startDateStr, endDateStr, quDao, fenqu, jixing);
	}
	@Override
	public Long getEnterGameUserCount(String startDateStr, String endDateStr, String quDao, String fenqu, String jixing) {
		return playGameDao.getEnterGameUserCount(startDateStr, endDateStr, quDao, fenqu, jixing);
	}
	@Override
	public Long getLeftYouXIBiCount(String startDateStr, String qudao, String fenQu, String game) {
		return playGameDao.getLeftYouXIBiCount(startDateStr, qudao, fenQu, game);
	}
	@Override
	public Long getLeftYuanbaoCount(String startDateStr, String qudao, String fenQu, String game) {
		return playGameDao.getLeftYuanbaoCount(startDateStr, qudao, fenQu, game);
	}
	@Override
	public Long getReg_LoginUserCount_temp2(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return playGameDao.getReg_LoginUserCount_temp2(startDateStr,endDateStr, qudao, fenQu, jixing);
	}
	@Override
	public Long getReg_LoginUserCount(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return playGameDao.getReg_LoginUserCount(startDateStr,endDateStr, qudao, fenQu, jixing);
	}
	@Override
	public List<String[]> getZaiXianShiChangFenBu(String startDateStr, String qudao, String fenQu, String time) {
		return playGameDao.getZaiXianShiChangFenBu(startDateStr, qudao, fenQu, time);
	}
	@Override
	public List<String[]> getZaiXianShiChangFenBu_new(String startDateStr, String qudao, String fenQu, String time) {
		return playGameDao.getZaiXianShiChangFenBu_new(startDateStr, qudao, fenQu, time);
	}
	
	
	
	
	@Override
	public List<String[]> getPlayerLevelFenBu_sum(String sql) {
		return playGameDao.getPlayerLevelFenBu_sum(sql);
	}
	@Override
	public List<String[]> getPlayerLevelFenBu(String startRegStr, String endRegStr, String statDateStr, String fenQu, String sex, String nation) {
		return playGameDao.getPlayerLevelFenBu(startRegStr, endRegStr, statDateStr, fenQu, sex, nation);
	}
	
	@Override
	public List<String[]> getLostPlayerLevelFenBu(String startRegStr, String endRegStr, String fenQu, String qudao, String nation, String dayNum) {
		return playGameDao.getLostPlayerLevelFenBu(startRegStr, endRegStr, fenQu, qudao, nation, dayNum);
	}
	@Override
	public String active_pay_AVGTimes(String startDateStr, String endDateStr, String fenQu,String qudao) {
		return playGameDao.active_pay_AVGTimes(startDateStr, endDateStr, fenQu,qudao);
	}
	@Override
	public String active_pay_Money(String startDateStr, String endDateStr, String fenQu,String qudao) {
		return playGameDao.active_pay_Money(startDateStr, endDateStr, fenQu,qudao);
	}
	@Override
	public String active_pay_userCount(String startDateStr, String endDateStr, String fenQu,String qudao) {
		return playGameDao.active_pay_userCount(startDateStr, endDateStr, fenQu,qudao);
	}
	@Override
	public String active_userAVGOnLineTime(String startDateStr, String endDateStr, String fenQu,String qudao) {
		return playGameDao.active_userAVGOnLineTime(startDateStr, endDateStr, fenQu,qudao);
	}
	@Override
	public String active_userCount(String startDateStr, String endDateStr, String fenQu,String qudao) {
		return playGameDao.active_userCount(startDateStr, endDateStr, fenQu,qudao);
	}
	@Override
	public String active_userLoginTimes(String startDateStr, String endDateStr, String fenQu,String qudao) {
		return playGameDao.active_userLoginTimes(startDateStr, endDateStr, fenQu,qudao);
	}
	

	@Override
	public String active_pay_AVGTimes_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_AVGTimes_wliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_Money_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_Money_wliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_userCount_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_userCount_wliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	
	
	@Override
	public String active_userAVGOnLineTime_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userAVGOnLineTime_wliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userCount_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userCount_wliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userLoginTimes_wliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userLoginTimes_wliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_youxibi_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_youxibi_whuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_yuanbao_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_yuanbao_whuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_AVGTimes_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_AVGTimes_whuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_Money_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_Money_whuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_userCount_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_userCount_whuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_money_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_money_whuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_userCount_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_userCount_whuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userAVGOnLineTime_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userAVGOnLineTime_whuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userCount_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userCount_whuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userLoginTimes_whuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userLoginTimes_whuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_youxibi_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_youxibi_wjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_yuanbao_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_youxibi_wjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_AVGTimes_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_AVGTimes_wjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_Money_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_Money_wjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_userCount_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_userCount_wjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_money_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_money_wjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_userCount_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_userCount_wjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userAVGOnLineTime_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userAVGOnLineTime_wjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userCount_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userCount_wjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userLoginTimes_wjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userLoginTimes_wjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	
	
	//////////////////////////月活跃用户 ///////////////////////////
	@Override
	public String active_left_Myouxibi(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_Myouxibi(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_Myuanbao(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_Myuanbao(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_youxibi_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_youxibi_Mhuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_youxibi_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_youxibi_Mjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_youxibi_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_youxibi_Mliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_yuanbao_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_yuanbao_Mhuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_yuanbao_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_yuanbao_Mjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_left_yuanbao_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_left_yuanbao_Mliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_MuserAVGOnLineTime(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_MuserAVGOnLineTime(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_MuserCount(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_MuserCount(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_MuserLoginTimes(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_MuserLoginTimes(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_AVGTimes_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_AVGTimes_Mhuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_AVGTimes_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_AVGTimes_Mjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_AVGTimes_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_AVGTimes_Mliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_MAVGTimes(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_MAVGTimes(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_MMoney(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_MMoney(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_Money_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_Money_Mhuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_Money_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_Money_Mjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_Money_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_Money_Mliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_MuserCount(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_MuserCount(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_userCount_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_userCount_Mhuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_userCount_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_userCount_Mjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_pay_userCount_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_pay_userCount_Mliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_Mmoney(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_Mmoney(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_money_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_money_Mhuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_money_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_money_Mjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_money_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_money_Mliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_MuserCount(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_MuserCount(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_userCount_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_userCount_Mhuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_userCount_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_userCount_Mjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_spend_userCount_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_spend_userCount_Mliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userAVGOnLineTime_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userAVGOnLineTime_Mhuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userAVGOnLineTime_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userAVGOnLineTime_Mjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userAVGOnLineTime_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userAVGOnLineTime_Mliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userCount_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userCount_Mhuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userCount_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userCount_Mjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userCount_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userCount_Mliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userLoginTimes_Mhuiliu(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userLoginTimes_Mhuiliu(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userLoginTimes_Mjihuo(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userLoginTimes_Mjihuo(startDateStr, endDateStr, fenQu, qudao);
	}
	@Override
	public String active_userLoginTimes_Mliucun(String startDateStr, String endDateStr, String fenQu, String qudao) {
		return playGameDao.active_userLoginTimes_Mliucun(startDateStr, endDateStr, fenQu, qudao);
	}
	
	//////////////////////////月活跃用户 ///////////////////////////
	
	@Override
	public String getLiuCun(String startDate, String endDate, String startDateStat, String endDateStat, String fenQu, String qudao, String jixing) {
		return playGameDao.getLiuCun(startDate, endDate, startDateStat, endDateStat, fenQu, qudao, jixing);
	}
	@Override
	public String getMonthLoginUsercout(String startDateStr, String endDateStr, String fenQu, String qudao, String jixing) {
		return playGameDao.getMonthLoginUsercout(startDateStr, endDateStr, fenQu, qudao, jixing);
	}
}
