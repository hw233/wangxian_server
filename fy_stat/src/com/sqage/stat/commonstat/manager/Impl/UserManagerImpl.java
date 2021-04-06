package com.sqage.stat.commonstat.manager.Impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sqage.stat.commonstat.dao.Impl.LoginAndChargeCountDaoImpl;
import com.sqage.stat.commonstat.dao.Impl.OnlineUserStatDaoImpl;
import com.sqage.stat.commonstat.dao.Impl.UserDaoImpl;
import com.sqage.stat.commonstat.entity.User;
import com.sqage.stat.commonstat.manager.UserManager;

public class UserManagerImpl implements UserManager {
	
	UserDaoImpl userDao;
	OnlineUserStatDaoImpl onlineUserStatDao;
	LoginAndChargeCountDaoImpl loginAndChargeCountDao;
	
	static UserManagerImpl self;
	public void init(){
		self = this;
	}
	
	

	public static UserManagerImpl getInstance() {
		return self;
	}
	
	
	@Override
	public List<User> getByUserName(String userName) {
		return userDao.getByUserName(userName);
	}



	@Override
	public ArrayList<String> getQudao(Date date) {
		return userDao.getQudao(date);
	}

	public OnlineUserStatDaoImpl getOnlineUserStatDao() {
		return onlineUserStatDao;
	}
	public void setOnlineUserStatDao(OnlineUserStatDaoImpl onlineUserStatDao) {
		this.onlineUserStatDao = onlineUserStatDao;
	}
	
	@Override
	public boolean addFenQu(String strs) {
		return userDao.addFenQu(strs);
	}

	@Override
	public boolean delFenQu(String fenquname) {
		return userDao.delFenQu(fenquname);
	}

	@Override
	public ArrayList<String []> getFenQu(Date date) {
		return userDao.getFenQu(date);
	}
	public LoginAndChargeCountDaoImpl getLoginAndChargeCountDao() {
		return loginAndChargeCountDao;
	}

	public void setLoginAndChargeCountDao(LoginAndChargeCountDaoImpl loginAndChargeCountDao) {
		this.loginAndChargeCountDao = loginAndChargeCountDao;
	}
	
	
	@Override
	public ArrayList<String[]> getFenQuByStatus(String status) {
		return userDao.getFenQuByStatus(status);
	}

	@Override
	public ArrayList<String[]> getFenQu_Group(Date date) {
		return userDao.getFenQu_Group(date);
	}

	@Override
	public ArrayList<String[]> getFenQuGroup(Date date) {
		return userDao.getFenQuGroup(date);
	}

	@Override
	public boolean addCurrencyType(String currencyType) {
		return userDao.addCurrencyType(currencyType);
	}

	@Override
	public boolean addDaoJuColor(String daoJuColor) {
		return userDao.addDaoJuColor(daoJuColor);
	}

	@Override
	public boolean addDaoJuName(String daoJuName) {
		return userDao.addDaoJuName(daoJuName);
	}

	@Override
	public boolean addGetType(String getType) {
		return userDao.addGetType(getType);
	}

	@Override
	public boolean addReasonType(String reasonType) {
		return userDao.addReasonType(reasonType);
	}

	@Override
	public ArrayList<String[]> getCurrencyType() {
		return userDao.getCurrencyType();
	}

	@Override
	public ArrayList<String[]> getDaoJuColor() {
		return userDao.getDaoJuColor();
	}

	@Override
	public ArrayList<String[]> getDaoJuName() {
		return userDao.getDaoJuName();
	}

	@Override
	public ArrayList<String[]> getGetType() {
		return userDao.getGetType();
	}

	@Override
	public ArrayList<String[]> getReasonType() {
		return userDao.getReasonType();
	}

	@Override
	public ArrayList<String[]> getZhuCeUserChongzhi_Mon(String regStartDateStr, String regEndDateStr, String statStartdateStr, String statEnddateStr,
			String quDao, String zhouqi, String jiXing) {
		return userDao.getZhuCeUserChongzhi_Mon(regStartDateStr, regEndDateStr, statStartdateStr, statEnddateStr, quDao, zhouqi, jiXing);
	}

	public UserDaoImpl getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	@Override
	public User add(User user) {
		return userDao.add(user);
	}

	@Override
	public boolean deleteById(Long id) {
		return userDao.deleteById(id);
	}

	@Override
	public User getById(Long id) {
		return userDao.getById(id);
	}

	@Override
	public List<User> getBySql(String sql) {
		return userDao.getBySql(sql);
	}

