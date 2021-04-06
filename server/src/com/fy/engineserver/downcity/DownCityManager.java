package com.fy.engineserver.downcity;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.BeatHeartThread;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.event.PlayerInOutGameListener;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.stat.DownCitySchedule;
import com.fy.engineserver.downcity.stat.DownCityScheduleManager;
import com.fy.engineserver.message.DOWNCITY_PREPARE_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 副本管理
 * 
 * 
 *
 */
public class DownCityManager implements Runnable {

//	public static Logger logger = Logger.getLogger(DownCityManager.class);
public	static Logger logger = LoggerFactory.getLogger(DownCityManager.class);
	
	private static DownCityManager self;
	
	public static String[] 进入副本所需要的物品 = new String[]{Translate.如花的铃铛,Translate.如花的拨浪鼓,Translate.牛魔王的戒尺,Translate.牛魔王的藤条,Translate.舜的慧剑,Translate.舜的慧刀,Translate.孟婆的迷魂汤,Translate.孟婆的忘情水,Translate.冥帅夺魂铃,Translate.冥帅夺魂令,Translate.崔钰判官笔,Translate.崔钰勾魂笔};
	public static String[] 需要物品才能进入的副本中文名 = new String[]{Translate.太虚幻境,Translate.太虚幻境元神,Translate.玉虚迷境,Translate.玉虚迷境元神,Translate.清虚苦境,Translate.清虚苦境元神,Translate.奈何绝境,Translate.奈何绝境元神,Translate.望乡灭境,Translate.望乡灭境元神,Translate.酆都鬼境,Translate.酆都鬼境元神};
	public static String[] 需要物品才能进入的副本英文名 = new String[]{"taixuhuanjing","taixuhuanjingyuanshen","yuxumijing","yuxumijingyuanshen","qingxukujing","qingxukujingyuanshen","naihejuejing","naihejuejingyuanshen","wangxiangmiejing","wangxiangmiejingyuanshen","fengduguijing","fengdouguijingyuanshen"};
	public static RecordAction[] 对应统计action = new RecordAction[]{RecordAction.通关太虚幻境本尊,RecordAction.通关太虚幻境元神,RecordAction.通关玉虚迷境本尊,RecordAction.通关玉虚迷境元神};
	
	public static int[] 每天进入副本最大次数 = new int[]{20,20,30,30,20,20,30,30,30,30,30,30};
	public static String[] 有进入次数的副本中文名 = new String[]{Translate.太虚幻境,Translate.太虚幻境元神,Translate.玉虚迷境,Translate.玉虚迷境元神,Translate.清虚苦境,Translate.清虚苦境元神,Translate.奈何绝境,Translate.奈何绝境元神,Translate.望乡灭境,Translate.望乡灭境元神,Translate.酆都鬼境,Translate.酆都鬼境元神};
	public static String[] 有进入次数的副本英文名 = new String[]{"taixuhuanjing","taixuhuanjingyuanshen","yuxumijing","yuxumijingyuanshen","qingxukujing","qingxukujingyuanshen","naihejuejing","naihejuejingyuanshen","wangxiangmiejing","wangxiangmiejingyuanshen","fengduguijing","fengdouguijingyuanshen"};
	
	public static String[] 副本中文名 = new String[]{Translate.太虚幻境,Translate.太虚幻境元神,Translate.玉虚迷境,Translate.玉虚迷境元神,Translate.清虚苦境,Translate.清虚苦境元神,Translate.奈何绝境,Translate.奈何绝境元神,Translate.望乡灭境,Translate.望乡灭境元神,Translate.酆都鬼境,Translate.酆都鬼境元神};
	public static String[] 副本英文名 = new String[]{"taixuhuanjing","taixuhuanjingyuanshen","yuxumijing","yuxumijingyuanshen","qingxukujing","qingxukujingyuanshen","naihejuejing","naihejuejingyuanshen","wangxiangmiejing","wangxiangmiejingyuanshen","fengduguijing","fengdouguijingyuanshen"};
	public static int[][] 副本进入坐标 = new int[][]{{3690,307},{3690,307},{3239,318},{3239,318},{4130,2585},{4130,2585},{3490,636},{3490,636},{1928,3755},{1928,3755},{463,3372},{463,3372}};
	public static String[] 通关副本给的物品 = new String[]{Translate.太虚幻境奖励,Translate.太虚幻境_元神奖励,Translate.玉虚迷境奖励,Translate.玉虚迷境_元神奖励,Translate.清虚苦境奖励,Translate.清虚苦境_元神奖励,Translate.奈何绝境奖励,Translate.奈何绝境_元神奖励,Translate.望乡灭境奖励,Translate.望乡灭境_元神奖励,Translate.酆都鬼境奖励,Translate.酆都鬼境_元神奖励};
	
	//限制飞行的副本
	public static String limitFlyName [] = {"wangxiangmiejing","wangxiangmiejingyuanshen","fengduguijing","fengdouuijingyuanshen"};
	public static int limitFlushMonsterIds [] = {10010499,10010471,10010507,10010494};
	public static int limitFlushBoss坐标 [] [] = {{1274,933},{1274,933},{2451,165},{2451,165}};
	public static int wangxiangmiejing精英怪ids [] = {10010501,10010502,10010503};
	public static int wangxiangmiejingyuanshen精英怪ids [] = {10010477,10010478,10010479};
	public static int fengduguijing精英怪ids [] = {10010508,10010509};
	public static int fengdouguijingyuanshen精英怪ids [] = {10010495,10010496};
	
	public static Map<String, int[]> flashBossRelyJy = new HashMap<String, int[]>();
	static{
		flashBossRelyJy.put("wangxiangmiejing", wangxiangmiejing精英怪ids);
		flashBossRelyJy.put("wangxiangmiejingyuanshen", wangxiangmiejingyuanshen精英怪ids);
		flashBossRelyJy.put("fengduguijing", fengduguijing精英怪ids);
		flashBossRelyJy.put("fengdouguijingyuanshen", fengdouguijingyuanshen精英怪ids);
	}
	
	public static byte PREPARE_STATUS_没有准备 = 0;
	public static byte PREPARE_STATUS_准备 = 1;
	public static byte PREPARE_STATUS_不去 = 2;
	
