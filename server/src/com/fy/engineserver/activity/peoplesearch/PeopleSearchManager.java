package com.fy.engineserver.activity.peoplesearch;



import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.CheckAttribute;
import com.fy.engineserver.activity.datamanager.AbstractActivity;
import com.fy.engineserver.activity.datamanager.ActivityConstant;
import com.fy.engineserver.activity.datamanager.ActivityDataManager;
import com.fy.engineserver.core.BeatHeartThread;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.peoplesearch.Option_Guess_PeopleSearch;
import com.fy.engineserver.menu.peoplesearch.Option_PeopleSearch_Info;
import com.fy.engineserver.menu.peoplesearch.Option_PeopleSearch_Lookover;
import com.fy.engineserver.message.CAVE_QUERY_SELFFAERY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.newtask.ActivityTaskExp;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;

@CheckAttribute("斩妖降魔管理器")
public class PeopleSearchManager implements Runnable , AbstractActivity{

	static Random random = new Random();

	@CheckAttribute("系统是否开放")
	public static boolean systemOpen = true;
	@CheckAttribute("心跳间隔")
	public static long step = 100L;
	@CheckAttribute("是否正在心跳")
	public static boolean running = true;
	@CheckAttribute("消息数")
	public static int messageNum = 3;
	@CheckAttribute("每天可做的次数")
	public static int dailyNum = 1;
	@CheckAttribute("配置文件路径")
	private String filePath;
	@CheckAttribute("最小等级限制")
	public static int levelLimit = 111;
	@CheckAttribute("消耗体力")
	public static int thew = 15;
	@CheckAttribute("boss的ID")
	private int[] bossId = { 10010305, 10010306, 10010307 };
	@CheckAttribute("人物的出生点")
	private Point playerBournPoint = new Point(988, 664);
	@CheckAttribute("boss的出生点")
	private Point bossBournPoint = new Point(520, 360);
	@CheckAttribute("场景名字,英文")
	public static String sceneName = "mishi";
	@CheckAttribute("移除场景时间间隔")
	public static long removeTime = TimeTool.TimeDistance.MINUTE.getTimeMillis() * 20;
	@CheckAttribute("心跳超时时间")
	public static long timeOutTime = 1000 * 3;
	@CheckAttribute("奖励经验ID")
	public static int expPrizeId = 38;
	@CheckAttribute("奖励物品名字")
	public static String prizeArticleName = Translate.古董;
	@CheckAttribute("奖励物品颜色")
	public static int prizeArticleColor = 1;
	@CheckAttribute("最大分数")
	public static int maxScore = 10;
	public static String[] color_article = { "0xffff", "0x33FF00", "0x0000FF", "0xE43AAD", "0xF48542" };
	@CheckAttribute("心跳次数")
	protected long amountOfBeatheart = 0;
	@CheckAttribute("心跳超时次数")
	protected long amountOfOverflow = 0;
	@CheckAttribute(value = "所有的目标NPC", des = "<id,PeopleTemplet>")
	private Hashtable<Integer, PeopleTemplet> allTempletNpc = new Hashtable<Integer, PeopleTemplet>();
	@CheckAttribute(value = "分国家模板", des = "<id,List<PeopleTemplet>>")
	private HashMap<Integer, List<PeopleTemplet>> countryTempletNpc = new HashMap<Integer, List<PeopleTemplet>>();
	@CheckAttribute("消息提供者")
	private List<CountryNpc> messageProviders = new ArrayList<CountryNpc>();
	/** 所有心跳的场景 */
	private LinkedHashMap<String, PeopleSearchScene> scenes = new LinkedHashMap<String, PeopleSearchScene>();
	private List<PeopleSearchScene> newScenes = new ArrayList<PeopleSearchScene>();
	/** 周几开启  1为周天  7为周六*/
	public static int[] openDayOfWeek = new int[]{2, 4, 6};
	public static int[][] openTime = new int[][]{{11,00}};
	public static int[][] endTime = new int[][]{{11,30}};

	private static PeopleSearchManager instance;

	public static PeopleSearchManager getInstance() {
		return instance;
	}

	/**
	 * 通过ID获得斩妖降魔模板
	 * @param id
	 * @return
	 */
	public PeopleTemplet getPeopleTemplet(int id) {
		return allTempletNpc.get(id);
	}

