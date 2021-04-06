package com.sqage.stat.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.PlayerLoginDAO;
import com.sqage.stat.model.PlayerLogin;

public class PlayerLoginManager {

	protected static PlayerLoginManager m_self = null;
    
	protected static final Log log = LogFactory.getLog(PlayerLoginManager.class);
	    
    protected PlayerLoginDAO PlayerLoginDAO;
        
    public static PlayerLoginManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		m_self = this;
		System.out.println("["+PlayerLoginManager.class.getName()+"] [initialized]");
		log.info("["+PlayerLoginManager.class.getName()+"] [initialized]");
	}
	
	public PlayerLogin createPlayerLogin(PlayerLogin PlayerLogin) {
		PlayerLoginDAO.save(PlayerLogin);
		return PlayerLogin;
	}

	public PlayerLogin getPlayerLogin(long id) {
		PlayerLogin PlayerLogin = PlayerLoginDAO.findById(id);
		return PlayerLogin;
	}
	
	public int getCount() {
		return PlayerLoginDAO.getRowNum();
	}
	
	public List getPlayerLogins() {
		return PlayerLoginDAO.findAll();
	}
	
	public List getPlayerLogins(int start, int length) {
		return PlayerLoginDAO.findPageRows(start, length);
	}
	
	public void updatePlayerLogin(PlayerLogin d) {
		PlayerLoginDAO.attachDirty(d);
	}
	
	public void deletePlayerLogin(long id) {
		PlayerLogin d = getPlayerLogin(id);
		if(d != null) {
			PlayerLoginDAO.delete(getPlayerLogin(id));
		}
	}
	
	/**
	 * 得到某服务器在某日登陆的角色数
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public int getLoginPlayerNum(Date starttime, Date endtime, long serverid) {
		return PlayerLoginDAO.getUniLoginPlayerNum(starttime, endtime, serverid);
	}
	
	/**
	 * 得到所有服务器在某日的登陆角色数
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public int getLoginPlayerNum(Date starttime, Date endtime) {
		return PlayerLoginDAO.getUniLoginPlayerNum(starttime, endtime);
	}
	
	/**
	 * 得到某服务器某日的登陆次数
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public int getLoginNum(Date starttime, Date endtime, long serverid) {
		return PlayerLoginDAO.getLoginNum(starttime, endtime, serverid);
	}
	
	/**
	 * 得到所有服务器在某日的登陆次数
	 * @param statdate
	 * @return
	 */
	public int getLoginNum(Date starttime, Date endtime) {
		return PlayerLoginDAO.getLoginNum(starttime, endtime);
	}
	
	/**
	 * 得到某服务器某日的登陆次数
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public int getLoginUserNum(Date starttime, Date endtime, long serverid) {
		return PlayerLoginDAO.getLoginUserNum(starttime, endtime, serverid);
	}
	
	public int getChannelLoginUserNum(Date starttime, Date endtime, long channelid) {
		return PlayerLoginDAO.getChannelLoginUserNum(starttime, endtime, channelid);
	}
	
	public int getChannelLoginUserNum(Date starttime, Date endtime, long channelid, long channelitemid) {
		return PlayerLoginDAO.getChannelLoginUserNum(starttime, endtime, channelid, channelitemid);
	}
	
	/**
	 * 得到所有服务器在某日的登陆次数
	 * @param statdate
	 * @return
	 */
	public int getLoginUserNum(Date starttime, Date endtime) {
		return PlayerLoginDAO.getLoginUserNum(starttime, endtime);
	}
	
	/**
	 * 得到某服务器某日新角色的登陆次数
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public int getNewPlayerLoginNum(Date starttime, Date endtime, long serverid) {
		return PlayerLoginDAO.getNewPlayerLoginNum(starttime, endtime, serverid);
	}
	
	/**
	 * 得到所有服务器某日心交社的登陆次数
	 * @param statdate
	 * @return
	 */
	public int getNewPlayerLoginNum(Date starttime, Date endtime) {
		return PlayerLoginDAO.getNewPlayerLoginNum(starttime, endtime);
	}
	
	/**
	 * 得到某服务器某日新角色的登陆次数
	 * @param statdate
	 * @param serverid
	 * @return
	 */
	public int getLoginNewPlayerNum(Date starttime, Date endtime, long serverid) {
		return PlayerLoginDAO.getLoginNewPlayerNum(starttime, endtime, serverid);
	}
	
	/**
	 * 得到所有服务器某日心交社的登陆次数
	 * @param statdate
	 * @return
	 */
	public int getLoginNewPlayerNum(Date starttime, Date endtime) {
		return PlayerLoginDAO.getLoginNewPlayerNum(starttime, endtime);
	}
	
	/**
	 * 得到新用户活跃数
	 * @param statdate
	 * @return
	 */
	public int getNewUserLoginNum(Date regstart, Date regend, Date loginstart, Date loginend) {
		return PlayerLoginDAO.getNewUserLoginNum(regstart, regend, loginstart, loginend);
	}
	
	public int getOldUserLoginNum(int levelstart, int levelend, Date loginstart, Date loginend, Date statstart, Date statend) {
		return PlayerLoginDAO.getOldUserLoginNum(levelstart, levelend, loginstart, loginend, statstart, statend);
	}
	
	public int getOldUserLoginNum(int levelstart, int levelend, Date loginstart, Date loginend) {
		return PlayerLoginDAO.getOldUserLoginNum(levelstart, levelend, loginstart, loginend);
	}
	
	/**
	 * 分渠道得到新用户活跃数
	 * @param statdate
	 * @return
	 */
	public int getNewUserLoginNumByChannel(long channelid, long channelitemid, Date regstart, Date regend, Date loginstart, Date loginend) {
		return PlayerLoginDAO.getNewUserLoginNumByChannel(channelid, channelitemid, regstart, regend, loginstart, loginend);
	}
	
	/**
	 * 分渠道得到新用户活跃数
	 * @param statdate
	 * @return
	 */
	public int getNewUserLoginNumByChannel(long channelid, Date regstart, Date regend, Date loginstart, Date loginend) {
		return PlayerLoginDAO.getNewUserLoginNumByChannel(channelid, -1, regstart, regend, loginstart, loginend);
	}
	
	public HashMap<Integer, Long> getLoginUserLevelMap(Date statdate) {
		return PlayerLoginDAO.getLoginUserLevelMap(statdate);
	}


	public PlayerLoginDAO getPlayerLoginDAO() {
		return PlayerLoginDAO;
	}

	public void setPlayerLoginDAO(PlayerLoginDAO PlayerLoginDAO) {
		this.PlayerLoginDAO = PlayerLoginDAO;
	}

}
