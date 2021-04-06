package com.fy.engineserver.tournament.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.GameDataRecord;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.battlefield.BattleField;
import com.fy.engineserver.battlefield.BattleFieldInfo;
import com.fy.engineserver.battlefield.concrete.BattleFieldManager;
import com.fy.engineserver.battlefield.concrete.TournamentField;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Battle_BiWuPlayerGoIntoBattleField;
import com.fy.engineserver.menu.Option_Battle_BiWuSignUp;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.newBillboard.IBillboardPlayerInfo;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.tournament.data.OneTournamentData;
import com.fy.engineserver.tournament.data.StatueForTournament;
import com.fy.engineserver.tournament.data.TournamentData;
import com.fy.engineserver.tournament.data.TournamentRankDataClient;
import com.fy.engineserver.tournament.data.TournamentRewardDataClient;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * 1.现在武圣对玩家的黏性太低，改动想要达到目的：非pve的玩家可以偶然体验玩耍，pvp的玩家全天都可以玩，这样游戏可以多一条玩法线路,
 *   最理想的是游戏里面能出现带打赛季等级的商人。
 *   
 * 2.可持续辅助玩法，需要增加一些机制，比如：赛季，观众模式(其他玩家可以隐身进去观看大神操作),直播,一键分享,官网视频宣传等。
 * 
 * 3.入口位置要醒目,这是一个功能性很强的系统(本人玩魔兽7年，7年pvp，每天上线就在奥格(主城)门口pk，或者战场，或者竞技场途中)。
 * 
 * 4.为了强化pvp,后期可以加入玩家之间的挑战PK(玩家不死亡，最先1滴血为失败者),可以在主城门口弄个PK区域，这样PVP玩家每天上线没事干,
 *   就会出现在这个区域互相切磋。
 * 
 * 5.后期会出现2v2,3v3战队形式的比赛形式。
 *   
 * 
 * @author wtx
 * @create 2018-4-3 上午11:22:59
 */
public class TournamentManager implements Runnable {

	private static TournamentManager self;
	public static Logger logger = LoggerFactory.getLogger(TournamentManager.class.getName());

	// 比武雕像数据
	public List<StatueForTournament> statueList = new ArrayList<StatueForTournament>();

	public boolean running = false;

	public boolean noticePlayerIcon = true;
	
	private TournamentManager() {

	}

	public static TournamentManager getInstance() {
		return self;
	}

	public void init() throws Exception {
		
		if(GameConstants.getInstance().getServerName().equals("国内本地测试")){
			每天比武开启时间 = new int[] { 720, 735, 960,975,1050, 1065 };
		}
		em = SimpleEntityManagerFactory.getSimpleEntityManager(TournamentData.class);
		emPlayer = SimpleEntityManagerFactory.getSimpleEntityManager(OneTournamentData.class);
		long startTime = SystemTime.currentTimeMillis();
		running = true;
		Thread thread = new Thread(this, "TournamentManager");
		thread.start();
		self = this;
		long[] ids = em.queryIds(TournamentData.class, "");
		if (ids != null && ids.length > 0) {
			td = em.find(ids[ids.length - 1]);
		}
		if (td == null) {
			td = new TournamentData();
			long id = em.nextId();
			td.id = id;
			em.save(td);
		}
		{
			// 初始化雕像数据
			initStatue();
		}
		// 加载本周比武玩家onePlayerTournamentDataMap
		{
			long[] currentIds = 得到本周竞技的人的id();
			if (currentIds != null && currentIds.length > 0) {
				for (long id : currentIds) {
					OneTournamentData data = emPlayer.find(id);
					onePlayerTournamentDataMap.put(id, data);
				}
			}
		}
		Calendar calendar = Calendar.getInstance();
		date = calendar.get(Calendar.DAY_OF_WEEK);

		// //测试使用的时间
		// {
		// 每天比武开启时间 = new int[160];
		// int minute = 600;
		// for(int i = 0; i < 每天比武开启时间.length; i++){
		// 每天比武开启时间[i] = minute;
		// minute += 5;
		// }
		// }

		{
			上周比武按名次排序后的id = 得到上周竞技的人的id();
			putStatue(上周比武按名次排序后的id);
		}
		load();
		比武奖励初始化();
		ServiceStartRecord.startLog(this);
	}

	public void initStatue() {
		statueList.add(new StatueForTournament(new Point(2973 , 1432), "miemoshenyu", 600132, "斗战胜佛", ArticleManager.COLOR_ORANGE_2, "传送光效/半透明黑白光效", 1, 600118));
		statueList.add(new StatueForTournament(new Point(2650 , 1473), "miemoshenyu", 600133, "乾坤惟仙", ArticleManager.COLOR_PURPLE, "传送光效/半透明黑白光效", 2, 600119));
		statueList.add(new StatueForTournament(new Point(2984 , 1625), "miemoshenyu", 600134, "武圣达人", ArticleManager.COLOR_BLUE, "传送光效/半透明黑白光效", 3, 600120));
	}

	public SimpleEntityManager<TournamentData> em;
	public SimpleEntityManager<OneTournamentData> emPlayer;

	public SimpleEntityManager<TournamentData> getEm() {
		return em;
	}

	public void setEm(SimpleEntityManager<TournamentData> em) {
		this.em = em;
	}

	public TournamentData td;
	/**
	 * 个人比武数据
	 */
	public Hashtable<Long, OneTournamentData> onePlayerTournamentDataMap = new Hashtable<Long, OneTournamentData>();

