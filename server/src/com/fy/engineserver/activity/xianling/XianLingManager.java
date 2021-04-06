package com.fy.engineserver.activity.xianling;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate_MeetMonster;
import com.fy.engineserver.datasource.buff.BuffTemplate_XianlingScore;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.homestead.faery.service.QuerySelect;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.quizActivity.Option_UnConfirm_Close;
import com.fy.engineserver.menu.xianling.Option_Confirm_ActBuff;
import com.fy.engineserver.menu.xianling.Option_Confirm_BuyEnergy;
import com.fy.engineserver.menu.xianling.Option_Confirm_ExitBuZhuo;
import com.fy.engineserver.menu.xianling.Option_Confirm_Refresh;
import com.fy.engineserver.menu.xianling.Option_Confirm_SilverRefresh;
import com.fy.engineserver.menu.xianling.Option_Confirm_UseJinfenCard;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_USER_ENTER_SERVER_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.XIANLING_OPEN_MAIN_RES;
import com.fy.engineserver.message.XL_BILLBOARD_RES;
import com.fy.engineserver.message.XL_BUTENERGY_RES;
import com.fy.engineserver.message.XL_CHALLENGE_REQ;
import com.fy.engineserver.message.XL_CHALLENGE_RES;
import com.fy.engineserver.message.XL_CHALLENGE_SURE_REQ;
import com.fy.engineserver.message.XL_CROSSBILLBOARD_RES;
import com.fy.engineserver.message.XL_GET_SCORE_PRIZE_REQ;
import com.fy.engineserver.message.XL_GET_SCORE_PRIZE_RES;
import com.fy.engineserver.message.XL_NOTICE_RES;
import com.fy.engineserver.message.XL_OPEN_MEETMONSTER_BUFF_RES;
import com.fy.engineserver.message.XL_OPEN_SCORE_BUFF_RES;
import com.fy.engineserver.message.XL_QUERY_SKILL_RES;
import com.fy.engineserver.message.XL_REFRESH_ENERGY_RES;
import com.fy.engineserver.message.XL_SCORE_PRIZE_RES;
import com.fy.engineserver.message.XL_SHOW_SKILL_HELP_RES;
import com.fy.engineserver.message.XL_TIMEDTASK_PRIZE_RES;
import com.fy.engineserver.message.XL_TIMEDTASK_REQ;
import com.fy.engineserver.message.XL_TIMEDTASK_RES;
import com.fy.engineserver.message.XL_USE_SCORE_CARD_REQ;
import com.fy.engineserver.message.XL_USE_SCORE_CARD_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.ExcelDataLoadUtil;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.NpcinfoFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.JsonUtil;
import com.xuanzhi.tools.transport.Connection;

public class XianLingManager extends Thread {
	// TODO 配logback.xml
	public static Logger logger = LoggerFactory.getLogger(XianLingManager.class);
	public static XianLingManager instance;
	public List<XianLing> activities = new ArrayList<XianLing>(); // 表里配的所有活动
	public List<XianLingLevelData> levelDatas = new ArrayList<XianLingLevelData>(); // 所有关卡信息,从1开始
	public int[] scores = new int[5]; // 怪物对应的积分，下标为颜色品质
	public int[] catchMonsterRates = new int[5]; // 怪物对应的捕捉成功几率，下标为颜色品质
	private List<BoardPrize> boardPrizes = new ArrayList<BoardPrize>(); // 排行奖励
	private List<ScorePrize> scorePrizes = new ArrayList<ScorePrize>(); // 积分奖励
	public int[] taskRates = new int[5];
	public int[] taskScores = new int[5];
	public Map<Byte, List<TimedTask>> taskTypeMap = new HashMap<Byte, List<TimedTask>>(); // Map<怪物大类, List<限时任务TimedTask>>
	public Map<Integer, TimedTask> timedTaskMap = new HashMap<Integer, TimedTask>(); // 限时任务Map<限时任务id,TimedTask>
	public Map<Byte, List<ActivityProp>> timedTaskPrizeMap = new HashMap<Byte, List<ActivityProp>>(); // 限时任务奖励Map<怪物大类,ActivityProp>
	public Map<Integer, MeetMonsterRate> meetMonsterRateMap = new HashMap<Integer, MeetMonsterRate>(); // 遇怪buff数据
	public Map<Integer, XianLingSkill> skillMap = new HashMap<Integer, XianLingSkill>(); // 技能Map<技能id, XianLingSkill>
	public Map<Integer, MonsterData> scaleMap = new HashMap<Integer, MonsterData>(); // 怪物比例Map<怪物catorageId, MonsterData>
	public Map<String, String> translateMap = new HashMap<String, String>(); // 翻译

	public int[] taskPrizeNum = new int[] { 1, 1, 1, 2, 2 }; // 0-4,五类怪物对应的限时任务奖励道具个数

	public static final long lastingTime = 60 * 60 * 60 * 1000; // 60小时(活动持续3天，第一天的10:00开启，第三天的22:00结束；)
	// public static final long lastingTime = 24 * 60 * 60 * 1000; // 测试时间
	public boolean isOpen = false; // 单服判断活动当前是否处于开启状态
	public static int NEEDLEVEL = 150; // 玩家等级限制
	public static int MAXENERGY = 30; // 真气值上限
	public static int REFRESH_TIMEDTASK_COST = 500000;// 刷新限时任务消耗银子，单位文
	public static long TIMEDTASK_REFRESHTIME = 12 * 60 * 60 * 1000;// 限时任务获得免费刷新次数间隔，单位文
	public static int REFRESH_BUFF_COST = 500000;// 刷新激活buff消耗银子，单位文
	public static long REFRESH_BUFF_LASTING = 30 * 60 * 1000;// 激活buff持续时间，单位ms
	public static long SCORE_BUFF_LASTING = 30 * 60 * 1000;// 积分卡buff持续时间，单位ms
	public static String 积分卡 = "50%积分卡";// 统计名
	public static String 高级积分卡 = "100%积分卡";// 统计名
	public static int 单次购买真气点数 = 10;
	public static int 初始购买真气银两 = 1000000;// 单位文
	public static int 购买真气增长银两 = 500000;// 单位文
	public static long 真气自动恢复间隔 = 15 * 60 * 1000;// 单位ms
	public static int 入榜积分要求 = 20000;
	public static int 本服排行数量 = 50;
	public static int 跨服排行数量 = 100;
	public static String[] showTitle = new String[] { "一星●", "二星●", "三星●", "四星●", "五星●" };
	
	public static int 提前多少分钟提醒 = 2 * 24 * 60 ; //提前两天提醒,单位：分钟
	public static int 提醒间隔分钟 = 60; //每一小时提醒一次，单位：分钟
	public static long 上次提醒时间 = 0l; 
	public static boolean 已提示过 = false; 
	public static long 活动开启时间 = 0l;  

	public static boolean activityOpening = false;

	public static SimpleEntityManager<PlayerXianLingData> xianlingEm;
	public static SimpleEntityManager<XianLingBillBoardData> boardEm;
	public Map<Long, XianLingBillBoardData> billboardMap = new HashMap<Long, XianLingBillBoardData>();
	public List<XianLingBillBoardData> csBillboardList = new ArrayList<XianLingBillBoardData>();// 单服的跨服排行
	// public Map<Long, XianLingBillBoardData> csBillboardMap = new HashMap<Long, XianLingBillBoardData>();// 单服的跨服排行
	public List<XianLingBillBoardData> crossBillboardList = new ArrayList<XianLingBillBoardData>();// 跨服的排行榜数据
	public Map<Long, XianLingBillBoardData> crossBillboardMap = new HashMap<Long, XianLingBillBoardData>();// 跨服的排行榜数据

	public DiskCache disk = null; // Map<all,List<playerId>>,Map<playerId,XianlingBillboardData>
	private String diskFile;

	public String getDiskFile() {
		return diskFile;
	}

	public void setDiskFile(String diskFile) {
		this.diskFile = diskFile;
	}

	public XianLingManager getInstance() {
		return instance;
	}

	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMonsterTitle(Monster monster) {
		int color = monster.getColor();
		if (color < 0 || color > showTitle.length) {
			return "【--(" + color + ")--】";
		}
		return showTitle[color];
	}

	/**
	 * 获取当前活动
	 * @return
	 */
	public XianLing getCurrentActivity() {
		long now = System.currentTimeMillis();
		for (XianLing xl : activities) {
			if (xl.getStartTime() < now && now < xl.getEndTime()) {
				return xl;
			}
		}
		return null;
	}

	/**
	 * 更新一条排行榜信息
	 * @param player
	 * @param score
	 * @param billboardMap
	 * @param updateCS
	 *            是否要通知跨服
	 */
	public void updateOneData(Player player, int score, long lastUpdateTime, String serverName, Map<Long, XianLingBillBoardData> billboardMap, int activityKey, boolean updateCS) {
		try {
			XianLingBillBoardData bbdata = null;
			if (!billboardMap.containsKey(player.getId())) {
				Map<Integer, XLBillBoardData> xlDataMap = new HashMap<Integer, XLBillBoardData>();
				bbdata = new XianLingBillBoardData(player.getId(), xlDataMap);
				billboardMap.put(player.getId(), bbdata);
			}
			bbdata = billboardMap.get(player.getId());
			Map<Integer, XLBillBoardData> xlDataMap = bbdata.getXlDataMap();
			if (!xlDataMap.containsKey(activityKey)) {
				xlDataMap.put(activityKey, new XLBillBoardData(activityKey, serverName, player.getName(), player.getUsername(), score, System.currentTimeMillis()));
			}
			XLBillBoardData data = xlDataMap.get(activityKey);
			data.setActivityId(activityKey);
			data.setLastUpdateTime(lastUpdateTime);
			data.setScore(score);
			data.setPlayerName(player.getName());
			data.setUserName(player.getUsername());
			data.setServerName(serverName);
			xlDataMap.put(activityKey, data);
			bbdata.setXlDataMap(xlDataMap);
			XianLingManager.boardEm.notifyFieldChange(bbdata, "xlDataMap");

			bbdata.setNotSaveVars(activityKey);
			billboardMap.put(player.getId(), bbdata);
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [更新排行榜信息] [" + player.getLogString() + "] [score:" + score + "] [billboardMap长度:" + billboardMap.size() + "] [activityKey:" + activityKey + "]");
			// 通知跨服
			if (updateCS) {
				if (disk.get("all") == null) {
					disk.put("all", new HashSet<Long>());
				}
				HashSet<Long> playerIdSet = (HashSet<Long>) disk.get("all");
				playerIdSet.add(player.getId());
				disk.put("all", playerIdSet);
				// 为了只给跨服传当期数据，new一个新对象
				Map<Integer, XLBillBoardData> xlDataMapSend = new HashMap<Integer, XLBillBoardData>();
				xlDataMapSend.put(activityKey, data);
				XianLingBillBoardData dataSend = new XianLingBillBoardData(player.getId(), xlDataMapSend);
				disk.put(player.getId(), (Serializable) dataSend);
				noticeReadData();
			}
			if (boardEm.find(bbdata.getId()) != null) {
				boardEm.notifyFieldChange(bbdata, "xlDataMap");
			} else {
				boardEm.save(bbdata);
			}

		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [更新排行榜信息,异常] [" + player.getLogString() + "] [score:" + score + "]", e);
		}
	}

