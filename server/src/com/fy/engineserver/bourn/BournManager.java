package com.fy.engineserver.bourn;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.CheckAttribute;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyEntityManager;
import com.fy.engineserver.activity.fairyRobbery.instance.FairyRobberyEntity;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.auction.service.AuctionManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.seal.data.Seal;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.RandomTool.RandomType;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.YinZiKuCunFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerMysqlImpl;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl;

/**
 * 境界manager
 * 
 */
@CheckAttribute("境界manager")
public class BournManager implements TaskConfig {
	// 元宝刷新任务,所需VIP等级
	public final static int SILVER_REFTASK_VIP = 4;
	public static Logger logger = LoggerFactory.getLogger(BournManager.class);
	public static DecimalFormat df = new DecimalFormat("##.00%");
	@CheckAttribute("最大任务星级")
	public static int maxStar = 10;
	@CheckAttribute("最大境界等级")
	public static int maxBournLevel = 15;
	@CheckAttribute("打坐要求的境界等级")
	public static int minZezenBournLevel = 1;
	@CheckAttribute("用经验刷新任务")
	public static int refreshUseExp = 0;
	@CheckAttribute("用道具刷新任务")
	public static int refreshUseMoney = 1;
	@CheckAttribute("元宝刷新任务消耗")
	public static int coinCostOfRefresh;
	@CheckAttribute("打坐获得经验的周期")
	public static int zazenCycle;
	@CheckAttribute("每天增加的打坐时间")
	public static long zazenTimeDailyIncrease;
	@CheckAttribute("普通用户可累计的最大打坐时间")
	public static long maxZazenTimeOfCommonUser;
	@CheckAttribute("VIP用户可累计的最大打坐时间")
	public static Long[] maxZazenTimeOfVIPUser;
	@CheckAttribute("用户每天日常任务获得数")
	public static int taskDailyIncrease;
	@CheckAttribute("普通用户最大任务数量")
	public static int maxTaskNumOfCommonUser;
	@CheckAttribute("VIP用户最大任务数量")
	public static Integer[] maxTaskNumOfVIPUser;
	@CheckAttribute("每个境界的各个星级任务 ")
	public static HashMap<Short, Task[][]> starTaskMap = new HashMap<Short, Task[][]>();
	@CheckAttribute("境界配置信息 ")
	private BournCfg[] bournCfgs = new BournCfg[maxBournLevel];
	@CheckAttribute("每个级别的描述信息")
	public static String[] bournDes = Translate.text_bourn_012;
	@CheckAttribute("除杂的描述")
	public static String purifyDes = Translate.text_bourn_011;
	@CheckAttribute("开始能打坐境界等级")
	public static int zazenLevel = 1;
	@CheckAttribute("开始能做日常的境界等级")
	public static int dailyTaskLevel = 2;
	@CheckAttribute("经验刷新任务 几率")
	public static double[] expRefreshRate;
	@CheckAttribute("元宝刷新任务 几率")
	public static double[] moneyRefreshRate;
	@CheckAttribute("默认刷新几率")
	public static double[] defaultRate;

	private static Random RANDOM = new Random();

	private TaskManager taskManager;

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	// 资源路径
	private String resoursePath = "";

	private static BournManager instance;

	private static final String LOCK = "LOCK";

	private BournManager() {

	}

	public static BournManager getInstance() {
		if (instance == null) {
			synchronized (LOCK) {
				if (instance == null) {
					instance = new BournManager();
				}
			}
		}
		return instance;
	}

	public String getResoursePath() {
		return resoursePath;
	}

	public void setResoursePath(String resoursePath) {
		this.resoursePath = resoursePath;
	}

	public BournCfg[] getBournCfgs() {
		return bournCfgs;
	}

	public void setBournCfgs(BournCfg[] bournCfgs) {
		this.bournCfgs = bournCfgs;
	}

