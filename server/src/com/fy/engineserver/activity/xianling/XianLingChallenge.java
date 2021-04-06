package com.fy.engineserver.activity.xianling;

import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_READ_TIMEBAR_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.message.XL_CATCH_SUCC_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.SimpleMonster;

public class XianLingChallenge {
	/** 挑战者id */
	private long playerId;
	/** 挑战者所在game */
	private Game game;
	/** 挑战结果 0挑战中 1捕捉成功,-1怪物死亡, -2死亡 ,-3下线上线 */
	private byte result = 0;
	/** 目标怪物categoryId */
	private int categoryId = -1;
	/** 目标怪物id */
	private long targetId = -1;
	/** 是否已刷怪 */
	private boolean refreshMonster = false;
	/** 进入前地图名 */
	private String preMapName;
	/** 进入前x坐标 */
	private int x;
	/** 进入前y坐标 */
	private int y;

	/** 捕捉成功后奖励的积分 */
	private int score;
	/** 捕捉次数 */
	private int catchTimes;
	/** 怪物比例 */
	private int scale;
	private long removeTime; // 从线程中剔除时间(改成不用了)

	public static final byte PLAYERDEATH = -2; // 玩家死亡
	public static final byte MONSTERKILLED = -1; // 怪物死亡
	public static final byte SUCCEED = 1; // 挑战成功
	public static final byte NOTONLINE = -3; // 玩家不在线
	public static final byte USETRANSPROP = -9; // 传送
	public static final byte TIMEEND = -10; // 副本超时
	public static final byte TRANSFER = -2 ; // 系统弹框拉人

	public XianLingChallenge(long playerId, Game game, byte result, int categoryId, long targetId, boolean refreshMonster, String preMapName, int x, int y, int scale) {
		super();
		this.playerId = playerId;
		this.game = game;
		this.result = result;
		this.categoryId = categoryId;
		this.targetId = targetId;
		this.refreshMonster = refreshMonster;
		this.preMapName = preMapName;
		this.x = x;
		this.y = y;
		this.scale = scale;
	}

