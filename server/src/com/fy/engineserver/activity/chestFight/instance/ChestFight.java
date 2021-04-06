package com.fy.engineserver.activity.chestFight.instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.activity.chestFight.model.ChestBaseModel;
import com.fy.engineserver.activity.chestFight.model.ChestFightModel;
import com.fy.engineserver.activity.chestFight.model.ChestModel;
import com.fy.engineserver.activity.chestFight.model.TempReturnModel;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.ChestFightNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.util.TimeTool;

public class ChestFight {
	public Game game;
	/** 在游戏内的角色  */
	public volatile Map<Long, ChestFighyPlayer> partPlayers = new Hashtable<Long, ChestFighyPlayer>();
	
	public Object lock = new Object();
	
	/** 系统产出的宝箱列表 */
	public Map<Integer, ChestFightModel> chestList = new Hashtable<Integer, ChestFightModel>();
	/** 上次检测是否开启宝箱时间 */
	private long lastCheckChestTime = 0L;		
	/** 检测宝箱开启间隔 */
	public static long checkChestIntever = 500;
	/** 最大可进入玩家数量 */
	public int maxAllowNum = 10;
	/** 已经开启的宝箱数量 */
	public volatile int 已经开启的宝箱个数 = 0;
	public int 宝箱个数 = 10;
	/** 状态  根据状态有不同规则（初始状态不允许pk） */
	public int state = 0;
	/*** 是否刷新过宝箱 */
	public volatile boolean refreshChest = false;
	
	private long lastChangeStateTime;
	
	private long lastCheckNoticeTime;
	/** 上次世界提示时间 */
	public long lastNoticeTime;
	/** 刷新宝箱次数 */
	public int refreshTimes = 3;
	
	private long 宝箱全部捡完时间  = 0;
	
	public static long[] refreshInteTimes = new long[]{60000, 30000, 0};
	public static String[] tips = new String[]{Translate.刷新神魂提示3,Translate.刷新神魂提示2,Translate.刷新神魂提示1};
	
	public static Random ran = new Random(System.currentTimeMillis());
	
	public static final int 准备 = 1;			//不允许pk
	public static final int 开始 = 2;			//刷宝箱
	public static final int 结束 = 3;
	//测试服开日志，方便查问题
	public static boolean openDebug = true;
	
	public static long 结束后多久刷回城npc = 60000;
	public static int backTownNpcId = 600212;
	public static int[] npcPoints = new int[]{1279,842};
	long npcId = -1;
	
