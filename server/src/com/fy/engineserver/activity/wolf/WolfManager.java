package com.fy.engineserver.activity.wolf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.wolf.config.ActivityConfig;
import com.fy.engineserver.activity.wolf.runtask.NoticeTask;
import com.fy.engineserver.activity.wolf.runtask.WolfGameThread;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_XuanYun;
import com.fy.engineserver.datasource.buff.BuffTemplate_ZengShu;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.wolf.Option_Wolf_SignUp_Sure;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.message.NOTICE_DISASTER_COUNTDOWN_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.message.SET_POSITION_REQ;
import com.fy.engineserver.message.WOLF_FIGHT_QUERY_RES;
import com.fy.engineserver.message.WOLF_SIGN_UP_SURE_RES;
import com.fy.engineserver.message.WOLF_STATE_NOTICE_RES;
import com.fy.engineserver.message.WOLF_USE_SKILL_REQ;
import com.fy.engineserver.message.WOLF_USE_SKILL_RES;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.sprite.CountdownAgent;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.server.TestServerConfigManager;

public class WolfManager {

	private static WolfManager self;
	
	public static Logger logger = ActivitySubSystem.logger;
	
	private int limitSignLevel = 110;
	
	public static boolean START_RUN = true;
	public static long SLEEP_TIME = 1000;
	public static long GAME_SLEEP_TIME = 200;
	
	//当前正在生效的配置
	public ActivityConfig currConfig; 
	
	public static List<Long> signList = new ArrayList<Long>();
	
	public static int ONE_DAY_MAX_JOIN_NUMS = 3;
	
	//不用翻译
	public String boxName = "天阙大宝箱";
	
	//副本集合
	public List<WolfGame> allGames = new ArrayList<WolfGame>();
	
	private int THREAD_NUM = 3;
	private WolfGameThread threads[] = new WolfGameThread[THREAD_NUM];
	
	private int maxTeamNums = 5; 
	private int maxJoinNums = 500;
	public String mapName = "tianquehuanjing";
	
	public boolean testLog = false;
	
	private String wolfAvata = "/part/chongwu_shihunxuelang04.xtl";
	private String sheepAvata = "/part/xinchongwu_xiaoyang.xtl";
	
	public Map<Long,JoinNumData> joinNums = new HashMap<Long, JoinNumData>();
	
	public long waitTime = 20 * 1000;
	
	public static final int 利齿 = 1;
	public static final int 撕咬 = 2;
	public static final int 召唤牧场大叔 = 3;
	public static final int 极速 = 4;
	
	public static String eatSheepLiZhi = "活动特效/大灰狼吞羊";
	public static String eatGrassLiZhi = "任务光效/任务完成";
	
	private String skillMess[] = {"","利齿","撕咬","召唤牧场大叔","极速"};
	
	public static long boxRefrshTimeLength = 40 * 1000L;
	public static long grassRefrshTimeLength = 20 * 1000L;
	
	public int [] wolfPoints = {2230,644};
	public int [][] sheepPoints = {{3806,989},{4143,2226},{3990,3665},{2242,4066},{550,3374},{370,2200},{780,1085}};
	/**
	 * 小羊npc1号店至8号点刷新点坐标
	 */
	public int [][][] npcPoints = {{{2124,653},{2373,690},{2381,814},{2248,755},{2100,798},{2236,841},{2361,909},{2344,991},{2248,960},{2128,927},{2122,1046},{2264,1079},{2378,1119},{2250,1208},{2387,1242},{2110,1189},{2144,1300},{2284,1343},{2422,1345},{2409,1454},{2124,1420}},
								 {{3130,1455},{3222,1567},{3393,1516},{3318,1437},{3257,1332},{3409,1347},{3515,1392},{3363,1193},{3555,1244},{3660,1344},{3505,1129},{3614,1056},{3765,1205},{3765,1066},{3884,1080},{3722,898}},
								 {{4198,2146},{4209,2359},{4110,2262},{3937,2331},{3957,2151},{3787,2089},{3789,2240},{3779,2429},{3585,2320},{3599,2169},{3574,2065},{3343,2045},{3414,2194},{3337,2364}},
								 {{4001,3666},{3821,3622},{4024,3560},{3951,3430},{3795,3465},{3657,3518},{3692,3367},{3827,3286},{3647,3226},{3471,3339},{3426,3210},{3587,3109},{3429,3041},{3253,3146},{3474,2931},{3222,3006},{3283,2879}},
								 {{2321,3984},{2151,3986},{2346,3871},{2099,3835},{2257,3776},{2392,3722},{2178,3691},{2330,3564},{2129,3533},{2415,3414},{2250,3406},{2434,3308},{2308,3238},{2123,3281},{2158,3125}},
								 {{458,3399},{705,3378},{542,3229},{740,3243},{721,3140},{924,3249},{889,3065},{1052,3096},{1227,3120},{1328,3018},{1115,2948},{1265,2865}},
								 {{1153,2396},{1175,2244},{1192,2101},{1052,2059},{965,2282},{838,2327},{887,2136},{762,2081},{698,2240},{577,2329},{478,2175},{529,2040},{386,2323},{238,2267},{296,2058}},
								 {{788,1002},{630,1061},{970,1104},{833,1163},{685,1243},{902,1351},{1071,1261},{999,1466},{1164,1439},{1246,1353},{1388,1467},{1293,1596},{1090,1589},{1188,1702},{1339,1849},{1420,1681},{1576,1623}},
								 {{2626,2754},{2325,2890},{1984,2829},{2254,2582},{2776,2506},{2504,2393},{1924,2484},{1930,2285},{2220,2352},{2331,2201},{2605,2225},{2855,2080},{2692,1920},{2410,2006},{2462,1763},{2078,2091},{2151,1860},{1629,2626},{1616,2332},{1705,2098},{1832,1859}},
								 {{2042,2602},{1735,2412},{2500,2539},{2251,2839},{1933,2877},{2682,2680},{2688,2299},{2014,2107}}};
	