	/**
	 * 得到随机的提供信息的NPC
	 * @param country
	 * @return
	 */
	public CountryNpc[] getRandomMessageNpc(Player player) {
		List<CountryNpc> npcs = new ArrayList<CountryNpc>();
		int num = 0;
		while (num < messageNum) {// 有点风险,满足条件的NPC必须得大于 messageNum,否则死循环...
			CountryNpc randomNpc = messageProviders.get(random.nextInt(messageProviders.size()));
			if (randomNpc != null && !npcs.contains(randomNpc)) {
				npcs.add(randomNpc);
				num++;
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [得到一个消息NPC] [" + randomNpc.toString() + "]");
				}
			}
		}
		return npcs.toArray(new CountryNpc[0]);
	}

	/**
	 * 随机获得一个目标模板
	 * @param country
	 * @return
	 */
	public synchronized PeopleTemplet getRandomPeopleTemplet(int country) {
		int resultIndex = random.nextInt(countryTempletNpc.get(country).size());
		return countryTempletNpc.get(country).get(resultIndex);
	}

	/**
	 * NPC是否在"猜猜看"的NPC列表中
	 * @param npc
	 * @return
	 */
	public boolean inGuessNpc(NPC npc, Game game) {
		CountryNpc countryNpc = new CountryNpc(npc, game);
		Iterator<PeopleTemplet> itor = allTempletNpc.values().iterator();
		while (itor.hasNext()) {
			PeopleTemplet peopleTemplet = itor.next();
			if (peopleTemplet.getTarget().equals(countryNpc)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是在"ta是谁"NPC列表中
	 * @param npc
	 * @param game
	 * @return
	 */
	public boolean inMessageNPC(NPC npc, Game game) {
		CountryNpc countryNpc = new CountryNpc(npc, game);
		for (int i = 0; i < messageProviders.size(); i++) {
			CountryNpc temp = messageProviders.get(i);
			if (countryNpc.equals(temp)) {
				return true;
			}
		}
		return false;
	}

	private void loadFile() throws Exception {
		long start = SystemTime.currentTimeMillis();
		File file = new File(getFilePath());
		if (!file.exists()) {
			throw new Exception(PeopleSearchManager.class + ",配置文件不存在:" + getFilePath());
		}
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);

		HSSFSheet sheet = workbook.getSheetAt(0);// 目标NPC页
		int maxRow = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < maxRow; i++) {
			try {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				int id = (int) row.getCell(index++).getNumericCellValue();
				int country = (int) row.getCell(index++).getNumericCellValue();
				String mapName = (row.getCell(index++).getStringCellValue());
				String name = (row.getCell(index++).getStringCellValue());
				CountryNpc target = new CountryNpc(name, mapName, country);
				String[][] des = new String[3][];
				des[0] = StringTool.string2Array((row.getCell(index++).getStringCellValue()), "@@", String.class);
				des[1] = StringTool.string2Array((row.getCell(index++).getStringCellValue()), "@@", String.class);
				des[2] = StringTool.string2Array((row.getCell(index++).getStringCellValue()), "@@", String.class);
				PeopleTemplet peopleTemplet = new PeopleTemplet(id, target, des);
				allTempletNpc.put(id, peopleTemplet);
				if (!countryTempletNpc.containsKey(country)) {
					countryTempletNpc.put(country, new ArrayList<PeopleTemplet>());
				}
				countryTempletNpc.get(country).add(peopleTemplet);
			} catch (Exception e) {
				ActivitySubSystem.logger.error("[斩妖降魔] [系统加载] [异常] [目标NPC页] [行数:" + i + "]", e);
				throw e;
			}
		}
		/************************************************************************************************************/
		sheet = workbook.getSheetAt(1);// 提供消息NPC页
		maxRow = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < maxRow; i++) {
			try {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				int country = (int) row.getCell(index++).getNumericCellValue();
				String mapName = (row.getCell(index++).getStringCellValue());
				String name = (row.getCell(index++).getStringCellValue());
				CountryNpc messageProvider = new CountryNpc(name, mapName, country);
				messageProviders.add(messageProvider);
			} catch (Exception e) {
				ActivitySubSystem.logger.error("[斩妖降魔] [系统加载] [异常] [提供消息NPC页] [行数:" + i + "]", e);
				throw e;
			}
		}
		System.out.println(this.getClass() + "[斩妖降魔] [配置文件加载完成] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
		/************************************************************************************************************/
	}

	public boolean isPeopleSearchSceneExist(Player p) {
		boolean b = scenes.containsKey(p.getPeopleSearchKey());
		if (b) return true;
		synchronized (this.newScenes) {
			for (PeopleSearchScene e : newScenes) {
				if (e.getOwner() == p) {
					return true;
				}
			}
		}
		return false;
	}

	public PeopleSearchScene getPeopleSearchScene(Player p) {
		PeopleSearchScene ss = scenes.get(p.getPeopleSearchKey());
		if (ss != null) {
			return ss;
		}
		synchronized (this.newScenes) {
			for (PeopleSearchScene e : newScenes) {
				if (e.getOwner() == p) {
					return e;
				}
			}
		}
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn(p.getLogString() + "[斩妖降魔] [没找到对应的场景]");
		}
		return null;
	}

	public void putToNewList(PeopleSearchScene e) {
		synchronized (newScenes) {
			newScenes.add(e);
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Hashtable<Integer, PeopleTemplet> getAllTempletNpc() {
		return allTempletNpc;
	}

	public void setAllTempletNpc(Hashtable<Integer, PeopleTemplet> allTempletNpc) {
		this.allTempletNpc = allTempletNpc;
	}

	public List<CountryNpc> getMessageProviders() {
		return messageProviders;
	}

	public void setMessageProviders(List<CountryNpc> messageProviders) {
		this.messageProviders = messageProviders;
	}

	public int[] getBossId() {
		return bossId;
	}

	public void setBossId(int[] bossId) {
		this.bossId = bossId;
	}

	public Point getBossBournPoint() {
		return bossBournPoint;
	}

	public void setBossBournPoint(Point bossBournPoint) {
		this.bossBournPoint = bossBournPoint;
	}

	public Point getPlayerBournPoint() {
		return playerBournPoint;
	}

	public void setPlayerBournPoint(Point playerBournPoint) {
		this.playerBournPoint = playerBournPoint;
	}

	public void init() throws Exception {
		
		loadFile();
		instance = this;
		Thread thread = new Thread(instance, "PeopleSearchManager-" + new Random().nextInt(1000000));
		thread.start();
		ServiceStartRecord.startLog(this);
	}

	@Override
	public void run() {
		while (running) {
			try {
				synchronized (newScenes) {
					for (PeopleSearchScene newSence : newScenes) {
						scenes.put(newSence.getOwner().getPeopleSearchKey(), newSence);
					}
					newScenes.clear();
				}
				// 心跳....
				long start = SystemTime.currentTimeMillis();
				if (scenes == null) {
					scenes = new LinkedHashMap<String, PeopleSearchScene>();
				}
				Iterator<PeopleSearchScene> itor = scenes.values().iterator();
				while (itor.hasNext()) {
					PeopleSearchScene peopleSearchScene = (PeopleSearchScene) itor.next();
					if (peopleSearchScene == null) {// 剔除空对象和无效的对象
						itor.remove();
						continue;
					}
					if (!peopleSearchScene.isValid()) {
						itor.remove();
						if (ActivitySubSystem.logger.isWarnEnabled()) {
							ActivitySubSystem.logger.warn(peopleSearchScene.getOwner().getLogString() + "[斩妖降魔] [移除无效的场景]");
						}
						continue;
					}
					peopleSearchScene.heartbeat(start);
				}
				long end = SystemTime.currentTimeMillis();
				long cost = end - start;
				if (cost > timeOutTime) {
					BeatHeartThread.logger.error("[{}] [beatheart-overflow]  [overflow:{}] [amount{}] [cost:{}ms]", new Object[] { Thread.currentThread().getName(), amountOfOverflow, amountOfBeatheart, cost });
					amountOfOverflow++;
				}
				amountOfBeatheart++;
				
				if (cost < step) {
					Thread.sleep(step - cost);
				}
			} catch (Throwable e) {
				ActivitySubSystem.logger.error("寻人地图心跳异常", e);
				try {
					Thread.sleep(1000L);
				} catch (Throwable e1) {
					ActivitySubSystem.logger.error("[寻人地图心跳异常] [又异常了] [停1秒]", e1);
				}
			}
		}
	}

	/**
	 * 将角色传送到自己的场景,当出现任何错误,都将玩家传送到回城点
	 * @param owner
	 */
	public void transferToSelfScence(Player owner) {
		PeopleSearch peopleSearch = owner.getPeopleSearch();
		if (peopleSearch != null) {
			if (peopleSearch.canEnterGame()) {
				if (isPeopleSearchSceneExist(owner)) {
					PeopleSearchScene peopleSearchScene = getPeopleSearchScene(owner);
					owner.getCurrentGame().playerLeaveGame(owner);
					// 这是条通用协议,用于处理进入特殊地图
					CAVE_QUERY_SELFFAERY_RES res = new CAVE_QUERY_SELFFAERY_RES(GameMessageFactory.nextSequnceNum(), peopleSearchScene.getGame().gi.name, owner.getPeopleSearchKey());
					owner.addMessageToRightBag(res);
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [要进入自己的boss地图] [成功] [通知地图名字:" + owner.getPeopleSearchKey() + "]");
					}
					return;
				} else {
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [要进入自己的boss地图] [失败] [没找到目标地图,可能被移除了]");
					}
				}
			} else {
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [要进入自己的boss地图] [失败] [还没找到目标]");
				}
			}
		} else {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [要进入自己的boss地图] [失败] [没有进行中的活动]");
			}
		}
		{
			// 上面判断没通过,直接传送到回城点
			owner.getCurrentGame().transferGame(owner, new TransportData(0, 0, 0, 0, owner.getResurrectionMapName(), owner.getResurrectionX(), owner.getResurrectionY()));
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [要进入自己的boss地图] [失败] [直接传送到回城点]");
			}
		}
	}

	public PeopleSearchScene[] getPeopleSearchScenes() {
		return scenes.values().toArray(new PeopleSearchScene[0]);
	}

	/***
	 * 给NPC添加该有的窗口
	 * @param player
	 * @param npc
	 * @param mw
	 */
	public MenuWindow addOption(Player player, NPC npc, MenuWindow mw, Game game) {
		if (player.getPeopleSearch() == null) {
			return mw;
		}
		boolean isSearchNPC = false;
		if (inGuessNpc(npc, game)) {
			isSearchNPC = true;
		}
		if (!isSearchNPC) {
			if (inMessageNPC(npc, game)) {
				isSearchNPC = true;
			}
		}
		if (!isSearchNPC) {
			if (ActivitySubSystem.logger.isInfoEnabled()) {
				ActivitySubSystem.logger.info(player.getLogString() + "[斩妖降魔] [NPC:" + npc.getName() + "不是寻人NPC] [直接返回]");
			}
			return mw;
		}
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [进入添加菜单逻辑] ");
		}
		PeopleSearch peopleSearch = player.getPeopleSearch();
		CountryNpc countryNpc = new CountryNpc(npc, player.getCurrentGame());
		CountryNpc target = peopleSearch.getPeopleTemplet().getTarget();

		if (mw == null || mw.getId() == -1) {
			MenuWindow newmw = WindowManager.getInstance().createTempMenuWindow(60);
			newmw.setTitle(mw == null ? "" : mw.getTitle());
			newmw.setDescriptionInUUB(mw == null ? "" : mw.getDescriptionInUUB());
			newmw.setNpcId(mw == null ? 1 : mw.getNpcId());
			newmw.setShowTask(true);
			mw = newmw;
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [是-1的wid] [创建一个新的] [npc:" + npc.getName() + "] [wid:" + mw.getId() + "]");
			}
		}
		// 处理猜猜看菜单
		if (peopleSearch.isFound()) { // 找到目标了,非目标不显示"寻找"菜单
			if (countryNpc.equals(target)) {
				Option_Guess_PeopleSearch option = new Option_Guess_PeopleSearch(npc);
				option.setText(Translate.我猜就是你);
				player.getCurrentGame().addOption2MenuWindow(player, mw, option);
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [找到猜猜看菜单] [" + countryNpc.toString() + "] [已经找到目标,当前就是目标]");
				}
			}
		} else { // 没有找到目标,所有的目标NPC都加上菜单
			List<PeopleTemplet> countryTemplet = countryTempletNpc.get(Integer.valueOf(player.getCountry()));
			if (countryTemplet == null) {
				ActivitySubSystem.logger.error(player.getLogString() + "[斩妖降魔] [找到猜猜看菜单] [没有找到国家相应的NPC列表] [国家:" + CountryManager.得到国家名(player.getCountry()) + "]");
			} else {
				Iterator<PeopleTemplet> itor = countryTemplet.iterator();
				while (itor.hasNext()) {
					PeopleTemplet tempTemplet = itor.next();// 每个目标模板
					if (tempTemplet.getTarget().equals(countryNpc)) {
						Option_Guess_PeopleSearch option = new Option_Guess_PeopleSearch(npc);
						option.setText(Translate.我猜就是你);
						player.getCurrentGame().addOption2MenuWindow(player, mw, option);
						
						Option_PeopleSearch_Info peopleSearch_Info = new Option_PeopleSearch_Info();
						peopleSearch_Info.setNPC(npc);
						peopleSearch_Info.setText(Translate.斩妖降魔);
						player.getCurrentGame().addOption2MenuWindow(player, mw, peopleSearch_Info);
						
						
						if (ActivitySubSystem.logger.isWarnEnabled()) {
							ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [找到猜猜看菜单] [当前npc:" + countryNpc.toString() + "] [模板:" + tempTemplet.getId() + "] [相符]");
						}
						break;
					} else {
						if (ActivitySubSystem.logger.isInfoEnabled()) {
							ActivitySubSystem.logger.info(player.getLogString() + "[斩妖降魔] [找到猜猜看菜单] [当前npc:" + countryNpc.toString() + "] [模板:" + tempTemplet.getId() + "] [不符]");
						}
					}
				}
			}
		}
		// 处理打探消息菜单
		for (CountryNpc messageNpc : peopleSearch.getMessageNpc()) {
			if (messageNpc.equals(countryNpc)) {
				Option_PeopleSearch_Lookover option = new Option_PeopleSearch_Lookover(npc);
				option.setText(Translate.说Ta是谁);
				player.getCurrentGame().addOption2MenuWindow(player, mw, option);
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + "[斩妖降魔] [打开消息提供者菜单] [" + countryNpc.toString() + "] [是否找到目标:" + peopleSearch.isFound() + "]");
				}
				break;
			} else {
				if (ActivitySubSystem.logger.isInfoEnabled()) {
					ActivitySubSystem.logger.info(player.getLogString() + "[斩妖降魔] [打开非消息提供者菜单] [" + countryNpc.toString() + "] [是否找到目标:" + peopleSearch.isFound() + "]");
				}
			}
		}
		return mw;
	}

	/**
	 * 得到经验奖励
	 * @param score
	 * @return
	 */
	public static long getExp(Player player, int score) {
		ActivityTaskExp atExp = TaskManager.getInstance().activityPrizeMap.get(expPrizeId);
		if (atExp == null) {
			ActivitySubSystem.logger.error(player.getLogString() + "[斩妖降魔] [完成了寻人] [经验配置不存在] [经验组ID:" + expPrizeId + "] [当前分数:" + score + "]");
			return 1L;
		}
		long exp = atExp.getExpPrize()[player.getLevel() - 1];
		long prizeExp = (long) (exp * 0.4 + exp * 0.6 * score / 10);
		return prizeExp;
	}
	
	public boolean isOpen(long now) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(now);
		int w = c.get(Calendar.DAY_OF_WEEK);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		for (int i=0; i<openDayOfWeek.length; i++) {
			if (openDayOfWeek[i] == w) {
				for (int j=0; j<openTime.length; j++) {
					if (hour >= openTime[j][0] && min >= openTime[j][1] && hour <= endTime[j][0] && min <= endTime[j][1]) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public boolean isOpen() {
		return this.isOpen(System.currentTimeMillis());
	}

	@Override
	public String getActivityName() {
		// TODO Auto-generated method stub
		return ActivityConstant.斩妖除魔;
	}

	@Override
	public List<String> getActivityOpenTime(long now) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(now);
		boolean open = false;
		int w = c.get(Calendar.DAY_OF_WEEK);
		for (int i=0; i<openDayOfWeek.length; i++) {
			if (w == openDayOfWeek[i]) {
				open = true;
				break;
			}
		}
		if (!open) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		for (int i=0; i<openTime.length; i++) {
			result.add(openTime[i][0] + ":" + ActivityDataManager.getMintisStr(openTime[i][1]));
		}
		return result;
	}

	@Override
	public boolean isActivityTime(long now) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(now);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		for (int i=0; i<openTime.length; i++) {
			if (hour >= openTime[i][0] && min >= openTime[i][1] && hour <= endTime[i][0] && min <= endTime[i][1]) {
				return true;
			}
		}
		return false;
	}
}
