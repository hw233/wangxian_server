package com.fy.engineserver.activity.disaster;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.datamanager.AbstractActivity;
import com.fy.engineserver.activity.datamanager.ActivityConstant;
import com.fy.engineserver.activity.datamanager.ActivityDataManager;
import com.fy.engineserver.activity.disaster.instance.Disaster;
import com.fy.engineserver.activity.disaster.instance.DisasterPlayer;
import com.fy.engineserver.activity.disaster.instance.DisasterStep;
import com.fy.engineserver.activity.disaster.module.DisasterBaseModule;
import com.fy.engineserver.activity.disaster.module.DisasterExpRewardModule;
import com.fy.engineserver.activity.disaster.module.DisasterOpenModule;
import com.fy.engineserver.activity.disaster.module.DisasterRewardModule;
import com.fy.engineserver.activity.disaster.module.TempTranslate;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.message.DISASTER_MATCH_SUCCESS_REQ;
import com.fy.engineserver.message.DISASTER_SIGN_SUCCESS_REQ;
import com.fy.engineserver.message.DISASTER_START_INFO_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.util.ExcelDataLoadUtil;

/**
 * 金猴天灾
 */
public class DisasterManager implements Runnable, AbstractActivity{
	public static Logger logger = LoggerFactory.getLogger(DisasterManager.class);
	
	private static DisasterManager inst;
	/** 匹配信息id */
	public static int matchId = 0;
	
	/** 金猴天灾基础数据配置 */
	public DisasterBaseModule baseModule;
	/** 金猴天灾游戏场景心跳线程 */
	public DisasterGameThread[] gameThreads = new DisasterGameThread[5]; 
	
	public DisasterRefreshThread refreshThred ;
	/** 报名玩家列表 */
	public Map<Long, SignInfo> signIds = new LinkedHashMap<Long, SignInfo>();
//	public List<Long> signIds = new ArrayList<Long>();
	/** 匹配成功组信息 */
	public Map<Integer, DisasterMatchInfo> matchInfos = new ConcurrentHashMap<Integer, DisasterMatchInfo>();
	/** 玩家对应匹配信息id */
	public Map<Long, Integer> playerMatchIds = new ConcurrentHashMap<Long, Integer>();
	/** 翻译 */
	public Map<String, String> translate = new HashMap<String, String>();
	/** 上次匹配成功时间记录(没有匹配则此时间为第一个报名时间) */
	public long lastMatchTime;
	/** 奖励模板*/
	public List<DisasterRewardModule> rewards = new ArrayList<DisasterRewardModule>();
	/** 经验桃经验配置 */
	public List<DisasterExpRewardModule> expRewards = new ArrayList<DisasterExpRewardModule>();
	private String fileName;
	/** 进入次数记录 */
	public Map<Long, Integer> enterRecord = new HashMap<Long, Integer>();
	
	
	public static DisasterManager getInst() {
		return inst;
	}
	
	@SuppressWarnings("unchecked")
	public void init() throws Exception {
		inst = this;
		File file = new File(fileName);
		List<DisasterBaseModule> temp = (List<DisasterBaseModule>) ExcelDataLoadUtil.loadExcelDataAsList(file, 0, DisasterBaseModule.class, logger);
		if (temp.size() > 0) {
			baseModule = temp.get(0);
		} else {
			logger.warn("*************************************************************************");
			logger.warn("*******************************基础配置不存在*********************************");
			logger.warn("*************************************************************************");
		}
		rewards = (List<DisasterRewardModule>) ExcelDataLoadUtil.loadExcelDataAsList(file, 1, DisasterRewardModule.class, logger);
		List<TempTranslate> tempTrans = (List<TempTranslate>) ExcelDataLoadUtil.loadExcelDataAsList(file, 2, TempTranslate.class, logger);
		for (int i=0; i<tempTrans.size(); i++) {
			translate.put(tempTrans.get(i).getKey(), tempTrans.get(i).getValue());
		}
		expRewards = (List<DisasterExpRewardModule>) ExcelDataLoadUtil.loadExcelDataAsList(file, 3, DisasterExpRewardModule.class, logger);
		Thread t = new Thread(this, "DisasterManager");
		t.start();
		for (int i=0; i<gameThreads.length; i++) {
			gameThreads[i] = new DisasterGameThread();
			gameThreads[i].setName("DisasterGameThread_" + i);
			gameThreads[i].start();
		}
		refreshThred = new DisasterRefreshThread();
		refreshThred.setName("DisasterRefreshThread");
		refreshThred.start();
	}
	public static void main(String[] args) throws Exception {
		DisasterManager m = new DisasterManager();
		m.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//disaster//disaster.xls");
		m.init();
	}
	
