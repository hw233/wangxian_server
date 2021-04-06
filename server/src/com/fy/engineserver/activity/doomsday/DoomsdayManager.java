package com.fy.engineserver.activity.doomsday;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_JIAZUBOSS_REQ;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.DateUtil;

/**
 * 末日活动管理器
 * 
 */
public class DoomsdayManager implements Runnable {

	public static Logger logger = ActivitySubSystem.logger;

	public static String doomsdayLoggerHead = " [末日活动] ";

	private static DoomsdayManager instance;

	public static boolean running = true;

	public static long step = 100L;

	/** 材料兑换贡献卡配置 */
	public static List<DoomsdayManager.ExchageData> materialExchageDatas = new ArrayList<DoomsdayManager.ExchageData>();
	// /** 兑换钥匙配置 */
	// public static List<DoomsdayManager.ExchageData> keyExchageDatas = new ArrayList<DoomsdayManager.ExchageData>();

	public static String boss1Mapname = "toudengcang";
	public static String boss2Mapname = "chuanzhangshi";
	public static String guajiMapname = "fulicang";
	public static String guibingMapname = "guibincang";
	
	public static String kunlunshengdian = "kunlunshengdian";
	
	/** 进船舱的道具名字*/
	public static String chuanCangKey = Translate.方舟通行证;
	/** 进头等舱的道具名字*/
	public static String boss1Key = Translate.头等舱钥匙;
	/** 进船长室的道具名字*/
	public static String boss2Key = Translate.船长室钥匙;
	/** 进贵宾  大R的道具名字*/
	public static String guiBingKey = Translate.神秘钥匙;
	/** 贡献卡的名字 */
	public static String cardName = Translate.贡献卡;
	
	public static String[] req_propNames = new String[] {"楠木", "铸铁", "胶黏剂"};
	public static String[] EVERY_DAY_PRIZE = {"船长室钥匙", "神秘钥匙"};
	public static int every_day_prizeNum = 5;
	
	public static String[] chuanCangMapNames = new String[]{"putongfangzhouchuancang", "youzhifangzhouchuancang", "dingjifangzhouchuancang"};
	public static long[] PROGRESS_TO_BOAT_TYPE = new long[] {0, 9000, 18000};
	public static String[] boatNames = new String[]{"普通方舟", "优质方舟", "顶级方舟"};
	public static int[] boatTypeIndex = new int[]{-1,-1,-1,-1};
	
	public static int[] noticeHourTimes = new int[]{12,14,20,22};					//广播小时
	public boolean[] isNotice = new boolean[]{false, false, false, false};			//是否广播过
//	public static int[] noticeHourTimes = new int[]{12,14,18,19,20,22, 23};								//广播小时
//	public boolean[] isNotice = new boolean[]{false, false, false, false, false, false, false};			//是否广播过
	public long lastHeatTime = 0L;													//广播时间
	
	//头等舱
	public static String[] boss1StartTimes = new String[]{"11:00:00", "22:00:00"};
	public static long[] boss1StartTimesLong;
	//这个是3个国家一个一个
	public DoomsdayBossGame[] boss1Games = new DoomsdayBossGame[3];
	
	//船长室
	public static String boss2StartTime = "22:00:00";
	public static long boss2StartTimeLong;
	//这个是3个国家一个一个
	public DoomsdayBossGame[] boss2Games = new DoomsdayBossGame[3];
	
	//头等舱
	public static int[] boss1IDs = new int[]{10010399, 10010401, 10010409};
	//船长
	public static int[] boss2IDs = new int[]{10010400, 10010402, 10010410};
	//普通怪物
	public static int[] monsterIDs = new int[]{1200081, 1200082, 1200083, 1200084, 1200085, 1200086, 1200087, 1200088, 1200089, 1200090, 1200091, 1200092, 1200093, 1200094, 1200095, 1200096};
	//船
	public static int[] boatNpcIDs = new int[]{600060, 600071, 600072};
	public static int[] boatNpcXY = new int[]{2579, 3734};
	
	public static String guajiBuffName = "福利舱加成";
	public long guajiBuffTime = 1000L * 60 * 60 * 2;
	
	public static String player_title_type = "活动16";

	public static Map<String, String> mapAndKeys = new HashMap<String, String>();// 进入的门和对应的钥匙
	
	public int thread_num = 10;
	public DoomsdayThread[] doomsdayThreads;
	public HashMap<Long, DoomsdayFuLiGame> fuliGames = new HashMap<Long, DoomsdayFuLiGame>();
	public int DoomsGameMax = 600;							//福利房间个数限制

	//头等舱伤害排行 奖励
	public static String[][] boss1BillboardPrize = {{"头等舱金牌礼包(普通)", "头等舱银牌礼包(普通)", "头等舱铜牌礼包(普通)"},
												   {"头等舱金牌礼包(优质)", "头等舱银牌礼包(优质)", "头等舱铜牌礼包(优质)"},
												   {"头等舱金牌礼包(顶级)", "头等舱银牌礼包(顶级)", "头等舱铜牌礼包(顶级)"}};
	//头等舱击杀礼包
	public static String[] boss1KillPrize = {"头等舱击杀礼包(普通)", "头等舱击杀礼包(优质)", "头等舱击杀礼包(顶级)"};
	//头等舱参与礼包
	public static String[] boss1JoinPrize = {"头等舱参与礼包(普通)", "头等舱参与礼包(优质)", "头等舱参与礼包(顶级)"};
	//船长室伤害排行 奖励
	public static String[][] boss2BillboardPrize = {{"船长室金牌礼包(普通)", "船长室银牌礼包(普通)", "船长室铜牌礼包(普通)"},
												   {"船长室金牌礼包(优质)", "船长室银牌礼包(优质)", "船长室铜牌礼包(优质)"},
												   {"船长室金牌礼包(顶级)", "船长室银牌礼包(顶级)", "船长室铜牌礼包(顶级)"}};
	//船长击杀礼包
	public static String[] boss2KillPrize = {"船长室击杀礼包(普通)", "船长室击杀礼包(优质)", "船长室击杀礼包(顶级)"};
	//船长参与礼包
	public static String[] boss2JoinPrize = {"船长室参与礼包(普通)", "船长室参与礼包(优质)", "船长室参与礼包(顶级)"};

	public boolean isRefBoatNpc = false;					//是否刷新过船NPC
	public boolean isBoatStart = false;					//是否船是开的
	public boolean isBoatStartNotic = true;				//船建好是否广播过 
	public boolean isBoss1StartNotic_1 = false;			//头等舱1是否公告
	public boolean isBoss1StartNotic_2 = false;			//头等舱1是否公告
	public boolean isBoss2StartNotic = false;				//船长室是否公告

	/** 造船活动开始时间 init */
	public static long doomsdayStarttime = 0L;
	/** 诺亚方舟产生时间(过了这个时间兑换就不增加船的建设度) init */
	public static long doomsdayBoatBornTime = 0L;
	public static long doomsdayEndtime = 0L;

	/** 造船贡献度map */
	// public Map<Long, ContributeData> contributeDataMap = new HashMap<Long, DoomsdayManager.ContributeData>();

	public static enum DoomsdayDCKey {
		DAILY_CONTRIBUTE("每天贡献数据"), // <playerId,Data>
		TOTAL_CONTRIBUTE("总的贡献数据"), // <playerId,Data>
		COUNTRY_BOAT_PROGRESS("各个国家造船进度"), // <country,value>
		DAILY_PLAYER_ENTER_BOSS_RECORD("每天玩家进入BOSS副本的记录"), // <playerId,List<enterDate>>
		DAILY_PLAYER_ENTER_GUAJI_RECORD("玩家每天进入挂机地图的记录"), // List<PlayerId>
		;
		private String des;

		private DoomsdayDCKey(String des) {
			this.des = des;
		}

		public String getDes() {
			return des;
		}

		public void setDes(String des) {
			this.des = des;
		}

		public String getDaykey(String day) {
			return day + "_" + this.name();
		}
	}

	private String dcFile;

	public DiskCache diskCache;

	public String getDcFile() {
		return dcFile;
	}

	public void setDcFile(String dcFile) {
		this.dcFile = dcFile;
	}

	public static DoomsdayManager getInstance() {
		if(instance ==null){
			instance = new DoomsdayManager();
		}
		return instance;
	}