	/**
	 * 如果物品名为空就表示进入副本不需要物品
	 * @return
	 */
	public static String 通过副本中文名得到进入副本所需物品(String downCityName){
		for(int i = 0; i < 需要物品才能进入的副本中文名.length && i < 进入副本所需要的物品.length; i++){
			if(需要物品才能进入的副本中文名[i].equals(downCityName)){
				return 进入副本所需要的物品[i];
			}
		}
		return null;
	}
	
	/**
	 * 如果物品名为空就表示进入副本不需要物品
	 * @return
	 */
	public static String 通过副本英文名得到进入副本所需物品(String downCityName){
		for(int i = 0; i < 需要物品才能进入的副本英文名.length && i < 进入副本所需要的物品.length; i++){
			if(需要物品才能进入的副本英文名[i].equals(downCityName)){
				return 进入副本所需要的物品[i];
			}
		}
		return null;
	}
	public static RecordAction 通过副本统计action(String downCityName){
		for(int i = 0; i < 需要物品才能进入的副本英文名.length && i < 对应统计action.length; i++){
			if(需要物品才能进入的副本英文名[i].equals(downCityName)){
				return 对应统计action[i];
			}
		}
		return null;
	}
	
	/**
	 * 如果物品名为空就表示副本不额外给物品
	 * @return
	 */
	public static String 通过副本中文名得到副本给的物品(String downCityName){
		for(int i = 0; i < 副本中文名.length && i < 通关副本给的物品.length; i++){
			if(副本中文名[i].equals(downCityName)){
				return 通关副本给的物品[i];
			}
		}
		return null;
	}
	
	/**
	 * 如果物品名为空就表示副本不额外给物品
	 * @return
	 */
	public static String 通过副本英文名得到副本给的物品(String downCityName){
		for(int i = 0; i < 副本英文名.length && i < 通关副本给的物品.length; i++){
			if(副本英文名[i].equals(downCityName)){
				return 通关副本给的物品[i];
			}
		}
		return null;
	}
	
	/**
	 * 如果副本有进入次数限制，那么返回进入次数，没有次数限制的副本默认可以进入200次
	 * @return
	 */
	public static int 通过副本中文名得到每天进入副本最大次数(String downCityName){
		for(int i = 0; i < 每天进入副本最大次数.length && i < 有进入次数的副本中文名.length; i++){
			if(有进入次数的副本中文名[i].equals(downCityName)){
				return 每天进入副本最大次数[i];
			}
		}
		return 200;
	}
	
	/**
	 * 如果副本有进入次数限制，那么返回进入次数，没有次数限制的副本默认可以进入200次
	 * @return
	 */
	public static int 通过副本英文名得到每天进入副本最大次数(String downCityName){
		for(int i = 0; i < 每天进入副本最大次数.length && i < 有进入次数的副本英文名.length; i++){
			if(有进入次数的副本英文名[i].equals(downCityName)){
				return 每天进入副本最大次数[i];
			}
		}
		return 200;
	}
	
	public static String 通过副本英文名得到副本中文名(String downCityName){
		for(int i = 0; i < 副本中文名.length && i < 副本英文名.length; i++){
			if(副本英文名[i].equals(downCityName)){
				return 副本中文名[i];
			}
		}
		return downCityName;
	}

	
	public static int[] 通过副本英文名得到副本进入坐标(String downCityName){
		for(int i = 0; i < 副本进入坐标.length && i < 副本英文名.length; i++){
			if(副本英文名[i].equals(downCityName)){
				return 副本进入坐标[i];
			}
		}
		return new int[]{200,200};
	}
	
	public static int[] 通过副本英文名得到BOSS坐标(String downCityName){
		for(int i = 0; i < limitFlushBoss坐标.length && i < limitFlyName.length; i++){
			if(limitFlyName[i].equals(downCityName)){
				return limitFlushBoss坐标[i];
			}
		}
		return new int[]{200,200};
	}

	public static DownCityManager getInstance(){
		if(self == null){
			self = new DownCityManager();
		}
		return self;
	}
	
	/**
	 * 此数据在load方法中生成
	 */
	protected ArrayList<DownCityInfo> dciList = new ArrayList<DownCityInfo>();
	
	/**
	 * 此是运行时的数据, id -- > downCity
	 */
	protected Hashtable<String,DownCity> downCityMap = new Hashtable<String,DownCity>();
	/**
	 * diskcache中存储的标记，true存储false不存
	 */
	public static boolean diskCacheFlag = true;
	boolean running = true;
	int threadNum = 5;//10;
	long removeDcTime;
	public BeatHeartThread[] threads;
	
	Thread thread;
	Thread subThread;
	
	File configFile;
	
	//副本进度存储的目录
	File saveDir;
	
	GameManager gameManager;
	
	
	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	
	public ArrayList<DownCityInfo> getDciList() {
		return dciList;
	}