	public void notifyPlayerEnterServer(Player player) {
		try {
			SignInfo info = DisasterManager.getInst().signIds.get(player.getId());
			if (info != null) {
				this.cancelSign(player);
				return ;
			}
//			DISASTER_SIGN_SUCCESS_REQ req = new DISASTER_SIGN_SUCCESS_REQ(GameMessageFactory.nextSequnceNum(), info.getSignTime());
//			player.addMessageToRightBag(req);
			long now = System.currentTimeMillis();
			Disaster d = this.getPlayerDisaster(player);
			if (d != null) {
				DisasterPlayer dp = d.findDp(player);
				if (dp == null || dp.isEnd()) {
					return ;
				}
				long st = DisasterStep.STEP_READY.getDurationTime() - (now - d.startTime); 
				if (st > 0) {
					DISASTER_START_INFO_REQ req2 = new DISASTER_START_INFO_REQ(GameMessageFactory.nextSequnceNum(), DisasterManager.getInst().getTranslate("start_des"), 
							st);
					player.addMessageToRightBag(req2);
				}
			}
		} catch (Exception e) {
			logger.warn("[玩家进入游戏] [检测玩家是否正在匹配中] [异常] [" + player.getLogString() + "]", e);
		}
	}
	/**
	 * 玩家进入其他场景通知
	 * @param p
	 */
	public void noticePlayerTrans2OtherGame(Player p, String oldGameName, String gameName) {
		if (oldGameName == null || !baseModule.getMapName().equals(oldGameName)) {
			return;
		} 
		for (int i=0; i<gameThreads.length; i++) {
			if (gameThreads[i] != null) {
				if (gameThreads[i].noticePlayerTrans2OtherGame(p, gameName)) {
					return ;
				}
			}
		}
	}
	
	public void pickExpAe(Player player) {
		long rate = 0;
		for (DisasterExpRewardModule module : expRewards) {
			if (module.getMinLevel() <= player.getLevel() && module.getMaxLevel() >= player.getLevel()) {
				rate = module.getRewardExpRate();
				break;
			}
		}
//		long exp = player.getNextLevelExp() * rate / 10000;
		player.addExp(rate, ExperienceManager.ADDEXP_REASON_TASK);
		if (logger.isDebugEnabled()) {
			logger.debug("[pickExpAe] [" + player.getLogString() + "] [经验:" + rate + "]");
		}
	}
	
	/**
	 * 获取奖励模板
	 * @param player
	 * @param rank
	 * @return
	 */
	public DisasterRewardModule getDisasterRewardModule(Player player, int rank) {
		for (int i=0; i<rewards.size(); i++) {
			DisasterRewardModule module = rewards.get(i);
			if (module.getMinLevel() <= player.getLevel() && module.getMaxLevel() >= player.getLevel() && module.getRankLevel() == rank) {
				return module;
			}
		}
		return null;
	}
	
	/**
	 * 创建游戏场景
	 * @return
	 * @throws Exception
	 */
	public Disaster createDisaster() throws Exception {
		Disaster disaster = new Disaster();
		disaster.initGame();
		disaster.startTime = System.currentTimeMillis();
		return disaster;
	}
	
