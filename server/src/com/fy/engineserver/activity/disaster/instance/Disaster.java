package com.fy.engineserver.activity.disaster.instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.activity.disaster.DisasterConstant;
import com.fy.engineserver.activity.disaster.DisasterManager;
import com.fy.engineserver.activity.disaster.module.DisasterBaseModule;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.DISASTER_END_INFO_REQ;
import com.fy.engineserver.message.DISASTER_RANK_INFO_RES;
import com.fy.engineserver.message.DISASTER_START_INFO_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.effect.LinearEffectSummoned;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPCManager;

/**
 * 金猴天灾
 */
public class Disaster {
	/** 玩家游戏数据 */
	private List<DisasterPlayer> playerMap = new ArrayList<DisasterPlayer>();
	/** 上次心跳时间 */
	public long lastHeartbeatTime;
	/** 当前金猴天灾game */
	private Game game;
	/** 当前阶段 */
	private DisasterStep currentStep;
	/** 上次改变状态时间 */
	private long lastChangeStepTime;
	/** 开始时间 */
	public long startTime;
	/** 上次刷新经验宝箱时间 */
	private long lastRefreshBoxTime;
	
	public Random ran = new Random(System.currentTimeMillis());
	
	public Disaster() {
		currentStep = DisasterStep.STEP_READY;
		lastChangeStepTime = System.currentTimeMillis();
	}
	
