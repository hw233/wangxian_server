package com.fy.engineserver.activity.fairyBuddha.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.SimpleMonster;

public class FairyChallengeThread extends Thread {
	
	private List<FariyChallenge> gameList = new Vector<FariyChallenge>();
	
	public static long heartBeat = 100;
	public static boolean isStart = true;
	public static int heartBeatWarnTime = 150;	//毫秒
	public static int refreshBossTime = 2000;	//玩家进入地图后多少毫秒后刷出任务boss
	/** key=playerId value=startTime */
	public Map<Long, Long> timeMaps = new java.util.Hashtable<Long, Long>();
	
	@Override
	public void run() {
		if(FairyChallengeManager.logger.isWarnEnabled()) {
			FairyChallengeManager.logger.warn("[仙尊挑战] [线程启动成功] [线程name:"+this.getName()+"]");
		}
		while(isStart) {
			long startTime = System.currentTimeMillis();
			List<FariyChallenge> needRemoveList = new ArrayList<FariyChallenge>();
			Player player = null;
			FariyChallenge[] aaa = gameList.toArray(new FariyChallenge[gameList.size()]);
			for(FariyChallenge fc : aaa) {
				try {
					fc.getGame().heartbeat();
					try {
						player = GamePlayerManager.getInstance().getPlayer(fc.getPlayerId());
						if(player.isDeath() && fc.getResult() != FariyChallenge.DEATH) {
							this.notifyPlayerDead(player, FariyChallenge.DEATH);
						} else if (!player.isOnline() && fc.getResult() != FariyChallenge.NOTONLINE) {
							this.notifyPlayerDead(player, FariyChallenge.NOTONLINE);
						}
					} catch (Exception e) {
						FairyChallengeManager.logger.error("[仙尊挑战] [获取player异常] [playerId : " + fc.getPlayerId() + "]", e);
					}
					if(timeMaps.get(fc.getPlayerId()) != 0) {
						long time = timeMaps.get(fc.getPlayerId());
						if(fc.getTargetId() <= 0 && (System.currentTimeMillis() - time) >= refreshBossTime) {
							fc.refreshMonster();
						}
					} else {		//map中没有直接刷boss
						if(fc.getTargetId() <= 0) {
							fc.refreshMonster();
						}
					}
					if(fc.getResult() == FariyChallenge.USETRANSPROP || fc.getResult() == FariyChallenge.SUCCEED || fc.getResult() == FariyChallenge.DEATH ) {
						needRemoveList.add(fc);
						if(FairyChallengeManager.logger.isInfoEnabled()) {
							FairyChallengeManager.logger.info("[仙尊挑战] [加入到删除列表中] ["+fc+"]");
						}
					}
				} catch (Exception e) {
					FairyChallengeManager.logger.error("[仙尊挑战] [game心跳异常] [playerId : " + fc.getPlayerId() + "]", e);
				}
			}
			for(FariyChallenge fc : needRemoveList) {
				boolean result = gameList.remove(fc);
				timeMaps.remove(fc.getPlayerId());
				if(FairyChallengeManager.logger.isDebugEnabled()) {
					FairyChallengeManager.logger.debug("[仙尊挑战] [玩家挑战结束删除线程,删除结果:"+result+"] ["+fc+"]");
				}
			}
			long endTime = System.currentTimeMillis();
			if(endTime - startTime > heartBeatWarnTime) {
				if(FairyChallengeManager.logger.isWarnEnabled()) {
					FairyChallengeManager.logger.warn("[仙尊挑战] [线程心跳时间超时] [执行心跳时间:" + (endTime - startTime) + " 毫秒]");
				}
			}
			player = null;
			needRemoveList = null;
			try {
				Thread.sleep(heartBeat);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(FairyChallengeManager.logger.isWarnEnabled()) {
			FairyChallengeManager.logger.warn("[仙尊挑战] [线程停止工作] [isStart :" + isStart + "]");
		}
	}
	
	public void cleanAllChallenger() {
		FariyChallenge[] aaa = gameList.toArray(new FariyChallenge[gameList.size()]);
		for(FariyChallenge fc : aaa) {
			if(FairyChallengeManager.logger.isDebugEnabled()) {
				FairyChallengeManager.logger.debug("[仙尊挑战] [到时间清除所有挑战者] [" + fc.getPlayerId() + "][" + fc.getResult() + "] [线程name:" + this.getName() + "]");
			}
			fc.setResult(FariyChallenge.USETRANSPROP);
		}
	}
	
	public void notifyPlayerUseTransProp(Player player) {
		FariyChallenge[] aaa = gameList.toArray(new FariyChallenge[gameList.size()]);
		for(FariyChallenge fc : aaa) {
			if(player.getId() == fc.getPlayerId()) {
				if(FairyChallengeManager.logger.isDebugEnabled()) {
					FairyChallengeManager.logger.debug("[仙尊挑战] [找到FariyChallenge,更新result为-9] [" + player.getLogString() + "][" + fc.getGame() + "]");
				}
				fc.setResult(FariyChallenge.USETRANSPROP);
			}
		}
	}
	
	public Game getChallengeGmae(Player player) {
		FariyChallenge[] aaa = gameList.toArray(new FariyChallenge[gameList.size()]);
		for(FariyChallenge fc : aaa) {
			if(player.getId() == fc.getPlayerId()) {
				if(FairyChallengeManager.logger.isDebugEnabled()) {
					FairyChallengeManager.logger.debug("[仙尊挑战] [找到game] [" + player.getLogString() + "][" + fc.getGame() + "]");
				}
				return fc.getGame();
			}
		}
		return null;
	}
	public byte getChallengeResult(Player player) {
		FariyChallenge[] aaa = gameList.toArray(new FariyChallenge[gameList.size()]);
		for(FariyChallenge fc : aaa) {
			if(player.getId() == fc.getPlayerId()) {
				return fc.getResult();
			}
		}
		return 0;
	}
	/**
	 *  玩家死亡
	 * @param player
	 */
	public void notifyPlayerDead(Player player, byte type) {
		if(gameList == null || gameList.size() <= 0) {
			return;
		}
		FariyChallenge[] list = gameList.toArray(new FariyChallenge[gameList.size()]);
		for(FariyChallenge fc : list) {
			fc.notifyPlayerDead(player, type);
		}
	}
	
	/**
	 * 杀死怪物
	 * @param monster
	 */
	public void notifyMonsterKilled(SimpleMonster monster) {
		if(gameList == null || gameList.size() <= 0) {
			return;
		}
		FariyChallenge[] list = gameList.toArray(new FariyChallenge[gameList.size()]);
		for(FariyChallenge fc : list) {
			fc.notifyMonsterKilled(monster);
		}
	}
	/**
	 * 判断player是否存在于此线程中
	 * @param player
	 * @return
	 */
	public boolean isPlayerAtThread(Player player) {
		return isPlayerAtThread(player.getId());
	}
	public boolean isPlayerAtThread(long playerId) {
		if(gameList == null || gameList.size() <= 0) {
			return false;
		}
		FariyChallenge[] list = gameList.toArray(new FariyChallenge[gameList.size()]);
		for(FariyChallenge fc : list) {
			if(fc.getPlayerId() == playerId) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 开始挑战
	 * @param game
	 * @param player
	 * @param monster
	 */
	public void notifyStartChallenge(Game game, Player player) {
		try {
			Game currGame = player.getCurrentGame();
			FariyChallenge fc = new FariyChallenge(player.getId(), game, (byte) 0, player.getCurrSoul().getSoulType(), currGame.gi.name, player.getX(), player.getY(), player.getCurrSoul().getCareer());
			currGame.transferGame(player, new TransportData(0, 0, 0, 0, game.gi.name, FairyChallengeManager.bornPoint[0], FairyChallengeManager.bornPoint[1]));
			if(!gameList.contains(fc)) {
				gameList.add(fc);
			}
			if(player.isIsUpOrDown() /*&& player.isFlying()*/){
				player.downFromHorse(true);
			}
			if(player.getActivePetId() > 0){			//宠物本来在出战状态强制回收
				player.packupPet(false);
			}
			player.chanllengeFlag = 2;
			timeMaps.put(player.getId(), System.currentTimeMillis());
		} catch (Exception e) {
			
		}
	}

	public List<FariyChallenge> getGameList() {
		return gameList;
	}

	public void setGameList(List<FariyChallenge> gameList) {
		this.gameList = gameList;
	}

}
