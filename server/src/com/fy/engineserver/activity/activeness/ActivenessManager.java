package com.fy.engineserver.activity.activeness;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.boss.authorize.model.Passport;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventPlayerDropPet;
import com.fy.engineserver.event.cate.EventPlayerEnterScene;
import com.fy.engineserver.event.cate.EventPlayerLogin;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.message.ACTIVENESS_LIST_RES;
import com.fy.engineserver.message.CAN_GET_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.fy.engineserver.util.config.ServerFit4Activity;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.GameChongZhiFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class ActivenessManager implements MConsole, EventProc {

	public static boolean open = true;

	private List<ActivityForActiveness> activityForActivenesses = new ArrayList<ActivityForActiveness>();
	private Map<Integer,List<ActivityForActiveness>> activityForActivenesses2 = new HashMap<Integer, List<ActivityForActiveness>>();
	private List<PlayerActivenessInfo> playerActivenessInfos = new ArrayList<PlayerActivenessInfo>();

	public static SimpleEntityManager<PlayerActivenessInfo> em;

	public boolean isTest = false;
	public boolean openPop = true;// 是否登录自动弹框
	private MemoryNPCManager npcManager;
	private TaskManager taskManager;
	public ActivenessConfig conf;

	/** 活跃度的其它属性 */
	private int dayActivenessLimit; // 单日活跃度上限

	private List<Award> awards = new ArrayList<Award>();
	private List<Lottery> lotteries = new ArrayList<Lottery>();

	private int[] awardLevel;
	private String[] awardName;
	private String[] awardNameFtr;

	private String[] lotteryNames;
	private String[] lotteryNamesFtr;
	private Integer[] lotteryNums;
	private Integer[] lotteryColors;
	private Integer[] lotteryProbs;

	private int[] lotteryLevel; // 满足抽奖的活跃度等级
	private int[] lotteryTimes; // 活跃度对应的抽奖次数

	private int[] signAwardLevel;
	private String[] signAwardName;
	private String[] signAwardNameFtr;

	private static ActivenessManager instance;
	private String filePath;

	public static ActivenessManager getInstance() {
		return instance;
	}

	private void load() throws Exception {
		File file = new File(getFilePath());
		if (!isTest) {
			file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		}
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		try {
			HSSFWorkbook hssfWorkbook = null;
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			hssfWorkbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = null;
			sheet = hssfWorkbook.getSheetAt(0);
			if (sheet == null) return;
			int rows0 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows0; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
						ActivitySubSystem.logger.warn("读到了空白");
						break;
					}
//					if ("".equals(StringTool.modifyContent(cell.getStringCellValue()))) {
//						break;
//					}
					int id = (int) cell.getNumericCellValue();
					int activenessType = StringTool.getCellValue(row.getCell(index++), int.class);
					ActivenessType type = getActivenessType(activenessType);
					String name = row.getCell(index++).getStringCellValue();
					String nameFtr = row.getCell(index++).getStringCellValue();
					String startGameCn = "";
					cell = row.getCell(index++);
					if (cell != null) {
						startGameCn = cell.getStringCellValue();
					}
					String startGameCnFtr = "";
					cell = row.getCell(index++);
					if (cell != null) {
						startGameCnFtr = cell.getStringCellValue();
					}
					String startGameRes = "";
					if (!isTest) {
						if (startGameCn != null && !"".equals(startGameCn)) {
							startGameRes = GameManager.getInstance().getResName(startGameCn, CountryManager.国家A);
							if (startGameRes == null) {
								startGameRes = GameManager.getInstance().getResName(startGameCn, CountryManager.中立);
							}
						}
					}
					int startX = (int) row.getCell(index++).getNumericCellValue();
					int startY = (int) row.getCell(index++).getNumericCellValue();
					String shortdes = row.getCell(index++).getStringCellValue();
					String shortdesFtr = row.getCell(index++).getStringCellValue();
					String detaildes = row.getCell(index++).getStringCellValue();
					String detaildesFtr = row.getCell(index++).getStringCellValue();
					int exp = (int) row.getCell(index++).getNumericCellValue();
					int article = (int) row.getCell(index++).getNumericCellValue();
					int equipment = (int) row.getCell(index++).getNumericCellValue();
					int levelLimit = (int) row.getCell(index++).getNumericCellValue();
					int timesLimit = (int) row.getCell(index++).getNumericCellValue();
					int activityAdd = (int) row.getCell(index++).getNumericCellValue();
					int tili = (int) row.getCell(index++).getNumericCellValue();
					int country = (int) row.getCell(index++).getNumericCellValue();
					ActivityForActiveness activityForActiveness = new ActivityForActiveness(id, name, nameFtr, startGameRes, "", "", startX, startY, shortdes, shortdesFtr, detaildes, detaildesFtr, exp, article, equipment, levelLimit, timesLimit, activityAdd, tili, type);
					if (activityForActiveness == null || "".equals(activityForActiveness)) {
						ActivitySubSystem.logger.warn("[activityForActiveness" + name + " is null ]");
					} else {
						putToCollections(activityForActiveness);
						List<ActivityForActiveness> list = activityForActivenesses2.get(new Integer(country));
						if(list == null){
							list = new ArrayList<ActivityForActiveness>();
						}
						list.add(activityForActiveness);
						activityForActivenesses2.put(new Integer(country), list);
					}
				}
			}

			HSSFCell cell1 = hssfWorkbook.getSheetAt(1).getRow(1).getCell(0);
			dayActivenessLimit = (int) cell1.getNumericCellValue();

			/** 活跃度领奖 */
			sheet = hssfWorkbook.getSheetAt(2);
			if (sheet == null) return;
			int rows2 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows2; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					String startTime = row.getCell(index++).getStringCellValue();
					String endTime = row.getCell(index++).getStringCellValue();
					String platformStr = row.getCell(index++).getStringCellValue();
					String openServerStr = "";
					HSSFCell cell = row.getCell(index++);
					if (cell != null) {
						openServerStr = cell.getStringCellValue();
					}
					String limitServerStr = "";
					cell = row.getCell(index++);
					if (cell != null) {
						limitServerStr = cell.getStringCellValue();
					}
					Integer[] awardLevels = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ",", Integer.class);
					awardLevel = new int[awardLevels.length];
					for (int j = 0; j < awardLevels.length; j++) {
						if (awardLevels[j] != null) {
							awardLevel[j] = awardLevels[j].intValue();
						}
					}
					String[] awardNames = row.getCell(index++).getStringCellValue().trim().split(",");
					String[] awardCNNames = row.getCell(index++).getStringCellValue().trim().split(",");
					Award award = new Award(TimeTool.formatter.varChar19.parse(startTime), TimeTool.formatter.varChar19.parse(endTime), platformStr, openServerStr, limitServerStr, awardLevels, awardCNNames);
					awards.add(award);
				}
			}

			/** 抽奖 */
			sheet = hssfWorkbook.getSheetAt(3);
			if (sheet == null) return;
			int rows3 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows3; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					String startTime = row.getCell(index++).getStringCellValue();
					String endTime = row.getCell(index++).getStringCellValue();
					String platformStr = row.getCell(index++).getStringCellValue();
					String openServerStr = "";
					HSSFCell cell = row.getCell(index++);
					if (cell != null) {
						openServerStr = cell.getStringCellValue();
					}
					String limitServerStr = "";
					cell = row.getCell(index++);
					if (cell != null) {
						limitServerStr = cell.getStringCellValue();
					}
					String[] names = row.getCell(index++).getStringCellValue().trim().split(",");
					String[] cnNames = row.getCell(index++).getStringCellValue().trim().split(",");
					Integer[] nums = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ",", Integer.class);
					Integer[] colors = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ",", Integer.class);
					Integer[] probs = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ",", Integer.class);
					Lottery lottery = new Lottery(TimeTool.formatter.varChar19.parse(startTime), TimeTool.formatter.varChar19.parse(endTime), platformStr, openServerStr, limitServerStr, cnNames, nums, colors, probs);
					lotteries.add(lottery);
				}
			}

			/** 抽奖次数 */
			sheet = hssfWorkbook.getSheetAt(4);
			if (sheet == null) return;
			int rows4 = sheet.getPhysicalNumberOfRows();
			lotteryLevel = new int[rows4 - 1];
			lotteryTimes = new int[rows4 - 1];
			for (int i = 1; i < rows4; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					lotteryLevel[i - 1] = (int) row.getCell(index++).getNumericCellValue();
					lotteryTimes[i - 1] = (int) row.getCell(index++).getNumericCellValue();
				}
			}

			/** 签到领奖 */
			sheet = hssfWorkbook.getSheetAt(5);
			if (sheet == null) return;
			int rows5 = sheet.getPhysicalNumberOfRows();
			signAwardLevel = new int[rows5 - 1];
			signAwardName = new String[rows5 - 1];
			signAwardNameFtr = new String[rows5 - 1];
			for (int i = 1; i < rows5; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					int level = (int) row.getCell(index++).getNumericCellValue();
					String name = row.getCell(index++).getStringCellValue();
					String bameFtr = row.getCell(index++).getStringCellValue();
					signAwardLevel[i - 1] = level;
					signAwardName[i - 1] = name;
					signAwardNameFtr[i - 1] = bameFtr;
				}
			}
			ActivitySubSystem.logger.warn("[系统初始化] [活跃度]");
		} catch (Exception e) {
			throw e;
		}
	}


	/**
	 * 获得领奖的奖励信息
	 * @param time
	 * @param serverName
	 * @return
	 */
	public Award findRightAward(long time, String serverName) {
		for (Award a : awards) {
			try {
				if (a.getStartTime() < time && a.getEndTime() > time) {
					ServerFit4Activity sf4a = new ServerFit4Activity(a.getPlatforms(), a.getOpenServers(), a.getLimitServers());
					if (sf4a.thiserverFit()) {
						ActivitySubSystem.logger.warn("[活跃度] [" + serverName + "] [领奖礼包:" + Arrays.toString(a.getAwardCNName()) + "]");
						return a;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ActivitySubSystem.logger.warn("[活跃度] [" + serverName + "] [通用领奖礼包:" + Arrays.toString(awards.get(awards.size() - 1).getAwardCNName()) + "]");
		return awards.get(awards.size() - 1);
	}

	/**
	 * 获得抽奖的奖励信息
	 * @param time
	 * @param serverName
	 * @return
	 */
	public Lottery findRightLottery(long time, String serverName) {
		for (Lottery l : lotteries) {
			try {
				if (l.getStartTime() < time && l.getEndTime() > time) {
					ServerFit4Activity sf4a = new ServerFit4Activity(l.getPlatforms(), l.getOpenServers(), l.getLimitServers());
					if (sf4a.thiserverFit()) {
						ActivitySubSystem.logger.warn("[活跃度] [" + serverName + "] [抽奖礼包:" + Arrays.toString(l.getLotteryCNNames()) + "]");
						return l;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ActivitySubSystem.logger.warn("[活跃度] [" + serverName + "] [通用抽奖礼包:" + Arrays.toString(lotteries.get(lotteries.size() - 1).getLotteryCNNames()) + "]");
		return lotteries.get(lotteries.size() - 1);
	}

	/**
	 * 获得活跃度活动类型
	 * @param type
	 * @return ActivenessType
	 */
	public ActivenessType getActivenessType(int type) {
		for (ActivenessType at : ActivenessType.values()) {
			if (at.getType() == type) {
				return at;
			}
		}
		throw new IllegalStateException("[无效的activenessType:" + type + "]");
	}

	/**
	 * 获得玩家身上的有序活跃度活动列表
	 * @param player
	 * @return List<ActivityForActiveness>
	 */
	public List<ActivityForActiveness> getOrderedActiveness(Player player) {
		PlayerActivenessInfo pai = player.getActivenessInfo();
		List<ActivityForActiveness> listUndone = new ArrayList<ActivityForActiveness>();
		List<ActivityForActiveness> notInCycle = new ArrayList<ActivityForActiveness>();
		List<ActivityForActiveness> done = new ArrayList<ActivityForActiveness>();// 存放已经完成和不在活动周期内的活动
		if (pai != null) {
			Map<Integer, Integer> doneNum = pai.getDoneNum();
			for (ActivityForActiveness afa : activityForActivenesses2.get(new Integer(player.getCountry()))) {
				if (doneNum.get(afa.getId()) !=  null && (doneNum.get(afa.getId()) >= afa.getTimesLimit())) {
					done.add(afa); // 已完成
				} else if ((afa.getType().getName().equals("答题") && missTime()[0]) || (afa.getType().getName().equals("武圣争夺战") && missTime()[1]) || (afa.getType().getName().equals("采花大盗") && missTime()[2])) {
					// TODO 这里是固定为这几类写的，以后有类似的，要修改此处
					notInCycle.add(afa); // 未完成且已过了活动时间，这几个特殊处理
				} else {
					listUndone.add(afa); // 未完成且可以做
				}
			}
			listUndone.addAll(done);
			listUndone.addAll(notInCycle);
			return listUndone;
		} else {
			ActivitySubSystem.logger.warn(player.getLogString() + "[获取玩家活跃度信息失败]");
			return null;
		}
	}

	/**
	 * 判断几个特殊的活动是否过了当天活动时间
	 * 顺序是答题、武圣争夺战、采花
	 * @return boolean[]
	 */
	public boolean[] missTime() {
		boolean[] missTime = new boolean[3];
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		int nowToInt = Integer.parseInt(sdf.format(now));
		// TODO 这里是固定为这几类写的，以后有类似的，要修改此处
		if (nowToInt > 1730) {
			missTime[0] = true;
		}
		if (nowToInt > 1800) {
			missTime[1] = true;
		}
		if (nowToInt > 2200) {
			missTime[2] = true;
		}
		return missTime;
	}

	private void putToCollections(ActivityForActiveness activityForActiveness) {
		activityForActivenesses.add(activityForActiveness);
	}

	public boolean 今日是否签到(Player player){
		int[] caInfo = getCalenderInfo(System.currentTimeMillis());
		PlayerActivenessInfo pai = player.getActivenessInfo();
		boolean[] hasSign = pai.getHasSign();
		if (hasSign != null) {
			if (hasSign[caInfo[2] - 1]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据player获得活跃度信息
	 * @param player
	 * @return
	 */
	public PlayerActivenessInfo getPlayerActivenessInfoFromDB(Player player) {
		PlayerActivenessInfo pai = player.getActivenessInfo();
		if (pai != null) {
			if (pai.getDoneNum() == null) {
				Map<Integer, Integer> doneNum = new ConcurrentHashMap<Integer, Integer>();
				for (ActivityForActiveness afa : activityForActivenesses2.get(new Integer(player.getCountry()))) {
					doneNum.put(afa.getId(), 0);
				}
				pai.setDoneNum(doneNum);
			} else {
				// 如果有新添加的活跃度活动，在加载玩家信息的时候放到玩家身上
				for (ActivityForActiveness afa : activityForActivenesses2.get(new Integer(player.getCountry()))) {
					if (pai.getDoneNum().get(afa.getId()) == null) {
						pai.getDoneNum().put(afa.getId(), 0);
					}
				}
			}
			int days = getCalenderInfo(System.currentTimeMillis())[1];
			if (pai.getHasSign() == null || pai.getHasSign().length != days) {
				pai.setHasSign(new boolean[days]);
			}
			if (pai.getHasGotSign() == null) {
				pai.setHasGotSign(new boolean[signAwardLevel.length]);
			}
			return player.getActivenessInfo();
		}
		PlayerActivenessInfo activenessInfo = null;
		try {
			activenessInfo = em.find(player.getId()); // 如果玩家身上拿不到，就去数据库里拿
		} catch (Exception e) {
			ActivitySubSystem.logger.warn("[活跃度] [" + player.getLogString() + "]" + e);
			// return null;
		}
		// TODO 还要处理数据库里存在但是因为某种原因没有取到的情况,或者new了新对象存库没成功
		if (activenessInfo == null) {
			activenessInfo = new PlayerActivenessInfo();
			activenessInfo.setId(player.getId());
			activenessInfo.setGotten(new boolean[awardLevel.length]);
			ActivityManagers.getInstance().changeActivityNums(player);
			Map<Integer, Integer> doneNum = new ConcurrentHashMap<Integer, Integer>();
			for (ActivityForActiveness afa : activityForActivenesses2.get(new Integer(player.getCountry()))) {
				doneNum.put(afa.getId(), 0);
			}
			activenessInfo.setDoneNum(doneNum);
			int days = getCalenderInfo(System.currentTimeMillis())[1];
			activenessInfo.setHasSign(new boolean[days]);
			activenessInfo.setHasGotSign(new boolean[signAwardLevel.length]);
			em.notifyNewObject(activenessInfo);
			ActivitySubSystem.logger.info(player.getLogString() + "new activenessInfo successful");
		}
		player.setActivenessInfo(activenessInfo);
		return player.getActivenessInfo();
	}

	/**
	 * 获取月份，月份总天数，当天是几号
	 * @param time
	 * @return int[]，int[0]月份，int[1]月份总天数,int[2]当天是几号
	 */
	public int[] getCalenderInfo(long time) {
		int[] caInfo = new int[3];
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		caInfo[0] = c.get(c.MONTH) + 1;
		caInfo[1] = c.getActualMaximum(c.DAY_OF_MONTH);
		caInfo[2] = c.get(c.DAY_OF_MONTH);
		return caInfo;
	}

	/**
	 * 影响活跃度的活动记录次数,加1次
	 * @param player
	 * @param activenessType
	 * @param addNum
	 */
	public void record(Player player, ActivenessType activenessType) {
		ActivitySubSystem.logger.warn(player.getLogString() + "[完成活跃事件] [" + activenessType.toString() + "]");
		List<ActivityForActiveness> afaList = getAfaForType(activenessType,player);
		Map<Integer, Integer> doneNum = player.getActivenessInfo().getDoneNum();
		Set<Integer> cids = doneNum.keySet();
		Iterator<Integer> iter = cids.iterator();
		while (iter.hasNext()) {
			int id = iter.next();
			for (ActivityForActiveness afa : afaList) {
				if (id == afa.getId()) {
					doneNum.put(id, doneNum.get(id) + 1);
					player.getActivenessInfo().setDoneNum(doneNum);
					ActivitySubSystem.logger.warn(player.getLogString() + "[活跃度] [" + afa.getName() + "完成次数加1] [今日完成次数:" + doneNum.get(id) + "]");
					if (doneNum.get(id) == afa.getTimesLimit()) {
						addActiveness(player, afa);
						ActivitySubSystem.logger.warn(player.getLogString() + "[活跃度] [" + activenessType.getName() + "完成] [增加活跃度：" + afa.getActivityAdd() + "]");
					}
				}
			}
		}
	}

	/**
	 * 影响活跃度的活动记录次数
	 * @param player
	 * @param recordAction
	 * @param addNum
	 */
	public void record(Player player, ActivenessType activenessType, int addNum) {
		for (int i = 0; i < addNum; i++) {
			record(player, activenessType);
		}
	}

	
	public List<ActivityForActiveness> getAfaForType(ActivenessType activenessType,Player p) {
		List<ActivityForActiveness> afaList = new ArrayList<ActivityForActiveness>();
		for (ActivityForActiveness afa : activityForActivenesses2.get(new Integer(p.getCountry()))) {
			if (activenessType.getType() == afa.getType().getType()) {
				afaList.add(afa);
			}
		}
		return afaList;
	}

	/**
	 * 增加活跃度,同时根据条件更新可抽奖次数
	 * @param player
	 * @param afa
	 */
	public void addActiveness(Player player, ActivityForActiveness afa) {
		PlayerActivenessInfo pai = this.getPlayerActivenessInfoFromDB(player);
		if ((pai.getDayActiveness() + afa.getActivityAdd()) <= dayActivenessLimit) {
			pai.setDayActiveness(pai.getDayActiveness() + afa.getActivityAdd());
			ActivitySubSystem.logger.warn(player.getLogString() + "[增加单日活跃度:" + afa.getActivityAdd() + "] [增加后单日活跃度:" + pai.getDayActiveness() + "]");
			try {
				BillingCenter.getInstance().playerSaving(player, afa.getActivityAdd(), CurrencyType.ACTIVENESS, SavingReasonType.ACTIVENESS, "");
				addLog(player, afa.getActivityAdd(), SavingReasonType.getSavingReason(SavingReasonType.ACTIVENESS));
			} catch (SavingFailedException e) {
				e.printStackTrace();
			}
		} else {
			pai.setDayActiveness(dayActivenessLimit);
			ActivitySubSystem.logger.warn(player.getLogString() + "[单日活跃度达到上限] [增加后单日活跃度:" + pai.getDayActiveness() + "]");
		}
		ActivitySubSystem.logger.warn(player.getLogString() + "[增加总活跃度:" + afa.getActivityAdd() + "] [增加后总活跃度:" + pai.getTotalActiveness() + "]");

		int canLottery = 0;
		int n = 0;
		boolean temp = true;
		if (pai.getDayActiveness() < lotteryLevel[0]) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[活跃度不够不能抽奖] [活跃度：" + pai.getDayActiveness() + "]");
		} else {
			for (int i = 1; i < lotteryLevel.length; i++) {
				if (pai.getDayActiveness() < lotteryLevel[i]) {
					n = i - 1;
					temp = false;
					break;
				}
			}
			int size = temp ? lotteryTimes.length - 1 : n;
			canLottery = lotteryTimes[size];
		}
		pai.setCanLottery(canLottery);
		ActivitySubSystem.logger.warn(player.getLogString() + "[活跃度更新可抽奖次数：" + canLottery + "]");

		sendBright(player);
	}

	public void addLog(Player player, long value, String reson) {
		try {
			StatClientService statClientService = StatClientService.getInstance();
			GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();
			Passport pp = player.getPassport();
			if (pp != null) {
				gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
			}
			String channel = (player.getPassport() != null ? player.getPassport().getLastLoginChannel() : "无");
			gameChongZhiFlow.setAction(0); // 0 充值 ，1 消耗
			gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.ACTIVENESS));
			gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
			gameChongZhiFlow.setGame(CountryManager.得到国家名(player.getCountry()));
			gameChongZhiFlow.setGameLevel(String.valueOf(player.getLevel()));
			gameChongZhiFlow.setMoney(value);
			gameChongZhiFlow.setQuDao(channel);
			gameChongZhiFlow.setReasonType(reson);
			gameChongZhiFlow.setTime(System.currentTimeMillis());
			gameChongZhiFlow.setUserName(player.getUsername());
			if (!TestServerConfigManager.isTestServer()) {
				statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.error("活跃度添加统计日志出错=", e);
		}
	}

	/**
	 * 判断是否还有未领取和未抽取的奖励
	 * @param pai
	 * @return boolean
	 */
	public boolean canLotteryOrGetAward(PlayerActivenessInfo pai) {
		boolean hasSign = false;
		boolean getAllSignAward = true;
		boolean getAllAward = true;
		boolean getAllLottery = true;
		try {
			int day = getCalenderInfo(System.currentTimeMillis())[2];
			hasSign = pai.getHasSign()[day - 1];
			for (int i = signAwardLevel.length; i > 0; i--) {
				if (getSignDays(PlayerManager.getInstance().getPlayer(pai.getId())) >= signAwardLevel[i - 1]) {
					for (int j = 0; j <= i - 1; j++) {
						getAllSignAward = pai.getHasGotSign()[j] && getAllSignAward;
					}
				}
			}
			for (int i = awardLevel.length; i > 0; i--) {
				if (pai.getDayActiveness() >= awardLevel[i - 1]) {
					for (int j = 0; j <= i - 1; j++) {
						getAllAward = pai.getGotten()[j] && getAllAward;
					}
				}
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.warn("[playerid:" + pai.getId() + "]" + e);
			e.printStackTrace();
		}
		if (pai.getCanLottery() > 0 && pai.getHasLottery() < pai.getCanLottery()) {
			getAllLottery = false;
		}
		if (hasSign && getAllSignAward && getAllAward && getAllLottery) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 有可领取但未领取的奖励，或者可抽奖次数没使用，给客户端发协议闪粒子
	 * @param p
	 */
	public void sendBright(Player p) {
		PlayerActivenessInfo pai = p.getActivenessInfo();
		if (pai != null) {
			CAN_GET_RES canGetRes = new CAN_GET_RES();
			if (canLotteryOrGetAward(pai)) {
				canGetRes.setCanGet(true);
			} else {
				canGetRes.setCanGet(false);
			}
			ActivitySubSystem.logger.warn(p.getLogString() + "是否有未领取和抽取的奖励" + canGetRes.getCanGet());
			p.addMessageToRightBag(canGetRes);
		}
	}

	/**
	 * 通过id获得一个活跃度活动对象
	 * @param id
	 * @return ActivityForActiveness
	 */
	public ActivityForActiveness getAfaById(int id) {
		for (ActivityForActiveness afa : activityForActivenesses) {
			if (afa.getId() == id) {
				return afa;
			}
		}
		return null;
	}

	/** 抽奖 */
	public String doLottery() {
		int allValue = 0;
		for (int i = 0; i < lotteryProbs.length; i++) {
			allValue = allValue + lotteryProbs[i];
		}
		Random random = new Random();
		int randomNum = random.nextInt(allValue + 1);
		int nowValue = 0;
		for (int i = 0; i < lotteryProbs.length; i++) {
			if (nowValue < randomNum && randomNum <= nowValue + lotteryProbs[i]) {
				return lotteryNamesFtr[i];
			}
			nowValue = nowValue + lotteryProbs[i];
		}
		ActivitySubSystem.logger.error("[抽奖失败]");
		return null;
	}

	/**
	 * 处理协议请求
	 */
	public ACTIVENESS_LIST_RES getACTIVENESS_LIST_RES(Player player) {
		List<ActivityForActiveness> fit = ActivenessManager.getInstance().getOrderedActiveness(player);
		PlayerActivenessInfo playerActivenessInfo = player.getActivenessInfo();

		int[] doneNum = new int[fit.size()];
		int[] showDoneNum = new int[fit.size()];
		int[] maxNum = new int[fit.size()];
		String[] doneDes = new String[fit.size()];
		int[] showColor = new int[fit.size()];
		String[] goJoin = new String[fit.size()];

		for (int i = 0; i < doneDes.length; i++) {
			doneDes[i] = "";
		}
		for (int i = 0; i < showColor.length; i++) {
			showColor[i] = 0xffffff;
		}
		// 0x00ff00 绿色//0xff0000红色//0x888888灰色//0xffffff白色
		for (int i = 0; i < fit.size(); i++) {
			ActivityForActiveness afa = fit.get(i);
			maxNum[i] = afa.getTimesLimit();
			goJoin[i] = "";
			doneNum[i] = getDoneNum(playerActivenessInfo, afa);
			showDoneNum[i] = doneNum[i] > maxNum[i] ? maxNum[i] : doneNum[i];
			doneDes[i] += "(" + showDoneNum[i] + "/" + maxNum[i] + ")";

			/** 无坐标不能寻路 */
			if (afa.getStartX() == 0) {
				// showColor[i] = 0xff0000;
				goJoin[i] = Translate.无NPC不能寻路;
			}

			/** 等级不足不能寻路 */
			if (afa.getLevelLimit() > player.getLevel()) {
				// showColor[i] = 0xff0000;
				goJoin[i] = Translate.您的等级不足;
			}

			/** 体力值不足不能寻路 */
			if (afa.getTili() > 0 && afa.getTili() > player.getVitality()) {
				// showColor[i] = 0xff0000;
				goJoin[i] = Translate.体力值不足;
			}

			if ((afa.getType().getName().equals("答题") && missTime()[0]) || (afa.getType().getName().equals("武圣争夺战") && missTime()[1]) || (afa.getType().getName().equals("采花大盗") && missTime()[2])) {
				showColor[i] = 0x888888;
				goJoin[i] = Translate.不在时间段;
			}

			if (maxNum[i] <= doneNum[i] && maxNum[i] != 0) {
				showColor[i] = 0x00ff00;
			}
		}
		long now = System.currentTimeMillis();
		String serverName = GameConstants.getInstance().getServerName();
		Award award = findRightAward(now, serverName);
		if (award != null) {
			awardNameFtr = new String[award.getAwardLevel().length];
			for (int j = 0; j < awardLevel.length; j++) {
				awardLevel[j] = award.getAwardLevel()[j].intValue();
			}
			setAwardLevel(awardLevel);
			setAwardNameFtr(award.getAwardCNName());
		}
		String[] awardNames = getAwardNameFtr();
		long[] awardID = new long[awardNames.length];
		for (int i = 0; i < awardNames.length; i++) {
			Article a = ArticleManager.getInstance().getArticleByCNname(awardNames[i]);
			if (a != null) {
				ArticleEntity ae;
				try {
					ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
					if (ae != null) {
						awardID[i] = ae.getId();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		int[] needActiveness = getAwardLevel();
		sendBright(player);

		int[] caInfo = getCalenderInfo(System.currentTimeMillis());
		int signDays = getSignDays(player);
		String[] signAwardNames = getSignAwardNameFtr();
		long[] signAwardID = new long[signAwardNames.length];
		for (int i = 0; i < signAwardNames.length; i++) {
			Article a = ArticleManager.getInstance().getArticleByCNname(signAwardNames[i]);
			if (a != null) {
				ArticleEntity ae;
				try {
					ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
					if (ae != null) {
						signAwardID[i] = ae.getId();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		ActivitySubSystem.logger.warn(player.getLogString() + "签到信息" + Arrays.toString(playerActivenessInfo.getHasSign()));
		ACTIVENESS_LIST_RES res = new ACTIVENESS_LIST_RES(GameMessageFactory.nextSequnceNum(), fit.toArray(new ActivityForActiveness[0]), showColor, doneDes, goJoin, playerActivenessInfo, awardID, needActiveness, caInfo[0], caInfo[2], signDays, signAwardLevel, signAwardID, "");
		return res;
	}

	/**
	 * 每天首次登录弹窗,并加10活跃度
	 * @param player
	 */
	public void onFirstLogin(Player player) {
		long now = System.currentTimeMillis();
		if (ActivityManagers.getInstance().getDdc().get(player.getId() + "签到活跃度") == null) {
			ActivityManagers.getInstance().getDdc().put(player.getId() + "签到活跃度", 0l);
			ActivitySubSystem.logger.warn(player.getLogString() + "[首次重置签到活跃度]");
		}
		Long lastResetTime = (Long) ActivityManagers.getInstance().getDdc().get(player.getId() + "签到活跃度");
		if (!isSameDay(lastResetTime, now)) {
			resetActivenessInfo(player);
			record(player, ActivenessType.每日登陆);
			if (openPop) {
//				ACTIVENESS_LIST_RES res = getACTIVENESS_LIST_RES(player);
//				player.addMessageToRightBag(res);
//				ActivityManager.getInstance().handPlayerEnter(player, 0);
				ActivitySubSystem.logger.info(player.getLogString() + "[活跃度登录弹窗]");
			}
		}
	}

	/** 重置单日活跃度,完成次数,领奖和抽奖信息 */
	public void resetActivenessInfo(Player player) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		PlayerActivenessInfo pai = player.getActivenessInfo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (pai != null) {
			try {
				if (ActivityManagers.getInstance().getDdc().get(player.getId() + "签到活跃度") == null) {
					ActivityManagers.getInstance().getDdc().put(player.getId() + "签到活跃度", 0l);
					ActivitySubSystem.logger.warn(player.getLogString() + "[首次重置签到活跃度]");
				}
				Long lastResetTime = (Long) ActivityManagers.getInstance().getDdc().get(player.getId() + "签到活跃度");
				if (!isSameMonth(lastResetTime, now)) {
					int[] caInfo = getCalenderInfo(now);
					pai.setHasSign(new boolean[caInfo[1]]); // 因为每月的天数可能不同，所以跨月的时候重置一下这个字段
					ActivitySubSystem.logger.warn(player.getLogString() + "[重置月签到信息成功] [上次登录：" + sdf.format(player.getPlayerLastLoginTime()) + "]");
				}
				if (!isSameMonth(player.getActivenessInfo().getLastGetSignTime(), now)) {
					boolean[] hasGotSign = new boolean[signAwardLevel.length];
					pai.setHasGotSign(hasGotSign);
					ActivitySubSystem.logger.warn(player.getLogString() + "[重置签到领奖信息成功] [上次领奖：" + sdf.format(player.getActivenessInfo().getLastGetSignTime()) + "]");
				}
				if (!isSameDay(lastResetTime, now)) {
					Map<Integer, Integer> doneNum = player.getActivenessInfo().getDoneNum();
					Set<Integer> cids = doneNum.keySet();
					Iterator<Integer> iter = cids.iterator();
					while (iter.hasNext()) {
						int id = iter.next();
						doneNum.put(id, 0);
					}
					pai.setDoneNum(doneNum);
					pai.setDayActiveness(0);
					ActivitySubSystem.logger.warn(player.getLogString() + "[重置单日活跃度成功] [重置活动完成次数成功] [上次登录：" + sdf.format(player.getPlayerLastLoginTime()) + "]");
				}
				if (!isSameDay(player.getActivenessInfo().getLastGetTime(), now)) {
					boolean[] gotten = new boolean[awardLevel.length];
					pai.setGotten(gotten);
					ActivityManagers.getInstance().changeActivityNums(player);
					ActivitySubSystem.logger.warn(player.getLogString() + "[重置领奖信息成功] [上次领奖：" + sdf.format(player.getActivenessInfo().getLastGetTime()) + "] [重置后领奖记录：" + Arrays.toString(pai.getGotten()) + "]");
				}
				if (!isSameDay(player.getActivenessInfo().getLastLotteryTime(), now)) {
					pai.setHasLottery(0);
					pai.setCanLottery(0);
					ActivitySubSystem.logger.warn(player.getLogString() + "[重置活跃度抽奖信息成功] [上次抽奖：" + sdf.format(player.getActivenessInfo().getLastLotteryTime()) + "] [更新后已抽奖次数/可抽奖次数：" + pai.getHasLottery() + "/" + pai.getCanLottery() + "]");
				}

				ActivityManagers.getInstance().getDdc().put(player.getId() + "签到活跃度", System.currentTimeMillis());

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ActivitySubSystem.logger.warn(player.getLogString() + "[重置活跃度参数失败]");
		}
	}

	/**
	 * 判断两个类型为long的时间是否为同一天
	 * @param time1
	 * @param time2
	 * @return boolean
	 */
	public boolean isSameDay(long time1, long time2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str1 = sdf.format(time1);
		String str2 = sdf.format(time2);
		return str1.equals(str2);
	}

	/**
	 * 判断两个类型为long的时间是否为同一月
	 * @param time1
	 * @param time2
	 * @return boolean
	 */
	public boolean isSameMonth(long time1, long time2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String str1 = sdf.format(time1);
		String str2 = sdf.format(time2);
		return str1.equals(str2);
	}

	/**
	 * 获得某条活跃度活动的完成次数
	 * @param pai
	 * @param afa
	 * @return
	 * @throws Exception
	 */
	public int getDoneNum(PlayerActivenessInfo pai, ActivityForActiveness afa) {
		if (pai != null) {
			if (afa != null) {
				Map<Integer, Integer> doneNums = pai.getDoneNum();
				if (doneNums != null && doneNums.get(afa.getId()) != null) {
					int doneNum = doneNums.get(afa.getId());
					return doneNum;
				} else {
					ActivitySubSystem.logger.warn("doneNums is null");
				}
			} else {
				ActivitySubSystem.logger.warn("ActivityForActiveness is null");
			}
		} else {
			ActivitySubSystem.logger.warn("PlayerActivenessInfo is null");
		}
		return 0;
	}

	public int getSignDays(Player player) {
		PlayerActivenessInfo pai = player.getActivenessInfo();
		if (pai != null) {
			boolean[] hasSign = pai.getHasSign();
			if (hasSign != null) {
				int signDays = 0;
				for (boolean sign : hasSign) {
					if (sign) {
						signDays++;
					}
				}
				return signDays;
			} else {
				ActivitySubSystem.logger.warn(player.getLogString() + "[玩家签到信息不存在]");
			}
		} else {
			ActivitySubSystem.logger.warn(player.getLogString() + "[玩家的活跃度信息不存在]");
		}
		return 0;
	}

	/**
	 * 宠物放生活跃度统计
	 * @param evt
	 */
	public void playerDropPet(Player p, Pet pet) {
		if (pet.getRarity() == PetManager.千载难逢) {
			ActivenessManager.getInstance().record(p, ActivenessType.放生千载难逢宝宝);
		}
	}

	/**
	 * 宠物进阶活跃度统计
	 * @param p
	 */
	public void petJinJie(Player p) {
		ActivenessManager.getInstance().record(p, ActivenessType.宠物进阶);
	}

	// 活跃度统计
	public void playerEnterScene(Player p, Game game) {
		// if (game.getGameInfo().displayName.matches("福地洞天.*层")) {
		// ActivenessManager.getInstance().record(p, ActivenessType.福地洞天);
		// }
		// if (game.getGameInfo().displayName.equals("宠物岛")) {
		// ActivenessManager.getInstance().record(p, ActivenessType.灵兽岛);
		// }
		// if (game.getGameInfo().displayName.matches(".*圣兽阁")) {
		// ActivenessManager.getInstance().record(p, ActivenessType.圣兽阁);
		// }
		if (game.gi.name.matches("fudidongtian.*ceng")) {
			ActivenessManager.getInstance().record(p, ActivenessType.福地洞天);
		}
		if (game.gi.name.equals("shengshoumicheng")) {
			ActivenessManager.getInstance().record(p, ActivenessType.灵兽岛);
		}
		if (game.gi.name.matches(".*shengshouge")) {
			ActivenessManager.getInstance().record(p, ActivenessType.圣兽阁);
		}
	}

	public void init() throws Exception {
		
		long start = System.currentTimeMillis();
		load();
		conf = ActivenessConfig.instance;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(PlayerActivenessInfo.class);
		instance = this;
		doReg();
		ServiceStartRecord.startLog(this);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public MemoryNPCManager getNpcManager() {
		return npcManager;
	}

	public void setNpcManager(MemoryNPCManager npcManager) {
		this.npcManager = npcManager;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public List<PlayerActivenessInfo> getPlayerActivenessInfos() {
		return playerActivenessInfos;
	}

	public void setPlayerActivenessInfo(List<PlayerActivenessInfo> playerActivenessInfos) {
		this.playerActivenessInfos = playerActivenessInfos;
	}

	public int getDayActivenessLimit() {
		return dayActivenessLimit;
	}

	public void setDayActivenessLimit(int dayActivenessLimit) {
		this.dayActivenessLimit = dayActivenessLimit;
	}

	public int[] getAwardLevel() {
		return awardLevel;
	}

	public void setAwardLevel(int[] awardLevel) {
		this.awardLevel = awardLevel;
	}

	public String[] getAwardName() {
		return awardName;
	}

	public void setAwardName(String[] awardName) {
		this.awardName = awardName;
	}

	public String[] getLotteryNames() {
		return lotteryNames;
	}

	public void setLotteryNames(String[] lotteryNames) {
		this.lotteryNames = lotteryNames;
	}

	public Integer[] getLotteryNums() {
		return lotteryNums;
	}

	public void setLotteryNums(Integer[] lotteryNums) {
		this.lotteryNums = lotteryNums;
	}

	public Integer[] getLotteryProbs() {
		return lotteryProbs;
	}

	public void setLotteryProbs(Integer[] lotteryProbs) {
		this.lotteryProbs = lotteryProbs;
	}

	public void setPlayerActivenessInfos(List<PlayerActivenessInfo> playerActivenessInfos) {
		this.playerActivenessInfos = playerActivenessInfos;
	}

	public String[] getAwardNameFtr() {
		return awardNameFtr;
	}

	public void setAwardNameFtr(String[] awardNameFtr) {
		this.awardNameFtr = awardNameFtr;
	}

	public String[] getLotteryNamesFtr() {
		return lotteryNamesFtr;
	}

	public void setLotteryNamesFtr(String[] lotteryNamesFtr) {
		this.lotteryNamesFtr = lotteryNamesFtr;
	}

	public Integer[] getLotteryColors() {
		return lotteryColors;
	}

	public void setLotteryColors(Integer[] lotteryColors) {
		this.lotteryColors = lotteryColors;
	}

	public int[] getLotteryLevel() {
		return lotteryLevel;
	}

	public void setLotteryLevel(int[] lotteryLevel) {
		this.lotteryLevel = lotteryLevel;
	}

	public int[] getLotteryTimes() {
		return lotteryTimes;
	}

	public void setLotteryTimes(int[] lotteryTimes) {
		this.lotteryTimes = lotteryTimes;
	}

	public int[] getSignAwardLevel() {
		return signAwardLevel;
	}

	public void setSignAwardLevel(int[] signAwardLevel) {
		this.signAwardLevel = signAwardLevel;
	}

	public String[] getSignAwardName() {
		return signAwardName;
	}

	public void setSignAwardName(String[] signAwardName) {
		this.signAwardName = signAwardName;
	}

	public String[] getSignAwardNameFtr() {
		return signAwardNameFtr;
	}

	public void setSignAwardNameFtr(String[] signAwardNameFtr) {
		this.signAwardNameFtr = signAwardNameFtr;
	}

	public boolean isOpenPop() {
		return openPop;
	}

	public void setOpenPop(boolean openPop) {
		this.openPop = openPop;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

	public List<Lottery> getLotteries() {
		return lotteries;
	}

	public void setLotteries(List<Lottery> lotteries) {
		this.lotteries = lotteries;
	}

	@Override
	public String getMConsoleDescription() {
		return "活跃度相关参数";
	}

	@Override
	public String getMConsoleName() {
		return "ActivenessManager";
	}

	@Override
	public void doReg() {
		EventRouter.register(Event.SERVER_DAY_CHANGE, this);
		EventRouter.register(Event.PLAYER_LOGIN, this);
		EventRouter.register(Event.PLAYER_DROP_PET, this);
		EventRouter.register(Event.PET_JIN_JIE, this);
		EventRouter.register(Event.PLAYER_ENTER_SCENE, this);

	}

	@Override
	public void proc(Event evt) {
		switch (evt.id) {
		case Event.SERVER_DAY_CHANGE:
			// TODO 根据时间重置领奖数组
			resetData();
			break;
		case Event.PLAYER_LOGIN: {
			Player p = ((EventPlayerLogin) evt).player;
			sendBright(p);
			onFirstLogin(p);
			break;
		}
		case Event.PLAYER_DROP_PET: {
			EventPlayerDropPet evtWithObj = (EventPlayerDropPet) evt;
			Player player = evtWithObj.player;
			Pet pet = evtWithObj.pet;
			playerDropPet(player, pet);
			break;
		}
		case Event.PET_JIN_JIE: {
			EventWithObjParam evtWithObj = (EventWithObjParam) evt;
			Object[] obj = (Object[]) evtWithObj.param;
			Player p = (Player) obj[0];
			Pet pet = (Pet) obj[1];
			petJinJie(p, pet);
			break;
		}
		case Event.PLAYER_ENTER_SCENE: {
			EventPlayerEnterScene epes = (EventPlayerEnterScene) evt;
			Player p = epes.player;
			Game game = epes.game;
			playerEnterScene(p, game);
			sendBright(p);
			break;
		}
		}
	}

	private void petJinJie(Player player, Pet pet) {
		// 活跃度统计
		ActivenessManager.getInstance().record(player, ActivenessType.宠物进阶);
	}

	private void resetData() {
		Player[] onlinePlayers = GamePlayerManager.getInstance().getOnlinePlayers();
		for (Player p : onlinePlayers) {
			resetActivenessInfo(p);
			record(p, ActivenessType.每日登陆);
		}

	}

	// public static void main(String[] args) {
	// ActivenessManager am = new ActivenessManager();
	// am.setFilePath("D:\\code\\mieshi_server\\game_mieshi_server\\conf\\game_init_config\\activity\\activeness.xls");
	// am.isTest = true;
	// try {
	// am.load();
	// System.out.println(Arrays.toString(am.getLotteryLevel())+"---"+Arrays.toString(am.getLotteryTimes()));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//		 
	// }
}

class Award {
	private long startTime;
	private long endTime;
	private String platforms;
	private String openServers;
	private String limitServers;
	private Integer[] awardLevel;
	private String[] awardCNName;

	public Award(long startTime, long endTime, String platforms, String openServers, String limitServers, Integer[] awardLevel, String[] awardCNName) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.openServers = openServers;
		this.limitServers = limitServers;
		this.awardLevel = awardLevel;
		this.awardCNName = awardCNName;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getPlatforms() {
		return platforms;
	}

	public void setPlatforms(String platforms) {
		this.platforms = platforms;
	}

	public String getOpenServers() {
		return openServers;
	}

	public void setOpenServers(String openServers) {
		this.openServers = openServers;
	}

	public String getLimitServers() {
		return limitServers;
	}

	public void setLimitServers(String limitServers) {
		this.limitServers = limitServers;
	}

	public Integer[] getAwardLevel() {
		return awardLevel;
	}

	public void setAwardLevel(Integer[] awardLevel) {
		this.awardLevel = awardLevel;
	}

	public String[] getAwardCNName() {
		return awardCNName;
	}

	public void setAwardCNName(String[] awardCNName) {
		this.awardCNName = awardCNName;
	}

}

class Lottery {
	private long startTime;
	private long endTime;
	private String platforms;
	private String openServers;
	private String limitServers;
	private String[] lotteryCNNames;
	private Integer[] lotteryNums;
	private Integer[] lotteryColors;
	private Integer[] lotteryProbs;

	public Lottery(long startTime, long endTime, String platforms, String openServers, String limitServers, String[] lotteryCNNames, Integer[] lotteryNums, Integer[] lotteryColors, Integer[] lotteryProbs) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.openServers = openServers;
		this.limitServers = limitServers;
		this.lotteryCNNames = lotteryCNNames;
		this.lotteryNums = lotteryNums;
		this.lotteryColors = lotteryColors;
		this.lotteryProbs = lotteryProbs;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getPlatforms() {
		return platforms;
	}

	public void setPlatforms(String platforms) {
		this.platforms = platforms;
	}

	public String getOpenServers() {
		return openServers;
	}

	public void setOpenServers(String openServers) {
		this.openServers = openServers;
	}

	public String getLimitServers() {
		return limitServers;
	}

	public void setLimitServers(String limitServers) {
		this.limitServers = limitServers;
	}

	public String[] getLotteryCNNames() {
		return lotteryCNNames;
	}

	public void setLotteryCNNames(String[] lotteryCNNames) {
		this.lotteryCNNames = lotteryCNNames;
	}

	public Integer[] getLotteryNums() {
		return lotteryNums;
	}

	public void setLotteryNums(Integer[] lotteryNums) {
		this.lotteryNums = lotteryNums;
	}

	public Integer[] getLotteryColors() {
		return lotteryColors;
	}

	public void setLotteryColors(Integer[] lotteryColors) {
		this.lotteryColors = lotteryColors;
	}

	public Integer[] getLotteryProbs() {
		return lotteryProbs;
	}

	public void setLotteryProbs(Integer[] lotteryProbs) {
		this.lotteryProbs = lotteryProbs;
	}
}
