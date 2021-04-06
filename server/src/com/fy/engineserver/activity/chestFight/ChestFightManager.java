package com.fy.engineserver.activity.chestFight;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.chestFight.instance.ChestFight;
import com.fy.engineserver.activity.chestFight.model.ChestArticleModel;
import com.fy.engineserver.activity.chestFight.model.ChestBaseModel;
import com.fy.engineserver.activity.chestFight.model.ChestModel;
import com.fy.engineserver.activity.chestFight.model.TempReturnModel;
import com.fy.engineserver.activity.datamanager.AbstractActivity;
import com.fy.engineserver.activity.datamanager.ActivityConstant;
import com.fy.engineserver.activity.datamanager.ActivityDataManager;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.AbstractPlayer;
import com.fy.engineserver.sprite.Player;

public class ChestFightManager implements AbstractActivity{
	public static ChestFightManager inst = new ChestFightManager();
	
	public static Logger logger = ActivitySubSystem.logger;
	/** 宝箱目标   key=宝箱类型 */
	public Map<Integer, ChestModel> chests = new HashMap<Integer, ChestModel>();
	/** 基础杂项配置 */
	public ChestBaseModel baseModel ;
	/** 物品配置 */
	public Map<Integer, ChestArticleModel> articleMap = new HashMap<Integer, ChestArticleModel>();
	/** 线程 */
	public ChestFightThread thread;
	
	public ChestFight fight;
	
	private String fileName;
	
	public static long 开始前多久清除上次数据 = 12*60*1000L;
	
	public static long[] 开始前多久发世界提示 = new long[]{10*60*1000,5*60*1000,60*1000};
	
	/**
	 * 玩家进入活动
	 * @param player
	 */
	public void notifyEnterActiv(Player player) {
		String result = this.validate4Enter(player);
		if (result != null) {
			player.sendError(result);
			return ;
		}
		thread.heartTime =ChestFightThread.sleepTime4activ;
		fight.notifyEnterFight(player);
	}
	
	public void notifyLeaveGame(Player player) {
//		fight.notifyPlayerLeaveGame(player);
	}
	
	public TempReturnModel checkPlayerChestTypeAndTime(long playerId) {
		return fight.checkPlayerChestTypeAndTime(playerId);
	}
	
	public int getChestStateByPlayer(AbstractPlayer player) {
		if (player instanceof Player) {
			try {
				Player p = (Player) player;
				if(fight != null){
					if (fight.getPlayerGame(p) == null) {
						return 0;
					}
					return fight.getPlayerStates((Player) player);
				}
			} catch (Exception e) {
				logger.warn("[宝箱争夺] [获取玩家获得宝箱状态] [异常] [" + ((Player) player).getLogString() + "]", e);
			}
		}
		return 0;
	}
	
	public void notifyPlayerOutOfMap(long playerId) {
		this.fight.notifyPlayerOutOfMap(playerId);
	}

	public void notifyPlayerDead(Player player) {
		if(this.fight != null){
			this.fight.notifyPlayerDeath(player);
		}
	}
	
