package com.fy.engineserver.activity.TransitRobbery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.fy.engineserver.activity.TransitRobbery.Robbery.BaseRobbery;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

public class RobberyThread extends Thread {
	public static Logger logger = TransitRobberyManager.logger;

	public static int heartBeatTime = 100;
	/** 所有渡劫进度 */
	public List<BaseRobbery> allRobbery = new ArrayList<BaseRobbery>();
	/** 管理所有需要强制拉人 ------如果玩家主动进入渡劫，需要先关闭此心跳再设置玩家已经不需要强制拉入 */
	public volatile Map<Long, Integer> forceLeftTimes = new Hashtable<Long, Integer>();
	/** 没分钟更新一次强制拉人剩余时间 */
	private int UPDATE_TIME = 0;
	/** 玩家下线移除强制拉人时间心跳 */
	public volatile Set<Long> remogeList = new HashSet<Long>();
	
	public static volatile Map<Long, Long> tempForceMaps = new Hashtable<Long, Long>();

	private boolean isStart = true;

	private int count = 0;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		TransitRobberyManager.logger.info(getName() + "--[启动成功]");
		while (isStart) {
			long startTime = System.currentTimeMillis();
			count += 1;
			if (count >= 1000 / heartBeatTime) {
				count = 0;
			}
			synchronized (remogeList) {
				if (remogeList.size() > 0) {
					for (Long temp : remogeList) {
						Integer result = forceLeftTimes.remove(temp);
						logger.info("[清理强制拉人列表][id=" + temp + "] [result:" + result + "]");
					}
					remogeList.clear();
				}
			}
			long testStartTime = System.currentTimeMillis();
			if (allRobbery.size() > 0 || forceLeftTimes.size() > 0) {
				try {
					synchronized (allRobbery) {
						for (int i = 0; i < allRobbery.size(); i++) {
							BaseRobbery baseRobbery = null;
							try {
								baseRobbery = allRobbery.get(i);
								if (baseRobbery == null) {
									continue;
								}
								if (baseRobbery.passCountDownTime <= 0 || !baseRobbery.isAwardFlag) {
									testStartTime = System.currentTimeMillis();
									baseRobbery.heartBeat(); // 先game心跳--天劫心跳用来判断胜利与否
									if(logger.isWarnEnabled() && (System.currentTimeMillis() - testStartTime) > 20) {
										logger.warn("[天劫线程] [玩家天劫线程心跳超时] [心跳时间:" + (System.currentTimeMillis() - testStartTime) + "ms][玩家id:" + baseRobbery.playerId + "]");
									}
								} else {
									baseRobbery.checkCountDownTime();
								}
								testStartTime = System.currentTimeMillis();
								baseRobbery.game.heartbeat();
								if(logger.isWarnEnabled() && (System.currentTimeMillis() - testStartTime) > 20) {
									logger.warn("[天劫线程] [玩家天劫线程心跳超时] [心跳时间:" + (System.currentTimeMillis() - testStartTime) + "ms][玩家id:" + baseRobbery.playerId + "]");
								}
							} catch (Exception e) {
								logger.error("心跳线程出错，playerId=" + baseRobbery.playerId + "e{}", e);
							}
						}
					}
				} catch (Exception e) {
					logger.error("天劫线程出错:", e);
				}
				Iterator<Long> keys = forceLeftTimes.keySet().iterator();
				Long key = 0L;
				int leftTime = 0;
				Player player = null;
				if (count == 0) {
					UPDATE_TIME += 1;
				}
				long et2 = System.currentTimeMillis() - startTime;
				if (et2 > 200) {
					try {
						TransitRobberyManager.logger.error("[执行心跳消耗时间][" + et2 + "ms][" + this.getName() + "][" + allRobbery.size() + "]");
					} catch (Exception e) {
					}
				}
				while (keys.hasNext()) {
					try {
						key = keys.next();
						if(!GamePlayerManager.getInstance().isOnline(key)) {
							continue;
						}
						player = GamePlayerManager.getInstance().getPlayer(key);
					} catch (Exception e) {
						logger.error(this.getName() + " 获取玩家信息出错  = ", e);
						break;
					}
					if (count == 0) {
						leftTime = forceLeftTimes.get(key) - 1;
						if (UPDATE_TIME >= RobberyConstant.ONE_MINIT_SECOND) {
							leftTime -= 1;
						}
						if (leftTime > 0) {
							forceLeftTimes.put(key, leftTime);
							checkAndUpdateTime(player, leftTime);
						} else if (leftTime == 0) {
							remogeList.add(key);
							long now = System.currentTimeMillis();
							if (tempForceMaps.get(key) != null && (tempForceMaps.get(key) + RobberyConstant.ONE_DAY_SECOND * 1200) < now) {	//强制
								TransitRobberyManager.getInstance().ready2EnterRobberyScene(player, true);
							}
							synchronized (tempForceMaps) {
								tempForceMaps.put(key, now);
							}
						} else {
							remogeList.add(key);
						}
					}
				}
				long et = System.currentTimeMillis() - startTime;
				if (et2 > 300) {
					try {
						TransitRobberyManager.logger.error("[执行强制拉人消耗时间][" + et + "ms][" + this.getName() + "][" + remogeList.size() + "]");
					} catch (Exception e) {
					}
				}
				try {
					Thread.sleep(heartBeatTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.error("天界心跳县城出错==", e);
				}
			} else {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.error("天劫现成出错===================", e);
				}
			}
		}
	}

	private void checkAndUpdateTime(Player player, int leftTime) {
		if (!player.isOnline()) {
			remogeList.add(player.getId());
			TransitRobberyEntityManager.getInstance().updateTransitRobberyEntity(player.getId(), RobberyConstant.UPDATE_FORCE_PULL_TIME, leftTime);
		}
		if (UPDATE_TIME >= RobberyConstant.ONE_MINIT_SECOND) {
			TransitRobberyEntityManager.getInstance().updateTransitRobberyEntity(player.getId(), RobberyConstant.UPDATE_FORCE_PULL_TIME, leftTime);
			UPDATE_TIME = 0;
		}
	}

}