	public long[][] getCountryBillboard() {
		long[][] re = new long[3][2];
		long haotian = getBoatHold((byte)1);
		long wucheng = getBoatHold((byte)2);
		long cangyue = getBoatHold((byte)3);
		long max = haotian;
		int maxIndex = 1;
		if (max < wucheng) {
			max = wucheng;
			maxIndex = 2;
		}
		if (max < cangyue) {
			max = cangyue;
			maxIndex = 3;
		}
		re[0][0] = (long)maxIndex;
		re[0][1] = max;
		if (maxIndex == 1) {
			if (wucheng > cangyue) {
				re[1][0] = (long)2;
				re[1][1] = wucheng;
				re[2][0] = (long)3;
				re[2][1] = cangyue;
			}else {
				re[1][0] = (long)3;
				re[1][1] = cangyue;
				re[2][0] = (long)2;
				re[2][1] = wucheng;
			}
		}else if (maxIndex == 2) {
			if (haotian > cangyue) {
				re[1][0] = (long)1;
				re[1][1] = haotian;
				re[2][0] = (long)3;
				re[2][1] = cangyue;
			}else {
				re[1][0] = (long)3;
				re[1][1] = cangyue;
				re[2][0] = (long)1;
				re[2][1] = haotian;
			}
		}else if (maxIndex == 3) {
			if (haotian > wucheng) {
				re[1][0] = (long)1;
				re[1][1] = haotian;
				re[2][0] = (long)2;
				re[2][1] = wucheng;
			}else {
				re[1][0] = (long)2;
				re[1][1] = wucheng;
				re[2][0] = (long)1;
				re[2][1] = haotian;
			}
		}
		logger.warn(Arrays.toString(re[0]) + "~" + Arrays.toString(re[1]) + "~" + Arrays.toString(re[2]));
		return re;
	}
	
	/**
	 * 获得当天的排好序的贡献列表
	 * @param country
	 *            要查询的国家
	 * @return
	 */
	public List<ContributeData> getSortedContributeDatas(int country) {
		if (country == 0) { // 大排序
			Map<Long, DoomsdayManager.ContributeData> map = getAllPlayerData();
			List<ContributeData> list = new ArrayList<DoomsdayManager.ContributeData>();
			for (ContributeData data : map.values()) {
				list.add(data);
			}
			Collections.sort(list);
			return list;
		} else {// 各个国家每日的
			String todayKey = DoomsdayDCKey.DAILY_CONTRIBUTE.getDaykey(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
			Map<Long, DoomsdayManager.ContributeData> map = getDayPlayerData(todayKey);
			List<ContributeData> list = new ArrayList<DoomsdayManager.ContributeData>();
			for (ContributeData data : map.values()) {
				if (data.getCountry() == country) {
					list.add(data);
				}
			}
			Collections.sort(list);
			return list;
		}
	}

	/**
	 * 通过消耗物品的名字和数量确定兑换数据
	 * @param requestPropName
	 * @param requestPropNum
	 * @return
	 */
	public ExchageData getExchageCardData(String requestPropName, int requestPropNum) {
		for (ExchageData data : materialExchageDatas) {
			if (data.getRequestPropName().equals(requestPropName) && data.getRequestPropNum() == requestPropNum) {
				return data;
			}
		}
		return null;
	}

	/**
	 * 通过要兑换钥匙获得兑换数据
	 * @param keyName
	 * @return
	 */
	public ExchageData getExchageKeyData(String keyName) {
		for (ExchageData data : materialExchageDatas) {
			if (data.getReceivePropName().equals(keyName)) {
				return data;
			}
		}
		return null;
	}

	/** 所有兑换类数据 */
	public static class ExchageData {
		/** 兑换需要的物品名称 */
		private String requestPropName;
		/** 兑换需要的物品数量 */
		private int requestPropNum;
		private int requestPropColor;
		/** 兑换得到的物品名称 */
		private String receivePropName;
		/** 兑换得到的物品数量 */
		private int receivePropNum;
		/** 兑换得到物品的颜色 */
		private int receivePropColor;
		/** 兑换得到物品是否绑定 true 绑定,false不绑定 */
		private boolean receivePropBind;

		public ExchageData(String requestPropName, int requestPropNum, int requestPropColor, String receivePropName, int receivePropNum, int receivePropColor, boolean receivePropBind) {
			super();
			this.requestPropName = requestPropName;
			this.requestPropNum = requestPropNum;
			this.requestPropColor = requestPropColor;
			this.receivePropName = receivePropName;
			this.receivePropNum = receivePropNum;
			this.receivePropColor = receivePropColor;
			this.receivePropBind = receivePropBind;
		}

		public int getReceivePropColor() {
			return receivePropColor;
		}

		public void setReceivePropColor(int receivePropColor) {
			this.receivePropColor = receivePropColor;
		}

		public boolean isReceivePropBind() {
			return receivePropBind;
		}

		public void setReceivePropBind(boolean receivePropBind) {
			this.receivePropBind = receivePropBind;
		}

		public String getRequestPropName() {
			return requestPropName;
		}

		public void setRequestPropName(String requestPropName) {
			this.requestPropName = requestPropName;
		}

		public int getRequestPropNum() {
			return requestPropNum;
		}

		public void setRequestPropNum(int requestPropNum) {
			this.requestPropNum = requestPropNum;
		}

		public String getReceivePropName() {
			return receivePropName;
		}

		public void setReceivePropName(String receivePropName) {
			this.receivePropName = receivePropName;
		}

		public int getReceivePropNum() {
			return receivePropNum;
		}

		public void setReceivePropNum(int receivePropNum) {
			this.receivePropNum = receivePropNum;
		}

		@Override
		public String toString() {
			return "ExchageData [requestPropName=" + requestPropName + ", requestPropNum=" + requestPropNum + ", receivePropName=" + receivePropName + ", receivePropNum=" + receivePropNum + ", receivePropColor=" + receivePropColor + ", receivePropBind=" + receivePropBind + "]";
		}
	}
	
	public void duihuanTONGXINGZHENG(Player player) {
		try{
			if (!isDoomsdayBoatBornStart()) {
				player.sendError("船还未建造成功，不能兑换船票！");
				return;
			}
			Article article = ArticleManager.getInstance().getArticle(cardName);
			int hasNum = player.getArticleNum(cardName, article.getColorType(), BindType.BOTH);
			if (hasNum <= 0) {
				player.sendError("您包裹中" + cardName + "数量不足，不能兑换");
				return;
			}
			int emptyNum = player.getKnapsack_common().getEmptyNum();
			if (emptyNum == 0) {
				player.sendError("你的背包空间不足!");
				return;
			}
			player.removeArticleByNameColorAndBind(cardName, article.getColorType(), BindType.BOTH, "末日活动兑换", true);
			Article a = ArticleManager.getInstance().getArticle(chuanCangKey);
			ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.末日活动, player, a.getColorType(), 1, true);
			player.getKnapsack_common().putToEmptyCell(entity.getId(), 1, "末日活动兑换");
			logger.warn(doomsdayLoggerHead + " [兑换方舟通行证] [{}]", new Object[]{player.getLogString()});
		}catch(Exception e) {
			logger.error("duihuanTONGXINGZHENG出错", e);
		}
	}
	
