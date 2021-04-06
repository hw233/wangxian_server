package com.fy.engineserver.downcity.downcity3;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiaZuLivenessType;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.BATTLE_GUWU_INFO_REQ;
import com.fy.engineserver.message.BATTLE_GUWU_INFO_RES;
import com.fy.engineserver.message.BOOTH_BATTLE_INFO_REQ;
import com.fy.engineserver.message.BOOTH_BATTLE_INFO_RES;
import com.fy.engineserver.message.BOOTH_FIRST_PAGE_REQ;
import com.fy.engineserver.message.BOOTH_FIRST_PAGE_RES;
import com.fy.engineserver.message.BOOTH_GULI_REQ;
import com.fy.engineserver.message.BOOTH_HELP_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAYER_IN_SPESCENE2_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;


public class BossCityManager extends SubSystemAdapter implements Runnable {

	//活动入口,弹框入口
	//进入规则,房间安排
	//boss处理,显示通知，鼓励功能
	//副本结束,发放奖励
	
	private static BossCityManager self;
	
	public static Logger logger = LoggerFactory.getLogger(BossCityManager.class);
	
	private String filePath;
	
	private int startHour[] = {13,13};
	private int startMinute[] = {0,10};
	private String openTimeStr;
	
	public final int BOSS_CONFIG_SHEET = 0;
	public final int PLAYER_REWARD_SHEET = 1;
	public final int JIAZU_REWARD_SHEET = 2;
	public final int PLAYER_FIGHT_SHEET = 3;
	public final int JIAZU_FIGHT_SHEET = 4;
	
	private List<Integer> bossIds = new ArrayList<Integer>();
	private Map<Integer, PlayerReward> pRewards = new ConcurrentHashMap<Integer, PlayerReward>();
	private Map<Integer, JiaZuReward> jRewards = new ConcurrentHashMap<Integer, JiaZuReward>();
	private Map<Integer, Long> pGuLi = new ConcurrentHashMap<Integer, Long>();
	public Map<Integer, Long> jGuLi = new ConcurrentHashMap<Integer, Long>();
	
	protected Map<Long, CityPlayer> battleInfos = new ConcurrentHashMap<Long, CityPlayer>();
	protected Map<Long, CityJiaZu> jiazuInfos = new ConcurrentHashMap<Long, CityJiaZu>();
	public Map<Long, Integer> yuanzhenGuLi = new ConcurrentHashMap<Long, Integer>();
	
	private List<BossRoom> rooms = new ArrayList<BossRoom>();
	
	private boolean startRun = true;
	private long sleepTime = 1000L;
	private Monster boss;
	private int levelLimit = 20;
	public static int roomEnterLimit = 30;
	private int roomLimit = 100;
	public static final String mapname = "jiefengBOSStiaozhanditu";
	private int x = 1116;
	private int y = 1111;
	private int bossX = 1602;
	private int bossY = 1023;
	
	private List<CityGame> games = new ArrayList<CityGame>();
	
	private String ddcFile;
	private DiskCache cache;
	private final static String BOSS_ID_KEY = "BOSS_ID_KEY";
	private final static String LAST_UPDATE_BOSS_ID_KEY = "LAST_UPDATE_BOSS_ID_KEY";
	
	//线程相关
	protected int billboardShowNum = 5;
	int MAX_HANDLE_TASKS = 10;
	private long lastThreadCheckTime;
	private long threadCheckTimeLength = 60 * 1000L;
	public static final long GAME_SLEEP_TIME = 200;
	
	List<CityThread> threadPool = Collections.synchronizedList(new ArrayList<CityThread>());
	
	public static BossCityManager getInstance(){
		return self;
	}
	
	private static int cityId;
	public synchronized static int nextId(){
		if(cityId >= Integer.MAX_VALUE){
			cityId = 0;
		}
		cityId++;
		return cityId;
	}
	
