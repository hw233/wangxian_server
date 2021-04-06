package com.fy.engineserver.activity.furnace;

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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.furnace.instance.FrunaceInstance;
import com.fy.engineserver.activity.furnace.instance.FurnaceGame;
import com.fy.engineserver.activity.furnace.model.FurnaceBaseModel;
import com.fy.engineserver.activity.furnace.model.FurnaceModel;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.FrunaceNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class FurnaceManager {
	public static FurnaceManager inst;
	
	public static Logger logger = ActivitySubSystem.logger;
	
	public DiskCache disk = null;
	public static final String key = "FURNACE";
	/** key=playerId */
	public Map<Long, FurnaceGame> furnaceMap = new Hashtable<Long, FurnaceGame>();
	/** key=丹炉id */
	public Map<Integer, FurnaceModel> modelMap = new HashMap<Integer, FurnaceModel>();
	
	public Map<Integer, String> translate = new HashMap<Integer, String>();
	
	private String diskPath;
	
	private String fileName;
	
	public FurnaceBaseModel baseModel;
	
	public static int[] 仙丹经验比例 = new int[]{9,15,30,45,30,15,9,3}; 
	
	public static final int threadNum = 3;
	public int lastNum = 0;
	public FurnaceThread[] threads = new FurnaceThread[threadNum];
	public Object lock = new Object();
	
	public void init() throws Exception {
		inst = this;
		initFile();
		initDisk();
		for (int i=0; i<threads.length; i++) {
			threads[i] = new FurnaceThread();
			threads[i].start();
		}
	}
	
	public static void main(String[] args) throws Exception {
		String filePath = "E:/javacode2/hg1-clone/game_mieshi_server/conf/game_init_config/frunace/frunace.xls";
		FurnaceManager man = new FurnaceManager();
		man.setFileName(filePath);
		man.initFile();
	}
	/**
	 * 进入游戏
	 * @param player
	 */
	public void enterFrunaceGame(Player player) {
		String result = check4Enter(player);
		if (result == null) {
			FrunaceInstance inst = new FrunaceInstance();
			inst.game = createNewGame(player, baseModel.getMapName(), logger);
			if (inst.game == null) {
				logger.warn("[炼丹] [进入炼丹地图] [地图不存在] [" + player.getLogString() + "]");
				return ;
			}
			inst.playerId = player.getId();
			inst.maxPickTimes = baseModel.getFurnaceNum();
			int index = this.getNextIndex();
			inst.threadIndex = index;
			threads[index].addNewInst(inst);
			for (int i=0; i<baseModel.getFurnaceNum(); i++) {
				Integer[] points = baseModel.getFurnacePoints().get(i);
				this.refreshNpc(inst.game, points[0], points[1], baseModel.getFurnaceIds().get(i));
			}
			player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, inst.game.gi.name, baseModel.getBornPoint()[0], baseModel.getBornPoint()[1]));
			player.chanllengeFlag = 3;
			try {
				AchievementManager.getInstance().record(player, RecordAction.仙丹修炼次数);
			} catch (Exception e) {
				PlayerAimManager.logger.warn("[目标系统] [统计仙丹修炼次数] [异常] [" + player.getLogString() + "]", e);
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[炼丹] [玩家进入炼丹地图] [" + player.getLogString() + "]");
			}
		} else {
			player.sendError(result);
		}
	}
	
	public static void popwindow4enterFrun(Player player) {
		try {
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			Option_Cancel option2 = new Option_Cancel();
			option2.setText(Translate.确定);
			Option[] options = new Option[] {option2};
			mw.setOptions(options);
			String msg = FurnaceManager.inst.translate.get(3);
			mw.setDescriptionInUUB(msg);
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			player.addMessageToRightBag(creq);
		} catch (Exception e) {
			logger.warn("[炼丹过图弹窗] [异常] [" + player.getLogString() + "]", e);
		}
	}
	/**
	 * 是否为炼丹地图
	 * @param gamename
	 * @return
	 */
	public boolean isFurnaceMap(String gamename) {
		return baseModel.getMapName().equals(gamename);
	}
	
	public void pickup(Player player, int stayIndex,long npcId) {
		for (int i=0; i<threadNum; i++) {
			if (threads[i].pickup(player,stayIndex,npcId)) {
				return ;
			}
		}
	}
	
	public boolean isPicked(Player player, long npcId) {
		for (int i=0; i<threadNum; i++) {
			if (threads[i].isPicked(player,npcId)) {
				return true;
			}
		}
		return false;
	}
	
	public Game findPlayerGame(Player player) {
		Game game = null;
		for (int i=0; i<threadNum; i++) {
			game = threads[i].findPlayerGame(player);
			if (game != null) {
				return game;
			}
		}
		return null;
	}
	
	public void notifyPlayerLeaveGame(Player player) {
		try {
			for (int i=0; i<threadNum; i++) {
				threads[i].notifyLeaveGame(player);
			}
		} catch (Exception e) {
			logger.warn("[炼丹] [玩家离开地图] [异常] [" + player.getLogString() + "]", e);
		}
	}
	
	/**
	 * 刷npc
	 * @param game
	 * @param x
	 * @param y
	 */
	public void refreshNpc(Game game, int x, int y, int furnaceId) {
		FurnaceModel model = modelMap.get(furnaceId);
		if (model == null) {
			return ;
		}
		NPC npc = MemoryNPCManager.getNPCManager().createNPC(model.getNpcId());
		if (npc != null && npc instanceof FrunaceNPC) {
			((FrunaceNPC)npc).setMinTime(model.getMinTime());
			((FrunaceNPC)npc).setMaxTime(model.getMaxTime());
			npc.setX(x);
			npc.setY(y);
			game.addSprite(npc);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[创建丹炉npc] [失败] [npc类型不对或者不存在] [" + model.getNpcId() + "] [furnaceId : " + furnaceId + "]");
			}
		}
	}
	
	public int getNextIndex() {
		synchronized (lock) {
			if (lastNum >= threadNum) {
				lastNum = 0;
			}
			return lastNum++;
		}
	}
	/**
	 * 进入游戏前检查，检查时扣除次数
	 * @param player
	 * @return
	 */
	public String check4Enter(Player player) {
		if (player.getLevel() < baseModel.getLevelLimit()) {
			return Translate.等级不足;
		}
		if (player.getCurrentGame() == null) {
			return "当前地图为空";
		}
		FurnaceGame furnace = furnaceMap.get(player.getId());
		if (furnace == null) {
			furnace = new FurnaceGame();
			furnace.setPlayerId(player.getId());
			furnace.setLastResetTime(System.currentTimeMillis());
			furnace.setEnterTimes(0);
		}
		furnace.reset();
		if (furnace.getEnterTimes() >= getCycleTimes()) {
			return translate.get(2);
		}
		furnace.setEnterTimes(furnace.getEnterTimes() + 1);
		furnaceMap.put(player.getId(), furnace);
		disk.put(key, (Serializable) furnaceMap);
		return null;
		
	}
	
	public int getPlayerEnterTimes(Player player) {
		FurnaceGame furnace = furnaceMap.get(player.getId());
		if (furnace == null) {
			return 0;
		}
		furnace.reset();
		return furnace.getEnterTimes();
	}
	
	public static Game createNewGame(Player player , String mapname, Logger logger){
		try{
			GameManager gameManager = GameManager.getInstance();
			GameInfo gi = gameManager.getGameInfo(mapname);
			if(gi == null){
				if(logger.isWarnEnabled())
					logger.warn("[初始化场景] [失败] [对应的地图信息不存在][玩家:{}][{}][{}]", new Object[]{player.getName(),player.getId(),mapname});
				return null;
			}
			Game newGame = new Game(gameManager,gi);
			try {
				newGame.init();
			} catch (Exception e) {
				logger.error("[初始化场景] [异常]",e);
			}
			return newGame; 
		}catch(Exception e){
			logger.error("[初始化场景] [异常]", e);
		}
		return null;
	}
	
	public int getCycleTimes() {
		int result = baseModel.getEnterTimes();
		return result;
	}
	
	public void initFile() throws Exception {
		File f = new File(getFileName());
//		f = new File(ConfigServiceManager.getInstance().getFilePath(f));
//		if(!f.exists()){
//			throw new Exception(getFileName() + " 配表不存在! " + f.getAbsolutePath());
//		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0); // 基础配置
		int rows = sheet.getPhysicalNumberOfRows();
		try {
			HSSFRow row2 = sheet.getRow(1);
			int index= 0 ;
			int levelLimit = ReadFileTool.getInt(row2, index++, logger);
			int enterTimes = ReadFileTool.getInt(row2, index++, logger);
			String mapname = ReadFileTool.getString(row2, index++, logger);
			int[] bornPoint = ReadFileTool.getIntArrByString(row2, index++, logger, ",");
			if (bornPoint.length != 2) {
				throw new Exception("[玩家出生点坐标错误] [" + Arrays.toString(bornPoint) + "]");
			}
			int furnaceNum = ReadFileTool.getInt(row2, index++, logger);
			int[] furnaceIds = ReadFileTool.getIntArrByString(row2, index++, logger, ",");
			List<Integer> furnaceIdss = new ArrayList<Integer>();
			for (int i=0; i<furnaceIds.length; i++) {
				furnaceIdss.add(furnaceIds[i]);
			}
			String[] points = ReadFileTool.getStringArr(row2, index++, logger, ";");
			List<Integer[]> furnacePoints = new ArrayList<Integer[]>();
			for (int i=0; i<points.length; i++) {
				String[] str = points[i].split(",");
				Integer[] ii = new Integer[]{Integer.parseInt(str[0]), Integer.parseInt(str[1])};
				furnacePoints.add(ii);
			}
			if (furnaceNum != furnaceIdss.size() || furnaceIdss.size() != furnacePoints.size()) {
				throw new Exception("[丹炉数量与id或者坐标数不匹配]");
			}
			baseModel = new FurnaceBaseModel(levelLimit,enterTimes,mapname,bornPoint,furnaceNum,furnaceIdss,furnacePoints);
//			System.out.println(baseModel);
		} catch (Exception e) {
			logger.warn("["+getFileName()+"] [" + sheet.getSheetName() + "] [异常]", e);
		}
		
		sheet = workbook.getSheetAt(1);			//丹炉配置
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				FurnaceModel model = this.getFurnaceModel(row);
				modelMap.put(model.getId(), model);
//				System.out.println(model);
			}catch(Exception e) {
				throw new Exception(getFileName() + "第" + (i+1) + "行异常！！", e);
			}
		}
		sheet = workbook.getSheetAt(2);			//翻译
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				translate.put(ReadFileTool.getInt(row, 0, logger), ReadFileTool.getString(row, 1, logger));
			}catch(Exception e) {
				throw new Exception(getFileName() + "第" + (i+1) + "行异常！！", e);
			}
		}
		
	}
	
	private FurnaceModel getFurnaceModel(HSSFRow row) {
		FurnaceModel model = new FurnaceModel();
		int index = 0;
		model.setId(ReadFileTool.getInt(row, index++, logger));
		model.setNpcId(ReadFileTool.getInt(row, index++, logger));
		model.setMinTime(ReadFileTool.getLong(row, index++, logger, 0));
		model.setMaxTime(ReadFileTool.getLong(row, index++, logger, 0));
		return model;
	}
	
	@SuppressWarnings("unchecked")
	public void initDisk() throws Exception {
		File file = new File(getDiskPath());
		if (!file.exists()) {
			file.createNewFile();
		}
		disk = new DefaultDiskCache(file, null, "furnaceDisk", 100L * 365 * 24 * 3600 * 1000L);
		furnaceMap = (Map<Long, FurnaceGame>) disk.get(key);
		if (furnaceMap == null) {
			furnaceMap = new Hashtable<Long, FurnaceGame>();
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDiskPath() {
		return diskPath;
	}

	public void setDiskPath(String diskPath) {
		this.diskPath = diskPath;
	}
}
