package com.sqage.stat.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.PlayerExpenseDAO;
import com.xuanzhi.stat.model.PlayerExpense;

public class PlayerExpenseManager {

	protected static PlayerExpenseManager m_self = null;
    
	protected static final Log log = LogFactory.getLog(PlayerExpenseManager.class);
	    
    protected PlayerExpenseDAO PlayerExpenseDAO;
        
    public static PlayerExpenseManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		m_self = this;
		System.out.println("["+PlayerExpenseManager.class.getName()+"] [initialized]");
		log.info("["+PlayerExpenseManager.class.getName()+"] [initialized]");
	}
	
	public PlayerExpense createPlayerExpense(PlayerExpense PlayerExpense) {
		PlayerExpenseDAO.save(PlayerExpense);
		return PlayerExpense;
	}

	public PlayerExpense getPlayerExpense(long id) {
		PlayerExpense PlayerExpense = PlayerExpenseDAO.findById(id);
		return PlayerExpense;
	}
	
	public int getCount() {
		return PlayerExpenseDAO.getRowNum();
	}
	
	public List getPlayerExpenses() {
		return PlayerExpenseDAO.findAll();
	}
	
	public List getPlayerExpenses(int start, int length) {
		return PlayerExpenseDAO.findPageRows(start, length);
	}
	
	public void updatePlayerExpense(PlayerExpense d) {
		PlayerExpenseDAO.attachDirty(d);
	}
	
	public void deletePlayerExpense(long id) {
		PlayerExpense d = getPlayerExpense(id);
		if(d != null) {
			PlayerExpenseDAO.delete(getPlayerExpense(id));
		}
	}
	
	/**
	 * 获得某服务器某日某货币的消费独立角色数
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public int getExpensePlayerNum(Date starttime, Date endtime, long serverid, int currencyType) {
		return PlayerExpenseDAO.getExpenseUniPlayerNum(starttime, endtime, serverid, currencyType);
	}
	
	/**
	 * 获得所有服务器某日某货币的消费独立用户数
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public int getExpenseUserNum(Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getExpenseUniUserNum(starttime, endtime, currencyType);
	}
	
	public int getExpenseUserNum(Date starttime, Date endtime, int currencyType, long channelid, long channelitemid) {
		return PlayerExpenseDAO.getExpenseUniUserNum(starttime, endtime, currencyType, channelid, channelitemid);
	}
	
	public int getExpenseUserNum(Date starttime, Date endtime, int currencyType, long channelid) {
		return PlayerExpenseDAO.getExpenseUniUserNum(starttime, endtime, currencyType, channelid);
	}
	
	/**
	 * 获得所有服务器某日某货币的新用户消费独立用户数
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public int getExpenseNewUserNum(Date regstart, Date regend , Date starttime, Date endtime, int currencyType, long channelid) {
		return PlayerExpenseDAO.getExpenseUniNewUserNum(regstart, regend, starttime, endtime, currencyType, channelid);
	}
	
	/**
	 * 获得所有服务器某日某货币的新用户消费独立用户数
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public int getExpenseNewUserNum(Date regstart, Date regend , Date starttime, Date endtime, int currencyType, long channelid, long channelitemid) {
		return PlayerExpenseDAO.getExpenseUniNewUserNum(regstart, regend, starttime, endtime, currencyType, channelid, channelitemid);
	}
	
	/**
	 * 获得所有服务器某日某货币的新用户消费独立用户数
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public int getExpenseNewUserNum(Date regstart, Date regend , Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getExpenseUniNewUserNum(regstart, regend, starttime, endtime, currencyType);
	}
	
	/**
	 * 获得某服务器某日某货币的消费独立用户数
	 * @param starttime
	 * @param endtime
	 * @param currencyType
	 * @return
	 */
	public int getExpenseUserNum(Date starttime, Date endtime, long serverid, int currencyType) {
		return PlayerExpenseDAO.getExpenseUniUserNum(starttime, endtime, serverid, currencyType);
	}
	
	/**
	 * 获得所有服务器某日某货币的消费独立用户数
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public int getExpenseNewUserNum(Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getExpenseUniNewUserNum(starttime, endtime, starttime, endtime, currencyType);
	}
	
	/**
	 * 获得某服务器某日某货币的消费独立用户数
	 * @param starttime
	 * @param endtime
	 * @param currencyType
	 * @return
	 */
	public int getExpenseNewUserNum(Date starttime, Date endtime, long serverid, int currencyType) {
		return PlayerExpenseDAO.getExpenseUniNewUserNum(starttime, endtime, serverid, currencyType);
	}
	
	/**
	 * 获得所有服务器某日某货币的消费独立用户数
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public int getExpenseOldUserNum(Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getExpenseUniOldUserNum(starttime, endtime, currencyType);
	}
	
	/**
	 * 获得某服务器某日某货币的消费独立用户数
	 * @param starttime
	 * @param endtime
	 * @param currencyType
	 * @return
	 */
	public int getExpenseOldUserNum(Date starttime, Date endtime, long serverid, int currencyType) {
		return PlayerExpenseDAO.getExpenseUniOldUserNum(starttime, endtime, serverid, currencyType);
	}
	
	/**
	 * 获得所有服务器某日某货币的消费独立用户数
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public int getNologinExpenseOldUserNum(Date logintime, Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getNologinExpenseUniOldUserNum(logintime, starttime, endtime, currencyType);
	}
	
	/**
	 * 获得某服务器某日某货币的消费独立用户数
	 * @param starttime
	 * @param endtime
	 * @param currencyType
	 * @return
	 */
	public int getNologinExpenseOldUserNum(Date logintime, Date starttime, Date endtime, long serverid, int currencyType) {
		return PlayerExpenseDAO.getNologinExpenseUniOldUserNum(logintime, starttime, endtime, serverid, currencyType);
	}
	
	/**
	 * 获得所有服务器某日某货币的消费独立用户数
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public int getNoExpenseOldUserNum(Date expensetime, Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getNoExpenseUniOldUserNum(expensetime, starttime, endtime, currencyType);
	}
	
	/**
	 * 获得某服务器某日某货币的消费独立用户数
	 * @param starttime
	 * @param endtime
	 * @param currencyType
	 * @return
	 */
	public int getNoExpenseOldUserNum(Date expensetime, Date starttime, Date endtime, long serverid, int currencyType) {
		return PlayerExpenseDAO.getNoExpenseUniOldUserNum(expensetime, starttime, endtime, serverid, currencyType);
	}
	
	/**
	 * 获得某服务器某日某货币的消费总量
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public long getExpenseAmount(Date starttime, Date endtime, long serverid, int currencyType) {
		return PlayerExpenseDAO.getExpenseAmount(starttime, endtime, serverid, currencyType);
	}
	
	/**
	 * 获得所有服务器某日某货币的消费总量
	 * @param statdate
	 * @param currencyType
	 * @return
	 */
	public long getExpenseAmount(Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getExpenseAmount(starttime, endtime, currencyType);
	}
	
	public long getExpenseAmount(Date starttime, Date endtime, int currencyType, long channelid, long channelitemid) {
		return PlayerExpenseDAO.getExpenseAmount(starttime, endtime, currencyType, channelid, channelitemid);
	}
	
	public long getExpenseAmount(Date starttime, Date endtime, int currencyType, long channelid) {
		return PlayerExpenseDAO.getExpenseAmount(starttime, endtime, currencyType, channelid);
	}
	
	public long getExpenseAmount(Date regstart, Date regend, Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getExpenseAmount(regstart, regend, starttime, endtime, currencyType);
	}
	
	public long getExpenseAmount(Date regstart, Date regend, Date starttime, Date endtime, int currencyType, long channelid, long channelitemid) {
		return PlayerExpenseDAO.getExpenseAmount(regstart, regend, starttime, endtime, currencyType, channelid, channelitemid);
	}
	
	public long getExpenseAmount(Date regstart, Date regend, Date starttime, Date endtime, int currencyType, long channelid) {
		return PlayerExpenseDAO.getExpenseAmount(regstart, regend, starttime, endtime, currencyType, channelid);
	}
	
	/**
	 * 获得某服务器某日某货币的回收总量
	 * @param statdate
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public long getRecycleAmount(Date starttime, Date endtime, long serverid, int currencyType) {
		return PlayerExpenseDAO.getRecycleAmount(starttime, endtime, serverid, currencyType);
	}
	
	/**
	 *  获得某服务器某日某货币的回收总量
	 * @param statdate
	 * @param currencyType
	 * @return
	 */
	public long getRecycleAmount(Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getRecycleAmount(starttime, endtime, currencyType);
	}
	
	public float getExpenseUserAvgLevel(Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getExpenseUserAvgLevel(starttime, endtime, currencyType);
	}
	
	public float getExpenseUserAvgLevel(Date starttime, Date endtime, long serverid, int currencyType) {
		return PlayerExpenseDAO.getExpenseUserAvgLevel(starttime, endtime, serverid, currencyType);
	}
	
	public float getFirstExpenseUserAvgLevel(Date starttime, Date endtime, int currencyType) {
		return PlayerExpenseDAO.getFirstExpenseUserAvgLevel(starttime, endtime, currencyType);
	}
	
	public float getFirstExpenseUserAvgLevel(Date starttime, Date endtime, long serverid, int currencyType) {
		return PlayerExpenseDAO.getFirstExpenseUserAvgLevel(starttime, endtime, serverid, currencyType);
	}

	public PlayerExpenseDAO getPlayerExpenseDAO() {
		return PlayerExpenseDAO;
	}

	public void setPlayerExpenseDAO(PlayerExpenseDAO PlayerExpenseDAO) {
		this.PlayerExpenseDAO = PlayerExpenseDAO;
	}

}
