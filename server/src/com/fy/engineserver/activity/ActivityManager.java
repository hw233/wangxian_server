package com.fy.engineserver.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.activity.base.AddRateActivity;
import com.fy.engineserver.activity.base.GodDownActivityConfig;
import com.fy.engineserver.activity.base.SendFlowerActivityConfig;
import com.fy.engineserver.activity.chongzhiActivity.ChargePackageActivity;
import com.fy.engineserver.activity.chongzhiActivity.ChargeRecord;
import com.fy.engineserver.activity.chongzhiActivity.LevelPackageActivity;
import com.fy.engineserver.activity.chongzhiActivity.MonthCardActivity;
import com.fy.engineserver.activity.chongzhiActivity.MonthCardRecord;
import com.fy.engineserver.activity.chongzhiActivity.TotleCostActivity;
import com.fy.engineserver.activity.expactivity.DailyfExpActivity;
import com.fy.engineserver.activity.expactivity.DayilyTimeDistance;
import com.fy.engineserver.activity.expactivity.ExpActivity;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.loginActivity.VipLoginConfig;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewMoneyActivity;
import com.fy.engineserver.activity.newChongZhiActivity.TotalChongZhiActivity;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.activity.wolf.config.ActivityConfig;
import com.fy.engineserver.activity.wolf.config.TimeRangeConfig;
import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.exchange.Option_Choose_Exchange;
import com.fy.engineserver.menu.activity.exchange.Option_True_Exchange;
import com.fy.engineserver.message.ACTIVITY_IS_SHOW_RES;
import com.fy.engineserver.message.CHARGE_GET_PACKAGE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.MONTH_CARD_ACTIVITY_BUY_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.TOTLE_COST_ACTIVITY_RES;
import com.fy.engineserver.newBillboard.Billboard;
import com.fy.engineserver.newBillboard.BillboardDate;
import com.fy.engineserver.newBillboard.BillboardsManager;
import com.fy.engineserver.newBillboard.monitorLog.LogRecordManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.Utils;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * 活动的一些杂项管理
 * 
 */
public class ActivityManager implements MConsole {

	private MemoryNPCManager npcManager;
	private TaskManager taskManager;
	@ChangeAble("老玩家回归间隔")
	public long oldplayerlength = 90 * 24 * 60 * 60 * 1000l;
	
	@ChangeAble("周年寻宝")
	public String 周年寻宝 = Translate.周年寻宝;
	@ChangeAble("全服寻宝")
	public String 全服寻宝 = Translate.全服寻宝;
	@ChangeAble("极地寻宝")
	public String 极地寻宝 = Translate.极地寻宝;
	
	public static boolean isOpenYunYingActivity = false;
	
	private static ActivityManager instance;
	private String filePath;

	// 存储参加活动玩家的列表
	@ChangeAble("diskCache文件路径")
	private String diskFile;
	public DiskCache disk = null;

	private String fileName;

	public static int MONTH_CARD_BUY_LEVEL_LIMIT = 10;
	public static int EXP_ACTIVITY_SHEET = 0; // 所有经验活动sheet页
	public static int TUMOTIE_ACTIVITY_SHEET = 1; // 封魔录活动sheet页
	public static int CHUANGONG_ACTIVITY_SHEET = 2; // 传功活动sheet页
	public static int DATI_ACTIVITY_SHEET = 3; // 答题活动sheet页
	public static int JIFEN_ACTIVITY_SHEET = 4; // 积分活动sheet页
	public static int XUESHI_ACTIVITY_SHEET = 5; // 血石活动sheet页
	public static int GODDOWN_ACTIVITY_SHEET = 6; // 天降宝箱活动sheet页
	public static int FLOWER_ACTIVITY_SHEET = 7; // 天降宝箱活动sheet页
	public static int MONTH_CARD_ACTIVITY_SHEET = 8; //月卡活动sheet页
	public static int LEVEL_PACKAGE_ACTIVITY_SHEET = 9; //等级限制礼包活动sheet页
	public static int CHARGE_PACKAGE_ACTIVITY_SHEET = 10; //充值的礼包活动sheet页
	public static int WOLF_ACTIVITY_SHEET = 11; //狼吃羊活动sheet页

	public boolean openFunction = true;
	
	// 所有的活动简介列表
	private List<ActivityIntroduce> activityIntroduces = new ArrayList<ActivityIntroduce>();

	private List<ExpActivity> expActivityList = new ArrayList<ExpActivity>();// 所有经验活动
	private List<ExpActivity> tumotieActivityList = new ArrayList<ExpActivity>();// 封魔录经验活动

	private List<AddRateActivity> chuanGongActivity = new ArrayList<AddRateActivity>();// 传功活动
	private List<AddRateActivity> datiActivity = new ArrayList<AddRateActivity>();// 答题活动
	private List<AddRateActivity> jifenActivity = new ArrayList<AddRateActivity>();// 积分活动
	private List<AddRateActivity> xueShiActivity = new ArrayList<AddRateActivity>();// 血石活动
	public List<GodDownActivityConfig> godDownActivity = new ArrayList<GodDownActivityConfig>();// 天降宝箱活动
	public List<SendFlowerActivityConfig> sendFlowerActivity = new ArrayList<SendFlowerActivityConfig>();// 送花活动
	public List<MonthCardActivity> monthCardActivity = new ArrayList<MonthCardActivity>();// 月卡活动
	public List<LevelPackageActivity> levelPackageActivity = new ArrayList<LevelPackageActivity>();// 等级礼包活动
	public List<ChargePackageActivity> chargePackageActivity = new ArrayList<ChargePackageActivity>();// 等级礼包活动

	private List<NewPlayerPrize> newPlayerPrizes = new ArrayList<NewPlayerPrize>();// 新服渠道奖励
	private Map<String, List<ActivityProp>> prizeMap = new HashMap<String, List<ActivityProp>>();
	private String specialServerPrize;

	public static SimpleEntityManager<MonthCardRecord> em_record;
	public static SimpleEntityManager<ChargeRecord> em_charge;
	public List<MonthCardRecord> datas = new ArrayList<MonthCardRecord>();
	public List<ChargeRecord> records = new ArrayList<ChargeRecord>();
	public static long MONTH_CARD_LAST_TIME = 24*60*60*1000L;
	public static long ONE_DAY = 24*60*60*1000L;
	
	public static ActivityManager getInstance() {
		return instance;
	}

	public MemoryNPCManager getNpcManager() {
		return npcManager;
	}

	public void setNpcManager(MemoryNPCManager npcManager) {
		this.npcManager = npcManager;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<ExpActivity> getExpActivityList() {
		return expActivityList;
	}

	public void setExpActivityList(List<ExpActivity> expActivityList) {
		this.expActivityList = expActivityList;
	}

	public List<ExpActivity> getTumotieActivityList() {
		return tumotieActivityList;
	}

	public void setTumotieActivityList(List<ExpActivity> tumotieActivityList) {
		this.tumotieActivityList = tumotieActivityList;
	}

	public List<ActivityIntroduce> getActivityIntroduces() {
		return activityIntroduces;
	}

	public void setActivityIntroduces(List<ActivityIntroduce> activityIntroduces) {
		this.activityIntroduces = activityIntroduces;
	}

	public List<NewPlayerPrize> getNewPlayerPrizes() {
		return newPlayerPrizes;
	}

	public void setNewPlayerPrizes(List<NewPlayerPrize> newPlayerPrizes) {
		this.newPlayerPrizes = newPlayerPrizes;
	}

	public Map<String, List<ActivityProp>> getPrizeMap() {
		return prizeMap;
	}

	public void setPrizeMap(Map<String, List<ActivityProp>> prizeMap) {
		this.prizeMap = prizeMap;
	}

	public String getSpecialServerPrize() {
		return specialServerPrize;
	}

	public void setSpecialServerPrize(String specialServerPrize) {
		this.specialServerPrize = specialServerPrize;
	}
String messs = "1、登仙活动充值为累计一次，不可重复获得\n2、正常充值、充值月卡、充值每日礼包，均按1元=50两计入充值活动\n3、充值返利金额不计入本活动\n4、本活动累计达到不需要手动领取，均自动发放到邮件，请邮件内查收\n5、请注意活动时间，过期后充值无效";
	public void handleTotleCostActivityQuery(Player player,String reason){
		try {
			List<TotleCostActivity> list = new ArrayList<TotleCostActivity>();
			ArrayList<NewMoneyActivity> chongZhiActivitys = NewChongZhiActivityManager.instance.chongZhiActivitys;
			if(chongZhiActivitys == null || chongZhiActivitys.size() <= 0){
				player.sendError("无效的充值活动");
				return;
			}
			for(NewMoneyActivity a : chongZhiActivitys){
				if(a != null && a instanceof TotalChongZhiActivity){
					TotleCostActivity newA = new TotleCostActivity();
					TotalChongZhiActivity ta = (TotalChongZhiActivity)a;
					if(!ta.isCanServer() || !ta.isEffect()){
						continue;
					}
					Long oldMoney = ta.playerMoneys.get(player.getId());
					if (oldMoney == null) {
						oldMoney = new Long(0);
					}
					newA.setHasChargeRmb((int)oldMoney.longValue());
					newA.setNeedChargeRmb((int)ta.getNeedMoney());
					newA.setEndTime(a.getEndTimeLong());
					String[] rewardPropNames = ta.getRewardPropNames();
					long ids [] = new long[rewardPropNames.length];
					for(int i = 0;i < rewardPropNames.length;i++){
						Article article = ArticleManager.getInstance().getArticle(rewardPropNames[i]);
						ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(rewardPropNames[i], true, ta.getRewardColors()[i]);
						if(ae == null){
							ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.七日登录奖励, null, ta.getRewardColors()[i]);
						}
						ids[i] = ae.getId();
					}
					newA.setNums(ta.getRewardPropNums());
					newA.setColors(ta.getRewardColors());
					newA.setIds(ids);
					list.add(newA);
				}
			}
			if(list.size() == 0){
				TOTLE_COST_ACTIVITY_RES res = new TOTLE_COST_ACTIVITY_RES(GameMessageFactory.nextSequnceNum(),messs,new TotleCostActivity[]{});
				player.addMessageToRightBag(res);
				ActivitySubSystem.logger.warn("[请求累计充值界面] [失败:没有活动] ["+reason+"] [信息列表:"+list.size()+"/"+chongZhiActivitys.size()+"] ["+player.getLogString()+"]");
				return;
			}
			
			TOTLE_COST_ACTIVITY_RES res = new TOTLE_COST_ACTIVITY_RES(GameMessageFactory.nextSequnceNum(),messs,list.toArray(new TotleCostActivity[]{}));
			player.addMessageToRightBag(res);
			ActivitySubSystem.logger.warn("[请求累计充值界面] [成功] ["+reason+"] [信息列表:"+list.size()+"/"+chongZhiActivitys.size()+"] ["+player.getLogString()+"]");
		} catch (Exception e) {
			e.printStackTrace();
			ActivitySubSystem.logger.warn("[请求累计充值界面] [异常] ["+reason+"] ["+player.getLogString()+"]",e);
		}
	}
	

	public static boolean limitFunctionOfApple = true;
	public void handPlayerEnter(Player p,int type){
		if(p.getLevel() < 10){
			boolean [] results = new boolean[10];
			Arrays.fill(results, false);
			p.addMessageToRightBag(new ACTIVITY_IS_SHOW_RES(GameMessageFactory.nextSequnceNum(),1, results));
			ActivitySubSystem.logger.warn("[活动按钮显示] [失败：等级不够] [type:{}] [results:{}] [{}]",new Object[]{type,Arrays.toString(results),p.getLogString()});
			return;
		}
		//
		//99玩家上线 0客户端请求1 签到 2 七日宝箱 3每日礼包 4 在线奖励 5 活跃度 6 活跃度抽奖 7激活码 
		boolean [] results = new boolean[10];
		Arrays.fill(results, true);
		int cType = type;
		boolean result1 = false;
		int [] states = p.getLoginState();
		for(int stat : states){
			if(stat == 0 || stat == 1){
				result1 = true;
				break;
			}
		}
		results[1] = result1;
		if(result1){
			cType = 1;
		}
//		if(limitFunctionOfApple){
//			results[2] = false;
//			results[6] = false;
//		}
		if(p.getPassport() != null && p.getPassport().getRegisterChannel().contains("APPSTORE_XUNXIAN")){
//			results[6] = false;
		}
		ActivitySubSystem.logger.warn("[活动按钮显示] [cType:{}] [type:{}] [results:{}] [{}]",new Object[]{cType,type,Arrays.toString(results),p.getLogString()});
		p.addMessageToRightBag(new ACTIVITY_IS_SHOW_RES(GameMessageFactory.nextSequnceNum(),cType, results));
	}
	