	public boolean canRefreshBoss() {
		long now = System.currentTimeMillis();
		if (currentStep != null && currentStep != DisasterStep.STEP_READY) {
			if (currentStep == DisasterStep.STEP_FIRST) {
				if (now - lastChangeStepTime >= 10000) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
	public void heartbeat(long now) {
		game.heartbeat();
		lastHeartbeatTime = now;
		try {
			for (DisasterPlayer dp : playerMap) {
				if (dp.isLeaveGame()) {
					continue;
				}
				if (dp.getLeftHp() <= 0) {
					this.notifyPlayerRelive(dp, now);
				}
//				this.checkOnline(dp);
			}
		} catch (Exception e) {
			if (DisasterManager.logger.isInfoEnabled()) {
				DisasterManager.logger.info("[复活死亡玩家] [异常]", e);
			}
		}
	}
	
	public void checkOnline(DisasterPlayer dp) {
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(dp.getPlayerId());
			if (!player.isOnline()) {
				dp.setLeaveGame(true);
				if (DisasterManager.logger.isInfoEnabled()) {
					DisasterManager.logger.info("[检测玩家是否在线] [玩家已经不在线了] [" + player.getLogString() + "]");
				}
			}
		} catch (Exception e) {
			DisasterManager.logger.warn("[检查玩家是否下线] [异常] [" + dp.toString() + "]", e);
		}
	}
	
	public String getLogString() {
		StringBuffer sb = new StringBuffer();
		int index = 1;
		for (DisasterPlayer dp : playerMap) {
			sb.append("rank:" + (index++) + dp.toString());
		}
		return sb.toString();
	}
	
	/**
	 * 检查玩家是否已经下线
	 */
	public void checkPlayerOffline() {
		
	}
	/**
	 * 初始化地图
	 * @throws Exception 
	 */
	public void initGame() throws Exception {
		game = DevilSquareManager.instance.createNewGame(DisasterManager.getInst().baseModule.getMapName(),"2");
		
	}
	
	public boolean isPlayerDead(Player player) {
		DisasterPlayer dp = null;
		DisasterPlayer[] dps = playerMap.toArray(new DisasterPlayer[0]);
		for (int i=0; i<dps.length; i++) {
			if (dps[i].getPlayerId() == player.getId()) {
				dp = dps[i];
				break;
			}
		}
		if (dp == null) {
			return false;
		}
		if (dp.getLeftHp() <= 0 ) {
			return true;
		}
		return false;
	}
	public boolean isPlayerInDisaster(Player player) {
		DisasterPlayer dp = null;
		DisasterPlayer[] dps = playerMap.toArray(new DisasterPlayer[0]);
		for (int i=0; i<dps.length; i++) {
			if (dps[i].getPlayerId() == player.getId()) {
				dp = dps[i];
				break;
			}
		}
		if (dp == null || dp.isEnd() || currentStep == null) {
			return false;
		}
		if (dp.getLeftHp() <= 0 ) {
			return true;
		}
		return false;
	}
	
	public Game findPlayerGame(Player player) {
		DisasterPlayer dp = null;
		DisasterPlayer[] dps = playerMap.toArray(new DisasterPlayer[0]);
		for (int i=0; i<dps.length; i++) {
			if (dps[i].getPlayerId() == player.getId()) {
				dp = dps[i];
				break;
			}
		}
		if (dp == null) {
			return null;
		}
		if (currentStep == null ||  !currentStep.isCanEnter()) {
			if (!dp.isEnd()) {
				long[] rr = dp.reward(5, 5, true);
				try {
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[0], new int[0], Translate.空岛提前退出提示, String.format(Translate.空岛提前退出提示, rr[0]+""), 0L, 0L, 0L, "空岛大冒险");
				} catch (Exception e) {
					
				}
			}
			dp.setEnd(true);
			if (DisasterManager.logger.isDebugEnabled()) {
				DisasterManager.logger.debug("[获取玩家金猴天灾场景] [失败] [活动已开始] [" + player.getLogString() + "] [" + dp + "]");
			}
			return null;
		}
		if (dp.isLeaveGame() || dp.isEnd()) {
			if (DisasterManager.logger.isInfoEnabled()) {
				DisasterManager.logger.info("[获取玩家金猴天灾场景] [失败] [玩家进入后掉线或者离开了场景] [" + player.getLogString() + "] [" + dp + "]");
			}
			return null;
		}
		return game;
	}
	
	public static boolean damageLog = false;
	
	/**
	 * 玩家受到伤害
	 * @param player
	 * @param damage
	 * @return
	 */
	public boolean causeDamage(Player player, int damage) {
		DisasterPlayer dp = this.findDp(player);
		if (dp == null || dp.isLeaveGame() || dp.isEnd()) {
			return false;
		}
		if (this.currentStep != null && dp.getLeftHp() > 0) {
			int hp = dp.getLeftHp() - damage;
			if (hp <= 0) {
				hp = 0;
			}
			dp.setLeftHp(hp);
			if (hp <= 0) {
				//记录死亡次数并回复血量
				dp.setDeadTimes(dp.getDeadTimes() + 1);
				dp.setLastDeadTime(System.currentTimeMillis());
				Collections.sort(playerMap);			
				try {
					List<LinearEffectSummoned> list = game.getLinearSummoned();
					if (list != null && list.size() > 0) {
						for (LinearEffectSummoned e : list) {
							e.notifyPlayerDead(player);
						}
					}
				} catch (Exception e) {
					if (DisasterManager.logger.isInfoEnabled()) {
						DisasterManager.logger.info("[玩家死亡] [通知删除召唤物] [异常] [" + player.getLogString() + "]", e);
					}
				}
				player.getTimerTaskAgent().notifyMoved();
			}
			NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, player.getId(), (byte) Event.HP_DECREASED_SPELL, damage);
			player.addMessageToRightBag(req);
			player.getTimerTaskAgent().notifyBeAttacked();
			this.aroundChange();
		}
		if (damageLog && DisasterManager.logger.isDebugEnabled()) {
			DisasterManager.logger.debug("[玩家受到伤害] [" + player.getLogString() + "] [damage:" + damage + "] [leftHp:" + dp.getLeftHp() + "]");
		}
		return true;
	}
	public boolean notifyAroundChange(Player player) {
		DisasterPlayer dp = this.findDp(player);
		if (dp == null || dp.isLeaveGame() || dp.isEnd()) {
			return false;
		}
		this.aroundChange();
		return true;
	}
	
