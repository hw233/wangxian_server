package com.fy.engineserver.downcity.downcity2;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.downcity.city.City4Basic;
import com.fy.engineserver.downcity.city.City4SystemSkill;
import com.fy.engineserver.downcity.city.City4WuDi;
import com.fy.engineserver.downcity.city.CityAction;
import com.fy.engineserver.downcity.city.CityConfig;
import com.fy.engineserver.downcity.city.CityConstant;
import com.fy.engineserver.downcity.city.CityThread;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.charge.CardFunction;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu2.manager.BossConfig;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.downcity.Option_Confirm_StartTun;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_SHOW_DOWNCITY_TUN_REQ;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class DownCityManager2 {
	public static Logger logger = DownCityManager.logger;
	
	private String fileName;
	
	public static DownCityManager2 instance ;
	/** 显示的物品数量 */
	public static final int maxPropNum = 8;
	/** key=副本地图英文名 */
	public Map<String, DownCityTunModel> tunModelMap = new Hashtable<String, DownCityTunModel>();
	/** 基本配置   key=副本地图英文名*/
	public Map<String, DownCityBaseModel> dbmMap = new Hashtable<String, DownCityBaseModel>();
	
	public Random random = new Random(System.currentTimeMillis());
	public Random random2 = new Random();
	/**  存储玩家通关结果 */
	public Map<Long, DownCityCacheModel> cache = new Hashtable<Long, DownCityCacheModel>();
	
	public DownCity2Thread thread ;
	private static DiskCache dcache;
	private String diskCatchPath;
	
	public static final String[] allMapNames = new String[]{"taixuhuanjing", "yuxumijing", "qingxukujing", "naihejuejing", "wangxiangmiejing"
		, "fengduguijing", "taixuhuanjingyuanshen", "yuxumijingyuanshen", "qingxukujingyuanshen", "naihejuejingyuanshen", "wangxiangmiejingyuanshen"
		, "fengdouguijingyuanshen"};
	
	public static void main(String[] args) throws Exception {
		DownCityManager2 am = new DownCityManager2();
//		am.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//downcity2//downCityTurntable.xls");
//		am.initFile();
		am.setFileName2("D://workspace//server//conf//game_init_config//downcity2//downCityConfig.xls");
		am.initFile2();
	}
	
	public void init () throws Exception {
		instance = this;
		initFile();
		initFile2();
		dcache = new DefaultDiskCache(new File(diskCatchPath), "cityOutPut", 1000L * 60 * 60 * 24 * 365 * 10);
		initOutData();
		thread = new DownCity2Thread();
		thread.setName("副本奖励发放线程");
		thread.start();
		ServiceStartRecord.startLog(this);
	}
	
	public String getDiskCatchPath() {
		return diskCatchPath;
	}

	public void setDiskCatchPath(String diskCatchPath) {
		this.diskCatchPath = diskCatchPath;
	}

	public Map<Long, CityAction> cityMap = new HashMap<Long, CityAction>();
	
	/**
	 * 多人副本进入
	 * @param cityName
	 * @param player
	 */
	public void enterCity(String cityName,Player player){
		Team team = player.getTeam();
		if(team == null){
			player.sendError(Translate.请组好队伍再来参加);
			return;
		}
		
		List<Player> list = team.getMembers();
		if(list == null || list.size() < 2){
			player.sendError(Translate.队伍必须大于2人才能发起副本);
			return;
		}
		
		CityConfig config = null;
		for(CityConfig cityConfig : cityConfigs){
			if(cityConfig != null && cityConfig.getCityName().equals(cityName)){
				config = cityConfig;
			}
		}
		if(config == null){
			player.sendError(Translate.副本配置错误);
			return;
		}
		
		int [] cityEnterLimitNum = player.getCityEnterLimitNum();
		long [] cityEnterLimitDate = player.getCityEnterLimitDate();
		int monthCardAddNum = 0;
		if(player.ownMonthCard(CardFunction.副本百分200掉落次数每周增加5次)){
			monthCardAddNum = config.getMonthCardAddNum();
		}
		if(!TimeTool.instance.isSame(SystemTime.currentTimeMillis(), cityEnterLimitDate[0], TimeDistance.DAY)){
			cityEnterLimitNum[0] = 0;
			cityEnterLimitDate[0] = SystemTime.currentTimeMillis();
		}
		if(!TimeTool.instance.isSameWeek(SystemTime.currentTimeMillis(), cityEnterLimitDate[1])){
			cityEnterLimitNum[1] = 0;
			cityEnterLimitDate[1] = SystemTime.currentTimeMillis();
		}
		player.setCityEnterLimitDate(cityEnterLimitDate);
		player.setCityEnterLimitNum(cityEnterLimitNum);
		if(config.getDayLimitNum() > 0 && cityEnterLimitNum[0] >= config.getDayLimitNum()){
			player.sendError("该副本每日最多能打:"+config.getDayLimitNum()+"次");
			return;
		}
		if(config.getWeekLimitNum() > 0 && cityEnterLimitNum[1] >= (config.getWeekLimitNum() + monthCardAddNum)){
			player.sendError("该副本每周最多能打:"+(config.getWeekLimitNum()+monthCardAddNum)+"次");
			return;
		}
		
		for(Player p : list){
			if(p.getLevel() < config.getMinLevelLimit() || p.getLevel() > config.getMaxLevelLimit()){
				player.sendError(Translate.副本等级不满足);
				return;
			}
			
			int [] cityEnterLimitNum2 = p.getCityEnterLimitNum();
			long [] cityEnterLimitDate2 = p.getCityEnterLimitDate();
			int monthCardAddNum2 = 0;
			if(p.ownMonthCard(CardFunction.副本百分200掉落次数每周增加5次)){
				monthCardAddNum2 = config.getMonthCardAddNum();
			}
			if(!TimeTool.instance.isSame(SystemTime.currentTimeMillis(), cityEnterLimitDate2[0], TimeDistance.DAY)){
				cityEnterLimitNum2[0] = 0;
				cityEnterLimitDate2[0] = SystemTime.currentTimeMillis();
			}
			if(!TimeTool.instance.isSameWeek(SystemTime.currentTimeMillis(), cityEnterLimitDate2[1])){
				cityEnterLimitNum2[1] = 0;
				cityEnterLimitDate2[1] = SystemTime.currentTimeMillis();
			}
			p.setCityEnterLimitDate(cityEnterLimitDate2);
			p.setCityEnterLimitNum(cityEnterLimitNum2);
			if(config.getDayLimitNum() > 0 && cityEnterLimitNum2[0] >= config.getDayLimitNum()){
				player.sendError("队伍内有玩家已达今天最大进入次数");
				return;
			}
			if(config.getWeekLimitNum() > 0 && cityEnterLimitNum2[1] >= (config.getWeekLimitNum() + monthCardAddNum2)){
				player.sendError("队伍内有玩家已达本周最大进入次数");
				return;
			}
			
		}
		
		List<MapFlop> flop = DownCityManager2.instance.mapFlops.get(config.getMapName());
		if(flop == null){
			player.sendError(Translate.远征活动副本配置错误);
			return;
		}
		
		int cityType = config.getCityType();
		CityAction city = team.getCity();
		
		logger.warn("[进入多人副本-创建] [cityType:{}] [{}] [cityName:{}] [队长:{}] [team:{}] [{}]",new Object[]{cityType, city,cityName,player.getName(), list.size(),player.getLogString()});
		if(city != null){
			if(city.isEnd()){
				player.sendError(Translate.副本cd中);
				return;
			}
			if(city.getGame() != null){
				player.downFromHorse();
				player.transferGameCountry = 0;
				city.pMap().put(player.getId(), player);
				cityMap.put(player.getId(),city);
				player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, config.getMapName(), config.getPlayerXY()[0], config.getPlayerXY()[1]));
//				cityEnterLimitNum[0] += 1;
//				cityEnterLimitNum[1] += 1;
//				player.setCityEnterLimitNum(cityEnterLimitNum);
				
				logger.warn("[进入多人副本-退出重新进] [cityType:{}] [{}] [cityName:{}] [副本id:{}] [次数限制:{}] [月卡增加:{}] [队长:{}] [team:{}] [{}]",
						new Object[]{cityType, city.getId(),city.getGame(),cityName, Arrays.toString(cityEnterLimitNum),monthCardAddNum, player.getName(), list.size(),player.getLogString()});
			}
			return;
		}
		
		if(team.getCaptain() != player){
			player.sendError(Translate.只能是队长才能发起副本);
			return;
		}
		
		List<Player> members = team.getMembers();
		for(Player p : members){
			if(p.getId() == player.getId()){
				continue;
			}
			if(p.getCurrentGame() == null || p.getCurrentGame().gi == null){
				player.sendError(Translate.队友当前地图不允许进入副本);
				return;
			}
			if(!Arrays.asList(JiazuSubSystem.getInstance().allowEnterMaps).contains(p.getCurrentGame().gi.name)){
				player.sendError(Translate.队友当前地图不允许进入副本+"!");
				return;
			}
			if (p.isFighting()) {
				player.sendError(Translate.队友战斗状态不能进入);
				return;
			}
		}
		
		Game game = null;
		if(city == null){
			game = createGame(config);
			if(game == null){
				player.sendError(Translate.副本创建失败);
				return;
			}
			if(cityType == 0){
				city = new City4Basic(CityConstant.nextId());
				((City4Basic)city).flops = flop;
				Monster boss = refreshBoss(game,config,(City4Basic)city);
				if(boss == null){
					player.sendError(Translate.副本创建失败+"!");
					return;
				}
				((City4Basic)city).setGame(game);
				((City4Basic)city).setBos(boss);
			}
			else if(cityType == 1){
				city = new City4WuDi(CityConstant.nextId());
				((City4WuDi)city).flops = flop;
				Monster boss = refreshBoss(game,config,(City4WuDi)city);
				if(boss == null){
					player.sendError(Translate.副本创建失败+"!");
					return;
				}
				((City4WuDi)city).setConfig(config);
				((City4WuDi)city).setGame(game);
				((City4WuDi)city).setBos(boss);
			}else if(cityType == 2){
				city = new City4SystemSkill(CityConstant.nextId(),game);
				refreshBoss2(game,config,(City4SystemSkill)city);
				((City4SystemSkill)city).config = config;
			}
		}
		
		if(city == null){
			player.sendError(Translate.副本类型错误);
			return;
		}
		game = city.getGame();	
		team.setCity(city);
		
		for(Player p : list){
			Game pGame = p.getCurrentGame();
			if(pGame != null){
				p.downFromHorse();
				p.transferGameCountry = 0;
				city.pMap().put(p.getId(), p);
				cityMap.put(p.getId(),city);
				int [] cityEnterLimitNum2 = p.getCityEnterLimitNum();
				cityEnterLimitNum2[0] += 1;
				cityEnterLimitNum2[1] += 1;
				p.setCityEnterLimitNum(cityEnterLimitNum2);
				pGame.transferGame(p, new TransportData(0, 0, 0, 0, config.getMapName(), config.getPlayerXY()[0], config.getPlayerXY()[1]));
				logger.warn("[进入多人副本] [cityType:{}] [cityName:{}] [副本id:{}] [次数限制:{}] [月卡增加:{}] [队长:{}] [team:{}] [{}]",
						new Object[]{cityType,cityName,city.getId(), Arrays.toString(cityEnterLimitNum2),monthCardAddNum, player.getName(), list.size(),p.getLogString()});
			}
		}
		addCityTask(city);
	}
	
	public Monster refreshBoss(Game g,CityConfig config,City4WuDi city){
		for(int i=0;i<config.getMonsterIds().length;i++){
			int [] ids = config.getMonsterIds()[i];
			int [] xys = config.getAreaXYs()[i];
			for(int j=0;j<ids.length;j++){
				Monster m = MemoryMonsterManager.getMonsterManager().createMonster(ids[j]);
				m.setX(xys[0] + random2.nextInt(350));
				m.setY(xys[1] + random2.nextInt(350));
				m.setMonsterLocat((byte) 2);
				if(m.getName().contains("首领")){
					city.monsterNum_jy++;
				}else {
					city.monsterNum_pt++;
				}
				g.addSprite(m);
				m.setInvulnerable(true);
				m.setParticleName("BUFF/魔法盾0");
			}
		}
		Monster boss = MemoryMonsterManager.getMonsterManager().createMonster(config.getBossId());
		boss.setX(config.getBossXY()[0]);
		boss.setY(config.getBossXY()[1]);
		city.monsterNum_boss++;
		g.addSprite(boss);
		boss.setInvulnerable(true);
		boss.setParticleName("BUFF/魔法盾0");
		boss.setMonsterLocat((byte) 2);
		return boss;
	}
	
	
	public Monster refreshBoss(Game g,CityConfig config,City4Basic city){
		for(int i=0;i<config.getMonsterIds().length;i++){
			int [] ids = config.getMonsterIds()[i];
			int [] xys = config.getAreaXYs()[i];
			for(int j=0;j<ids.length;j++){
				Monster m = MemoryMonsterManager.getMonsterManager().createMonster(ids[j]);
				m.setX(xys[0] + random2.nextInt(350));
				m.setY(xys[1] + random2.nextInt(350));
				m.setMonsterLocat((byte) 2);
				if(m.getName().contains("首领")){
					city.monsterNum_jy++;
				}else {
					city.monsterNum_pt++;
				}
				g.addSprite(m);
				logger.warn("[刷新小怪成功] [基本副本] [id:"+m.getId()+"] [Name:"+m.getName()+"] [x:"+m.getX()+"] [y:"+m.getY()+"] [monsterNum_jy:"+city.monsterNum_jy+"] [monsterNum_pt:"+city.monsterNum_pt+"]");
//				m.setInvulnerable(true);
//				m.setParticleName("BUFF/魔法盾0");
			}
		}
		Monster boss = MemoryMonsterManager.getMonsterManager().createMonster(config.getBossId());
		boss.setX(config.getBossXY()[0]);
		boss.setY(config.getBossXY()[1]);
		city.monsterNum_boss++;
		g.addSprite(boss);
		boss.setInvulnerable(true);
		boss.setParticleName("BUFF/魔法盾0");
		boss.setMonsterLocat((byte) 2);
		logger.warn("[刷新boss成功] [基本副本] [id:"+boss.getId()+"] [Name:"+boss.getName()+"] [x:"+boss.getX()+"] [y:"+boss.getY()+"]");
		return boss;
	}
	
	public boolean inCityGame(Player p){
		if(p != null && p.getCurrentGame() != null && p.getCurrentGame().gi != null){
			for(CityConfig cityConfig : cityConfigs){
				if(cityConfig != null && cityConfig.getMapName().equals(p.getCurrentGame().gi.name)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean inTeamCityGame(Player p){
		if(p != null && p.getCurrentGame() != null && p.getCurrentGame().gi != null){
			for(CityConfig cityConfig : cityConfigs){
				if(cityConfig != null && cityConfig.getJoinType()==2 && cityConfig.getMapName().equals(p.getCurrentGame().gi.name)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean inSingleCityGame(Player p){
		if(p != null && p.getCurrentGame() != null && p.getCurrentGame().gi != null){
			for(CityConfig cityConfig : cityConfigs){
				if(cityConfig != null && cityConfig.getJoinType()==1 && cityConfig.getMapName().equals(p.getCurrentGame().gi.name)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean inSingleCityGame(String name){
		for(CityConfig cityConfig : cityConfigs){
			if(cityConfig != null && cityConfig.getJoinType()==1 && cityConfig.getMapName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public boolean inTeamCityGame(String name){
		for(CityConfig cityConfig : cityConfigs){
			if(cityConfig != null && cityConfig.getJoinType()==2 && cityConfig.getMapName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public boolean inCityGame(String name){
		for(CityConfig cityConfig : cityConfigs){
			if(cityConfig != null && cityConfig.getMapName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public void refreshBoss2(Game g,CityConfig config,City4SystemSkill city){
		for(int i=0;i<config.getMonsterIds().length;i++){
			int [] ids = config.getMonsterIds()[i];
			int [] xys = config.getAreaXYs()[i];
			for(int j=0;j<ids.length;j++){
				Monster m = MemoryMonsterManager.getMonsterManager().createMonster(ids[j]);
				m.setX(xys[0] + random2.nextInt(200));
				m.setY(xys[1] + random2.nextInt(200));
				m.setMonsterLocat((byte) 2);
				city.monsterNum++;
				g.addSprite(m);
			}
		}
	}
	
	public Game createGame(CityConfig config){
		GameManager gameManager = GameManager.getInstance();
		GameInfo gi = gameManager.getGameInfo(config.getMapName());
		if(gi == null){
			logger.warn("[创建多人副本-无敌] [失败:对应的地图信息不存在] [{}]", new Object[]{config.getMapName()});
			return null;
		}
		try {
			Game newGame = new Game(gameManager,gi);
			newGame.init();
			return newGame;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[创建多人副本-无敌] [失败:game初始化失败] [{}] [{}]", new Object[]{config.getMapName(),e});
			return null;
		}
	}
	
	public void addCityTask(CityAction city){
		CityThread handler = getCityHandler();
		handler.addTask(city);
	}
	
	List<CityThread> handlers = new ArrayList<CityThread>();
	public CityThread getCityHandler(){
		CityThread cThread = null;
		if(handlers.size() == 0){
			for(int i=0;i<5;i++){
				CityThread thread = new CityThread("副本处理-"+handlers.size());
				thread.start();
				handlers.add(thread);
				CityConstant.logger.warn("[初始化副本处理] [{}] [handlers:{}]",new Object[]{thread.getName()},handlers.size());
			}	
		}
		for(CityThread t : handlers){
			if(t != null && !t.isFull()){
				cThread = t;
				break;
			}
		}
		if(cThread == null){
			cThread = new CityThread("副本处理-"+handlers.size());
			cThread.start();
			handlers.add(cThread);
			CityConstant.logger.warn("[新增副本处理] [{}] [handlers:{}]",new Object[]{cThread.getName()},handlers.size());
		}
		return cThread;
	}
	
	
	
	/**
	 * 轮盘抽奖
	 * @return [0]物品临时id  [1]物品数量 [2]剩余次数
	 */
	public long[] startTun(Player player, boolean confirm) {
		PropModel resultProp = null;
		DownCityCacheModel dcm = null;
		int maxTime = 0 ;
		long[] result = new long[]{-1,-1,0};
		synchronized (player) {
			dcm = cache.get(player.getId());
			if (dcm == null) {
				player.sendError(Translate.可参与次数不足);
				return result;
			}
			maxTime = this.getMaxTimesByMapName(dcm.getMapName());
			if (dcm.getTimes() >= maxTime) {
				player.sendError(Translate.可参与次数不足);
				return result;
			}
			List<PropModel> list = new ArrayList<PropModel>();
			for (PropModel pm : dcm.getShowPropList()) {
				if (dcm.getPropList() == null || !dcm.getPropList().contains(pm)) {
					list.add(pm);
				}
			}
			resultProp = this.getResultProp(player, dcm.getMapName(), list);
			long cost = this.getCostByMapNameAndTimes(dcm.getMapName(), dcm.getTimes());
			if (cost > 0) {
				if ((player.getSilver() + player.getShopSilver()) < cost) {
					player.sendError(Translate.银子不足);
					return result;
				}
				if (!confirm) {
					//弹窗
					popConfirmWindow(player, (int) (cost/1000));
					return new long[]{-2,-2,0};
				}
				try {
					BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.副本结束转盘, "副本转盘抽奖");
				} catch (Exception e) {
					player.sendError(Translate.银子不足);
					return result;
				}
			}
			resultProp.setStartTime(System.currentTimeMillis());
			result[0] = resultProp.getTempArticleEntityId();
			result[1] = resultProp.getNum();
			dcm.setTimes(dcm.getTimes() + 1);
			result[2] = maxTime- dcm.getTimes();
			List<PropModel> getList = dcm.getPropList();
			if (getList == null) {
				getList = new ArrayList<PropModel>();
			}
			getList.add(resultProp);
			dcm.setPropList(getList);
		}
		thread.putPropModel2Thread(player, resultProp);
		logger.warn("[新副本] [玩家抽取奖励] [未发送] [" + player.getLogString() + "] [获得奖励:" + resultProp + "]");
		if (dcm.getTimes() >= maxTime) {
			cache.remove(player.getId());
		}
		return result;
	}
	
	private void popConfirmWindow(Player p, int cost) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_Confirm_StartTun option1 = new Option_Confirm_StartTun();
		option1.setText(MinigameConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = String.format(Translate.确认花钱转盘, cost);
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	
	/**
	 * 玩家通关副本调用方法
	 * @param player
	 * @param mapName
	 */
	public void notifyPlayerPassDownCity(Player player, String mapName) {
		synchronized (player) {
			if (cache.containsKey(player.getId())) {
				logger.warn("[新副本] [已经存在玩家通关副本信息，又提示通关] [" + player.getLogString() + "] [" + mapName + "] [oldMap:" + cache.get(player.getId()) + "]");
			}
			DownCityCacheModel dcm = new DownCityCacheModel();
			dcm.setTimes(0);
			dcm.setPlayerId(player.getId());
			dcm.setMapName(mapName);
			List<PropModel> showPropList = this.getShowPropsByMapName(mapName); 
			dcm.setShowPropList(showPropList);
			if (logger.isDebugEnabled()) {
				logger.debug("[新副本] [玩家通关副本弹出转盘] [" + player.getLogString() + "]");
				for (int i=0; i<showPropList.size(); i++) {
					logger.debug("[新副本] [随机出来的物品] [" + player.getLogString() + "] [" + showPropList.get(i) + "]");
				}
			}
			cache.put(player.getId(), dcm);
			long[] articleIds = new long[showPropList.size()];
			int[] articleNums = new int[showPropList.size()];
			for (int i=0; i<showPropList.size(); i++) {
				articleIds[i] = showPropList.get(i).getTempArticleEntityId();
				articleNums[i] = showPropList.get(i).getNum(); 
			}
			int maxTime = this.getMaxTimesByMapName(dcm.getMapName());
			NOTIFY_SHOW_DOWNCITY_TUN_REQ req = new NOTIFY_SHOW_DOWNCITY_TUN_REQ(GameMessageFactory.nextSequnceNum(), 
					articleIds, articleNums, maxTime);
			player.addMessageToRightBag(req);
			logger.warn("[新副本] [玩家通关副本出转盘] [" + player.getLogString() + "] [" + mapName + "]");
		}
	}
	
	/**
	 * 通过地图名获取对大可抽奖次数
	 * @param mapName
	 * @return
	 */
	public int getMaxTimesByMapName(String mapName) {
		DownCityBaseModel dbm = dbmMap.get(mapName);
		if (dbm == null) {
			logger.error("[新副本] [dbmMap中没有找到对应地图的model] [" + mapName + "] ");
			return -1;
		}
		return dbm.getMaxTimes();
	}
	/**
	 * 获取消耗的银子数
	 * @param mapName
	 * @param times 
	 * @return		-1代表次数已经用尽，不可继续抽奖了
	 */
	public long getCostByMapNameAndTimes(String mapName, int times) {
		DownCityBaseModel dbm = dbmMap.get(mapName);
		if (dbm == null) {
			logger.error("[新副本] [dbmMap中没有找到对应地图的model] [" + mapName + "] [times:" + times +"]");
			return -1;
		}
		if (dbm.getMaxTimes() <= times) {
			return -1;
		}
		return dbm.getCostMoney()[times];
	}
	/**
	 * 获取随机结果
	 * @return
	 */
	public PropModel getResultProp(Player player, String mapName, List<PropModel> list) {
		int total = 0;
		for (int i=0; i<list.size(); i++) {
			total += list.get(i).getProbabbly4Get();
		}
		int ran = player.random.nextInt(total);
		int tempP = 0;
		for (int i=0; i<list.size(); i++) {
			tempP += list.get(i).getProbabbly4Get();
			if (ran <= tempP) {
				return list.get(i);
			}
		}
		logger.error("[新副本] [getResultProp没有随机到奖励物品] [" + player.getLogString() + "] [" + mapName + "] [" + list + "]");
		return null;
	}
	
	/**
	 * 通过副本名得到显示的物品列表
	 * @param mapName
	 * @return
	 */
	public List<PropModel> getShowPropsByMapName(String mapName) {
		DownCityTunModel dtm = tunModelMap.get(mapName);
		if (dtm == null) {
			logger.error("[新副本] [没有找到对应副本地图名的DownCityTunModel ] [" + mapName + "]");
			return null;
		}
		List<PropModel> pml = new ArrayList<PropModel>();
		int count = 0;
		while (pml.size() < maxPropNum) {
			count++;
			List<PropModel> temp = new ArrayList<PropModel>();
			int totalRan = 0;
			for (int i=0; i<dtm.getProps().size(); i++) {
				if (!pml.contains(dtm.getProps().get(i))) {
					temp.add(dtm.getProps().get(i));
					totalRan += dtm.getProps().get(i).getProbabbly4Show();
				}
			}
			int ran = random.nextInt(totalRan);
			int tempP = 0;
			for (int i=0; i<temp.size(); i++) {
				tempP += temp.get(i).getProbabbly4Show();
				if (ran <= tempP) {
					pml.add( temp.get(i));
					break;
				}
			}
			temp = null;
			if (count >= 50) {
				break;
			}
		}
		if (pml.size() < maxPropNum) {
			for (int i=0; i<dtm.getProps().size(); i++) {
				if (!pml.contains(dtm.getProps().get(i))) {
					pml.add(dtm.getProps().get(i));
					if (pml.size() >= maxPropNum) {
						break;
					}
				}
			}
		}
		return pml;
	}
	
	private void putProp2TunMap(PropModel pm, String mapName) {
		DownCityTunModel dtm = tunModelMap.get(mapName);
		if(dtm == null) {
			dtm = new DownCityTunModel();
			dtm.setMapName(mapName);
		}
		if(!dtm.getProps().contains(pm)) {
			dtm.getProps().add(pm);
		}
		tunModelMap.put(mapName, dtm);
	}
	
	private String fileName2;
	public String getFileName2() {
		return fileName2;
	}
	public void setFileName2(String fileName2) {
		this.fileName2 = fileName2;
	}
	public List<CityConfig> cityConfigs = new ArrayList<CityConfig>();
	public Map<String,List<MapFlop>> mapFlops = new HashMap<String,List<MapFlop>>();
	private void initFile2() throws Exception {
		File f = new File(fileName2);
		if(!f.exists()){
			throw new Exception(fileName2 + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);	
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				CityConfig config = new CityConfig();
				config.setCityName(ReadFileTool.getString(row, index++, logger));
				config.setMapName(ReadFileTool.getString(row, index++, logger));
				config.setJoinType(ReadFileTool.getInt(row, index++, logger));
				config.setCityType(ReadFileTool.getInt(row, index++, logger));
				config.setJiaZuLevelLimit(ReadFileTool.getInt(row, index++, logger));
				config.setMinLevelLimit(ReadFileTool.getInt(row, index++, logger));
				config.setMaxLevelLimit(ReadFileTool.getInt(row, index++, logger));
				
				config.setWeekLimitNum(ReadFileTool.getInt(row, index++, logger));
				config.setDayLimitNum(ReadFileTool.getInt(row, index++, logger));
				config.setMonthCardAddNum(ReadFileTool.getInt(row, index++, logger));
				
				config.setBossId(ReadFileTool.getInt(row, index++, logger));
				config.setBossName(ReadFileTool.getString(row, index++, logger));
				config.setBossAvata(ReadFileTool.getString(row, index++, logger));
				config.setBossXY(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				config.setPlayerXY(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				String [] rewardName = ReadFileTool.getStringArr(row, index++, logger, ",");
				
				long [] ids = new long[rewardName.length];
				for(int j=0;j<ids.length;j++){
					Article a = ArticleManager.getInstance().getArticle(rewardName[j]);
					ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(a.getName(), true, a.getColorType());
					if(ae == null){
						try {
							ae = ArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.幻境单人副本, null, a.getColorType());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(ae == null){
						throw new Exception("加载副本信息错误，奖励不存在："+a.getName());
					}
					ids[j] = ae.getId();
				}
				config.setIds(ids);
				config.setNums(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				
				int [][] areaXYs = new int[5][];
				for(int jj=0;jj<areaXYs.length;jj++){
					areaXYs[jj] = ReadFileTool.getIntArrByString(row, index++, logger, ",");
				}
				config.setAreaXYs(areaXYs);
				int [][] mids = new int[5][];
				for(int jj=0;jj<mids.length;jj++){
					mids[jj] = ReadFileTool.getIntArrByString(row, index++, logger, ",");
				}
				config.setMonsterIds(mids);
				logger.warn("[加载副本信息] ["+config+"]");
				cityConfigs.add(config);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(1);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1;i<rows;i++){
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					int index = 0;
					int level = ReadFileTool.getInt(row, index++, logger);
					int id = ReadFileTool.getInt(row, index++, logger);
					long hp = ReadFileTool.getLong(row, index++, logger, 0);
					String[] run_rewards = ReadFileTool.getStringArr(row, index++, logger, ",");
					String[] kill_rewards = ReadFileTool.getStringArr(row, index++, logger, ",");
					String run_other_reward = ReadFileTool.getString(row, index++, logger);
					String kill_other_reward = ReadFileTool.getString(row, index++, logger);
					BossConfig c = new BossConfig();
					c.setLevel(level);
					c.setId(id);
					c.setHp(hp);
					c.setRun_reward1_10(run_rewards);
					c.setKill_reward1_10(kill_rewards);
					c.setRun_other_reward(run_other_reward);
					c.setKill_other_reward(kill_other_reward);
					JiazuManager2.bconfigs.put(c.getLevel(), c);
					logger.warn("[家族远征配置] ["+c+"]");
				}catch(Exception e){
					throw new Exception("[boss奖励] [异常] [行："+i+"]",e);
				}
			}
		}
		
		sheet = workbook.getSheetAt(2);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				MapFlop flop = new MapFlop();
				flop.setMapName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleColor(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleCount(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterType(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleFlop(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setBossArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setLimitFlopNum(ReadFileTool.getInt(row, index++, logger));
				flop.setLimitFlopType(ReadFileTool.getInt(row, index++, logger));
				
				logger.warn("[加载冰封副本掉率信息] ["+flop+"]");
				List<MapFlop> list = mapFlops.get(flop.getMapName());
				if(list == null){
					list = new ArrayList<MapFlop>();
					mapFlops.put(flop.getMapName(),new ArrayList<MapFlop>());
				}
				list.add(flop);
				mapFlops.put(flop.getMapName(),list);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(3);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				MapFlop flop = new MapFlop();
				flop.setMapName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleColor(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleCount(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterType(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleFlop(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setBossArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setLimitFlopNum(ReadFileTool.getInt(row, index++, logger));
				flop.setLimitFlopType(ReadFileTool.getInt(row, index++, logger));
				
				logger.warn("[加载虚空副本掉率信息] ["+flop+"]");
				List<MapFlop> list = mapFlops.get(flop.getMapName());
				if(list == null){
					list = new ArrayList<MapFlop>();
					mapFlops.put(flop.getMapName(),new ArrayList<MapFlop>());
				}
				list.add(flop);
				mapFlops.put(flop.getMapName(),list);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(4);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				MapFlop flop = new MapFlop();
				flop.setMapName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleColor(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleCount(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterType(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleFlop(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setBossArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setLimitFlopNum(ReadFileTool.getInt(row, index++, logger));
				flop.setLimitFlopType(ReadFileTool.getInt(row, index++, logger));
				
				logger.warn("[加载虚空副本掉率信息] ["+flop+"]");
				List<MapFlop> list = mapFlops.get(flop.getMapName());
				if(list == null){
					list = new ArrayList<MapFlop>();
					mapFlops.put(flop.getMapName(),new ArrayList<MapFlop>());
				}
				list.add(flop);
				mapFlops.put(flop.getMapName(),list);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(5);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				MapFlop flop = new MapFlop();
				flop.setMapName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleColor(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleCount(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterType(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleFlop(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setBossArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setLimitFlopNum(ReadFileTool.getInt(row, index++, logger));
				flop.setLimitFlopType(ReadFileTool.getInt(row, index++, logger));
				
				logger.warn("[加载虚空副本掉率信息] ["+flop+"]");
				List<MapFlop> list = mapFlops.get(flop.getMapName());
				if(list == null){
					list = new ArrayList<MapFlop>();
					mapFlops.put(flop.getMapName(),new ArrayList<MapFlop>());
				}
				list.add(flop);
				mapFlops.put(flop.getMapName(),list);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(6);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				MapFlop flop = new MapFlop();
				flop.setMapName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleColor(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleCount(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterType(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleFlop(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setBossArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setLimitFlopNum(ReadFileTool.getInt(row, index++, logger));
				flop.setLimitFlopType(ReadFileTool.getInt(row, index++, logger));
				
				logger.warn("[加载虚空副本掉率信息] ["+flop+"]");
				List<MapFlop> list = mapFlops.get(flop.getMapName());
				if(list == null){
					list = new ArrayList<MapFlop>();
					mapFlops.put(flop.getMapName(),new ArrayList<MapFlop>());
				}
				list.add(flop);
				mapFlops.put(flop.getMapName(),list);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(7);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				MapFlop flop = new MapFlop();
				flop.setMapName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleColor(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleCount(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterType(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleFlop(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setBossArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setLimitFlopNum(ReadFileTool.getInt(row, index++, logger));
				flop.setLimitFlopType(ReadFileTool.getInt(row, index++, logger));
				
				logger.warn("[加载虚空副本掉率信息] ["+flop+"]");
				List<MapFlop> list = mapFlops.get(flop.getMapName());
				if(list == null){
					list = new ArrayList<MapFlop>();
					mapFlops.put(flop.getMapName(),new ArrayList<MapFlop>());
				}
				list.add(flop);
				mapFlops.put(flop.getMapName(),list);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(8);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				MapFlop flop = new MapFlop();
				flop.setMapName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleColor(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleCount(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterType(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleFlop(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setBossArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setLimitFlopNum(ReadFileTool.getInt(row, index++, logger));
				flop.setLimitFlopType(ReadFileTool.getInt(row, index++, logger));
				
				logger.warn("[加载虚空副本掉率信息] ["+flop+"]");
				List<MapFlop> list = mapFlops.get(flop.getMapName());
				if(list == null){
					list = new ArrayList<MapFlop>();
					mapFlops.put(flop.getMapName(),new ArrayList<MapFlop>());
				}
				list.add(flop);
				mapFlops.put(flop.getMapName(),list);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(9);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				MapFlop flop = new MapFlop();
				flop.setMapName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleColor(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleCount(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterType(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleFlop(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setBossArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setLimitFlopNum(ReadFileTool.getInt(row, index++, logger));
				flop.setLimitFlopType(ReadFileTool.getInt(row, index++, logger));
				
				logger.warn("[加载虚空副本掉率信息] ["+flop+"]");
				List<MapFlop> list = mapFlops.get(flop.getMapName());
				if(list == null){
					list = new ArrayList<MapFlop>();
					mapFlops.put(flop.getMapName(),new ArrayList<MapFlop>());
				}
				list.add(flop);
				mapFlops.put(flop.getMapName(),list);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(10);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				MapFlop flop = new MapFlop();
				flop.setMapName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleName(ReadFileTool.getString(row, index++, logger));
				flop.setArticleColor(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleCount(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterType(ReadFileTool.getInt(row, index++, logger));
				flop.setArticleFlop(ReadFileTool.getInt(row, index++, logger));
				flop.setMonsterArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setBossArticleFlop(ReadFileTool.getIntArrByString(row, index++, logger, ","));
				flop.setLimitFlopNum(ReadFileTool.getInt(row, index++, logger));
				flop.setLimitFlopType(ReadFileTool.getInt(row, index++, logger));
				
				logger.warn("[加载虚空副本掉率信息] ["+flop+"]");
				List<MapFlop> list = mapFlops.get(flop.getMapName());
				if(list == null){
					list = new ArrayList<MapFlop>();
					mapFlops.put(flop.getMapName(),new ArrayList<MapFlop>());
				}
				list.add(flop);
				mapFlops.put(flop.getMapName(),list);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
	}
	
	NPCManager nm = MemoryNPCManager.getNPCManager();
	//副本都走个人掉落
	public void handleMonsterFlop(Game game,List<MapFlop> flops,Player p,Monster m){
		if(game != null && flops != null && (flops.size() > 0) && p != null){
			long now = SystemTime.currentTimeMillis();
			for(MapFlop flop : flops){
				int hasOut = 0;
				boolean hasOutLimit = false;
				if(flop.getLimitFlopType() > 0 && flop.getLimitFlopNum() > 0){
					hasOut = getOutPutNum(flop.getArticleName());
					if(hasOut >= flop.getLimitFlopNum()){
						hasOutLimit = true;
					}
				}
				if(hasOutLimit){
					logger.warn("[处理副本掉落] [错误;产出限制] [物品:"+flop.getArticleName()+"] [已经产出:"+hasOut+"] [产出限制数量:"+flop.getLimitFlopNum()+"] [p:"+(p!=null?p.getLogString():p)+"]");
					continue;
				}
				
				Article a = ArticleManager.getInstance().getArticle(flop.getArticleName());
				if(a == null){
					logger.warn("[处理副本掉落] [错误;物品不存在] [name:"+flop.getArticleName()+"] [p:"+(p!=null?p.getLogString():p)+"]");
					continue;
				}
				try {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a,false,ArticleEntityManager.副本掉落, p, flop.getArticleColor(), 1, true);
					
					FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
//					fcn.setFlopType((byte) 1);
					fcn.setStartTime(now);
					fcn.setEndTime(now + MemoryNPCManager.掉落NPC的存在时间);
					fcn.setAllCanPickAfterTime(MemoryNPCManager.掉落NPC的存在时间 + 1000);
					if(p.getTeam() != null){
//						Team team = p.getTeam();
//						List<Player> members = team.getMembers();
//						if(members != null && members.size() > 0){
//							for(Player pp : members){
//								fcn.setOwner(pp);
//							}
//						}
						fcn.setTeam(p.getTeam());
					}
					fcn.setOwner(p);
					fcn.setAe(ae);
					fcn.isMonsterFlop = true;
					fcn.setX(m.getX() + random2.nextInt(100));
					fcn.setY(m.getY() + random2.nextInt(100));
					StringBuffer sb = new StringBuffer();
					ArticleManager am = ArticleManager.getInstance();
					Article article = am.getArticle(ae.getArticleName());
					int color = ArticleManager.COLOR_WHITE;
					if (article.getFlopNPCAvata() != null) {
						fcn.setAvataSex(article.getFlopNPCAvata());
						if (article.getFlopNPCAvata().equals("yinyuanbao")) {
							fcn.setAvataSex("baoxiang");
						}
						ResourceManager.getInstance().getAvata(fcn);
					}
					color = ArticleManager.getColorValue(article, ae.getColorType());
					sb.append(ae.getArticleName());
					fcn.setName(sb.toString());
					fcn.setNameColor(color);
					fcn.setTitle("");
					game.addSprite(fcn);

					if(flop.getLimitFlopType() > 0 && flop.getLimitFlopNum() > 0){
						updateOutPutNum(flop.getArticleName(),1);
						hasOut = getOutPutNum(flop.getArticleName());
					}
					
					logger.warn("[处理副本掉落] [成功] [怪id:"+m.getSpriteCategoryId()+"] [怪物:"+m.getName()+"] [已经产出:"+hasOut+"] [产出限制:"+flop.getLimitFlopNum()+"] [物品:"+ae.getArticleName()+"] [颜色:"+ae.getColorType()+"] [game:"+(game.gi!=null?game.gi.name:"null")+"] [flops:"+flops.size()+"] [p:"+p.getLogString()+"]");
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.warn("[处理副本掉落] [异常] [game:"+game+"] [flops:"+flops.size()+"] [p:"+(p!=null?p.getLogString():p)+"]",ex);
				}
			}
		}else{
			logger.warn("[处理副本掉落] [错误] [game:"+game+"] [flops:"+flops+"] [p:"+(p!=null?p.getLogString():p)+"]");
		}
	}
	
	private void initFile() throws Exception {
		File f = new File(fileName);
		if(!f.exists()){
			throw new Exception(fileName + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);	
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				DownCityBaseModel dbm = new DownCityBaseModel();
				int index = 0;
				dbm.setMapName(ReadFileTool.getString(row, index++, logger));
				dbm.setShowTun(ReadFileTool.getInt(row, index++, logger) == 1);
				dbm.setMaxTimes(ReadFileTool.getInt(row, index++, logger));
				dbm.setCostMoney(ReadFileTool.getLongArrByString(row, index++, logger, ",", 1000L));
				if (dbm.getCostMoney().length != dbm.getMaxTimes()) {
					throw new Exception("[最大次数和消耗银子数量异常] [" + dbm.toString() + "]");
				}
				dbmMap.put(dbm.getMapName(), dbm);
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		
		sheet = workbook.getSheetAt(1);	
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				int index = 0;
				String[] mapNames = ReadFileTool.getString(row, index++, logger).split(",");
				String articleCNNName = ReadFileTool.getString(row, index++, logger);
//				System.out.println(articleCNNName);
				int colorType = ReadFileTool.getInt(row, index++, logger);
				int num = ReadFileTool.getInt(row, index++, logger);
				int[] probabbly4Show = ReadFileTool.getIntArrByString(row, index++, logger, ",");
				int[] probabbly4Get = ReadFileTool.getIntArrByString(row, index++, logger, ",");
				Article a = ArticleManager.getInstance().getArticleByCNname(articleCNNName);
				ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.恶魔广场, null, colorType);
				if (mapNames.length != probabbly4Show.length || mapNames.length != probabbly4Get.length) {
					throw new Exception("[地图名配置出错] [地图个数与概率个数不匹配] [" + Arrays.toString(mapNames) + "] [" + Arrays.toString(probabbly4Show) + "] [" + Arrays.toString(probabbly4Get) + "]");
				}
				for (int ii=0; ii<mapNames.length; ii++) {
					boolean flag = false;
					for (String s : allMapNames) {
						if (mapNames[ii] == null || mapNames[ii].isEmpty()) {
							throw new Exception("[地图名配置出错] [" + Arrays.toString(mapNames) + "]");
						}
						if (s.equals(mapNames[ii])) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						throw new Exception("[地图名配置出错] [没有配置的副本地图名] [" + Arrays.toString(mapNames) + "]");
					}
					PropModel pm = new PropModel(articleCNNName, colorType, num, probabbly4Show[ii], probabbly4Get[ii], ae.getId());
//					PropModel pm = new PropModel(articleCNNName, colorType, num, probabbly4Show[ii], probabbly4Get[ii], 1L);
					this.putProp2TunMap(pm, mapNames[ii]);
				}
			}catch(Exception e) {
				throw new Exception("[" + fileName + "] ["+ sheet.getSheetName() + "] [" + "第" + (i+1) + "行异常！！]", e);
			}
		}
		
		for (DownCityTunModel dctm : tunModelMap.values()) {
//			System.out.println(dctm);
			if (dctm.getProps().size() < maxPropNum) {
				throw new Exception("[物品数量配置低于"+maxPropNum+"个] [" + dctm.getMapName() + "]");
			}
		}
	}
	
	class OutPutData implements Serializable{
		private int outNum;
		private long outDate;
		public int getOutNum() {
			return outNum;
		}
		public void setOutNum(int outNum) {
			this.outNum = outNum;
		}
		public long getOutDate() {
			return outDate;
		}
		public void setOutDate(long outDate) {
			this.outDate = outDate;
		}
	}
	public static Map<String,OutPutData> outPut;
	public int getOutPutNum(String articleName){
		if(!outPut.containsKey(articleName)){
			return 0;
		}
		return outPut.get(articleName).getOutNum();
	}
	
	public void initOutData(){
		Object obj = cache.get("articleOUTPUT");
		if (obj == null) {
			outPut = new Hashtable<String,OutPutData>();
			dcache.put("articleOUTPUT", (Serializable)outPut);
		}else {
			outPut = (Hashtable<String,OutPutData>)obj;
		}
	}
	
	public void updateOutPutNum(String articleName,int value){
		OutPutData d = outPut.get(articleName);
		if(d == null){
			d = new OutPutData();
		}
		if(!TimeTool.instance.isSame(SystemTime.currentTimeMillis(), d.getOutDate(), TimeDistance.DAY)){
			d.setOutNum(0);
			d.setOutDate(SystemTime.currentTimeMillis());
		}
		d.setOutNum(value + d.getOutNum());
		outPut.put(articleName, d);
		dcache.put("articleOUTPUT", (Serializable)outPut);
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	
}