	public void init() throws Exception{
		if(true){
			return;
		}
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		if(configFile != null && configFile.exists()){
//			load(configFile);
//		}else{
			DownCityInfo di = new DownCityInfo();
			di.seqNum = "01";
			di.name = Translate.太虚幻境;
			di.mapDisplayName = Translate.太虚幻境;
			di.mapName = "taixuhuanjing";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 111;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "02";
			di.name = Translate.太虚幻境元神;
			di.mapDisplayName = Translate.太虚幻境元神;
			di.mapName = "taixuhuanjingyuanshen";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 111;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "03";
			di.name = Translate.玉虚迷境;
			di.mapDisplayName = Translate.玉虚迷境;
			di.mapName = "yuxumijing";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 131;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "04";
			di.name = Translate.玉虚迷境元神;
			di.mapDisplayName = Translate.玉虚迷境元神;
			di.mapName = "yuxumijingyuanshen";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 131;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "05";
			di.name = Translate.清虚苦境;
			di.mapDisplayName = Translate.清虚苦境;
			di.mapName = "qingxukujing";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 151;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "06";
			di.name = Translate.清虚苦境元神;
			di.mapDisplayName = Translate.清虚苦境元神;
			di.mapName = "qingxukujingyuanshen";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 151;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "07";
			di.name = Translate.奈何绝境;
			di.mapDisplayName = Translate.奈何绝境;
			di.mapName = "naihejuejing";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 171;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "08";
			di.name = Translate.奈何绝境元神;
			di.mapDisplayName = Translate.奈何绝境元神;
			di.mapName = "naihejuejingyuanshen";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 171;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "09";
			di.name = Translate.望乡灭境;
			di.mapDisplayName = Translate.望乡灭境;
			di.mapName = "wangxiangmiejing";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 191;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "10";
			di.name = Translate.望乡灭境元神;
			di.mapDisplayName = Translate.望乡灭境元神;
			di.mapName = "wangxiangmiejingyuanshen";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 191;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "11";
			di.name = Translate.酆都鬼境;
			di.mapDisplayName = Translate.酆都鬼境;
			di.mapName = "fengduguijing";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 211;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
			
			di = new DownCityInfo();
			di.seqNum = "12";
			di.name = Translate.酆都鬼境元神;
			di.mapDisplayName = Translate.酆都鬼境元神;
			di.mapName = "fengdouguijingyuanshen";
			di.lastingTime = 3600 * 1000;
			di.minPlayerLevel = 211;
			di.playerNumLimit = 5;
			di.resetType = DownCityInfo.RESET_TYPE_BY_PLAYER;
			di.x = 200;
			di.y = 200;
			dciList.add(di);
//		}
		
		
		
		threads = new BeatHeartThread[threadNum];
		for(int i = 0 ; i < threads.length ; i++){
			threads[i] = new BeatHeartThread();
			threads[i].setName(Translate.text_3985+(i+1));
			threads[i].setBeatheart(5);
			threads[i].start();
		}
		
		if(saveDir.exists() == false){
			saveDir.mkdirs();
		}
		
		loadDownCityProcess(saveDir);
		
		thread = new Thread(this,Translate.text_3986);
		thread.start();
		
		subThread = new Thread(new DownCitySubThread(), "DownCitySubThread");
		subThread.start();
		
		self = this;
		
		logger.warn("[系统初始化] [副本管理器] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
		ServiceStartRecord.startLog(this);
	}
	
	/**
	 * 加载副本进度，副本进度统一保存在一个目录下，
	 * 文件名为： 副本ID.dcp
	 * 文件的格式为xml
	 * @param dir
	 */
	private void loadDownCityProcess(File dir){
		File files[] = dir.listFiles(new FileFilter(){
			public boolean accept(File f) {
				if(f.isFile() && f.getName().endsWith(".dcp")){
					return true;
				}
				return false;
			}});
		
		for(int i = 0 ; i < files.length ; i++){
			Document dom = null;
			try{
				
//				if(TransferLanguage.characterTransferormFlag){
					InputStream is = new FileInputStream(files[i]);
					dom = XmlUtil.load(is,"utf-8");
//				}else{
//					dom = XmlUtil.load(files[i].getAbsolutePath());
//				}
				
			}catch(Exception e){
				System.out.println("[加载副本进度出错] ["+files[i].getAbsolutePath()+"] [XML文件解析出错]");
				if(logger.isWarnEnabled())
					logger.warn("[加载副本进度出错] ["+files[i].getAbsolutePath()+"] [XML文件解析出错]",e);
			}
			if(dom != null){
				try {
					Element root = dom.getDocumentElement();
					String id = XmlUtil.getAttributeAsString(root, "id", null);
					String name = XmlUtil.getAttributeAsString(root, "name", null);
					long createTime = XmlUtil.getAttributeAsLong(root, "createTime");
					long completeTime = XmlUtil.getAttributeAsLong(root, "completeTime");
					long endTime = XmlUtil.getAttributeAsLong(root, "endTime");
					byte downCityState= (byte)XmlUtil.getAttributeAsInteger(root, "downCityState",0);
					long tongguanTimePoint=XmlUtil.getAttributeAsLong(root, "tongguanTimePoint",-1);
					DownCityInfo di = getDownCityInfo(name);
					if(di == null){
						if(endTime < com.fy.engineserver.gametime.SystemTime.currentTimeMillis()){
//							logger.warn("[加载副本进度] [副本配置不存在] ["+name+"] [时间已过期，删除对应文件] ["+files[i].getAbsolutePath()+"]");
							if(logger.isWarnEnabled())
								logger.warn("[加载副本进度] [副本配置不存在] [{}] [时间已过期，删除对应文件] [{}]", new Object[]{name,files[i].getAbsolutePath()});
							files[i].delete();
							throw new Exception("副本["+name+"]配置不存在，时间已过期，删除对应文件");
						}else{
							throw new Exception("副本["+name+"]配置不存在，丢弃此副本进度");
						}
					}
					DownCity dc = new DownCity(id,di);
					GameInfo gi = gameManager.getGameInfo(di.getMapName());
					if(gi == null){
						throw new Exception("副本["+name+"]配置对应的地图["+di.getMapName()+"]不存在，丢弃此副本进度");
					}
					Game newGame = new Game(gameManager,gi);
					if(gameManager.getPlayerManager() instanceof PlayerInOutGameListener){
						newGame.addPlayerInOutGameListener((PlayerInOutGameListener)gameManager.getPlayerManager());
					}
			
					newGame.init();
					
					Element eles[] = XmlUtil.getChildrenByName(root, "player");
					for(int j = 0 ; j < eles.length ; j++){
						long pid = XmlUtil.getAttributeAsLong(eles[j], "id",-1);
						if(pid > 0){
							dc.keepProcessPlayerList.add(pid);
						}
					}
					
					HashMap<String,Long> map = new HashMap<String,Long>();
					eles = XmlUtil.getChildrenByName(root, "process");
					for(int j = 0 ; j < eles.length ; j++){
						String key = XmlUtil.getAttributeAsString(eles[j], "key",null, null);
						long value = XmlUtil.getAttributeAsLong(eles[j], "value",0);
						if(key != null){
							map.put(key, value);
						}
					}
					
					
					
					newGame.getSpriteFlushAgent().updateLastDisappearTime(map);
					newGame.setDownCity(dc);
					
					dc.setGame(newGame);
					dc.setCreateTime(createTime);
					dc.setEndTime(endTime);
					dc.setDownCityState(downCityState);
					dc.setTongguanTimePoint(tongguanTimePoint);
					dc.completeTime=completeTime;
					if(dc.completeTime!=0){
						dc.isAllMonsterDead=true;
					}
					
					int index = 0;
					int min = 1000;
					for(int j = 0 ; j < threads.length ; j++){
						int n = threads[j].getGames().length;
						if(n < min){
							min = n;
							index = j;
						}
					}
					threads[index].addGame(newGame);
					dc.threadIndex = index;
					dc.init();
					
					//newGame.getSubThread().setPooled(true);
					
					downCityMap.put(dc.getId(), dc);
					
//					logger.info("[加载副本进度] [成功] ["+dc.getId()+"] ["+dc.getDownCityStateAsString()+"] ["+di.getName()+"] ["+di.getMapName()+"] ["+files[i].getAbsolutePath()+"]");
					if(logger.isInfoEnabled())
						logger.info("[加载副本进度] [成功] [{}] [{}] [{}] [{}] [{}]", new Object[]{dc.getId(),dc.getDownCityStateAsString(),di.getName(),di.getMapName(),files[i].getAbsolutePath()});
					
				} catch (Exception e) {
					if(logger.isWarnEnabled())
						logger.warn("[加载副本进度] [失败] [出现异常] ["+files[i].getAbsolutePath()+"]",e);
				}
			}
		}
		String ids[] = downCityMap.keySet().toArray(new String[0]);
		for(int i = 0 ; i < dciList.size() ; i++){
			DownCityInfo di = dciList.get(i);
			String id = di.seqNum + DateUtil.formatDate(new java.util.Date(), "MMdd");
			int max = 0;
			for(int j = 0 ; j < ids.length ; j++){
				if(ids[j].startsWith(id)){
					int s = Integer.parseInt(ids[j].substring(id.length()));
					if(s > max){
						max = s;
					}
				}
			}
			di.next_id = max+1;
		}
	}
	
	public void destroy(){
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		running = false;
		DownCity dcs[] = downCityMap.values().toArray(new DownCity[0]);
		for(int i = 0 ; i < dcs.length ; i++){
			DownCity d = dcs[i];
			if(d.game.getSpriteFlushAgent().isDirty()){
				HashMap<String,Long> map = d.game.getSpriteFlushAgent().saveLastDisappearTime();
				d.game.getSpriteFlushAgent().setDirty(false);
				saveDownCityProcess(d,map);
			}
		}
		
		System.out.println("[Desctroy] 调用destroy方法保存所有副本进度，数量("+dcs.length+")， cost "+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+" ms");
	}
	
	/**
	 * 保存副本进度
	 * @param dc
	 * @param processMap
	 */
	protected void saveDownCityProcess(DownCity dc,HashMap<String,Long> processMap){
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='gb2312'?>\n");
		sb.append("<downcity id='"+dc.getId()+"' name='"+dc.di.getName()+"' createTime='"+dc.createTime+"' completeTime='"+dc.completeTime+"' endTime='"+dc.endTime+"' downCityState='"+dc.downCityState+"' tongguanTimePoint='"+dc.tongguanTimePoint+"'>\n");
		Long ps[] = dc.keepProcessPlayerList.toArray(new Long[0]);
		for(int i = 0 ; i < ps.length ; i++){
			sb.append("<player id='"+ps[i]+"'/>\n");
		}
		String keys[] = processMap.keySet().toArray(new String[0]);
		for(int i = 0 ; i < keys.length ; i++){
			sb.append("<process key='"+keys[i]+"' value='"+processMap.get(keys[i])+"' />\n");
		}
		sb.append("</downcity>\n");
		File file = new File(saveDir,dc.getId()+".dcp");
		try{
			FileOutputStream output = new FileOutputStream(file);
//			if(TransferLanguage.characterTransferormFlag){
				output.write(sb.toString().getBytes("utf-8"));
//			}else{
//				output.write(sb.toString().getBytes());
//			}
			
			output.flush();
			output.close();
			if(logger.isInfoEnabled()){
//				logger.info("[保存副本进度] [成功] ["+dc.getId()+"] ["+dc.getDownCityStateAsString()+"] ["+dc.getDi().getName()+"] ["+file.getAbsolutePath()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"ms]");
				if(logger.isInfoEnabled())
					logger.info("[保存副本进度] [成功] [{}] [{}] [{}] [{}] [{}ms]", new Object[]{dc.getId(),dc.getDownCityStateAsString(),dc.getDi().getName(),file.getAbsolutePath(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)});
			}
		}catch(Exception e){
			if(logger.isWarnEnabled())
				logger.warn("[保存副本进度] [失败] ["+dc.getId()+"] ["+dc.getDi().getName()+"] ["+file.getAbsolutePath()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"ms]",e);
		}
	}
	
	public DownCityInfo getDownCityInfo(String name){
		for(int i = 0 ; i < dciList.size() ; i++){
			DownCityInfo di = dciList.get(i);
			if(di.getName().equals(name)){
				return di;
			}
		}
		return null;
	}
	
	public boolean isValid(DownCity dc){
		if(dc == null) return false;
		if(dc.isValid() == false) return false;
		if(this.downCityMap.containsKey(dc.getId()) == false) return false;
		return true;
	}
	/**
	 * get DownCity by id
	 */
	public DownCity getDownCityById(String id){
		if(id == null) return null;
		return downCityMap.get(id);
	}
	/**
	 * 得到所有的副本
	 * @return
	 */
	public DownCity[] getAllDownCity(){
		return  downCityMap.values().toArray(new DownCity[0]);
	}
	/**
	 * 通过地图获得副本，一个地图只能作为一个副本。
	 * @param mapName
	 * @return
	 */
	public DownCityInfo getDownCityInfoByMapName(String mapName){
		for(int i = 0 ; i < dciList.size() ; i++){
			DownCityInfo di = dciList.get(i);
			if(di.getMapName().equals(mapName)){
				return di;
			}
		}
		return null;
	}
	
	/**
	 * 玩家进入副本
	 *  
	 *   非组队玩家： 
	 *              1. 无进度，进入新的副本
	 *              2. 有进度，进入此进度的副本
	 *   组队玩家：
	 *              1. 无进度，队长也无进度，队伍上无副本，创建新的副本，设置队伍副本，并进入副本
	 *              2. 无进度，队长也无进度，队伍上有副本，进入此副本
	 *              3. 无进度，队长有进度，进入队长副本
	 *              4. 有进度，队长无进度，队伍也无副本，进入自己的本
	 *              5. 有进度，队长无进度，队伍有副本，提示要重置自己的进度，才能进入副本。在勇士副本时如果副本状态downCityState为0可以重置副本为1不能重置副本
	 *              6. 有进度，队长有进度，提示要重置自己的进度，才能进入副本。在勇士副本时如果副本状态downCityState为0可以重置副本为1不能重置副本
	 * 
	 *  
	 * 如果能进入副本，返回能进入的副本，否则返回null，返回null的同时，会给客户端提示信息。
	 * 
	 * @param player
	 * @param dcName
	 */
	public DownCity enterDownCity(Player player,String dcName){
		
		return null;
	}
	
	/**
	 * 玩家排队后进入副本
	 *  
	 *              1. 队伍上无副本，创建新的副本，设置队伍副本，并进入副本
	 *              2. 队伍上有副本，进入队伍副本
	 * 
	 *  
	 * 如果能进入副本，返回能进入的副本，否则返回null，返回null的同时，会给客户端提示信息。
	 * 
	 * @param player
	 * @param dcName
	 */
	public DownCity lineupEnterDownCity(Player player,String dcName){
		
		return null;
	}
	
	public void notidyPlayerResetDownCity(DownCity dc,Player p){
		Long ii = new Long(p.getId());
		dc.keepProcessPlayerList.remove(ii);
		Player ps[] = dc.getPlayersKeepingProcess();
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < ps.length ; i++){
			sb.append(ps[i].getName() + ",");
		}
//		logger.warn("[重置副本] [玩家："+p.getName()+"]["+p.getId()+"] ["+dc.getId()+"] ["+dc.di.getName()+"] ["+dc.di.getMapName()+"] [保持进度的玩家："+sb+"]");
		if(logger.isWarnEnabled())
			logger.warn("[重置副本] [玩家：{}][{}] [{}] [{}] [{}] [保持进度的玩家：{}]", new Object[]{p.getName(),p.getId(),dc.getId(),dc.di.getName(),dc.di.getMapName(),sb});
		
		if(dc.keepProcessPlayerList.size() == 0){ //所有保留进度的人，都重置了此副本
			dc.setEndTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		}
	}
	/**
	 * 删除副本，包括对应的进度文件
	 * @param dc
	 */
	private void deleteDownCity(DownCity dc){
		downCityMap.remove(dc.getId());
		File file = new File(saveDir,dc.getId()+".dcp");
		boolean b = file.delete();
		if(b == false){
			file.deleteOnExit();
		}
		//dc.getGame().getSubThread().setRunning(false);
//		logger.warn("[副本心跳] [副本过期] [删除副本] ["+dc.getId()+"] ["+dc.di.getName()+"] ["+dc.di.getMapName()+"] ["+file.getAbsolutePath()+"]");
		if(logger.isWarnEnabled())
			logger.warn("[副本心跳] [副本过期] [删除副本] [{}] [{}] [{}] [{}]", new Object[]{dc.getId(),dc.di.getName(),dc.di.getMapName(),file.getAbsolutePath()});
	}
	
	private synchronized DownCity createNewDownCity(Player player,DownCityInfo di){
		String id = null;
		int c = 0;
		while(true){
			id = di.seqNum + DateUtil.formatDate(new java.util.Date(), "MMdd") + formatId(di.nextId(),4);
			if(downCityMap.containsKey(id) ==false){
				break;
			}
			c++;
			if(c == 10000){
//				logger.warn("[创建副本] [失败] [对应的副本已经超过10000个，无ID可用][玩家:"+player.getName()+"]["+player.getId()+"] ["+di.getName()+"] ["+di.getMapName()+"]");
				if(logger.isWarnEnabled())
					logger.warn("[创建副本] [失败] [对应的副本已经超过10000个，无ID可用][玩家:{}][{}] [{}] [{}]", new Object[]{player.getName(),player.getId(),di.getName(),di.getMapName()});
				return null;
			}
		}
		DownCity dc = new DownCity(id,di);
		GameManager gameManager = GameManager.getInstance();
		GameInfo gi = gameManager.getGameInfo(di.getMapName());
		if(gi == null){
//			logger.warn("[创建副本] [失败] [对应的地图信息不存在][玩家:"+player.getName()+"]["+player.getId()+"]["+di.getName()+"] ["+di.getMapName()+"]");
			if(logger.isWarnEnabled())
				logger.warn("[创建副本] [失败] [对应的地图信息不存在][玩家:{}][{}][{}] [{}]", new Object[]{player.getName(),player.getId(),di.getName(),di.getMapName()});
			return null;
		}
		Game newGame = new Game(gameManager,gi);
		if(gameManager.getPlayerManager() instanceof PlayerInOutGameListener){
			newGame.addPlayerInOutGameListener((PlayerInOutGameListener)gameManager.getPlayerManager());
		}
		try {
			newGame.init();
			newGame.setDownCity(dc);
			dc.setGame(newGame);
			dc.setCreateTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			dc.setEndTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + di.getLastingTime());
			
			int index = 0;
			int min = 1000;
			for(int i = 0 ; i < threads.length ; i++){
				int n = threads[i].getGames().length;
				if(n < min){
					min = n;
					index = i;
				}
			}
			threads[index].addGame(newGame);
			dc.threadIndex = index;
			dc.init();
			
		} catch (Exception e) {
			if(logger.isWarnEnabled())
				logger.warn("[创建副本] [失败] [出现异常][玩家:"+player.getName()+"]["+player.getId()+"] ["+di.getName()+"] ["+di.getMapName()+"]",e);
			return null;
		}
		
		downCityMap.put(dc.getId(), dc);
		HashMap<String,Long> map = dc.game.getSpriteFlushAgent().saveLastDisappearTime();
		saveDownCityProcess(dc,map);
		//创建副本后副本信息的保存
		DownCityScheduleManager dcsm = DownCityScheduleManager.getInstance();
		if(dcsm != null){
			try{
			DownCitySchedule dcs = new DownCitySchedule();
			dcs.setName(dc.getDi().getName());
			dcs.setDirty(true);
			dcs.setDownCityId(dc.getId());
			dcs.setStartTime(dc.getCreateTime());
			dcsm.createDownCitySchedule(dcs);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return dc;
	}
	
	public void run(){
		while(running){
			try{
				MemoryMonsterManager sm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
				MemoryNPCManager nm = (MemoryNPCManager)MemoryNPCManager.getNPCManager();
				
				Thread.sleep(2000);
				if(running == false) break;
				
				DownCity dcs[] = downCityMap.values().toArray(new DownCity[0]);
				for(int i = 0 ; i < dcs.length ; i++){
					try{
						dcs[i].heartbeat();
						
						if(dcs[i].isValid() == false){
							Game game = dcs[i].getGame();
							threads[dcs[i].threadIndex].removeGame(dcs[i].getGame());
							deleteDownCity(dcs[i]);
							
							//downCityMap.remove(dcs[i].getId());
							LivingObject los[] = game.getLivingObjects();
							for(int j = 0 ; j < los.length ; j++){
								if (los[j] instanceof Sprite) {
									Sprite sprite = (Sprite) los[j];
									if(sprite instanceof Monster){
										sm.removeMonster((Monster)sprite);
									}else if(sprite instanceof NPC){
										nm.removeNPC((NPC)sprite);
									}
								}
							}
						}
						
					}catch(Throwable e){
						if(logger.isWarnEnabled())
							logger.warn("[副本心跳] [失败] [出现异常] ["+dcs[i].di.getName()+"] ["+dcs[i].di.getMapName()+"]",e);	
					}
				}
				
				if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - removeDcTime >= 60000) {
					removeDcTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					dcs = downCityMap.values().toArray(new DownCity[0]);
					for(int i = 0 ; i < dcs.length ; i++){
						try{
							dcs[i].heartbeat();
							
							if(dcs[i].isValid() == false){
								Game game = dcs[i].getGame();
								threads[dcs[i].threadIndex].removeGame(dcs[i].getGame());
								deleteDownCity(dcs[i]);
								
								//downCityMap.remove(dcs[i].getId());
								LivingObject los[] = game.getLivingObjects();
								for(int j = 0 ; j < los.length ; j++){
									if (los[j] instanceof Sprite) {
										Sprite sprite = (Sprite) los[j];
										if(sprite instanceof Monster){
											sm.removeMonster((Monster)sprite);
										}else if(sprite instanceof NPC){
											nm.removeNPC((NPC)sprite);
										}
									}
								}
							}else if(dcs[i].getDi() != null && dcs[i].getDi().getResetType() == DownCityInfo.RESET_TYPE_BY_DAY){
								Player[] ps = dcs[i].getPlayersInGame();
								if((ps == null || ps.length == 0) && !dateCompare(dcs[i].getCreateTime(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis())){
									Game game = dcs[i].getGame();
									threads[dcs[i].threadIndex].removeGame(dcs[i].getGame());
									deleteDownCity(dcs[i]);
//									logger.warn("[系统零点重置] [name:"+dcs[i].getDi().getName()+"] [resetType:"+dcs[i].getDi().getResetType()+"] [createTime:"+dcs[i].getCreateTime()+"] [id:"+dcs[i].getId()+"]");
									if(logger.isWarnEnabled())
										logger.warn("[系统零点重置] [name:{}] [resetType:{}] [createTime:{}] [id:{}]", new Object[]{dcs[i].getDi().getName(),dcs[i].getDi().getResetType(),dcs[i].getCreateTime(),dcs[i].getId()});
									//downCityMap.remove(dcs[i].getId());
									LivingObject los[] = game.getLivingObjects();
									for(int j = 0 ; j < los.length ; j++){
										if (los[j] instanceof Sprite) {
											Sprite sprite = (Sprite) los[j];
											if(sprite instanceof Monster){
												sm.removeMonster((Monster)sprite);
											}else if(sprite instanceof NPC){
												nm.removeNPC((NPC)sprite);
											}
										}
									}
								}
							}
							
						}catch(Throwable e){
							if(logger.isWarnEnabled())
								logger.warn("[副本心跳] [失败] [出现异常] ["+dcs[i].di.getName()+"] ["+dcs[i].di.getMapName()+"]",e);	
						}
					}
				}
				
			}catch(Throwable e){
				if(logger.isWarnEnabled())
					logger.warn("[副本检查线程] [出现异常]",e);
			}
		}
	}
	/**
	 * 时间对比，如果为一天就返回true否则返回false
	 * @param id
	 * @param n
	 * @return
	 */
	public boolean dateCompare(long startTime,long compareTime){
		boolean same = false;
		String date1 = DateUtil.formatDate(new Date(startTime), "yyyyMMdd");
		String date2 = DateUtil.formatDate(new Date(compareTime), "yyyyMMdd");
		if(date1.equals(date2)){
			same = true;
		}
		return same;
	}
	public static String formatId(int id,int n){
		String s = "" + id;
		if(s.length() >= n) return s;
		while(s.length() < n){
			s = "0" + s;
		}
		return s;
	}

	public File getSaveDir() {
		return saveDir;
	}

	public void setSaveDir(File saveDir) {
		this.saveDir = saveDir;
	}
	
	public class DownCitySubThread implements Runnable {
		public ThreadPoolExecutor executor = new ThreadPoolExecutor(32, 32,
				8, TimeUnit.SECONDS,
				new ArrayBlockingQueue(500000),
				new ThreadPoolExecutor.AbortPolicy());
				
		public void run() {
			while(true) {
				try {
					Thread.sleep(100);
					DownCity dcs[] = downCityMap.values().toArray(new DownCity[0]);
					for(int i = 0 ; i < dcs.length ; i++){
						Game game = dcs[i].getGame();
						if(game != null) {
							//GameSubThread subThread = game.getSubThread();
//							if(!subThread.isRunning()) {
//								subThread.setRunning(true);
//								executor.execute(subThread);
//							}
						}
					}
				} catch(Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Hashtable<String, DownCity> getDownCityMap() {
		return downCityMap;
	}
	
	public static final byte 元神模式_本尊 = 0;
	public static final byte 元神模式_元神 = 1;
	public String 检查小队进入副本的合法性(Player player, byte 元神模式, String downCityName){
		if(player == null){
			return Translate.玩家不能为空;
		}
		Team team = player.getTeam();
		if(team == null){
			return Translate.没有小队不能发起副本;
		}
		if(team.getCaptain() != player){
			return Translate.只能是队长才能发起副本;
		}
		List<Player> list = team.getMembers();
		if(list == null){
			return Translate.队伍必须大于3人才能发起副本;
		}
		if(list.size() < 3){
			return Translate.队伍必须大于3人才能发起副本;
		}
		StringBuffer sb = new StringBuffer();
		if(元神模式 == 元神模式_本尊){
			for(Player p : list){
				if(p.getLevel() <= 110){
					sb.append(p.getName()+",");
				}
			}
			if(sb.length() > 0){
				return sb.substring(0, sb.length() - 1) + Translate.本尊级别不足;
			}
		}else{
			for(Player p : list){
				if(p.getSoul(Soul.SOUL_TYPE_SOUL) != null){
					if(p.getSoul(Soul.SOUL_TYPE_SOUL).getGrade() <= 110){
						sb.append(p.getName()+Translate.元神级别不足+",");
					}
				}else{
					sb.append(p.getName()+Translate.没有开启元神+",");
				}
			}
			if(sb.length() > 0){
				return sb.substring(0, sb.length() - 1);
			}
		}
		
		if(元神模式 == 元神模式_本尊){
			for(Player p : list){
				if(!p.getCurrSoul().isMainSoul()){
					sb.append(p.getName()+",");
				}
			}
			if(sb.length() > 0){
				return sb.substring(0, sb.length() - 1) + Translate.不是本尊不能进入本尊模式副本;
			}
		}else{
			for(Player p : list){
				if(p.getCurrSoul().isMainSoul()){
					sb.append(p.getName()+",");
				}
			}
			if(sb.length() > 0){
				return sb.substring(0, sb.length() - 1) + Translate.不是元神不能进入元神模式副本;
			}
		}
		
		//是否有牌子
		String articleName = 通过副本英文名得到进入副本所需物品(downCityName);
		if(articleName != null && !articleName.trim().equals("")){
			for(Player p : list){
				if(p.getArticleEntityNum(articleName) <= 0){
					sb.append(p.getName()+",");
				}
			}
			if(sb.length() > 0){
				return sb.substring(0, sb.length() - 1) + Translate.translateString(Translate.没有物品, new String[][]{{Translate.STRING_1,articleName}});
			}
		}
		
		//今日进入次数
		int count = 通过副本英文名得到每天进入副本最大次数(downCityName);
		for(Player p : list){
			if(p.得到今天进入副本次数(downCityName) >= count){
				sb.append(p.getName()+",");
			}
		}
		if(sb.length() > 0){
			return sb.substring(0, sb.length() - 1) + Translate.translateString(Translate.今日进入次数已经达到次, new String[][]{{Translate.COUNT_1,count+""}});
		}
		return null;
	}
	
	public void 队长申请打开创建副本房间的菜单(Player player, byte 元神模式, String downCityName){
		String result = 检查小队进入副本的合法性(player, 元神模式, downCityName);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, result);
			player.addMessageToRightBag(hreq);
			if(logger.isInfoEnabled()){
				logger.info("[队长申请打开创建副本房间的菜单] [失败] ["+player.getLogString()+"] [元神模式:"+元神模式+"] [perpareEnterDownCityName:"+downCityName+"] ["+result+"]");
			}
			return;
		}
		Team team = player.getTeam();
		if(team != null){
			team.perpareEnterDownCityName = downCityName;
			team.元神模式 = 元神模式;
			List<Player> list = team.getMembers();
			if(list != null){
				long[] ids = new long[list.size()];
				byte[] status = new byte[list.size()];
				for(int i = 0; i < list.size(); i++){
					ids[i] = list.get(i).getId();
					status[i] = 0;
					list.get(i).prepareEnterDownCityStatus = 0;
				}
				logger.warn("[队长申请打开创建副本房间的菜单] [成功] ["+player.getLogString()+"] [元神模式:"+元神模式+"] [perpareEnterDownCityName:"+downCityName+"]");
				DOWNCITY_PREPARE_WINDOW_REQ dpwr = new DOWNCITY_PREPARE_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), 通过副本英文名得到副本中文名(downCityName), ids, status, Translate.当全部队员都接受后方可由队长带领进入副本请您耐心等待);
				for(int i = 0; i < list.size(); i++){
					Player p = list.get(i);
					p.addMessageToRightBag(dpwr);
				}
			}
		}
	}
	
	/**
	 * 如果队员是准备状态，那么当他因为窗口关闭而发送"不去"状态的时候不予处理
	 * @param player
	 * @param status
	 */
	public void 队员修改队列状态(Player player, byte status){
		Team team = player.getTeam();
		if(team != null && team.perpareEnterDownCityName != null && !team.perpareEnterDownCityName.trim().equals("")){
			if(player.prepareEnterDownCityStatus != status){
				if(status == PREPARE_STATUS_不去){
					if(player.prepareEnterDownCityStatus != PREPARE_STATUS_准备){
						player.prepareEnterDownCityStatus = status;
						
						//所有人的状态更改
						List<Player> list = team.getMembers();
						if(list != null){
							for(int i = 0; i < list.size(); i++){
								if(list.get(i).prepareEnterDownCityStatus != PREPARE_STATUS_不去){
									list.get(i).prepareEnterDownCityStatus = PREPARE_STATUS_没有准备;
								}
							}
						}
					}else{
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.需要从准备状态退出才可以关闭窗口);
						player.addMessageToRightBag(hreq);
					}
				}else{
					player.prepareEnterDownCityStatus = status;
				}
				logger.warn("[修改队列状态] [成功] ["+player.getLogString()+"] [playerStatus:"+player.prepareEnterDownCityStatus+"] [status:"+status+"] [perpareEnterDownCityName:"+(team != null ? team.perpareEnterDownCityName : "")+"]");
				List<Player> list = team.getMembers();
				if(list != null){
					long[] ids = new long[list.size()];
					byte[] statuss = new byte[list.size()];
					for(int i = 0; i < list.size(); i++){
						ids[i] = list.get(i).getId();
						statuss[i] = list.get(i).prepareEnterDownCityStatus;
					}
					DOWNCITY_PREPARE_WINDOW_REQ dpwr = new DOWNCITY_PREPARE_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), 通过副本英文名得到副本中文名(team.perpareEnterDownCityName), ids, statuss, Translate.当全部队员都接受后方可由队长带领进入副本请您耐心等待);
					for(int i = 0; i < list.size(); i++){
						Player p = list.get(i);
						if(p.prepareEnterDownCityStatus != PREPARE_STATUS_不去){
							p.addMessageToRightBag(dpwr);
						}
					}
				}
			}
		} else{
			logger.warn("[修改队列状态] [失败] ["+player.getLogString()+"] [status:"+status+"] [perpareEnterDownCityName:"+(team != null ? team.perpareEnterDownCityName : "")+"]");
		}
	}
	
	public void 玩家在开启副本入口界面时进入队伍(Player player, Team team){
		try{
			player.prepareEnterDownCityStatus = -1;
			if(team != null){
				if(team.getCaptain() != player && team.getCaptain().prepareEnterDownCityStatus != -1 && team.getCaptain().prepareEnterDownCityStatus != PREPARE_STATUS_不去){
					队员修改队列状态(player, (byte)0);
				} 
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void 玩家在开启副本入口界面时退出队伍(Player player, Team team){
		try{
			player.prepareEnterDownCityStatus = -1;
			if(team != null && team.getCaptain() != null && team.getCaptain().prepareEnterDownCityStatus != -1){
				List<Player> list = team.getMembers();
				if(list != null){
					long[] ids = new long[list.size()];
					byte[] statuss = new byte[list.size()];
					for(int i = 0; i < list.size(); i++){
						ids[i] = list.get(i).getId();
						statuss[i] = list.get(i).prepareEnterDownCityStatus;
					}
					DOWNCITY_PREPARE_WINDOW_REQ dpwr = new DOWNCITY_PREPARE_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), 通过副本英文名得到副本中文名(team.perpareEnterDownCityName), ids, statuss, Translate.当全部队员都接受后方可由队长带领进入副本请您耐心等待);
					for(int i = 0; i < list.size(); i++){
						Player p = list.get(i);
						if(p.prepareEnterDownCityStatus != PREPARE_STATUS_不去 && team.getCaptain().prepareEnterDownCityStatus != -1){
							p.addMessageToRightBag(dpwr);
						}
					}
				}
			} 
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public void 创建并进入副本(Player player){
		if(player == null){
			return;
		}
		Team team = player.getTeam();
		if(team == null){
			return;
		}
		String downCityName = team.perpareEnterDownCityName;
		String result = 检查小队进入副本的合法性(player, team.元神模式, downCityName);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		try{
			if(player.isIsUpOrDown() && player.isFlying()){
				player.downFromHorse();
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("[创建并进入副本] [异常]",e);
		}
		DownCityInfo dci = getDownCityInfoByMapName(downCityName);
		if(dci != null){
			DownCity dc = createNewDownCity(player, dci);
			if(dc != null){
				team.setDownCity(dc);
				logger.warn("[创建并进入副本] [成功] ["+player.getLogString()+"] [downCityName:"+downCityName+"] [dc:"+dc.id+"]");
				Player[] ps = player.getTeamMembers();
				int[] point = 通过副本英文名得到副本进入坐标(downCityName);
				for(Player p : ps){
					p.prepareEnterDownCityStatus = -1;
					p.addDownCityProgress(dc);
					Game game = p.getCurrentGame();
					if(game != null){
						p.downFromHorse();
						game.transferGame(p, new TransportData(0, 0, 0, 0, downCityName, point[0], point[1]));
					}
				}
			}
		}else{
			logger.warn("[创建并进入副本] [失败] ["+player.getLogString()+"] [downCityName:"+downCityName+"]");
		}
	}
	
	
	public boolean isInDownCityForLimitFly(String gameDisplayName){
		for(String name : limitFlyName){
			if(name.equals(gameDisplayName)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isDownCityByName(String gameDisplayName){
		if(dciList != null){
			for(DownCityInfo dci : dciList){
				if(dci.getMapName().equals(gameDisplayName)){
					return true;
				}
			}
		}
		return false;
	}
	
	public void 进入队伍副本(Player player){
		String result = 进入队伍副本合法性判断(player);
		if(result != null){
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, result);
			player.addMessageToRightBag(hreq);
			logger.warn("[进入队伍副本] [失败] ["+player.getLogString()+"] [result:"+result+"]");
			return;
		}
		Team team = player.getTeam();
		if(team != null && team.getDownCity() != null){
			player.addDownCityProgress(team.getDownCity());
			Game game = player.getCurrentGame();
			int[] point = 通过副本英文名得到副本进入坐标(team.perpareEnterDownCityName);
			if(game != null){
				game.transferGame(player, new TransportData(0, 0, 0, 0, team.perpareEnterDownCityName, point[0], point[1]));
				logger.warn("[进入队伍副本] [成功] ["+player.getLogString()+"] [perpareEnterDownCityName:"+team.perpareEnterDownCityName+"]");
			}
		}else{
			logger.warn("[进入队伍副本] [失败] ["+player.getLogString()+"] [没有队伍或队伍副本为空]");
		}
	}
	
	public String 进入队伍副本合法性判断(Player player){
		if(player == null){
			return Translate.玩家不能为空;
		}
		Team team = player.getTeam();
		if(team == null){
			return Translate.没有小队不能进入小队副本;
		}

		if(team.perpareEnterDownCityName == null || team.perpareEnterDownCityName.trim().equals("")){
			return Translate.小队没有副本;
		}
		
		if(team.getDownCity() == null){
			return Translate.小队没有副本;
		}
		byte 元神模式 = team.元神模式;

		if(元神模式 == 元神模式_本尊){
			if(player.getLevel() <= 110){
				return Translate.本尊级别不足;
			}
		}else{
			if(player.getSoul(Soul.SOUL_TYPE_SOUL) != null){
				if(player.getSoul(Soul.SOUL_TYPE_SOUL).getGrade() <= 110){
					return Translate.元神级别不足;
				}
			}else{
				return Translate.没有开启元神;
			}
		}
		
		if(元神模式 == 元神模式_本尊){
			if(!player.getCurrSoul().isMainSoul()){
				return Translate.不是本尊不能进入本尊模式副本;
			}
		}else{
			if(player.getCurrSoul().isMainSoul()){
				return Translate.不是元神不能进入元神模式副本;
			}
		}
		
		//是否有牌子
		StringBuffer sb = new StringBuffer();
		String downCityName = team.perpareEnterDownCityName;
		String articleName = 通过副本英文名得到进入副本所需物品(downCityName);
		if(articleName != null && !articleName.trim().equals("")){
			if(player.getArticleEntityNum(articleName) <= 0){
				sb.append(Translate.translateString(Translate.没有物品, new String[][]{{Translate.STRING_1,articleName}}));
			}
		}
		if(sb.length() != 0){
			return sb.toString();
		}
		
		//今日进入次数
		int count = 通过副本英文名得到每天进入副本最大次数(downCityName);
		if(player.得到今天进入副本次数(downCityName) >= count){
			sb.append(Translate.translateString(Translate.今日进入次数已经达到次, new String[][]{{Translate.COUNT_1,count+""}}));
		}
		if(sb.length() != 0){
			return sb.toString();
		}
		return null;
	}
	
	
}