	/**
	 * 兑换物品,
	 * @param player
	 * @return
	 */
	public void excharge(Player player, String requestPropName, int requestPropNum) {
		try{
			if (player.getLevel() <= 40) {
				player.sendError("41级以上才能参加此活动");
				return;
			}
			
			if (!isDoomsdatBoatStart()) {
				player.sendError("活动还未开启或已经结束了。");
				return;
			}
			
			ExchageData exchageCardData = getInstance().getExchageCardData(requestPropName, requestPropNum);
			if (exchageCardData == null) {
				player.sendError("数据异常,请稍后再试!");
				logger.warn(player.getLogString() + doomsdayLoggerHead + "[兑换贡献卡] [" + requestPropName + "] [" + requestPropNum + "] [失败] [配置不存在]");
				return;
			}
			int hasNum = player.getArticleNum(requestPropName, exchageCardData.requestPropColor, BindType.BOTH);
			if (hasNum < requestPropNum) {
				player.sendError("你的" + requestPropName + "不足,不能捐献");
				return;
			}
			Article article = ArticleManager.getInstance().getArticle(exchageCardData.receivePropName);
			if (article == null) {
				player.sendError("要兑换的物品不存在:" + exchageCardData.receivePropName);
				logger.error(player.getLogString() + doomsdayLoggerHead + "[要兑换的物品不存在:" + exchageCardData.receivePropName + "]");
				return;
			}

			int emptyNum = player.getKnapsack_common().getEmptyNum();
			if (emptyNum == 0) {
				player.sendError("你的背包空间不足!");
				return;
			}
			// 移除目标物品后 新增奖励物品
			for (int i = 0; i < requestPropNum; i++) {
				player.removeArticleByNameColorAndBind(requestPropName, exchageCardData.requestPropColor, BindType.BOTH, "末日活动兑换", true);
			}

			try {
				ArticleEntity receiveArticleEntity = ArticleEntityManager.getInstance().createEntity(article, exchageCardData.receivePropBind, ArticleEntityManager.末日活动, player, exchageCardData.receivePropColor, exchageCardData.receivePropNum, true);
				player.getKnapsack_common().putToEmptyCell(receiveArticleEntity.getId(), exchageCardData.receivePropNum, "末日活动");
				logger.warn(player.getLogString() + doomsdayLoggerHead + "[增加物品成功] [" + receiveArticleEntity.getId() + "/" + receiveArticleEntity.getArticleName() + "] [数量:" + exchageCardData.receivePropNum + "]");
			} catch (Exception e) {
				logger.error(player.getLogString() + doomsdayLoggerHead + "[创建物品异常] [" + exchageCardData.getReceivePropName() + "]", e);
				player.sendError("异常,请联系GM!");
				return;
			}
			player.sendNotice("兑换成功,去背包里查看");
			int receivePropNum = exchageCardData.getReceivePropNum();
			addExchargeCardDaily(player, requestPropName, receivePropNum);
			logger.warn(player.getLogString() + doomsdayLoggerHead + "[兑换贡献卡] [" + requestPropName + "] [" + requestPropNum + "] [成功] ");
		}catch(Exception e) {
			logger.error("Option_ExchargeCard", e);
		}
	}
	

	/** 贡献卡兑换数据 */
	public static class ContributeData implements Serializable, Comparable<ContributeData> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 19001010101010922L;
		private long playerId;
		private int country;
		private int contributeCardNum;

		public ContributeData(long playerId, int country, int contributeCardNum) {
			super();
			this.playerId = playerId;
			this.country = country;
			this.contributeCardNum = contributeCardNum;
		}

		public long getPlayerId() {
			return playerId;
		}

		public void setPlayerId(long playerId) {
			this.playerId = playerId;
		}

		public int getContributeCardNum() {
			return contributeCardNum;
		}

		public void setContributeCardNum(int contributeCardNum) {
			this.contributeCardNum = contributeCardNum;
		}

		public int getCountry() {
			return country;
		}

		public void setCountry(int country) {
			this.country = country;
		}

		@Override
		public String toString() {
			return "ContributeData [playerId=" + playerId + ", country=" + country + ", contributeCardNum=" + contributeCardNum + "]";
		}