	public void destroy() {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			if (td != null) {
				em.flush(td);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[TournamentManager] [Destroy] 调用destroy方法异常啦");
		}
		em.destroy();
		emPlayer.destroy();
		System.out.println("[TournamentManager] [Destroy] 调用destroy方法, cost " + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) + " ms");
	}

	public static final int FIGHT_WIN_POINT = 5;

	public static final int FIGHT_LOST_POINT = 1;

	/**
	 * 下标为从第一名开始的前20个人为第一个阶段，以此类推，后面的各个阶段的人数
	 */
	public static int[] 比武排名各个阶段人数 = new int[] { 30, 50, 100, 150, 200, 250, 300, 500 };

	public static int[][] 比武排名各个阶段名次 = new int[][] { { 0, 29 }, { 30, 79 }, { 80, 179 }, { 180, 329 }, { 330, 529 }, { 530, 779 }, { 780, 1079 }, { 1080, 1579 } };

	public long[] rewardYinzis;

	public static String[][] 比武奖励物品 = new String[][] { { Translate.武圣斗魁奖励 }, { Translate.武圣斗帝奖励 }, { Translate.武圣斗圣奖励 }, { Translate.武圣斗尊奖励 }, { Translate.武圣斗宗奖励 }, { Translate.武圣斗皇奖励 }, { Translate.武圣斗王奖励 }, { Translate.武圣斗灵奖励 }, { Translate.武圣斗师奖励 }, { Translate.武圣斗者奖励 }, { Translate.武圣斗徒奖励 }, {} };
	public static String[] 比武奖励银子描述 = new String[] { Translate.比武奖励银子1阶, Translate.比武奖励银子2阶, Translate.比武奖励银子3阶, Translate.比武奖励银子4阶, Translate.比武奖励银子5阶, Translate.比武奖励银子6阶, Translate.比武奖励银子7阶, Translate.比武奖励银子8阶, Translate.比武奖励银子9阶, Translate.比武奖励银子10阶, Translate.比武奖励银子11阶, Translate.比武奖励银子12阶 };
	public static int[][] 比武奖励名次 = new int[][] { { 0, 0 }, { 1, 1 }, { 2, 2 }, { 3, 9 }, { 10, 19 }, { 20, 49 }, { 50, 99 }, { 100, 199 }, { 200, 299 }, { 300, 499 }, { 500, 999 }, { 1000, 1000000 } };

	public int 得到奖励index(int rank) {
		for (int i = 0; i < 比武奖励名次.length; i++) {
			if (rank >= 比武奖励名次[i][0] && rank <= 比武奖励名次[i][1]) {
				return i;
			}
		}
		return 比武奖励物品.length - 1;
	}

	/**
	 * 按照每天的分钟开始
	 */

	public static int[] 每天比武开启时间 = new int[] { 720, 735, 1050, 1065 };

	public static int[] 每天比武提示时间 = new int[] { 690, 1005 };
	// public static int[] 每天比武提示时间 = new int[]{710,965};
	// public static int[] 每天比武开启时间 = new int[]{780,1020,1040,1060,1080,1100,1120,1140,1160,1180,1200};
	List<TournamentRankDataClient> trdcList = new ArrayList<TournamentRankDataClient>();
	/**
	 * 比武时玩家与对手的map
	 * key，value均为玩家id
	 * 当value为null时，该玩家轮空，可以直接获得胜利
	 */
	public HashMap<Long, Long> 比武分组Map = new HashMap<Long, Long>();

	public HashMap<Long, BattleField> 比武场分配Map = new HashMap<Long, BattleField>();
	/**
	 * 暂时排名
	 * 每次比武前都会重新进行排名
	 * 只有有分的才可以排进这个表
	 */
	Long[] 当前积分排名表 = new Long[0];

	public long[] 上周比武按名次排序后的id = new long[0];

	TournamentRewardDataClient[] trdc = new TournamentRewardDataClient[0];

	File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void load() throws Exception {
		if (file != null && file.isFile() && file.exists()) {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = workbook.getSheetAt(0);
			int num = sheet.getPhysicalNumberOfRows();
			rewardYinzis = new long[num - 1];
			for (int i = 1; i < num; i++) {
				HSSFRow row = sheet.getRow(i);
				rewardYinzis[i - 1] = (long) row.getCell(1).getNumericCellValue();
			}
		}
	}
	
	/**
	 * 判断是否在比武之前或者比武等待的30s内,如果不在，则玩家这个时间上线进比武场血不加满
	 * @return
	 */
	public static boolean isWaitTime(){
		Calendar ca = Calendar.getInstance();
		int currtime = ca.get(ca.HOUR)*60*60 + ca.get(ca.MINUTE)*60 + ca.get(ca.SECOND);
		for(int i=0; i<每天比武开启时间.length; i++){
			if(currtime > (每天比武开启时间[i]*60 - 1*60) && currtime < (每天比武开启时间[i]*60 + 30)){
				logger.warn("在等待时间内，i="+i);
				return true;
			}
		}
		return false;
	}

	public void 比武奖励初始化() {
		TournamentRewardDataClient[] trdcTemp = new TournamentRewardDataClient[比武奖励名次.length];
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		for (int i = 0; i < 比武奖励名次.length; i++) {
			trdcTemp[i] = new TournamentRewardDataClient();
			String[] articles = 比武奖励物品[i];
			String yinziDes = 比武奖励银子描述[i];
			trdcTemp[i].setRankRange(new int[] { 比武奖励名次[i][0], 比武奖励名次[i][1] });
			trdcTemp[i].setRewardDes(yinziDes);
			long[] ids = new long[articles.length];
			int[] counts = new int[articles.length];
			for (int j = 0; j < counts.length; j++) {
				counts[j] = 1;
			}
			for (int j = 0; j < articles.length; j++) {
				try {
					Article a = am.getArticle(articles[j]);
					if (a != null) {
						ArticleEntity ae = aem.createTempEntity(a, true, ArticleEntityManager.CREATE_REASON_TOURNAMENT_REWARD, null, a.getColorType());
						if (ae != null) {
							ids[j] = ae.getId();
						}
					}
				} catch (Exception ex) {

				}
			}
			trdcTemp[i].setArticleEntityIds(ids);
			trdcTemp[i].setArticleEntityCounts(counts);
		}
		trdc = trdcTemp;
	}

	/**
	 * 根据currentTournamentPointMap表里的积分进行排序
	 */
	public synchronized void 根据积分排序() {
		try {
			synchronized (当前积分排名表) {
				if (td != null) {
					int currentWeek = 得到一年中的第几个星期_周日并到上星期(SystemTime.currentTimeMillis());
					ArrayList<Long> 积分不为零的玩家ids = new ArrayList<Long>();
					if (!onePlayerTournamentDataMap.isEmpty()) {
						for (Long l : onePlayerTournamentDataMap.keySet()) {
							if (onePlayerTournamentDataMap.get(l) != null && onePlayerTournamentDataMap.get(l).getCurrentWeek() == currentWeek && onePlayerTournamentDataMap.get(l).getCurrentTournamentPoint() > 0) {
								积分不为零的玩家ids.add(l);
							}
						}
					}
					Long[] ids = 积分不为零的玩家ids.toArray(new Long[0]);
					Arrays.sort(ids, new Comparator<Long>() {
						// PlayerManager pm = PlayerManager.getInstance();
						@Override
						public int compare(Long o1, Long o2) {
							try {
								if (onePlayerTournamentDataMap.get(o1).getCurrentTournamentPoint() != onePlayerTournamentDataMap.get(o2).getCurrentTournamentPoint()) {
									return onePlayerTournamentDataMap.get(o2).getCurrentTournamentPoint() - onePlayerTournamentDataMap.get(o1).getCurrentTournamentPoint();
								}
								// Player p1 = pm.getPlayer(o1);
								// Player p2 = pm.getPlayer(o2);
								// if(p1 != null && p2 != null){
								// if(p1.getLevel() != p2.getLevel()){
								// return p2.getLevel() - p1.getLevel();
								// }
								// if(p1.getExp() != p2.getExp()){
								// return (int)((p2.getExp() - p1.getExp())/1000);//避免出现越界
								// }
								// }
							} catch (Exception ex) {
								if (logger.isWarnEnabled()) logger.warn("[根据积分排序] [异常1]", ex);
							}

							return 0;
						}

					});
					this.当前积分排名表 = ids;
				} else {
					if (logger.isWarnEnabled()) logger.warn("[td==null]");
				}
			}
		} catch (Exception ex) {
			if (logger.isWarnEnabled()) logger.warn("[根据积分排序] [异常]", ex);
		}
	}

	public synchronized void signUp(Player player) {
		String result = 报名合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			logger.warn("[比武报名] [失败1:"+result+"] ["+player.getLogString()+"]");
			return;
		}
		if (!td.currentSignUpList.contains(player.getId())) {
			td.currentSignUpList.add(player.getId());
			td.setCurrentSignUpList(td.getCurrentSignUpList());
			String des = Translate.报名成功;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, des);
			player.addMessageToRightBag(hreq);
			if (logger.isWarnEnabled()) logger.warn("[比武报名] [成功] [报名人数："+td.currentSignUpList.size()+"] ["+player.getLogString()+"]");
			// ActivityNoticeManager.getInstance().playerAgreeActivity(player, Activity.武圣);
		}
	}
	
	public int endSignHour = 17;

	public String 报名合法性判断(Player player) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.武圣)) {
			return Translate.合服功能关闭提示;
		}
		if (td == null) {
			return Translate.服务器出现错误;
		}
		if (td.currentSignUpList == null) {
			return Translate.服务器出现错误;
		}
		if (player.getLevel() <= PlayerManager.保护最大级别) {
			return Translate.您的等级不够;
		}
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour >= endSignHour) {
			return Translate.每天17点前报名;
		}
		if (td.currentSignUpList.contains(player.getId())) {
			return Translate.您已经报过名了;
		}
		return null;
	}

	/**
	 * 把参加报名的玩家进行分组，按照阶段分组
	 */
	public synchronized ArrayList<ArrayList<Long>> 比武大分组() {
		ArrayList<ArrayList<Long>> idList = new ArrayList<ArrayList<Long>>();
		ArrayList<Long> hasList = new ArrayList<Long>();
		for (int i = 0; i < 比武排名各个阶段名次.length; i++) {
			int[] 一个阶段内名次 = 比武排名各个阶段名次[i];
			ArrayList<Long> iList = new ArrayList<Long>();
			for (int j = 0; j < 当前积分排名表.length && j >= 一个阶段内名次[0] && j <= 一个阶段内名次[1]; j++) {
				iList.add(当前积分排名表[j]);
			}
			if (!iList.isEmpty()) {
				int size = iList.size();
				// 报名的玩家根据积分段进行分组，iList为这个组中的所有玩家id，包括未报名的玩家，下面进行这些玩家筛选，删除未报名的玩家
				for (int k = size - 1; k >= 0; k--) {
					boolean has = false;
					for (Long l : td.currentSignUpList) {
						if (iList.get(k).longValue() == l.longValue()) {
							has = true;
							hasList.add(l);
							break;
						}
					}
					if (!has) {
						iList.remove(k);
					}
				}
				if (!iList.isEmpty()) {
					idList.add(iList);
				}
			}
		}

		ArrayList<Long> iList = new ArrayList<Long>();
		for (Long l : td.currentSignUpList) {
			boolean has = false;
			for (Long ln : hasList) {
				if (ln.longValue() == l.longValue()) {
					has = true;
					break;
				}
			}
			if (!has) {
				iList.add(l);
			}
		}
		if (!iList.isEmpty()) {
			idList.add(iList);
		}
		return idList;
	}

	public synchronized void 小分组(ArrayList<ArrayList<Long>> idList) {
		if (idList != null) {
			PlayerManager pm = PlayerManager.getInstance();
			Player[] ps = pm.getOnlinePlayers();
			for (ArrayList<Long> iList : idList) {
				if (iList != null) {
					ArrayList<Long> onLineList = new ArrayList<Long>();
					ArrayList<Long> notOnLineList = new ArrayList<Long>();
					for (Long l : iList) {
						try {
							boolean onLine = false;
							for (int i = 0; i < ps.length; i++) {
								Player p = ps[i];
								if (p != null && p.getId() == l) {
									onLine = true;
									break;
								}
							}
							if (onLine) {
								onLineList.add(l);
							} else {
								notOnLineList.add(l);
							}
						} catch (Exception ex) {
							if (logger.isInfoEnabled()) logger.info("[进行比武分组异常]" , ex);
						}
					}
					// 打乱顺序
					Collections.shuffle(onLineList);
					int count = onLineList.size();
					for (int i = 0; i < count; i += 2) {
						if (i + 1 < count) {
							比武分组Map.put(onLineList.get(i), onLineList.get(i + 1));
							比武分组Map.put(onLineList.get(i + 1), onLineList.get(i));
						} else {
							比武分组Map.put(onLineList.get(i), null);
						}
					}

					Collections.shuffle(notOnLineList);
					int c = notOnLineList.size();
					for (int i = 0; i < c; i += 2) {
						if (i + 1 < c) {
							比武分组Map.put(notOnLineList.get(i), notOnLineList.get(i + 1));
							比武分组Map.put(notOnLineList.get(i + 1), notOnLineList.get(i));
						} else {
							比武分组Map.put(notOnLineList.get(i), null);
						}
					}
				}
			}
		}
	}

	public void 进行比武分组() {
		long currtime = System.currentTimeMillis();
		比武分组Map.clear();
		比武场分配Map.clear();
		根据积分排序();
		ArrayList<ArrayList<Long>> idList = 比武大分组();
		小分组(idList);
		if (logger.isInfoEnabled()) logger.info("[进行比武分组耗时测试] [耗时："+(System.currentTimeMillis()-currtime)+"]");
	}

	public void 为比武玩家分配场次(int minute) {
		if (比武分组Map != null && 比武分组Map.keySet() != null) {
			long currtime = System.currentTimeMillis();
			int currentWeek = 得到一年中的第几个星期_周日并到上星期(SystemTime.currentTimeMillis());
			// 已经分配过场地
			ArrayList<Long> hasList = new ArrayList<Long>();
			for (Long l : 比武分组Map.keySet()) {
				if (l != null) {
					if (!hasList.contains(l)) {
						long testtime = System.currentTimeMillis();
						hasList.add(l);
						Long id = 比武分组Map.get(l);
						if (id == null) {
							try {
								long now = System.currentTimeMillis();
								PlayerManager pm = PlayerManager.getInstance();
								Player player = pm.getPlayer(l);
								if (player.isOnline()) {
									if (onePlayerTournamentDataMap.get(l) == null) {
										OneTournamentData optd = 得到玩家竞技数据(l);
										if (optd == null) {
											optd = new OneTournamentData(l, currentWeek);
										}
										onePlayerTournamentDataMap.put(l, optd);
									}
									if (onePlayerTournamentDataMap.get(l).getCurrentWeek() != currentWeek) {
										玩家本星期竞技数据归零(onePlayerTournamentDataMap.get(l));
										onePlayerTournamentDataMap.get(l).setCurrentWeek(currentWeek);
									}
									玩家赢一场竞技(onePlayerTournamentDataMap.get(l));
									// 活跃度统计
									ActivenessManager.getInstance().record(player, ActivenessType.武圣争夺战);
									String des = Translate.translateString(Translate.恭喜您的比赛轮空获得积分, new String[][] { { Translate.COUNT_1, TournamentManager.FIGHT_WIN_POINT + "" } });
									HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, des);
									player.addMessageToRightBag(hreq);
									if (logger.isWarnEnabled()) logger.warn("[轮空玩家] [账号：" + player.getUsername() + "] [角色名：" + player.getName() + "] [ID:" + player.getId() + "] [直接判为胜利] [耗时：" + (System.currentTimeMillis() - now) + "ms]", new Object[] { l, id });
								}else{
									if (logger.isWarnEnabled()) logger.warn("[轮空玩家不在线] [账号：" + player.getUsername() + "] [角色名：" + player.getName() + "] [ID:" + player.getId() + "] [耗时：" + (System.currentTimeMillis() - now) + "ms]", new Object[] { l, id });
								}
							} catch (Exception ex) {
								if (logger.isWarnEnabled()) logger.warn("[为比武玩家分配场次] [处理轮空] [id==null] [异常]", ex);
							}
						} else {
							if (hasList.contains(id)) {
								try {
									long now = System.currentTimeMillis();
									PlayerManager pm = PlayerManager.getInstance();
									Player player = pm.getPlayer(l);
									if (player.isOnline()) {
										if (onePlayerTournamentDataMap.get(l) == null) {
											OneTournamentData optd = 得到玩家竞技数据(l);
											if (optd == null) {
												optd = new OneTournamentData(l, currentWeek);
											}
											onePlayerTournamentDataMap.put(l, optd);
										}
										if (onePlayerTournamentDataMap.get(l).getCurrentWeek() != currentWeek) {
											玩家本星期竞技数据归零(onePlayerTournamentDataMap.get(l));
											onePlayerTournamentDataMap.get(l).setCurrentWeek(currentWeek);
										}
										玩家赢一场竞技(onePlayerTournamentDataMap.get(l));
										// 活跃度统计
										ActivenessManager.getInstance().record(player, ActivenessType.武圣争夺战);
										String des = Translate.translateString(Translate.恭喜您的比赛轮空获得积分, new String[][] { { Translate.COUNT_1, TournamentManager.FIGHT_WIN_POINT + "" } });
										HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, des);
										player.addMessageToRightBag(hreq);
										if (logger.isWarnEnabled()) logger.warn("[轮空玩家] [账号：" + player.getUsername() + "] [角色名：" + player.getName() + "] [ID:" + player.getId() + "] [直接判为胜利] [耗时：" + (System.currentTimeMillis() - now) + "ms]", new Object[] { l, id });
									}
								} catch (Exception ex) {
									if (logger.isWarnEnabled()) logger.warn("[为比武玩家分配场次] [haslist:"+hasList.size()+"] [异常] ["+ex+"]");
								}
							} else {
								hasList.add(id);
								if (创建比武场地(l, id,currtime)) {

								} else {
									if (logger.isWarnEnabled()) logger.warn("[创建比武场地] [失败] [{}] [{}]", new Object[] { l, id });
								}
							}
						}
						if (logger.isInfoEnabled()) logger.info("[分配场地耗时测试] [id:"+id+"] [耗时："+(System.currentTimeMillis()-testtime)+"ms]");
					}
				}
			}
			if (logger.isInfoEnabled()) logger.info("[创建比武场地耗时测试] [minute:"+minute+"] [比武数据："+比武分组Map.keySet().size()+"] [耗时："+(System.currentTimeMillis()-currtime)+"]");
		}
	}

	private Random random = new Random();

	public boolean 创建比武场地(long playerAId, long playerBId,long now) {
		//监测单纯创建比武场地耗时
		long nowtime = System.currentTimeMillis();
		int index = random.nextInt(BattleFieldInfo.仙武大会战场数组.length);
		BattleFieldInfo bi = new BattleFieldInfo();
		bi.setName(BattleFieldInfo.仙武大会战场地图数组[index]);
		bi.setBattleCategory(BattleFieldInfo.仙武大会战场数组[index]);
		bi.setBattleFightingType(BattleField.BATTLE_FIGHTING_TYPE_对战);
		bi.setMapName(BattleFieldInfo.仙武大会战场地图数组[index]);
		bi.setHolidyRewardParam(1);
		bi.setBattleRewardParam(30);
		bi.setLastingTimeForNotEnoughPlayers(30000L);
		bi.setMaxLifeTime(540 * 1000L);
		bi.setMaxPlayerLevel(1000);
		bi.setMinPlayerLevel(0);
		bi.setMinPlayerNumForStartOnOneSide(1);
		bi.setMaxPlayerNumOnOneSide(1);
		bi.setStartFightingTime(60 * 1000L);
		bi.setDescription(Translate.text_1900 + BattleFieldInfo.仙武大会战场地图数组[index]);
		bi.setBangpaiFlag(false);

		PlayerManager pm = PlayerManager.getInstance();
		BattleFieldManager bfm = BattleFieldManager.getInstance();
		Player playerA = null;
		Player playerB = null;
		String playerAName = "";
		String playerBName = "";
		try {
			playerA = pm.getPlayer(playerAId);
			playerAName = playerA.getName();
		} catch (Exception exx) {
			exx.printStackTrace();
			if (logger.isWarnEnabled()) logger.warn("[仙武大会得到人物异常] [" + playerAId + "]", exx);
		}
		try {
			playerB = pm.getPlayer(playerBId);
			playerBName = playerB.getName();
		} catch (Exception exx) {
			exx.printStackTrace();
			if (logger.isWarnEnabled()) logger.warn("[仙武大会得到人物异常] [" + playerBId + "]", exx);
		}

		bi.setDescription(playerAName + "vs" + playerBName);
		BattleField battle = bfm.createNewBattleField(bi);

		if (battle != null && battle instanceof TournamentField) {
			((TournamentField) battle).setBornRegionXForA(BattleFieldInfo.仙武大会战场出生点数组[index][0]);
			((TournamentField) battle).setBornRegionYForA(BattleFieldInfo.仙武大会战场出生点数组[index][1]);
			((TournamentField) battle).setBornRegionXForB(BattleFieldInfo.仙武大会战场出生点数组[index][2]);
			((TournamentField) battle).setBornRegionYForB(BattleFieldInfo.仙武大会战场出生点数组[index][3]);
			((TournamentField) battle).setFightRegionXForA(BattleFieldInfo.仙武大会战场战斗点数组[index][0]);
			((TournamentField) battle).setFightRegionYForA(BattleFieldInfo.仙武大会战场战斗点数组[index][1]);
			((TournamentField) battle).setFightRegionXForB(BattleFieldInfo.仙武大会战场战斗点数组[index][2]);
			((TournamentField) battle).setFightRegionYForB(BattleFieldInfo.仙武大会战场战斗点数组[index][3]);
			((TournamentField) battle).sideA = playerAId;
			((TournamentField) battle).sideB = playerBId;
			((TournamentField) battle).playerA = playerA;
			((TournamentField) battle).playerB = playerB;
			比武场分配Map.put(playerAId, battle);
			比武场分配Map.put(playerBId, battle);
		}else{
			if (logger.isWarnEnabled()) logger.warn(battle == null?"battle == null":"[比武场分配异常] ["+battle.getClass()+"] [" + playerAId + "] [" + playerBId + "]");
		}
		WindowManager windowManager = WindowManager.getInstance();
		if (windowManager == null) {
			if (logger.isWarnEnabled()) logger.warn("[创建比武场地] [失败] [windowManager == null]");
			return false;
		}
		if (playerA != null && playerA.isOnline()) {
			String res = GlobalTool.verifyMapTrans(playerA.getId());
			if (res == null) {
				MenuWindow mw = windowManager.createTempMenuWindow(60);
				mw.setTitle(Translate.进入竞技场);
				mw.setDescriptionInUUB(Translate.进入竞技场提示);
				Option_Battle_BiWuPlayerGoIntoBattleField option = new Option_Battle_BiWuPlayerGoIntoBattleField();
				option.setText(Translate.进入竞技场);
				option.setColor(0xFFFFFF);
				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.取消);
				cancel.setColor(0xFFFFFF);
				mw.setOptions(new Option[] { option, cancel });
				CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
				playerA.addMessageToRightBag(req);
			}
		}
		if (playerB != null && playerB.isOnline()) {
			String res = GlobalTool.verifyMapTrans(playerB.getId());
			if (res == null) {
				MenuWindow mw = windowManager.createTempMenuWindow(60);
				mw.setTitle(Translate.进入竞技场);
				mw.setDescriptionInUUB(Translate.进入竞技场);
				Option_Battle_BiWuPlayerGoIntoBattleField option = new Option_Battle_BiWuPlayerGoIntoBattleField();
				option.setText(Translate.进入竞技场);
				option.setColor(0xFFFFFF);
				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.取消);
				cancel.setColor(0xFFFFFF);
				mw.setOptions(new Option[] { option, cancel });
				CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
				playerB.addMessageToRightBag(req);
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("[创建比武场地] [成功] [{}] [{}] [{}]  [耗时：{}] [创建比武场地耗时：{}]", new Object[] { bi.getDescription(), playerAId, playerBId ,(System.currentTimeMillis()-now),(System.currentTimeMillis()-nowtime)});
		}
		return true;
	}

	public int minute;
	public int date;
	public int week;
	public boolean notify1;
	public boolean notify2;
	public long flushTime;

	@Override
	public void run() {
		Calendar calendar = Calendar.getInstance();
		while (running) {
			try {
				Thread.sleep(25000);
				if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
					continue;
				}
				if (UnitServerFunctionManager.needCloseFunctuin(Function.武圣)) {
					continue;
				}
				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				calendar.setTimeInMillis(now);
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				minute = 60 * hour + minute;
				if (minute != this.minute && isTimeValid(minute)) {
					this.minute = minute;
					进行比武分组();
					为比武玩家分配场次(minute);
				}
				int date = calendar.get(Calendar.DAY_OF_WEEK);
				if (this.date != date) {
					this.date = date;
					notify1 = false;
					notify2 = false;
					td.currentSignUpList.clear();
					td.setCurrentSignUpList(td.getCurrentSignUpList());
					if (date == 2) {
						每周的竞技列表切换();
					}
				}

				if (minute == 每天比武提示时间[0]) {
					if (!notify1) {
						notify1 = true;
						给在线玩家发送报名窗口();
					}
				}

				if (minute == 每天比武提示时间[1]) {
					if (!notify2) {
						notify2 = true;
						给在线玩家发送报名窗口();
					}
				}
				if (now - flushTime > 1800000) {
					flushTime = now;
					刷新本周按积分排名的前100名人的数据_客户端();
				}

			} catch (Throwable ex) {
				logger.error("竞技心跳出现异常", ex);
			}
		}
	}

	public void 每周的竞技列表切换() {
		上周比武按名次排序后的id = 得到切换周的竞技的人的id();
		putStatue(上周比武按名次排序后的id);
		if (!onePlayerTournamentDataMap.isEmpty()) {
			for (Long l : onePlayerTournamentDataMap.keySet()) {
				if (onePlayerTournamentDataMap.get(l) != null) {
					onePlayerTournamentDataMap.get(l).setLastWeek(onePlayerTournamentDataMap.get(l).getCurrentWeek());
					onePlayerTournamentDataMap.get(l).setLastWeekLostCount(onePlayerTournamentDataMap.get(l).getCurrentLostCount());
					onePlayerTournamentDataMap.get(l).setLastWeekTournamentPoint(onePlayerTournamentDataMap.get(l).getCurrentTournamentPoint());
					onePlayerTournamentDataMap.get(l).setLastWeekWinCount(onePlayerTournamentDataMap.get(l).getCurrentWinCount());
					onePlayerTournamentDataMap.get(l).setPickReward(false);
					玩家本星期竞技数据归零(onePlayerTournamentDataMap.get(l));
				}
			}
			// onePlayerTournamentDataMap.clear();
		}
	}

	public boolean isTimeValid(int minute) {
		for (int i = 0; i < 每天比武开启时间.length; i++) {
			int value = 每天比武开启时间[i];
			if (value == minute) {
				return true;
			}
		}
		return false;
	}

	public long[] 得到上周竞技的人的id() {
		int lastWeek = 得到一年中的第几个星期_周日并到上星期(System.currentTimeMillis() - 7 * 1l * 24 * 3600 * 1000);
		try {
			long count = emPlayer.count();
			// return emPlayer.queryIds(OneTournamentData.class, "lastWeek="+lastWeek, "lastWeekTournamentPoint desc", 1, count+1);
			return emPlayer.queryIds(OneTournamentData.class, "lastWeek=?", new Object[] { lastWeek }, "lastWeekTournamentPoint desc", 1, count + 1);
		} catch (Exception ex) {
			logger.error("[得到上周竞技的人的id] [异常]", ex);
			return new long[0];
		}
	}

	public long[] 得到本周竞技的人的id() {
		int week = 得到一年中的第几个星期_周日并到上星期(System.currentTimeMillis());
		try {
			long count = emPlayer.count();
			return emPlayer.queryIds(OneTournamentData.class, "currentWeek=?", new Object[] { week }, "currentTournamentPoint desc", 1, count + 1);
		} catch (Exception ex) {
			logger.error("[得到本周竞技的人的id] [异常]", ex);
			return new long[0];
		}
	}

	public long[] 得到切换周的竞技的人的id() {
		int lastWeek = 得到一年中的第几个星期_周日并到上星期(System.currentTimeMillis() - 7 * 1l * 24 * 3600 * 1000);
		try {
			long count = emPlayer.count();
			return emPlayer.queryIds(OneTournamentData.class, "currentWeek=?", new Object[] { lastWeek }, "currentTournamentPoint desc", 1, count + 1);
		} catch (Exception ex) {
			logger.error("[得到切换周的竞技的人的id] [异常]", ex);
			return new long[0];
		}
	}

	public List<OneTournamentData> 得到本周按积分排名的前1000名人的数据() {
		int week = 得到一年中的第几个星期_周日并到上星期(System.currentTimeMillis());
		try {
			return emPlayer.query(OneTournamentData.class, "currentWeek=?", new Object[] { week }, "currentTournamentPoint desc", 1, 1001);
		} catch (Exception ex) {
			logger.error("[得到本周按积分排名的前1000名人的数据] [异常]", ex);
			return new ArrayList<OneTournamentData>(0);
		}
	}

	public List<TournamentRankDataClient> 得到本周按积分排名的前100名人的数据_客户端() {
		return trdcList;
	}

	public void 刷新本周按积分排名的前100名人的数据_客户端() {
		List<OneTournamentData> list = 得到本周按积分排名的前1000名人的数据();
		List<TournamentRankDataClient> listC = new ArrayList<TournamentRankDataClient>(0);
		if (list != null) {
			int count = 0;
//			SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
			PlayerManager pm = PlayerManager.getInstance();
			TransitRobberyEntityManager trm = TransitRobberyEntityManager.getInstance();
			for (OneTournamentData otd : list) {
				if (otd != null) {
					try {
						List<IBillboardPlayerInfo> il = trm.getBillboardPlayerInfo(otd.playerId);
						if(il != null && il.size() > 0) {
							IBillboardPlayerInfo ipi = il.get(0);
							TournamentRankDataClient c = new TournamentRankDataClient();
							c.setId(otd.playerId);
							c.setLost(otd.getCurrentLostCount());
							c.setWin(otd.getCurrentWinCount());
							c.setPoint(otd.getCurrentTournamentPoint());
							c.setCareer(ipi.getMainCareer());
							c.setCountry(ipi.getCountry());
							c.setLevel((short) ipi.getLevel());
							c.setName(ipi.getName());
							listC.add(c);
						} else {
							Player p = pm.getPlayer(otd.playerId);
							TournamentRankDataClient c = new TournamentRankDataClient();
							c.setId(otd.playerId);
							c.setLost(otd.getCurrentLostCount());
							c.setWin(otd.getCurrentWinCount());
							c.setPoint(otd.getCurrentTournamentPoint());
							c.setCareer(p.getCareer());
							c.setCountry(p.getCountry());
							c.setLevel((short) p.getLevel());
							c.setName(p.getName());
							listC.add(c);
						}
						count++;
						if (count >= 100) {
							break;
						}
					} catch (Exception ex) {
						logger.error("[刷新本周按积分排名的前100名人的数据_客户端, 错误]  ", ex);
					}

				}
			}
		}
		trdcList = listC;
	}

	public void 领取奖励(Player player) {
		int rank = 10000;
		boolean canPickReward = false;
		if (上周比武按名次排序后的id != null && 上周比武按名次排序后的id.length > 0) {
			for (int i = 0; i < 上周比武按名次排序后的id.length; i++) {
				if (上周比武按名次排序后的id[i] == player.getId()) {
					rank = i;
					canPickReward = true;
					break;
				}
			}
		}
		if (canPickReward) {
			OneTournamentData otd = onePlayerTournamentDataMap.get(player.getId());
			if (otd == null) {
				otd = 得到玩家竞技数据(player.getId());
			}

			if (otd != null && !otd.pickReward) {
				int index = 得到奖励index(rank);
				String[] articleNames = 比武奖励物品[index];
				if (articleNames.length > 0) {
					int emptyCells = 0;
					Knapsack k = player.getKnapsack_common();
					if (k != null) {
						emptyCells += k.getEmptyNum();
					}
					k = player.getKnapsack_fangbao();
					if (k != null) {
						emptyCells += k.getEmptyNum();
					}
					if (emptyCells < articleNames.length) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.背包空间不足);
						player.addMessageToRightBag(hreq);
						return;
					}
					otd.setPickReward(true);
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleManager am = ArticleManager.getInstance();
					for (int i = 0; i < articleNames.length; i++) {
						Article a = am.getArticle(articleNames[i]);
						if (a != null) {
							try {
								ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_TOURNAMENT_REWARD, player, a.getColorType(), 1, true);
								// 合服打折活动
								CompoundReturn cr = ActivityManagers.getInstance().getValue(3, player);
								int count = 1;
								if (cr != null && cr.getBooleanValue()) {
									count = cr.getIntValue();
								}
								//
								player.putToKnapsacks(ae, count, "领取竞技奖励");
								// player.putToKnapsacks(ae, "领取竞技奖励");
							} catch (Exception ex) {
								logger.error("[领取竞技奖励] [异常] [" + player.getLogString() + "] [rank:" + rank + "]", ex);
							}
						}
					}
					long rewardYinzi = 0;
					if (rewardYinzis.length > rank) {
						rewardYinzi = rewardYinzis[rank];
					} else {
						rewardYinzi = rewardYinzis[rewardYinzis.length - 1];
					}
					BillingCenter bc = BillingCenter.getInstance();
					try {
						// 合服打折活动
						CompoundReturn cr = ActivityManagers.getInstance().getValue(3, player);
						int count = 1;
						if (cr != null && cr.getBooleanValue()) {
							count = cr.getIntValue();
						}
						//
						bc.playerSaving(player, rewardYinzi * count, CurrencyType.SHOPYINZI, SavingReasonType.TOURNAMENT, "竞技奖励银子");
					} catch (Exception ex) {
						logger.error("[领取竞技奖励] [异常] [" + player.getLogString() + "] [rewardYinzi:" + rewardYinzi + "] [rank:" + rank + "]", ex);
					}
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.领取奖励成功);
					player.addMessageToRightBag(hreq);
					logger.warn("[领取竞技奖励] [成功] [" + player.getLogString() + "] [rewardYinzi:" + rewardYinzi + "] [rank:" + rank + "]");
					if (AchievementManager.getInstance() != null) {
						if (rank < 50) {
							AchievementManager.getInstance().record(player, RecordAction.武斗前50名次数, 1);
							if (rank < 10) {
								AchievementManager.getInstance().record(player, RecordAction.武斗前10名次数, 1);
								if (rank == 0) {
									AchievementManager.getInstance().record(player, RecordAction.武斗冠军次数, 1);
									try {
										if (PlayerAimManager.instance.disk.get(PlayerAimManager.wushengKey) != null) {
											long playerId = (Long) PlayerAimManager.instance.disk.get(PlayerAimManager.wushengKey);
											if (player.getId() == playerId) {
												GameDataRecord gdr = AchievementManager.getInstance().getPlayerDataRecord(player, RecordAction.连续获得武圣争夺战第一名次数);
												if (gdr != null) {
													AchievementManager.getInstance().record(player, RecordAction.连续获得武圣争夺战第一名次数, gdr.getNum()+1);
												} else {
													AchievementManager.getInstance().record(player, RecordAction.连续获得武圣争夺战第一名次数, 1);
												}
											} else {
												GameDataRecord gdr = AchievementManager.getInstance().getPlayerDataRecord(player, RecordAction.连续获得武圣争夺战第一名次数);
												if (gdr != null) {
													AchievementManager.getInstance().record(player, RecordAction.连续获得武圣争夺战第一名次数, 0);
												}
											}
											PlayerAimManager.instance.disk.put(PlayerAimManager.wushengKey, player.getId());
										} else {
											PlayerAimManager.instance.disk.put(PlayerAimManager.wushengKey, player.getId());
										}
									} catch (Exception e) {
										PlayerAimManager.logger.error("[目标系统] [统计武圣连续第一] [异常] [" + player.getLogString() + "]", e);
									}
								} else if (rank == 1) {
									AchievementManager.getInstance().record(player, RecordAction.武斗亚军次数, 1);
								} else if (rank == 2) {
									AchievementManager.getInstance().record(player, RecordAction.武斗季军次数, 1);
								}
							}

						}
					}
				}
			} else {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.你已经领过奖励了);
				player.addMessageToRightBag(hreq);
				logger.warn("[领取竞技奖励] [失败] [" + player.getLogString() + "] [已经领过奖励] [rank:" + rank + "]");
			}
		}
	}

	public OneTournamentData 得到玩家竞技数据(long id) {
		OneTournamentData otd = onePlayerTournamentDataMap.get(id);
		if (otd != null) {
			return otd;
		}
		try {
			otd = emPlayer.find(id);
			if (otd != null) {
				onePlayerTournamentDataMap.put(id, otd);
			}
			return otd;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 星期日计算到上周
	 * @return
	 */
	public static int 得到一年中的第几个星期_周日并到上星期(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day == 1) {
			if (week == 1) {
				// 切换年
				calendar.setTimeInMillis(time - 7 * 1l * 24 * 3600 * 1000);
				week = calendar.get(Calendar.WEEK_OF_YEAR);
			} else {
				// 星期日算作上周
				week = week - 1;
			}
		}
		return week;
	}

	public void 玩家本星期竞技数据归零(OneTournamentData optd) {
		optd.setCurrentLostCount(0);
		optd.setCurrentTournamentPoint(0);
		optd.setCurrentWeek(-2);
		optd.setCurrentWinCount(0);
	}

	public void 玩家赢一场竞技(OneTournamentData optd) {
		if (optd == null) {
			return;
		}
		optd.setCurrentTournamentPoint(optd.getCurrentTournamentPoint() + FIGHT_WIN_POINT);
		logger.warn("[胜利加积分] [" + optd.playerId + "] [积分变化:" + FIGHT_WIN_POINT + "] [变化后：" + optd.getCurrentTournamentPoint() + "]");
		optd.setCurrentWinCount(optd.getCurrentWinCount() + 1);

		try {
			Player player = PlayerManager.getInstance().getPlayer(optd.playerId);
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.参加武斗次数, 1);
				AchievementManager.getInstance().record(player, RecordAction.武斗获胜次数, 1);
				GameDataRecord gdr = AchievementManager.getInstance().getPlayerDataRecord(player, RecordAction.武斗连胜次数);
				if (gdr != null) {
					AchievementManager.getInstance().record(player, RecordAction.武斗连胜次数, gdr.getNum()+1);
				}
			}
		} catch (Exception ex) {

		}
	}

	public void 玩家输一场竞技(OneTournamentData optd) {
		if (optd == null) {
			return;
		}
		optd.setCurrentTournamentPoint(optd.getCurrentTournamentPoint() + FIGHT_LOST_POINT);
		logger.warn("[失败加积分] [" + optd.playerId + "] [积分变化:" + FIGHT_LOST_POINT + "] [变化后：" + optd.getCurrentTournamentPoint() + "]");
		optd.setCurrentLostCount(optd.getCurrentLostCount() + 1);

		try {
			Player player = PlayerManager.getInstance().getPlayer(optd.playerId);
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.参加武斗次数, 1);
				GameDataRecord gdr = AchievementManager.getInstance().getPlayerDataRecord(player, RecordAction.武斗连胜次数);
				if (gdr != null) {
					AchievementManager.getInstance().record(player, RecordAction.武斗连胜次数, 0);
				}
			}
		} catch (Exception ex) {

		}
	}

	public TournamentRewardDataClient[] 得到比武奖励() {
		return trdc;
	}

	public boolean 是否可以领取奖励(Player player) {
		int rank = 10000;
		boolean canPickReward = false;
		if (上周比武按名次排序后的id != null && 上周比武按名次排序后的id.length > 0) {
			for (int i = 0; i < 上周比武按名次排序后的id.length; i++) {
				if (上周比武按名次排序后的id[i] == player.getId()) {
					rank = i;
					canPickReward = true;
					logger.warn("[是否可以领取奖励1] [" + player.getLogString() + "] [rank:" + rank + "]");
					break;
				}
			}
		}
		if (canPickReward) {
			OneTournamentData otd = onePlayerTournamentDataMap.get(player.getId());
			if (otd == null) {
				otd = 得到玩家竞技数据(player.getId());
			}

			if (otd != null && !otd.pickReward) {
				logger.warn("[是否可以领取奖励2] [" + player.getLogString() + "] [rank:" + rank + "]");
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public TournamentRewardDataClient 得到玩家的奖励(Player player) {
		if (是否可以领取奖励(player)) {
			TournamentRewardDataClient c = new TournamentRewardDataClient();
			int rank = 10000;
			if (上周比武按名次排序后的id != null && 上周比武按名次排序后的id.length > 0) {
				for (int i = 0; i < 上周比武按名次排序后的id.length; i++) {
					if (上周比武按名次排序后的id[i] == player.getId()) {
						rank = i;
						break;
					}
				}
			}
			int index = 得到奖励index(rank);
			if (trdc.length > index) {
				TournamentRewardDataClient temp = trdc[index];
				c.articleEntityCounts = temp.articleEntityCounts;
				c.articleEntityIds = temp.articleEntityIds;
				c.rankRange = new int[] { rank, rank };
			}
			if (rewardYinzis.length > rank) {
				c.setRewardDes(Translate.银子 + "：" + BillingCenter.得到带单位的银两(rewardYinzis[rank]));
			} else {
				c.setRewardDes(Translate.银子 + "：" + BillingCenter.得到带单位的银两(rewardYinzis[rewardYinzis.length - 1]));
			}
			return c;
		} else {
			return new TournamentRewardDataClient();
		}
	}

	public synchronized void 给在线玩家发送报名窗口() {
		if (td == null) {
			TournamentManager.logger.warn("[报名] [失败] [td == null]");
			return;
		}
		if (td.currentSignUpList == null) {
			TournamentManager.logger.warn("[报名] [失败] [currentSignUpList == null]");
			return;
		}

		PlayerManager pm = PlayerManager.getInstance();
		List<Player> players = new ArrayList<Player>();
		if (pm != null) {
			Player[] ps = pm.getOnlinePlayers();
			if (ps != null) {
				for (Player p : ps) {
					if (p != null && p.getLevel() > PlayerManager.保护最大级别 && !td.currentSignUpList.contains(p.getId())) {
						WindowManager mm = WindowManager.getInstance();
						MenuWindow mw = mm.createTempMenuWindow(180);
						mw.setDescriptionInUUB(Translate.武圣争夺战半小时后一触即发您还没有报名报名参加吗);
						Option_Battle_BiWuSignUp option = new Option_Battle_BiWuSignUp();
						option.setText(Translate.确定);
						Option cancelOption = new Option_UseCancel();
						cancelOption.setText(Translate.取消);
						mw.setOptions(new Option[] { option, cancelOption });
						CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
						p.addMessageToRightBag(req);
						players.add(p);
					}
				}

			}else{
				TournamentManager.logger.warn("[报名] [失败] [playerss == null]");
			}
		}
		// ActivityNoticeManager.getInstance().activityStart(Activity.武圣, players.toArray(new Player[0]));
	}

	/**
	 * 放置雕像
	 */
	public void putStatue(long[] playerIds) {
		if (!GameConstants.getInstance().getServerName().equals("国内本地测试")) {
			return;
		}
		if (GameConstants.getInstance().getServerName().equals("国内本地测试")) {
			playerIds = new long[] {1100000000005468164L,1100000000005468163L,1100000000005468162L};
		}
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn("放置比武雕像NPC:" + Arrays.toString(playerIds));
		}
		if (playerIds != null && playerIds.length > 0) {
			for (StatueForTournament statue : statueList) {
				long playerId = 0;
				switch (statue.getIndex()) {
				case 1:
					playerId = playerIds[0];
					break;
				case 2:
					playerId = playerIds.length >= 2 ? playerIds[1] : 0L;
					break;
				case 3:
					playerId = playerIds.length >= 3 ? playerIds[2] : 0L;
					break;

				default:
					break;
				}
				try {
					if (playerId != 0) {
						Player player = GamePlayerManager.getInstance().getPlayer(playerId);
						if (player != null) {
							statue.putNpcLike(player);
							ActivitySubSystem.logger.warn("[放置角色雕像] [" + player.getLogString() + "] [" + statue.toString() + "]");
						}
					}
				} catch (Exception e) {
					ActivitySubSystem.logger.warn("[放置角色雕像] [异常] [playerId:" + playerId + "]", e);
				}
			}
		}
	}
}