	/**
	 * 
	 * @param player
	 * @return 未更新成功的玩家列表
	 */
	public void noticeReadData() {
		if (disk != null) {
			if (disk.get("all") != null) {
				HashSet<Long> playerIdSet = (HashSet<Long>) disk.get("all");
				disk.put("all", new HashSet<Long>());
				HashSet<Long> removeSet = new HashSet<Long>();
				for (Long id : playerIdSet) {
					if (logger.isDebugEnabled()) logger.debug("[仙灵] [推送跨服数据2]" + disk.get(id));
					if (disk.get(id) != null) {
						XianLingBillBoardData bbdata = (XianLingBillBoardData) disk.get(id);
						if (logger.isDebugEnabled()) logger.debug("[仙灵] [推送跨服数据3] [bbdata=" + bbdata + "]");
						if (bbdata != null) {
							Player player;
							try {
								player = PlayerManager.getInstance().getPlayer(id);
								if (player != null) {
									long endTime = getCurrentActivity() == null ? 0 : getCurrentActivity().getEndTime();
									String result = sendUpdateInfo(player, bbdata, getActivitykey(), endTime);
									if (logger.isInfoEnabled()) logger.info("[仙灵] [推送跨服数据] [" + player.getLogString() + "] [result:" + result + "]");
									if (result != null && result.equals("sendOK")) {
										removeSet.add(id);
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				if (removeSet.size() > 0) {
					for (Long id : removeSet) {
						playerIdSet.remove(id);
					}
				}
				HashSet<Long> currentIdSet = (HashSet<Long>) disk.get("all");
				currentIdSet.addAll(playerIdSet);
				disk.put("all", currentIdSet);
			}
		}
	}

	/** 更新本地排行帮数据中的跨服排名 */
	public void updateSelfServerCrossRank() {
		Set<Long> findSet = new HashSet<Long>();
		for (int i = 0; i < csBillboardList.size(); i++) {
			XianLingBillBoardData bd = csBillboardList.get(i);
			Map<Integer, XLBillBoardData> xlDataMap = bd.getXlDataMap();
			if (xlDataMap != null && xlDataMap.containsKey(getActivitykey())) {
				XLBillBoardData data = xlDataMap.get(getActivitykey());
				bd.setCrossServerRank(data.getCrossServerRank());
				bd.setNotSaveVars(getActivitykey());
				xlDataMap.put(getActivitykey(), data);
				if (billboardMap.containsKey(bd.getId())) {
					findSet.add(bd.getId());
					XianLingBillBoardData bd1 = billboardMap.get(bd.getId());
					Map<Integer, XLBillBoardData> xlDataMap1 = bd1.getXlDataMap();
					if (xlDataMap1 != null && xlDataMap1.containsKey(getActivitykey())) {
						XLBillBoardData data1 = xlDataMap1.get(getActivitykey());
						data1.setCrossServerRank(bd.getCrossServerRank());
						xlDataMap1.put(getActivitykey(), data1);
					}
					bd1.setCrossServerRank(bd.getCrossServerRank());
					bd1.setXlDataMap(xlDataMap1);
					billboardMap.put(bd.getId(), bd1);
				}
			}
			bd.setXlDataMap(xlDataMap);
			csBillboardList.set(i, bd);
		}
		for (Long id : billboardMap.keySet()) {
			if (!findSet.contains(id)) {
				XianLingBillBoardData bd1 = billboardMap.get(id);
				Map<Integer, XLBillBoardData> xlDataMap1 = bd1.getXlDataMap();
				if (xlDataMap1 != null && xlDataMap1.containsKey(getActivitykey())) {
					XLBillBoardData data1 = xlDataMap1.get(getActivitykey());
					data1.setCrossServerRank(-1);
					xlDataMap1.put(getActivitykey(), data1);
				}
				bd1.setCrossServerRank(-1);
				bd1.setXlDataMap(xlDataMap1);
				billboardMap.put(id, bd1);
			}
		}
	}

	/**
	 * 更新本服排行榜中的玩家跨服名次crossServerRank(从0开始),并返回某个玩家的跨服名次
	 */
	public int updateCrossServerBillBoard(List<XianLingBillBoardData> billboardList, Player player) {
		int tempIndex = 0;
		int crossRank = -1;
		for (XianLingBillBoardData bd : billboardList) {
			long playerId = bd.getId();
			for (int i = tempIndex; i < csBillboardList.size(); i++) {
				if (csBillboardList.get(i).getId() == playerId) {
					Map<Integer, XLBillBoardData> xlDataMap = csBillboardList.get(i).getXlDataMap();
					if (xlDataMap != null && xlDataMap.containsKey(getActivitykey())) {
						XLBillBoardData data = xlDataMap.get(getActivitykey());
						bd.setCrossServerRank(i);
						if (player != null && player.getId() == playerId) {
							crossRank = i;
							if (logger.isDebugEnabled()) logger.debug("[仙灵] [" + player.getLogString() + "] [crossRank:" + crossRank + "]");
							tempIndex = i + 1;
							continue;
						}
					}
				}
			}
		}
		return crossRank;
	}

	/**
	 * 更新跨服排行榜中的玩家跨服名次crossServerRank(从0开始)
	 */
	public void updateCrossServerRank() {
		if (crossBillboardList.size() > 0) {
			for (int i = 0; i < crossBillboardList.size(); i++) {
				Map<Integer, XLBillBoardData> xlDataMap = crossBillboardList.get(i).getXlDataMap();
				if (xlDataMap != null && xlDataMap.containsKey(activityKey)) {
					xlDataMap.get(activityKey).setCrossServerRank(i);
					crossBillboardList.get(i).setCrossServerRank(i);
					crossBillboardList.get(i).setXlDataMap(xlDataMap);
				}
			}
		}
	}

	/**
	 * 排序
	 */
	private static Comparator<XianLingBillBoardData> order = new Comparator<XianLingBillBoardData>() {

		@Override
		public int compare(XianLingBillBoardData o1, XianLingBillBoardData o2) {
			if (o1.getScore() < o2.getScore()) {
				return 1;
			} else if (o1.getScore() == o2.getScore()) {
				if (o1.getLastUpdateTime() > o2.getLastUpdateTime()) {
					return 1;
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		}

	};

	/**
	 * 得到要刷的怪物id
	 * @param level
	 *            关卡等级
	 * @return
	 */
	public int getOneMonster(Player player, int level) {
		XianLingLevelData data = levelDatas.get(level);
		String[] monsterIds = data.getMonsterCategoryIds();
		int[] rates = data.getRates();
		// 根据玩家的遇怪buff更新概率数组
		if (data.getType() == 0 && player.getMeetMonsterBuffId() > 0) {
			rates = meetMonsterRateMap.get(player.getMeetMonsterBuffId()).getMeetMonsterRates();
		}
		if (logger.isDebugEnabled()) logger.debug("[仙灵] [刷怪几率:" + Arrays.toString(rates) + "] [level(从1开始):" + (level + 1) + "] [" + player.getLogString() + "]");

		int index = getIndexByRate(rates);
		if (index >= 0) {
			String[] mIds = monsterIds[index].split(",");
			Random random = new Random();
			int randomIndex = random.nextInt(mIds.length);
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [刷怪颜色:" + index + "] [怪物cId:" + mIds[randomIndex] + "] [level(从1开始):" + (level + 1) + "] [" + player.getLogString() + "]");
			return Integer.valueOf(mIds[randomIndex]);
		}
		if (logger.isWarnEnabled()) logger.warn("[仙灵] [刷怪失败] [level(从1开始):" + (level + 1) + "]" + player.getLogString());
		return -1;
	}

	/**
	 * 全概率获得随机得到的结果
	 * @param rates
	 * @return
	 */
	public int getIndexByRate(int rates[]) {
		int allValue = 0;
		for (int i = 0; i < rates.length; i++) {
			allValue = allValue + rates[i];
		}
		Random random = new Random();
		int randomNum = random.nextInt(allValue) + 1;
		int nowValue = 0;
		for (int i = 0; i < rates.length; i++) {
			if (nowValue < randomNum && randomNum <= nowValue + rates[i]) {
				return i;
			}
			nowValue = nowValue + rates[i];
		}
		ActivitySubSystem.logger.error("[获取失败]");
		return -1;
	}

	public void sendNotice(int type, Player player) {
		PlayerXianLingData xianlingData = player.getXianlingData();
		long timedTaskLeftTime = 0l;
		if (xianlingData != null) {
			if (type == 1) {
				if (xianlingData.getTaskId() > 0) {
					timedTaskLeftTime = (xianlingData.getNextRefreshTime() - System.currentTimeMillis()) > 0 ? (xianlingData.getNextRefreshTime() - System.currentTimeMillis()) : 0l;
				} else {
					timedTaskLeftTime = -1l;
				}
				if (timedTaskLeftTime <= 0) {
					if (XianLingManager.logger.isDebugEnabled()) XianLingManager.logger.debug("[仙灵] [限时任务有免费刷新次数] [activenessSubSystem]" + player.getLogString());
					player.addMessageToRightBag(new XL_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 1));
				}
			} else if (type == 2) {
				// 如果有未领取的积分奖励则提示
				boolean hasUntakePrize = false;
				for (int i = 0; i < scorePrizes.size(); i++) {
					ScorePrize sp = scorePrizes.get(i);
					if (sp != null) {
						int needScore = sp.getNeedScore();
						if (xianlingData.getScore() > needScore) {
							if (xianlingData.getTakeScorePrizeMap().containsKey(needScore)) {
								boolean take = xianlingData.getTakeScorePrizeMap().containsKey(needScore);
								if (!take) {
									hasUntakePrize = true;
								}
							} else {
								hasUntakePrize = true;
							}
						}
					}
				}
				if (hasUntakePrize) {
					if (XianLingManager.logger.isDebugEnabled()) XianLingManager.logger.debug("[仙灵] [有未领取的积分奖励] [XianLingManager.XIANLING_OPEN_MAIN_RES]" + player.getLogString());
					player.addMessageToRightBag(new XL_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 2));
				}
			}
		}
	}

	/**
	 * 打开主面板
	 * @param player
	 */
	public void handle_OPEN_MAIN_REQ(Player player) {
		// if (getCurrentActivity() == null) {
		// player.sendError(Translate.活动未开启);
		// }
		PlayerXianLingData xianLingData = player.getXianlingData();
		if (xianLingData != null) {
			int oldActivityKey = xianLingData.getActivityKey();
			if (getCurrentActivity() != null) {
				int currActivityKey = getCurrentActivity().getActivityId();
				if (oldActivityKey != currActivityKey) {
					resetPlayerXianLingData(player);
					xianLingData.setActivityKey(currActivityKey);
					if (logger.isDebugEnabled()) logger.debug("[仙灵] [发送打开主面板协议] [XianLingManager.handle_OPEN_MAIN_REQ] [" + player.getLogString() + "] [清数据------1]");
				}
			} else {
				if (oldActivityKey != 0) {
					resetPlayerXianLingData(player);
					xianLingData.setActivityKey(0);
					if (logger.isDebugEnabled()) logger.debug("[仙灵] [发送打开主面板协议] [XianLingManager.handle_OPEN_MAIN_REQ] [" + player.getLogString() + "] [清数据------2]");
				}
			}
			String icon = "";
			long buff_meetMongsterLeftTime = 0l;
			long buff_xianlingScoreLeftTime = 0l;
			int buff_xianlingScoreLevel = 0;
			long tempId = 0l;
			int meetMonsterBuffId = player.getMeetMonsterBuffId();
			long timedTaskLeftTime = 0l;
			String monsterIcon = "";
			ArrayList<Buff> buffs = player.getBuffs();
			if (meetMonsterBuffId > 0) {
				MeetMonsterRate mm = meetMonsterRateMap.get(meetMonsterBuffId);
				icon = mm.getIcon();
				for (Buff buff : buffs) {
					if (buff.getTemplate() instanceof BuffTemplate_MeetMonster) {
						buff_meetMongsterLeftTime = buff.getInvalidTime() - System.currentTimeMillis();
					}
				}
			}
			int scoreBuff = player.getScoreBuff();
			if (scoreBuff > 0) {
				for (Buff buff : buffs) {
					// 积分卡buff
					if (buff.getTemplate() instanceof BuffTemplate_XianlingScore) {
						buff_xianlingScoreLeftTime = buff.getInvalidTime() - System.currentTimeMillis();
						buff_xianlingScoreLevel = buff.getLevel();
					}
				}
				if (buff_xianlingScoreLevel == 0) {
					Article a = ArticleManager.getInstance().getArticleByCNname(积分卡);
					if (a != null) {
						ArticleEntity ae;
						try {
							ae = ArticleEntityManager.getInstance().createTempEntity(a, false, ArticleEntityManager.仙灵临时物品, null, a.getColorType());
							if (ae != null) {
								tempId = ae.getId();
							}
						} catch (Exception e) {
							if (logger.isDebugEnabled()) logger.debug("[仙灵] [创建积分卡异常] [XianLingManager.handle_OPEN_MAIN_REQ]" + player.getLogString(), e);
						}
					}
				} else if (buff_xianlingScoreLevel == 1) {
					Article a = ArticleManager.getInstance().getArticleByCNname(高级积分卡);
					if (a != null) {
						ArticleEntity ae;
						try {
							ae = ArticleEntityManager.getInstance().createTempEntity(a, false, ArticleEntityManager.仙灵临时物品, null, a.getColorType());
							if (ae != null) {
								tempId = ae.getId();
							}
						} catch (Exception e) {
							if (logger.isDebugEnabled()) logger.debug("[仙灵] [创建高级积分卡异常] [XianLingManager.handle_OPEN_MAIN_REQ]" + player.getLogString(), e);
						}
					}
				}
			}
			if (xianLingData.getTaskId() > 0) {
				TimedTask task = timedTaskMap.get(xianLingData.getTaskId());
				if (task != null) {
					monsterIcon = task.getMonsterIcon();
				}
				timedTaskLeftTime = (xianLingData.getNextRefreshTime() - System.currentTimeMillis()) > 0 ? (xianLingData.getNextRefreshTime() - System.currentTimeMillis()) : 0l;
			} else {
				timedTaskLeftTime = -1l;
			}
			if (timedTaskLeftTime <= 0) {
				if (logger.isDebugEnabled()) logger.debug("[仙灵] [限时任务有免费刷新次数] [XianLingManager.handle_OPEN_MAIN_REQ]" + player.getLogString());
				player.addMessageToRightBag(new XL_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 1));
			}
			String mapNames[] = new String[levelDatas.size()];
			for (int i = 0; i < levelDatas.size(); i++) {
				mapNames[i] = levelDatas.get(i).getName();
			}
			XIANLING_OPEN_MAIN_RES res = new XIANLING_OPEN_MAIN_RES(GameMessageFactory.nextSequnceNum(), xianLingData.getScore(), icon, buff_meetMongsterLeftTime, tempId, buff_xianlingScoreLeftTime, buff_xianlingScoreLevel, timedTaskLeftTime, monsterIcon, xianLingData.getMaxLevel(), xianLingData.getEnergy(), MAXENERGY, mapNames, getCurrentActivity() != null);
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [发送打开主面板协议] [XianLingManager.handle_OPEN_MAIN_REQ] [" + player.getLogString() + "] [buff_xianlingScoreLevel]");
			player.addMessageToRightBag(res);
			sendNotice(2, player);
		} else {
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [获取玩家仙灵数据失败] [XianLingManager.handle_OPEN_MAIN_REQ]" + player.getLogString());
		}
	}

	/**
	 * 点关卡图标
	 * @param level
	 * @param player
	 */
	public XL_CHALLENGE_RES handle_XL_CHALLENGE_REQ(XL_CHALLENGE_REQ message, Player player) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.仙灵大会)) {
			player.sendError(Translate.合服功能关闭提示);
			return null;
		}
		if (XianLingChallengeManager.getInstance().findXLChallenge(player) != null) {
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [正在其他关卡挑战]  [XianLingManager.handle_XL_CHALLENGE_RES] [" + player.getLogString() + "]");
			return null;
		}
		if (player.isFlying()) {
			player.downFromHorse(true);
		}
		PlayerXianLingData xianlingData = XianLingManager.instance.getPlayerXianLingData(player);
		if (xianlingData != null) {
			XL_CHALLENGE_REQ req = (XL_CHALLENGE_REQ) message;
			int level = req.getLevel(); // 客户端传过来的从0开始
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [请求进入关卡]  [XianLingManager.handle_XL_CHALLENGE_RES] [" + player.getLogString() + "] [关卡从1开始:" + (level + 1) + "] [当前真气:" + xianlingData.getEnergy() + "]");
			XianLingLevelData levelData = XianLingManager.instance.levelDatas.get(level);
			if (levelData != null) {
				String result = XianLingChallengeManager.instance.canChallenge(player, levelData);
				if (result == null) {
					if (System.currentTimeMillis() - xianlingData.getLastCostEnergyTime() > 1000) {
						xianlingData.setEnergy(player.getXianlingData().getEnergy() - 1);
						try {
							StatClientService statClientService = StatClientService.getInstance();
							NpcinfoFlow npcinfoFlow = new NpcinfoFlow();
							npcinfoFlow.setAward("0");
							CareerManager cm = CareerManager.getInstance();
							npcinfoFlow.setCareer(cm.getCareer(player.getCareer()).getName());
							npcinfoFlow.setColumn1(player.getName());
							npcinfoFlow.setColumn2("");
							npcinfoFlow.setCreateDate(new Date().getTime());
							npcinfoFlow.setFenQu(GameConstants.getInstance().getServerName());
							npcinfoFlow.setGameLevel(player.getLevel());
							npcinfoFlow.setGetDaoJu(0);
							npcinfoFlow.setGetWuPin(0);
							npcinfoFlow.setGetYOuXiBi(0);
							Connection conn = player.getConn();
							if (conn != null) {
								NEW_USER_ENTER_SERVER_REQ mess = (NEW_USER_ENTER_SERVER_REQ) conn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
								if (mess != null) {
									npcinfoFlow.setJixing(mess.getPhoneType());
								}
							}
							npcinfoFlow.setNpcName("仙灵大会");
							npcinfoFlow.setTaskType("消耗真气");
							npcinfoFlow.setUserName(player.getUsername());
							statClientService.sendNpcinfoFlow("", npcinfoFlow);
						} catch (Exception e) {
							if (logger.isErrorEnabled()) logger.warn("[仙灵] [统计真气]  [XianLingManager.handle_XL_CHALLENGE_RES] [" + player.getLogString() + "] [关卡从1开始:" + levelData.getLevel() + "]", e);
						}
						xianlingData.setLastCostEnergyTime(System.currentTimeMillis());
						if (xianlingData.getEnergy() == MAXENERGY - 1) {
							xianlingData.setNextResumeEnergy(System.currentTimeMillis() + 真气自动恢复间隔);
						}
						if (logger.isWarnEnabled()) logger.warn("[仙灵] [进关卡扣真气]  [XianLingManager.handle_XL_CHALLENGE_RES] [" + player.getLogString() + "] [关卡从1开始:" + levelData.getLevel() + "] [扣除后真气:" + xianlingData.getEnergy() + "]");
					} else {
						player.sendError(Translate.整理背包太过频繁);
						return null;
					}
					if (xianlingData.getMaxLevel() + 1 == levelData.getLevel()) {
						xianlingData.setMaxLevel(xianlingData.getMaxLevel() + 1);
						if (logger.isDebugEnabled()) logger.debug("[仙灵] [进关卡更新挑战过的最大关卡从1开始:" + xianlingData.getMaxLevel() + "]  [XianLingManager.handle_XL_CHALLENGE_RES]" + player.getLogString());
					}
					int categoryId = getOneMonster(player, level);
					Monster monster = getOneMonater(player, categoryId);
					if (monster == null) {
						if (logger.isDebugEnabled()) logger.debug("[仙灵] [发送刷怪协议] [monster=null] [XianLingManager.handle_XL_CHALLENGE_RES]" + player.getLogString());
						return null;
					}
					XL_CHALLENGE_RES res = send_XL_CHALLENGE_RES(player, levelData, monster, categoryId);
					return res;
				} else {
					player.sendError(result);
					if (logger.isDebugEnabled()) logger.debug("[仙灵] [判断是否可挑战] [XianLingChallengeManager.canChallenge] [" + player.getLogString() + "] [" + result + "]");
				}
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [获取玩家仙灵数据失败] [XianLingManager.handle_XL_CHALLENGE_REQ]" + player.getLogString());
		}
		return null;
	}

	/**
	 * 刷新怪
	 * @param level
	 * @param player
	 */
	public Monster getOneMonater(Player player, int categoryId) {

		if (categoryId > 0) {
			Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(categoryId);
			monster.setOwner(player);
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [刷怪成功] [" + player.getLogString() + "] [怪物CategoryId:" + categoryId + "] [" + monster.getName() + "] [color:" + monster.getColor() + "]");
			return monster;
		}
		if (logger.isDebugEnabled()) logger.debug("[仙灵] [发送刷怪失败] [monster=null] [XianLingManager.getOneMonater]" + player.getLogString());
		return null;
	}

	public XL_CHALLENGE_RES send_XL_CHALLENGE_RES(Player player, XianLingLevelData levelData, Monster monster, int categoryId) {
		PlayerXianLingData xianLingData = player.getXianlingData();
		if (xianLingData != null) {
			if (xianLingData.getTaskId() > 0 && xianLingData.getTaskState() == 1) {
				if (timedTaskMap.containsKey(xianLingData.getTaskId())) {
					TimedTask task = timedTaskMap.get(xianLingData.getTaskId());
					if (monster.getSpriteCategoryId() == task.getMonsterCategoryId()) {
						// 是目标怪
						player.sendError(Translate.遇到目标怪);
					}
				} else {
					if (logger.isWarnEnabled()) logger.warn("[仙灵] [获取限时任务数据失败] [XianLingManager.handle_XL_CHALLENGE_RES]" + player.getLogString());
				}
			}
			int scale = scaleMap.get(categoryId) == null ? 1000 : scaleMap.get(categoryId).getScale();
			int x = scaleMap.get(categoryId) == null ? 0 : scaleMap.get(categoryId).getX();
			int y = scaleMap.get(categoryId) == null ? 0 : scaleMap.get(categoryId).getY();
			XL_CHALLENGE_RES res = new XL_CHALLENGE_RES(GameMessageFactory.nextSequnceNum(), levelData.getLevel() - 1, levelData.getName(), monster.getName(), monster.getAvata(), scale, x, y, monster.getColor(), monster instanceof BossMonster, monster.getId(), categoryId, xianLingData.getEnergy(), xianLingData.getMaxLevel());
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [发送挑战协议] [XianLingManager.handle_XL_CHALLENGE_RES] [" + player.getLogString() + "] [scale:" + scaleMap.get(categoryId) + "]");
			return res;
		} else {
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [获取玩家仙灵数据失败] [XianLingManager.handle_XL_CHALLENGE_RES]" + player.getLogString());
		}
		return null;
	}

	/**
	 * 确认挑战协议
	 * @param req
	 * @param player
	 */
	public void handle_open_guanqia_sure(XL_CHALLENGE_SURE_REQ req, Player player) {
		if (req != null) {
			long monsterId = req.getMonsterId();
			int categoryId = req.getCategoryId();
			Monster monster = MemoryMonsterManager.getMonsterManager().getMonster(monsterId);
			if (monster == null) {
				if (logger.isDebugEnabled()) logger.debug("[仙灵] [确认挑战] [monster=null] [XianLingManager.handle_open_guanqia_sure] [" + player.getLogString() + "] [categoryId:" + categoryId + "]");
				return;
			}
			monster.setName(getMonsterTitle(monster) + monster.getName());
			monster.setOwner(player);
			monster.setLevel(player.getLevel());
			XianLingChallengeManager.instance.startChallenge(player, req.getLevel(), monsterId, categoryId);
		}
	}

	/**
	 * 请求技能
	 * @param player
	 */
	public XL_QUERY_SKILL_RES handle_QUERY_SKILL_REQ(Player player) {
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			if (!xianlingData.isShownHelp()) {
				player.addMessageToRightBag(new XL_SHOW_SKILL_HELP_RES(GameMessageFactory.nextSequnceNum()));
				xianlingData.setShownHelp(true);
			}
			// Map<Integer, Long> skillCDMap = xianlingData.getSkillCDMap();
			int publicCDTime = 0;
			int skillIds[] = new int[skillMap.size()];
			String icons[] = new String[skillMap.size()];
			long cdTimes[] = new long[skillMap.size()];
			int nums[] = new int[skillMap.size()];
			Integer[] ids = skillMap.keySet().toArray(new Integer[0]);
			for (int i = 0; i < ids.length; i++) {
				XianLingSkill skill = skillMap.get(ids[i]);
				if (skill != null) {
					publicCDTime = skill.getPuclicCDTime();
					skillIds[i] = skill.getSkillId();
					icons[i] = skill.getIcon();
					cdTimes[i] = skillMap.get(skill.getSkillId()).getCdTime();
					Article a = ArticleManager.getInstance().getArticleByCNname(skill.getArticleCNName());
					if (a != null) {
						nums[i] = player.countArticleInKnapsacksByName(a.getName());
					}
				}
			}
			XL_QUERY_SKILL_RES res = new XL_QUERY_SKILL_RES(GameMessageFactory.nextSequnceNum(), publicCDTime, skillIds, icons, cdTimes, nums, XianLingChallengeManager.countDownTime);
			if (logger.isErrorEnabled()) logger.error("[仙灵] [发送请求技能] [XianLingManager.handle_QUERY_SKILL_REQ] [" + player.getLogString() + "] [" + Arrays.toString(skillIds) + "]");
			return res;
		} else {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [请求技能] [PlayerXianLingData=null] [XianLingManager.handle_open_guanqia_sure]" + player.getLogString());
		}
		return null;
	}

	Random random = new Random();

	/**
	 * 处理打开限时任务面板协议
	 */
	public XL_TIMEDTASK_RES handle_XL_TIMEDTASK_REQ(XL_TIMEDTASK_REQ req, Player player) {
		if (!isOpen) {
			player.sendError(Translate.不在时间段);
			return null;
		}
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			byte operateType = req.getOptrateType();
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [打开限时任务面板] [XianLingManager.handle_XL_TIMEDTASK_REQ] [" + player.getLogString() + "] [operateType:" + operateType + "] [taskState:" + xianlingData.getTaskState() + "]");
			TimedTask task = null;
			if (operateType == (byte) 0) {
				if (xianlingData.getTaskId() <= 0) {
					// return refreshTimedTask(player, false);
					return new XL_TIMEDTASK_RES(req.getSequnceNum(), -1, "", "", new String[] {}, 0, 0, 0, 0, false, "", Translate.限时任务描述, 0, new long[] {}, new int[] {}, new boolean[] {}, -1, (byte) 0, false, REFRESH_TIMEDTASK_COST);
				} else {
					task = timedTaskMap.get(xianlingData.getTaskId());
					return send_TIMEDTASK_RES(task, xianlingData, player);
				}

			} else if (operateType == (byte) 1) {
				if (System.currentTimeMillis() > xianlingData.getNextRefreshTime()) {
					return refreshTimedTask(player, false);
				} else if (xianlingData.getTaskState() == (byte) 2) {
					if (xianlingData.isTakePrize()) {
						// 扣银子刷新
						return refreshTimedTask(player, true);
					} else {
						// 未领取奖励，是否刷新
						WindowManager windowManager = WindowManager.getInstance();
						MenuWindow mw = windowManager.createTempMenuWindow(600);
						mw.setDescriptionInUUB(Translate.限时任务未领奖);
						if (logger.isDebugEnabled()) logger.debug("[仙灵] [刷新限时任务二次提示] [XianLingManager.handle_XL_TIMEDTASK_REQ]" + player.getLogString());
						Option_Confirm_Refresh o1 = new Option_Confirm_Refresh();
						o1.setText(Translate.确定);

						Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
						o2.setText(Translate.取消);
						mw.setOptions(new Option[] { o1, o2 });
						player.addMessageToRightBag(new QUERY_WINDOW_RES(req.getSequnceNum(), mw, mw.getOptions()));
						return null;
					}
				} else if (xianlingData.getTaskState() == (byte) 0) {
					return refreshTimedTask(player, false);
				} else {
					return refreshTimedTask(player, true);
				}
			}
		} else {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [获取玩家仙灵数据失败] [XianLingManager.handle_XL_TIMEDTASK_REQ]" + player.getLogString());
		}
		return null;
	}

	/**
	 * 发送限时任务协议
	 * @param task
	 * @param xianlingData
	 */
	public XL_TIMEDTASK_RES send_TIMEDTASK_RES(TimedTask task, PlayerXianLingData xianlingData, Player player) {
		if (task != null) {
			Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(task.getMonsterCategoryId());
			String[] articleCNNames = xianlingData.getArticleCNName();
			long[] tempIds = new long[articleCNNames.length];
			for (int i = 0; i < tempIds.length; i++) {
				Article a = ArticleManager.getInstance().getArticleByCNname(articleCNNames[i]);
				if (a != null) {
					ArticleEntity ae;
					try {
						ae = ArticleEntityManager.getInstance().createTempEntity(a, xianlingData.getBind()[i], ArticleEntityManager.仙灵临时物品, null, xianlingData.getArticleColor()[i]);
						if (ae != null) {
							tempIds[i] = ae.getId();
						}
					} catch (Exception e) {
						if (logger.isErrorEnabled()) logger.error("[仙灵] [异常] [奖励物品不存在] [" + articleCNNames[i] + "] [XianLingManager.send_TIMEDTASK_RES]" + player.getLogString(), e);
						e.printStackTrace();
					}
				}
			}
			int x = scaleMap.get(task.getMonsterCategoryId()) == null ? 0 : scaleMap.get(task.getMonsterCategoryId()).getX();
			int y = scaleMap.get(task.getMonsterCategoryId()) == null ? 0 : scaleMap.get(task.getMonsterCategoryId()).getY();
			int scale = scaleMap.get(task.getMonsterCategoryId()) == null ? 1000 : scaleMap.get(task.getMonsterCategoryId()).getScale();

			XL_TIMEDTASK_RES res = new XL_TIMEDTASK_RES(GameMessageFactory.nextSequnceNum(), task.getTaskId(), task.getMonsterIcon(), monster.getName(), monster.getAvata(), monster.getColor(), scale, x, y, monster instanceof BossMonster, task.getDes(), Translate.限时任务描述, taskScores[task.getType()], tempIds, xianlingData.getArticleNum(), xianlingData.getBind(), xianlingData.getNextRefreshTime() - System.currentTimeMillis(), xianlingData.getTaskState(), xianlingData.isTakePrize(), REFRESH_TIMEDTASK_COST);
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [发送限时任务协议] [XianLingManager.send_TIMEDTASK_RES]" + player.getLogString());
			return res;
		} else {
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [发送限时任务协议] [task=null] [XianLingManager.send_TIMEDTASK_RES]" + player.getLogString());
		}
		return null;
	}

	/**
	 * 刷新一个限时任务给玩家
	 * @param xianlingData
	 * @return
	 */
	public XL_TIMEDTASK_RES refreshTimedTask(Player player, boolean costSilver) {
		TimedTask task = null;
		try {
			PlayerXianLingData xianlingData = player.getXianlingData();
			if (xianlingData != null) {
				byte type = (byte) getIndexByRate(taskRates);
				if (taskTypeMap.keySet().contains(type)) {
					int index = random.nextInt(taskTypeMap.get(type).size());
					if (logger.isDebugEnabled()) logger.debug("[仙灵] [进入刷新限时任务] [XianLingManager.refreshTimedTask] [" + player.getLogString() + "]  [taskState:" + xianlingData.getTaskState() + "] [type:" + type + "] [index:" + index + "]");
					task = taskTypeMap.get(type).get(index);
					if (costSilver) {
						// 扣银子刷新限时任务，二次提示
						WindowManager windowManager = WindowManager.getInstance();
						MenuWindow mw = windowManager.createTempMenuWindow(600);
						mw.setDescriptionInUUB(Translate.translateString(Translate.银子刷新限时任务, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(REFRESH_TIMEDTASK_COST) } }));
						if (logger.isDebugEnabled()) logger.debug("[仙灵] [银子刷新限时任务二次提示] [XianLingManager.refreshTimedTask]" + player.getLogString());
						Option_Confirm_SilverRefresh o1 = new Option_Confirm_SilverRefresh(xianlingData, task);
						o1.setText(Translate.确定);

						Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
						o2.setText(Translate.取消);
						mw.setOptions(new Option[] { o1, o2 });
						player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
						return null;
					}
					xianlingData.setTaskId(task.getTaskId());
					xianlingData.setTaskState((byte) 1);
					xianlingData.setTakePrize(false);
					xianlingData.setNextRefreshTime(System.currentTimeMillis() + TIMEDTASK_REFRESHTIME);

					int num = taskPrizeNum[task.getType()];
					List<ActivityProp> prizeList = timedTaskPrizeMap.get(task.getType());
					if (prizeList != null) {
						// 分两种情况写是为了容错的
						if (prizeList.size() > num) {
							String[] articleCNName = new String[num];
							int[] articleColor = new int[num];
							int[] articleNum = new int[num];
							boolean[] bind = new boolean[num];
							List<Integer> randomList = new ArrayList<Integer>();
							for (int i = 0; i < articleCNName.length; i++) {
								boolean find = false;
								while (!find) {
									int order = random.nextInt(prizeList.size());
									if (!randomList.contains(order)) {
										find = true;
										randomList.add(order);
										articleCNName[i] = prizeList.get(order).getArticleCNName();
										articleColor[i] = prizeList.get(order).getArticleColor();
										articleNum[i] = prizeList.get(order).getArticleNum();
										bind[i] = prizeList.get(order).isBind();
										if (logger.isInfoEnabled()) logger.info("[仙灵] [刷新限时任务] [XianLingManager.refreshTimedTask] [" + player.getLogString() + "] [限时任务id:" + task.getTaskId() + "] [order:" + order + "] [奖励物品名:" + articleCNName[i] + "] [颜色：" + articleColor[i] + "] [个数:" + Arrays.toString(articleNum) + "] [是否绑定:" + bind[i] + "]");
									}
								}

							}
							xianlingData.setArticleCNName(articleCNName);
							xianlingData.setArticleColor(articleColor);
							xianlingData.setArticleNum(articleNum);
							xianlingData.setBind(bind);
							if (logger.isInfoEnabled()) logger.info("[仙灵] [刷新限时任务] [XianLingManager.refreshTimedTask] [" + player.getLogString() + "] [限时任务id:" + task.getTaskId() + "] [奖励物品名:" + Arrays.toString(articleCNName) + "] [颜色：" + Arrays.toString(articleColor) + "] [个数:" + Arrays.toString(articleNum) + "] [是否绑定:" + Arrays.toString(bind) + "]");
						} else {
							String[] articleCNName = new String[num];
							int[] articleColor = new int[num];
							int[] articleNum = new int[num];
							boolean[] bind = new boolean[num];
							for (int i = 0; i < prizeList.size(); i++) {
								articleCNName[i] = prizeList.get(i).getArticleCNName();
								articleColor[i] = prizeList.get(i).getArticleColor();
								articleNum[i] = prizeList.get(i).getArticleNum();
								bind[i] = prizeList.get(i).isBind();
								if (logger.isInfoEnabled()) logger.info("[仙灵] [刷新限时任务] [XianLingManager.refreshTimedTask] [" + player.getLogString() + "] [限时任务id:" + task.getTaskId() + "] [order:" + order + "] [奖励物品名:" + articleCNName[i] + "] [颜色：" + articleColor[i] + "] [个数:" + Arrays.toString(articleNum) + "] [是否绑定:" + bind[i] + "]");
							}
							xianlingData.setArticleCNName(articleCNName);
							xianlingData.setArticleColor(articleColor);
							xianlingData.setArticleNum(articleNum);
							xianlingData.setBind(bind);
							if (logger.isInfoEnabled()) logger.info("[仙灵] [刷新限时任务] [XianLingManager.refreshTimedTask] [" + player.getLogString() + "] [限时任务id:" + task.getTaskId() + "] [奖励物品名:" + Arrays.toString(articleCNName) + "] [颜色：" + Arrays.toString(articleColor) + "] [个数:" + Arrays.toString(articleNum) + "] [是否绑定:" + Arrays.toString(bind) + "]");
						}
					}
					if (logger.isInfoEnabled()) logger.info("[仙灵] [刷新限时任务] [XianLingManager.refreshTimedTask] [" + player.getLogString() + "] [限时任务id:" + task.getTaskId() + "]");
					return send_TIMEDTASK_RES(task, xianlingData, player);
				} else {
					if (logger.isDebugEnabled()) logger.debug("[仙灵] [刷新限时任务] [XianLingManager.refreshTimedTask] [" + player.getLogString() + "] [没有该类型任务:" + type + "]");
				}
			} else {
				if (logger.isErrorEnabled()) logger.error("[仙灵] [获取玩家仙灵数据失败] [XianLingManager.refreshTimedTask]" + player.getLogString());
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [刷新限时任务] [XianLingManager.refreshTimedTask]" + player.getLogString(), e);
		}
		return null;
	}