	public void loadFile() throws Exception {
		Workbook workbook = null;
		try {
			File file = new File(getResoursePath());
			if (file.exists()) {
				workbook = Workbook.getWorkbook(file);
				/** 境界经验配置 */
				Sheet sheet = workbook.getSheet(0);
				int maxRow = sheet.getRows();
				List<BournCfg> bournCfgs = new ArrayList<BournCfg>();
				for (int i = 1; i < maxRow; i++) {
					Cell[] cells = sheet.getRow(i);
					int index = 0;
					int grade = Integer.valueOf(cells[index++].getContents());
					int exp = Integer.valueOf(cells[index++].getContents());
					long startTask = Long.valueOf(cells[index++].getContents());
					long endTask = Long.valueOf(cells[index++].getContents());
					int playerMinLevel = Integer.valueOf(cells[index++].getContents());
					int zazenOnceExp = Integer.valueOf(cells[index++].getContents());
					long refreshTaskExpCost = Long.valueOf(cells[index++].getContents());

					BournCfg bournCfg = new BournCfg(grade, exp, startTask, endTask, playerMinLevel, zazenOnceExp, refreshTaskExpCost);
					bournCfgs.add(bournCfg);
				}
				setBournCfgs(bournCfgs.toArray(new BournCfg[0]));

				/** 境界任务配置 */
				sheet = workbook.getSheet(1);

				int maxRows = sheet.getRows();// 最大行数了
				int columns = sheet.getColumns();// 列数量

				int columnIndex = 0;
				Cell[] cells = sheet.getColumn(++columnIndex);
				defaultRate = new double[maxRows - 1];
				for (int i = 1; i < maxRows; i++) {
					defaultRate[i - 1] = (df.parse(StringTool.modifyContent(cells[i]))).doubleValue();
				}
				cells = sheet.getColumn(++columnIndex);
				expRefreshRate = new double[maxRows - 1];
				for (int i = 1; i < maxRows; i++) {
					expRefreshRate[i - 1] = (df.parse(StringTool.modifyContent(cells[i]))).doubleValue();
				}
				cells = sheet.getColumn(++columnIndex);
				moneyRefreshRate = new double[maxRows - 1];
				for (int i = 1; i < maxRows; i++) {
					moneyRefreshRate[i - 1] = (df.parse(StringTool.modifyContent(cells[i]))).doubleValue();
				}
				/** 各个星级任务列表 */

				short tempBournLevel = 2;// 从第二级开始才有刷新任务
				for (int i = ++columnIndex; i < columns; i++) {
					short currentLevel = tempBournLevel++;
					starTaskMap.put(currentLevel, new Task[maxRows - 1][]);
					cells = sheet.getColumn(i);
					for (int j = 1; j < maxRows; j++) {
						Long[] taskIds = StringTool.string2Array(StringTool.modifyContent(cells[j]), ",", Long.class);
						Task[] tasks = new Task[taskIds.length];
						for (int m = 0; m < tasks.length; m++) {
							tasks[m] = taskManager.getTask(taskIds[m]);
						}
						starTaskMap.get(currentLevel)[j - 1] = tasks;
					}
				}

				/** 杂碎配置 */
				sheet = workbook.getSheet(2);
				cells = sheet.getRow(1);
				int index = 0;
				zazenCycle = Integer.valueOf(cells[index++].getContents()) * 1000;
				zazenTimeDailyIncrease = Long.valueOf(cells[index++].getContents()) * 1000 * 60;
				maxZazenTimeOfCommonUser = Long.valueOf(cells[index++].getContents()) * 1000 * 60;
				maxZazenTimeOfVIPUser = StringTool.string2Array(cells[index++].getContents(), ",", Long.class);
				taskDailyIncrease = Integer.valueOf(cells[index++].getContents());
				maxTaskNumOfCommonUser = Integer.valueOf(cells[index++].getContents());
				maxTaskNumOfVIPUser = StringTool.string2Array(cells[index++].getContents(), ",", Integer.class);
				coinCostOfRefresh = Integer.valueOf(cells[index++].getContents());
				for (int i = 0; i < maxZazenTimeOfVIPUser.length; i++) {
					maxZazenTimeOfVIPUser[i] = maxZazenTimeOfVIPUser[i] * 1000 * 60;
				}
			} else {
				logger.error("文件不存在:" + getResoursePath());
			}
		} catch (Exception e) {
			logger.error("[加载境界]", e);
			throw e;
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
	}

	/**
	 * 升级<BR/>
	 * 1.到达最高级别了<BR/>
	 * 2.没找到配置,只做记录,不提示给玩家<BR/>
	 * 3.经验不足<BR/>
	 * 4.等级不足<BR/>
	 * 5.必要任务未完成<BR/>
	 * 6.下一级配置未找到<BR/>
	 * @param player
	 * @return
	 */
	public CompoundReturn bournlevelUp(Player player) {
		int currBournLevel = player.getClassLevel();

		if (currBournLevel >= maxBournLevel) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}

		BournCfg bournCfg = getBournCfg(currBournLevel);
		BournCfg nextBournCfg = getBournCfg(currBournLevel + 1);
		if (nextBournCfg == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
		}
		if (bournCfg == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		if (player.getBournExp() < bournCfg.getExp()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		if (nextBournCfg.getPlayerMinLevel() > player.getLevel()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
		}

		Task task = taskManager.getTask(bournCfg.getEndTask());
		if (task == null || player.getTaskStatus(task) != TASK_STATUS_DEILVER) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
		}
		if (currBournLevel >= 11) {
			FairyRobberyEntity entity = FairyRobberyEntityManager.inst.getEntity(player);
			if (entity == null || entity.getPassLevel() <= (currBournLevel - 11)) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(7);
			}
		}
		// 满足升级条件，执行升级操作

		player.setClassLevel((short) (player.getClassLevel() + 1));
		player.setBournExp(0);

		if (player.getClassLevel() == BournManager.zazenLevel) {
			player.setLeftZazenTime(zazenTimeDailyIncrease);
		}
		if (player.getClassLevel() == BournManager.dailyTaskLevel) {
			player.setMaxBournTaskNum(taskDailyIncrease);
		}
		// 世界广播
		// 恭喜XXX境界提升到了XXX

		// Translate.text_bourn_001
		// 给玩家身上自动加入下一阶段境界的任务
		if (nextBournCfg != null) {
			Task nextTask = TaskManager.getInstance().getTask(nextBournCfg.getStartTask());
			if (nextTask != null) {
				if (player.getTaskStatus(nextTask) > 0) {
					logger.error("[玩家已经接取过任务了]" + player.getLogString() + "[任务 :{}]", new Object[] { task.getId() });
				} else {
					CompoundReturn cr = player.addTaskByServer(nextTask);
					if (!cr.getBooleanValue()) {
						logger.error(player.getLogString() + "[境界务角色不满足接取条件:{}]", new Object[] { TaskSubSystem.getInstance().getInfo(cr.getIntValue()) });
					}
				}
			} else {
				logger.error(player.getLogString() + "[境界起始任务不存在:{}]", new Object[] { player.getClassLevel() });
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 开始打坐<BR/>
	 * 1.境界不足，暂时不能打坐 <BR/>
	 * 2.没有可用的打坐时间<BR/>
	 * @return
	 */
	public CompoundReturn beginZezen(Player player) {
		if (player.getClassLevel() < minZezenBournLevel) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		if (player.getLeftZazenTime() <= 0) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		player.setZazening(true);
		player.setLastGotBournExpTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 结束打坐<BR/>
	 * 1.没有在打坐,不给用户返回<BR/>
	 * @return
	 */
	public CompoundReturn endZezen(Player player) {
		if (!player.isZazening()) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		player.setZazening(false);
		return CompoundReturn.createCompoundReturn().setBooleanValue(true);
	}

	/**
	 * 刷新任务<BR/>
	 * 1.没找到配置,不提示<BR/>
	 * 2.当前经验不足<BR/>
	 * 3.任务达到最大数量了,不能再刷新了<BR/>
	 * 4.元宝不足以支付<BR/>
	 * 5.扣钱异常<BR/>
	 * 6.星级满了,不能刷了<BR/>
	 * 7.VIP等级不足,不能用元宝刷新<BR/>
	 * 8.VIP等级不足,只能刷到6星<BR/>
	 * @param player
	 * @param refreshType
	 * @return
	 */
	public CompoundReturn refreshTask(Player player, int refreshType) {

		BournCfg bournCfg = getBournCfg(player.getClassLevel());
		CompoundReturn cr = CompoundReturn.createCompoundReturn();
		if (bournCfg == null) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		if (player.getDeliverBournTaskNum() >= getMaxBournTaskNum(player)) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getLogString() + "[角色刷新境界任务] [任务均已做完,不能刷新了]");
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(3);
		}
		if (player.getCurrBournTaskStar() >= 9) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(6);
		}
		if (player.getVipLevel() <= 2 && player.getCurrBournTaskStar() >= 5) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(8);
		}
		long cost = 0;
		if (refreshType == refreshUseExp) {
			long playerCurrExp = player.getExp();
			long refreshNeedExp = getBournCfg(player.getClassLevel()).getRefreshTaskExpCost();
			refreshNeedExp = refreshNeedExp <= 0 ? 1 : refreshNeedExp;
			if (refreshNeedExp > playerCurrExp) {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getLogString() + "[角色刷新境界任务] [当前经验不足]");
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
			}
			// 判断通过 扣经验 刷新任务
			cr = doRefreshTask(player, refreshType, refreshNeedExp);
			player.subExp(refreshNeedExp, "刷新任务");
			cost = refreshNeedExp;
		} else if (refreshType == refreshUseMoney) {
			if (player.getVipLevel() < SILVER_REFTASK_VIP) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(7);
			}
			if (player.getSilver() + player.getShopSilver() >= coinCostOfRefresh) { // 元宝刷新,扣银子(非绑银)
				try {
					BillingCenter.getInstance().playerExpense(player, coinCostOfRefresh, CurrencyType.SHOPYINZI, ExpenseReasonType.BOURN_REF_TASK, "");
				} catch (Exception e) {
					logger.error(player.getLogString() + "[元宝刷新境界任务异常]", e);
					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(5);
				}
			} else {
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(4);
			}
			cr = doRefreshTask(player, refreshType, coinCostOfRefresh);
			cost = coinCostOfRefresh;
		}
		if (cr != null) {
			player.setCurrBournTaskName(((Task) cr.getObjValue()).getName());
			player.setCurrBournTaskStar(cr.getIntValue());
			String notice = "";
			if (refreshType == refreshUseMoney) {
				notice = Translate.translateString(Translate.text_bourn_018, new String[][] { { Translate.COUNT_1, String.valueOf(1 + player.getCurrBournTaskStar()) }, { Translate.STRING_1, BillingCenter.得到带单位的银两(cost) } });
			} else if (refreshType == refreshUseExp) {
				notice = Translate.translateString(Translate.text_bourn_019, new String[][] { { Translate.COUNT_1, String.valueOf(1 + player.getCurrBournTaskStar()) }, { Translate.STRING_1, String.valueOf(cost) } });
			}
			player.sendError(notice);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getLogString() + "[刷新境界任务] [成功] " + notice);
			}
		}
		return cr.setBooleanValue(true);
	}

	/**
	 * 真实的执行刷新 返回刷新结果 将星级及其Task返回
	 * @param player
	 * @param refreshType
	 * @return
	 */
	public CompoundReturn doRefreshTask(Player player, int refreshType, long cost) {
		int star = player.getCurrBournTaskStar();
		int resStar = star;
		Task task = null;
		if (player.getCurrBournTaskName() == null || "".equals(player.getCurrBournTaskName())) {
			// 第一次刷新
			List<Integer> resultIndexs = RandomTool.getResultIndexs(RandomType.groupRandom, defaultRate, 1);
			star = resultIndexs.get(0);
			task = starTaskMap.get(player.getClassLevel())[star][new Random().nextInt(starTaskMap.get(player.getClassLevel())[star].length)];

			if (logger.isWarnEnabled()) {
				logger.warn(player.getLogString() + "[第一次刷新任务][星级{}][任务{}]", new Object[] { star, task });
			}
		} else {
			int currentBournTaskStar = player.getCurrBournTaskStar();
			resStar = currentBournTaskStar;// 结果星级
			if (refreshType == refreshUseExp) {
				// 经验刷新
				// 经验刷新一次不升则降,满级后不可刷新
				double rate = expRefreshRate[currentBournTaskStar];
				double result = RANDOM.nextDouble();
				if (rate >= result) {// 成功,升星
					resStar++;
					if (logger.isWarnEnabled()) {
						logger.warn(player.getLogString() + "[经验] [刷新成功] [星级:{}] [rate:{}] [result:{}] [消耗:{}]", new Object[] { resStar, rate, result, cost });
					}
				} else {// 失败,降星
					if (resStar <= 0) {
						resStar = 0;
					} else {
						resStar--;
					}
					if (logger.isWarnEnabled()) {
						logger.warn(player.getLogString() + "[经验] [刷新失败] [星级:{}] [rate:{}] [result:{}]", new Object[] { resStar, rate, result });
					}
				}
				// 刷新任务
				// List<Integer> resultIndexs = RandomTool.getResultIndexs(RandomType.groupRandom, expRefreshRate, 1);
				// star = resultIndexs.get(0);
				// task = starTaskMap.get(player.getClassLevel())[star][new Random().nextInt(starTaskMap.get(player.getClassLevel())[star].length)];
			} else {// 元宝刷新,不会下降
				double rate = moneyRefreshRate[currentBournTaskStar];
				double result = RANDOM.nextDouble();
				if (rate >= result) {// 成功,升星
					resStar++;
					if (logger.isWarnEnabled()) {
						logger.warn(player.getLogString() + "[元宝] [刷新成功] [星级:{}] [rate:{}] [result:{}] [消耗:{}]", new Object[] { resStar, rate, result, cost });
					}
				} else {// 失败,不降星
					// DO NOTHING
					if (logger.isWarnEnabled()) {
						logger.warn(player.getLogString() + "[元宝] [刷新失败] [星级:{}] [rate:{}] [result:{}]", new Object[] { resStar, rate, result });
					}
				}
			}
			task = starTaskMap.get(player.getClassLevel())[resStar][new Random().nextInt(starTaskMap.get(player.getClassLevel())[resStar].length)];

			if (logger.isWarnEnabled()) {
				logger.warn(player.getLogString() + "[非第一次刷新任务] [星级{}] [任务{}]", new Object[] { star, task });
			}
		}
		return CompoundReturn.createCompoundReturn().setIntValue(resStar).setObjValue(task);
	}

	public int getMaxBournTaskNum(Player player) {
		return player.getMaxBournTaskNum();
	}

	/**
	 * 得到某级别的境界配置
	 * @param boundLevel
	 * @return
	 */
	public BournCfg getBournCfg(int boundLevel) {
		for (BournCfg cfg : getBournCfgs()) {
			if (cfg.getGrade() == boundLevel) {
				return cfg;
			}
		}
		return null;
	}

	public void init() throws Exception {
		
		loadFile();
		initTimer();
		instance = this;
		ServiceStartRecord.startLog(this);
	}
	
	long vipleves[] = new long[]{0*100l,10*100l,70*100l,160*100l,300*100l,600*100l,1200*100l,2500*100l,7000*100l,14000*100l,30000*100l,100000*100l,200000*100l,500000*100l,800000*100l,1000000*100l};
	public long getRMB(int vipLevel)
	{
		return vipleves[vipLevel];
	}
	
	public void sendStatPoints4VipLevel(int vipLevel) throws Exception
	{
		if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			return;
		}
		long st = System.currentTimeMillis();
		long chargePointsSum = 0l;
		long playercount = 0l;
		int pointstype = 0;
		int maxLevel = 70;
		SealManager sealManager = SealManager.getInstance();
		int[] sealLevels = SealManager.SEAL_LEVELS;
		for(int k = 0; k < sealLevels.length;k++)
		{
			Seal seal = sealManager.sealMap.get(sealLevels[k]);
			
			if(seal != null)
			{
				if(seal.getSealCanOpenTime() > System.currentTimeMillis())
				{
					if(k > 0)
					{
						maxLevel = sealLevels[k-1];
					}
				}
			}
		}
		
		String sql = "select count(*),nvl(sum(chargePoints),0) from Player where RMB >= ? and RMB < ?";
		Connection  conn = ((SimpleEntityManagerOracleImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
	 	PreparedStatement  pstmt = conn.prepareStatement(sql);
	 	
	 	//韩服这里rmb必须除以440 这是韩币
	 	if(vipLevel < vipleves[vipleves.length-1])
	 	{
	 		pstmt.setLong(1, vipleves[vipLevel]);
	 		pstmt.setLong(2, vipleves[vipLevel+1]);
	 	}
	 	else
	 	{
	 		sql = "select count(*),nvl(sum(chargePoints),0) from Player where RMB >= ?";
		 	pstmt = conn.prepareStatement(sql);
		 	pstmt.setLong(1, vipleves[vipLevel]);
	 	}
	 	
		ResultSet rs = pstmt.executeQuery();
		if(rs != null)
		{
			if(rs.next())
			{
				playercount = rs.getLong(1);
				chargePointsSum = rs.getLong(2);
				pointstype++;
				YinZiKuCunFlow yinZiKuCunFlow=new YinZiKuCunFlow();
				yinZiKuCunFlow.setCount(chargePointsSum);    //剩余银子数量（单位：文）
				yinZiKuCunFlow.setColumn1(pointstype+"");
				yinZiKuCunFlow.setColumn2("2");
				yinZiKuCunFlow.setColumn3(maxLevel+"");
				yinZiKuCunFlow.setColumn4(vipLevel+"");
				yinZiKuCunFlow.setColumn5(playercount+"");
				yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
				yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
				if (!TestServerConfigManager.isTestServer()) {
					StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
				}

				logger.warn("[获取当前根据vip统计服务器总积分] [成功] [积分:"+chargePointsSum+"] [type:"+pointstype+"] ["+vipLevel+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]<br/>");
			}

			rs.close();
		}




		if(pstmt != null)
		{
			pstmt.close();
		}

		if(conn != null)
		{
			conn.close();
		}
		
		
		
		
		
		
		//
		sql = "select count(*),nvl(sum(chargePoints),0) from Player where RMB >= ? and RMB < ?  and quitgametime + 30*1000*3600*24 >= ? ";
		pstmt = conn.prepareStatement(sql);
	 	
	 	//韩服这里rmb必须除以440 这是韩币
	 	if(vipLevel < vipleves[vipleves.length-1])
	 	{
	 		pstmt.setLong(1, vipleves[vipLevel]);
	 		pstmt.setLong(2, vipleves[vipLevel+1]);
	 		pstmt.setLong(3, st);
	 	}
	 	else
	 	{
	 		sql = "select count(*),nvl(sum(chargePoints),0) from Player where RMB >= ? and quitgametime + 30*1000*3600*24 >= ? ";
		 	pstmt = conn.prepareStatement(sql);
		 	pstmt.setLong(1, vipleves[vipLevel]);
		 	pstmt.setLong(2, st);
	 	}
	 	
		rs = pstmt.executeQuery();
		if(rs != null)
		{
			if(rs.next())
			{
				playercount = rs.getLong(1);
				chargePointsSum = rs.getLong(2);
				pointstype++;
				YinZiKuCunFlow yinZiKuCunFlow=new YinZiKuCunFlow();
				yinZiKuCunFlow.setCount(chargePointsSum);    //剩余银子数量（单位：文）
				yinZiKuCunFlow.setColumn1(pointstype+"");
				yinZiKuCunFlow.setColumn2("2");
				yinZiKuCunFlow.setColumn3(maxLevel+"");
				yinZiKuCunFlow.setColumn4(vipLevel+"");
				yinZiKuCunFlow.setColumn5(playercount+"");
				yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
				yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
				if (!TestServerConfigManager.isTestServer()) {
					StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
				}

				logger.warn("[获取当前根据vip统计30天前服务器总积分] [成功] [积分:"+chargePointsSum+"] [type:"+pointstype+"] ["+vipLevel+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]");
			}

			rs.close();
		}




		if(pstmt != null)
		{
			pstmt.close();
		}

		if(conn != null)
		{
			conn.close();
		}
		
		
		
		//
		sql = "select count(*),nvl(sum(chargePoints),0) from Player where RMB >= ? and RMB < ?  and (quitgametime + 7*1000*3600*24) >= ? ";
		pstmt = conn.prepareStatement(sql);
	 	
	 	//韩服这里rmb必须除以440 这是韩币
	 	if(vipLevel < vipleves[vipleves.length-1])
	 	{
	 		pstmt.setLong(1, vipleves[vipLevel]);
	 		pstmt.setLong(2, vipleves[vipLevel+1]);
	 		pstmt.setLong(3, st);
	 	}
	 	else
	 	{
	 		sql = "select count(*),nvl(sum(chargePoints),0) from Player where RMB >= ? and (quitgametime + 7*1000*3600*24) >= ? ";
		 	pstmt = conn.prepareStatement(sql);
		 	pstmt.setLong(1, vipleves[vipLevel]);
		 	pstmt.setLong(2, st);
	 	}
	 	
		rs = pstmt.executeQuery();
		if(rs != null)
		{
			if(rs.next())
			{
				playercount = rs.getLong(1);
				chargePointsSum = rs.getLong(2);
				pointstype++;
				YinZiKuCunFlow yinZiKuCunFlow=new YinZiKuCunFlow();
				yinZiKuCunFlow.setCount(chargePointsSum);    //剩余银子数量（单位：文）
				yinZiKuCunFlow.setColumn1(pointstype+"");
				yinZiKuCunFlow.setColumn2("2");
				yinZiKuCunFlow.setColumn3(maxLevel+"");
				yinZiKuCunFlow.setColumn4(vipLevel+"");
				yinZiKuCunFlow.setColumn5(playercount+"");
				yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
				yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
				if (!TestServerConfigManager.isTestServer()) {
					StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
				}

				logger.warn("[获取当前根据vip统计7天前服务器总积分] [成功] [积分:"+chargePointsSum+"] [type:"+pointstype+"] ["+vipLevel+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]");
			}

			rs.close();
		}




		if(pstmt != null)
		{
			pstmt.close();
		}

		if(conn != null)
		{
			conn.close();
		}
	}
	

	/**
	 * 启动定时器每天晚上0点刷新玩家的境界任务和打坐时间.增加体力值
	 */
	private void initTimer() {
		Calendar now = Calendar.getInstance();
		long nowLong = now.getTimeInMillis();
		now.add(Calendar.DAY_OF_YEAR, 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		long nextLong = now.getTimeInMillis();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 执行更新玩家的境界任务和打坐时间
				Player[] onlinePlayers = GamePlayerManager.getInstance().getOnlinePlayers();
				for (Player p : onlinePlayers) {
					try {
						ChargeManager.getInstance().updateCradReward(p);
						p.setOnlineTimeOfDay(0);
						p.setRewardState(new int[]{0,0,0,0});
						p.modifyDailyChangevalue();
					} catch (Throwable t) {
						logger.error("[心跳异常]", t);
					}
				}
				
				try{
					ActivityManagers.hasOutNums.clear();
					BossCityManager.getInstance().initBoss();
				}catch(Exception e){
					e.printStackTrace();
				}
				
//				JJCManager.getInstance().addPlayerStat();
//				JJCManager.getInstance().addTeamStat();
				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				long st = System.currentTimeMillis();
				try
				{
					String sql = "";
					PlatformManager platformManager = PlatformManager.getInstance();
					if(platformManager != null)
					{
						boolean isOracle = false;
						long silverSum = 0l;
						
						if(platformManager.isPlatformOf(Platform.官方))
						{
							isOracle = true;
						}
						else
						{
							isOracle = false;
						}
						
						//发拍卖和和求购的银子
						//COLUMN2    3：拍卖  4：求购
						YinZiKuCunFlow yinZiKuCunFlow=new YinZiKuCunFlow();
						yinZiKuCunFlow.setCount(RequestBuyManager.getInstance().allMoney);    //求购的银两
						yinZiKuCunFlow.setColumn1("1");//和介云恒商定 这里按天发送的先暂时写死为1
						yinZiKuCunFlow.setColumn2("4");
						yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
						yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
						if (!TestServerConfigManager.isTestServer() ) {
							StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);  
						}
						
						yinZiKuCunFlow=new YinZiKuCunFlow();
						yinZiKuCunFlow.setCount(AuctionManager.getInstance().haveMoney);    //拍卖的银两
						yinZiKuCunFlow.setColumn1("1");
						yinZiKuCunFlow.setColumn2("3");
						yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
						yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
						if (!TestServerConfigManager.isTestServer() ) {
							StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);  
						}
						
						
						if(isOracle)
						{
							sql = "select sum(silver) from player";
							conn = ((SimpleEntityManagerOracleImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
							 
						}
						else
						{
							sql = "select sum(silver) from PLAYER_S1";
							conn = ((SimpleEntityManagerMysqlImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
						
						}
						
						long curTime = new Date().getTime();
						
						pstmt = conn.prepareStatement(sql);
						rs = pstmt.executeQuery();
						int type = 0;
						if(rs != null)
						{
							if(rs.next())
							{
								silverSum = rs.getLong(1);
								type++;
								yinZiKuCunFlow=new YinZiKuCunFlow();
								yinZiKuCunFlow.setCount(silverSum);    //剩余银子数量（单位：文）
								yinZiKuCunFlow.setColumn1(type+"");
								yinZiKuCunFlow.setColumn2("1");
								yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
								yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
								if (!TestServerConfigManager.isTestServer() ) {
									StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
								}
								
								logger.warn("[获取当前服务器总银两] [成功] [银两:"+silverSum+"] [type:"+type+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]<br/>");
							}
							
							rs.close();
						}
						
						if(pstmt != null)
						{
							pstmt.close();
						}
						
						if(conn != null)
						{
							conn.close();
						}
						

						if(isOracle)
						{
							sql = "select sum(silver) from player where quitgametime + 30*1000*3600*24 >= ? ";
							conn = ((SimpleEntityManagerOracleImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
							 
						}
						else
						{
							sql = "select sum(silver) from PLAYER_S1 where QUITGAMETIME + 30*1000*3600*24 >= ?";
							conn = ((SimpleEntityManagerMysqlImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
						
						}
						
						
						pstmt = conn.prepareStatement(sql);
						pstmt.setLong(1, curTime);
						rs = pstmt.executeQuery();
						if(rs != null)
						{
							if(rs.next())
							{
								silverSum = rs.getLong(1);
								type++;
								yinZiKuCunFlow=new YinZiKuCunFlow();
								yinZiKuCunFlow.setCount(silverSum);    //剩余银子数量（单位：文）
								yinZiKuCunFlow.setColumn1(type+"");
								yinZiKuCunFlow.setColumn2("1");
								yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
								yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
								if (!TestServerConfigManager.isTestServer() ) {
									StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
								}
								
								logger.warn("[获取当前服务器总银两] [成功] [银两:"+silverSum+"] [type:"+type+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]<br/>");
							}
							
							rs.close();
						}
						
						if(pstmt != null)
						{
							pstmt.close();
						}
						
						if(conn != null)
						{
							conn.close();
						}
						
						
						

						if(isOracle)
						{
							sql = "select sum(silver) from player where quitgametime + 7*1000*3600*24 >= ?";
							conn = ((SimpleEntityManagerOracleImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
							 
						}
						else
						{
							sql = "select sum(silver) from PLAYER_S1 where QUITGAMETIME + 7*1000*3600*24 >= ?";
							conn = ((SimpleEntityManagerMysqlImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
						
						}
						
						
						pstmt = conn.prepareStatement(sql);
						pstmt.setLong(1, curTime);
						rs = pstmt.executeQuery();
						if(rs != null)
						{
							if(rs.next())
							{
								silverSum = rs.getLong(1);
								type++;
								yinZiKuCunFlow=new YinZiKuCunFlow();
								yinZiKuCunFlow.setCount(silverSum);    //剩余银子数量（单位：文）
								yinZiKuCunFlow.setColumn1(type+"");
								yinZiKuCunFlow.setColumn2("1");
								yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
								yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
								if (!TestServerConfigManager.isTestServer() ) {
									StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
								}
								
								logger.warn("[获取当前服务器总银两] [成功] [银两:"+silverSum+"] [type:"+type+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]<br/>");
							}
							
							rs.close();
						}
						
						if(pstmt != null)
						{
							pstmt.close();
						}
						
						if(conn != null)
						{
							conn.close();
						}
						
						
						//发统计积分 由于台服还没有上 所以先发国服 以防报错
						
						if(isOracle)
						{
							//判断当前服务器最高等级
							//如果封印时间大于当前时间 则去其前一个封印级别作为游戏服最高等级
							
							int maxLevel = 70;
							SealManager sealManager = SealManager.getInstance();
							int[] sealLevels = SealManager.SEAL_LEVELS;
							for(int k = 0; k < sealLevels.length;k++)
							{
								Seal seal = sealManager.sealMap.get(sealLevels[k]);
								
								if(seal != null)
								{
									if(seal.getSealCanOpenTime() > System.currentTimeMillis())
									{
										if(k > 0)
										{
											maxLevel = sealLevels[k-1];
										}
									}
								}
							}
							
							
							sql = "select nvl(sum(chargePoints),0) from player";
							conn = ((SimpleEntityManagerOracleImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();



							long chargePointsSum = 0l;
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							int pointstype = 0;
							if(rs != null)
							{
								if(rs.next())
								{
									chargePointsSum = rs.getLong(1);
									pointstype++;
									yinZiKuCunFlow=new YinZiKuCunFlow();
									yinZiKuCunFlow.setCount(chargePointsSum);    //积分（单位：文）
									yinZiKuCunFlow.setColumn1(pointstype+"");
									yinZiKuCunFlow.setColumn2("2");
									yinZiKuCunFlow.setColumn3(maxLevel+"");
									yinZiKuCunFlow.setColumn4(-1+"");
									yinZiKuCunFlow.setColumn5(-1+"");
									yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
									yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
									if (!TestServerConfigManager.isTestServer() ) {
										StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
									}

									logger.warn("[获取当前服务器总积分] [成功] [积分:"+chargePointsSum+"] [type:"+pointstype+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]<br/>");
								}

								rs.close();
							}

							if(pstmt != null)
							{
								pstmt.close();
							}

							if(conn != null)
							{
								conn.close();
							}


							sql = "select nvl(sum(chargePoints),0) from player where quitgametime + 30*1000*3600*24 >= ? ";
							conn = ((SimpleEntityManagerOracleImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();



							pstmt = conn.prepareStatement(sql);
							pstmt.setLong(1, curTime);
							rs = pstmt.executeQuery();
							if(rs != null)
							{
								if(rs.next())
								{
									chargePointsSum = rs.getLong(1);
									pointstype++;
									yinZiKuCunFlow=new YinZiKuCunFlow();
									yinZiKuCunFlow.setCount(chargePointsSum);    //剩余银子数量（单位：文）
									yinZiKuCunFlow.setColumn1(pointstype+"");
									yinZiKuCunFlow.setColumn2("2");
									yinZiKuCunFlow.setColumn3(maxLevel+"");
									yinZiKuCunFlow.setColumn4(-1+"");
									yinZiKuCunFlow.setColumn5(-1+"");
									yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
									yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
									if (!TestServerConfigManager.isTestServer() ) {
										StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
									}

									logger.warn("[获取当前服务器总积分] [成功] [积分:"+chargePointsSum+"] [type:"+pointstype+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]<br/>");
								}

								rs.close();
							}

							if(pstmt != null)
							{
								pstmt.close();
							}

							if(conn != null)
							{
								conn.close();
							}




							sql = "select nvl(sum(chargePoints),0) from player where quitgametime + 7*1000*3600*24 >= ?";
							conn = ((SimpleEntityManagerOracleImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
								

							pstmt = conn.prepareStatement(sql);
							pstmt.setLong(1, curTime);
							rs = pstmt.executeQuery();
							if(rs != null)
							{
								if(rs.next())
								{
									chargePointsSum = rs.getLong(1);
									pointstype++;
									yinZiKuCunFlow=new YinZiKuCunFlow();
									yinZiKuCunFlow.setCount(chargePointsSum);    //剩余银子数量（单位：文）
									yinZiKuCunFlow.setColumn1(pointstype+"");
									yinZiKuCunFlow.setColumn2("2");
									yinZiKuCunFlow.setColumn3(maxLevel+"");
									yinZiKuCunFlow.setColumn4(-1+"");
									yinZiKuCunFlow.setColumn5(-1+"");
									yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
									yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
									if (!TestServerConfigManager.isTestServer() ) {
										StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
									}

									logger.warn("[获取当前服务器总积分] [成功] [积分:"+chargePointsSum+"] [type:"+pointstype+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]<br/>");
								}

								rs.close();
							}




							if(pstmt != null)
							{
								pstmt.close();
							}

							if(conn != null)
							{
								conn.close();
							}
								
							//目前只支持oracle 等mysql产生了新字段后 再加统计积分的功能
							for(int i=0;i<=10;i++)
							{
								try
								{
									sendStatPoints4VipLevel(i);
								}
								catch(Exception e)
								{
									logger.error("[根据vip等级发送统计积分] [出现异常]",e);
								}
							}
						}
						else
						{

							//判断当前服务器最高等级
							//如果封印时间大于当前时间 则去其前一个封印级别作为游戏服最高等级
							
							int maxLevel = 70;
							SealManager sealManager = SealManager.getInstance();
							int[] sealLevels = SealManager.SEAL_LEVELS;
							for(int k = 0; k < sealLevels.length;k++)
							{
								Seal seal = sealManager.sealMap.get(sealLevels[k]);
								
								if(seal != null)
								{
									if(seal.getSealCanOpenTime() > System.currentTimeMillis())
									{
										if(k > 0)
										{
											maxLevel = sealLevels[k-1];
										}
									}
								}
							}
							
							
							sql = "select sum(chargePoints) from PLAYER_S1";
							conn = ((SimpleEntityManagerMysqlImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();



							long chargePointsSum = 0l;
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery();
							int pointstype = 0;
							if(rs != null)
							{
								if(rs.next())
								{
									chargePointsSum = rs.getLong(1);
									pointstype++;
									yinZiKuCunFlow=new YinZiKuCunFlow();
									yinZiKuCunFlow.setCount(chargePointsSum);    //积分（单位：文）
									yinZiKuCunFlow.setColumn1(pointstype+"");
									yinZiKuCunFlow.setColumn2("2");
									yinZiKuCunFlow.setColumn3(maxLevel+"");
									yinZiKuCunFlow.setColumn4(-1+"");
									yinZiKuCunFlow.setColumn5(-1+"");
									yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
									yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
									if (!TestServerConfigManager.isTestServer() ) {
										StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
									}

									logger.warn("[获取当前服务器总积分] [成功] [积分:"+chargePointsSum+"] [type:"+pointstype+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]<br/>");
								}

								rs.close();
							}

							if(pstmt != null)
							{
								pstmt.close();
							}

							if(conn != null)
							{
								conn.close();
							}


							sql = "select sum(chargePoints) from PLAYER_S1 where quitgametime + 30*1000*3600*24 >= ? ";
							conn = ((SimpleEntityManagerMysqlImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();



							pstmt = conn.prepareStatement(sql);
							pstmt.setLong(1, curTime);
							rs = pstmt.executeQuery();
							if(rs != null)
							{
								if(rs.next())
								{
									chargePointsSum = rs.getLong(1);
									pointstype++;
									yinZiKuCunFlow=new YinZiKuCunFlow();
									yinZiKuCunFlow.setCount(chargePointsSum);    //剩余银子数量（单位：文）
									yinZiKuCunFlow.setColumn1(pointstype+"");
									yinZiKuCunFlow.setColumn2("2");
									yinZiKuCunFlow.setColumn3(maxLevel+"");
									yinZiKuCunFlow.setColumn4(-1+"");
									yinZiKuCunFlow.setColumn5(-1+"");
									yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
									yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
									if (!TestServerConfigManager.isTestServer() ) {
										StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
									}

									logger.warn("[获取当前服务器总积分] [成功] [积分:"+chargePointsSum+"] [type:"+pointstype+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]<br/>");
								}

								rs.close();
							}

							if(pstmt != null)
							{
								pstmt.close();
							}

							if(conn != null)
							{
								conn.close();
							}




							sql = "select sum(chargePoints) from PLAYER_S1 where quitgametime + 7*1000*3600*24 >= ?";
							conn = ((SimpleEntityManagerMysqlImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
								

							pstmt = conn.prepareStatement(sql);
							pstmt.setLong(1, curTime);
							rs = pstmt.executeQuery();
							if(rs != null)
							{
								if(rs.next())
								{
									chargePointsSum = rs.getLong(1);
									pointstype++;
									yinZiKuCunFlow=new YinZiKuCunFlow();
									yinZiKuCunFlow.setCount(chargePointsSum);    //剩余银子数量（单位：文）
									yinZiKuCunFlow.setColumn1(pointstype+"");
									yinZiKuCunFlow.setColumn2("2");
									yinZiKuCunFlow.setColumn3(maxLevel+"");
									yinZiKuCunFlow.setColumn4(-1+"");
									yinZiKuCunFlow.setColumn5(-1+"");
									yinZiKuCunFlow.setCreateDate(new Date().getTime());  //日期
									yinZiKuCunFlow.setFenQu(GameConstants.getInstance().getServerName());             //分区
									if (!TestServerConfigManager.isTestServer() ) {
										StatClientService.getInstance().sendYinZiKuCunFlow("wangxian",yinZiKuCunFlow);    
									}

									logger.warn("[获取当前服务器总积分] [成功] [积分:"+chargePointsSum+"] [type:"+pointstype+"] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]<br/>");
								}

								rs.close();
							}




							if(pstmt != null)
							{
								pstmt.close();
							}

							if(conn != null)
							{
								conn.close();
							}
								
							//目前只支持oracle 等mysql产生了新字段后 再加统计积分的功能
							for(int i=0;i<=10;i++)
							{
								try
								{
									sendStatPoints4VipLevel(i);
								}
								catch(Exception e)
								{
									logger.error("[根据vip等级发送统计积分] [出现异常]",e);
								}
							}
						
						}
						
					}
					else
					{
						logger.warn("[获取当前服务器总银两] [失败] [无法获取平台信息] [银两:--] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]");

					}
					
				}
				catch(Exception e)
				{
					logger.warn("[获取当前服务器总银两] [失败] [银两:--] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]"+e.getMessage());
				}
				finally
				{
					if(rs != null)
					{
						rs = null;
					}

					if(pstmt != null)
					{
						pstmt = null;
					}
					
					if(conn != null)
					{
						conn = null;
					}
				}

			}
		}, (nextLong - nowLong), 1000 * 60 * 60 * 24L);
	}
	
	@SuppressWarnings("static-access")
	public void sendPlayerRobberyInfo() {
		String sql4Robbery = "";
		List<StringBuffer> sql4Pinfos = new ArrayList<StringBuffer>();
		String sqlBase4Pinfo = "";
		PlatformManager platformManager = PlatformManager.getInstance();
		long st = System.currentTimeMillis();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Long> playerIds = new ArrayList<Long>();
		try {
			boolean isOracle = false;
			if(platformManager.isPlatformOf(Platform.官方)) {
				isOracle = true;
			} else {
				isOracle = false;
			}
			if(isOracle) {
				sql4Robbery = "select id,currentLevel,passLayer from TransitRobberyEntity";
				sqlBase4Pinfo = "select id,RMB,playerLevel,career,country from Player where ";
				conn = ((SimpleEntityManagerOracleImpl<TransitRobberyEntity>) (TransitRobberyEntityManager.getInstance()).em).getConnection();
			} else {
				sql4Robbery = "select id,currentLevel,passLayer from TRANSITROBBERYENTITY";
				sqlBase4Pinfo = "select id,RMB,playerLevel,career,country from PLAYER_S1 where ";
				conn = ((SimpleEntityManagerMysqlImpl<TransitRobberyEntity>) (TransitRobberyEntityManager.getInstance()).em).getConnection();
			}
			long playerId = 0;
			int currentLevel = 0;
			int passLayer = 0;
			pstmt = conn.prepareStatement(sql4Robbery);
			rs = pstmt.executeQuery();
			if(rs != null) {
				while(rs.next()) {
					playerId = rs.getLong(1);
					currentLevel = rs.getInt(2);
					passLayer = rs.getInt(3);
					playerIds.add(playerId);
				}
				rs.close();
			}
			if(pstmt != null) {
				pstmt.close();
			}
			if(conn != null) {
				conn.close();
			}
			
			
			int tempLeft = playerIds.size();
			int perUnitNum = 100;
			int tempIndex = 0;
			while(tempLeft >= perUnitNum) {
				tempLeft -= perUnitNum;
				StringBuffer sbi = new StringBuffer();
				sbi.append(sqlBase4Pinfo);
				for(int i=tempIndex; i<(tempIndex + perUnitNum); i++) {
					sbi.append("id=" + playerIds.get(i));
					if(i < (tempIndex + perUnitNum - 1)) {
						sbi.append(" or ");
					}
				}
				tempIndex = tempIndex + perUnitNum;
				sql4Pinfos.add(sbi);
			}
			if(tempLeft > 0) {
				StringBuffer sbi = new StringBuffer();
				sbi.append(sqlBase4Pinfo);
				for(int i=tempIndex; i<(tempIndex + tempLeft); i++) {
					sbi.append("id=" + playerIds.get(i));
					if(i < (tempIndex + tempLeft - 1)) {
						sbi.append(" or ");
					}
				}
				tempIndex = tempIndex + tempLeft;
				sql4Pinfos.add(sbi);
			}
			
			long playerIdT = 0;
			long rmb = 0;
			int playerLevel = 0;
			byte career = 0;
			byte country = 0;
			
			
			for(int i=0; i<sql4Pinfos.size(); i++) {
				if(isOracle) {
					conn = ((SimpleEntityManagerOracleImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
				} else {
					conn = ((SimpleEntityManagerMysqlImpl<Player>) ((GamePlayerManager)GamePlayerManager.getInstance()).em).getConnection();
				}
				pstmt = conn.prepareStatement(sql4Pinfos.get(i).toString());
				rs = pstmt.executeQuery();
				if(rs != null) {
					while(rs.next()) {
						playerId = rs.getLong(1);
						rmb = rs.getLong(2);
						playerLevel = rs.getInt(3);
						career = rs.getByte(4);
						country = rs.getByte(5);
					}
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			}
		} catch (Exception e) {
			logger.warn("[获取当前服务器渡劫信息] [失败] [渡劫:--] [当前服务器名称:"+GameConstants.getInstance().getServerName() +"] [cost:"+(System.currentTimeMillis()-st)+"ms]"+e.getMessage());
		} finally {
			if(conn != null) {
				conn = null;
			}
			if(pstmt != null) {
				pstmt = null;
			}
			if(rs != null) {
				rs = null;
			}
			if(sql4Pinfos != null) {
				sql4Pinfos = null;
			}
		}
	}
}