	public boolean noticePlayerTrans2OtherGame(Player player, String gameName) {
		DisasterPlayer dp = this.findDp(player);
		if (dp == null || dp.isLeaveGame() || dp.isEnd()) {
			return false;
		}
		dp.setEnd(true);
		long[] rr = dp.reward(5, 5, true);
		try {
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[0], new int[0], Translate.空岛提前退出提示, String.format(Translate.空岛提前退出提示, rr[0]+""), 0L, 0L, 0L, "空岛大冒险");
		} catch (Exception e) {
			
		}
		return true;
	}
	
	public void notifyPlayerRelive(DisasterPlayer dp, long now) {
		if (dp.getLeftHp() <= 0 && dp.getLastReliveTime() < dp.getLastDeadTime() && (now-dp.getLastDeadTime()) >= DisasterConstant.RELIVE_INTEVAL) {
			dp.setLeftHp(DisasterConstant.BASEHP);
			dp.setLastReliveTime(now);
			this.aroundChange();
		}
	}
	/**
	 * 玩家复活
	 * @param player
	 * @return
	 */
	public boolean notifyPlayerRelive(Player player, long now) {
		DisasterPlayer dp = findDp(player);
		if (dp == null) {
			return false;
		}
		dp.setLeftHp(DisasterConstant.BASEHP);
		dp.setLastReliveTime(now);
		this.aroundChange();
		return true;
	}
	
	public DisasterPlayer findDp(Player player) {
		DisasterPlayer dp = null;
		DisasterPlayer[] dps = playerMap.toArray(new DisasterPlayer[0]);
		for (int i=0; i<dps.length; i++) {
			if (dps[i].getPlayerId() == player.getId()) {
				dp = dps[i];
				break;
			}
		}
		return dp;
	}
	
	/**
	 * 玩家进入游戏
	 * @param player
	 */
	public boolean notifyPlayerEnter(Player player) {
		synchronized (playerMap) {
			Game currentGame = player.getCurrentGame();
			DisasterPlayer dp = null;
			DisasterPlayer[] dps = playerMap.toArray(new DisasterPlayer[0]);
			for (int i=0; i<dps.length; i++) {
				if (dps[i].getPlayerId() == player.getId()) {
					dp = dps[i];
					break;
				}
			}
			if (dp != null) {
				if (DisasterManager.logger.isInfoEnabled()) {
					DisasterManager.logger.info("[玩家进入金猴天灾场景] [失败] [之前已进入] [" + player.getLogString() + "] [玩家当前地图:" + (currentGame != null ? currentGame.gi.name : "null") + "]");
				}
				return false;
			}
			if (player.isShouStatus()) {
				player.resetShouStat("进入空岛");
			}
			int[] oldPoint = new int[]{player.getX(), player.getY()};
			Integer[] points = DisasterManager.getInst().baseModule.getPlayerBornPoint(player.random);
			currentGame.transferGame(player, new TransportData(0, 0, 0, 0, game.gi.name, points[0], points[1]));
			playerMap.add(new DisasterPlayer(player.getId(), DisasterConstant.BASEHP, currentGame.gi.name, oldPoint));
			this.aroundChange();
			long now = System.currentTimeMillis();
			long st = DisasterStep.STEP_READY.getDurationTime() - (now - startTime); 
			DISASTER_START_INFO_REQ req = new DISASTER_START_INFO_REQ(GameMessageFactory.nextSequnceNum(), DisasterManager.getInst().getTranslate("start_des"), 
					st);
			player.addMessageToRightBag(req);
			
			if (DisasterManager.logger.isWarnEnabled()) {
				DisasterManager.logger.warn("[玩家进入金猴天灾游戏] [成功] [" + player.getLogString() + "]");
			}
			return true;
		}
	}
	/**
	 * 检测是否进入下一阶段
	 * @return
	 */
	public void checkStepChange(long now) {
		if (currentStep != null) {
			if ((now - lastChangeStepTime) >= currentStep.getDurationTime()) {
				lastRefreshBoxTime = now;
				currentStep = currentStep.getNextStep();
				lastChangeStepTime = now;
				if (currentStep == null) {
					disasterEnd();
					if (DisasterManager.logger.isDebugEnabled()) {
						DisasterManager.logger.debug("[结束] [" + currentStep + "] [" + this.getLogString() + "]");
					}
				} else {
					currentStep.refreshNPC(game);
					if (DisasterManager.logger.isDebugEnabled()) {
						DisasterManager.logger.debug("[刷新NPC] [" + currentStep.getClass().getName() + "] [" + this.getLogString() + "]");
					}
				}
			}
			if (currentStep != null && currentStep.isNeedRefreshBox()) {
				this.notifyRefreashBox(now);
			}
		} else if ((now - lastChangeStepTime) >= DisasterConstant.END_WAIT_TIME) {		//游戏结束一定时间后将所有玩家传送出场景
			for (DisasterPlayer dp : playerMap) {
				this.回城(dp);
			}
		}
		//刷新经验桃NPC逻辑
		
	}
	public void notifyRefreashBox(long now) {
		if ((now - lastRefreshBoxTime) <= DisasterConstant.REFRESH_BOX_INTEVAL) {
			return ;
		}
		lastRefreshBoxTime = now;
		DisasterBaseModule module = DisasterManager.getInst().baseModule;
		List<Integer> list = new ArrayList<Integer>();
		int len = module.getBoxPoints().size();
		Article at = ArticleManager.getInstance().getArticleByCNname(DisasterConstant.BOX_ARTICLE_CNNNAME);
		for(int i=0; i<DisasterConstant.REFRESH_BOX_NUM; i++) {
			int tempIndex = ran.nextInt(len);
			if (list.contains(tempIndex)) {
				for (int j=0; j<len; j++) {
					if (!list.contains(j)) {
						tempIndex = j;
						break;
					}
				}
			}
			list.add(tempIndex);
			FlopCaijiNpc npc = getFlopCaijiNpc(DisasterConstant.BOX_NPCID, at, DisasterConstant.BOX_CLEAN_TIME);
			npc.setX(module.getBoxPoints().get(tempIndex)[0]);
			npc.setY(module.getBoxPoints().get(tempIndex)[1]);
			game.addSprite(npc);
			if(DisasterManager.logger.isDebugEnabled()) {
				DisasterManager.logger.debug("[金猴天灾] [刷箱子] [坐标:" + npc.getX() + " && " + npc.getY() + "]");
			}
		}
//		if(!"".equals(boxName)) {
//			String tempStr = Translate.translateString(Translate.XX宝箱刷新了, new String[][] {{Translate.STRING_1, boxName}});
//			noticeAllPlayerSthRefreash(tempStr);
//		}
	}
	

	private FlopCaijiNpc getFlopCaijiNpc(int categoryId, Article article, int boxCleanTime) {
		ArticleEntity ae = null;
		try {
			ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.活动, null, article.getColorType());
//			ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.活动, null, article.getColorType(), 1, true);
		} catch (Exception e) {
			DisasterManager.logger.error("[金猴天灾刷宝箱出错]", e);
			return null;
		}
		NPCManager nm = MemoryNPCManager.getNPCManager();
		FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
		fcn.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		fcn.setAllCanPickAfterTime(100);
		fcn.setEndTime(SystemTime.currentTimeMillis() + boxCleanTime*1000);
		ResourceManager.getInstance().getAvata(fcn);
		fcn.setArticle(article);
		fcn.setAe(ae);
		fcn.setNameColor(ArticleManager.getColorValue(article, article.getColorType()));
		fcn.setCount(1);
		fcn.availiableDistance = DisasterConstant.availiableDistance;
		fcn.setName(article.getName());
		fcn.setAvataRace("NPC");
		fcn.setAvataSex(DisasterConstant.BOX_AVATA);
		ResourceManager.getInstance().getAvata(fcn);
		return fcn;
	}
	/**
	 * 金猴天灾活动游戏结束，弹窗结算框及发放奖励
	 */
	public void disasterEnd() {
		game.removeAllNpc();
		Collections.sort(playerMap);	
		List<DisasterPlayer> tempList = this.getAlivePlayers();
		int len = tempList.size();
		long[] playerId = new long[len];
		String[] playerNames = new String[len];
		int[] deadTimes = new int[len];
		long[] rewardExp = new long[len];
		long[] rewardAeId = new long[0];
		synchronized (playerMap){	//发放奖励
			for (int i=0; i<len; i++) {
				long[] rr = tempList.get(i).reward(playerMap.size(), (i+1));
				playerId[i] = tempList.get(i).getPlayerId();
				playerNames[i] = tempList.get(i).getPlayerNames();
				deadTimes[i] = tempList.get(i).getDeadTimes();
				rewardExp[i] = rr[0];
				if (rr[1] > 0) {
					rewardAeId = Arrays.copyOf(rewardAeId, rewardAeId.length+1);
					rewardAeId[rewardAeId.length-1] = rr[1];
				}
			}
		}
		{	//推送结束界面给客户端1
			for (int i=0; i<len; i++) {
				try {
					if (tempList.get(i).isLeaveGame()) {
						continue;
					}
					Player player = GamePlayerManager.getInstance().getPlayer(tempList.get(i).getPlayerId());
					if (!DisasterManager.getInst().isPlayerInGame(player)) {
						continue;
					}
					DISASTER_END_INFO_REQ req = new DISASTER_END_INFO_REQ(GameMessageFactory.nextSequnceNum(), playerId, 
							playerNames, deadTimes, rewardExp, rewardAeId);
					player.addMessageToRightBag(req);
				} catch (Exception e) {
					DisasterManager.logger.warn("[发送活动结束面板] [异常] ", e);
				}
			}
		}
	}
	
	public boolean leaveGame(Player player) {
		DisasterPlayer dp = this.findDp(player);
		if (dp == null || dp.isLeaveGame() || dp.isEnd()) {
			return false;//tianzai
		}
		this.回城(dp);
		return true;
	}
	
	public boolean 回城(DisasterPlayer dp){
		try{
			if (dp.isEnd()) {
				return false;
			}
			dp.setEnd(true);
			Player p = GamePlayerManager.getInstance().getPlayer(dp.getPlayerId());
			Game currentGame = p.getCurrentGame();
			if(currentGame != null && !currentGame.gi.name.equals(game.gi.name)){
				if(DisasterManager.logger.isDebugEnabled()) {
					DisasterManager.logger.debug("[玩家已经不再这个区域了，不需要在回程了]");
				}
				return true;
			}
			String mapName = dp.getGameMap();
			int x = dp.getPoint()[0];
			int y = dp.getPoint()[1];
			game.transferGame(p, new TransportData(0, 0, 0, 0, mapName, x, y), true);
			DisasterManager.logger.warn("[金猴天灾活动] [回城] [成功] ["+p.getLogString()+"]");
			return true;
		}catch(Exception e){
			DisasterManager.logger.error("[金猴天灾活动出错] ", e);
			return false;
		}
	}
	
	/**
	 * 是否可以将场景删掉
	 * @param now
	 * @return
	 */
	public boolean needDelete(long now) {
		if (currentStep == null && (now - lastChangeStepTime) >= DisasterConstant.END_WAIT_TIME && game.getPlayers().size() <= 0) {
			return true;
		}
		return false;
	}
	public List<DisasterPlayer> getAlivePlayers() {
		List<DisasterPlayer> temp = new ArrayList<DisasterPlayer>();
		for (int i=0; i<playerMap.size(); i++) {
			if (playerMap.get(i).isEnd()) {
				continue;
			}
			temp.add(playerMap.get(i));
		}
		return temp;
	}
	
	public static boolean 防止玩家进入其他场景 = true;
	
	/**
	 * 发送排行变化
	 */
	public void aroundChange() {
		DisasterPlayer[] dps = this.getAlivePlayers().toArray(new DisasterPlayer[0]);
		if (DisasterManager.logger.isDebugEnabled()) {
			DisasterManager.logger.debug("[发送玩家状态变化] [成功] [" + Arrays.toString(dps) + "]");
		}
		
		for (int i=0; i<dps.length; i++) {
			try {
				DISASTER_RANK_INFO_RES req = new DISASTER_RANK_INFO_RES(GameMessageFactory.nextSequnceNum(), dps);
				if (dps[i].isEnd()) {
					continue;
				}
				Player player = GamePlayerManager.getInstance().getPlayer(dps[i].getPlayerId());
				if (DisasterManager.getInst().isPlayerInGame(player)) {
					player.addMessageToRightBag(req);
					if (DisasterManager.logger.isDebugEnabled()) {
						DisasterManager.logger.debug("[通知玩家环境变化] [" + player.getLogString() + "]");
					}
				}
				
			} catch (Exception e) {
				DisasterManager.logger.warn("[通知玩家环境变化] [异常] [" + dps[i] + "]", e);
			}
		}
	}

	public long getLastRefreshBoxTime() {
		return lastRefreshBoxTime;
	}

	public void setLastRefreshBoxTime(long lastRefreshBoxTime) {
		this.lastRefreshBoxTime = lastRefreshBoxTime;
	}

	public DisasterStep getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(DisasterStep currentStep) {
		this.currentStep = currentStep;
	}

	public List<DisasterPlayer> getPlayerMap() {
		return playerMap;
	}

	public void setPlayerMap(List<DisasterPlayer> playerMap) {
		this.playerMap = playerMap;
	}
}
