package com.sqage.stat.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.PlayerLogoutDAO;
import com.sqage.stat.model.PlayerLogout;

public class PlayerLogoutManager {

	protected static PlayerLogoutManager m_self = null;
    
	protected static final Log log = LogFactory.getLog(PlayerLogoutManager.class);
	    
    protected PlayerLogoutDAO PlayerLogoutDAO;
        
    public static PlayerLogoutManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		m_self = this;
		System.out.println("["+PlayerLogoutManager.class.getName()+"] [initialized]");
		log.info("["+PlayerLogoutManager.class.getName()+"] [initialized]");
	}
	
	public PlayerLogout createPlayerLogout(PlayerLogout PlayerLogout) {
		PlayerLogoutDAO.save(PlayerLogout);
		return PlayerLogout;
	}

	public PlayerLogout getPlayerLogout(long id) {
		PlayerLogout PlayerLogout = PlayerLogoutDAO.findById(id);
		return PlayerLogout;
	}
	
	public int getCount() {
		return PlayerLogoutDAO.getRowNum();
	}
	
	public List getPlayerLogouts() {
		return PlayerLogoutDAO.findAll();
	}
	
	public List getPlayerLogouts(int start, int length) {
		return PlayerLogoutDAO.findPageRows(start, length);
	}
	
	public void updatePlayerLogout(PlayerLogout d) {
		PlayerLogoutDAO.attachDirty(d);
	}
	
	public void deletePlayerLogout(long id) {
		PlayerLogout d = getPlayerLogout(id);
		if(d != null) {
			PlayerLogoutDAO.delete(getPlayerLogout(id));
		}
	}
	
	/**
	 * 得到某服务器末日的在线总时长
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public long getOnlinetime(Date statdate, long serverid) {
		return PlayerLogoutDAO.getOnlinetime(statdate, serverid);
	}
	
	/**
	 * 获得所有服务器某日的在线总时长
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public long getOnlinetime(Date statdate) {
		return PlayerLogoutDAO.getOnlinetime(statdate);
	}
	
	/**
	 * 得到某服务器末日的在线总时长
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public long getOnlinetime(Date starttime, Date endtime, long serverid) {
		return PlayerLogoutDAO.getOnlinetime(starttime, endtime, serverid);
	}
	
	/**
	 * 获得所有服务器某日的在线总时长
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public long getOnlinetime(Date starttime, Date endtime) {
		return PlayerLogoutDAO.getOnlinetime(starttime, endtime);
	}
	
	/**
	 * 得到某服务器某日新用户的在线总时长
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public long getNewPlayerOnlinetime(Date statdate, long serverid) {
		return PlayerLogoutDAO.getNewPlayerOnlinetime(statdate, serverid);
	}
	
	/**
	 * 获得某段时间内所有玩家的在线时长
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public long getPlayerOnlineTime(Date starttime, Date endtime) {
		return PlayerLogoutDAO.getPlayerOnlinetime(starttime, endtime);
	}
	
	/**
	 * 获得某段时间内所有玩家的在线时长
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public long getChannelPlayerOnlineTime(Date starttime, Date endtime, long channelid) {
		return PlayerLogoutDAO.getChannelPlayerOnlinetime(starttime, endtime, channelid);
	}
	
	/**
	 * 获得某段时间内所有玩家的在线时长
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public long getChannelPlayerOnlineTime(Date starttime, Date endtime, long channelid, long channelitemid) {
		return PlayerLogoutDAO.getChannelPlayerOnlinetime(starttime, endtime, channelid, channelitemid);
	}
	
	/**
	 * 得到所有服务器某日新用户的在线总时长
	 * @param statdate
	 * @return
	 */
	public long getNewPlayerOnlinetime(Date statdate) {
		return PlayerLogoutDAO.getNewPlayerOnlinetime(statdate);
	}
	
	public long getNewUserPlayerOnlinetime(Date starttime, Date endtime) {
		return PlayerLogoutDAO.getNewUserPlayerOnlinetime(starttime, endtime);
	}
	
	public long getNewUserPlayerOnlinetime(Date starttime, Date endtime, long serverid) {
		return PlayerLogoutDAO.getNewUserPlayerOnlinetime(starttime, endtime, serverid);
	}
	
	public HashMap<Integer, Integer> getNewUserOnlinetimeMap(Date starttime, Date endtime) {
		return PlayerLogoutDAO.getNewUserPlayerOnlinetimeMap(starttime, endtime);
	}
	
	public HashMap<Integer, Integer> getLoginUserOnlinetimeMap(Date statdate) {
		return PlayerLogoutDAO.getLoginUserOnlinetimeMap(statdate);
	}
	
	public HashMap<Integer, Long> getLoginUserLevelMap(Date statdate) {
		return PlayerLogoutDAO.getLoginUserLevelMap(statdate);
	}
	
	public HashMap<Integer, Integer> getNewUserOnlinetimeMap(Date starttime, Date endtime, long channelid) {
		return PlayerLogoutDAO.getNewUserPlayerOnlinetimeMap(starttime, endtime, channelid);
	}
	
	public HashMap<Integer, Integer> getNewUserOnlinetimeMap(Date starttime, Date endtime, long channelid, long channelitemid) {
		return PlayerLogoutDAO.getNewUserPlayerOnlinetimeMap(starttime, endtime, channelid, channelitemid);
	}

	public PlayerLogoutDAO getPlayerLogoutDAO() {
		return PlayerLogoutDAO;
	}

	public void setPlayerLogoutDAO(PlayerLogoutDAO PlayerLogoutDAO) {
		this.PlayerLogoutDAO = PlayerLogoutDAO;
	}

}
