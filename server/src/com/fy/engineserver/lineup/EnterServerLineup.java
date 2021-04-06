package com.fy.engineserver.lineup;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.LINEUP_STATUS_REQ;
import com.xuanzhi.tools.transport.Connection;

/**
 *
 * 
 * @version 创建时间：Mar 1, 2012 2:08:04 PM
 * 
 */
public class EnterServerLineup {
	
	public static int NOTIFY_TIMEOUT = 30*60*1000;
	
	public static int ENTER_TIMEOUT = 5*60*1000;
		
	public List<LineupItem> queue;
	
	public List<LineupItem> prepareToEnter;
	
	public int notifyTimeoutNum;
	
	public int enterTimeoutNum;
	
	public EnterServerAgent agent;
	
	public EnterServerLineup(EnterServerAgent agent) {
		this.agent = agent;
		queue = Collections.synchronizedList(new LinkedList<LineupItem>());
		prepareToEnter = Collections.synchronizedList(new LinkedList<LineupItem>());
	}
	
	
	public List<LineupItem> getQueue() {
		return queue;
	}
	
	public List<LineupItem> getPrepareToEnter() {
		return prepareToEnter;
	}


	public void setPrepareToEnter(List<LineupItem> prepareToEnter) {
		this.prepareToEnter = prepareToEnter;
	}
	
	public synchronized void syncLineupStatus() {
		long start = System.currentTimeMillis();
		Iterator<LineupItem> ite = queue.iterator();
		int online = 0;
		int offline = 0;
		int pos = 0;
		while(ite.hasNext()) {
			LineupItem item = ite.next();
			try {
				Connection conn = item.getConn();
				if(conn != null && conn.getState() != Connection.CONN_STATE_CLOSE) {
					LINEUP_STATUS_REQ req = new LINEUP_STATUS_REQ(GameMessageFactory.nextSequnceNum(), online, offline, agent.getEstimateTime(pos));
					GameNetworkFramework.getInstance().sendMessage(conn, req, "LINEUP_STATUS_REQ");
					online++;
				} else {
					offline++;
				}
			} catch(Exception e) {
				if(EnterServerAgent.logger.isInfoEnabled())
					EnterServerAgent.logger.info("[和排队中的客户端同步排队信息] [异常]", e);
			}
			pos++;
		}
		if(EnterServerAgent.logger.isInfoEnabled())
			EnterServerAgent.logger.info("[和排队中的客户端同步排队信息] [在线:"+online+"] [离线:"+offline+"] ["+(System.currentTimeMillis()-start)+"ms]");
	}


	/**
	 * 某一个帐号加入排队
	 * @param userName
	 */
	public synchronized LineupItem doLineup(String userName, Connection conn) {
		//首先检查玩家是否已经在队列中
		LineupItem item = getLineupItem(userName);
		if(item != null) {
			item.setConn(conn);
			return item;
		} else {
			item = new LineupItem();
			item.setConn(conn);
			item.setUserName(userName);
			item.setEnterTime(System.currentTimeMillis());
			item.setNotifyEnterTime(0);
			queue.add(item);
			int pos = queue.size()-1;
			item.setPos(pos);
			return item;
		}
	}
	
	public int[] getLineupState() {
		int online = 0;
		int offline = 0;
		try {
			for(LineupItem item : queue) {
				if(item.getConn() == null || item.getConn().getState() == Connection.CONN_STATE_CLOSE) {
					offline++;
				} else {
					online++;
				}
			}
		} catch(Exception e) {
			EnterServerAgent.logger.error("[获得排队状态时异常]", e);
		}
		return new int[]{online, offline};
	}
	
	/**
	 * 获得在用户之前的排队信息
	 * 0-在线排队的用户 ， 1-离线排队的用户
	 * @return
	 */
	public int[] getLinenupStat(String userName) {
		int online = 0;
		int offline = 0;
		try {
			for(LineupItem item : queue) {
				if(item.getUserName().equals(userName)) {
					break;
				}
				if(item.getConn() == null || item.getConn().getState() == Connection.CONN_STATE_CLOSE) {
					offline++;
				} else {
					online++;
				}
			}
		} catch(Exception e) {
			EnterServerAgent.logger.error("[获得排队状态时异常] [userName:"+userName+"]", e);
		}
		return new int[]{online, offline};
	}
	
	/**
	 * 检查超时的用户
	 */
	public synchronized void checkAndClear() {
		long now = System.currentTimeMillis();
		int nt = 0;
		int et = 0;
		
		//先检查排队队列中下线排队超时的用户
		Iterator<LineupItem> ite = queue.iterator();
		while(ite.hasNext()) {
			LineupItem item = ite.next();
			if(item.getNotifyEnterTime() > 0 && now - item.getNotifyEnterTime() > NOTIFY_TIMEOUT) {
				ite.remove();
				notifyTimeoutNum++;
				nt++;
				if (EnterServerAgent.logger.isDebugEnabled())
				EnterServerAgent.logger.debug("[通知进入超时] ["+item.getLogStr()+"]");
			}
		}
		
		//检查准备进入游戏，而长时间未进入的用户
		ite = prepareToEnter.iterator();
		while(ite.hasNext()) {
			LineupItem item = ite.next();
			if(now - item.getNotifyEnterTime() > ENTER_TIMEOUT) {
				ite.remove();
				enterTimeoutNum++;
				et++;
				if (EnterServerAgent.logger.isDebugEnabled())
				EnterServerAgent.logger.debug("[准备进入超时] ["+item.getLogStr()+"]");
			}
		}
		
		if(EnterServerAgent.logger.isInfoEnabled())
			EnterServerAgent.logger.info("[检查清理超时的排队和进入] [通知超时:"+nt+"] [进入超时:"+et+"] [超时总数(通知/进入):"+(notifyTimeoutNum+"/"+enterTimeoutNum)+"] ["+(System.currentTimeMillis()-now)+"ms]");
	}
	