	/**
	 * 领取限时任务奖励
	 * @param req
	 * @param player
	 */
	public XL_TIMEDTASK_PRIZE_RES handle_XL_TIMEDTASK_PRIZE_REQ(Player player) {
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			TimedTask task = timedTaskMap.get(xianlingData.getTaskId());
			if (task != null) {
				switch (xianlingData.getTaskState()) {
				case (byte) 2:
					if (xianlingData.isTakePrize()) {
						player.sendError(Translate.已经领取过奖励);
					} else {
						List<Player> players = new ArrayList<Player>();
						players.add(player);
						ActivityProp[] prize = new ActivityProp[xianlingData.getArticleCNName().length];
						for (int i = 0; i < xianlingData.getArticleCNName().length; i++) {
							prize[i] = new ActivityProp(xianlingData.getArticleCNName()[i], xianlingData.getArticleColor()[i], xianlingData.getArticleNum()[i], xianlingData.getBind()[i]);
						}
						ActivityManager.getInstance().sendMailForActivity(players, prize, Translate.限时任务标题, Translate.限时任务内容, "领取仙灵大会限时任务奖励");
						player.sendError(Translate.领奖成功);
						addScore(player, taskScores[task.getType()]);
						xianlingData.setTakePrize(true);
						XL_TIMEDTASK_PRIZE_RES res = new XL_TIMEDTASK_PRIZE_RES(GameMessageFactory.nextSequnceNum(), task.getTaskId(), xianlingData.getTaskState(), xianlingData.isTakePrize(), xianlingData.getScore());
						return res;
					}
					break;
				default:
					player.sendError(Translate.没有可领取的奖励);
					break;
				}
			}
		} else {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [领取限时任务奖励] [xianlingData=null] [XianLingManager.handle_XL_TIMEDTASK_PRIZE_REQ]" + player.getLogString());
		}
		return null;
	}

	/**
	 * 打开激活buff面板
	 * @param player
	 */
	public XL_OPEN_MEETMONSTER_BUFF_RES handle_OPEN_MEETMONSTER_BUFF_REQ(Player player) {
		if (!isOpen) {
			player.sendError(Translate.不在时间段);
			return null;
		}
		String icons[] = new String[meetMonsterRateMap.size()];
		String buffNames[] = new String[meetMonsterRateMap.size()];
		String icon = "";
		String buffName = "";
		// long leftTime = 0;
		try {
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [请求打开激活buff面板] [handle_OPEN_MEETMONSTER_BUFF_REQ]" + player.getLogString());
			for (int i = 0; i < meetMonsterRateMap.size(); i++) {
				int aa = i + 1;
				if (meetMonsterRateMap.get(aa) != null) {
					icons[i] = meetMonsterRateMap.get(aa).getIcon();
					buffNames[i] = meetMonsterRateMap.get(aa).getDes();
					if (meetMonsterRateMap.get(aa).getBuffId() == player.getMeetMonsterBuffId()) {
						icon = meetMonsterRateMap.get(aa).getIcon();
						buffName = meetMonsterRateMap.get(aa).getDes();
						// Buff b = player.getBuffByName("遇怪加成");
						// if (b != null) {
						// leftTime = b.getInvalidTime() - System.currentTimeMillis();
						// }
					}
				}
			}
			XL_OPEN_MEETMONSTER_BUFF_RES res = new XL_OPEN_MEETMONSTER_BUFF_RES(GameMessageFactory.nextSequnceNum(), icons, buffNames, icon, buffName, REFRESH_BUFF_COST);
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [发送打开激活buff面板] [handle_OPEN_MEETMONSTER_BUFF_REQ] [" + player.getLogString() + "] [buffName:" + buffName + "]");
			return res;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [打开激活buff面板] [异常] [XianLingManager.handle_OPEN_MEETMONSTER_BUFF_REQ]" + player.getLogString(), e);
		}
		return null;
	}

	public void handle_EXIT_CHALLENGE_REQ(Player player) {
		XianLingChallenge xc = XianLingChallengeManager.getInstance().findXLChallenge(player);
		if (xc != null) {
			if (xc.getResult() != 0) {
				xc.setResult(xc.USETRANSPROP);
			} else {
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				mw.setDescriptionInUUB(Translate.translateString(Translate.退出副本二次提示, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(XianLingManager.REFRESH_BUFF_COST) } }));
				if (logger.isDebugEnabled()) logger.debug("[仙灵] [退出副本二次提示] [XianLingManager.handle_EXIT_CHALLENGE_REQ]" + player.getLogString());
				Option_Confirm_ExitBuZhuo o1 = new Option_Confirm_ExitBuZhuo(xc);
				o1.setText(Translate.确定);

				Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
				o2.setText(Translate.取消);
				mw.setOptions(new Option[] { o1, o2 });
				player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
			}
		} else {
			Game game = player.getCurrentGame();
			if (game != null) {
				// 之前有莫名被传送到出生点的问题，在这里加了log并修改为传送到复活点
				// game.transferGame(player, new TransportData(0, 0, 0, 0, player.getHomeMapName(), player.getHomeX(), player.getHomeY()));
				String mapName = TransportData.getMainCityMap(player.getCountry());
				int x = player.getResurrectionX();
				int y = player.getResurrectionY();
				game.transferGame(player, new TransportData(0, 0, 0, 0, mapName, x, y));
				if (logger.isErrorEnabled()) logger.error("[仙灵] [未获取到玩家挑战数据] [XianLingChallenge=null] [XianLingManager.handle_EXIT_CHALLENGE_REQ] [" + player.getLogString() + "]");
			} else {
				if (logger.isErrorEnabled()) logger.error("[仙灵] [退出挑战异常] [XianLingChallenge=null] [XianLingManager.handle_EXIT_CHALLENGE_REQ] [" + player.getLogString() + "]");
			}
		}
	}

	/**
	 * 刷新激活buff
	 * @param player
	 */
	public void handle_ACT_MEETMONSTER_BUFF_REQ(Player player) {
		int meetMonsterBuffId = player.getMeetMonsterBuffId();
		if (meetMonsterBuffId > 0) {
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setDescriptionInUUB(Translate.translateString(Translate.激活buff二次提示2, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(XianLingManager.REFRESH_BUFF_COST) } }));
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [激活buff二次提示] [XianLingManager.handle_ACT_MEETMONSTER_BUFF_REQ]" + player.getLogString());
			Option_Confirm_ActBuff o1 = new Option_Confirm_ActBuff();
			o1.setText(Translate.确定);

			Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
			o2.setText(Translate.取消);
			mw.setOptions(new Option[] { o1, o2 });
			player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
		} else {
			WindowManager windowManager = WindowManager.getInstance();
			MenuWindow mw = windowManager.createTempMenuWindow(600);
			mw.setDescriptionInUUB(Translate.translateString(Translate.激活buff二次提示1, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(XianLingManager.REFRESH_BUFF_COST) } }));
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [激活buff二次提示] [XianLingManager.handle_ACT_MEETMONSTER_BUFF_REQ]" + player.getLogString());
			Option_Confirm_ActBuff o1 = new Option_Confirm_ActBuff();
			o1.setText(Translate.确定);

			Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
			o2.setText(Translate.取消);
			mw.setOptions(new Option[] { o1, o2 });
			player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
		}
	}

	/**
	 * 打开积分卡界面
	 * @param player
	 */
	public XL_OPEN_SCORE_BUFF_RES handle_OPEN_SCORE_BUFF_REQ(Player player) {
		if (!isOpen) {
			player.sendError(Translate.不在时间段);
			return null;
		}
		long tempId = 0;
		long seniorempId = 0;
		int num = 0;
		int seniorNum = 0;
		Article a = ArticleManager.getInstance().getArticleByCNname(积分卡);
		if (a != null) {
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createTempEntity(a, false, ArticleEntityManager.仙灵临时物品, null, a.getColorType());
				if (ae != null) {
					tempId = ae.getId();
				} else {
					if (logger.isWarnEnabled()) logger.warn("[仙灵] [获取积分卡失败] [handle_OPEN_SCORE_BUFF_REQ]" + player.getLogString());
					return null;
				}
				num = player.countArticleInKnapsacksByName(a.getName());
			} catch (Exception e) {
				if (logger.isErrorEnabled()) logger.error("[仙灵] [创建积分卡异常] [handle_OPEN_SCORE_BUFF_REQ]" + player.getLogString(), e);
			}
		}
		Article a2 = ArticleManager.getInstance().getArticleByCNname(高级积分卡);
		if (a2 != null) {
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createTempEntity(a2, false, ArticleEntityManager.仙灵临时物品, null, a.getColorType());
				if (ae != null) {
					seniorempId = ae.getId();
				} else {
					if (logger.isWarnEnabled()) logger.warn("[仙灵] [获取高级积分卡失败] [handle_OPEN_SCORE_BUFF_REQ]" + player.getLogString());
					return null;
				}
				seniorNum = player.countArticleInKnapsacksByName(a2.getName());
			} catch (Exception e) {
				if (logger.isErrorEnabled()) logger.error("[仙灵] [创建高级积分卡异常] [handle_OPEN_SCORE_BUFF_REQ]" + player.getLogString(), e);
			}
		}
		XL_OPEN_SCORE_BUFF_RES res = new XL_OPEN_SCORE_BUFF_RES(GameMessageFactory.nextSequnceNum(), tempId, num, seniorempId, seniorNum);
		if (logger.isDebugEnabled()) logger.debug("[仙灵] [打开积分卡界面] [handle_OPEN_SCORE_BUFF_REQ]" + player.getLogString());
		return res;
	}

	/**
	 * 使用积分卡
	 * @param req
	 * @param player
	 */
	public void handle_USE_SCORE_CARD_REQ(XL_USE_SCORE_CARD_REQ req, Player player) {
		Byte type = req.getCardType();
		Article a = null;
		if (type == 0) {
			a = ArticleManager.getInstance().getArticleByCNname(积分卡);
		} else if (type == 1) {
			a = ArticleManager.getInstance().getArticleByCNname(高级积分卡);
		}
		if (a != null) {
			int num = player.countArticleInKnapsacksByName(a.getName());
			if (num <= 0) {
				player.sendError(Translate.物品个数不足);
				return;
			}
			// player.removeArticle(a.getName(), "仙灵使用积分卡");
			ArrayList<Buff> buffs = player.getBuffs();
			int buffLevel = -1;
			int scoreBuff = player.getScoreBuff();
			if (scoreBuff > 0) {
				for (Buff buff : buffs) {
					// 积分卡buff
					if (buff.getTemplate() instanceof BuffTemplate_XianlingScore) {
						buffLevel = buff.getLevel();
					}
				}
			}

			if (type == 0) {
				if (buffLevel == 0) {
					player.sendError(Translate.已有该BUFF);
					return;
				} else if (buffLevel == 1) {
					player.sendError(Translate.已有更高级BUFF);
					return;
				}
			} else if (type == 1) {
				if (buffLevel == 1) {
					player.sendError(Translate.已有该BUFF);
					return;
				}
			}
			if (buffLevel != -1) {

				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				mw.setDescriptionInUUB(Translate.使用积分卡二次提示);
				if (logger.isDebugEnabled()) logger.debug("[仙灵] [使用积分卡二次提示] [XianLingManager.handle_USE_SCORE_CARD_REQ]" + player.getLogString());
				Option_Confirm_UseJinfenCard o1 = new Option_Confirm_UseJinfenCard(type, a);
				o1.setText(Translate.确定);

				Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
				o2.setText(Translate.取消);
				mw.setOptions(new Option[] { o1, o2 });
				player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
				return;
			} else {
				send_USE_SCORE_CARD_RES(player, a, type);
			}
		}

	}

	public void send_USE_SCORE_CARD_RES(Player player, Article a, int type) {
		try {
			XianLingManager xm = XianLingManager.instance;
			Knapsack kp = player.getKnapsack_common();
			if (kp != null) {
				int count = kp.countArticle(a.getName());
				if (count >= 1) {
					ArticleEntity ae = kp.remove(kp.indexOf(a.getName()), "仙灵使用积分卡", true);
					if (ae != null) {
						// 统计
						ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.ARTICLES, 1, "仙灵使用积分卡", null);
						try {
							StatClientService statClientService = StatClientService.getInstance();
							NpcinfoFlow npcinfoFlow = new NpcinfoFlow();
							npcinfoFlow.setAward("0");
							CareerManager cm = CareerManager.getInstance();
							npcinfoFlow.setCareer(cm.getCareer(player.getCareer()).getName());
							npcinfoFlow.setColumn1(player.getName());
							npcinfoFlow.setColumn2("");
							npcinfoFlow.setCreateDate(new Date().getTime());
							npcinfoFlow.setFenQu(GameConstants.getInstance().getServerName());
							npcinfoFlow.setGameLevel(player.getLevel());
							npcinfoFlow.setGetDaoJu(0);
							npcinfoFlow.setGetWuPin(0);
							npcinfoFlow.setGetYOuXiBi(0);
							Connection conn = player.getConn();
							if (conn != null) {
								NEW_USER_ENTER_SERVER_REQ mess = (NEW_USER_ENTER_SERVER_REQ) conn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
								if (mess != null) {
									npcinfoFlow.setJixing(mess.getPhoneType());
								}
							}
							npcinfoFlow.setNpcName("仙灵大会");
							npcinfoFlow.setTaskType("使用积分卡");
							npcinfoFlow.setUserName(player.getUsername());
							statClientService.sendNpcinfoFlow("", npcinfoFlow);
						} catch (Exception e) {
							if (logger.isErrorEnabled()) logger.warn("[仙灵] [使用积分卡统计]", e);
						}
					}
					// 加buff
					FairylandTreasureManager.getInstance().fireBuff(player, "积分加倍_@@", type, xm.SCORE_BUFF_LASTING);
					player.sendError(Translate.使用成功);
					XL_USE_SCORE_CARD_RES res = new XL_USE_SCORE_CARD_RES(GameMessageFactory.nextSequnceNum(), ae.getId(), xm.SCORE_BUFF_LASTING);
					if (xm.logger.isWarnEnabled()) xm.logger.warn("[仙灵] [使用积分卡] [Option_Confirm_UseJinfenCard] [" + player.getLogString() + "] [type:" + type + "]");
					player.addMessageToRightBag(res);
				}
			}

		} catch (Exception e) {
			if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [使用积分卡] [异常] [Option_Confirm_UseJinfenCard]" + player.getLogString(), e);
			e.printStackTrace();
		}

	}

	/**
	 * 打开购买真气面板
	 * @param player
	 */
	public XL_BUTENERGY_RES handle_BUTENERGY_REQ(Player player) {
		if (!isOpen) {
			player.sendError(Translate.不在时间段);
			return null;
		}
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			int needSilver = 初始购买真气银两;
			if (xianlingData.getBugEntityTimes() > 0) {
				needSilver += xianlingData.getBugEntityTimes() * 购买真气增长银两;
			}
			XL_BUTENERGY_RES res = new XL_BUTENERGY_RES(GameMessageFactory.nextSequnceNum(), Translate.购买真气描述, needSilver, 单次购买真气点数);
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [打开购买真气] [handle_BUTENERGY_REQ]" + player.getLogString());
			return res;
		} else {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [打开购买真气面板] [xianlingData=null] [XianLingManager.handle_BUTENERGY_REQ]" + player.getLogString());
		}
		return null;
	}

	/**
	 * 确定购买真气
	 * @param player
	 */
	public void handle_BUTENERGY_SURE_REQ(Player player) {
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			int needSilver = 初始购买真气银两;
			if (xianlingData.getBugEntityTimes() > 0) {
				needSilver += xianlingData.getBugEntityTimes() * 购买真气增长银两;
			}
			if (player.getSilver() < needSilver) {
				player.sendError(Translate.银子不足);
				return;
			} else {
				WindowManager windowManager = WindowManager.getInstance();
				MenuWindow mw = windowManager.createTempMenuWindow(600);
				mw.setDescriptionInUUB(Translate.translateString(Translate.购买真气二次提示, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(needSilver) } }));
				if (logger.isDebugEnabled()) logger.debug("[仙灵] [购买真气二次提示] [XianLingManager.handle_XL_TIMEDTASK_REQ]" + player.getLogString());
				Option_Confirm_BuyEnergy o1 = new Option_Confirm_BuyEnergy(xianlingData, needSilver);
				o1.setText(Translate.确定);

				Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
				o2.setText(Translate.取消);
				mw.setOptions(new Option[] { o1, o2 });
				player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
				return;
			}
		} else {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [确定购买真气] [xianlingData=null] [XianLingManager.handle_BUTENERGY_SURE_REQ]" + player.getLogString());
		}
	}

	/**
	 * 打开领取积分奖励界面
	 * @param player
	 */
	public XL_SCORE_PRIZE_RES handle_SCORE_PRIZE_REQ(Player player) {
		if (!isOpen) {
			player.sendError(Translate.不在时间段);
			return null;
		}
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			int[] needScore = new int[scorePrizes.size()];
			TempPrize[] tp = new TempPrize[scorePrizes.size()];
			byte[] state = new byte[scorePrizes.size()];

			for (int i = 0; i < scorePrizes.size(); i++) {
				ScorePrize sp = scorePrizes.get(i);
				if (sp != null) {
					needScore[i] = sp.getNeedScore();
					ActivityProp[] aps = sp.getPrizeProps();
					long[] tempIds = new long[aps.length];
					String[] articleNames = new String[aps.length];
					int[] nums = new int[aps.length];
					boolean[] bind = new boolean[aps.length];
					if (aps != null && aps.length > 0) {
						for (int j = 0; j < aps.length; j++) {
							ActivityProp ap = aps[j];
							Article a = ArticleManager.getInstance().getArticleByCNname(ap.getArticleCNName());
							if (a != null) {
								ArticleEntity ae;
								try {
									ae = ArticleEntityManager.getInstance().getEntity(sp.getTempIds()[j]);
									if (ae != null) {
										tempIds[j] = ae.getId();
										articleNames[j] = ae.getArticleName();
										nums[j] = ap.getArticleNum();
										bind[j] = ap.isBind();

									}
									tp[i] = new TempPrize(tempIds, articleNames, nums, bind);
								} catch (Exception e) {
									if (logger.isDebugEnabled()) logger.debug("[仙灵] [创建临到道具:" + a.getName_stat() + "] [异常] [handle_SCORE_PRIZE_REQ] [" + player.getLogString() + "] [score:" + xianlingData.getScore() + "] [state:" + Arrays.toString(state) + "]", e);
								}
							}
						}
					}
				}
				if (xianlingData.getScore() >= sp.getNeedScore()) {
					state[i] = (byte) 1;
				}
				Map<Integer, Boolean> takeSPMap = xianlingData.getTakeScorePrizeMap();
				if (takeSPMap != null && takeSPMap.containsKey(sp.getNeedScore())) {
					if (takeSPMap.get(sp.getNeedScore())) {
						state[i] = (byte) 2;
					}
				}
			}
			XL_SCORE_PRIZE_RES res = new XL_SCORE_PRIZE_RES(GameMessageFactory.nextSequnceNum(), needScore, tp, state);
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [打开领取积分奖励界面] [handle_SCORE_PRIZE_REQ] [" + player.getLogString() + "] [score:" + xianlingData.getScore() + "] [state:" + Arrays.toString(state) + "]");
			return res;
		} else {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [打开领取积分奖励界面] [xianlingData=null] [XianLingManager.handle_SCORE_PRIZE_REQ]" + player.getLogString());
		}
		return null;
	}

	/**
	 * 领取积分奖励
	 * @param message
	 * @param player
	 */
	public XL_GET_SCORE_PRIZE_RES handle_GET_SCORE_PRIZE_REQ(XL_GET_SCORE_PRIZE_REQ message, Player player) {
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			int score = message.getScore();
			if (score > xianlingData.getScore()) {
				player.sendError(Translate.积分不够);
				return null;
			}

			Map<Integer, Boolean> takeSPMap = xianlingData.getTakeScorePrizeMap();
			if (takeSPMap != null && takeSPMap.containsKey(score)) {
				if (takeSPMap.get(score)) {
					player.sendError(Translate.已领取奖励);
					return null;
				}
			}

			for (ScorePrize sp : scorePrizes) {
				if (sp.getNeedScore() == score) {
					List<Player> players = new ArrayList<Player>();
					players.add(player);
					ActivityProp[] prize = sp.getPrizeProps();
					String mailContent = Translate.translateString(Translate.积分奖励内容, new String[][] { { Translate.STRING_1, score + "" } });
					ActivityManager.getInstance().sendMailForActivity(players, prize, Translate.积分奖励标题, mailContent, "领取仙灵大会积分奖励");
					player.sendError(Translate.已领取积分奖励);
					if (takeSPMap == null) {
						takeSPMap = new HashMap<Integer, Boolean>();
					}
					takeSPMap.put(score, true);
					xianlingData.setTakeScorePrizeMap(takeSPMap);
				}
			}
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [领取仙灵大会积分奖励] [" + player.getLogString() + "] [积分档次:" + score + "] [玩家积分:" + xianlingData.getScore() + "]");
			XL_GET_SCORE_PRIZE_RES res = new XL_GET_SCORE_PRIZE_RES(message.getSequnceNum(), score, true);
			return res;
		} else {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [领取积分奖励] [xianlingData=null] [XianLingManager.handle_GET_SCORE_PRIZE_REQ]" + player.getLogString());
		}
		return null;
	}

	public XL_CROSSBILLBOARD_RES handle_CROSSBILLBOARD_REQ(Player player) {
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			try {
				// 排行奖励展示
				String[] rankDes = new String[boardPrizes.size()];
				rankDes[0] = Translate.第 + 1 + Translate.名;
				TempPrize[] tp = new TempPrize[boardPrizes.size()];
				for (int i = 0; i < boardPrizes.size(); i++) {
					BoardPrize bp = boardPrizes.get(i);
					if (bp != null) {
						if (i > 0) {
							rankDes[i] = Translate.第 + bp.getStartIndex() + "~" + bp.getEndIndex() + Translate.名;
						}
						ActivityProp[] aps = bp.getPrizeProps();
						long[] tempIds = new long[aps.length];
						String[] articleNames = new String[aps.length];
						int[] nums = new int[aps.length];
						boolean[] bind = new boolean[aps.length];
						if (aps != null && aps.length > 0) {
							for (int j = 0; j < aps.length; j++) {
								ActivityProp ap = aps[j];
								Article a = ArticleManager.getInstance().getArticleByCNname(ap.getArticleCNName());
								if (a != null) {
									ArticleEntity ae;
									try {
										ae = ArticleEntityManager.getInstance().createTempEntity(a, ap.isBind(), ArticleEntityManager.仙灵临时物品, null, ap.getArticleColor());
										if (ae != null) {
											tempIds[j] = ae.getId();
											articleNames[j] = ae.getArticleName();
											nums[j] = ap.getArticleNum();
											bind[j] = ap.isBind();
										}
										tp[i] = new TempPrize(tempIds, articleNames, nums, bind);
									} catch (Exception e) {
										if (logger.isDebugEnabled()) logger.debug("[仙灵] [打开跨服排行榜] [XianLingManager.handle_CROSSBILLBOARD_REQ] [创建奖励异常] [" + player.getLogString() + "]", e);
									}
								}
							}
						}
					}

				}
				// 排行展示
				XianLingBillBoardData selfData = null;
				// 取跨服排行
				long endTime = getCurrentActivity() == null ? 0 : getCurrentActivity().getEndTime();
				sendUpdateInfo(player, null, getActivitykey(), endTime);
				updateSelfServerCrossRank();
				// updateBoardNotSaveVars(csBillboardList);
				// updateCrossServerRank();
				List<XianLingBillBoardData> sendBillboardList = new ArrayList<XianLingBillBoardData>();
				if (csBillboardList.size() > 跨服排行数量) {
					sendBillboardList = csBillboardList.subList(0, 跨服排行数量);
				} else {
					sendBillboardList = csBillboardList;
				}
				// 跨服排行榜数据
				for (int i = 0; i < csBillboardList.size(); i++) {
					if (csBillboardList.get(i).getId() == player.getId()) {
						selfData = csBillboardList.get(i);
					}
				}
				if (selfData == null) {
					List<XianLingBillBoardData> billboardList = map2OrderList(billboardMap, getActivitykey());
					for (int i = 0; i < billboardList.size(); i++) {
						if (billboardList.get(i).getId() == player.getId()) {
							selfData = billboardList.get(i);
						}
					}
				}
				if (selfData == null) {
					selfData = new XianLingBillBoardData(player);
					selfData.setPlayerName(player.getName());
					selfData.setServerName(GameConstants.getInstance().getServerName());
					selfData.setCrossServerRank(-1);
					selfData.setScore(0);
				}
				XianLing xl = getCurrentActivity();
				long leftTime = 0l;
				if (xl != null) {
					leftTime = xl.getEndTime() - System.currentTimeMillis();
				}
				XL_CROSSBILLBOARD_RES res = new XL_CROSSBILLBOARD_RES(GameMessageFactory.nextSequnceNum(), rankDes, tp, sendBillboardList.toArray(new XianLingBillBoardData[0]), selfData.getCrossServerRank(), selfData, leftTime);
				if (logger.isDebugEnabled()) logger.debug("[仙灵] [打开跨服排行榜] [XianLingManager.handle_CROSSBILLBOARD_REQ] [" + player.getLogString() + "] [数量:" + csBillboardList.size() + "] [crossRank:" + selfData.getCrossServerRank() + "]");
				return res;
			} catch (Exception e) {
				if (logger.isErrorEnabled()) logger.error("[仙灵] [打开跨服排行榜] [XianLingManager.handle_CROSSBILLBOARD_REQ]" + player.getLogString(), e);
			}
		} else {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [跨服排行榜] [xianlingData=null] [XianLingManager.handle_CROSSBILLBOARD_REQ]" + player.getLogString());
		}
		return null;
	}

	/**
	 * 本服排行
	 * @param player
	 */
	public XL_BILLBOARD_RES handle_BILLBOARD_REQ(Player player) {
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			try {
				// 排行展示
				int rank = -1;
				XianLingBillBoardData selfData = null;
				updateSelfServerCrossRank();
				List<XianLingBillBoardData> billboardList = map2OrderList(billboardMap, getActivitykey());
				for (int i = 0; i < billboardList.size(); i++) {
					if (billboardList.get(i).getId() == player.getId()) {
						rank = i;
						selfData = billboardList.get(i);
					}
				}
				if (selfData == null) {
					selfData = new XianLingBillBoardData(player);
					selfData.setPlayerName(player.getName());
					selfData.setServerName(GameConstants.getInstance().getServerName());
					selfData.setCrossServerRank(-1);
					selfData.setScore(0);
				}
				// selfData.setCrossServerRank(updateCrossServerBillBoard(billboardList, player));
				List<XianLingBillBoardData> sendList = new ArrayList<XianLingBillBoardData>();
				if (billboardList.size() > 本服排行数量) {
					sendList = billboardList.subList(0, 本服排行数量);
				} else {
					sendList = billboardList;
				}
				for (XianLingBillBoardData bd : sendList) {
					bd.setNotSaveVars(getActivitykey());
				}
				XianLing xl = getCurrentActivity();
				long leftTime = 0l;
				if (xl != null) {
					leftTime = xl.getEndTime() - System.currentTimeMillis();
				}
				XL_BILLBOARD_RES res = new XL_BILLBOARD_RES(GameMessageFactory.nextSequnceNum(), sendList.toArray(new XianLingBillBoardData[0]), rank, selfData, leftTime);
				if (logger.isDebugEnabled()) logger.debug("[仙灵] [打开本服排行榜] [XianLingManager.handle_BILLBOARD_REQ] [" + player.getLogString() + "] [rank:" + rank + "]");
				return res;
			} catch (Exception e) {
				if (logger.isErrorEnabled()) logger.error("[仙灵] [本服排行榜] [XianLingManager.handle_BILLBOARD_REQ]" + player.getLogString(), e);
				e.printStackTrace();
			}
		} else {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [本服排行榜] [xianlingData=null] [XianLingManager.handle_BILLBOARD_REQ]" + player.getLogString());
		}
		return null;
	}

	public List<XianLingBillBoardData> map2OrderList(Map<Long, XianLingBillBoardData> billboardMap, int activityKey) {
		List<XianLingBillBoardData> billboardList = new ArrayList<XianLingBillBoardData>();
		for (XianLingBillBoardData bbdata : billboardMap.values()) {
			if (bbdata != null) {
				bbdata.setNotSaveVars(activityKey);
				bbdata.setCrossServerRank(bbdata.getCrossServerRank());
				Map<Integer, XLBillBoardData> xlDataMap = bbdata.getXlDataMap();
				if (xlDataMap != null && xlDataMap.containsKey(activityKey)) {
					billboardList.add(bbdata);
				}
			}
		}
		Collections.sort(billboardList, order);
		return billboardList;
	}

	/**
	 * 加积分
	 * @param player
	 * @param addScore
	 */
	public void addScore(Player player, int addScore) {
		if (!isOpen) {
			player.sendError(Translate.活动结束积分不计入排行);
			return;
		}
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			int oldValue = xianlingData.getScore();
			xianlingData.setScore(oldValue + addScore);
			if (logger.isWarnEnabled()) logger.warn("[仙灵大会] [积分增加] [" + player.getLogString() + "] [增加:" + addScore + "] [增加前:" + oldValue + "] [增加后:" + xianlingData.getScore() + "]");
			// 积分变化更新本地排行榜
			XianLingManager.instance.updateOneData(player, xianlingData.getScore(), System.currentTimeMillis(), GameConstants.getInstance().getServerName(), billboardMap, getActivitykey(), true);
		} else {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [加积分] [xianlingData=null] [XianLingManager.addScore]" + player.getLogString());
		}
	}

	public boolean inXianLingGame(Player player) {
		return XianLingChallengeManager.mapNames.contains(player.getCurrentGame().gi.name);
		// return XianLingChallengeManager.instance.findXLChallenge(player) != null;
	}

	/**
	 * 获取玩家仙灵数据
	 * @param player
	 * @return
	 */
	public PlayerXianLingData getPlayerXianLingData(Player player) {
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			return xianlingData;
		}
		try {
			xianlingData = xianlingEm.find(player.getId());
			if (xianlingData == null) {
				xianlingData = new PlayerXianLingData(player.getId(), MAXENERGY, 0, 0, (byte) 0, false, 0, 0, 0, 0, new HashMap<Integer, Boolean>(), new HashMap<Integer, Long>());
				if (getCurrentActivity() != null) {
					xianlingData.setActivityKey(getCurrentActivity().getActivityId());
				}
				xianlingEm.notifyNewObject(xianlingData);
				if (logger.isWarnEnabled()) logger.warn(player.getLogString() + "[仙灵大会] [创建玩家数据]");
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [获取玩家仙灵数据异常] [XianLingManager.getPlayerXianLingData]" + player.getLogString(), e);
			e.printStackTrace();
		}
		return xianlingData;
	}

	public int getActivitykey() {
		XianLing xianling = getCurrentActivity();
		if (xianling != null) {
			return xianling.getActivityId();
		} else {
			long now = System.currentTimeMillis();
			long tempTime = 0;
			XianLing tempxl = null;
			for (XianLing xl : activities) {
				if (xl.getStartTime() < now) {
					if (tempTime == 0 || tempTime > (now - xl.getStartTime())) {
						tempTime = now - xl.getStartTime();
						tempxl = xl;
					}
				}
			}
			if (tempxl != null) return tempxl.getActivityId();
		}
		return 0;
	}

	/**
	 * 初始化本服排行榜
	 * @param activitykey
	 */
	public void getBillBoard(int activitykey) {
		if (activitykey == 0) {
			return;
		}
		QuerySelect<XianLingBillBoardData> querySelect = new QuerySelect<XianLingBillBoardData>();
		try {
			List<XianLingBillBoardData> billboardList = querySelect.selectAll(XianLingBillBoardData.class, "id = ?", new Object[] { activitykey }, null, boardEm);
			if (billboardList != null) {
				for (XianLingBillBoardData bbdata : billboardList) {
					billboardMap.put(bbdata.getId(), bbdata);
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [获取玩家仙灵数据异常] [XianLingManager.getBillBoard]" + "[activitykey:" + activitykey + "]", e);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void load() throws Exception {
		File file = new File(getFilePath());
		// file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		levelDatas = (List<XianLingLevelData>) ExcelDataLoadUtil.loadExcelDataAsList(file, 1, XianLingLevelData.class, logger);
		HSSFWorkbook hssfWorkbook = null;
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		hssfWorkbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = null;
		// 活动时间sheet
		List<BaseActivityInstance> tempList = new ArrayList<BaseActivityInstance>();
		sheet = hssfWorkbook.getSheetAt(0);
		if (sheet == null) return;
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = 2; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			int index = 0;
			int activityId = (int) row.getCell(index++).getNumericCellValue();
			String startTime = row.getCell(index++).getStringCellValue();
			String endTime = "2000-01-01 00:00:00"; // 随便给个值,只有创建对象的时候用到
			String platForms = row.getCell(index++).getStringCellValue();
			HSSFCell cell = row.getCell(index++);
			String openServerNames = "";
			if (cell != null) {
				openServerNames = cell.getStringCellValue();
			}
			cell = row.getCell(index++);
			String notOpenServers = "";
			if (cell != null) {
				notOpenServers = cell.getStringCellValue();
			}
			XianLing xl = new XianLing(startTime, endTime, platForms, openServerNames, notOpenServers);
			xl.setEndTime(xl.getStartTime() + lastingTime);
			xl.setActivityId(activityId);
			activities.add(xl);
			tempList.add(xl);
		}
		if (tempList.size() > 0) {
			AllActivityManager.instance.add2AllActMap(AllActivityManager.xianlingActivity, tempList);
		}

		// 关卡相关sheet
		sheet = hssfWorkbook.getSheetAt(2);
		if (sheet == null) return;
		HSSFRow row = sheet.getRow(2);
		if (row != null) {
			int index = 0;
			Integer[] scoreInt = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", Integer.class);
			if (scores.length != scoreInt.length) {
				throw new Exception("xianling.xls关卡相关sheet有错,scores.length:" + scoreInt.length);
			}
			for (int i = 0; i < scoreInt.length; i++) {
				scores[i] = scoreInt[i].intValue();
			}
			Integer[] rateInt = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", Integer.class);
			if (catchMonsterRates.length != rateInt.length) {
				throw new Exception("xianling.xls关卡相关sheet有错,catchMonsterRates.length:" + rateInt.length);
			}
			for (int i = 0; i < rateInt.length; i++) {
				catchMonsterRates[i] = rateInt[i].intValue();
			}

		}
		// 排行奖励sheet
		sheet = hssfWorkbook.getSheetAt(3);
		if (sheet == null) return;
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 2; i < rows; i++) {
			row = sheet.getRow(i);
			int index = 0;
			int startIndex = (int) row.getCell(index++).getNumericCellValue();
			int endIndex = (int) row.getCell(index++).getNumericCellValue();
			String[] articleCNName = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", String.class);
			Integer[] articleColor = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", Integer.class);
			Integer[] articleNum = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", Integer.class);
			String[] bind = StringTool.getCellValue(row.getCell(index++), String.class).split(";");
			if (articleCNName.length == articleColor.length && articleColor.length == articleNum.length && articleNum.length == bind.length) {
				ActivityProp[] ap = new ActivityProp[articleCNName.length];
				long[] tempIds = new long[ap.length];
				for (int j = 0; j < ap.length; j++) {
					ap[j] = new ActivityProp(articleCNName[j], articleColor[j], articleNum[j], Boolean.parseBoolean(bind[j]));
					Article a = ArticleManager.getInstance().getArticleByCNname(articleCNName[j]);
					if (a != null) {
						ArticleEntity ae = ArticleEntityManager.getInstance().createTempEntity(a, ap[j].isBind(), ArticleEntityManager.仙灵临时物品, null, a.getColorType());
						if (ae != null) {
							tempIds[j] = ae.getId();
						}
					}
				}
				boardPrizes.add(new BoardPrize(startIndex, endIndex, ap, tempIds));
			} else {
				throw new Exception("xianling.xls排行奖励sheet有错,奖励数组长度不相等:" + articleCNName.length + "/" + articleColor.length + "/" + articleNum.length + "/" + bind.length);
			}
		}

		// 积分奖励sheet
		sheet = hssfWorkbook.getSheetAt(4);
		if (sheet == null) return;
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 2; i < rows; i++) {
			row = sheet.getRow(i);
			int index = 0;
			int needScore = (int) row.getCell(index++).getNumericCellValue();
			String[] articleCNName = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", String.class);
			Integer[] articleColor = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", Integer.class);
			Integer[] articleNum = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", Integer.class);
			String[] bind = StringTool.getCellValue(row.getCell(index++), String.class).split(";");
			if (articleCNName.length == articleColor.length && articleColor.length == articleNum.length && articleNum.length == bind.length) {
				ActivityProp[] ap = new ActivityProp[articleCNName.length];
				long[] tempIds = new long[ap.length];
				for (int j = 0; j < ap.length; j++) {
					ap[j] = new ActivityProp(articleCNName[j], articleColor[j], articleNum[j], Boolean.parseBoolean(bind[j]));
					Article a = ArticleManager.getInstance().getArticleByCNname(articleCNName[j]);
					if (a != null) {
						ArticleEntity ae = ArticleEntityManager.getInstance().createTempEntity(a, ap[j].isBind(), ArticleEntityManager.仙灵临时物品, null, articleColor[j]);
						if (ae != null) {
							tempIds[j] = ae.getId();
						}
					}
				}
				scorePrizes.add(new ScorePrize(needScore, ap, tempIds));
			} else {
				throw new Exception("xianling.xls排行奖励sheet有错,奖励数组长度不相等:" + articleCNName.length + "/" + articleColor.length + "/" + articleNum.length + "/" + bind.length);
			}
		}

		// 限时任务sheet
		sheet = hssfWorkbook.getSheetAt(5);
		if (sheet == null) return;
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 2; i < rows; i++) {
			row = sheet.getRow(i);
			int index = 0;
			int taskId = (int) row.getCell(index++).getNumericCellValue();
			byte type = (byte) row.getCell(index++).getNumericCellValue();
			int monsterCategoryId = (int) row.getCell(index++).getNumericCellValue();
			index++;
			String monsterIcon = row.getCell(index++).getStringCellValue();
			String des = row.getCell(index++).getStringCellValue();
			TimedTask tt = new TimedTask(taskId, type, monsterCategoryId, monsterIcon, des);
			timedTaskMap.put(taskId, tt);
			if (!taskTypeMap.keySet().contains(type)) {
				taskTypeMap.put(type, new ArrayList<TimedTask>());
			}
			List<TimedTask> ttList = taskTypeMap.get(type);
			ttList.add(tt);
			taskTypeMap.put(type, ttList);
		}
		// 限时任务奖励sheet
		sheet = hssfWorkbook.getSheetAt(6);
		if (sheet == null) return;
		rows = sheet.getPhysicalNumberOfRows();
		byte type = 0;
		for (int i = 2; i < rows; i++) {
			row = sheet.getRow(i);
			int index = 0;
			HSSFCell cell = row.getCell(index++);
			if (cell != null) {
				type = (byte) cell.getNumericCellValue();
				if (!timedTaskPrizeMap.keySet().contains(type)) {
					timedTaskPrizeMap.put(type, new ArrayList<ActivityProp>());
				}
			}
			cell = row.getCell(index++);
			if (cell != null) {
				String articleCNName = cell.getStringCellValue();
				int articleColor = (int) row.getCell(index++).getNumericCellValue();
				int articleNum = (int) row.getCell(index++).getNumericCellValue();
				boolean bind = (boolean) row.getCell(index++).getBooleanCellValue();
				ActivityProp ap = new ActivityProp(articleCNName, articleColor, articleNum, bind);
				List<ActivityProp> apList = timedTaskPrizeMap.get(type);
				apList.add(ap);
				timedTaskPrizeMap.put(type, apList);
			}

		}

		// 限时任务相关sheet
		sheet = hssfWorkbook.getSheetAt(7);
		if (sheet == null) return;
		row = sheet.getRow(2);
		if (row != null) {
			int index = 0;
			Integer[] taskRateInt = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", Integer.class);
			if (taskRates.length != taskRateInt.length) {
				throw new Exception("xianling.xls限时任务相关sheet有错,taskRates.length:" + taskRateInt.length);
			}
			for (int i = 0; i < taskRateInt.length; i++) {
				taskRates[i] = taskRateInt[i].intValue();
			}
			Integer[] taskScoreInt = StringTool.string2Array(StringTool.getCellValue(row.getCell(index++), String.class), ";", Integer.class);
			if (taskScores.length != taskScoreInt.length) {
				throw new Exception("xianling.xls限时任务相关sheet有错,taskScores.length:" + taskScoreInt.length);
			}
			for (int i = 0; i < taskScoreInt.length; i++) {
				taskScores[i] = taskScoreInt[i].intValue();
			}

		}

		meetMonsterRateMap = (Map<Integer, MeetMonsterRate>) ExcelDataLoadUtil.loadExcelData(file, 8, MeetMonsterRate.class, logger);
		skillMap = (Map<Integer, XianLingSkill>) ExcelDataLoadUtil.loadExcelData(file, 9, XianLingSkill.class, logger);
		// 怪物大小sheet
		sheet = hssfWorkbook.getSheetAt(10);
		if (sheet == null) return;
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 2; i < rows; i++) {
			row = sheet.getRow(i);
			int index = 0;
			int monsterCategoryId = (int) row.getCell(index++).getNumericCellValue();
			index++;
			int scale = (int) row.getCell(index++).getNumericCellValue();
			int x = (int) row.getCell(index++).getNumericCellValue();
			int y = (int) row.getCell(index++).getNumericCellValue();
			MonsterData md = new MonsterData(monsterCategoryId, scale, x, y);
			scaleMap.put(monsterCategoryId, md);
		}
		// 翻译sheet
		sheet = hssfWorkbook.getSheetAt(11);
		if (sheet == null) return;
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 2; i < rows; i++) {
			row = sheet.getRow(i);
			int index = 0;
			String key = row.getCell(index++).getStringCellValue();
			String des = row.getCell(index++).getStringCellValue();
			translateMap.put(key, des);
		}
		if(translateMap.keySet().contains("提前多少分钟提醒")){
			提前多少分钟提醒 = Integer.valueOf(translateMap.get("提前多少分钟提醒"));
		}
		if(translateMap.keySet().contains("提醒间隔分钟")){
			提醒间隔分钟 = Integer.valueOf(translateMap.get("提醒间隔分钟"));
		}
	}

	public static void main(String[] args) {
		XianLingManager xlm = new XianLingManager();
		xlm.setFilePath("E:\\工作文档\\需求文档\\xianling.xls");
		try {
			xlm.load();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	/**
	 * 从数据库中读出，单服存到单服排行榜，跨服存到跨服排行榜，并且把XianLingBillBoardData内的xlDataMap的值赋值到不存库的字段
	 */
	public void initBillBoard() {
		QuerySelect<XianLingBillBoardData> querySelect = new QuerySelect<XianLingBillBoardData>();
		try {
			int key = getActivitykey();
			boolean 是仙灵单服 = true;
			if (key > 0) {
				List<XianLingBillBoardData> tempList = querySelect.selectAll(XianLingBillBoardData.class, null, new Object[] {}, null, boardEm);
				if (logger.isWarnEnabled()) logger.warn("[仙灵] [初始化排行榜数据] [排行榜条数:" + tempList.size() + "]");
				for (XianLingBillBoardData data : tempList) {
					if (是仙灵单服) {
						billboardMap.put(data.getId(), data);
					}
					if (isCrossServer()) {
						crossBillboardMap.put(data.getId(), data);
						if (logger.isWarnEnabled()) logger.warn("[仙灵] [初始化跨服排行榜数据] [排行榜条数:" + tempList.size() + "]");
					}
					Map<Integer, XLBillBoardData> xlDataMap = data.getXlDataMap();
					if (xlDataMap != null && xlDataMap.size() > 0) {
						if (xlDataMap.keySet().contains(key)) {
							XLBillBoardData xlData = xlDataMap.get(key);
							if (xlData != null) {
								data.setNotSaveVars(key);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵大会] [XianLingManager.initBillBoard]", e);
		}
		// if (billboardList.size() > 0) {
		// for (XianLingBillBoardData data : billboardList) {
		// data.getXlDataMap();
		// }
		// }
	}

	/**
	 * 更新XianLingBillBoardData不存库的变量
	 * @param csBillboardList
	 */
	public void updateBoardNotSaveVars(List<XianLingBillBoardData> csBillboardList) {
		if (csBillboardList.size() > 0) {
			for (XianLingBillBoardData bbdata : csBillboardList) {
				bbdata.setNotSaveVars(getActivitykey());
			}
		}
	}

	public void playerEnter(Player player) {
		try {
			PlayerXianLingData xianLingData = player.getXianlingData();
			if (xianLingData != null) {
				if (xianLingData.getActivityKey() == 0) {
					resetPlayerXianLingData(player);
				} else if (getCurrentActivity() != null && getCurrentActivity().getActivityId() != xianLingData.getActivityKey()) {
					resetPlayerXianLingData(player);
				}

			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.error("[仙灵] [重置玩家数据异常] [XianLingManager.playerEnter]", e);
		}
	}

	public void playerLeave(Player player) {
		XianLingChallengeThread[] list = XianLingChallengeManager.instance.threads.toArray(new XianLingChallengeThread[XianLingChallengeManager.instance.threads.size()]);
		for (XianLingChallengeThread t : list) {
			List<XianLingChallenge> gameList = t.getGameList();
			if (gameList.size() > 0) {
				for (XianLingChallenge xc : gameList) {
					if (xc.getPlayerId() == this.getId()) {
						xc.回城(player);
						XianLingManager.logger.error("[" + player.getLogString() + "] [仙灵] [XianLingChallengeManager.destroy] [玩家下线踢出仙灵副本]");
					}
				}
			}
		}
	}

	public boolean isPlayerInGame(Player player) {
		if (player.getCurrentGame() != null) {
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [XianLingManager.isPlayerInGame] [" + player.getLogString() + "] [game=" + player.getCurrentGame().gi.name + "]");
			return XianLingChallengeManager.mapNames.contains(player.getCurrentGame().gi.name);
		} else {
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [XianLingManager.isPlayerInGame] [game=null]");
		}
		return false;
	}

	public boolean sendBoardPrize() {
		try {
			for (int i = 0; i < boardPrizes.size(); i++) {
				int startRank = boardPrizes.get(i).getStartIndex();
				int endRank = boardPrizes.get(i).getEndIndex();
				List<Player> players = new ArrayList<Player>();
				if (logger.isDebugEnabled()) logger.debug("[仙灵大会] [发送排行奖励--------1] [csBillboardList:" + csBillboardList.size() + "]");
				for (int j = 0; j < csBillboardList.size(); j++) {
					XianLingBillBoardData bbdata = csBillboardList.get(j);
					if (bbdata.getServerName().equals(GameConstants.getInstance().getServerName()) && bbdata.getScore() >= 入榜积分要求) {
						if (logger.isDebugEnabled()) logger.debug("[仙灵大会] [发送排行奖励----2] [bbdata:" + bbdata.getPlayerName() + "] [bbdata.getCrossServerRank:" + bbdata.getCrossServerRank() + "] [判断名次范围:" + (startRank - 1) + "~" + (endRank - 1) + "]");
						if (bbdata.getCrossServerRank() >= (startRank - 1) && bbdata.getCrossServerRank() <= (endRank - 1)) {
							try {
								Player p = PlayerManager.getInstance().getPlayer(bbdata.getId());
								players.add(p);
							} catch (Exception e) {
								if (logger.isErrorEnabled()) logger.error("[仙灵大会] [发送排行奖励异常]", e);
								e.printStackTrace();
							}
						}
					}
				}
				if (players.size() > 0) {
					ActivityManager.sendMailForActivity(players, boardPrizes.get(i).getPrizeProps(), Translate.排行奖励标题, Translate.排行奖励内容, "仙灵大会排行奖励");
					if (logger.isWarnEnabled()) logger.warn("[仙灵大会] [发送排行奖励]");
				}
			}
			if (logger.isWarnEnabled()) logger.warn("[仙灵大会] [发送排行奖励成功]");
			return true;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵大会] [发送排行奖励异常]", e);
		}
		if (logger.isWarnEnabled()) logger.warn("[仙灵大会] [发送排行奖励失败]");
		return false;
	}

	/**
	 * 玩家心跳中处理的
	 * @param player
	 */
	public void resumeEnergy(Player player) {
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			if (xianlingData != null && getCurrentActivity() != null) {
				long now = System.currentTimeMillis();
				if (now > xianlingData.getNextRefreshTime() && xianlingData.getTaskState() == (byte) 1) {
					xianlingData.setTaskState((byte) 3); // 限时任务超时失效
				}
				long nextResume = xianlingData.getNextResumeEnergy();
				if (nextResume != 0 && now > nextResume && xianlingData.getEnergy() < MAXENERGY) {
					long num = (now - nextResume) / 真气自动恢复间隔 + 1;
					xianlingData.setNextResumeEnergy(num * 真气自动恢复间隔 + xianlingData.getNextResumeEnergy());
					long a = num + xianlingData.getEnergy();
					if (a > MAXENERGY) {
						a = MAXENERGY;
					}
					xianlingData.setEnergy((int) a);
					if (logger.isWarnEnabled()) logger.warn("[仙灵大会] [真气自动恢复:" + a + "点] [" + player.getLogString() + "]");
					player.addMessageToRightBag(new XL_REFRESH_ENERGY_RES(GameMessageFactory.nextSequnceNum(), xianlingData.getEnergy(), xianlingData.getNextResumeEnergy()));
				}
			}
		}
	}

	private void resetData() {
		Player[] onlinePlayers = GamePlayerManager.getInstance().getOnlinePlayers();
		for (Player p : onlinePlayers) {
			resetPlayerXianLingData(p);
		}

	}

	public void resetPlayerXianLingData(Player p) {
		try {
			p.setScoreBuff(0);
			p.setMeetMonsterBuffId(0);
			ArrayList<Buff> buffs = p.getBuffs();
			ArrayList<Buff> removeList = new ArrayList<Buff>();
			for (Buff buff : buffs) {
				if (buff.getTemplate() instanceof BuffTemplate_XianlingScore || buff.getTemplate() instanceof BuffTemplate_MeetMonster) {
					removeList.add(buff);
				}
			}
			if (removeList.size() > 0) {
				for (Buff buff : removeList) {
					buff.setInvalidTime(0);
				}
			}
			p.setBuffs(buffs);
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [活动结束清玩家Buff] [" + p.getLogString() + "]");
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [活动结束清玩家buff异常] [" + p.getLogString() + "]", e);
			e.printStackTrace();
		}
		try {
			PlayerXianLingData xianlingData = p.getXianlingData();
			if (xianlingData != null) {
				xianlingData.setMaxLevel(0);
				xianlingData.setTaskId(0);
				xianlingData.setTaskState((byte) -1);
				xianlingData.setArticleCNName(new String[0]);
				xianlingData.setArticleColor(new int[0]);
				xianlingData.setArticleNum(new int[0]);
				xianlingData.setBind(new boolean[0]);
				xianlingData.setTakePrize(false);
				xianlingData.setNextRefreshTime(0);
				xianlingData.setScore(0);
				xianlingData.setBugEntityTimes(0);
				xianlingData.setNextResumeEnergy(0);
				xianlingData.setTakeScorePrizeMap(new HashMap<Integer, Boolean>());
				xianlingData.setSkillCDMap(new HashMap<Integer, Long>());
				xianlingData.setPuclicCDEnd(0);
				xianlingData.setEnergy(30);
				xianlingData.setLastCostEnergyTime(0);
				// xianlingData.setActivityKey(0);
				if (logger.isWarnEnabled()) logger.warn("[仙灵] [活动结束清玩家数据] [" + xianlingData.getId() + "]");

			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [活动结束清玩家数据异常]", e);
			e.printStackTrace();
		}

	}
	
	public void 发送滚屏(){
		long now = System.currentTimeMillis();
		if(activities.size()>0){
			活动开启时间 = 0L;
			for(XianLing xl:activities){
				if(xl != null){
					long startTime = xl.getStartTime();
					if(startTime > now ){
						活动开启时间  = startTime;
					}
				}
			}
		}
		if((活动开启时间-now) > 0l && ((活动开启时间-now) <= 提前多少分钟提醒*60000) && !已提示过){
			try{
				String des  = translateMap.get("滚屏提示");
				ChatMessageService cms = ChatMessageService.getInstance();
				ChatMessage msg = new ChatMessage();
				msg.setMessageText("<f color='0x14ff00'>"+des+"</f>");
				cms.sendRoolMessageToSystem(msg);
				上次提醒时间 = now;
				已提示过 = true;
				if (logger.isWarnEnabled()) logger.warn("[仙灵大会] [发送滚屏]]");
			}catch(Exception e){
				logger.error("[发送开始滚屏异常]",e);
			}
		}
		if(now - 上次提醒时间 > 提醒间隔分钟 * 60000){
			已提示过 = false;
		}
	}

	public void init() throws Exception{
		
		load();
		File file = new File(diskFile);
		disk = new DefaultDiskCache(file, null, "xianling", 100L * 365 * 24 * 3600 * 1000L);
		instance = this;
		try {
			xianlingEm = SimpleEntityManagerFactory.getSimpleEntityManager(PlayerXianLingData.class);
			boardEm = SimpleEntityManagerFactory.getSimpleEntityManager(XianLingBillBoardData.class);
			// corssBoardEm = SimpleEntityManagerFactory.getSimpleEntityManager(XianLingBillBoardData.class);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[加载仙灵大会em异常]", e);
		}

		int activityKey = getActivitykey();
		if (activityKey != 0) {
			// getBillBoard(activityKey);
			initBillBoard();
		}
		this.setName("仙灵");
		this.start();
		// 如果有尚未更新到跨服的数据，则更新
		noticeReadData();
		ServiceStartRecord.startLog(this);
	}

	/************* 处理跨服逻辑 **********************/
	public static final String[] serverIp = new String[] { "116.213.204.143", "8001", "12001" };
	public static final String addressHead = "http://";
	public static final String addressTail = "/acrossServer/acrossServerCommunication";
	public Map<Long, String> playerSessions = new Hashtable<Long, String>();
	public static final String crossServerName = "跨服1";
	public static boolean CSOpen = true;
	public static final long REFRESHTIME = 5 * 60000;
	public static long quickRefresh = 5 * 60000;// 最后几分钟提高刷新频率
	public static long quickRefreshTime = 30000;// 提高刷新频率后多久刷一次

	public long lastRefreshTime = 0;
	public static int activityKey; // 单服请求跨服排行榜的时候传给跨服的当前活动期数
	public static long endTime;
	private long sendPrizeTime;

	public void refreshCrossBillBoard() {
		crossBillboardList = map2OrderList(crossBillboardMap, activityKey);
		updateCrossServerRank();
		lastRefreshTime = System.currentTimeMillis();
		if (logger.isWarnEnabled()) logger.warn("[仙灵大会] [刷新跨服排行榜] [activityKey:" + activityKey + "]");
	}

	/**
	 * 每5分钟刷新一次，活动结束前最后5分钟，每1分钟刷新一次
	 * @return
	 */
	public boolean isRefreshTime() {
		if (lastRefreshTime == 0) {
			return true;
		}
		if (System.currentTimeMillis() - lastRefreshTime > REFRESHTIME) {
			return true;
		}
		if (endTime > System.currentTimeMillis()) {
			if ((endTime - System.currentTimeMillis() <= quickRefresh) && (System.currentTimeMillis() - lastRefreshTime > quickRefreshTime)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void run() {
		while (CSOpen) {
			发送滚屏();
			if (isCrossServer()) {
				if (isRefreshTime()) {
					refreshCrossBillBoard();
				}
			}
			try {
				XianLing activity = getCurrentActivity();
				if (activity != null) {
					isOpen = true;
					if (disk.get(getActivitykey()) == null) {
						// sendCrossServerActivityKey(activity.getActivityId());
						disk.put(getActivitykey(), 1);// 1-活动开启；2-活动结束；3-排行奖励已发放
						if (logger.isWarnEnabled()) logger.warn("[仙灵大会] [活动开启] [id:" + activity.getActivityId() + "]");
					}
				} else if (activity == null) {
					isOpen = false;
					if (getActivitykey() > 0 && disk.get(getActivitykey()) != null && (((Integer) disk.get(getActivitykey())) != 2 && ((Integer) disk.get(getActivitykey())) != 3)) {
						disk.put(getActivitykey(), 2);// 1-活动开启；2-活动结束；3-排行奖励已发放
						if (logger.isWarnEnabled()) logger.warn("[仙灵大会] [活动结束]");
						resetData();
						sendTimeEnd(getActivitykey(), GameConstants.getInstance().getServerName());
						sendPrizeTime = System.currentTimeMillis() + 15 * 60000;
					}
				}
				if (disk.get(getActivitykey()) != null && ((Integer) disk.get(getActivitykey())) == 2 && System.currentTimeMillis() >= sendPrizeTime) {
					sendUpdateInfo(null, null, getActivitykey(), 0);
					updateSelfServerCrossRank();
					boolean result = sendBoardPrize();
					csBillboardList.clear();
					if (result) {
						disk.put(getActivitykey(), 3);// 1-活动开启；2-活动结束；3-排行奖励已发放
					}
				}
			} catch (Exception e1) {
				if (logger.isErrorEnabled()) logger.error("[仙灵大会] [活动状态处理异常]", e1);
				e1.printStackTrace();
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isCrossServer() {
		if (TestServerConfigManager.isTestServer()) {
			return "更端测试C".equals(GameConstants.getInstance().getServerName());
		}
		return crossServerName.equals(GameConstants.getInstance().getServerName());
	}

	/**
	 * 接收单服推送的更新积分信息
	 * @param playerInfo
	 * @return
	 */
	public boolean recievePlayerInfo(String data, int activityKey, XianLingBillBoardData receiveData, String serverName) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> linkedHashMap = JsonUtil.objectFromJson(data, Map.class);
			Player player = (Player) convertToSpecialedType(linkedHashMap.get("player"), Player.class);
			if (player == null) {
				if (logger.isWarnEnabled()) logger.warn("[仙灵大会] [XianLingManager.recievePlayerInfo] [player == null] [更新信息失败]");
				return false;
			}

			if (!receiveData.getXlDataMap().containsKey(activityKey)) {
				receiveData.getXlDataMap().put(activityKey, new XLBillBoardData(activityKey, serverName, player.getName(), player.getUsername(), 0, System.currentTimeMillis()));
			}
			XLBillBoardData xlboardData = receiveData.getXlDataMap().get(activityKey);
			if (xlboardData != null) {
				// 积分变化更新跨服排行榜
				updateOneData(player, xlboardData.getScore(), xlboardData.getLastUpdateTime(), xlboardData.getServerName(), crossBillboardMap, activityKey, false);
			}
			return true;

		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵大会] [XianLingManager.recievePlayerInfo]", e);
		}
		return false;
	}

	public Map<String, Object> collectCrossBoard() {
		List<XianLingBillBoardData> bbList = new ArrayList<XianLingBillBoardData>();
		for (XianLingBillBoardData bbdata : crossBillboardList) {
			if (bbdata != null && bbdata.getScore() >= 入榜积分要求) {
				bbList.add(bbdata);
			}

		}
		try {
			Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
			// if (bbList.size() > 跨服排行数量) {
			// returnMap.put("boardData", bbList.subList(0, 跨服排行数量));
			// } else {
			returnMap.put("boardData", bbList);
			// }
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [取跨服数据] [bbList.size=" + bbList.size() + "]");
			return returnMap;

		} catch (Exception e) {
			if (logger.isDebugEnabled()) logger.debug("[仙灵] [取跨服数据异常] [bbList.size=" + bbList.size() + "]", e);
		}
		return null;
	}

	public String sendCrossServerActivityKey(int activityKey) {
		Map headers = new HashMap();
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		try {
			paramMap.put("op", "send_activity");
			paramMap.put("activityKey", "" + activityKey);
			putSignParam(paramMap);
			String param = getSignParam(paramMap);
			String[] ss = serverIp;
			if (TestServerConfigManager.isTestServer()) { // 如果是测试服传来的数据，则设置更端c为跨服
				ss = new String[] { "116.213.192.203", "8003", "12003" };
			}
			String ip = ss[0];
			String port = ss[1];
			String url = addressHead + ip + ":" + port + addressTail;
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), param.getBytes("utf-8"), headers, 60000, 60000);
			String result = new String(bytes, "UTF-8");
			if (logger.isInfoEnabled()) {
				logger.warn("[推送活动id] [跨服消息] [发送] [activityKey:" + activityKey + "]");
			}
			if (result != null && result.contains("success")) {
				return "success";
			}
			return result;
		} catch (Exception e) {
			logger.error("[推送活动id] [积分] [异常] [activityKey:" + activityKey + "]", e);
		}
		return "error";
	}

	public String sendTimeEnd(int activityKey, String serverName) {
		Map headers = new HashMap();
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		try {
			paramMap.put("op", "send_timeEnd");
			paramMap.put("activityKey", "" + activityKey);
			paramMap.put("serverName", serverName);
			putSignParam(paramMap);
			String param = getSignParam(paramMap);
			String[] ss = serverIp;
			if (TestServerConfigManager.isTestServer()) { // 如果是测试服传来的数据，则设置更端c为跨服
				ss = new String[] { "116.213.192.203", "8003", "12003" };
			}
			String ip = ss[0];
			String port = ss[1];
			String url = addressHead + ip + ":" + port + addressTail;
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), param.getBytes("utf-8"), headers, 60000, 60000);
			String result = new String(bytes, "UTF-8");
			if (logger.isInfoEnabled()) {
				logger.warn("[推送活动结束] [跨服消息] [发送] [activityKey:" + activityKey + "] [serverName:" + serverName + "]");
			}
			if (result != null && result.contains("finish")) {
				return "finish";
			}
			return result;
		} catch (Exception e) {
			logger.error("[推送活动结束] [异常] [activityKey:" + activityKey + "]", e);
		}
		return "error";
	}

	/**
	 * 给跨服发送信息(xlData=null的时候为请求跨服排行，xlData！=null的时候为推送玩家积分)
	 * @param player
	 * @param bbData
	 * @return
	 */
	public String sendUpdateInfo(Player player, XianLingBillBoardData bbData, int activityKey, long endTime) {
		long startTime = System.currentTimeMillis();
		Map headers = new HashMap();
		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		try {
			paramMap.put("op", "update_score");
			paramMap.put("servername", URLEncoder.encode(GameConstants.getInstance().getServerName(), "utf-8"));

			Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
			if (bbData != null) {
				returnMap.put("boardData", URLEncoder.encode(JsonUtil.jsonFromObject(bbData), "utf-8"));
			} else {
				returnMap.put("opType", "getCrossBoard");
			}
			returnMap.put("activityKey", activityKey);
			if (endTime > 0) {
				returnMap.put("endTime", endTime);
			}
			// Map<String, Object> linkedHashMap = JsonUtil.objectFromJson(data, Map.class);
			// Player player = (Player) convertToSpecialedType(linkedHashMap.get("player"), Player.class);
			if (player != null) {
				returnMap.put("player", player);
				// String session = startTime + "_" + player.getId();
				// paramMap.put("player_session", session);
			}
			String pData = JsonUtil.jsonFromObject(returnMap);
			paramMap.put("player_data", URLEncoder.encode(pData, "utf-8"));
			putSignParam(paramMap);
			String param = getSignParam(paramMap);

			String[] ss = serverIp;
			if (TestServerConfigManager.isTestServer()) { // 如果是测试服传来的数据，则设置更端c为跨服
				ss = new String[] { "116.213.192.203", "8003", "12003" };
			}
			String ip = ss[0];
			String port = ss[1];
			String url = addressHead + ip + ":" + port + addressTail;
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), param.getBytes("utf-8"), headers, 60000, 60000);
			String result = new String(bytes, "UTF-8");

			String rr = new String(bytes, "UTF-8");
//			if ("getCrossBoard".equals(returnMap.get("opType")) && rr != null && !"".equals(rr)) {
//				String sss = URLDecoder.decode(rr, "utf-8");
//				LinkedHashMap<String, Object> map1 = (LinkedHashMap<String, Object>) JsonUtil.objectFromJson(sss, Map.class);
//				if (map1.get("boardData") != null) {
//					csBillboardList = (ArrayList<XianLingBillBoardData>) AcrossServerManager.convertToTypeReferenceType(map1.get("boardData"), new TypeReference<ArrayList<XianLingBillBoardData>>() {
//					});
//					if (logger.isInfoEnabled()) {
//						logger.info("[收到跨服消息] [耗时:" + (System.currentTimeMillis() - startTime) + "] [pData:" + pData.length() + "] [pDatabyte:" + pData.getBytes().length + "]");
//					}
//
//				}
//			}
			// if (player != null) {
			// if (logger.isInfoEnabled()) {
			// logger.warn("[跨服消息] [发送] [" + player.getLogString() + "] [" + url + "] [result:" + result + "]");
			// }
			// if (logger.isDebugEnabled()) {
			// logger.debug("[跨服消息] [" + player.getLogString() + "] [耗时:" + (System.currentTimeMillis() - startTime) + "] [rr:" + rr + "] [pData:" + pData.length() +
			// "] [pDatabyte:" + pData.getBytes().length + "]");
			// }
			// }
			if (rr != null && rr.contains("sendOK")) {
				return "sendOK";
			}
			// if (rr != null && rr.contains("success")) {
			// playerSessions.put(player.getId(), session);
			// return null;
			// }
			if (rr != null && rr.contains("span")) {
				return Translate.服务器出现错误;
			}
			return rr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
	}

	public static void putSignParam(Map<String, String> paramMap) {
		paramMap.put("authorize.username", "wx_across_user");
		paramMap.put("authorize.password", "111111");
	}

	public static String getSignParam(Map<String, String> params) {
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			String v = params.get(keys[i]);
			if (v != null && !v.isEmpty()) {
				sb.append(keys[i] + "=" + v + "&");
			}
		}
		String md5Str = sb.substring(0, sb.length() - 1);

		return md5Str;
	}

	public boolean idCrossServer() {
		return GameConstants.getInstance().getServerName().equals("更端测试b");
	}

	public static Object convertToSpecialedType(Object o, Class<?> t) throws Exception {
		return JsonUtil.objectFromJson(JsonUtil.jsonFromObject(o), t);
	}

	public List<BoardPrize> getBoardPrizes() {
		return boardPrizes;
	}

	public void setBoardPrizes(List<BoardPrize> boardPrizes) {
		this.boardPrizes = boardPrizes;
	}

	public List<ScorePrize> getScorePrizes() {
		return scorePrizes;
	}

	public void setScorePrizes(List<ScorePrize> scorePrizes) {
		this.scorePrizes = scorePrizes;
	}

}
