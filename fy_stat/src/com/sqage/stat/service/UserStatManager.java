package com.sqage.stat.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.UserStatDAO;
import com.sqage.stat.model.UserStat;
import com.xuanzhi.stat.service.Area;
import com.xuanzhi.tools.cache.LruMapCache;

public class UserStatManager {

	protected static UserStatManager m_self = null;
    
	protected static final Log logger = LogFactory.getLog(UserStatManager.class);
	
	protected static LruMapCache mCache;
    
    protected UserStatDAO userStatDAO;
        
    public static UserStatManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
    	mCache = new LruMapCache();
		m_self = this;
		System.out.println("["+UserStatManager.class.getName()+"] [initialized]");
		logger.info("["+UserStatManager.class.getName()+"] [initialized]");
	}
	
	public UserStat createUserStat(UserStat userStat) {
		boolean result=userStatDAO.save(userStat);
		if(result){
		mCache.__put(userStat.getId(), userStat);
		mCache.__put(userStat.getUsername(), userStat);
		}else{
			userStat=null;
		}
		logger.info("[创建UserStat] ["+userStat.getUsername()+"] ["+userStat.getChannel()+"] ["+userStat.getChannelitem()+"]");
		return userStat;
	}

	public UserStat getUserStat(long id) {
		if(mCache.__get(id) != null) {
			return (UserStat)mCache.__get(id);
		}
		UserStat userStat = userStatDAO.findById(id);
		if(userStat != null) {
			mCache.__put(userStat.getId(), userStat);
			mCache.__put(userStat.getUsername(), userStat);
		}
		return userStat;
	}

	public UserStat getUserStat(String username) {
		if(mCache.__get(username) != null) {
			return (UserStat)mCache.__get(username);
		}
		List<UserStat> list = userStatDAO.findByUsername(username);
		if(list.size() > 0) {
			UserStat userStat = list.get(0);
			mCache.__put(userStat.getId(), userStat);
			mCache.__put(userStat.getUsername(), userStat);
			return userStat;
		}
		return null;
	}
	
	public int getCount() {
		return userStatDAO.getRowNum();
	}
	
	/**
	 * 获得某注册类型的用户数
	 * @param type
	 * @param start
	 * @param end
	 * @param channelid
	 * @param channelitemid
	 * @return
	 */
	public int getRegisterTypeUser(int type, Date start, Date end, long channelid, long channelitemid) {
		long l = System.currentTimeMillis();
		int result = userStatDAO.getRowNum(type, start, end, channelid, channelitemid);
		if(logger.isInfoEnabled()) {
			logger.info("[getRegisterTypeUser1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获得某注册类型的用户
	 * @param type
	 * @param start
	 * @param end
	 * @param channelid
	 * @return
	 */
	public int getRegisterTypeUser(int type, Date start, Date end, long channelid) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getRowNum(type, start, end, channelid);
		if(logger.isInfoEnabled()) {
			logger.info("[getRegisterTypeUser2] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获得某注册类型的用户
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	public int getRegisterTypeUser(int type, Date start, Date end) {
		long l = System.currentTimeMillis();
		int result = userStatDAO.getRowNum(type, start, end);
		if(logger.isInfoEnabled()) {
			logger.info("[getRegisterTypeUser3] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获得白名单类型的用户数
	 * @param type
	 * @param start
	 * @param end
	 * @param channelid
	 * @param channelitemid
	 * @return
	 */
	public int getRegisterWhiteUser(Date start, Date end, long channelid, long channelitemid) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getWhiteUserRowNum(start, end, channelid, channelitemid);
		if(logger.isInfoEnabled()) {
			logger.info("[getRegisterTypeUser4] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获得白名单的用户
	 * @param type
	 * @param start
	 * @param end
	 * @param channelid
	 * @return
	 */
	public int getRegisterWhiteUser(Date start, Date end, long channelid) {
		long l = System.currentTimeMillis();
		int result = userStatDAO.getWhiteUserRowNum(start, end, channelid);
		if(logger.isInfoEnabled()) {
			logger.info("[getRegisterTypeUser5] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获得白名单类型的用户
	 * @param type
	 * @param start
	 * @param end
	 * @return
	 */
	public int getRegisterWhiteUser(Date start, Date end) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getWhiteUserRowNum(start, end);
		if(logger.isInfoEnabled()) {
			logger.info("[getRegisterTypeUser6] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获取多少天没有登录的用户
	 * @param day
	 * @return
	 */
	public int getNologinCount(Date statdate, int day) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getNologinRowNum(statdate, day);
		if(logger.isInfoEnabled()) {
			logger.info("[getNologinCount1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getNologinNewUserCount(Date statdate, int day) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getNologinNewUserRowNum(statdate, day);
		if(logger.isInfoEnabled()) {
			logger.info("[getNologinNewUserCount1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getActiveOldUserNum(Date loginstarttime, Date loginendtime, Date befregtime) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getActiveOldUserNum(loginstarttime, loginendtime, befregtime);
		if(logger.isInfoEnabled()) {
			logger.info("[getActiveOldUserNum1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getLoginUserNum(Date starttime, Date endtime) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getLoginUserNum(starttime, endtime);
		if(logger.isInfoEnabled()) {
			logger.info("[getLoginUserNum1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getChannelLoginUserNum(Date starttime, Date endtime, long channelid) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getChannelLoginUserNum(starttime, endtime, channelid);
		if(logger.isInfoEnabled()) {
			logger.info("[getChannelLoginUserNum1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getChannelLoginUserNum(Date starttime, Date endtime, long channelid, long channelitemid) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getChannelLoginUserNum(starttime, endtime, channelid, channelitemid);
		if(logger.isInfoEnabled()) {
			logger.info("[getChannelLoginUserNum2] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getUserNum() {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getUserNum();
		if(logger.isInfoEnabled()) {
			logger.info("[getUserNum1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getChannelUserNum(long channelid) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getChannelUserNum(channelid);
		if(logger.isInfoEnabled()) {
			logger.info("[getChannelUserNum1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getChannelUserNum(long channelid, long channelitemid) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getChannelUserNum(channelid, channelitemid);
		if(logger.isInfoEnabled()) {
			logger.info("[getChannelUserNum2] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获得未登录的新用户
	 * @param regstart
	 * @param regend
	 * @param lastlogintime
	 * @return
	 */
	public List getNologinNewUsers(Date regstart, Date regend, Date lastlogintime) {
		long l = System.currentTimeMillis();
		List result =  userStatDAO.findNologinNewUsers(regstart, regend, lastlogintime);
		if(logger.isInfoEnabled()) {
			logger.info("[getNologinNewUsers1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获得未登录的新用户
	 * @param regstart
	 * @param regend
	 * @param lastlogintime
	 * @return
	 */
	public int getNologinNewUserNum(Date regstart, Date regend, Date lastlogintime) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.findNologinNewUserNum(regstart, regend, lastlogintime);
		if(logger.isInfoEnabled()) {
			logger.info("[getNologinNewUserNum2] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getNologinActiveOldUserNum(Date regtime, Date lastloginstart, Date lastloginend) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getNologinActiveOldUserNum(regtime, lastloginstart, lastloginend);
		if(logger.isInfoEnabled()) {
			logger.info("[getNologinActiveOldUserNum1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获得总充值数
	 * @return
	 */
	public long getSavingYuanbaoTotal() {
		long l = System.currentTimeMillis();
		long result =  userStatDAO.getSavingYuanbaoTotal();
		if(logger.isInfoEnabled()) {
			logger.info("[getSavingYuanbaoTotal1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public long getSavingRmbTotal() {
		long l = System.currentTimeMillis();
		long result =  userStatDAO.getSavingRmbTotal();
		if(logger.isInfoEnabled()) {
			logger.info("[getSavingRmbTotal1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public List getUserStats() {
		long l = System.currentTimeMillis();
		List result =  userStatDAO.findAll();
		if(logger.isInfoEnabled()) {
			logger.info("[getUserStats1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public List getUserStats(int start, int length) {
		long l = System.currentTimeMillis();
		List result =  userStatDAO.findPageRows(start, length);
		if(logger.isInfoEnabled()) {
			logger.info("[getUserStats1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public List getNewUsers(Date start, Date end) {
		long l = System.currentTimeMillis();
		List result =  userStatDAO.findNewUserStat(start, end);
		if(logger.isInfoEnabled()) {
			logger.info("[getNewUsers1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getNewUserNum(Date start, Date end) {
		long l = System.currentTimeMillis();
		int result = userStatDAO.getRowNum(start, end);
		if(logger.isInfoEnabled()) {
			logger.info("[getNewUserNum2] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getWhiteUserMobileRowNum(Date start, Date end) {
		long l = System.currentTimeMillis();
		int result = userStatDAO.getWhiteUserMobileRowNum(start, end);
		if(logger.isInfoEnabled()) {
			logger.info("[getNewMobileNum] ["+result+"] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getNewUserNum(Date start, Date end, long channelid, long channelitemid) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getRowNum(start, end, channelid, channelitemid);
		if(logger.isInfoEnabled()) {
			logger.info("[getNewUserNum3] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public int getNewUserNum(Date start, Date end, long channelid) {
		long l = System.currentTimeMillis();
		int result =  userStatDAO.getRowNum(start, end, channelid);
		if(logger.isInfoEnabled()) {
			logger.info("[getNewUserNum4] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获取多少天前新用户
	 * @param day
	 * @return
	 */
	public int getNewUser(Date statdate, int day) {
		long l = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTime(statdate);
		cal.add(Calendar.DAY_OF_YEAR, 0-day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date starttime = cal.getTime();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		Date endtime = cal.getTime();
		int result =  userStatDAO.getRowNum(starttime, endtime);
		if(logger.isInfoEnabled()) {
			logger.info("[getNewUser1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public HashMap<Long, Integer> statRegisterArea(Date starttime, Date endtime) {
		long l = System.currentTimeMillis();
		HashMap result =  userStatDAO.statRegisterArea(starttime, endtime);
		if(logger.isInfoEnabled()) {
			logger.info("[statRegisterArea1] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public HashMap<Long, Integer> statRegisterArea(Date starttime, Date endtime, long channelid) {
		long l = System.currentTimeMillis();
		HashMap result =  userStatDAO.statRegisterArea(starttime, endtime, channelid);
		if(logger.isInfoEnabled()) {
			logger.info("[statRegisterArea2] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public HashMap<Long, Integer> statRegisterArea(Date starttime, Date endtime, long channelid, long channelitemid) {
		long l = System.currentTimeMillis();
		HashMap result =  userStatDAO.statRegisterArea(starttime, endtime, channelid, channelitemid);
		if(logger.isInfoEnabled()) {
			logger.info("[statRegisterArea3] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public HashMap<Area, Integer> statActiveUserArea(Date starttime, Date endtime) {
		long l = System.currentTimeMillis();
		HashMap result =  userStatDAO.statActiveUserArea(starttime, endtime);
		if(logger.isInfoEnabled()) {
			logger.info("[statActiveUserArea] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public HashMap<Long, Integer> statAllUserArea() {
		long l = System.currentTimeMillis();
		HashMap result =  userStatDAO.statAllUserArea();
		if(logger.isInfoEnabled()) {
			logger.info("[statAllUserArea] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	public HashMap<String, Integer> statActiveUserUserAgent(Date starttime, Date endtime) {
		long l = System.currentTimeMillis();
		HashMap result =  userStatDAO.statActiveUserMobiletype(starttime, endtime);
		if(logger.isInfoEnabled()) {
			logger.info("[statActiveUserArea] ["+(System.currentTimeMillis()-l)+"]");
		}
		return result;
	}
	
	/**
	 * 获得多少天未登录的用户等级分布
	 * @param nologindays
	 * @return
	 */
	public HashMap<Integer, Long> getNewUserMaxLevelPlayerLevelMap(Date start, Date end) {
		return userStatDAO.getNewUserMaxLevelPlayerLevelMap(start, end);
	}
	
	public HashMap<Integer, Long> getLoginUserLevelMap(Date statdate) {
		return userStatDAO.getLoginUserLevelMap(statdate);
	}
	
	public HashMap<Integer,Long> getNoLoginUserMaxLevelMap(int days) {
		return userStatDAO.getNoLoginUserMaxLevelMap(days);
	}
	
	public int getChannelUniqMobileUserNum(long channelid, Date starttime, Date endtime, int minLevel) {
		return userStatDAO.getChannelUniqMobileUserNum(channelid, starttime, endtime, minLevel);
	}
	
	public int getChannelUniqMobileNormalUserNum(long channelid, Date starttime, Date endtime, int minLevel) {
		return userStatDAO.getChannelUniqMobileNormalUserNum(channelid, starttime, endtime, minLevel);
	}
	
	public int getChannelUniqMobileUserNum(long channelid, long channelitemid, Date starttime, Date endtime, int minLevel) {
		return userStatDAO.getChannelUniqMobileUserNum(channelid, channelitemid, starttime, endtime, minLevel);
	}
	
	public int getChannelUniqMobileNormalUserNum(long channelid, long channelitemid, Date starttime, Date endtime, int minLevel) {
		return userStatDAO.getChannelUniqMobileNormalUserNum(channelid, channelitemid, starttime, endtime, minLevel);
	}
	
	public void updateUserStat(UserStat d) {
		userStatDAO.attachDirty(d);
		mCache.remove(d.getId());
		mCache.remove(d.getUsername());
		logger.info("[更新UserStat] ["+d.getUsername()+"] ["+d.getChannel()+"] ["+d.getChannelitem()+"]");
	}
	 
	public void deleteUserStat(long id) {
		UserStat d = getUserStat(id);
		if(d != null) {
			userStatDAO.delete(getUserStat(id));
			mCache.remove(d.getId());
			mCache.remove(d.getUsername());
		}
	}

	public UserStatDAO getUserStatDAO() {
		return userStatDAO;
	}

	public void setUserStatDAO(UserStatDAO userStatDAO) {
		this.userStatDAO = userStatDAO;
	}

}
