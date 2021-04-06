package com.fy.engineserver.country.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.activity.fairyRobbery.FairyRobberyManager;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.cityfight.CityFightManager;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.country.data.BiaoJu;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.data.CountryInfoForClient;
import com.fy.engineserver.country.data.CountryManagerData;
import com.fy.engineserver.country.data.CountryQiujinAndJinyanForClient;
import com.fy.engineserver.country.data.WangZheTransferPointOnMap;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_JingYan;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.guozhan.GuozhanHistory;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.guozhan.GuozhanStat;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Country_bamian_queren_byId;
import com.fy.engineserver.menu.Option_Country_ciguan;
import com.fy.engineserver.menu.Option_Country_ciguan_queren_byId;
import com.fy.engineserver.menu.Option_Country_jiejin_queren_byId;
import com.fy.engineserver.menu.Option_Country_jingbiao;
import com.fy.engineserver.menu.Option_Country_jinyan;
import com.fy.engineserver.menu.Option_Country_jinyan_byName;
import com.fy.engineserver.menu.Option_Country_notice;
import com.fy.engineserver.menu.Option_Country_qiujin;
import com.fy.engineserver.menu.Option_Country_qiujin_byName;
import com.fy.engineserver.menu.Option_Country_renming_byName;
import com.fy.engineserver.menu.Option_Country_renming_queren_byId;
import com.fy.engineserver.menu.Option_Country_shifang;
import com.fy.engineserver.menu.Option_Country_shifang_byId;
import com.fy.engineserver.menu.Option_Country_shifang_queren_byId;
import com.fy.engineserver.menu.Option_Country_tuoyun_chuansong;
import com.fy.engineserver.menu.Option_Country_zhaojiling;
import com.fy.engineserver.menu.Option_Country_zhaojiling_byId;
import com.fy.engineserver.menu.Option_Country_zhaojilingbynpc;
import com.fy.engineserver.menu.Option_Country_zhiqu;
import com.fy.engineserver.menu.Option_Country_zhuijiazijin;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.COUNTRY_WANGZHEZHIYIN_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_RES;
import com.fy.engineserver.message.QUERY_COUNTRY_COMMISSION_OR_RECALL_RES;
import com.fy.engineserver.message.QUERY_COUNTRY_JINKU_REQ;
import com.fy.engineserver.message.QUERY_COUNTRY_JINKU_RES;
import com.fy.engineserver.message.QUERY_COUNTRY_MAIN_PAGE_RES;
import com.fy.engineserver.message.QUERY_COUNTRY_OFFICER_RES;
import com.fy.engineserver.message.QUERY_COUNTRY_QIUJIN_JINYAN_RES;
import com.fy.engineserver.message.QUERY_COUNTRY_VOTE_RES;
import com.fy.engineserver.message.QUERY_COUNTRY_WANGZHEZHIYIN_RES;
import com.fy.engineserver.message.QUERY_COUNTRY_YULINJUN_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.npc.BiaoCheNpc;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.transport.RequestMessage;

public class CountryManager implements Runnable {

	public boolean running = true;

	public CountryManagerData data;
	public SimpleEntityManager<Country> em;
	public SimpleEntityManager<CountryManagerData> emm;

	public SimpleEntityManager<Country> getEm() {
		return em;
	}

	public void setEm(SimpleEntityManager<Country> em) {
		this.em = em;
	}

	public SimpleEntityManager<CountryManagerData> getEmm() {
		return emm;
	}

	public void setEmm(SimpleEntityManager<CountryManagerData> emm) {
		this.emm = emm;
	}

	public static int LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME = 18;
	public static int LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME2 = 19;
	public static int LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME3 = 20;
	public static int LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME4 = 21;
	public static int LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME_MINUTE = 30;
	public static int LIMIT_COUNTRY_FIGHT_CLOSE_PLAYER_START_TIME = 19;
	public static int LIMIT_COUNTRY_FIGHT_CLOSE_PLAYER_END_TIME = 20;

	public static Logger logger = LoggerFactory.getLogger(CountryManager.class.getName());

	public static final byte 中立 = 0;
	public static final byte 国家A = 1;
	public static final byte 国家B = 2;
	public static final byte 国家C = 3;

	public static final byte[] 国BYTE = { 国家A, 国家B, 国家C };

	public static final String 国家A名字 = Translate.国家A名字;
	public static final String 国家B名字 = Translate.国家B名字;
	public static final String 国家C名字 = Translate.国家C名字;

	// 0阶
	public static final int 国王 = 1;
	// 1阶
	public static final int 大司马 = 2;
	public static final int 大将军 = 3;
	public static final int 元帅 = 4;
	public static final int 宰相 = 5;
	// 2阶
	public static final int 巡捕_国王 = 6;
	public static final int 巡捕_元帅 = 7;
	public static final int 御史大夫_国王 = 8;
	public static final int 御史大夫_宰相 = 9;
	// 3阶
	public static final int 护国公 = 10;
	public static final int 辅国公 = 11;
	public static final int 御林卫队 = 12;
	public static final int[] 体力活动额外经验收益加成 = new int[] { 0, 20, 16, 16, 12, 12, 8, 8, 8, 8, 4, 4, 4 };// 下标与上面对应

	public static int 得到官阶(int 职位) {
		if (职位 == 国王) {
			return 0;
		} else if (职位 == 大司马 || 职位 == 大将军 || 职位 == 元帅 || 职位 == 宰相) {
			return 1;
		} else if (职位 == 巡捕_国王 || 职位 == 巡捕_元帅 || 职位 == 御史大夫_国王 || 职位 == 御史大夫_宰相) {
			return 2;
		} else if (职位 == 护国公 || 职位 == 辅国公 || 职位 == 御林卫队) {
			return 3;
		}
		return 4;
	}

	public int date;
	public static final String[] 官职名称 = new String[] { Translate.无官职, Translate.国王, Translate.大司马, Translate.大将军, Translate.元帅, Translate.宰相, Translate.巡捕_国王, Translate.巡捕_元帅, Translate.御史大夫_国王, Translate.御史大夫_宰相, Translate.护国公, Translate.辅国公, Translate.御林卫队 };

	public static String 得到官职名(int 官职) {
		if (官职 >= 0 && 官职 < 官职名称.length) {
			return 官职名称[官职];
		}
		return null;
	}

	public static final String[] 国家名称 = new String[] { 国家A名字, 国家B名字, 国家C名字 };

	public static String 得到国家名(int 国家id) {
		if (国家id > 0 && 国家id <= 国家名称.length) {
			return 国家名称[国家id - 1];
		}
		return Translate.中立;
	}

	public static final int[] 拥有托运权利的官员 = new int[] { 国王, 大司马, 大将军, 元帅, 宰相, 巡捕_国王, 巡捕_元帅, 御史大夫_国王, 御史大夫_宰相, 御林卫队 };
	public static final int[] 拥有发布国战权利的官员 = new int[] { 国王, 元帅, 大将军 };
	public static final int[] 拥有发布反击战权利的官员 = new int[] { 国王, 元帅, };
	public static final int[] 拥有发布国运权利的官员 = new int[] { 国王, 宰相, 大司马, 御史大夫_国王, 御史大夫_宰相 };
	public static final int[] 拥有发布国探权利的官员 = new int[] { 国王, 宰相, 大司马, 御史大夫_国王, 御史大夫_宰相 };
	public static final int[] 拥有发布领地争夺战权利的官员 = new int[] { 大将军 };
	public static final int[] 拥有使用王者之令权利的官员 = new int[] { 国王, 大司马, 大将军, 元帅, 宰相, 巡捕_国王, 巡捕_元帅, 御史大夫_国王, 御史大夫_宰相 };
	public static final int[] 拥有禁言权利的官员 = new int[] { 国王, 宰相, 御史大夫_国王, 御史大夫_宰相 };
	public static final int[] 拥有囚禁权利的官员 = new int[] { 国王, 宰相, 御史大夫_国王, 御史大夫_宰相 };
	public static final int[] 拥有使用召集令权力的官员 = new int[] { 国王, 大司马, 大将军 };
	public static final int[] 拥有使用召集NPC权力的官员 = new int[] { 大将军, 元帅 };

	public static final String 囚禁地图名称 = Translate.囚禁地图名;
	public static final int 囚禁X坐标 = 936;
	public static final int 囚禁Y坐标 = 636;
	public static final String 囚禁buff名称 = Translate.囚禁buff名称;
	public static final String 释放地图名称 = Translate.释放地图名;
	public static final String 国王令传送状态 = Translate.国王令传送状态;
	public static final int 释放X坐标 = 1660;
	public static final int 释放Y坐标 = 1026;
	public static String 禁言buff名称 = Translate.禁言buff名称;
	Random random = new Random();
	public static int 每天支取上限 = 1000;
	public static int 每天国运最早开始时间 = 1080;// 分钟
	public static int 每天国运最晚开始时间 = 1140;// 分钟
	public static int 每天国探最早开始时间 = 750;// 分钟
	public static int 每天国探最晚开始时间 = 810;// 分钟
	public static long 国运时长 = 5400000;
	public static long 国探时长 = 3600000;
	public static int 每天竞标开始时间 = 12;
	public static int 每天竞标结束时间 = 23;
	public static double 竞标剩余资金率 = 0.97;
	public static long 竞标最低资金 = 10000000;
	public static int 镖车与玩家的距离最大值 = 300;
	public static double 国王获得镖局利润率 = 0.3;

	public static int 国家召集每次召集的人数 = 100;
	public static int 国王囚禁次数 = 20;
	public static int 御林卫队囚禁次数 = 1;
	public static int 其他官员囚禁次数 = 2;
	public static int 基础俸禄 = 100;
	public static int 发放俸禄资金下限 = 1000000;
	public static long 禁用霸体和御卫buff时长 = 10 * 60 * 1000;
	public static int 国王使用技能拉人次数 = 10;
	public static String 禁用国王专用技能 = Translate.禁用国王专用技能;
	public static String 免疫 = Translate.免疫;
	public static long 免疫时长 = 90 * 1000;
	public static String 加血 = Translate.加血百分比;
	public static long 加血时长 = 15 * 1000;
	public static String 国王专用技能1 = Translate.王者霸气;
	public static String 国王专用技能2 = Translate.御卫术;

	/**
	 * 每个国家官员信息，key为国家编号
	 */
	public Hashtable<Byte, Country> countryMap = new Hashtable<Byte, Country>();

	private static CountryManager self;

	/**
	 * 可以使用王者之印的地图的坐标点文件
	 */
	public String mapWangZheTransferPointfile = "";

	public String getMapWangZheTransferPointfile() {
		return mapWangZheTransferPointfile;
	}

	public void setMapWangZheTransferPointfile(String mapWangZheTransferPointfile) {
		this.mapWangZheTransferPointfile = mapWangZheTransferPointfile;
	}

	public HashMap<String, WangZheTransferPointOnMap> englishMapNameTransferPoints = new HashMap<String, WangZheTransferPointOnMap>();
	public HashMap<Integer,HashMap<String, WangZheTransferPointOnMap>> englishMapNameTransferPoints2 = new HashMap<Integer, HashMap<String,WangZheTransferPointOnMap>>();
	public HashMap<String, WangZheTransferPointOnMap> chinaMapNameTransferPoints = new HashMap<String, WangZheTransferPointOnMap>();
	public HashMap<Integer,HashMap<String, WangZheTransferPointOnMap>> chinaMapNameTransferPoints2 = new HashMap<Integer, HashMap<String,WangZheTransferPointOnMap>>();
	public static final int 地图传送区域sheet = 0;

	public static final int 地图传送区域_mapName_所在列 = 0;
	public static final int 地图传送区域_传送位置横纵坐标及范围半径传送位置名字_所在列 = 1;

	public void init() throws Exception {
		
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Country.class);
		emm = SimpleEntityManagerFactory.getSimpleEntityManager(CountryManagerData.class);
		self = this;
		// 判断文件不存在就新产生国家，如果文件存在证明已经有国家了
		long count = 0;

		count = em.count();