	/**
	 * 角色死亡
	 * @param player
	 */
	public void notifyPlayerDead(Player player, byte type) {
		if (player.getId() == this.getPlayerId()) {
			player.send_HINT_REQ(Translate.失败, (byte) 5);
			setResult(type);
			//复活
			player.setHp(player.getMaxHP());
			player.setMp(player.getMaxMP());
			player.setState(Constants.STATE_STAND);
			PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte)0, "", player.getMaxHP(), player.getMaxMP());
			player.addMessageToRightBag(res);
			if (XianLingChallengeManager.logger.isDebugEnabled()) {
				XianLingChallengeManager.logger.debug("[仙灵挑战] [失败] [角色死亡或者下线] [" + player.getLogString() + "]");
			}
		}
	}

	/**
	 * 捕捉怪物
	 * @param p
	 * @param monster
	 */
	Random random = new Random();

	public void notifyMonsterCatched(XianLingChallengeThread thread, Player player, long endTime, Monster monster, int catchTimes) {
		if (targetId <= 0 || monster == null) {
			if (XianLingChallengeManager.logger.isDebugEnabled()) XianLingChallengeManager.logger.debug("[仙灵挑战-catchMonster] [异常] [目标id:" + targetId + "] [monster:" + monster + "][" + playerId + "]");
			return;
		}

		boolean result = false;

		// 根据在关卡内的剩余时间计算捕捉是否成功
		long breakTime = endTime - System.currentTimeMillis();
		if (breakTime < XianLingChallengeManager.barTime) {
			if (XianLingChallengeManager.logger.isDebugEnabled()) XianLingChallengeManager.logger.debug("[仙灵挑战-catchMonster] [倒计时即将结束,捕捉失败] [目标id:" + targetId + "] [monsterName:" + monster.getName() + "] [monsterColor:" + monster.getColor() + "] [" + playerId + "]");
			result = false;
		}

		// 根据怪物剩余血量计算捕捉是否成功
		float hpRate = (float) monster.getHp() / monster.getMaxHP();
		if (hpRate * 100 <= 1) {
			int randomRate = XianLingManager.instance.catchMonsterRates[monster.getColor()];
			if (monster.getColor() < 4) {
				if (XianLingChallengeManager.logger.isWarnEnabled()) XianLingChallengeManager.logger.warn("[仙灵挑战-catchMonster] [橙色以下直接捕捉成功] [目标id:" + targetId + "] [monsterName:" + monster.getName() + "] [monsterColor:" + monster.getColor() + "] [hpRate:" + hpRate + "] [" + playerId + "]");
				result = true;
			} else {
				if (random.nextInt(100) + 1 <= randomRate) {
					if (XianLingChallengeManager.logger.isWarnEnabled()) XianLingChallengeManager.logger.warn("[仙灵挑战-catchMonster] [捕捉成功] [目标id:" + targetId + "] [monsterName:" + monster.getName() + "] [monsterColor:" + monster.getColor() + "] [hpRate:" + hpRate + "] [" + playerId + "]");
					result = true;
				}
			}
		} else {
			// result = ((100%*3-当前HP百分比*2)/100%*3)/(10*(怪物颜色+1)/捕捉次数*1.5)
			double temp = ((1 * 3 - hpRate * 2) / 1 * 3) / (10 * (monster.getColor() + 1) / catchTimes * 1.5);
			if (temp > 1) {
				if (XianLingChallengeManager.logger.isWarnEnabled()) XianLingChallengeManager.logger.warn("[仙灵挑战-catchMonster] [捕捉成功] [目标id:" + targetId + "] [monsterColor:" + monster.getColor() + "] [hpRate:" + hpRate + "] [" + playerId + "]");
				result = true;

			}
		}
		CatchTimeBar timeBar = new CatchTimeBar(this, result, player, monster.getSpriteCategoryId());
		player.getTimerTaskAgent().createTimerTask(timeBar, XianLingChallengeManager.barTime, TimerTask.type_仙灵捕捉);
		NOTICE_CLIENT_READ_TIMEBAR_REQ timebar_REQ = new NOTICE_CLIENT_READ_TIMEBAR_REQ(GameMessageFactory.nextSequnceNum(), XianLingChallengeManager.barTime, Translate.捕捉);
		player.addMessageToRightBag(timebar_REQ);
		// 成功则读条满，不成功则读条中断,随机出一个打断读条的时间， 这里好好研究下
		if (!result) {
			long randomTime = random.nextInt(XianLingChallengeManager.barTime - 1600) + 500;
			breakTime = breakTime > randomTime ? randomTime : breakTime;
			if (XianLingChallengeManager.logger.isInfoEnabled()) XianLingChallengeManager.logger.info("[仙灵挑战-catchMonster] [捕捉失败打断读条] [目标id:" + targetId + "] [读条时间:" + breakTime + "] [monsterColor:" + monster.getColor() + "] [hpRate:" + hpRate + "] [" + playerId + "]");
			thread.timebarMaps.put(player.getTimerTaskAgent(), System.currentTimeMillis() + breakTime);
		}else{
			XianLingChallengeManager.instance.catchMonster.put(playerId, targetId);
			if (XianLingChallengeManager.logger.isWarnEnabled()) XianLingChallengeManager.logger.warn("[仙灵挑战-catchMonster] [捕捉成功等待加积分] [目标id:" + targetId + "] [monsterColor:" + monster.getColor() + "] [hpRate:" + hpRate + "] [" + playerId + "]");
		}
	}

	/**
	 * 杀死怪物
	 * @param monster
	 */
	public void notifyMonsterKilled(SimpleMonster monster) {
		if (targetId <= 0) {
			if (XianLingChallengeManager.logger.isDebugEnabled()) XianLingChallengeManager.logger.debug("[仙灵挑战-notifyMonsterKilled] [异常] [目标id错误:" + targetId + "][" + playerId + "]");
			return;
		}
		if (monster.getSpriteId() == targetId) {
			if (result != SUCCEED) {
				setResult(MONSTERKILLED);
				if (XianLingChallengeManager.logger.isDebugEnabled()) XianLingChallengeManager.logger.debug("[仙灵挑战] [失败] [杀死目标怪物] [" + monster + "]");
			}

		}
	}

	public boolean 回城(Player p) {

		try {
			Game currentGame = p.getCurrentGame();
			if (currentGame == null) {
				XianLingChallengeManager.logger.info("[玩家当前game为空][" + p.getLogString() + "]");
				return false;
			}
			if (currentGame != null && !currentGame.gi.name.equals(game.gi.name)) {
				XianLingChallengeManager.logger.info("[玩家已经不再这个区域了，不需要在回程了][" + p.getLogString() + "]");
				return true;
			}
			String mapName = TransportData.getMainCityMap(p.getCountry());
			int x = p.getResurrectionX();
			int y = p.getResurrectionY();
			if (preMapName != null && !preMapName.isEmpty()) {
				mapName = preMapName;
				x = this.x;
				y = this.y;
			}
			p.setHp(p.getMaxHP() / 2);
			p.setMp(p.getMaxMP() / 2);
			p.setState(Player.STATE_STAND);
			p.notifyRevived();
			currentGame.transferGame(p, new TransportData(0, 0, 0, 0, mapName, x, y), true);
			PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, "", p.getHp(), p.getMp());
			p.addMessageToRightBag(res);
			XianLingChallengeManager.logger.warn("[仙灵挑战] [回城] [成功] [" + p.getLogString() + "]");
			return true;
		} catch (Exception e) {
			if (p != null) {
				XianLingChallengeManager.logger.warn("[仙灵挑战] [回城] [异常][" + p.getLogString() + "]", e);
			} else {
				XianLingChallengeManager.logger.error("[仙灵挑战] [传送出副本出错] ", e);
			}
			return false;
		}

	}

	public void refreshMonster(Player player) {
		try {
			Monster monster = XianLingChallengeManager.instance.refreashMonster(game, targetId, scale);
			if (monster != null) {
				// targetId = monster.getId();
				score = XianLingManager.instance.scores[monster.getColor()];
				refreshMonster = true;
				monster.setOwner(player);
				if (XianLingChallengeManager.logger.isInfoEnabled()) XianLingChallengeManager.logger.info("[仙灵挑战]" + player.getLogString() + " [刷出boss] [目标bossId:" + targetId + "] [boss模板id:" + monster.getSpriteCategoryId() + "]");
			}
		} catch (Exception e) {
			if (XianLingChallengeManager.logger.isInfoEnabled()) XianLingChallengeManager.logger.info("[仙灵挑战] [刷boss异常] [目标bossId:" + targetId + "]", e);
			e.printStackTrace();
		}

	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		if (this.result == SUCCEED && (result == PLAYERDEATH || result == MONSTERKILLED)) {
			return;
		}
		this.result = result;
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(this.getPlayerId());
			if (result == SUCCEED) { // 只有胜利的时候通知
				PlayerXianLingData xianLingData = player.getXianlingData();
				if (xianLingData != null) {
					if (result == 1) {
						// 挑战成功加积分
						int scoreBuff = player.getScoreBuff();
						if (scoreBuff > 0) {
							score = (int) (score + (float) score / 100 * scoreBuff);
						}
						if(XianLingChallengeManager.instance.catchMonster.containsKey(this.getPlayerId()) && XianLingChallengeManager.instance.catchMonster.get(this.getPlayerId()) == targetId){
							XianLingManager.instance.addScore(player, score);
							XianLingChallengeManager.instance.catchMonster.remove(this.getPlayerId());
							player.addMessageToRightBag(new XL_CATCH_SUCC_RES(GameMessageFactory.nextSequnceNum(), score, true));
							if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [副本结束] [通知客户端弹成功] [result=" + result + "] [playerId : " + this.getPlayerId() + "] ");
						}else{
							if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [副本结束] [拦截重复加积分] [result=" + result + "] [playerId : " + this.getPlayerId() + "] ");
						}
						
						if (XianLingManager.instance.timedTaskMap.containsKey(xianLingData.getTaskId())) {
							TimedTask task = XianLingManager.instance.timedTaskMap.get(xianLingData.getTaskId());
							// 完成限时任务
							if (xianLingData.getTaskState() == (byte) 1 && task.getMonsterCategoryId() == categoryId) {
								xianLingData.setTakePrize(false);
								xianLingData.setTaskState((byte) 2);
								if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [完成限时任务] [" + player.getLogString() + "] [taskId:" + task.getTaskId() + "] [categoryId:" + categoryId + "]");
							}
						}
					}
				}
				player.sendError(Translate.捕捉成功);
				Monster monster = MemoryMonsterManager.getMonsterManager().getMonster(targetId);
				if (monster != null) {
					monster.setHp(0);
				}
			} else if (result == PLAYERDEATH || result == MONSTERKILLED || result == TIMEEND) {
				player.addMessageToRightBag(new XL_CATCH_SUCC_RES(GameMessageFactory.nextSequnceNum(), 0, false));
				if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵挑战] [副本结束] [通知客户端弹] [result=" + result + "] [playerId : " + this.getPlayerId() + "] ");
			}else if(result == NOTONLINE){
				回城(player);
			}
			player.xl_chanllengeFlag = result;
			if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵挑战] [result=" + result + "] [playerId : " + this.getPlayerId() + "] ");
		} catch (Exception e) {
			if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵挑战] [结束异常] [playerId : " + this.getPlayerId() + "] ", e);
		}
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public String getPreMapName() {
		return preMapName;
	}

	public void setPreMapName(String preMapName) {
		this.preMapName = preMapName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isRefreshMonster() {
		return refreshMonster;
	}

	public void setRefreshMonster(boolean refreshMonster) {
		this.refreshMonster = refreshMonster;
	}

	public int getCatchTimes() {
		return catchTimes;
	}

	public void setCatchTimes(int catchTimes) {
		this.catchTimes = catchTimes;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public long getRemoveTime() {
		return removeTime;
	}

	public void setRemoveTime(long removeTime) {
		this.removeTime = removeTime;
	}

}