	public static void put2Bag(Player player, List<ChestArticleModel> aeList) {
		try {
			List<Player> players = new ArrayList<Player>();
			players.add(player);
			List<ActivityProp> list = new ArrayList<ActivityProp>();
			int needCell = 0;
			for (int i=0; i<aeList.size(); i++) {
				ChestArticleModel am = aeList.get(i);
				Article a = ArticleManager.getInstance().getArticleByCNname(am.getArticleCNNName());
				if (a.isOverlap()) {
					needCell ++;
				} else {
					needCell += am.getNum();
				}
			}
			Knapsack bag = player.getKnapsack_common();
			if (bag.getEmptyNum() >= needCell) {			//背包空格足够
				for (int i=0; i<aeList.size(); i++) {
					ChestArticleModel am = aeList.get(i);
					Article a = ArticleManager.getInstance().getArticleByCNname(am.getArticleCNNName());
					if (a == null) {
						logger.warn("[发送物品] [异常] [物品不存在] [" + player.getLogString() + "] [" + am + "]");
						continue;
					}
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, am.isBind(), ArticleEntityManager.宝箱大乱斗, player,
							am.getColorType(), am.getNum(), true);
					for (int j=0; j<am.getNum(); j++) {
						bag.put(ae, "宝箱大乱斗");
						EnchantEntityManager.sendArticleStat(player, ae, "宝箱大乱斗");
					}
				}
			} else {
				for (int i=0; i<aeList.size(); i++) {
					ChestArticleModel am = aeList.get(i);
					Article a = ArticleManager.getInstance().getArticleByCNname(am.getArticleCNNName());
					if (a == null) {
						logger.warn("[发送物品] [异常] [物品不存在] [" + player.getLogString() + "] [" + am + "]");
						continue;
					}
					list.add(new ActivityProp(am.getArticleCNNName(), am.getColorType(), am.getNum(), am.isBind()));
				}
				ActivityProp[] props = list.toArray(new ActivityProp[0]);
				ActivityManager.sendMailForActivity(players, props,Translate.系统, Translate.神魂大乱斗奖励物品, "宝箱大乱斗");
				player.sendError(Translate.背包已满);
			}
		} catch (Exception e) {
			logger.warn("[发送物品] [异常] [" + player.getLogString() + "]", e);
		}
	}
	
	
	/**
	 * 验证玩家是否可以进入活动地图
	 * @param player
	 * @return  null代表可以进入
	 */
	public String validate4Enter(Player player) {
		if (!checkTime()) {
			return Translate.未到进入时间;
		}
		if (player.getLevel() < baseModel.getLevelLimit()) {
			return Translate.等级不足;
		}
		return fight.check4Enter(player);
	}
	
	public boolean checkTime() {
		long openTime = -1;
		long now = System.currentTimeMillis();
		for (int i=0; i<baseModel.getOpenDayOfWeek().length; i++) {
			Calendar cal = Calendar.getInstance();
			if (cal.get(Calendar.DAY_OF_WEEK) == baseModel.getOpenDayOfWeek()[i]) {		//只关注天相同
				cal.set(Calendar.HOUR_OF_DAY, baseModel.getOpenHourOfDay()[i]);
				cal.set(Calendar.MINUTE, baseModel.getOpenMinitOfHour()[i]);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				openTime = cal.getTimeInMillis();
				if (now >= openTime && now <= (openTime + baseModel.getLastTime())) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean checkTime(long now) {
		long openTime = -1;
		for (int i=0; i<baseModel.getOpenDayOfWeek().length; i++) {
			Calendar cal = Calendar.getInstance();
			if (cal.get(Calendar.DAY_OF_WEEK) == baseModel.getOpenDayOfWeek()[i]) {		//只关注天相同
				cal.set(Calendar.HOUR_OF_DAY, baseModel.getOpenHourOfDay()[i]);
				cal.set(Calendar.MINUTE, baseModel.getOpenMinitOfHour()[i]);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				openTime = cal.getTimeInMillis();
				if (now >= openTime && now <= (openTime + baseModel.getLastTime())) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean checkTime4HeartBeat() {
		long openTime = -1;
		long now = System.currentTimeMillis();
		for (int i=0; i<baseModel.getOpenDayOfWeek().length; i++) {
			Calendar cal = Calendar.getInstance();
			if (cal.get(Calendar.DAY_OF_WEEK) == baseModel.getOpenDayOfWeek()[i]) {		//只关注天相同
				cal.set(Calendar.HOUR_OF_DAY, baseModel.getOpenHourOfDay()[i]);
				cal.set(Calendar.MINUTE, baseModel.getOpenMinitOfHour()[i]);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				openTime = cal.getTimeInMillis() - 10000;						//开始前10秒修改副本心跳
				if (now >= openTime && now <= (openTime + baseModel.getLastTime() + 10000)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 是否到时间清楚上次活动数据
	 * @return
	 */
	public boolean isEndTime() {
		long openTime = -1;
		long now = System.currentTimeMillis();
		for (int i=0; i<baseModel.getOpenDayOfWeek().length; i++) {
			Calendar cal = Calendar.getInstance();
			if (cal.get(Calendar.DAY_OF_WEEK) == baseModel.getOpenDayOfWeek()[i]) {		//只关注天相同
				cal.set(Calendar.HOUR_OF_DAY, baseModel.getOpenHourOfDay()[i]);
				cal.set(Calendar.MINUTE, baseModel.getOpenMinitOfHour()[i]);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				openTime = cal.getTimeInMillis();
				long cleanTime = openTime - 开始前多久清除上次数据;
				if (now >= cleanTime && now < openTime) {		
					return true;
				}
			}
		}
		return false;
	}
	
	public int isNoticeTime(long now, long lastNoticeTime) {
		long openTime = -1;
		for (int i=0; i<baseModel.getOpenDayOfWeek().length; i++) {
			Calendar cal = Calendar.getInstance();
			if (cal.get(Calendar.DAY_OF_WEEK) == baseModel.getOpenDayOfWeek()[i]) {		//只关注天相同
				cal.set(Calendar.HOUR_OF_DAY, baseModel.getOpenHourOfDay()[i]);
				cal.set(Calendar.MINUTE, baseModel.getOpenMinitOfHour()[i]);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				openTime = cal.getTimeInMillis();
				for (int j=0;j<开始前多久发世界提示.length; j++) {
					long noticeTime = openTime - 开始前多久发世界提示[j];
					long noticeTime2 = openTime;
					if (j < (开始前多久发世界提示.length-1)) {
						noticeTime2 -= 开始前多久发世界提示[j+1];
					}
					if (now >= noticeTime && now < noticeTime2 && lastNoticeTime < noticeTime) {
						return j;
					}
				}
			}
		}
		return -1;
	}
	
	public boolean isPlayerInActive(Player player) {
		try {
			if (player.getCurrentGame() != null) {
				if (this.isChestFightMap(player.getCurrentGame().gi.name)) {
					return fight.getPlayerGame(player) != null;
				}
			}
		} catch (Exception e) {
			
		}
		return false;
	}
	
	public Game getFightGameByPlayer(Player player) {
		return fight.getPlayerGame(player);
	}
	
	public boolean isChestFightMap(String mapName) {
		return false;
//		if (baseModel.getMapName().equalsIgnoreCase(mapName)) {
//			return true;
//		}
//		return false;
	}
	
	public void init() throws Exception {
		inst = this;
//		initFile();
//		initGame();
	}
	
	public static void main(String[] args) throws Exception {
		ChestFightManager ins = new ChestFightManager();
		ins.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//carbon//chestFight.xls");
		ins.initFile();
	}
	
	/**
	 * 初始化宝箱争夺地图
	 * @throws Exception
	 */
	public void initGame() throws Exception {
		thread = new ChestFightThread();
		fight = new ChestFight();
		fight.maxAllowNum = baseModel.getMaxAllowPlayer();
		fight.已经开启的宝箱个数 = 0;
		fight.宝箱个数 = baseModel.getChestNum();
		fight.game = DevilSquareManager.instance.createNewGame(baseModel.getMapName(),"1");
		fight.setState(ChestFight.结束);
		thread.start();
	}
	
	public void initFile() throws Exception {
		File f = new File(getFileName());
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0); // 基础配置
		int rows = sheet.getPhysicalNumberOfRows();
		{
			HSSFRow row2 = sheet.getRow(1);
			setBaseModel(row2);
		}
		sheet = workbook.getSheetAt(2);			//宝箱物品配置
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				ChestArticleModel model = this.getChestArticleModel(row);
				articleMap.put(model.getId(), model);
			}catch(Exception e) {
				throw new Exception(getFileName() + "第" + (i+1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(1);			//宝箱配置---最后再读这页
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				ChestModel model = this.getChestModel(row);
				chests.put(model.getChestType(), model);
			}catch(Exception e) {
				throw new Exception(getFileName() + "第" + (i+1) + "行异常！！", e);
			}
		}
		int size = chests.size();
		for (ChestArticleModel am : articleMap.values()) {
			if (am.getProb().length != size) {
				throw new Exception("["+am.getId()+"] ["+am.getArticleCNNName()+"] [物品概率配置与宝箱类型个数不符]");
			}
		}
	}
	
	public ChestArticleModel getChestArticleModel(HSSFRow row) {
		ChestArticleModel model = new ChestArticleModel();
		int index = 0;
		model.setId(ReadFileTool.getInt(row, index++, logger));
		model.setArticleCNNName(ReadFileTool.getString(row, index++, logger));
		model.setColorType(ReadFileTool.getInt(row, index++, logger));
		model.setNum(ReadFileTool.getInt(row, index++, logger));
		model.setBind(ReadFileTool.getBoolean(row, index++, logger));
		model.setProb(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		return model;
	}
	
	public ChestModel getChestModel(HSSFRow row) {
		ChestModel model = new ChestModel();
		int index = 0;
		model.setChestType(ReadFileTool.getInt(row, index++, logger));
		model.setChestName(ReadFileTool.getString(row, index++, logger));
		model.setOpenTime(ReadFileTool.getLong(row, index++, logger, 0) * 1000L);
		int[] aa = ReadFileTool.getIntArrByString(row, index++, logger, ",");
		List<ChestArticleModel> ca = new ArrayList<ChestArticleModel>();
		for (int i=0; i<aa.length; i++) {
			ChestArticleModel m = articleMap.get(aa[i]);
			ca.add(m);
		}
		model.setArticleList(ca);
		model.setMaxObtain(ReadFileTool.getInt(row, index++, logger));
		model.setNpcId(ReadFileTool.getInt(row, index++, logger));
		return model;
	}
	
	public void setBaseModel(HSSFRow row) throws Exception {
		baseModel = new ChestBaseModel();
		int index = 0;
		baseModel.setLevelLimit(ReadFileTool.getInt(row, index++, logger));
		baseModel.setMapName(ReadFileTool.getString(row, index++, logger));
		baseModel.setMaxAllowPlayer(ReadFileTool.getInt(row, index++, logger));
		baseModel.setBasicExpRate(ReadFileTool.getDouble(row, index++, logger));
		List<Integer[]> bornPoints = new ArrayList<Integer[]>();
		String bp1 = ReadFileTool.getString(row, index++, logger);
		String[] pp1 = bp1.split(";");
		for (int i=0; i<pp1.length; i++) {
			String[] points = pp1[i].split(",");
			Integer[] b = new Integer[]{Integer.parseInt(points[0]),Integer.parseInt(points[1])};
			bornPoints.add(b);
		}
		baseModel.setBornPoints(bornPoints);
		baseModel.setWaitTime(ReadFileTool.getLong(row, index++, logger, 0L) * 1000L);
		baseModel.setOpenDayOfWeek(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		baseModel.setOpenHourOfDay(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		baseModel.setOpenMinitOfHour(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		baseModel.setLastTime(ReadFileTool.getLong(row, index++, logger, 0L) * 1000L);
		baseModel.setChestNum(ReadFileTool.getInt(row, index++, logger));
		baseModel.setChestTypes(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		baseModel.setRefreshNums(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		List<Integer[]> chestPoints = new ArrayList<Integer[]>();
		String bp2 = ReadFileTool.getString(row, index++, logger);
		String[] pp2 = bp2.split(";");
		for (int i=0; i<pp2.length; i++) {
			String[] points = pp2[i].split(",");
			Integer[] b = new Integer[]{Integer.parseInt(points[0]),Integer.parseInt(points[1])};
			chestPoints.add(b);
		}
		baseModel.setChestBornPoints(chestPoints);
		int total = 0;
		for (int i=0; i<baseModel.getRefreshNums().length; i++) {
			total += baseModel.getRefreshNums()[i];
		}
		if (baseModel.getOpenDayOfWeek().length != baseModel.getOpenHourOfDay().length || baseModel.getOpenDayOfWeek().length != baseModel.getOpenMinitOfHour().length) {
			throw new Exception("[开启时间配置错误]");
		}
		if (baseModel.getChestNum() != total) {
			throw new Exception("[宝箱配置个数与刷新配置个数不一致]");
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
		return ActivityConstant.神魂大乱斗;
	}

	@Override
	public List<String> getActivityOpenTime(long now) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(now);
		int w = c.get(Calendar.DAY_OF_WEEK);
		List<String> result = new ArrayList<String>();
		for (int i=0; i<baseModel.getOpenDayOfWeek().length; i++) {
			if (w == baseModel.getOpenDayOfWeek()[i]) {
				result.add(baseModel.getOpenHourOfDay()[i] + ":" + ActivityDataManager.getMintisStr(baseModel.getOpenMinitOfHour()[i]));
			}
		}
		return result;
	}

	@Override
	public boolean isActivityTime(long now) {
		// TODO Auto-generated method stub
		return this.checkTime(now);
	}
	
}