	@Override
	public boolean update(User user) {
		return userDao.update(user);
	}
	
	@Override
	public boolean updateList(ArrayList<User> userList) {
		return userDao.updateList(userList);
	}

	public boolean addUserList(ArrayList<User> userList) {
		return userDao.addUserList(userList);
	}
	
	
	
	@Override
	public List<String[]> getJueSeGuoJiaFenBu(String startDateStr, String endDateStr, String flag, String qudao, String fenQu) {
		return userDao.getJueSeGuoJiaFenBu(startDateStr, endDateStr, flag, qudao, fenQu);
	}
	/**
	 * 指定日期各个渠道注册用户数
	 */
	public ArrayList<String> getQudaoRegistUserCount(String dateStr)
	{
		return userDao.getQudaoRegistUserCount(dateStr);
	}
	@Override
	/**
	 * 获取注册但是没有创建角色的用户数
	 * @param statDateStr 开始日期
	 * @param endDateStr  终止日期
	 * @return
	 */
	public Long getZhuCeNOCreatPlayer(String startDateStr, String endDateStr, String quDao, String game) {
		return userDao.getZhuCeNOCreatPlayer(startDateStr, endDateStr, quDao, game);
	}
	
	
	@Override
	public Long getYouXiaoUerAVGOnLineTime(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return userDao.getYouXiaoUerAVGOnLineTime(startDateStr, endDateStr, qudao, fenQu, jixing);
	}
	@Override
	/**
	 * 有效用户数
	 */
	public Long getYouXiaoUerCount(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return userDao.getYouXiaoUerCount(startDateStr, endDateStr, qudao, fenQu, jixing);
	}
	@Override
	public Long getRegistUerCount(String startDateStr, String endDateStr, String qudao, String fenQu, String jixing) {
		return userDao.getRegistUerCount(startDateStr, endDateStr, qudao, fenQu, jixing);
	}
	@Override
	public List<String[]> getstat_common(String dateStr) {
		return userDao.getstat_common(dateStr);
	}
	
	@Override
	public List<String[]> getstat_common(String dateStr,String orderIndex) {
		return userDao.getstat_common(dateStr);
	}
	@Override
	public boolean addstat_common(String dateStr, String[] data) {
		return userDao.addstat_common(dateStr, data);
	}
	
	@Override
	public List<Object[]> getMaxAndAvgUserNumByHoursWholeDay(Date day,String fenqu, String qudao) {
		return onlineUserStatDao.getMaxAndAvgUserOnlineNumByHoursInWholeDay(day, fenqu, qudao);
	}
	@Override
	public List<String[]> getregestUserLevel(String regStartDateStr, String regEndDateStr, String statStartdateStr, String quDao, String fenQu) {
		return userDao.getregestUserLevel(regStartDateStr, regEndDateStr, statStartdateStr, quDao, fenQu);
	}
	
	
	

	
	
	
	
	
	@Override
	public Object[] countLoginAndRunOff(String beginDate, String endDate,
			Integer afterDay, String fenqu, String qudao) throws Exception {
		// TODO Auto-generated method stub
		return loginAndChargeCountDao.countLoginAndRunOff(beginDate, endDate, afterDay, fenqu, qudao);
	}
	@Override
	public Map<String, Integer> countRunOff(String beginDate, String endDate,
			Integer afterDay, String fenqu, String qudao) throws Exception {
		// TODO Auto-generated method stub
		return loginAndChargeCountDao.countRunOff(beginDate, endDate, afterDay, fenqu, qudao);
	}
	@Override
	public Map<Integer, Integer> countRunOffLevelSpread(String beginDate,
			String endDate, Integer afterDay, String fenqu, String qudao)
			throws Exception {
		// TODO Auto-generated method stub
		return loginAndChargeCountDao.countRunOffLevelSpread(beginDate, endDate, afterDay, fenqu, qudao);
	}
	@Override
	public Object[] countLoginAndCharge(String beginDate, String endDate,
			Integer afterDay, String fenqu, String qudao) throws Exception {
		// TODO Auto-generated method stub
		return loginAndChargeCountDao.countLoginAndCharge(beginDate, endDate, afterDay, fenqu, qudao);
	}
	@Override
	public Object[] countNotLoginAndChargeSpread(String beginDate,
			String endDate, Integer afterDay, String fenqu, String qudao)
			throws Exception {
		// TODO Auto-generated method stub
		return loginAndChargeCountDao.countNotLoginAndChargeSpread(beginDate, endDate, afterDay, fenqu, qudao);
	}
	
}