	private void load() throws Exception {
		File file = new File(getFilePath());
		file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		try {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			HSSFSheet sheet = workbook.getSheetAt(0);
			int maxRow = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				int id = StringTool.getCellValue(row.getCell(index++), int.class);
				String icon = row.getCell(index++).getStringCellValue();
				int showType = StringTool.getCellValue(row.getCell(index++), int.class);
				Integer[] weekDay = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ",", Integer.class);
				String startS = StringTool.getCellValue(row.getCell(index++), String.class);
				String endS = StringTool.getCellValue(row.getCell(index++), String.class);
				Calendar end = Calendar.getInstance();
				Integer[] startTimes = StringTool.string2Array(startS, "-", Integer.class);
				Calendar start = Calendar.getInstance();
				start.set(Calendar.HOUR_OF_DAY, startTimes[0]);
				start.set(Calendar.MINUTE, startTimes[1]);
				Integer[] endTimes = StringTool.string2Array(endS, "-", Integer.class);
				end.set(Calendar.HOUR_OF_DAY, endTimes[0]);
				end.set(Calendar.MINUTE, endTimes[1]);
				String openTimeDistance = startS + "-" + endS;

				String name = row.getCell(index++).getStringCellValue();
				String startGameCn = StringTool.getCellValue(row.getCell(index++), String.class);
				String startGameRes = "";
				String startNpc = StringTool.getCellValue(row.getCell(index++), String.class);
				startNpc = startNpc == null || "".equals(startNpc) ? "" : startNpc;
				startGameCn = startGameCn == null || "".equals(startGameCn) ? "" : startGameCn;
				int startX = 0;
				int startY = 0;
				int[] xs = null;
				int[] ys = null;
				String [] games = null;
				String [] gamecns = null;
				if(startGameCn != null && !startGameCn.isEmpty() && startGameCn.contains(",")){
					gamecns = startGameCn.split(",");
					xs = new int[gamecns.length];
					ys = new int[gamecns.length];
					games = new String[gamecns.length];
					for(int p=0;p<gamecns.length;p++){
						CompoundReturn cr = TaskManager.getInstance().getNPCInfoByGameAndNameByCountry(gamecns[p], startNpc, p+1);
						if (cr.getBooleanValue()) {
							xs[p]=cr.getIntValues()[0];
							ys[p]=cr.getIntValues()[1];
							games[p]=GameManager.getInstance().getResName(gamecns[p], p+1);
						}
					}
				}
				CompoundReturn cr = TaskManager.getInstance().getNPCInfoByGameAndName(startGameCn, startNpc);
				if (cr.getBooleanValue()) {
					startX = cr.getIntValues()[0];
					startY = cr.getIntValues()[1];
					startGameRes = GameManager.getInstance().getResName(startGameCn, CountryManager.国家A);
					if (startGameRes == null) {
						startGameRes = GameManager.getInstance().getResName(startGameCn, CountryManager.中立);
					}
				}
				String des = StringTool.getCellValue(row.getCell(index++), String.class);
				String value = StringTool.getCellValue(row.getCell(index++), String.class);
				String[] lables = new String[0];
				if (value == null || "".equals(value)) {

				} else {
					lables = StringTool.string2Array(value, ",", String.class);
				}
				int activityAdd = StringTool.getCellValue(row.getCell(index++), int.class);
				String taskGroupName = StringTool.getCellValue(row.getCell(index++), String.class);
				int dailyNum = 0;
				if (taskGroupName == null || "".equals(taskGroupName)) {
					taskGroupName = "";
					dailyNum = 0;
				} else {
					List<Task> list = taskManager.getTaskGrouopMap().get(taskGroupName);
					if (list != null && list.size() > 0) {
						dailyNum = list.get(0).getDailyTaskMaxNum();
					} else {
						dailyNum = 0;
					}
				}
				int levelLimit = StringTool.getCellValue(row.getCell(index++), int.class);
				String totalStartTime = StringTool.getCellValue(row.getCell(index++), String.class);
				String totalEndTime = StringTool.getCellValue(row.getCell(index++), String.class);
				String isShowAccordTime = StringTool.getCellValue(row.getCell(index++), String.class);
				String[] showservers = new String[0];
				value = StringTool.getCellValue(row.getCell(index++), String.class);
				if (value == null || "".equals(value)) {

				} else {
					showservers = StringTool.string2Array(value, ",", String.class);
				}
				String[] limitservers = new String[0];
				value = StringTool.getCellValue(row.getCell(index++), String.class);
				if (value == null || "".equals(value)) {

				} else {
					limitservers = StringTool.string2Array(value, ",", String.class);
				}
				index += 1;
				int star = StringTool.getCellValue(row.getCell(index), Integer.class);
				int activityType = StringTool.getCellValue(row.getCell(index+1), Integer.class);
				ActivityIntroduce activityIntroduce = new ActivityIntroduce(id, icon, showType, openTimeDistance, name, startGameRes, startGameCn, startNpc, startX, startY, des, activityAdd, lables, levelLimit, taskGroupName, dailyNum, weekDay, start, end, isShowAccordTime, showservers, limitservers, TimeTool.formatter.varChar19.parse(totalStartTime), TimeTool.formatter.varChar19.parse(totalEndTime));
				activityIntroduce.setStartXs(xs);
				activityIntroduce.setStartYs(ys);
				activityIntroduce.setStartGames(games);
				activityIntroduce.setStartGameCnNames(gamecns);
				activityIntroduce.setStar(star);
				activityIntroduce.setActivityType(activityType);
				putToCollections(activityIntroduce);
				ActivitySubSystem.logger.warn("[系统初始化] [活动介绍] [xs:"+(xs!=null?Arrays.toString(xs):"null")+"] [ys:"+(ys!=null?Arrays.toString(ys):"null")+"] " +
						"[games:"+(games!=null?Arrays.toString(games):"null")+"]  [gamecns:"+(gamecns!=null?Arrays.toString(gamecns):"null")+"] [star:"+star+"] [activityType:"+activityType+"] [" + activityIntroduce.toString() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActivitySubSystem.logger.warn("[系统初始化] [异常=====================] [" + e + "] ");
			throw e;
		}
	}

	private void loadSpecialServerPrize() throws Exception {
		File file = new File(getSpecialServerPrize());
		file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		try {
			Workbook workbook = Workbook.getWorkbook(file);

			Sheet sheet2 = workbook.getSheet(2);
			for (int i = 1; i < sheet2.getRows(); i++) {
				Cell[] cells = sheet2.getRow(i);
				int index = 0;
				String prizeGroup = (StringTool.modifyContent(cells[index++]));
				String articleName = (StringTool.modifyContent(cells[index++]));
				String articleCNName = (StringTool.modifyContent(cells[index++]));
				int articleColor = Integer.valueOf(StringTool.modifyContent(cells[index++]));
				int articleNum = Integer.valueOf(StringTool.modifyContent(cells[index++]));
				boolean bind = Boolean.valueOf(StringTool.modifyContent(cells[index++]));
				ActivityProp ap = new ActivityProp(articleCNName, articleColor, articleNum, bind);
				if (!prizeMap.containsKey(prizeGroup)) {
					prizeMap.put(prizeGroup, new ArrayList<ActivityProp>());
				}
				prizeMap.get(prizeGroup).add(ap);
			}

			Sheet sheet1 = workbook.getSheet(1);
			for (int i = 1; i < sheet1.getRows(); i++) {
				Cell[] cells = sheet1.getRow(i);
				int index = 0;
				String platformName = (StringTool.modifyContent(cells[index++]));
				String serverName = (StringTool.modifyContent(cells[index++]));
				String prizeGroupName = (StringTool.modifyContent(cells[index++]));
				String mailTitle = (StringTool.modifyContent(cells[index++]));
				String mailContent = (StringTool.modifyContent(cells[index++]));
				Platform platform = PlatformManager.getInstance().getPlatForm(platformName.trim());
				NewPlayerPrize ss = new NewPlayerPrize(platform, serverName, prizeMap.get(prizeGroupName), prizeGroupName, mailTitle, mailContent);
				newPlayerPrizes.add(ss);
			}
			ActivitySubSystem.logger.warn("[新服奖励初始化完毕]");
		} catch (Exception e) {
			throw e;
		}
	}

	public List<ActivityIntroduce> getFitActivityIntroduce(Player player) {
		List<ActivityIntroduce> list = new ArrayList<ActivityIntroduce>();
		for (ActivityIntroduce ai : activityIntroduces) {
			// TODO 判断 时间,服务器 是否符合
			if (ai.isServerShow() && ai.isShowAccordStartAndEndTime()) {
				list.add(ai);
			}
		}
		Collections.sort(list, order);
		return list;
	}

	public static Comparator<ActivityIntroduce> order = new Comparator<ActivityIntroduce>() {
		@Override
		public int compare(ActivityIntroduce o1, ActivityIntroduce o2) {
			Calendar now = Calendar.getInstance();
			return o1.isIncycle(now) ? -1 : (o2.isIncycle(now) ? 1 : 0);
		}
	};

