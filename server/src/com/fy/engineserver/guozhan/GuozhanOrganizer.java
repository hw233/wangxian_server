package com.fy.engineserver.guozhan;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordType;
import com.fy.engineserver.guozhan.exception.CountryAlreadyDeclareWarException;
import com.fy.engineserver.guozhan.exception.CountryCanNotBeDeclareWarException;
import com.fy.engineserver.guozhan.exception.DeclareWarTimeErrorException;
import com.fy.engineserver.guozhan.exception.OperationNotPermitException;
import com.fy.engineserver.guozhan.menu.Option_Guozhan_Cancel;
import com.fy.engineserver.guozhan.menu.Option_Guozhan_OK;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 国战的组织者
 * 本类为负责游戏内国战的宣战组织，国战到点开启通知以及玩家手动参与国战等等
 * 宣战规则：
 * 		1.排名第一的国家可以被每天宣战，弱国允许隔天被宣战一次
 * 		2.每日0-18时，可以对没有被宣战的国家宣战，如果今日本国被其他国家宣战，那么他不能再对其他国宣战
 * 		3.宣战后次日20：15开启国战
 * 		
 * 
 * @version 创建时间：May 3, 2012 1:56:11 PM
 * 
 */
public class GuozhanOrganizer implements Runnable {
	
	public static Logger logger = LoggerFactory.getLogger(GuozhanOrganizer.class);
	
	public static final int STATE_未开启 = 0;
	public static final int STATE_已开启 = 1;
	
	private String cacheFile;
	
	public DiskCache cache = null;
	
	private HashMap<Byte, ArrayList<FastMessage>> messageMap;
	
	private Constants constants = null;
	
	private ArrayList<DeclareWar> declareList;
	
	private List<Guozhan> guozhanList;
	
	private ArrayList<GuozhanHistory> historyList;
	
	private ArrayList<GuozhanStat> statList;
	
	private HashMap<Integer, Long[]> expMap;
	
	private String expConfigFile;
	
	private int state;
	
	public boolean running = true;
	
	public static GuozhanOrganizer self;
	
	public static GuozhanOrganizer getInstance() {
		return self;
	}
	
