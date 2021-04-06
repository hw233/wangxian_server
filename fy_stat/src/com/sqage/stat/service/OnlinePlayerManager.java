package com.sqage.stat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.OnlinePlayerDAO;
import com.sqage.stat.model.OnlinePlayer;
import com.xuanzhi.tools.queue.DefaultQueue;

public class OnlinePlayerManager implements Runnable  {

	protected static OnlinePlayerManager m_self = null;
    
	protected static final Log logger = LogFactory.getLog(OnlinePlayerManager.class);
	    
    protected OnlinePlayerDAO onlinePlayerDAO;
    
    private DefaultQueue queue;
        
    public static OnlinePlayerManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		queue = new DefaultQueue(2000000);
		m_self = this;
		Thread t = new Thread(this, "OnlinePlayerManager");
		t.start();
		System.out.println("["+OnlinePlayerManager.class.getName()+"] [initialized]");
		logger.info("["+OnlinePlayerManager.class.getName()+"] [initialized]");
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				long now = System.currentTimeMillis();
				ArrayList<OnlinePlayer> pslist = new ArrayList<OnlinePlayer>();
				while(!queue.isEmpty()) {
					OnlinePlayer ps = (OnlinePlayer)queue.pop();
					if(ps == null) {
						break;
					} else {
						pslist.add(ps);
					}
					if(pslist.size() >= 100) {
						break;
					}
				}
				if(pslist.size() > 0) {
					onlinePlayerDAO.batchAdd(pslist);
					logger.info("[批量创建] ["+pslist.size()+"] ["+(System.currentTimeMillis()-now)+"ms]");
				} else {
					Thread.sleep(1000);
				}
			} catch(Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
	}

	public void addNewOnlinePlayer(OnlinePlayer op) {
		queue.push(op);
	}
	
	public OnlinePlayer createOnlinePlayer(OnlinePlayer onlinePlayer) {
		onlinePlayerDAO.save(onlinePlayer);
		return onlinePlayer;
	}

	public OnlinePlayer getOnlinePlayer(long id) {
		OnlinePlayer onlinePlayer = onlinePlayerDAO.findById(id);
		return onlinePlayer;
	}
	
	public int getCount() {
		return onlinePlayerDAO.getRowNum();
	}
	
	public List getOnlinePlayers() {
		return onlinePlayerDAO.findAll();
	}
	
	public List getOnlinePlayers(int start, int length) {
		return onlinePlayerDAO.findPageRows(start, length);
	}
	
	public void updateOnlinePlayer(OnlinePlayer d) {
		onlinePlayerDAO.attachDirty(d);
	}
	 
	public void deleteOnlinePlayer(long id) {
		OnlinePlayer d = getOnlinePlayer(id);
		if(d != null) {
			onlinePlayerDAO.delete(getOnlinePlayer(id));
		}
	}
	
	/**
	 * 得到最高在线
	 * @return
	 */
	public int getMaxOnlineNum() {
		return onlinePlayerDAO.getMaxOnlineNum();
	}
	
	/**
	 * 得到一段时间以内的最高在线
	 * @param startttime
	 * @param endtime
	 * @return
	 */
	public int getMaxOnlineNum(Date starttime, Date endtime) {
		return onlinePlayerDAO.getMaxOnlineNum(starttime, endtime);
	}
	
	/**
	 * 得到某服务器最高在线
	 * @return
	 */
	public int getMaxOnlineNum(long serverid) {
		return onlinePlayerDAO.getMaxOnlineNum(serverid);
	}
	
	/**
	 * 得到某服务器某段时间的最高在线
	 * @param starttime
	 * @param endtime
	 * @param serverid
	 * @return
	 */
	public int getMaxOnlineNum(Date starttime, Date endtime, long serverid) {
		return onlinePlayerDAO.getMaxOnlineNum(starttime, endtime, serverid);
	}
	
	/**
	 * 获得平均在线
	 * @return
	 */
	public float getAvgOnlineNum() {
		return onlinePlayerDAO.getAvgOnlineNum();
	}	
	
	/**
	 * 获得平均在线
	 * @return
	 */
	public float getChannelAvgOnlineNum(long channelid, long channelitemid) {
		return onlinePlayerDAO.getChannelAvgOnlineNum(channelid, channelitemid);
	}
	
	/**
	 * 获得平均在线
	 * @return
	 */
	public float getChannelAvgOnlineNum(long channelid) {
		return onlinePlayerDAO.getChannelAvgOnlineNum(channelid);
	}
	
	/**
	 * 获得某段时间内平均在线
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public float getAvgOnlineNum(Date starttime, Date endtime) {
		return onlinePlayerDAO.getAvgOnlineNum(starttime, endtime);
	}	
	
	/**
	 * 获得某段时间内平均在线
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public float getChannelAvgOnlineNum(Date starttime, Date endtime, long channelid, long channelitemid) {
		return onlinePlayerDAO.getChannelAvgOnlineNum(starttime, endtime, channelid, channelitemid);
	}
	
	/**
	 * 获得某段时间内平均在线
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public float getChannelAvgOnlineNum(Date starttime, Date endtime, long channelid) {
		return onlinePlayerDAO.getChannelAvgOnlineNum(starttime, endtime, channelid);
	}
	
	/**
	 * 获得某服务器平均在线
	 * @return
	 */
	public float getAvgOnlineNum(long serverid) {
		return onlinePlayerDAO.getAvgOnlineNum(serverid);
	}
	
	/**
	 * 获得某服务器某段时间平均在线
	 * @return
	 */
	public float getAvgOnlineNum(Date starttime, Date endtime, long serverid) {
		return onlinePlayerDAO.getAvgOnlineNum(starttime, endtime, serverid);
	}
	
	/**
	 * 获得某时段的平均在线用户
	 * @param hourFrom
	 * @param hourTo
	 * @return
	 */
	public float getAvgOnlineNum(int hourFrom, int hourTo) {
		return onlinePlayerDAO.getAvgOnlineNum(hourFrom, hourTo);
	}
	
	/**
	 * 获得某服务器某时段的平均在线用户
	 * @param hourFrom
	 * @param hourTo
	 * @return
	 */
	public float getAvgOnlineNum(long serverid, int hourFrom, int hourTo) {
		return onlinePlayerDAO.getAvgOnlineNum(serverid, hourFrom, hourTo);
	}

	public OnlinePlayerDAO getOnlinePlayerDAO() {
		return onlinePlayerDAO;
	}

	public void setOnlinePlayerDAO(OnlinePlayerDAO onlinePlayerDAO) {
		this.onlinePlayerDAO = onlinePlayerDAO;
	}

}