		if (count == 0) {
			Country country = new Country();
			long id = em.nextId();
			country.setId(id);
			country.setCountryId(国家A);
			country.setName(国家A名字);
			em.save(country);
			countryMap.put(country.getCountryId(), country);
			country = new Country();
			id = em.nextId();
			country.setId(id);
			country.setCountryId(国家B);
			country.setName(国家B名字);
			em.save(country);
			countryMap.put(country.getCountryId(), country);
			country = new Country();
			id = em.nextId();
			country.setId(id);
			country.setCountryId(国家C);
			country.setName(国家C名字);
			em.save(country);
			countryMap.put(country.getCountryId(), country);
			data = new CountryManagerData();
			id = emm.nextId();
			data.setId(id);
			emm.save(data);
		} else {
			long[] ids = em.queryIds(Country.class, "");
			for (int i = 0; i < ids.length; i++) {
				long id = ids[i];
				Country country = em.find(id);
				countryMap.put(country.getCountryId(), country);
			}
			long[] cmdIds = emm.queryIds(CountryManagerData.class, "");
			if (cmdIds != null && cmdIds.length > 0) {
				data = emm.find(cmdIds[cmdIds.length - 1]);
			}
			if (data == null) {
				data = new CountryManagerData();
				long id = emm.nextId();
				data.setId(id);
				emm.save(data);
			}
		}
		loadFromInputStream(new FileInputStream(new File(mapWangZheTransferPointfile)));
		Thread thread = new Thread(this, "CountryManager");
		thread.start();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		int date = calendar.get(Calendar.DATE);
		this.date = date;
		ServiceStartRecord.startLog(this);
	}

	public static CountryManager getInstance() {
		return self;
	}

	/**
	 * 虚拟机关闭时调用，在spring中配置
	 */
	public void destroy() {
		save();
		em.destroy();
		emm.destroy();
	}

	public void save() {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (countryMap != null) {
			for (Country country : countryMap.values()) {
				if (country != null) {
					if (country.isDirty()) {
						if (country.getLastUpdateTime() == 0) {
							country.setLastUpdateTime(now);
							try {
								em.flush(country);
								country.setDirty(false);
								if (logger.isInfoEnabled()) {
									// logger.info("[保存新国家] ["+country.getId()+"] ["+country.getName()+"]");
									if (logger.isInfoEnabled()) logger.info("[保存新国家] [{}] [{}]", new Object[] { country.getCountryId(), country.getName() });
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								notSaveSuccessCountryMap.put(country.getCountryId(), country);
								logger.error("[保存新国家失败再次保存] [" + country.getCountryId() + "] [" + country.getName() + "]", ex);
							}
						} else {
							try {
								country.setLastUpdateTime(now);
								em.flush(country);
								country.setDirty(false);
								if (logger.isInfoEnabled()) {
									// logger.info("[更新国家] ["+country.getId()+"] ["+country.getName()+"]");
									if (logger.isInfoEnabled()) logger.info("[更新国家] [{}] [{}]", new Object[] { country.getCountryId(), country.getName() });
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								notUpdateSuccessCountryMap.put(country.getCountryId(), country);
								logger.error("[更新国家失败再次更新] [" + country.getCountryId() + "] [" + country.getName() + "]", ex);
							}
						}
					}
				} else {
					logger.error("国家为空");
				}
			}
			if (!notSaveSuccessCountryMap.isEmpty()) {
				try {
					for (Byte countryId : notSaveSuccessCountryMap.keySet()) {
						Country c = notSaveSuccessCountryMap.get(countryId);
						if (c != null) {
							em.flush(c);
							c.setDirty(false);
						}
					}
					notSaveSuccessCountryMap.clear();
					if (logger.isInfoEnabled()) {
						logger.info("[再次保存新国家]");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error("[保存新国家失败再次保存依然失败]", ex);
				}
			}
			if (!notUpdateSuccessCountryMap.isEmpty()) {
				try {
					for (Byte countryId : notUpdateSuccessCountryMap.keySet()) {
						Country c = notUpdateSuccessCountryMap.get(countryId);
						if (c != null) {
							em.flush(c);
							c.setDirty(false);
						}
					}
					notUpdateSuccessCountryMap.clear();
					if (logger.isInfoEnabled()) {
						logger.info("[再次更新国家]");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error("[更新国家失败再次更新依然失败]", ex);
				}
			}
		}
		if (data != null && data.isDirty()) {
			try {
				emm.flush(data);
				data.setDirty(false);
				if (logger.isInfoEnabled()) {
					// logger.info("[系统关闭更新国家管理器数据] ["+data.info()+"]");
					if (logger.isInfoEnabled()) logger.info("[系统关闭更新国家管理器数据] [{}]", new Object[] { data.info() });
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("[系统关闭更新国家管理器数据异常采用保存新国家管理器数据操作]", e);
				try {
					emm.flush(data);
					data.setDirty(false);
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error("[系统关闭保存新国家管理器数据异常]", ex);
				}
			}
		}
	}

	public void 官员申请托运(Player player) {
		String result = 托运合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.托运);
		mw.setDescriptionInUUB(Translate.请选择要托运到的地点);

		Option_Country_tuoyun_chuansong options[] = new Option_Country_tuoyun_chuansong[3];
		options[0] = new Option_Country_tuoyun_chuansong();
		options[0].setText(Translate.托运地点);

		options[1] = new Option_Country_tuoyun_chuansong();
		options[1].setText(Translate.托运地点);

		options[2] = new Option_Country_tuoyun_chuansong();
		options[2].setText(Translate.托运地点);

		mw.setOptions(options);

		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
	}

	public void 官员托运(Game game, Player player, Option_Country_tuoyun_chuansong chuansong) {
		String result = 托运合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		// 托运到指定的位置
		TransportData transportData = new TransportData(0, 0, 0, 0, chuansong.getTargetMapName(), chuansong.getTargetMapX(), chuansong.getTargetMapY());
		Game newGame = GameManager.getInstance().getGameByName(chuansong.getTargetMapName(), player.getCountry());
		if (newGame != null) {
			// 首先清除本地图的镖车
			BiaoCheNpc 镖车 = player.得到玩家镖车();
			if (镖车 != null) {
				game.removeSprite(镖车);

				// 玩家跳转地图
				game.transferGame(player, transportData);
				镖车.setX(player.getX());
				镖车.setY(player.getY());
				newGame.addSprite(镖车);
				int count = 1;
				if (data.已托运次数Map.get(player.getId()) != null) {
					count = count + data.已托运次数Map.get(player.getId());
				}
				data.已托运次数Map.put(player.getId(), count);
				data.setDirty(true);
			}
		}

	}

	/**
	 * 托运合法性判断
	 * 返回值为空代表可以托运，返回值不为空代表不能托运，返回值为不能托运原因
	 * @param player
	 * @return
	 */
	public String 托运合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		long playerId = player.getId();
		if (!国家官员合法性判断(player.getCountry(), playerId)) {
			return Translate.权限不足请成为国家官员后再使用此功能;
		}
		Country country = countryMap.get(player.getCountry());
		long time = country.成为国家官员时间Map.get(playerId);
		if ((com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - time) / 24 / 3600 / 1000 <= 0) {
			return Translate.成为国家官员一天后才能用此功能;
		}
		int count = 0;
		if (data.已托运次数Map.get(playerId) != null) {
			count = data.已托运次数Map.get(playerId);
		}
		if (count >= 每天官职能托运的次数(官职(player.getCountry(), playerId))) {
			return Translate.translateString(Translate.今天使用次数已经用光, new String[][] { { Translate.COUNT_1, 每天官职能托运的次数(官职(player.getCountry(), playerId)) + "" } });
		}
		if (!player.isYunbiao()) {
			return Translate.只有接了镖车才能使用此功能;
		}
		return null;
	}

	public boolean 国家官员合法性判断(byte countryIndex, long playerId) {
		boolean ok = false;
		if (countryMap != null && countryMap.get(countryIndex) != null) {
			Country country = countryMap.get(countryIndex);
			if (country != null) {
				if (country.getKingId() == playerId) {
					ok = true;
				} else if (country.getDasimaId() == playerId) {
					ok = true;
				} else if (country.getMarshalId() == playerId) {
					ok = true;
				} else if (country.getPoliceByKingId() == playerId) {
					ok = true;
				} else if (country.getPoliceByMarshalId() == playerId) {
					ok = true;
				} else if (country.getPrimeMinisterId() == playerId) {
					ok = true;
				} else if (country.getSeniorGeneralId() == playerId) {
					ok = true;
				} else if (country.getYushidafuByKingId() == playerId) {
					ok = true;
				} else if (country.getYushidafuByPrimeMinisterId() == playerId) {
					ok = true;
				} else {
					long[] ids = country.getYulinjunIds();
					if (ids != null) {
						for (long id : ids) {
							if (id == playerId) {
								return true;
							}
						}
					}
				}
			}
		}
		return ok;
	}

	public int 官职(byte countryIndex, long playerId) {
		int 官职 = -1;
		if (countryMap != null && countryMap.get(countryIndex) != null) {
			Country country = countryMap.get(countryIndex);
			if (country != null) {
				if (country.getKingId() == playerId) {
					官职 = 国王;
				} else if (country.getDasimaId() == playerId) {
					官职 = 大司马;
				} else if (country.getMarshalId() == playerId) {
					官职 = 元帅;
				} else if (country.getPoliceByKingId() == playerId) {
					官职 = 巡捕_国王;
				} else if (country.getPoliceByMarshalId() == playerId) {
					官职 = 巡捕_元帅;
				} else if (country.getPrimeMinisterId() == playerId) {
					官职 = 宰相;
				} else if (country.getSeniorGeneralId() == playerId) {
					官职 = 大将军;
				} else if (country.getHuguogongId() == playerId) {
					官职 = 护国公;
				} else if (country.getFuguogongId() == playerId) {
					官职 = 辅国公;
				} else if (country.getYushidafuByKingId() == playerId) {
					官职 = 御史大夫_国王;
				} else if (country.getYushidafuByPrimeMinisterId() == playerId) {
					官职 = 御史大夫_宰相;
				} else {
					long[] ids = country.getYulinjunIds();
					if (ids != null) {
						for (long id : ids) {
							if (id == playerId) {
								return 御林卫队;
							}
						}
					}
				}
			} else {
				// logger.error("[country == null] ["+countryIndex+"] ["+playerId+"]");
				logger.error("[country == null] [{}] [{}]", new Object[] { countryIndex, playerId });
			}
		} else {
			// logger.error("[countryMap == null || countryMap.get(countryIndex) == null] ["+countryIndex+"] ["+playerId+"]");
			logger.error("[countryMap == null || countryMap.get(countryIndex) == null] [{}] [{}]", new Object[] { countryIndex, playerId });
		}
		return 官职;
	}

	/**
	 * 体力活动经验官员额外收益
	 * @param player
	 * @param exp
	 *            原本应加的经验
	 * @param reason
	 */
	public void addExtraExp(Player player, long exp) {
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 > 0) {
			long extraExp = exp * 体力活动额外经验收益加成[官职] / 100;
			if (extraExp > 0) {
				player.addExp(extraExp, ExperienceManager.官员体力活动加成);
			}
		}
	}

	public int 每天官职能托运的次数(int 官职编号) {
		if (官职编号 >= 国王 && 官职编号 <= 御林卫队) {
			if (官职编号 == 国王) {
				return 4;
			} else if (官职编号 == 御林卫队) {
				return 1;
			} else {
				return 2;
			}
		}
		return 0;
	}

	public void 官员申请囚禁(Player player) {
		String result = 囚禁合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		int count = 0;
		String s = player.getCountry() + "-" + 官职(player.getCountry(), player.getId());
		if (data.已囚禁次数Map.get(s) != null) {
			count = data.已囚禁次数Map.get(s);
		}
		int hasCount = 每天官职能囚禁的次数(官职(player.getCountry(), player.getId())) - count;
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.囚禁 + " " + Translate.translateString(Translate.您还能囚禁次, new String[][] { { Translate.COUNT_1, hasCount + "" } }));
		mw.setDescriptionInUUB(Translate.请输入囚禁人名称);
		Option_Country_qiujin_byName option = new Option_Country_qiujin_byName();
		option.setText(Translate.囚禁);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancel });
		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, Translate.请输入囚禁人名称, Translate.囚禁, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);

	}

	public void 囚禁(Player player, Player playerB) {
		String result = 囚禁合法性判断2(player, playerB);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			// logger.warn("[囚禁] [失败] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player != null?player.getName():"")+"] ["+(playerB
			// != null?playerB.getUsername():"")+"] ["+(playerB != null?playerB.getId():"")+"] ["+(playerB != null?playerB.getName():"")+"] ["+result+"]");
			if (logger.isWarnEnabled()) logger.warn("[囚禁] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : ""), result });
			return;
		}
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		if (btm != null) {
			// 囚禁到指定的位置
			TransportData transportData = new TransportData(0, 0, 0, 0, 囚禁地图名称, 囚禁X坐标, 囚禁Y坐标);
			Game game = GameManager.getInstance().getGameByName(playerB.getGame(), playerB.getCountry());
			if (game == null) {
				game = GameManager.getInstance().getGameByName(player.getGame(), player.getCountry());
			}
			if (game == null) {
				game = GameManager.getInstance().getGameByName(player.getGame(), 0);
			}
			if (game != null) {
				// 死亡的时候被囚禁
				if (playerB.isDeath()) {
					playerB.setHp(1);
					playerB.setState(Constants.STATE_STAND);
					playerB.notifyRevived();
					PLAYER_REVIVED_RES res = new PLAYER_REVIVED_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, "免费复活", 1, 0);
					playerB.addMessageToRightBag(res);
				}
				game.transferGame(playerB, transportData);
				BuffTemplate buffTemplate = btm.getBuffTemplateByName(囚禁buff名称);
				Buff buff = buffTemplate.createBuff(1);
				buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 3600 * 1000);
				playerB.placeBuff(buff);
				String description = Translate.你被囚禁了;
				try {
					description = Translate.translateString(Translate.你被囚禁了详细, new String[][] { { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, { Translate.PLAYER_NAME_1, player.getName() } });
				} catch (Exception ex) {

				}
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
				playerB.addMessageToRightBag(hreq);
				int count = 1;
				String s = player.getCountry() + "-" + 官职(player.getCountry(), player.getId());
				if (data.已囚禁次数Map.get(s) != null) {
					count += data.已囚禁次数Map.get(s);
				}
				data.已囚禁次数Map.put(s, count);
				data.setDirty(true);
				try {
					ChatMessageService cms = ChatMessageService.getInstance();
					ChatMessage msg = new ChatMessage();
					Country country = this.countryMap.get(player.getCountry());
					// <国家><被囚禁人姓名>被<官职><玩家姓名>囚禁一小时！（世界）
					String descri = Translate.translateString(Translate.某人被某人囚禁, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.PLAYER_NAME_2, playerB.getName() }, { Translate.STRING_2, 得到国家名(player.getCountry()) } });
					msg.setMessageText(descri);
					cms.sendMessageToSystem(msg);
					// logger.info("[囚禁] ["+country.getName()+"] ["+descri+"]");
					if (logger.isInfoEnabled()) logger.info("[囚禁] [{}] [{}]", new Object[] { country.getName(), descri });
				} catch (Exception ex) {

				}

				{
					if (playerB.getQiujinTime() > 0) {
						Calendar calendar = Calendar.getInstance();
						int day = calendar.get(Calendar.DAY_OF_YEAR);
						calendar.setTimeInMillis(playerB.getQiujinTime());
						int day2 = calendar.get(Calendar.DAY_OF_YEAR);
						if (day != day2) {
							playerB.setQiujinCount(0);
						}
					} else {
						playerB.setQiujinCount(0);
					}
					playerB.setQiujinTime(System.currentTimeMillis());
					playerB.setQiujinCount(playerB.getQiujinCount() + 1);
				}

				if (AchievementManager.getInstance() != null) {
					AchievementManager.getInstance().record(playerB, RecordAction.被关监狱次数, 1);
				}
				player.addMessageToRightBag(得到囚禁和禁言的人(null, player));
				if (logger.isWarnEnabled()) logger.warn("[囚禁] [成功] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : "") });
			}
		}
	}

	public String 囚禁合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		long playerId = player.getId();
		int 官职 = 官职(player.getCountry(), playerId);
		boolean has = false;
		for (int 拥有权力官职 : 拥有囚禁权利的官员) {
			if (拥有权力官职 == 官职) {
				has = true;
				break;
			}
		}
		if (!has) {
			return Translate.权限不足不能使用此功能;
		}
		if (player.getCurrentGame() != null && player.getCurrentGame().gi.name.equals(囚禁地图名称)) {
			return Translate.当前不能做此操作;
		}
		int count = 0;
		String s = player.getCountry() + "-" + 官职(player.getCountry(), player.getId());
		if (data.已囚禁次数Map.get(s) != null) {
			count = data.已囚禁次数Map.get(s);
		}
		if (count >= 每天官职能囚禁的次数(官职(player.getCountry(), playerId))) {
			return Translate.translateString(Translate.今天使用次数已经用光, new String[][] { { Translate.COUNT_1, 每天官职能囚禁的次数(官职(player.getCountry(), playerId)) + "" } });
		}

		return null;
	}

	public String 囚禁合法性判断2(Player player, Player playerB) {
		String result = 囚禁合法性判断(player);
		if (result == null) {
			if (playerB == null) {
				return Translate.人不能为空;
			}
			if (player == playerB) {
				return Translate.不能囚禁自己;
			}
			if (player.getCountry() != playerB.getCountry()) {
				return Translate.不能囚禁其他国家的人;
			}
			if (!playerB.isOnline()) {
				return Translate.此人不在线不能囚禁;
			}
			if (TransitRobberyEntityManager.getInstance().isPlayerInRobbery(playerB.getId())) {
				return Translate.此人正在渡劫不能囚禁;
			}
			FairyRobberyManager fins = FairyRobberyManager.inst;
			if (fins != null && fins.isPlayerInRobbery(playerB)) {
				return Translate.此人正在渡劫不能囚禁;
			}
			if (DevilSquareManager.instance.isPlayerInDevilSquare(playerB)) {
				return Translate.副本中不能囚禁;
			}
			if (playerB.isInBattleField()) {
				return Translate.正在战场不能囚禁;
			}
			String gr = GlobalTool.verifyTransByOther(playerB.getId());
			if (gr != null) {
				return Translate.限制地图囚禁;
			}
			if (ChestFightManager.inst.isPlayerInActive(playerB)) {
				return Translate.限制地图囚禁;
			}

			// 对playerB的囚禁操作，用buff形式表示，此buff累计还是已经囚禁的囚犯不让再次囚禁
			List<Buff> buffs = playerB.getAllBuffs();
			if (buffs != null) {
				for (Buff buff : buffs) {
					if (buff != null && buff.getTemplate() != null && 囚禁buff名称.equals(buff.getTemplate().getName())) {
						return result = Translate.此人已经被囚禁了;
					}
				}
			}

			{
				if (playerB.getQiujinTime() > 0) {
					Calendar calendar = Calendar.getInstance();
					int day = calendar.get(Calendar.DAY_OF_YEAR);
					calendar.setTimeInMillis(playerB.getQiujinTime());
					int day2 = calendar.get(Calendar.DAY_OF_YEAR);
					if (day == day2) {
						if (playerB.getQiujinCount() >= 2) {
							return Translate.此人今天已经被囚禁两次; 
						}
					}
				}

			}

			// 限制18点到20点不能使用囚禁
			{
				Calendar calendar = Calendar.getInstance();
				boolean isRightTime = false;
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				if (hour == LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME && minute >= LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME_MINUTE) {
					isRightTime = true;
				}
				if (hour == LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME4 && minute <= LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME_MINUTE) {
					isRightTime = true;
				}
				if (hour == LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME2 || hour == LIMIT_CITY_FIGHT_CLOSE_PLAYER_START_TIME3) {
					isRightTime = true;
				}
				logger.warn("[城战开战前1个小时不能囚禁] [isRightTime:" + isRightTime + "] [hour:" + hour + "] [minute:" + minute + "] [操作人:" + player.getName() + "] [被囚禁人:" + playerB.getName() + "] [是否城战攻防:" + 是否参加城战(playerB) + "] [是否城战防守方:" + 是否是城战防守一方(playerB) + "]");
				if (isRightTime) {
					if (是否参加城战(playerB) || 是否是城战防守一方(playerB)) {
						return Translate.每天18点19点期间不能使用此功能;
					}
				}

				{// 国战限制
					logger.warn("[国战不能囚禁] [isRightTime:" + isRightTime + "] [hour:" + hour + "] [操作人:" + player.getName() + "] [被囚禁人:" + playerB.getName() + "] [国家今天是否打国战:" + GuozhanOrganizer.getInstance().isCountryTodayGuozhan(playerB.getCountry()) + "]");
					if (isRightTime) {
						if (GuozhanOrganizer.getInstance().isCountryTodayGuozhan(playerB.getCountry())) {
							return Translate.每天17点22点期间不能使用此功能;
						}
					}
				}
				{// 抢国战
					if (hour == 23 || hour == 24 || hour == 0 || hour == 1) {
						return Translate.抢国战期间不能关人;
					}
				}
			}

		}
		return result;
	}

	public boolean 是否参加王城城战(Player player2) {
		CityFightManager cfm = CityFightManager.getInstance();
		if (cfm != null) {
			return cfm.是否参加王城城战(player2);
		}
		return false;
	}

	public boolean 是否参加城战(Player player2) {
		CityFightManager cfm = CityFightManager.getInstance();
		if (cfm != null) {
			boolean isFight = false;
			for (String name : CityFightManager.可占领的城市) {
				isFight = cfm.是否参加城战(player2, name);
				if (isFight) {
					break;
				}
			}
			return isFight;
		}
		return false;
	}

	public boolean 是否是城战防守一方(Player player2) {

		CityFightManager cfm = CityFightManager.getInstance();
		if (cfm != null) {
			boolean isFight = false;
			for (String name : CityFightManager.可占领的城市) {
				isFight = cfm.是否是城战防守一方(player2, name);
				if (isFight) {
					break;
				}
			}
			return isFight;
		}
		return false;
	}

	public String getHoldCityMess(String mapName) {
		return CityFightManager.getInstance().得到城战守方信息(mapName);
	}

	public int 每天官职能囚禁的次数(int 官职编号) {
		// if (官职编号 >= 国王 && 官职编号 <= 御林卫队) {
		// if (官职编号 == 国王) {
		// return 国王囚禁次数;
		// } else if (官职编号 == 御林卫队) {
		// return 御林卫队囚禁次数;
		// } else {
		// return 其他官员囚禁次数;
		// }
		// }
		// return 0;
		if (官职编号 == 国王) {
			return 5;
		} else {
			return 1;
		}
	}

	public void 官员申请释放(Player player) {
		String result = 释放合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			if (logger.isWarnEnabled()) logger.warn("[释放申请] [失败] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), result });
			return;
		}
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.释放);
		mw.setDescriptionInUUB(Translate.请选择要释放的人);
		ArrayList<Option> ol = new ArrayList<Option>();
		PlayerManager pm = PlayerManager.getInstance();
		Player[] livingObjects = pm.getOnlinePlayerByCountry(player.getCountry());
		ArrayList<Player> players = new ArrayList<Player>();
		if (livingObjects != null) {
			for (Player lo : livingObjects) {
				if (lo.isInPrison()) {
					players.add(lo);
				}
			}
		}
		for (Player p : players) {
			Option_Country_shifang_byId option = new Option_Country_shifang_byId();
			option.setPlayerId(p.getId());
			option.setText(p.getName());
			ol.add(option);
		}

		if (!ol.isEmpty()) {
			mw.setOptions(ol.toArray(new Option[0]));
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
		} else {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.目前监狱里没有玩家);
			player.addMessageToRightBag(hreq);
		}

		// logger.warn("[释放申请] [成功] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player != null?player.getName():"")+"]");
		if (logger.isWarnEnabled()) logger.warn("[释放申请] [成功] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : "") });
	}

	public void 释放(Player player, Player playerB) {
		String result = 释放合法性判断2(player, playerB);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			// logger.warn("[释放] [失败] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player != null?player.getName():"")+"] ["+(playerB
			// != null?playerB.getUsername():"")+"] ["+(playerB != null?playerB.getId():"")+"] ["+(playerB != null?playerB.getName():"")+"] ["+result+"]");
			if (logger.isWarnEnabled()) logger.warn("[释放] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : ""), result });
			return;
		}
		WindowManager mm = WindowManager.getInstance();
		MenuWindow mw = mm.createTempMenuWindow(600);
		mw.setDescriptionInUUB(Translate.translateString(Translate.确定要释放此人吗, new String[][] { new String[] { Translate.PLAYER_NAME_1, playerB.getName() } }));
		Option_Country_shifang_queren_byId option = new Option_Country_shifang_queren_byId();
		option.setPlayerId(playerB.getId());
		option.setText(Translate.确定);
		Option cancelOption = new Option_UseCancel();
		cancelOption.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancelOption });
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);
	}

	public void 确认释放(Player player, Player playerB) {
		String result = 释放合法性判断2(player, playerB);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			// logger.warn("[释放确认] [失败] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player !=
			// null?player.getName():"")+"] ["+(playerB != null?playerB.getUsername():"")+"] ["+(playerB != null?playerB.getId():"")+"] ["+(playerB !=
			// null?playerB.getName():"")+"] ["+result+"]");
			if (logger.isWarnEnabled()) logger.warn("[释放确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : ""), result });
			return;
		}
		// 对playerB的囚禁操作，用buff形式表示，此buff累计还是已经囚禁的囚犯不让再次囚禁
		Buff qiujinBuff = playerB.getBuffByName(囚禁buff名称);
		if (qiujinBuff != null) {
			qiujinBuff.setInvalidTime(0);
			// 释放到指定的位置
			Game game = GameManager.getInstance().getGameByName(释放地图名称, playerB.getCountry());
			if (game == null) {
				game = GameManager.getInstance().getGameByName(释放地图名称, player.getCountry());
			}
			if (game == null) {
				game = GameManager.getInstance().getGameByName(释放地图名称, 中立);
			}
			TransportData transportData = new TransportData(0, 0, 0, 0, 释放地图名称, 释放X坐标, 释放Y坐标);
			if (game != null) {
				MapArea area = game.gi.getMapAreaByName(Translate.出生点);
				if (area != null) {
					transportData = new TransportData(0, 0, 0, 0, 释放地图名称, (int) (area.getX() + Math.random() * area.getWidth()), (int) (area.getY() + Math.random() * area.getHeight()));
				} else {
					logger.error("[解禁地图出生点不存在] [" + Translate.出生点 + "]");
				}
			} else {
				logger.error("[解禁地图不存在] [" + 释放地图名称 + "]");
			}
			if (game != null) {
				game.transferGame(playerB, transportData);
			}
			try {
				ChatMessageService cms = ChatMessageService.getInstance();
				ChatMessage msg = new ChatMessage();
				Country country = this.countryMap.get(player.getCountry());
				// <国家><被囚禁人姓名>被<官职><玩家姓名>释放出狱。（世界）
				String descri = Translate.translateString(Translate.某人被某人释放, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.PLAYER_NAME_2, playerB.getName() }, { Translate.STRING_2, 得到国家名(player.getCountry()) } });
				msg.setMessageText(descri);
				cms.sendMessageToSystem(msg);
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, descri);
				playerB.addMessageToRightBag(hreq);
				// logger.info("[释放] ["+country.getName()+"] ["+descri+"]");
				if (logger.isInfoEnabled()) logger.info("[释放] [{}] [{}]", new Object[] { country.getName(), descri });

			} catch (Exception ex) {

			}
			player.addMessageToRightBag(得到囚禁和禁言的人(null, player));
			if (logger.isWarnEnabled()) logger.warn("[释放确认] [成功] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : "") });
		}
	}

	public String 释放合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		long playerId = player.getId();
		int 官职 = 官职(player.getCountry(), playerId);
		boolean has = false;
		for (int 拥有权力官职 : 拥有囚禁权利的官员) {
			if (拥有权力官职 == 官职) {
				has = true;
				break;
			}
		}
		if (!has) {
			return Translate.权限不足不能使用此功能;
		}
		return null;
	}

	public String 释放合法性判断2(Player player, Player playerB) {
		String result = 释放合法性判断(player);
		if (result == null) {
			if (playerB == null) {
				return Translate.人不能为空;
			}
			if (player == playerB) {
				return Translate.不能释放自己;
			}
			if (player.getCountry() != playerB.getCountry()) {
				return Translate.不能释放其他国家的人;
			}
			if (!playerB.isOnline()) {
				return Translate.此人不在线不能释放;
			}
			if (!playerB.isInPrison()) {
				return Translate.此人没有被囚禁;
			}
		}
		return result;
	}

	public void 官员申请禁言(Player player) {
		String result = 禁言合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.禁言);
		mw.setDescriptionInUUB(Translate.请输入禁言人名称);
		Option_Country_jinyan_byName option = new Option_Country_jinyan_byName();
		option.setText(Translate.禁言);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancel });
		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, Translate.请输入禁言人名称, Translate.禁言, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);

	}

	public void 禁言(Player player, Player playerB) {
		String result = 禁言合法性判断2(player, playerB);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		// 对playerB的禁言操作，用buff形式表示，此buff累计还是已经禁言的囚犯不让再次禁言
		if (playerB.isJinyan()) {
			result = Translate.此人已经被禁言了;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		if (btm != null) {

			BuffTemplate buffTemplate = btm.getBuffTemplateByName(禁言buff名称);
			if (buffTemplate == null) {
				logger.error("[禁言buff不存在] [" + 禁言buff名称 + "]");
			}
			Buff buff = buffTemplate.createBuff(1);
			buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 3600 * 1000);
			playerB.placeBuff(buff);
			String description = Translate.你被禁言了;
			try {
				description = Translate.translateString(Translate.你被禁言了详细, new String[][] { { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, { Translate.PLAYER_NAME_1, player.getName() } });
			} catch (Exception ex) {

			}
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
			playerB.addMessageToRightBag(hreq);
			try {
				ChatMessageService cms = ChatMessageService.getInstance();
				ChatMessage msg = new ChatMessage();
				Country country = this.countryMap.get(player.getCountry());
				String descri = Translate.translateString(Translate.某人被某人禁言, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.PLAYER_NAME_2, playerB.getName() }, { Translate.STRING_2, 得到国家名(player.getCountry()) } });
				msg.setMessageText(descri);
				cms.sendMessageToSystem(msg);
				// logger.info("[禁言] ["+country.getName()+"] ["+descri+"]");
				if (logger.isInfoEnabled()) logger.info("[禁言] [{}] [{}]", new Object[] { country.getName(), descri });

			} catch (Exception ex) {

			}
			int count = 1;
			String s = player.getCountry() + "-" + 官职(player.getCountry(), player.getId());
			if (data.已禁言次数Map.get(s) != null) {
				count += data.已禁言次数Map.get(s);
			}
			data.已禁言次数Map.put(s, count);
			data.setDirty(true);
			player.addMessageToRightBag(得到囚禁和禁言的人(null, player));
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(playerB, RecordAction.被禁言次数, 1);
			}
		}
	}

	public String 禁言合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		long playerId = player.getId();
		int 官职 = 官职(player.getCountry(), playerId);
		boolean has = false;
		for (int 拥有权力官职 : 拥有禁言权利的官员) {
			if (拥有权力官职 == 官职) {
				has = true;
				break;
			}
		}
		if (!has) {
			return Translate.权限不足不能使用此功能;
		}
		int count = 0;
		String s = player.getCountry() + "-" + 官职(player.getCountry(), player.getId());
		if (data.已禁言次数Map.get(s) != null) {
			count = data.已禁言次数Map.get(s);
		}
		if (count >= 每天官职能禁言的次数(官职(player.getCountry(), playerId))) {
			return Translate.translateString(Translate.今天使用次数已经用光, new String[][] { { Translate.COUNT_1, 每天官职能禁言的次数(官职(player.getCountry(), playerId)) + "" } });
		}
		// 限制18点到20点不能使用禁言
		// {
		// Calendar calendar = Calendar.getInstance();
		// int hour = calendar.get(Calendar.HOUR_OF_DAY);
		// if(hour == 18 || hour == 19){
		// return Translate.每天18点19点期间不能使用此功能;
		// }
		// }
		return null;
	}

	public String 禁言合法性判断2(Player player, Player playerB) {
		String result = 禁言合法性判断(player);
		if (result == null) {
			if (playerB == null) {
				return Translate.人不能为空;
			}
			if (player == playerB) {
				return Translate.不能禁言自己;
			}
			if (player.getCountry() != playerB.getCountry()) {
				return Translate.不能禁言其他国家的人;
			}
			if (!playerB.isOnline()) {
				return Translate.此人不在线不能禁言;
			}
		}
		return result;
	}

	public void 解禁(Player player, Player playerB) {
		String result = 解禁合法性判断2(player, playerB);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			// logger.warn("[释放] [失败] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player != null?player.getName():"")+"] ["+(playerB
			// != null?playerB.getUsername():"")+"] ["+(playerB != null?playerB.getId():"")+"] ["+(playerB != null?playerB.getName():"")+"] ["+result+"]");
			if (logger.isWarnEnabled()) logger.warn("[解禁] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : ""), result });
			return;
		}
		WindowManager mm = WindowManager.getInstance();
		MenuWindow mw = mm.createTempMenuWindow(600);
		mw.setDescriptionInUUB(Translate.translateString(Translate.确定要解禁此人吗, new String[][] { new String[] { Translate.PLAYER_NAME_1, playerB.getName() } }));
		Option_Country_jiejin_queren_byId option = new Option_Country_jiejin_queren_byId();
		option.setPlayerId(playerB.getId());
		option.setText(Translate.确定);
		Option cancelOption = new Option_UseCancel();
		cancelOption.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancelOption });
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);
	}

	public void 确认解禁(Player player, Player playerB) {
		String result = 解禁合法性判断2(player, playerB);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			// logger.warn("[释放确认] [失败] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player !=
			// null?player.getName():"")+"] ["+(playerB != null?playerB.getUsername():"")+"] ["+(playerB != null?playerB.getId():"")+"] ["+(playerB !=
			// null?playerB.getName():"")+"] ["+result+"]");
			if (logger.isWarnEnabled()) logger.warn("[解禁确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : ""), result });
			return;
		}
		// 对playerB的囚禁操作，用buff形式表示，此buff累计还是已经囚禁的囚犯不让再次囚禁
		Buff jinyanBuff = playerB.getBuffByName(禁言buff名称);
		if (jinyanBuff != null) {
			jinyanBuff.setInvalidTime(0);
			try {
				ChatMessageService cms = ChatMessageService.getInstance();
				ChatMessage msg = new ChatMessage();
				Country country = this.countryMap.get(player.getCountry());
				// <国家><被囚禁人姓名>被<官职><玩家姓名>释放出狱。（世界）
				String descri = Translate.translateString(Translate.某人被某人解禁, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.PLAYER_NAME_2, playerB.getName() }, { Translate.STRING_2, 得到国家名(player.getCountry()) } });
				msg.setMessageText(descri);
				cms.sendMessageToSystem(msg);
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, descri);
				playerB.addMessageToRightBag(hreq);
				if (logger.isInfoEnabled()) logger.info("[解禁] [{}] [{}]", new Object[] { country.getName(), descri });

			} catch (Exception ex) {

			}
			player.addMessageToRightBag(得到囚禁和禁言的人(null, player));
			if (logger.isWarnEnabled()) logger.warn("[解禁确认] [成功] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : "") });
		}
	}

	public String 解禁合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		long playerId = player.getId();
		int 官职 = 官职(player.getCountry(), playerId);
		boolean has = false;
		for (int 拥有权力官职 : 拥有禁言权利的官员) {
			if (拥有权力官职 == 官职) {
				has = true;
				break;
			}
		}
		if (!has) {
			return Translate.权限不足不能使用此功能;
		}
		return null;
	}

	public String 解禁合法性判断2(Player player, Player playerB) {
		String result = 解禁合法性判断(player);
		if (result == null) {
			if (playerB == null) {
				return Translate.人不能为空;
			}
			if (player == playerB) {
				return Translate.不能解禁自己;
			}
			if (player.getCountry() != playerB.getCountry()) {
				return Translate.不能解禁其他国家的人;
			}
			if (!playerB.isOnline()) {
				return Translate.此人不在线不能解禁;
			}
			if (!playerB.isJinyan()) {
				return Translate.此人没有被禁言;
			}
		}
		return result;
	}

	public int 每天官职能禁言的次数(int 官职编号) {
		// if (官职编号 >= 国王 && 官职编号 <= 御林卫队) {
		// if (官职编号 == 国王) {
		// return 5;
		// } else if (官职编号 == 御林卫队) {
		// return 1;
		// } else {
		// return 2;
		// }
		// }
		// return 0;
		return 2;
	}

	public void 官员申请辞官(Player player) {
		String result = 辞官合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			// logger.warn("[辞官申请] [失败] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player !=
			// null?player.getName():"")+"] ["+result+"]");
			if (logger.isWarnEnabled()) logger.warn("[辞官申请] [失败] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), result });
			return;
		}
		WindowManager mm = WindowManager.getInstance();
		MenuWindow mw = mm.createTempMenuWindow(600);
		mw.setDescriptionInUUB(Translate.确定要辞去官职吗);
		Option_Country_ciguan_queren_byId option = new Option_Country_ciguan_queren_byId();
		option.setPlayerId(player.getId());
		option.setText(Translate.确定);
		Option cancelOption = new Option_UseCancel();
		cancelOption.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancelOption });

		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);
		// logger.warn("[辞官申请] [成功] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player !=
		// null?player.getName():"")+"] ["+result+"]");
		if (logger.isWarnEnabled()) logger.warn("[辞官申请] [成功] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), result });
	}

	public void 官员确定辞官(Player player) {
		String result = 辞官合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			// logger.warn("[辞官确认] [失败] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player !=
			// null?player.getName():"")+"] ["+result+"]");
			if (logger.isWarnEnabled()) logger.warn("[辞官确认] [失败] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), result });
			return;
		}
		String 官职名 = 得到官职名(官职(player.getCountry(), player.getId()));
		boolean success = 国家官员辞官(player.getCountry(), player.getId());
		if (success) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您已经成功辞去官职);
			player.addMessageToRightBag(hreq);
			player.setCountryPosition(0);
			撤销授勋表彰(player);
			try {
				ChatMessageService cms = ChatMessageService.getInstance();
				ChatMessage msg = new ChatMessage();
				Country country = this.countryMap.get(player.getCountry());
				String descri = Translate.translateString(Translate.辞官详细, new String[][] { new String[] { Translate.STRING_1, 官职名 }, new String[] { Translate.PLAYER_NAME_1, player.getName() } });
				msg.setMessageText(descri);
				cms.sendMessageToCountry(country.getCountryId(), msg);
				// logger.info("[禁言] ["+country.getName()+"] ["+descri+"]");
				if (logger.isInfoEnabled()) logger.info("[禁言] [{}] [{}]", new Object[] { country.getName(), descri });

			} catch (Exception ex) {

			}
			// logger.warn("[辞官确认] [成功] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player != null?player.getName():"")+"]");
			if (logger.isWarnEnabled()) logger.warn("[辞官确认] [成功] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : "") });
		}
	}

	public boolean 国家官员辞官(byte countryIndex, long playerId) {
		boolean ok = false;
		if (countryMap != null && countryMap.get(countryIndex) != null) {
			Country country = countryMap.get(countryIndex);
			if (country != null) {
				if (country.getKingId() == playerId) {
					country.setKingId(-1);
					ok = true;
				} else if (country.getDasimaId() == playerId) {
					country.setDasimaId(-1);
					ok = true;
				} else if (country.getMarshalId() == playerId) {
					country.setMarshalId(-1);
					ok = true;
				} else if (country.getPoliceByKingId() == playerId) {
					country.setPoliceByKingId(-1);
					ok = true;
				} else if (country.getPoliceByMarshalId() == playerId) {
					country.setPoliceByMarshalId(-1);
					ok = true;
				} else if (country.getPrimeMinisterId() == playerId) {
					country.setPrimeMinisterId(-1);
					ok = true;
				} else if (country.getSeniorGeneralId() == playerId) {
					country.setSeniorGeneralId(-1);
					ok = true;
				} else if (country.getYushidafuByKingId() == playerId) {
					country.setYushidafuByKingId(-1);
					ok = true;
				} else if (country.getYushidafuByPrimeMinisterId() == playerId) {
					country.setYushidafuByPrimeMinisterId(-1);
					ok = true;
				} else {
					long[] ids = country.getYulinjunIds();
					if (ids != null) {
						for (int i = 0; i < ids.length; i++) {
							if (ids[i] == playerId) {
								ids[i] = -1;
								ok = true;
							}
						}
					}
				}
				country.成为国家官员时间Map.remove(playerId);
				country.setDirty(true);
			}
		}
		return ok;
	}

	public String 辞官合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		long playerId = player.getId();
		if (!国家官员合法性判断(player.getCountry(), playerId)) {
			return Translate.权限不足请成为国家官员后再使用此功能;
		}
		if (官职(player.getCountry(), playerId) == 国王) {
			return Translate.国王大人您要辞官的话会引起混乱的;
		}
		return null;
	}

	public void 撤销授勋表彰(Player player) {
		Country country = this.countryMap.get(player.getCountry());
		long ids[] = country.getBiaozhangIds();
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (ids[i] == player.getId()) {
					ids[i] = -1;
				}
			}
		}
		ids = country.getShouxunIds();
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (ids[i] == player.getId()) {
					ids[i] = -1;
				}
			}
		}
		if (country.成为表彰官员时间Map.contains(player.getId())) {
			country.成为表彰官员时间Map.remove(player.getId());
		}
		if (country.成为授勋官员时间Map.contains(player.getId())) {
			country.成为授勋官员时间Map.remove(player.getId());
		}
		country.setDirty(true);
	}

	public void 任命申请(Player player, int 授予官职) {
		String result = 任命合法性判断(player, 授予官职);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			if (logger.isWarnEnabled()) logger.warn("[任命申请] [失败] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), 授予官职, result });
			return;
		}
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.任命);
		mw.setDescriptionInUUB(Translate.请输入任命人名称);
		Option_Country_renming_byName option = new Option_Country_renming_byName();
		option.setText(Translate.任命);
		option.setGuanzhi(授予官职);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancel });
		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, Translate.请输入任命人名称, Translate.任命, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);
		if (logger.isWarnEnabled()) logger.warn("[任命申请] [成功] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), 授予官职, result });
	}

	public void 任命弹框提示(Player player, Player playerB, int 授予官职) {
		String result = 任命合法性判断2(player, playerB, 授予官职);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			if (logger.isWarnEnabled()) logger.warn("[任命弹框提示] [失败] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), 授予官职, result });
			return;
		}
		int 官职B = 官职(playerB.getCountry(), playerB.getId());
		WindowManager mm = WindowManager.getInstance();
		MenuWindow mw = mm.createTempMenuWindow(600);
		String descriptioin = Translate.任命;
		try {
			String 授予官职名 = 得到官职名(授予官职);
			if (官职B >= 0) {
				String 官职B名 = 得到官职名(官职B);
				descriptioin = Translate.translateString(Translate.确认更改职位吗, new String[][] { new String[] { Translate.PLAYER_NAME_1, playerB.getName() }, new String[] { Translate.STRING_1, 官职B名 }, new String[] { Translate.STRING_2, 授予官职名 } });
			} else {
				descriptioin = Translate.translateString(Translate.确认任命吗, new String[][] { new String[] { Translate.PLAYER_NAME_1, playerB.getName() }, new String[] { Translate.STRING_1, 授予官职名 } });
			}
		} catch (Exception ex) {

		}
		mw.setDescriptionInUUB(descriptioin);
		Option_Country_renming_queren_byId option = new Option_Country_renming_queren_byId();
		option.setPlayerId(playerB.getId());
		option.setGuanzhi(授予官职);
		option.setText(Translate.确定);
		Option cancelOption = new Option_UseCancel();
		cancelOption.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancelOption });
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);
		if (logger.isWarnEnabled()) logger.warn("[任命弹框提示] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : ""), 授予官职, result });
	}

	public void 任命确认(Player player, Player playerB, int 授予官职) {
		String result = 任命合法性判断2(player, playerB, 授予官职);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			if (logger.isWarnEnabled()) logger.warn("[任命确认] [失败] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : ""), 授予官职, result });
			return;
		}
		int 官职B = 官职(playerB.getCountry(), playerB.getId());
		Country country = countryMap.get(playerB.getCountry());
		if (官职B >= 0) {
			if (官职B == 巡捕_国王) {
				country.setPoliceByKingId(-1);
			}
			if (官职B == 巡捕_元帅) {
				country.setPoliceByMarshalId(-1);
			}
			if (官职B == 御史大夫_国王) {
				country.setYushidafuByKingId(-1);
			}
			if (官职B == 御史大夫_宰相) {
				country.setYushidafuByPrimeMinisterId(-1);
			}
			if (官职B == 大司马) {
				long id = country.getDasimaId();
				if (id == playerB.getId()) {
					country.setDasimaId(-1);
				}
			}
			if (官职B == 大将军) {
				long id = country.getSeniorGeneralId();
				if (id == playerB.getId()) {
					country.setSeniorGeneralId(-1);
				}
			}
			if (官职B == 元帅) {
				long id = country.getMarshalId();
				if (id == playerB.getId()) {
					country.setMarshalId(-1);
				}
			}
			if (官职B == 宰相) {
				long id = country.getPrimeMinisterId();
				if (id == playerB.getId()) {
					country.setPrimeMinisterId(-1);
				}
			}
			if (官职B == 护国公) {
				long id = country.getHuguogongId();
				if (id == playerB.getId()) {
					country.setHuguogongId(-1);
				}
			}
			if (官职B == 辅国公) {
				long id = country.getFuguogongId();
				if (id == playerB.getId()) {
					country.setFuguogongId(-1);
				}
			}
			if (官职B == 御林卫队) {
				long[] ids = country.getYulinjunIds();
				if (ids != null) {
					for (int i = 0; i < ids.length; i++) {
						if (ids[i] == playerB.getId()) {
							ids[i] = -1;
							country.setYulinjunIds(ids);
							break;
						}
					}
				}
			}
			任命官职保存(country, player, playerB, 授予官职);
			playerB.setCountryPosition(授予官职);
		} else {
			任命官职保存(country, player, playerB, 授予官职);
			country.成为国家官员时间Map.put(playerB.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			country.setDirty(true);
			playerB.setCountryPosition(授予官职);
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		String description = Translate.任命成功;
		try {
			description = Translate.translateString(Translate.你成功任命, new String[][] { new String[] { Translate.PLAYER_NAME_1, playerB.getName() }, new String[] { Translate.STRING_1, 得到官职名(授予官职) } });
		} catch (Exception ex) {

		}
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
		player.addMessageToRightBag(hreq);
		String descriptionB = Translate.任命成功;
		try {
			descriptionB = Translate.translateString(Translate.你被任命, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, new String[] { Translate.STRING_2, 得到官职名(授予官职) } });
		} catch (Exception ex) {

		}
		撤销授勋表彰(playerB);
		HINT_REQ hreqb = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, descriptionB);
		playerB.addMessageToRightBag(hreqb);
		ChatMessageService cms = ChatMessageService.getInstance();
		if (cms != null) {
			try {
				ChatMessage msg = new ChatMessage();
				// <玩家姓名>被<官职><玩家姓名>任命为<任命官职>！(国家)
				String descri = Translate.translateString(Translate.某人被某人任命, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.PLAYER_NAME_2, playerB.getName() }, new String[] { Translate.STRING_2, 得到官职名(授予官职) } });
				msg.setMessageText(descri);
				cms.sendMessageToCountry(country.getCountryId(), msg);
				if (logger.isInfoEnabled()) logger.info("[任命] [{}] [{}]", new Object[] { country.getName(), descri });
			} catch (Exception ex) {

			}
		}
		if (logger.isWarnEnabled()) logger.warn("[任命确认] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : ""), 授予官职, description, descriptionB });
	}

	public boolean 任命官职保存(Country country, Player player, Player playerB, int 授予官职) {
		long playerId = player.getId();
		int 官职 = 官职(player.getCountry(), playerId);
		if (官职 == 元帅) {
			long id = country.getPoliceByMarshalId();
			if (id > 0) {
				Player p = getPlayer(id);
				if (p != null) {
					return false;
				}
			}
			country.setPoliceByMarshalId(playerB.getId());
		}
		if (官职 == 宰相) {
			long id = country.getYushidafuByPrimeMinisterId();
			if (id > 0) {
				Player p = getPlayer(id);
				if (p != null) {
					return false;
				}
			}
			country.setYushidafuByPrimeMinisterId(playerB.getId());
		}
		if (官职 == 国王) {
			if (授予官职 == 巡捕_国王) {
				long id = country.getPoliceByKingId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return false;
					}
				}
				country.setPoliceByKingId(playerB.getId());
			}
			if (授予官职 == 御史大夫_国王) {
				long id = country.getYushidafuByKingId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return false;
					}
				}
				country.setYushidafuByKingId(playerB.getId());
			}
			if (授予官职 == 巡捕_元帅) {
				long id = country.getPoliceByMarshalId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return false;
					}
				}
				country.setPoliceByMarshalId(playerB.getId());
			}
			if (授予官职 == 御史大夫_宰相) {
				long id = country.getYushidafuByPrimeMinisterId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return false;
					}
				}
				country.setYushidafuByPrimeMinisterId(playerB.getId());
			}
			if (授予官职 == 大司马) {
				long id = country.getDasimaId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return false;
					}
				}
				country.setDasimaId(playerB.getId());
			}
			if (授予官职 == 大将军) {
				long id = country.getSeniorGeneralId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return false;
					}
				}
				country.setSeniorGeneralId(playerB.getId());
			}
			if (授予官职 == 元帅) {
				long id = country.getMarshalId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return false;
					}
				}
				country.setMarshalId(playerB.getId());
			}
			if (授予官职 == 宰相) {
				long id = country.getPrimeMinisterId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return false;
					}
				}
				country.setPrimeMinisterId(playerB.getId());
			}
			if (授予官职 == 辅国公) {
				long id = country.getFuguogongId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return false;
					}
				}
				country.setFuguogongId(playerB.getId());
			}
			if (授予官职 == 护国公) {
				long id = country.getHuguogongId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return false;
					}
				}
				country.setHuguogongId(playerB.getId());
			}
			if (授予官职 == 御林卫队) {
				long[] ids = country.getYulinjunIds();
				if (ids != null) {
					boolean full = true;
					for (int i = 0; i < ids.length; i++) {
						if (ids[i] <= 0) {
							ids[i] = playerB.getId();
							full = false;
							country.setYulinjunIds(ids);
							break;
						} else {
							Player p = getPlayer(ids[i]);
							if (p == null) {
								ids[i] = playerB.getId();
								full = false;
								country.setYulinjunIds(ids);
								break;
							}
						}
					}
					if (full) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public Player getPlayer(long id) {
		PlayerManager pm = PlayerManager.getInstance();
		try {
			return pm.getPlayer(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String 任命合法性判断(Player player, int 授予官职) {
		if (player == null) {
			return Translate.人不能为空;
		}
		long playerId = player.getId();
		int 官职 = 官职(player.getCountry(), playerId);
		String result = 国家可任命罢免官员合法性判断(player.getCountry(), playerId, 官职, 授予官职);
		if (result != null) {
			return result;
		}
		Country country = countryMap.get(player.getCountry());
		if (官职 == 元帅) {
			long id = country.getPoliceByMarshalId();
			if (id > 0) {
				Player p = getPlayer(id);
				if (p != null) {
					return Translate.此官职已经满人了;
				}
			}
		}
		if (官职 == 宰相) {
			long id = country.getYushidafuByPrimeMinisterId();
			if (id > 0) {
				Player p = getPlayer(id);
				if (p != null) {
					return Translate.此官职已经满人了;
				}
			}
		}
		if (官职 == 国王) {
			if (授予官职 == 巡捕_国王) {
				long id = country.getPoliceByKingId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return Translate.此官职已经满人了;
					}
				}
			}
			if (授予官职 == 御史大夫_国王) {
				long id = country.getYushidafuByKingId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return Translate.此官职已经满人了;
					}
				}
			}
			if (授予官职 == 巡捕_元帅) {
				long id = country.getPoliceByMarshalId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return Translate.此官职已经满人了;
					}
				}
			}
			if (授予官职 == 御史大夫_宰相) {
				long id = country.getYushidafuByPrimeMinisterId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return Translate.此官职已经满人了;
					}
				}
			}
			if (授予官职 == 大司马) {
				long id = country.getDasimaId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return Translate.此官职已经满人了;
					}
				}
			}
			if (授予官职 == 大将军) {
				long id = country.getSeniorGeneralId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return Translate.此官职已经满人了;
					}
				}
			}
			if (授予官职 == 元帅) {
				long id = country.getMarshalId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return Translate.此官职已经满人了;
					}
				}
			}
			if (授予官职 == 宰相) {
				long id = country.getPrimeMinisterId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return Translate.此官职已经满人了;
					}
				}
			}
			if (授予官职 == 辅国公) {
				long id = country.getFuguogongId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return Translate.此官职已经满人了;
					}
				}
			}
			if (授予官职 == 护国公) {
				long id = country.getHuguogongId();
				if (id > 0) {
					Player p = getPlayer(id);
					if (p != null) {
						return Translate.此官职已经满人了;
					}
				}
			}
			if (授予官职 == 御林卫队) {
				long[] ids = country.getYulinjunIds();
				if (ids != null) {
					boolean full = true;
					for (long id : ids) {
						if (id <= 0) {
							full = false;
							break;
						} else {
							Player p = getPlayer(id);
							if (p == null) {
								full = false;
								break;
							}
						}
					}
					if (full) {
						return Translate.此官职已经满人了;
					}
				}
			}
		}
		return null;
	}

	public String 任命合法性判断2(Player player, Player playerB, int 授予官职) {
		String result = 任命合法性判断(player, 授予官职);
		if (result != null) {
			return result;
		}
		if (player == null || playerB == null) {
			return Translate.人不能为空;
		}
		if (player == playerB) {
			return Translate.不能任免自己;
		}
		if (player.getCountry() != playerB.getCountry()) {
			return Translate.不能任命其他国家的人;
		}
		long playerId = player.getId();
		int 官职 = 官职(player.getCountry(), playerId);
		int 官职B = 官职(playerB.getCountry(), playerB.getId());
		if (官职 != 国王 && 官职B > 0) {
			return Translate.您不能任命其他官员;
		}
		return null;
	}

	public String 国家可任命罢免官员合法性判断(byte countryIndex, long playerId, int 官职, int 授予官职) {
		if (官职 == -1) {
			return Translate.权限不足请成为圣皇元帅宰相后再使用此功能;
		}
		if (授予官职 < 大司马 || 授予官职 > 御林卫队) {
			return Translate.没有此官职;
		}
		if (官职 == 国王) {
			return null;
		}
		if (授予官职 == 巡捕_元帅) {
			if (官职 != 元帅) {
				return Translate.巡捕只能国王和元帅可以任命罢免;
			}
		} else if (授予官职 == 御史大夫_宰相) {
			if (官职 != 宰相) {
				return Translate.御史大夫只能国王和宰相可以任命罢免;
			}
		} else {
			if (官职 == 国王) {
				return null;
			}
			return Translate.此官职只有国王可以任命罢免;
		}
		return null;
	}

	public void 罢免申请(Player player, Player playerB, int 授予官职) {
		String result = 罢免合法性判断(player, playerB, 授予官职);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		WindowManager mm = WindowManager.getInstance();
		MenuWindow mw = mm.createTempMenuWindow(600);
		String descriptioin = Translate.罢免;
		try {
			String 授予官职名 = 得到官职名(授予官职);
			descriptioin = Translate.translateString(Translate.确认罢免吗, new String[][] { new String[] { Translate.PLAYER_NAME_1, playerB.getName() }, new String[] { Translate.STRING_1, 授予官职名 } });
		} catch (Exception ex) {

		}
		mw.setDescriptionInUUB(descriptioin);
		Option_Country_bamian_queren_byId option = new Option_Country_bamian_queren_byId();
		option.setPlayerId(playerB.getId());
		option.setGuanzhi(授予官职);
		option.setText(Translate.确定);
		Option cancelOption = new Option_UseCancel();
		cancelOption.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancelOption });
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
		player.addMessageToRightBag(req);
	}

	public void 罢免确认(Player player, Player playerB, int 授予官职) {
		String result = 罢免合法性判断(player, playerB, 授予官职);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		罢免官职保存(country, player, playerB, 授予官职);
		int 官职 = 官职(player.getCountry(), player.getId());
		String description = Translate.罢免成功;
		playerB.setCountryPosition(0);
		try {
			description = Translate.translateString(Translate.罢免成功详细, new String[][] { new String[] { Translate.PLAYER_NAME_1, playerB.getName() }, new String[] { Translate.STRING_1, 得到官职名(授予官职) } });
		} catch (Exception ex) {

		}
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
		player.addMessageToRightBag(hreq);
		String descriptionB = Translate.你被罢免了;
		try {
			descriptionB = Translate.translateString(Translate.你被罢免了详细, new String[][] { new String[] { Translate.PLAYER_NAME_1, player.getName() }, new String[] { Translate.STRING_1, 得到官职名(官职) } });
		} catch (Exception ex) {

		}
		撤销授勋表彰(playerB);
		HINT_REQ hreqb = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, descriptionB);
		playerB.addMessageToRightBag(hreqb);
		ChatMessageService cms = ChatMessageService.getInstance();
		if (cms != null) {
			try {
				ChatMessage msg = new ChatMessage();
				String descri = Translate.translateString(Translate.某人被某人罢免, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.PLAYER_NAME_2, playerB.getName() }, new String[] { Translate.STRING_2, 得到官职名(授予官职) } });
				msg.setMessageText(descri);
				cms.sendMessageToCountry(country.getCountryId(), msg);
				// logger.info("[罢免] ["+country.getName()+"] ["+descri+"]");
				if (logger.isInfoEnabled()) logger.info("[罢免] [{}] [{}]", new Object[] { country.getName(), descri });
			} catch (Exception ex) {

			}
		}
		// logger.warn("[罢免确认] [成功] ["+(player != null?player.getUsername():"")+"] ["+(player != null?player.getId():"")+"] ["+(player != null?player.getName():"")+"] ["+(playerB
		// != null?playerB.getUsername():"")+"] ["+(playerB != null?playerB.getId():"")+"] ["+(playerB !=
		// null?playerB.getName():"")+"] ["+授予官职+"] ["+description+"] ["+descriptionB+"]");
		if (logger.isWarnEnabled()) logger.warn("[罢免确认] [成功] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), (playerB != null ? playerB.getUsername() : ""), (playerB != null ? playerB.getId() : ""), (playerB != null ? playerB.getName() : ""), 授予官职, description, descriptionB });
	}

	public boolean 罢免官职保存(Country country, Player player, Player playerB, int 授予官职) {
		long playerId = player.getId();
		int 官职 = 官职(player.getCountry(), playerId);
		long idTemp = -1;
		if (官职 == 元帅) {
			long id = country.getPoliceByMarshalId();
			if (id <= 0) {
				return false;
			} else {
				idTemp = country.getPoliceByMarshalId();
				country.setPoliceByMarshalId(-1);
			}
		}
		if (官职 == 宰相) {
			long id = country.getYushidafuByPrimeMinisterId();
			if (id <= 0) {
				return false;
			} else {
				idTemp = country.getYushidafuByPrimeMinisterId();
				country.setYushidafuByPrimeMinisterId(-1);
			}
		}
		if (官职 == 国王) {
			if (授予官职 == 巡捕_国王) {
				long id = country.getPoliceByKingId();
				if (id <= 0) {
					return false;
				} else {
					idTemp = country.getPoliceByKingId();
					country.setPoliceByKingId(-1);
				}
			}
			if (授予官职 == 御史大夫_国王) {
				long id = country.getYushidafuByKingId();
				if (id <= 0) {
					return false;
				} else {
					idTemp = country.getYushidafuByKingId();
					country.setYushidafuByKingId(-1);
				}
			}
			if (授予官职 == 巡捕_元帅) {
				long id = country.getPoliceByMarshalId();
				if (id <= 0) {
					return false;
				} else {
					idTemp = country.getPoliceByMarshalId();
					country.setPoliceByMarshalId(-1);
				}
			}
			if (授予官职 == 御史大夫_宰相) {
				long id = country.getYushidafuByPrimeMinisterId();
				if (id <= 0) {
					return false;
				} else {
					idTemp = country.getYushidafuByPrimeMinisterId();
					country.setYushidafuByPrimeMinisterId(-1);
				}
			}
			if (授予官职 == 大司马) {
				long id = country.getDasimaId();
				if (id <= 0) {
					return false;
				} else {
					idTemp = country.getDasimaId();
					country.setDasimaId(-1);
				}
			}
			if (授予官职 == 大将军) {
				long id = country.getSeniorGeneralId();
				if (id <= 0) {
					return false;
				} else {
					idTemp = country.getSeniorGeneralId();
					country.setSeniorGeneralId(-1);
				}
			}
			if (授予官职 == 元帅) {
				long id = country.getMarshalId();
				if (id <= 0) {
					return false;
				} else {
					idTemp = country.getMarshalId();
					country.setMarshalId(-1);
				}
			}
			if (授予官职 == 宰相) {
				long id = country.getPrimeMinisterId();
				if (id <= 0) {
					return false;
				} else {
					idTemp = country.getPrimeMinisterId();
					country.setPrimeMinisterId(-1);
				}
			}
			if (授予官职 == 护国公) {
				long id = country.getHuguogongId();
				if (id <= 0) {
					return false;
				} else {
					idTemp = country.getHuguogongId();
					country.setHuguogongId(-1);
				}
			}
			if (授予官职 == 辅国公) {
				long id = country.getFuguogongId();
				if (id <= 0) {
					return false;
				} else {
					idTemp = country.getFuguogongId();
					country.setFuguogongId(-1);
				}
			}
			if (授予官职 == 御林卫队) {
				long[] ids = country.getYulinjunIds();
				if (ids != null) {
					boolean full = true;
					for (int i = 0; i < ids.length; i++) {
						if (ids[i] == playerB.getId()) {
							idTemp = playerB.getId();
							ids[i] = -1;
							full = false;
							country.setYulinjunIds(ids);
							break;
						}
					}
					if (full) {
						return false;
					}
				}
			}
			country.成为国家官员时间Map.remove(idTemp);
			country.setDirty(true);
		}
		return true;
	}

	public String 罢免合法性判断(Player player, Player playerB, int 授予官职) {
		if (player == null || playerB == null) {
			return Translate.人不能为空;
		}
		if (player == playerB) {
			return Translate.不能任免自己;
		}
		if (player.getCountry() != playerB.getCountry()) {
			return Translate.不能罢免其他国家的人;
		}
		int 官职B = 官职(playerB.getCountry(), playerB.getId());
		if (官职B != 授予官职) {
			return Translate.请进行正确操作;
		}
		long playerId = player.getId();
		int 官职 = 官职(player.getCountry(), playerId);
		String result = 国家可任命罢免官员合法性判断(player.getCountry(), playerId, 官职, 授予官职);
		if (result != null) {
			return result;
		}
		Country country = countryMap.get(player.getCountry());
		if (官职 == 元帅) {
			long id = country.getPoliceByMarshalId();
			if (id != playerB.getId()) {
				return Translate.此官职没有这个人;
			}
		}
		if (官职 == 宰相) {
			long id = country.getYushidafuByPrimeMinisterId();
			if (id != playerB.getId()) {
				return Translate.此官职没有这个人;
			}
		}
		if (官职 == 国王) {
			if (授予官职 == 巡捕_国王) {
				long id = country.getPoliceByKingId();
				if (id != playerB.getId()) {
					return Translate.此官职没有这个人;
				}
			}
			if (授予官职 == 御史大夫_国王) {
				long id = country.getYushidafuByKingId();
				if (id != playerB.getId()) {
					return Translate.此官职没有这个人;
				}
			}
			if (授予官职 == 巡捕_元帅) {
				long id = country.getPoliceByMarshalId();
				if (id != playerB.getId()) {
					return Translate.此官职没有这个人;
				}
			}
			if (授予官职 == 御史大夫_宰相) {
				long id = country.getYushidafuByPrimeMinisterId();
				if (id != playerB.getId()) {
					return Translate.此官职没有这个人;
				}
			}
			if (授予官职 == 大司马) {
				long id = country.getDasimaId();
				if (id != playerB.getId()) {
					return Translate.此官职没有这个人;
				}
			}
			if (授予官职 == 大将军) {
				long id = country.getSeniorGeneralId();
				if (id != playerB.getId()) {
					return Translate.此官职没有这个人;
				}
			}
			if (授予官职 == 元帅) {
				long id = country.getMarshalId();
				if (id != playerB.getId()) {
					return Translate.此官职没有这个人;
				}
			}
			if (授予官职 == 宰相) {
				long id = country.getPrimeMinisterId();
				if (id != playerB.getId()) {
					return Translate.此官职没有这个人;
				}
			}
			if (授予官职 == 护国公) {
				long id = country.getHuguogongId();
				if (id != playerB.getId()) {
					return Translate.此官职没有这个人;
				}
			}
			if (授予官职 == 辅国公) {
				long id = country.getFuguogongId();
				if (id != playerB.getId()) {
					return Translate.此官职没有这个人;
				}
			}
			if (授予官职 == 御林卫队) {
				long[] ids = country.getYulinjunIds();
				if (ids != null) {
					boolean full = true;
					for (long id : ids) {
						if (id == playerB.getId()) {
							full = false;
							break;
						}
					}
					if (full) {
						return Translate.此官职没有这个人;
					}
				}
			}
		}
		return null;
	}

	public void 授勋(Player player, Player playerB) {
		String result = 授勋合法性判断(player, playerB);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null) {
			long[] ids = country.getShouxunIds();
			if (ids != null) {
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] <= 0) {
						ids[i] = playerB.getId();
						country.setShouxunIds(ids);
						country.成为授勋官员时间Map.put(playerB.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						country.setDirty(true);
						try {
							ChatMessageService cms = ChatMessageService.getInstance();
							ChatMessage msg = new ChatMessage();
							String descri = Translate.translateString(Translate.授勋详细, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, 得到官职名(官职(playerB.getCountry(), playerB.getId())) }, { Translate.PLAYER_NAME_2, playerB.getName() } });
							msg.setMessageText(descri);
							cms.sendMessageToCountry(country.getCountryId(), msg);
							// logger.info("[授勋] ["+country.getName()+"] ["+descri+"]");
							if (logger.isInfoEnabled()) logger.info("[授勋] [{}] [{}]", new Object[] { country.getName(), descri });

						} catch (Exception ex) {
							if (logger.isInfoEnabled()) logger.info("[授勋] [" + country.getName() + "] [国家频道广播异常]", ex);

						}
						break;
					} else if (ids[i] == playerB.getId()) {
						return;
					} else {
						try {
							PlayerManager pm = PlayerManager.getInstance();
							pm.getPlayer(ids[i]);
						} catch (Exception ex) {
							ids[i] = playerB.getId();
							country.setShouxunIds(ids);
							country.成为授勋官员时间Map.put(playerB.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							country.setDirty(true);
							try {
								ChatMessageService cms = ChatMessageService.getInstance();
								ChatMessage msg = new ChatMessage();
								String descri = Translate.translateString(Translate.授勋详细, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, 得到官职名(官职(playerB.getCountry(), playerB.getId())) }, { Translate.PLAYER_NAME_2, playerB.getName() } });
								msg.setMessageText(descri);
								cms.sendMessageToCountry(country.getCountryId(), msg);
								// logger.info("[授勋] ["+country.getName()+"] ["+descri+"]");
								if (logger.isInfoEnabled()) logger.info("[授勋] [{}] [{}]", new Object[] { country.getName(), descri });

							} catch (Exception exx) {
								if (logger.isInfoEnabled()) logger.info("[授勋] [" + country.getName() + "] [国家频道广播异常]", exx);
							}
							break;
						}
					}
				}
			}
		}
	}

	public String 授勋合法性判断(Player player, Player playerB) {
		if (player == null || playerB == null) {
			return Translate.人不能为空;
		}
		if (player == playerB) {
			return Translate.不需要给自己授勋;
		}
		if (player.getCountry() != playerB.getCountry()) {
			return Translate.不能给其他国家的人授勋;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 != 国王) {
			return Translate.此功能只有国王才有权使用;
		}
		int 官职B = 官职(playerB.getCountry(), playerB.getId());
		if (官职B == -1) {
			return Translate.不能给没有官职的人授勋;
		}
		Country country = countryMap.get(player.getCountry());
		boolean full = true;
		if (country != null) {
			long[] ids = country.getShouxunIds();
			if (ids != null) {
				for (long id : ids) {
					if (id <= 0) {
						full = false;
					} else if (id == playerB.getId()) {
						return Translate.此人已经授勋;
					} else {
						try {
							PlayerManager pm = PlayerManager.getInstance();
							pm.getPlayer(id);
						} catch (Exception ex) {
							full = false;
						}
					}
				}
			}
		}
		if (full) {
			return Translate.授勋人数已满;
		}
		return null;
	}

	public void 撤销授勋(Player player, Player playerB) {
		String result = 撤销授勋合法性判断(player, playerB);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null) {
			long[] ids = country.getShouxunIds();
			if (ids != null) {
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] == playerB.getId()) {
						ids[i] = -1;
						country.setShouxunIds(ids);
						country.成为授勋官员时间Map.remove(playerB.getId());
						country.setDirty(true);
						try {
							ChatMessageService cms = ChatMessageService.getInstance();
							ChatMessage msg = new ChatMessage();
							String descri = Translate.translateString(Translate.撤销授勋, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, 得到官职名(官职(playerB.getCountry(), playerB.getId())) }, { Translate.PLAYER_NAME_2, playerB.getName() } });
							msg.setMessageText(descri);
							cms.sendMessageToCountry(country.getCountryId(), msg);
							// logger.info("[撤销授勋] ["+country.getName()+"] ["+descri+"]");
							if (logger.isInfoEnabled()) logger.info("[撤销授勋] [{}] [{}]", new Object[] { country.getName(), descri });

						} catch (Exception ex) {
							if (logger.isInfoEnabled()) logger.info("[撤销授勋] [" + country.getName() + "] [国家频道广播异常]", ex);
						}
						return;
					}
				}
			}
		}
	}

	public String 撤销授勋合法性判断(Player player, Player playerB) {
		if (player == null || playerB == null) {
			return Translate.人不能为空;
		}
		if (player == playerB) {
			return Translate.不需要给自己撤销授勋;
		}
		if (player.getCountry() != playerB.getCountry()) {
			return Translate.不能给其他国家的人撤销授勋;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 != 国王) {
			return Translate.此功能只有国王才有权使用;
		}
		Country country = countryMap.get(player.getCountry());
		boolean has = false;
		if (country != null) {
			long[] ids = country.getShouxunIds();
			if (ids != null) {
				for (long id : ids) {
					if (id == playerB.getId()) {
						has = true;
						break;
					}
				}
			}
		}
		if (!has) {
			return Translate.授勋列表中没有此人;
		}
		return null;
	}

	public void 表彰(Player player, Player playerB) {
		String result = 表彰合法性判断(player, playerB);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null) {
			long[] ids = country.getBiaozhangIds();
			if (ids != null) {
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] <= 0) {
						ids[i] = playerB.getId();
						country.setBiaozhangIds(ids);
						country.成为表彰官员时间Map.put(playerB.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						country.setDirty(true);
						try {
							ChatMessageService cms = ChatMessageService.getInstance();
							ChatMessage msg = new ChatMessage();
							String descri = Translate.translateString(Translate.表彰详细, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, 得到官职名(官职(playerB.getCountry(), playerB.getId())) }, { Translate.PLAYER_NAME_2, playerB.getName() } });
							msg.setMessageText(descri);
							cms.sendMessageToCountry(country.getCountryId(), msg);
							// logger.info("[表彰] ["+country.getName()+"] ["+descri+"]");
							if (logger.isInfoEnabled()) logger.info("[表彰] [{}] [{}]", new Object[] { country.getName(), descri });

						} catch (Exception ex) {
							if (logger.isInfoEnabled()) logger.info("[表彰] [" + country.getName() + "] [国家频道广播异常]", ex);
						}
						break;
					} else if (ids[i] == playerB.getId()) {
						return;
					} else {
						try {
							PlayerManager pm = PlayerManager.getInstance();
							pm.getPlayer(ids[i]);
						} catch (Exception ex) {
							ids[i] = playerB.getId();
							country.setBiaozhangIds(ids);
							country.成为表彰官员时间Map.put(playerB.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							country.setDirty(true);
							try {
								ChatMessageService cms = ChatMessageService.getInstance();
								ChatMessage msg = new ChatMessage();
								String descri = Translate.translateString(Translate.表彰详细, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, 得到官职名(官职(playerB.getCountry(), playerB.getId())) }, { Translate.PLAYER_NAME_2, playerB.getName() } });
								msg.setMessageText(descri);
								cms.sendMessageToCountry(country.getCountryId(), msg);
								// logger.info("[表彰] ["+country.getName()+"] ["+descri+"]");
								if (logger.isInfoEnabled()) logger.info("[表彰] [{}] [{}]", new Object[] { country.getName(), descri });

							} catch (Exception exx) {
								if (logger.isInfoEnabled()) logger.info("[表彰] [" + country.getName() + "] [国家频道广播异常]", exx);
							}
							break;
						}
					}
				}
			}
		}
	}

	public String 表彰合法性判断(Player player, Player playerB) {
		if (player == null || playerB == null) {
			return Translate.人不能为空;
		}
		if (player == playerB) {
			return Translate.不需要给自己表彰;
		}
		if (player.getCountry() != playerB.getCountry()) {
			return Translate.不能给其他国家的人表彰;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 != 国王) {
			return Translate.此功能只有国王才有权使用;
		}
		int 官职B = 官职(playerB.getCountry(), playerB.getId());
		if (官职B == -1) {
			return Translate.不能给没有官职的人表彰;
		}
		Country country = countryMap.get(player.getCountry());
		boolean full = true;
		if (country != null) {
			long[] ids = country.getBiaozhangIds();
			if (ids != null) {
				for (long id : ids) {
					if (id <= 0) {
						full = false;
					} else if (id == playerB.getId()) {
						return Translate.此人已经表彰;
					} else {
						try {
							PlayerManager pm = PlayerManager.getInstance();
							pm.getPlayer(id);
						} catch (Exception ex) {
							full = false;
						}
					}
				}
			}
		}
		if (full) {
			return Translate.表彰人数已满;
		}
		return null;
	}

	public void 撤销表彰(Player player, Player playerB) {
		String result = 撤销表彰合法性判断(player, playerB);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null) {
			long[] ids = country.getBiaozhangIds();
			if (ids != null) {
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] == playerB.getId()) {
						ids[i] = -1;
						country.setBiaozhangIds(ids);
						country.成为表彰官员时间Map.remove(playerB.getId());
						country.setDirty(true);
						try {
							ChatMessageService cms = ChatMessageService.getInstance();
							ChatMessage msg = new ChatMessage();
							String descri = Translate.translateString(Translate.撤销表彰, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.STRING_2, 得到官职名(官职(playerB.getCountry(), playerB.getId())) }, { Translate.PLAYER_NAME_2, playerB.getName() } });
							msg.setMessageText(descri);
							cms.sendMessageToCountry(country.getCountryId(), msg);
							// logger.info("[撤销表彰] ["+country.getName()+"] ["+descri+"]");
							if (logger.isInfoEnabled()) logger.info("[撤销表彰] [{}] [{}]", new Object[] { country.getName(), descri });

						} catch (Exception ex) {
							if (logger.isInfoEnabled()) logger.info("[表彰] [" + country.getName() + "] [国家频道广播异常]", ex);
						}
						return;
					}
				}
			}
		}
	}

	public String 撤销表彰合法性判断(Player player, Player playerB) {
		if (player == null || playerB == null) {
			return Translate.人不能为空;
		}
		if (player == playerB) {
			return Translate.不需要给自己撤销表彰;
		}
		if (player.getCountry() != playerB.getCountry()) {
			return Translate.不能给其他国家的人撤销表彰;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 != 国王) {
			return Translate.此功能只有国王才有权使用;
		}
		Country country = countryMap.get(player.getCountry());
		boolean has = false;
		if (country != null) {
			long[] ids = country.getBiaozhangIds();
			if (ids != null) {
				for (long id : ids) {
					if (id == playerB.getId()) {
						has = true;
						break;
					}
				}
			}
		}
		if (!has) {
			return Translate.表彰列表中没有此人;
		}
		return null;
	}

	public synchronized void 投票(Player player, boolean[] votes, byte voteType) {
		String result = 投票合法性判断(player, votes, voteType);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country == null) {
			return;
		}
		if (voteType == 0) {
			data.已在NPC处投票次数Map.put(player.getId(), 1);
		} else {
			data.已在玩家发起中投票次数Map.put(player.getId(), 1);
		}
		data.setDirty(true);
		int[] votess = country.currentVote;
		if (votess != null) {
			for (int i = 0; i < votess.length && i < votes.length; i++) {
				if (votes[i]) {
					votess[i] = votess[i] + 1;
				}
			}
			country.setCurrentVote(votess);
			country.setCurrentAllVote(country.getCurrentAllVote() + 1);
		}
		// 给投票者加buff
		fireBuff(player, "投票福利", 24, 30 * 60 * 1000);
		player.sendError(Translate.投票福利提示);
	}

	public static void fireBuff(Player player, String buffName, int buffLevel, long time) {
		try {
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			Buff buff = bt.createBuff(buffLevel);
			buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + time);
			buff.setCauser(player);
			if(bt instanceof BuffTemplate_JingYan){
				BuffTemplate_JingYan bt_jingYan = (BuffTemplate_JingYan)bt;
				int expPercent[] = bt_jingYan.getExpPercent();
				if(expPercent != null && expPercent.length > buffLevel){
					if(expPercent[buffLevel] > 0)
						buff.setDescription(Translate.野外打怪经验加成+expPercent[buffLevel]+"%");
					else if(expPercent[buffLevel] < 0)
						buff.setDescription(Translate.野外打怪经验加成+(-expPercent[buffLevel])+"%");
				}
			}
			player.placeBuff(buff);
		} catch (Exception e) {
			logger.error("[fireBuff] [异常] [" + player.getLogString4Knap() + "] [" + buffName + "] [" + buffLevel + "]", e);
		}
	}

	public String 投票合法性判断(Player player, boolean[] votes, byte voteType) {
		String result = null;
		if (player == null) {
			return Translate.人不能为空;
		}
		if (votes == null || votes.length != 9) {
			return Translate.请进行正确操作;
		}
		if (voteType == 0) {
			if (data.已在NPC处投票次数Map.get(player.getId()) != null) {
				return Translate.今天已经在NPC处投过票了;
			}
		} else {
			if (data.已在玩家发起中投票次数Map.get(player.getId()) != null) {
				return Translate.今天已经在玩家发起的投票中投过票了;
			}
		}
		return result;
	}

	public void 查看金库资金(QUERY_COUNTRY_JINKU_REQ req, Player player) {
		long count = 0;
		String jiazuName = "";
		if (this.countryMap != null) {
			Country country = this.countryMap.get(player.getCountry());
			if (country != null) {
				count = country.getKingMoney();
				BiaoJu bj = country.getBiaoju();
				if (bj != null) {
					JiazuManager jm = JiazuManager.getInstance();
					if (jm != null) {
						Jiazu jz = jm.getJiazu(bj.getJiazuId());
						if (jz != null) {
							jiazuName = jz.getName();
						}
					}
				}
			}
		}
		QUERY_COUNTRY_JINKU_RES res = new QUERY_COUNTRY_JINKU_RES(req.getSequnceNum(), count, jiazuName);
		player.addMessageToRightBag(res);
	}

	public void 国王支取金库申请(Player player) {
		String result = 支取金库合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.支取资金);
		long count = 0;
		Country country = this.countryMap.get(player.getCountry());
		if (country != null) {
			count = country.getKingMoney();
		}
		mw.setDescriptionInUUB(Translate.translateString(Translate.请输入资金数金库详细, new String[][] { { Translate.COUNT_1, count + "" } }));
		Option_Country_zhiqu option = new Option_Country_zhiqu();
		option.setText(Translate.确定);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancel });
		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, Translate.请输入支取资金数, Translate.确定, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);
	}

	public void 国王支取金库(Player player, int count) {
		String result = 支取金库合法性判断2(player, count);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		// 国王金钱操作
		BillingCenter bc = BillingCenter.getInstance();
		try {
			bc.playerSaving(player, count, CurrencyType.SHOPYINZI, SavingReasonType.GUOWANGZHIQUJINKU, "国王支取金库");
		} catch (Exception e) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.支取失败);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null) {
			country.kingMoney = country.kingMoney - count;
			country.setDirty(true);
		}
	}

	public String 支取金库合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 != 国王) {
			return Translate.您没有这个权限;
		}
		return null;
	}

	public String 支取金库合法性判断2(Player player, int count) {
		if (player == null) {
			return Translate.人不能为空;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 != 国王) {
			return Translate.您没有这个权限;
		}
		if (每天支取上限 < count) {
			return Translate.超出每日支取上限;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null && country.kingMoney < count) {
			return Translate.国库里没有这么多钱;
		}
		return null;
	}

	public void 发布国家公告申请(Player player) {
		String result = 发布国家公告合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.公告);
		mw.setDescriptionInUUB(Translate.请输入公告);
		Option_Country_notice option = new Option_Country_notice();
		option.setText(Translate.公告);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancel });
		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, Translate.请输入公告, Translate.公告, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);
	}

	public void 发布国家公告(Player player, String notice) {
		String result = 发布国家公告合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null) {
			country.setNotice(notice);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.公告发布成功);
			player.addMessageToRightBag(hreq);
		}
	}

	public String 发布国家公告合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 != 国王) {
			return Translate.您没有这个权限;
		}
		return null;
	}

	public void 发布国运(Player player) {
		String result = 发布国运合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null) {
			try {
				country.setGuoyunStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				ChatMessageService cms = ChatMessageService.getInstance();
				ChatMessage msg = new ChatMessage();
				String description = Translate.translateString(Translate.国家国运开始, new String[][] { { Translate.STRING_1, country.getName() } });
				msg.setMessageText(description);
				cms.sendMessageToSystem(msg);
				description = Translate.translateString(Translate.某人发布了国家运镖, new String[][] { { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, { Translate.STRING_2, player.getName() } });
				msg.setMessageText(description);
				cms.sendMessageToCountry(country.getCountryId(), msg);
				// logger.info("[国运] ["+country.getName()+"] ["+description+"]");
				if (logger.isInfoEnabled()) logger.info("[国运] [{}] [{}]", new Object[] { country.getName(), description });
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String 发布国运合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		boolean 官职合法 = false;
		for (int gz : 拥有发布国运权利的官员) {
			if (gz == 官职) {
				官职合法 = true;
				break;
			}
		}
		if (!官职合法) {
			return Translate.您没有这个权限;
		}
		Country country = countryMap.get(player.getCountry());
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int time = hour * 60 + min;
		if (time < 每天国运最早开始时间 || time > 每天国运最晚开始时间) {
			return Translate.发布国运时间不对;
		}
		int date1 = calendar.get(Calendar.DATE);
		calendar.setTimeInMillis(country.getGuoyunStartTime());
		int date2 = calendar.get(Calendar.DATE);
		if (date1 == date2) {
			return Translate.今天已经发布过国运了;
		}
		return null;
	}

	public void 发布国探(Player player) {
		String result = 发布国探合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null) {
			try {
				country.setGuotanStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				ChatMessageService cms = ChatMessageService.getInstance();
				ChatMessage msg = new ChatMessage();
				String description = Translate.translateString(Translate.国家国探开始, new String[][] { { Translate.STRING_1, country.getName() } });
				msg.setMessageText(description);
				cms.sendMessageToSystem(msg);
				description = Translate.translateString(Translate.某人发布了国家刺探, new String[][] { { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, { Translate.STRING_2, player.getName() } });
				msg.setMessageText(description);
				cms.sendMessageToCountry(country.getCountryId(), msg);
				// logger.info("[国探] ["+country.getName()+"] ["+description+"]");
				if (logger.isInfoEnabled()) logger.info("[国探] [{}] [{}]", new Object[] { country.getName(), description });
			} catch (Exception ex) {
				if (logger.isErrorEnabled()) logger.error("[国探异常] [" + country.getName() + "]", ex);
			}
		}
	}

	public String 发布国探合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		boolean 官职合法 = false;
		for (int gz : 拥有发布国探权利的官员) {
			if (gz == 官职) {
				官职合法 = true;
				break;
			}
		}
		if (!官职合法) {
			return Translate.您没有这个权限;
		}
		Country country = countryMap.get(player.getCountry());
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int time = hour * 60 + min;
		if (time < 每天国探最早开始时间 || time > 每天国探最晚开始时间) {
			return Translate.发布国探时间不对;
		}
		int date1 = calendar.get(Calendar.DATE);
		calendar.setTimeInMillis(country.getGuotanStartTime());
		int date2 = calendar.get(Calendar.DATE);
		if (date1 == date2) {
			return Translate.今天已经发布过国探了;
		}
		return null;
	}

	public String 结盟() {
		String result = null;

		return result;
	}

	public String 结盟合法性判断() {
		String result = null;

		return result;
	}

	public String 解盟() {
		String result = null;

		return result;
	}

	public String 解盟合法性判断() {
		String result = null;

		return result;
	}

	public boolean 每天对国家的数据进行维护() {
		data.已托运次数Map.clear();
		data.已囚禁次数Map.clear();
		data.已禁言次数Map.clear();
		data.已在NPC处投票次数Map.clear();
		data.已在玩家发起中投票次数Map.clear();
		data.已发放俸禄的国王Map.clear();
		data.国王使用召集令的次数Map.clear();
		data.大司马使用召集令的次数Map.clear();
		data.大将军使用召集令的次数Map.clear();
		data.使用一次召集令召集来的人的数量Map.clear();
		data.使用王者之印的次数Map.clear();
		data.国王使用御卫术拉御林军的次数Map.clear();

		data.reReleasePeektask();
		data.reReleaseBricktask();

		data.setDirty(true);
		if (countryMap != null) {
			for (Country country : countryMap.values()) {
				if (country != null) {
					BiaoJu biaoju = country.getBiaoju();
					if (biaoju != null) {
						// 给国王金库存钱以及镖局经营人邮件
						long oldMoney = biaoju.getCurrentMoney() - biaoju.getJingbiaoMoney();
						if (oldMoney > 0) {
							long guowangMoney = (long) (oldMoney * 国王获得镖局利润率);
							long oldPlayerId = biaoju.getPlayerId();
							if (oldPlayerId > 0) {
								// 给他发邮件
								long lastMoney = oldMoney - guowangMoney;
								MailManager mm = MailManager.getInstance();
								if (mm != null) {
									try {
										mm.sendMail(oldPlayerId, new ArticleEntity[0], Translate.镖局经营资金, Translate.这是昨天镖局经营资金, lastMoney, 0, 0, "镖局经营资金");
									} catch (Exception ex) {
										logger.error("[镖局经营资金发邮件] [异常] [oldPlayerId:" + oldPlayerId + "] [lastMoney:" + lastMoney + "]", ex);
									}
								}
							}
							country.setKingMoney(country.getKingMoney() + guowangMoney);
						}
						biaoju.setToubaoweiyuejin(0);
						biaoju.setPlayerId(biaoju.getJingbiaoPlayerId());
						biaoju.setCurrentMoney(biaoju.getJingbiaoMoney());
						biaoju.setJiazuId(biaoju.getJingbiaoJiazuId());
						try {
							ChatMessageService cms = ChatMessageService.getInstance();
							ChatMessage msg = new ChatMessage();
							String description = "";
							JiazuManager jm = JiazuManager.getInstance();
							Jiazu jiazu = jm.getJiazu(biaoju.getJiazuId());
							if (jiazu == null) {
								description = Translate.translateString(Translate.本期镖局没有家族竞标成功, new String[][] { { Translate.STRING_1, CountryManager.得到国家名(country.getCountryId()) } });
							} else {
								description = Translate.translateString(Translate.恭喜家族获得镖局运营权, new String[][] { { Translate.STRING_1, jiazu.getName() }, { Translate.STRING_2, CountryManager.得到国家名(country.getCountryId()) } });
							}
							description = "<f color='0x14ff00'>" + description + "</f>";
							msg.setMessageText(description);
							cms.sendRoolMessageToSystem(msg);
							// logger.info("[系统零时维护] [镖局运营权] ["+country.getName()+"] ["+description+"]");
							if (logger.isInfoEnabled()) logger.info("[系统零时维护] [镖局运营权] [{}] [{}]", new Object[] { country.getName(), description });
						} catch (Exception ex) {

						}
						biaoju.setJingbiaoPlayerId(-1);
						biaoju.setJingbiaoMoney(0);
						biaoju.setJingbiaoJiazuId(-1);
					}
					double[] votes = new double[country.currentVote.length];
					if (country.currentAllVote == 0) {
						country.currentAllVote = 1;
					}
					for (int i = 0; i < country.currentVote.length; i++) {
						votes[i] = country.currentVote[i] * 100 / country.currentAllVote;
					}
					country.yesterdayVote = votes;
					country.currentVote = new int[9];
					country.currentAllVote = 0;

					country.setDirty(true);
				}
			}
		}
		return true;
	}

	private void 国家运镖状态设置(Country country) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (country.isGuoyun()) {
			if (now - country.getGuoyunStartTime() > 国运时长) {
				country.setGuoyun(false);
			}
		} else {
			if (now - country.getGuoyunStartTime() <= 国运时长) {
				country.setGuoyun(true);
				给在线人发国运状态通知(country, now);
			}
		}
	}

	public void 给在线人发国运状态通知(Country country, long now) {
		PlayerManager pm = PlayerManager.getInstance();
		if (pm != null) {
			Player[] ps = pm.getOnlinePlayerByCountry(country.getCountryId());
			if (ps != null) {
				long leftTime = ((country.getGuoyunStartTime() + 国运时长) - now) / 1000;
				for (Player p : ps) {
					NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 100, (int) leftTime, Translate.国运);
					p.addMessageToRightBag(req);
				}
			}
		}
	}

	private void 国家刺探状态设置(Country country) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (country.isGuotan()) {
			if (now - country.getGuotanStartTime() > 国探时长) {
				country.setGuotan(false);
			}
		} else {
			if (now - country.getGuotanStartTime() <= 国探时长) {
				country.setGuotan(true);
				给在线人发国探状态通知(country, now);
			}
		}
	}

	public void 给在线人发国探状态通知(Country country, long now) {
		PlayerManager pm = PlayerManager.getInstance();
		if (pm != null) {
			Player[] ps = pm.getOnlinePlayerByCountry(country.getCountryId());
			if (ps != null) {
				long leftTime = ((country.getGuotanStartTime() + 国探时长) - now) / 1000;
				for (Player p : ps) {
					NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), (byte) 101, (int) leftTime, Translate.国探);
					p.addMessageToRightBag(req);
				}
			}
		}
	}

	public synchronized void 竞标申请(Player player) {
		String result = 竞标合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		CountryManager cm = CountryManager.getInstance();
		Country country = cm.countryMap.get(player.getCountry());
		BiaoJu biaoju = country.getBiaoju();
		long money = 0;
		if (biaoju != null) {
			money = biaoju.getJingbiaoMoney();
		}
		String 镖局经营者 = Translate.本镖局目前没有家族运营您可以为明天的运营竞标;
		if (biaoju != null && biaoju.getJiazuId() > 0) {
			JiazuManager jm = JiazuManager.getInstance();
			Jiazu jiazu = jm.getJiazu(player.getJiazuId());
			if (jiazu != null) {
				镖局经营者 = Translate.translateString(Translate.本镖局现在由某家族运营您可以为明天的运营竞标, new String[][] { { Translate.STRING_1, jiazu.getName() } });
			}
		}
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.竞标);
		mw.setDescriptionInUUB(镖局经营者 + Translate.translateString(Translate.请输入资金数详细, new String[][] { { Translate.COUNT_1, money + "" }, { Translate.COUNT_2, BillingCenter.得到带单位的银两(竞标最低资金) } }));
		Option_Country_jingbiao option = new Option_Country_jingbiao();
		option.setText(Translate.竞标);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancel });
		long cost = 竞标最低资金;
		if (money > 0) {
			cost = money * 100 / 97 + 1000;
		}
		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, cost + "", Translate.竞标, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);
	}

	public synchronized void 竞标(Player player, long money) {
		String result = 竞标合法性判断2(player, money);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null) {
			BiaoJu biaoju = country.getBiaoju();
			if (biaoju == null) {
				biaoju = new BiaoJu();
				country.setBiaoju(biaoju);
			}
			if (biaoju != null) {
				// 找出原竞标人，把剩余资金邮件退还
				long oldMoney = biaoju.getJingbiaoMoney();
				long oldPlayerId = biaoju.getJingbiaoPlayerId();

				biaoju.setJingbiaoMoney(0);
				biaoju.setJingbiaoPlayerId(-1);
				biaoju.setJingbiaoJiazuId(-1);
				// 邮件
				MailManager mm = MailManager.getInstance();
				try {
					mm.sendMail(oldPlayerId, new ArticleEntity[0], Translate.竞标被超越, Translate.您的竞标被他人超越附件中有税后金额请查收, oldMoney, 0, 0, "镖局竞标被超越");
				} catch (Exception ex) {
					logger.error("[竞标被超越发邮件] [异常] [oldPlayerId:" + oldPlayerId + "] [lastMoney:" + oldMoney + "]", ex);
				}
				try {
					PlayerManager pm = PlayerManager.getInstance();
					Player oldPlayer = pm.getPlayer(oldPlayerId);
					if (oldPlayer.isOnline()) {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.您的竞标被别人超过);
						oldPlayer.addMessageToRightBag(hreq);
					}
				} catch (Exception ex) {

				}

				BillingCenter bc = BillingCenter.getInstance();
				try {
					bc.playerExpense(player, money, CurrencyType.SHOPYINZI, ExpenseReasonType.JINGBIAO, "竞标", -1);
				} catch (Exception e) {
					BillingCenter.银子不足时弹出充值确认框(player, Translate.银子不足是否去充值);
					return;
				}
				biaoju.setJingbiaoMoney((long) (money * 竞标剩余资金率));
				biaoju.setJingbiaoPlayerId(player.getId());
				biaoju.setJingbiaoJiazuId(player.getJiazuId());
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.竞标成功如果有人超过您的竞标会有邮件通知);
				player.addMessageToRightBag(hreq);
			}
		}
	}

	public String 竞标合法性判断(Player player) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.镖局)) {
			return Translate.合服功能关闭提示;
		}
		if (player == null) {
			return Translate.人不能为空;
		}
		// 判断是否为家族族长
		JiazuManager jm = JiazuManager.getInstance();
		Jiazu jiazu = jm.getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return Translate.您还没有家族;
		}
		if (jiazu.getJiazuMaster() != player.getId()) {
			return Translate.只有家族族长可以竞标;
		}
		// 判断是否为占领城市的宗派
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		ZongPai zp = zpm.getZongPaiById(jiazu.getZongPaiId());
		if (zp == null) {
			return Translate.您的家族没有宗派;
		}
		String cityName = zpm.getCityNameByZongPai(zp);
		if (cityName == null || cityName.equals("")) {
			return Translate.您的宗派没有占领任何城市;
		}
		CountryManager cm = CountryManager.getInstance();
		Country country = cm.countryMap.get(player.getCountry());
		BiaoJu biaoju = country.getBiaoju();
		if (biaoju != null) {
			if (biaoju.getJingbiaoJiazuId() == player.getJiazuId()) {
				return Translate.您的家族目前是最高竞标者;
			}
		}
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(now);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 每天竞标开始时间 || hour >= 每天竞标结束时间) {
			return Translate.竞标时间不正确;
		}
		return null;
	}

	public String 竞标合法性判断2(Player player, long money) {
		String result = 竞标合法性判断(player);
		if (result != null) {
			return result;
		}
		if (money > player.getSilver() + player.getShopSilver()) {
			return Translate.您的资金不足;
		}
		if (money < 竞标最低资金) {
			return Translate.translateString(Translate.竞标资金太少详细, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(竞标最低资金) } });
		}
		CountryManager cm = CountryManager.getInstance();
		Country country = cm.countryMap.get(player.getCountry());
		BiaoJu biaoju = country.getBiaoju();
		long currentMoney = 0;
		if (biaoju != null) {
			currentMoney = biaoju.getJingbiaoMoney();
			if (biaoju.getJingbiaoJiazuId() == player.getJiazuId()) {
				return Translate.您的家族目前是最高竞标者;
			}
		}
		if ((long) (money * 竞标剩余资金率) <= currentMoney) {
			return Translate.竞标资金必须高于当前竞标资金;
		}
		return null;
	}

	public synchronized void 追加资金申请(Player player) {
		String result = 追加资金合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = this.countryMap.get(player.getCountry());
		BiaoJu biaoju = country.getBiaoju();
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.追加资金);
		mw.setDescriptionInUUB(Translate.translateString(Translate.请输入镖局资金数详细, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(biaoju.getCurrentMoney()) }, { Translate.COUNT_2, (biaoju.getToubaoweiyuejin() > 0 ? BillingCenter.得到带单位的银两(biaoju.getToubaoweiyuejin()) : Translate.零文) } }));
		Option_Country_zhuijiazijin option = new Option_Country_zhuijiazijin();
		option.setText(Translate.确定);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancel });
		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, Translate.请输入资金数, Translate.确定, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);
	}

	public synchronized void 追加资金(Player player, long money) {
		String result = 追加资金合法性判断2(player, money);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		Country country = countryMap.get(player.getCountry());
		if (country != null) {
			BiaoJu biaoju = country.getBiaoju();
			if (biaoju != null) {
				// 扣钱
				try {
					BillingCenter bc = BillingCenter.getInstance();
					bc.playerExpense(player, money, CurrencyType.SHOPYINZI, ExpenseReasonType.ZHUIJIAZIJIN, "追加资金", -1);
				} catch (Exception ex) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.银子不足);
					player.addMessageToRightBag(hreq);
					return;
				}
				biaoju.setCurrentMoney((long) (biaoju.getCurrentMoney() + money * 竞标剩余资金率));
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.追加资金成功详细, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两((long) (money * 竞标剩余资金率)) }, { Translate.COUNT_2, BillingCenter.得到带单位的银两(biaoju.getCurrentMoney()) } }));
				player.addMessageToRightBag(hreq);
			}
		}
	}

	public String 追加资金合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		// 判断是否为家族族长
		JiazuManager jm = JiazuManager.getInstance();
		Jiazu jiazu = jm.getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return Translate.您还没有家族;
		}
		if (jiazu.getJiazuMaster() != player.getId()) {
			return Translate.只有家族族长可以追加资金;
		}
		CountryManager cm = CountryManager.getInstance();
		Country country = cm.countryMap.get(player.getCountry());
		BiaoJu biaoju = country.getBiaoju();
		if (biaoju == null) {
			return Translate.目前镖局没有家族运营;
		}
		if (biaoju.getJiazuId() != player.getJiazuId()) {
			return Translate.您的家族没有运营镖局;
		}
		return null;
	}

	public String 追加资金合法性判断2(Player player, long money) {
		String result = 追加资金合法性判断(player);
		if (result != null) {
			return result;
		}
		if (money <= 0) {
			return Translate.资金错误;
		}
		if (money > player.getSilver() + player.getShopSilver()) {
			return Translate.您的资金不足;
		}
		return null;
	}

	public synchronized void 投保(Player player) {
		String result = 投保合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		// 扣钱
		int cost = 投保所需金钱(player);
		BiaoCheNpc 镖车 = player.得到玩家镖车();
		int toubaoweiyuejin = 镖车.getCfg().getNeedMoney();
		// 扣钱
		try {
			BillingCenter bc = BillingCenter.getInstance();
			bc.playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.TOUBAO, "投保");
		} catch (Exception ex) {
			BillingCenter.银子不足时弹出充值确认框(player, Translate.银子不足是否去充值);
			return;
		}

		if (镖车 != null) {
			镖车.setToubao(true);
			镖车.setTitle(镖车.getTitle() + "<f color='0x14ff00'>保</f>");
		}
		BiaoJu biaoju = countryMap.get(player.getCountry()).getBiaoju();
		biaoju.setCurrentMoney(biaoju.getCurrentMoney() + cost);
		biaoju.setToubaoweiyuejin(biaoju.getToubaoweiyuejin() + toubaoweiyuejin);
		Country country = countryMap.get(player.getCountry());
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.投保成功详细, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(cost) }, { Translate.COUNT_2, BillingCenter.得到带单位的银两(toubaoweiyuejin) } }));
		player.addMessageToRightBag(hreq);
		country.setDirty(true);
	}

	public synchronized void 交付镖车(Player player, BiaoCheNpc 镖车) {
		if (镖车 == null) {
			return;
		}

		if (镖车.isToubao()) {
			BiaoJu biaoju = countryMap.get(player.getCountry()).getBiaoju();
			if (biaoju == null) {
				return;
			}
			if (镖车.isDestroyed()) {
				// 把违约金邮件形式发放玩家
				MailManager mm = MailManager.getInstance();
				try {
					biaoju.setCurrentMoney(biaoju.getCurrentMoney() - 镖车.getCfg().getNeedMoney());
					biaoju.setToubaoweiyuejin(biaoju.getToubaoweiyuejin() - 镖车.getCfg().getNeedMoney());
					String description = Translate.translateString(Translate.您的镖车损毁详细, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(镖车.getCfg().getNeedMoney()) } });
					mm.sendMail(player.getId(), new ArticleEntity[0], Translate.镖局赔付, description, 镖车.getCfg().getNeedMoney(), 0, 0, "镖局赔付");
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
					player.addMessageToRightBag(hreq);
					logger.warn("[交付镖车] [损毁赔付] [" + player.getLogString() + "] [" + description + "]");
				} catch (Exception e) {
					logger.warn("[交付镖车] [损毁赔付异常] [" + player.getLogString() + "]", e);
				}
			} else {
				biaoju.setToubaoweiyuejin(biaoju.getToubaoweiyuejin() - 镖车.getCfg().getNeedMoney());
				logger.warn("[交付镖车] [完好] [" + player.getLogString() + "]");
			}
			Country country = countryMap.get(player.getCountry());
			country.setDirty(true);
		}
		if (!镖车.isDestroyed()) {
			BillingCenter bc = BillingCenter.getInstance();
			try {
				if (镖车.getCfg() != null) {
					bc.playerSaving(player, 镖车.getCfg().getNeedMoney(), CurrencyType.SHOPYINZI, SavingReasonType.JIAOFUBIAOCHE, "交付镖车");
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.任务押金归还详细, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(镖车.getCfg().getNeedMoney()) } }));
					player.addMessageToRightBag(hreq);
					logger.warn("[交付镖车] [完好支付任务费用] [" + player.getLogString() + "] [" + 镖车.getCfg().getNeedMoney() + "]");
				}
			} catch (Exception e) {
				logger.warn("[交付镖车] [完好支付任务费用异常] [" + player.getLogString() + "] [" + 镖车.getCfg().getNeedMoney() + "]", e);
			}
		}
	}

	public String 投保合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour >= 22) {
			return Translate.某点以后不让投保;
		}
		Country country = countryMap.get(player.getCountry());
		if (country == null) {
			return Translate.国家不存在;
		}
		if (country.getBiaoju() == null) {
			return Translate.当前没有任何家族运营镖局;
		}
		if (country.getBiaoju().getPlayerId() <= 0) {
			return Translate.当前没有任何家族运营镖局;
		}
		BiaoCheNpc 镖车 = player.得到玩家镖车();
		if (镖车 == null) {
			return Translate.只有接了镖车才能使用此功能;
		}
		if (镖车.isToubao()) {
			return Translate.镖车已经投保了;
		}
		if (镖车.isJiazuCar()) {
			return Translate.家族镖车不允许投保;
		}
		if (镖车.getCfg() == null) {
			return Translate.服务器出现错误;
		}
		if (镖车.getGrade() < 0) {
			return Translate.破烂车也来投保;
		}
		int cost = 投保所需金钱(player);
		if (player.getSilver() + player.getShopSilver() < cost) {
			return Translate.银子不足;
		}
		int toubaoweiyuejin = 镖车.getCfg().getNeedMoney();
		if (toubaoweiyuejin + country.getBiaoju().getToubaoweiyuejin() > country.getBiaoju().getCurrentMoney()) {
			return Translate.镖局金钱不足;
		}
		if (player.getGame() != null && !player.getGame().equals(镖车.getGameName())) {
			return Translate.镖车和你不在一张地图;
		}
		if (镖车.getHp() != 镖车.getMaxHP()) {
			return Translate.镖车血量不满不能投保;
		}
		if (距离(镖车.getX(), 镖车.getY(), player.getX(), player.getY()) > 镖车与玩家的距离最大值) {
			return Translate.镖车和你距离太远;
		}
		return null;
	}

	public synchronized void 发放俸禄(Player player) {
		String result = 发放俸禄合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		int count = 发放俸禄的金钱(player);
		Country country = getCountryMap().get(player.getCountry());
		if (count > country.kingMoney) {
			String description = Translate.国库里没有这么多钱;
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
			player.addMessageToRightBag(hreq);
			return;
		}
		data.已发放俸禄的国王Map.put(player.getCountry(), 1);
		data.setDirty(true);
		给官员发放俸禄(player);
		得到国家主页面res(player);
		country.setDirty(true);
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.俸禄发放成功);
		player.addMessageToRightBag(hreq);
	}

	public String 发放俸禄合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 != 国王) {
			return Translate.此功能只有国王才有权使用;
		}
		if (data.已发放俸禄的国王Map.get(player.getCountry()) != null) {
			return Translate.今天已经发放过俸禄了;
		}
		Country country = getCountryMap().get(player.getCountry());
		if (country.kingMoney < 发放俸禄资金下限) {
			String description = Translate.国库里没有这么多钱;
			try {
				description = Translate.translateString(Translate.发放俸禄最低资金, new String[][] { { Translate.COUNT_1, BillingCenter.得到带单位的银两(发放俸禄资金下限) } });
			} catch (Exception ex) {

			}
			return description;
		}
		return null;
	}

	public int 发放俸禄的金钱(Player player) {
		int count = 0;
		Country country = getCountryMap().get(player.getCountry());
		double[] votes = country.yesterdayVote;
		if (votes != null) {
			for (double d : votes) {
				count += 基础俸禄 * 1d * (1 + (d - 0.5));
			}
		}
		return count;
	}

	public void 给官员发放俸禄(Player player) {
		Country country = getCountryMap().get(player.getCountry());
		double[] votes = country.yesterdayVote;
		int[] 官员俸禄 = country.get官员俸禄();
		int counts = 0;
		if (votes != null && 官员俸禄 != null && votes.length == 官员俸禄.length) {
			for (int i = 0; i < votes.length; i++) {
				int count = 0;
				count = (int) (基础俸禄 * 1d * (1 + (votes[i] - 0.5)));
				// 国王,大司马,大将军,元帅,宰相,巡捕国王任命,巡捕元帅任命,御史大夫国王任命,御史大夫宰相
				// 按照官职序号-1的顺序安排
				官员俸禄[i] = count;
				counts += count;
			}
			country.setKingMoney(country.getKingMoney() - counts);
			country.set官员俸禄(官员俸禄);
			try {
				ChatMessageService cms = ChatMessageService.getInstance();
				ChatMessage msg = new ChatMessage();
				String descri = Translate.translateString(Translate.国王发放俸禄详细, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() } });
				msg.setMessageText(descri);
				cms.sendMessageToCountry(country.getCountryId(), msg);
				// logger.info("[发放俸禄] ["+country.getName()+"] ["+descri+"]");
				if (logger.isWarnEnabled()) logger.warn("[发放俸禄] [{}] [{}]", new Object[] { country.getName(), descri });
			} catch (Exception ex) {

			}
		}
	}

	/**
	 * 官员提取俸禄
	 * @param player
	 */
	public synchronized void 提取俸禄(Player player) {
		String result = 提取俸禄合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		int 官职 = 官职(player.getCountry(), player.getId());
		Country country = getCountryMap().get(player.getCountry());
		int count = country.get官员俸禄()[官职 - 1];
		BillingCenter bc = BillingCenter.getInstance();
		try {
			// 加钱
			bc.playerSaving(player, count, CurrencyType.SHOPYINZI, SavingReasonType.FENGLU, "领取俸禄");
			country.get官员俸禄()[官职 - 1] = 0;
			country.set官员俸禄(country.get官员俸禄());
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.领取俸禄成功);
			player.addMessageToRightBag(hreq);
			得到国家主页面res(player);
			try {
				ChatMessageService cms = ChatMessageService.getInstance();
				ChatMessage msg = new ChatMessage();
				String descri = Translate.translateString(Translate.领取俸禄成功详细, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职(player.getCountry(), player.getId())) }, new String[] { Translate.PLAYER_NAME_1, player.getName() }, { Translate.COUNT_1, count + "" } });
				msg.setMessageText(descri);
				cms.sendMessageToCountry(country.getCountryId(), msg);
				if (logger.isInfoEnabled()) logger.info("[俸禄] [{}] [{}]", new Object[] { country.getName(), descri });
			} catch (Exception ex) {

			}
		} catch (Exception ex) {

		}

	}

	/**
	 * 官员提取俸禄判断
	 * @param player
	 */
	public String 提取俸禄合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		Country country = getCountryMap().get(player.getCountry());
		if (country.get官员俸禄() == null || 官职 - 1 < 0 || 官职 - 1 >= country.get官员俸禄().length) {
			return Translate.只有特定官员才可以领取俸禄;
		}
		if (country.get官员俸禄()[官职 - 1] <= 0) {
			return Translate.没有俸禄可以领取;
		}
		return null;
	}

	public int 投保所需金钱(Player player) {
		int cost = 1000000000;
		BiaoCheNpc 镖车 = player.得到玩家镖车();
		int color = 镖车.getGrade();
		if (color == 0) {
			cost = 镖车.getCfg().getNeedMoney() * 10 / 100;
		} else if (color == 1) {
			cost = 镖车.getCfg().getNeedMoney() * 20 / 100;
		} else if (color == 2) {
			cost = 镖车.getCfg().getNeedMoney() * 35 / 100;
		} else if (color == 3) {
			cost = 镖车.getCfg().getNeedMoney() * 70 / 100;
		} else if (color == 4) {
			cost = 镖车.getCfg().getNeedMoney();
		}
		if (cost <= 0) {
			cost = 10000;
		}
		return cost;
	}

	public int 距离(float x, float y, float x1, float y1) {
		return (int) Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
	}

	public String 系统维护合法性判断() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if ((hour == 23 && minute == 59) || (hour == 0 && minute <= 10)) {
			return Translate.现在是系统维护时间请稍后再试;
		}
		return null;
	}

	public void 召集令申请(Player player) {
		String result = 召集令合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}

		int 官职 = 官职(player.getCountry(), player.getId());
		int count = 0;
		if (官职 == 国王) {
			count = 0;
			if (data.国王使用召集令的次数Map.get(player.getCountry()) != null) {
				count = data.国王使用召集令的次数Map.get(player.getCountry());
			}
		}
		if (官职 == 大司马) {
			count = 0;
			if (data.大司马使用召集令的次数Map.get(player.getCountry()) != null) {
				count = data.大司马使用召集令的次数Map.get(player.getCountry());
			}
		}
		if (官职 == 大将军) {
			count = 0;
			if (data.大将军使用召集令的次数Map.get(player.getCountry()) != null) {
				count = data.大将军使用召集令的次数Map.get(player.getCountry());
			}
		}

		int money = 使用召集令的花费(count);
		String description = Translate.召集令;
		try {
			if (money <= 0) {
				description = Translate.translateString(Translate.召集令详细描述免费, new String[][] { { Translate.COUNT_1, (count + 1) + "" } });
			} else {
				description = Translate.translateString(Translate.召集令详细描述花钱, new String[][] { { Translate.COUNT_1, (count + 1) + "" }, { Translate.COUNT_2, BillingCenter.得到带单位的银两(money) } });
			}
		} catch (Exception ex) {

		}

		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.召集令);
		mw.setDescriptionInUUB(description);
		Option_Country_zhaojiling option = new Option_Country_zhaojiling();
		option.setText(Translate.召唤);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancel });
		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, Translate.全体国民都点击确定到我这来, Translate.召唤, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);
	}

	public void 给地图上所有本国人发召集消息(Player player, String description) {
		String result = 召集令合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		boolean isXianjie = false;
		try {
			String mapname = player.getCurrentGame().gi.name;
			isXianjie = RobberyConstant.没飞升玩家不可进入的地图.contains(mapname);
		} catch (Exception e) {

		}
		int 官职 = 官职(player.getCountry(), player.getId());
		int count = 0;
		if (官职 == 国王) {
			count = 0;
			if (data.国王使用召集令的次数Map.get(player.getCountry()) != null) {
				count = data.国王使用召集令的次数Map.get(player.getCountry());
			}
		}
		if (官职 == 大司马) {
			count = 0;
			if (data.大司马使用召集令的次数Map.get(player.getCountry()) != null) {
				count = data.大司马使用召集令的次数Map.get(player.getCountry());
			}
		}
		if (官职 == 大将军) {
			count = 0;
			if (data.大将军使用召集令的次数Map.get(player.getCountry()) != null) {
				count = data.大将军使用召集令的次数Map.get(player.getCountry());
			}
		}

		int money = 使用召集令的花费(count);
		// 扣钱
		if (money > 0) {
			try {
				BillingCenter bc = BillingCenter.getInstance();
				bc.playerExpense(player, money, CurrencyType.SHOPYINZI, ExpenseReasonType.TIANZUNLING, "使用混元至圣令");
			} catch (Exception ex) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.银子不足);
				player.addMessageToRightBag(hreq);
				return;
			}
		}

		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		// 做召集次数计算
		召集次数增加(player);
		PlayerManager pm = PlayerManager.getInstance();
		Player[] players = pm.getOnlinePlayers();
		DevilSquareManager inst = DevilSquareManager.instance;
		if (players != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			for (Player p : players) {
				if (inst != null && inst.isPlayerInDevilSquare(p)) { // 恶魔广场不弹框
					continue;
				}
//				if (p.getCurrentGame() != null && JJCManager.isJJCBattle(p.getCurrentGame().gi.name)) {
//					continue;
//				}
				if (GlobalTool.verifyTransByOther(p.getId(), player.getCurrentGame()) != null) {
					continue;
				}
				if (isXianjie && p.getLevel() <= 220) {
					continue;
				}
				if (p != player && p.getCountry() == player.getCountry() && p.getLevel() > PlayerManager.保护最大级别) {
					// logger.warn("[给地图上所有本国人发召集消息1] ["+p.getUsername()+"] ["+p.getId()+"] ["+p.getName()+"] ["+description+"]");
					if (logger.isDebugEnabled()) logger.debug("[给地图上所有本国人发召集消息1] [{}] [{}] [{}] [{}]", new Object[] { p.getUsername(), p.getId(), p.getName(), description });
					if (玩家是否可以传送(p)) {
						// logger.warn("[给地图上所有本国人发召集消息2] ["+p.getUsername()+"] ["+p.getId()+"] ["+p.getName()+"] ["+description+"]");
						if (logger.isDebugEnabled()) logger.debug("[给地图上所有本国人发召集消息2] [{}] [{}] [{}] [{}]", new Object[] { p.getUsername(), p.getId(), p.getName(), description });
						WindowManager mm = WindowManager.getInstance();
						MenuWindow mw = mm.createTempMenuWindow(100);
						mw.setDescriptionInUUB(Translate.translateString(Translate.正在召集你们是否前往, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职) }, new String[] { Translate.STRING_2, hour + ":" + minute }, new String[] { Translate.STRING_3, description } }));
						Option_Country_zhaojiling_byId option = new Option_Country_zhaojiling_byId();
						option.setPlayerId(p.getId());
						option.setSendTime(now);
						option.setMapName(player.getGame());
						option.country = player.getTransferGameCountry();
						option.setX(player.getX());
						option.setY(player.getY());
						option.setText(Translate.确定);
						Option cancelOption = new Option_UseCancel();
						cancelOption.setText(Translate.取消);
						mw.setOptions(new Option[] { option, cancelOption });
						CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
						p.addMessageToRightBag(req);
					}
				}
			}
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.传送请求发送成功);
			player.addMessageToRightBag(hreq);
			// logger.warn("[给地图上所有本国人发召集消息] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+description+"]");
			if (logger.isWarnEnabled()) logger.warn("[给地图上所有本国人发召集消息] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), description });
		} else {
			// logger.warn("[给地图上所有本国人发召集消息，地图上没人] ["+(player != null ? player.getUsername():"")+"] ["+(player != null ? player.getId():"")+"] ["+(player != null ?
			// player.getName():"")+"] ["+description+"]");
			if (logger.isWarnEnabled()) logger.warn("[给地图上所有本国人发召集消息，地图上没人] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), description });
		}
	}

	public boolean 玩家是否可以传送(Player player) {
		if (player == null) {
			return false;
		}
		if (!player.isAlive()) {
			return false;
		}
		List<Buff> buffs = player.getAllBuffs();
		if (buffs != null) {
			for (Buff buff : buffs) {
				if (buff != null && buff.getTemplate() != null && 囚禁buff名称.equals(buff.getTemplate().getName())) {
					return false;
				}
				if (buff != null && buff.getTemplate() != null && 国王令传送状态.equals(buff.getTemplate().getName())) {
					return false;
				}
			}
		}
		return true;
	}

	public String 召集令合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 <= 0) {
			return Translate.您还不是国家官员;
		}
		boolean has = false;
		for (int 拥有权力官职 : 拥有使用召集令权力的官员) {
			if (拥有权力官职 == 官职) {
				has = true;
				break;
			}
		}
		if (!has) {
			return Translate.权限不足请成为圣皇大司马大将军后在使用此功能;
		}
		try {
			if (!player.isIsGuozhan()) {
				Game game = player.getCurrentGame();
				if (game != null && game.gi != null) {
					if (game.country != player.getCountry()) {
						return Translate.非国战状态不能在国外使用;
					}
				}
			}
		} catch (Exception ex) {

		}

		try {
			if (SealManager.getInstance().isSealDownCity(player.getCurrentGame().gi.name)) {
				return Translate.玩家正在进行封印副本挑战;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String result = GlobalTool.verifyTransByOther(player.getId());
			if (result != null) {
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void 召集次数增加(Player player) {
		if (player == null) {
			return;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		int count = 0;
		if (官职 == 国王) {
			if (data.国王使用召集令的次数Map.get(player.getCountry()) != null) {
				count = data.国王使用召集令的次数Map.get(player.getCountry());
			}
			data.国王使用召集令的次数Map.put(player.getCountry(), count + 1);
			data.setDirty(true);
		}
		if (官职 == 大司马) {
			if (data.大司马使用召集令的次数Map.get(player.getCountry()) != null) {
				count = data.大司马使用召集令的次数Map.get(player.getCountry());
			}
			data.大司马使用召集令的次数Map.put(player.getCountry(), count + 1);
			data.setDirty(true);
		}
		if (官职 == 大将军) {
			if (data.大将军使用召集令的次数Map.get(player.getCountry()) != null) {
				count = data.大将军使用召集令的次数Map.get(player.getCountry());
			}
			data.大将军使用召集令的次数Map.put(player.getCountry(), count + 1);
			data.setDirty(true);
		}
	}

	public int 使用召集令的花费(int count) {
		int money = (count - 5) * 100000;
		if (money < 0) {
			money = 0;
		}
		return money;
	}

	// public int 得到官员能够使用召集令的次数(int 官职){
	// int count = 0;
	// if(官职 == 国王){
	// count = 5;
	// }
	// if(官职 == 大司马){
	// count = 3;
	// }
	// if(官职 == 大将军){
	// count = 3;
	// }
	// return count;
	// }
	// 下面是官员使用NPC来召集玩家的函数
	public void 使用召集NPC申请(Player player) {
		String result = 召集NPC合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		int maxCount = 得到官员能够使用召集NPC的次数(官职);
		int count = maxCount;
		if (官职 == 元帅) {
			count = 0;
			if (data.元帅使用召集NPC的次数Map.get(player.getCountry()) != null) {
				count = data.元帅使用召集NPC的次数Map.get(player.getCountry());
			}
		}
		if (官职 == 大将军) {
			count = 0;
			if (data.大将军使用召集NPC的次数Map.get(player.getCountry()) != null) {
				count = data.大将军使用召集NPC的次数Map.get(player.getCountry());
			}
		}
		int hasCount = maxCount - count;
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.召集令 + Translate.translateString(Translate.您还能使用多少次, new String[][] { { Translate.COUNT_1, hasCount + "" } }));
		mw.setDescriptionInUUB(Translate.召集令描述);
		Option_Country_zhaojilingbynpc option = new Option_Country_zhaojilingbynpc();
		option.setText(Translate.召唤);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancel });
		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 100, Translate.在这里输入, Translate.召唤, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);
	}

	public void 给地图上所有本国人发召集NPC消息(Player player, String description) {
		String result = 召集NPC合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		// 做召集次数计算
		召集NPC次数增加(player);
		PlayerManager pm = PlayerManager.getInstance();
		Player[] players = pm.getOnlinePlayers();
		if (players != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			int 官职 = 官职(player.getCountry(), player.getId());
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			for (Player p : players) {
				if (p != player && p.getCountry() == player.getCountry()) {
					// logger.warn("[给地图上所有本国人发召集NPC消息1] ["+p.getUsername()+"] ["+p.getId()+"] ["+p.getName()+"] ["+description+"]");
					if (logger.isWarnEnabled()) logger.warn("[给地图上所有本国人发召集NPC消息1] [{}] [{}] [{}] [{}]", new Object[] { p.getUsername(), p.getId(), p.getName(), description });
					if (玩家是否可以传送(p)) {
						// logger.warn("[给地图上所有本国人发召集NPC消息2] ["+p.getUsername()+"] ["+p.getId()+"] ["+p.getName()+"] ["+description+"]");
						if (logger.isWarnEnabled()) logger.warn("[给地图上所有本国人发召集NPC消息2] [{}] [{}] [{}] [{}]", new Object[] { p.getUsername(), p.getId(), p.getName(), description });
						WindowManager mm = WindowManager.getInstance();
						MenuWindow mw = mm.createTempMenuWindow(100);
						mw.setDescriptionInUUB(Translate.translateString(Translate.正在召集你们是否前往, new String[][] { new String[] { Translate.STRING_1, 得到官职名(官职) }, new String[] { Translate.STRING_2, hour + ":" + minute }, new String[] { Translate.STRING_3, description } }));
						Option_Country_zhaojiling_byId option = new Option_Country_zhaojiling_byId();
						option.setPlayerId(p.getId());
						option.setSendTime(now);
						option.setMapName(player.getGame());
						option.setX(player.getX());
						option.setY(player.getY());
						option.setText(Translate.确定);
						Option cancelOption = new Option_UseCancel();
						cancelOption.setText(Translate.取消);
						mw.setOptions(new Option[] { option, cancelOption });
						CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
						p.addMessageToRightBag(req);
					}
				}
			}
			// logger.warn("[给地图上所有本国人发召集NPC消息] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+description+"]");
			if (logger.isWarnEnabled()) logger.warn("[给地图上所有本国人发召集NPC消息] [{}] [{}] [{}] [{}]", new Object[] { player.getUsername(), player.getId(), player.getName(), description });
		} else {
			// logger.warn("[给地图上所有本国人发召集NPC消息，地图上没人] ["+(player != null ? player.getUsername():"")+"] ["+(player != null ? player.getId():"")+"] ["+(player != null ?
			// player.getName():"")+"] ["+description+"]");
			if (logger.isWarnEnabled()) logger.warn("[给地图上所有本国人发召集NPC消息，地图上没人] [{}] [{}] [{}] [{}]", new Object[] { (player != null ? player.getUsername() : ""), (player != null ? player.getId() : ""), (player != null ? player.getName() : ""), description });
		}
	}

	public String 召集NPC合法性判断(Player player) {
		if (player == null) {
			return Translate.人不能为空;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		if (官职 <= 0) {
			return Translate.您还不是国家官员;
		}
		boolean has = false;
		for (int 拥有权力官职 : 拥有使用召集NPC权力的官员) {
			if (拥有权力官职 == 官职) {
				has = true;
				break;
			}
		}
		if (!has) {
			return Translate.权限不足请成为大将军元帅后在使用此功能;
		}
		int maxCount = 得到官员能够使用召集NPC的次数(官职);
		if (maxCount == 0) {
			return Translate.translateString(Translate.今天使用次数已经用光, new String[][] { { Translate.COUNT_1, 得到官员能够使用召集NPC的次数(官职) + "" } });
		}
		int count = maxCount;
		if (官职 == 元帅) {
			count = 0;
			if (data.元帅使用召集NPC的次数Map.get(player.getCountry()) != null) {
				count = data.元帅使用召集NPC的次数Map.get(player.getCountry());
			}
		}
		if (官职 == 大将军) {
			count = 0;
			if (data.大将军使用召集NPC的次数Map.get(player.getCountry()) != null) {
				count = data.大将军使用召集NPC的次数Map.get(player.getCountry());
			}
		}
		if (count >= maxCount) {
			return Translate.translateString(Translate.今天使用次数已经用光, new String[][] { { Translate.COUNT_1, 得到官员能够使用召集NPC的次数(官职) + "" } });
		}
		try {
			Game game = player.getCurrentGame();
			if (game != null && game.gi != null) {
				if (game.country != player.getCountry()) {
					return Translate.必须在本国地图才能使用;
				}
			}
		} catch (Exception ex) {

		}
		return null;
	}

	public void 召集NPC次数增加(Player player) {
		if (player == null) {
			return;
		}
		int 官职 = 官职(player.getCountry(), player.getId());
		int count = 0;
		if (官职 == 元帅) {
			if (data.元帅使用召集NPC的次数Map.get(player.getCountry()) != null) {
				count = data.元帅使用召集NPC的次数Map.get(player.getCountry());
			}
			data.元帅使用召集NPC的次数Map.put(player.getCountry(), count + 1);
			data.setDirty(true);
		}
		if (官职 == 大将军) {
			if (data.大将军使用召集NPC的次数Map.get(player.getCountry()) != null) {
				count = data.大将军使用召集NPC的次数Map.get(player.getCountry());
			}
			data.大将军使用召集NPC的次数Map.put(player.getCountry(), count + 1);
			data.setDirty(true);
		}
	}

	public int 得到官员能够使用召集NPC的次数(int 官职) {
		int count = 0;
		if (官职 == 元帅) {
			count = 3;
		}
		if (官职 == 大将军) {
			count = 3;
		}
		return count;
	}

	public CountryManagerData getData() {
		return data;
	}

	public void setData(CountryManagerData data) {
		this.data = data;
	}

	public Hashtable<Byte, Country> getCountryMap() {
		return countryMap;
	}

	public void setCountryMap(Hashtable<Byte, Country> countryMap) {
		this.countryMap = countryMap;
	}

	private Hashtable<Byte, Country> notSaveSuccessCountryMap = new Hashtable<Byte, Country>();
	private Hashtable<Byte, Country> notUpdateSuccessCountryMap = new Hashtable<Byte, Country>();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		while (running) {
			try {
				Thread.sleep(100000);
				if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
					continue;
				}
				if (countryMap != null) {
					for (Country country : countryMap.values()) {
						if (country != null) {
							国家运镖状态设置(country);
							国家刺探状态设置(country);
							calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							int date = calendar.get(Calendar.DATE);
							if (this.date != date) {
								int hour = calendar.get(Calendar.HOUR_OF_DAY);
								long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
								if (hour == 0) {
									if (每天对国家的数据进行维护()) {
										if (logger.isInfoEnabled()) logger.info("[国家系统维护] [{}] [{}ms]", new Object[] { country.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
									} else {
										if (logger.isInfoEnabled()) logger.info("[国家系统维护失败] [{}] [{}ms]", new Object[] { country.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
									}
								}
								this.date = date;
							}
						} else {
							logger.error("国家为空");
						}
					}
				}
				save();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void loadFromInputStream(InputStream is) throws Exception {
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(地图传送区域sheet);
		int rowNum = sheet.getPhysicalNumberOfRows();
		HashMap<String, WangZheTransferPointOnMap> transferPoints = new HashMap<String, WangZheTransferPointOnMap>();
		HashMap<String, WangZheTransferPointOnMap> chinaMapNameTransferPoints = new HashMap<String, WangZheTransferPointOnMap>();
		for (int i = 1; i < rowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell hc = row.getCell(地图传送区域_mapName_所在列);
			WangZheTransferPointOnMap wzt = new WangZheTransferPointOnMap();
			String name1 = null;
			String name2 = null;
			try {
				String name = (hc.getStringCellValue().trim());
				wzt.setDisplayMapName(name);
				chinaMapNameTransferPoints.put(name, wzt);
				name1 = name;
				name = transferCHINAToENGLISH(name);
				name2 = name;
				wzt.setMapName(name);
				transferPoints.put(name, wzt);
			} catch (Exception e) {
				throw e;
			}

			hc = row.getCell(地图传送区域_传送位置横纵坐标及范围半径传送位置名字_所在列);
			try {
				String XYWidthHeight = (hc.getStringCellValue().trim());
				String[] arrays = XYWidthHeight.split("@");
				if (arrays.length == 1) {
					arrays = XYWidthHeight.split("@");
				}
				short[] xs = new short[arrays.length];
				short[] ys = new short[arrays.length];
				short[] widths = new short[arrays.length];
				String[] 传送位置名 = new String[arrays.length];
				wzt.setPointsX(xs);
				wzt.setPointsY(ys);
				wzt.setRanges(widths);
				wzt.setPointNames(传送位置名);
				for (int j = 0; j < arrays.length; j++) {
					String a = arrays[j];
					String[] as = a.split(";");
					if (as.length == 1) {
						as = a.split("；");
					}
					xs[j] = Short.parseShort(as[0]);
					ys[j] = Short.parseShort(as[1]);
					widths[j] = Short.parseShort(as[2]);
					传送位置名[j] = as[3];
				}
			} catch (Exception e) {
				throw e;
			}
			
			hc = row.getCell(2);
			int country = (int)hc.getNumericCellValue();
			HashMap<String, WangZheTransferPointOnMap> mape = englishMapNameTransferPoints2.get(country);
			if(mape == null){
				mape = new HashMap<String, WangZheTransferPointOnMap> ();
			}
			mape.put(name2, wzt);
			englishMapNameTransferPoints2.put(country, mape);
			
			HashMap<String, WangZheTransferPointOnMap> mape2 = chinaMapNameTransferPoints2.get(country);
			if(mape2 == null){
				mape2 = new HashMap<String, WangZheTransferPointOnMap> ();
			}
			mape2.put(name1, wzt);
			chinaMapNameTransferPoints2.put(country, mape2);
		}

		this.englishMapNameTransferPoints = transferPoints;
		this.chinaMapNameTransferPoints = chinaMapNameTransferPoints;
	}

	public String transferCHINAToENGLISH(String str) {
		GameManager gm = GameManager.getInstance();
		for (GameInfo gif : gm.getGameInfos()) {
			if (gif.displayName.equals(str)) {
				return gif.name;
			}
		}
		return str;
	}

	/**
	 * 官员使用王者之印
	 * @param player
	 * @param req
	 */
	public synchronized void 使用王者之印(Player player, COUNTRY_WANGZHEZHIYIN_REQ req) {
		String result = 使用王者之印合法性判断(player);
		if (result != null) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, result);
			player.addMessageToRightBag(hreq);
			return;
		}
		CountryManager cm = CountryManager.getInstance();
		WangZheTransferPointOnMap wzt = cm.englishMapNameTransferPoints2.get(new Integer(player.getCountry())).get(((COUNTRY_WANGZHEZHIYIN_REQ) req).getMapName());
		if (wzt != null) {
			int x = ((COUNTRY_WANGZHEZHIYIN_REQ) req).getX();
			int y = ((COUNTRY_WANGZHEZHIYIN_REQ) req).getY();

			Game game = player.getCurrentGame();
			if (game != null) {
				Hashtable<Long, Integer> map = cm.data.使用王者之印的次数Map;
				Hashtable<Long, Long> 王者之印总次数Map = cm.data.使用王者之印的总次数Map;
				long allCount = 0;
				int count = 0;
				if (map.get(player.getId()) != null) {
					count = map.get(player.getId());
				}
				if (王者之印总次数Map.get(player.getId()) != null) {
					allCount = 王者之印总次数Map.get(player.getId());
				}
				int money = cm.使用王者之印花费(count);
				// 扣钱
				try {
					BillingCenter bc = BillingCenter.getInstance();
					bc.playerExpense(player, money, CurrencyType.BIND_YINZI, ExpenseReasonType.WANGZHEZHIYIN, "使用王者之印");
				} catch (Exception ex) {
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.您今天可使用的绑银不足);
					player.addMessageToRightBag(hreq);
					return;
				}
				count++;
				allCount++;
				game.transferGame(player, new TransportData(0, 0, 0, 0, ((COUNTRY_WANGZHEZHIYIN_REQ) req).getMapName(), x, y));
				cm.data.使用王者之印的次数Map.put(player.getId(), count);
				cm.data.使用王者之印的总次数Map.put(player.getId(), allCount);
				cm.data.setDirty(true);
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.传送成功);
				player.addMessageToRightBag(hreq);
			}
		}
	}

	public String 使用王者之印合法性判断(Player player) {

		CountryManager cm = CountryManager.getInstance();
		Game game = player.getCurrentGame();
		if (game == null) {
			return Translate.不在地图不能使用;
		}
		int 官职 = cm.官职(player.getCountry(), player.getId());
		int[] 可以使用王者的官职 = CountryManager.拥有使用王者之令权利的官员;
		boolean canUse = false;
		for (int guanzhi : 可以使用王者的官职) {
			if (guanzhi == 官职) {
				canUse = true;
				break;
			}
		}
		if (!canUse) {
			return Translate.您没有权限使用;
		}
		if (game.country != player.getCountry()) {
			return Translate.只能在本国地图使用;
		}
		Hashtable<Long, Integer> map = cm.data.使用王者之印的次数Map;
		int count = 0;
		if (map.get(player.getId()) != null) {
			count = map.get(player.getId());
		}
		int money = cm.使用王者之印花费(count);
		if (!player.bindSilverEnough(money)) {
			return Translate.您今天可使用的绑银不足;
		}
		return null;
	}

	public int 使用王者之印花费(int count) {
		int money = count * 50000 + 1000;
		return money;
	}

	public void 得到国家主页面res(Player player) {
		CountryManager cm = CountryManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		if (cm != null && pm != null) {
			String countryName = "";
			String kingName = "";
			String dasimaName = "";
			String seniorGeneralName = "";
			String marshalName = "";
			String primeMinisterName = "";
			String policeByKingName = "";
			String yushidafuByKingName = "";
			String policeByMarshalName = "";
			String yushidafuByPrimeMinisterName = "";
			long[] ids = new long[9];
			String[] shouxunNames = new String[0];
			String[] biaozhangNames = new String[0];
			byte kingFengLuTake = 0;
			byte dasimaFengLuTake = 0;
			byte seniorGeneralFengLuTake = 0;
			byte marshalFengLuTake = 0;
			byte primeMinisterFengLuTake = 0;
			byte policeByKingFengLuTake = 0;
			byte yushidafuByKingFengLuTake = 0;
			byte policeByMarshalFengLuTake = 0;
			byte yushidafuByPrimeMinisterFengLuTake = 0;
			String vote = "";
			String notice = "";
			long guozhanZiYuan = 0;
			Country country = cm.countryMap.get(player.getCountry());
			if (country != null) {
				countryName = CountryManager.得到国家名(player.getCountry());
				long kingId = country.getKingId();
				ids[国王 - 1] = kingId;
				if (kingId > 0) {
					try {
						Player p = pm.getPlayer(kingId);
						if (p != null) {
							kingName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long dasimaId = country.getDasimaId();
				ids[大司马 - 1] = dasimaId;
				if (dasimaId > 0) {
					try {
						Player p = pm.getPlayer(dasimaId);
						if (p != null) {
							dasimaName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long seniorGeneralId = country.getSeniorGeneralId();
				ids[大将军 - 1] = seniorGeneralId;
				if (seniorGeneralId > 0) {
					try {
						Player p = pm.getPlayer(seniorGeneralId);
						if (p != null) {
							seniorGeneralName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long marshalId = country.getMarshalId();
				ids[元帅 - 1] = marshalId;
				if (marshalId > 0) {
					try {
						Player p = pm.getPlayer(marshalId);
						if (p != null) {
							marshalName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long primeMinisterId = country.getPrimeMinisterId();
				ids[宰相 - 1] = primeMinisterId;
				if (primeMinisterId > 0) {
					try {
						Player p = pm.getPlayer(primeMinisterId);
						if (p != null) {
							primeMinisterName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long policeByKingId = country.getPoliceByKingId();
				ids[巡捕_国王 - 1] = policeByKingId;
				if (policeByKingId > 0) {
					try {
						Player p = pm.getPlayer(policeByKingId);
						if (p != null) {
							policeByKingName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long yushidafuByKingId = country.getYushidafuByKingId();
				ids[御史大夫_国王 - 1] = yushidafuByKingId;
				if (yushidafuByKingId > 0) {
					try {
						Player p = pm.getPlayer(yushidafuByKingId);
						if (p != null) {
							yushidafuByKingName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long policeByMarshalId = country.getPoliceByMarshalId();
				ids[巡捕_元帅 - 1] = policeByMarshalId;
				if (policeByMarshalId > 0) {
					try {
						Player p = pm.getPlayer(policeByMarshalId);
						if (p != null) {
							policeByMarshalName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long yushidafuByPrimeMinisterId = country.getYushidafuByPrimeMinisterId();
				ids[御史大夫_宰相 - 1] = yushidafuByPrimeMinisterId;
				if (yushidafuByPrimeMinisterId > 0) {
					try {
						Player p = pm.getPlayer(yushidafuByPrimeMinisterId);
						if (p != null) {
							yushidafuByPrimeMinisterName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long[] shouxunIds = country.getShouxunIds();
				if (shouxunIds != null) {
					shouxunNames = new String[shouxunIds.length];
					for (int i = 0; i < shouxunIds.length; i++) {
						shouxunNames[i] = "";
						try {
							shouxunNames[i] = pm.getPlayer(shouxunIds[i]).getName();
						} catch (Exception ex) {

						}
					}
				}
				long[] biaozhangIds = country.getBiaozhangIds();
				if (biaozhangIds != null) {
					biaozhangNames = new String[biaozhangIds.length];
					for (int i = 0; i < biaozhangIds.length; i++) {
						biaozhangNames[i] = "";
						try {
							biaozhangNames[i] = pm.getPlayer(biaozhangIds[i]).getName();
						} catch (Exception ex) {

						}
					}
				}
				int[] 俸禄 = country.get官员俸禄();
				try {
					for (int i = 0; i < 俸禄.length; i++) {
						int value = 俸禄[i];
						if (value > 0) {
							// 国王,大司马,大将军,元帅,宰相,巡捕国王任命,巡捕元帅任命,御史大夫国王任命,御史大夫宰相
							// 0未发放，1发放未领取，2已领取俸禄
							// 按照官职序号-1的顺序安排
							switch (i) {
							case 0:
								kingFengLuTake = 1;
								break;
							case 1:
								dasimaFengLuTake = 1;
								break;
							case 2:
								seniorGeneralFengLuTake = 1;
								break;
							case 3:
								marshalFengLuTake = 1;
								break;
							case 4:
								primeMinisterFengLuTake = 1;
								break;
							case 5:
								policeByKingFengLuTake = 1;
								break;
							case 6:
								policeByMarshalFengLuTake = 1;
								break;
							case 7:
								yushidafuByKingFengLuTake = 1;
								break;
							case 8:
								yushidafuByPrimeMinisterFengLuTake = 1;
								break;
							}
						} else {
							if (cm.data.已发放俸禄的国王Map.get(player.getCountry()) != null) {
								switch (i) {
								case 0:
									kingFengLuTake = 2;
									break;
								case 1:
									dasimaFengLuTake = 2;
									break;
								case 2:
									seniorGeneralFengLuTake = 2;
									break;
								case 3:
									marshalFengLuTake = 2;
									break;
								case 4:
									primeMinisterFengLuTake = 2;
									break;
								case 5:
									policeByKingFengLuTake = 2;
									break;
								case 6:
									policeByMarshalFengLuTake = 2;
									break;
								case 7:
									yushidafuByKingFengLuTake = 2;
									break;
								case 8:
									yushidafuByPrimeMinisterFengLuTake = 2;
									break;
								}
							}
						}
					}

				} catch (Exception ex) {

				}
				if (country.currentAllVote <= 0) {
					vote = "0%";
				} else {
					vote = (country.currentVote[0] * 100 / country.currentAllVote) + "%";
				}
				notice = country.getNotice();
				guozhanZiYuan = country.getGuozhanResource();
			}
			QUERY_COUNTRY_MAIN_PAGE_RES res = new QUERY_COUNTRY_MAIN_PAGE_RES(GameMessageFactory.nextSequnceNum(), countryName, kingName, dasimaName, seniorGeneralName, marshalName, primeMinisterName, policeByKingName, yushidafuByKingName, policeByMarshalName, yushidafuByPrimeMinisterName, ids, shouxunNames, biaozhangNames, kingFengLuTake, dasimaFengLuTake, seniorGeneralFengLuTake, marshalFengLuTake, primeMinisterFengLuTake, policeByKingFengLuTake, yushidafuByKingFengLuTake, policeByMarshalFengLuTake, yushidafuByPrimeMinisterFengLuTake, vote, notice, guozhanZiYuan);
			player.addMessageToRightBag(res);
		}
	}

	public void 发送官员名称(Player player) {
		CountryManager cm = CountryManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		if (cm != null && pm != null) {
			// String countryName = "";
			String kingName = "";
			String dasimaName = "";
			String seniorGeneralName = "";
			String marshalName = "";
			String primeMinisterName = "";
			String policeByKingName = "";
			String yushidafuByKingName = "";
			String policeByMarshalName = "";
			String yushidafuByPrimeMinisterName = "";
			// long[] ids = new long[9];
			Country country = cm.countryMap.get(player.getCountry());
			if (country != null) {
				// countryName = CountryManager.得到国家名(player.getCountry());
				long kingId = country.getKingId();
				// ids[国王 - 1] = kingId;
				if (kingId > 0) {
					try {
						Player p = pm.getPlayer(kingId);
						if (p != null) {
							kingName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long dasimaId = country.getDasimaId();
				// ids[大司马 - 1] = dasimaId;
				if (dasimaId > 0) {
					try {
						Player p = pm.getPlayer(dasimaId);
						if (p != null) {
							dasimaName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long seniorGeneralId = country.getSeniorGeneralId();
				// ids[大将军 - 1] = seniorGeneralId;
				if (seniorGeneralId > 0) {
					try {
						Player p = pm.getPlayer(seniorGeneralId);
						if (p != null) {
							seniorGeneralName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long marshalId = country.getMarshalId();
				// ids[元帅 - 1] = marshalId;
				if (marshalId > 0) {
					try {
						Player p = pm.getPlayer(marshalId);
						if (p != null) {
							marshalName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long primeMinisterId = country.getPrimeMinisterId();
				// ids[宰相 - 1] = primeMinisterId;
				if (primeMinisterId > 0) {
					try {
						Player p = pm.getPlayer(primeMinisterId);
						if (p != null) {
							primeMinisterName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long policeByKingId = country.getPoliceByKingId();
				// ids[巡捕_国王 - 1] = policeByKingId;
				if (policeByKingId > 0) {
					try {
						Player p = pm.getPlayer(policeByKingId);
						if (p != null) {
							policeByKingName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long yushidafuByKingId = country.getYushidafuByKingId();
				// ids[御史大夫_国王 - 1] = yushidafuByKingId;
				if (yushidafuByKingId > 0) {
					try {
						Player p = pm.getPlayer(yushidafuByKingId);
						if (p != null) {
							yushidafuByKingName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long policeByMarshalId = country.getPoliceByMarshalId();
				// ids[巡捕_元帅 - 1] = policeByMarshalId;
				if (policeByMarshalId > 0) {
					try {
						Player p = pm.getPlayer(policeByMarshalId);
						if (p != null) {
							policeByMarshalName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long yushidafuByPrimeMinisterId = country.getYushidafuByPrimeMinisterId();
				// ids[御史大夫_宰相 - 1] = yushidafuByPrimeMinisterId;
				if (yushidafuByPrimeMinisterId > 0) {
					try {
						Player p = pm.getPlayer(yushidafuByPrimeMinisterId);
						if (p != null) {
							yushidafuByPrimeMinisterName = p.getName();
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
			}

			QUERY_COUNTRY_OFFICER_RES res = new QUERY_COUNTRY_OFFICER_RES(GameMessageFactory.nextSequnceNum(), kingName, dasimaName, seniorGeneralName, marshalName, primeMinisterName, policeByKingName, yushidafuByKingName, policeByMarshalName, yushidafuByPrimeMinisterName);
			player.addMessageToRightBag(res);
		}
	}

	public void 得到投票结果(Player player) {
		Country country = this.countryMap.get(player.getCountry());
		PlayerManager pm = PlayerManager.getInstance();
		if (country != null) {
			String totalVoteRateDes = "";
			String[] officerNames = new String[9];
			String[] officerVoteRatesDes = new String[9];
			int allVote = 1;
			if (country.currentAllVote <= 0) {
				totalVoteRateDes = "0%";
			} else {
				allVote = country.currentAllVote;
				totalVoteRateDes = (country.currentVote[0] * 100 / country.currentAllVote) + "%";
			}
			StringBuffer sb = new StringBuffer();
			StringBuffer sbb = new StringBuffer();
			try {
				sb.append((country.getKingId() > 0 ? pm.getPlayer(country.getKingId()).getName() : ""));
				if (country.getKingId() > 0) {
					sbb.append(" " + (country.currentVote[0] * 100 / allVote) + "%" + (country.currentVote[0] * 100 / allVote > country.yesterdayVote[0] * 100 ? " ↑ " : (country.currentVote[0] * 100 / allVote < country.yesterdayVote[0] * 100 ? " ↓ " : " ↔ ")) + Translate.昨 + ": " + country.yesterdayVote[0] + "%");
				}
			} catch (Exception ex) {

			}
			officerNames[0] = sb.toString();
			officerVoteRatesDes[0] = sbb.toString();
			sb = new StringBuffer();
			sbb = new StringBuffer();
			try {
				sb.append((country.getDasimaId() > 0 ? pm.getPlayer(country.getDasimaId()).getName() : ""));
				if (country.getDasimaId() > 0) {
					sbb.append(" " + (country.currentVote[1] * 100 / allVote) + "%" + (country.currentVote[1] * 100 / allVote > country.yesterdayVote[1] * 100 ? " ↑ " : (country.currentVote[1] * 100 / allVote < country.yesterdayVote[1] * 100 ? " ↓ " : " ↔ ")) + Translate.昨 + ": " + country.yesterdayVote[1] + "%");
				}
			} catch (Exception ex) {

			}
			officerNames[1] = sb.toString();
			officerVoteRatesDes[1] = sbb.toString();
			sb = new StringBuffer();
			sbb = new StringBuffer();
			try {
				sb.append((country.getSeniorGeneralId() > 0 ? pm.getPlayer(country.getSeniorGeneralId()).getName() : ""));
				if (country.getSeniorGeneralId() > 0) {
					sbb.append(" " + (country.currentVote[2] * 100 / allVote) + "%" + (country.currentVote[2] * 100 / allVote > country.yesterdayVote[2] * 100 ? " ↑ " : (country.currentVote[2] * 100 / allVote < country.yesterdayVote[2] * 100 ? " ↓ " : " ↔ ")) + Translate.昨 + ": " + country.yesterdayVote[2] + "%");
				}
			} catch (Exception ex) {

			}
			officerNames[2] = sb.toString();
			officerVoteRatesDes[2] = sbb.toString();
			sb = new StringBuffer();
			sbb = new StringBuffer();
			try {
				sb.append((country.getMarshalId() > 0 ? pm.getPlayer(country.getMarshalId()).getName() : ""));
				if (country.getMarshalId() > 0) {
					sbb.append(" " + (country.currentVote[3] * 100 / allVote) + "%" + (country.currentVote[3] * 100 / allVote > country.yesterdayVote[3] * 100 ? " ↑ " : (country.currentVote[3] * 100 / allVote < country.yesterdayVote[3] * 100 ? " ↓ " : " ↔ ")) + Translate.昨 + ": " + country.yesterdayVote[3] + "%");
				}
			} catch (Exception ex) {

			}
			officerNames[3] = sb.toString();
			officerVoteRatesDes[3] = sbb.toString();
			sb = new StringBuffer();
			sbb = new StringBuffer();
			try {
				sb.append((country.getPrimeMinisterId() > 0 ? pm.getPlayer(country.getPrimeMinisterId()).getName() : ""));
				if (country.getPrimeMinisterId() > 0) {
					sbb.append(" " + (country.currentVote[4] * 100 / allVote) + "%" + (country.currentVote[4] * 100 / allVote > country.yesterdayVote[4] * 100 ? " ↑ " : (country.currentVote[4] * 100 / allVote < country.yesterdayVote[4] * 100 ? " ↓ " : " ↔ ")) + Translate.昨 + ": " + country.yesterdayVote[4] + "%");
				}
			} catch (Exception ex) {

			}
			officerNames[4] = sb.toString();
			officerVoteRatesDes[4] = sbb.toString();
			sb = new StringBuffer();
			sbb = new StringBuffer();
			try {
				sb.append((country.getPoliceByKingId() > 0 ? pm.getPlayer(country.getPoliceByKingId()).getName() : ""));
				if (country.getPoliceByKingId() > 0) {
					sbb.append(" " + (country.currentVote[5] * 100 / allVote) + "%" + (country.currentVote[5] * 100 / allVote > country.yesterdayVote[5] * 100 ? " ↑ " : (country.currentVote[5] * 100 / allVote < country.yesterdayVote[5] * 100 ? " ↓ " : " ↔ ")) + Translate.昨 + ": " + country.yesterdayVote[5] + "%");
				}
			} catch (Exception ex) {

			}
			officerNames[5] = sb.toString();
			officerVoteRatesDes[5] = sbb.toString();
			sb = new StringBuffer();
			sbb = new StringBuffer();
			try {
				sb.append((country.getPoliceByMarshalId() > 0 ? pm.getPlayer(country.getPoliceByMarshalId()).getName() : ""));
				if (country.getPoliceByMarshalId() > 0) {
					sbb.append(" " + (country.currentVote[6] * 100 / allVote) + "%" + (country.currentVote[6] * 100 / allVote > country.yesterdayVote[6] * 100 ? " ↑ " : (country.currentVote[6] * 100 / allVote < country.yesterdayVote[6] * 100 ? " ↓ " : " ↔ ")) + Translate.昨 + ": " + country.yesterdayVote[6] + "%");
				}
			} catch (Exception ex) {

			}
			officerNames[6] = sb.toString();
			officerVoteRatesDes[6] = sbb.toString();
			sb = new StringBuffer();
			sbb = new StringBuffer();
			try {
				sb.append((country.getYushidafuByKingId() > 0 ? pm.getPlayer(country.getYushidafuByKingId()).getName() : ""));
				if (country.getYushidafuByKingId() > 0) {
					sbb.append(" " + (country.currentVote[7] * 100 / allVote) + "%" + (country.currentVote[7] * 100 / allVote > country.yesterdayVote[7] * 100 ? " ↑ " : (country.currentVote[7] * 100 / allVote < country.yesterdayVote[7] * 100 ? " ↓ " : " ↔ ")) + Translate.昨 + ": " + country.yesterdayVote[7] + "%");
				}
			} catch (Exception ex) {

			}
			officerNames[7] = sb.toString();
			officerVoteRatesDes[7] = sbb.toString();
			sb = new StringBuffer();
			sbb = new StringBuffer();
			try {
				sb.append((country.getYushidafuByPrimeMinisterId() > 0 ? pm.getPlayer(country.getYushidafuByPrimeMinisterId()).getName() : ""));
				if (country.getYushidafuByPrimeMinisterId() > 0) {
					sbb.append(" " + (country.currentVote[8] * 100 / allVote) + "%" + (country.currentVote[8] * 100 / allVote > country.yesterdayVote[8] * 100 ? " ↑ " : (country.currentVote[8] * 100 / allVote < country.yesterdayVote[8] * 100 ? " ↓ " : " ↔ ")) + Translate.昨 + ": " + country.yesterdayVote[8] + "%");
				}
			} catch (Exception ex) {

			}
			officerNames[8] = sb.toString();
			officerVoteRatesDes[8] = sbb.toString();
			QUERY_COUNTRY_VOTE_RES res = new QUERY_COUNTRY_VOTE_RES(GameMessageFactory.nextSequnceNum(), totalVoteRateDes, officerNames, officerVoteRatesDes);
			player.addMessageToRightBag(res);
		}
	}

	public void 返回任命或罢免国家官员列表(Player player) {
		返回查询任命或罢免国家官员列表(player);
	}

	public void 返回授勋或撤销国家官员列表(Player player) {
		返回查询任命或罢免国家官员列表(player);
	}

	public void 返回表彰或撤销国家官员列表(Player player) {
		返回查询任命或罢免国家官员列表(player);
	}

	public void 返回查询任命或罢免国家官员列表(Player player) {
		PlayerManager pm = PlayerManager.getInstance();
		CountryManager cm = CountryManager.getInstance();
		String[] shouxunNames = new String[0];
		String[] biaozhangNames = new String[0];
		Country country = cm.countryMap.get(player.getCountry());
		ArrayList<CountryInfoForClient> countryInfoForClients = new ArrayList<CountryInfoForClient>();
		if (country != null) {
			long[] shouxunIds = country.getShouxunIds();
			if (shouxunIds != null) {
				shouxunNames = new String[shouxunIds.length];
				for (int i = 0; i < shouxunIds.length; i++) {
					shouxunNames[i] = "";
					try {
						shouxunNames[i] = pm.getPlayer(shouxunIds[i]).getName();
					} catch (Exception ex) {

					}
				}
			}
			long[] biaozhangIds = country.getBiaozhangIds();
			if (biaozhangIds != null) {
				biaozhangNames = new String[biaozhangIds.length];
				for (int i = 0; i < biaozhangIds.length; i++) {
					biaozhangNames[i] = "";
					try {
						biaozhangNames[i] = pm.getPlayer(biaozhangIds[i]).getName();
					} catch (Exception ex) {

					}
				}
			}
			long dasimaId = country.getDasimaId();
			if (dasimaId > 0) {
				try {
					Player p = pm.getPlayer(dasimaId);
					if (p != null) {
						CountryInfoForClient cif = new CountryInfoForClient();
						cif.setCountryPosition(大司马);
						cif.setPlayerId(p.getId());
						cif.setPlayerName(p.getName());
						cif.setLevel(p.getMainSoul().getGrade());
						cif.setZongPaiName(p.getZongPaiName());
						cif.setOnLine(p.isOnline());
						for (String name : shouxunNames) {
							if (p.getName().equals(name)) {
								cif.setShouxun(true);
								break;
							}
						}
						for (String name : biaozhangNames) {
							if (p.getName().equals(name)) {
								cif.setBiaozhang(true);
								break;
							}
						}
						countryInfoForClients.add(cif);
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[查询任命或罢免国家官员列表] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
				}
			}
			long seniorGeneralId = country.getSeniorGeneralId();
			if (seniorGeneralId > 0) {
				try {
					Player p = pm.getPlayer(seniorGeneralId);
					if (p != null) {
						CountryInfoForClient cif = new CountryInfoForClient();
						cif.setCountryPosition(大将军);
						cif.setPlayerId(p.getId());
						cif.setPlayerName(p.getName());
						cif.setLevel(p.getMainSoul().getGrade());
						cif.setZongPaiName(p.getZongPaiName());
						cif.setOnLine(p.isOnline());
						for (String name : shouxunNames) {
							if (p.getName().equals(name)) {
								cif.setShouxun(true);
								break;
							}
						}
						for (String name : biaozhangNames) {
							if (p.getName().equals(name)) {
								cif.setBiaozhang(true);
								break;
							}
						}
						countryInfoForClients.add(cif);
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[查询任命或罢免国家官员列表] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
				}
			}
			long marshalId = country.getMarshalId();
			if (marshalId > 0) {
				try {
					Player p = pm.getPlayer(marshalId);
					if (p != null) {
						CountryInfoForClient cif = new CountryInfoForClient();
						cif.setCountryPosition(元帅);
						cif.setPlayerId(p.getId());
						cif.setPlayerName(p.getName());
						cif.setLevel(p.getMainSoul().getGrade());
						cif.setZongPaiName(p.getZongPaiName());
						cif.setOnLine(p.isOnline());
						for (String name : shouxunNames) {
							if (p.getName().equals(name)) {
								cif.setShouxun(true);
								break;
							}
						}
						for (String name : biaozhangNames) {
							if (p.getName().equals(name)) {
								cif.setBiaozhang(true);
								break;
							}
						}
						countryInfoForClients.add(cif);
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[查询任命或罢免国家官员列表] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
				}
			}
			long primeMinisterId = country.getPrimeMinisterId();
			if (primeMinisterId > 0) {
				try {
					Player p = pm.getPlayer(primeMinisterId);
					if (p != null) {
						CountryInfoForClient cif = new CountryInfoForClient();
						cif.setCountryPosition(宰相);
						cif.setPlayerId(p.getId());
						cif.setPlayerName(p.getName());
						cif.setLevel(p.getMainSoul().getGrade());
						cif.setZongPaiName(p.getZongPaiName());
						cif.setOnLine(p.isOnline());
						for (String name : shouxunNames) {
							if (p.getName().equals(name)) {
								cif.setShouxun(true);
								break;
							}
						}
						for (String name : biaozhangNames) {
							if (p.getName().equals(name)) {
								cif.setBiaozhang(true);
								break;
							}
						}
						countryInfoForClients.add(cif);
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[查询任命或罢免国家官员列表] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
				}
			}
			long policeByKingId = country.getPoliceByKingId();
			if (policeByKingId > 0) {
				try {
					Player p = pm.getPlayer(policeByKingId);
					if (p != null) {
						CountryInfoForClient cif = new CountryInfoForClient();
						cif.setCountryPosition(巡捕_国王);
						cif.setPlayerId(p.getId());
						cif.setPlayerName(p.getName());
						cif.setLevel(p.getMainSoul().getGrade());
						cif.setZongPaiName(p.getZongPaiName());
						cif.setOnLine(p.isOnline());
						for (String name : shouxunNames) {
							if (p.getName().equals(name)) {
								cif.setShouxun(true);
								break;
							}
						}
						for (String name : biaozhangNames) {
							if (p.getName().equals(name)) {
								cif.setBiaozhang(true);
								break;
							}
						}
						countryInfoForClients.add(cif);
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[查询任命或罢免国家官员列表] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
				}
			}
			long yushidafuByKingId = country.getYushidafuByKingId();
			if (yushidafuByKingId > 0) {
				try {
					Player p = pm.getPlayer(yushidafuByKingId);
					if (p != null) {
						CountryInfoForClient cif = new CountryInfoForClient();
						cif.setCountryPosition(御史大夫_国王);
						cif.setPlayerId(p.getId());
						cif.setPlayerName(p.getName());
						cif.setLevel(p.getMainSoul().getGrade());
						cif.setZongPaiName(p.getZongPaiName());
						cif.setOnLine(p.isOnline());
						for (String name : shouxunNames) {
							if (p.getName().equals(name)) {
								cif.setShouxun(true);
								break;
							}
						}
						for (String name : biaozhangNames) {
							if (p.getName().equals(name)) {
								cif.setBiaozhang(true);
								break;
							}
						}
						countryInfoForClients.add(cif);
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[查询任命或罢免国家官员列表] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
				}
			}
			long policeByMarshalId = country.getPoliceByMarshalId();
			if (policeByMarshalId > 0) {
				try {
					Player p = pm.getPlayer(policeByMarshalId);
					if (p != null) {
						CountryInfoForClient cif = new CountryInfoForClient();
						cif.setCountryPosition(巡捕_元帅);
						cif.setPlayerId(p.getId());
						cif.setPlayerName(p.getName());
						cif.setLevel(p.getMainSoul().getGrade());
						cif.setZongPaiName(p.getZongPaiName());
						cif.setOnLine(p.isOnline());
						for (String name : shouxunNames) {
							if (p.getName().equals(name)) {
								cif.setShouxun(true);
								break;
							}
						}
						for (String name : biaozhangNames) {
							if (p.getName().equals(name)) {
								cif.setBiaozhang(true);
								break;
							}
						}
						countryInfoForClients.add(cif);
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[查询任命或罢免国家官员列表] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
				}
			}
			long yushidafuByPrimeMinisterId = country.getYushidafuByPrimeMinisterId();
			if (yushidafuByPrimeMinisterId > 0) {
				try {
					Player p = pm.getPlayer(yushidafuByPrimeMinisterId);
					if (p != null) {
						CountryInfoForClient cif = new CountryInfoForClient();
						cif.setCountryPosition(御史大夫_宰相);
						cif.setPlayerId(p.getId());
						cif.setPlayerName(p.getName());
						cif.setLevel(p.getMainSoul().getGrade());
						cif.setZongPaiName(p.getZongPaiName());
						cif.setOnLine(p.isOnline());
						for (String name : shouxunNames) {
							if (p.getName().equals(name)) {
								cif.setShouxun(true);
								break;
							}
						}
						for (String name : biaozhangNames) {
							if (p.getName().equals(name)) {
								cif.setBiaozhang(true);
								break;
							}
						}
						countryInfoForClients.add(cif);
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[查询任命或罢免国家官员列表] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
				}
			}
			long huguogongId = country.getHuguogongId();
			if (huguogongId > 0) {
				try {
					Player p = pm.getPlayer(huguogongId);
					if (p != null) {
						CountryInfoForClient cif = new CountryInfoForClient();
						cif.setCountryPosition(护国公);
						cif.setPlayerId(p.getId());
						cif.setPlayerName(p.getName());
						cif.setLevel(p.getMainSoul().getGrade());
						cif.setZongPaiName(p.getZongPaiName());
						cif.setOnLine(p.isOnline());
						for (String name : shouxunNames) {
							if (p.getName().equals(name)) {
								cif.setShouxun(true);
								break;
							}
						}
						for (String name : biaozhangNames) {
							if (p.getName().equals(name)) {
								cif.setBiaozhang(true);
								break;
							}
						}
						countryInfoForClients.add(cif);
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[查询任命或罢免国家官员列表] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
				}
			}
			long fuguogongId = country.getFuguogongId();
			if (fuguogongId > 0) {
				try {
					Player p = pm.getPlayer(fuguogongId);
					if (p != null) {
						CountryInfoForClient cif = new CountryInfoForClient();
						cif.setCountryPosition(辅国公);
						cif.setPlayerId(p.getId());
						cif.setPlayerName(p.getName());
						cif.setLevel(p.getMainSoul().getGrade());
						cif.setZongPaiName(p.getZongPaiName());
						cif.setOnLine(p.isOnline());
						for (String name : shouxunNames) {
							if (p.getName().equals(name)) {
								cif.setShouxun(true);
								break;
							}
						}
						for (String name : biaozhangNames) {
							if (p.getName().equals(name)) {
								cif.setBiaozhang(true);
								break;
							}
						}
						countryInfoForClients.add(cif);
					}
				} catch (Exception ex) {
					if (logger.isWarnEnabled()) logger.warn("[查询任命或罢免国家官员列表] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
				}
			}
			long[] yulinjunIds = country.getYulinjunIds();
			if (yulinjunIds != null) {
				for (int i = 0; i < yulinjunIds.length; i++) {
					try {
						Player p = pm.getPlayer(yulinjunIds[i]);
						if (p != null) {
							CountryInfoForClient cif = new CountryInfoForClient();
							cif.setCountryPosition(御林卫队);
							cif.setPlayerId(p.getId());
							cif.setPlayerName(p.getName());
							cif.setLevel(p.getMainSoul().getGrade());
							cif.setZongPaiName(p.getZongPaiName());
							cif.setOnLine(p.isOnline());
							for (String name : shouxunNames) {
								if (p.getName().equals(name)) {
									cif.setShouxun(true);
									break;
								}
							}
							for (String name : biaozhangNames) {
								if (p.getName().equals(name)) {
									cif.setBiaozhang(true);
									break;
								}
							}
							countryInfoForClients.add(cif);
						}
					} catch (Exception ex) {

					}
				}
			}
		}

		QUERY_COUNTRY_COMMISSION_OR_RECALL_RES res = new QUERY_COUNTRY_COMMISSION_OR_RECALL_RES(GameMessageFactory.nextSequnceNum(), countryInfoForClients.toArray(new CountryInfoForClient[0]));
		player.addMessageToRightBag(res);
	}

	public void 得到王者之印res(Player player) {
		CountryManager cm = CountryManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		if (cm != null && pm != null) {
			String countryName = "";
			String kingName = "";
			String dasimaName = "";
			String seniorGeneralName = "";
			String marshalName = "";
			String primeMinisterName = "";
			String policeByKingName = "";
			String yushidafuByKingName = "";
			String policeByMarshalName = "";
			String yushidafuByPrimeMinisterName = "";
			long[] kingWangZhe = new long[2];
			long[] dasimaWangZhe = new long[2];
			long[] seniorGeneralWangZhe = new long[2];
			long[] marshalWangZhe = new long[2];
			long[] primeMinisterWangZhe = new long[2];
			long[] policeByKingWangZhe = new long[2];
			long[] yushidafuByKingWangZhe = new long[2];
			long[] policeByMarshalWangZhe = new long[2];
			long[] yushidafuByPrimeMinisterWangZhe = new long[2];
			Country country = cm.countryMap.get(player.getCountry());
			if (country != null) {
				countryName = CountryManager.得到国家名(player.getCountry());
				long kingId = country.getKingId();
				if (kingId > 0) {
					try {
						Player p = pm.getPlayer(kingId);
						if (p != null) {
							kingName = p.getName();
							if (cm.data.使用王者之印的次数Map.get(p.getId()) != null) {
								kingWangZhe[0] = cm.data.使用王者之印的次数Map.get(p.getId());
							}
							if (cm.data.使用王者之印的总次数Map.get(p.getId()) != null) {
								kingWangZhe[1] = cm.data.使用王者之印的总次数Map.get(p.getId());
							}
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long dasimaId = country.getDasimaId();
				if (dasimaId > 0) {
					try {
						Player p = pm.getPlayer(dasimaId);
						if (p != null) {
							dasimaName = p.getName();
							if (cm.data.使用王者之印的次数Map.get(p.getId()) != null) {
								dasimaWangZhe[0] = cm.data.使用王者之印的次数Map.get(p.getId());
							}
							if (cm.data.使用王者之印的总次数Map.get(p.getId()) != null) {
								dasimaWangZhe[1] = cm.data.使用王者之印的总次数Map.get(p.getId());
							}
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long seniorGeneralId = country.getSeniorGeneralId();
				if (seniorGeneralId > 0) {
					try {
						Player p = pm.getPlayer(seniorGeneralId);
						if (p != null) {
							seniorGeneralName = p.getName();
							if (cm.data.使用王者之印的次数Map.get(p.getId()) != null) {
								seniorGeneralWangZhe[0] = cm.data.使用王者之印的次数Map.get(p.getId());
							}
							if (cm.data.使用王者之印的总次数Map.get(p.getId()) != null) {
								seniorGeneralWangZhe[1] = cm.data.使用王者之印的总次数Map.get(p.getId());
							}
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long marshalId = country.getMarshalId();
				if (marshalId > 0) {
					try {
						Player p = pm.getPlayer(marshalId);
						if (p != null) {
							marshalName = p.getName();
							if (cm.data.使用王者之印的次数Map.get(p.getId()) != null) {
								marshalWangZhe[0] = cm.data.使用王者之印的次数Map.get(p.getId());
							}
							if (cm.data.使用王者之印的总次数Map.get(p.getId()) != null) {
								marshalWangZhe[1] = cm.data.使用王者之印的总次数Map.get(p.getId());
							}
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long primeMinisterId = country.getPrimeMinisterId();
				if (primeMinisterId > 0) {
					try {
						Player p = pm.getPlayer(primeMinisterId);
						if (p != null) {
							primeMinisterName = p.getName();
							if (cm.data.使用王者之印的次数Map.get(p.getId()) != null) {
								primeMinisterWangZhe[0] = cm.data.使用王者之印的次数Map.get(p.getId());
							}
							if (cm.data.使用王者之印的总次数Map.get(p.getId()) != null) {
								primeMinisterWangZhe[1] = cm.data.使用王者之印的总次数Map.get(p.getId());
							}
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long policeByKingId = country.getPoliceByKingId();
				if (policeByKingId > 0) {
					try {
						Player p = pm.getPlayer(policeByKingId);
						if (p != null) {
							policeByKingName = p.getName();
							if (cm.data.使用王者之印的次数Map.get(p.getId()) != null) {
								policeByKingWangZhe[0] = cm.data.使用王者之印的次数Map.get(p.getId());
							}
							if (cm.data.使用王者之印的总次数Map.get(p.getId()) != null) {
								policeByKingWangZhe[1] = cm.data.使用王者之印的总次数Map.get(p.getId());
							}
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long yushidafuByKingId = country.getYushidafuByKingId();
				if (yushidafuByKingId > 0) {
					try {
						Player p = pm.getPlayer(yushidafuByKingId);
						if (p != null) {
							yushidafuByKingName = p.getName();
							if (cm.data.使用王者之印的次数Map.get(p.getId()) != null) {
								yushidafuByKingWangZhe[0] = cm.data.使用王者之印的次数Map.get(p.getId());
							}
							if (cm.data.使用王者之印的总次数Map.get(p.getId()) != null) {
								yushidafuByKingWangZhe[1] = cm.data.使用王者之印的总次数Map.get(p.getId());
							}
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long policeByMarshalId = country.getPoliceByMarshalId();
				if (policeByMarshalId > 0) {
					try {
						Player p = pm.getPlayer(policeByMarshalId);
						if (p != null) {
							policeByMarshalName = p.getName();
							if (cm.data.使用王者之印的次数Map.get(p.getId()) != null) {
								policeByMarshalWangZhe[0] = cm.data.使用王者之印的次数Map.get(p.getId());
							}
							if (cm.data.使用王者之印的总次数Map.get(p.getId()) != null) {
								policeByMarshalWangZhe[1] = cm.data.使用王者之印的总次数Map.get(p.getId());
							}
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
				long yushidafuByPrimeMinisterId = country.getYushidafuByPrimeMinisterId();
				if (yushidafuByPrimeMinisterId > 0) {
					try {
						Player p = pm.getPlayer(yushidafuByPrimeMinisterId);
						if (p != null) {
							yushidafuByPrimeMinisterName = p.getName();
							if (cm.data.使用王者之印的次数Map.get(p.getId()) != null) {
								yushidafuByPrimeMinisterWangZhe[0] = cm.data.使用王者之印的次数Map.get(p.getId());
							}
							if (cm.data.使用王者之印的总次数Map.get(p.getId()) != null) {
								yushidafuByPrimeMinisterWangZhe[1] = cm.data.使用王者之印的总次数Map.get(p.getId());
							}
						}
					} catch (Exception ex) {
						if (logger.isWarnEnabled()) logger.warn("[查询国家主页面] [异常] [" + player.getUsername() + "] [" + player.getId() + "] [" + player.getName() + "]", ex);
					}
				}
			}
			QUERY_COUNTRY_WANGZHEZHIYIN_RES res = new QUERY_COUNTRY_WANGZHEZHIYIN_RES(GameMessageFactory.nextSequnceNum(), countryName, kingName, dasimaName, seniorGeneralName, marshalName, primeMinisterName, policeByKingName, yushidafuByKingName, policeByMarshalName, yushidafuByPrimeMinisterName, kingWangZhe, dasimaWangZhe, seniorGeneralWangZhe, marshalWangZhe, primeMinisterWangZhe, policeByKingWangZhe, yushidafuByKingWangZhe, policeByMarshalWangZhe, yushidafuByPrimeMinisterWangZhe);
			player.addMessageToRightBag(res);
		}
	}

	public void 得到御林卫队res(Player player) {
		CountryManager cm = CountryManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		JiazuManager jm = JiazuManager.getInstance();
		if (cm != null && pm != null && jm != null) {
			String countryName = "";
			String[] yunlinjunNames = new String[0];
			String[] jiazuNames = new String[0];
			Country country = cm.countryMap.get(player.getCountry());
			if (country != null) {
				countryName = CountryManager.得到国家名(player.getCountry());
				if (country.getYulinjunIds() != null) {
					yunlinjunNames = new String[country.getYulinjunIds().length];
					jiazuNames = new String[country.getYulinjunIds().length];
					for (int i = 0; i < country.getYulinjunIds().length; i++) {
						long id = country.getYulinjunIds()[i];
						if (id <= 0) {
							yunlinjunNames[i] = "";
							jiazuNames[i] = "";
						} else {
							try {
								Player p = pm.getPlayer(id);
								yunlinjunNames[i] = p.getName();
								if (jm.getJiazu(p.getJiazuId()) != null) {
									jiazuNames[i] = jm.getJiazu(p.getJiazuId()).getName();
								} else {
									jiazuNames[i] = "";
								}
							} catch (Exception ex) {
								yunlinjunNames[i] = "";
								jiazuNames[i] = "";
							}
						}
					}
				}
			}
			QUERY_COUNTRY_YULINJUN_RES res = new QUERY_COUNTRY_YULINJUN_RES(GameMessageFactory.nextSequnceNum(), countryName, yunlinjunNames, jiazuNames);
			player.addMessageToRightBag(res);
		}
	}

	public void 占领王城(Player player) {
		if (player == null) {
			if (logger.isInfoEnabled()) logger.info("[国王产生] [失败] [player == null]");
			return;
		}
		JiazuManager jm = JiazuManager.getInstance();
		if (jm == null) {
			if (logger.isInfoEnabled()) logger.info("[国王产生] [失败] [jm == null]");
			return;
		}
		if (player.getJiazuId() < 0) {
			if (logger.isInfoEnabled()) logger.info("[国王产生] [失败] [player.getJiazuId() < 0]");
			return;
		}
		Jiazu jiazu = jm.getJiazu(player.getJiazuId());
		if (jiazu == null) {
			if (logger.isInfoEnabled()) logger.info("[国王产生] [失败] [jiazu == null]");
			return;
		}
		if (jiazu.getZongPaiId() < 0) {
			if (logger.isInfoEnabled()) logger.info("[国王产生] [失败] [jiazu.getZongPaiId() < 0]");
			return;
		}
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		if (zpm == null) {
			if (logger.isInfoEnabled()) logger.info("[国王产生] [失败] [zpm == null]");
			return;
		}
		ZongPai zp = zpm.getZongPaiById(jiazu.getZongPaiId());
		if (zp == null) {
			if (logger.isInfoEnabled()) logger.info("[国王产生] [失败] [zp == null]");
			return;
		}
		Player p = null;
		PlayerManager pm = PlayerManager.getInstance();
		try {
			p = pm.getPlayer(zp.getMasterId());
		} catch (Exception ex) {

		}
		if (p == null) {
			if (logger.isInfoEnabled()) logger.info("[国王产生] [失败] [p == null]");
			return;
		}
		Country country = this.countryMap.get(p.getCountry());
		if (country == null) {
			if (logger.isInfoEnabled()) logger.info("[国王产生] [失败] [country == null]");
			return;
		}
		int 官职B = 官职(p.getCountry(), p.getId());
		if (官职B >= 0) {
			if (官职B == 巡捕_国王) {
				country.setPoliceByKingId(-1);
			}
			if (官职B == 巡捕_元帅) {
				country.setPoliceByMarshalId(-1);
			}
			if (官职B == 御史大夫_国王) {
				country.setYushidafuByKingId(-1);
			}
			if (官职B == 御史大夫_宰相) {
				country.setYushidafuByPrimeMinisterId(-1);
			}
			if (官职B == 大司马) {
				long id = country.getDasimaId();
				if (id == p.getId()) {
					country.setDasimaId(-1);
				}
			}
			if (官职B == 大将军) {
				long id = country.getSeniorGeneralId();
				if (id == p.getId()) {
					country.setSeniorGeneralId(-1);
				}
			}
			if (官职B == 元帅) {
				long id = country.getMarshalId();
				if (id == p.getId()) {
					country.setMarshalId(-1);
				}
			}
			if (官职B == 宰相) {
				long id = country.getPrimeMinisterId();
				if (id == p.getId()) {
					country.setPrimeMinisterId(-1);
				}
			}
			if (官职B == 护国公) {
				long id = country.getHuguogongId();
				if (id == p.getId()) {
					country.setHuguogongId(-1);
				}
			}
			if (官职B == 辅国公) {
				long id = country.getFuguogongId();
				if (id == p.getId()) {
					country.setFuguogongId(-1);
				}
			}
			if (官职B == 御林卫队) {
				long[] ids = country.getYulinjunIds();
				if (ids != null) {
					for (int i = 0; i < ids.length; i++) {
						if (ids[i] == p.getId()) {
							ids[i] = -1;
							country.setYulinjunIds(ids);
							break;
						}
					}
				}
			}
		}
		String oldKing = null;
		if (country.getKingId() > 0) {
			try {
				oldKing = pm.getPlayer(country.getKingId()).getName();
				pm.getPlayer(country.getKingId()).setCountryPosition(0);
				country.成为国家官员时间Map.remove(country.getKingId());
			} catch (Exception ex) {

			}
		}
		String description = "";
		country.setKingId(p.getId());
		p.setCountryPosition(国王);
		if (country.成为国家官员时间Map.get(p.getId()) == null) {
			country.成为国家官员时间Map.put(p.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		}
		country.setDirty(true);
		if (oldKing == null) {
			description = Translate.translateString(Translate.国王诞生详细, new String[][] { { Translate.STRING_1, this.得到国家名(p.getCountry()) }, { Translate.STRING_2, this.得到官职名(国王) }, { Translate.PLAYER_NAME_1, p.getName() } });
		} else {
			description = Translate.translateString(Translate.国王更替详细, new String[][] { { Translate.STRING_1, this.得到国家名(p.getCountry()) }, { Translate.STRING_2, this.得到官职名(国王) }, { Translate.PLAYER_NAME_1, p.getName() }, { Translate.PLAYER_NAME_2, oldKing } });
		}
		try {
			ChatMessageService cms = ChatMessageService.getInstance();
			ChatMessage msg = new ChatMessage();
			msg.setMessageText("<f color='0x14ff00'>" + description + "</f>");
			cms.sendRoolMessageToSystem(msg);
			cms.sendRoolMessageToSystem(msg);
			cms.sendRoolMessageToSystem(msg);
			if (AchievementManager.getInstance() != null) {
				AchievementManager.getInstance().record(player, RecordAction.连任国王次数, 1);
			}
			// logger.info("[国王产生] ["+country.getName()+"] ["+description+"]");
			if (logger.isInfoEnabled()) logger.info("[国王产生] [{}] [{}]", new Object[] { country.getName(), description });
		} catch (Exception ex) {
			if (logger.isInfoEnabled()) logger.info("[国王产生] [异常]", ex);
		}
	}

	public void 弹出策划指定的官员相关权利窗口(Player player) {
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.官员相关权利);
		mw.setDescriptionInUUB(Translate.官员相关权利);
		Option_Country_qiujin option1 = new Option_Country_qiujin();
		option1.setText(Translate.囚禁);
		Option_Country_shifang option2 = new Option_Country_shifang();
		option2.setText(Translate.释放);
		Option_Country_jinyan option3 = new Option_Country_jinyan();
		option3.setText(Translate.禁言);
		Option_Country_ciguan option4 = new Option_Country_ciguan();
		option4.setText(Translate.辞官);
		Option_UseCancel cancel = new Option_UseCancel();
		cancel.setText(Translate.取消);
		Option[] options = new Option[] { option1, option2, option3, option4, cancel };
		mw.setOptions(options);
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, options);
		player.addMessageToRightBag(res);
	}

	public static void main(String[] a) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		if ((hour == 20 && minute == 7) || (hour == 20 && minute <= 10)) {
		} else {
		}
	}

	/**
	 * 通过countryId获得一个国家
	 * @param countryId
	 * @return
	 */
	public Country getCountryByCountryId(byte countryId) {
		return countryMap.get(countryId);
	}

	public void 罢免国王(byte country) {
		Country c = this.countryMap.get(country);
		if (c != null) {
			long id = c.getKingId();
			if (id > 0) {
				try {
					c.setKingId(0);
					c.成为国家官员时间Map.remove(id);
					PlayerManager pm = PlayerManager.getInstance();
					pm.getPlayer(id).setCountryPosition(0);
					c.setDirty(true);
					logger.warn("[罢免国王] [成功] [" + id + "] [" + country + "]");
				} catch (Exception ex) {
					logger.error("[罢免国王] [异常] [" + id + "] [" + country + "]", ex);
				}
			}
		}
	}

	/**
	 * 获得排行后的国家列表
	 * @return
	 */
	public Country[] getSortedCountrys() {
		long start = System.currentTimeMillis();
		try {
			Country cs[] = this.countryMap.values().toArray(new Country[0]);
			Arrays.sort(cs, new Comparator<Country>() {

				@Override
				public int compare(Country o1, Country o2) {
					// TODO Auto-generated method stub
					long point1 = 0;
					long point2 = 0;
					// 国战次数
					GuozhanOrganizer org = GuozhanOrganizer.getInstance();
					int guozhanNum = 0;
					List<GuozhanHistory> hlist = org.getCountryGuozhanHistory(o1);
					if (hlist != null) {
						guozhanNum = hlist.size();
					}
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_YEAR, -1);
					cal.set(Calendar.HOUR, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					long startTime = cal.getTimeInMillis();
					cal.add(Calendar.DAY_OF_YEAR, 1);
					long endTime = cal.getTimeInMillis();

					if (guozhanNum < 15) {
						long playerLoginNum = PlayerManager.getInstance().getPlayerNumByLastingRequestTime(o1.getCountryId(), startTime, endTime);
						GuozhanStat stat = org.getGuozhanStat(o1.getCountryId());
						long winNum = 0;
						if (stat != null) {
							winNum = stat.getWinNum();
						}
						if (guozhanNum == 0) {
							guozhanNum = 1;
						}
						point1 = playerLoginNum + (long) (new Double(winNum) / new Double(guozhanNum) * 5000);
					} else {
						long playerLoginNum = PlayerManager.getInstance().getPlayerNumByLastingRequestTime(o1.getCountryId(), startTime, endTime);
						List<GuozhanHistory> historyList = org.getGuozhanHistoryList();
						int num = 15;
						long winNum = 0;
						for (int i = historyList.size() - 1; i >= 0 && num > 0; i--) {
							GuozhanHistory h = historyList.get(i);
							if ((h.getAttackCountryName().equals(o1.getName()) && h.getResult() == 0) || (h.getDefendCountryName().equals(o1.getName()) && h.getResult() == 1)) {
								winNum++;
							}
							num--;
						}
						point1 = playerLoginNum + (long) (new Double(winNum) / new Double(15) * 5000);
					}

					// country2:
					hlist = org.getCountryGuozhanHistory(o2);
					if (hlist != null) {
						guozhanNum = hlist.size();
					}
					if (guozhanNum < 15) {
						long playerLoginNum = PlayerManager.getInstance().getPlayerNumByLastingRequestTime(o2.getCountryId(), startTime, endTime);
						GuozhanStat stat = org.getGuozhanStat(o2.getCountryId());
						long winNum = 0;
						if (stat != null) {
							winNum = stat.getWinNum();
						}
						if (guozhanNum == 0) {
							guozhanNum = 1;
						}
						point2 = playerLoginNum + (long) (new Double(winNum) / new Double(guozhanNum) * 5000);
					} else {
						long playerLoginNum = PlayerManager.getInstance().getPlayerNumByLastingRequestTime(o2.getCountryId(), startTime, endTime);
						List<GuozhanHistory> historyList = org.getGuozhanHistoryList();
						int num = 15;
						long winNum = 0;
						for (int i = historyList.size() - 1; i >= 0 && num > 0; i--) {
							GuozhanHistory h = historyList.get(i);
							if ((h.getAttackCountryName().equals(o2.getName()) && h.getResult() == 0) || (h.getDefendCountryName().equals(o2.getName()) && h.getResult() == 1)) {
								winNum++;
							}
							num--;
						}
						point2 = playerLoginNum + (long) (new Double(winNum) / new Double(15) * 5000);
					}

					if (point1 > point2) {
						return -1;
					} else if (point2 > point1) {
						return 1;
					}
					return 0;
				}

			});

			if (logger.isInfoEnabled()) {
				logger.info("[国家排序] [国家数量:" + cs.length + "] [1:" + cs[0].getName() + "] [2:" + cs[1].getName() + "] [3:" + cs[2].getName() + "] [" + (System.currentTimeMillis() - start) + "ms]");
			}
			return cs;
		} catch (Exception e) {
			logger.error("为国家按战力排序时发生异常", e);
		}
		return new Country[0];
	}

	public QUERY_COUNTRY_QIUJIN_JINYAN_RES 得到囚禁和禁言的人(RequestMessage message, Player player) {
		ArrayList<CountryQiujinAndJinyanForClient> countryQiujinAndJinyanForClients = new ArrayList<CountryQiujinAndJinyanForClient>();
		PlayerManager pm = PlayerManager.getInstance();
		Player[] ps = pm.getOnlinePlayerByCountry(player.getCountry());
		long now = System.currentTimeMillis();
		if (ps != null) {
			for (Player p : ps) {
				if (p != null) {
					if (p.isInPrison() || p.isJinyan()) {
						CountryQiujinAndJinyanForClient cqajfc = new CountryQiujinAndJinyanForClient();
						cqajfc.setPlayerId(p.getId());
						cqajfc.setPlayerName(p.getName());
						if (p.isJinyan()) {
							Buff buff = p.getBuffByName(CountryManager.禁言buff名称);
							if (buff != null) {
								cqajfc.setLastJinyanTime(buff.getInvalidTime() - now);
							}
						}
						if (p.isInPrison()) {
							Buff buff = p.getBuffByName(CountryManager.囚禁buff名称);
							if (buff != null) {
								cqajfc.setLastQiujinTime(buff.getInvalidTime() - now);
							}
						}
						countryQiujinAndJinyanForClients.add(cqajfc);
					}
				}
			}
		}
		if (message != null) {
			QUERY_COUNTRY_QIUJIN_JINYAN_RES res = new QUERY_COUNTRY_QIUJIN_JINYAN_RES(message.getSequnceNum(), countryQiujinAndJinyanForClients.toArray(new CountryQiujinAndJinyanForClient[0]));
			return res;
		} else {
			QUERY_COUNTRY_QIUJIN_JINYAN_RES res = new QUERY_COUNTRY_QIUJIN_JINYAN_RES(GameMessageFactory.nextSequnceNum(), countryQiujinAndJinyanForClients.toArray(new CountryQiujinAndJinyanForClient[0]));
			return res;
		}
	}

	/**
	 * 是否可以享受授勋奖励
	 * @param p
	 * @return
	 */
	public boolean isShouXunReward(Player p) {
		if (p == null || p.getCountryPosition() <= 0) {
			return false;
		}
		Country country = countryMap.get(p.getCountry());
		if (country != null) {
			long[] ids = country.getShouxunIds();
			boolean has = false;
			if (ids != null) {
				for (long id : ids) {
					if (id == p.getId()) {
						has = true;
					}
				}
			}
			if (has) {
				Long time = country.成为授勋官员时间Map.get(p.getId());
				if (time != null) {
					if (System.currentTimeMillis() - time >= 24 * 1l * 3600 * 1000) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 是否可以享受表彰奖励
	 * @param p
	 * @return
	 */
	public boolean isBiaoZhangReward(Player p) {
		if (p == null || p.getCountryPosition() <= 0) {
			return false;
		}
		Country country = countryMap.get(p.getCountry());
		if (country != null) {
			long[] ids = country.getBiaozhangIds();
			boolean has = false;
			if (ids != null) {
				for (long id : ids) {
					if (id == p.getId()) {
						has = true;
					}
				}
			}
			if (has) {
				Long time = country.成为表彰官员时间Map.get(p.getId());
				if (time != null) {
					if (System.currentTimeMillis() - time >= 24 * 1l * 3600 * 1000) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