	/**
	 * 经验草坐标
	 */
	public static int [][] grassPoints = {{2626,2754},{2325,2890},{1984,2829},{2254,2582},{2776,2506},{2504,2393},{1924,2484},{1930,2285},{2220,2352},{2331,2201},{2605,2225},{2855,2080},{2692,1920},{2410,2006},{2462,1763},{2078,2091},{2151,1860},{1629,2626},{1616,2332},
											{1705,2098},{1832,1859},{2255,625},{2124,653},{2373,690},{2381,814},{2248,755},{2100,798},{2236,841},{2361,909},{2344,991},{2248,960},{2128,927},{2122,1046},{2264,1079},{2378,1119},{2250,1208},{2387,1242},{2110,1189},{2144,1300},
											{2284,1343},{2422,1345},{2409,1454},{2124,1420},{3130,1455},{3222,1567},{3393,1516},{3318,1437},{3257,1332},{3409,1347},{3515,1392},{3363,1193},{3555,1244},{3660,1344},{3505,1129},{3614,1056},{3765,1205},{3765,1066},{3884,1080},
											{3722,898},{4198,2146},{4209,2359},{4110,2262},{3937,2331},{3957,2151},{3787,2089},{3789,2240},{3779,2429},{3585,2320},{3599,2169},{3574,2065},{3343,2045},{3414,2194},{3337,2364},{4001,3666},{3821,3622},{4024,3560},{3951,3430},
											{3795,3465},{3657,3518},{3692,3367},{3827,3286},{3647,3226},{3471,3339},{3426,3210},{3587,3109},{3429,3041},{3253,3146},{3474,2931},{3222,3006},{3283,2879},{2321,3984},{2151,3986},{2346,3871},{2099,3835},{2257,3776},{2392,3722},
											{2178,3691},{2330,3564},{2129,3533},{2415,3414},{2250,3406},{2434,3308},{2308,3238},{2123,3281},{2158,3125},{458,3399},{705,3378},{542,3229},{740,3243},{721,3140},{924,3249},{889,3065},{1052,3096},{1227,3120},{1328,3018},{1115,2948},
											{1265,2865},{1153,2396},{1175,2244},{1192,2101},{1052,2059},{965,2282},{838,2327},{887,2136},{762,2081},{698,2240},{577,2329},{478,2175},{529,2040},{386,2323},{238,2267},{296,2058},{788,1002},{630,1061},{970,1104},{833,1163},
											{685,1243},{902,1351},{1071,1261},{999,1466},{1164,1439},{1246,1353},{1388,1467},{1293,1596},{1090,1589},{1188,1702},{1339,1849},{1420,1681},{1576,1623}};
	
	public static int [][] boxPoints = {{2230,644},{3806,989},{4143,2226},{3990,3665},{2242,4066},{550,3374},{370,2200},{780,1085}};
		
	public static long fightLastTime = 3 * 60 * 1000;
	
	Random random = new Random();
	
	private String fileName;
	
	public static final int 凡级青草 = 0;
	public static final int 灵级青草 = 1;
	public static final int 仙级青草 = 2;
	public static final int 狼吃羊经验 = 3;
	public static final int 结算经验 = 4;
	
	public Map<Integer,Map<Integer,Long>> expDatas = new HashMap<Integer, Map<Integer,Long>>();
	public Map<Integer,Long> wolfExpDatas = new HashMap<Integer, Long>();
	/**
	 * 结算经验
	 * 任务完成度1：失败2：成功3：封神
	 * 等级
	 * 经验
	 */
	public Map<Integer,Map<Integer,Long>> rewardExpDatas = new HashMap<Integer, Map<Integer,Long>>();
	
	public static WolfManager getInstance(){
		if(self == null){
			self = new WolfManager();
		}
		return self;
	}
	
	public void init() throws Exception{
		if(true){
			return;
		}
		self = this;
		initRunTasks();
		initGrassExpData();
		if(TestServerConfigManager.isTestServer()){
			testLog = true;
		}
		ServiceStartRecord.startLog(this);
	}
	