	private void putToCollections(ActivityIntroduce activityIntroduce) {
		activityIntroduces.add(activityIntroduce);
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(TaskManager taskManager) {
		this.taskManager = taskManager;
	}

	/************************************* 活动名字/数据列表 *************************************/
	public final static String key_最后一次获得奖励时间 = "最后一次获得奖励时间";
	public final static String key_完美紫装免费拿 = "完美紫装免费拿";
	public boolean 分服活动open = false;
	public static String[] 完美紫装免费拿servers = { "风雪之巅", "桃源仙境", "昆仑圣殿", "昆华古城", "燃烧圣殿", "太华神山", "东海龙宫", "炼狱绝地", "云海冰岚", "无极冰原", "福地洞天", "峨嵋金顶", "云波鬼岭", "黄金海岸", "", "蓬莱仙阁", "鹊桥仙境", "瑶台仙宫", "太虚幻境", "问天灵台", "玉幡宝刹", "国内本地测试", "潘多拉星", "正式发布服" };
	public static String[] 完美紫装免费拿_A = { "完美紫胸甲", "完美紫鞋", "完美紫头盔", "完美紫戒指", "完美紫护手" };
	public static String[] 完美紫装免费拿_B = { "高级完美紫胸甲", "高级完美紫鞋", "高级完美紫头盔", "高级完美紫戒指", "高级完美紫护手" };
	public static long 完美紫装免费拿start;
	public static long 完美紫装免费拿end;

	public static long 中秋国庆天天有好礼start;
	public static long 中秋国庆天天有好礼end;
	public static String[] 中秋国庆天天有好礼article = { "鉴定符", "高级鲁班令", "铭刻符", "初级培养石", "强化石" };
	public static int[] 中秋国庆天天有好礼num = { 10, 5, 6, 39, 50 };

	public final static String key_中秋国庆天天有好礼 = "中秋国庆天天有好礼";

	/************************************* 活动名字/数据列表 *************************************/

	@ChangeAble(value = "棉花糖开始时间", isTime = true)
	public long 棉花糖开始时间;
	@ChangeAble("棉花糖开始时间_tw")
	public long 棉花糖开始时间_tw;
	@ChangeAble("棉花糖结束时间")
	public long 棉花糖结束时间;
	@ChangeAble("棉花糖结束时间_tw")
	public long 棉花糖结束时间_tw;
	public boolean 棉花糖开关 = true;

	public void init() throws Exception {
		
		em_record = SimpleEntityManagerFactory.getSimpleEntityManager(MonthCardRecord.class);
		em_charge = SimpleEntityManagerFactory.getSimpleEntityManager(ChargeRecord.class);
		load();
		loadSpecialServerPrize();
		instance = this;
//		ActivityThread at = ActivityThread.getInstance();
//		at.init();
//		Thread t = new Thread(at, "排行榜活动");
//		t.start();

		File file = new File(diskFile);
		disk = new DefaultDiskCache(file, null, "activity", 100L * 365 * 24 * 3600 * 1000L);

		init大奉送();

		if(TestServerConfigManager.isTestServer()){
			openFunction = true;
		}
		
		initActivityFile();
		// initExpActivity();
		// initTumotieExpActivity();
		// //
		// initAddrateActivity();

		MConsoleManager.register(getInstance());
		ServiceStartRecord.startLog(this);
	}

	public static void main(String[] args) throws Exception {
		ActivityManager am = new ActivityManager();
		am.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//activity//mulActivities.xls");
		am.initActivityFile();
	}

	private AddRateActivity getAddRateActivity(HSSFRow row, String activityType) throws Exception {
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";
		double mults = 1;
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		startTime = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		endTime = cell.getStringCellValue().trim();

		cell = row.getCell(rowNum++);
		platForm = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		if (cell != null) {
			openServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		if (cell != null) {
			notOpenServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		mults = (double) cell.getNumericCellValue();

		AddRateActivity ara = new AddRateActivity(startTime, endTime, platForm, openServerName, notOpenServerName);
		ara.setOtherVar(mults, activityType);

		return ara;
	}
	
	/**
	 * 月卡充值成功
	 * @param p
	 * @param cardName
	 */
	public String chargeSucc(Player p,String cardId){
		long now = System.currentTimeMillis();
		MonthCardActivity activity = null;
		if(monthCardActivity != null && monthCardActivity.size() > 0){
			for(MonthCardActivity a : monthCardActivity){
				if(a != null && a.getCardId().equals(cardId)){
					activity = a;
					break;
				}
			}
		}
		
		if(activity == null){
			if (ActivitySubSystem.logger.isInfoEnabled()) {
				ActivitySubSystem.logger.info("[保存月卡信息] [充值] [失败:充值卡类型不符合] [{}] [record:{}] [玩家:{}]", new Object[] { cardId,datas.size(), p.getLogString()});
			}
			return "充值卡类型不符合";
		}
		
		if(!activity.isEffectActivity(p)){
			if (ActivitySubSystem.logger.isInfoEnabled()) {
				ActivitySubSystem.logger.info("[保存月卡信息] [充值] [失败:玩家不满足活动条件] [{}] [record:{}] [玩家:{}]", new Object[] { cardId,datas.size(), p.getLogString()});
			}
			return "玩家不满足活动条件";
		}
		
		MonthCardRecord record = getMonthCardRecord(p, activity.getCardName());
		//首冲
		boolean isfirstcharge = false;
		if(record == null){
			isfirstcharge = true;
			record = new MonthCardRecord();
			record.setHaveDays(System.currentTimeMillis()+activity.getLastDays()*MONTH_CARD_LAST_TIME);
		}else{
			record.setHaveDays(record.getHaveDays() + activity.getLastDays()*MONTH_CARD_LAST_TIME);
		}
		record.setCardName(activity.getCardName());
		try {
			if(record.getId() <= 0){
				record.setId(em_record.nextId());
			}
			record.setPid(p.getId());
			em_record.flush(record);
			MonthCardRecord data = null;
			for(MonthCardRecord re : datas){
				if(re.getCardName().equals(activity.getCardName()) && re.getPid() == p.getId()){
					data = re;
				}
			}
			if(data == null){
				datas.add(record);
			}else{
				data.setHaveDays(record.getHaveDays());
			}
			p.sendError(Translate.月卡购买成功+activity.getCardName());
			
			if(activity.getChargeRewardNames() != null && activity.getChargeRewardNames().length > 0 && activity.getChargeRewardColors() != null && activity.getChargeRewardColors().length > 0){
				long ids [] = new long[activity.getChargeRewardNames().length];
				List<ArticleEntity> aes = new ArrayList<ArticleEntity>();
				for(int j=0;j<activity.getChargeRewardNames().length;j++){
					String name = activity.getChargeRewardNames()[j];
					Article a = ArticleManager.getInstance().getArticle(name);
					if(a == null){
						p.sendError(Translate.赛季奖励配置错误);
						ActivitySubSystem.logger.warn("[月卡活动界面] [出错] [配置的立返奖励不存在] [卡名:{}] [奖励:{}]",new Object[]{activity.getCardName(),name});
						return "配置的立返奖励不存在";
					}
					try {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.月卡活动立返奖励, p, activity.getChargeRewardColors()[j], activity.getChargeRewardCounts()[j], true);
						aes.add(ae);
						ids[j] = ae.getId();
					} catch (Exception e) {
						e.printStackTrace();
						p.sendError(Translate.赛季奖励配置错误);
						ActivitySubSystem.logger.warn("[月卡活动界面] [出错] [创建物品] [卡名:{}] [奖励:{}]",new Object[]{activity.getCardName(),name});
						return "创建物品异常";
					}
				}
				if(!p.putAll(aes.toArray(new ArticleEntity[]{}), "月卡立返奖励")){
					MailManager.getInstance().sendMail(p.getId(), aes.toArray(new ArticleEntity[]{}), Translate.恭喜您获得了月卡奖励, Translate.恭喜您获得了月卡奖励, 0, 0, 0, "月卡日返奖励");
				}
			}
			
			MONTH_CARD_ACTIVITY_BUY_RES res = new MONTH_CARD_ACTIVITY_BUY_RES(GameMessageFactory.nextSequnceNum(), true);
			p.addMessageToRightBag(res);
			if (ActivitySubSystem.logger.isInfoEnabled()) {
				ActivitySubSystem.logger.info("[保存月卡信息] [充值] [成功] [isfirstcharge:{}] [datas:{}] [剩余:{}] [信息:{}] [耗时:{}ms]", new Object[] { isfirstcharge,datas.size(), record.getHaveDays(),record, (System.currentTimeMillis() - now) });
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (ActivitySubSystem.logger.isInfoEnabled()) {
				ActivitySubSystem.logger.info("[保存月卡信息] [充值] [异常] [record:{}] [信息:{}] [{}] ", new Object[] { datas.size(), record,e});
			}
			return "异常";
		}
		return null;
	}
	
	public MonthCardRecord getMonthCardRecord(Player p,String cardName) {
		long now = System.currentTimeMillis();
		for(MonthCardRecord d : datas){
			if(d != null && d.getCardName() != null){
				if(d.getPid() == p.getId() && d.getCardName().equals(cardName)){
					return d;
				}
			}
		}
		try {
			List<MonthCardRecord> list = em_record.query(MonthCardRecord.class, " pid = "+p.getId()+" AND cardName = '" + cardName + "'", "", 1, 100);
			if (ActivitySubSystem.logger.isInfoEnabled()) {
				ActivitySubSystem.logger.info("[获取月卡信息] [成功] [{}] [list:{}] [cardName:{}] [耗时:{}ms]", new Object[] {p.getLogString(), list == null ? "0" : list.size(),cardName, (System.currentTimeMillis() - now) });
			}
			if(list != null && list.size() > 0){
				datas.add(list.get(0));
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActivitySubSystem.logger.error("[获取月卡信息] [异常] [{}] [cardName:{}] [耗时:{}ms] [异常:{}]", new Object[] {p.getLogString(), cardName, (System.currentTimeMillis() - now), e });
		}
		return null;
	}
	
	public ChargeRecord getChargeRecord(Player player){
		long now = System.currentTimeMillis();
		for(ChargeRecord record : records){
			if(record.getId() == player.getId()){
				return record;
			}
		}
		
		try {
			List<ChargeRecord> list = em_charge.query(ChargeRecord.class, " id = "+player.getId(), "", 1, 100);
			if (ActivitySubSystem.logger.isInfoEnabled()) {
				ActivitySubSystem.logger.info("[获取充值送礼包信息] [成功] [{}] [list:{}] [耗时:{}ms]", new Object[] {player.getLogString(), list == null ? "0" : list.size(),(System.currentTimeMillis() - now) });
			}
			if(list != null && list.size() > 0){
				records.add(list.get(0));
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActivitySubSystem.logger.error("[获取充值送礼包信息] [异常] [{}] [耗时:{}ms] [异常:{}]", new Object[] {player.getLogString(), (System.currentTimeMillis() - now), e });
		}
		
		ChargeRecord record = new ChargeRecord();
		record.setId(player.getId());
		try {
			em_charge.flush(record);
			records.add(record);
			ActivitySubSystem.logger.info("[获取充值送礼包信息] [成功] [首冲新记录] [records:{}] [玩家:{}]",new Object[]{records.size(), player.getLogString()});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return record;
	}
	
	/**
	 * 是否弹框通知充值的礼包活动
	 * @param player
	 * @return
	 */
	public boolean isNoticeChargePage(Player player){
		if(!isOpenYunYingActivity){
			return false;
		}
		if(chargePackageActivity == null || chargePackageActivity.size() == 0){
			return false;
		}
		boolean isEffect = false;
		for(ChargePackageActivity activity : chargePackageActivity){
			if(activity.isEffectActivity(player)){
				isEffect = true;
				break;
			}
		}
		if(!isEffect){
			return false;
		}
		ChargeRecord record = getChargeRecord(player);
		if(ActivityManagers.isSameDay(System.currentTimeMillis(), record.getLastChargeTime())){
			return false;
		}
		return true;
	}
	
	public void handleChargePage(Player player){
		long now = System.currentTimeMillis();
		ChargeRecord record = getChargeRecord(player);
		if(!ActivityManagers.isSameDay(now, record.getLastChargeTime())){
			try {
				int oldDays = record.getDays();
				if(now - record.getLastChargeTime() > ONE_DAY){
					ActivitySubSystem.logger.info("[更新玩家充值获得礼包记录] [上线重置更新记录] [连冲天数:{}] [玩家:{}]",new Object[]{oldDays,player.getLogString()} );
					record.setDays(0);
				}
				em_charge.flush(record);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int nextDays = record.getDays() + 1;
		ChargePackageActivity activity = null;
		int maxDays = 0;
		for(ChargePackageActivity a : chargePackageActivity){
			if(a.getDays() > maxDays){
				maxDays = a.getDays();
			}
		}
		
		if(nextDays > maxDays){
			nextDays = maxDays;
		}
		
		for(ChargePackageActivity a : chargePackageActivity){
			if(nextDays == a.getDays()){
				activity = a;
			}
		}
		
		if(activity == null){
			ActivitySubSystem.logger.warn("[通知玩家充值获取礼包界面] [出错:活动配置错误] [玩家:{}]",new Object[]{player.getLogString()});
			return;
		}
		Article a = ArticleManager.getInstance().getArticle(activity.getPackageName());
		if(a == null){
			ActivitySubSystem.logger.warn("[通知玩家充值获取礼包界面] [出错:物品不存在] [{}] [玩家:{}]",new Object[]{activity.getPackageName(), player.getLogString()});
			return;
		}
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(activity.getPackageName(), true, a.getColorType());
			if(ae == null){
				ae = ArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.每日充值礼包活动, player, a.getColorType());
			}		
			if(ae == null){
				ActivitySubSystem.logger.warn("[通知玩家充值获取礼包界面] [出错:ae is null] [{}] [玩家:{}]",new Object[]{activity.getPackageName(), player.getLogString()});
				return;
			}
			CHARGE_GET_PACKAGE_RES res = new CHARGE_GET_PACKAGE_RES(GameMessageFactory.nextSequnceNum(), activity.getTitleMess(), activity.getContentMess(), ae.getId());
//			WindowManager windowManager = WindowManager.getInstance();
//			MenuWindow mw = windowManager.createTempMenuWindow(600);
//			String contentmess = Translate.translateString(Translate.花xx元购买xx, new String[][]{{Translate.COUNT_1,String.valueOf(activity.getMoney())},{Translate.STRING_2,activity.getPackageName()}});
//			mw.setDescriptionInUUB(contentmess);
//			mw.setTitle(Translate.text_419);
//			Option_EveryDay_Charge_Sure sure = new Option_EveryDay_Charge_Sure();
//			sure.setText(Translate.确定);
//			sure.setRes(res);
//			Option_Cancel oc = new Option_Cancel();
//			oc.setText(Translate.取消);
//			mw.setOptions(new Option[]{sure,oc});
//			QUERY_WINDOW_RES req_win = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
			ActivitySubSystem.logger.warn("[通知玩家充值获取礼包界面] [成功] [玩家:{}] [nextDays:{}] [maxDays:{}]",new Object[]{player.getLogString(),nextDays,maxDays});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean updateChargeRecord(Player player){
		try{
			long now = System.currentTimeMillis();
			ChargeRecord record = getChargeRecord(player);
			int nextDays = record.getDays() + 1;
			ChargePackageActivity activity = null;
			int maxDays = 0;
			for(ChargePackageActivity a : chargePackageActivity){
				if(a.getDays() > maxDays){
					maxDays = a.getDays();
				}
			}
			
			if(nextDays > maxDays){
				nextDays = maxDays;
			}
			
			for(ChargePackageActivity a : chargePackageActivity){
				if(nextDays == a.getDays()){
					activity = a;
				}
			}
			
			if(activity == null){
				ActivitySubSystem.logger.warn("[玩家充值获得礼包] [发奖励出错:活动配置错误] [玩家:{}]",new Object[]{player.getLogString()});
				return false;
			}
			
			Article a = ArticleManager.getInstance().getArticle(activity.getPackageName());
			if(a == null){
				ActivitySubSystem.logger.warn("[玩家充值获得礼包] [发奖励出错:物品不存在] [{}] [玩家:{}]",new Object[]{activity.getPackageName(), player.getLogString()});
				return false;
			}
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.每日充值礼包活动, player, a.getColorType(), 1, true); 
				if(ae == null){
					ActivitySubSystem.logger.warn("[玩家充值获得礼包] [发奖励出错:ae is null] [{}] [玩家:{}]",new Object[]{activity.getPackageName(), player.getLogString()});
					return false;
				}
				
				if(!ActivityManagers.isSameDay(now, record.getLastChargeTime())){
					int oldDays = record.getDays();
					if(now - record.getLastChargeTime() < ONE_DAY){
						record.setDays(record.getDays() + 1);
						ActivitySubSystem.logger.info("[更新玩家充值获得礼包记录] [更新记录] [连冲天数变化:{}] [玩家:{}]",new Object[]{oldDays+"-->"+ record.getDays(),player.getLogString()} );
					}else{
						record.setDays(0);
						ActivitySubSystem.logger.info("[更新玩家充值获得礼包记录] [重置记录] [连冲天数变化:{}] [玩家:{}]",new Object[]{oldDays+"-->"+ record.getDays(),player.getLogString()} );
					}
					record.setLastChargeTime(now);
					em_charge.flush(record);
				}
				
				if(!player.putAll(new ArticleEntity[]{ae}, "充值获得礼包")){
					String mess = Translate.translateString(Translate.恭喜您获得了, new String[][]{{Translate.STRING_1,activity.getPackageName()},{Translate.COUNT_1,String.valueOf(1)}});
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, mess, mess, 0, 0, 0, "充值获得礼包");
				}
				ActivitySubSystem.logger.warn("[玩家充值获得礼包] [发奖励成功] [玩家:{}] [nextDays:{}] [maxDays:{}]",new Object[]{player.getLogString(),nextDays,maxDays});
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch (Exception e) {
			e.printStackTrace();
			ActivitySubSystem.logger.info("[更新玩家充值获得礼包记录] [异常:{}]",e);
		}
		return false;
	}
	
	private AddRateActivity getJifenAddRateActivity(HSSFRow row, String activityType) throws Exception {
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";
		double mults = 0;

		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		startTime = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		endTime = cell.getStringCellValue().trim();

		cell = row.getCell(rowNum++);
		platForm = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		if (cell != null) {
			openServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		if (cell != null) {
			notOpenServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		mults = cell.getNumericCellValue();


		AddRateActivity ara = new AddRateActivity(startTime, endTime, platForm, openServerName, notOpenServerName);
		ara.setOtherVar(mults, activityType);

		return ara;
	}

	/**
	 * 通用经验与封魔录经验都使用此方法
	 * @param row
	 * @return
	 * @throws Exception
	 */
	private DailyfExpActivity getExpActivity(HSSFRow row, String activityType) throws Exception {
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";
		String activityName = "";

		String timeHours = "";
		String timeMinits = "";
		double mults = 1;
		String limitMaps = "";

		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		startTime = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		endTime = cell.getStringCellValue().trim();

		cell = row.getCell(rowNum++);
		platForm = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		if (cell != null) {
			openServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		if (cell != null) {
			notOpenServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		activityName = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		timeHours = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		timeMinits = cell.getStringCellValue().trim();

		cell = row.getCell(rowNum++);
		mults = cell.getNumericCellValue();

		cell = row.getCell(rowNum++);
		if (cell != null) {
			limitMaps = cell.getStringCellValue().trim();
		}

		List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();
		String[] tempP1 = timeHours.split("\\|");
		String[] tempP2 = timeMinits.split("\\|");
		if (tempP1.length != tempP2.length) {
			throw new Exception("小时与分钟配表不匹配！");
		}
		for (int i = 0; i < tempP1.length; i++) {
			int[] point1 = RefreshSpriteManager.parse2Int(tempP1[i].split(","));
			int[] point2 = RefreshSpriteManager.parse2Int(tempP2[i].split(","));
			if ((point1.length != 2) || (point2.length != 2)) {
				throw new Exception("小时或者分钟开启结束时间不匹配！");
			}
			times.add(new DayilyTimeDistance(point1[0], point2[0], point1[1], point2[1]));
		}
		DailyfExpActivity dea = new DailyfExpActivity(startTime, endTime, platForm, openServerName, notOpenServerName);
		dea.setOtherVar(activityType, mults, activityName, times);
		if (!"".equals(limitMaps)) {
			String[] lms = limitMaps.split(",");
			Set<String> limaps = new HashSet<String>();
			for (String m : lms) {
				if (!"".equals(m)) {
					limaps.add(m);
				}
			}
			dea.setLimitmaps(limaps);
		}
		return dea;
	}

	/**
	 * 初始化天降宝箱
	 * @param row
	 * @param activityType
	 * @throws Exception
	 */
	public void loadGodDownActivity(HSSFRow row) throws Exception {
		long now = System.currentTimeMillis();
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";

		String timeHours = "";
		String rewardnames = "";
		String liziname = "";
		boolean hasFlowerDown = false;

		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		startTime = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		endTime = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		platForm = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		if (cell != null) {
			openServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		if (cell != null) {
			notOpenServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		timeHours = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		rewardnames = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		if(cell != null){
			liziname = cell.getStringCellValue().trim();
		}
		hasFlowerDown = StringTool.getCellValue(row.getCell(rowNum++), Boolean.class);
		GodDownActivityConfig gc = new GodDownActivityConfig(startTime, endTime, platForm, openServerName, notOpenServerName);
		gc.setStartTimes(timeHours, rewardnames, hasFlowerDown);
		gc.setLiZiName(liziname);
		godDownActivity.add(gc);
		ActivitySubSystem.logger.warn("[天降宝箱活动] [加载成功] [数量:{}] [活动:{}] [耗时:{}]",new Object[]{godDownActivity.size(),gc.getInfoShow(), (System.currentTimeMillis() - now)});
	}
	
	/**
	 * 送花活动
	 * @param row
	 * @throws Exception
	 */
	public void loadSendFlowerActivity(HSSFRow row) throws Exception {
		long now = System.currentTimeMillis();
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";

		String flowerName = "";
		int flowerNums = 0;
		String senderRewardArticleName = "";
		int senderRewardArticleNum = 0;
		String receiveRewardArticleName = "";
		int receiveRewardArtilceNum = 0;
		String liziname = "";
		boolean hasNotice = false;

		int rowNum = 0;
		startTime = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		endTime = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		platForm = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		openServerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		notOpenServerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		flowerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		flowerNums = StringTool.getCellValue(row.getCell(rowNum++), Integer.class);
		senderRewardArticleName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		senderRewardArticleNum = StringTool.getCellValue(row.getCell(rowNum++), Integer.class);
		receiveRewardArticleName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		receiveRewardArtilceNum = StringTool.getCellValue(row.getCell(rowNum++), Integer.class);
		liziname = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		hasNotice = StringTool.getCellValue(row.getCell(rowNum++), Boolean.class);
		
		SendFlowerActivityConfig gc = new SendFlowerActivityConfig(startTime, endTime, platForm, openServerName, notOpenServerName);
		gc.setArgs(flowerName, flowerNums, senderRewardArticleName, senderRewardArticleNum, receiveRewardArticleName, receiveRewardArtilceNum, liziname, hasNotice);
		sendFlowerActivity.add(gc);
		ActivitySubSystem.logger.warn("[送花活动] [加载成功] [数量:{}] [活动:{}] [耗时:{}]",new Object[]{sendFlowerActivity.size(),gc.getInfoShow(), (System.currentTimeMillis() - now)});
	}
	
	/**
	 * 月卡活动加载
	 * @param row
	 * @throws Exception
	 */
	public void loadMonthCardActivity(HSSFRow row) throws Exception {
		long now = System.currentTimeMillis();
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";

		String cardName = "";
		String cardId = "";
		int needMoney = 0;
		String chargeRewardName = "";
		String chargeRewardColor = "";
		String chargeRewardCount = "";
		String dayRewardName = "";
		String dayRewardColor = "";
		String dayRewardCount = "";
		String dayRewardInfo = "";
		String noticeMess = "";
		int levelLimit = 0;

		int rowNum = 0;
		startTime = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		endTime = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		platForm = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		openServerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		notOpenServerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		
		cardName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		cardId = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		needMoney = StringTool.getCellValue(row.getCell(rowNum++), Integer.class);
		chargeRewardName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		chargeRewardColor = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		chargeRewardCount = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		dayRewardName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		dayRewardColor = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		dayRewardCount = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		dayRewardInfo = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		noticeMess = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		levelLimit = StringTool.getCellValue(row.getCell(rowNum++), Integer.class);
		int lastdays = StringTool.getCellValue(row.getCell(rowNum++), Integer.class);
		
		String chargeRewardNames [] = chargeRewardName.split(",");
		int chargeRewardColors [] = ActivityManagers.getInstance().StringToInt(chargeRewardColor.split(","));
		int chargeRewardCounts [] = ActivityManagers.getInstance().StringToInt(chargeRewardCount.split(","));
		int dayRewardColors [] = ActivityManagers.getInstance().StringToInt(dayRewardColor.split(","));
		int dayRewardCounts [] = ActivityManagers.getInstance().StringToInt(dayRewardCount.split(","));
		String dayRewardNames [] = dayRewardName.split(",");
		if(chargeRewardNames == null || chargeRewardColors== null || chargeRewardCounts == null){
			throw new Exception("月卡活动,立返奖励配置错误");
		}
		if(dayRewardColors == null || dayRewardCounts== null || dayRewardNames == null){
			throw new Exception("月卡活动,日返奖励配置错误");
		}

		if(chargeRewardNames.length != chargeRewardColors.length || chargeRewardNames.length != chargeRewardCounts.length){
			throw new Exception("月卡活动,立返奖励配置错误,长度不一致");
		}
		
		if(dayRewardColors.length != dayRewardNames.length || dayRewardNames.length != dayRewardCounts.length){
			throw new Exception("月卡活动,日返奖励配置错误,长度不一致");
		}
		
		MonthCardActivity mcActivity = new MonthCardActivity(startTime, endTime, platForm, openServerName, notOpenServerName);
		mcActivity.setArgs(cardName,cardId, needMoney, chargeRewardNames, chargeRewardColors, chargeRewardCounts, dayRewardNames,dayRewardColors,dayRewardCounts, dayRewardInfo, noticeMess,levelLimit);
		mcActivity.setLastDays(lastdays);
		monthCardActivity.add(mcActivity);
		ActivitySubSystem.logger.warn("[月卡活动] [加载成功] [数量:{}] [活动:{}] [耗时:{}]",new Object[]{sendFlowerActivity.size(),mcActivity.getInfoShow(), (System.currentTimeMillis() - now)});
	}
	
	/**
	 * 商店等级礼包活动
	 * @param row
	 * @throws Exception
	 */
	public void loadLevelPackageActivity(HSSFRow row) throws Exception {
		long now = System.currentTimeMillis();
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";
		int levelLimit = 0;

		int days;
		long money = 0;
		String packageName;
		String titleMess;	
		String contentMess;

		int rowNum = 0;
		startTime = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		endTime = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		platForm = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		openServerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		notOpenServerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		
		days = StringTool.getCellValue(row.getCell(rowNum++), Integer.class);
		money = StringTool.getCellValue(row.getCell(rowNum++), Long.class);
		packageName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		titleMess = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		contentMess = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		levelLimit = StringTool.getCellValue(row.getCell(rowNum++), Integer.class);
		
		ChargePackageActivity mcActivity = new ChargePackageActivity(startTime, endTime, platForm, openServerName, notOpenServerName);
		mcActivity.setArgs(days, money, packageName, titleMess, contentMess,levelLimit);
		chargePackageActivity.add(mcActivity);
		ActivitySubSystem.logger.warn("[商店充值等级礼包活动] [加载成功] [数量:{}] [活动:{}] [耗时:{}]",new Object[]{chargePackageActivity.size(),mcActivity.getInfoShow(), (System.currentTimeMillis() - now)});
	}
	
	public List<ActivityConfig> configs = new ArrayList<ActivityConfig>();
	
	public List<VipLoginConfig> vipConfigs = new ArrayList<VipLoginConfig>();

	public void loadVipLoginActivity(HSSFRow row) throws Exception {
		long now = System.currentTimeMillis();
		int rowNum = 0;
		try {
			VipLoginConfig config = new VipLoginConfig();
			Map<Integer,String> configs = config.getConfigs();
			String startTimeStr = StringTool.getCellValue(row.getCell(rowNum++), String.class);
			String endStr = StringTool.getCellValue(row.getCell(rowNum++), String.class);
			for(int i=0;i<11;i++){
				configs.put((i+1), StringTool.getCellValue(row.getCell(rowNum++), String.class));
			}
			config.setStartTime(TimeTool.formatter.varChar19.parse(startTimeStr));
			config.setEndTime(TimeTool.formatter.varChar19.parse(endStr));
			config.setConfigs(configs);
			config.init();
			
			vipConfigs.add(config);
			ActivitySubSystem.logger.warn("[加载VIP登录配置] [加载成功] [数量:{}] [配置:{}] [耗时:{}]",new Object[]{vipConfigs.size(),config,(System.currentTimeMillis() - now)});
		} catch (Exception e) {
			e.printStackTrace();
			ActivitySubSystem.logger.warn("[加载VIP登录配置] [加载异常] [数量:{}] [耗时:{}] [{}]",new Object[]{vipConfigs.size(),(System.currentTimeMillis() - now),e});
			throw new Exception(e);
		}
	}
	
	public String sameDayKey = "yyyy-MM-dd";
	
	public boolean isSameDay(long time1, long time2) {
		SimpleDateFormat sdf = new SimpleDateFormat(sameDayKey);
		String str1 = sdf.format(time1);
		String str2 = sdf.format(time2);
		return str1.equals(str2);
	}
	
	public int levelLimit = 70;
	
	public void vipLoginActivity(Player p){
		try {
			if(p.getLevel() < levelLimit){
				return;
			}
			long lastRewardTime = p.getLastVipLoginRewardTime();
			if(!isSameDay(lastRewardTime, System.currentTimeMillis())){
				if(vipConfigs != null && vipConfigs.size() > 0){
					for(VipLoginConfig config : vipConfigs){
						if(config != null && config.isOpen()){
							String name = config.getRewardName(p.getVipLevel());
							int count = config.getRewardNum(p.getVipLevel());
							Article article = ArticleManager.getInstance().getArticle(name);
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true,  ArticleEntityManager.vip登录活动, p, article.getColorType(), count, true);
							MailManager.getInstance().sendMail(p.getId(), new ArticleEntity[]{ae}, new int[]{count}, "五周年登陆福利", "飘渺寻仙曲五周年专属福卡,集齐不同数量福卡可到【周年福利大使】处兑换专属五周年福利哦！", 0, 0, 0, "五周年登陆福利");
							p.setLastVipLoginRewardTime(System.currentTimeMillis());
							ActivitySubSystem.logger.warn("[vip登录活动] [奖励成功] [物品:"+name+"] [数量:"+count+"] [vipLevel:"+p.getVipLevel()+"] ["+p.getLogString()+"]");
							break;
						}else{
							ActivitySubSystem.logger.warn("[vip登录活动] [活动没开启] [vipConfigs:"+(vipConfigs!=null?vipConfigs.size():"nul")+"] [config:"+config+"] [vipLevel:"+p.getVipLevel()+"] ["+p.getLogString()+"]");
						}
					}
				}else{
					ActivitySubSystem.logger.warn("[vip登录活动] [活动没没配置] [vipConfigs:"+(vipConfigs!=null?vipConfigs.size():"nul")+"] [vipLevel:"+p.getVipLevel()+"] ["+p.getLogString()+"]");
				}
			}else{
				ActivitySubSystem.logger.warn("[vip登录活动] [奖励失败:今天已经领取过] [vipLevel:"+p.getVipLevel()+"] ["+p.getLogString()+"]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActivitySubSystem.logger.warn("[vip登录活动] [异常] [vipLevel:"+p.getVipLevel()+"] ["+p.getLogString()+"]",e);
		}
	}
	
	public void loadWolfActivity(HSSFRow row) throws Exception {
		long now = System.currentTimeMillis();
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";
		String hourStr = "";
		String minuteStr = "";
		String timeshow = "";
		
		
		int rowNum = 0;
		startTime = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		endTime = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		platForm = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		openServerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		notOpenServerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		hourStr = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		minuteStr = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		timeshow = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		
		TimeRangeConfig config = new TimeRangeConfig();
		if(startTime == null || startTime == null){
			throw new Exception("狼吃羊活动加载异常:时间不能为空");
		}
		config.setStartTime(TimeTool.formatter.varChar19.parse(startTime));
		config.setEndTime(TimeTool.formatter.varChar19.parse(endTime));
		
		String[] temp1 = platForm.split(",");
		Platform[] platForms = new Platform[temp1.length];
		for(int i=0; i<temp1.length; i++) {	
			platForms[i] = getPlatForm(temp1[i].trim());
		}
		config.setPlatForms(platForms);
		
		String[] temp2 = openServerName.split(",");
		String[] temp3 = notOpenServerName.split(",");
		if(!"".equals(openServerName) && !"ALL_SERVER".equals(openServerName)) {				//开启活动服务器名
			List<String> openServerNames = new ArrayList<String>();
			for(int i=0; i<temp2.length; i++) {		
				openServerNames.add(temp2[i].trim());
			}
			config.setOpenServerNames(openServerNames);
		}
		if(!"".equals(notOpenServerName)) {				
			List<String> notOpenServerNames = new ArrayList<String>();
			for(int i=0; i<temp3.length; i++) {		
				notOpenServerNames.add(temp3[i].trim());
			}
			config.setNotOpenServerNames(notOpenServerNames);
		}
		
		List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();
		String[] tempP1 = hourStr.split("\\|");
		String[] tempP2 = minuteStr.split("\\|");
		if (tempP1.length != tempP2.length) {
			throw new Exception("狼吃羊活动加载失败：小时与分钟配表不匹配！");
		}
		for (int i = 0; i < tempP1.length; i++) {
			int[] point1 = RefreshSpriteManager.parse2Int(tempP1[i].split(","));
			int[] point2 = RefreshSpriteManager.parse2Int(tempP2[i].split(","));
			if ((point1.length != 2) || (point2.length != 2)) {
				throw new Exception("狼吃羊活动加载失败：小时或者分钟开启结束时间不匹配！");
			}
			times.add(new DayilyTimeDistance(point1[0], point2[0], point1[1], point2[1]));
		}
		config.setTimes(times);
		config.setTimeShow(timeshow);
		
		configs.add(config);
		ActivitySubSystem.logger.warn("[狼吃羊活动] [加载成功] [数量:{}] [活动:{}] [timeshow:{}] [耗时:{}]",new Object[]{configs.size(),config, timeshow,(System.currentTimeMillis() - now)});
	}
	
	private Platform getPlatForm(String pN) throws Exception {
		for(Platform p : Platform.values()) {
			if(p.getPlatformName().equals(pN)) {
				return p;
			}
		}
		throw new Exception("配置了错误的平台名{}" + pN);
	}
	
	/**
	 * 每日充值的礼包
	 * @param row
	 * @throws Exception
	 */
	public void loadChargePackageActivity(HSSFRow row) throws Exception {
		long now = System.currentTimeMillis();
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";

		String shopName = "";
		int lowLevel = 0;
		int maxLevel = 0;
		String goodsNames = "";
		String moneystr = "";

		int rowNum = 0;
		startTime = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		endTime = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		platForm = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		openServerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		notOpenServerName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		
		shopName = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		lowLevel = StringTool.getCellValue(row.getCell(rowNum++), Integer.class);
		maxLevel = StringTool.getCellValue(row.getCell(rowNum++), Integer.class);
		goodsNames = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		moneystr = StringTool.getCellValue(row.getCell(rowNum++), String.class);
		
		String gs [] = goodsNames.split(",");
		int moneys [] = ActivityManagers.getInstance().StringToInt(moneystr.split(","));
		
		if(gs == null || moneys== null){
			throw new Exception("商店充值等级礼包活动,商品和钱配置错误");
		}
		
		if(gs.length != moneys.length){
			throw new Exception("商店充值等级礼包活动,商品和钱配置错误,长度不一致");
		}
		
		LevelPackageActivity mcActivity = new LevelPackageActivity(startTime, endTime, platForm, openServerName, notOpenServerName);
		mcActivity.setArgs(shopName, lowLevel, maxLevel, gs,moneys);
		levelPackageActivity.add(mcActivity);
		ActivitySubSystem.logger.warn("[商店充值等级礼包活动] [加载成功] [数量:{}] [活动:{}] [耗时:{}]",new Object[]{levelPackageActivity.size(),mcActivity.getInfoShow(), (System.currentTimeMillis() - now)});
	}
	
	/**
	 * 商店中的商品按照玩家等级显示
	 * @param shopName
	 * @return
	 */
	public boolean showByPlayerLevel(String shopName){
		for(LevelPackageActivity activity : levelPackageActivity){
			if(activity != null && activity.getShopName().equals(shopName)){
				return true;
			}
		}
		return false;
	}
	
	public Goods[] getLevelGoods(String shopName,Player player,Goods[] goods){
		LevelPackageActivity activity = null;
		for(LevelPackageActivity a : levelPackageActivity){
			if(a != null && a.getShopName().equals(shopName)){
				activity = a;
				break;
			}
		}
		ShopManager.logger.warn("[等级商店测试] [playername:"+player.getName()+"] [shopName:"+shopName+"] ["+activity+"]");
		if(activity == null){
			return goods;
		}
		ShopManager.logger.warn("[等级商店测试] [playername:"+player.getName()+"] [shopName:"+shopName+"] ["+activity.isEffectActivity(player)+"] ["+activity+"]");
		
		if(!activity.isEffectActivity(player)){
			return goods;
		}
		
		List<Goods> newGoods = new ArrayList<Goods>();
		for(Goods g : goods){
			for(LevelPackageActivity a : levelPackageActivity){
				if(a.getLowLevel() <= player.getLevel() && player.getLevel() <= a.getMaxLevel()){
					if(a != null && a.getShopName().equals(shopName) && a.getGoodsname() != null && a.getMoneys() != null){
						for(int i=0;i<a.getGoodsname().length;i++){
							if(a.getGoodsname()[i].equals(g.getArticleName_stat())){
								g.setPrice(a.getMoneys()[i] * 100);
								newGoods.add(g);
							}
						}
					}
				}
			}
		}
		ShopManager.logger.warn("[等级商店测试] [playername:"+player.getName()+"] [shopName:"+shopName+"] [goods:"+goods.length+"] [newGoods:"+newGoods.size()+"] ["+activity.isEffectActivity(player)+"] ["+activity+"]");
		if(newGoods.size() <= 0){
			return goods;
		}
		return newGoods.toArray(new Goods[] {});
	}
	
	
	/**
	 * 读配表初始化活动
	 * @throws Exception
	 * @throws IOException
	 * @throws FileNotFoundException
	 *             excel配表
	 */
	private void initActivityFile() throws Exception {
		File f = new File(fileName);
		f = new File(ConfigServiceManager.getInstance().getFilePath(f));
		if (!f.exists()) {
			throw new Exception("mulActivities.xls配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(EXP_ACTIVITY_SHEET); // 所有经验活动
		int rows = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> allExpList = new ArrayList<BaseActivityInstance>();
		List<BaseActivityInstance> tumoExpList = new ArrayList<BaseActivityInstance>();
		List<BaseActivityInstance> chuangongExpList = new ArrayList<BaseActivityInstance>();
		List<BaseActivityInstance> datiExpList = new ArrayList<BaseActivityInstance>();
		List<BaseActivityInstance> jifenExpList = new ArrayList<BaseActivityInstance>();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				DailyfExpActivity temp = getExpActivity(row, AllActivityManager.allExpAct);
				expActivityList.add(temp);
				allExpList.add(temp);
			} catch (Exception e) {
				throw new Exception("所有经验活动sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}

		sheet = workbook.getSheetAt(TUMOTIE_ACTIVITY_SHEET); // 封魔录活动
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				DailyfExpActivity temp = getExpActivity(row, AllActivityManager.tumoExpAct);
				tumotieActivityList.add(temp);
				tumoExpList.add(temp);
			} catch (Exception e) {
				throw new Exception("封魔录活动sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}
		

		sheet = workbook.getSheetAt(CHUANGONG_ACTIVITY_SHEET); // 传功活动
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				AddRateActivity temp = getAddRateActivity(row, AllActivityManager.chuangongEnergyAct);
				chuanGongActivity.add(temp);
				chuangongExpList.add(temp);
			} catch (Exception e) {
				throw new Exception("传功活动sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}

		sheet = workbook.getSheetAt(DATI_ACTIVITY_SHEET); // 答题活动
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				AddRateActivity temp = getAddRateActivity(row, AllActivityManager.datiJifenAct);
				datiActivity.add(temp);
				datiExpList.add(temp);
			} catch (Exception e) {
				throw new Exception("答题活动sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}

		sheet = workbook.getSheetAt(JIFEN_ACTIVITY_SHEET); // 答题活动
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				AddRateActivity temp = getJifenAddRateActivity(row, AllActivityManager.jifenAct);
				jifenActivity.add(temp);
				jifenExpList.add(temp);
			} catch (Exception e) {
				throw new Exception("积分活动sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(XUESHI_ACTIVITY_SHEET);	
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				AddRateActivity temp = getJifenAddRateActivity(row, AllActivityManager.xueShiActivity);
				xueShiActivity.add(temp);
//				jifenExpList.add(temp);		//如果别的地方做此类活动，请加一下
			} catch (Exception e) {
				throw new Exception("血石活动sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(GODDOWN_ACTIVITY_SHEET); //天降宝箱活动
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 2; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				loadGodDownActivity(row);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("天降宝箱活动sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(FLOWER_ACTIVITY_SHEET); //送花活动
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 2; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				loadSendFlowerActivity(row);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("送花活动sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(MONTH_CARD_ACTIVITY_SHEET); //月卡活动
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				ActivitySubSystem.logger.warn("[加载月卡活动] [row==null] ["+i+"]");
				continue;
			}
			try {
				loadMonthCardActivity(row);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("月卡活动sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}
		 
		sheet = workbook.getSheetAt(LEVEL_PACKAGE_ACTIVITY_SHEET); //等级限制礼包活动
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				ActivitySubSystem.logger.warn("[加载等级限制礼包活动] [row==null] ["+i+"]");
				continue;
			}
			try {
				loadChargePackageActivity(row);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("等级限制礼包sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(CHARGE_PACKAGE_ACTIVITY_SHEET); //充值的礼包活动
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				ActivitySubSystem.logger.warn("[加载充值购买礼包活动] [row==null] ["+i+"]");
				continue;
			}
			try {
				loadLevelPackageActivity(row);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("加载充值购买礼包sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}
		
		sheet = workbook.getSheetAt(WOLF_ACTIVITY_SHEET); 
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				ActivitySubSystem.logger.warn("[加载狼吃羊活动] [row==null] ["+i+"]");
				continue;
			}
			try {
				loadWolfActivity(row);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("加载狼吃羊sheet页，第" + (i + 1) + "行异常！！", e);
			}
		}
		
		
		AllActivityManager.instance.add2AllActMap(AllActivityManager.allExpAct, allExpList);
		AllActivityManager.instance.add2AllActMap(AllActivityManager.tumoExpAct, tumoExpList);
		AllActivityManager.instance.add2AllActMap(AllActivityManager.chuangongEnergyAct, chuangongExpList);
		AllActivityManager.instance.add2AllActMap(AllActivityManager.datiJifenAct, datiExpList);
		AllActivityManager.instance.add2AllActMap(AllActivityManager.jifenAct, jifenExpList);
	}

	public List<AddRateActivity> getChuanGongActivity() {
		return chuanGongActivity;
	}

	public void setChuanGongActivity(List<AddRateActivity> chuanGongActivity) {
		this.chuanGongActivity = chuanGongActivity;
	}

	public List<AddRateActivity> getDatiActivity() {
		return datiActivity;
	}

	public void setDatiActivity(List<AddRateActivity> datiActivity) {
		this.datiActivity = datiActivity;
	}

	public List<AddRateActivity> getJifenActivity() {
		return jifenActivity;
	}

	public void setJifenActivity(List<AddRateActivity> jifenActivity) {
		this.jifenActivity = jifenActivity;
	}

	public List<AddRateActivity> getXueShiActivity() {
		return this.xueShiActivity;
	}

	public void setXueShiActivity(List<AddRateActivity> xueShiActivity) {
		this.xueShiActivity = xueShiActivity;
	}

	/** 棉花糖奖励 **/
	class MHTReward implements Runnable {

		@Override
		public void run() {
			while (棉花糖开关) {
				if (System.currentTimeMillis() > 棉花糖结束时间) {
					棉花糖开关 = false;
					BillboardsManager.logger.error("[棉花糖奖励活动结束，关闭]");
				}
				try {
					Thread.sleep(10000);//
				} catch (Throwable e) {
					e.printStackTrace();
				}
				if (isEffective()) {
					sentPrize();
				}
			}
		}

		public boolean isSameDay(long time1, long time2) {
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(time1);
			int year1 = ca.get(Calendar.YEAR);
			int month1 = ca.get(Calendar.MONTH);
			int day1 = ca.get(Calendar.DAY_OF_MONTH);
			// int hour1=ca.get(Calendar.HOUR_OF_DAY);
			// int min1=ca.get(Calendar.MINUTE);

			ca.setTimeInMillis(time2);
			int year2 = ca.get(Calendar.YEAR);
			int month2 = ca.get(Calendar.MONTH);
			int day2 = ca.get(Calendar.DAY_OF_MONTH);
			// int hour2=ca.get(Calendar.HOUR_OF_DAY);
			// int min2=ca.get(Calendar.MINUTE);

			return year1 == year2 && month1 == month2 && day1 == day2;// &&hour1==hour2&&min1==min2;//
		}

		private boolean isEffective() {
			long now = System.currentTimeMillis();
			if (now < 棉花糖开始时间 || now > 棉花糖结束时间) {
				return false;
			}
			Calendar cl = Calendar.getInstance();
			if (cl.get(Calendar.HOUR_OF_DAY) == 23) {//
				/** 按日结算 **/
				if (cl.get(Calendar.MINUTE) == 58 || cl.get(Calendar.MINUTE) == 59) {//
					Long lastTime = (Long) disk.get("61孩子王201305281729");
					if (lastTime == null) {
						disk.put("61孩子王201305281729", System.currentTimeMillis());
						BillboardsManager.logger.error("【棉花糖排行榜】[添加新的统计数据]");
						return true;
					} else {
						if (!isSameDay(lastTime.longValue(), System.currentTimeMillis())) {
							disk.put("61孩子王201305281729", System.currentTimeMillis());
							BillboardsManager.logger.error("【棉花糖排行榜】[不是同一天的统计数据]");
							return true;
						}
					}
				}
			}
			return false;
		}

		// 1-10名奖励物品,数量，颜色
		String articleRewardName = Translate.六一专属稀有宠物碎片;

		int articleRewardNum[] = { 80, 20, 15, 10, 10, 5, 5, 5, 5, 5 };

		ArticleEntityManager am = ArticleEntityManager.getInstance();
		ChatMessageService cms = ChatMessageService.getInstance();

		ArticleEntity ae = null;

		private void sentPrize() {
			if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
				articleRewardName = "專屬稀有寵物碎片";
			}
			Billboard dayFlower = BillboardsManager.getInstance().getBillboard(Translate.魅力, Translate.当日棉花糖);
			if (dayFlower != null) {
				BillboardDate[] data = dayFlower.getDates();
				if (data != null && data.length > 0) {
					if (data.length < 10) {//
						BillboardsManager.logger.error("【棉花糖排行榜】[没有获得奖励] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [排行榜数量少于10，不符合活动规则] [数量：" + data.length + "]");
					} else {
						for (int i = 0; i < 10; i++) {//
							String datevalue = data[i].getDateValues()[3]; //
							if (datevalue != null && datevalue.trim().length() > 0 && Long.parseLong(datevalue) > 9999) {//
								Player player = null;
								try {
									player = PlayerManager.getInstance().getPlayer(data[i].getDateId());
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								if (player != null) {
									Article a = ArticleManager.getInstance().getArticle(articleRewardName);
									if (a == null) {
										BillboardsManager.logger.error("【棉花糖排行榜】[没有获得奖励] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [物品不存在,物品名:" + articleRewardName + "] [数量：" + data.length + "]");
										continue;
									}
									try {
										ae = am.createEntity(a, true, ArticleEntityManager.活动棉花糖, player, a.getColorType(), articleRewardNum[i], true);
										if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
											MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { articleRewardNum[i] }, "專屬稀有寵物碎片", Translate.孩子王大比拼_reward_tw + (i + 1) + Translate.当日棉花糖_reward2, 0, 0, 0, "61活动");
										} else {
											MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { articleRewardNum[i] }, Translate.孩子王大比拼, Translate.孩子王大比拼_reward + (i + 1) + Translate.当日棉花糖_reward2, 0, 0, 0, "61活动");
										}
										String msg = Translate.text_marriage_011 + "[" + player.getName() + Translate.text_3745 + Translate.孩子王大比拼 + Translate.中获得了第 + (i + 1) + Translate.名 + "!";
										if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
											msg = Translate.text_marriage_011 + "[" + player.getName() + Translate.text_3745 + Translate.孩子王大比拼_tw + Translate.中获得了第 + (i + 1) + Translate.名 + "!";
										}
										if (i <= 1) {
											ChatMessage chat = new ChatMessage();
											chat.setMessageText(msg);
											cms.sendMessageToSystem(chat);
										}
										BillboardsManager.logger.error("【棉花糖排行榜】【获得奖励】 [服务器：" + GameConstants.getInstance().getServerName() + "] [帐号：" + player.getUsername() + "] [角色名：" + player.getName() + "] [id:" + player.getId() + "] [第" + (i + 1) + "名] [奖励：" + ae.getArticleName() + "]");
									} catch (Exception e) {
										e.printStackTrace();
										BillboardsManager.logger.error("【棉花糖排行榜】【异常】 [服务器：" + GameConstants.getInstance().getServerName() + "] [帐号：" + player.getUsername() + "] [角色名：" + player.getName() + "] [id:" + player.getId() + "] [第" + (i + 1) + "名]", e);
									}
								}
							} else {
								BillboardsManager.logger.error("【棉花糖排行榜】[没有获得奖励] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [第十名积分低于10000分则全服无奖励] [积分：" + datevalue + "] [数量：" + data.length + "]");
							}
						}
					}
				} else {
					BillboardsManager.logger.error("【棉花糖排行榜】[没有获得奖励] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [排行榜数量为空]");
				}
			} else {
				BillboardsManager.logger.error("【棉花糖排行榜】[没有获得奖励] [服务器：" + GameConstants.getInstance().getServerName() + "] [原因] [排行榜为空]");
			}
		}

	}

//	public void initExpActivity() {
//
//		List<DayilyTimeDistance> times_for_腾讯0509 = new ArrayList<DayilyTimeDistance>();
//		times_for_腾讯0509.add(new DayilyTimeDistance(19, 00, 20, 00));
//		expActivityList.add(new DailyfExpActivity("腾讯0509", "2013-05-12 00:00:00", "2013-05-18 00:00:00", 2, times_for_腾讯0509, new ServersConfig() {
//			@Override
//			public boolean thiserverFit() {
//				if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
//					return true;
//				}
//				return false;
//			}
//		}));
//
//		List<DayilyTimeDistance> times_for_台湾0903 = new ArrayList<DayilyTimeDistance>();
//		times_for_台湾0903.add(new DayilyTimeDistance(00, 00, 23, 59));
//		Set<String> limitmaps = new HashSet<String>();
//		limitmaps.add("jiazuditu");
//		expActivityList.add(new DailyfExpActivity("台湾0903", "2013-06-05 00:00:00", "2013-09-10 23:59:59", limitmaps, 1.5, times_for_台湾0903, new ServersConfig() {
//			@Override
//			public boolean thiserverFit() {
//				if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
//					return true;
//				}
//				return false;
//			}
//		}));
//		List<DayilyTimeDistance> times_for_台湾0917 = new ArrayList<DayilyTimeDistance>();
//		times_for_台湾0917.add(new DayilyTimeDistance(18, 00, 23, 50));
//		expActivityList.add(new DailyfExpActivity("台湾0805", "2013-08-05 00:00:00", "2013-09-19 23:59:59", 2, times_for_台湾0917, new ServersConfig() {
//			@Override
//			public boolean thiserverFit() {
//				String servername = GameConstants.getInstance().getServerName();
//				if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
//					if ("皇圖霸業".equals(servername) || "仙尊降世".equals(servername)) {
//						return true;
//					}
//				}
//				return false;
//			}
//		}));
//		List<DayilyTimeDistance> times_for_korea = new ArrayList<DayilyTimeDistance>();
//		times_for_korea.add(new DayilyTimeDistance(10, 00, 12, 00));
//		times_for_korea.add(new DayilyTimeDistance(21, 00, 23, 59));
//		expActivityList.add(new DailyfExpActivity("韩服08022_1", "2013-08-22 00:00:00", "2013-08-22 23:59:59", 2, times_for_korea, new ServersConfig() {
//			@Override
//			public boolean thiserverFit() {
//				if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
//					GameConstants gc = GameConstants.getInstance();
//					String servername = gc.getServerName();
//					String names[] = { "S10-구천현녀", "S11-풍도대제", "ST" };
//					for (String name : names) {
//						if (name.equals(servername)) {
//							return true;
//						}
//					}
//					return false;
//				}
//				return false;
//			}
//		}));
//	}

	/**
	 * 获得由于经验活动带来的经验加成
	 * @param calendar
	 * @return
	 */
	public double getExpRateFromActivity(Calendar calendar) {
		for (ExpActivity expActivity : expActivityList) {
			CompoundReturn cr = expActivity.getExpActivityMultiple(calendar);
			if (cr.getBooleanValue()) {
				return cr.getDoubleValue();
			}
		}
		return 1;
	}

	/**
	 * 限制的地图
	 * @param calendar
	 * @return
	 */
	public String[] getLimitMaps(Calendar calendar) {
		for (ExpActivity expActivity : expActivityList) {
			CompoundReturn cr = expActivity.getExpActivityMultiple(calendar);
			if (cr.getBooleanValue()) {
				return cr.getStringValues();
			}
		}
		return null;
	}

	/**
	 * 封魔录专用
	 * @param calendar
	 * @return
	 */
	public CompoundReturn getExpRateFromTumotieActivity(Calendar calendar) {
		for (ExpActivity expActivity : tumotieActivityList) {
			CompoundReturn cr = expActivity.getExpActivityMultiple(calendar);
			if (cr.getBooleanValue()) {
				return cr.setObjValue(expActivity);
			}
		}
		return CompoundReturn.create().setBooleanValue(false).setDoubleValue(1);
	}

	private void onDoPrize(Player player) {
		try {
			long now = SystemTime.currentTimeMillis();
			if (TimeTool.instance.isSame(now, getPlayerLastGotPrizeTime(player), TimeDistance.DAY)) {
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + "[登陆游戏] [和上次登陆是同一天]");
				}
				return;
			}
			boolean gotA = do完美紫装免费拿(player, now);
			boolean gotB = do中秋国庆天天有好礼(player, now);
			if (gotA || gotB) {
				setPlayerLastGotPrizeTime(player, now);
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[处理奖励异常]", e);
		}
	}

	private String get最后一次获得奖励时间key(long playerId) {
		return key_最后一次获得奖励时间 + playerId;
	}

	private long getPlayerLastGotPrizeTime(Player player) {
		Long time = (Long) disk.get(get最后一次获得奖励时间key(player.getId()));
		if (time == null) {
			time = 0L;
			disk.put(get最后一次获得奖励时间key(player.getId()), time);
		}
		return time;
		// Hashtable<Long, Long> map = (Hashtable<Long, Long>) disk.get(key_最后一次获得奖励时间);
		// if (!map.contains(Long.valueOf(player.getId()))) {
		// ActivitySubSystem.logger.warn(player.getLogString() + "[最后一次获得奖励时间] [原来列表中没有] [列表长度:" + map.size() + "]");
		// for (Iterator<Long> itor = map.keySet().iterator(); itor.hasNext();) {
		// long id = itor.next();
		// long time = map.get(id);
		// ActivitySubSystem.logger.warn("[列表里有的]" + id + "----" + time);
		// }
		// map.put(Long.valueOf(player.getId()), 0L);
		// disk.put(key_最后一次获得奖励时间, map);
		// }
		// ActivitySubSystem.logger.warn(player.getLogString() + "[最后一次获得奖励时间] [" + ((Hashtable<Long, Long>) disk.get(key_最后一次获得奖励时间)).get(player.getId()) + "]");
		// return ((Hashtable<Long, Long>) disk.get(key_最后一次获得奖励时间)).get(player.getId());
	}

	private void setPlayerLastGotPrizeTime(Player player, long time) {
		// Hashtable<Long, Long> map = (Hashtable<Long, Long>) disk.get(key_最后一次获得奖励时间);
		// map.put(player.getId(), time);
		// disk.put(key_最后一次获得奖励时间, map);
		disk.put(get最后一次获得奖励时间key(player.getId()), Long.valueOf(time));
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn(player.getLogString() + "[更新最后一次获得奖励时间" + time + "]");
		}
	}

	private boolean do完美紫装免费拿(Player player, long now) {
		if (!分服活动open) {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_完美紫装免费拿 + "]" + "[服务器不符]");
			}
			return false;
		}
		if (now < 完美紫装免费拿start || now > 完美紫装免费拿end) {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_完美紫装免费拿 + "]" + "[时间不符]");
			}
			return false;
		}
		int sealLevel = SealManager.getInstance().getSealLevel();
		if (player.getLevel() + 30 > sealLevel) {

			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_完美紫装免费拿 + "]" + "[等级不符] [当前服务器封印等级:" + sealLevel + "]");
			}
			return false;
		}
		Hashtable<Long, Integer> map = ((Hashtable<Long, Integer>) (disk.get(key_完美紫装免费拿)));
		if (!map.containsKey(player.getId())) {
			map.put(player.getId(), 0);
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_完美紫装免费拿 + "]" + "[第一次登陆]");
			}
			// disk.put(key_完美紫装免费拿, map);
		}
		int times = map.get(player.getId());
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_完美紫装免费拿 + "]" + "[第" + times + "次登陆]");
		}
		if (times > 完美紫装免费拿_A.length - 1) {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_完美紫装免费拿 + "]" + "[第" + times + "次登陆] [超过最大次数]");
			}
			return false;
		}
		String prizeName = "";
		if (player.getLevel() <= 80) {
			prizeName = 完美紫装免费拿_A[times];
		} else {
			prizeName = 完美紫装免费拿_B[times];

		}
		Article article = ArticleManager.getInstance().getArticle(prizeName);
		if (article == null) {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_完美紫装免费拿 + "]" + "[第" + times + "次登陆] [奖励物品不存在:" + prizeName + "]");
			}
			return false;
		}
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.运营发放活动奖励, player, article.getColorType(), 1, true);
			long mailId = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, "恭喜您获得完美紫装", "恭喜您在“等级飞升”系列活动中获得完美紫装备，请在附件中查收！10月7日前每天登陆游戏都获得完美紫装，最多可获得5件，活动仅此一回，不要错过哦！", 0, 0, 0, key_最后一次获得奖励时间);
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_完美紫装免费拿 + "]" + "[第" + times + "次登陆] [奖励物品:" + prizeName + "] [发送成功] [物品ID:" + ae.getId() + "] [邮件ID:" + mailId + "]");
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_完美紫装免费拿 + "]" + "[第" + times + "次登陆] [创建奖励:" + prizeName + "] [异常]", e);
			return false;
		}
		map = ((Hashtable<Long, Integer>) (disk.get(key_完美紫装免费拿)));
		map.put(player.getId(), ++times);
		disk.put(key_完美紫装免费拿, map);
		return true;
	}

