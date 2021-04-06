package com.fy.engineserver.carbon.devilSquare;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.datamanager.AbstractActivity;
import com.fy.engineserver.activity.datamanager.ActivityConstant;
import com.fy.engineserver.carbon.CarbonManager;
import com.fy.engineserver.carbon.CarbonMgIns;
import com.fy.engineserver.carbon.devilSquare.instance.DevilSquare;
import com.fy.engineserver.carbon.devilSquare.model.BaseModel;
import com.fy.engineserver.carbon.devilSquare.model.DevilTranslateModel;
import com.fy.engineserver.carbon.devilSquare.model.PortalRefreashModel;
import com.fy.engineserver.carbon.devilSquare.model.RefreashModel;
import com.fy.engineserver.carbon.devilSquare.model.RefreashRule;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventPlayerLogin;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.carbon.Option_ConfirmEnterCarbon;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.seal.data.Seal;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;

public class DevilSquareManager implements CarbonMgIns,EventProc,AbstractActivity{
	public static Logger logger = LoggerFactory.getLogger(DevilSquareManager.class);
	
	public static DevilSquareManager instance;
	/** 配置文件地址 */
	private String initFileName;
	/** key为副本编号，value为对应副本的基础配置 */
	public Map<Integer, BaseModel> baseModelMap;
	/** key对应refreash索引id， value为刷新规则 */
	public Map<Integer, RefreashModel> refreashModelMap;
	/** 传送门刷新规则 */
	public PortalRefreashModel potalRule;
	/** 副本数量 */
	public static int devilSquareNum = 4;
	/** 副本管理线程 */
	public DevilSquareThread[] dsThreads;
	/** 副本刷怪线程 */
	public DevilSquareRefreashThread dsRefreashThread;
	/** 副本实例管理 */
	public Map<Integer, DevilSquare> dsInst = new LinkedHashMap<Integer, DevilSquare>();			//副本开启时创建实例放入list，副本结束时清空list
	/** 锁 */
	public Object lock = new Object();
	/** 传送门 */
	public Map<Integer, NPC> portal = null;
	/** 恶魔广场地图名记录 */
	public static Set<String> carbonMaps = new HashSet<String>();
	/** 颜色最大值 */
	public static int maxColor = 3;
	/** 副本翻译相关 */
	public Map<Integer, DevilTranslateModel> transMap;
	/** 经验翻倍怪物 */
	public static Map<Integer, Double> mulMonsterIds = new HashMap<Integer, Double>();
	
	/*** 后台杀怪测试用 */
	public Map<String, Integer> map = new HashMap<String, Integer>(); //物品掉落
	public static int testMonsterId = -1;
	/** 是否开启副本物品掉落统计 */
	public boolean isDropStatisticFlag = false;
	/** 副本解封时间 */
	public Map<Integer, Long> carbonSealTimes = new HashMap<Integer, Long>();
	
	public boolean isPreNotify = true;
	/** key=怪物id（配表）  value=可获得的积分 */
	public Map<Integer, Integer> monsterScores = new HashMap<Integer, Integer>();
	/**
	 * 获得怪物可获得的积分
	 * @param spriteId
	 * @return
	 */
	public int getScore(int spriteId) {
		int result = 0;
		if(monsterScores.get(spriteId) != null) {
			result = monsterScores.get(spriteId);
		}
		return result;
	}
	
	public synchronized void notifyTest(String aeName, int colorType) {
		if(!isDropStatisticFlag) {
			return;
		}
		String temp = aeName + "_" + colorType;
		int tt = 0;
		if(map.get(temp) == null) {
			tt = 1;
		} else {
			tt = map.get(temp) + 1;
		}
		map.put(temp, tt);
	}
	/** 以上测试用 */
	
	
	@Override
	public void doPreAct() {
		if(isPreNotify) {
			this.sendWordMsg(Translate.恶魔广场传送门即将出现);
			logger.warn(Translate.恶魔广场传送门即将出现);
			isPreNotify = false;
		}
	}
	