	public void initGrassExpData() throws Exception{
		File f = new File(fileName);
		if(!f.exists()){
			throw new Exception("经验草文件不存在");
		}
		
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		
		HSSFSheet sheet = workbook.getSheetAt(凡级青草);		
		int rows = sheet.getPhysicalNumberOfRows();
		
		for(int i=1;i<rows;i++){
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					int level = StringTool.getCellValue(row.getCell(index++), Integer.class);
					long exps = StringTool.getCellValue(row.getCell(index++), Long.class);
					Map<Integer,Long> map = expDatas.get(凡级青草);
					if(map == null){
						map = new HashMap<Integer, Long>();
					}
					map.put(level, exps);
					expDatas.put(凡级青草, map);
				}catch(Exception e){
					throw new Exception("[加载经验草文件] [页："+凡级青草+"] [行："+i+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
			}else{
				throw new Exception("[加载经验草文件] [失败] [原因：数据为空!]");
			}
		}
		
		sheet = workbook.getSheetAt(灵级青草);		
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1;i<rows;i++){
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					int level = StringTool.getCellValue(row.getCell(index++), Integer.class);
					long exps = StringTool.getCellValue(row.getCell(index++), Long.class);
					Map<Integer,Long> map = expDatas.get(灵级青草);
					if(map == null){
						map = new HashMap<Integer, Long>();
					}
					map.put(level, exps);
					expDatas.put(灵级青草, map);
				}catch(Exception e){
					throw new Exception("[加载经验草文件] [页："+灵级青草+"] [行："+i+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
			}else{
				throw new Exception("[加载经验草文件] [失败] [原因：数据为空!]");
			}
		}
		