	public static Map<Long,Long> entenP = new HashMap<Long, Long>();
	
	
	public void init() throws Exception{
		self = this;
		File file = new File(ddcFile);
		cache = new DefaultDiskCache(file, null, "bossCityId", 100L * 365 * 24 * 3600 * 1000L);
		initFile();
		initBoss();
		new Thread(this,"BossCityManager").start();
		GameNetworkFramework.getInstance().addSubSystem(this);
		ServiceStartRecord.startLog(this);
	}
	public boolean updateBossDebug = false;
	public void initBoss() throws Exception{
		Integer bossId = (Integer)cache.get(BOSS_ID_KEY);
		Long lastUpdateTime = (Long)cache.get(LAST_UPDATE_BOSS_ID_KEY);
		boolean needUpdate = false;
		int updateType = 0;
		if(bossId == null){
			needUpdate = true;
			updateType = 1;
		}
		if(lastUpdateTime != null && !isSameDay(lastUpdateTime, System.currentTimeMillis())){
			needUpdate = true;
			updateType = 2;
		}
		if(needUpdate || updateBossDebug){
			if(bossIds.size() == 0){
				throw new Exception("boss配置错误");
			}
			Random random = new Random();
			int index = random.nextInt(bossIds.size());
			bossId = bossIds.get(index);
			lastUpdateTime = System.currentTimeMillis();
			cache.put(BOSS_ID_KEY, bossId);
			cache.put(LAST_UPDATE_BOSS_ID_KEY, lastUpdateTime);
		}
		boss = MemoryMonsterManager.getMonsterManager().getMonster(bossId);
		if(boss == null){
			boss = MemoryMonsterManager.getMonsterManager().createMonster(bossId);
		}
		if(boss == null)
			throw new Exception("初始化boss失败:"+bossId);
		logger.warn("[服务器启动] [初始化boss] [0:不更新,1:boss不存在,2:隔天更新-->{}] [id:{}] [bossId:{}] [name:{}] [{}]",new Object[]{updateType,boss.getId(),bossId,boss.getName(),updateBossDebug});
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public boolean isSameDay(long time1, long time2) {
		String str1 = sdf.format(time1);
		String str2 = sdf.format(time2);
		return str1.equals(str2);
	}
	
	public void initFile() throws Exception{
		File file = new File(filePath);
		if(filePath == null || !file.exists() || !file.isFile()){
			throw new Exception("BossCityManager filePath ["+filePath+"] is error");
		}
		pRewards.clear();
		jRewards.clear();
		pGuLi.clear();
		jGuLi.clear();
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);

		HSSFSheet sheet = workbook.getSheetAt(BOSS_CONFIG_SHEET);
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				int bossId = StringTool.getCellValue(row.getCell(index++), Integer.class);
				String bossName = StringTool.getCellValue(row.getCell(index++), String.class);
				bossIds.add(bossId);
				logger.info("[boss副本] [加载bossId] [id:{}] [name:{}]",new Object[]{bossId,bossName});
			}
		}
		
		sheet = workbook.getSheetAt(PLAYER_REWARD_SHEET);
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				PlayerReward reward = new PlayerReward();
				int sealLevel = StringTool.getCellValue(row.getCell(index++), Integer.class);
				String canYuReward = StringTool.getCellValue(row.getCell(index++), String.class);
				if(canYuReward == null || canYuReward.isEmpty()){
					throw new Exception("BossCityManager canYuReward not exist1");
				}
				String [] rs = canYuReward.split(",");
				for(String r : rs){
					if(!articleIsExist(r)){
						throw new Exception("BossCityManager canYuReward not exist2:"+r);
					}
				}
				
				int rankStep = 8;
				String rewardStr = "";
				String rewardNames [] = new String[0];
				String rankRewards [] [] = new String[rankStep][];
				for(int j = 0; j < rankStep; j++){
					rewardStr = StringTool.getCellValue(row.getCell(index++), String.class);
					rewardNames = rewardStr.split(",");
					for(String name : rewardNames){
						if(!articleIsExist(name)){
							throw new Exception("BossCityManager name not exist");
						}
					}
					rankRewards[j] = rewardNames;
				}
				String showRewards = StringTool.getCellValue(row.getCell(index++), String.class);
				reward.setCanYuReward(rs);
				reward.setRankRewards(rankRewards);
				reward.setShowRewards(showRewards.split(","));
				pRewards.put(sealLevel, reward);
				logger.info("[boss副本] [加载PLayer-Reward] [封印:{}] [{}]",new Object[]{sealLevel,reward});
			}
		}
		
		sheet = workbook.getSheetAt(JIAZU_REWARD_SHEET);
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				JiaZuReward reward = new JiaZuReward();
				int sealLevel = StringTool.getCellValue(row.getCell(index++), Integer.class);
				
				int rankStep = 7;
				String rewardStr = "";
				String rewardNames [] = new String[0];
				int ranks [] = new int[rankStep];
				int lingmings [] = new int[rankStep];
				int zijins [] = new int[rankStep];
				for(int j = 0; j < rankStep; j++){
					rewardStr = StringTool.getCellValue(row.getCell(index++), String.class);
					rewardNames = rewardStr.split(",");
					ranks[j] = j+1;
					lingmings[j] = Integer.parseInt(rewardNames[0]);
					zijins[j] = Integer.parseInt(rewardNames[1]);
				}
				reward.setRank(ranks);
				reward.setLingMai(lingmings);
				reward.setZiJin(zijins);
				jRewards.put(sealLevel, reward);
				logger.info("[boss副本] [加载JiaZu-Reward] [封印:{}] [{}]",new Object[]{sealLevel,reward});
			}
		}	
		
		sheet = workbook.getSheetAt(PLAYER_FIGHT_SHEET);
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				int bili = StringTool.getCellValue(row.getCell(index++), Integer.class);
				long cost = StringTool.getCellValue(row.getCell(index++), Long.class);
				pGuLi.put(bili, cost);
				logger.info("[boss副本] [加载Player-GuLi] [比例:{}] [花费:{}]",new Object[]{bili,cost});
			}
		}	
		
		sheet = workbook.getSheetAt(JIAZU_FIGHT_SHEET);
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				int bili = StringTool.getCellValue(row.getCell(index++), Integer.class);
				long cost = StringTool.getCellValue(row.getCell(index++), Long.class);
				jGuLi.put(bili, cost);
				logger.info("[boss副本] [加载JiaZu-GuLi] [比例:{}] [花费:{}]",new Object[]{bili,cost});
			}
		}
	}
	
	
	public boolean isBossCityGame(Player player) {
		Game game = player.getCurrentGame();
		if (game != null && game.gi != null && mapname.equals(game.gi.name)) {
			return true;
		}
		return false;
	}
	
	public boolean isBossCityGame(String mapName) {
		if (mapname.equals(mapName)) {
			return true;
		}
		return false;
	}
	
	public boolean articleIsExist(String name){
		return ArticleManager.getInstance().getArticle(name) == null ? false : true;
	}
	
	public String getOpenTimeStr(){
		if(openTimeStr == null){
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<startHour.length;i++){
				sb.append(startHour[i]).append(":").append(startMinute[i]).append("-");
			}
			openTimeStr = sb.toString().substring(0, sb.toString().lastIndexOf("-"));
		}
		return openTimeStr;
	}
	
	public boolean isOpen(){
		Calendar cl = Calendar.getInstance();
		int hour = cl.get(Calendar.HOUR_OF_DAY);
		int minute = cl.get(Calendar.MINUTE);
		return (startHour[0] <= hour && hour <= startHour[1]) && (startMinute[0] <= minute && minute < startMinute[1] - 1);
	}
	
	public boolean isClose(){
		Calendar cl = Calendar.getInstance();
		
		cl.set(Calendar.HOUR_OF_DAY, startHour[0]);
		cl.set(Calendar.SECOND, 0);
		cl.set(Calendar.MINUTE, startMinute[0]);
		long startTime = cl.getTimeInMillis();
		
		cl.set(Calendar.HOUR_OF_DAY, startHour[1]);
		cl.set(Calendar.SECOND, 0);
		cl.set(Calendar.MINUTE, startMinute[1]);
		long endTime = cl.getTimeInMillis();
		long now = System.currentTimeMillis();
		if(startTime <= now && now < endTime){
			return false;
		}
		return true;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public void run() {
		while(startRun){
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				JiazuManager2.instance.noticePlayer();
				noticeStartFight();
				rewardPlayer();
				rewardJiaZu();
				if(System.currentTimeMillis() - lastThreadCheckTime >= threadCheckTimeLength){
					lastThreadCheckTime = System.currentTimeMillis();
					Iterator<CityThread> it = threadPool.iterator();
					while(it.hasNext()){
						CityThread thread = (CityThread)it.next();
						if(thread != null && thread.isEmpty()){
							thread.destory();
							it.remove();
							logger.warn("[boss副本] [删除线程任务] [线程池数:{}]", new Object[]{threadPool.size()});
						}
					}
				}
				
				if(boss == null){
					boss = MemoryMonsterManager.getMonsterManager().createMonster(20113631);
					logger.warn("[boss副本] [心跳中创建boss] [boss:{}]",new Object[]{boss});
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("[boss副本] [心跳异常] [boss:{}] [{}]",new Object[]{boss,e});
			}
		}
	}
	
	public boolean hasNoticeToday = false;
	public void noticeStartFight(){
		if(!isClose()){
			if(hasNoticeToday){
				return;
			}
			clearDirtyData();
			hasNoticeToday = true;
			Player ps [] = PlayerManager.getInstance().getOnlinePlayers();
			if(ps == null || ps.length <= 0){
				return;
			}
			
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setTitle(Translate.boss挑战活动开启了);
			mw.setDescriptionInUUB(Translate.boss挑战活动开启了);
			Option_Enter_City_Confirm sure = new Option_Enter_City_Confirm();
			sure.setText(Translate.确定);
			mw.setOptions(new Option[] { sure });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			for(Player p : ps){
				if(p == null){
					continue;
				}
				if(DownCityManager2.instance.inCityGame(p)){
					continue;
				}
				p.addMessageToRightBag(res);
				logger.warn("[弹框通知玩家参加全民boss] ["+p.getLogString()+"]");
			}
		}else{
			//rooms.clear();
			hasNoticeToday = false;
		}
	}
	
	public void addTask(CityGame game){
		CityThread effectThread = null;
		for(CityThread t : threadPool){
			if(t != null && t.isEffect()){
				effectThread = t;
			}
		}
		if(effectThread == null){
			int nextId = threadPool.size();
			effectThread = new CityThread("boss副本心跳-"+(++nextId));
			new Thread(effectThread,effectThread.tName).start();
			threadPool.add(effectThread);
		}
		effectThread.addTask(game);
	}
	
	public long getNextOpenTime(){
		long now = System.currentTimeMillis();
		Calendar cl = Calendar.getInstance();
		cl.set(Calendar.HOUR_OF_DAY, startHour[0]);
		cl.set(Calendar.MINUTE, startMinute[0]);
		long startTime = cl.getTimeInMillis();
		cl.set(Calendar.HOUR_OF_DAY, startHour[1]);
		cl.set(Calendar.MINUTE, startMinute[1]);
		long endTime = cl.getTimeInMillis();
		if(now >= endTime){
			return TimeTool.oneDay - (now - startTime);
		}
		if(now < startTime){
			return startTime - now;
		}
		return -1;
	}
	
	public synchronized void entenRoom(Player p){
		long now = System.currentTimeMillis();
		if(p.getLevel() < levelLimit){
			p.sendError(Translate.级20后再打boss吧);
			return;
		}
		if(p.getJiazuId() <= 0){
			p.sendError(Translate.家族不存在不能挑战);
			return;
		}
		if(rooms.size() >= roomLimit){
			p.sendError(Translate.副本已满);
			return;
		}
		
		BossRoom room = p.room;
		if(room == null){
			for(BossRoom r : rooms){
				if(!r.isFull()){
					room = r;
					break;
				}
			}
		}
		if(room == null){
			CityGame city = creatCity();
			if(city == null){
				p.sendError(Translate.创建副本失败);
				logger.warn("[玩家进入房间] [全民boss] [失败:创建副本1] [{}] [{}] [cost:{}]",new Object[]{room,p.getLogString(),(System.currentTimeMillis()-now)});
				return;
			}
			room = new BossRoom();
			rooms.add(room);
			room.setCity(city);
			city.setRoom(room);
		}
		
		CityGame city = room.getCity();
		if(city == null || city.state == 1){
			p.sendError(Translate.创建副本失败);
			logger.warn("[玩家进入房间] [全民boss] [失败:创建副本2] [{}] [city:{}] [{}] [cost:{}]",new Object[]{room,city,p.getLogString(),(System.currentTimeMillis()-now)});
			return;
		}
		
		CityPlayer info = battleInfos.get(p.getId());
		if(info == null){
			info = new CityPlayer();
			info.setPlayerId(p.getId());
			info.setPlayerName(p.getName());
			battleInfos.put(p.getId(), info);
		}
		
		CityJiaZu jiazuinfo = jiazuInfos.get(p.getJiazuId());
		if(jiazuinfo == null){
			jiazuinfo = new CityJiaZu();
			jiazuinfo.setJiaZuId(p.getJiazuId());
			jiazuinfo.setJiazuName(p.getJiazuName());
			jiazuInfos.put(p.getJiazuId(), jiazuinfo);
		}
		
		room.entenRoom(p.getId());
		if(!room.isIsrun()){
			room.setIsrun(true);
			addTask(city);
		}
		p.room = room;
		p.setPkMode(Player.和平模式);
		p.getCurrentGame().transferGame(p, new TransportData(0, 0, 0, 0, mapname, x, y));
		AchievementManager.getInstance().record(p, RecordAction.世界BOSS);
		logger.warn("[玩家进入房间] [全民boss] [房间数:{}] [{}] [{}] [cost:{}]",new Object[]{rooms.size(), room.getCity().getId(),p.getLogString(),(System.currentTimeMillis()-now)});
	}
	
	public CityGame creatCity(){
		Game game = createGame();
		if(game != null){
			CityGame cGame = new CityGame(nextId(), game);
			Integer bossId = (Integer)cache.get(BOSS_ID_KEY);
			Monster m = MemoryMonsterManager.getMonsterManager().createMonster(bossId);
			m.setX(bossX);
			m.setY(bossY);
			game.addSprite(m);
			games.add(cGame);
			logger.warn("[进入房间创建boss] ["+m.getName()+"] ["+m.getId()+"]");
			return cGame;
		}
		return null;
	}
	
	private void clearDirtyData(){
		logger.warn("[新活动开启] [清理缓存数据] [games:{}] [rooms:{}] [battleInfos:{}] [jiazuInfos:{}]",new Object[]{games.size(), rooms.size(), battleInfos.size(), jiazuInfos.size()});
		for(BossRoom r : rooms){
			if(r != null){
				r.clearRoomData();
			}
		}
		games.clear();
		rooms.clear();
		battleInfos.clear();
		jiazuInfos.clear();
	}
	
	public Game createGame(){
		GameManager gameManager = GameManager.getInstance();
		GameInfo gi = gameManager.getGameInfo(mapname);
		if(gi == null){
			logger.warn("[newGame] [失败:对应的地图信息不存在] [{}]", new Object[]{mapname});
			return null;
		}
		try {
			Game newGame = new Game(gameManager,gi);
			newGame.init();
			return newGame;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[newGame] [失败:game初始化失败] [{}] [{}]", new Object[]{mapname,e});
			return null;
		}
	}
	
	public int getPlayerRewardInx(int rank){
		if(rank > 0 && rank < 4){
			return rank - 1;
		}else if(rank >= 4 && rank <= 10){
			return 3;
		}else if(rank >= 11 && rank <= 20){
			return 4;
		}else if(rank >= 21 && rank <= 50){
			return 5;
		}else if(rank >= 51 && rank <= 100){
			return 6;
		}else if(rank >= 101 && rank <= 500){
			return 7;
		}
		return -1;
	}
	
	public int getJiaZuRewardInx(int rank){
		if(rank > 0 && rank < 4){
			return rank - 1;
		}else if(rank >= 4 && rank <= 5){
			return 3;
		}else if(rank >= 6 && rank <= 10){
			return 4;
		}else if(rank >= 11 && rank <= 20){
			return 5;
		}else if(rank >= 21 && rank <= 50){
			return 6;
		}
		return 7;
	}
	
	public void rewardPlayer(){
		try {
			if(isClose()){
				if(battleInfos.size() <= 0){
					return;
				}
				CityPlayer[] cPlayers2 = battleInfos.values().toArray(new CityPlayer[]{});
				CityPlayer[] cPlayers = cPlayers2;
				battleInfos.clear();
				if(cPlayers != null && cPlayers.length > 0){
					Arrays.sort(cPlayers, new Comparator<CityPlayer>() {
						@Override
						public int compare(CityPlayer o1, CityPlayer o2) {
							return new Long(o2.getPlayerDamage()).compareTo(new Long(o1.getPlayerDamage()));
						}
					});
					int rank = 0;
					PlayerReward reward = BossCityManager.getInstance().getpRewards().get(SealManager.getInstance().getSealLevel());
					if(reward == null){
						BossCityManager.logger.warn("[副本结束] [玩家奖励出错] [sealLevel:{}]",new Object[]{SealManager.getInstance().getSealLevel()});
					}else{
						for(CityPlayer p : cPlayers){
							if(p != null){
								rank++;
								int index = BossCityManager.getInstance().getPlayerRewardInx(rank);
								if(index >= 0){
									String names [] = reward.getRankRewards()[index];
									Player player = PlayerManager.getInstance().getPlayer(p.getPlayerId());
									JiazuManager2.instance.addLiveness(player, JiaZuLivenessType.世界boss_每个参与人员);
									//名次奖励
									String mess = Translate.translateString(Translate.boss奖励内容, new String[][] { { Translate.STRING_1, rank+""}});
									if(p.getPlayerDamage() > 0){
										List<ArticleEntity> list = new ArrayList<ArticleEntity>();
										for(String name : names){
											Article article = ArticleManager.getInstance().getArticle(name);
											ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.boss副本, player, article.getColorType(), 1, true);
											list.add(ae);
										}
										MailManager.getInstance().sendMail(p.getPlayerId(), list.toArray(new ArticleEntity[]{}), Translate.boss奖励, mess, 0, 0, 0, "全民boss奖励");
									}
									
									//参与奖励
									String[] rs = reward.getCanYuReward();
									if(rs != null && rs.length > 0){
										List<ArticleEntity> aes = new ArrayList<ArticleEntity>();
										for(int i=0;i<reward.getCanYuReward().length;i++){
											String canyuReward = reward.getCanYuReward()[i];
											Article canYuArticle = ArticleManager.getInstance().getArticle(canyuReward);
											ArticleEntity ae2 = ArticleEntityManager.getInstance().createEntity(canYuArticle, true, ArticleEntityManager.boss副本, player, canYuArticle.getColorType(), 1, true);
											aes.add(ae2);
										}
										MailManager.getInstance().sendMail(p.getPlayerId(), aes.toArray(new ArticleEntity[]{}), Translate.boss参与奖励, mess, 0, 0, 0, "全民boss奖励");
									}else{
										BossCityManager.logger.warn("[副本结束] [玩家奖励] [错误:参与奖励不存在] [参与奖励:{}] [rank:{}] [sealLevel:{}] [{}]",new Object[]{(rs != null?Arrays.toString(rs):"null"),rank,SealManager.getInstance().getSealLevel(),player.getLogString()});
									}
									BossCityManager.logger.warn("[副本结束] [玩家奖励] [成功] [参与奖励:{}] [rank:{}] [sealLevel:{}] [{}] [{}]",
											new Object[]{ (rs != null?Arrays.toString(rs):"null"),rank,SealManager.getInstance().getSealLevel(),battleInfos.size()+"/"+cPlayers.length, player.getLogString()});
								}
								
							}
						}
					}
				}else {
					BossCityManager.logger.warn("[副本结束] [玩家奖励] [失败:数据为空========]");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			BossCityManager.logger.warn("[副本结束] [玩家奖励异常] [sealLevel:{}] [{}]",new Object[]{SealManager.getInstance().getSealLevel(),e});
		}
	}
	
	public void rewardJiaZu(){
		try {
			if(isClose()){
				if(jiazuInfos.size() <= 0){
					return;
				}
				CityJiaZu[] ds2 = jiazuInfos.values().toArray(new CityJiaZu[]{});
				CityJiaZu[] ds = ds2; 
				jiazuInfos.clear();
				if(ds != null && ds.length > 0){
					Arrays.sort(ds, new Comparator<CityJiaZu>() {
						@Override
						public int compare(CityJiaZu o1, CityJiaZu o2) {
							return new Long(o2.getJiaZuDamage()).compareTo(new Long(o1.getJiaZuDamage()));
						}
					});
					int rank = 0;
					JiaZuReward reward = BossCityManager.getInstance().getjRewards().get(SealManager.getInstance().getSealLevel());
					if(reward == null){
						BossCityManager.logger.warn("[副本结束] [家族奖励出错] [sealLevel:{}]",new Object[]{SealManager.getInstance().getSealLevel()});
					}else{
						for(CityJiaZu p : ds){
							if(p != null){
								rank++;
								if(p.getJiaZuDamage() <= 0){
									continue;
								}
								Jiazu jiazu = JiazuManager.getInstance().getJiazu(p.getJiaZuId());
								if(rank == 1){
									JiazuManager2.instance.addLiveness(jiazu, JiaZuLivenessType.世界boss_排名1);
								}else if(rank == 2){
									JiazuManager2.instance.addLiveness(jiazu, JiaZuLivenessType.世界boss_排名2);
								}else if(rank == 3){
									JiazuManager2.instance.addLiveness(jiazu, JiaZuLivenessType.世界boss_排名3);
								}else if(rank == 4 || rank == 5){
									JiazuManager2.instance.addLiveness(jiazu, JiaZuLivenessType.世界boss_排名4_5);
								}else if(rank == 6 || rank == 7 || rank == 8 || rank == 9 || rank == 10){
									JiazuManager2.instance.addLiveness(jiazu, JiaZuLivenessType.世界boss_排名6_10);
								}else if(rank >= 11 && rank <= 20){
									JiazuManager2.instance.addLiveness(jiazu, JiaZuLivenessType.世界boss_排名11_20);
								}
								int index = BossCityManager.getInstance().getJiaZuRewardInx(rank);
								if(index >= 0){
									int lm = reward.getLingMai()[index] * 1000;
									int zj = reward.getZiJin()[index] * 1000;
									jiazu.setConstructionConsume(jiazu.getConstructionConsume() + lm);
									jiazu.setJiazuMoney(jiazu.getJiazuMoney() + zj);
									BossCityManager.logger.warn("[副本结束] [家族奖励] [成功] [家族:{}] [rank:{}] [index:{}] [灵脉:{}] [资金:{}] [sealLevel:{}]",
											new Object[]{ p.getJiaZuId()+"--"+p.getJiazuName(), rank,index,lm,zj,SealManager.getInstance().getSealLevel()});
								}
							}
						}
					}
				}else{
					BossCityManager.logger.warn("[副本结束] [家族奖励] [失败:数据为空========]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			BossCityManager.logger.warn("[副本结束] [家族奖励异常] [sealLevel:{}] [{}]",new Object[]{SealManager.getInstance().getSealLevel(),e});
		}
	}
	
	boolean openLog = false;
	public int addGuLiDamageOfYuanZheng(Fighter f,int damage){
		if(damage <= 0){
			return 0;
		}
		openLog = false;
		if(f != null && f instanceof Player){
			if(((Player)f).getName().equals("左迎天")){
				openLog = true;
			}
		}
		if(JiazuManager2.instance.isOpen() || openLog){
			Player p = null;
			try {
				if(f instanceof Player){
					p = (Player)f;
				}
//				else if(f instanceof Pet){
//					p = ((Pet)f).getMaster();
//				}
				if(openLog){
					Integer currGuLi = yuanzhenGuLi.get(new Long(p.getJiazuId()));
					Skill.logger.warn("[额外增加远征伤害] [yuanzhenGuLi:"+yuanzhenGuLi.size()+"] [currGuLi:"+currGuLi+"] [damage:"+damage+"] [jiazuid:"+p.getJiazuId()+"] ["+p.getLogString()+"]");
				}
				if(p == null){
					if(openLog){
						Skill.logger.warn("[额外增加远征伤害] [失败:玩家不存在] [{}] [damage:{}]",new Object[]{f.getName(),damage});
					}
					return 0;
				}
				if(p.getJiazuId() <= 0){
					if(openLog){
						Skill.logger.warn("[额外增加远征伤害] [失败:家族id不正确] [{}] [damage:{}]",new Object[]{f.getName(),damage});
					}
					return 0;
				}
				
				if(p.getCurrentGame() == null || p.getCurrentGame().gi == null || p.getCurrentGame().gi.name == null){
					if(openLog){
						Skill.logger.warn("[额外增加远征伤害] [失败:玩家当前地图有问题] [game:{}] [gi:{}] [damage:{}] [{}]",
						new Object[]{p.getCurrentGame(),(p.getCurrentGame()==null?"null":p.getCurrentGame().gi),damage,p.getLogString()});
					}
					return 0;
				}
				
				if(!p.getCurrentGame().gi.name.equals("zhanmotianyu")){
					if(openLog){
						Skill.logger.warn("[额外增加远征伤害] [失败:玩家不在远征副本] [game:{}] [damage:{}] [{}]",new Object[]{p.getCurrentGame().gi.name,damage,p.getLogString()});
					}
					return 0;
				}
				
				//宠物算玩家伤害，并且共享玩家的鼓舞值
				Integer currGuLi = yuanzhenGuLi.get(new Long(p.getJiazuId()));
				if(currGuLi == null || currGuLi == 0){
					if(openLog){
						Skill.logger.warn("[额外增加远征伤害] [失败:鼓励值为0 ，不予计算] [damage:{}] [家族id:{}] [{}]",new Object[]{damage,p.getJiazuId(),p.getLogString()});
					}
					return 0;
				}
				double addProp = (double)currGuLi / 100;
				long addDamage = (long)(damage * addProp);
				
				Skill.logger.warn("[额外增加远征伤害] [成功] [技能伤害:{}] [增加伤害:{}] [百分比:{}] [鼓舞级别:{}] [玩家:{}] [家族:{}]",
						new Object[]{damage,addDamage,addProp,currGuLi, p.getName(),p.getJiazuName()});
				return (int)addDamage;
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("[额外增加远征伤害] [异常] [damage:{}] [{}] [{}]",new Object[]{damage,p.getLogString(),e});
			}
		}
		return 0;
	}
	
	/**
	 * 更新玩家战斗信息
	 * @param p
	 */
	public void updateBattleDamage(Fighter f,int damage){
		if(isClose()){
			return;
		}
		Player p = null;
		try {
			if(f instanceof Player){
				p = (Player)f;
			}else if(f instanceof Pet){
				p = ((Pet)f).getMaster();
			}
			
			if(p == null || p.room == null){
				return;
			}
			if(!p.room.getIds().contains(new Long(p.getId()))){
				logger.warn("[更新战斗伤害] [失败:玩家不在房间] [{}] [{}]",new Object[]{p.room.getIds(),p.getLogString()});
				return;
			}
			if(!p.room.getCity().getGame().isPlayerInGame(p)){
				String currMap = "";
				if(p.getCurrentGame() != null && p.getCurrentGame().gi != null){
					currMap = p.getCurrentGame().gi.name;
				}
				logger.warn("[更新战斗伤害] [失败:玩家不在副本] [玩家所在地图:{}] [{}]",new Object[]{currMap,p.getLogString()});
				return;
			}
			//宠物算玩家伤害，并且共享玩家的鼓舞值
			
			CityPlayer info = battleInfos.get(p.getId());
			if(info == null){
				info = new CityPlayer();
				info.setPlayerId(p.getId());
				info.setPlayerName(p.getName());
			}
			double addDamage = damage * (1 + info.getPlayerPercent() / 100);
			info.setPlayerDamage(info.getPlayerDamage() + (long)addDamage);
			battleInfos.put(p.getId(), info);
			
			long jiazuID = p.getJiazuId();
			CityJiaZu jiazuinfo = jiazuInfos.get(jiazuID);
			if(jiazuinfo == null){
				jiazuinfo = new CityJiaZu();
				jiazuinfo.setJiaZuId(jiazuID);
				jiazuinfo.setJiazuName(p.getJiazuName());
			}
			jiazuinfo.setJiaZuDamage(jiazuinfo.getJiaZuDamage() + (long)addDamage);
			jiazuInfos.put(jiazuID, jiazuinfo);
			logger.warn("[更新战斗伤害] [成功] [技能初始伤害:{}] [计算后伤害:{}] [玩家总伤害:{}] [家族总伤害:{}] [玩家鼓励值:{}] [玩家:{}] [家族:{}]",
					new Object[]{damage,addDamage,info.getPlayerDamage(),jiazuinfo.getJiaZuDamage(),info.getPlayerPercent(), p.getName(),p.getJiazuName()});
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[更新战斗伤害] [异常] [damage:{}] [{}] [{}]",new Object[]{damage,p.getLogString(),e});
		}
	}
	
	public void handleCityPage(int type,Player player){

		long now = System.currentTimeMillis();
		
		int sealLevel = SealManager.getInstance().getSealLevel();
		PlayerReward reward = pRewards.get(sealLevel);
		if(reward == null){
			sealLevel = 70;
			reward = pRewards.get(sealLevel);
			if(reward == null){
				player.sendError(Translate.服务器配置错误);
				return;
			}
		}
		if(reward.getShowRewards() == null || reward.getShowRewards().length <= 0){
			player.sendError(Translate.服务器配置错误+"!");
			return;
		}
		
		String names [] = new String[]{};
		long damages [] = new long[]{};
		int rank = 0;
		long damage = 0;
		String name = "";
		
		int jiaZuJoins = 0;
		Set<JiazuMember> members = JiazuManager.getInstance().getJiazuMember(player.getJiazuId());
		if(members != null && members.size() > 0){
			for(JiazuMember member : members){
				if(member != null && battleInfos.get(member.getPlayerID()) != null){
					jiaZuJoins++;
				}
			}
		}
		
		String showNames[] = reward.getShowRewards();
		long articleIds [] = new long[showNames.length];
		int articleNums [] = new int[showNames.length];
		for(int i=0;i<showNames.length;i++){
			Article a = ArticleManager.getInstance().getArticle(showNames[i]);
			ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(a.getName(), true, a.getColorType());
			if(ae == null){
				try {
					ae = ArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.boss副本, player, a.getColorType());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			articleIds[i] = ae.getId();
			articleNums[i] = 1; 
		}
		
		if(battleInfos.size() > 0){
			if(type == 0){
				CityPlayer[]  ds = battleInfos.values().toArray(new CityPlayer[]{});
				Arrays.sort(ds, new Comparator<CityPlayer>() {
					@Override
					public int compare(CityPlayer o1, CityPlayer o2) {
						return new Long(o1.getPlayerDamage()).compareTo(new Long(o2.getPlayerDamage()));
					}
				});
				int showNums = billboardShowNum;
				if(ds.length < billboardShowNum){
					showNums = ds.length;
				}
				if(showNums > 0){
					CityPlayer[] showDatas = new CityPlayer[showNums];
					names = new String[showNums];
					damages = new long[showNums];
					for(int i=0;i<ds.length;i++){
						if(i < showNums){
							showDatas[i] = ds[i];
							names[i] = showDatas[i].getPlayerName();
							damages[i] = showDatas[i].getPlayerDamage();
						}
						if(ds[i].getPlayerId() == player.getId()){
							rank = i + 1;
						}
					}
				}
				CityPlayer pinfo = battleInfos.get(player.getId());
				if(pinfo != null){
					damage = pinfo.getPlayerDamage();
					name = player.getName();
				}
			}
		}
		
		if(jiazuInfos.size() > 0){
			if(type == 1){
				CityJiaZu[] ds = jiazuInfos.values().toArray(new CityJiaZu[]{});
				Arrays.sort(ds, new Comparator<CityJiaZu>() {
					@Override
					public int compare(CityJiaZu o1, CityJiaZu o2) {
						return new Long(o1.getJiaZuDamage()).compareTo(new Long(o2.getJiaZuDamage()));
					}
				});
				
				int showNums = billboardShowNum;
				if(ds.length < billboardShowNum){
					showNums = ds.length;
				}
				if(showNums > 0){
					CityJiaZu[] showDatas = new CityJiaZu[showNums];
					names = new String[showNums];
					damages = new long[showNums];
					for(int i=0;i<ds.length;i++){
						if(i < showNums){
							showDatas[i] = ds[i];
							names[i] = showDatas[i].getJiazuName();
							damages[i] = showDatas[i].getJiaZuDamage();
						}
						if(ds[i].getJiaZuId() == player.getJiazuId()){
							rank = i + 1;
						}
					}
				}
				CityJiaZu jinfo = jiazuInfos.get(player.getJiazuId());
				if(jinfo != null){
					damage = jinfo.getJiaZuDamage();
					name = jinfo.getJiazuName();
				}
			}
		}
		String avata = boss.getAvataRace() +"_"+ boss.getAvataSex();
		logger.warn("[boss副本] [请求主界面] [type:{}] [rank:{}] [avata:{}] [names:{}] [{}] [cost:{}]",
				new Object[]{type,rank,avata,Arrays.toString(names),player.getLogString(),(System.currentTimeMillis()-now)});
		
		BOOTH_FIRST_PAGE_RES res = new BOOTH_FIRST_PAGE_RES(GameMessageFactory.nextSequnceNum(), type, names, damages, rank, damage, name, boss.getName(), avata, 
				jiaZuJoins, getOpenTimeStr(), articleIds, articleNums, getNextOpenTime());
		player.addMessageToRightBag(res);
	
	}
	
	@SuppressWarnings("unused")
	private void handle_BOOTH_FIRST_PAGE_REQ(RequestMessage message, Player player) {
		BOOTH_FIRST_PAGE_REQ req = (BOOTH_FIRST_PAGE_REQ) message;
		int type = req.getBillboardType();
		handleCityPage(type,player);
	}
	
	@SuppressWarnings("unused")
	private void handle_BOOTH_HELP_REQ(RequestMessage message, Player player) {
		String a = "<f size='37'>全民BOSS活动说明：</f>\n\n<f size='37'>————参与条件————</f>\n\n<f size='30'>每日13:00开始，20级以上，并且有家族的玩家，可以参与击退域外邪魔活动</f>\n<f size='37'>————活动说明————</f>\n<f size='30'>活动开启后，持续10分钟，期间玩家都可以进入，在副本内死亡，等待5秒即可复活</f>\n<f size='30'>活动结算时，只有对BOSS造成伤害的玩家可以获得奖励，没有伤害的玩家无法获得奖励</f>\n\n<f size='37'>————鼓舞说明————</f>\n<f size='30'>进入全面BOSS后，可以点击个人鼓舞或家族鼓舞，增加对BOSS伤害，</f>\n<f size='30'>其中个人鼓舞只有自己伤害增加，家族鼓舞是家族全部进入成员伤害增加，</f>\n<f size='30'>鼓舞等级不同增加伤害的百分比不同，同时每级鼓舞花费的费用也是不同的</f>\n<f size='30'>（注意：家族鼓舞/个人鼓舞需要花费的都是银子）</f>\n\n<f size='37'>————个人奖励————</f>\n<f size='30'>所有给BOSS造成伤害的用户都可以获得酒及封魔录奖励</f>\n<f size='30'>根据排名不同，前100万都有额外奖励</f>\n<f size='30'>前三名的玩家，还有机会获得稀有宠物“九尾妖狐”</f>\n<f size='30'>（注：70,110,150,190,220级封印阶段所获得的奖励都不同）</f>\n\n<f size='37'>————家族奖励————</f>\n<f size='30'>根据排名奖励资金及灵脉值，所有参与全民BOSS的家族都有奖励</f>\n<f size='30'>（具体奖励规则，请查看各国家主城全民BOSS—NPC家族排行奖励细则）</f>";
		player.addMessageToRightBag(new BOOTH_HELP_RES(message.getSequnceNum(), a));
	}
	
	@SuppressWarnings("unused")
	private void handle_BOOTH_START_FIGHT_REQ(RequestMessage message, Player player) {
		if(!BossCityManager.getInstance().isOpen()){
			player.sendError("活动还没开启");
			return;
		}
		entenRoom(player);
	}
	
	@SuppressWarnings("unused")
	private void handle_BOOTH_GULI_REQ(RequestMessage message, Player player) {
		int type = ((BOOTH_GULI_REQ)message).getBillboardType();
		CityPlayer city = battleInfos.get(player.getId());
		if(city == null){
			player.sendError(Translate.鼓励失败);
			return;
		}
		if(type == 0){
			int currGuLi = (int)city.getPlayerPercent();
			if(pGuLi.get(currGuLi + 10) == null){
				player.sendError(Translate.鼓励已达最大);
				return;
			}
			if(pGuLi.get(currGuLi) == null){
				player.sendError(Translate.鼓励比例配置错误);
				return;
			}
			
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setTitle(Translate.当前鼓励比例);
			String mess = Translate.translateString(Translate.下一级鼓励比例, new String[][] { { Translate.STRING_1, "10%" }, { Translate.STRING_2, BillingCenter.得到带单位的银两(pGuLi.get(currGuLi + 10) * 1000) } });
			mw.setDescriptionInUUB(mess);
			Option_Cost_Confirm sure = new Option_Cost_Confirm();
			sure.setgType(type);
			sure.setText(Translate.确定);
			mw.setOptions(new Option[] { sure });
			QUERY_WINDOW_RES res1 = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res1);
		}else if(type == 1){
			int currGuLi = (int)city.getJiaZuPercent();
			if(jGuLi.get(currGuLi + 2) == null){
				player.sendError(Translate.鼓励已达最大);
				return;
			}
			if(jGuLi.get(currGuLi) == null){
				player.sendError(Translate.鼓励比例配置错误);
				return;
			}
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setTitle(Translate.当前鼓励比例);
			String mess = Translate.translateString(Translate.下一级鼓励比例, new String[][] { { Translate.STRING_1, "2%" }, { Translate.STRING_2, BillingCenter.得到带单位的银两(jGuLi.get(currGuLi + 2) * 1000) } });
			mw.setDescriptionInUUB(mess);
			Option_Cost_Confirm sure = new Option_Cost_Confirm();
			sure.setgType(type);
			sure.setText(Translate.确定);
			mw.setOptions(new Option[] { sure });
			QUERY_WINDOW_RES res1 = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res1);
		}
		
	}
	
	@SuppressWarnings("unused")
	private void handle_BATTLE_GUWU_INFO_REQ(RequestMessage message, Player player) {
		BATTLE_GUWU_INFO_REQ req = (BATTLE_GUWU_INFO_REQ)message;
		int guliLevel = req.getGuliLevel();
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if(jiazu == null){
			player.sendError(Translate.没有家族不能鼓舞);
			return;
		}
		int nextGuLi = 0;
		String mess = "";
		Integer currGuLi = yuanzhenGuLi.get(new Long(player.getJiazuId()));
		synchronized (jiazu) {
			if(currGuLi == null){
				yuanzhenGuLi.put(player.getJiazuId(),0);
				currGuLi = yuanzhenGuLi.get(new Long(player.getJiazuId()));
			}
			if(currGuLi != guliLevel){
				player.sendError(Translate.该级别鼓励已经被抢啦);
				return;
			}
			nextGuLi = currGuLi + 2;
			if(jGuLi.get(nextGuLi) == null){
				player.sendError(Translate.鼓励已达最大);
				return;
			}
			if(jGuLi.get(currGuLi) == null){
				player.sendError(Translate.鼓励比例配置错误);
				return;
			}
			
			
			Long cost = jGuLi.get(nextGuLi);
			if(cost == null){
				player.sendError(Translate.鼓励已达最大);
				return;
			}
			try {
				BillingCenter.getInstance().playerExpense(player, cost.longValue() * 1000, CurrencyType.YINZI, ExpenseReasonType.副本鼓舞, "副本鼓舞");
				Game.logger.warn("[家族远征] [家族鼓舞] [消费:{}] [比例:{}] [{}]",new Object[]{cost,nextGuLi, player.getLogString()});
			} catch (Exception e) {
				e.printStackTrace();
				player.sendError(Translate.元宝不足);
				return;
			} 
			Long nextCost = jGuLi.get(nextGuLi + 2);
			if(nextCost != null){
				cost = nextCost;
			}
			mess = Translate.translateString(Translate.下一级鼓励比例, new String[][] { { Translate.STRING_1, "2%" }, { Translate.STRING_2, BillingCenter.得到带单位的银两(cost * 1000) } });
			
			yuanzhenGuLi.put(player.getJiazuId(),nextGuLi);
		}
		
		List<JiazuMember> list = jiazu.getMembers();
		if (list != null && list.size() > 0) {
			for (JiazuMember jm : list) {
				if (GamePlayerManager.getInstance().isOnline(jm.getPlayerID())) {
					try {
						Player p = GamePlayerManager.getInstance().getPlayer(jm.getPlayerID());
						BATTLE_GUWU_INFO_RES res = new BATTLE_GUWU_INFO_RES(req.getSequnceNum(),mess,nextGuLi,nextGuLi);
						p.addMessageToRightBag(res);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		BATTLE_GUWU_INFO_RES res = new BATTLE_GUWU_INFO_RES(req.getSequnceNum(),mess,nextGuLi,nextGuLi);
		player.addMessageToRightBag(res);
		Game.logger.warn("[家族远征] [鼓舞] [请求级别:"+guliLevel+"] [当前:"+currGuLi+"] [下一级别:"+nextGuLi+"] [家族:"+jiazu.getLogString()+"] ["+player.getLogString()+"]");
	}
	
	@SuppressWarnings("unused")
	private void handle_PLAYER_IN_SPESCENE2_REQ(RequestMessage message, Player player) {
		int[] sceneTypes = new int[10];
		int[] results = new int[10];
//			1:空岛,2:全民boss,3:副本,4:家族远征(带鼓舞)
//		if (DisasterManager.getInst().isPlayerInGame(player)) {
//			results[0] = 1;
//		}
		Game game = player.getCurrentGame();
		if(game == null){
			logger.warn("[请求特定场景] [失败:game is null] [results:"+(Arrays.toString(results))+"] [sceneTypes:"+Arrays.toString(sceneTypes)+"] ["+player.getLogString()+"]");
			PLAYER_IN_SPESCENE2_RES res = new PLAYER_IN_SPESCENE2_RES(message.getSequnceNum(),sceneTypes, results);
			player.addMessageToRightBag(res);
			return;
		}
		GameInfo gi = game.gi;
		if(gi == null){
			logger.warn("[请求特定场景] [失败:gi is null] ["+game.realSceneName+"] [results:"+(Arrays.toString(results))+"] [sceneTypes:"+Arrays.toString(sceneTypes)+"] ["+player.getLogString()+"]");
			PLAYER_IN_SPESCENE2_RES res = new PLAYER_IN_SPESCENE2_RES(message.getSequnceNum(),sceneTypes, results);
			player.addMessageToRightBag(res);
			return;
		}
		String mapName = gi.name;
		if(mapName == null || mapName.isEmpty()){
			logger.warn("[请求特定场景] [失败:gi.name is null] ["+game.realSceneName+"] [results:"+(Arrays.toString(results))+"] [sceneTypes:"+Arrays.toString(sceneTypes)+"] ["+player.getLogString()+"]");
			PLAYER_IN_SPESCENE2_RES res = new PLAYER_IN_SPESCENE2_RES(message.getSequnceNum(),sceneTypes, results);
			player.addMessageToRightBag(res);
			return;
		}
		
		if(mapName.equals(BossCityManager.mapname)){
			results[1] = 1;
		}
		
		if(DownCityManager2.instance.inCityGame(mapName)){
			results[2] = 1;
		}
		
		if(mapName.equals("zhanmotianyu")){
			results[3] = 1;
		}
		
		logger.warn("[请求特定场景] [成功] [mapName:"+mapName+"] [results:"+(Arrays.toString(results))+"] [sceneTypes:"+Arrays.toString(sceneTypes)+"] ["+player.getLogString()+"]");
		PLAYER_IN_SPESCENE2_RES res = new PLAYER_IN_SPESCENE2_RES(message.getSequnceNum(),sceneTypes, results);
		player.addMessageToRightBag(res);
		return;
	}
	
	@SuppressWarnings("unused")
	private void handle_BOOTH_BATTLE_INFO_REQ(RequestMessage message, Player player) {
		long now = System.currentTimeMillis();
		BOOTH_BATTLE_INFO_REQ req = (BOOTH_BATTLE_INFO_REQ)message;
		int type = req.getBillboardType();
		int playerGuLi = 0;
		int jiaZuGuLi = 0;
		
		
		CityPlayer info = battleInfos.get(player.getId());
		CityJiaZu jiazuinfo = jiazuInfos.get(player.getJiazuId());
		if(info != null){
			playerGuLi = (int)info.getPlayerPercent();
		}
		if(jiazuinfo != null){
			jiaZuGuLi= (int)info.getJiaZuPercent();
		}
			
		int jiaZuJoins = 0;
		Set<JiazuMember> members = JiazuManager.getInstance().getJiazuMember(player.getJiazuId());
		if(members != null && members.size() > 0){
			for(JiazuMember member : members){
				if(member != null && battleInfos.get(member.getPlayerID()) != null){
					jiaZuJoins++;
				}
			}
		}
		
		String names [] = {""};
		long damages [] = {0};
		int rank = 0;
		long damage = 0;
		String name = "";
		
		if(battleInfos.size() > 0){
			if(type == 0){
				CityPlayer[]  ds = battleInfos.values().toArray(new CityPlayer[]{});
				Arrays.sort(ds, new Comparator<CityPlayer>() {
					@Override
					public int compare(CityPlayer o1, CityPlayer o2) {
						return new Long(o2.getPlayerDamage()).compareTo(new Long(o1.getPlayerDamage()));
					}
				});
				int showNums = billboardShowNum;
				if(ds.length < billboardShowNum){
					showNums = ds.length;
				}
				if(showNums > 0){
//					CityPlayer[] showDatas = new CityPlayer[showNums];
					names = new String[showNums];
					damages = new long[showNums];
					for(int i=0;i<ds.length;i++){
						if(i < showNums){
//							showDatas[i] = ds[i];
							names[i] = ds[i].getPlayerName();
							damages[i] = ds[i].getPlayerDamage();
						}
						if(ds[i].getPlayerId() == player.getId()){
							rank = i + 1;
						}
					}
				}
				CityPlayer pinfo = battleInfos.get(player.getId());
				if(pinfo != null){
					damage = pinfo.getPlayerDamage();
					name = player.getName();
				}
			}
		}
		
		if(jiazuInfos.size() > 0){
			if(type == 1){
				CityJiaZu[] ds = jiazuInfos.values().toArray(new CityJiaZu[]{});
				Arrays.sort(ds, new Comparator<CityJiaZu>() {
					@Override
					public int compare(CityJiaZu o1, CityJiaZu o2) {
						return new Long(o2.getJiaZuDamage()).compareTo(new Long(o1.getJiaZuDamage()));
					}
				});
				
				int showNums = billboardShowNum;
				if(ds.length < billboardShowNum){
					showNums = ds.length;
				}
				if(showNums > 0){
					CityJiaZu[] showDatas = new CityJiaZu[showNums];
					names = new String[showNums];
					damages = new long[showNums];
					for(int i=0;i<ds.length;i++){
						if(i < showNums){
							showDatas[i] = ds[i];
							names[i] = showDatas[i].getJiazuName();
							damages[i] = showDatas[i].getJiaZuDamage();
						}
						if(ds[i].getJiaZuId() == player.getJiazuId()){
							rank = i + 1;
						}
					}
				}
				CityJiaZu jinfo = jiazuInfos.get(player.getJiazuId());
				if(jinfo != null){
					damage = jinfo.getJiaZuDamage();
					name = jinfo.getJiazuName();
				}
			}
		}
		
		BOOTH_BATTLE_INFO_RES res = new BOOTH_BATTLE_INFO_RES(req.getSequnceNum(), type, playerGuLi, jiaZuGuLi, jiaZuJoins, rank, damage, name, names, damages);
		player.addMessageToRightBag(res);
		if(logger.isInfoEnabled()){
			logger.info("[boss副本] [请求战斗信息] [{}] [名次:{}] [伤害:{}] [个人鼓舞:{}] [家族鼓舞:{}] [{}] [{}] [{}] [cost:{}]",
					new Object[]{(type==0?"个人":"家族"),rank,damage,playerGuLi,jiaZuGuLi, Arrays.toString(names),Arrays.toString(damages), player.getLogString(), (System.currentTimeMillis()-now)});
		}
	}
	
	@Override
	public String[] getInterestedMessageTypes() {
		return new String[]{"BOOTH_FIRST_PAGE_REQ","BOOTH_HELP_REQ","BOOTH_START_FIGHT_REQ","BOOTH_BATTLE_INFO_REQ","BOOTH_GULI_REQ","BATTLE_GUWU_INFO_REQ","PLAYER_IN_SPESCENE2_REQ"};
	}

	@Override
	public String getName() {
		return "BossCityManager";
	}
	
	

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		try {
			Player player = check(conn, message, logger);
			Method cacheMethod = methodCache.get(new Integer(message.getType()));
			if (cacheMethod != null) {
				ResponseMessage res = (ResponseMessage) cacheMethod.invoke(this, message, player);
				return res;
			}
			Method m = this.getClass().getDeclaredMethod("handle_" + message.getTypeDescription(), RequestMessage.class, Player.class);
			if (m != null) {
				methodCache.put(new Integer(message.getType()), m);
			}
			ResponseMessage res = (ResponseMessage) m.invoke(this, message, player);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[bossCityManager] [handleReqeustMessage] [error] [message:{}] [{}]", new Object[] { message.getTypeDescription(), e });
			throw e;
		}
	}

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}
	
	public String getDdcFile() {
		return ddcFile;
	}

	public void setDdcFile(String ddcFile) {
		this.ddcFile = ddcFile;
	}

	public Map<Long, CityPlayer> getBattleInfos() {
		return battleInfos;
	}

	public Map<Long, CityJiaZu> getJiazuInfos() {
		return jiazuInfos;
	}

	public Map<Integer, PlayerReward> getpRewards() {
		return pRewards;
	}

	public Map<Integer, JiaZuReward> getjRewards() {
		return jRewards;
	}

	public Map<Integer, Long> getpGuLi() {
		return pGuLi;
	}

	public void setpGuLi(Map<Integer, Long> pGuLi) {
		this.pGuLi = pGuLi;
	}

	public Map<Integer, Long> getjGuLi() {
		return jGuLi;
	}

	public void setjGuLi(Map<Integer, Long> jGuLi) {
		this.jGuLi = jGuLi;
	}

	public int[] getStartHour() {
		return startHour;
	}

	public void setStartHour(int[] startHour) {
		this.startHour = startHour;
	}

	public int[] getStartMinute() {
		return startMinute;
	}

	public void setStartMinute(int[] startMinute) {
		this.startMinute = startMinute;
	}

}