	@Override
	public void doAct() {					
		// TODO Auto-generated method stub
		synchronized (lock) {
			//判断是否为副本开启时间
			for(DevilSquareThread dt : dsThreads) {				//设置副本心跳速度
				if(dt.heartbeatTime != DevilSquareConstant.NOMAL) {
					dt.heartbeatTime = DevilSquareConstant.NOMAL;
				}
			}
			Calendar currentCalender = Calendar.getInstance();
			int minits = currentCalender.get(Calendar.MINUTE);
			int seconds = currentCalender.get(Calendar.SECOND);
			int times = minits * 60 + seconds;
			boolean isTimeRight = potalRule.getTransLastTime() > times;
			boolean isTransAct = false;
			Iterator<Integer> ite = dsInst.keySet().iterator();
			isPreNotify = true;
			while(ite.hasNext()) {
				int key = ite.next();
				DevilSquare ds = dsInst.get(key);
				BaseModel bm = this.baseModelMap.get(key);
				if(ds.getStatus() == DevilSquareConstant.UNSTART || ds.getStatus() == DevilSquareConstant.FINISH) {			//改变副本状态
					if(!isTimeRight) {				//如果已经超出传送时间则直接返回，不可改变副本状态
						continue;
					}
					ds.setStatus(DevilSquareConstant.PREPARE, bm.getPrepareTime());
					if(logger.isInfoEnabled()) {
						logger.info("[**********恶魔广场,开启，可以入场***********]");
					}
//					ds.openTime = System.currentTimeMillis();
					if(logger.isInfoEnabled()) {
						logger.info("[恶魔广场，已经超过时间]");
					}
					if(!isTransAct) {									//出传送门,多个副本只需要出现一个传送门就可以了
						isTransAct = true;
						if(portal == null) {
							portal = new HashMap<Integer, NPC>();
						}
						portal = refreshNPC(potalRule.getTransMap(), potalRule.getTransNPCId(), potalRule.getTransPoints().get(0));
					}
				} else if(ds.getStatus() == DevilSquareConstant.PREPARE) {			//单个副本有可能在传送门时间到前就开启
					long cutT = System.currentTimeMillis();
					if(cutT >= (ds.lastStatusChangeT + potalRule.getTransLastTime() * 1000)) {
						
						ds.setStatus(DevilSquareConstant.START, bm.getPrepareTime());					//副本开始
						if(logger.isInfoEnabled()) {
							logger.info("[**********恶魔广场正式开启，准备刷怪***********]");
						}
						if(!isTransAct) {									//清除传送门
							isTransAct = true;
							removeTransNPC(potalRule.getTransMap(), portal);
							portal = null;
						}
					}
				} else if(isTimeRight){
					logger.error("[恶魔广场][" + key + "级副本] [到点未开启，副本状态不对，当前状态为:" + ds.getStatus() + "]");;
				}
			}
		}
	}
	