	public String getLogString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[state:" + state + "]");
		sb.append("[lastNoticeTime:" + lastNoticeTime + "]");
		sb.append("[maxAllowNum:" + maxAllowNum + "]");
		sb.append("[已经开启的宝箱个数:" + 已经开启的宝箱个数 + "]");
		sb.append("[refreshChest:" + refreshChest + "]");
		for (ChestFighyPlayer p : partPlayers.values()) {
			sb.append(p);
		}
		return sb.toString();
	}
	
	public static boolean 开启通知宝箱变化 = true;
	public long lastNoticeChestTime = 0;
	
	public void heartBeat(long now) {
		try {
			game.heartbeat();
		} catch (Exception e) {
			ChestFightManager.logger.warn("[宝箱争夺] [心跳异常] ", e);
		}
		try {
			Iterator<Long> ite = partPlayers.keySet().iterator();
			while (ite.hasNext()) {
				long key = ite.next();
				try {
					 ChestFighyPlayer fight = partPlayers.get(key);
					 if (fight.isLiveInFight()) {
						 Player p = GamePlayerManager.getInstance().getPlayer(fight.getPlayerId());
						 if (!p.isOnline()) {
							 this.dropAllChest(p, "玩家下线");
						 }
					 }
				} catch (Exception e) {
					ChestFightManager.logger.warn("[宝箱争夺] [异常]", e);
				}
			}
		} catch (Exception e) {
			
		}
		if (this.state == 结束 && ChestFightManager.inst.checkTime()) {
			this.setState(准备);
		}
		if (now >= (lastCheckNoticeTime + 5000)) {		//5s检测一次
			try {
				if (this.state == 开始) {		//检测是否需要清理上次数据
					if (ChestFightManager.inst.isEndTime()) {
						this.setState(结束);
					}
				} else if (this.state == 结束) {	//检测是否需要给玩家世界提示
					int idi = ChestFightManager.inst.isNoticeTime(now,lastNoticeTime);
					if (idi >= 0) {
						lastNoticeTime = now;
						noticeAllPlayerAct(idi);
					}
				}
			} catch (Exception e) {
			}
		}
		
		if (this.state == 准备 && lastChangeStateTime > 0 && (now >= (lastChangeStateTime + ChestFightManager.inst.baseModel.getWaitTime()))) {
			this.setState(开始);
		}
		
		
		if (this.state == 结束 && game != null && game.getLivingObjects() != null && game.getLivingObjects().length > 0) {
			try {
				this.clealAllSprite();
			} catch (Exception e) {
				ChestFightManager.logger.warn("[宝箱争夺] [副本结束状态清除所有生物] [异常] ", e);
			}
		}
		
		try {
			if (已经开启的宝箱个数 < 宝箱个数 &&now >= (lastCheckChestTime + checkChestIntever)) {
				lastCheckChestTime = now;
				//检查宝箱时间，是否需要开启
				synchronized (lock) {
					Iterator<Long> ite = partPlayers.keySet().iterator();
					while (ite.hasNext()) {
						long key = ite.next();
						ChestFighyPlayer fight = partPlayers.get(key);
						if (!fight.isLiveInFight()) {
							continue;
						}
						int num = fight.notifyOpenChest(now,this);
						已经开启的宝箱个数 += num;
						if (已经开启的宝箱个数 >= 宝箱个数) {
							this.noticeAllInnerPlayerTips(Translate.所有神魂被开完提示);
							宝箱全部捡完时间 = now;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			ChestFightManager.logger.warn("[宝箱争夺] [心跳中开启宝箱] [异常]", e);
		}
		
		if (开启通知宝箱变化 && (now >= (lastNoticeChestTime+1000))) {
			lastNoticeChestTime = now;
			try {
				Iterator<Long> ite = partPlayers.keySet().iterator();
				while (ite.hasNext()) {
					long key = ite.next();
					ChestFighyPlayer fight = partPlayers.get(key);
					if (fight.isLiveInFight() && GamePlayerManager.getInstance().isOnline(fight.getPlayerId())) {
						Player p = GamePlayerManager.getInstance().getPlayer(fight.getPlayerId());
						p.setChestType();
					}
				}
			} catch (Exception e) {
				
			}
		}
		try { 
			if (refreshTimes > 0 && this.state == 开始 && (lastChangeStateTime + refreshInteTimes[refreshTimes-1]) <= now) {
				notifyRefreshChest();
			}
		} catch (Exception e) {
			
		}
		if (宝箱全部捡完时间 > 0 && now >= (宝箱全部捡完时间 + 结束后多久刷回城npc)) {
			宝箱全部捡完时间 = 0;
			refreshBackTownNpc();
			
		}
	}
	
	public void refreshBackTownNpc() {
		try {
			NPC npc = MemoryNPCManager.getNPCManager().createNPC(backTownNpcId);
			if (npc != null) {
				npc.setX(npcPoints[0]);
				npc.setY(npcPoints[1]);
				game.addSprite(npc);
				npcId = npc.getId();
				ActivitySubSystem.logger.warn("[宝箱争夺] [刷出回城npc] [成功] [" + npc.getId() + "] [" + npc.getName() + "] [" + Arrays.toString(npcPoints) + "]");
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.warn("[宝箱争夺] [刷出回城npc] [异常] ", e);
		}
	}
	
	public TempReturnModel checkPlayerChestTypeAndTime(long playerId) {
		ChestFighyPlayer cfp = partPlayers.get(playerId);
		int[] chestTypes = new int[0];
		long[] chestTimes = new long[0];
		if (cfp != null) {
			List<ChestFightModel> list = cfp.getObtainChests();
			for (int i=0; i<list.size(); i++) {
				if (!list.get(i).isOpen()) {
					ChestModel model = ChestFightManager.inst.chests.get(list.get(i).getChestType());
					chestTypes = Arrays.copyOf(chestTypes, chestTypes.length+1);
					chestTypes[chestTypes.length-1] = list.get(i).getChestType();
					chestTimes = Arrays.copyOf(chestTimes, chestTimes.length+1);
					chestTimes[chestTimes.length-1] = list.get(i).getObtainTime() + model.getOpenTime();
				}
			}
		}
		TempReturnModel m = new TempReturnModel(chestTypes, chestTimes);
		return m;
	}
	private volatile int chestId = 0;
	
	public void notifyRefreshChest() {
		if (refreshTimes <= 0) {
			return ;
		}
		ChestBaseModel model = ChestFightManager.inst.baseModel;
//		List<Integer> tempList = new ArrayList<Integer>();
		for (int i=0; i<model.getChestTypes().length; i++) {
			if (model.getChestTypes()[i] != refreshTimes) {			
				continue;
			}
			ChestModel cm = ChestFightManager.inst.chests.get(model.getChestTypes()[i]);
			for (int j=0; j<model.getRefreshNums()[i]; j++) {
				ChestFightModel cfm = new ChestFightModel();
				cfm.setChestId(chestId++);
				cfm.setChestType(model.getChestTypes()[i]);
				chestList.put(cfm.getChestId(), cfm);
				//刷宝箱
				int tempIndex = ran.nextInt(model.getChestBornPoints().size());
				if (tempList.contains(tempIndex)) {
					for (int k=0; k<model.getChestBornPoints().size(); k++) {
						if (!tempList.contains(k)) {
							tempIndex = k;
							break;
						}
					}
				}
				tempList.add(tempIndex);
				cfm.setPointIndex(tempIndex);
				Integer[] point = model.getChestBornPoints().get(tempIndex);
				ChestFightNPC npc = getChestFightNPC(cm.getNpcId(), cfm.getChestId());
				npc.setX(point[0]);
				npc.setY(point[1]);
				game.addSprite(npc);
				ChestFightManager.logger.warn("[宝箱争夺] [刷新宝箱] [" + npc.getId() + "] [" + cfm + "] [tempIndex:" + tempIndex + "] [" + Arrays.toString(point) + "]");
			}
		}
		this.noticeAllInnerPlayerTips(tips[refreshTimes-1]);
		refreshTimes--;
	}
	
	public int getPlayerStates(Player player) {
		ChestFighyPlayer p = partPlayers.get(player.getId());
		if (p == null || !p.isLiveInFight()) {
			return -1;
		}
		int jin = p.getChestNumByType(1);
		int yin = p.getChestNumByType(2);
		int tong = p.getChestNumByType(3);
		return jin*100+yin*10+tong;
	}
	/**
	 * 给副本内的玩家提示
	 * @param msg
	 */
	public void noticeAllInnerPlayerTips(String msg) {
		try {
			Iterator<Long> ito = partPlayers.keySet().iterator();
			while (ito.hasNext()) {
				long key = ito.next();
				ChestFighyPlayer cfp = partPlayers.get(key);
				if (cfp.isLiveInFight()) {
					Player p = GamePlayerManager.getInstance().getPlayer(key);
					p.send_HINT_REQ(msg, (byte) 6);
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	public void noticeAllPlayerAct(int index) {
		try {
			ChatMessage msgA = new ChatMessage();
			String a = TimeTool.instance.getShowTime(ChestFightManager.开始前多久发世界提示[index]);
			msgA.setMessageText(String.format(Translate.开始世界提示, a));
			ChatMessageService.getInstance().sendMessageToSystem(msgA);
			ChatMessageService.getInstance().sendHintMessageToSystem(msgA);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ChatMessageService.logger.error("[世界公告] [异常] ", e);
		}
	}
	/**
	 * 副本开启前10分钟清楚地图内所有生物
	 */
	public void clealAllSprite() {
		synchronized (lock) {
			Iterator<Long> ite = partPlayers.keySet().iterator();
			while (ite.hasNext()) {
				long key = ite.next();
				ChestFighyPlayer cp = partPlayers.get(key);
				if (cp.isLiveInFight()) {
					try {
						cp.setLiveInFight(false);
						Player player = GamePlayerManager.getInstance().getPlayer(cp.getPlayerId());
						int level = player.getSoulLevel();
						long exp = (long) (ExperienceManager.maxExpOfLevel[level] * ChestFightManager.inst.baseModel.getBasicExpRate() / 100);
						player.addExp(exp, ExperienceManager.飞升炼丹);
						回城(player);
//						dropAllChest(player,"离开地图");
						player.setPkMode(Player.和平模式);
						player.setChestType();
						player.modifyNameColorType();
						ChestFightManager.logger.warn("[宝箱争夺] [强制玩家离开地图] [成功] [奖励经验:" + exp + "] [" + player.getLogString() + "]");
					} catch (Exception e) {
						ChestFightManager.logger.warn("[宝箱争夺] [传送玩家出地图] [异常] [" + cp + "]");
					}
				}
			}
		}
		game.removeAllChestNpc();
	}
	
	private boolean 回城(Player p){
		try{
			Game currentGame = p.getCurrentGame();
			if(currentGame != null && !currentGame.gi.name.equals(game.gi.name)){
				ChestFightManager.logger.info("[宝箱争夺] [玩家已经不再这个区域了，不需要在回程了]");
				return true;
			}
			String mapName = p.getResurrectionMapName();
			int x = p.getResurrectionX();
			int y = p.getResurrectionY();
			game.transferGame(p, new TransportData(0, 0, 0, 0, mapName, x, y), true);
			ChestFightManager.logger.warn("[宝箱争夺] [回城] [成功] ["+p.getLogString()+"]");
			return true;
		}catch(Exception e){
			if(p != null){
				ChestFightManager.logger.warn("[宝箱争夺] [回城] [异常][" + p.getLogString() + "]", e);
			} else {
				ChestFightManager.logger.error("[传送出副本出错] ", e);
			}
			return false;
		}
	}
	
	public boolean isActiveAct() {
		if (ChestFightManager.inst.checkTime4HeartBeat()) {
			return true;
		}
		if (this.game != null && this.game.getLivingObjects() != null && this.partPlayers.size() > 0) {
			for (ChestFighyPlayer p : this.partPlayers.values()) {
				if (p.isLiveInFight()) {
					return true;
				}
			}
		}
		return false;
	}
	
	List<Integer> tempList = new ArrayList<Integer>();
	
	/**
	 * 设置状态   根据不同状态有不同操作和规则   
	 * @param state
	 */
	public void setState(int state) {
		this.state = state;
		lastChangeStateTime = System.currentTimeMillis();
		if (openDebug) {
			ChestFightManager.logger.warn("[宝箱争夺] [修改状态] [" + state + "]");
		}
		if (state == 准备 || state == 结束) {			//设置地图不允许pk
			game.gi.setLimitPVP(true);
			if (state == 结束) {
				if (npcId > 0) {
					NPC npc = MemoryNPCManager.getNPCManager().getNPC(npcId);
					if (npc != null) {
						game.removeSprite(npc);
					}
				}
				clealAllSprite();			//清除所有玩家
				已经开启的宝箱个数 = 0;
				refreshChest = false;
				refreshTimes = 3;
				chestList.clear();
				partPlayers.clear();
				tempList.clear();
				npcId = -1;
			}
		} else {					//打开pk
			game.gi.setLimitPVP(false);
			if (state == 开始) {		//刷新宝箱
				synchronized (lock) {
					if (!refreshChest) {
						refreshChest = true;
						notifyRefreshChest();
					}
				}
			}
		}
	}
	private ChestFightNPC getChestFightNPC(int npcId, int chestId) {
		NPCManager nm = MemoryNPCManager.getNPCManager();
		ChestFightNPC fcn = (ChestFightNPC) nm.createNPC(npcId);
		fcn.setChestId(chestId);
		fcn.setPickState((byte) 0);
		fcn.setCreateTime(System.currentTimeMillis());
		return fcn;
	}
	
	public String check4Enter(Player player) {
		if (getPlayerNum() >= maxAllowNum) {
			return "进入人数已达上限";
		}
		if (partPlayers.containsKey(player.getId())) {
			return "不允许多次进入";
		}
		return null;
	}
	
	public void notifyEnterFight(Player player) {
		long now = System.currentTimeMillis();
		synchronized (lock) {
			String result = check4Enter(player);
			if (result != null) {
				player.sendError(result);
				return ;
			}
			Game currentGame = player.getCurrentGame();
			if (currentGame == null) {
				player.sendError("异常");
				return ;
			}
			//设置pk模式为全体
			player.setPkMode(Player.全体模式);
			//设置名字颜色
			player.setNameColorType(Player.名字颜色_红色);
			//解散玩家组队
			if (player.getTeam() != null) {
				player.getTeam().removeMember(player,0);
			}
			//下飞行坐骑
			if (player.isFlying()) {
				player.downFromHorse();
			}
			List<Integer[]> list = ChestFightManager.inst.baseModel.getBornPoints();
			Integer[] point = list.get(ran.nextInt(list.size()));
			currentGame.transferGame(player, new TransportData(0, 0, 0, 0, game.gi.name, point[0], point[1]));
			ChestFighyPlayer p = new ChestFighyPlayer(player.getId(), now);
			partPlayers.put(player.getId(), p);
			if (this.state == 准备 && lastChangeStateTime > 0 && (now < (lastChangeStateTime + ChestFightManager.inst.baseModel.getWaitTime()))) {
				int noticeStartFightTime = (int) ((lastChangeStateTime + ChestFightManager.inst.baseModel.getWaitTime() - now) / 1000);
				if (noticeStartFightTime > 0) {
					/*NOTIFY_ROBBERY_COUNTDOWN_REQ resp = new NOTIFY_ROBBERY_COUNTDOWN_REQ();
					resp.setCountType((byte)2);
					resp.setLeftTime(noticeStartFightTime);
					resp.setContentmass("开启倒计时");			//没有用到，客户端使用艺术字体
					player.addMessageToRightBag(resp);*/
					player.chanllengeFlag = 100+noticeStartFightTime;
					if (ChestFightManager.logger.isDebugEnabled()) {
						ChestFightManager.logger.debug("[宝箱争夺] [玩家进入地图] [通知倒计时] [成功] [" + player.getLogString() + "] [" + noticeStartFightTime + "]");
					}
				}
				if (ChestFightManager.logger.isDebugEnabled()) {
					ChestFightManager.logger.debug("[宝箱争夺] [玩家进入地图] [通知倒计时] [失败] [已经到时间刷宝箱] [" + player.getLogString() + "] [" + noticeStartFightTime + "]");
				}
			}
			try {
				AchievementManager.getInstance().record(player, RecordAction.参加宝箱大乱斗次数);
			} catch (Exception e) {
				PlayerAimManager.logger.warn("[目标系统] [统计仙丹修炼次数] [异常] [" + player.getLogString() + "]", e);
			}
			ChestFightManager.logger.warn("[宝箱争夺] [玩家进入争夺地图] [成功] [" + player.getLogString() + "]");
		}
	}
	/**
	 * 获取呆在地图中的玩家数量
	 * @return
	 */
	public int getPlayerNum() {
		int result = 0;
		for (ChestFighyPlayer p : partPlayers.values()) {
			if (p.isLiveInFight()) {
				result += 1;
			}
		}
		return result;
	}
	/**
	 * 玩家死亡
	 * @param player
	 */
	public void notifyPlayerDeath(Player player) {
		if (partPlayers.containsKey(player.getId())) {
			ChestFighyPlayer p = partPlayers.get(player.getId());
			if (p.isLiveInFight()) {
				dropAllChest(player,"角色死亡");
			}
		}
	}
	
	public void notifyPlayerOutOfMap(long  playerId) {
		if (partPlayers.containsKey(playerId)) {
			ChestFighyPlayer p = partPlayers.get(playerId);
			if (p.isLiveInFight()) {
				try {
					Player player = GamePlayerManager.getInstance().getPlayer(playerId);
					dropAllChest(player,"玩家进入碰撞区");
					player.sendError(Translate.进入碰撞区神魂掉落);
				} catch (Exception e) {
					ChestFightManager.logger.warn("[宝箱争夺] [玩家进入碰撞区] [处理异常] [" + playerId + "]", e);
				}
			}
		}
	}
	
	/**
	 * 玩家离开地图
	 * @param player
	 */
	public void notifyPlayerLeaveGame(Player player) {
		if (partPlayers.containsKey(player.getId())) {			//角色在宝箱争夺中
			ChestFighyPlayer p = partPlayers.get(player.getId());
			if (p.isLiveInFight()) {
				p.setLiveInFight(false);
				p.setLeaveTime(System.currentTimeMillis());
				//给予基础经验奖励
				int level = player.getSoulLevel();
				long exp = (long) (ExperienceManager.maxExpOfLevel[level] * ChestFightManager.inst.baseModel.getBasicExpRate() / 100);
				player.addExp(exp, ExperienceManager.飞升炼丹);
				//没有开启的宝箱丢到地上
				dropAllChest(player,"离开地图");
				player.setPkMode(Player.和平模式);
				player.setChestType();
				player.modifyNameColorType();
				ChestFightManager.logger.warn("[宝箱争夺] [玩家离开地图] [成功] [奖励经验:" + exp + "] [" + player.getLogString() + "]");
			}
		}
	}
	public static int[] tempPoint = new int[]{30, 60, 90,-30,-60,-90, 0};
	/**
	 * 掉落所有宝箱
	 * @param player
	 */
	public void dropAllChest(Player player,String reason) {
		synchronized (lock) {
			try {
				ChestFighyPlayer p = partPlayers.get(player.getId());
				if (p.getObtainChests() != null && p.getObtainChests().size() > 0) {
					List<ChestFightModel> temp = new ArrayList<ChestFightModel>();
//					List<Integer> tempList = new ArrayList<Integer>();
					int tempIndex = -1;
					for (int i=0; i<p.getObtainChests().size(); i++) {
						ChestFightModel chest = p.getObtainChests().get(i);
						String b = chest.dropChest(player);
						if (b == null) {
							temp.add(chest);
							tempIndex = tempIndex + 1;
							if (tempIndex >= tempPoint.length) {
								tempIndex = tempPoint.length-1;
							}
							chest.setPointIndex(-1);
							Integer[] point = new Integer[]{player.getX()+tempPoint[tempIndex], player.getY() + tempPoint[tempIndex]};
							if ("玩家进入碰撞区".equals(reason)) {				//进入碰撞区掉落单独处理，不掉落在玩家身边 
								ChestBaseModel model = ChestFightManager.inst.baseModel;
								int tt = ran.nextInt(model.getChestBornPoints().size());
								if (this.tempList.contains(tt)) {
									for (int k=0; k<model.getChestBornPoints().size(); k++) {
										if (!this.tempList.contains(k)) {
											tt = k;
											break;
										}
									}
								}
								this.tempList.add(tt);
								point = model.getChestBornPoints().get(tt);
							}
							ChestModel cm = ChestFightManager.inst.chests.get(chest.getChestType());
							ChestFightNPC npc = getChestFightNPC(cm.getNpcId(), chest.getChestId());
							npc.setX(point[0]);
							npc.setY(point[1]);
							game.addSprite(npc);
							ChestFightManager.logger.warn("[宝箱争夺] [刷新宝箱] [" + npc.getId() + "] [" + Arrays.toString(point) + "]");
						}
						ChestFightManager.logger.warn("[宝箱争夺] [玩家宝箱掉落] ["+(b==null?"成功":b)+"] [" + player.getLogString() + "] [" + reason + "] [" + chest + "] [tempIndex:" + tempIndex + "]");
					}
					if (temp.size() > 0) {
						for (ChestFightModel idd : temp) {
							p.getObtainChests().remove(idd);
						}
						player.setChestType();
					}
				}
			} catch (Exception e) {
				ChestFightManager.logger.warn("[宝箱争夺] [玩家宝箱掉落] [异常] [" + player.getLogString() + "] [" + reason + "]");
			}
			
		}
	}
	/**
	 * 捡起宝箱
	 * @param player
	 * @param chestId
	 */
	public boolean notifyPickUpChest(Player player, int chestId) {
		long now = System.currentTimeMillis();
		ChestFightModel chest = chestList.get(chestId);
		if (chest == null) {
			ChestFightManager.logger.warn("[宝箱争夺] [玩家捡起宝箱] [失败] [未知宝箱id] [" + chestId + "] [" + player.getLogString() + "]");
			return false;
		}
		if (chest.isOpen() || chest.getPlayerId() > 0) {
			ChestFightManager.logger.warn("[宝箱争夺] [玩家捡起宝箱] [失败] [宝箱已经被别人捡起] [" + chestId + "] [" + player.getLogString() + "] [" + chest + "]");
			return false;
		}
		ChestModel model = ChestFightManager.inst.chests.get(chest.getChestType());
		if (model == null) {
			ChestFightManager.logger.warn("[宝箱争夺] [玩家捡起宝箱] [失败] [目标未空] [" + player.getLogString() + "] [" + chest + "]");
			return false;
		}
		if (player.isDeath()) {
			ChestFightManager.logger.warn("[宝箱争夺] [玩家捡起宝箱] [失败] [角色死亡] [" + player.getLogString() + "] [" + chest + "]");
			return false;
		}
		synchronized (lock) {
			if (chest.isOpen() || chest.getPlayerId() > 0) {
				ChestFightManager.logger.warn("[宝箱争夺] [玩家捡起宝箱] [失败] [宝箱已经被别人捡起] [" + chestId + "] [" + player.getLogString() + "] [" + chest + "]");
				return false;
			}
			ChestFighyPlayer p = partPlayers.get(player.getId());
			if (p == null) {
				ChestFightManager.logger.warn("[宝箱争夺] [玩家捡起宝箱] [失败] [没有玩家进入副本信息] [" + chestId + "] [" + player.getLogString() + "] [" + chest + "]");
				return false ;
			}
			if (p.getChestNumByType(chest.getChestType()) < model.getMaxObtain()) {
				chest.setObtainTime(now);
				chest.setPlayerId(player.getId());
				player.setChestType();
				p.getObtainChests().add(chest);
				if (chest.getPointIndex() >= 0) {
					for (int i=0; i<tempList.size(); i++) {
						if (tempList.get(i) == chest.getPointIndex()) {
							tempList.remove(i);
							break;
						}
					}
				}
//				tempList.remove(chest.getPointIndex());
				ChestFightManager.logger.warn("[宝箱争夺] [玩家捡起宝箱] [成功] [" + player.getLogString() + "] [" + chest + "]");
				return true;
			} else {
				player.sendError(Translate.拥有过多此类神魂);
			}
		}
		return false ;
	}
	
	public Game getPlayerGame(Player player) {
		if (partPlayers.containsKey(player.getId())) {
			ChestFighyPlayer p = partPlayers.get(player.getId());
			if (p.isLiveInFight()) {
				return game;
			}
		}
		synchronized (lock) {				//防止多线程延迟导致玩家进入操作在传送之后
			if (partPlayers.containsKey(player.getId())) {
				ChestFighyPlayer p = partPlayers.get(player.getId());
				if (p.isLiveInFight()) {
					return game;
				}
			}
		}
		return null;
	}
}