		sheet = workbook.getSheetAt(仙级青草);		
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1;i<rows;i++){
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					int level = StringTool.getCellValue(row.getCell(index++), Integer.class);
					long exps = StringTool.getCellValue(row.getCell(index++), Long.class);
					Map<Integer,Long> map = expDatas.get(仙级青草);
					if(map == null){
						map = new HashMap<Integer, Long>();
					}
					map.put(level, exps);
					expDatas.put(仙级青草, map);
				}catch(Exception e){
					throw new Exception("[加载经验草文件] [页："+仙级青草+"] [行："+i+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
			}else{
				throw new Exception("[加载经验草文件] [失败] [原因：数据为空!]");
			}
		}
		
		sheet = workbook.getSheetAt(狼吃羊经验);		
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1;i<rows;i++){
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				try{
					int level = StringTool.getCellValue(row.getCell(index++), Integer.class);
					long exps = StringTool.getCellValue(row.getCell(index++), Long.class);
					wolfExpDatas.put(level, exps);
				}catch(Exception e){
					throw new Exception("[加载狼吃羊经验文件] [页："+狼吃羊经验+"] [行："+i+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
			}else{
				throw new Exception("[加载狼吃羊经验文件] [失败] [原因：数据为空!]");
			}
		}
		
		sheet = workbook.getSheetAt(结算经验);		
		rows = sheet.getPhysicalNumberOfRows();
		for(int i=1;i<rows;i++){
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				int result = 0;
				int level = 0;
				long exps = 0;
				try{
					result = StringTool.getCellValue(row.getCell(index++), Integer.class);
				}catch(Exception e){
					e.printStackTrace();
					throw new Exception("[加载结算经验文件] [result] [页："+结算经验+"] [行："+i+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				try{
					level = StringTool.getCellValue(row.getCell(index++), Integer.class);
				}catch(Exception e){
					e.printStackTrace();
					throw new Exception("[加载结算经验文件] [level] [页："+结算经验+"] [行："+i+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				try{
					HSSFCell cell = row.getCell(index++);
					if(cell.getCellType() == 2){
						cell.setCellType(0);
					}
					exps = StringTool.getCellValue(cell, Long.class);
				}catch(Exception e){
					throw new Exception("[加载结算经验文件] [exps] [页："+结算经验+"] [行："+i+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				Map<Integer,Long> map = rewardExpDatas.get(result);
				if(map == null){
					map = new HashMap<Integer, Long>();
				}
				map.put(level, exps);
				rewardExpDatas.put(result, map);
				if(logger.isWarnEnabled()){
					logger.warn("[加载经验草数据] [结果:"+result+"] [等级:"+level+"] [经验"+exps+"]");
				}
			}else{
				throw new Exception("[加载结算经验文件] [失败] [原因：数据为空!]");
			}
		}
		if(logger.isWarnEnabled()){
			logger.warn("[加载经验草数据] [成功] [经验草数据:"+expDatas.size()+"] [狼吃羊经验数据:"+wolfExpDatas.size()+"] [结算经验:"+rewardExpDatas.size()+"]");
		}
	}
	
	public void initRunTasks(){
		new Thread(new NoticeTask(),"小羊快跑活动通知").start();
		for(int i=0;i<THREAD_NUM;i++){
			threads[i] = new WolfGameThread();
			threads[i].tName = "小羊快跑游戏心跳"+(i+1);
			new Thread(threads[i],"小羊快跑游戏心跳"+(i+1)).start();
		}
	}
	
	public String signUp(Player p,boolean isSure){
		if(p == null){
			return Translate.text_marriage_058;
		}
		if(p.getLevel() < limitSignLevel){
			return Translate.translateString(Translate.等级太低不能报名, new String[][] { { Translate.COUNT_1, String.valueOf(limitSignLevel) }});
		}
		if(p.getTeamMark() != Player.TEAM_MARK_NONE){
			return Translate.队伍中不能报名;
		}
		if(signList.contains(p.getId())){
			return Translate.已经报名;
		}
		if(signList.size() >= maxJoinNums){
			return Translate.报名人数已达上限;
		}
		if(!isStart()){
			return Translate.活动还没开启;
		}
		JoinNumData data = joinNums.get(p.getId());
		if(data == null){
			data = new JoinNumData();
		}
		if(data.getJoinNums() >= ONE_DAY_MAX_JOIN_NUMS){
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [报名失败:今天参加次数达上限] [参与次数:"+data.getJoinNums()+"] [人数:"+signList.size()+"] ["+p.getLogString()+"]");
			}
			return Translate.您的参与次数不足;
		}
		WolfGame wg = getWolfGame(p);
		if(wg != null){
			return Translate.匹配成功不能报名;
		}
		
		if(!isSure){
			WindowManager wm = WindowManager.getInstance();
			MenuWindow mw = wm.createTempMenuWindow(600);
			mw.setDescriptionInUUB(Translate.法宝报名弹框);
			Option_Wolf_SignUp_Sure option1 = new Option_Wolf_SignUp_Sure();
			option1.setText(MinigameConstant.CONFIRM);
			Option[] options = new Option[] {option1};
			mw.setOptions(options);
			CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
			p.addMessageToRightBag(creq);
			return null;
		}
		
//		if(p.getActiveMagicWeaponId() > 0){
//			return Translate.携带法宝不能报名;
//		}
		signList.add(p.getId());
		joinNums.put(p.getId(), data);
		if(logger.isWarnEnabled()){
			logger.warn("[小羊快跑] [报名成功] [参与次数:"+data.getJoinNums()+"] [人数:"+signList.size()+"] ["+p.getLogString()+"]");
		}
		return Translate.报名成功;
	}
	
	public boolean isSameDay(long time1, long time2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str1 = sdf.format(time1);
		String str2 = sdf.format(time2);
		return str1.equals(str2);
	}
	
	public void cancelSignUp(Player p,boolean notice){
		if(p == null){
			return;
		}
		if(signList.contains(p.getId())){
			signList.remove(p.getId());
		}
		if(notice){
			p.sendError(Translate.取消报名成功);
		}
		WOLF_STATE_NOTICE_RES res2 = new WOLF_STATE_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 2);
		p.addMessageToRightBag(res2);
		WOLF_SIGN_UP_SURE_RES res = new WOLF_SIGN_UP_SURE_RES(GameMessageFactory.nextSequnceNum(), false);
		p.addMessageToRightBag(res);
	}
	
	/**
	 * 玩家上线
	 */
	public void enterGame(Player p){
		WolfGame wg = getWolfGame(p);
		if(wg != null){
			playerLeave(p,"玩家上线");
		}
		cancelSignUp(p, false);
		if(p.isHold()){
			p.setHold(false);
		}
		if(logger.isWarnEnabled()){
			logger.warn("[小羊快跑] [处理玩家上线] [是否定身:"+p.isHold()+"] [wg:"+(wg==null?"null":wg.state)+"] ["+p.getLogString()+"]");
		}
	}
	
	public void initConfig(){
		for(ActivityConfig c : ActivityManager.getInstance().configs){
			if(c != null && c.platFit()){
				currConfig = c;
				break;
			}
		}
		if(currConfig != null && !currConfig.isStart()){
			for(long id : signList){
				if(PlayerManager.getInstance().isOnline(id)){
					try {
						Player p = PlayerManager.getInstance().getPlayer(id);
						WOLF_STATE_NOTICE_RES res2 = new WOLF_STATE_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 2);
						p.addMessageToRightBag(res2);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			joinNums.clear();
			signList.clear();
			List<Player> rmps = new ArrayList<Player>();
			for(long id : WolfManager.signList){
				if(PlayerManager.getInstance().isOnline(id)){
					try {
						Player p = PlayerManager.getInstance().getPlayer(id);
						rmps.add(p);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(rmps.size() > 0){
				for(Player rp : rmps){
					WolfManager.getInstance().cancelSignUp(rp,false);
				}
			}
		}
	}
	
	public boolean isStart(){
		if(currConfig != null && currConfig.isStart()){
			return true;
		}
		return false;
	}
	
	public void matchTeam(){
		if(signList == null || signList.size() < maxTeamNums){
			return;
		}
		if(currConfig == null || currConfig.isStart() == false){
			if(signList.size() > 0){
				if(logger.isWarnEnabled()){
					logger.warn("[小羊快跑] [清空报名人数] [人数:"+signList.size()+"]");
				}
				signList.clear();
				return;
			}
		}
		List<Long> matchList = new ArrayList<Long>();
		boolean isSucc = false;
		for(long id : signList){
			if(PlayerManager.getInstance().isOnline(id)){
				matchList.add(id);
			}
			if(matchList.size() == maxTeamNums){
				Game game = createGame(mapName);
				if(game != null){
					WolfGame wGame = new WolfGame(game, matchList);
					addGameTask(wGame);
					isSucc = true;
					try {
						Player p = PlayerManager.getInstance().getPlayer(id);
						if(p != null){
							p.sendError(Translate.匹配成功);
							if(logger.isWarnEnabled()){
								logger.warn("[小羊快跑] [匹配成功] [报名人数:"+signList.size()+"] ["+p.getLogString()+"]");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		if(isSucc){
			signList.removeAll(matchList);
		}
	}
	
	//比赛开始刷小羊，只刷一次
	public void refreshMonster(Game game){
		for(int i=0;i<npcPoints.length;i++){
			int [][] points = getRandomPoints(npcPoints[i], 6);
			for(int j=0;j<points.length;j++){
				int [] ps = points[j];
				Sprite sprite = null;
				if(ps != null && ps.length == 2){
					sprite = MemoryMonsterManager.getMonsterManager().getMonster(1700125);
					if(sprite == null){
						sprite = MemoryMonsterManager.getMonsterManager().createMonster(1700125);
					}
					sprite.setAvata(new String[]{sheepAvata});
					sprite.setX(ps[0]);
					sprite.setY(ps[1]);
					sprite.setBornPoint(new Point(sprite.getX(),sprite.getY()));
					game.addSprite(sprite);
				}
				if(testLog){
					logger.warn("[小羊快跑] [刷新小羊] ["+(i+1)+"号点] [spriteID:"+(sprite==null?"null":sprite.getId())+"] [怪物数量:"+points.length+"] [坐标:"+(ps!=null?Arrays.toString(ps):"null")+"]");
				}
			}
		}
	}
	
	public void noticeEndTime(Player p,long time){
		int overTime = (int)((time)/1000);
		if(overTime > 0){
			NOTICE_DISASTER_COUNTDOWN_REQ resp = new NOTICE_DISASTER_COUNTDOWN_REQ();
			resp.setCountdownType(CountdownAgent.COUNT_TYPE_WOLF);
			resp.setLeftTime(overTime);
			resp.setDescription(Translate.副本倒计时);
			p.addMessageToRightBag(resp);
		}
		NOTICE_CLIENT_COUNTDOWN_REQ resp = new NOTICE_CLIENT_COUNTDOWN_REQ();
		resp.setCountdownType(CountdownAgent.COUNT_TYPE_DUJIE);
		resp.setLeftTime(0);
		resp.setDescription(Translate.天劫拉人倒计时);
		p.addMessageToRightBag(resp);
		if(logger.isWarnEnabled()){
			logger.warn("[小羊快跑] [通知副本倒计时] [hold:"+p.isHold()+"] [时间:"+overTime+"] ["+p.getLogString()+"]");
		}
	}
	
	/**
	 * 随机获得规定长度集合数据
	 */
	public int[][] getRandomPoints(int[][] points,int length){
		int[][] newPoints = new int[length][];
		int[][] bs = Arrays.copyOf(points, points.length);
		for(int i=0;i<newPoints.length;i++){
			int index = random.nextInt(bs.length);
			int [][] newbs = new int[bs.length-1][];
			System.arraycopy(bs, 0, newbs, 0, index);
			System.arraycopy(bs,index+1,newbs,index,newbs.length - index);
			newPoints[i] = bs[index];
			bs = newbs;
		}
		return newPoints;
	}
	
	public void noticeEnter(){
		if(allGames == null || allGames.size() == 0){
			return;
		}
		for(WolfGame game : allGames){
			if(game == null || !game.isEffect()){
				continue;
			}
			//进入时间30倒计时结束
			if(game.state == ActivityState.等待进入){
				game.state = ActivityState.准备战斗;
				for(long id : game.joinList){
					if(id <= 0 || !PlayerManager.getInstance().isOnline(id)){
						continue;
					}
					try {
						Player p = PlayerManager.getInstance().getPlayer(id);
						if(!game.notice2List.contains(id)){
							playerEnter(p);
						}
						WOLF_STATE_NOTICE_RES res = new WOLF_STATE_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 2);
						p.addMessageToRightBag(res);
						if(logger.isWarnEnabled()){
							logger.warn("[小羊快跑] [通知玩家进入副本] [系统拉入玩家进入副本通知玩家胜负规则] ["+p.getLogString()+"]");
						}
					} catch (Exception e) {
						e.printStackTrace();
						if(logger.isWarnEnabled()){
							logger.warn("[小羊快跑] [通知玩家进入副本] [异常] [{}] [信息数:{}] [{}]", new Object[]{id,game.bornInfos.size(),e});
						}
					}
				}
			}
		}
	}
	
	public void playerLeave(Player p,String reason){
		p.setHold(false);
		JoinNumData data = joinNums.get(p.getId());
		if(data != null && signList.contains(p.getId())){
			int joinNums = data.getJoinNums() - 1;
			if(joinNums < 0){
				joinNums = 0;
			}
			data.setJoinNums(joinNums);
		}
		if(signList.contains(p.getId())){
			signList.remove(p.getId());
		}
		WolfGame g = getWolfGame(p);
		//等待进入状态下线，视玩家自动放弃比赛
		if(g != null){
			if(g.inGame(p)){
				g.joinList.remove(p.getId());
				g.battleInfos.remove(p.getId());
				if(g.state != ActivityState.准备战斗 && g.state != ActivityState.等待进入){
					g.updateBattleInfo("玩家掉线",g.battleInfos);
				}
				//logger.warn("[小羊快跑] [处理玩家离线] [当前状态:"+g.state+"] [g.joinList:"+g.joinList.size()+"] [g.battleInfos:"+g.battleInfos.size()+"] ["+p.getLogString()+"] ");
			}
			p.setPkMode(Player.和平模式);
		}
		
		p.setSpeedA(150);
		ResourceManager.getInstance().getAvata(p);
		if(logger.isWarnEnabled()){
			logger.warn("[玩家下线] [signList:"+signList.size()+"] [副本人数:"+(g!=null?g.joinList.size():"0")+"] [状态:"+(g!=null?g.state:"null")+"] [reason:"+reason+"] ["+p.getLogString()+"]");
		}
	}
	
	public boolean playerEnter(Player p){
		WolfGame game = null;
		for(WolfGame g : allGames){
			if(g != null && g.inGame(p)){
				game = g;
				break;
			}
		}
		if(game != null){
			BornPoint point = game.bornInfos.get(p.getId());
			if(point == null){
				if(logger.isWarnEnabled()){
					logger.warn("[小羊快跑] [通知玩家进入副本] [失败:出生点信息不存在] [{}] [信息数:{}]", new Object[]{p.getId(),game.bornInfos.size()});
				}
				return false;
			}
			if (p.isIsUpOrDown()){
				p.downFromHorse(true);
			}
			if (p.getActivePetId() > 0 ){
				p.packupPet(true);
			}
			if(p.isDeath()){
				p.setHp(p.getMaxHP());
				p.setMp(p.getMaxMP());
				p.setState(Player.STATE_STAND);
				p.notifyRevived();
				PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.战场免费复活成功, p.getMaxHP(), p.getMaxMP());
				p.addMessageToRightBag(res);
			}
			if(point.getRole() == 1){
				p.setAvata(new String[]{wolfAvata});
			}else{
				p.setAvata(new String[]{sheepAvata});
				p.setSpeedA(130);
			}
			p.setAvataType(new byte[] { 0 });
			p.getCurrentGame().transferGame(p, new TransportData(0, 0, 0, 0, mapName, point.getX(), point.getY()));
			p.setHold(true);
			//发送胜负规则
			WOLF_FIGHT_QUERY_RES res2 = new WOLF_FIGHT_QUERY_RES(GameMessageFactory.nextSequnceNum(), Translate.规则1,Translate.失败描述,Translate.成功描述, Translate.封神描述, Translate.大灰狼,  Translate.小绵羊);
			p.addMessageToRightBag(res2);
			p.setPkMode(Player.全体模式);
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [进入副本] [hold:"+p.isHold()+"] [是否在坐骑:"+p.isIsUpOrDown()+"] [是否携带宠物:"+p.getActivePetId()+"] [speed:"+p.getSpeed()+"] [speedA:"+p.getSpeedA()+"] [speedC:"+p.getSpeedC()+"] [speedE:"+p.getSpeedE()+"] [point:"+point+"] ["+p.getLogString()+"]");
			}
			game.notice2List.add(p.getId());
			return true;
		}
		return false;
	}
	
	public WolfGame getWolfGame(Player player){
		for(WolfGame g : allGames){
			if(g != null && g.inGame(player)){
				return g;
			}
		}
		return null;
	}
	
	public Game getGame(Player player,String mName){
		Game game = null;
		for(WolfGame g : allGames){
			if(g != null && g.inGame(player)){
				game = g.game;
			}
		}
		if(logger.isWarnEnabled()){
			logger.warn("[玩家进入游戏] [mName:"+mName+"] [game:"+game+"] [allGames:"+allGames.size()+"] ["+player.getLogString()+"]");
		}
		return game;
	}
	
	public Game createGame(String mapname){
		GameManager gameManager = GameManager.getInstance();
		GameInfo gi = gameManager.getGameInfo(mapname);
		if(gi == null){
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [创建副本] [失败:对应的地图信息不存在] [{}]", new Object[]{mapname});
			}
			return null;
		}
		Game newGame = new Game(gameManager,gi);
		try {
			newGame.init();
			return newGame;
		} catch (Exception e) {
			e.printStackTrace();
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [创建副本] [失败:game初始化失败] [{}] [{}]", new Object[]{mapname,e});
			}
			return null;
		}
	}
	
	public boolean isWolfGame(Player player){
		Game game = player.getCurrentGame();
		if(game != null && game.gi != null){
			String mName = game.gi.name;
			if(mName != null && mName.equals(mapName)){
				return true;
			}
		}
		return false;
	}
	
	public void handle_WOLF_USE_SKILL_RES(WOLF_USE_SKILL_REQ req,Player player){
		long targetPlayerId = req.getTargetPlayerId();
		int skillId = req.getSkillId();
		if(skillId <= 0 || skillId > 4){
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [使用技能] [失败:技能id错误] [技能id:"+skillId+"] [目标id:"+targetPlayerId+"] ["+player.getLogString()+"]");
			}
			return;
		}
		WolfGame wg = getWolfGame(player);
		if(wg == null){
			if(logger.isWarnEnabled()){
				logger.warn("[小羊快跑] [使用技能失败:战斗场景不存在] ["+player.getLogString()+"]");
			}
			return;
		}
		if(wg.state != ActivityState.战斗开始){
			player.sendError(Translate.不能使用技能);
			return;
		}
		WolfBattleInfo info = wg.battleInfos.get(player.getId());
		if(info != null && info.isDead()){
			player.sendError(Translate.被识破不能使用技能);
			return;
		}
		Player targetP = null;
		Sprite targetS = null;
		String targetType = "无目标";
		try {
			targetP = PlayerManager.getInstance().getPlayer(targetPlayerId);
			if(targetP != null){
				targetType = "玩家";
			}
		} catch (Exception e) {
			try {
				targetS = MemoryMonsterManager.getMonsterManager().getMonster(targetPlayerId);
				if(targetS != null){
					targetType = "NPC";
				}
			} catch (Exception ex) {
			}
		}
		long eatSheepExp = 0;
		switch (skillId) {
		case 利齿:
			//近战抓取小羊技能，cd时间5秒
			if(targetType.equals("无目标")){
				player.sendError(Translate.无效的目标);
				return;
			}
			if(targetP != null){
				WolfBattleInfo binfo = wg.battleInfos.get(targetP.getId());
				if(binfo.isDead()){
					player.sendError(Translate.无效的目标);
					return;
				}
				wg.playerDead(player,targetP);
				eatSheepExp = WolfManager.getInstance().wolfExpDatas.get(player.getCurrSoul().getGrade());
				player.addExp(eatSheepExp, ExperienceManager.吃一只小羊);
				ResourceManager.getInstance().getAvata(targetP);
				player.sendError(Translate.translateString(Translate.识破一只小羊, new String[][] { { Translate.STRING_1, targetP.getName()},{ Translate.COUNT_1, String.valueOf(eatSheepExp) }}));
				targetP.sendError(Translate.被识破);
				for(long id : wg.joinList){
					if(player.getId() != id && targetP.getId() != id){
						if(PlayerManager.getInstance().isOnline(id)){
							try {
								Player sp = PlayerManager.getInstance().getPlayer(id);
								sp.sendError(Translate.translateString(Translate.通知识破一只小羊, new String[][] { { Translate.STRING_1, targetP.getName()}}));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				noticePlayerMove(player, targetP);
				piaoLiZi(wg.game.getPlayers(), eatSheepLiZhi,"吃羊",player);
			}else if(targetS != null){
				noticePlayerMove(player, targetS);
				piaoLiZi(wg.game.getPlayers(), eatSheepLiZhi,"吃羊npc",player);
				player.getCurrentGame().removeSprite(targetS);
			}
			break;
		case 撕咬:
			if(targetType.equals("无目标")){
				player.sendError(Translate.无效的目标);
				return;
			}
			//远程定身小羊技能（无伤害），cd时间20秒	
			BuffTemplate tt_ds = BuffTemplateManager.getInstance().getBuffTemplateById(1650);
			if (tt_ds instanceof BuffTemplate_XuanYun == false) {
				if(logger.isInfoEnabled()){
					logger.info("[小羊快跑] [使用技能] [失败:buff不正确] [技能id:"+skillId+"] [目标id:"+targetPlayerId+"] ["+player.getLogString()+"]");
				}
				return;
			}
			Buff buff_ds = tt_ds.createBuff(1);
			buff_ds.setStartTime(SystemTime.currentTimeMillis());
			buff_ds.setInvalidTime(buff_ds.getStartTime() + 3000);
			buff_ds.setCauser(player);
			if(targetP != null){
				targetP.placeBuff(buff_ds);
				//HunshiManager.instance.dealWithInfectSkill(player ,targetP, buff_ds);
			}else if(targetS != null){
				targetS.placeBuff(buff_ds);
				//HunshiManager.instance.dealWithInfectSkill(player ,targetS, buff_ds);
			}
			break;
		case 召唤牧场大叔:
			//对大灰狼造成眩晕3秒，cd时间20秒	
			if(!targetType.equals("玩家")){
				player.sendError(Translate.无效的目标);
				return;
			}
			if(targetP != null && !targetP.getName().equals(wg.wolfName)){
				player.sendError(Translate.无效的目标);
				return;
			}
			BuffTemplate tt = BuffTemplateManager.getInstance().getBuffTemplateById(1714);
			if (tt instanceof BuffTemplate_XuanYun == false) {
				if(logger.isInfoEnabled()){
					logger.info("[小羊快跑] [使用技能] [失败:buff不正确] [技能id:"+skillId+"] [目标id:"+targetPlayerId+"] ["+player.getLogString()+"]");
				}
				return;
			}
			Buff buff = tt.createBuff(1);
			buff.setStartTime(SystemTime.currentTimeMillis());
			buff.setInvalidTime(buff.getStartTime() + 3000);
			buff.setCauser(player);
			if(targetP != null){
				targetP.placeBuff(buff);
			}else if(targetS != null){
				targetS.placeBuff(buff);
			}
			break;
		case 极速:
			//3秒内增加小羊移动速度50点，cd时间20秒	
			BuffTemplate tt2 = BuffTemplateManager.getInstance().getBuffTemplateById(1758);
			if (!(tt2 instanceof BuffTemplate_ZengShu)) {
				if(logger.isInfoEnabled()){
					logger.info("[小羊快跑] [使用技能] [失败:buff不正确] [技能id:"+skillId+"] [目标id:"+targetPlayerId+"] ["+player.getLogString()+"]");
				}
				return;
			}
			Buff buff2 = tt2.createBuff(1);
			buff2.setStartTime(SystemTime.currentTimeMillis());
			buff2.setInvalidTime(buff2.getStartTime() + 3000);
			buff2.setCauser(player);
			player.placeBuff(buff2);
			break;
		}
		String mess = skillMess[skillId];
		WOLF_USE_SKILL_RES res = new WOLF_USE_SKILL_RES(req.getSequnceNum(), 1, skillId);
		player.addMessageToRightBag(res);
		if(logger.isInfoEnabled()){
			logger.info("[小羊快跑] [使用技能] [成功] [目标类型:"+targetType+"] [技能:"+mess+"] [技能id:"+skillId+"] [目标id:"+targetPlayerId+"] [eatSheepExp:"+eatSheepExp+"] ["+player.getLogString()+"]");
		}
	}

	List<Long> reList = new ArrayList<Long>();
	/**
	 * 报名中玩家检查
	 */
	public void checkOnlinePlayer(){
		reList.clear();
		for(long id : signList){
			if(PlayerManager.getInstance().isOnline(id) == false){
				reList.add(id);
			}
		}
		if(reList.size() > 0){
			if(logger.isInfoEnabled()){
				logger.info("[小羊快跑] [删除掉线的报名玩家] [成功] [reList:"+reList.size()+"] [signList:"+signList.size()+"]");
			}
			signList.removeAll(reList);
		}
	}
	
	/**
	 * 开启一个副本
	 */
	public void addGameTask(WolfGame game){
		int minTaskIndex = 0;
		int minTaskNums = Integer.MAX_VALUE;
		for(int i=0;i<threads.length;i++){
			WolfGameThread t = threads[i];
			if(minTaskNums > t.getTaskNums()){
				minTaskNums = t.getTaskNums();
				minTaskIndex = i;
			}
		}
		threads[minTaskIndex].addTask(game);
		if(testLog){
			for(WolfGameThread t : threads){
				if(logger.isInfoEnabled()){
					logger.info("[小羊快跑] [线程情况] [副本数量:{}] [minTaskIndex:{}] [{}]", new Object[]{allGames.size(),minTaskIndex, t});
				}
			}
		}
		allGames.add(game);
	}

	/**
	 * 通知周围其他玩家闪现
	 */
	public void noticePlayerMove(Player player, Fighter target){
		if (player == null || target == null) {
			return ;
		}
		SET_POSITION_REQ  req = new SET_POSITION_REQ(GameMessageFactory.nextSequnceNum(), (byte)Game.REASON_CLIENT_STOP,
				player.getClassType(), player.getId(), (short)target.getX(), (short)target.getY());
		player.addMessageToRightBag(req);
		player.setX(target.getX());
		player.setY(target.getY());
		
//		long t = ase.getStartTime() + this.getDuration1() + getEffectiveTime(0);
		
		Point s = new Point(player.getX(),player.getY());
		Point e = new Point(player.getX() + 30,player.getY());
		int L = Graphics2DUtil.distance(s, e);
		if(L > 20){
			short pointX[] = new short[2];
			short pointY[] = new short[2];
			short roadLen[] = new short[1];
			pointX[0] = (short) s.x;
			pointY[0] = (short) s.y;
			
			int L1 = L - 20;
			int L2 = 20;
			pointX[1] = (short)((L1 * e.x + L2 * s.x)/L);
			pointY[1] = (short)((L1 * e.y + L2 * s.y)/L);
			
			roadLen[0] = (short) L1;
			MoveTrace path = new MoveTrace(roadLen, new Point[]{new Point(pointX[0],pointY[0]),new Point(pointX[1],pointY[1])},50);
			
			player.setMoveTrace(path);
		}
	}
	
	
	/**
	 * 粒子
	 */
	public void piaoLiZi(List<Player> ps,String liZiName,String reaseon,Player target){
		try{
			for(Player p : ps){
				ParticleData[] particleDatas = new ParticleData[1];
				particleDatas[0] = new ParticleData(target.getId(), liZiName, 2000, 2, 1, 1);
				NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
				p.addMessageToRightBag(resp);
				if(logger.isInfoEnabled()){
					logger.info("[小羊快跑] [播放粒子] [reaseon:"+reaseon+"] [liZiName:"+liZiName+"] [target:"+target.getLogString()+"] ["+p.getLogString()+"]");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			if(logger.isWarnEnabled()){
				logger.warn("[飘箱子活动] [飘箱子] [异常:{}]",e);
			}
		}
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