	private boolean do中秋国庆天天有好礼(Player player, long now) {
		if (now < 中秋国庆天天有好礼start || now > 中秋国庆天天有好礼end) {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_中秋国庆天天有好礼 + "]" + "[时间不符]");
			}
			return false;
		}
		Hashtable<Long, Integer> map = (Hashtable<Long, Integer>) disk.get(key_中秋国庆天天有好礼);
		if (!map.contains(player.getId())) {
			map.put(player.getId(), 0);
		}
		int times = map.get(player.getId());
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_中秋国庆天天有好礼 + "]" + "[第" + times + "次登陆]");
		}
		if (times > 中秋国庆天天有好礼article.length - 1) {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_中秋国庆天天有好礼 + "]" + "[第" + times + "次登陆] [超过最大次数]");
			}
			return false;
		}
		try {
			String prizeName = 中秋国庆天天有好礼article[times];

			Article article = ArticleManager.getInstance().getArticle(prizeName);
			if (article == null) {
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_中秋国庆天天有好礼 + "]" + "[第" + times + "次登陆] [物品不存在" + prizeName + "]");
				}
				return false;
			}
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.运营发放活动奖励, player, article.getColorType(), 中秋国庆天天有好礼num[times], true);
			long mailId = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 中秋国庆天天有好礼num[times] }, "恭喜您获得中秋国庆上线好礼", "恭喜您在“中秋国庆天天礼”活动中登陆" + (times + 1) + "天，获得第1份上线好礼", 0, 0, 0, key_中秋国庆天天有好礼);
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn(player.getLogString() + " [" + key_中秋国庆天天有好礼 + "]" + "[第" + times + "次登陆] [奖励物品:" + prizeName + "] [发送成功] [物品ID:" + ae.getId() + "] [邮件ID:" + mailId + "]");
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.error(player.getLogString() + " [" + key_中秋国庆天天有好礼 + "]" + "[第" + times + "次登陆] [异常]", e);
			return false;
		}

		map = ((Hashtable<Long, Integer>) (disk.get(key_中秋国庆天天有好礼)));
		map.put(player.getId(), ++times);
		disk.put(key_中秋国庆天天有好礼, map);
		return true;
	}

	public String getDiskFile() {
		return diskFile;
	}

	public void setDiskFile(String diskFile) {
		this.diskFile = diskFile;
	}

	public String 八喜礼包 = Translate.八喜礼包;
	public String 八星礼包 = Translate.八星礼包;

	public String[] 经验金钱大奉送时间 = new String[] { "2012-09-21 00:00:00", "2012-10-08 00:00:00" };
	public long[] start_stopTime = new long[经验金钱大奉送时间.length];

	public void init大奉送() throws Exception {

		Date date1 = LogRecordManager.sdf.parse(经验金钱大奉送时间[0]);
		Date date2 = LogRecordManager.sdf.parse(经验金钱大奉送时间[1]);

		if (date1.getTime() >= date2.getTime()) {
			throw new RuntimeException("经验金钱大奉送时间配置错误");
		}
		start_stopTime[0] = date1.getTime();
		start_stopTime[1] = date2.getTime();
	}

	public synchronized void 经验金钱大奉送(Player player) {

		try {
			if (!ActivityManager.getInstance().分服活动open) {
				return;
			}
			long sealLevel = SealManager.getInstance().getSealLevel();
			if (player.getLevel() <= sealLevel - 30) {

				int level = player.getLevel();
				Article a = null;
				if (level <= 80) {
					a = ArticleManager.getInstance().getArticle(八喜礼包);
				} else {
					a = ArticleManager.getInstance().getArticle(八星礼包);
				}

				if (a == null) {
					ActivitySubSystem.logger.error("[经验金钱大奉送发送失败没有物品] [" + player.getLogString() + "]");
					return;
				}
				long now = System.currentTimeMillis();
				if (now < start_stopTime[0] || now > start_stopTime[1]) {
					ActivitySubSystem.logger.warn("[经验金钱大奉送时间不符合] [" + player.getLogString() + "] [" + now + "]");
					return;
				}
				PlayerActivityRecord record = null;
				Object o = disk.get(player.getId());
				if (o != null) {
					record = (PlayerActivityRecord) o;
					if (record.getDafengsongTime() > 0) {
						ActivitySubSystem.logger.error("[经验金钱大奉送已经发送过] [" + player.getLogString() + "]");
						return;
					} else {
						record.setDafengsongTime(now);
					}
				} else {
					record = new PlayerActivityRecord();
					record.setDafengsongTime(now);
				}

				if (record != null && record.isDirty()) {
					record.setDirty(false);
					disk.put(player.getId(), record);
				}

				发送大奉送礼物(player, a);
				ActivitySubSystem.logger.error("[经验金钱大奉送发送成功] [" + player.getLogString() + "]");
			} else {
				ActivitySubSystem.logger.warn("[经验金钱大奉送等级不符合] [" + player.getLogString() + "]");
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[经验金钱大奉送异常] [" + player.getLogString() + "]", e);
		}
	}

	public void 发送大奉送礼物(Player player, Article a) throws Exception {

		ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, null, a.getColorType(), 1, true);
		MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, Translate.恭喜您获得经验礼包, Translate.恭喜您在等级飞升系列活动中获得经验礼包, 0, 0, 0, "经验金钱大奉送");
	}

	public static String 在线一重礼 = Translate.在线一重礼;
	public static String 在线二重礼 = Translate.在线二重礼;
	public static String 在线三重礼 = Translate.在线三重礼;
	public static String 在线四重礼 = Translate.在线四重礼;
	public static String 在线活动标题 = "恭喜您获得在线奖励";
	public static String 在线活动内容1 = "您已连续在线30分钟，获得";
	public static String 在线活动内容2 = "，请在附件中提取。祝您游戏愉快！ ";

	public static long 十分钟 = 1l * 10 * 60 * 1000;
	public static long 三十分钟 = 1l * 30 * 60 * 1000;
	public static long 五十分钟 = 1l * 50 * 60 * 1000;
	public static long 七十分钟 = 1l * 70 * 60 * 1000;

	// 月份小1
	public int[] 在线特殊日期 = new int[] { 2012, 11, 4 };
	public int[][] 指定日期 = { 在线特殊日期, { 2012, 11, 5 }, { 2012, 11, 6 }, { 2012, 11, 7 }, { 2012, 11, 8 }, { 2012, 11, 9 } };

	public synchronized void onlineActivity(Player player, long time) {

		try {
			// 只有qq服有这个
			if (!isQQServer(player)) {
				return;
			}
			boolean bln = false;
			for (int[] ints : 指定日期) {
				if (Utils.isSpecialSameDay(ints[0], ints[1], ints[2])) {
					bln = true;
					break;
				}
			}
			if (bln) {
				PlayerActivityRecord pr = null;
				Object o = disk.get(player.getId());
				if (o == null) {
					pr = new PlayerActivityRecord();
				} else {
					pr = (PlayerActivityRecord) o;
				}

				long lastFlushOnlineTime = pr.getLastOnlineReceiveTime();

				boolean isSameDay = Utils.isSameDay(time, lastFlushOnlineTime);
				if (!isSameDay) {
					pr.setLastOnlineReceiveTime(time);
					pr.setFirstReceive(false);
					pr.setSecondReceive(false);
					pr.setThirdReceive(false);
					pr.setFourthReceive(false);
				}

				long duartionTime = time - player.getEnterServerTime();
				long distanceLastReceive = time - pr.getLastOnlineReceiveTime();
				if (duartionTime >= 三十分钟 && distanceLastReceive >= 三十分钟) {
					if (!pr.isFirstReceive()) {
						pr.setFirstReceive(true);
						pr.setLastOnlineReceiveTime(time);
						// 发邮件;
						Article a = ArticleManager.getInstance().getArticle(在线一重礼);
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, 在线活动标题, 在线活动内容1 + 在线一重礼 + 在线活动内容2, 0, 0, 0, "在线奖励");
						player.sendError("你已连续在线30分钟，获得" + 在线一重礼);
						ActivitySubSystem.logger.error("[qq旧服活动] [第一个] [" + player.getLogString() + "]");
					} else if (!pr.isSecondReceive()) {
						distanceLastReceive = time - pr.getLastOnlineReceiveTime();
						pr.setSecondReceive(true);
						pr.setLastOnlineReceiveTime(time);
						// 发邮件;
						Article a = ArticleManager.getInstance().getArticle(在线二重礼);
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, 在线活动标题, 在线活动内容1 + 在线二重礼 + 在线活动内容2, 0, 0, 0, "在线奖励");
						player.sendError("你已连续在线30分钟，获得" + 在线二重礼);
						ActivitySubSystem.logger.error("[qq旧服活动] [第二个] [" + player.getLogString() + "]");
					} else if (!pr.isThirdReceive() && distanceLastReceive >= 三十分钟) {
						distanceLastReceive = time - pr.getLastOnlineReceiveTime();
						pr.setThirdReceive(true);
						pr.setLastOnlineReceiveTime(time);
						// 发邮件;
						Article a = ArticleManager.getInstance().getArticle(在线三重礼);
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, 在线活动标题, 在线活动内容1 + 在线三重礼 + 在线活动内容2, 0, 0, 0, "在线奖励");
						player.sendError("你已连续在线30分钟，获得" + 在线三重礼);
						ActivitySubSystem.logger.error("[qq旧服活动] [第三个] [" + player.getLogString() + "]");
					} else if (!pr.isFourthReceive() && distanceLastReceive >= 三十分钟) {
						distanceLastReceive = time - pr.getLastOnlineReceiveTime();
						pr.setFourthReceive(true);
						pr.setLastOnlineReceiveTime(time);
						// 发邮件;
						Article a = ArticleManager.getInstance().getArticle(在线四重礼);
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, 在线活动标题, 在线活动内容1 + 在线四重礼 + 在线活动内容2, 0, 0, 0, "在线奖励");
						player.sendError("你已连续在线30分钟，获得" + 在线四重礼);
						ActivitySubSystem.logger.error("[qq旧服活动] [第四个] [" + player.getLogString() + "]");
					}
				}

				if (pr.isDirty()) {
					ActivitySubSystem.logger.warn("[保存活动对象] [" + player.getLogString() + "] [" + pr.isDirty() + "]");
					pr.setDirty(false);
					disk.put(player.getId(), pr);
				}
			} else {
				if (ActivitySubSystem.logger.isDebugEnabled()) {
					ActivitySubSystem.logger.debug("[在线活动不在指定时间] [" + player.getLogString() + "]");
				}
			}

		} catch (Exception e) {
			ActivitySubSystem.logger.error("[连续在线活动异常] [" + player.getLogString() + "]", e);
		}
	}

	public int[] 增加次数特殊日期 = new int[] { 2012, 11, 4 };
	public int[][] 增加次数指定日期 = { 增加次数特殊日期, { 2012, 11, 5 }, { 2012, 11, 6 }, { 2012, 11, 7 }, { 2012, 11, 8 }, { 2012, 11, 9 } };
	public static String[] articleName = { Translate.白玫瑰, Translate.蓝色妖姬, Translate.棒棒糖, Translate.巧克力 };
	// 白玫瑰 1，第一次 78，第二次 156
	public static int[][] consumeNum = { { 78, 156 }, { 39, 78 }, { 78, 156 }, { 39, 78 } };

	public Hashtable<Long, ExchangeActivityRecord> exchangeRecordMap = new Hashtable<Long, ExchangeActivityRecord>();

	public static String[] des = { Translate.兑换喝酒, Translate.兑换封魔录 };

	public static int maxNum = 2;

	// type 0:酒 1:帖
	public int getDayExchangeAddNum(Player player, String fenlei) {
		int num = 0;
		int type = -1;
		try {
			ActivitySubSystem.logger.warn("[查看指定物品的次数] [" + player.getLogString() + "] [" + fenlei + "]");
			if (fenlei.equals(Translate.二级分类酒)) {
				type = 0;
			} else if (fenlei.equals(Translate.封魔录)) {
				type = 1;
			} else {
				return 0;
			}

			boolean specialDay = false;
			if (isQQServer(player)) {
				for (int[] ints : 增加次数指定日期) {
					specialDay = Utils.isSpecialSameDay(ints[0], ints[1], ints[2]);
					if (specialDay) {
						break;
					}
				}
			}
			if (specialDay) {
				ExchangeActivityRecord er = exchangeRecordMap.get(player.getId());
				if (er != null) {
					num = er.returnNum(player, type);
				} else {
					ActivitySubSystem.logger.error("[查询兑换增加次数错误] [没有交换实体] [" + player.getLogString() + "]");
				}
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[查询兑换增加次数异常] [" + player.getLogString() + "] [类型:" + type + "]", e);
		}
		ActivitySubSystem.logger.warn("[返回增加次数成功] [" + player.getLogString() + "] [类型:" + type + "] [次数:" + num + "]");
		return num;
	}

	// 打开兑换窗口，显示兑换酒还是帖
	public void beginExchange(Player player) {

		boolean specialDay = false;
		if (isQQServer(player)) {
			for (int[] ints : 增加次数指定日期) {
				specialDay = Utils.isSpecialSameDay(ints[0], ints[1], ints[2]);
				if (specialDay) {
					break;
				}
			}
		}
		if (specialDay) {
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			String descript = Translate.玩家可使用一定数量的白玫瑰;
			mw.setDescriptionInUUB(descript);

			Option_Choose_Exchange beer = new Option_Choose_Exchange();
			beer.setChooseType(0);
			beer.setText(Translate.增加酒次数);

			Option_Choose_Exchange tie = new Option_Choose_Exchange();
			tie.setChooseType(1);
			tie.setText(Translate.增加封魔录次数);

			Option_Cancel cancle = new Option_Cancel();
			cancle.setText(Translate.取消);
			mw.setOptions(new Option[] { beer, tie, cancle });
			QUERY_WINDOW_RES res1 = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res1);
			ActivitySubSystem.logger.warn("[打开兑换窗口成功] [" + player.getLogString() + "]");
		} else {
			player.sendError(Translate.兑换活动还没有开始今天不能兑换);
		}

	}

	// chooseType选择兑换酒，还是兑换帖
	public void showTrueExchange(Player player, int chooseType) {

		boolean specialDay = false;
		if (isQQServer(player)) {
			for (int[] ints : 增加次数指定日期) {
				specialDay = Utils.isSpecialSameDay(ints[0], ints[1], ints[2]);
				if (specialDay) {
					break;
				}
			}
		}
		if (specialDay) {
			ExchangeActivityRecord er = exchangeRecordMap.get(player.getId());
			if (er == null) {
				er = new ExchangeActivityRecord();
				exchangeRecordMap.put(player.getId(), er);
			}

			int alreadyNum = er.returnNum(player, chooseType);
			if (alreadyNum >= maxNum) {
				player.sendError("每天兑换" + ActivityManager.des[chooseType] + "次数只能增加" + ActivityManager.maxNum + "次，你今天已经完成，不能在兑换");
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn("[增加次数] [次数已满] [" + player.getLogString() + "] [" + chooseType + "]");
				}
				return;
			}
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			String descript = "";
			if (alreadyNum == 0) {
				descript = "当日第一次兑换";
			} else {
				descript = "已有一次兑换";
			}
			mw.setDescriptionInUUB(descript);
			Option_True_Exchange 白玫瑰o = new Option_True_Exchange();
			白玫瑰o.setChooseType(chooseType);
			白玫瑰o.setArticleType(0);
			白玫瑰o.setText(Translate.白玫瑰 + consumeNum[0][alreadyNum]);

			Option_True_Exchange 蓝色妖姬o = new Option_True_Exchange();
			蓝色妖姬o.setChooseType(chooseType);
			蓝色妖姬o.setArticleType(1);
			蓝色妖姬o.setText(Translate.蓝色妖姬 + consumeNum[1][alreadyNum]);

			Option_True_Exchange 棒棒糖o = new Option_True_Exchange();
			棒棒糖o.setChooseType(chooseType);
			棒棒糖o.setArticleType(2);
			棒棒糖o.setText(Translate.棒棒糖 + consumeNum[2][alreadyNum]);

			Option_True_Exchange 巧克力o = new Option_True_Exchange();
			巧克力o.setChooseType(chooseType);
			巧克力o.setArticleType(3);
			巧克力o.setText(Translate.巧克力 + consumeNum[3][alreadyNum]);

			Option_Cancel cancle = new Option_Cancel();
			cancle.setText(Translate.取消);

			mw.setOptions(new Option[] { 白玫瑰o, 蓝色妖姬o, 棒棒糖o, 巧克力o, cancle });
			QUERY_WINDOW_RES res1 = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res1);
			ActivitySubSystem.logger.warn("[打开真正兑换窗口成功] [" + player.getLogString() + "]");
		} else {
			player.sendError(Translate.兑换活动还没有开始今天不能兑换);
		}
	}

	public void trueExchange(Player player, int chooseType, int articleType) {

		ExchangeActivityRecord er = exchangeRecordMap.get(player.getId());
		if (er != null) {
			boolean bln = er.exchangeNum(player, chooseType, articleType);
			if (bln) {
				player.sendError("兑换增加" + des[chooseType] + "次数成功，今天增加了" + er.returnNum(player, chooseType) + "次");
				ActivitySubSystem.logger.warn("[兑换增加次数成功] [" + player.getLogString() + "] [" + chooseType + "] [物品:" + articleType + "]");
			}
		} else {
			ActivitySubSystem.logger.warn("[真实兑换错误] [没有兑换实体] [" + player.getLogString() + "]");
		}

	}

	public int[] 增加酒帖特殊 = new int[] { 2012, 11, 4 };
	public int[][] 增加酒帖特殊日期 = { 增加酒帖特殊, { 2012, 11, 5 }, { 2012, 11, 6 }, { 2012, 11, 7 }, { 2012, 11, 8 }, { 2012, 11, 9 } };

	public static int 特殊日期增加酒帖次数 = 1;

	// 特殊活动增加酒帖次数(11月22日—11月26日 活动期间，每天“喝酒”和“使用封魔录”的次数在原有基础上增加一次。)
	public int specialActivityAddNum(Player player) {
		boolean specialDay = false;
		if (isQQServer(player)) {
			try {
				for (int[] ints : 增加酒帖特殊日期) {
					specialDay = Utils.isSpecialSameDay(ints[0], ints[1], ints[2]);
					if (specialDay) {
						break;
					}
				}
			} catch (Exception e) {
				ActivitySubSystem.logger.error("[是否是指定增加酒帖时间异常] [" + player.getLogString() + "]", e);
			}
		}
		if (specialDay) {
			return 特殊日期增加酒帖次数;
		} else {
			return 0;
		}

	}

	// 太虚幻境 幽冥山谷 昆仑圣殿 凌霄宝殿 霸气乾坤 烟雨青山 仙山琼阁
	public static String[] QQServer = { "太虚幻境", "幽冥山谷", "昆仑圣殿", "凌霄宝殿", "霸气乾坤", "烟雨青山", "新功能测试" };

	public boolean isQQServer(Player player) {
		boolean qqServer = false;
		GameConstants gc = GameConstants.getInstance();
		for (String ss : QQServer) {
			if (ss.equals(gc.getServerName())) {
				qqServer = true;
				break;
			}
		}
		return qqServer;
	}

	public long get棉花糖开始时间() {
		return 棉花糖开始时间;
	}

	public void set棉花糖开始时间(long 棉花糖开始时间) {
		this.棉花糖开始时间 = 棉花糖开始时间;
	}

	public long get棉花糖结束时间() {
		return 棉花糖结束时间;
	}

	public void set棉花糖结束时间(long 棉花糖结束时间) {
		this.棉花糖结束时间 = 棉花糖结束时间;
	}

	public long get棉花糖开始时间_tw() {
		return 棉花糖开始时间_tw;
	}

	public void set棉花糖开始时间_tw(long 棉花糖开始时间_tw) {
		this.棉花糖开始时间_tw = 棉花糖开始时间_tw;
	}

	public static long baoshiActivityStartTime = TimeTool.formatter.varChar19.parse("2013-06-03 00:00:00");
	public static long baoshiActivityEndTime = TimeTool.formatter.varChar19.parse("2013-07-11 00:00:00");

	public static long baoshiActivityStartTime_tw = TimeTool.formatter.varChar19.parse("2013-07-19 00:00:00");
	public static long baoshiActivityEndTime_tw = TimeTool.formatter.varChar19.parse("2013-07-24 00:00:00");

	public static String[] baoshiComposePrize_sqage = { null, null, null, null, null, null, "铸灵石(6级)", "铸灵石(7级)", "铸灵石(8级)", "铸灵石(9级)" };// 宝石合成的奖励
	public static String[] baoshiComposePrize_taiwan = { null, null, null, null, null, null, "鑄靈石(6級)", "鑄靈石(7級)", "鑄靈石(8級)", "鑄靈石(9級)" };// 宝石合成的奖励
	public static String[] baoshiComposePrize_tencent = { null, null, null, null, "铸灵石(4级)", "铸灵石(5级)", "铸灵石(6级)", "铸灵石(7级)", "铸灵石(8级)", "铸灵石(9级)" };// 宝石合成的奖励

	public static String[] activityBaoshiNames = { "无相", "炎焚", "魔渊", "混沌" };
	static {
		if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
			activityBaoshiNames = new String[] { "無相", "炎焚", "魔淵", "混沌" };
		}
	}

	public static String[] limitserver = { "漫天花雨", "破晓之穹" };

	/**
	 * 通知玩家合成了宝石
	 * @param player合成的玩家
	 * @param resultBaoshiLevel合成结果宝石的等级
	 */
	public void noticeBaoshiCompose(Player player, int resultBaoshiLevel, String articleName) {
		if (resultBaoshiLevel < 0 || resultBaoshiLevel >= baoshiComposePrize_sqage.length) {
			return;
		}
		boolean isActivityBaoshi = false;
		for (String name : activityBaoshiNames) {
			if (articleName.contains(name)) {
				isActivityBaoshi = true;
				break;
			}
		}
		if (!isActivityBaoshi) {
			return;
		}

		// String servername = GameConstants.getInstance().getServerName();
		// for(String name:limitserver){
		// if(name.equals(servername)){
		// return;
		// }
		// }
		ActivitySubSystem.logger.warn(player.getLogString() + " [合成宝石] [参与铸灵石活动] [" + articleName + "] [宝石等级:" + resultBaoshiLevel + "]");

		if (PlatformManager.getInstance().isPlatformOf(Platform.官方, Platform.腾讯)) {
			long now = System.currentTimeMillis();
			if (baoshiActivityStartTime <= now && baoshiActivityEndTime >= now) {
				// 在活动时间内
				String prizeArticleName = null;
				if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
					prizeArticleName = baoshiComposePrize_sqage[resultBaoshiLevel];
				} else if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
					prizeArticleName = baoshiComposePrize_tencent[resultBaoshiLevel];
				}
				if (prizeArticleName != null) {
					Article article = ArticleManager.getInstance().getArticle(prizeArticleName);
					if (article != null) {
						try {
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.活动, player, article.getColorType(), 1, true);
							long mailId = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, "宝石升级UP!UP!UP!", "恭喜您参加石升级UP!UP!UP!成功，活动系统奖励，请查收附件！", 0L, 0L, 0L, "宝石合成活动");
							player.sendError("恭喜您参加石升级UP!UP!UP!成功，请去邮件内查收奖励！~");
							ActivitySubSystem.logger.error(player.getLogString() + " [获得宝石合成奖励:" + prizeArticleName + "] [mailId:" + mailId + "]");
						} catch (Exception e) {
							ActivitySubSystem.logger.error(player.getLogString() + " [创建物品失败" + prizeArticleName + "]", e);
						}
					} else {
						ActivitySubSystem.logger.error(player.getLogString() + " [物品不存在" + prizeArticleName + "]");
					}
				}
			}
		}
		if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
			long now = System.currentTimeMillis();
			if (baoshiActivityStartTime_tw <= now && baoshiActivityEndTime_tw >= now) {
				// 在活动时间内
				String prizeArticleName = baoshiComposePrize_taiwan[resultBaoshiLevel];
				if (prizeArticleName != null) {
					Article article = ArticleManager.getInstance().getArticle(prizeArticleName);
					if (article != null) {
						try {
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.活动, player, article.getColorType(), 1, true);
							long mailId = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, "升級寶石送驚喜", "恭喜您合成新屬性寶石,為您送上鑄靈石一顆,祝您在飘渺寻仙曲世界中更加所向披靡！ ", 0L, 0L, 0L, "宝石合成活动");
							player.sendError("恭喜您合成新屬性寶石,為您送上鑄靈石一顆,祝您在飘渺寻仙曲世界中更加所向披靡！ ");
							ActivitySubSystem.logger.error(player.getLogString() + " [获得宝石合成奖励:" + prizeArticleName + "] [mailId:" + mailId + "]");
						} catch (Exception e) {
							ActivitySubSystem.logger.error(player.getLogString() + " [创建物品失败" + prizeArticleName + "]", e);
						}
					} else {
						ActivitySubSystem.logger.error(player.getLogString() + " [物品不存在" + prizeArticleName + "]");
					}
				}
			}
		}
	}

	@Override
	public String getMConsoleName() {
		return "活动管理器";
	}

	@Override
	public String getMConsoleDescription() {
		return "活动相关的数据";
	}

	/**
	 * 每日登录充值获取礼包
	 */
	public void loginChargeGetPackage(Player player){
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public double getYuanqiRateFormActivity() {
		long now = SystemTime.currentTimeMillis();
		for (AddRateActivity cga : chuanGongActivity) {
			if (cga.inActivity(now)) {
//				return cga.getAddRate();
				 return cga.getAddRate() + 1;
			}
		}
		return 1;
	}

	public double getDatiExpRateFormActivity() {
		long now = SystemTime.currentTimeMillis();
		for (AddRateActivity cga : datiActivity) {
			if (cga.inActivity(now)) {
				return cga.getAddRate() + 1;
				// return cga.getAddRate() + 1;
			}
		}
		return 1;
	}

	public double getJifenAddRateFormActivity() {
		long now = SystemTime.currentTimeMillis();
		for (AddRateActivity cga : jifenActivity) {
			if (cga.inActivity(now)) {
				return cga.getAddRate();
			}
		}
		return 0;
	}
	
	public double getXueShiAddRateFormActivity() {
		long now = SystemTime.currentTimeMillis();
		for (AddRateActivity cga : xueShiActivity) {
			if (cga.inActivity(now)) {
				return cga.getAddRate();
			}
		}
		return 0;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void sendNewPlayerPrize(Player player) {
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		PlatformManager pm = PlatformManager.getInstance();
		GameConstants gc = GameConstants.getInstance();
		for (NewPlayerPrize npp : newPlayerPrizes) {
			if (npp.getPlatform().equals(pm.getPlatform())) {
				if (npp.getServerName().equals(gc.getServerName())) {
					// 发送奖励
					sendMailForActivity(players, npp.getPrizeList().toArray(new ActivityProp[0]), npp.getMailTitle(), npp.getMailContent(), "渠道新服礼包");
					ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [渠道新服礼包发送完毕] ");
				}
			}
		}
	}

	public static void sendMailForActivity(List<Player> players, ActivityProp[] activityProps, String mailTitle, String mailContent, String sendReason) {
		sendMailForActivity(players, activityProps, mailTitle, mailContent, sendReason, null);
	}

	public static List<String> sendMailForActivity(List<Player> players, ActivityProp[] activityProps, String mailTitle, String mailContent, String sendReason, HttpServletRequest request) {
		List<String> mailConte = new ArrayList<String>();
		for (Player player : players) {
			List<ArticleEntity> articleEntities = new ArrayList<ArticleEntity>();
			List<Integer> articleEntityNums = new ArrayList<Integer>();
			for (ActivityProp activityProp : activityProps) {
				Article article = ArticleManager.getInstance().getArticleByCNname(activityProp.getArticleCNName());
				int createTimes = 1;// 要创建几次物品
				int onceCreateNum = 1;// 每次创建几个
				if (article != null) {
					if (article.isOverlap()) {
						int overLapNum = article.getOverLapNum();
						if (overLapNum <= 0) {
							ActivitySubSystem.logger.warn("[物品可堆叠可是堆叠数量小于1 加容错] [" + player.getLogString() + "] [articleName:" + article.getName_stat() + "]");
							overLapNum = 1;
						}
						createTimes = activityProp.getArticleNum() / overLapNum + ((activityProp.getArticleNum() % overLapNum) == 0 ? 0 : 1);
						onceCreateNum = overLapNum;
					} else {
						createTimes = activityProp.getArticleNum();
						onceCreateNum = 1;
					}
					// ActivitySubSystem.logger.warn("[物品：" + activityProp.getArticleCNName() + "] [color:" + activityProp.getArticleColor() + "] [article.getOverLapNum():" +
					// article.getOverLapNum() + "] [createTimes:" + createTimes + "] [onceCreateNum:" + onceCreateNum + "] ");
					int leftNum = activityProp.getArticleNum();
					for (int i = 0; i < createTimes - (article.isOverlap() ? 1 : 0); i++) {
						leftNum -= onceCreateNum;
						try {
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, activityProp.isBind(), ArticleEntityManager.CREATE_REASON_huodong_libao, player, activityProp.getArticleColor(), onceCreateNum, true);
							articleEntities.add(ae);
							if ("宝箱大乱斗".equals(sendReason)) {
								EnchantEntityManager.sendArticleStat(player, ae, sendReason);
							}
							articleEntityNums.add(onceCreateNum);
						} catch (Exception e) {
							ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [创建物品出错]", e);
						}
					}
					if (article.isOverlap() && leftNum > 0) {
						try {
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, activityProp.isBind(), ArticleEntityManager.CREATE_REASON_huodong_libao, player, activityProp.getArticleColor(), leftNum, true);
							articleEntities.add(ae);
							articleEntityNums.add(leftNum);
						} catch (Exception e) {
							ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [创建物品出错]", e);
						}
					}
				} else {
					ActivitySubSystem.logger.warn("[邮件发送奖励物品] [物品不存在] [articleName:" + activityProp.getArticleCNName() + "] [颜色:"+activityProp.getArticleColor()+"] [数量:"+activityProp.getArticleNum()+"] [" + player.getLogString() + "]");
				}
				// ActivitySubSystem.logger.warn("[物品：" + activityProp.getArticleCNName() + "] [color:" + activityProp.getArticleColor() + "] [要发物品的个数:" + articleEntities.size() +
				// "]");
			}
			List<ArticleEntity> oneMailSendArticleEntity = new ArrayList<ArticleEntity>();
			List<Integer> oneMailSendArticleEntityNum = new ArrayList<Integer>();
			for (int i = 0; i < articleEntities.size(); i++) {
				oneMailSendArticleEntity.add(articleEntities.get(i));
				oneMailSendArticleEntityNum.add(articleEntityNums.get(i));
				// ActivitySubSystem.logger.warn("[添加要发送的物品] [" + articleEntities.get(i).getArticleName() + "] [" + articleEntities.get(i).getColorType() + "]");
				if (i % 5 == 4 || i == articleEntities.size() - 1) {// 确定要发送一封邮件
					// ActivitySubSystem.logger.warn("确定要发送邮件(" + i + "/" + articleEntities.size() + ")");
					// SEND CLEAR LIST
					int[] counts = new int[oneMailSendArticleEntityNum.size()];
					for (int index = 0; index < oneMailSendArticleEntityNum.size(); index++) {
						counts[index] = oneMailSendArticleEntityNum.get(index);
					}
					try {
						// MailManager.getInstance().sendMail(player.getId(), oneMailSendArticleEntity.toArray(new ArticleEntity[0]), counts, mailTitle, mailContent, 0L, 0L, 0L,
						// sendReason);
						long mailId = MailManager.getInstance().sendMail(player.getId(), oneMailSendArticleEntity.toArray(new ArticleEntity[0]), counts, mailTitle, mailContent, 0L, 0L, 0L, sendReason);
						if (request != null) {
							try {
								String res = getMailStringForEmail(mailId, request);
								if (res != null) {
									mailConte.add(res);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [发送邮件成功]");
					} catch (Exception e) {
						ActivitySubSystem.logger.warn("[" + player.getLogString() + "] [发送物品出错]", e);
					} finally {
						oneMailSendArticleEntity.clear();
						oneMailSendArticleEntityNum.clear();
					}
				}
			}
		}
		return mailConte;
	}

	public long getOldplayerlength() {
		return oldplayerlength;
	}

	public void setOldplayerlength(long oldplayerlength) {
		this.oldplayerlength = oldplayerlength;
	}

	/**
	 * 通过邮件ID得到监控邮件的内容
	 * @param gameMailId
	 * @return
	 */
	public static String getMailStringForEmail(long gameMailId, HttpServletRequest request) {
		Mail mail = MailManager.getInstance().getMail(gameMailId);
		if (mail == null || request == null) {
			ActivitySubSystem.logger.error("mail不存在[mailid:" + gameMailId + "] [request:" + request + "]");
			return null;
		}
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(mail.getReceiver());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (player == null) {
			return null;
		}
		StringBuffer content = new StringBuffer();

		content.append("<table style='font-size:12px;text-align:center;' border='1'>");

		content.append("<tr>");
		content.append("<td style='background-color:red;color:white;font-size:14px;font-weight:bold;' colspan='2'>发送者信息</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color:black;color:white;font-size:14px;font-weight: bold;'>发送者</td>");
		content.append("<td>").append(request.getSession().getAttribute("authorize.username")).append("</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color:black;color:white;font-size:14px;font-weight:bold;'>IP</td>");
		content.append("<td>").append(request.getRemoteAddr()).append("</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color:black;color:white;font-size:14px;font-weight:bold;'>服务器</td>");
		content.append("<td>").append(GameConstants.getInstance().getServerName()).append("</td>");
		content.append("</tr>");

		content.append("</table>");
		content.append("<HR>");

		content.append("<table style='font-size: 12px;text-align: center;' border='1'>");
		content.append("<tr>");
		content.append("<td style='background-color: red;color: white;font-size: 14px;font-weight: bold;' colspan='2'>接受者信息</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>账号</td>");
		content.append("<td>").append(player.getUsername()).append("</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>角色ID</td>");
		content.append("<td>").append(player.getId()).append("</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>角色名</td>");
		content.append("<td>").append(player.getName()).append("</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>角色等级</td>");
		content.append("<td>").append(player.getLevel()).append("</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>vip等级</td>");
		content.append("<td>").append(player.getVipLevel()).append("</td>");
		content.append("</tr>");

		content.append("</table>");
		content.append("<HR>");

		content.append("<table style='font-size: 12px;text-align: center;' border='1'>");
		content.append("<tr>");
		content.append("<td style='background-color: red;color: white;font-size: 14px;font-weight: bold;' colspan='2'>邮件信息</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>邮件ID</td>");
		content.append("<td>").append(gameMailId).append("</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>邮件标题</td>");
		content.append("<td>").append(mail.getTitle()).append("</td>");
		content.append("</tr>");

		content.append("<tr>");
		content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>邮件内容</td>");
		content.append("<td>").append(mail.getContent()).append("</td>");
		content.append("</tr>");

		int index = 0;
		for (int i = 0; i < mail.getCells().length; i++) {
			com.fy.engineserver.datasource.article.data.props.Cell cell = mail.getCells()[i];
			if (cell == null) {
				continue;
			}
			long entityId = cell.getEntityId();
			if (entityId <= 0) {
				continue;
			}
			ArticleEntity articleEntity = ArticleEntityManager.getInstance().getEntity(entityId);
			if (articleEntity == null) {
				continue;
			}
			Article article = ArticleManager.getInstance().getArticle(articleEntity.getArticleName());
			if (article == null) {
				continue;
			}
			index++;

			content.append("<tr>");
			content.append("<td style='background-color: black;color: white;font-size: 14px;font-weight: bold;'>第").append(index).append("个</td>");
			content.append("<td>").append("[" + article.getName_stat() + "] [" + articleEntity.getId() + "] [" + articleEntity.getArticleName() + "] [颜色:" + ArticleManager.getColorString(article, articleEntity.getColorType()) + "(" + articleEntity.getColorType() + ")] [数量:" + cell.getCount() + "] [" + (articleEntity.isBinded() ? "绑定" : "不绑定")).append("]</td>");
			content.append("</tr>");
		}

		if (mail.getCoins() > 0) {
			content.append("<tr>");
			content.append("<td style='background-color: red;color: white;font-size: 14px;font-weight: bold;'>银子</td>");
			content.append("<td>").append(BillingCenter.得到带单位的银两(mail.getCoins())).append("</td>");
			content.append("</tr>");
		}

		content.append("</table>");
		content.append("<HR>");

		return content.toString();
	}
}