	public boolean isPlayerInDevilSquare(Player p) {
		for(DevilSquareThread dt : dsThreads) {
			if(dt.getDs().playerList.contains(p.getId())) {
				return true;
			}
		}
		return false;
	}
	public boolean isPlayerInDevilSquare(long playerId) {
		for(DevilSquareThread dt : dsThreads) {
			if(dt.getDs().playerList.contains(playerId)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 得到玩家副本中可以使用原地复活的次数----如果需要给vip等特殊增加也在这儿做
	 * @param p
	 * @return
	 */
	public int getPlayerCanReliveTimes(Player p) {
		return DevilSquareConstant.RELIVE_TIMES;
	}
	
	public boolean isPlayerIndevilSquareMap(Player p) {
		if (p.getCurrentGame() == null) {
			return false;
		}
		if(carbonMaps.contains(p.getCurrentGame().gi.getName())) {
			return true;
		}
		return false;
	}
	public boolean isPlayerIndevilSquareMap(Game g) {
		if (g == null || g.gi == null) {
			return false;
		}
		if(carbonMaps.contains(g.gi.getName())) {
			return true;
		}
		return false;
	}
	
	public void setCarbonStatus(int key, byte status) {
		if(dsInst.get(key) != null) {
			BaseModel bm = this.baseModelMap.get(key);
			dsInst.get(key).setStatus(status, bm.getPrepareTime());
			logger.error("[恶魔广场] [后台改变恶魔城堡状态] [key:" + key + "] [status:" + status + "]");
		}
	}
	/**
	 * 怪物死亡时调用，用来判断副本最终boss是否被击杀
	 * @param m
	 */
	public void notifyMonsterKilled(Monster m) {
		Iterator<Integer> ite = dsInst.keySet().iterator();
		while(ite.hasNext()) {
			int key = ite.next();
			dsInst.get(key).notifyBossKilled(m);
		}
	}
	/**
	 * 玩家上线通知
	 * @param player
	 */
	public void notifyPlayerEnterServer(Player player) {
		Iterator<Integer> ite = dsInst.keySet().iterator();
		while(ite.hasNext()) {
			int key = ite.next();
			dsInst.get(key).notifyPlayerEnterServer(player);
		}
	}
	/**
	 * 玩家使用回城时通知
	 * @param p
	 */
	public void notifyUseTransProp(Player p) {
		if(!this.isPlayerInDevilSquare(p)) {
			return;
		}
		Iterator<Integer> ite = dsInst.keySet().iterator();
		while(ite.hasNext()) {
			int key = ite.next();
			dsInst.get(key).notifyUseTransProp(p);
		}
	}
	/**
	 * 玩家原地复活
	 * @param p
	 * @return
	 */
	public boolean notifyPlayerRelive(Player p) {
		if(!this.isPlayerInDevilSquare(p)) {
			return true;
		}
		boolean result = true;
		Iterator<Integer> ite = dsInst.keySet().iterator();
		while(ite.hasNext()) {
			int key = ite.next();
			if (!dsInst.get(key).notifyPlayerRelive(p)) {		//玩家已经超过复活次数
				result = false;
			}
		}
		return result;
	}
	/**
	 * 通知玩家获得宝箱物品
	 */
	public void notifyPlayerGetProp(Player p, ArticleEntity ae, Props prop) {
		if(!this.isPlayerInDevilSquare(p)) {
			return;
		}
		Iterator<Integer> ite = dsInst.keySet().iterator();
		while(ite.hasNext()) {
			int key = ite.next();
			dsInst.get(key).notifyGetPropByBox(p, ae, prop);
		}
	}
	/**
	 * 清除传送门
	 * @param mapName
	 * @param npcs
	 */
	public void removeTransNPC(String mapName, Map<Integer, NPC> npcs) {
		if(npcs == null) {
			return;
		}
		Iterator<Integer> ite = npcs.keySet().iterator();
		while(ite.hasNext()) {
			int key = ite.next();
			Game game = GameManager.getInstance().getGameByName(mapName, key);
			NPC npc = npcs.get(key); 
			if(npc != null) {
				game.removeSprite(npc);
			}
		}
	}
	
	/**
	 * 刷npc
	 * @param game
	 * @param npcId
	 * @param player
	 * @throws Exception 
	 */
	public Map<Integer, NPC> refreshNPC(String mapName, int npcId, Integer[] points){
		Map<Integer, NPC> map = new HashMap<Integer, NPC>();
		NPC npc = null;
		Game game = null;
		boolean isSucceed = false;
		for(int i=0; i<=3; i++) {
			game = GameManager.getInstance().getGameByName(mapName, i);
			if(game == null) {
				logger.error("[恶魔广场刷传送门错误，没有找到对应国家的对应地图] [mapName=" + mapName + "&&country=" + i + "]");
				continue;
			}
			try{
				npc = MemoryNPCManager.getNPCManager().createNPC(npcId);
				if(npc != null){
					npc.setX(points[0]);
					npc.setY(points[1]);
					game.addSprite(npc);
					map.put(i, npc);
					logger.warn("[传送副本刷新成功] [npcId:"+npcId+"] [mapName=" + mapName + "&&country=" + i + "] [刷新坐标:x=" + points[0] + "&& y=" + points[1] + "]");
					isSucceed = true;
					if(i == 0) {					//如果策划填写的是中立地图，只刷一次就可以了
						break;
					}
				} else {
					logger.error("[恶魔广场][刷传送门错误][没找到npc,npcId=" + npcId + "]");
				}
			}catch (Exception e) {
				logger.error("[恶魔广场][刷传送门错误]", e);
			}
		}
		if(isSucceed) {
			this.sendWordMsg(Translate.恶魔广场传送门已经出现);
			if(logger.isInfoEnabled()) {
				logger.info("[恶魔广场] [刷出世界喊话]");
			}
		}
		return map;
	}

	@Override
	public byte getActType() {
		// TODO Auto-generated method stub
		return DevilSquareConstant.DAILY_HOUR;
	}

	@Override
	public List<Integer> getActHour() {		//根据传送门的刷新时间规则在写
		// TODO Auto-generated method stub
		List<Integer> openTimes = new ArrayList<Integer>();
		openTimes.addAll(potalRule.getOpenTime());
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.addDevilOpenTimes);
		if(cr != null && cr.getBooleanValue() && cr.getIntValues() != null && cr.getIntValues().length > 0) {
			for (int i=0; i<cr.getIntValues().length; i++) {
				if(!openTimes.contains(cr.getIntValues()[i])) {
					openTimes.add(cr.getIntValues()[i]);
				} else if(ActivitySubSystem.logger.isInfoEnabled()) {
					ActivitySubSystem.logger.info("[恶魔城堡加额外次数] [活动新增时间与原配表时间有冲突][未生效时间 :" + cr.getIntValues()[i] + " 点]");
				}
			}
		}
		return openTimes;
	}
	
	/**
	 * 客户端发送协议请求进入副本
	 * @param p
	 * @param carbonLevel
	 */
	public void notifyEnterCarbon(Player p, int carbonLevel, boolean useUpperFlag) {
		if(this.isCarbonSeal(carbonLevel)) {
			logger.debug("[恶魔广场] [副本还在封印状态，进入失败] [副本等级:" + carbonLevel + "] [解封时间:" + carbonSealTimes.get(carbonLevel) + "]");
			long openTime = carbonSealTimes.get(carbonLevel);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(openTime);
			String str = Translate.translateString(Translate.副本将在XX天开启, new String[][] {{Translate.STRING_1, calendar.get(Calendar.YEAR) + ""}, {Translate.STRING_2, (calendar.get(Calendar.MONTH)+1) + ""}, {Translate.STRING_3, calendar.get(Calendar.DAY_OF_MONTH) + ""}});
			p.sendError(str);
			return;
		}
		if(dsInst.get(carbonLevel) == null || dsInst.get(carbonLevel).getStatus() != DevilSquareConstant.PREPARE) {
			logger.error("[副本不在准备状态，不允许进入][" + p.getLogString() + "][" + carbonLevel + "]");
			p.sendError(Translate.副本还没开始);
			return;
		}
		if(p.getLevel() < DevilSquareConstant.minEnterLevel) {
			p.sendError(Translate.角色等级不足);
			return;
		}
		DevilSquare ds = dsInst.get(carbonLevel);
		String str = ds.ready2Carbon(p);
		if(str != null) {
			p.sendError(str);
			return;
		} 
		BaseModel bm = baseModelMap.get(carbonLevel);
		if(!removeItem(bm.getTicket(), p, bm.getTicketColor(), bm.getTicketNum(), useUpperFlag, carbonLevel)) {
			if(logger.isDebugEnabled()) {
				logger.debug("[进入恶魔广场失败，没有门票] [" + p.getLogString() + "]");
			}
			return ;
		}
		try {
			ds.notifyEnterCarbon(p);
			if(p.isIsUpOrDown() && p.isFlying()){
				p.downFromHorse();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[进入恶魔广场出异常][" + p.getLogString() + "] ", e);
		}
	}
	/**
	 * 从玩家身上移除物品
	 * @param itemName
	 * @param player
	 * @param num
	 * @param useUpperFlag 如果为false，则玩家没有副本所需颜色门票，有 更高颜色的门票时给出弹框提示
	 * @return
	 */
	public boolean removeItem(String itemName, Player player, int color, int num, boolean useUpperFlag, int carbonLevel){				//需要对物品数量进行判定，首先需要确定门票是否可以堆叠
		//检查物品
		Knapsack bag = player.getKnapsack_common();
		if(bag == null){
			logger.error("取得的玩家背包是null {}", player.getName());
			return false;
		}
		int tempColor = color;
		boolean flag = false;
		for(int i=tempColor; i<=maxColor; i++) {
			if(bag.countArticle(itemName, i) >= num) {
				flag = true;
				tempColor = i;
				break;
			}
		}
		if(!flag) {
			player.sendError(Translate.副本门票不足);
			return false;
		}
		for(int i=0; i<num; i++) {
			int idx = bag.indexOf(itemName, tempColor);
			if(idx<0){
				player.sendError(Translate.没有副本门票);									
				return false;
			}
			if(!useUpperFlag && tempColor != color) {			//使用高级门票加二次确认
				popConfirmWindow(player, carbonLevel);
				return false;
			}
			ArticleEntity ae = bag.remove(idx, "进入恶魔广场", true);
			if (ae != null) {
				logger.info("remove player [{}] tp item [{}]",
						player.getName(), itemName + " color ["+ tempColor +"]");
				ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "进入恶魔城堡删除", null);
			}else{
				logger.error("格子号正确，但移除失败.player [{}],item [{}]",
						player.getName(), itemName + " color ["+ tempColor +"]");
				return false;
			}
		}
		return true;
	}
	
	private void popConfirmWindow(Player p, int carbonLevel) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_ConfirmEnterCarbon option1 = new Option_ConfirmEnterCarbon();
		option1.setText(MinigameConstant.CONFIRM);
		option1.setCarbonLevel(carbonLevel);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
//		String msg = Translate.translateString(Translate.确认使用高级门票, new String[][] {{Translate.STRING_1, country}, {Translate.STRING_2, name}});
		mw.setDescriptionInUUB(Translate.确认使用高级门票);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	
	/**
	 * 
	 * @param refreashRules		baseModel中的刷新规则
	 * @param refreashTimes		当前刷怪波数
	 * @return		
	 */
	public int[] getRefreashModelKey(int key, int refreashTimes) {
		int[] result = new int[2];
		List<Integer> refreashRules = baseModelMap.get(key).getRefreashRule();
		RefreashModel rm = null;
		int temp = 0;
		for(int i=0; i<refreashRules.size(); i++) {
			rm = refreashModelMap.get(refreashRules.get(i));
			temp += rm.getRefreashTimes();
			if(temp > refreashTimes) {
				result[0] = rm.getId();
				int tt = (temp - rm.getRefreashTimes()) < 0 ? 0 : (temp - rm.getRefreashTimes());
				result[1] = refreashTimes - tt;
				return result;
			}
		}
		logger.error("[恶魔广场] [刷新规则错误，当前刷新次数:" + refreashTimes + "] [副本最低进入等级:" + key + "]");
		return null;
	}
	
	public void init() throws Exception {
		
//		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		instance = this;
		loadFileData();
		//启动管理线程
		dsThreads = new DevilSquareThread[devilSquareNum];
		for(int i=0; i<dsThreads.length; i++) {
			dsThreads[i] = new DevilSquareThread();
			dsThreads[i].setName("恶魔广场管理线程-" + i);
			dsThreads[i].heartbeatTime = DevilSquareConstant.DORMANT;
			dsThreads[i].start();
		}
		dsRefreashThread = new DevilSquareRefreashThread();
		dsRefreashThread.setName("[恶魔广场刷怪线程]");
		
		Set<Integer> tempKey = baseModelMap.keySet();			//副本直接初始化出来，未开启时心跳速度慢
		Iterator<Integer> ite = tempKey.iterator();
		int len = 0;
		while(ite.hasNext()) {											//需要设置副本的各种时间等东西
			int key = ite.next();
			DevilSquare ds = new DevilSquare();
			BaseModel bm = baseModelMap.get(key);
			if(bm == null) {
				throw new Exception("[恶魔广场初始化失败，基础配置没找到] [" + key + "]");
			}
			ds.maxNumbers = bm.getMaxPlayer();
			ds.lastTime = potalRule.getTransLastTime() * 1000;
			ds.game = this.createNewGame(bm.getCarBonMap(),"3");
			ds.bornPoints = bm.getBornPoints();
			ds.bossId = bm.getBossId();
			ds.maxMonsterNum = bm.getMaxMonsterNum();
			ds.carbonLastTime = bm.getCarbonLastTime() * 1000;
			ds.kickoutTime = bm.getIntervarl4Kick() * 1000;
			ds.carbonLevel = key;
			if(len >= devilSquareNum) { 
				len = 0;
			}
			ds.intever4Box = bm.getIntever4RefreashBoxs();
			dsThreads[len].setDs(ds); 
			len++;
			dsInst.put(key, ds);
		}
		initSealTime();
		CarbonManager.getInst().registManager(this);
		this.doReg();
		dsRefreashThread.start();
		ServiceStartRecord.startLog(this);
	}
	
	public static void main(String[] args) throws Exception {
		DevilSquareManager ds = new DevilSquareManager();
		ds.setInitFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//carbon//DevilSquare.xls");
		ds.loadFileData();
		
//		Calendar currentCalender = Calendar.getInstance();
//		System.out.println(currentCalender.get(Calendar.MINUTE));
//		System.out.println(currentCalender.get(Calendar.SECOND));
		
		//ds.initSealTime();
//		ds.initSealTime(150);
//		ds.initSealTime(190);
//		ds.initSealTime(220);
		
//		System.out.println(Timestamp.valueOf("2014-08-20 00:00:00").getTime());
//		long b = (180 * 24 * 60 * 60 *1000L);
//		System.out.println(b);
//		long lll = Timestamp.valueOf("2014-02-20 00:00:00").getTime() + b;
//		System.out.println(lll);
//		System.out.println(new Timestamp(lll));
	}
	/**
	 * 初始化副本封印时间
	 * @param key
	 */
	private void initSealTime() {
		Seal seal = SealManager.getInstance().getSeal();
//		int tempKey = key;
//		int tempKey = key == DevilSquareConstant.SEAL_MAX_LEVELS ? 190 : key;
//		Seal seal = new Seal();
//		seal.setSealLevel(220);
//		seal.setSealCanOpenTime(Timestamp.valueOf("2014-02-20 00:00:00").getTime());
		if(seal != null && seal.getSealLevel() <= DevilSquareConstant.SEAL_MAX_LEVELS) {
			long[] sealTime = new long[4];			//封印等级为70,110,150,190时间
			int tempIndex = 4;
			for(int i=0; i<sealTime.length; i++) {
				if(seal.getSealLevel() == SealManager.SEAL_LEVELS[i]) {
					sealTime[i] = seal.getSealCanOpenTime();
					tempIndex = i;
					break;
				}
			}
			for(int i=0; i<sealTime.length; i++) {
				if(i == tempIndex) {
					continue;
				} else if (i < tempIndex) {
					int days = 0;
					for(int j=i; j<tempIndex; j++) {
						days += SealManager.SEAL_OPEN_DAYS[j+1];
					}
					sealTime[i] = seal.getSealCanOpenTime() - (days * 24 * 60 * 60 *1000L);
//					System.out.println("["+SealManager.SEAL_LEVELS[i]+"级可解封时间]"+"days : " + (-days));
				} else {
					int days = 0;
					for(int j=tempIndex; j<i; j++) {
						days +=SealManager.SEAL_OPEN_DAYS[j+1];
					}
					sealTime[i] = seal.getSealCanOpenTime() + (days * 24 * 60 * 60 *1000L);
//					System.out.println("["+SealManager.SEAL_LEVELS[i]+"级可解封时间]"+"[days : " + days + "]");
				}
			}
			for(int i=0; i<DevilSquareConstant.SEAL_OPEN_LEVELS_DEVILSQUARE.length; i++) {
//				System.out.println("["+SealManager.SEAL_LEVELS[i]+"] [解封时间:" + new Timestamp(sealTime[i]) + "]");
				long value = sealTime[i] + DevilSquareConstant.SEAL_OPEN_DAYS_DEVILSQUARE[i] * 24 * 60 * 60 * 1000L;
				carbonSealTimes.put(DevilSquareConstant.SEAL_OPEN_LEVELS_DEVILSQUARE[i], value);
				logger.error("[恶魔广场] ["+DevilSquareConstant.SEAL_OPEN_LEVELS_DEVILSQUARE[i]+"级副本][解封时间:" + new Timestamp(value) + "]");
			}
		}
	}
	/**
	 * 副本是否处于封印状态
	 * @param carbonLevel
	 * @return false已解封
	 */
	public boolean isCarbonSeal(int carbonLevel) {
		if(carbonSealTimes.get(carbonLevel) != null) {
			long currTime = System.currentTimeMillis();
			long openTime = carbonSealTimes.get(carbonLevel);
			if(openTime > currTime) {
				return true;
			}
		}
		return false;			
	}

	public Game createNewGame(String mapname,String reason) throws Exception{
			GameManager gameManager = GameManager.getInstance();
			GameInfo gi = gameManager.getGameInfo(mapname);
			if(gi == null){
				throw new Exception("[副本初始化错误，没找到地图] [reason:"+reason+"] [" + mapname + "]");
			}
			Game newGame = new Game(gameManager,gi);
			newGame.init();
			return newGame; 
	}
	
	public void refreashMonster(Game game, int monsterId, int[] points) {
		try {
			Sprite sprite = MemoryMonsterManager.getMonsterManager().createMonster(monsterId);
			if (sprite != null) {
				sprite.setX(points[0]);
				sprite.setY(points[1]);
				sprite.setBornPoint(new Point(sprite.getX(), sprite.getY()));
				game.addSprite(sprite);
				if(logger.isInfoEnabled()) {
					logger.info("[恶魔广场刷出怪物][" + monsterId + "][坐标=" + points[0] + "、" + points[1] + "]");
				}
			} else {
				logger.error("[恶魔广场刷怪出错][怪物没创建好，monsterId=" + monsterId + "]");
			}
		} catch (Exception e) {
			logger.error("[恶魔广场刷怪出错]", e);
		}
	}
	/**
	 * 世界公告------滚屏+系统
	 * @param player
	 * @param msg
	 */
	public void sendWordMsg(String msg){
		ChatMessage msgA = new ChatMessage();
		msgA.setMessageText("<f color='0x14ff00'>"+msg+"</f>");
		try {
//			ChatMessageService.getInstance().sendHintMessageToSystem(msgA);
			ChatMessageService.getInstance().sendRoolMessageToSystem(msgA);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[恶魔广场，世界通告异常]", e);
		}
	}
	
	private void loadFileData() throws Exception {
		File f = new File(getInitFileName());
		if(!f.exists()){
			throw new Exception("tDevilSquare.xls配表不存在! " + f.getAbsolutePath());
		}
		baseModelMap = new LinkedHashMap<Integer, BaseModel>();
		refreashModelMap = new ConcurrentHashMap<Integer, RefreashModel>();
		transMap = new ConcurrentHashMap<Integer, DevilTranslateModel>();
		
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		
		HSSFSheet sheet = workbook.getSheetAt(0);			//传送门刷出规则
		int rows = sheet.getPhysicalNumberOfRows();
		HSSFRow row2 = sheet.getRow(1);
		potalRule = getPortalRefreashModel(row2);
		
		sheet = workbook.getSheetAt(2);			//副本刷怪逻辑配置
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				RefreashModel temp = getRefreashModel(row);
				refreashModelMap.put(temp.getId(), temp);
			}catch(Exception e) {
				throw new Exception("tDevilSquare.xls第" + (i+1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(1);			//副本基础配置
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				BaseModel temp = getBaseModel(row);
				baseModelMap.put(temp.getLevelLimit(), temp);
			}catch(Exception e) {
				throw new Exception("tDevilSquare.xls第" + (i+1) + "行异常！！", e);
			}
		}
		devilSquareNum = baseModelMap.size();
		
		sheet = workbook.getSheetAt(3);			//翻译
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				DevilTranslateModel temp = getDevilTranslateModel(row);
				transMap.put(temp.getLevel(), temp);
			}catch(Exception e) {
				throw new Exception("tDevilSquare.xls第" + (i+1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(4);			//经验倍数
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int rowNum = 0;
				HSSFCell cell = row.getCell(rowNum++);
				int cId = (int) cell.getNumericCellValue();
				cell = row.getCell(rowNum++);
				Double mul = cell.getNumericCellValue();
				mulMonsterIds.put(cId, mul);
			}catch(Exception e) {
				throw new Exception("tDevilSquare.xls第" + (i+1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(5);		//怪物积分表
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int rowNum = 0;
				HSSFCell cell = row.getCell(rowNum++);
				String sids = cell.getStringCellValue();
				cell = row.getCell(rowNum++);
				int mul = (int) cell.getNumericCellValue();
				String[] spriteId = sids.split(",");
				for(int ii=0; ii<spriteId.length; ii++) {
					int tempL = Integer.parseInt(spriteId[ii]);
					monsterScores.put(tempL, mul);
				}
			}catch(Exception e) {
				throw new Exception("tDevilSquare.xls第" + (i+1) + "行异常！！", e);
			}
		}
	}
	
	public DevilTranslateModel getDevilTranslateModel(HSSFRow row) throws Exception {
		DevilTranslateModel dt = new DevilTranslateModel();
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		int level = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		String plays = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		String bcStory = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		String drops = cell.getStringCellValue();
		String[] temp = drops.split(";");
		long[] temp2 = new long[temp.length];
		String[] dropprobabbly = new String[temp.length];
		for(int i=0; i<temp.length; i++) {
			String[] temp3 = temp[i].split(",");
			Article a = ArticleManager.getInstance().getArticle(temp3[0]);
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.恶魔广场, null, Integer.parseInt(temp3[1]));
			temp2[i] = ae.getId();
			dropprobabbly[i] = temp3[2];
		}
		dt.setDropProbabbly(dropprobabbly);
		dt.setLevel(level);
		dt.setBcStory(bcStory);
		dt.setPlays(plays);
		dt.setDropProps(temp2);
		BaseModel bm = baseModelMap.get(level);
		Article a = ArticleManager.getInstance().getArticle(bm.getTicket());
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.恶魔广场, null, bm.getTicketColor());
		dt.setCostProps(ae.getId());
		dt.setCostNum(bm.getTicketNum());
		return dt;
	}
	
	public PortalRefreashModel getPortalRefreashModel(HSSFRow row) throws Exception {
		PortalRefreashModel pfm = new PortalRefreashModel();
		int transNPCId ;
		String openTime;
		int lastTime;
		String mapName;
		String points;
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		transNPCId = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		openTime = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		lastTime = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		mapName = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		points = cell.getStringCellValue();
		String[] tt = openTime.split(",");
		List<Integer> ll = new ArrayList<Integer>();
		for(String ss : tt) {
			ll.add(Integer.parseInt(ss));
		}
		pfm.setOpenTime(ll);
		pfm.setTransLastTime(lastTime);
		pfm.setTransMap(mapName);
		String[] pp = points.split(",");
		if(pp.length < 2) {
			throw new Exception("[传送门刷新坐标配置错误]");
		}
		Integer[] ii = new Integer[2];
		ii[0] = Integer.parseInt(pp[0]);
		ii[1] = Integer.parseInt(pp[1]);
		List<Integer[]> list = new ArrayList<Integer[]>();
		list.add(ii);
		pfm.setTransPoints(list);
		pfm.setTransNPCId(transNPCId);
		return pfm;
	}
	
	private RefreashModel getRefreashModel(HSSFRow row) throws Exception {
		RefreashModel rm = new RefreashModel();
		int id;
		String refreashPoints;
		int refreashTimes;
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		id = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		refreashPoints = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		refreashTimes = (int) cell.getNumericCellValue();
		List<RefreashRule> refreashRules = new ArrayList<RefreashRule>();
		for(int i=0; i<refreashTimes; i++) {
			cell = row.getCell(rowNum++);
			int interval = (int) cell.getNumericCellValue();
			cell = row.getCell(rowNum++);
			int intervalRevive = (int) cell.getNumericCellValue();
			cell = row.getCell(rowNum++);
			int monsterIds = (int) cell.getNumericCellValue();
			RefreashRule rfr = new RefreashRule(interval, monsterIds, 1, intervalRevive);
			refreashRules.add(rfr);
		}
		
		rm.setId(id);
		List<Integer[]> rpp = new ArrayList<Integer[]>();
		String[] p = refreashPoints.split(";");
		for(String ss : p){
			String[] pp = ss.split(",");
			if(pp == null){
				pp = ss.split("，");
			}
			
			if(pp.length != 2) {
				throw new Exception("坐标点集合配置不对ss:"+ss+"--pp:"+(pp==null?"":Arrays.toString(pp)));
			}
			Integer[] ii = new Integer[2];
			ii[0] = Integer.parseInt(pp[0]);
			ii[1] = Integer.parseInt(pp[1]);
			rpp.add(ii);
		}
		rm.setRefreashRules(refreashRules);
		rm.setRefreashPoints(rpp);
		rm.setRefreashTimes(refreashTimes);
		return rm;
	}
	/**
	 * 副本基础表sheet页解析读取
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private BaseModel getBaseModel(HSSFRow row) throws Exception {
		BaseModel bm = new BaseModel();
		int levelLimit;
		int maxNum;
		String ticket;
		int ticketNum;
		String carBonMap;
		String bornPoints;
		int readyTime;
		int lastTime;
		String refreashRule;
		
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		levelLimit = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		String cMapName = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		maxNum = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		ticket = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		int ticketColor = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		ticketNum = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		carBonMap = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		bornPoints = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		readyTime = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		lastTime = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		int intervarl4Kick = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		refreashRule = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		int maxMonsterNum = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		int interval4Clean = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		int interval4Boss = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		int bossId = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		String bossPoint = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		String intever4Box = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		String boxPoints = cell.getStringCellValue();
		cell = row.getCell(rowNum++);
		int boxNum = (int) cell.getNumericCellValue();
		
		bm.setLevelLimit(levelLimit);
		bm.setcMapName(cMapName);
		bm.setTicketColor(ticketColor);
		bm.setMaxPlayer(maxNum);
		bm.setTicket(ticket);
		bm.setTicketNum(ticketNum);
		bm.setCarBonMap(carBonMap);
		bm.setMaxMonsterNum(maxMonsterNum);
		bm.setBoxNum(boxNum);
		String[] ib = intever4Box.split(",");
		List<Integer> ibb = new ArrayList<Integer>();
		for(int i=0; i<ib.length; i++) {
			ibb.add(Integer.parseInt(ib[i]));
		}
		bm.setIntever4RefreashBoxs(ibb);
		//设置出生坐标点集合
		List<Integer[]> pointList = new ArrayList<Integer[]>();
		String[] p = bornPoints.split(";");
		for(String ss : p){
			String[] pp = ss.split(",");
			if(pp.length != 2) {
				throw new Exception("坐标点集合配置不对");
			}
			Integer[] ii = new Integer[2];
			ii[0] = Integer.parseInt(pp[0]);
			ii[1] = Integer.parseInt(pp[1]);
			pointList.add(ii);
		}
		List<Integer[]> boxPointList = new ArrayList<Integer[]>();
		String[] pb = boxPoints.split(";");
		for(String ss : pb){
			String[] pp = ss.split(",");
			if(pp.length != 2) {
				throw new Exception("坐标点集合配置不对");
			}
			Integer[] ii = new Integer[2];
			ii[0] = Integer.parseInt(pp[0]);
			ii[1] = Integer.parseInt(pp[1]);
			boxPointList.add(ii);
		}
		bm.setBoxPoints(boxPointList);
		bm.setIntervarl4Clean(interval4Clean);
		bm.setInterverl4Boss2(interval4Boss);
		bm.setBossId(bossId);
		bm.setIntervarl4Kick(intervarl4Kick);
		String[] pp = bossPoint.split(",");
		if(pp.length != 2) {
			throw new Exception("boss坐标点集合配置不对");
		}
		Integer[] ii = new Integer[2];
		ii[0] = Integer.parseInt(pp[0]);
		ii[1] = Integer.parseInt(pp[1]);
		bm.setBossPoint(ii);
		bm.setBornPoints(pointList);
		bm.setPrepareTime(readyTime);
		bm.setCarbonLastTime(lastTime);
		String[] tempRe = refreashRule.split(",");
		List<Integer> reList = new ArrayList<Integer>();
		int maxReTimes = 0;
		for(int i=0; i<tempRe.length; i++) {
			int tempI = Integer.parseInt(tempRe[i]);
			maxReTimes += refreashModelMap.get(tempI).getRefreashTimes();
			reList.add(tempI);
		}
		bm.setMaxRefreashTimes(maxReTimes);
		bm.setRefreashRule(reList);
		carbonMaps.add(carBonMap);
		
		return bm;
	}
	
	public String getInitFileName() {
		return initFileName;
	}

	public void setInitFileName(String initFileName) {
		this.initFileName = initFileName;
	}
	@Override
	public int preNotifyMinits() {
		return DevilSquareConstant.PRE_NOTIFY;
	}

	@Override
	public int maxNotifyMinits() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public void proc(Event evt) {
		// TODO Auto-generated method stub
		if(logger.isDebugEnabled()) {
			logger.debug("[恶魔广场] [收到玩家上线通知][" + evt.id + "]");
		}
		switch (evt.id) {
		case Event.PLAYER_LOGIN:
			if(evt instanceof EventPlayerLogin) {
				EventPlayerLogin el = (EventPlayerLogin) evt;
				if(logger.isDebugEnabled()) {
					logger.debug("[恶魔广场] [收到玩家上线通知2][" + el.player + "]");
				}
				if(el.player != null) {
					notifyPlayerEnterServer(el.player);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void doReg() {
		// TODO Auto-generated method stub
		EventRouter.register(Event.PLAYER_LOGIN, this);
	}

	@Override
	public String getActivityName() {
		// TODO Auto-generated method stub
		return ActivityConstant.恶魔城堡;
	}

	@Override
	public List<String> getActivityOpenTime(long now) {
		// TODO Auto-generated method stub
		List<Integer> list = this.getActHour();
		List<String> result = new ArrayList<String>();
		for (int i=0; i<list.size(); i++) {
			result.add(list.get(i) + ":00");
		}
		return result;
	}

	@Override
	public boolean isActivityTime(long now) {
		// TODO Auto-generated method stub
		for (DevilSquare ds : dsInst.values()) {
			if (ds.getStatus() == DevilSquareConstant.PREPARE) {
				return true;
			}
		}
		return false;
	}
}