		@Override
		public int compareTo(ContributeData o) {
			return o.getContributeCardNum() - this.getContributeCardNum();
		}
	}

	@Override
	public void run() {
		while (running) {
			long startTime = SystemTime.currentTimeMillis();
			try {
				boolean isOtherTaday = false;
				Calendar calendar = Calendar.getInstance();
				int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
				calendar.setTimeInMillis(lastHeatTime);
				int oldDay = calendar.get(Calendar.DAY_OF_MONTH);
				if (nowDay != oldDay) {
					isOtherTaday = true;
				}
				if (isOtherTaday) {
					//新的一天
					try{
						if (lastHeatTime != 0) {
							everyDayPrize(lastHeatTime);
						}
						
						for (int i = 0; i < isNotice.length; i++) {
							isNotice[i] = false;
						}
						
						//重新初始化boss开的公告
						isBoss1StartNotic_1 = false;
						isBoss1StartNotic_2 = false;
						isBoss2StartNotic = false;
						
						//重新计算BOSS开始时间
						boss1StartTimesLong = new long[2];
						String day = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
						String day1_1 = day + " " + boss1StartTimes[0];
						String day1_2 = day + " " + boss1StartTimes[1];
						boss1StartTimesLong[0] = DateUtil.parseDate(day1_1, "yyyy-MM-dd HH:mm:ss").getTime();
						boss1StartTimesLong[1] = DateUtil.parseDate(day1_2, "yyyy-MM-dd HH:mm:ss").getTime();
						
						String day2_1 = day + " " + boss2StartTime;
						boss2StartTimeLong = DateUtil.parseDate(day2_1, "yyyy-MM-dd HH:mm:ss").getTime();
						
						logger.warn("[新的一天开始了] [昨天=" + oldDay + "] [今天=" + nowDay + "] [头等=" + day1_1 + "] [头等=" + day1_2 + "] [船长=" + day2_1 + "]");
					}catch(Exception e) {
						logger.error("新的一天更新数据出错:", e);
					}
				}
				lastHeatTime = SystemTime.currentTimeMillis();
				if (isDoomsdayBoatBornStart() && !isBoatStartNotic) {
					//船建好了
					isBoatStartNotic = true;
					Player[] onlines = PlayerManager.getInstance().getOnlinePlayers();
					String haotian = "<f color='0xffff00'>恭喜昆仑成功建造出:" + boatNames[getBoatHoldIndex((byte)1)] + "请大家去昆仑圣殿找守船人登船</f>";
					String wuchen = "<f color='0xffff00'>恭喜九州成功建造出:" + boatNames[getBoatHoldIndex((byte)2)] + "请大家去昆仑圣殿找守船人登船</f>";
					String cangyue = "<f color='0xffff00'>恭喜万法成功建造出:" + boatNames[getBoatHoldIndex((byte)3)] + "请大家去昆仑圣殿找守船人登船</f>";
					
					for (int i = 0; i < onlines.length; i++) {
						onlines[i].sendNotice(haotian);
						onlines[i].sendNotice(wuchen);
						onlines[i].sendNotice(cangyue);
					}
				}
				if (isDoomsdatBoatStart() && !isDoomsdayBoatBornStart()) {
					//活动开启  船还未造好
					notice("世界末日即将来临，只有诺亚方舟可带我们逃离灭顶之灾。造船行动迫在眉睫，仙友们快去<f color='#FFFF00' >昆仑圣殿</f>找<f color='#FFFF00' >船运检校官</f>询问详情吧。");
				} else if (isDoomsdayBoatBornStart()) {
					//船造好了
					if (!isRefBoatNpc) {
						//刷NPC
						try{
							isRefBoatNpc = true;
							for (int i = 0; i < 3; i++) {
								int country = i + 1;
								int npcId = boatNpcIDs[getBoatHoldIndex((byte)country)];
								NPC npc = ((MemoryNPCManager)MemoryNPCManager.getNPCManager()).createNPC(npcId);
								if (npc != null) {
									Game game = GameManager.getInstance().getGameByName(kunlunshengdian, country);
									npc.setGameNames(game.gi);
									npc.setX(boatNpcXY[0]);
									npc.setY(boatNpcXY[1]);
									game.addSprite(npc);
								}else {
									logger.error("[船NPC刷失败NPC不存在] [{}] [{}]", new Object[]{country, npcId});
								}
							}
						}catch(Exception e) {
							logger.error("run刷船NPC出错:", e);
						}
					}
					notice("诺亚方舟已经造好，请各位仙友去本国的<f color='#FFFF00' >昆仑圣殿</f>登船。");
					try{
						long now = SystemTime.currentTimeMillis();
						if (boss1StartTimesLong[0] > now && boss1StartTimesLong[0] - now < 1000 * 60 * 5 && !isBoss1StartNotic_1) {
							isBoss1StartNotic_1 = true;
							Player[] players = PlayerManager.getInstance().getOnlinePlayers();
							String msg = "凶狠的末日精英将在11:00出没于诺亚方舟头等舱，消灭他就有大把的奖励拿。仙友们准备好了吗？";
							for (int j = 0; j < players.length; j++) {
								players[j].send_HINT_REQ(msg, (byte)3);
							}
						}
						if (boss1StartTimesLong[1] > now && boss1StartTimesLong[1] - now < 1000 * 60 * 5 && !isBoss1StartNotic_2) {
							isBoss1StartNotic_2 = true;
							Player[] players = PlayerManager.getInstance().getOnlinePlayers();
							String msg = "凶狠的末日精英将在22:00出没于诺亚方舟头等舱，消灭他就有大把的奖励拿。仙友们准备好了吗？";
							for (int j = 0; j < players.length; j++) {
								players[j].send_HINT_REQ(msg, (byte)3);
							}
						}
						if (boss1StartTimesLong[1] < now && now - boss1StartTimesLong[1] < 1000 * 10) {
							if (boss1Games[0] == null) {
								Player[] players = PlayerManager.getInstance().getOnlinePlayers();
								String msg = "<f color='#e86db2'>BOSS末日精英</f>出现在<f color='#f7e354'>诺亚方舟</f>地图上，请仙友们尽快将其降服！";
								for (int j = 0; j < players.length; j++) {
									players[j].send_HINT_REQ(msg, (byte)3);
									players[j].sendNotice(msg);
								}
								boss1Games[0] = new DoomsdayBossGame(boss1Mapname, 1, boss1IDs[(int)getBoatHoldIndex((byte)1)], SealManager.getInstance().getSealLevel());
							}
							if (boss1Games[1] == null) {
								boss1Games[1] = new DoomsdayBossGame(boss1Mapname, 2, boss1IDs[(int)getBoatHoldIndex((byte)2)], SealManager.getInstance().getSealLevel());
							}
							if (boss1Games[2] == null) {
								boss1Games[2] = new DoomsdayBossGame(boss1Mapname, 3, boss1IDs[(int)getBoatHoldIndex((byte)3)], SealManager.getInstance().getSealLevel());
							}
						}else if (boss1StartTimesLong[0] < now && now - boss1StartTimesLong[0] < 1000 * 10) {
							if (boss1Games[0] == null) {
								Player[] players = PlayerManager.getInstance().getOnlinePlayers();
								String msg = "<f color='#e86db2'>BOSS末日精英</f>出现在<f color='#f7e354'>诺亚方舟</f>地图上，请仙友们尽快将其降服！";
								for (int j = 0; j < players.length; j++) {
									players[j].send_HINT_REQ(msg, (byte)3);
									players[j].sendNotice(msg);
								}
								boss1Games[0] = new DoomsdayBossGame(boss1Mapname, 1, boss1IDs[(int)getBoatHoldIndex((byte)1)], SealManager.getInstance().getSealLevel());
							}
							if (boss1Games[1] == null) {
								boss1Games[1] = new DoomsdayBossGame(boss1Mapname, 2, boss1IDs[(int)getBoatHoldIndex((byte)2)], SealManager.getInstance().getSealLevel());
							}
							if (boss1Games[2] == null) {
								boss1Games[2] = new DoomsdayBossGame(boss1Mapname, 3, boss1IDs[(int)getBoatHoldIndex((byte)3)], SealManager.getInstance().getSealLevel());
							}
						}
						for (int i = 0; i < boss1Games.length; i++) {
							if (boss1Games[i] != null) {
								boss1Games[i].heatbeat();
								if (boss1Games[i].isRellOver()) {
									boss1Games[i] = null;
								}
							}
						}
					}catch(Exception e) {
						logger.error(doomsdayLoggerHead + "头等舱run心跳出错:", e);
					}
					try{
						long now = SystemTime.currentTimeMillis();
						if (boss2StartTimeLong > now && boss2StartTimeLong - now < 1000 * 60 * 5 && !isBoss2StartNotic) {
							isBoss2StartNotic = true;
							Player[] players = PlayerManager.getInstance().getOnlinePlayers();
							String msg = "发动末日阴谋的船长昆卡将在22:00出没于诺亚方舟船长室，勇士们，去消灭他斩获末日惊喜吧！";
							for (int j = 0; j < players.length; j++) {
								players[j].send_HINT_REQ(msg, (byte)3);
							}
						}
						if (boss2StartTimeLong < now && now - boss2StartTimeLong < 1000 * 10) {
							if (boss2Games[0] == null) {
								Player[] players = PlayerManager.getInstance().getOnlinePlayers();
								String msg = "<f color='#e86db2'>BOSS船长昆卡</f>出现在<f color='#f7e354'>诺亚方舟</f>地图上，请仙友们尽快将其降服！";
								for (int j = 0; j < players.length; j++) {
									players[j].send_HINT_REQ(msg, (byte)3);
									players[j].sendNotice(msg);
								}
								boss2Games[0] = new DoomsdayBossGame(boss2Mapname, 1, boss2IDs[(int)getBoatHoldIndex((byte)1)], SealManager.getInstance().getSealLevel());
							}
							if (boss2Games[1] == null) {
								boss2Games[1] = new DoomsdayBossGame(boss2Mapname, 2, boss2IDs[(int)getBoatHoldIndex((byte)2)], SealManager.getInstance().getSealLevel());
							}
							if (boss2Games[2] == null) {
								boss2Games[2] = new DoomsdayBossGame(boss2Mapname, 3, boss2IDs[(int)getBoatHoldIndex((byte)3)], SealManager.getInstance().getSealLevel());
							}
						}
						for (int i = 0; i < boss2Games.length; i++) {
							if (boss2Games[i] != null) {
								boss2Games[i].heatbeat();
								if (boss2Games[i].isRellOver()) {
									boss2Games[i] = null;
								}
							}
						}
					}catch(Exception e) {
						logger.error(doomsdayLoggerHead + "船长室run心跳出错:", e);
					}
					
					//检查福利房
					try{
						List<DoomsdayFuLiGame> games = new ArrayList<DoomsdayFuLiGame>(fuliGames.values());
						for (DoomsdayFuLiGame fuli : games) {
							if (fuli.isRellOver()) {
								fuliGames.remove(fuli.getPlayerID());
								doomsdayThreads[fuli.getThreadIndex()].removeGame(fuli);
							}
						}
					}catch(Exception e) {
						logger.error(doomsdayLoggerHead + "检查福利房run心跳出错:", e);
					}
					
				}else if (isBoatStart && !isDoomsdayBoatBornStart()) {
					//船活动结束
					isBoatStart = false;
					activityOver();
				}
			}catch (Exception e){
				logger.warn("心跳出错:", e);
			} finally {
				long cost = SystemTime.currentTimeMillis() - startTime;
				if (cost < step) {
					try {
						Thread.sleep(step - cost);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void activityOver() {
		try{
			HashMap<Long, ContributeData> datas = getAllPlayerData();
			ArrayList<ContributeData> list_datas = new ArrayList<ContributeData>();
			for (Long keyID : datas.keySet()) {
				list_datas.add(datas.get(keyID));
			}
			Collections.sort(list_datas);
			String playerNames = "末日活动圆满结束，恭喜 ";
			for (int j = 0; j < every_day_prizeNum; j++) {
				if (list_datas.size() > j) {
					ContributeData data = list_datas.get(j);
					try{
						Player player = PlayerManager.getInstance().getPlayer(data.getPlayerId());
						playerNames += CountryManager.得到国家名(player.getCountry()) + "的<f color='0x1da2dd'>" + player.getName() + "</f>总贡献榜排名:第" + (j+1)+",";
						PlayerTitlesManager.getInstance().addTitle(player, player_title_type, true);
						logger.warn("[活动结束，发称号] [{}] [{}] [{}] [{}]", new Object[]{doomsdayLoggerHead, player.getLogString(), j, data.getContributeCardNum()});
					}catch(Exception e) {
						logger.error("[发放活动结束奖励出错] [{}] [{}]", new Object[]{doomsdayLoggerHead, data.toString()});
					}
				}
			}
			playerNames += "获得<f color='0xffff00'>救世主</f>称号";
			Player[] onlines = PlayerManager.getInstance().getOnlinePlayers();
			for (int i = 0; i < onlines.length; i++) {
				onlines[i].sendNotice(playerNames);
			}
			
		}catch(Exception e) {
			logger.error("activityOver出错", e);
		}
	}
	
	/**
	 * 发某个时间排行前5的奖励
	 * @param time
	 */
	public void everyDayPrize(long time) {
		String oldDayKey = DoomsdayDCKey.DAILY_CONTRIBUTE.getDaykey(DateUtil.formatDate(new Date(time), "yyyy-MM-dd"));
		Map<Long, DoomsdayManager.ContributeData> map = getDayPlayerData(oldDayKey);
		for (int i = 1; i < 4; i++) {
			List<ContributeData> list = new ArrayList<DoomsdayManager.ContributeData>();
			for (ContributeData data : map.values()) {
				if (data.getCountry() == i) {
					list.add(data);
				}
			}
			Collections.sort(list);
			for (int j = 0; j < every_day_prizeNum; j++) {
				if (list.size() > j) {
					try{
						ContributeData data = list.get(j);
						Article a = ArticleManager.getInstance().getArticle(EVERY_DAY_PRIZE[0]);
						Article b = ArticleManager.getInstance().getArticle(EVERY_DAY_PRIZE[1]);
						if (a != null && b != null) {
							ArticleEntity entityA = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.末日活动, null, a.getColorType(), 1, true);
							ArticleEntity entityB = ArticleEntityManager.getInstance().createEntity(b, true, ArticleEntityManager.末日活动, null, a.getColorType(), 1, true);
							MailManager.getInstance().sendMail(data.getPlayerId(), new ArticleEntity[]{entityA, entityB}, "排行奖励", "恭喜您获得方舟贡献排行前5奖励。", 0, 0, 0, "末日活动每日排行奖励");
							logger.warn("[每日排行奖励] [{}] [id={}] [贡献={}] [j={}]", new Object[]{doomsdayLoggerHead, data.getPlayerId(), data.getContributeCardNum(), j});
						}else {
							logger.error("[每日排行奖励物品不存在] [{}] [{}] [{}] [{}]", new Object[]{doomsdayLoggerHead, data.getPlayerId(), i, j});
						}
					}catch(Exception e) {
						logger.error("[发放每日奖励出错] [{}] [{}]", new Object[]{doomsdayLoggerHead, i + "~" + j});
					}
				}
			}
		}
	}
	
	/**
	 * 发公告
	 * 
	 */
	public void notice(String msg) {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		for (int i = 0; i < noticeHourTimes.length; i++) {
			if (noticeHourTimes[i] == hour) {
				if (!isNotice[i]) {
					isNotice[i] = true;
					Player[] players = PlayerManager.getInstance().getOnlinePlayers();
					for (int j = 0; j < players.length; j++) {
						players[j].send_HINT_REQ(msg, (byte)3);
					}
				}
				break;
			}
		}
	}

	/**
	 * 增加兑换数据
	 * @param id
	 * @param receivePropNum
	 */
	public void addExchargeCardDaily(Player player, String reqestPropName, int receivePropNum) {
		try{
			String todayKey = DoomsdayDCKey.DAILY_CONTRIBUTE.getDaykey(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
			HashMap<Long, ContributeData> contributeDataMap = getDayPlayerData(todayKey);
	
			if (!contributeDataMap.containsKey(player.getId())) {
				contributeDataMap.put(player.getId(), new ContributeData(player.getId(), player.getCountry(), 0));
			}
			ContributeData contributeData = contributeDataMap.get(player.getId());
			if (contributeData != null) {
				contributeData.setContributeCardNum(contributeData.getContributeCardNum() + receivePropNum);
				//保存个人兑换数据
				diskCache.put(todayKey, contributeDataMap);
				addExchargeCardTotal(player, receivePropNum);
				logger.warn(player.getLogString() + doomsdayLoggerHead + "[当天贡献:" + todayKey + "] [增加贡献值:" + receivePropNum + "] [增加后:" + contributeData.getContributeCardNum() + "]");
				if (!isDoomsdayBoatBornStart()) {
					HashMap<Integer, Long[]> countrys = getCountryProgress();
					Long[] values = countrys.get((int)player.getCountry());
					int i = 0;
					for (; i < req_propNames.length; i++ ) {
						if (req_propNames[i].equals(reqestPropName)) {
							values[i] += receivePropNum;
							countrys.put((int)player.getCountry(), values);
							break;
						}
					}
					//保存国家兑换数据
					diskCache.put(DoomsdayDCKey.COUNTRY_BOAT_PROGRESS.name(), countrys);
					logger.warn(player.getLogString() + doomsdayLoggerHead + "[国家贡献:" + receivePropNum + "] [哪个:"+i+"] [增加后:" + Arrays.toString(values) + "]");
				}
			} else {
				logger.warn(player.getLogString() + doomsdayLoggerHead + "[找不到任何数据,应该是出了异常]");
			}
		}catch(Exception e) {
			logger.error("addExchargeCardDaily", e);
		}
	}

	/**
	 * 给玩家加总贡献
	 * @param player
	 * @param receivePropNum
	 */
	private void addExchargeCardTotal(Player player, int receivePropNum) {
		HashMap<Long, ContributeData> map = getAllPlayerData();
		if (!map.containsKey(player.getId())) {
			map.put(player.getId(), new ContributeData(player.getId(), player.getCountry(), 0));
		}
		ContributeData contributeData = map.get(player.getId());
		contributeData.setContributeCardNum(contributeData.getContributeCardNum() + receivePropNum);
		diskCache.put(DoomsdayDCKey.TOTAL_CONTRIBUTE.name(), map);
		logger.warn(player.getLogString() + doomsdayLoggerHead + "[总贡献] [增加贡献值:" + receivePropNum + "] [增加后:" + contributeData.getContributeCardNum() + "]");
	}
	
	public void lookSipExc (Player player) {
		try{
			if (!isDoomsdatBoatStart()) {
				player.sendError("活动还未开启，不能查看！");
				return;
			}
			
			long haveTime = doomsdayBoatBornTime - System.currentTimeMillis();
			StringBuffer msg = new StringBuffer("");
			if (haveTime > 0) {
				long data = haveTime / 1000 / 60 / 60 / 24;
				long xiaoshi = (haveTime / 1000 / 60 / 60) % 24;
				msg.append("<f color='0xe86db2'>").append("距方舟造好还有");
				msg.append(data).append("天").append(xiaoshi).append("小时").append("</f>\n");
			}else {
				msg.append("<f color='0xe86db2'>").append("方舟已经造好</f>\n");
			}
			
			HashMap<Integer, Long[]> countryProgress = getCountryProgress();
			Long[] coutry = countryProgress.get((int)player.getCountry());
			long min = coutry[0];
			if (min > coutry[1]) {
				min = coutry[1];
			}
			if (min > coutry[2]) {
				min = coutry[2];
			}
			msg.append("当前材料数量:").append("<f color='0xf7e354'>")
			.append("-").append(req_propNames[0]).append(coutry[0]).append(" ")
			.append("-").append(req_propNames[1]).append(coutry[1]).append(" ")
			.append("-").append(req_propNames[2]).append(coutry[2]).append(" ")
			.append("</f>\n");
			msg.append("当前成长值:").append("<f color='0xf7e354'>")
			.append(min)
			.append("</f>\n");
			{
				int boatIndex = getBoatHoldIndex(player.getCountry());
				String color = "";
				if (boatIndex == 0) {
					color = "0x17ba1f";
				}else if (boatIndex == 1) {
					color = "0x1da2dd";
				}else if (boatIndex == 2) {
					color = "0xfb7905";
				}
				msg.append("当前方舟品质:").append("<f color='"+color+"'>")
				.append(boatNames[boatIndex])
				.append("</f>\n");
			}
			for (int i = 0; i < boatNames.length; i++) {
				String color = "";
				if (i == 0) {
					color = "0x17ba1f";
				}else if (i == 1) {
					color = "0x1da2dd";
				}else if (i == 2) {
					color = "0xfb7905";
				}
				msg.append("<f color='"+color+"'>").append(boatNames[i]).append("</f>");
				if (i == 0 || i == 1) {
					msg.append("--------->");
				}
			}
			msg.append("\n");
			
			String[] jingyan = new String[]{"经验+5%", "经验+10%", "经验+15%"};
			String[] duihuan = new String[]{"普通兑换", "精品兑换", "顶级兑换"};
			String[] diaoluo = new String[]{"BOSS普通掉落", "BOSS精品掉落", "BOSS顶级掉落"};
			String[] neishi = new String[]{"普通内饰", "舒适内饰", "豪华内饰"};
			for (int i = 0; i < 3; i++) {
				String color = "";
				if (i == 0) {
					color = "0x17ba1f";
				}else if (i == 1) {
					color = "0x1da2dd";
				}else if (i == 2) {
					color = "0xfb7905";
				}
				msg.append("<f color='"+color+"'>").append(boatNames[i]).append("</f>");
				msg.append("(").append("<f color='0xf7e354'>").append("需成长值").append(PROGRESS_TO_BOAT_TYPE[i]).append("</f>").append("):");
				msg.append(jingyan[i]).append(" ").append(duihuan[i]).append(" ").append(diaoluo[i]).append(" ").append(neishi[i]);
				msg.append("\n");
			}
			//注意
			msg.append("<f color='0xe86db2'>注意</f>：三种材料都集齐1次，计1点成长值\n");
			msg.append("12月20日0点之后交材料只能获得贡献卡，将不再增加船的进度。");
			logger.warn(msg.toString());
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			mw.setDescriptionInUUB(msg.toString());
			mw.setOptions(new Option[]{});
			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
			player.addMessageToRightBag(req);
		}catch(Exception e) {
			logger.error("Option_LookShipExc", e);
		}
	}
	
	/**
	 * 去福利地图
	 * @param player
	 */
	public void enterFuLiMap(Player player) {
		try{
			if (!isDoomsdayBoatBornStart()) {
				player.sendError("船还未造好，请再造好后再登船");
				return;
			}
			if (player.getLevel() <= 40) {
				player.sendError("您等级太低，不能参加此活动。");
				return;
			}
			if (player.getCurrentGame() == null) {
				logger.error(doomsdayLoggerHead + " [进福利房玩家当前地图不存在] [{}]", new Object[]{player.getLogString()});
				return;
			}
			String dayKey = DoomsdayDCKey.DAILY_PLAYER_ENTER_GUAJI_RECORD.getDaykey(DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
			ArrayList<Long> dayPlayerData = getDayPlayerFuLi(dayKey);
			if (dayPlayerData.contains(player.getId())) {
				//如果已经进入过，查有没有buff，没有就不让进了
				Buff buff = player.getBuffByName(guajiBuffName);
				if (buff == null) {
					player.sendError("今天的福利时间已经用尽。");
					return;
				}
				
				int minIndex = -1;
				int minGameNum = 1000000;
				for (int i = 0; i < doomsdayThreads.length; i++) {
					if (minGameNum > doomsdayThreads[i].getFuliGames().size()) {
						minGameNum = doomsdayThreads[i].getFuliGames().size();
						minIndex = i;
					}
				}
				DoomsdayFuLiGame fuli = new DoomsdayFuLiGame(guajiMapname, monsterIDs[(player.getLevel()- 40)/10], player.getId());
				fuli.setThreadIndex(minIndex);
				doomsdayThreads[minIndex].addGame(fuli);
				fuliGames.put(player.getId(), fuli);
				//传送
				MapArea area = fuli.getGame().gi.getMapAreaByName(Translate.出生点);
				int bornX = 300;
				int bornY = 300;
				if (area != null) {
					Random random = new Random();
					bornX = area.getX()+random.nextInt(area.getWidth());
					bornY = area.getY() + random.nextInt(area.getHeight());
				}
				TransportData transportData = new TransportData(0,0,0,0,guajiMapname,bornX,bornY);
				player.getCurrentGame().transferGame(player, transportData);
				logger.warn(doomsdayLoggerHead + " [进入福利地图] [{}] [{}]", new Object[]{player.getLogString(), fuli.getLogString()});
			}else {
				//没有进入过  查是不是房间满了
				int gameNums = 0;
				int minIndex = -1;
				int minGameNum = 1000000;
				for (int i = 0; i < doomsdayThreads.length; i++) {
					gameNums += doomsdayThreads[i].getFuliGames().size();
					if (minGameNum > doomsdayThreads[i].getFuliGames().size()) {
						minGameNum = doomsdayThreads[i].getFuliGames().size();
						minIndex = i;
					}
				}
				if (gameNums >= DoomsGameMax) {
					player.sendError("福利舱当前玩家过多，请耐心等待");
					return;
				}
				//加到列表中
				dayPlayerData.add(player.getId());
				diskCache.put(dayKey, dayPlayerData);
				//加buff
				BuffTemplateManager btm = BuffTemplateManager.getInstance();
				Buff buff = btm.getBuffTemplateByName(guajiBuffName).createBuff(1);
				buff.setInvalidTime(System.currentTimeMillis() + guajiBuffTime);
				player.placeBuff(buff);
				DoomsdayFuLiGame fuli = new DoomsdayFuLiGame(guajiMapname, monsterIDs[(player.getLevel()- 40)/10], player.getId());
				doomsdayThreads[minIndex].addGame(fuli);
				fuli.setThreadIndex(minIndex);
				fuliGames.put(player.getId(), fuli);
				//传送
				MapArea area = fuli.getGame().gi.getMapAreaByName(Translate.出生点);
				int bornX = 300;
				int bornY = 300;
				if (area != null) {
					Random random = new Random();
					bornX = area.getX()+random.nextInt(area.getWidth());
					bornY = area.getY() + random.nextInt(area.getHeight());
				}
				TransportData transportData = new TransportData(0,0,0,0,guajiMapname,bornX,bornY);
				player.getCurrentGame().transferGame(player, transportData);
				logger.warn(doomsdayLoggerHead + " [进入福利地图] [{}] [{}]", new Object[]{player.getLogString(), fuli.getLogString()});
			}
		}catch(Exception e) {
			logger.error(doomsdayLoggerHead + " [进福利地图出错:] ", e);
		}
	}
	
	/**
	 * 进入贵宾地图
	 * @param player
	 */
	public void enterGuiBingMap(Player player) {
		try{
			if (!isDoomsdayBoatBornStart()) {
				player.sendError("船还未造好，请再造好后再登船");
				return;
			}
			if (player.getCurrentGame() == null) {
				logger.error(doomsdayLoggerHead + " [进贵宾室玩家当前地图不存在] [{}]", new Object[]{player.getLogString()});
				return;
			}
			// 进门
			String keyName = mapAndKeys.get(guibingMapname);
			if (keyName == null) {
				player.sendError("数据异常,请稍后再试!钥匙不存在");
				logger.error(player.getLogString() + doomsdayLoggerHead + "[进门失败] [门对应的钥匙配置不存在,map:" + guibingMapname + "]");
				return;
			}
			Article keyArticle = ArticleManager.getInstance().getArticle(keyName);
			if (keyArticle == null) {
				player.sendError("数据异常,请稍后再试!");
				logger.error(player.getLogString() + doomsdayLoggerHead + "[进门失败] [门对应的钥匙物品不存在,map:" + guibingMapname + "] [key:" + keyName + "]");
				return;
			}
			int articleNum = player.getArticleNum(keyName, keyArticle.getColorType(), BindType.BIND);
			if (articleNum <= 0) {
				player.sendError("你没有钥匙:" + keyName + ",不能进入!");
				return;
			}
			//删除钥匙
			player.removeArticleByNameColorAndBind(keyName, keyArticle.getColorType(), BindType.BIND, "进入贵宾室地图扣除", true);
			Game chuanCangGame = GameManager.getInstance().getGameByName(guibingMapname, player.getCountry());
			MapArea area = chuanCangGame.gi.getMapAreaByName(Translate.出生点);
			int bornX = 300;
			int bornY = 300;
			if (area != null) {
				Random random = new Random();
				bornX = area.getX()+random.nextInt(area.getWidth());
				bornY = area.getY() + random.nextInt(area.getHeight());
			}
			TransportData transportData = new TransportData(0,0,0,0,guibingMapname,bornX,bornY);
			player.setTransferGameCountry(player.getCountry());
			player.getCurrentGame().transferGame(player, transportData);
			logger.warn(doomsdayLoggerHead + " [进入贵宾室] [{}] [{}] [{}]", new Object[]{player.getLogString(), keyName, guibingMapname});
		}catch(Exception e){
			logger.error("enterGuiBingMap出错" + player.getLogString(), e);
		}
	}
	
	/**
	 * 进入BOSS地图
	 * @param player
	 * @param mapName
	 */
	public void enterBossMap(Player player, String mapName) {
		try{
			if (!isDoomsdayBoatBornStart()) {
				player.sendError("船还未造好，请再造好后再登船");
				return;
			}
			if (player.getCurrentGame() == null) {
				logger.error(doomsdayLoggerHead + " [进BOSS地图玩家当前地图不存在] [{}] [{}]", new Object[]{player.getLogString(), mapName});
				return;
			}
			int isBoss1Or2 = 0;
			if (boss1Mapname.equals(mapName)) {
				isBoss1Or2 = 1;
				if (boss1Games[player.getCountry() - 1] == null || boss1Games[player.getCountry() - 1].isOver()) {
					player.sendError("头等舱暂未开启，不能进入！");
					return;
				}
			} else if (boss2Mapname.equals(mapName)) {
				isBoss1Or2 = 2;
				if (boss2Games[player.getCountry() - 1] == null || boss2Games[player.getCountry() - 1].isOver()) {
					player.sendError("船长室暂未开启，不能进入！");
					return;
				}
			}
			if (isBoss1Or2 != 1 && isBoss1Or2 != 2) {
				logger.error(doomsdayLoggerHead + " [地图不是1也不是2？] [{}] [{}]", new Object[]{player.getLogString(), mapName});
				return;
			}
			// 进门
			String keyName = mapAndKeys.get(mapName);
			if (keyName == null) {
				player.sendError("数据异常,请稍后再试!钥匙不存在");
				logger.error(player.getLogString() + doomsdayLoggerHead + "[进门失败] [门对应的钥匙配置不存在,map:" + mapName + "]");
				return;
			}
			Article key = ArticleManager.getInstance().getArticle(keyName);
			if (key == null) {
				player.sendError("数据异常,请稍后再试!");
				logger.error(player.getLogString() + doomsdayLoggerHead + "[进门失败] [门对应的钥匙物品不存在,map:" + mapName + "] [key:" + keyName + "]");
				return;
			}
			int articleNum = player.getArticleNum(keyName, key.getColorType(), BindType.BIND);
			if (articleNum <= 0) {
				player.sendError("你没有钥匙:" + keyName + ",不能进入!");
				return;
			}
			HashMap<Long, List<String>> playerEnterRecord = getPlayerEnterBoss();
			List<String> enterList = playerEnterRecord.get(player.getId());
			if (enterList == null) {
				enterList = new ArrayList<String>();
				playerEnterRecord.put(player.getId(), enterList);
				diskCache.put(DoomsdayDCKey.DAILY_PLAYER_ENTER_BOSS_RECORD.name(), playerEnterRecord);
			}
			String day = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			if (enterList.contains(day)) {
				player.sendError("你今天已经进入过方舟了,明天再来吧");
				return;
			}
			//删除钥匙
			player.removeArticleByNameColorAndBind(keyName, key.getColorType(), BindType.BIND, "进入活动地图扣除", true);
			//加入今天挑战列表
			enterList.add(day);
			diskCache.put(DoomsdayDCKey.DAILY_PLAYER_ENTER_BOSS_RECORD.name(), playerEnterRecord);
			if (isBoss1Or2 == 1) {
				MapArea area = boss1Games[player.getCountry() - 1].getGame().gi.getMapAreaByName(Translate.出生点);
				int bornX = 300;
				int bornY = 300;
				if (area != null) {
					Random random = new Random();
					bornX = area.getX()+random.nextInt(area.getWidth());
					bornY = area.getY() + random.nextInt(area.getHeight());
				}
				TransportData transportData = new TransportData(0, 0, 0, 0, boss1Games[player.getCountry() - 1].getGame().gi.name, bornX, bornY);
				player.getCurrentGame().transferGame(player, transportData);
			}else if (isBoss1Or2 == 2) {
				MapArea area = boss2Games[player.getCountry() - 1].getGame().gi.getMapAreaByName(Translate.出生点);
				int bornX = 300;
				int bornY = 300;
				if (area != null) {
					Random random = new Random();
					bornX = area.getX()+random.nextInt(area.getWidth());
					bornY = area.getY() + random.nextInt(area.getHeight());
				}
				TransportData transportData = new TransportData(0, 0, 0, 0, boss2Games[player.getCountry() - 1].getGame().gi.name, bornX, bornY);
				player.getCurrentGame().transferGame(player, transportData);
			}
			logger.warn(player.getLogString() + doomsdayLoggerHead + "[进门成功] [door:" + mapName + "] [key:" + keyName + "]");
		}catch(Exception e) {
			logger.error("enterBossMap出错" + player.getLogString(), e);
		}
	}

	public void init() {
		if(true){
			return;
		}
		
		diskCache = new DefaultDiskCache(new File(getDcFile()), "末日活动", 1000L * 60 * 60 * 24 * 100);

		mapAndKeys.put(chuanCangMapNames[0], chuanCangKey);
		mapAndKeys.put(chuanCangMapNames[1], chuanCangKey);
		mapAndKeys.put(chuanCangMapNames[2], chuanCangKey);
		mapAndKeys.put(boss1Mapname, boss1Key);
		mapAndKeys.put(boss2Mapname, boss2Key);
		mapAndKeys.put(guibingMapname, guiBingKey);

		materialExchageDatas.add(new ExchageData(req_propNames[0], 1, 2, cardName, 1, 3, true));
		materialExchageDatas.add(new ExchageData(req_propNames[0], 10, 2, cardName, 10, 3, true));
		materialExchageDatas.add(new ExchageData(req_propNames[0], 100, 2, cardName, 100, 3, true));
		materialExchageDatas.add(new ExchageData(req_propNames[1], 1, 2, cardName, 1, 3, true));
		materialExchageDatas.add(new ExchageData(req_propNames[1], 10, 2, cardName, 10, 3, true));
		materialExchageDatas.add(new ExchageData(req_propNames[1], 100, 2, cardName, 100, 3, true));
		materialExchageDatas.add(new ExchageData(req_propNames[2], 1, 2, cardName, 1, 3, true));
		materialExchageDatas.add(new ExchageData(req_propNames[2], 10, 2, cardName, 10, 3, true));
		materialExchageDatas.add(new ExchageData(req_propNames[2], 100, 2, cardName, 100, 3, true));

		doomsdayStarttime = TimeTool.formatter.varChar19.parse("2012-12-09 11:00:00");
		doomsdayBoatBornTime = TimeTool.formatter.varChar19.parse("2012-12-20 00:00:00");
		doomsdayEndtime = TimeTool.formatter.varChar19.parse("2012-12-23 23:59:59");

		instance = this;
		
		doomsdayThreads = new DoomsdayThread[thread_num];
		for (int i = 0; i < thread_num; i++) {
			doomsdayThreads[i] = new DoomsdayThread();
			doomsdayThreads[i].name = "doomsdayFuLiThread" +i;
			doomsdayThreads[i].start();
		}
		
		boss1StartTimesLong = new long[2];
		String day = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		String day1_1 = day + " " + boss1StartTimes[0];
		String day1_2 = day + " " + boss1StartTimes[1];
		boss1StartTimesLong[0] = DateUtil.parseDate(day1_1, "yyyy-MM-dd HH:mm:ss").getTime();
		boss1StartTimesLong[1] = DateUtil.parseDate(day1_2, "yyyy-MM-dd HH:mm:ss").getTime();
		
		String day2_1 = day + " " + boss2StartTime;
		boss2StartTimeLong = DateUtil.parseDate(day2_1, "yyyy-MM-dd HH:mm:ss").getTime();
		
		if (isDoomsdayBoatBornStart()) {
			//如果船已经开了，就刷NPC
			isRefBoatNpc = true;
			for (int i = 0; i < 3; i++) {
				int country = i + 1;
				NPC npc = MemoryNPCManager.getNPCManager().createNPC(boatNpcIDs[getBoatHoldIndex((byte)country)]);
				Game game = GameManager.getInstance().getGameByName(kunlunshengdian, country);
				npc.setGameNames(game.gi);
				npc.setX(boatNpcXY[0]);
				npc.setY(boatNpcXY[1]);
				game.addSprite(npc);
			}
		}
		
		Thread thread = new Thread(instance, "末日活动");
		thread.start();
		ServiceStartRecord.startLog(this);
	}
	
	//末日活动是不是开了
	public boolean isDoomsdatBoatStart() {
		long now = System.currentTimeMillis();
		if (now >= doomsdayStarttime && now <= doomsdayEndtime) {
			return true;
		}
		return false;
	}
	
	//末日船是不是建好了
	public boolean isDoomsdayBoatBornStart() {
		long now = System.currentTimeMillis();
		if (now >= doomsdayBoatBornTime && now <= doomsdayEndtime) {
			isBoatStart = true;
			return true;
		}
		isBoatStartNotic = false;
		return false;
	}
	
	public float getDoomsdayExp(Player player, Monster monster, Game game) {
		Buff buff = player.getBuffByName(guajiBuffName);
		if (buff == null) {
			return 1;
		}
		if (!guajiMapname.equals(game.gi.name)) {
			return 1;
		}
		float[] expAdd = new float[]{1.05f, 1.10f, 1.15f};
		
		return expAdd[getBoatHoldIndex(player.getCountry())];
	}
	
	/**
	 * 判断地图是否可以用召集类道具
	 * @param gameName
	 * @return
	 */
	public boolean isGameCanUseZhaoJi(String gameName) {
		if (boss1Mapname.equals(gameName)) {
			return false;
		}else if (boss2Mapname.equals(gameName)) {
			return false;
		}else if (guajiMapname.equals(gameName)) {
			return false;
		}else if (guibingMapname.equals(gameName)) {
			return false;
		}else {
			for (int i = 0; i < chuanCangMapNames.length; i++) {
				if (chuanCangMapNames[i].equals(gameName)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 是不是末日地图
	 * @param gameName
	 * @return
	 */
	public boolean isToDoomsDayGame(String gameName) {
		if (!isDoomsdatBoatStart()) {
			return false;
		}
		if (boss1Mapname.equals(gameName)) {
			return true;
		}else if (boss2Mapname.equals(gameName)) {
			return true;
		}else if (guajiMapname.equals(gameName)) {
			return true;
		}
		return false;
	}
	
	public Game getDoomsDayGame(Player player, String gameName) {
		if (boss1Mapname.equals(gameName)) {
			if (boss1Games[player.getCountry() - 1] != null){
				NOTICE_CLIENT_JIAZUBOSS_REQ req = new NOTICE_CLIENT_JIAZUBOSS_REQ(GameMessageFactory.nextSequnceNum(), true);
				player.addMessageToRightBag(req);
				return boss1Games[player.getCountry() - 1].getGame();
			}else {
				return null;
			}
		} else if (boss2Mapname.equals(gameName)) {
			if (boss2Games[player.getCountry() - 1] != null){
				NOTICE_CLIENT_JIAZUBOSS_REQ req = new NOTICE_CLIENT_JIAZUBOSS_REQ(GameMessageFactory.nextSequnceNum(), true);
				player.addMessageToRightBag(req);
				return boss2Games[player.getCountry() - 1].getGame();
			}else {
				return null;
			}
		} else if (guajiMapname.equals(gameName)) {
			DoomsdayFuLiGame game = fuliGames.get(player.getId());
			if (game != null) {
				if (!game.isOver() && !game.isTimeOver()) {
					return game.getGame();
				}
			}
		}
		return null;
	}
	
	/**
	 * 得到方舟成长值
	 * @param countryType
	 * @return
	 */
	public long getBoatHold(byte countryType) {
		Map<Integer, Long[]> countryProgress = getCountryProgress();
		Long[] nowValue = countryProgress.get((int)countryType);
		long minValue = Long.MAX_VALUE;
		for (int i = 0; i < nowValue.length; i++) {
			if (minValue > nowValue[i]) {
				minValue = nowValue[i];
			}
		}
		return minValue;
	}
	
	/**
	 * 得到方舟品质
	 * @param countryType
	 * @return
	 */
	public int getBoatHoldIndex(byte countryType) {
		if (isDoomsdayBoatBornStart()) {
			if (boatTypeIndex[countryType] < 0) {
				boatTypeIndex[countryType] = getBoatHoldIndexToProgres(countryType);
			}
			return boatTypeIndex[countryType];
		}
		return getBoatHoldIndexToProgres(countryType);
	}
	
	/**
	 * 去查询国家的具体值来确定方舟品质
	 * @param countryType
	 * @return
	 */
	private int getBoatHoldIndexToProgres(byte countryType) {
		long minValue = getBoatHold(countryType);
		for (int i = PROGRESS_TO_BOAT_TYPE.length; --i >= 0; ) {
			if (PROGRESS_TO_BOAT_TYPE[i] <= minValue) {
				return i;
			}
		}
		return 0;
	}
	
	public String getBoatHoldMapName(Player player) {
		return chuanCangMapNames[getBoatHoldIndex(player.getCountry())];
	}
	
	/**
	 * 这个方法从diskCache中取  COUNTRY_BOAT_PROGRESS 这个 如果没有 他会自己创建初始化并put进去
	 * @return
	 */
	public HashMap<Integer, Long[]> getCountryProgress() {
		String coutryProgressKey = DoomsdayDCKey.COUNTRY_BOAT_PROGRESS.name();
		if (diskCache.get(coutryProgressKey) == null) {
			HashMap<Integer, Long[]> value = new HashMap<Integer, Long[]>();
			value.put(1, new Long[]{0L,0L,0L});
			value.put(2, new Long[]{0L,0L,0L});
			value.put(3, new Long[]{0L,0L,0L});
			diskCache.put(coutryProgressKey, value);
			logger.warn(doomsdayLoggerHead + "创建各个国家造船进度");
		}
		HashMap<Integer, Long[]> countryProgress = (HashMap<Integer, Long[]>) diskCache.get(coutryProgressKey);
		return countryProgress;
	}
	
	/**
	 * 这个方法从diskCache中取  DAILY_CONTRIBUTE 这个 如果没有 他会自己创建初始化并put进去
	 * @param dayKey
	 * @return
	 */
	public HashMap<Long, DoomsdayManager.ContributeData> getDayPlayerData(String dayKey) {
		if (diskCache.get(dayKey) == null) {// 没有当天数据,增加一个
			diskCache.put(dayKey, new HashMap<Long, DoomsdayManager.ContributeData>());
			logger.warn(doomsdayLoggerHead + "创建每天玩家数据" + dayKey);
		}
		HashMap<Long, ContributeData> contributeDataMap = (HashMap<Long, ContributeData>) diskCache.get(dayKey);
		return contributeDataMap;
	}
	
	/**
	 * 这个方法从diskCache中取  DAILY_PLAYER_ENTER_GUAJI_RECORD 这个 如果没有 他会自己创建初始化并put进去
	 * @param dayKey
	 * @return
	 */
	public ArrayList<Long> getDayPlayerFuLi (String dayKey) {
		if (diskCache.get(dayKey) == null) {// 没有当天数据,增加一个
			diskCache.put(dayKey, new ArrayList<Long>());
			logger.warn(doomsdayLoggerHead + "创建每天玩家福利房数据" + dayKey);
		}
		ArrayList<Long> dayPlayerIds = (ArrayList<Long>) diskCache.get(dayKey);
		return dayPlayerIds;
	}
	
	/**
	 * 这个方法从diskCache中取  TOTAL_CONTRIBUTE 这个 如果没有 他会自己创建初始化并put进去
	 * @return
	 */
	public HashMap<Long, ContributeData> getAllPlayerData() {
		String key = DoomsdayDCKey.TOTAL_CONTRIBUTE.name();
		if (diskCache.get(key) == null) {
			diskCache.put(key, new HashMap<Long, ContributeData>());
			logger.warn(doomsdayLoggerHead + "创建总玩家贡献数据");
		}
		HashMap<Long, ContributeData> map = (HashMap<Long, ContributeData>) diskCache.get(key);
		return map;
	}
	
	/**
	 * 这个方法从diskCache中取  DAILY_PLAYER_ENTER_BOSS_RECORD 这个 如果没有 他会自己创建初始化并put进去
	 * @return
	 */
	public HashMap<Long, List<String>> getPlayerEnterBoss() {
		String key = DoomsdayDCKey.DAILY_PLAYER_ENTER_BOSS_RECORD.name();
		if (diskCache.get(key) == null) {
			diskCache.put(key, new HashMap<Long, List<String>>());
		}
		HashMap<Long, List<String>> playerEnterRecord = (HashMap<Long, List<String>>) diskCache.get(key);
		return playerEnterRecord;
	}
	
}