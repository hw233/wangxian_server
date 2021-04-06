package com.fy.engineserver.activity.fairyRobbery;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.fairyRobbery.instance.FairyRobbery;
import com.fy.engineserver.activity.fairyRobbery.instance.FairyRobberyEntity;
import com.fy.engineserver.activity.fairyRobbery.model.FairyRobberyModel;
import com.fy.engineserver.activity.fairyRobbery.model.RobberyMonsterModel;
import com.fy.engineserver.activity.furnace.FurnaceManager;
import com.fy.engineserver.bourn.BournCfg;
import com.fy.engineserver.bourn.BournManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.transitrobbery.Option_FairyBackTown;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.ENTER_ROBBERT_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;

/**
 * 仙界渡劫
 * @author Administrator
 *
 */
public class FairyRobberyManager {
	public static FairyRobberyManager inst;
	public static Logger logger = TransitRobberyManager.logger;
	
	private String fileName;
	
	public List<String> mapName = new ArrayList<String>();
	
	public Map<String, FairyRobberyModel> robberys = new LinkedHashMap<String, FairyRobberyModel>();
	
	public static final int 开启线程个数 = 3;
	/** 管理线程 */
	public FairyRobberyThread[] threads = new FairyRobberyThread[开启线程个数];
	/** 渡劫结束后传送玩家到固定地点 */
	public static String 传送出地图 = "lingxiaotiancheng";
	public static int[] 坐标 = new int[]{3573,1325};
	public static String NPCNAME = "仙劫使者";
	
	public static final int 击杀 = 0;
	public static final int 生存 = 1;
	
	public volatile int 上次加入副本索引 = 0;
	
	public void init() throws Exception {
		inst = this;
		initFile();
		for (int i=0; i<threads.length; i++) {
			threads[i] = new FairyRobberyThread();
			threads[i].setName("[仙界渡劫-" + i + "]");
			threads[i].start();
		}
	}
	
	public void notifyPlayerStart(Player player) {
		try {
			for (FairyRobberyThread t : threads) {
				if (t.notifyStart(player)){
					if (logger.isDebugEnabled()) {
						logger.debug("[仙界渡劫] [收到玩家开始通知] [" + player.getLogString() + "]");
					}
					return;
				}
			}
		} catch (Exception e) {
			logger.warn("[仙界渡劫] [玩家点击开始按钮] [异常] [" + player.getLogString() + "]", e);
		}
	}
	
	/**
	 * 玩家离开渡劫场景
	 * @param player
	 */
	public void notifyPlayerLeaveRobbery(Player player) {
		try {
			if (this.isPlayerInRobbery(player)) {
				for (FairyRobberyThread t : threads) {
					if (t.notifyLeaveGame(player)){
						return;
					}
				}
			}
		} catch (Exception e) {
			logger.warn("[仙界渡劫] [处理玩家离开场景] [异常] [" + player.getLogString() + "]", e);
		}
	}
	public void notifyKillBoss(Player player, long bossId) {
		try {
			if (this.isPlayerInRobbery(player)) {
				for (FairyRobberyThread t : threads) {
					if (t.notifyKillBoss(player, bossId)){
						return;
					}
				}
			}
		} catch (Exception e) {
			logger.warn("[仙界渡劫] [处理玩家离开场景] [异常] [" + player.getLogString() + "]", e);
		}
	}
	/**
	 * 获取玩家渡劫game
	 * @param player
	 * @return
	 */
	public Game getPlayerRobberyGame(Player player) {
		try {
			Game g = null;
			for (FairyRobberyThread t : threads) {
				g = t.getPlayerGame(player);
				if (g != null) {
					return g;
				}
			}
		} catch (Exception e) {
			logger.warn("[仙界渡劫] [获取玩家渡劫地图] [异常] [" + player.getLogString() + "]", e);
		}
		return null;
	}
	/**
	 * 玩家死亡
	 * @param player
	 */
	public void notifyPlayerDeath(Player player) {
		try {
			if (this.isPlayerInRobbery(player)) {
				for (FairyRobberyThread t : threads) {
					if (t.notifyPlayerDead(player)) {
						return ;
					}
				}
			}
		} catch (Exception e) {
			logger.warn("[仙界渡劫] [处理玩家死亡] [异常] [" + player.getLogString() + "]", e);
		}
	}
	/**
	 * 检测玩家是否可以进入渡劫
	 * @param player
	 * @param robberyName
	 * @return  null 代表可以进入   
	 */
	public String validPlayerEnter(Player player, String robberyName) {
		FairyRobberyModel model = this.robberys.get(robberyName);
		if (model == null) {
			return Translate.服务器出现错误;
		}
		if (player.getLevel() < model.getLevelLimit()) {
			return Translate.等级不足;
		}
		if (player.getCurrentGame() == null || !传送出地图.equalsIgnoreCase(player.getCurrentGame().gi.name)) {
			return Translate.距离进入点太远;
		}
		if (player.getClassLevel() != model.getClassLvLimit()) {
			return Translate.境界不满足;
		}
		Task task = TaskManager.getInstance().getTask(model.getTaskId());
		if (!player.inDeliver(task)) {
			return String.format(Translate.需要完成任务XX, task.getName());
		}
		BournCfg bournCfg = BournManager.getInstance().getBournCfg(player.getClassLevel());
		if (player.getBournExp() < bournCfg.getExp()) {
			return Translate.需要灵气值满;
		}
		FairyRobberyEntity entity = FairyRobberyEntityManager.inst.getEntity(player);
		if (entity == null || (entity.getPassLevel()+1) != model.getId()) {
			return "未通过之前渡劫";
		}
		return null;
	}
	
