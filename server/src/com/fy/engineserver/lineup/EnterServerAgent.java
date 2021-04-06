package com.fy.engineserver.lineup;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.USER_ENTER_SERVER_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.CommonDiskCacheManager;
import com.fy.engineserver.util.CommonSubSystem;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;

/**
 * 
 * 
 * @version 创建时间：Mar 1, 2012 5:38:37 PM
 * 
 */
public class EnterServerAgent implements Runnable {

	public static Logger logger = LoggerFactory.getLogger(EnterServerAgent.class.getName());

	/**
	 * 最大允许进入数量
	 */
	public int maxOnline = 0;

	/**
	 * 新手村允许进入的数量
	 */
	public int xinShouCunOnline = 0;

	public long lineupIn = 0;

	/**
	 * 排队队列
	 */
	private EnterServerLineup lineup;

	// private EnterServerTester tester;

	private long totalAvgElaped;

	private long avgNum;

	private boolean newEnter = true;

	private Player currentOnlinePlayers[];

	private long lastSyncStatusTime = System.currentTimeMillis();

	private static EnterServerAgent instance;

	public static EnterServerAgent getInstance() {
		return instance;
	}

	public void init() {
		
		lineup = new EnterServerLineup(this);
		Thread t = new Thread(this, "EnterServerAgent");
		t.start();
		instance = this;
		
		maxOnline = 0;
		xinShouCunOnline = 0;
		if (CommonDiskCacheManager.getInstance().getProps(CommonSubSystem.总在线) != null) {
			try {
				maxOnline = Integer.valueOf(CommonDiskCacheManager.getInstance().getProps(CommonSubSystem.总在线).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} 
		if (maxOnline == 0) {
			maxOnline = 2000;
			CommonDiskCacheManager.getInstance().put(CommonSubSystem.总在线, maxOnline);
		}
		
		if (CommonDiskCacheManager.getInstance().getProps(CommonSubSystem.新手村在线) != null) {
			try {
				xinShouCunOnline = Integer.valueOf(CommonDiskCacheManager.getInstance().getProps(CommonSubSystem.新手村在线).toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} 
		
		if (xinShouCunOnline == 0) {
			xinShouCunOnline = 500;
			CommonDiskCacheManager.getInstance().put(CommonSubSystem.新手村在线, xinShouCunOnline);
		}
		ServiceStartRecord.startLog(this);
	}

	public void reInit() {
		lineup = new EnterServerLineup(this);
		maxOnline = 1800;
		xinShouCunOnline = 500;
		lineupIn = 0;
		// this.tester = null;
	}

	public EnterServerLineup getLineup() {
		return lineup;
	}

	public void setLineup(EnterServerLineup lineup) {
		this.lineup = lineup;
	}

	public int getMaxOnline() {
		return maxOnline;
	}

	public void setMaxOnline(int maxOnline) {
		this.maxOnline = maxOnline;
		CommonDiskCacheManager.getInstance().put(CommonSubSystem.总在线, maxOnline);
	}

	public int getXinShouCunOnline() {
		return xinShouCunOnline;
	}

	public void setXinShouCunOnline(int xinShouCunOnline) {
		this.xinShouCunOnline = xinShouCunOnline;
		CommonDiskCacheManager.getInstance().put(CommonSubSystem.新手村在线, xinShouCunOnline);
	}

	// public EnterServerTester getTester() {
	// return tester;
	// }
	//
	// public void setTester(EnterServerTester tester) {
	// this.tester = tester;
	// }

	public long getLineupIn() {
		return lineupIn;
	}

	public void setLineupIn(int lineupIn) {
		this.lineupIn = lineupIn;
	}

	public long getTotalAvgElaped() {
		return totalAvgElaped;
	}

	public void setTotalAvgElaped(long totalAvgElaped) {
		this.totalAvgElaped = totalAvgElaped;
	}

	public long getAvgNum() {
		return avgNum;
	}

	public void setAvgNum(long avgNum) {
		this.avgNum = avgNum;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);

				long start = System.currentTimeMillis();

				// 检查排队item的状态，去掉超时的玩家
				lineup.checkAndClear();

				if (start - lastSyncStatusTime > 10 * 1000) {
					lastSyncStatusTime = start;
					lineup.syncLineupStatus();
				}

				currentOnlinePlayers = PlayerManager.getInstance().getOnlinePlayers();

				if (lineup.getQueue().size() > 0) {

					int newNum = 得到新手村在线玩家数量();
					int total = 得到所有在线玩家数量();
					int prepare = 得到准备进入游戏的玩家数量();
					// int notInGame = 得到在线玩家但未进入游戏的玩家();

					// 检查是否能进入
					List<LineupItem> poped = new ArrayList<LineupItem>();
					synchronized (this) {
						int canEnter1 = xinShouCunOnline - (newNum + prepare);
						int canEnter2 = maxOnline - (total + prepare);
						if (canEnter1 > canEnter2) {
							canEnter1 = canEnter2;
						}
						if (canEnter1 > 0) {
							for (int i = 0; i < canEnter1 && lineup.getQueue().size() > 0; i++) {
								LineupItem item = lineup.popOne();
								if (item != null) {
									poped.add(item);
								}
							}
						}
					}
					for (LineupItem item : poped) {
						notifyEnterServer(item);
						lineupIn++;
					}
					int stat[] = lineup.getLineupState();

					if (logger.isInfoEnabled()) logger.info("[入服代理心跳] [当前在线玩家:" + currentOnlinePlayers.length + "] [排队队列:" + (stat[0] + stat[1]) + "] [在线/离线:" + stat[0] + "/" + stat[1] + "] [当前平均进入间隔:" + this.getAvgElapedPerNum() + "] [准备进入:" + prepare + "] [游戏内玩家总数:" + total + "] [新手村总数:" + newNum + "] [通过排队进入的数量:" + lineupIn + "] [" + (System.currentTimeMillis() - start) + "ms]");
				} else {
					if (logger.isInfoEnabled()) logger.info("[入服代理心跳] [当前在线玩家:" + currentOnlinePlayers.length + "] [排队队列:0] [在线/离线:0/0] [当前平均进入间隔:" + this.getAvgElapedPerNum() + "] [通过排队进入的数量:" + lineupIn + "] [" + (System.currentTimeMillis() - start) + "ms]");
				}
			} catch (Throwable e) {
				logger.error("[入服代理心跳失败]", e);
			}
		}
	}

	/**
	 * 是否能直接进入服务器，如果能则增加准备进入数量
	 * @return
	 */
	public synchronized boolean canEnterDirectly(String userName, Connection conn) {
		long start = System.currentTimeMillis();
		if (newEnter) {
			currentOnlinePlayers = PlayerManager.getInstance().getOnlinePlayers();
			newEnter = false;
		}
		int total = 得到所有在线玩家数量();
		int newNum = 得到新手村在线玩家数量();
		int prepare = 得到准备进入游戏的玩家数量();
		// int notInGame = 得到在线玩家但未进入游戏的玩家();
		int linenum = lineup.getQueue().size();
		if (linenum <= 0) {
			if (total + prepare < maxOnline && newNum + prepare < xinShouCunOnline) {
				lineup.prepareToEnter(userName, conn);
				if (logger.isInfoEnabled()) logger.info("[入服请求：可以直接进入] [userName:" + userName + "] [游戏内总数:" + total + "] [新手村:" + newNum + "] [准备进入:" + prepare + "] [队列长度:" + linenum + "] [通过排队进入的数量:" + lineupIn + "] [" + (System.currentTimeMillis() - start) + "ms]");
				return true;
			}
		} else {
			if (lineup.isPreparing(userName, conn)) {
				if (logger.isInfoEnabled()) logger.info("[入服请求：可以直接进入(之前在准备进入)] [userName:" + userName + "] [游戏内总数:" + total + "] [新手村:" + newNum + "] [准备进入:" + prepare + "] [队列长度:" + linenum + "] [通过排队进入的数量:" + lineupIn + "] [" + (System.currentTimeMillis() - start) + "ms]");
				return true;
			}
			if (total + prepare < maxOnline && newNum + prepare < xinShouCunOnline) {
				// 检查排队中的是否都是离线的
				int state[] = lineup.getLineupState();
				if (state[0] == 0) {
					if (logger.isInfoEnabled()) logger.info("[入服请求：可以直接进入(队列中在线为0)] [userName:" + userName + "] [游戏内总数:" + total + "] [新手村:" + newNum + "] [准备进入:" + prepare + "] [队列长度:" + linenum + "] [通过排队进入的数量:" + lineupIn + "] [" + (System.currentTimeMillis() - start) + "ms]");
					return true;
				}
			}
		}
		if (logger.isInfoEnabled()) logger.info("[入服请求：需要排队] [userName:" + userName + "] [游戏内总数:" + total + "] [新手村:" + newNum + "] [准备进入:" + prepare + "] [队列长度:" + linenum + "] [通过排队进入的数量:" + lineupIn + "] [" + (System.currentTimeMillis() - start) + "ms]");
		return false;
	}

	/**
	 * 玩家进入游戏后回调通知
	 */
	public synchronized void playerEnterCallback(String userName) {
		long start = System.currentTimeMillis();
		lineup.removeLineup(userName);
		LineupItem item = lineup.removePrepareToEnter(userName);
		int total = 得到所有在线玩家数量();
		int newNum = 得到新手村在线玩家数量();
		int prepare = 得到准备进入游戏的玩家数量();
		newEnter = true;
		if (logger.isInfoEnabled()) logger.info("[玩家进入游戏回调] [-] [从准备到进入时间:" + (item != null ? (start - item.getNotifyEnterTime()) : "NULL") + "ms] [userName:" + userName + "] [游戏内总数:" + total + "] [新手村:" + newNum + "] [准备进入:" + prepare + "] [队列长度:" + lineup.getQueue().size() + "] [通过排队进入的数量:" + lineupIn + "] [" + (System.currentTimeMillis() - start) + "ms]");
	}

	/**
	 * 取消排队
	 * @param userName
	 */
	public synchronized void cancelLineup(String userName) {
		long start = System.currentTimeMillis();
		LineupItem item = lineup.removeLineup(userName);
		lineup.removePrepareToEnter(userName);
		int total = 得到所有在线玩家数量();
		int newNum = 得到新手村在线玩家数量();
		int prepare = 得到准备进入游戏的玩家数量();
		if (logger.isInfoEnabled()) logger.info("[玩家取消排队] [-] [userName:" + userName + "] [排队时长:" + (item != null ? (start - item.getEnterTime()) : "--") + "ms] [游戏内总数:" + total + "] [新手村:" + newNum + "] [准备进入:" + prepare + "] [队列长度:" + lineup.getQueue().size() + "] [通过排队进入的数量:" + lineupIn + "] [" + (System.currentTimeMillis() - start) + "ms]");
	}

	/**
	 * 进行排队
	 */
	public synchronized void doLineup(String userName, Connection conn) {
		long start = System.currentTimeMillis();
		LineupItem item = lineup.doLineup(userName, conn);
		int total = 得到所有在线玩家数量();
		int newNum = 得到新手村在线玩家数量();
		int prepare = 得到准备进入游戏的玩家数量();
		int queueNum = lineup.getQueue().size();
		if (logger.isInfoEnabled()) logger.info("[玩家排队] [-] [userName:" + userName + "] [预估进入时间:" + this.getEstimateTime(item.getPos()) + "] [游戏内总数:" + total + "] [新手村:" + newNum + "] [准备进入:" + prepare + "] [队列长度:" + queueNum + "] [通过排队进入的数量:" + lineupIn + "] [" + (System.currentTimeMillis() - start) + "ms]");
	}

	/**
	 * 得到排队的状态
	 * 0-在我之前在线的排队人数, 1-在我之前的离线排队人数
	 * @param userName
	 * @return
	 */
	public int[] getLineupStatus(String userName) {
		return lineup.getLinenupStat(userName);
	}

	public int[] getLineupStatus() {
		return lineup.getLineupState();
	}

	public int 得到新手村在线玩家数量() {
		int num = 0;
		if (currentOnlinePlayers != null) {
			for (Player p : currentOnlinePlayers) {
				if (p != null && p.getCurrentGame() != null && p.getCurrentGame().gi != null && p.getCurrentGame().gi.name != null && (p.getCurrentGame().gi.name.equals("linshuangcun") || p.getCurrentGame().gi.name.equals("jiaoshacun") || p.getCurrentGame().gi.name.equals("taoyuancun"))) {
					num++;
				}
			}
		}
		return num;
	}

	public int 得到所有在线玩家数量() {
		if (currentOnlinePlayers != null) {
			return currentOnlinePlayers.length;
		}
		return 0;
	}

	// public int 得到在线玩家但未进入游戏的玩家() {
	// int num = 0;
	// if(currentOnlinePlayers != null) {
	// for (Player p : currentOnlinePlayers) {
	// if (p.getConn() != null && p.getConn().getState() != Connection.CONN_STATE_CLOSE && p.getCurrentGame() == null) {
	// num++;
	// }
	// }
	// }
	// return num;
	// }

	public int 得到准备进入游戏的玩家数量() {
		return lineup.getPrepareToEnter().size();
	}

	/**
	 * 通知一个排队item进入游戏服
	 * @param item
	 */
	public void notifyEnterServer(LineupItem item) {
		long start = System.currentTimeMillis();

		lineup.prepareToEnter(item.getUserName(), item.getConn());

		USER_ENTER_SERVER_RES res = new USER_ENTER_SERVER_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, 0L, 0L, 0L);
		GameNetworkFramework.getInstance().sendMessage(item.getConn(), res, "USER_ENTER_SERVER_RES");

		// 统计一下排队时间
		if (item.getNotifyEnterTime() == 0) {
			long elaped = start - item.getEnterTime();
			int pos = item.getPos();
			long avg = elaped / (pos + 1);
			accumulate(avg);
		}
		int total = 得到所有在线玩家数量();
		int newNum = 得到新手村在线玩家数量();
		int prepare = 得到准备进入游戏的玩家数量();
		int queueNum = lineup.getQueue().size();
		if (logger.isInfoEnabled()) logger.info("[通知玩家准备进入游戏] [-] [userName:" + item.getUserName() + "] [新手村:" + newNum + "] [游戏内总数:" + total + "] [准备进入:" + prepare + "] [队列长度:" + queueNum + "] [通过排队进入的数量:" + lineupIn + "] [" + (System.currentTimeMillis() - start) + "ms]");
	}

	public void accumulate(long avgElaped) {
		this.totalAvgElaped += avgElaped;
		this.avgNum++;
	}

	/**
	 * 获得平均每个位置所消耗的时间,ms
	 * @return
	 */
	public long getAvgElapedPerNum() {
		if (avgNum > 0) {
			return totalAvgElaped / avgNum;
		}
		return 0;
	}

	/**
	 * 得到预估进入的时间
	 * @param pos
	 * @return
	 */
	public long getEstimateTime(int pos) {
		return getAvgElapedPerNum() * (pos + 1);
	}

	/**
	 * 得到用户当前所在位置
	 * @param userName
	 * @return
	 */
	public int getNowPosition(String userName) {
		return lineup.getNowPos(userName);
	}

	/**
	 * 得到某用户的预计进入时间
	 * @param userName
	 * @return
	 */
	public long getEstimateTime(String userName) {
		int nowPos = lineup.getNowPos(userName);
		return getEstimateTime(nowPos);
	}

	public void clearLineup() {
		lineup.queue.clear();
		lineup.prepareToEnter.clear();
	}
}
