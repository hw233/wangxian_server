package com.fy.engineserver.activity.disaster;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.disaster.instance.Disaster;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 金猴天灾地图心跳管理
 */
public class DisasterGameThread extends Thread{
	/** 线程管理的金猴天灾游戏场景列表 */
	private List<Disaster> gameList = new ArrayList<Disaster>();
	/** 需要删除的场景 */
	private List<Disaster> needRemove = new ArrayList<Disaster>();
	/** 需要添加的场景 */
	private List<Disaster> needAdd = new ArrayList<Disaster>();
	public static boolean isOpen = true;
	
	private long threadTime = 100;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		DisasterManager.logger.warn("[DisasterGameThread] [启动成功] [" + this.getName() + "]");
		while(isOpen) {
			long now = System.currentTimeMillis();
			if (needAdd.size() > 0) {
				synchronized (needAdd) {
					gameList.addAll(needAdd);
					needAdd.clear();
					if (DisasterManager.logger.isDebugEnabled()) {
						DisasterManager.logger.debug("[有新加游戏场景] [加入] [结束] [加入后gameList:" + gameList.size() + "]");
					}
				}
			}
			for (int i=0; i<gameList.size(); i++) {
				try {
					gameList.get(i).heartbeat(now);
					if (gameList.get(i).needDelete(now)) {
						needRemove.add(gameList.get(i));
					}
				} catch (Exception e) {
					if (DisasterManager.logger.isInfoEnabled()) {
						DisasterManager.logger.info("[金猴天灾] [游戏场景心跳异常] [" + gameList.get(i).getLogString() + "]", e);
					}
				}
			}
			
			if (needRemove.size() > 0) {
				gameList.removeAll(needRemove);
				needRemove.clear();
			}
			try {
				Thread.sleep(threadTime);
			} catch (Exception e) {
				
			}
		}
		DisasterManager.logger.warn("[DisasterGameThread] [关闭成功] [" + this.getName() + "]");
	}
	
	public boolean isPlayerDead(Player player) {
		Disaster[] ds = gameList.toArray(new Disaster[gameList.size()]);
		for (Disaster d : ds) {
			boolean b = d.isPlayerDead(player);
			if (b) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPlayerInDisaster(Player player) {
		Disaster[] ds = gameList.toArray(new Disaster[gameList.size()]);
		for (Disaster d : ds) {
			boolean b = d.isPlayerDead(player);
			if (b) {
				return true;
			}
		}
		return false;
	}
	
	public Game findPlayerGame(Player player) {
		Disaster[] ds = gameList.toArray(new Disaster[gameList.size()]);
		Game game = null;
		for (Disaster d : ds) {
			game = d.findPlayerGame(player);
			if (game != null) {
				return game;
			}
		}
		return null;
	}
	
	public Disaster findPlayerDisaster(Player player) {
		Disaster[] ds = gameList.toArray(new Disaster[gameList.size()]);
		for (Disaster d : ds) {
			if (d.findDp(player) != null && !d.findDp(player).isEnd() && d.getCurrentStep() != null) {
				return d;
			}
		}
		return null;
	}
	
	public List<Disaster> findPlayerRecentlyDisaster(Player player) {
		List<Disaster> list = new ArrayList<Disaster>();
		Disaster[] ds = gameList.toArray(new Disaster[gameList.size()]);
		for (Disaster d : ds) {
			if (d.findDp(player) != null) {
				list.add(d);
			}
		}
		return list;
	}
	
	public boolean notifyAroundChange(Player player) {
		Disaster[] ds = gameList.toArray(new Disaster[gameList.size()]);
		boolean result = false;
		for (Disaster d : ds) {
			result = d.notifyAroundChange(player);
			if (result) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 玩家进入其他场景，等同于提前退出
	 * @param p
	 * @param gameName
	 * @return
	 */
	public boolean noticePlayerTrans2OtherGame(Player p, String gameName) {
		Disaster[] ds = gameList.toArray(new Disaster[gameList.size()]);
		boolean result = false;
		for (Disaster d : ds) {
			result = d.noticePlayerTrans2OtherGame(p, gameName);
			if (result) {
				return true;
			}
		}
		return false;
	}
	
	public boolean causeDamage(Player player, int damage) {
		Disaster[] ds = gameList.toArray(new Disaster[gameList.size()]);
		boolean result = false;
		for (Disaster d : ds) {
			result = d.causeDamage(player, damage);
			if (result) {
				return true;
			}
		}
		return false;
	}
	public boolean leaveGame(Player player) {
		Disaster[] ds = gameList.toArray(new Disaster[gameList.size()]);
		boolean result = false;
		for (Disaster d : ds) {
			result = d.leaveGame(player);
			if (result) {
				return true;
			}
		}
		return false;
	}
	
	public void addNewGame(Disaster d) {
		synchronized (needAdd) {
			if (!needAdd.contains(d)) {
				needAdd.add(d);
			}
		}
	}

	public List<Disaster> getGameList() {
		return gameList;
	}

	public void setGameList(List<Disaster> gameList) {
		this.gameList = gameList;
	}

	public List<Disaster> getNeedRemove() {
		return needRemove;
	}

	public void setNeedRemove(List<Disaster> needRemove) {
		this.needRemove = needRemove;
	}
}