	public void notifyPlayerEnterRobbery(Player player, String robberyName) {
		String result = this.validPlayerEnter(player, robberyName);
		if (result != null) {
			player.sendError(result);
			return ;
		}
		int temp = 上次加入副本索引++;
		if (temp < 0 || temp >= threads.length) {
			temp = 0;
		}
		FairyRobbery f = this.createNewRobbery(player, robberyName);
		if (f != null) {
			threads[temp].addNewRobbery(f);
		}
	}
	/**
	 * 创建副本
	 * @param player
	 * @param robberyName
	 * @return
	 */
	public FairyRobbery createNewRobbery(Player player, String robberyName) {
		FairyRobberyModel model = this.robberys.get(robberyName);
		if (model == null) {
			logger.warn("[仙界渡劫] [创建渡劫副本] [异常] [对应渡劫不存在] [robberyName:" + robberyName + "] [" + player.getLogString() + "]");
			return null;
		}
		FairyRobbery robbery = new FairyRobbery();
		robbery.playerId = player.getId();
		robbery.game = FurnaceManager.createNewGame(player, model.getMapName(), logger);
		if (robbery.game == null) {
			logger.warn("[仙界渡劫] [创建渡劫副本] [异常] [创建地图异常] [robberyName:" + robberyName + "] [" + player.getLogString() + "]");
			return null;
		}
		robbery.name = robberyName;
		Game g = player.getCurrentGame();
		if (g == null) {
			logger.warn("[仙界渡劫] [玩家进入副本失败] [玩家当前地图为空] [robberyName:" + robberyName + "] [" + player.getLogString() + "]");
			return null;
		}
		if (player.isIsUpOrDown()) {
			player.downFromHorse(true);
		}
		if (player.getActivePetId() > 0 ) {		//收回宠物
			player.packupPet(true);
		}
		ENTER_ROBBERT_RES req = new ENTER_ROBBERT_RES(GameMessageFactory.nextSequnceNum(), 0, (byte) 0, model.getStartDes());
		player.addMessageToRightBag(req);
		g.transferGame(player, new TransportData(0, 0, 0, 0, robbery.game.gi.name, model.getBornPoint()[0],  model.getBornPoint()[1]));
		logger.warn("[仙界渡劫] [玩家进入副本成功] [robberyName:" + robberyName + "] [" + player.getLogString() + "]");
		return robbery;
	}
	
	public boolean isPlayerInRobbery(Player player) {
		try {
			Game g = player.getCurrentGame();
			if (g == null) {
				return false;
			}
			return mapName.contains(g.gi.name);
		} catch (Exception e) {
			logger.warn("[仙界渡劫] [判断玩家是否在渡劫地图] [异常] [" + player.getLogString() + "]", e);
		}
		return false;
	}
	
	public boolean isRobberyMap(String gameName) {
		return mapName.contains(gameName);
	}
	