	public void init() {
		
		long start = System.currentTimeMillis();
		cache = new DefaultDiskCache(new File(cacheFile),"国战数据",10*365*24*60*60*1000);
		if((constants = (Constants)cache.get("constants")) == null) {
			constants = new Constants();
		}
		if((declareList = (ArrayList<DeclareWar>)(cache.get("declareList"))) == null) {
			declareList = new ArrayList<DeclareWar>();
		}
		if((messageMap = (HashMap<Byte,ArrayList<FastMessage>>)cache.get("messageMap")) == null) {
			messageMap = new HashMap<Byte,ArrayList<FastMessage>>();
		}
		if((historyList = (ArrayList<GuozhanHistory>)(cache.get("historyList"))) == null) {
			historyList = new ArrayList<GuozhanHistory>();
		}
		if((statList = (ArrayList<GuozhanStat>)(cache.get("statList"))) == null) {
			statList = new ArrayList<GuozhanStat>();
		}
		
		guozhanList = new ArrayList<Guozhan>();
		
		readExpMapFromConfig();
		
		self = this;
		
		Thread t = new Thread(this, "GuozhanOrganizer");
		t.start();
		
		logger.warn("[国战管理员初始化完成] [宣战数量:"+declareList.size()+"]  [expMap:"+expMap.size()+"] ["+GuozhanOrganizer.class.getName()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		ServiceStartRecord.startLog(this);
	}
	
	public void readExpMapFromConfig() {
		
		expMap = new HashMap<Integer, Long[]>();
	
		try {
			POIFSFileSystem pss = new POIFSFileSystem(new FileInputStream(expConfigFile));
			HSSFWorkbook workbook = new HSSFWorkbook(pss);
	
			HSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					HSSFCell hc = row.getCell(0);
					int level = 0;
					try {
						level = Integer.parseInt(hc.getStringCellValue().trim());
					} catch (Exception ex) {
						level = (int) hc.getNumericCellValue();
					}
	
					hc = row.getCell(1);
					long winExp = 0;
					try {
						winExp = Long.parseLong(hc.getStringCellValue().trim());
					} catch (Exception ex) {
						winExp = (long)hc.getNumericCellValue();
					}
	
					hc = row.getCell(2);
					long loseExp = 0;
					try {
						loseExp = Long.parseLong(hc.getStringCellValue().trim());
					} catch (Exception ex) {
						loseExp = (long) hc.getNumericCellValue();
					}
					expMap.put(level, new Long[]{winExp, loseExp});
				}
			}
		} catch(Exception e) {
			throw new RuntimeException("初始化国战经验表时发生异常");
		}
	}
	
	public void destroy() {
		long start = System.currentTimeMillis();
		
		cache.put("constants", constants);
		cache.put("declareList", declareList);
		cache.put("messageMap", messageMap);
		cache.put("historyList", historyList);
		cache.put("statList", statList);
		((DefaultDiskCache)cache).destory();
		
		System.out.println("[国战管理员Destroy完成] ["+GuozhanOrganizer.class.getName()+"] ["+(System.currentTimeMillis()-start)+"ms]");

	}
	
	public String getExpConfigFile() {
		return expConfigFile;
	}

	public void setExpConfigFile(String expConfigFile) {
		this.expConfigFile = expConfigFile;
	}

	public String getCacheFile() {
		return cacheFile;
	}

	public void setCacheFile(String cacheFile) {
		this.cacheFile = cacheFile;
	}
	
	public void run() {
		while(running) {
			try {
				Thread.sleep(1000);
				long now = System.currentTimeMillis();
				if(state == STATE_未开启) {
					for(DeclareWar dw : declareList) {
						if(!dw.isNotified() && this.isGuozhanOpenBeforeTime(dw.getDeclareTime(), constants.提前通知时间)) {
							dw.setNotified(true);
							sendGuozhanNotify(dw);
						}
					}
					boolean open = isGuozhanOpenTime(now);
					if(open) {
						state = STATE_已开启;
						guozhanList.clear();
						Iterator<DeclareWar> dwite = declareList.iterator();
						while(dwite.hasNext()) {
							DeclareWar dw = dwite.next();
							long dtime = dw.getDeclareTime();
							Calendar cal = Calendar.getInstance();
							int today = cal.get(Calendar.DAY_OF_YEAR);
							int year = cal.get(Calendar.YEAR);
							cal.setTimeInMillis(dtime);
							cal.add(Calendar.DAY_OF_YEAR, 1);
							if(cal.get(Calendar.DAY_OF_YEAR) == today && cal.get(Calendar.YEAR) == year) {
								//今日开战
								startGuozhan(dw.getDeclareCountryId(), dw.getEnemyCountryId());
								dwite.remove();
								cache.put("declareList", declareList);
							}
						}
					}
				} else {
					boolean allOver = true;
					for(Guozhan guozhan : guozhanList) {
						if(guozhan.getState() == Guozhan.STATE_开启) {
							guozhan.heartbeat();
							allOver = false;
						}
					}
					if(allOver) {
						state = STATE_未开启;
					}
				}
			} catch(Throwable e) {
				logger.error("[国战管理心跳错误]", e);
			}
		}
	}
	
	/**
	 * 发送提前国战通知
	 * @param declareWar
	 */
	public void sendGuozhanNotify(DeclareWar declareWar) {
		Player ps[] = PlayerManager.getInstance().getOnlinePlayerByCountry(declareWar.getDeclareCountryId());
		String message = Translate.translateString(Translate.国战通知, new String[][]{{"@tongzhishijian@", String.valueOf(constants.提前通知时间)}, {"@country_name@", CountryManager.得到国家名(declareWar.getEnemyCountryId())}});
		sendMessagesToPlayers(ps, message, (byte)5);
		ps = PlayerManager.getInstance().getOnlinePlayerByCountry(declareWar.getEnemyCountryId());
		message = Translate.translateString(Translate.国战通知, new String[][]{{"@tongzhishijian@", String.valueOf(constants.提前通知时间)}, {"@country_name@", CountryManager.得到国家名(declareWar.getDeclareCountryId())}});
		sendMessagesToPlayers(ps, message, (byte)5);
		declareWar.setNotified(true);
		if(logger.isInfoEnabled()) {
			logger.info("[发送国战即将开始通知] [攻方:"+CountryManager.getInstance().getCountryByCountryId(declareWar.getDeclareCountryId()).getName()+"] [防守方:"+CountryManager.getInstance().getCountryByCountryId(declareWar.getEnemyCountryId()).getName()+"]");
		}
	}
	
	public DeclareWar getDeclareWar(int id) {
		for(DeclareWar dw : declareList) {
			if(dw.getId() == id) {
				return dw;
			}
		}
		return null;
	}
	
	/**
	 * 手动开启国战
	 * @param dw
	 */
	public void testStartGuozhan(DeclareWar dw) {
		declareList.remove(dw);
		cache.put("declareList", declareList);
		try {
			startGuozhan(dw.getDeclareCountryId(), dw.getEnemyCountryId());
			state = STATE_已开启;
		} catch(Exception e) {
			state = STATE_未开启;
			logger.error("[手动开启国战错误]", e);
		}
	}
	
	/**
	 * 国家今日是否被宣战
	 * @param countryId
	 * @return
	 */
	public boolean isCountryBeDeclareWar(byte countryId) {
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_YEAR);
		int year = cal.get(Calendar.YEAR);
		for(DeclareWar dw : declareList) {
			if(dw.getEnemyCountryId() == countryId) {
				cal.setTimeInMillis(dw.getDeclareTime());
				if(cal.get(Calendar.DAY_OF_YEAR) == today && cal.get(Calendar.YEAR) == year) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 某个国家今日是否向其他国家宣过战
	 * @param countryId
	 * @return
	 */
	public boolean isCountryDeclareWar(byte countryId) {
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_YEAR);
		int year = cal.get(Calendar.YEAR);
		for(DeclareWar dw : declareList) {
			if(dw.getDeclareCountryId() == countryId) {
				cal.setTimeInMillis(dw.getDeclareTime());
				if(cal.get(Calendar.DAY_OF_YEAR) == today && cal.get(Calendar.YEAR) == year) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 得到国家今日的宣战
	 * @param countryId
	 * @return
	 */
	public DeclareWar getCountryDeclareWar(byte countryId) {
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_YEAR);
		int year = cal.get(Calendar.YEAR);
		for(DeclareWar dw : declareList) {
			if(dw.getDeclareCountryId() == countryId) {
				cal.setTimeInMillis(dw.getDeclareTime());
				if(cal.get(Calendar.DAY_OF_YEAR) == today && year == cal.get(Calendar.YEAR)) {
					return dw;
				}
			}
		}
		return null;
	}
	
	/**
	 * 得到国家昨天的宣战
	 * @param countryId
	 * @return
	 */
	public DeclareWar getCountryLastDayDeclareWar(byte countryId) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		int lastDay = cal.get(Calendar.DAY_OF_YEAR);
		int year = cal.get(Calendar.YEAR);
		for(DeclareWar dw : declareList) {
			if(dw.getDeclareCountryId() == countryId) {
				cal.setTimeInMillis(dw.getDeclareTime());
				if(cal.get(Calendar.DAY_OF_YEAR) == lastDay &&  year == cal.get(Calendar.YEAR)) {
					return dw;
				}
			}
		}
		return null;
	}
	
	/**
	 * 国家是否今天打国战
	 * @param countryId
	 * @return
	 */
	public boolean isCountryTodayGuozhan(byte countryId) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		int day = cal.get(Calendar.DAY_OF_YEAR);
		int year = cal.get(Calendar.YEAR);
		for(DeclareWar dw : declareList) {
			if(dw.getDeclareCountryId() == countryId || dw.getEnemyCountryId() == countryId) {
				cal.setTimeInMillis(dw.getDeclareTime());
				if(cal.get(Calendar.DAY_OF_YEAR) == day && cal.get(Calendar.YEAR) == year) {
					return true;
				}
			}
		}
		for(Guozhan guozhan : guozhanList) {
			if(guozhan.getAttacker().getCountryId() == countryId || guozhan.getDefender().getCountryId() == countryId) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 得到对于某国当前可以被宣战的国家列表
	 * 宣战规则：
	 * 1.两个国家不能连续两天一起打国战
	 * 2.弱国不能连续两天被宣战
	 * @param countryId
	 * @return
	 */
	public synchronized List<Country> getCanBeDeclareWarCountryList(byte countryId) {
		if(isCountryDeclareWar(countryId) || isCountryBeDeclareWar(countryId)) {
			return new ArrayList<Country>(0);
		}
		Hashtable<Byte, Country> cmap = CountryManager.getInstance().getCountryMap();
		Set<Map.Entry<Byte, Country>> es = cmap.entrySet();
		List<Country> cs = new ArrayList<Country>();
		Country sortedCountries[] = CountryManager.getInstance().getSortedCountrys();
		long weakCountryId = -1;
		if(sortedCountries != null && sortedCountries.length == 3) {
			weakCountryId = sortedCountries[sortedCountries.length-1].getCountryId();
		}
		for(Map.Entry<Byte, Country> e : es) {
			if(countryId == e.getValue().getCountryId()) {
				continue;
			}
			if(isCountryBeDeclareWar(e.getValue().getCountryId()) || isCountryDeclareWar(e.getValue().getCountryId())) {
				continue;
			}
			//弱国不能连续两天被宣战
			boolean weakCountry = e.getValue().getCountryId()==weakCountryId;
			if(weakCountry) {
				//第一的国家可以每天被宣战，否则只能隔天宣战
				if(isCountryTodayGuozhan(e.getValue().getCountryId())) {
					continue;
				}
			}
			//两个国家如果今天打了国战，不能再宣战
			DeclareWar dw = this.getCountryDeclareWar(countryId);
			if(dw != null && dw.getEnemyCountryId() == e.getValue().getCountryId()) {
				continue;
			}
			dw = this.getCountryDeclareWar(e.getValue().getCountryId());
			if(dw != null && dw.getEnemyCountryId() == countryId) {
				continue;
			}
			dw = this.getCountryLastDayDeclareWar(countryId);
			if(dw != null && dw.getEnemyCountryId() == e.getValue().getCountryId()) {
				continue;
			}
			dw = this.getCountryLastDayDeclareWar(e.getValue().getCountryId());
			if(dw != null && dw.getEnemyCountryId() == countryId) {
				continue;
			}
			cs.add(e.getValue());
		}
		return cs;
	}

	/**
	 * 宣战，只有混元至圣、纯阳真圣、司命天王才能发起宣战
	 * 失败条件: 
	 * 					1.被宣战国今天已经被宣战，
	 * 					2.宣战国今天被宣战，或者已经宣战了别的国家
	 * 					3.不在宣战时间内：0-18时
	 * 					4.被宣战国不是排名第一的国家，但是今天要进行国战
	 * @param player
	 * @param countryId
	 */
	public synchronized DeclareWar declareWar(Player player, byte countryId) 
				throws OperationNotPermitException,CountryCanNotBeDeclareWarException,DeclareWarTimeErrorException,CountryAlreadyDeclareWarException {
		long start = System.currentTimeMillis();
		//1.检查官职是否允许
		CountryManager cm = CountryManager.getInstance();
		int 官职 = cm.官职(player.getCountry(), player.getId());
		if(官职 != CountryManager.国王 && 官职 != CountryManager.元帅 && 官职 != CountryManager.大将军) {
			throw new OperationNotPermitException(Translate.玩家官职不够);
		}
		//2.检查时间是否允许
		boolean isDeclareTime = isDeclareWarTime(start);
		if(!isDeclareTime) {
			throw new DeclareWarTimeErrorException(Translate.宣战时间错误);
		}
		//3.检查本国是否今日已经宣战
		if(isCountryDeclareWar(player.getCountry()) || isCountryBeDeclareWar(player.getCountry())) {
			throw new CountryAlreadyDeclareWarException(Translate.本国今日已经宣战);
		}
		//4.检查国家是否可以被宣战
		List<Country> cs = getCanBeDeclareWarCountryList(player.getCountry());
		boolean contain = false;
		for(Country c : cs) {
			if(c.getCountryId() == countryId) {
				contain = true;
				break;
			}
		}
		if(!contain) {
			throw new CountryCanNotBeDeclareWarException(Translate.被宣战国今日不能再被宣战);
		}
		//下面创建宣战
		DeclareWar dw = new DeclareWar();
		dw.setId(Integer.valueOf(StringUtil.randomIntegerString(8)));
		dw.setDeclareCountryId(player.getCountry());
		dw.setDeclarePlayerId(player.getId());
		dw.setDeclareTime(start);
		dw.setEnemyCountryId(countryId);
		declareList.add(dw);
		cache.put("declareList", declareList);
		if(logger.isInfoEnabled()) {
			logger.info("[对某国宣战] [宣战国:"+cm.得到国家名(dw.getDeclareCountryId())+"] [被宣战国:"+cm.得到国家名(dw.getEnemyCountryId())+"] " +
					"[宣战人:"+player.getLogString()+"] [宣战人职位:"+cm.得到官职名(cm.官职(player.getCountry(), player.getId()))+"] " +
							"[消耗:"+(System.currentTimeMillis()-start)+"ms]");
		}
		Player players[] = PlayerManager.getInstance().getOnlinePlayers();
		String content = Guozhan.紫色S +  cm.得到国家名(dw.getDeclareCountryId()) + "</f>"+Guozhan.黄色S+"向</f>" + Guozhan.紫色S + cm.得到国家名(dw.getEnemyCountryId()) + "</f>"+Guozhan.黄色S+"发起了宣战！</f>";
		sendMessagesToPlayers(players, content, (byte)5);
		return dw;
	}
	
	/**
	 * 创建一个国战实体，并且开启国战
	 * 1.通知国战双方所有在线玩家，是否加入国战
	 * 2.防守方刷新NPC以及BOSS
	 * @param attackCountryId
	 * @param defendCountryId
	 */
	public void startGuozhan(byte attackCountryId, byte defendCountryId) throws Exception {
		long start = System.currentTimeMillis();
		
		//创建一个国战实体
		Guozhan guozhan = new Guozhan();
		CountryManager cm = CountryManager.getInstance();
		Country att = cm.getCountryByCountryId(attackCountryId);
		Country def = cm.getCountryByCountryId(defendCountryId);
		guozhan.setAttacker(att);
		guozhan.setDefender(def);
		guozhan.setStartTime(start);
		guozhan.setState(Guozhan.STATE_开启);
		guozhan.setEndTime(start + constants.国战初始时长*60*1000);
		try {
			guozhan.init();
		} catch (Exception e) {
			logger.warn("国战初始化异常",e);
		}
		guozhanList.add(guozhan);
		
		//发送是否参与国战的确认
		PlayerManager playerManager = PlayerManager.getInstance();
		Player attackPlayers[ ] = playerManager.getOnlinePlayerByCountry(guozhan.getAttacker().getCountryId());
		String enemyCountryName = CountryManager.得到国家名(guozhan.getDefender().getCountryId());
		List<Player> needNoticePlayers = new ArrayList<Player>();
		int level = GuozhanOrganizer.getInstance().getConstants().国战等级限制;
		for(Player p : attackPlayers) {
			if(p.getLevel() >= level && !p.isInPrison()) {
				if (GlobalTool.verifyMapTrans(p.getId()) == null) {
					sendConfirmFightToPlayer(p, enemyCountryName);
					needNoticePlayers.add(p);
				}
			}
		}
		
		Player defendPlayers[] = playerManager.getOnlinePlayerByCountry(guozhan.getDefender().getCountryId());
		enemyCountryName = CountryManager.得到国家名(guozhan.getAttacker().getCountryId());
		for(Player p : defendPlayers) {
			if(p.getLevel() >= level && !p.isInPrison()) {
				sendConfirmFightToPlayer(p, enemyCountryName);
				needNoticePlayers.add(p);
			}
		}
		
		int num = getGuozhanTotalNum();
		cache.put("guozhan_num", num+1);
		
		Player players[] = PlayerManager.getInstance().getOnlinePlayers();
		String content = Translate.translateString(Translate.国战开始, new String[][]{{"@attacker_name@", att.getName()},{"@defender_name@", def.getName()}});
		sendMessagesToPlayers(players, content, (byte)5);
		
	//	ActivityNoticeManager.getInstance().activityStart(Activity.国战,needNoticePlayers.toArray(new Player[0]));
		
		logger.info("[国战开启] [进攻方:"+guozhan.getAttacker().getName()+"] [防守方:"+guozhan.getDefender().getName()+"] [开启时间:"+DateUtil.formatDate(new Date(guozhan.getStartTime()), "yyyy-MM-dd HH:mm:ss")+"] [结束时间:"+DateUtil.formatDate(new Date(guozhan.getEndTime()), "yyyy-MM-dd HH:mm:ss")+"]");
	}
	
	/**
	 * 给玩家发送参加国战的确认
	 * @param player
	 * @param enemyCountryName
	 */
	public void sendConfirmFightToPlayer(Player player, String enemyCountryName) {
		String selfCountryName = CountryManager.得到国家名(player.getCountry());
		
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60  * 10);
		mw.setTitle(Translate.国战);
		mw.setDescriptionInUUB(Translate.translateString(Translate.国战开始窗口, new String[][]{{"@country1@",selfCountryName},{"@country2@", enemyCountryName}}));		
		Option_Guozhan_OK ok = new Option_Guozhan_OK();
		ok.setText("是");
		ok.setColor(0xffffff);
		
		Option_Guozhan_Cancel cancel = new Option_Guozhan_Cancel();
		cancel.setText("否");
		cancel.setColor(0xffffff);
		
		mw.setOptions(new Option[]{ok,cancel});
		
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
	}
	
	/**
	 * 玩家参与国战
	 * 条件：
	 * 		1.玩家的国家正在国战中
	 * 		2.玩家必须40级以上
	 * 		3.离国战结束不能少于15分钟
	 * 
	 * @param player
	 */
	public synchronized void playerAttendGuozhan(Player player) {
		long start = System.currentTimeMillis();
		Guozhan guozhan = getPlayerGuozhan(player);
		if(guozhan != null) {
			player.setGuozhan(true);
			guozhan.addAttendPlayer(player);
			GuozhanBornPoint bp = guozhan.getBornPoint(player);
			player.setTransferGameCountry(bp.getMapCountry());
			int x = bp.getX();
			int y = bp.getY();
			int ranX = new Random().nextInt(30);
			int ranY = new Random().nextInt(30);
			ranX -= 15;
			ranY -= 15;
			x += ranX;
			y += ranY;
			player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, bp.getMapName(), x, y));
			player.setLastGame(bp.getMapName());
			player.setLastX(player.getX());
			player.setLastY(player.getY());
			player.setGame(bp.getMapName());
			player.setX(x);
			player.setY(y);
	//		ActivityNoticeManager.getInstance().playerAgreeActivity(player, Activity.国战);
			if(logger.isInfoEnabled()) 
				logger.info("[玩家参与国战] [map:{}] [xy:{}] [player:{}] [消耗{}ms]", new Object[]{bp.getMapName(),x+"/"+y,player.getLogString(), System.currentTimeMillis()-start});
			EnterLimitManager.setValues(player, PlayerRecordType.同意国战次数);
		}
	}
	
	public void playerReturnBack(Player player) {
		player.setTransferGameCountry(player.getCountry());
		Game game = GameManager.getInstance().getGameByName((String) constants.attackerBornMap, player.getCountry());
		GameInfo gi = game.gi;
		MapArea area = gi.getMapAreaByName(Translate.出生点);
		int x = area.getX();
		int y = area.getY();
		int ranX = new Random().nextInt(30);
		int ranY = new Random().nextInt(30);
		ranX -= 15;
		ranY -= 15;
		x += ranX;
		y += ranY;
		if(player.getCurrentGame() != null) {
			player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, gi.getMapName(), x, y));
			player.setLastGame(gi.getMapName());
			player.setLastX(player.getX());
			player.setLastY(player.getY());
			player.setGame(gi.getMapName());
			player.setX(x);
			player.setY(y);
		}
	}
	
	/**
	 * 某个国家的国战是否开启状态
	 * @param countryId
	 */
	public boolean isGuozhanOpen(byte countryId) {
		for(Guozhan g : guozhanList) {
			if(g.getAttacker().getCountryId() == countryId || g.getDefender().getCountryId() == countryId) {
				if(g.getState() == Guozhan.STATE_开启) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获得当前玩家的国战实体
	 * @param player
	 * @return
	 */
	public Guozhan getPlayerGuozhan(Player player) {
		for(Guozhan g : guozhanList) {
			if(g.getAttacker().getCountryId() == player.getCountry() || g.getDefender().getCountryId() == player.getCountry()) {
				if(g.getState() == Guozhan.STATE_开启) {
					return g;
				}
			}
		}
		return null;
	}
	
	/**
	 * 得到某个国家正在进行的国战实体
	 * @param countryId
	 * @return
	 */
	public Guozhan getGuozhan(byte countryId) {
		for(Guozhan g : guozhanList) {
			if(g.getAttacker().getCountryId() == countryId || g.getDefender().getCountryId() == countryId) {
				if(g.getState() == Guozhan.STATE_开启) {
					return g;
				}
			}
		}
		return null;
	}
	
	/**
	 * 当前是否是宣战时间
	 * @param time
	 * @return
	 */
	public boolean isDeclareWarTime(long time) {
		String str[] = constants.开始宣战时间.split(":");
		int hour = Integer.valueOf(str[0]);
		int min = Integer.valueOf(str[1]);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, 0);
		long start = cal.getTimeInMillis();
		str = constants.结束宣战时间.split(":");
		hour = Integer.valueOf(str[0]);
		min = Integer.valueOf(str[1]);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, 0);
		long end = cal.getTimeInMillis();
		if(time >= start && time < end) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是国战开启时间
	 * @param time
	 * @return
	 */
	public boolean isGuozhanOpenTime(long time) {
		String str[] = constants.国战开启时间.split(":");
		int hour = Integer.valueOf(str[0]);
		int min = Integer.valueOf(str[1]);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, 0);
		long start = cal.getTimeInMillis();
		cal.add(Calendar.MINUTE, 5);
		long end = cal.getTimeInMillis();
		if(time >= start && time <  end) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是国战开启前30分钟
	 * @param minutes
	 * @return
	 */
	public boolean isGuozhanOpenBeforeTime(long declareTime, int beforeMinutes) {
		long time = System.currentTimeMillis();
		String str[] = constants.国战开启时间.split(":");
		int hour = Integer.valueOf(str[0]);
		int min = Integer.valueOf(str[1]);
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_YEAR);
		int year = cal.get(Calendar.YEAR);
		cal.setTimeInMillis(declareTime);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		if(cal.get(Calendar.DAY_OF_YEAR) == today && cal.get(Calendar.YEAR) == year) {
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, min);
			cal.add(Calendar.MINUTE, -beforeMinutes);
			cal.set(Calendar.SECOND, 0);
			long start = cal.getTimeInMillis();
			cal.add(Calendar.MINUTE, 5);
			long end = cal.getTimeInMillis();
			if(time >= start && time <  end) {
				return true;
			}
		}
		return false;
	}

	public Constants getConstants() {
		return constants;
	}

	public void setConstants(Constants constants) {
		this.constants = constants;
	}
	
	/**
	 * 增加一条命令
	 * @param player
	 * @param name
	 * @param message
	 * @throws OperationNotPermitException
	 */
	public synchronized void addFastMessage(Player player, String name, String message) throws OperationNotPermitException {
		long start = System.currentTimeMillis();
		//1.检查官职是否允许
		CountryManager cm = CountryManager.getInstance();
		int 官职 = cm.官职(player.getCountry(), player.getId());
		if(官职 != CountryManager.国王 && 官职 != CountryManager.元帅 && 官职 != CountryManager.大将军) {
			throw new OperationNotPermitException(Translate.玩家官职不够);
		}
		byte countryId = player.getCountry();
		ArrayList<FastMessage> mlist = messageMap.get(countryId);
		if(mlist == null) {
			mlist = new ArrayList<FastMessage>();
			messageMap.put(countryId, mlist);
		}
		if(mlist.size() > 100) {
			logger.warn("[玩家蛋疼设置了超过100条命令] ["+player.getLogString()+"]");
			return;
		}
		FastMessage mes = new FastMessage("hd_tongyong", name, message);
		mlist.add(mes);
		cache.put("messageMap", messageMap);
		if(logger.isInfoEnabled())
			logger.info("[玩家添加命令] ["+player.getLogString()+"] [命令:"+name+"] [message:"+message+"] ["+(System.currentTimeMillis()-start)+"ms]");
	}
	
	/**
	 * 删除命令
	 * @param player
	 * @param index
	 */
	public synchronized void removeFastMessage(Player player, int index) throws OperationNotPermitException {
		long start = System.currentTimeMillis();
		//1.检查官职是否允许
		CountryManager cm = CountryManager.getInstance();
		int 官职 = cm.官职(player.getCountry(), player.getId());
		if(官职 != CountryManager.国王 && 官职 != CountryManager.元帅 && 官职 != CountryManager.大将军) {
			throw new OperationNotPermitException(Translate.玩家官职不够);
		}
		byte countryId = player.getCountry();
		ArrayList<FastMessage> mlist = messageMap.get(countryId);
		if(mlist != null && mlist.size() > index) {
			mlist.remove(index);
		} else {
			if(logger.isInfoEnabled())
				logger.info("[玩家删除命令错误] ["+player.getLogString()+"] [index:"+index+"] ["+(System.currentTimeMillis()-start)+"ms]");
		}
		cache.put("messageMap", messageMap);
		if(logger.isInfoEnabled())
			logger.info("[玩家删除命令] ["+player.getLogString()+"] [index:"+index+"] ["+(System.currentTimeMillis()-start)+"ms]");
	}
	
	/**
	 * 得到所有命令，包含三条默认的命令
	 * @param player
	 * @return
	 */
	public List<FastMessage> getFastMessages(Player player) {
		byte countryId = player.getCountry();
		ArrayList<FastMessage> mlist = messageMap.get(countryId);
		List<FastMessage> ms = new ArrayList<FastMessage>();
		ms.addAll(constants.publicMessages);
		if(mlist != null && mlist.size() > 0) {
			ms.addAll(mlist);
		}
		return ms;
	}

	public DiskCache getCache() {
		return cache;
	}

	public void setCache(DiskCache cache) {
		this.cache = cache;
	}

	public HashMap<Byte, ArrayList<FastMessage>> getMessageMap() {
		return messageMap;
	}

	public void setMessageMap(HashMap<Byte, ArrayList<FastMessage>> messageMap) {
		this.messageMap = messageMap;
	}

	public ArrayList<DeclareWar> getDeclareList() {
		return declareList;
	}

	public void setDeclareList(ArrayList<DeclareWar> declareList) {
		this.declareList = declareList;
	}

	public List<Guozhan> getGuozhanList() {
		return guozhanList;
	}

	public void setGuozhanList(List<Guozhan> guozhanList) {
		this.guozhanList = guozhanList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public void updateConstants() {
		cache.put("constants", constants);
	}
	
	/**
	 * 发送消息给一群玩家
	 * @param players
	 * @param content
	 * @param type	
	 * 	服务器通知客户端:
	 * 		0标识2秒就消失的提示窗口
	 * 		1标识信息到聊天栏
	 * 		2标识在屏幕上方显示文字，持续几秒,最多3条10秒，颜色服务器控制
	 * 		3标识从屏幕中间右向左滚动 字一个个显示一个个消失，颜色服务器控制
	 * 		4标识在屏幕下方从左向右显示滚动信息并带粒子, 2条，变颜色1秒，持续10秒, 
	 * 		5覆盖所有窗口上面的提示窗口，一定时间后消失点击不消失，并加入聊天中
	 */
	public void sendMessagesToPlayers(Player players[], String content, byte type) {
		for(Player p : players) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), type, content);
			p.addMessageToRightBag(hintreq);
			if(logger.isDebugEnabled()) {
				logger.debug("[发送广播] ["+p.getUsername()+"] ["+content+"]");
			}
		}
	}

	public ArrayList<GuozhanHistory> getGuozhanHistoryList() {
		return historyList;
	}
	
	/**
	 * 得到某个国家中的国战次数
	 * @param country
	 * @return
	 */
	public List<GuozhanHistory> getCountryGuozhanHistory(Country country) {
		List<GuozhanHistory> list = new ArrayList<GuozhanHistory>();
		for(GuozhanHistory h : historyList) {
			if(h.getAttackCountryName().equals(country.getName()) || h.getDefendCountryName().equals(country.getName())) {
				list.add(h);
			}
		}
		return list;
	}
	
	public GuozhanHistory getGuozhanHistory(long id) {
		for(GuozhanHistory h : historyList) {
			if(h.getId() == id) {
				return h;
			}
		}
		return null;
	}

	public void setHistoryList(ArrayList<GuozhanHistory> historyList) {
		this.historyList = historyList;
	}
	
	public synchronized void updateHistoryList() {
		//只保留最近100个国战历史
		while(historyList.size() > 100) {
			historyList.remove(0);
		}
		cache.put("historyList", historyList);
	}
	
	/**
	 * 得到某国国战统计，没有则创建一个 
	 * @param countryId
	 * @return
	 */
	public GuozhanStat getGuozhanStat(byte countryId) {
		for(GuozhanStat stat : statList) {
			if(stat.getCountryId() == countryId) {
				return stat;
			}
		}
		GuozhanStat stat = new GuozhanStat();
		stat.setCountryId(countryId);
		statList.add(stat);
		return stat;
	}
	
	/**
	 * 得到排好序的国战列表
	 * @return
	 */
	public List<GuozhanStat> getSortedGuozhanList() {
		List<GuozhanStat> slist = new ArrayList<GuozhanStat>();
		slist.addAll(statList);
		Collections.sort(slist, new Comparator<GuozhanStat>() {

			@Override
			public int compare(GuozhanStat o1, GuozhanStat o2) {
				// TODO Auto-generated method stub
				if(o1.getWinNum() > o2.getWinNum()) {
					return -1;
				} else if(o1.getWinNum() < o2.getWinNum()) {
					return 1;
				}
				return 0;
			}
			
		});
		
		return slist;
	}
	
	/**
	 * 更新国战统计
	 */
	public synchronized void updateGuozhanStat() {
		cache.put("statList", statList);
	}

	public ArrayList<GuozhanStat> getStatList() {
		return statList;
	}

	public void setStatList(ArrayList<GuozhanStat> statList) {
		this.statList = statList;
	}

	public HashMap<Integer, Long[]> getExpMap() {
		return expMap;
	}

	public void setExpMap(HashMap<Integer, Long[]> expMap) {
		this.expMap = expMap;
	}

	public int getGuozhanTotalNum() {
		Integer n = (Integer)cache.get("guozhan_num");
		if(n != null) {
			return n;
		}
		return 0;
	}
}
