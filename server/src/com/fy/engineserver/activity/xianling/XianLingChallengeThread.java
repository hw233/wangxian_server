package com.fy.engineserver.activity.xianling;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTaskAgent;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.SimpleMonster;

public class XianLingChallengeThread extends Thread {
	private List<XianLingChallenge> gameList = new Vector<XianLingChallenge>();
	public static boolean isStart = true;
	public static int refreshBossTime = 5000; // 玩家进入地图后多少毫秒后刷出任务boss
	/** key=playerId value=startTime */
	public Map<Long, Long> timeMaps = new Hashtable<Long, Long>();
	public Map<TimerTaskAgent, Long> timebarMaps = new Hashtable<TimerTaskAgent, Long>(); // map<TimerTaskAgent,打断读条时间>
	public static long heartBeat = 100;
	public static int heartBeatWarnTime = 150; // 毫秒

	@Override
	public void run() {
		if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [线程启动成功] [线程name:" + this.getName() + "]");
		while (isStart) {
			try {
				long startTime = System.currentTimeMillis();
				List<XianLingChallenge> needRemoveList = new ArrayList<XianLingChallenge>();
				List<TimerTaskAgent> removeTimeBarList = new ArrayList<TimerTaskAgent>();
				Player player = null;
				XianLingChallenge[] list = gameList.toArray(new XianLingChallenge[gameList.size()]);
				for (XianLingChallenge xc : list) {
					try {
						xc.getGame().heartbeat();
						try {
							player = GamePlayerManager.getInstance().getPlayer(xc.getPlayerId());
							if (player.isDeath() && !(xc.getResult() == XianLingChallenge.PLAYERDEATH || xc.getResult() == XianLingChallenge.USETRANSPROP || xc.getResult() == XianLingChallenge.TRANSFER)) {
								if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [线程name:" + this.getName() + "] [玩家死亡:" + player.getLogString() + "] [xc.getResult()=" + xc.getResult() + "]");
								this.notifyPlayerDead(player, XianLingChallenge.PLAYERDEATH);
							} else if (!player.isOnline() && !(xc.getResult() == XianLingChallenge.NOTONLINE || xc.getResult() == XianLingChallenge.USETRANSPROP || xc.getResult() == XianLingChallenge.TRANSFER)) {
								if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [线程name:" + this.getName() + "] [玩家不在线:" + player.getLogString() + "] [xc.getResult()=" + xc.getResult() + "]");
								this.notifyPlayerDead(player, XianLingChallenge.NOTONLINE);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (timeMaps.get(xc.getPlayerId()) != 0) {
							long time = timeMaps.get(xc.getPlayerId());
							if (!xc.isRefreshMonster() && (System.currentTimeMillis() - time) >= refreshBossTime) {
								xc.refreshMonster(player);
							}
						}
						if ((timeMaps.get(xc.getPlayerId()) + XianLingChallengeManager.countDownTime + refreshBossTime) < System.currentTimeMillis() && !(xc.getResult() == XianLingChallenge.TIMEEND || xc.getResult() == XianLingChallenge.USETRANSPROP || xc.getResult() == XianLingChallenge.TRANSFER)) {
							if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [副本超时] [线程name:" + this.getName() + "] [玩家:" + player.getLogString() + "] [xc.getResult()=" + xc.getResult() + "]");
							// player.send_HINT_REQ(Translate.超出传送时间, (byte) 5);
							xc.setResult(XianLingChallenge.TIMEEND);
							// timeMaps.remove(xc.getPlayerId());
						}

						// }

						if (xc.getResult() == XianLingChallenge.USETRANSPROP || xc.getResult() == XianLingChallenge.NOTONLINE || xc.getResult() == XianLingChallenge.TIMEEND) {
							xc.回城(player);
							needRemoveList.add(xc);
							if (XianLingManager.logger.isInfoEnabled()) XianLingManager.logger.info("[仙灵挑战] [加入到删除列表中] [" + xc + "]");
						}
						if (xc.getResult() == XianLingChallenge.TRANSFER) {
							needRemoveList.add(xc);
							if (XianLingManager.logger.isInfoEnabled()) XianLingManager.logger.info("[仙灵挑战中被拉] [加入到删除列表中] [" + xc + "]");
						}
					} catch (Exception e) {
						if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵挑战] [game心跳异常] [playerId : " + xc.getPlayerId() + "]", e);
					}
				}
				// 打断读条
				if (timebarMaps.size() > 0) {
					TimerTaskAgent[] temp = timebarMaps.keySet().toArray(new TimerTaskAgent[timebarMaps.size()]);
					for (TimerTaskAgent ta : temp) {
						if (ta != null) {
							if (timebarMaps.get(ta) < System.currentTimeMillis()) {
								removeTimeBarList.add(ta);
								try {
									if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [捕捉失败打断读条] [线程name:" + this.getName() + "]");
									ta.notifyCatchFailed();
									if(player != null){
										player.sendError(Translate.捕捉失败);
									}
								} catch (Exception e) {
									if (XianLingManager.logger.isInfoEnabled()) XianLingManager.logger.info("[仙灵捕捉] [读条打断异常]", e);
								}
							}
						}
					}
				}

				if (removeTimeBarList.size() > 0) {
					for (TimerTaskAgent ta : removeTimeBarList) {
						timebarMaps.remove(ta);
					}
				}
				for (XianLingChallenge xc : needRemoveList) {
					boolean result = gameList.remove(xc);
					timeMaps.remove(xc.getPlayerId());
					if (XianLingChallengeManager.logger.isDebugEnabled()) {
						XianLingChallengeManager.logger.debug("[仙灵挑战] [玩家挑战结束删除线程,删除结果:" + result + "] [" + xc + "]");
					}
				}
				long endTime = System.currentTimeMillis();
				if (endTime - startTime > heartBeatWarnTime) {
					if (XianLingChallengeManager.logger.isWarnEnabled()) {
						XianLingChallengeManager.logger.warn("[仙灵挑战] [线程心跳时间超时] [执行心跳时间:" + (endTime - startTime) + " 毫秒]");
					}
				}
				player = null;
				needRemoveList = null;
				try {
					Thread.sleep(heartBeat);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵挑战] [game心跳异常]", e);
			}
		}
		if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [线程结束] [isStart:" + isStart + "]");
	}

	/**
	 * 开始挑战
	 * @param game
	 * @param player
	 * @param monster
	 */
	public void notifyStartChallenge(XianLingLevelData levelData, long monsterId, Game game, Player player, int categoryId) {
		try {
			Game currGame = player.getCurrentGame();
			int scale = 0;
			if (XianLingManager.instance.scaleMap.containsKey(categoryId)) {
				scale = XianLingManager.instance.scaleMap.get(categoryId).getScale();
			}
			if (player.isIsUpOrDown() /* && player.isFlying() */) {
				player.downFromHorse(true);
			}
			if (player.getActivePetId() > 0) { // 宠物本来在出战状态强制回收
				player.packupPet(false);
			}
			XianLingChallenge xc = new XianLingChallenge(player.getId(), game, (byte) 0, categoryId, monsterId, false, currGame.gi.name, player.getX(), player.getY(), scale);
			xc.setRemoveTime(System.currentTimeMillis() + XianLingChallengeManager.countDownTime + refreshBossTime);
			currGame.transferGame(player, new TransportData(0, 0, 0, 0, game.gi.name, XianLingChallengeManager.bornPoint[0], XianLingChallengeManager.bornPoint[1]));
			if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [" + player.getLogString() + "] [传送进入地图:" + game.gi.name + "]");
			if (!gameList.contains(xc)) {
				gameList.add(xc);
			}
			// if (player.getXianlingData() != null) {
			// if (player.getXianlingData().getMaxLevel() < levelData.getLevel()) {
			// player.getXianlingData().setMaxLevel(levelData.getLevel());
			// }
			// }
			player.setHp(player.getMaxHP());
			player.setMp(player.getMaxMP());
			player.xl_chanllengeFlag = 2;
			timeMaps.put(player.getId(), System.currentTimeMillis());
		} catch (Exception e) {
			if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [" + player.getLogString() + "] [传送进入地图:" + game.gi.name + "]", e);

		}
	}

	/**
	 * 玩家死亡
	 * @param player
	 */
	public void notifyPlayerDead(Player player, byte type) {
		if (gameList == null || gameList.size() <= 0) {
			return;
		}
		XianLingChallenge[] list = gameList.toArray(new XianLingChallenge[gameList.size()]);
		for (XianLingChallenge xc : list) {
			xc.notifyPlayerDead(player, type);
		}
	}

	/**
	 * 杀死怪物
	 * @param monster
	 */
	public void notifyMonsterKilled(SimpleMonster monster) {
		if (gameList == null || gameList.size() <= 0) {
			return;
		}
		XianLingChallenge[] list = gameList.toArray(new XianLingChallenge[gameList.size()]);
		for (XianLingChallenge xc : list) {
			xc.notifyMonsterKilled(monster);
		}
	}

	public Game findGame(Player player) {
		XianLingChallenge[] aa = gameList.toArray(new XianLingChallenge[gameList.size()]);
		for (XianLingChallenge x : aa) {
			if (x.getPlayerId() == player.getId()) {
				if (x.getResult() != 0) {
					return null;
				}
				return x.getGame();
			}
		}
		return null;
	}

	public XianLingChallenge findXLChallenge(Player player) {
		XianLingChallenge[] aa = gameList.toArray(new XianLingChallenge[gameList.size()]);
		for (XianLingChallenge x : aa) {
			if (x.getPlayerId() == player.getId()) {
				return x;
			}
		}
		return null;
	}

	/**
	 * 捕捉怪物
	 * @param monster
	 */
	public void notifyMonsterCatched(Player player, Monster monster, int catchTimes) {
		if (gameList == null || gameList.size() <= 0) {
			return;
		}
		XianLingChallenge[] list = gameList.toArray(new XianLingChallenge[gameList.size()]);
		for (XianLingChallenge xc : list) {
			if(xc.getPlayerId() != player.getId()){
				continue;
			}
			long endTime = timeMaps.get(player.getId()) + XianLingChallengeManager.countDownTime;
			xc.notifyMonsterCatched(this, player, endTime, monster, catchTimes);
		}
	}

	public boolean isPlayerAtThread(long playerId) {
		if (gameList == null || gameList.size() <= 0) {
			return false;
		}
		XianLingChallenge[] list = gameList.toArray(new XianLingChallenge[gameList.size()]);
		for (XianLingChallenge xc : list) {
			if (xc.getPlayerId() == playerId) {
				return true;
			}
		}
		return false;
	}

	public List<XianLingChallenge> getGameList() {
		return gameList;
	}

	public void setGameList(List<XianLingChallenge> gameList) {
		this.gameList = gameList;
	}

}