	public Monster refreshMonsterInPlayerPoint(Player player) {
		try {
			if (this.isPlayerInGame(player)) {
				Disaster d = this.getPlayerDisaster(player);
				if (d != null && d.canRefreshBoss()) {
					Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(DisasterConstant.TEMP_MONSTER_ID2);
					monster.setX(player.getX());
					monster.setY(player.getY());
					player.getCurrentGame().addSprite(monster);
					monster.setBornPoint(new Point(monster.getX(), monster.getY()));
					if (logger.isDebugEnabled()) {
						logger.debug("[在玩家身边刷boss] [成功] [" + player.getLogString() + "]");
					}
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("[在玩家身边刷boss] [失败] [阶段不对] [" + player.getLogString() + "]");
					}
				}
			}
		} catch (Exception e) {
			logger.warn("[在玩家身边刷boss] [异常] [" + player.getLogString() + "]", e);
		}
		return null;
	}
	
	/**
	 * 金猴天灾开启
	 */
	public void disasterStart() {
		signIds.clear();
		matchInfos.clear();
		playerMatchIds.clear();
		enterRecord.clear();
	}
	
	/**
	 * 匹配成功
	 * @return
	 */
	public DisasterMatchInfo createMatchInfo() {
		try {
			int num = signIds.size() >= 5 ? 5 : signIds.size();
			Disaster d = this.createDisaster();
			int id = matchId + 1;
			DisasterMatchInfo info = new DisasterMatchInfo(id, d);
			int threadIndex = id % gameThreads.length;
			gameThreads[threadIndex].addNewGame(d);
			long[] playerIds = new long[num];
			
			Iterator<Long> ito = signIds.keySet().iterator();
			int tempIndex = 0;
			while(ito.hasNext()) {
				long key = ito.next();
				playerIds[tempIndex++] = key;
				if (logger.isDebugEnabled()) {
					logger.debug("[匹配成功] [玩家id:" + key + "]");
				}
				ito.remove();
				if (tempIndex >= playerIds.length) {
					break;
				}
			}
			Player player = null;
			List<Long> deletePlayerIds = new ArrayList<Long>();
			long[] resultIds = new long[0];
			for (int i=0; i<playerIds.length; i++) {
				try {
					player = GamePlayerManager.getInstance().getPlayer(playerIds[i]);
					boolean deductResult = false;
					if (this.checkEnterTimes(player)) {
						deductResult = this.deductEnterTimes(player);
						if (deductResult) {
							popEntermWindow(player, info.getDisaster().startTime);
							//发送匹配成功提示
							if (logger.isDebugEnabled()) {
								logger.debug("[弹窗提示玩家匹配成功] [成功] [" + player.getLogString() + "] [deductResult:" + deductResult + "]");
							}
						}
					} else {
						deletePlayerIds.add(player.getId());
						if (logger.isInfoEnabled()) {
							logger.info("[弹窗提示玩家匹配成功] [失败] [进入次数不足] [" + player.getLogString() + "] [deductResult:" + deductResult + "]");
						}
					}
					
				} catch (Exception e) {
					logger.warn("[发送匹配成功消息] [异常] [playerId:" + playerIds[i] + "]", e);
				}
				playerMatchIds.put(player.getId(), id);
			}
			for (int i=0; i<playerIds.length; i++) {
				boolean needDelete = deletePlayerIds.contains(playerIds[i]);
				if (!needDelete) {
					resultIds = Arrays.copyOf(resultIds, resultIds.length + 1);
					resultIds[resultIds.length - 1] = playerIds[i];
				}
			}
			info.setPlayerIds(resultIds);
			info.setMatchedTime(System.currentTimeMillis());
			if (logger.isInfoEnabled()) {
				logger.info("[创建匹配信息] [成功] [" + info + "] [" + info.getLogString() + "] [resultIds:" + Arrays.toString(resultIds) + "] [playerIds:" + Arrays.toString(playerIds) + "]" );
			}
			matchInfos.put(info.getId(), info);
			return info;
		} catch (Exception e) {
			logger.warn("[创建匹配信息] [异常]", e);
		}
		return null;
	}
	
	private void popEntermWindow(Player p, long startTime) {
		long leftTime = DisasterStep.STEP_READY.getDurationTime() - (System.currentTimeMillis() - startTime);
		DISASTER_MATCH_SUCCESS_REQ req = new DISASTER_MATCH_SUCCESS_REQ(GameMessageFactory.nextSequnceNum(), leftTime);
		p.addMessageToRightBag(req);
		/*WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_enterDisaster option1 = new Option_enterDisaster();
		option1.setText(MinigameConstant.CONFIRM);
		Option_cancel option2 = new Option_cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = getTranslate("confirm_enter");		
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);*/
	}
	
	
	/**
	 * 报名
	 * @param player
	 */
	public void signUp(Player player) {
		if (signIds.containsKey(player.getId())) {
			player.sendError(getTranslate("already_signup"));
			return ;
		}
		long now = System.currentTimeMillis();
		if (!baseModule.isDisasterOpen(now)) {
			player.sendError(getTranslate("not_current_time"));
			return ;
		}
		if (player.getTeam() != null) {
			player.sendError(getTranslate("need_leave_team"));
			return ;
		}
		if (player.getLevel() < baseModule.getLevelLimit()) {
			player.sendError(getTranslate("level_not_match"));
			return ;
		}
		if (!this.checkEnterTimes(player)) {
			player.sendError(getTranslate("num_insufficient"));
			return ;
		}
		synchronized (signIds) {
			if (signIds.containsKey(player.getId())) {
				return ;
			}
			signIds.put(player.getId(), new SignInfo(player.getId(), now));
			logger.warn("[报名金猴天灾活动] [成功] [" + player.getLogString() + "]");
		}
		DISASTER_SIGN_SUCCESS_REQ req = new DISASTER_SIGN_SUCCESS_REQ(GameMessageFactory.nextSequnceNum(), now);
		player.addMessageToRightBag(req);
		if (signIds.size() <= 2) {
			lastMatchTime = System.currentTimeMillis();
		}
	}
	/**
	 * 取消报名
	 * @param player
	 */
	public void cancelSign(Player player) {
		if (!signIds.containsKey(player.getId())) {
			return ;
		}
		synchronized (signIds) {
			signIds.remove(player.getId());
		}
	}
	
	public void leaveDisaster(Player player) {
		for (int i=0; i<gameThreads.length; i++) {
			if (gameThreads[i] != null) {
				if (gameThreads[i].leaveGame(player)) {
					return ;
				}
			}
		}
	}
	
	/**
	 * 受到伤害
	 * @param player
	 * @param damage
	 */
	public void causeDamage(Player player, int damage) {
		for (int i=0; i<gameThreads.length; i++) {
			if (gameThreads[i] != null) {
				if (gameThreads[i].causeDamage(player, damage)) {
					return ;
				}
			}
		}
	}
	public void notifyAroundChange(Player player) {
		for (int i=0; i<gameThreads.length; i++) {
			if (gameThreads[i] != null) {
				if (gameThreads[i].notifyAroundChange(player)) {
					return ;
				}
			}
		}
	}
	
	/**
	 * 判断玩家是否在金猴天灾场景地图内
	 * @param player
	 * @return
	 */
	public boolean isPlayerInGame(Player player) {
		Game game = player.getCurrentGame();
		if (game != null && baseModule.getMapName().equals(game.gi.name)) {
			return true;
		}
		return false;
	}
	
	
	public boolean isPlayerDead(Player player) {
		for (int i=0; i<gameThreads.length; i++) {
			if (gameThreads[i] != null) {
				boolean b = gameThreads[i].isPlayerDead(player);
				if (b) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isDisasterMap(String gameName) {
		if (baseModule != null) {
			return baseModule.getMapName().equals(gameName);
		} else {
			return false;
		}
	}
	
	public Game getPlayerGame(Player player) {
		Game game = null;
		for (int i=0; i<gameThreads.length; i++) {
			if (gameThreads[i] != null) {
				game = gameThreads[i].findPlayerGame(player);
				if (game != null) {
					return game;
				}
			}
		}
		return null;
	}
	
	public Disaster getPlayerDisaster(Player player) {
		Disaster d = null;
		for (int i=0; i<gameThreads.length; i++) {
			if (gameThreads[i] != null) {
				d = gameThreads[i].findPlayerDisaster(player);
				if (d != null) {
					return d;
				}
			}
		}
		return null;
	}
	
	public Disaster findPlayerRecentlyDisaster(Player player) {
		Disaster d = null;
		List<Disaster> temp = new ArrayList<Disaster>();
		for (int i=0; i<gameThreads.length; i++) {
			if (gameThreads[i] != null) {
				List<Disaster> list = gameThreads[i].findPlayerRecentlyDisaster(player);
				if (list != null && list.size() > 0) {
					temp.addAll(list);
				}
			}
		}
		for (int i=0; i<temp.size(); i++) {
			if (d == null || d.startTime < temp.get(i).startTime) {
				d = temp.get(i);
			}
		}
		return d;
	}
	
	/**
	 * 玩家选择进入游戏
	 * @param player
	 * @param disaster
	 * @return
	 */
	public void optionEnterDisaster(Player player) {
		Integer key = playerMatchIds.get(player.getId());
		if (key != null) {
			DisasterMatchInfo info = matchInfos.get(key);
			this.notifyPlayerEnterGame(player, info.getDisaster());
		}
	} 
	
	public void optionCancel(Player player) {
		if (logger.isInfoEnabled()) {
			logger.info("[玩家选择放弃进入金猴天灾] [成功] [" + player.getLogString() + "]");
		}
	}
	
	private boolean notifyPlayerEnterGame(Player player, Disaster disaster) {
//		if (this.checkEnterTimes(player)) {
			if (player.isIsUpOrDown()) {			//下坐骑
				player.downFromHorse(true);
			}
			if (player.getActivePetId() > 0) {
				player.packupPet(true);					//收回宠物
			}
			return disaster.notifyPlayerEnter(player);
//		}
		/*if (logger.isInfoEnabled()) {
			logger.info("[玩家选择进入金猴天灾游戏] [失败] [次数不足] [" + player.getLogString() + "]");
		}*/
//		return false;
	}
	/**
	 * 扣除玩家进入次数
	 * @param player
	 * @return
	 */
	public boolean deductEnterTimes(Player player) {
		synchronized (enterRecord) {
			Integer a = enterRecord.get(player.getId());
			if (a != null && a >= DisasterConstant.MAX_ENTER_TIMES) {
				return false;
			}
			if (a == null) {
				a = 0;
			}
			a += 1;
			enterRecord.put(player.getId(), a);
			return true;
		}
	}
	
	/**
	 * 检查玩家是否有足够次数进入游戏
	 * @param player
	 * @return
	 */
	public boolean checkEnterTimes(Player player) {
		Integer a = enterRecord.get(player.getId());
		if (a != null && a >= DisasterConstant.MAX_ENTER_TIMES) {
			return false;
		}
		return true;
	}
	
	public String getTranslate(String key, Object ...obj) {
		try {
			String str = translate.get(key);
			if (str == null) {
				return key;
			}
			if (obj.length <= 0) {
				return str;
			}
			return String.format(str,obj);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("[获取翻译] [异常] [key:" + key + "] [" + Arrays.toString(obj) + "]", e);
			}
		}
		return key;
	}
	
	/** 是否已发送提前通知 */
	public boolean[] noticeFlag = new boolean[DisasterConstant.prenoticeTime.length];
	public static int heartBeatTime = 2000;
	
	public static boolean isOpen = true;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isOpen) {
			//处理游戏开始
			long now = System.currentTimeMillis();
			{
				boolean b = baseModule.isDisasterOpen(now);
				if (noticeFlag[noticeFlag.length-1] && b) {
					Arrays.fill(noticeFlag, false);
				} else if (!b){
					if (signIds.size() > 0) {
						synchronized (signIds) {
							signIds.clear();
						}
					}
					for (int i=0; i<DisasterConstant.prenoticeTime.length; i++) {
						if (!noticeFlag[i] && baseModule.needPrenotice(now, DisasterConstant.prenoticeTime[i])) {
							if (i == 0) {
								this.disasterStart();
							}
							noticeFlag[i] = true;
							//发送世界提示给玩家，提示活动即将开启
							long min = DisasterConstant.prenoticeTime[i]/60000;
							DevilSquareManager.instance.sendWordMsg(getTranslate("prenotice_msg", min + ""));
							if (logger.isDebugEnabled()) {
								logger.debug("[通知玩家活动即将开始] [" + getTranslate("prenotice_msg", min + "") + "]");
							}
						}
					}
				}
			}
			//处理匹配
			{
				if (signIds.size() >= DisasterConstant.MAX_NUM) {
					synchronized (signIds) {
						kickUnavaliblePlayer();
						if (signIds.size() >= DisasterConstant.MAX_NUM) {
							createMatchInfo();
							lastMatchTime = now;
						}
					}
				} else if (signIds.size() >= DisasterConstant.MIN_NUM && (now - lastMatchTime) >= DisasterConstant.matchCD) {
					synchronized (signIds) {
						kickUnavaliblePlayer();
						if (signIds.size() >= DisasterConstant.MIN_NUM) {
							createMatchInfo();
							lastMatchTime = now;
						}
					}
				}
			}
			try {
				Thread.sleep(heartBeatTime);
			} catch (Exception e) {
				
			}
		}
	}
	/**
	 * 匹配前将所有不符合进入游戏条件的玩家踢出报名列表
	 */
	public void kickUnavaliblePlayer() {
		Player player = null;
		Iterator<Long> ito = signIds.keySet().iterator();
		while (ito.hasNext()) {
			long playerId = ito.next();
			try {
				player = GamePlayerManager.getInstance().getPlayer(playerId);
				Game game = player.getCurrentGame();
				if (!player.isOnline() || game == null) {
					ito.remove();
					continue;
				}
				for (int i=0; i<DisasterConstant.unavaibleMaps.length; i++) {
					if (DisasterConstant.unavaibleMaps[i].equals(game.gi.name)) {
						ito.remove();
						break;
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("[踢出不符合参加活动规则玩家] [成功] [" + player.getLogString() + "]");
				}
			} catch (Exception e) {
				ito.remove();
				logger.warn("[踢出不符合参加活动规则玩家] [异常] [playerId:" + playerId + "]", e);
			}
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getActivityName() {
		// TODO Auto-generated method stub
		return ActivityConstant.空岛大冒险;
	}

	@Override
	public List<String> getActivityOpenTime(long now) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(now);
		int w = c.get(Calendar.DAY_OF_WEEK);
		List<DisasterOpenModule> list = baseModule.getTimeRules();
		List<String> result = new ArrayList<String>();
		for (int i=0; i<list.size(); i++) {
			if (w == list.get(i).getDayOfWeek()) {
				result.add(list.get(i).getStartHourOfDay() + ":" + ActivityDataManager.getMintisStr(list.get(i).getStartMinOfHour()));
			}
		}
		return result;
	}

	@Override
	public boolean isActivityTime(long now) {
		// TODO Auto-generated method stub
		return baseModule.isDisasterOpen(now);
	}
}