	/**
	 * 删除一个排队
	 * @param userName
	 */
	public synchronized LineupItem removeLineup(String userName) {
		Iterator<LineupItem> ite = queue.iterator();
		while(ite.hasNext()) {
			LineupItem item = ite.next();
			if(item.getUserName().equals(userName)) {
				ite.remove();
				if (EnterServerAgent.logger.isDebugEnabled())
				EnterServerAgent.logger.debug("[删除排队] ["+item.getLogStr()+"]");
				return item;
			}
		}
		return null;
	}
	
	/**
	 * 玩家准备进入游戏
	 * @param userName
	 * @param conn
	 */
	public synchronized void prepareToEnter(String userName, Connection conn) {
		long now = System.currentTimeMillis();
		LineupItem item = new LineupItem();
		item.setConn(conn);
		item.setUserName(userName);
		item.setEnterTime(now);
		item.setNotifyEnterTime(now);
		prepareToEnter.add(item);
		if (EnterServerAgent.logger.isDebugEnabled())
		EnterServerAgent.logger.debug("[准备进入服务器] ["+item.getLogStr()+"] [prepare:"+prepareToEnter.size()+"]");
	}
	
	/**
	 * 是否在准备进入服务器
	 * @param userName
	 * @param conn
	 * @return
	 */
	public synchronized boolean isPreparing(String userName, Connection conn) {
		Iterator<LineupItem> ite = prepareToEnter.iterator();
		while(ite.hasNext()) {
			LineupItem item = ite.next();
			if(item.getUserName().equals(userName)) {
				item.setConn(conn);
				if (EnterServerAgent.logger.isDebugEnabled())
				EnterServerAgent.logger.debug("[是否在准备进入服务器] ["+item.getLogStr()+"]");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 删除准备进入游戏的用户
	 * @param userName
	 */
	public synchronized LineupItem removePrepareToEnter(String userName) {
		Iterator<LineupItem> ite = prepareToEnter.iterator();
		while(ite.hasNext()) {
			LineupItem item = ite.next();
			if(item.getUserName().equals(userName)) {
				ite.remove();
				if (EnterServerAgent.logger.isDebugEnabled())
				EnterServerAgent.logger.debug("[删除准备进入服务器] ["+item.getLogStr()+"]");
				return item;
			}
		}
		return null;
	}
	
	/**
	 * 从队列中取出一个在线的连接，准备通知玩家进入游戏
	 */
	public synchronized LineupItem popOne() {
		long now = System.currentTimeMillis();
		Iterator<LineupItem> ite = queue.iterator();
		int pos = 0;
		while(ite.hasNext()) {
			LineupItem item = ite.next();
			if(item.getConn() == null || item.getConn().getState() == Connection.CONN_STATE_CLOSE) {
				if(item.getNotifyEnterTime() == 0) {
					item.setNotifyEnterTime(now);
					if (EnterServerAgent.logger.isDebugEnabled())
					EnterServerAgent.logger.debug("[POP] [此玩家离线,设置优先] [pos:"+pos+"] ["+item.getLogStr()+"] ["+(System.currentTimeMillis()-now)+"ms]");
				}
			} else {
				ite.remove();
				if (EnterServerAgent.logger.isDebugEnabled())
				EnterServerAgent.logger.debug("[POP] [成功取出] [pos:"+pos+"] ["+item.getLogStr()+"] ["+(System.currentTimeMillis()-now)+"ms]");
				return item;
			}
			pos++;
		}
		return null;
	}
	
	/**
	 * 得到队列中的排队item
	 * @param userName
	 */
	public synchronized LineupItem getLineupItem(String userName) {
		for(LineupItem item : queue) {
			if(item.getUserName().equals(userName)) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * 获得玩家的当前在队列中的位置
	 * @param userName
	 * @return
	 */
	public synchronized int getNowPos(String userName) {
		int i = 0;
		for(LineupItem item : queue) {
			if(item.getUserName().equals(userName)) {
				break;
			}
			i++;
		}
		return i;
	}


	public int getNotifyTimeoutNum() {
		return notifyTimeoutNum;
	}


	public void setNotifyTimeoutNum(int notifyTimeoutNum) {
		this.notifyTimeoutNum = notifyTimeoutNum;
	}


	public int getEnterTimeoutNum() {
		return enterTimeoutNum;
	}


	public void setEnterTimeoutNum(int enterTimeoutNum) {
		this.enterTimeoutNum = enterTimeoutNum;
	}
}