	public static void 回城复活(Player player){
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate buffTemplate = btm.getBuffTemplateByName(Translate.无敌buff);
		if(buffTemplate != null) {
			Buff buff1 = buffTemplate.createBuff(1);
			if (buff1 != null) {
				buff1.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 2000);
				player.placeBuff(buff1);
				player.是否回城复活 = true;
			}
		}
		player.setHp(player.getMaxHP() / 2);
		player.setMp(player.getMaxMP() / 2);
		player.setState(Player.STATE_STAND);
		player.notifyRevived();
		// 对player的囚禁操作，用buff形式表示，此buff累计还是已经囚禁的囚犯不让再次囚禁
		List<Buff> buffs = player.getAllBuffs();
		if (buffs != null) {
			for (Buff buff : buffs) {
				if (buff != null && buff.getTemplate() != null && CountryManager.囚禁buff名称.equals(buff.getTemplate().getName())) {
					return;
				}
			}
		}
		PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte)0, "", player.getMaxHP() / 2, player.getMaxMP() / 2);
		player.addMessageToRightBag(res);
	}
	
	public static void 回城(Player p, Game game){
		try{
			Game currentGame = p.getCurrentGame();
			if(currentGame != null && !currentGame.gi.name.equals(game.gi.name)){
				logger.info("[玩家已经不再这个区域了，不需要在回程了] [" + p.getLogString() + "]");
				return;
			}
			String mapName = 传送出地图;
			int x = 坐标[0];
			int y = 坐标[1];
			game.transferGame(p, new TransportData(0, 0, 0, 0, mapName, x, y), true);
			logger.warn("[回城] [成功] ["+p.getLogString()+"]");
		}catch(Exception e){
			logger.warn("[回城] [异常]", e);
		}
	}
	
	public static void popBackTownWindow(Player player, Game game, String des){
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		mw.setDescriptionInUUB(des);
		Option_FairyBackTown option1 = new Option_FairyBackTown();
		option1.setGame(game);
		option1.setText(RobberyConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(RobberyConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		player.addMessageToRightBag(creq);
	}
	
	public static void main(String[] args) throws Exception {
		FairyRobberyManager ins = new FairyRobberyManager();
		ins.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//transitRobbery//fairyrobbery.xls");
		ins.initFile();
	}
	
	public void initFile() throws Exception{
		File f = new File(getFileName());
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0); 
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				FairyRobberyModel model = this.getFairyRobberyModel(row);
				if (model.getBornPoint().length != 2) {
					throw new Exception("[玩家出生点坐标配置错误] [" + Arrays.toString(model.getBornPoint()) + "]");
				}
				for (RobberyMonsterModel m : model.getBossList()) {
					if (m.getBornPoint().length != 2) {
						throw new Exception("[boss出生点坐标配置错误] [" + Arrays.toString(m.getBornPoint()) + "]");
					}
				}
				robberys.put(model.getName(), model);
				if (!mapName.contains(model.getMapName())) {
					mapName.add(model.getMapName());
				}
//				System.out.println(model);
			}catch(Exception e) {
				throw new Exception(getFileName() + "第" + (i+1) + "行异常！！", e);
			}
		}
	}
	public FairyRobberyModel getFairyRobberyModel(HSSFRow row){
		FairyRobberyModel model = new FairyRobberyModel();
		int index = 0;
		model.setId(ReadFileTool.getInt(row, index++, logger));
		model.setName(ReadFileTool.getString(row, index++, logger));
		model.setLevelLimit(ReadFileTool.getInt(row, index++, logger));
		model.setTaskId(ReadFileTool.getLong(row, index++, logger, 0));
		model.setClassLvLimit(ReadFileTool.getInt(row, index++, logger));
		model.setMapName(ReadFileTool.getString(row, index++, logger));
		model.setBornPoint(ReadFileTool.getIntArrByString(row, index++, logger, ","));
		model.setStartDes(ReadFileTool.getString(row, index++, logger));
		model.setFailureDes(ReadFileTool.getString(row, index++, logger));
		model.setEndDes(ReadFileTool.getString(row, index++, logger));
		model.setRefreshBossNum(ReadFileTool.getInt(row, index++, logger));
		List<RobberyMonsterModel> bossList = new ArrayList<RobberyMonsterModel>();
		String bossIds = ReadFileTool.getString(row, index++, logger);
		int[] points = ReadFileTool.getIntArrByString(row, index++, logger, ",");
		String timeLimits = ReadFileTool.getString(row, index++, logger);
		String succeedtypes = ReadFileTool.getString(row, index++, logger);
		if (model.getRefreshBossNum() > 1) {
			String[] bb = bossIds.split(",");
			String[] tls = null;
			String[] sct = null;
			if (timeLimits.contains(",")) {
				tls = timeLimits.split(",");
			}
			if (succeedtypes.contains(",")) {
				sct = succeedtypes.split(",");
			}
			for (int i=0; i<model.getRefreshBossNum(); i++) {
				RobberyMonsterModel rm = new RobberyMonsterModel();
				rm.setBossId(Integer.parseInt(bb[i]));
				rm.setBornPoint(points);
				if (tls != null) {
					rm.setLimitTime(Long.parseLong(tls[i])*1000);
				} else {
					rm.setLimitTime((long) (Double.parseDouble(timeLimits)*1000));
				}
				if (sct != null) {
					rm.setSuccType(Integer.parseInt(sct[i]));
				} else {
					rm.setSuccType(Integer.parseInt(succeedtypes));
				}
				bossList.add(rm);
			}
		} else {
			RobberyMonsterModel rm = new RobberyMonsterModel();
			rm.setBossId(Integer.parseInt(bossIds));
			rm.setBornPoint(points);
			rm.setLimitTime((long) (Double.parseDouble(timeLimits)*1000));
			rm.setSuccType(Integer.parseInt(succeedtypes));
			bossList.add(rm);
		}
		model.setBossList(bossList);
		return model;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
